package com.laka.live.ui.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.bean.Course;
import com.laka.live.util.NumberUtils;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.TimeUtil;
import com.laka.live.util.ViewUtils;

/**
 * Created by Lyf on 2017/6/10.
 */

public class CourseView extends LinearLayout {

    private View root;
    private Context mContext;
    private CountDownTimer mCountDownTimer;

    public CourseView(Context context, Course course) {
        super(context);
        initView(context, course);
    }

    public CourseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, null);
    }

    public CourseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, null);
    }

    private void initView(Context mContext, final Course course) {

        this.mContext = mContext;
        root = LayoutInflater.from(mContext).inflate(R.layout.item_course, this);
        // 获取控件
        Chronometer time = ViewUtils.findById(root, R.id.time);
        TextView title = ViewUtils.findById(root, R.id.title);
        TextView money = ViewUtils.findById(root, R.id.money);
        TextView textStatus = ViewUtils.findById(root, R.id.status);
        SimpleDraweeView userFace = ViewUtils.findById(root, R.id.userFace);
        // 绑定数据
        title.setText(course.getTitle());
        money.setText(NumberUtils.getCoursePriceFormat(course.getPrice(), "味豆"));
        textStatus.setText(course.getStatus_text());
        userFace.setImageURI(course.cover_url);
        time.setText(course.getFormatStartTime());

        //  开播倒计时
        if (TimeUtil.isToday(course.start_time * 1000) && course.isLive()
                && (course.status == Course.NOTSTART || course.status == Course.ALREADY)) {
            textStatus.setText("");
            ViewUtils.findById(root, R.id.divider).setVisibility(View.GONE);
            setCountDownTimer(course.start_time, time);
        }else if (course.status == Course.LIVING) {
            ViewUtils.findById(root, R.id.animation).setVisibility(View.VISIBLE);
            time.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer ch) {
                    long livingTime = System.currentTimeMillis() - course.getBeginTime() * 1000;
                    ch.setText(TimeUtil.getTimeWithStr(livingTime / 1000));
                }
            });
            time.start();
        } else if (course.status == Course.CHANGETIME) {
            time.setText("");
            ViewUtils.findById(root, R.id.divider).setVisibility(GONE);
        }

        if (course.isLive() && course.status <= Course.LIVING && course.status != Course.CANCEL) {
            ViewUtils.findById(root, R.id.live_iv).setVisibility(VISIBLE);
        }else{
            ViewUtils.findById(root, R.id.live_iv).setVisibility(GONE);
        }
    }

    // 设置价格控格的价格颜色
    public void setPriceColor(int color) {
        TextView money = ViewUtils.findById(root, R.id.money);
        money.setTextColor(ResourceHelper.getColor(color));
    }

    public void setCourseNum(int courseNum) {

        String courseNumText;
        if (courseNum < 9) {
            courseNumText = "0" + (courseNum + 1);
        } else {
            courseNumText = String.valueOf(courseNum + 1);
        }
        TextView courseNum1 = ViewUtils.findById(root, R.id.courseNum1);
        TextView courseNum2 = ViewUtils.findById(root, R.id.courseNum2);
        ViewUtils.findById(root, R.id.numLayout).setVisibility(View.VISIBLE);
        courseNum1.setText("课堂" + courseNumText);
        courseNum2.setText("SECTION" + courseNumText);

    }

    // 设置倒计时
    private void setCountDownTimer(long startTime, final Chronometer chronometer) {

        long millisInFuture = startTime * 1000 - System.currentTimeMillis();

        if (millisInFuture <= 0) {
            chronometer.setText("直播准备中");
            return;
        }

        if (mCountDownTimer != null) { // 停止倒计时
            mCountDownTimer.cancel();
        }

        mCountDownTimer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                chronometer.setText("倒计时 " + TimeUtil.getTimeWithStr(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                chronometer.setText("直播准备中");
            }
        };

        mCountDownTimer.start();
    }

}
