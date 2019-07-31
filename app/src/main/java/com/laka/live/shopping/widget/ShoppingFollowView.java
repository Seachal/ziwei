package com.laka.live.shopping.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.laka.live.R;
import com.laka.live.util.HardwareUtil;
import com.laka.live.util.ResourceHelper;


/**
 * Created by zhxu on 2016/4/27.
 * Email:357599859@qq.com
 */
public class ShoppingFollowView extends View {

    private Drawable mDrawable;
    private int mStartX, mStartY;

    private Paint mPaint;

    private int mLineWidth, mLineStartX, mLineStartY;

    private float[] mTranslate = new float[3];

    private int mCurrPosition = 0, mLastPosition = 0;
    private float mCurrX = 0;

    private int mSpeed = 0;

    private boolean mIsStop = false, mIsStart = false;

    private View[] mView;

    private int mDelayMillis = 4000;

    public ShoppingFollowView(Context context) {
        this(context, null);
    }

    public ShoppingFollowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShoppingFollowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setViews(View[] mView) {
        this.mView = mView;
    }

    private void init() {
        mDrawable = ResourceHelper.getDrawable(R.drawable.shopping_cell_explanation);
        mDrawable.setBounds(0, 0, mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight());

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#E6E6E6"));
        mPaint.setStrokeWidth(1.5f);
    }

    public void startAnimation() {
        removeCallbacks(mRunnable);
        postDelayed(mRunnable, mDelayMillis);
    }

    public void stopAnimation() {
        removeCallbacks(mRunnable);
    }

    public int getCurrPosition() {
        return mCurrPosition;
    }

    public void setCurrPosition(int mCurrPosition) {
        if (mIsStart) {
            return;
        }
        this.mCurrPosition = mCurrPosition;
        mIsStop = false;
        postDelayed(mCountdownRunnable, 10);
        hideAllViews(mView[mCurrPosition]);
    }

    private void hideAllViews(View _view) {
        for (View view : mView) {
            view.setVisibility(GONE);
        }
        _view.setVisibility(VISIBLE);
    }

    private void animation() {
        mIsStart = true;
        mSpeed++;
        float translateLast = mTranslate[mLastPosition];
        float translate = translateLast - mTranslate[mCurrPosition];
        if (translate == 0) {
            mIsStop = true;
            return;
        }
        if (translate < 0) {
            if (translateLast + Math.abs(translate) > mCurrX) {
                mCurrX = (float) (mCurrX + (1.2 * mSpeed));
            } else {
                mCurrX = translateLast + Math.abs(translate);
                mIsStop = true;
            }
        } else {
            if (translateLast - translate < mCurrX) {
                mCurrX = (float) (mCurrX - (1.2 * mSpeed));
            } else {
                mCurrX = translateLast - translate;
                mIsStop = true;
            }
        }
        invalidate();
    }

    private void stop() {
        mSpeed = 0;
        removeCallbacks(mCountdownRunnable);
        mLastPosition = mCurrPosition;
        mCurrX = mTranslate[mLastPosition];
        mIsStart = false;
    }

    private Runnable mCountdownRunnable = new Runnable() {
        @Override
        public void run() {
            animation();
            if (!mIsStop) {
                postDelayed(mCountdownRunnable, 10);
            } else {
                stop();
            }
        }
    };

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            int position = getCurrPosition();
            if (position == mView.length - 1) {
                position = 0;
            } else {
                position++;
            }
            setCurrPosition(position);
            if (!mIsStop) {
                startAnimation();
            } else {
                stopAnimation();
            }
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawLine(mLineStartX, mLineStartY, mLineStartX + mLineWidth, mLineStartY, mPaint);

        canvas.translate(mCurrX, mStartY);
        mDrawable.draw(canvas);
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);

        int itemsWidth = ResourceHelper.getDimen(R.dimen.space_90);

        mLineStartX = (HardwareUtil.screenWidth - itemsWidth * 3) / 6;
        mLineStartY = height - mLineStartX;
        mLineWidth = width - mLineStartX * 2;

        mStartX = ((itemsWidth + mLineStartX * 2) / 2) - (mDrawable.getIntrinsicWidth() / 2);
        mStartY = mLineStartY - mDrawable.getIntrinsicHeight() + ResourceHelper.getDimen(R.dimen.divider_height);

        mCurrX = mStartX;
        mTranslate[0] = mCurrX;
        mTranslate[1] = (mLineWidth / 2) - (mDrawable.getIntrinsicWidth() / 2) + mLineStartX;
        mTranslate[2] = (mLineWidth / 6) * 4 + mStartX + mLineStartX;
    }
}
