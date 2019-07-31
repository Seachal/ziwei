package com.laka.live.ui.widget.decoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;


/**
 * @author Rayman
 * @date 2017/10/16
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "DividerItemDecoration";

    //获取系统的分割线
    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    private Context context;

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    /**
     * 分割线Drawable
     */
    private Drawable mDivider;

    /**
     * 方向
     */
    private int mOrientation;

    /**
     * 设置Item之间的高度
     */
    private int mItemSpacing = 0;
    private int[] mSpacingPosition = null;

    /**
     * 设置分割线的左右边距
     */
    private int dividerLeft = 0, dividerRight = 0;
    private int dividerAlpha = 0;
    private boolean isUseStartDivider = false;


    public DividerItemDecoration(Context context) {
        this.context = context;
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        mItemSpacing = mDivider.getIntrinsicHeight();
        a.recycle();
    }

    public DividerItemDecoration(Context context, int orientation) {
        this(context);
        setOrientation(orientation);
    }

    public DividerItemDecoration(Context context, int orientation, int dividerHeight) {
        this(context, orientation);
        this.mItemSpacing = dividerHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
//        Log.i(TAG, "recyclerview - itemdecoration---onDraw()");
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    /**
     * 绘制垂直分割线
     *
     * @param canvas
     * @param parent
     */
    private void drawVertical(Canvas canvas, RecyclerView parent) {
//        Log.e(TAG, "vertical list left:" + dividerLeft + "\tright:" + dividerRight);
        //从父容器设置Padding的左边开始
        final int left = parent.getPaddingLeft() + dividerLeft;
        final int right = parent.getWidth() - parent.getPaddingRight() - dividerRight;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            //获取每个child
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();

            //获取该View底部到父容器左上角的垂直距离。因为线条是绘制在子View的底部的
            int top = child.getBottom() + params.bottomMargin;
//            int bottom = top + mDivider.getIntrinsicHeight() + mItemSpacing;
            if (isUseStartDivider && i == 0) {
                top = child.getTop();
            }
            int bottom = top + mItemSpacing;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.setAlpha(dividerAlpha);
            mDivider.draw(canvas);
//            Log.e(TAG, "child top:" + top + "\tchild bottom:" + bottom + "\tchild left:" + left + "\tchild right:" + right);
        }
    }

    /**
     * 绘制视频分割线
     *
     * @param c
     * @param parent
     */
    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int left = child.getRight() + params.rightMargin;
            if (isUseStartDivider && i == 0) {
                left = child.getLeft();
            }
            final int right = left + mItemSpacing;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.setAlpha(dividerAlpha);
            mDivider.draw(c);
        }
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //1.RecyclerView会调用此方法，获取到Item之间的间距，设置Rect矩形
        //获取Item之间的间隔。
        int right = 0;
        int bottom = 0;

        int childPosition = parent.getChildAdapterPosition(view);

        if (mOrientation == VERTICAL_LIST) {
            right = mDivider.getIntrinsicWidth();
            bottom = 0;
        } else {
            right = 0;
            bottom = mDivider.getIntrinsicHeight();
        }

        //设置具体位置的宽度
        if (mSpacingPosition != null) {
            for (int j = 0; j < mSpacingPosition.length; j++) {
                int k = mSpacingPosition[j] - 1;
                if (childPosition == k) {
                    if (mOrientation == VERTICAL_LIST) {
                        bottom += mItemSpacing;
                    } else {
                        right += mItemSpacing;
                    }
                }
            }
        } else {
            if (mOrientation == VERTICAL_LIST) {
                bottom += mItemSpacing;
            } else {
                right += mItemSpacing;
            }
        }

        if (isLastColumn(childPosition, parent)) {
            //假若是最后一列，则不绘制线条
            outRect.set(0, 0, 0, bottom);
        } else if (isLastCow(childPosition, parent)) {
            //假若是最后一行，不绘制线条
            outRect.set(0, 0, right, 0);
        } else {
            //垂直的时候，最大间隔就是分割线的高度了
            int top = 0, left = 0;
            if (mOrientation == VERTICAL_LIST) {
                if (isUseStartDivider && childPosition == 0) {
                    top = mItemSpacing;
                }
                outRect.set(0, top, right, bottom);
            } else {                //在水平的时候，那么最大的间隔就是分割线的宽度
                if (isUseStartDivider && childPosition == 0) {
                    left = mItemSpacing;
                }
                outRect.set(left, 0, right, bottom);
            }
        }
    }


    /**
     * 判断是否为最后一列
     *
     * @param childAdapterPosition 子View的Position
     * @param parent               父容器
     * @return
     */
    private boolean isLastColumn(int childAdapterPosition, RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            int spanCount = getSpanCount(parent);
            if (spanCount == 0) {
                return false;
            }
            if ((childAdapterPosition + 1) % spanCount == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为最后一行
     *
     * @param childAdapterPosition 子View的Position
     * @param parent               父容器
     * @return
     */
    private boolean isLastCow(int childAdapterPosition, RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            int spanCount = getSpanCount(parent);
            if (spanCount == 0) {
                return false;
            }

            int childCount = parent.getAdapter().getItemCount();    //获取子布局数量
            int lastRowCount = childCount % spanCount;

            Log.e(TAG, "lastRowCount：" + lastRowCount + "\nchildCount：" + childCount);
            if (childAdapterPosition >= childCount - spanCount) {
                //最后一行的数量小于SpanCount
                if (lastRowCount < spanCount || lastRowCount == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private int getSpanCount(RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager glm = (GridLayoutManager) layoutManager;
            int spanCount = glm.getSpanCount();
            return spanCount;
        }
        return 0;
    }

    public DividerItemDecoration setOrientation(int orientation) {
        this.mOrientation = orientation;
        return this;
    }

    public DividerItemDecoration setDividerHeight(int dividerHeight) {
        this.mItemSpacing = dividerHeight;
        return this;
    }

    /**
     * 设置DividerPaddingLeft
     *
     * @param left
     * @return
     */
    public DividerItemDecoration setDividerPaddingLeft(int left) {
        this.dividerLeft = left;
        return this;
    }

    /**
     * 设置Divider的paddingRight
     *
     * @param right
     * @return
     */
    public DividerItemDecoration setDividerPaddingRight(int right) {
        this.dividerRight = right;
        return this;
    }

    /**
     * 设置divider的position
     *
     * @param position
     * @return
     */
    public DividerItemDecoration setDividerPosition(int position[]) {
        this.mSpacingPosition = position;
        return this;
    }

    /**
     * 设置divider的透明度
     *
     * @param alpha
     * @return
     */
    public DividerItemDecoration setDividerAlpha(int alpha) {
        this.dividerAlpha = alpha;
        return this;
    }

    public DividerItemDecoration setDivider(Drawable divider) {
        this.mDivider = divider;
        return this;
    }

    public DividerItemDecoration setUseStartDivider(boolean isUseStartDivider) {
        this.isUseStartDivider = isUseStartDivider;
        return this;
    }
}
