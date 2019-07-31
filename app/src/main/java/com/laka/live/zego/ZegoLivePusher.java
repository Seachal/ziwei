package com.laka.live.zego;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.ksy.recordlib.service.util.audio.KSYBgmPlayer;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.application.LiveApplication;
import com.laka.live.audio.BgmManager;
import com.laka.live.lakalive.LivePusher;
import com.laka.live.txrtmp.OnBgmMixerListener;
import com.laka.live.util.Common;
import com.laka.live.util.Log;
import com.laka.live.util.Utils;
import com.laka.live.zego.utils.ZegoRoomUtil;
import com.laka.live.zego.widgets.ViewLive;
import com.zego.zegoliveroom.ZegoLiveRoom;
import com.zego.zegoliveroom.callback.IZegoLivePublisherCallback;
import com.zego.zegoliveroom.callback.IZegoLoginCompletionCallback;
import com.zego.zegoliveroom.callback.IZegoRoomCallback;
import com.zego.zegoliveroom.constants.ZegoConstants;
import com.zego.zegoliveroom.constants.ZegoVideoViewMode;
import com.zego.zegoliveroom.entity.AuxData;
import com.zego.zegoliveroom.entity.ZegoStreamInfo;
import com.zego.zegoliveroom.entity.ZegoStreamQuality;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by luwies on 16/10/8.
 */
public class ZegoLivePusher extends LivePusher implements IZegoLivePublisherCallback {

    private static final String TAG = "ZegoLivePusher";

    private ViewLive mLocalView;

    private String mUserId;

    private String mUserName;

    private String mLiveChannel;

    private String mPublishStreamID;

    private String mPublishStreamTitle;

    protected String mRoomID = null;

    protected Activity mActivity;
    private boolean mFrontCamera = true;

    public void setFrontCamera(boolean mFrontCamera) {
        this.mFrontCamera = mFrontCamera;
    }

    public void setmActivity(Activity mActivity) {
        this.mActivity = mActivity;

        if (mLocalView != null) {
            mLocalView.setActivityHost(mActivity);
            if (mZegoLiveRoom != null) {
                Log.d(TAG, " mZegoLiveRoom!=null");
            }
            mLocalView.setZegoLiveRoom(mZegoLiveRoom);
//            mLocalView.setShareToQQCallback(new ViewLive.IShareToQQCallback() {
//                @Override
//                public String getRoomID() {
//                    return mRoomID;
//                }
//            });
            mListViewLive.add(mLocalView);
        } else {
            Log.d(TAG, " mLocalView==null");
        }
//        initViewList(mLocalView);//切换主屏
    }

//    private void initViewList(final ViewLive vlBigView) {
//
//        List<ViewLive> list = new ArrayList<>();
//
//        LinearLayout llViewList = (LinearLayout) findViewById(R.id.ll_viewlist);
//        for (int i = 0, llChildListSize = llViewList.getChildCount(); i < llChildListSize; i++) {
//            if (llViewList.getChildAt(i) instanceof LinearLayout) {
//                LinearLayout llChildList = (LinearLayout) llViewList.getChildAt(i);
//
//                for (int j = 0, viewLiveSize = llChildList.getChildCount(); j < viewLiveSize; j++) {
//                    if (llChildList.getChildAt(j) instanceof ViewLive) {
//                        final ViewLive viewLive = (ViewLive) llChildList.getChildAt(j);
//
//                        viewLive.setActivityHost(mActivity);
//                        viewLive.setZegoLiveRoom(mZegoLiveRoom);
//                        viewLive.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                viewLive.toExchangeView(vlBigView);
//                            }
//                        });
//
//                        list.add((ViewLive) llChildList.getChildAt(j));
//                    }
//                }
//            }
//        }
//
//        for (int size = list.size(), i = size - 1; i >= 0; i--) {
//            mListViewLive.add(list.get(i));
//        }
//    }

