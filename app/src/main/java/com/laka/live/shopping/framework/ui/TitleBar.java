/*
 * Copyright (c) 2015. SJY.JIANGSU Corporation. All rights reserved
 */

package com.laka.live.shopping.framework.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.shopping.widget.MaterialRippleLayout;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;

import java.util.List;

public abstract class TitleBar extends LinearLayout implements
        View.OnClickListener {

    private FrameLayout mLayoutNavigation;
    private TextView mTitleTv;
    private ImageView mBackImageView;

    protected FrameLayout mFrameLayoutTab;

    protected ActionBar mActionBar;

    protected ITitleBarListener mTitleBarListener;

    public TitleBar(Context context, ITitleBarListener titleBarListener) {
        super(context);
        mTitleBarListener = titleBarListener;
        initComponent();
        initListener();
    }

    private void initComponent() {

        Context context = getContext();

        mLayoutNavigation = new FrameLayout(context);
        mLayoutNavigation.setLayoutParams(new LayoutParams(0,LayoutParams.MATCH_PARENT, 1f));

        MaterialRippleLayout backLayout = new MaterialRippleLayout(getContext());
        MaterialRippleLayout titleLayout = new MaterialRippleLayout(getContext());
        MaterialRippleLayout actionLayout = new MaterialRippleLayout(getContext());

        FrameLayout.LayoutParams backFrameLayout = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT,
                Gravity.CENTER_VERTICAL);
        FrameLayout.LayoutParams titleFrameLayout = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);
        FrameLayout.LayoutParams actionFrameLayout = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT,
                Gravity.RIGHT);

        // 添加到mLayoutNavigation里面去
        mLayoutNavigation.addView(backLayout, backFrameLayout);
        mLayoutNavigation.addView(titleLayout, titleFrameLayout);
        mLayoutNavigation.addView(actionLayout,actionFrameLayout);

        // 设置标题的样式
        mTitleTv = new TextView(getContext());
        mTitleTv.setClickable(false);
        mTitleTv.setTextColor(ResourceHelper.getColor(R.color.color333333));
        setTextSize(17);
        TextPaint tp = mTitleTv.getPaint();
        tp.setFakeBoldText(true);

        // 设置返回按钮的样式
        mBackImageView = new ImageView(getContext());
        mBackImageView.setImageResource(R.drawable.nav_icon_back);
        backFrameLayout = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT,
                Gravity.CENTER_VERTICAL);
        backFrameLayout.leftMargin = ResourceHelper.getDimen(R.dimen.space_15);
        backFrameLayout.rightMargin = ResourceHelper.getDimen(R.dimen.space_15);

        // 设置右边文本的样式
        mActionBar = createActionBar();
        mActionBar.setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));

        // 添加到mLayoutNavigation的子View里面去
        backLayout.addView(mBackImageView, backFrameLayout);
        titleLayout.addView(mTitleTv,titleFrameLayout);
        actionLayout.addView(mActionBar,actionFrameLayout);

        // 这两行是用来隐藏整个TitleView的,暂时没什么用
        mFrameLayoutTab = new FrameLayout(context);
        mFrameLayoutTab.setLayoutParams(new LayoutParams(0,
                LayoutParams.MATCH_PARENT, 0f));

        // 添加到当前View
        this.addView(mLayoutNavigation);
        this.addView(mFrameLayoutTab);
        this.setBackgroundColor(TitleBar.getBgColor());
    }

    private void initListener() {
        mBackImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mTitleBarListener != null) {
                    mTitleBarListener.onBackActionButtonClick();
                }
            }
        });

    }

    public abstract ActionBar createActionBar();

    public abstract void acceptCommand(int commandId, Object object);

    public void setTitle(String title) {
       mTitleTv.setVisibility(View.VISIBLE);
        mTitleTv.setText(title);
    }

    public void setTitle(int titleResId) {
        mTitleTv.setVisibility(View.VISIBLE);
        mTitleTv.setText(titleResId);
    }

    public void setTextSize(float size) {
        mTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }


    public void setTextSize(int unit, float size) {
        mTitleTv.setTextSize(unit, size);
    }

    public void hideBackActionButton() {
        mBackImageView.setVisibility(View.GONE);
    }

    public void showBackActionButton() {
        mBackImageView.setVisibility(View.VISIBLE);
    }

    public void hideTitleView() {
        mTitleTv.setVisibility(View.GONE);
        LayoutParams navigationLayoutParams = (LayoutParams) mFrameLayoutTab
                .getLayoutParams();
        navigationLayoutParams.weight = 3f;

        LayoutParams actionBarLayoutParams = (LayoutParams) mActionBar
                .getLayoutParams();
        actionBarLayoutParams.width = 0;
        actionBarLayoutParams.weight = 1f;
    }

    public void showTitleView() {
        if (TextUtils.isEmpty(mTitleTv.getText())) {
            mTitleTv.setVisibility(View.GONE);
        } else {
            mTitleTv.setVisibility(View.VISIBLE);
        }

        LayoutParams navigationLayoutParams = (LayoutParams) mFrameLayoutTab
                .getLayoutParams();
        navigationLayoutParams.weight = 0f;

        LayoutParams actionBarLayoutParams = (LayoutParams) mActionBar
                .getLayoutParams();
        actionBarLayoutParams.width = LayoutParams.WRAP_CONTENT;
        actionBarLayoutParams.weight = 0f;
    }

    public String getTitle() {
        return mTitleTv.getText().toString();
    }


    public void setTitleColor(int color) {
        mTitleTv.setTextColor(color);
    }

    public void setTabView(View view) {
        mFrameLayoutTab.addView(view);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TitleBarActionItem) {

            mTitleBarListener
                    .onTitleBarActionItemClick(((TitleBarActionItem) v)
                            .getItemId());
        }
    }

    protected TextView getTitleView() {
        return mTitleTv;
    }

    public void switchActionBar(int actionBarId, boolean animated) {
        mActionBar.switchActionItems(actionBarId, animated);
    }

    public void setActionItems(List<TitleBarActionItem> items) {
        mActionBar.setActionItems(items);
    }

    public ActionBar getActionBar() {
        return mActionBar;
    }


    public static int getBgColor() {
        return ResourceHelper.getColor(R.color.title_bg_color);

    }

}
