package com.laka.live.shopping.search.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.laka.live.R;
import com.laka.live.shopping.search.info.SearchInfo;
import com.laka.live.shopping.search.widget.SearchHotGoodsView;
import com.laka.live.shopping.widget.MaterialRippleLayout;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.RippleEffectHelper;

import java.util.List;

/**
 * Created by gangqing on 2016/4/21.
 * Email:denggangqing@ta2she.com
 */
public class SearchHistoryAdapter extends BaseAdapter implements SearchHotGoodsView.HotGoodsItemClickListener {
    private static final int MAX_VIEW_TYPE = 3;
    private Context mContext;
    private List<SearchInfo> mData;
    private AdapterCallBack mCallBack;

    public SearchHistoryAdapter(Context context, List<SearchInfo> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).viewType;
    }

    @Override
    public int getViewTypeCount() {
        return MAX_VIEW_TYPE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            if (getItemViewType(position) == SearchInfo.VIEW_TYPE_HOT) {
                View view = View.inflate(mContext, R.layout.search_adapter_item_hot_goods_layout, null);
                SearchHotGoodsView goodsView = (SearchHotGoodsView) view.findViewById(R.id.search_hot_goods);
                goodsView.setData(mData.get(position).HotSearchList);
                goodsView.setHotGoodsItemClickListener(this);
                convertView = view;
            } else if (getItemViewType(position) == SearchInfo.VIEW_TYPE_HISTORY) {
                TextView tv = new TextView(mContext);
                int dimen = ResourceHelper.getDimen(R.dimen.space_10);
                tv.setTextColor(ResourceHelper.getColor(R.color.search_history_item));
                tv.setPadding(0, dimen, 0, dimen);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResourceHelper.getDimen(R.dimen.space_12));
                tv.setText(mData.get(position).historyInfo.text);
                MaterialRippleLayout rippleLayout = RippleEffectHelper.addRippleEffectInView(tv);
                convertView = rippleLayout;
            } else if (getItemViewType(position) == SearchInfo.VIEW_TYPE_DELETE) {
                View view = View.inflate(mContext, R.layout.search_adapter_item_delete_layout, null);
                MaterialRippleLayout rippleLayout = RippleEffectHelper.addRippleEffectInView(view);
                convertView = rippleLayout;
            }
        }
        return convertView;
    }

    @Override
    public void onItemClick(View v) {
        if (mCallBack != null) {
            mCallBack.onHotSearchItemClick(((TextView) v).getText().toString());
        }
    }

    public void setCallBack(AdapterCallBack callBack) {
        this.mCallBack = callBack;
    }

    public interface AdapterCallBack {
        void onHotSearchItemClick(String content);
    }
}
