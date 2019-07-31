package com.laka.live.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.util.Log;

/**
 * Created by ios on 16/5/27.
 */
public class ScrollbleViewPager extends ViewPager {
    private static final String TAG = "RoomScrollbleViewPager";
    private boolean scrollble = true;

    private float mLastX;

    private float mLastY;

    public ScrollbleViewPager(Context context) {
        super(context);
    }

    public ScrollbleViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            Log.d(TAG, " onTouchEvent scrollble=" + scrollble);
            if (!scrollble) {
                return true;
            } else {
                if (ev.getAction() == MotionEvent.ACTION_DOWN) {//只响应按下事件
                    EventBusManager.postEvent(ev, SubcriberTag.ON_TOUCH_VIDEO_VIEW);
//                    return super.onTouchEvent(ev);
                }
            }

            boolean isResume = super.onTouchEvent(ev);
            Log.error(TAG, "onTouchEvent isResume = " + isResume);
            return isResume;
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        try {
            if (!scrollble) {
                return false;
            } else {
                boolean isResume = super.onInterceptTouchEvent(event);
                Log.error(TAG, "onInterceptTouchEvent isResume = " + isResume);
                return isResume;
            }
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /*@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mLastX = ev.getX();
            mLastY = ev.getY();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE ||
                ev.getAction() == MotionEvent.ACTION_UP) {
            float x = ev.getX();
            float y = ev.getY();
            if (Math.abs(x - mLastX) + 50 > Math.abs(y - mLastY)) {
                Log.error(TAG, "横向滑动");
                return super.dispatchTouchEvent(ev);
            } else {
                Log.error(TAG, "纵向滑动");
                return false;
            }
        }

        return super.dispatchTouchEvent(ev);
    }
*/
    public boolean isScrollble() {
        return scrollble;
    }

    public void setScrollble(boolean scrollble) {
        this.scrollble = scrollble;
    }
}
