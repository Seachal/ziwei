package com.laka.live.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.laka.live.R;

import java.util.ArrayList;

/**
 * Created by luwies on 16/3/21.
 */
public class TagLayout extends ViewGroup {

    private int mHorizontalDividerWidth;

    private int mVerticalDividerWidth;

    public TagLayout(Context context) {
        this(context, null);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Context context = getContext();
        mHorizontalDividerWidth = context.getResources()
                .getDimensionPixelSize(R.dimen.tag_cloud_horizontal_diveder);
        mVerticalDividerWidth = context.getResources()
                .getDimensionPixelSize(R.dimen.tag_cloud_vertical_diveder);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int curWidth, curHeight, curLeft, curTop, maxHeight;

        //get the available size of child view
        final int childLeft = this.getPaddingLeft();
        final int childTop = this.getPaddingTop();

        final int childRight = this.getMeasuredWidth() - this.getPaddingRight();
        final int childBottom = this.getMeasuredHeight() - this.getPaddingBottom();

        final int childWidth = childRight - childLeft;
        final int childHeight = childBottom - childTop;

        maxHeight = 0;
        curLeft = childLeft;
        curTop = childTop;

        ArrayList<View> line = null;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE)
                return;

            //Get the maximum size of the child
            child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST));
            curWidth = child.getMeasuredWidth();
            curHeight = child.getMeasuredHeight();

            //store the max height
            if (maxHeight < curHeight)
                maxHeight = curHeight;

            if (curLeft == childLeft) {
                //每行第一个
                line = new ArrayList<>();
            } else {
                curLeft += mHorizontalDividerWidth;
            }


            if (curLeft + curWidth >= childRight) {

                if (line.isEmpty() || curLeft + curWidth == childRight) {
                    //这是该行第一个，或者是最后一个
                    line.add(child);
                    //layout 这一行
                    layoutLine(childLeft, childRight, curTop, line);
                    curLeft = childLeft;
                } else {
                    //layout 这一行
                    layoutLine(childLeft, childRight, curTop, line);
                    //新的一行

                    line = new ArrayList<>();
                    line.add(child);
                    curLeft = childLeft + curWidth;

                    if (i == count - 1) {
                        curTop += maxHeight + mVerticalDividerWidth;
                        layoutLine(childLeft, childRight, curTop, line);
                        break;
                    }
                }
                //然后就换行
                curTop += maxHeight + mVerticalDividerWidth;
                maxHeight = 0;
            } else {
                line.add(child);
                if (i == count - 1) {
                    layoutLine(childLeft, childRight, curTop, line);
                } else {
                    curLeft += curWidth;
                }
            }


            //do the layout
//            child.layout(curLeft, curTop, curLeft + curWidth, curTop + curHeight);

//            curLeft += curWidth;
        }
    }

    private void layoutLine(int childLeft, int childRight, int curTop, ArrayList<View> line) {
        int maxWidth = childRight - childLeft;
        int totalWidth = 0;
        int count = line.size();
        for (int i = 0; i < count; i++) {
            View child = line.get(i);
            totalWidth += child.getMeasuredWidth();
            if (i != 0) {
                totalWidth += mHorizontalDividerWidth;
            }
        }
        int curLeft = childLeft + (maxWidth - totalWidth) / 2;

        for (int i = 0; i < count; i++) {
            View child = line.get(i);
            int curWidth = child.getMeasuredWidth();
            int curHeight = child.getMeasuredHeight();
            if (i != 0) {
                curLeft += mHorizontalDividerWidth;
            }
            child.layout(curLeft, curTop, curLeft + curWidth, curTop + curHeight);
            curLeft += curWidth;
        }
    }
}
