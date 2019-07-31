package com.laka.live.video.ui.widget.parallaxpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import com.laka.live.util.Utils;
import com.nineoldandroids.view.ViewHelper;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Author:Rayman
 * @Date:2018/8/24
 * @Description:视差动画ViewPager，主要是改变滑动的X值形成滑动差值
 */

public class ParallaxPager extends ViewPager {

    private final String TAG = this.getClass().getSimpleName();

    /**
     * description:滑动距离与速率控制
     **/
    private static final int TOUCH_SCROLL_THRESHOLD = 300;
    private static final int TOUCH_SCROLL_VELOCITY = 300;


    /**
     * description:子View
     **/
    private HashMap<Integer, View> mChildViews = new LinkedHashMap<>();
    private View leftView;
    private View rightView;

    /**
     * description:手势滑动控制
     **/
    private float downX, downY;
    private float moveX, moveY;
    private float distanceX, distanceY;
    private boolean isIntercept = false;
    private VelocityTracker mVelocityTracker;

    public ParallaxPager(Context context) {
        this(context, null);
    }

    public ParallaxPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initProperties();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);

        isIntercept = super.onInterceptTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.computeCurrentVelocity(1000);
                moveX = ev.getX();
                moveY = ev.getY();
                distanceX = Math.abs(moveX - downX);
                distanceY = Math.abs(moveY - downY);
                if (distanceX > distanceY) {
                    Logger.e("Move----水平滑动：" + distanceX + "\n" + distanceY
                            + "\n输出X速率：" + mVelocityTracker.getXVelocity()
                            + "\n输出Y速率：" + mVelocityTracker.getYVelocity());
                    if (distanceX > TOUCH_SCROLL_THRESHOLD ||
                            Math.abs(mVelocityTracker.getXVelocity()) > TOUCH_SCROLL_VELOCITY) {
                        Logger.e("Move----拦截事件");
                        isIntercept = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                releaseVelocity();
                break;
            default:
                releaseVelocity();
                break;
        }
        Logger.e("ParallaxPager-------onInterceptTouchEvent:" + isIntercept);
        return isIntercept;
    }

    private void initProperties() {
        for (int i = 0; i < getChildCount(); i++) {
            mChildViews.put(i, getChildAt(i));
        }
    }

    /**
     * 页面滚动，主要在滚动的设置，设置leftView的差值
     *
     * @param position
     * @param offset
     * @param offsetPixels
     */
    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        // 滑动特别小的距离时，我们认为没有动，可有可无的判断
        float effectOffset = isDragValid(offset) ? 0 : offset;

        leftView = getChildAtPosition(position);
        if (mChildViews.size() > 1) {
            rightView = getChildAtPosition(position + 1);
        }

        dragParallax(leftView, rightView, effectOffset, offsetPixels);
        super.onPageScrolled(position, offset, offsetPixels);
    }

    /**
     * 绑定ChildView，虽然这个在Adapter里面也有实现，这里就简单抽取一个API设置
     */
    public void setUpChildView(List<View> childViews) {
        if (Utils.isEmpty(childViews)) {
            return;
        }
        for (int i = 0; i < childViews.size(); i++) {
            mChildViews.put(i, childViews.get(i));
        }
    }

    private View getChildAtPosition(int position) {
        return mChildViews.get(position);
    }

    private boolean isDragValid(float positionOffset) {
        return Math.abs(positionOffset) < 0.0001;
    }

    /**
     * 其实实现的逻辑很简单，右侧的View保持不变，但是左侧的View在执行动画的时候，用NineOld控制translationX，
     * 然后加上一个translationX的差值
     *
     * @param leftView
     * @param rightView
     * @param positionOffset
     * @param positionOffsetPixels
     */
    private void dragParallax(View leftView, View rightView, float positionOffset, float positionOffsetPixels) {
        if (rightView != null) {
            rightView.bringToFront();
        }

        if (leftView != null) {
            ViewHelper.setTranslationX(leftView, positionOffsetPixels - 200 * positionOffset);
        }
    }

    private void releaseVelocity() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }
}