/*
 * Copyright (c) 2015. SJY.JIANGSU Corporation. All rights reserved
 */

package com.laka.live.shopping.framework.ui;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.laka.live.shopping.widget.MaterialRippleLayout;

import java.util.List;

public abstract class ActionBar extends LinearLayout {

    public List<TitleBarActionItem> mItems;
    protected OnClickListener mOnClickListener;

    public ActionBar(Context context, OnClickListener onClickListener) {
        super(context);
        mOnClickListener = onClickListener;
        initComponent();
    }

    private void initComponent() {
        this.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
    }

    public TitleBarActionItem getItem(int id) {
        TitleBarActionItem res = null;
        if (null != mItems) {
            for (TitleBarActionItem item : mItems) {
                if (item.getItemId() == id) {
                    res = item;
                    break;
                }
            }
        }

        return res;
    }

    public void setActionItems(List<TitleBarActionItem> items) {
        this.removeAllViews();
        mItems = items;
        if (items == null || items.size() == 0) {
            return;
        }
        int size = items.size();
        for (int i = 0; i < size; i++) {
            TitleBarActionItem item = mItems.get(i);
            item.setOnClickListener(mOnClickListener);
            MaterialRippleLayout rippleLayout = new MaterialRippleLayout(getContext());
            rippleLayout.addView(item);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            this.addView(rippleLayout, layoutParams);
        }
    }

    public abstract void switchActionItems(int actionBarId, boolean animated);

    public abstract void acceptCommand(int commandId, Object object);


    public abstract void enterEditState();

    public abstract void outEditState();

    public  boolean isEmpty() {
        return mItems == null || mItems.size() == 0;
    }

}
