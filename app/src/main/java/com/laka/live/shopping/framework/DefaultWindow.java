/*
 *Copyright (c) 2015-2015. SJY.JIANGSU Corporation. All rights reserved
 */

package com.laka.live.shopping.framework;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.laka.live.R;
import com.laka.live.shopping.framework.ui.BaseLayerLayout;
import com.laka.live.shopping.framework.ui.DefaultTitleBar;
import com.laka.live.shopping.framework.ui.ITitleBarListener;
import com.laka.live.shopping.framework.ui.TitleBar;
import com.laka.live.util.ResourceHelper;


public class DefaultWindow extends AbstractWindow implements ITitleBarListener {
    protected final static int ID_TITLEBAR = 0x1000;
    protected final static int ID_TOOLBAR = 0x1001;

    protected View mTitleBar;

    private IDefaultWindowCallBacks mDefaultWindowCallBacks;

    private boolean mIsEditState = false;
    private boolean mLastFlagOfSwipeGesture = true;

    public DefaultWindow(Context context, IDefaultWindowCallBacks callBacks) {
        this(context, callBacks, WindowLayerType.ONLY_USE_BASE_LAYER);
    }


    public DefaultWindow(Context context, IDefaultWindowCallBacks callBacks,
                         WindowLayerType useLayerType) {
        super(context, callBacks, useLayerType);
        mDefaultWindowCallBacks = callBacks;
        mTitleBar = onCreateTitleBar();
    }

    public DefaultWindow(Context context, IDefaultWindowCallBacks callBacks,
                         boolean fullScreen) {
        super(context, callBacks, WindowLayerType.ONLY_USE_BASE_LAYER,
                fullScreen);
        mDefaultWindowCallBacks = callBacks;
        if (!fullScreen) {
            mStatusBarView = createDefaultStatusBar();
            if (mStatusBarView != null) {
                getBaseLayer().addView(mStatusBarView);
            }
            mTitleBar = onCreateTitleBar();
        } else {
            mTitleBar = onCreateTitleBar();
        }

    }

    @Override
    protected BaseLayerLayout createDefaultBaseLayer() {
        BaseLayerLayout ret = new BaseLayerLayout(getContext());
        ret.setWillNotDraw(false);
        return ret;
    }

    protected TitleBar getTitleBarInner() {
        TitleBar ret = null;
        if ((null != this.mTitleBar) && (this.mTitleBar instanceof TitleBar)) {
            ret = (TitleBar) this.mTitleBar;
        }
        return ret;
    }


    @SuppressWarnings("ResourceType")
    protected View onCreateTitleBar() {
        DefaultTitleBar titleBar = new DefaultTitleBar(getContext(), this);
        titleBar.setLayoutParams(getTitleBarLPForBaseLayer());
        titleBar.setId(ID_TITLEBAR);
        getBaseLayer().addView(titleBar);
        return titleBar;
    }

    public void setTitle(String title) {
        if (this.getTitleBarInner() != null) {
            this.getTitleBarInner().setTitle(title);
        }
    }

    public void setTitle(int titleResId) {
        if (this.getTitleBarInner() != null) {
            this.getTitleBarInner().setTitle(titleResId);
        }
    }

    public String getTitle() {
        if (this.getTitleBarInner() != null) {
            return this.getTitleBarInner().getTitle();
        }
        return null;
    }


    public void setTitleColor(int color) {
        if (this.getTitleBarInner() != null) {
            getTitleBarInner().setTitleColor(color);
        }
    }

    public void setTextSize(float size) {
        if (this.getTitleBarInner() != null) {
            getTitleBarInner().setTextSize(size);
        }
    }

    public void setTextSize(int unit, float size) {
        if (this.getTitleBarInner() != null) {
            getTitleBarInner().setTextSize(unit, size);
        }
    }

    public void setTitleVisibility(int visibility) {
        if (this.getTitleBarInner() != null) {
            getTitleBarInner().setVisibility(visibility);
        }
    }

    protected View getTitleBar() {
        return mTitleBar;
    }

    protected BaseLayerLayout.LayoutParams getTitleBarLPForBaseLayer() {
        BaseLayerLayout.LayoutParams lp = new BaseLayerLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                ResourceHelper.getDimen(R.dimen.titlebar_height));
        lp.type = BaseLayerLayout.TYPE_TITLE_BAR;
        return lp;
    }

    protected BaseLayerLayout.LayoutParams getContentLPForBaseLayer() {
        BaseLayerLayout.LayoutParams lp = new BaseLayerLayout.LayoutParams(
                BaseLayerLayout.LayoutParams.MATCH_PARENT,
                BaseLayerLayout.LayoutParams.MATCH_PARENT);
        lp.type = BaseLayerLayout.TYPE_CONTENT_VIEW;
        if (mIsFullScreen) {
            lp.type = BaseLayerLayout.TYPE_CONTENT_VIEW_EXTENDS_TITLE_BAR;
        }
        return lp;
    }

    @Override
    public void onTitleBarActionItemClick(int itemId) {
    }

    public void switchActionBar(int actionBarId, boolean animated) {
        if (null != this.getTitleBarInner()) {
            this.getTitleBarInner().switchActionBar(actionBarId, animated);
        }
    }

    public final void enterEditState() {
        if (mIsEditState) {
            return;
        }

        mIsEditState = true;
        mLastFlagOfSwipeGesture = isEnableSwipeGesture();
        setEnableSwipeGesture(false);
        onEnterEditState();
    }

    protected void onEnterEditState() {

    }

    public final void exitEditState() {
        if (!mIsEditState) {
            return;
        }

        mIsEditState = false;
        setEnableSwipeGesture(mLastFlagOfSwipeGesture);
        onExitEditState();
    }

    protected void onExitEditState() {

    }

    @Override
    public void onBackActionButtonClick() {
        mDefaultWindowCallBacks.onTitleBarBackClicked(this);
    }

    @Override
    public boolean isInEditMode() {
        return mIsEditState;
    }

    @Override
    public void resetWindowBackground() {
        super.resetWindowBackground();
        if (mTitleBar != null) {
            mTitleBar.setBackgroundColor(TitleBar.getBgColor());
        }
    }

    @Override
    public void setWindowBackground(Drawable drawable) {
        super.setWindowBackground(drawable);
        if (mTitleBar != null) {
            mTitleBar.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public void setTitleBarBackground(int color) {
        setStatusBarBackground(color);
        if (mTitleBar != null) {
            mTitleBar.setBackgroundColor(color);
        }
    }
}