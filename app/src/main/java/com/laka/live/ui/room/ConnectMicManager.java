package com.laka.live.ui.room;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.bean.UserInfo;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.lakalive.LiveCallback;
import com.laka.live.manager.BytesReader;
import com.laka.live.manager.OnResultListenerAdapter;
import com.laka.live.manager.RoomManager;
import com.laka.live.msg.JoinLiveMsg;
import com.laka.live.msg.OpenLiveMsg;
import com.laka.live.network.DataProviderRoom;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.fragment.BaseFragment;
import com.laka.live.ui.widget.dialog.GenericDialog;
import com.laka.live.ui.widget.dialog.IDialogOnClickListener;
import com.laka.live.util.Common;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;
import com.laka.live.zego.ZegoApiManager;
import com.laka.live.zego.ZegoLivePlayer;
import com.laka.live.zego.ZegoLivePusher;
import com.laka.live.zego.utils.ZegoRoomUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * Created by ios on 16/10/25.
 */

public class ConnectMicManager implements EventBusManager.OnEventBusListener {
    private static final String TAG = "ConnectMicManager";
    BaseFragment mActivity;
    RoomManager roomManager;
    boolean isFirstZhubo = false;
    public boolean isSecondZhubo = false;
    private ZegoLivePusher mLivePusher;//用于第二主播推流
    private ZegoLivePlayer mLivePlayer;//用于拉流第二主播
    private String channelId;//当前频道ID
    private String zhuboId;//当前主播ID
    private LiveSmallView mLiveSmallView;

    private boolean isOpenConnectMic = true;

    private boolean isInConnectMic;

    private boolean enableClearLinkList;

    private boolean isOpenVideo = true;

    public BytesReader.Audience curLinkUser;

    public boolean isApplying = false; //是否申请中

    private ConnectMicManager() {

    }


    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public ConnectMicManager(BaseFragment activity, LiveSmallView liveSmallView, final boolean isFirstZhubo, String zhuboId) {
        mActivity = activity;
        this.isFirstZhubo = isFirstZhubo;
        this.zhuboId = zhuboId;
        this.mLiveSmallView = liveSmallView;
        mLiveSmallView.setConnectMicManager(this);
        roomManager = RoomManager.getInstance();
        roomManager.addResultListener(new OnResultListenerAdapter() {
            @Override
            public void chatDidReceiveMessage(BytesReader.AudienceMessage message) {
                Log.d(TAG, String.format("收到消息 来自:%s 类型:%d 内容:%s 等级:%d id:%s time:%d  type:%d",
                        message.nickName, message.type, message.content, message.level, message.audienceID, message.time, message.type));
            }
        });

    }

    /**
     * 副播开始播放
     */
    private void startPlaySecond(String secondStreamId) {
        if (Utils.isEmpty(secondStreamId)) {
            Log.d(TAG, "没有 secondStreamId");
            return;
        }
        if (isSecondZhubo) {
            stopPushSecond();
        }
//        if (mLivePlayer != null) {
//            ZegoApiManager.getInstance().unRegister(mLivePlayer);
//        }

        ZegoLivePlayer.Builder build = new ZegoLivePlayer.Builder();
        build.setUserName(zhuboId);
        build.setUserId(zhuboId);
        build.setLiveChannel(channelId);
        build.setStreamID(secondStreamId);
//        build.setRemoteView(mLiveSmallView.getTextureView(false));
//        build.setRemoteViewIndex(ZegoAVKitCommon.ZegoRemoteViewIndex.Second);
        mLivePlayer = build.build();
        mLiveSmallView.setVisibility(View.VISIBLE);
        //切换到播放模式
        mLiveSmallView.switchMode(LiveSmallView.MODE_READY);
        mLivePlayer.setLoginChannel(true);
        mLivePlayer.setLiveCallback(liveCallBack);
        mLivePlayer.startPlay();

    }


