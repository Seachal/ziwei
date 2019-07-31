package com.laka.live.ui.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by luwies on 16/6/29.
 */
public class NewestItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;

    private boolean hasHead;

    public NewestItemDecoration(int spanCount, int spacing) {
        this.spanCount = spanCount;
        this.spacing = spacing;
    }

    public void setHasHead(boolean hasHead) {
        this.hasHead = hasHead;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int count = parent.getAdapter().getItemCount();
        if (hasHead) {
            if (position == 0) {
                outRect.left = 0;
                outRect.top = spacing;
                outRect.right = 0;
                outRect.bottom = 0;
                return;
            } else {
                position -= 1;
                count -= 1;
            }
        }
        int column = position % spanCount; // item column

        int lastLineStart = count - spanCount;

        if (column == 0) {
            outRect.left = 0;
        } else {
            outRect.left = spacing;
        }
        outRect.right = 0;
        outRect.top = spacing;
        if (position < lastLineStart) {
            outRect.bottom = 0;
        } else {
            outRect.bottom = spacing;
        }
    }

}
