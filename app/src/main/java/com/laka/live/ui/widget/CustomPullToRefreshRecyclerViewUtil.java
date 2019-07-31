package com.laka.live.ui.widget;
/*
 * @ClassName: CustomPullToRefreshRecyclerViewUtil
 * @Description:
 * @Author: chuan
 * @Version: 1.0
 * @Date: 12/12/16
 */

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

public class CustomPullToRefreshRecyclerViewUtil {

    public int[] findLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager != null) {

            if (layoutManager instanceof LinearLayoutManager) {
                return new int[]{
                        ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition()};
            }

            if (layoutManager instanceof StaggeredGridLayoutManager) {
                return ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
            }
        }
        return new int[]{RecyclerView.NO_POSITION};
    }

    public int[] findFirstCompletelyVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager != null) {

            if (layoutManager instanceof LinearLayoutManager) {
                return new int[]{
                        ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition()};
            }

            if (layoutManager instanceof StaggeredGridLayoutManager) {
                return ((StaggeredGridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPositions(null);
            }

        }
        return new int[]{RecyclerView.NO_POSITION};
    }

    public int[] findFirstVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager != null) {

            if (layoutManager instanceof LinearLayoutManager) {
                return new int[]{
                        ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition()};
            }

            if (layoutManager instanceof StaggeredGridLayoutManager) {
                return ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(null);
            }
        }
        return new int[]{RecyclerView.NO_POSITION};
    }
}
