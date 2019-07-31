package com.laka.live.ui.rankinglist;

import android.view.View;

/**
 * Created by linhz on 2015/11/29.
 * Email: linhaizhong@ta2she.com
 */
public interface IRankingTabView {

    int TAB_TO_SHOW = 0;
    int TAB_TO_HIDE = 1;

    /**
     * @param tabChangedFlag TAB_TO_SHOW / TAB_TO_HIDE
     */
    void onTabChanged(int tabChangedFlag);

    String getTitle();

    void onTabDoubleTap();

    View getView();

    void onWindowStateChange(int stateFlag);

}
