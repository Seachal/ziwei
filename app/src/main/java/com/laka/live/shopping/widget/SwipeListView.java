package com.laka.live.shopping.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.laka.live.application.LiveApplication;
import com.laka.live.util.Log;

/**
 * Created by Lyf on 2017/7/19.
 * 解决item的点击事件与SwipeMenuListView的手动侧滑事件冲突的问题
 */
public class SwipeListView extends SwipeMenuListView {

    private long lastTime;
    private boolean isFastClick;
    private float lastX, lastY;
    private onItemPositionClickListener onItemPositionClickListener;

    public SwipeListView(Context context) {
        super(context);
    }

    public SwipeListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SwipeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int pointToPosition;
    private boolean isIntercept;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 当前点击的位置
        pointToPosition = pointToPosition((int) event.getX(), (int) event.getY());

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isIntercept = (pointToPosition == -1);
            smoothCloseMenu();
        }
        // 这么做，主要是解决，在用删除按钮删除一个item(A)后。该item(A)的事件会被传到下一个item(B)。
        // 这样，就会出现，在其它空白位置侧滑时，也会触发item(B)的侧滑事件，这显然是不合理的。
        if (isIntercept) {
            return true;
        }

        boolean isConsumed = super.onTouchEvent(event);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(LiveApplication.getInstance());
        int minDistance = viewConfiguration.getScaledTouchSlop();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                lastY = event.getY();
                isFastClick = (System.currentTimeMillis() - lastTime) < 500;
                lastTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (onItemPositionClickListener != null && !isFastClick && (System.currentTimeMillis() - lastTime < 200)
                        && Math.abs(lastX - event.getX()) < minDistance && Math.abs(lastY - event.getY()) < minDistance) {

                    if (pointToPosition != -1) {
                        onItemPositionClickListener.onClickPosition(pointToPosition);
                    }

                }
                break;
        }
        return isConsumed;
    }


    public void setOnItemPositionClickListener(@Nullable onItemPositionClickListener listener) {
        this.onItemPositionClickListener = listener;
    }

    public interface onItemPositionClickListener {
        void onClickPosition(int position);
    }

}
