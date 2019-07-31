package com.laka.live.zego;

import android.telephony.PhoneStateListener;
import android.text.TextUtils;

import com.laka.live.account.AccountInfoManager;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.lakalive.LivePlayer;
import com.laka.live.util.Log;
import com.laka.live.util.Utils;
import com.laka.live.zego.widgets.ViewLive;
import com.zego.zegoliveroom.ZegoLiveRoom;
import com.zego.zegoliveroom.callback.IZegoLivePlayerCallback;
import com.zego.zegoliveroom.callback.IZegoLoginCompletionCallback;
import com.zego.zegoliveroom.callback.IZegoRoomCallback;
import com.zego.zegoliveroom.constants.ZegoConstants;
import com.zego.zegoliveroom.constants.ZegoVideoViewMode;
import com.zego.zegoliveroom.entity.ZegoStreamInfo;
import com.zego.zegoliveroom.entity.ZegoStreamQuality;

/**
 * Created by luwies on 16/10/8.
 */
public class ZegoLivePlayer extends LivePlayer implements IZegoLivePlayerCallback {

    private static final String TAG = "ZegoLivePlayer";

    private ViewLive mRemoteView;

    private String mUserId;

    private String mUserName;

    private String mLiveChannel;

    private String mStreamID;

    private boolean isLoginChannel = false;

    // 初始化来电状态, false表示没有来电
    private boolean mHaveBeenCalled = false;


    protected String mRoomID = null;

    private PhoneStateListener mPhoneStateListener;

    public boolean isLoginChannel() {
        return isLoginChannel;
    }

    public void setLoginChannel(boolean loginChannel) {
        isLoginChannel = loginChannel;
    }

    private ZegoLivePlayer() {
//        ZegoApiManager.getInstance().register(this);
        mZegoLiveRoom = ZegoApiManager.getInstance().getZegoLiveRoom();
    }

    public void setRemoteView(ViewLive remoteView) {
        this.mRemoteView = remoteView;
        mListViewLive.add(mRemoteView);
    }


