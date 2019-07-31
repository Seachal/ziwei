package com.laka.live.gift;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.laka.live.R;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.util.Log;
import com.laka.live.util.Util;
import com.laka.live.util.Utils;

/**
 * 定制特效进度动画的ProgressBar
 * Created by guan
 */
public class AnimProgressBar extends ProgressBar {
    private static final String TAG = "AnimProgressBar";
    private Paint mPaint;
    private int mWidth;
    /**
     * 要绘制的bitmap
     */
    private Bitmap mBitmap;

    private Bitmap mBitmapBuffer1;
    private Bitmap mBitmapBuffer2;
    /**
     * 执行动画Runnable
     */
//    private FirstRunnable mFirstRunnable;
//    private SecondRunnable mSecondRunnable;
    /**
     * 第二帧动画位移
     */
    private int mTranslate = 0;


    public AnimProgressBar(Context context) {
        this(context, null);
        init();
    }

    public AnimProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.setHorizontalScrollBarEnabled(true);
        this.setMax(20);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.setProgressDrawable(getResources().getDrawable(R.drawable.anim_progress_bg));//
        this.setBackgroundResource(R.drawable.live_special_bar_bg);//anim_progressbar_bg
//        mFirstRunnable = new FirstRunnable();
//        mSecondRunnable = new SecondRunnable();

        setPadding(Utils.dip2px(getContext(), 3), Utils.dip2px(getContext(), 3), Utils.dip2px(getContext(), 3), Utils.dip2px(getContext(), 3));
//        mBitmapBuffer1 = ((BitmapDrawable) getResources().getDrawable(R.drawable.pb_anim_pic1)).getBitmap();
//        mBitmapBuffer2 = ((BitmapDrawable) getResources().getDrawable(R.drawable.pb_anim_pic2)).getBitmap();
//        setPadding(getPaddingLeft(), mBitmapBuffer1.getHeight() / 2, getPaddingRight(), mBitmapBuffer1.getHeight() / 2);
    }


    @Override
    public synchronized void setMax(int max) {
        super.setMax(max);
        //todo 设置闪电位置

    }

    /**
     * @param index
     */
    private void setDrawable(int index) {
        if (index == 1) {
            mTranslate = 0;
            mBitmap = mBitmapBuffer1;
        } else {
            mTranslate = Utils.dip2px(getContext(), 4);
            mBitmap = mBitmapBuffer2;
        }
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap == null) {
            return;
        }
        float radio = getProgress() * 1.0f / getMax();
        float progressPos = mWidth * radio;
        canvas.save();
        canvas.translate(getPaddingLeft(), getHeight() / 2);
        float x = progressPos + mTranslate - mBitmap.getWidth() / 2;
        // 不做小于0判断
        if (x > mWidth - mBitmap.getWidth() / 2) {
            x = mWidth - mBitmap.getWidth() / 2;
        }
//        canvas.drawBitmap(mBitmap, x, -mBitmap.getHeight() / 2, mPaint);
        canvas.restore();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 默认pic1
//        setDrawable(1);
//        postDelayed(mFirstRunnable, 300);
    }

    /**
     * 更新动画
     *
     * @param
     */
//    private void updateAnim(int index) {
//        setDrawable(index);
//        invalidate();
//        postDelayed(index == 1 ? mFirstRunnable : mSecondRunnable, 300);
//    }
    public void setBitmapBuffer1(Bitmap bitmap) {
        mBitmapBuffer1 = bitmap;
    }

    public void setBitmapBuffer2(Bitmap bitmap) {
        this.mBitmapBuffer2 = bitmap;
    }

    public void setAmount(int count) {
        curAmount = count;
        refreshProgress();
    }

//    public void addProgress(int i) {
//        curAmount +=i;
//        refreshProgress();
//    }

    private void refreshProgress() {
        int progress = getProgress();
        if (curAmount == tag1) {
            Log.d(TAG, "达到1阶段动画");
        } else if (curAmount == tag2) {
            Log.d(TAG, "达到2阶段动画");
        } else if (curAmount == tag3) {
            Log.d(TAG, "达到3阶段动画");
        }

        if (curAmount <= tag1) {
            setProgress(step1 * curAmount);
            Log.d(TAG, " curProgress=" + getProgress() + " curAmount=" + curAmount + " step1");
        } else if (curAmount <= tag2) {
            setProgress(five + (step2 * (curAmount - tag1)));
            Log.d(TAG, " curProgress=" + getProgress() + " curAmount=" + curAmount + " step2");
        } else {
            int part = five * 3 + (step3 * (curAmount - tag2));
            if (part < getMax()) {
                setProgress(part);
            } else {
                setProgress(getMax());
            }
            Log.d(TAG, " curProgress=" + getProgress() + " curAmount=" + curAmount + " step3");
        }
    }

    int curAmount = 0;
    int tag1 = 0;
    int tag2 = 0;
    int tag3 = 0;
    int total = 0;
    int step1 = 0;
    int step2 = 0;
    int step3 = 0;
    //    int tag1 = 99;
//    int tag2 = 520;
//    int tag3 = 1314;
//    int total = tag1 * tag2 * tag3;
//    int step1 = total / 5 / tag1;
//    int step2 = total / 5 / tag2*2;
//    int step3 = total / 5 / tag3*2;
    int five;

    public void initProgress(int tag1, int tag2, int tag3) {
        curAmount = 0;
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.tag3 = tag3;
        total = tag1 * tag2 * tag3;
        five = total / 5;
        step1 = five / tag1;
        step2 = five / (tag2 - tag1) * 2;
        step3 = five / (tag3 - tag2) * 2;
        Log.d(TAG, "total=" + total + " step1=" + step1 + " step2=" + step2 + " step3=" + step3);
        setMax(total);
        setProgress(0);
    }


//    class FirstRunnable implements Runnable {
//
//        @Override
//        public void run() {
//            updateAnim(2);
//        }
//    }
//
//    class SecondRunnable implements Runnable {
//
//        @Override
//        public void run() {
//            updateAnim(1);
//        }
//    }
}
