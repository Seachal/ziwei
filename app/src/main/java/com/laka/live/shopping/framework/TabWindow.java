
package com.laka.live.shopping.framework;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.laka.live.R;
import com.laka.live.ui.rankinglist.widget.OnTabChangedListener;
import com.laka.live.ui.rankinglist.widget.TabWidget;
import com.laka.live.shopping.framework.ui.TitleBar;
import com.laka.live.shopping.framework.ui.TitleBarCommandId;
import com.laka.live.util.ResourceHelper;

import java.util.ArrayList;

public class TabWindow extends DefaultWindow implements OnTabChangedListener {

    protected TabWidget mTabWidget;

    private static final int DEFAULT_TAB_COUNT = 2;
    private ArrayList<ITabView> mTabViewsArray = new ArrayList<>(DEFAULT_TAB_COUNT);
    private int mTabCount;

    public TabWindow(Context context, IDefaultWindowCallBacks callBacks) {
        this(context, callBacks, WindowLayerType.ONLY_USE_BASE_LAYER);
    }

    public TabWindow(Context context, IDefaultWindowCallBacks callBacks, WindowLayerType useLayerType) {
        super(context, callBacks, useLayerType);
        onCreateContent();
    }

    public TabWindow(Context context, IDefaultWindowCallBacks callBacks, boolean isFullScreen) {
        super(context, callBacks, isFullScreen);
        onCreateContent();
    }

    public void setTabbarInTitlebar(boolean enable) {
        if (enable) {
            if (null != this.getTitleBarInner()) {
                getTitleBarInner().hideTitleView();
            }
            moveTabBarToTitleBar();
        } else {
            if (null != this.getTitleBarInner()) {
                getTitleBarInner().showTitleView();
            }
            moveTabBarToTabWidget();
        }
    }

    public void addTab(ITabView tabView) {

        String title = tabView.getTitle();

        TextView titleView = new TextView(getContext());
        titleView.setText(title);
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResourceHelper.getDimen(R.dimen.tabbar_textsize));

        mTabWidget.addTab(tabView.getTabView(), titleView);
        mTabViewsArray.add(tabView);
        ++mTabCount;
    }

    public void setCurrentTab(int index) {
        mTabWidget.snapToTab(index, false);
    }

    public void setCurrentTab(int index, boolean enableAnim) {
        mTabWidget.snapToTab(index, enableAnim);
    }

    public int getCurrentTabIndex() {
        return mTabWidget.getCurrentTabIndex();
    }

    public ITabView getCurrentTab() {
        return mTabViewsArray.get(getCurrentTabIndex());
    }

    public int getTabSize() {
        return mTabViewsArray.size();
    }

    public void reset() {
        if (mTabWidget != null) {
            mTabWidget.reset();
        }
    }

    private View onCreateContent() {
        TabWidget tabWidget = new TabWidget(getContext());
        tabWidget.setOnTabChangedListener(this);
        mTabWidget = tabWidget;
        getBaseLayer().addView(tabWidget, getContentLPForBaseLayer());
        return tabWidget;
    }

    public void snapToTab(int index) {
        mTabWidget.snapToTab(index);
    }

    public void snapToTab(int index, boolean withAnimation) {
        mTabWidget.snapToTab(index, withAnimation);
    }


    @Override
    protected void onWindowStateChange(int stateFlag) {
        super.onWindowStateChange(stateFlag);
        switch (stateFlag) {
            case STATE_AFTER_PUSH_IN:
                for (int i = 0; i < mTabCount; ++i) {
                    ITabView child = mTabViewsArray.get(i);
                    child.onPrepareContentView();
                }
                if (mTabWidget.getCurrentTabIndex() >= 0 && mTabWidget.getCurrentTabIndex() < mTabViewsArray.size()) {
                    mTabViewsArray.get(mTabWidget.getCurrentTabIndex()).onTabChanged(ITabView.TAB_AFTER_PUSH_IN);
                }
                break;
            case STATE_ON_SHOW:
                if (mTabWidget.getCurrentTabIndex() >= 0 && mTabWidget.getCurrentTabIndex() < mTabViewsArray.size()) {
                    mTabViewsArray.get(mTabWidget.getCurrentTabIndex()).onTabChanged(ITabView.TAB_TO_SHOW);
                }
                break;

            case STATE_ON_HIDE:
            case STATE_BEFORE_POP_OUT:
            case STATE_BEFORE_SWITCH_OUT:
                if (mTabWidget.getCurrentTabIndex() >= 0 && mTabWidget.getCurrentTabIndex() < mTabViewsArray.size()) {
                    mTabViewsArray.get(mTabWidget.getCurrentTabIndex()).onTabChanged(ITabView.TAB_TO_HIDE);
                }
                break;
        }
    }

    @Override
    public void onTabChanged(int newTabIndex, int oldTabIndex) {
        if (newTabIndex != oldTabIndex) {

            if (getTitleBarInner() != null) {
                getTitleBarInner().acceptCommand(TitleBarCommandId.TAB_SWITCH_ACTION_BAR, newTabIndex);
            }

            if (oldTabIndex > -1 && oldTabIndex < mTabViewsArray.size()) {
                ITabView chilcToHide = mTabViewsArray.get(oldTabIndex);
                chilcToHide.onTabChanged(ITabView.TAB_TO_HIDE);
            }
            ITabView childToShow = mTabViewsArray.get(newTabIndex);
            childToShow.onTabChanged(ITabView.TAB_TO_SHOW);
        }
    }

    @Override
    public void onTabChangeStart(int newTabIndex, int oldTabIndex) {

    }

    @Override
    public void onTabChangedByTitle(int tabIndex) {
    }

    @Override
    public boolean onTabChangPreProcess(int newIndex) {
        return false;
    }

    public void addTabBarDecorView(View decorView, RelativeLayout.LayoutParams lp) {
        mTabWidget.addTabBarDecorView(decorView, lp);
    }

    public void removeTabBarDecorView(View decorView) {
        mTabWidget.removeTabBarDecorView(decorView);
    }

    public View getTabTitleView(int index) {
        return mTabWidget.getTabTitleView(index);
    }

    private void moveTabBarToTitleBar() {
        View view = mTabWidget.peelingTabbarOff();
        view.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM));
        mTabWidget.setTabbarContainerBg(new ColorDrawable(Color.TRANSPARENT));
        if (null != this.getTitleBarInner()) {
            this.getTitleBarInner().setTabView(view);
        }
    }

    private void moveTabBarToTabWidget() {
        mTabWidget.setTabbarContainerBg(new ColorDrawable(TitleBar.getBgColor()));
        mTabWidget.pullTabbarIn();
    }


    @Override
    public void notify(Notification notification) {
        super.notify(notification);
    }

    @Override
    protected void onEnterEditState() {
        super.onEnterEditState();
        mTabWidget.lock();
    }

    @Override
    protected void onExitEditState() {
        super.onExitEditState();
        mTabWidget.unlock();
    }

    public void replaceTabContent(int index, View view) {
        mTabWidget.replaceContent(index, view);
    }

    public void recoverTabContent(int index) {
        mTabWidget.recoverContent(index);
    }
}
    
