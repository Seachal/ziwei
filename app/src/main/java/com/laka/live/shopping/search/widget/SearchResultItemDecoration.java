package com.laka.live.shopping.search.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by gangqing on 2016/4/28.
 * Email:denggangqing@ta2she.com
 */
public class SearchResultItemDecoration extends RecyclerView.ItemDecoration {
    private int spacing;

    public SearchResultItemDecoration(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = spacing;
        outRect.right = spacing;
        outRect.top = spacing;
        outRect.bottom = spacing;
    }
}