    public boolean isLoginChannel() {
        return isLoginChannel;
    }

    public void setLoginChannel(boolean loginChannel) {
        isLoginChannel = loginChannel;
    }

    private boolean isLoginChannel = false;

    // 初始化来电状态, false表示没有来电
    private boolean mHaveBeenCalled = false;

    private boolean isPushing = false;

    private PhoneStateListener mPhoneStateListener;

    public ZegoLivePusher() {
//        ZegoApiManager.getInstance().register(this);
        mZegoLiveRoom = ZegoApiManager.getInstance().getZegoLiveRoom();
    }

    protected void initPhoneCallingListener() {
        TelephonyManager tm = (TelephonyManager) LiveApplication.getInstance()
                .getSystemService(Service.TELEPHONY_SERVICE);
        if (mPhoneStateListener == null) {
            mPhoneStateListener = new PhoneStateListener() {
                @Override
                public void onCallStateChanged(int state, String incomingNumber) {
                    super.onCallStateChanged(state, incomingNumber);
                    switch (state) {
                        case TelephonyManager.CALL_STATE_RINGING:
                            Log.info("TestPhoneState", "响铃:来电号码" + incomingNumber);
                            if (!mHaveBeenCalled) {
                                // 标记已来电
                                mHaveBeenCalled = true;
                                destroy();
                            }
                            break;
                        case TelephonyManager.CALL_STATE_IDLE:
                            Log.info("TestPhoneState", "挂断电话");
                            if (mHaveBeenCalled) {
                                // 恢复来电状态为false
                                mHaveBeenCalled = false;

                                // 重新登陆频道

                                startPreview();
                                loginChannel();
                            }

                            break;
                        case TelephonyManager.CALL_STATE_OFFHOOK:
                            Log.info("TestPhoneState", "接听");
                            break;
                    }
                }
            };
        }
        tm.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private void unInitPhoneCallingListener() {
        TelephonyManager tm = (TelephonyManager) LiveApplication.getInstance()
                .getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
    }

    private void setLocalView(ViewLive localView) {
        this.mLocalView = localView;
//        mListViewLive.add(localView);
//        ZegoApiManager.getInstance().setLocalView(localView);
//        ZegoApiManager.getInstance().setLocalViewMode(ZegoAVKitCommon.ZegoVideoViewMode.ScaleAspectFill);
    }

    private void setUserId(String userId) {
        this.mUserId = userId;
    }

    private void setUserName(String userName) {
        this.mUserName = userName;
    }

    private void setLiveChannel(String liveChannel) {
        this.mLiveChannel = liveChannel;
    }

    private void setPublishStreamID(String publishStreamID) {
        this.mPublishStreamID = publishStreamID;
    }

    private void setPublishStreamTitle(String publishStreamTitle) {
        this.mPublishStreamTitle = publishStreamTitle;
    }

    private void loginChannel() {
//        logoutChannel();
//        mRoomID = ZegoRoomUtil.getRoomID(ZegoRoomUtil.ROOM_TYPE_MORE);
        mRoomID = AccountInfoManager.getInstance().getCurrentAccountUserIdStr();
        String userID = AccountInfoManager.getInstance().getCurrentAccountUserIdStr();
//        String userName = AccountInfoManager.getInstance().getCurrentAccountNickName();
//        String userName = mLiveChannel;
        String userName = mUserName;
        Log.d(TAG, " 设置用户信息 userID=" + userID + " userName=" + userName);
        ZegoLiveRoom.setUser(userID, userName);
        mZegoLiveRoom = ZegoApiManager.getInstance().getZegoLiveRoom();
        mZegoLiveRoom.setZegoLivePublisherCallback(this);
        mZegoLiveRoom.loginRoom(mLiveChannel, mPublishStreamTitle, ZegoConstants.RoomRole.Anchor, new IZegoLoginCompletionCallback() {
            @Override
            public void onLoginCompletion(int errorCode, ZegoStreamInfo[] zegoStreamInfos) {
                Log.d(TAG, " onLoginCompletion errorCode=" + errorCode);
                if (errorCode == 0) {
                    handleAnchorLoginRoomSuccess(zegoStreamInfos);
                } else {
                    handleAnchorLoginRoomFail(errorCode);
                }
            }
        });
//        ZegoUser zegoUser = new ZegoUser(mUserId, mUserName);
//        ZegoApiManager.getInstance().loginChannel(zegoUser, mLiveChannel);
//        initPhoneCallingListener();
        Log.d(TAG, " loginChannel mUserId=" + mUserId + " mUserName=" + mUserName + " mLiveChannel=" + mLiveChannel
                + " mPublishStreamTitle=" + mPublishStreamTitle
                + " mRoomID=" + mRoomID);
    }

    private void logoutChannel() {
        // 退出频道, 连麦情况下，要stop掉所有的stream后，才执行logoutchannel。
//        ZegoApiManager.getInstance().logoutChannel();
        mZegoLiveRoom.logoutRoom();
        unInitPhoneCallingListener();
        isLoginChannel = false;
    }

    public void setViewMode(boolean isFull) {
        if (isFull) {
            mZegoLiveRoom.setPreviewViewMode(ZegoVideoViewMode.ScaleAspectFill);
        } else {
            mZegoLiveRoom.setPreviewViewMode(ZegoVideoViewMode.ScaleAspectFit);
        }
    }

    @Override
    public void startPreview() {
        // 设置水印
        mZegoLiveRoom.setWaterMarkImagePath("asset:watermark.png");
        Rect rect = new Rect();
//        rect.left = LiveApplication.screenWidth - Utils.dip2px(mActivity, 58);
//        rect.top = Utils.dip2px(mActivity, 8);
//        rect.right = rect.left + 150;
//        rect.bottom = rect.top + 42;
        rect.left = 450;
        rect.top = 60;
        rect.right = 600;
        rect.bottom = 102;

        Log.d(TAG, " watermark left=" + rect.left + " top=" + rect.top
                + " right=" + rect.right + " bottom=" + rect.bottom);
        mZegoLiveRoom.setPreviewWaterMarkRect(rect);
        mZegoLiveRoom.setPublishWaterMarkRect(rect);

        mZegoLiveRoom.setPreviewView(mLocalView.getTextureView());
        boolean ret = mZegoLiveRoom.startPreview();
        mZegoLiveRoom.setPreviewViewMode(ZegoVideoViewMode.ScaleAspectFill);
        mZegoLiveRoom.enableMic(true);
        Log.error(TAG, "startPreview ret = " + ret);
    }

    @Override
    public void startPublish() {
        Log.d(TAG, " startPublish isLoginChannel=" + isLoginChannel);
        if (!isLoginChannel) {
            if (Utils.isEmpty(mUserName)) {
                Log.d(TAG, "mUserName 空不登陆");
                return;
            }
            logoutChannel();//先退出再登录
            startPreview();//先退出再登录
            loginChannel();
        } else {
//            ZegoApiManager.getInstance().startPublish(mPublishStreamTitle, mPublishStreamID);
//            Log.d(TAG, " startPublish mPublishStreamTitle=" + mPublishStreamTitle + " mPublishStreamID=" + mPublishStreamID);
            // 6.0及以上的系统需要在运行时申请CAMERA RECORD_AUDIO权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(mActivity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(mActivity, new String[]{
                            Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 101);
                } else {
                    publishStream();
                }
            } else {
                publishStream();
            }

        }
    }

    protected void publishStream() {
        Log.d(TAG, " publishStream mPublishStreamID=" + mPublishStreamID);
        if (TextUtils.isEmpty(mPublishStreamID)) {
            Log.d(TAG, " publishStream mPublishStreamID == null");
            handleAnchorLoginRoomFail(-1);
            return;
        }
        ViewLive freeViewLive = getFreeViewLive();
        if (freeViewLive == null) {
            Log.d(TAG, " publishStream freeViewLive == null");
            handleAnchorLoginRoomFail(-1);
            return;
        }

        // 设置流信息
        freeViewLive.setStreamID(mPublishStreamID);
        freeViewLive.setPublishView(true);

        // 初始化配置信息, 混流模式使用
//        initPublishConfigs();


        Log.d(TAG, " 更新摄像头方向 mFrontCamera=" + mFrontCamera);
        Map<String, String> mapUrls = new HashMap<>();
        mapUrls.put(Common.CAMERA_DEVICE_POSITION, String.valueOf(mFrontCamera ? 2 : 1));
        Gson gson = new Gson();
        String json = gson.toJson(mapUrls);
        // 开始播放
//        mZegoLiveRoom.setPreviewView(freeViewLive.getTextureView());
//        mZegoLiveRoom.startPreview();
        mZegoLiveRoom.startPublishing(mPublishStreamID, mPublishStreamTitle, mPublishFlag, json);
//        mZegoLiveRoom.setPreviewViewMode(ZegoVideoViewMode.ScaleAspectFill);


//        onPublishSucc();
    }

    @Override
    public void stopPreview() {
        mZegoLiveRoom.stopPreview();
    }

    @Override
    public void stopPublish() {
        mZegoLiveRoom.stopPublishing();
    }

    @Override
    public void destroy() {
        stopPreview();
        stopPublish();
//        ZegoApiManager.getInstance().setLocalView(null);
//        ZegoApiManager.getInstance().unRegister(this);
        logoutChannel();
    }

    @Override
    public void setLiveId(String userName, String streamId, String channelId) {
        this.mUserName = userName;
        this.mPublishStreamID = streamId;
        this.mLiveChannel = channelId;
    }

    @Override
    public boolean enableBeautifying(int beauty) {
        if (mZegoLiveRoom != null)
            return mZegoLiveRoom.enableBeautifying(beauty);
        return true;
    }

    @Override
    public boolean enableMic(boolean enable) {
        if (mZegoLiveRoom != null)
            return mZegoLiveRoom.enableMic(enable);
        return true;
    }

    @Override
    public boolean enableTorch(boolean enable) {
        return mZegoLiveRoom.enableTorch(enable);
    }

    @Override
    public boolean setFilter(int index) {
        return mZegoLiveRoom.setFilter(ZegoRoomUtil.getZegoBeauty(index));
    }

    @Override
    public boolean setFrontCam(boolean bFront) {
        return mZegoLiveRoom.setFrontCam(bFront);
    }

