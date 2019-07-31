package com.laka.live.shopping.search.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.laka.live.R;
import com.laka.live.ui.widget.AlphaTextView;
import com.laka.live.ui.widget.FlowLayout;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;

import java.util.List;

/**
 * Created by gangqing on 2016/4/21.
 * Email:denggangqing@ta2she.com
 */
public class SearchHotGoodsView extends FrameLayout {
    private FlowLayout mFlowLayout;
    private TextView mTitle;
    private HotGoodsItemClickListener mListener;

    public SearchHotGoodsView(Context context) {
        super(context);
        initView();
    }

    public SearchHotGoodsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View rootView = View.inflate(getContext(), R.layout.search_hot_goods_layout, null);
        mFlowLayout = (FlowLayout) rootView.findViewById(R.id.hot_goods_flow);
        mTitle = (TextView) rootView.findViewById(R.id.search_hot_goods_title);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(rootView, params);
    }

    public void setTitle(String title) {
        if (StringUtils.isNotEmpty(title)) {
            mTitle.setText(title);
        }
    }

    public void setData(List<String> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        mFlowLayout.removeAllViews();
        int minSpace = ResourceHelper.getDimen(R.dimen.space_5);
        int maxSpace = ResourceHelper.getDimen(R.dimen.space_10);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(minSpace, minSpace, minSpace, minSpace);
        for (String title : list) {
            AlphaTextView tv = new AlphaTextView(getContext());
            tv.setText(title);
            tv.setBackground(ResourceHelper.getDrawable(R.drawable.search_hot_goods_item_bg));
            tv.setPadding(maxSpace, minSpace, maxSpace, minSpace);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResourceHelper.getDimen(R.dimen.space_12));
            mFlowLayout.addView(tv, params);
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(v);
                    }
                }
            });
        }
    }

    public void setHotGoodsItemClickListener(HotGoodsItemClickListener listener) {
        this.mListener = listener;
    }

    public interface HotGoodsItemClickListener {
        void onItemClick(View v);
    }
}
