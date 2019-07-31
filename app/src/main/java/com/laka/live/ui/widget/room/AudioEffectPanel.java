package com.laka.live.ui.widget.room;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.laka.live.R;
import com.laka.live.audio.BgmManager;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.ui.widget.panel.BasePanel;
import com.laka.live.util.Common;
import com.laka.live.util.Log;
import com.laka.live.util.UiPreference;
import com.laka.live.util.Utils;
import com.tencent.karaoke.common.media.audiofx.Reverb;

/**
 * Created by ios on 16/6/27.
 */
public class AudioEffectPanel extends BasePanel {

    private static final String TAG = "AudioEffectPanel";
    private View parentView;
    private ImageView mIvClose;
    private Button mBtnNone, mBtnYuan, mBtnLiuxing, mBtnLow, mBtnHigh, mBtnReset;
    private SeekBar mSbVoice, mSbbgm;

    private int[] REVERB_LEVEL_DEFAULT = new int[]{Reverb.NEW_REVERB_NONE, Reverb.NEW_REVERB_KTV, Reverb.NEW_REVERB_CIXING, Reverb.NEW_REVERB_YOUYUAN, Reverb.NEW_REVERB_LAOCHANGPIAN};

    public AudioEffectPanel(Context context) {
        super(context);
        setAlpha(0);
        initView();
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_none:
                    changeEffect(REVERB_LEVEL_DEFAULT[0]);
                    break;
                case R.id.btn_yuan:
                    changeEffect(REVERB_LEVEL_DEFAULT[1]);
                    break;
                case R.id.btn_liuxing:
                    changeEffect(REVERB_LEVEL_DEFAULT[2]);
                    break;
                case R.id.btn_low:
                    changeEffect(REVERB_LEVEL_DEFAULT[3]);
                    break;
                case R.id.btn_high:
                    changeEffect(REVERB_LEVEL_DEFAULT[4]);
                    break;
                case R.id.btn_reset:
                    mSbbgm.setProgress(mSbbgm.getMax()/2);
                    mSbVoice.setProgress(mSbVoice.getMax()/2);
                    changeEffect(REVERB_LEVEL_DEFAULT[0]);
                    break;
                case R.id.iv_close:
                    hidePanel();
                    break;
            }
        }
    };

    private void changeEffect(int effect) {
        Log.d(TAG, "changeEffect index=" + effect);
        mBtnNone.setSelected(false);
        mBtnYuan.setSelected(false);
        mBtnLiuxing.setSelected(false);
        mBtnLow.setSelected(false);
        mBtnHigh.setSelected(false);
        if (effect == REVERB_LEVEL_DEFAULT[0]) {
            mBtnNone.setSelected(true);
        } else if (effect == REVERB_LEVEL_DEFAULT[1]) {
            mBtnYuan.setSelected(true);
        } else if (effect == REVERB_LEVEL_DEFAULT[2]) {
            mBtnLiuxing.setSelected(true);
        } else if (effect == REVERB_LEVEL_DEFAULT[3]) {
            mBtnLow.setSelected(true);
        } else if (effect == REVERB_LEVEL_DEFAULT[4]) {
            mBtnHigh.setSelected(true);
        }
//        if (mLivePusher != null) {
//            //todo 设置混响
//            mLivePusher.setReverbLevel(effect);
//        }
        UiPreference.putInt(Common.KEY_AUDIO_EFFECT, effect);

    }

    private void initView() {
        mBtnNone = (Button) parentView.findViewById(R.id.btn_none);
        mBtnYuan = (Button) parentView.findViewById(R.id.btn_yuan);
        mBtnLiuxing = (Button) parentView.findViewById(R.id.btn_liuxing);
        mBtnLow = (Button) parentView.findViewById(R.id.btn_low);
        mBtnHigh = (Button) parentView.findViewById(R.id.btn_high);
        mBtnNone.setOnClickListener(listener);
        mBtnYuan.setOnClickListener(listener);
        mBtnLiuxing.setOnClickListener(listener);
        mBtnLow.setOnClickListener(listener);
        mBtnHigh.setOnClickListener(listener);
        mIvClose = (ImageView) parentView.findViewById(R.id.iv_close);
        mIvClose.setOnClickListener(listener);
        mBtnReset = (Button) parentView.findViewById(R.id.btn_reset);
        mBtnReset.setOnClickListener(listener);

        mSbVoice = (SeekBar) parentView.findViewById(R.id.seekbar_voice);
        mSbVoice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float voiceVolume = (float) seekBar.getProgress() / seekBar.getMax()  ;
                float bgmVolume = UiPreference.getFloat(Common.KEY_AUDIO_BGM_VOLUME, 0.5f);
                Log.d(TAG, " voiceVolume=" + voiceVolume + " bgmVolume=" + bgmVolume);
//                if (mLivePusher != null) {
//                    UiPreference.putFloat(Common.KEY_AUDIO_VOICE_VOLUME, voiceVolume);
//                    //todo 设置音量
//                    mLivePusher.setVolume(voiceVolume, bgmVolume);
//                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSbbgm = (SeekBar) parentView.findViewById(R.id.seekbar_bgm);
        mSbbgm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float bgmVolume = (float) seekBar.getProgress() / seekBar.getMax() ;
                float voiceVolume = UiPreference.getFloat(Common.KEY_AUDIO_VOICE_VOLUME, 0.5f);
                Log.d(TAG, " voiceVolume=" + voiceVolume + " bgmVolume=" + bgmVolume);
//                if (mLivePusher != null) {
//                    UiPreference.putFloat(Common.KEY_AUDIO_BGM_VOLUME, bgmVolume);
//                    //todo 设置音量
//                    mLivePusher.setVolume(voiceVolume, bgmVolume);
//                }
                BgmManager.getInstance().setBgmVolume(bgmVolume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        int effect = UiPreference.getInt(Common.KEY_AUDIO_EFFECT, REVERB_LEVEL_DEFAULT[0]);
        changeEffect(effect);
    }

    @Override
    protected View onCreateContentView() {
        parentView = View.inflate(mContext, R.layout.panel_audio_effect, null);
        return parentView;
    }

    @Override
    protected FrameLayout.LayoutParams getLayoutParams() {
        int width = FrameLayout.LayoutParams.MATCH_PARENT;
//        int height = Utils.getScreenHeight(mContext) / 2;
        int height = Utils.dip2px(mContext,255);
        Log.d(TAG, " width=" + width + " height=" + height);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height);
        lp.gravity = Gravity.BOTTOM;
        return lp;
    }

//    TXLivePusher mLivePusher;

//    public void showPanel(TXLivePusher mLivePusher) {
//        super.showPanel();
//        this.mLivePusher = mLivePusher;
//
//        float bgmVolume = UiPreference.getFloat(Common.KEY_AUDIO_BGM_VOLUME, 0.5f);
//        float voiceVolume = UiPreference.getFloat(Common.KEY_AUDIO_VOICE_VOLUME, 0.5f);
//        Log.d(TAG," sbVoiceProgress="+voiceVolume+"*"+mSbVoice.getMax()+"="+voiceVolume * mSbVoice.getMax());
//        mSbVoice.setProgress((int) (voiceVolume * mSbVoice.getMax()));
//        mSbbgm.setProgress((int) (bgmVolume * mSbbgm.getMax()));
//        if (mLivePusher != null) {
//            //todo 设置音量
//            mLivePusher.setVolume(voiceVolume, bgmVolume);
//        }
//    }

    public void hidePanel() {
        super.hidePanel();
    }


}
