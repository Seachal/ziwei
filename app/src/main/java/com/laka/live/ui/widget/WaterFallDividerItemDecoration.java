package com.laka.live.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Created by chuan on 16/12/6.
 */
public class WaterFallDividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private boolean mShowTopDivider = false;

    public WaterFallDividerItemDecoration(Context context, int resId) {
        this(context, resId, false);
    }

    public WaterFallDividerItemDecoration(Context context, int resId, boolean showTopDivider) {
        mDivider = ContextCompat.getDrawable(context, resId);
        mShowTopDivider = showTopDivider;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
//        drawVertical(c, parent);
//        drawHorizontal(c, parent);
    }

    /**
     * 绘制垂直分割线
     */
    private void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    /**
     * 绘制横向分割线
     */
    private void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.left = 0;
        if (mShowTopDivider) {
            outRect.top = mDivider.getIntrinsicHeight();
        } else {
            outRect.bottom = mDivider.getIntrinsicHeight();
        }
        outRect.right = mDivider.getIntrinsicWidth();
    }
}
