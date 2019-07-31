package com.laka.live.ui.widget.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.laka.live.R;
import com.laka.live.util.Utils;

/**
 * Created by luwies on 16/9/6.
 */
public class MusicLoading extends FrameLayout {

    private static final String TAG = "MusicLoading";

    private static final long DEFAULT_SCALE_DURATION = 1280L;

    private static final long DELAY = 100L;

    private Context mContext;

    private int mLineCounts = 10;

    private LinearLayout mContentLayout;

    private boolean isStart = false;

    private int mLineMargin;

    private int mBigLineWidth;

    private int mLittleLineWidth;

    private long mDuration;

    private int mLineColor;

    private AB mAB;

    private long mTotalDuration;

    private ValueAnimator mValueAnimator;

    private int mHalfHeight;

    private int mLineHeight;

    public MusicLoading(Context context) {
        super(context);
        init(context, null);
    }

    public MusicLoading(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MusicLoading(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;

        initAttrs(attrs);

        mTotalDuration = mDuration + DELAY * (mLineCounts - 1);

        setLineHeight(mContext.getResources().getDimensionPixelSize(R.dimen.music_loading_line_height));

        addContent();

        mValueAnimator = createScaleAnimator();

        startLoading();
    }

    private void initAttrs(AttributeSet attrs) {

        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.MusicLoading);
        if (ta != null) {
            mLineMargin = ta.getDimensionPixelSize(R.styleable.MusicLoading_margin,
                    Utils.dip2px(mContext, 8f));
            mBigLineWidth = ta.getDimensionPixelSize(R.styleable.MusicLoading_bigLineWidth,
                    Utils.dip2px(mContext, 3f));
            mLittleLineWidth = ta.getDimensionPixelSize(R.styleable.MusicLoading_littleLineWidth,
                    Utils.dip2px(mContext, 2.5f));
            mLineColor = ta.getColor(R.styleable.MusicLoading_lineColor,
                    ContextCompat.getColor(mContext, R.color.colorF65843));
            mDuration = ta.getInteger(R.styleable.MusicLoading_duration, (int) DEFAULT_SCALE_DURATION);

            ta.recycle();
        }

    }

    public void setLineHeight(int height) {
        mLineHeight = height;
        mHalfHeight = height / 2;
        mAB = calculateAB(0, 0, mDuration / 4, mHalfHeight);

        if (mContentLayout == null) {
            return;
        }
        int size = mContentLayout.getChildCount();
        View child;
        for (int i = 0; i < size; i++) {
            child = mContentLayout.getChildAt(i);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
            lp.height = height;
        }
        mContentLayout.requestLayout();
    }

    public void setLineMargin(int margin) {
        mLineMargin = margin;

        int size = mContentLayout.getChildCount();
        View child;
        for (int i = 0; i < size; i++) {
            child = mContentLayout.getChildAt(i);
            if (i != 0) {
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                lp.leftMargin = margin;
            }
        }
        mContentLayout.requestLayout();
    }

    public void setBigLineWidth(int width) {
        mBigLineWidth = width;
        setLineWidth();
    }

    public void setLittleLineWidth(int width) {
        mLittleLineWidth = width;
        setLineWidth();
    }

    private void setLineWidth() {
        int size = mContentLayout.getChildCount();
        View child;
        for (int i = 0; i < size; i++) {
            child = mContentLayout.getChildAt(i);
            setLineWidth(child, i);
        }
        mContentLayout.requestLayout();
    }

    public void setLineColor(int color) {
        mLineColor = color;
        int size = mContentLayout.getChildCount();
        View child;
        for (int i = 0; i < size; i++) {
            child = mContentLayout.getChildAt(i);
            child.setBackgroundColor(color);
        }
    }

    public void setDuration(long duration) {
        mDuration = duration;
        mTotalDuration = mDuration + DELAY * (mLineCounts - 1);
        mAB = calculateAB(0, 0, mDuration / 4, mHalfHeight);
    }

    private void setLineWidth(View child, int i) {
        int width;
        if (i < mLineCounts / 2) {
            width = i % 2 == 0 ? mBigLineWidth : mLittleLineWidth;
        } else {
            width = i % 2 != 0 ? mBigLineWidth : mLittleLineWidth;
        }
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        lp.width = width;
    }

    private void addContent() {
        mContentLayout = new LinearLayout(mContext);
        mContentLayout.setOrientation(LinearLayout.HORIZONTAL);

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        addView(mContentLayout, params);

        addLines(mContentLayout);
    }

    private void addLines(LinearLayout linearLayout) {

        for (int i = 0; i < mLineCounts; i++) {
            linearLayout.addView(createLine(i));
        }
    }

    private View createLine(int index) {
        View view = new View(mContext);
        int width;
        if (index <= mLineCounts / 2) {
            width = index % 2 == 0 ? mBigLineWidth : mLittleLineWidth;
        } else {
            width = index % 2 != 0 ? mBigLineWidth : mLittleLineWidth;
        }

        int height = mLineHeight;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);

        if (index != 0) {
            params.leftMargin = mLineMargin;
        }

        view.setLayoutParams(params);
        view.setBackgroundColor(mLineColor);

        return view;
    }

    private ValueAnimator createScaleAnimator() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                long currentTime = animation.getCurrentPlayTime();
                int size = mContentLayout.getChildCount();
                View target;
                for (int i = 0; i < size; i++) {
                    target = mContentLayout.getChildAt(i);
                    updateTarget(target, currentTime, i);
                }

            }
        });
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration(mTotalDuration);
        return valueAnimator;
    }

    private void updateTarget(View target, long curentTime, int index) {
        long time = curentTime % mTotalDuration;
        long offset = index * DELAY;
        int h = getCurrentHeight(time - offset);

        target.setTop(h);
        target.setBottom(mHalfHeight * 2 - h);

    }

    private int getCurrentHeight(long currentTime) {

        int h;

        int maxHeight = mHalfHeight;
        if (currentTime <= 0) {
            h = 0;
        } else if (currentTime < mDuration) {
            long t = currentTime % (mDuration / 4);
            h = (int) (mAB.a * t + mAB.b);
            if (currentTime < mDuration / 4) {

            } else if (currentTime < mDuration / 2) {
                h = maxHeight - h;
            } else if (currentTime < mDuration * 3 / 4) {

            } else {
                h = maxHeight - h;
            }
        } else {
            h = 0;
        }
        return h;
    }

    private AB calculateAB(long t1, int y1, long t2, int y2) {
        AB ab = new AB();
        ab.a = (y1 - y2) / (double) (t1 - t2);
        ab.b = y1 - ab.a * t1;
        return ab;
    }

    public long getTotalDuration() {
        return mTotalDuration;
    }

    public void startLoading() {

        if (isStart) {
            return;
        }

        mValueAnimator.start();

        isStart = true;
    }

    public void stopLoading() {

        mValueAnimator.cancel();
        isStart = false;
        reset();
    }

    public void reset() {
        int size = mContentLayout.getChildCount();
        View child;
        for (int i = 0; i < size; i++) {
            child = mContentLayout.getChildAt(i);
            child.setTop(0);
            child.setBottom(mLineHeight);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (VISIBLE == visibility) {
            startLoading();
        } else {
            stopLoading();
        }
    }


    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (changedView == this) {
            if (visibility == VISIBLE) {
                startLoading();
            } else {
                stopLoading();
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startLoading();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopLoading();
    }

    class AB {
        double a;
        double b;
    }
}
