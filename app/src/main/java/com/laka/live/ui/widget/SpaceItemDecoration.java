package com.laka.live.ui.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.util.UIUtil;

/**
 * Created by Lyf on 2017/7/20.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace ;
    private BaseAdapter mAdapter;

    /**
     * @param space 传入的值，其单位视为dp
     */
    public SpaceItemDecoration(int space) {
        this.mSpace = space;
    }

    /**
     * @param space 传入的值，其单位视为dp
     */
    public SpaceItemDecoration(int space, BaseAdapter mAdapter) {
        this.mSpace = space;
        this.mAdapter = mAdapter;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int pos = parent.getChildAdapterPosition(view);

        outRect.left = 0;
        outRect.top = 0;
        outRect.bottom = 0;
        outRect.right = mSpace;
        if(pos == 0){
            outRect.left = mSpace;
        }
    }

}
