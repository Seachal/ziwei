package com.laka.live.ui.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.download.DownloadState;

/**
 * Created by luwies on 16/8/11.
 */
public class MusicDownLoadBtn extends FrameLayout {

    private Context mContext;

    private ImageView mPlayImage;

    private View mRingProgress;

    private ArcDrawable mRingProgressDrawable;

    public MusicDownLoadBtn(Context context) {
        super(context);
        init(context);
    }

    public MusicDownLoadBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MusicDownLoadBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.music_download_btn_layout, this);
        mPlayImage = (ImageView) findViewById(R.id.play);
        mRingProgress = findViewById(R.id.arc_progress);
        mRingProgressDrawable = new ArcDrawable(mContext);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mRingProgress.setBackground(mRingProgressDrawable);
        } else {
            mRingProgress.setBackgroundDrawable(mRingProgressDrawable);
        }
        updateNone();
    }

    public void updateState(DownloadState state) {
        updateState(state, 0);
    }

    public void updateState(DownloadState state, int progress) {
        switch (state) {
            case STARTED:
            case WAITING:
            case ERROR:
                updateProgress(progress);
                break;
            case FINISHED:
                updateFinished();
            break;
            case STOPPED:
                updateStoped(progress);
                break;
            case NONE:
                updateNone();
                break;
            default:
                updateNone();
                break;
        }
    }

    private void updateProgress(int progress) {
        mRingProgress.setVisibility(VISIBLE);
        mPlayImage.setImageResource(R.drawable.music_downing_btn);
        mPlayImage.setBackgroundDrawable(null);
        mRingProgressDrawable.setProgress(progress);
        mRingProgress.postInvalidate();
    }

    private void updateFinished() {
        mRingProgress.setVisibility(GONE);
        mPlayImage.setImageResource(R.drawable.live_btn_music_play_icon);
        mPlayImage.setBackgroundResource(R.drawable.live_btn_music_play_bg_selector);
    }

    private void updateStoped(int progress) {
        mRingProgress.setVisibility(VISIBLE);
        mPlayImage.setImageResource(R.drawable.live_music_btn_stop);
        mPlayImage.setBackgroundDrawable(null);
        mRingProgressDrawable.setProgress(progress);
        mRingProgress.postInvalidate();
    }

    private void updateNone() {
        mRingProgress.setVisibility(GONE);
        mPlayImage.setImageResource(R.drawable.live_btn_music_download);
        mPlayImage.setBackgroundResource(R.drawable.live_btn_music_play_bg_selector);
    }
}
