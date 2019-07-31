package com.laka.live.ui.room;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.manager.BytesReader;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Copyright © 2016 Zego. All rights reserved.
 * des: 直播view.
 */
public class LiveSmallView extends RelativeLayout implements View.OnClickListener {
    private static final String TAG = "LiveSmallView";
    /**
     * ZegoAVkit支持3条流同时play, 索引为ZegoRemoteViewIndex.First,
     * ZegoRemoteViewIndex.Second, ZegoRemoteViewIndex.Third
     * 索引值相应为0, 1, 2
     * 自定义publish的流的所引值为100
     */
    public static final int MODE_READY = 1, MODE_LIVE = 2, MODE_END = 3, MODE_NO_VIDEO = 4;


    private View mRootView;

    private TextureView mTextureView;
    private TextureView mLiveView,mPlayView;

    //准备控件画面
    private RelativeLayout rlReady;
    private SimpleDraweeView ivUserFace;
    private TextView tvName, tvTime, tvTips;
    private ImageView ivClose, ivOpenVideo;

    //播放中控件画面
    private RelativeLayout rlLive;
    private SimpleDraweeView ivLiveUserFace;
    private TextView tvLiveName;
    private ImageView ivLiveClose, ivLiveOpenVideo;

    //结束控件画面
    private LinearLayout rlEnd;
    private TextView tvCancle;
    private Button tvConfirm;
    ConnectMicManager connectMicManager;

    BytesReader.Audience curUser;

    int curMode = MODE_READY;
    boolean isCountDownFinish = false;

    private Animation mAnimation;

    public void setConnectMicManager(ConnectMicManager connectMicManager) {
        this.connectMicManager = connectMicManager;
    }

    public LiveSmallView(Context context) {
        super(context);
        initViews(context, false);
    }

