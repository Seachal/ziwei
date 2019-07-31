package com.laka.live.shopping.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zhxu on 2016/5/5.
 * Email:357599859@qq.com
 */
public class ShoppingCustomRecycler extends RecyclerView {

    private IShoppingCustomRecycler mCallBack;

    public void setCallBack(IShoppingCustomRecycler mCallBack) {
        this.mCallBack = mCallBack;
    }

    public ShoppingCustomRecycler(Context context) {
        this(context, null);
    }

    public ShoppingCustomRecycler(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShoppingCustomRecycler(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        LayoutManager layoutManager = new LayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPosition(0);
        setLayoutManager(layoutManager);
        setHasFixedSize(true);
        setItemAnimator(null);
    }

    private class LayoutManager extends LinearLayoutManager {

        private LayoutManager(Context context) {
            super(context);
        }

        @Override
        public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
            super.onMeasure(recycler, state, widthSpec, heightSpec);
            if (state.getItemCount() == 0) {
                return;
            }
            View view = recycler.getViewForPosition(0);
            if (view != null) {
                measureChild(view, widthSpec, heightSpec);
                int measuredWidth = MeasureSpec.getSize(widthSpec);
                int measuredHeight = view.getMeasuredHeight();
                setMeasuredDimension(measuredWidth, measuredHeight);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            if (mCallBack != null) {
                mCallBack.onState(false);
            }
        }
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            if (mCallBack != null) {
                mCallBack.onState(true);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public interface IShoppingCustomRecycler {
        void onState(boolean canScroll);
    }
}
