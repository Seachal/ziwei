package com.laka.live.ui.widget;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;

import com.laka.live.application.LiveApplication;
import com.laka.live.util.Log;
import com.laka.live.util.Utils;


/**
 * @ClassName: FloatWindow
 * @Description: 悬浮窗口
 * @Author: chuan
 * @Version: 1.0
 * @Date: 09/08/2017
 */

public class FloatWindow {
    private final static String TAG = FloatWindow.class.getSimpleName();

    private WindowManager mWindowManager;
    private LayoutParams mLayoutParams;

    private RootLinearLayout mRootLinearLayout;

    private static FloatWindow INSTANCE;

    public static FloatWindow getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FloatWindow();
        }

        return INSTANCE;
    }

    /**
     * 初始化
     */
    public void initFloatWindow() {
        init();
    }

    /**
     * 添加子view
     *
     * @param view   子view
     * @param params LayoutParams
     */
    public void addChildView(View view, ViewGroup.LayoutParams params) {
        if (mRootLinearLayout != null) {
            mRootLinearLayout.removeAllViews();
            mRootLinearLayout.addView(view, params);
        }
    }

    /**
     * 添加子view
     *
     * @param view 子view
     */
    public void addChildView(View view) {
        if (mRootLinearLayout != null) {
            mRootLinearLayout.removeAllViews();
            mRootLinearLayout.addView(view);
        }
    }

    public void addRootView() {
        if (mWindowManager != null && mRootLinearLayout != null && mLayoutParams != null) {
            mWindowManager.removeView(mRootLinearLayout);
            mWindowManager.addView(mRootLinearLayout, mLayoutParams);
//            if(isShowing){
//                showFloatWindow();
//            }
        }
    }

    /**
     * 显示悬浮窗。
     */
    public void showFloatWindow() {
        Log.d(TAG, " showFloatWindow");
        isShowing = true;
        if (mRootLinearLayout != null) {
            ViewGroup.LayoutParams lp = mRootLinearLayout.getLayoutParams();
            Log.d(TAG, " showFloatWindow成功 width=" + lp.width + " height=" + lp.width + " getChildCount=" + mRootLinearLayout.getChildCount());
            mRootLinearLayout.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, " showFloatWindow失败");
        }
    }

    /**
     * 隐藏悬浮窗但不销毁悬浮窗。
     */
    public void hideFloatWindow() {
        Log.d(TAG, " hideFloatWindow");
        isShowing = false;
        if (mRootLinearLayout != null) {
            mRootLinearLayout.setVisibility(View.GONE);
        }
    }

    public void goBackground() {
        if (mRootLinearLayout != null) {
            mRootLinearLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 销毁
     */
    public void destoryFloatWindow() {
        Log.d(TAG, " destoryFloatWindow");
        isShowing = false;
        try {
            if (mWindowManager != null && mRootLinearLayout != null) {
//                mWindowManager.removeView(mRootLayout);
                mRootLinearLayout.removeAllViews();
                mRootLinearLayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.d(TAG, " destoryFloatWindow报错");
        }

    }

    private FloatWindow() {
    }

    private void init() {
        if (mRootLinearLayout == null) {
            mWindowManager = (WindowManager) LiveApplication.getInstance().getSystemService(LiveApplication.WINDOW_SERVICE);

            mLayoutParams = new LayoutParams();
            mLayoutParams.type = LayoutParams.TYPE_TOAST;
            mLayoutParams.format = PixelFormat.RGBA_8888;
            mLayoutParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
            mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
            mLayoutParams.x = Utils.getScreenWidth(LiveApplication.getInstance()) - Utils.dip2px(LiveApplication.getInstance(), 100);
            mLayoutParams.y = Utils.getScreenHeight(LiveApplication.getInstance()) - Utils.dip2px(LiveApplication.getInstance(), 230);

            mLayoutParams.width = LayoutParams.WRAP_CONTENT;
            mLayoutParams.height = LayoutParams.WRAP_CONTENT;

            mRootLinearLayout = new RootLinearLayout(LiveApplication.getInstance());
            mWindowManager.addView(mRootLinearLayout, mLayoutParams);
        }
    }

    boolean isShowing;

    public boolean isShowing() {
        return isShowing;
    }

    private class RootLinearLayout extends LinearLayout {
        private float mDownX, mDownY;
        private boolean isTouchForMove = false;

        private final Float MOVE_THRESHOLD = 10.0F;

        public RootLinearLayout(Context context) {
            super(context);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {

            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isTouchForMove = false;
                    mDownX = ev.getX();
                    mDownY = ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float deltaX = ev.getX() - mDownX;
                    float deltaY = ev.getY() - mDownY;
                    float absDeltaX = Math.abs(deltaX);
                    float absDeltaY = Math.abs(deltaY);
                    if (absDeltaX > MOVE_THRESHOLD || absDeltaY > MOVE_THRESHOLD) {
                        isTouchForMove = true;
                        mLayoutParams.x = (int) (ev.getRawX() - mDownX);
                        mLayoutParams.y = (int) (ev.getRawY() - mDownY);
                        mWindowManager.updateViewLayout(mRootLinearLayout, mLayoutParams);
                    }
                    break;
                default:
                    break;
            }

            return isTouchForMove || super.dispatchTouchEvent(ev);
        }
    }
}
