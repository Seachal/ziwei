
package com.laka.live.ui.rankinglist.widget;

public interface OnTabChangedListener {

    void onTabChangeStart(int newTabIndex, int oldTabIndex);

    void onTabChanged(int newTabIndex, int oldTabIndex);

    void onTabChangedByTitle(int tabIndex);

    boolean onTabChangPreProcess(int newIndex);

}
