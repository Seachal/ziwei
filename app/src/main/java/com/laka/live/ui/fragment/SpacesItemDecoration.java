package com.laka.live.ui.fragment;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.laka.live.R;
import com.laka.live.ui.adapter.HomeFollowAdapter;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;

import java.util.HashMap;

/**
 * Created by zhxu on 2015/12/24.
 * Email:357599859@qq.com
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;
    private HashMap<Integer, Integer> divider = new HashMap<>();
    private ItemDecorationCallBack mCallBack;

    public SpacesItemDecoration(ItemDecorationCallBack callBack) {
        mCallBack = callBack;
    }

    public void refresh() {
        divider.clear();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int position = parent.getChildAdapterPosition(view);

        if (mCallBack != null) {

            int type = mCallBack.getItemType(position);

            if (type == HomeFollowAdapter.TYPE_LIVE || type == HomeFollowAdapter.TYPE_DIVIDE) {
                mSpace = 0;
                outRect.left = 0;
                outRect.right = 0;
                divider.put(position, 0);
            } else {

                mSpace = ResourceHelper.getDimen(R.dimen.space_2);

                int tempPosition;

                if (type == HomeFollowAdapter.TYPE_REPLAY && (divider.size() == 2 || divider.size() == 4))
                    tempPosition = position - divider.size();
                else
                    tempPosition = (position + 1) - divider.size();

                boolean isRight = tempPosition % 2 == 0;

                if (isRight)
                    outRect.left = mSpace;
                else
                    outRect.right = mSpace;

            }

            outRect.top = 0;
            outRect.bottom = mSpace * 2;

        }

    }

    public interface ItemDecorationCallBack {
        // 获取item类型
        int getItemType(int position);
    }

}
