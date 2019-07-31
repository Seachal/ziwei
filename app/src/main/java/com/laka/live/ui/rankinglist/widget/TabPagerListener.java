
package com.laka.live.ui.rankinglist.widget;

public interface TabPagerListener {

    public void onTabChangeStart(int newTabIndex, int oldTabIndex);

    public void onTabChanged(int newTabIndex, int oldTabIndex);

    public void onTabSliding(int x, int y);

    public boolean onTabSlideOut();

}
