package com.laka.live.shopping.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.laka.live.R;
import com.laka.live.util.HardwareUtil;
import com.laka.live.util.ResourceHelper;

/**
 * Created by zhxu on 2016/3/9.
 * Email:357599859@qq.com
 */
public class ShoppingViewGroup extends ViewGroup {

    public void setChildCount(int mChildCount) {
        this.mChildCount = mChildCount;
    }

    private int mChildCount = 0;

    public ShoppingViewGroup(Context context) {
        this(context, null);
    }

    public ShoppingViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShoppingViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        mChildCount = mChildCount / 2 + mChildCount % 2;
        int height = mChildCount * (HardwareUtil.screenWidth / 2 * 4 / 9);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        int margin = ResourceHelper.getDimen(R.dimen.space_1);
        int viewGroupWidth = getMeasuredWidth() - margin;

        int mPainterPosX = left;
        int mPainterPosY = 0;

        int childCount = getChildCount();
        int width = viewGroupWidth / 2, height = width * 4 / 9;

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);

            if (mPainterPosX + width > viewGroupWidth) {
                mPainterPosX = left;
                mPainterPosY += height + margin;
            }

            childView.measure(width, height);
            childView.layout(mPainterPosX + (i % 2 == 0 ? 0 : margin), mPainterPosY, mPainterPosX + width, mPainterPosY + height);
            mPainterPosX += width;
        }
    }
}