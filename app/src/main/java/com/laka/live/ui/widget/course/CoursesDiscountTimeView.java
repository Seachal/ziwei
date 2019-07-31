package com.laka.live.ui.widget.course;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.ui.course.detail.CourseDetailHelper;
import com.laka.live.util.NumberUtils;
import com.laka.live.util.ThreadManager;
import com.laka.live.util.TimeUtil;
import com.laka.live.util.ViewUtils;

/**
 * Created by Lyf on 2017/8/29.
 */

public class CoursesDiscountTimeView extends RelativeLayout {

    private long[] dates = null;
    private TextView[] dateTv = new TextView[8];
    private TextView mDiscountTimeTv;
    private CountDownTimer mDiscountTimer;

    public CoursesDiscountTimeView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CoursesDiscountTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CoursesDiscountTimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        View view = LayoutInflater.from(context).inflate(R.layout.view_courses_discount_time, this);
        dateTv[0] = ViewUtils.findById(view, R.id.dFirst);
        dateTv[1] = ViewUtils.findById(view, R.id.dSecond);
        dateTv[2] = ViewUtils.findById(view, R.id.hFirst);
        dateTv[3] = ViewUtils.findById(view, R.id.hSecond);
        dateTv[4] = ViewUtils.findById(view, R.id.mFirst);
        dateTv[5] = ViewUtils.findById(view, R.id.mSecond);
        dateTv[6] = ViewUtils.findById(view, R.id.sFirst);
        dateTv[7] = ViewUtils.findById(view, R.id.sSecond);
        mDiscountTimeTv = ViewUtils.findById(view, R.id.discountTimeTv);
        setVisibility(GONE);
    }

    // 设置优惠倒计时
    public void setCoursesDiscountTimer(final CourseDetailHelper helper) {

        if (mDiscountTimer != null) {
            mDiscountTimer.cancel();
        }

        if (helper == null) {
            return;
        }

        float discount = helper.getDiscount(); // 折扣
        long millisInFuture = helper.getCourseTrailer().getDiscountRemainingTime() * 1000; // 倒计时

        if (millisInFuture > 0) {
            if (discount > 0) {
                mDiscountTimeTv.setText(NumberUtils.getCoursePriceFormat(discount, "折优惠距离结束还剩"));
            } else {
                mDiscountTimeTv.setText("限时免费距离结束还剩");
            }
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
            return;
        }

        mDiscountTimer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                setTimestamp(helper.getActivity(), millisUntilFinished);
            }

            @Override
            public void onFinish() {
                helper.getData(helper.getCourseId());
            }
        };

        mDiscountTimer.start();
    }

    /**
     * @param timestamp 毫秒
     */
    public void setTimestamp(final Activity activity, final long timestamp) {

        ThreadManager.execute(new Runnable() {
            @Override
            public void run() {
                dates = TimeUtil.getCoursesDiscountTime(dates, timestamp / 1000);

                for (int i = 0; i < dates.length; ++i) {

                    if (!dateTv[i].getText().equals(String.valueOf(dates[i]))) {
                        // 刷新控件时才在UI做
                        refreshTime(activity, i);
                    }

                }

            }
        });

    }

    /**
     * 在主线程刷新UI
     *
     * @param index 要刷新的控件的下标
     */
    private void refreshTime(final Activity activity, final int index) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dateTv[index].setText(String.valueOf(dates[index]));
            }
        });

    }

}
