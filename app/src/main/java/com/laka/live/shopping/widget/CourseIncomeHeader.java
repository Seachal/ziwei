package com.laka.live.shopping.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.util.ResourceHelper;

/**
 * @ClassName: CourseIncomeHeaderView
 * @Description: 课程收入头部
 * @Author: chuan
 * @Version: 1.0
 * @Date: 25/07/2017
 */

public class CourseIncomeHeader extends LinearLayout {
    private TextView mTotalTv;
    private TextView mLiveTv;
    private TextView mVideoTv;
    private TextView mAgentTv;

    public CourseIncomeHeader(Context context) {
        this(context, null);
    }

    public CourseIncomeHeader(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CourseIncomeHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI(context);
    }

    private void initUI(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_course_income_header, this);
        mTotalTv = (TextView) view.findViewById(R.id.total_tv);
        mLiveTv = (TextView) view.findViewById(R.id.live_tv);
        mVideoTv = (TextView) view.findViewById(R.id.video_tv);
        mAgentTv = (TextView) view.findViewById(R.id.agent_tv);

        setOrientation(VERTICAL);
    }

    public void updateData(String totalIncome, String liveIncome, String videoIncome, String agentIncome) {
        mTotalTv.setText(ResourceHelper.getString(R.string.income_money, totalIncome));
        mLiveTv.setText(ResourceHelper.getString(R.string.income_money, liveIncome));
        mVideoTv.setText(ResourceHelper.getString(R.string.income_money, videoIncome));
        mAgentTv.setText(ResourceHelper.getString(R.string.income_money, agentIncome));
    }
}
