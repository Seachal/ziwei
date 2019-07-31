package com.laka.live.shopping.viewholder;

import android.view.View;
import android.widget.LinearLayout;

import com.laka.live.R;
import com.laka.live.ui.adapter.BaseAdapter;

/**
 * @ClassName: ClassifyViewHolder
 * @Description: 商城一级分类
 * @Author: chuan
 * @Version: 1.0
 * @Date: 17/07/2017
 */

public class ClassifyViewHolder extends BaseAdapter.ViewHolder {
    private LinearLayout mLineOne;
    private LinearLayout mLineTwo;

    public ClassifyViewHolder(View itemView) {
        super(itemView);

        mLineOne = (LinearLayout) itemView.findViewById(R.id.line_one);
        mLineTwo = (LinearLayout) itemView.findViewById(R.id.line_two);
    }

    @Override
    public void update(BaseAdapter adapter, int position, Object o) {
        //HomeClassifyView
    }
}
