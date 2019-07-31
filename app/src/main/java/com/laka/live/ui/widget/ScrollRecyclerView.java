package com.laka.live.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.laka.live.R;
import com.laka.live.shopping.widget.ShoppingCustomRecycler;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;

import org.xutils.common.util.LogUtil;


/**
 * Created by Lyf on 2017/8/3.
 * 解决ScrollView与RecyclerView横向滚动时的事件冲突
 */
public class ScrollRecyclerView extends RecyclerView {

    public ScrollRecyclerView(Context context) {
        this(context, null, 0);
    }

    public ScrollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
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

    }

    private float lastX, lastY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {

        boolean intercept = super.onInterceptTouchEvent(e);

        switch (e.getAction()) {

            case MotionEvent.ACTION_DOWN:
                lastX = e.getX();
                lastY = e.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (lastX < e.getX() && lastX < ResourceHelper.getDimen(R.dimen.space_30)) {
                    // 靠近屏幕左侧，并且手势是从左到右的，不要拦截，让给侧滑退出控件
                    break;
                }
                // 只要横向大于竖向，就拦截掉事件。
                float slopX = Math.abs(e.getX() - lastX);
                float slopY = Math.abs(e.getY() - lastY);
                //Log.log("slopX=" + slopX + ", slopY=" + slopY);
                // 部分机型点击事件，都当移动事件处理，而x、y的值又都是0，所以只需要加个判断就行。(slopX > 0 || slopY > 0)
                LogUtil.e("------lastX-------:" + lastX + "------lastY-------:" + lastY);
                LogUtil.e("------slopX-------:" + slopX + "------slopY-------:" + slopY);
                if (slopX >= slopY && (slopX > 0 || slopY > 0)) {
                    requestDisallowInterceptTouchEvent(true);
                    intercept = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
        }
        //Log.log("intercept" + e.getAction() + "=" + intercept);
        return intercept;
    }

}
