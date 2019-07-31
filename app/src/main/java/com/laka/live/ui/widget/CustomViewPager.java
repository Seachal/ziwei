package com.laka.live.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.ScrollView;

/**
 * Created by luwies on 16/4/16.
 */
public class CustomViewPager extends ViewPager {

    private float x, y;

    private OnTouchListener mListener;

    public CustomViewPager(Context context) {
        this(context, null);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        onTouch(ev);

        /*ViewParent scrollParent = getScrollViewParent(getParent());

        if (scrollParent == null) {
            return super.dispatchTouchEvent(ev);
        }

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            x = ev.getX();
            y = ev.getY();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            float x1 = ev.getX();
            float y1 = ev.getY();
            float distanceX = x1 - x;
            float distanceY = y1 - y;
            x = x1;
            y = y1;
            if (Math.abs(distanceX) *//** 10*//* > Math.abs(distanceY))
                scrollParent.requestDisallowInterceptTouchEvent(true);
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            scrollParent.requestDisallowInterceptTouchEvent(false);
        }*/

        return super.dispatchTouchEvent(ev);
    }

    private ViewParent getScrollViewParent(ViewParent parent) {
        if (parent instanceof ScrollView || parent instanceof AbsListView || parent == null
                || parent instanceof RecyclerView) {
            return parent;
        }
        return getScrollViewParent(parent.getParent());
    }

    private void onTouch(MotionEvent ev) {
        if (mListener != null) {
            mListener.onTouch(ev);
        }
    }

    public void setOntouchListener(OnTouchListener listener) {
        mListener = listener;
    }

    public interface OnTouchListener {
        void onTouch(MotionEvent ev);
    }
}