//    @Override
//    public void onLoginChannel(String liveChannel, int retCode) {
//        Log.d(TAG, " onLoginChannel liveChannel=" + liveChannel + " retCode=" + retCode);
//        if (retCode == 0) {
//            isLoginChannel = true;
//            // 登录成功
//            // 调用startPublish开始直播
//            ZegoApiManager.getInstance().startPublish(mPublishStreamTitle, mPublishStreamID);
//        } else {
//            // 登录失败
//            onPublishStop(retCode);
//        }
//    }

    /**
     * 主播登录房间成功.
     */
    protected void handleAnchorLoginRoomSuccess(ZegoStreamInfo[] zegoStreamInfos) {
        Log.d(TAG, " handleAnchorLoginRoomSuccess");
//        mPublishTitle = PreferenceUtil.getInstance().getUserName() + " is coming";
//        mPublishStreamID = ZegoRoomUtil.getPublishStreamID();
        // 开始推流
        startPublish();
        isLoginChannel = true;

    }

    /**
     * 主播登录房间失败.
     */
    protected void handleAnchorLoginRoomFail(int errorCode) {
        Log.d(TAG, " handleAnchorLoginRoomFail errorCode=" + errorCode);
        onPublishStop(errorCode);
    }

//    @Override
//    public void onPublishSucc(String streamID, String liveChannel, HashMap<String, Object> info) {
//        onPublishSucc();
//        isPushing = true;
//    }
//
//    @Override
//    public void onPublishStop(int retCode, String streamID, String liveChannel) {
//        Log.error(TAG, new StringBuilder("onPublishStop : retCode = ").append(retCode)
//                .append("streamID : ").append(streamID)
//                .append("liveChannel : ").append(liveChannel)
//                .toString());
//        onPublishStop(retCode);
//        isPushing = false;
//    }

    @Override
    public void onMixStreamConfigUpdate(int retCode, String mixStreamID, HashMap<String, Object> info) {
        Log.error(TAG, new StringBuilder("onMixStreamConfigUpdate : retCode = ").append(retCode)
                .append("mixStreamID : ").append(mixStreamID)
                .append("info : ").append(info)
                .toString());
    }


    @Override
    public void onCaptureVideoSize(int width, int height) {
        Log.d(TAG, " onCaptureVideoSize");
        super.onCaptureVideoSize(width, height);
    }


    private OnBgmMixerListener mOnBgmMixerListener;
    private BgmManager mBgmManager = BgmManager.getInstance();

    @Override
    public void onPublishStateUpdate(int stateCode, String streamID, HashMap<String, Object> streamInfo) {
        //推流状态更新
        if (stateCode == 0) {
            handlePublishSucc(streamID, streamInfo);
        } else {
            handlePublishStop(stateCode, streamID);
        }
    }

    /**
     * 推流成功.
     */
    protected void handlePublishSucc(String streamID, HashMap<String, Object> info) {
        onPublishSucc();
//        ViewLive viewLivePublish = getViewLiveByStreamID(streamID);
//        List<String> listUrls = getShareUrlList(info);
//        if (viewLivePublish != null && listUrls.size() > 0) {
//            // 设置分享连接
//            viewLivePublish.setListShareUrls(listUrls);
//
//            // 将流地址发送给房间观众
//            Map<String, String> mapUrls = new HashMap<>();
//            mapUrls.put(Constants.FIRST_ANCHOR, String.valueOf(true));
//            mapUrls.put(Constants.KEY_HLS, listUrls.get(0));
//            mapUrls.put(Constants.KEY_RTMP, listUrls.get(1));
//            Gson gson = new Gson();
//            String json = gson.toJson(mapUrls);
//            mZegoLiveRoom.updateStreamExtraInfo(json);
//        }
        Log.d(TAG, " handlePublishSucc streamID=" + streamID);
        super.handlePublishSucc(streamID);
    }

    @Override
    public void onJoinLiveRequest(int i, String s, String s1, String s2) {

    }

    @Override
    public void onPublishQualityUpdate(String s, ZegoStreamQuality zegoStreamQuality) {
        handlePublishQualityUpdate(mPublishStreamID, zegoStreamQuality.quality, zegoStreamQuality.videoFPS, zegoStreamQuality.videoBitrate);
    }

