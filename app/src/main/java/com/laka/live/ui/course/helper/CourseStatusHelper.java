package com.laka.live.ui.course.helper;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import com.laka.live.bean.Course;
import com.laka.live.util.TimeUtil;
import com.laka.live.util.Utils;

/**
 * Created by Lyf on 2017/9/27.
 */
public class CourseStatusHelper {

    private CountDownTimer mCountDownTimer;

    // 统一设置课程的时间和状态(兼容所有带有复用的列表)
    public void setCourseStatus(final Course course, Chronometer mTimeTv,
                                TextView mStatusTv, View mWaveView, View mDivider) {

        // 重置状态
        mTimeTv.stop(); // 停止直播计时
        mWaveView.setVisibility(View.GONE); // 停止直播动画
        mDivider.setVisibility(View.VISIBLE); // 显示分割线
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel(); // 停止计时
        }

        // 资讯类比较特殊
        if (course.isNews() || Utils.isEmpty(course.getStatus_text()) || " null".equals(course.getStatus_text())) {
            mStatusTv.setText("发布时间");
            mTimeTv.setText(course.getFormatStartTime()); // 设置课程时间
            return;
        }

        // 绑定新数据
        mTimeTv.setText(course.getFormatStartTime()); // 设置课程时间
        mStatusTv.setText(course.getStatus_text()); // 设置课程状态

        //  开播倒计时
        if (TimeUtil.isToday(course.start_time * 1000) && course.isLive()
                && (course.status == Course.NOTSTART || course.status == Course.ALREADY)) {
            mStatusTv.setText("");
            mDivider.setVisibility(View.GONE);
            setCountDownTimer(course.start_time, mTimeTv);
        } else if (course.status == Course.LIVING) {
            mWaveView.setVisibility(View.VISIBLE);
            mTimeTv.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer ch) {
                    long livingTime = System.currentTimeMillis() - course.getBeginTime() * 1000;
                    ch.setText(TimeUtil.getTimeWithStr(livingTime / 1000));
                }
            });
            mTimeTv.start();
        } else if (course.status == Course.CHANGETIME || course.status == Course.CANCEL) {
            mTimeTv.setText(""); // 开播时间调整,就不用显示开播时间了
            mDivider.setVisibility(View.GONE);
        }

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
