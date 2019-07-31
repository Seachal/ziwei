package com.laka.live.video.ui.widget.layoutmanager;

public interface OnViewPagerListener {

    /**
     * 页面滚动时候是否到顶部(底部)继续滚动
     *
     * @param isUpScroll
     */
    void onPagerStillScroll(boolean isUpScroll);

    /**
     * 页面加载完成
     */
    void onInitComplete();

    /**
     * 滑动销毁
     *
     * @param isNext
     * @param position
     */
    void onPageRelease(boolean isNext, int position);

    /**
     * 选中的监听以及判断是否滑动到底部
     *
     * @param position
     * @param isBottom
     */
    void onPageSelected(int position, boolean isBottom);


}