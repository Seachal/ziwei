package com.laka.live.audio;

import com.ksy.recordlib.service.util.audio.AudioUtils;
import com.ksy.recordlib.service.util.audio.MixerSync;
import com.laka.live.txrtmp.OnBgmMixerListener;
import com.laka.live.util.Log;

/**
 * Created by luwies on 16/8/8.
 */
public class OnBgmMixerListenerImp implements OnBgmMixerListener {

    private BgmManager mBgmManager;

    public OnBgmMixerListenerImp(BgmManager bgmManager) {
        mBgmManager = bgmManager;
    }

    @Override
    public void onBgmMixer(byte[] audio, float musicVolume) {
        if (audio == null || audio.length == 0) {
            return;
        }
        MixerSync mixerSync = mBgmManager.getMixerSync();
        short[] data = mixerSync.get(audio.length / 2, mBgmManager.getPosition());
        if (data != null && data.length > 0) {

            byte[] bytes = AudioUtils.shortArrayToByteArray(data);
            System.arraycopy(bytes, 0, audio, 0, audio.length);
        }
    }
}