    /**
     * 副播开始推流
     */
    private void startPushSecond(String streamId) {
        Log.d(TAG," startPushSecond");
        if (Utils.isEmpty(channelId)) {
            Log.d(TAG, "没有 channelId");
            return;
        }
        if (Utils.isEmpty(streamId)) {
            Log.d(TAG, "没有 streamId");
            return;
        }
        stopPlaySecond();
        isSecondZhubo = true;

//        if (mLivePusher != null) {
//            ZegoApiManager.getInstance().unRegister(mLivePusher);
//        }

        Log.d(TAG, " startPushSecond streamId=" + streamId + " channelId=" + channelId);
        ZegoLivePusher.Builder build = new ZegoLivePusher.Builder();
        build.setLiveChannel(channelId);
        build.setPublishStreamID(streamId);//todo 暂时使用自己的用户id
        build.setUserId(zhuboId);
//        build.setLocalView(mLiveSmallView.getTextureView(true));
        mLivePusher = build.build();
        mLivePusher.enableMic(true);
        mLivePusher.setLiveCallback(liveCallBack);
        mLivePusher.setFrontCam(true);
        mLivePusher.enableBeautifying(ZegoRoomUtil.getZegoBeauty(3));
        mLiveSmallView.setVisibility(View.VISIBLE);
        mLiveSmallView.switchMode(LiveSmallView.MODE_READY);
        mLivePusher.startPreview();
        mLivePusher.setLoginChannel(true);
        //开始推流
        mLivePusher.startPublish();

        BytesReader.Audience user = new BytesReader().getAudienceObj();
        UserInfo userInfo = AccountInfoManager.getInstance().getAccountInfo();
        user.id = userInfo.getIdStr();
        user.nickName = userInfo.getNickName();
        user.avatar = userInfo.getAvatar();
        mLiveSmallView.setUserInfo(user);
    }

    private LiveCallback liveCallBack = new LiveCallback() {
        @Override
        public void onPublishSucc() {
            Log.d(TAG, " onPublishSucc ");//完成连麦操作
            if (!isFirstZhubo) {
                showHasVideo();
                isSecondZhubo = true;//已经是第二主播
                isApplying = true;
                EventBusManager.postEvent(0, SubcriberTag.REFRESH_LINK_MIC_PANEL);
                roomManager.openLinkMic("", 0);
            }

        }

        @Override
        public void onPublishStop(int retCode) {
            Log.d(TAG, " onPublishStop retCode=" + retCode);
            if (retCode != 1 && isSecondZhubo) {
                Log.d(TAG, "推流中断连麦中止");
                ToastHelper.showToast("网络不好,连麦中断");
                roomManager.closeLinkMic("");
                mLiveSmallView.setVisibility(View.GONE);
                EventBusManager.postEvent((Integer) R.string.live_push_network_suggest_not_see, SubcriberTag.LINK_STOP_EXIT_ROOM);
            }
            if (retCode != 1 && isFirstZhubo) {
                EventBusManager.postEvent((Integer) R.string.live_push_network_connect_error, SubcriberTag.LINK_STOP_EXIT_ROOM);
            }
        }

        @Override
        public void onPlaySucc() {
            Log.d(TAG, " onPlaySucc isVideoOpen="+curLinkUser.isVideoOpen);
            if (curLinkUser.isVideoOpen == 1) {
                mLiveSmallView.switchMode(LiveSmallView.MODE_LIVE);
            } else {
                mLiveSmallView.switchMode(LiveSmallView.MODE_NO_VIDEO);
            }
        }

        @Override
        public void onPlayStopEvent(int retCode, String streamID, String liveChannel) {
            Log.d(TAG, " onPlayStopEvent retCode=" + retCode + " streamID=" + streamID + " liveChannel=" + liveChannel);

        }

        @Override
        public void onVideoSizeChanged(int width, int height) {
            Log.d(TAG, " onVideoSizeChanged width=" + width + " height=" + height);
            if (curLinkUser.isVideoOpen == 1) {
                mLiveSmallView.switchMode(LiveSmallView.MODE_LIVE);
            } else {
                mLiveSmallView.switchMode(LiveSmallView.MODE_NO_VIDEO);
            }
        }

        @Override
        public void onCaptureVideoSize(int width, int height) {
            Log.d(TAG, " onCaptureVideoSize width=" + width + " height=" + height);
        }

        @Override
        public void onPlayQualityUpdate(int quality) {
            Log.d(TAG, " onPlayQualityUpdate quality=" + quality
            +" mLinkLowQualityCount="+mLinkLowQualityCount);
            if (!isFirstZhubo) {
                return;
            }
            if (quality >= 3) {
                mLinkLowQualityCount++;
//                if (mLinkLowQualityCount >= 3) {
//                    stopPlaySecond();
//                    closeLinkMic(curLinkUser.id);
//                    mLinkLowQualityCount = 0;
//                }
            } else {
                mLinkLowQualityCount = 0;
            }
        }

        @Override
        public void onPublishQulityUpdate(int quality) {
            Log.d(TAG, " onPublishQulityUpdate quality=" + quality);
        }
    };

