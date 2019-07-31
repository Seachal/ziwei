package com.laka.live.pop;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.laka.live.R;
import com.laka.live.util.ResourceHelper;

import framework.ioc.InjectUtil;

public class BasePop extends PopupWindow {

    protected ViewGroup popContent;
    protected Activity mContext;
    protected Boolean DismissOnTouch;

    @SuppressLint("InflateParams")
    public BasePop(Activity mContext) {
        super(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        // 必须加,不然会被虚拟按键给挡住
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        DismissOnTouch = true;
        popContent = (ViewGroup) LayoutInflater.from(mContext).inflate(
                R.layout.pop_base, null);
        super.setContentView(popContent);
        setBackgroundDrawable(new ColorDrawable(
                ResourceHelper.getColor(R.color.transparent)));
        setFocusable(true);
        setOutsideTouchable(true);
        update();
        this.mContext = mContext;
        popContent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (DismissOnTouch)
                    dismiss();
            }
        });

        InjectUtil.inject(this);
    }


    public Boolean getDismissOnTouch() {
        return DismissOnTouch;
    }

    /**
     * 设置是否，点击屏幕退出
     *
     * @param dismissOnTouch
     */
    public void setDismissOnTouch(Boolean dismissOnTouch) {
        DismissOnTouch = dismissOnTouch;
    }

    public void showAtTop() {
        try {
            showAtLocation(popContent, Gravity.TOP, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAtCenter() {
        try {
            showAtLocation(popContent, Gravity.CENTER, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAtBottom() {
        try {

            showAtLocation(popContent, Gravity.BOTTOM, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPadding(int left, int top, int right, int bottom) {
        popContent.setPadding(left, top, right, bottom);
    }

    public View setContentView(int layoutRes) {
        View view = LayoutInflater.from(mContext).inflate(layoutRes, popContent);
        InjectUtil.injectView(this, view);
        return view;
    }

    public ViewGroup getRootView() {
        return popContent;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T findById(int id) {
        return (T) popContent.findViewById(id);
    }


    @Override
    public void showAsDropDown(View anchor) {

        // 解决7.0版本，android源码显示pop位置错误的BUG
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor);

    }

}
