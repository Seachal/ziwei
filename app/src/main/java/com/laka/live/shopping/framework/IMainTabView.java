package com.laka.live.shopping.framework;

import android.view.View;

/**
 * Created by linhz on 2015/11/29.
 * Email: linhaizhong@ta2she.com
 */
public interface IMainTabView {

    int TAB_TO_SHOW = 0;
    int TAB_TO_HIDE = 1;

    /**
     * @param tabChangedFlag TAB_TO_SHOW / TAB_TO_HIDE
     */
    void onTabChanged(int tabChangedFlag);

    void onTabDoubleTap();

    View getView();

    void onWindowStateChange(int stateFlag);

    boolean onWindowBackKeyEvent();

}
