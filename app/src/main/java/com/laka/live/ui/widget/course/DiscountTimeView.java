package com.laka.live.ui.widget.course;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.bean.Course;
import com.laka.live.bean.CourseDetail;
import com.laka.live.ui.course.detail.CourseDetailHelper;
import com.laka.live.ui.widget.AnimationView;
import com.laka.live.util.Common;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ThreadManager;
import com.laka.live.util.TimeUtil;
import com.laka.live.util.ViewUtils;

/**
 * Created by Lyf on 2017/8/29.
 */
public class DiscountTimeView extends RelativeLayout {

    private long[] dates = null;
    private View mRootView, discountTimeLayout;
    private TextView buyerCount;
    private Chronometer courseTime;
    private AnimationView animation;
    private TextView limitTextView;
    private CountDownTimer mCountDownTimer, mDiscountTimer;
    private TextView[] dateTv = new TextView[4];

    public DiscountTimeView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public DiscountTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public DiscountTimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        mRootView = LayoutInflater.from(context).inflate(R.layout.view_discount_time, this);
        dateTv[0] = ViewUtils.findById(mRootView, R.id.daysTv);
        dateTv[1] = ViewUtils.findById(mRootView, R.id.hoursTv);
        dateTv[2] = ViewUtils.findById(mRootView, R.id.minutesTv);
        dateTv[3] = ViewUtils.findById(mRootView, R.id.secondsTv);
        buyerCount = ViewUtils.findById(mRootView, R.id.buyerCount);
        animation = ViewUtils.findById(mRootView, R.id.animation);
        courseTime = ViewUtils.findById(mRootView, R.id.courseTime);
        limitTextView = ViewUtils.findById(mRootView, R.id.limitTextView);
        discountTimeLayout = ViewUtils.findById(mRootView, R.id.discountTimeLayout);

    }

    // 绑定开播倒计时数据
    public void setStartLiveCountDownData(final Course course, final long beginTime) {
        if (course == null) {
            return;
        }
        animation.setVisibility(View.GONE);
        buyerCount.setText(course.getBuyer_count());
        if (course.status == Course.LIVING) {
            animation.setVisibility(View.VISIBLE);
            courseTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer ch) {
                    long livingTime = System.currentTimeMillis() - beginTime * 1000;
                    ch.setText(course.getStatus_text() + " " + TimeUtil.getTimeWithStr(livingTime / 1000));
                }
            });
            courseTime.start();
        } else if (course.status == Course.CHANGETIME) {
            courseTime.setText(course.getStatus_text());
        } else {
            // 倒计时
            if (TimeUtil.isToday(course.start_time * 1000) && course.isLive()
                    && (course.status == Course.NOTSTART || course.status == Course.ALREADY)) {
                setCountDownTimer(course.start_time);
            } else {
                courseTime.setText(course.getStatus_text() + " " + course.getFormatStartTime());
            }

        }
    }

    // 绑定优惠倒计时数据
    public void setDiscountTimeData(final CourseDetailHelper helper) {

        if (helper.getCourseDetail() == null) {
            return;
        }

        CourseDetail courseDetail = helper.getCourseDetail();
        boolean hasSimilar = courseDetail.hasSimilarCourse();

        if (courseDetail.course.getPrice() == 0 ||
                (!courseDetail.hasSimilarCourse()) && courseDetail.course_trailer.savedCoins > 0) {
            mRootView.setPadding(0, 0, 0, 0);
        } else {
            mRootView.setPadding(0, 0, 0, ResourceHelper.getDimen(R.dimen.space_4));
        }

        long millisInFuture = courseDetail.course_trailer.getDiscountRemainingTime() * 1000;

        // 单节课才显示
        if (millisInFuture > 0 && !hasSimilar) {
            discountTimeLayout.setVisibility(VISIBLE);
            setDiscountTimer(helper, millisInFuture);
            Drawable drawable;
            if (courseDetail.course_trailer.getTime_limit_type() == Common.TIME_LIMIT_TYPE_FREE) {
                drawable = ResourceHelper.getDrawable(R.drawable.public_label_free2);
            } else {
                drawable = ResourceHelper.getDrawable(R.drawable.public_label_sale2);
            }
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            limitTextView.setCompoundDrawables(drawable, null, null, null);
        } else {
            discountTimeLayout.setVisibility(GONE);
        }

    }

    // 设置开播倒计时
    private void setCountDownTimer(long startTime) {

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

        long millisInFuture = startTime * 1000 - System.currentTimeMillis();

        if (millisInFuture <= 0) {
            courseTime.setText("直播准备中");
            return;
        }

        mCountDownTimer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                courseTime.setText("倒计时 " + TimeUtil.getTimeWithStr(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                courseTime.setText("直播准备中");
            }
        };

        mCountDownTimer.start();
    }

    // 设置优惠倒计时
    private void setDiscountTimer(final CourseDetailHelper helper, long millisInFuture) {

        if (mDiscountTimer != null) {
            mDiscountTimer.cancel();
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

                dates = TimeUtil.getDiscountTime(dates, timestamp);

                for (int i = 0; i < dateTv.length; ++i) {
                    String text = "";
                    if (dates[i] < 10) {
                        text = "0";
                    }
                    text += String.valueOf(dates[i]);
                    if (!dateTv[i].getText().equals(text)) {
                        refreshTime(activity, i, text);
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
    private void refreshTime(final Activity activity, final int index, final String text) {

        if (activity == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dateTv[index].setText(text);
            }
        });

    }

    /**
     * 退出时要退掉倒计时
     */
    public void onDestroy() {

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        if (mDiscountTimer != null) {
            mDiscountTimer.cancel();
        }

    }

    //  seconds = ViewUtils.findById(view, R.id.seconds);
    //  minutes = ViewUtils.findById(view, R.id.minutes);
    //  hours = ViewUtils.findById(view, R.id.hours);
    //  days = ViewUtils.findById(view, R.id.days);
    //  days.setVisibility(Integer.valueOf(dates[0] + dates[1]) > 0 ? VISIBLE : GONE);
    //  hours.setVisibility((Integer.valueOf(dates[2] + dates[3]) > 0 || days.getVisibility() == VISIBLE) ? VISIBLE : GONE);
    //  minutes.setVisibility((Integer.valueOf(dates[4] + dates[5]) > 0 || hours.getVisibility() == VISIBLE) ? VISIBLE : GONE);

}
