package com.laka.live.audio;

import com.ksy.recordlib.service.util.audio.KSYBgmPlayer;
import com.ksy.recordlib.service.util.audio.MixerSync;
import com.laka.live.R;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.txrtmp.OnBgmMixerListener;
import com.laka.live.util.Log;
import com.laka.live.util.ToastHelper;
import com.laka.live.zego.ZegoApiManager;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * Created by ios on 16/8/8.
 */
public class BgmManager {
    private static final String TAG = "BgmManager";
    private KSYBgmPlayer mKsyBgmPlayer;
    private static BgmManager self;
    private float bgmVolume = 0.6f;
    private boolean earMirror = false;
    private MixerSync mBgmMixerSync;
    private boolean isStop = false;
    private boolean isPlaying = false;
    private KSYBgmPlayer.OnBgmPcmListener mOnBgmPcmListener = new OnBgmPcmListenerImp(this);
    private OnBgmMixerListener mOnBgmMixerListener = new OnBgmMixerListenerImp(this);

    private MixerSync mMixerSync;

    public static BgmManager getInstance() {
        if (self == null) {
            synchronized (BgmManager.class) {
                if (self == null) {
                    self = new BgmManager();
                }
            }
        }
        return self;
    }

    private BgmManager() {
        mMixerSync = new MixerSync();
    }

    public void startMixMusic(String musicPath) {


        if (mKsyBgmPlayer == null) {
            mKsyBgmPlayer = KSYBgmPlayer.getInstance();
            mKsyBgmPlayer.setOnBgmPlayerListener(mBgmListener);
            mKsyBgmPlayer.setVolume(bgmVolume);
            mKsyBgmPlayer.setMute(earMirror);
            mBgmMixerSync = new MixerSync();
        }
        mKsyBgmPlayer.stop(); //先停止上次播放
        mKsyBgmPlayer.setOnBgmPcmListener(mOnBgmPcmListener);

        File musicFile = new File(musicPath);
        if (musicFile.exists() && musicFile.length() > 0) {
            Log.d(TAG, "bgm文件存在");
        } else {
            Log.d(TAG, "bgm文件不存在");
            ToastHelper.showToast(R.string.bgm_not_found);
            return;
        }

//        ZegoApiManager.getInstance().enableMux(true);
        mKsyBgmPlayer.start(musicPath, false);//不循环播放
        isPlaying = true;
        isStop = false;
    }

    public boolean stopMixMusic() {
//        ZegoApiManager.getInstance().enableMux(false);
        if (this.mKsyBgmPlayer == null) {
            return false;
        }
        this.mKsyBgmPlayer.setOnBgmPcmListener(null);
        this.mKsyBgmPlayer.stop();
        if (this.mBgmMixerSync != null) {
            this.mBgmMixerSync.flush();
        }
        isPlaying = false;
        isStop = true;
        return true;
    }

    public void destory() {
        if (mKsyBgmPlayer != null) {
            mKsyBgmPlayer.release();
            mKsyBgmPlayer = null;
        }
    }

    public void resume() {
        if (mKsyBgmPlayer != null) {
            mKsyBgmPlayer.resume();
            isPlaying = isStop == false;
        } else {
            isPlaying = false;
        }
    }

    public void pause() {
        if (mKsyBgmPlayer != null) {
            mKsyBgmPlayer.pause();
        }
        isPlaying = false;
    }

    private KSYBgmPlayer.OnBgmPlayerListener mBgmListener = new KSYBgmPlayer.OnBgmPlayerListener() {
        @Override
        public void onCompleted() {
            Log.d(TAG, "End of the currently playing music");
            EventBusManager.postEvent(0, SubcriberTag.AUDIO_TRACK_STOP_BGM);//通知歌词隐藏
        }

        @Override
        public void onError(int err) {
            Log.e(TAG, "onMusicError: " + err);
        }
    };

    public boolean isPlaying() {
        return isPlaying;
    }

    public MixerSync getMixerSync() {
        return mMixerSync;
    }

    public OnBgmMixerListener getOnBgmMixerListener() {
        return mOnBgmMixerListener;
    }

    public long getPosition() {
        if (mKsyBgmPlayer == null) {
            return 0;
        }
        try {
            return mKsyBgmPlayer.getPosition();
        }catch (Exception e){
            return 0;
        }

    }

    public void setBgmVolume(float volume) {
        bgmVolume = volume;
        if (mKsyBgmPlayer != null) {
            mKsyBgmPlayer.setVolume(volume);
        }
    }

}