    public void updatePlayerView() {
        Log.d(TAG, " updatePlayerView mRemoteView=" + mRemoteView);
        mZegoLiveRoom.updatePlayView(mStreamID, mRemoteView);
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public void setUserName(String userName) {
        this.mUserName = userName;
    }

    public void setLiveChannel(String liveChannel) {
        this.mLiveChannel = liveChannel;
    }

    public void setStreamID(String streamID) {
        this.mStreamID = streamID;
    }

//    public void setRemoteViewIndex(ZegoAVKitCommon.ZegoRemoteViewIndex index) {
//        this.mRemoteViewIndex = index;
//    }

    private void loginChannel() {
//        mRoomID = ZegoRoomUtil.getRoomID(ZegoRoomUtil.ROOM_TYPE_MORE);
        mRoomID = mUserId;
        String userID = AccountInfoManager.getInstance().getCurrentAccountUserIdStr();
//        String userName = AccountInfoManager.getInstance().getCurrentAccountNickName();
        String userName = mLiveChannel;
        Log.d(TAG, " 设置用户信息 userID=" + userID + " userName=" + userName + " mRoomID=" + mRoomID);
        ZegoLiveRoom.setUser(userID, userName);
        mZegoLiveRoom = ZegoApiManager.getInstance().getZegoLiveRoom();
        mZegoLiveRoom.setZegoLivePlayerCallback(this);
        mZegoLiveRoom.loginRoom(mLiveChannel, "", ZegoConstants.RoomRole.Audience, new IZegoLoginCompletionCallback() {
            @Override
            public void onLoginCompletion(int errorCode, ZegoStreamInfo[] zegoStreamInfos) {
                if (errorCode == 0) {
                    handleAudienceLoginRoomSuccess(zegoStreamInfos);
                    if (zegoStreamInfos != null && zegoStreamInfos.length > 0) {
                        ZegoStreamInfo info = zegoStreamInfos[zegoStreamInfos.length - 1];
                        Log.d(TAG, " onLoginCompletion onStreamExtraInfoUpdated info=" + info.extraInfo);
                        if (!Utils.isEmpty(info.extraInfo)) {
                            EventBusManager.postEvent(info, SubcriberTag.ROOM_EXTRA_INFO_UPDATE);
                        }
                    }
                } else {
                    handleAudienceLoginRoomFail(errorCode);
                }
            }
        });

        mZegoLiveRoom.setZegoRoomCallback(new IZegoRoomCallback() {
            @Override
            public void onKickOut(int i, String s) {
                Log.d(TAG, " IZegoRoomCallback onKickOut");
            }

            @Override
            public void onDisconnect(int i, String s) {
                Log.d(TAG, " IZegoRoomCallback onDisconnect");
            }

            @Override
            public void onReconnect(int i, String s) {

            }

            @Override
            public void onTempBroken(int i, String s) {

            }

            @Override
            public void onStreamUpdated(int i, ZegoStreamInfo[] zegoStreamInfos, String s) {
                Log.d(TAG, " IZegoRoomCallback onStreamUpdated");
            }

            @Override
            public void onStreamExtraInfoUpdated(ZegoStreamInfo[] zegoStreamInfos, String s) {
                Log.d(TAG, " IZegoRoomCallback onStreamExtraInfoUpdated zegoStreamInfos.length=" + zegoStreamInfos.length);
                if (zegoStreamInfos != null && zegoStreamInfos.length > 0) {
                    ZegoStreamInfo info = zegoStreamInfos[zegoStreamInfos.length - 1];
                    Log.d(TAG, " onStreamExtraInfoUpdated info=" + info.extraInfo);
//                    EventBusManager.postEvent(info, SubcriberTag.ROOM_EXTRA_INFO_UPDATE);
                }
            }

            @Override
            public void onRecvCustomCommand(String s, String s1, String s2, String s3) {
                Log.d(TAG, " IZegoRoomCallback onRecvCustomCommand");
            }
        });
        Log.d(TAG, "player loginChannel mUserId=" + mUserId + " mUserName=" + mUserName + " mLiveChannel=" + mLiveChannel
                + " mRoomID=" + mRoomID);
//        ZegoUser zegoUser = new ZegoUser(mUserId, mUserName);
//        ZegoApiManager.getInstance().loginChannel(zegoUser, mLiveChannel);
//        initPhoneCallingListener();
    }

    private void logoutChannel() {
        // 退出频道, 连麦情况下，要stop掉所有的stream后，才执行logoutchannel。
        mZegoLiveRoom.logoutRoom();
//        ZegoApiManager.getInstance().logoutChannel();
//        unInitPhoneCallingListener();
        isLoginChannel = false;
    }

    @Override
    public void startPlay() {
        if (!isLoginChannel) {
            loginChannel();
        } else {
            startPlay(mStreamID);
        }
    }
//    @Override
//    public void startPlay() {
//        if (!isLoginChannel) {
//            loginChannel();
//        } else {
//            startStream();
//        }
//    }

//    private void startStream() {
//        Log.d(TAG,"startPlay mStreamID="+mStreamID+" mRemoteViewIndex="+mRemoteViewIndex);
//        ZegoApiManager.getInstance().setRemoteViewMode(mRemoteViewIndex, ZegoAVKitCommon.ZegoVideoViewMode.ScaleAspectFill);//多路播放必须设置
//        ZegoApiManager.getInstance().setRemoteView(mRemoteViewIndex, mRemoteView);
//        ZegoApiManager.getInstance().startPlayStream(mStreamID, mRemoteViewIndex);
//    }


    @Override
    public void stopPlay() {
        mZegoLiveRoom.stopPlayingStream(mStreamID);
//        mZegoLiveRoom.setZegoLivePublisherCallback(null);
//        mZegoLiveRoom.setZegoLivePlayerCallback(null);
//        mZegoLiveRoom.setZegoRoomCallback(null);
//        ZegoApiManager.getInstance().stopPlayStream(mStreamID);
    }

    @Override
    public void destroy() {
        Log.d(TAG, " destroy");
        //停止观看
//        ZegoApiManager.getInstance().stopPlayStream(mStreamID);
//        ZegoApiManager.getInstance().setRemoteView(mRemoteViewIndex, null);
//        ZegoApiManager.getInstance().unRegister(this);
        //停止观看
        mZegoLiveRoom.stopPlayingStream(mStreamID);
// 在离开直播或者观看页面时，请将sdk的callback置空，避免内存泄漏
// 清空回调, 避免内存泄漏
        mZegoLiveRoom.setZegoLivePublisherCallback(null);
        mZegoLiveRoom.setZegoLivePlayerCallback(null);
        mZegoLiveRoom.setZegoRoomCallback(null);

        logoutChannel();
    }

    @Override
    public void reset(String streamId, String channelId) {
        mZegoLiveRoom.logoutRoom();
//        ZegoApiManager.getInstance().logoutChannel();
        isLoginChannel = false;
        mStreamID = streamId;
        mLiveChannel = channelId;
    }

//    @Override
//    public void onLoginChannel(String liveChannel, int retCode) {
//        Log.d(TAG," onLoginChannel liveChannel="+liveChannel+" retCode="+retCode);
//        if (retCode == 0) {
//            isLoginChannel = true;
//            // 登录成功
//            // startPlay
//            startStream();
//        } else {
//            // 登录失败
//            onPlayStopEvent(retCode,mStreamID,liveChannel);
//        }
//    }


//    @Override
//    public void onPublishStop(int retCode, String streamID, String liveChannel) {
//        Log.error(TAG, new StringBuilder("onPublishStop : retCode = ").append(retCode)
//                .append("streamID : ").append(streamID)
//                .append("liveChannel : ").append(liveChannel)
//                .toString());
//        onPublishStop(retCode);
//    }
//
//    @Override
//    public void onMixStreamConfigUpdate(int retCode, String mixStreamID, HashMap<String, Object> info) {
//        Log.error(TAG, new StringBuilder("onMixStreamConfigUpdate : retCode = ").append(retCode)
//                .append("mixStreamID : ").append(mixStreamID)
//                .append("info : ").append(info)
//                .toString());
//    }
//
//    @Override
//    public void onPlaySucc(String streamID, String liveChannel) {
//        Log.error(TAG, new StringBuilder("onPlaySucc ")
//                .append(" streamID : ").append(streamID)
//                .append(" liveChannel : ").append(liveChannel)
//                .toString());
//        onPlaySucc();
//    }
//
//    @Override
//    public void onPlayStop(int retCode, String streamID, String liveChannel) {
//        Log.error(TAG, new StringBuilder("onPlayStop : retCode = ").append(retCode)
//                .append("streamID : ").append(streamID)
//                .append("liveChannel : ").append(liveChannel)
//                .toString());
//        onPlayStopEvent(retCode, streamID, liveChannel);
//    }


//    @Override
//    public void onPlayQualityUpdate(String streamID, int quality,double var1,double var2) {
//        onPlayQualityUpdate(quality);
//    }


//    @Override
//    public AuxData onAuxCallback(int dataLen) {
//        return handleAuxCallback(dataLen);
//    }

    /**
     * 推流质量更新.
     */
    protected void handlePublishQualityUpdate(String streamID, int quality, double videoFPS, double videoBitrate) {
        onPlayQualityUpdate(quality);
        ViewLive viewLive = getViewLiveByStreamID(streamID);
        if (viewLive != null) {
            viewLive.setLiveQuality(quality, videoFPS, videoBitrate);
        }

        // for espresso test, don't delete the log
//        LiveQualityLogger.write("publishStreamQuality:%d, streamId: %s, videoFPS: %.2f, videoBitrate: %.2fKb/s", quality, streamID, videoFPS, videoBitrate);
    }

    @Override
    public void onPlayStateUpdate(int stateCode, String streamID) {
        // 拉流状态更新
        if (stateCode == 0) {
            handlePlaySucc(streamID);
        } else {
            handlePlayStop(stateCode, streamID);
        }
    }

    @Override
    public void onPlayQualityUpdate(String s, ZegoStreamQuality zegoStreamQuality) {

    }

    @Override
    protected void onPlayQualityUpdate(int quality) {
        handlePlayQualityUpdate(mStreamID, quality, 0, 0);
    }

//    @Override
//    public void onPlayQualityUpdate(String streamID, int quality, double videoFPS, double videoBitrate) {
//        // 拉流质量回调
//        handlePlayQualityUpdate(streamID, quality, videoFPS, videoBitrate);
//    }

    @Override
    public void onInviteJoinLiveRequest(int i, String s, String s1, String s2) {

    }

    @Override
    public void onRecvEndJoinLiveCommand(String s, String s1, String s2) {

    }

    @Override
    public void onVideoSizeChangedTo(String streamID, int width, int height) {
        handleVideoSizeChanged(streamID, width, height);
    }

    public int curWidth, curHeight;
    public String curStreamID;

    /**
     * 拉流分辨率更新.
     */
    protected void handleVideoSizeChanged(String streamID, int width, int height) {
        if (curWidth == 0) {
            curWidth = width;
            curHeight = height;
        }
        curStreamID = streamID;
        //hidePlayBackground();
        Log.d(TAG, " handleVideoSizeChanged ScaleAspectFit width=" + width + " height=" + height
                + " ");
        if (width > height) {
            ViewLive viewLivePlay = getViewLiveByStreamID(streamID);
            if (viewLivePlay != null) {
//                if (viewLivePlay.getWidth() < viewLivePlay.getHeight()) {
//                    viewLivePlay.setZegoVideoViewMode(true, ZegoVideoViewMode.ScaleAspectFill);
//                    mZegoLiveRoom.setViewMode(ZegoVideoViewMode.ScaleAspectFill, streamID);
////                    viewLivePlay.setZegoVideoViewMode(true, ZegoVideoViewMode.ScaleAspectFit);
////                    mZegoLiveRoom.setViewMode(ZegoVideoViewMode.ScaleAspectFit, streamID);
//                } else {
//                    viewLivePlay.setZegoVideoViewMode(true, ZegoVideoViewMode.ScaleAspectFit);
//                    mZegoLiveRoom.setViewMode(ZegoVideoViewMode.ScaleAspectFit, streamID);
////                    viewLivePlay.setZegoVideoViewMode(true, ZegoVideoViewMode.ScaleAspectFill);
////                    mZegoLiveRoom.setViewMode(ZegoVideoViewMode.ScaleAspectFill, streamID);
//                }
            }
        }

//        mRlytControlHeader.bringToFront();

    }

    public void changeVideoOrientation(int flag) {
        if (flag == 0) {//竖屏
            handleVideoSizeChanged(curStreamID, curWidth, curHeight);
        } else {
            handleVideoSizeChanged(curStreamID, curHeight, curWidth);
        }
    }

    /**
     * 拉流质量更新.
     */
    protected void handlePlayQualityUpdate(String streamID, int quality, double videoFPS, double videoBitrate) {
        ViewLive viewLive = getViewLiveByStreamID(streamID);
        if (viewLive != null) {
            viewLive.setLiveQuality(quality, videoFPS, videoBitrate);
        }

        // for espresso test, don't delete the log
//        LiveQualityLogger.write("playStreamQuality: %d, streamId: %s, videoFPS: %.2f, videoBitrate: %.2fKb/s", quality, streamID, videoFPS, videoBitrate);
    }
//    protected AuxData handleAuxCallback(int dataLen) {
//        // 开启伴奏后, sdk每20毫秒一次取数据
//        if (!mEnableBackgroundMusic || dataLen <= 0) {
//            return null;
//        }
//
//        AuxData auxData = new AuxData();
//        auxData.dataBuf = new byte[dataLen];
//
//        try {
//            AssetManager am = getAssets();
//            if (mIsBackgroundMusic == null) {
//                mIsBackgroundMusic = am.open("a.pcm");
//            }
//            int len = mIsBackgroundMusic.read(auxData.dataBuf);
//
//            if (len <= 0) {
//                // 歌曲播放完毕
//                mIsBackgroundMusic.close();
//                mIsBackgroundMusic = null;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        auxData.channelCount = 2;
//        auxData.sampleRate = 44100;
//
//
//        return auxData;
//    }

    public static class Builder {
        private ViewLive remoteView;

        private String userId;

        private String userName;

        private String liveChannel;

        private String streamID;

//        private ZegoAVKitCommon.ZegoRemoteViewIndex remoteViewIndex = ZegoAVKitCommon.ZegoRemoteViewIndex.First;

        public Builder setRemoteView(ViewLive remoteView) {
            this.remoteView = remoteView;
            return this;
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

        public Builder setStreamID(String streamID) {
            this.streamID = streamID;
            return this;
        }

//        public void setRemoteViewIndex(ZegoAVKitCommon.ZegoRemoteViewIndex remoteViewIndex) {
//            this.remoteViewIndex = remoteViewIndex;
//        }

        public ZegoLivePlayer build() {
            ZegoLivePlayer player = new ZegoLivePlayer();
            player.setRemoteView(remoteView);
            player.setUserId(userId);
            player.setUserName(userName);
            player.setLiveChannel(liveChannel);
            player.setStreamID(streamID);
//            player.setRemoteViewIndex(remoteViewIndex);
            return player;
        }
    }


    /**
     * 观众登录房间成功.
     */
    protected void handleAudienceLoginRoomSuccess(ZegoStreamInfo[] zegoStreamInfos) {

        startPlay(mStreamID);

//        // 播放房间的流
//        if (zegoStreamInfos != null && zegoStreamInfos.length > 0) {
//            for (int i = 0; i < zegoStreamInfos.length; i++) {
//                startPlay(zegoStreamInfos[i].streamID);
//            }
//        }
//
//        // 分享主播视频
//        if (zegoStreamInfos != null && zegoStreamInfos.length > 0) {
//            for (ZegoStreamInfo info : zegoStreamInfos) {
//
//                ViewLive viewLive = getViewLiveByStreamID(info.streamID);
//                final HashMap<String, String> mapInfo =
//                        (new Gson()).fromJson(info.extraInfo, new TypeToken<HashMap<String, String>>() {
//                        }.getType());
//
//                if (viewLive != null && mapInfo != null) {
//                    boolean firstAnchor = Boolean.valueOf(mapInfo.get(Constants.FIRST_ANCHOR));
//                    if (firstAnchor) {
//                        List<String> listUrls = new ArrayList<>();
//                        listUrls.add(mapInfo.get(Constants.KEY_HLS));
//                        listUrls.add(mapInfo.get(Constants.KEY_RTMP));
//                        viewLive.setListShareUrls(listUrls);
//                        break;
//                    }
//
//                }
//            }
//        }

        // 打印log
        Log.d(TAG, ": onLoginRoom success(" + mRoomID + "), streamCounts:" + zegoStreamInfos.length);
    }

    /**
     * 开始播放流.
     */
    protected void startPlay(String streamID) {
        Log.d(TAG, " startPlay streamID=" + streamID);
        if (TextUtils.isEmpty(streamID)) {
            return;
        }

        if (isStreamExisted(streamID)) {
            Log.d(TAG, "流已存在");
//            ToastHelper.showToast("流已存在");
//            Toast.makeText(this, "流已存在", Toast.LENGTH_SHORT).show();
            return;
        }

        ViewLive freeViewLive = getFreeViewLive();
        if (freeViewLive == null) {
            Log.d(TAG, " freeViewLive==null");
            return;
        }

        // 设置流信息
        freeViewLive.setStreamID(streamID);
        freeViewLive.setPlayView(true);

        // 输出播放状态
        Log.d(TAG, ": start play stream(" + streamID + ")");

        // 初始化拉流参数, 外部渲染模式使用
//        initPlayConfgis(freeViewLive, streamID);

        // 播放
        mZegoLiveRoom.startPlayingStream(streamID, freeViewLive.getTextureView());
        mZegoLiveRoom.setViewMode(ZegoVideoViewMode.ScaleAspectFit, streamID);//ScaleAspectFill
    }

    public void setViewMode(boolean isFull) {
        if (true) {
            return;
        }
        if (isFull) {
            boolean result = mZegoLiveRoom.setViewMode(ZegoVideoViewMode.ScaleAspectFill, curStreamID);
            Log.d(TAG, " setViewMode isFull=" + isFull + " result=" + result + " ScaleAspectFill");
        } else {
            boolean result = mZegoLiveRoom.setViewMode(ZegoVideoViewMode.ScaleAspectFit, curStreamID);
            Log.d(TAG, " setViewMode isFull=" + isFull + " result=" + result + " ScaleAspectFit");
        }
    }

    private boolean isStreamExisted(String streamID) {
        if (TextUtils.isEmpty(streamID)) {
            return true;
        }

        boolean isExisted = false;

        for (ViewLive viewLive : mListViewLive) {
            if (streamID.equals(viewLive.getStreamID())) {
                isExisted = true;
                break;
            }
        }

        return isExisted;
    }

    /**
     * 观众登录房间失败.
     */
    protected void handleAudienceLoginRoomFail(int errorCode) {
        // 打印log
        Log.d(TAG, ": onLoginRoom fail(" + mRoomID + ") errorCode:" + errorCode);
    }
}