    public LiveSmallView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initViews(context, false);
    }

    public LiveSmallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initViews(context, false);
    }

    private void initViews(Context context, boolean isBigView) {
        mRootView = LayoutInflater.from(context).inflate(R.layout.view_live_small, null);
        mLiveView = (TextureView) mRootView.findViewById(R.id.textureView);
        mPlayView = (TextureView) mRootView.findViewById(R.id.playView);
        mTextureView = mPlayView;
        //准备画面
        rlReady = (RelativeLayout) mRootView.findViewById(R.id.rl_ready);
        ivUserFace = (SimpleDraweeView) mRootView.findViewById(R.id.user_face);
        ivUserFace.setOnClickListener(this);
        tvName = (TextView) mRootView.findViewById(R.id.tv_name);
        tvName.setOnClickListener(this);
        tvTime = (TextView) mRootView.findViewById(R.id.tv_time);
        tvTips = (TextView) mRootView.findViewById(R.id.tv_tips);
        ivClose = (ImageView) mRootView.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(this);
        ivOpenVideo = (ImageView) mRootView.findViewById(R.id.iv_open_video);
        ivOpenVideo.setOnClickListener(this);
        //播放中
        rlLive = (RelativeLayout) mRootView.findViewById(R.id.rl_live);
        ivLiveUserFace = (SimpleDraweeView) mRootView.findViewById(R.id.live_user_face);
        ivLiveUserFace.setOnClickListener(this);
        tvLiveName = (TextView) mRootView.findViewById(R.id.tv_live_name);
        tvLiveName.setOnClickListener(this);
        ivLiveClose = (ImageView) mRootView.findViewById(R.id.iv_live_close);
        ivLiveClose.setOnClickListener(this);
        ivLiveOpenVideo = (ImageView) mRootView.findViewById(R.id.iv_live_open_video);
        ivLiveOpenVideo.setOnClickListener(this);
        //结束控件画面
        rlEnd = (LinearLayout) mRootView.findViewById(R.id.rl_end);
        tvConfirm = (Button) mRootView.findViewById(R.id.tv_confirm);
        tvCancle = (TextView) mRootView.findViewById(R.id.tv_cancle);
        tvConfirm.setOnClickListener(this);
        tvCancle.setOnClickListener(this);
        addView(mRootView);

        mAnimation = AnimationUtils.loadAnimation(context , R.anim.live_count_down) ;    }

    public void setUserInfo(BytesReader.Audience user) {
        if (user == null) {
            return;
        }
        curUser = user;
        tvName.setText(user.nickName);
        tvLiveName.setText(user.nickName);
        ImageUtil.loadImage(ivUserFace, user.avatar);
        ImageUtil.loadImage(ivLiveUserFace, user.avatar);

        refreshOpenVideo(user.isVideoOpen == 1 ? true : false);

        if (connectMicManager.isSecondZhubo) {
            ivOpenVideo.setVisibility(View.VISIBLE);
            ivLiveOpenVideo.setVisibility(View.VISIBLE);
        } else {
            ivOpenVideo.setVisibility(View.GONE);
            ivLiveOpenVideo.setVisibility(View.GONE);
        }

        Log.d(TAG, " isSecondZhubo=" + connectMicManager.isSecondZhubo + " isFirstZhubo=" + connectMicManager.isFirstZhubo);
        if (connectMicManager.isSecondZhubo || connectMicManager.isFirstZhubo) {
            ivClose.setVisibility(View.VISIBLE);
            ivLiveClose.setVisibility(View.VISIBLE);
        } else {
            ivClose.setVisibility(View.GONE);
            ivLiveClose.setVisibility(View.GONE);
        }
    }

    boolean isVideoOpen;

    public void refreshOpenVideo(boolean videoOpen) {
        this.isVideoOpen = videoOpen;
        if (isVideoOpen) {
            ivOpenVideo.setImageResource(R.drawable.live_icon_video_h);
            ivLiveOpenVideo.setImageResource(R.drawable.live_icon_video_h);
        } else {
            ivOpenVideo.setImageResource(R.drawable.live_icon_videoclose);
            ivLiveOpenVideo.setImageResource(R.drawable.live_icon_videoclose);
        }

        if (connectMicManager.isSecondZhubo) {
            ivOpenVideo.setVisibility(View.VISIBLE);
            ivLiveOpenVideo.setVisibility(View.VISIBLE);
        } else {
            ivOpenVideo.setVisibility(View.GONE);
            ivLiveOpenVideo.setVisibility(View.GONE);
        }
    }

    public void switchMode(int mode) {
        Log.d(TAG, "switchMode curMode=" + curMode + " mode=" + mode);
        curMode = mode;
        if (mode == MODE_READY) {
            mTextureView.setVisibility(View.INVISIBLE);
            rlReady.setVisibility(View.VISIBLE);
            rlLive.setVisibility(View.GONE);
            rlEnd.setVisibility(View.GONE);
            if (connectMicManager.isZhubo()){
                tvTime.setVisibility(VISIBLE);
                new LinkCountDownTimer(7000L , 1000L).start();
            }
            tvTips.setText("即将视频互动");
        } else if (mode == MODE_LIVE) {
            mTextureView.setVisibility(View.VISIBLE);
            if (!connectMicManager.isZhubo()||isCountDownFinish){
                rlReady.setVisibility(GONE);
                rlLive.setVisibility(View.VISIBLE);
            }
            rlEnd.setVisibility(View.GONE);
        } else if (mode == MODE_END) {
            rlReady.setVisibility(View.GONE);
            rlLive.setVisibility(View.GONE);
            rlEnd.setVisibility(View.VISIBLE);
        } else if (mode == MODE_NO_VIDEO) {
            mTextureView.setVisibility(View.INVISIBLE);
            rlReady.setVisibility(View.VISIBLE);
            rlLive.setVisibility(View.GONE);
            rlEnd.setVisibility(View.GONE);
            tvTips.setText("语音互动中");
        }

    }

    public TextureView getTextureView(boolean isLive) {
        if(isLive){
            mPlayView.setVisibility(View.GONE);
            mLiveView.setVisibility(View.VISIBLE);
            mTextureView = mLiveView;
        }else{
            mPlayView.setVisibility(View.VISIBLE);
            mLiveView.setVisibility(View.GONE);
            mTextureView = mPlayView;
        }
        return mTextureView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
            case R.id.iv_live_close:
                if (connectMicManager.isSecondZhubo || connectMicManager.isFirstZhubo) {
                    switchMode(MODE_END);
                }
                break;
            case R.id.tv_confirm:
                Log.d(TAG, " onClick tv_confirm curMode=" + curMode);
                if (curMode == MODE_END) {
                    if (connectMicManager.isSecondZhubo) {
                        connectMicManager.closeLinkMic("");
                    } else {
                        if (connectMicManager.curLinkUser != null) {
                            connectMicManager.closeLinkMic(connectMicManager.curLinkUser.id);
                        }
                    }
                    if(isVideoOpen){
                        switchMode(MODE_LIVE);
                    }else{
                        switchMode(MODE_NO_VIDEO);
                    }
                }
                break;
            case R.id.tv_cancle:
                Log.d(TAG," onClick cancle curMode="+curMode+" isVideoOpen="+isVideoOpen);
                if (curMode == MODE_END) {
                    if(isVideoOpen){
                        switchMode(MODE_LIVE);
                    }else{
                        switchMode(MODE_NO_VIDEO);
                    }
                }
                break;
            case R.id.iv_open_video:
            case R.id.iv_live_open_video:
                if (connectMicManager.isSecondZhubo) {
                    connectMicManager.openVideo();
                }
                break;
            case R.id.user_face:
            case R.id.tv_name:
                if (curUser != null && (curMode == MODE_READY || curMode == MODE_NO_VIDEO)) {
                    EventBusManager.postEvent(curUser.id, SubcriberTag.SHOW_USER_POP);
                }
                break;
            case R.id.live_user_face:
            case R.id.tv_live_name:
                if (curUser != null && curMode == MODE_LIVE) {
                    EventBusManager.postEvent(curUser.id, SubcriberTag.SHOW_USER_POP);
                }
                break;
        }
    }


    private class LinkCountDownTimer extends CountDownTimer{

        public LinkCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            isCountDownFinish = false;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int time = (int)millisUntilFinished / 1000 ;
            switch (time){
                case 1 :
                    tvTime.setText("开始");
                    break;
                default:
                    tvTime.setText(String.valueOf(time - 1));
                    tvTime.startAnimation(mAnimation);
                    break;
            }

        }

        @Override
        public void onFinish() {
            Log.d(TAG," onFinish curMode="+curMode+" isVideoOpen="+isVideoOpen);
            if (curMode == MODE_LIVE){
                if(isVideoOpen){
                    rlReady.setVisibility(GONE);
                    rlLive.setVisibility(View.VISIBLE);
                    switchMode(MODE_LIVE);
                }else{
                    switchMode(MODE_NO_VIDEO);
                }
//                rlReady.setVisibility(View.GONE);
//                rlLive.setVisibility(View.VISIBLE);
            }
            tvTime.setVisibility(INVISIBLE);
            isCountDownFinish = true;
        }
    }
}
