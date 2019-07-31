package com.laka.live.shopping.widget;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

import com.laka.live.util.ResourceHelper;


/**
 * 倒计时View
 * Created by linhz on 2016/1/2.
 * Email: linhaizhong@ta2she.com
 */
public class ShoppingCountdownView extends TextView {
    private long mStartTime;

    private boolean mStop = false;

    private int mResID = 0;

    // 倒计时完成后，是否停止倒计时，为了不影响其他地方的使用所以添加该标识
    private boolean mStopWhenCountdownFinish = false;

    // 倒计时完成后显示的文案
    private String mFinishTxt = "";

    public ShoppingCountdownView(Context context) {
        super(context);
    }

    public ShoppingCountdownView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShoppingCountdownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setStopWhenCountdownFinish(boolean stopWhenCountdownFinish) {
        mStopWhenCountdownFinish = stopWhenCountdownFinish;
    }

    public void setFinishTxt(String finishTxt) {
        mFinishTxt = finishTxt;
    }

    public void setStartTime(long time) {
        setStartTime(time, 0);
    }

    public void setStartTime(long time, int resId) {
        mStartTime = time;
        mResID = resId;
        changeText();
    }

    public void startCountdown() {
        mStop = false;
        removeCallbacks(mCountdownRunnable);
        postDelayed(mCountdownRunnable, 1000);
    }

    public void stopCountdown() {
        mStop = true;
        removeCallbacks(mCountdownRunnable);
    }

    private void changeText() {
        // 判断倒计时是否已经小于0，如果小于0，则停止倒计时
        if (mStartTime < 0 && mStopWhenCountdownFinish) {
            stopCountdown();
            setText(mFinishTxt);
            return;
        }
        if (mResID > 0) {
            updateCountdownText(mResID);
        } else {
            updateCountdownText();
        }
    }

    private Runnable mCountdownRunnable = new Runnable() {
        @Override
        public void run() {
            mStartTime -= 1;
            changeText();
            if (!mStop) {
                postDelayed(mCountdownRunnable, 1000);
            }
        }
    };

    private void updateCountdownText() {
        long seconds = mStartTime % 60;
        long mins = (mStartTime / 60) % 60;
        long hours = (mStartTime / 3600) % 24;
        long day = mStartTime / 3600 / 24;
        StringBuilder builder = new StringBuilder();
        if (day > 0) {
            builder.append(day).append("天 ");
        }
        builder.append(String.format("%02d", hours));
        builder.append(":");
        builder.append(String.format("%02d", mins));
        builder.append(":");
        builder.append(String.format("%02d", seconds));

        setText(builder.toString());
    }

    private void updateCountdownText(int resId) {
        long seconds = mStartTime % 60;
        long mins = (mStartTime / 60) % 60;
        long hours = (mStartTime / 3600) % 24;
        long day = mStartTime / 3600 / 24;
        StringBuilder builder = new StringBuilder();
        if (day > 0) {
            builder.append(day).append("天 ");
        }
        builder.append(String.format("%02d", hours));
        builder.append(":");
        builder.append(String.format("%02d", mins));
        builder.append(":");
        builder.append(String.format("%02d", seconds));

        setText(Html.fromHtml(String.format(ResourceHelper.getString(resId), builder.toString())));
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopCountdown();
    }
}