    private int mLinkLowQualityCount = 0;

    public void start() {
        Log.d(TAG, " start 注册");
        EventBusManager.register(this);
    }

    public void stop() {
        Log.d(TAG, " stop 反注册");
        EventBusManager.unregister(this);
        if (mLivePusher != null) {
            mLivePusher.stopPreview();
            mLivePusher.stopPublish();
            mLivePusher.destroy();
        }
        if (mLivePlayer != null) {
            mLivePlayer.stopPlay();
            mLivePlayer.destroy();
        }
//        ZegoApiManager.getInstance().unRegisterAll();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PostEvent event) {
        String tag = event.tag;

        if (SubcriberTag.ADD_LINK_MIC_RSP.equals(tag)) {// 添加连麦返回
            Bundle bd = (Bundle) event.event;
            int errorCode = bd.getInt(Common.ERROE_CODE);
            String userId = bd.getString(Common.USER_ID);
            short status = bd.getShort(Common.STATUS);
            if (errorCode == RoomManager.TLV_E_OK) {
                Log.d(TAG, "添加连麦成功 userId=" + userId + " status=" + status);

                if (isFirstZhubo) {
                    Log.d(TAG, "主播添加连麦成功刷新连麦列表");
                    EventBusManager.postEvent(0, SubcriberTag.REFRESH_LINK_LIST);
                    EventBusManager.postEvent(0, SubcriberTag.REFRESH_ONLINE_LIST);
                } else {
                    isApplying = true;
                    EventBusManager.postEvent(0, SubcriberTag.REFRESH_LINK_MIC_PANEL);
                }

            } else {
                Log.d(TAG, " 添加连麦失败 errorCode=" + errorCode);
                if (errorCode == RoomManager.TLV_E_CONDITION_LACK) {
                    String errMsg = bd.getString(Common.ERROR_MSG, ResourceHelper.getString(R.string.link_condition_lack));
                    ToastHelper.showToast(errMsg);
                }

            }
        } else if (SubcriberTag.CONFIRM_LINK_MIC_RSP.equals(tag)) {// 确认连麦返回
            Bundle bd = (Bundle) event.event;
            int errorCode = bd.getInt(Common.ERROE_CODE);
            String userId = bd.getString(Common.USER_ID);
            short status = bd.getShort(Common.STATUS);
            if (errorCode == RoomManager.TLV_E_OK) {
                Log.d(TAG, "确认连麦成功 userId=" + userId + " status=" + status + " mineId=" + AccountInfoManager.getInstance().getCurrentAccountUserIdStr());
                if (status == 3 && !isFirstZhubo) {
                    Log.d(TAG, " 粉丝自己发起的连麦开始请求推流");
                    getPushUrl();
                } else {
                    EventBusManager.postEvent(0, SubcriberTag.REFRESH_LINK_LIST);
                }

            } else {
                Log.d(TAG, " 确认连麦失败 errorCode=" + errorCode);
                String errMsg = bd.getString(Common.ERROR_MSG, ResourceHelper.getString(R.string.link_condition_lack));
                ToastHelper.showToast(errMsg);
            }
        } else if (SubcriberTag.REFUSE_LINK_MIC_RSP.equals(tag)) {// 拒绝连麦返回
            Bundle bd = (Bundle) event.event;
            int errorCode = bd.getInt(Common.ERROE_CODE);
            if (errorCode == RoomManager.TLV_E_OK) {
                Log.d(TAG, "拒绝连麦成功  ");
            } else {
                Log.d(TAG, "拒绝连麦失败 errorCode=" + errorCode);
            }
        } else if (SubcriberTag.LINK_MIC_UNICAST.equals(tag)) {
            Bundle bd = (Bundle) event.event;
            final String userId = bd.getString(Common.USER_ID);
            short status = bd.getShort(Common.STATUS);
            int result = bd.getInt(Common.RESULT);
            int sign = bd.getInt(Common.SIGN);
            Log.d(TAG, "单播连麦消息  userId=" + userId + " status=" + status + " sign=" + sign + " result=" + result + " isFirstZhubo=" + isFirstZhubo);
            if (result == RoomManager.TLV_E_OK) {
                if (sign == RoomManager.TLV_LINK_MIC_CONFIRM && status == 2) {//观众确认连麦等待主播确认
                    if (isFirstZhubo) {//第一主播响应
//                        mActivity.showButtonDialog(ResourceHelper.getString(R.string.link_mic_tips), String.format(ResourceHelper.getString(R.string.receive_fans_apply_connect), userId), R.string.yes, R.string.refuse,
//                                true, true, true, true, new IDialogOnClickListener() {
//                                    @Override
//                                    public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
//                                        if (viewId == GenericDialog.ID_BUTTON_YES) {
//                                            roomManager.confirmLinkMic(userId);
//                                        } else {
//                                            roomManager.refuseLinkMic(userId);
//                                        }
//                                        return false;
//                                    }
//                                });
                        //改为小红点,不弹窗
                        EventBusManager.postEvent(0, SubcriberTag.RECEIVE_LINK_APPLY);
                        EventBusManager.postEvent(0, SubcriberTag.REFRESH_LINK_LIST);
                    }
                } else if (sign == RoomManager.TLV_LINK_MIC_CONFIRM && status == 1) {//主播确认连麦等待观众确认
                    mActivity.showButtonDialog(ResourceHelper.getString(R.string.link_mic_tips), ResourceHelper.getString(R.string.receive_zhubo_apply_connect), R.string.yes, R.string.refuse,
                            false, false, true, true, new IDialogOnClickListener() {
                                @Override
                                public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                                    if (viewId == GenericDialog.ID_BUTTON_YES) {
                                        if (Utils.checkPermission(mActivity.getContext(), Manifest.permission.RECORD_AUDIO)
                                                && Utils.checkPermission(mActivity.getContext(), Manifest.permission.CAMERA)) {
                                            AnalyticsReport.onEvent(mActivity.getContext() , LiveReport.MY_LIVE_EVENT_11273);
                                            roomManager.confirmLinkMic("");
                                        } else {
                                            ToastHelper.showToast("请先授权摄像头和麦克风");
                                            EventBusManager.postEvent(0, SubcriberTag.REQUEST_LIVE_PERMISSION);
                                        }
                                    } else {
                                        AnalyticsReport.onEvent(mActivity.getContext() , LiveReport.MY_LIVE_EVENT_11272);
                                        roomManager.refuseLinkMic("");
                                    }
                                    return false;
                                }
                            });
                } else if (sign == RoomManager.TLV_LINK_MIC_CONFIRM && status == 3) {
                    if (!isFirstZhubo) {
                        Log.d(TAG, "TLV_LINK_MIC_CONFIRM 观众开始获取 streamId并推流");
                        getPushUrl();
                    } else {
                        Log.d(TAG, "TLV_LINK_MIC_CONFIRM 主播收到观众确认");
                        //刷新列表
                        EventBusManager.postEvent(0, SubcriberTag.REFRESH_LINK_LIST);
                    }

                } else if (sign == RoomManager.TLV_LINK_MIC_REFUSE) {
                    String content = "";
                    if (!isFirstZhubo) {
                        Log.d(TAG, "TLV_LINK_MIC_REFUSE 主播拒绝连麦");
                        content = ResourceHelper.getString(R.string.zhubo_refuse_link_mic);
                        isApplying = false;
                        EventBusManager.postEvent(0, SubcriberTag.REFRESH_LINK_MIC_PANEL);
                    } else {
                        Log.d(TAG, "TLV_LINK_MIC_REFUSE 粉丝拒绝连麦");
                        content = ResourceHelper.getString(R.string.fans_refuse_link_mic);
                    }
                    mActivity.showButtonDialog(ResourceHelper.getString(R.string.link_mic_tips), content, R.string.yes, 0,
                            false, false, true, true, new IDialogOnClickListener() {
                                @Override
                                public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                                    return false;
                                }
                            });
                } else if (sign == RoomManager.TLV_LINK_MIC_ADD && status == 3) {
                    Log.d(TAG, "TLV_LINK_MIC_ADD 观众需要确认");

                }

            } else if (result == 1) {
                if (sign == RoomManager.TLV_LINK_MIC_CONFIRM && status == -1) {
                    Log.d(TAG, " TLV_LINK_MIC_CONFIRM");
                    ToastHelper.showToast("连麦确认超时取消");
                    if (!isFirstZhubo) {
                        mActivity.dismissButtonDialog();
                    }
                    isApplying = false;
                    EventBusManager.postEvent(0, SubcriberTag.REFRESH_LINK_MIC_PANEL);
                }
            } else if (result == 2 || result == 3) {
                if (isFirstZhubo) {
                    //刷新列表
                    EventBusManager.postEvent(0, SubcriberTag.REFRESH_LINK_LIST);
                }
            }
        } else if (SubcriberTag.OPEN_LINK_MIC_RSP.equals(tag)) {
            Bundle bd = (Bundle) event.event;
            final String userId = bd.getString(Common.USER_ID);
            int errorCode = bd.getInt(Common.ERROE_CODE);
            if (errorCode == RoomManager.TLV_E_OK) {
                Log.d(TAG, " OPEN_LINK_MIC_RSP  userId=" + userId);//todo ui处理
            } else {
                stopPushSecond();
                if (errorCode == 28) {
                    getPushUrl();
                } else {
                    ToastHelper.showToast("连麦失败,请稍后再试");
                }
                Log.d(TAG, " OPEN_LINK_MIC_RSP 失败 errorCode=" + errorCode);//todo ui处理
            }


        } else if (SubcriberTag.CLOSE_LINK_MIC_RSP.equals(tag)) {
            Bundle bd = (Bundle) event.event;
            final String userId = bd.getString(Common.USER_ID);
            int errorCode = bd.getInt(Common.ERROE_CODE);
            if (errorCode == RoomManager.TLV_E_OK) {
                Log.d(TAG, " CLOSE_LINK_MIC_RSP  userId=" + userId);//todo ui处理
                stopPushSecond();
                EventBusManager.postEvent(0, SubcriberTag.REFRESH_LINK_LIST);
                isApplying = false;
                EventBusManager.postEvent(0, SubcriberTag.REFRESH_LINK_MIC_PANEL);
            } else {
                Log.d(TAG, "CLOSE_LINK_MIC_RSP 失败 errorCode=" + errorCode);
            }
        } else if (SubcriberTag.LINK_MIC_MULTICAST.equals(tag)) {
            Bundle bd = (Bundle) event.event;
            int sign = bd.getInt(Common.SIGN);
            String userId = bd.getString(Common.USER_ID);
            Log.d(TAG, " LINK_MIC_MULTICAST 连麦广播消息 sign=" + sign + " userId=" + userId);
            if (sign == RoomManager.TLV_LINK_MIC_OPEN) {
                refreshLinkUsers();
            } else if (sign == RoomManager.TLV_LINK_MIC_CLOSE) {
                if (isSecondZhubo && userId.equals(AccountInfoManager.getInstance().getCurrentAccountUserIdStr())) {
                    isSecondZhubo = false;
                    isApplying = false;
                    EventBusManager.postEvent(0, SubcriberTag.REFRESH_LINK_MIC_PANEL);
                    ToastHelper.showToast("连麦断开");
                    Log.d(TAG, " 自己的连麦被主播断掉");
                    stopPushSecond();
                }

                refreshLinkUsers();
            } else if (sign == RoomManager.TLV_LINK_VDO_OPEN) {
                showHasVideo();
            } else if (sign == RoomManager.TLV_LINK_VDO_CLOSE) {
                showNoVideo();
            }
        } else if (SubcriberTag.QUERY_LINK_USER.equals(tag)) {
            Log.d(TAG, " QUERY_LINK_USER 返回");
            Bundle bd = (Bundle) event.event;
            ArrayList<BytesReader.Audience> linkUsers = (ArrayList<BytesReader.Audience>) bd.getSerializable("list");
            if (!Utils.listIsNullOrEmpty(linkUsers)) {
                BytesReader.Audience user = linkUsers.get(0);
                curLinkUser = user;
                isOpenVideo = user.isVideoOpen == 1 ? true : false;
                Log.d(TAG, " linkUsers=" + linkUsers.size() + " id=" + user.id + " streamId=" + user.streamId + " isVideoOpen=" + user.isVideoOpen);
                if (user.id.equals(AccountInfoManager.getInstance().getCurrentAccountUserIdStr())) {
                    Log.d(TAG, "自己的画面不播放");
                    mLiveSmallView.setUserInfo(user);
                } else {
                    mLiveSmallView.setUserInfo(user);
                    startPlaySecond(user.streamId);
                }
            } else {
                Log.d(TAG, "连麦列表为空");//// TODO: 16/10/31 停止所有播放
                if (isSecondZhubo) {
                    stopPushSecond();
                } else {
                    stopPlaySecond();
                }
            }
        } else if (SubcriberTag.CLEAR_LINK_LIST_RSP.equals(tag)) {
            Bundle bd = (Bundle) event.event;
            int errorCode = bd.getInt(Common.ERROE_CODE);
            if (errorCode == RoomManager.TLV_E_OK) {
                Log.d(TAG, " CLEAR_LINK_LIST_RSP 成功刷新列表");
            } else {
                Log.d(TAG, " CLEAR_LINK_LIST_RSP 失败");
                ToastHelper.showToast("清空失败,请稍后再试");
            }
        } else if (SubcriberTag.ROOM_LINK_DATA.equals(tag)) {
            Bundle bd = (Bundle) event.event;

            int connectCnt = bd.getInt(Common.CONNECT_CNT, 0);
            int canConnect = bd.getInt(Common.IS_CAN_CONNECT, 0);
            int openConnect = bd.getInt(Common.IS_OPEN_CONNECT, 0);

            if (openConnect == 1) {
                isOpenConnectMic = true;
            } else {
                isOpenConnectMic = false;
            }

            boolean isShowConnectMic;
            if (connectCnt > 0) {
                isShowConnectMic = true;
            } else {
                isShowConnectMic = false;
            }
            EventBusManager.postEvent(isShowConnectMic, SubcriberTag.IS_SHOW_LINK_ICON);

            Log.d(TAG, " ROOM_LINK_DATA connectCnt=" + connectCnt + " isCanConnect=" + canConnect + " isOpenConnect=" + openConnect);
        } else if (SubcriberTag.CLOSE_LINK_MIC.equals(tag)) {
            //断开连麦
            if (isFirstZhubo) {
                if (curLinkUser != null) {
                    closeConnectMic(curLinkUser.id, curLinkUser.nickName);
                } else {
                    Log.d(TAG, " 当前没有连麦用户");
                }
            } else {
                closeConnectMic("", "");
            }

        }
    }

    private void showNoVideo() {
        if(curLinkUser!=null){
            curLinkUser.isVideoOpen = 0;
        }
        mLiveSmallView.refreshOpenVideo(false);
        mLiveSmallView.switchMode(LiveSmallView.MODE_NO_VIDEO);
    }

    private void showHasVideo() {
        if(curLinkUser!=null){
            curLinkUser.isVideoOpen = 1;
        }
        mLiveSmallView.refreshOpenVideo(true);
        mLiveSmallView.switchMode(LiveSmallView.MODE_LIVE);
    }

    public void refreshLinkUsers() {
        Log.d(TAG, " 刷新正在连麦用户列表");
//        roomManager.queryLinkUsers(0);
    }

    /**
     * 副播停止推流
     */
    public void stopPushSecond() {
        isSecondZhubo = false;
        isApplying = false;
        EventBusManager.postEvent(0, SubcriberTag.REFRESH_LINK_MIC_PANEL);
        if (mLivePusher != null) {
            mLivePusher.setLiveCallback(null);
            mLivePusher.stopPublish();
        }
        mLiveSmallView.setVisibility(View.GONE);
    }

    /**
     * 播放副播
     */
    public void stopPlaySecond() {
        if (mLivePlayer != null) {
            mLivePlayer.setLiveCallback(null);
            mLivePlayer.stopPlay();
        }
        mLiveSmallView.setVisibility(View.GONE);
    }

    private void getPushUrl() {
        Log.d(TAG,"getPushUrl joinLive");
        DataProviderRoom.joinLive(this, AccountInfoManager.getInstance().getCurrentAccountUserIdStr(), channelId, new GsonHttpConnection.OnResultListener<JoinLiveMsg>() {
            @Override
            public void onSuccess(JoinLiveMsg openLiveMsg) {
                Log.d(TAG," getPushUrl onSuccess streamId ="+openLiveMsg.getStreamId());
                if (openLiveMsg.isSuccessFul()) {
                    startPushSecond(openLiveMsg.getStreamId());
                }
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                Log.d(TAG, " joinLive onFail errorCode=" + errorCode);
            }
        });

    }

    /**
     * 主播确认连麦
     */
    public void zhuboConfirm(String userId) {
        Log.d(TAG, " zhuboConfirm userId=" + userId);
        roomManager.confirmLinkMic(userId);
    }


    /**
     * 主播邀请连麦
     */
    public void addConnectMic(String userId) {
        Log.d(TAG, " addConnectMic=" + userId);
        roomManager.addLinkMic(userId);
    }

    public boolean isInConnectMic() {
        return isInConnectMic;
    }

    public boolean isOpenConnectMic() {
        return isOpenConnectMic;
    }

    public void setInConnectMic(boolean isIn) {
        isInConnectMic = isIn;
    }

    public boolean enableClearLinkList() {
        return enableClearLinkList;
    }

    public void setClearLinkList(boolean enable) {
        enableClearLinkList = enable;
    }

    /**
     * 拒绝连麦
     */
    public void refuse(String userId) {
        Log.d(TAG, "refuse userId=" + userId);
        roomManager.refuseLinkMic(userId);
    }

    /**
     * 取消连麦
     */
    public void closeConnectMic(final String userId, String name) {
        String content = "";
        if (Utils.isEmpty(userId)) {
            content = ResourceHelper.getString(R.string.confirm_cancel_connect_zhubo);
        } else {
            content = String.format(ResourceHelper.getString(R.string.confirm_cancel_connect_fans), name);
        }
        mActivity.showButtonDialog(ResourceHelper.getString(R.string.link_mic_tips), content, R.string.yes, R.string.cancel,
                true, true, true, true, new IDialogOnClickListener() {
                    @Override
                    public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                        if (viewId == GenericDialog.ID_BUTTON_YES) {
                            closeLinkMic(userId);
                        }
                        return false;
                    }
                });
    }

    public void closeLinkMic(String userId) {
        Log.d(TAG, "closeLinkMic userId=" + userId);
        roomManager.closeLinkMic(userId);
    }

    /**
     * 打开连麦
     */
    public void openConnectMic(final String userId) {
        //弹出pannel
        mActivity.showButtonDialog(R.string.link_mic_tips, R.string.confirm_apply_connect_zhubo, R.string.yes, R.string.cancel,
                true, true, new IDialogOnClickListener() {
                    @Override
                    public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                        if (viewId == GenericDialog.ID_BUTTON_YES) {
                            addConnectMic(userId);
                        }
                        return false;
                    }
                });
    }

    public void toggleConnectMic() {
        if (isOpenConnectMic()) {
            Log.d(TAG, " 关闭连麦模式");
            //关闭连麦模式
            roomManager.postLiveData((short) 5, (short) 0);
            isOpenConnectMic = false;
        } else {
            Log.d(TAG, " 打开连麦模式");
            //打开连麦模式
            roomManager.postLiveData((short) 5, (short) 1);
            isOpenConnectMic = true;
        }
    }

    /**
     * 观众打开摄像头
     */
    public void openVideo() {
        Log.d(TAG, " openVideo isOpenVideo=" + !isOpenVideo);
        if (!isOpenVideo) {
            AnalyticsReport.onEvent( mActivity.getContext() , LiveReport.MY_LIVE_EVENT_11276);
            roomManager.postLiveData((short) 2, (short) 1);
        } else {
            AnalyticsReport.onEvent( mActivity.getContext() , LiveReport.MY_LIVE_EVENT_11275);
            roomManager.postLiveData((short) 2, (short) 0);
        }

        this.isOpenVideo = !isOpenVideo;
        mLiveSmallView.refreshOpenVideo(isOpenVideo);

    }

    public boolean isZhubo() {
        return isFirstZhubo || isSecondZhubo;
    }

    public void clearConnectList() {
        Log.d(TAG, " clearLinkList 清空连麦列表");
        roomManager.clearLinkList();
    }
}
