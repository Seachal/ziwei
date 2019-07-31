package com.laka.live.ui.widget.course;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.bean.Course;
import com.laka.live.util.Common;
import com.laka.live.util.NumberUtils;
import com.laka.live.util.ViewUtils;

/**
 * Created by Lyf on 2017/9/1.
 */

public class CoursePriceView extends LinearLayout {

    private View mRootView, mTipText, mPriceLayout;
    private TextView mCoursePrice, mOriginalPrice;

    public CoursePriceView(Context context) {
        super(context);
        initView(context);
    }

    public CoursePriceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CoursePriceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {

        mRootView = LayoutInflater.from(context).inflate(R.layout.view_course_price, this);
        mTipText = ViewUtils.findById(mRootView, R.id.tipText);
        mPriceLayout = ViewUtils.findById(mRootView, R.id.priceLayout);
        mCoursePrice = ViewUtils.findById(mRootView, R.id.coursePrice);
        mOriginalPrice = ViewUtils.findById(mRootView, R.id.originalPrice);

    }

    /**
     * @param course      课程bean
     * @param saved_coins 优惠的价格
     * @param hasSimilar  是否有套课
     */
    public void initData(Course course, float saved_coins, boolean hasSimilar, int limitType) {

        int textSize;
        int visibility;
        Typeface typeface;

        // 价格大于0，才显示mTipText以及显示特殊字体
        if (course.getPrice() > 0 && ((limitType != Common.TIME_LIMIT_TYPE_FREE) || hasSimilar)) {
            textSize = 30;
            visibility = VISIBLE;
            typeface = ViewUtils.getTypeface(getContext(), "BlackJack.TTF");
        } else {
            textSize = 14;
            visibility = GONE;
            typeface = Typeface.DEFAULT;
        }

        if (hasSimilar) {
            saved_coins = 0; // 套课只显示原价，所以优惠的价格重置为0
            mOriginalPrice.setVisibility(GONE);
        } else {
            // 只有一节课并有优惠才显示原价。
            if (saved_coins > 0 && (limitType != Common.TIME_LIMIT_TYPE_FREE)) {
                mOriginalPrice.setVisibility(View.VISIBLE);
                mOriginalPrice.setText(NumberUtils.splitDoubleStr("原价", course.getPrice()));
                ViewUtils.setPartTextDeleteLine(mOriginalPrice, 2, mOriginalPrice.getText().length());
            } else {
                mOriginalPrice.setVisibility(View.GONE);
            }
        }

        // 设置价格
        if (course.getPrice() - saved_coins == 0) {
            textSize = 14;
            visibility = GONE;
            typeface = Typeface.DEFAULT;
        }

        // 设置样式
        mTipText.setVisibility(visibility);
        mCoursePrice.setTypeface(typeface);
        mCoursePrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

        mCoursePrice.setText(NumberUtils.getCoursePriceFormat(course.getPrice() - saved_coins));

    }

}