//    @Override
//    public void onPublishQualityUpdate(String streamID, int quality, double videoFPS, double videoBitrate) {
//        // 推流质量回调
//        handlePublishQualityUpdate(streamID, quality, videoFPS, videoBitrate);
//    }


    @Override
    protected void onPublishQulityUpdate(int quality) {
        handlePublishQualityUpdate(mPublishStreamID, quality, 0, 0);
    }

    /**
     * 推流质量更新.
     */
    protected void handlePublishQualityUpdate(String streamID, int quality, double videoFPS, double videoBitrate) {
//        onPublishQulityUpdate(quality);
        if (mLiveCallback != null) {
            mLiveCallback.onPublishQulityUpdate(quality);
        }
        ViewLive viewLive = getViewLiveByStreamID(streamID);
        if (viewLive != null) {
            viewLive.setLiveQuality(quality, videoFPS, videoBitrate);
        }

        // for espresso test, don't delete the log
//        LiveQualityLogger.write("publishStreamQuality:%d, streamId: %s, videoFPS: %.2f, videoBitrate: %.2fKb/s", quality, streamID, videoFPS, videoBitrate);
    }

    @Override
    public AuxData onAuxCallback(int dataLen) {
        AuxData auxData = new AuxData();
        auxData.dataBuf = new byte[dataLen];
        if (mBgmManager.isPlaying()) {
            if (mOnBgmMixerListener == null) {
                mOnBgmMixerListener = mBgmManager.getOnBgmMixerListener();
            }

            mOnBgmMixerListener.onBgmMixer(auxData.dataBuf, 1f);
        }

        auxData.channelCount = 1;//KSYBgmPlayer.getInstance().getChannel();
        auxData.sampleRate = KSYBgmPlayer.getInstance().getSampleRate();
        return auxData;
    }

    public int curWidth, curHeight;

    @Override
    public void onCaptureVideoSizeChangedTo(int width, int height) {
        curWidth = width;
        curHeight = height;
        onVideoSizeChanged(width, height);
    }

    public void changeVideoOrientation(int flag) {
        Log.d(TAG, " changeVideoOrientation " + (flag == 0 ? "竖屏" : "横屏"));
        if (flag == 0) {//竖屏
//            mZegoLiveRoom.setPreviewViewMode(ZegoVideoViewMode.ScaleAspectFill);
//            mZegoLiveRoom.setPreviewRotation(180);
            onVideoSizeChanged(curWidth, curHeight);
        } else {
//            mZegoLiveRoom.setPreviewRotation(180);
//            mZegoLiveRoom.setPreviewViewMode(ZegoVideoViewMode.ScaleAspectFill);
            onVideoSizeChanged(curHeight, curWidth);
        }
    }

    public boolean isPushing() {
        return isPushing;
    }

    public void updateStreamExtraInfo(String json) {
        mZegoLiveRoom.updateStreamExtraInfo(json);
    }


    public static class Builder {

        private ViewLive localView;

        private String userId;

        private String userName;

        private String liveChannel;

        private String courseID;

        private String publishStreamID;

        private String publishStreamTitle;

        public void setLocalView(ViewLive localView) {
            this.localView = localView;
        }

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder setLiveChannel(String liveChannel) {
            this.liveChannel = liveChannel;
            return this;
        }

        public Builder setPublishStreamID(String publishStreamID) {
            this.publishStreamID = publishStreamID;
            return this;
        }

        public Builder setPublishStreamTitle(String publishStreamTitle) {
            this.publishStreamTitle = publishStreamTitle;
            return this;
        }

        public ZegoLivePusher build() {
            ZegoLivePusher pusher = new ZegoLivePusher();
            pusher.setLocalView(localView);
            pusher.setUserId(userId);
            pusher.setUserName(userName);
            pusher.setLiveChannel(liveChannel);
            pusher.setPublishStreamID(publishStreamID);
            pusher.setPublishStreamTitle(publishStreamTitle);


            return pusher;
        }
    }

    protected void stopAllStream() {
        for (ViewLive viewLive : mListViewLive) {
            if (viewLive.isPublishView()) {
                stopPublish();
            } else if (viewLive.isPlayView()) {
                stopPlay(viewLive.getStreamID());
            }
            // 释放view
            viewLive.setFree();
        }
    }

    protected void stopPlay(String streamID) {
        if (!TextUtils.isEmpty(streamID)) {
            // 临时处理
            handlePlayStop(1, streamID);

            // 输出播放状态
            Log.d(TAG, "stopPlay" + ": stop play stream(" + streamID + ")");
            mZegoLiveRoom.stopPlayingStream(streamID);
        }
    }


}
