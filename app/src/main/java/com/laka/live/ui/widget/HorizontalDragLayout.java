package com.laka.live.ui.widget;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.LinearLayout;

import com.laka.live.util.Log;

/**
 * an Horizontal Scroll View like IOS.QQ scroll to choice delete.
 * Created by jayuchou on 15/7/14.
 */
public class HorizontalDragLayout extends LinearLayout {

    private static final String TAG = "HorizontalDragLayout";

    private float x, y;

    private View mContentView;
    private View mDeleteView;
    private ViewDragHelper mDragHlper;

    private int dragDistance;
    /**
     * --- min scroll velocity ---
     */
    private int minVelocity = 600;

    private Point point;

    private final double AUTO_OPEN_SPEED_LIMIT = 800.0;
    private int draggedX;

    private boolean isDragEnable = true;

    public HorizontalDragLayout(Context context) {
        super(context);
    }

    public HorizontalDragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mDragHlper = ViewDragHelper.create(this, 1.0f, new ViewDragCallback());
        point = new Point();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        dragDistance = mDeleteView.getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        point.x = mContentView.getLeft();
        point.y = mContentView.getTop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = getChildAt(0);
        mDeleteView = getChildAt(1);
    }


    public void setIsDragEnable(boolean isDragEnable) {
        this.isDragEnable = isDragEnable;
    }

    /**
     * --- ViewDragHelper.CallBack for MotionEvent ---
     */
    private class ViewDragCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View view, int i) {
            return isDragEnable && (view == mContentView || view == mDeleteView);
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            Log.error(TAG, "clampViewPositionHorizontal = " + left + "/" + dx);
            int realLeft = 0;
            if (child == mContentView) {
                if (left > 0) {// right scroll
                    realLeft = 0;
                } else if (left < 0) {// left scroll
                    realLeft = Math.abs(left) > mDeleteView.getWidth() ? -mDeleteView.getWidth() : left;
                }
            } else {
                int maxleft = mContentView.getWidth();
                realLeft = Math.abs(left) > maxleft ? maxleft : left;
                realLeft = Math.max(realLeft, mContentView.getWidth() - mDeleteView.getWidth());
            }
            return realLeft;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            /*super.onViewReleased(releasedChild, xvel, yvel);
            Log.w(TAG, "--- scroll finish when finger up ---");
            Log.w(TAG, "onViewReleased = " + xvel + "/" + yvel);
            if (releasedChild == mContentView) {
                if (xvel > minVelocity) {
                    mDragHlper.settleCapturedViewAt(point.x, point.y);
                } else if (xvel <= -minVelocity) {
                    mDragHlper.settleCapturedViewAt(point.x - mDeleteView.getWidth(), point.y);
                }
                invalidate();
            } else {
                if (xvel > minVelocity) {
                    mDragHlper.smoothSlideViewTo(mContentView, 0, 0);
                    invalidate();
                }
            }*/

            super.onViewReleased(releasedChild, xvel, yvel);
            boolean settleToOpen = false;
            if (xvel > AUTO_OPEN_SPEED_LIMIT) {
                settleToOpen = false;
            } else if (xvel < -AUTO_OPEN_SPEED_LIMIT) {
                settleToOpen = true;
            } else if (draggedX <= -dragDistance / 2) {
                settleToOpen = true;
            } else if (draggedX > -dragDistance / 2) {
                settleToOpen = false;
            }

            final int settleDestX = settleToOpen ? -dragDistance : 0;
            mDragHlper.smoothSlideViewTo(mContentView, settleDestX, 0);
            ViewCompat.postInvalidateOnAnimation(HorizontalDragLayout.this);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            Log.error(TAG, "--- when view position changed = " + left + "/" + dx);
            draggedX = left;
            if (changedView == mContentView) {
                mDeleteView.offsetLeftAndRight(dx);
            } else if (changedView == mDeleteView) {
                mContentView.offsetLeftAndRight(dx);
            }
            invalidate();
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            // must override when set mDeleteView clickable true;
            return child.getWidth();
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return child.getWidth();
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);

            enableParent(getParent(), state == ViewDragHelper.STATE_IDLE);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isDragEnable == false) {
            return super.dispatchTouchEvent(ev);
        }

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            x = ev.getX();
            y = ev.getY();
            Log.error(TAG, "dispatchTouchEvent : ACTION_DOWN");
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            float x1 = ev.getX();
            float y1 = ev.getY();
            float distanceX = x1 - x;
            float distanceY = y1 - y;
            x = x1;
            y = y1;
            if (Math.abs(distanceX) > Math.abs(distanceY)) {
//                enableParent(getParent(), false);
                requestDisallowInterceptTouchEvent(getParent(), true);
            }
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
//            enableParent(getParent(), true);
            requestDisallowInterceptTouchEvent(getParent(), false);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isDragEnable == false) {
            return super.onInterceptTouchEvent(ev);
        }
        int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            return false;
        }
        return mDragHlper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isDragEnable == false) {
            return super.onTouchEvent(event);
        }
        mDragHlper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mDragHlper.continueSettling(true)) {
            invalidate();
        }
    }

    private void requestDisallowInterceptTouchEvent(ViewParent viewParent, boolean disallowIntercept) {
        if (viewParent != null) {
            viewParent.requestDisallowInterceptTouchEvent(disallowIntercept);
            viewParent = viewParent.getParent();
            requestDisallowInterceptTouchEvent(viewParent, disallowIntercept);
        }
    }

    private void enableParent(ViewParent viewParent, boolean isEnable) {
        if (viewParent != null) {

            if (viewParent instanceof PageListLayout) {
                Log.error(TAG, "viewParent : " + viewParent.getClass());
                ((View)viewParent).setEnabled(isEnable);
                return;
            }

            viewParent = viewParent.getParent();
            enableParent(viewParent, isEnable);
        }
    }
}
