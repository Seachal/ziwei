package com.laka.live.ui.widget.panel;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.laka.live.R;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.SystemUtil;

/**
 * Created by zwl on 2015/12/10.
 * Email: 1501448275@qq.com
 */
public abstract class BasePanel {
    protected Context mContext;

    protected WindowManager mWindowManager;
    protected WindowManager.LayoutParams mWlps;

    protected PanelLayout mRootView;
    protected View mContentView;

    protected boolean mIsShowing = false;

    protected boolean mIsCancelTouchOutside = true;

    protected PanelListener mListener;

    public BasePanel(Context context) {
        this(context, true);
    }

    public BasePanel(Context context, boolean autoInit) {
        mContext = context;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mWlps = new WindowManager.LayoutParams();
        mWlps.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        mWlps.flags |= WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
        mWlps.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        mWlps.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWlps.height = WindowManager.LayoutParams.MATCH_PARENT;
        mWlps.format = PixelFormat.TRANSLUCENT;
        mWlps.windowAnimations = R.style.SlideBottom2TopAnim;
        mWlps.dimAmount = 0.4f;
        if (SystemUtil.isTransparentStatusBarEnable()) {
            SystemUtil.configTransparentStatusBar(mWlps);
        }
        if (autoInit) {
            initPanel();
        }
    }

    protected void initPanel() {
        if (mRootView == null) {
            mRootView = new PanelLayout(mContext);
        }
        mContentView = onCreateContentView();

        mRootView.addView(mContentView, getLayoutParams());
    }

    protected abstract View onCreateContentView();

    public View getContentView() {
        return mContentView;
    }

    protected FrameLayout.LayoutParams getLayoutParams() {
        int width = FrameLayout.LayoutParams.MATCH_PARENT;
        int height = FrameLayout.LayoutParams.WRAP_CONTENT;
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height);
        lp.gravity = Gravity.BOTTOM;
        return lp;
    }

    public void setAnimation(int animStyle) {
        mWlps.windowAnimations = animStyle;
    }

    public void setParams(int width, int height, int gravity) {
        mWlps.width = width;
        mWlps.height = height;
        mWlps.gravity = gravity;
    }

    public void setPanelListener(PanelListener listener) {
        mListener = listener;
    }

    public void setDimValue(float value) {
        mWlps.dimAmount = value;
    }

    public void showPanel() {
        if (mRootView.getParent() != null) {
            return;
        }

        beforeShow();
        try {
            mWindowManager.addView(mRootView, mWlps);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mIsShowing = true;
        onShow();
        if (mListener != null) {
            mListener.onPanelShow();
        }
    }

    protected void beforeShow() {

    }

    protected void onShow() {

    }

    public void hidePanel() {
        mIsShowing = false;
        if (mRootView.getParent() == null) {
            return;
        }
        beforeHide();
        try {
            mWindowManager.removeView(mRootView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onHide();
        if (mListener != null) {
            mListener.onPanelHide();
        }
    }

    protected void beforeHide() {

    }

    protected void onHide() {

    }

    protected boolean onBackPressed() {
        return false;
    }

    public void setCancelTouchOutside(boolean cancel) {
        mIsCancelTouchOutside = cancel;
    }

    public boolean isShowing() {
        return mIsShowing;
    }

    private class PanelLayout extends FrameLayout {
        private boolean mTouchDownOutside = false;

        public PanelLayout(Context context) {
            super(context);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            Rect rect = new Rect();
            mContentView.getHitRect(rect);
            int touchX = (int) ev.getX();
            int touchY = (int) ev.getY();
            int action = ev.getAction();
            if (!rect.contains(touchX, touchY) && action == MotionEvent.ACTION_DOWN) {
                mTouchDownOutside = true;
            }

            if ((action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) && mTouchDownOutside) {
                mTouchDownOutside = false;
                if (mIsCancelTouchOutside) {
                    hidePanel();
                }
            }

            return super.dispatchTouchEvent(ev);
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_UP
                    && (event.getKeyCode() == KeyEvent.KEYCODE_BACK || event.getKeyCode() == KeyEvent.KEYCODE_MENU)) {
                if (!onBackPressed()) {
                    hidePanel();
                }
            }
            return super.dispatchKeyEvent(event);
        }

    }

    public interface PanelListener {
        void onPanelShow();

        void onPanelHide();
    }

    public void setAlpha(float mAlpha) {
        this.mWlps.dimAmount = mAlpha;
    }
}
