package com.laka.live.ui.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import com.laka.live.R;
import com.laka.live.thread.BackgroundThread;

/**
 * Created by luwies on 16/5/18.
 */
public class RefreshLoadingView extends AppCompatImageView {

    private AnimationDrawable mDrawable;

    private OnLoadListener mOnLoadListener;

    public RefreshLoadingView(Context context) {
        super(context);
        init();
    }

    public RefreshLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RefreshLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        final Handler handler = new Handler(Looper.getMainLooper());
        BackgroundThread.post(new Runnable() {
            @Override
            public void run() {
                mDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.refresh_loading);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        setImageDrawable(mDrawable);
                        if (getVisibility() == View.VISIBLE) {
                            start();
                        }
                        onLoad();
                    }
                });
            }
        });

    }


    public void start() {
        if (mDrawable != null) {
            setImageDrawable(mDrawable);
            mDrawable.start();
        }
    }

    public void stop() {
        if (mDrawable != null) {
            mDrawable.stop();
        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (changedView == this) {
            if (visibility == VISIBLE) {
                start();
            } else {
                stop();
            }
        }
    }

    public void setOnLoadListener(OnLoadListener loadListener) {
        mOnLoadListener = loadListener;
    }

    private void onLoad() {
        if (mOnLoadListener != null) {
            mOnLoadListener.onLoaded();
        }
    }

    public interface OnLoadListener {
        void onLoaded();
    }
}
