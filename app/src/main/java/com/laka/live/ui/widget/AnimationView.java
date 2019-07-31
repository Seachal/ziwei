package com.laka.live.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import com.laka.live.R;
import com.laka.live.thread.BackgroundThread;

public class AnimationView extends AppCompatImageView {

    private int AnimationDrawableId;
    private AnimationDrawable mDrawable;
    private OnLoadListener mOnLoadListener;

    public AnimationView(Context context) {
        super(context);
        init(context, null);
    }

    public AnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AnimationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnimationView);
            AnimationDrawableId = typedArray.getResourceId(R.styleable.AnimationView_drawable_id, 0);
            final Handler handler = new Handler(Looper.getMainLooper());
            BackgroundThread.post(new Runnable() {
                @Override
                public void run() {
                    mDrawable = (AnimationDrawable) getResources().getDrawable(AnimationDrawableId);
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
            typedArray.recycle();
        }

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
