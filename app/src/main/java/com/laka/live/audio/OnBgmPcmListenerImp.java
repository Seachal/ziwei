package com.laka.live.audio;


import com.ksy.recordlib.service.util.audio.KSYBgmPlayer;
import com.ksy.recordlib.service.util.audio.MixerSync;

/**
 * Created by luwies on 16/8/8.
 */
public class OnBgmPcmListenerImp implements KSYBgmPlayer.OnBgmPcmListener {

    private BgmManager mBgmManager;

    public OnBgmPcmListenerImp(BgmManager audioRecorder) {
        this.mBgmManager = audioRecorder;
    }

    @Override
    public void onPcmData(short[] data, long pts) {
        if (mBgmManager.isPlaying()) {
            MixerSync mMixerSync = mBgmManager.getMixerSync();
            mMixerSync.put(data, pts);
        }
    }
}
