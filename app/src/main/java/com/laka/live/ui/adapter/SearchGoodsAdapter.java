package com.laka.live.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laka.live.R;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsBaseBean;
import com.laka.live.ui.viewholder.GoodsViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lyf on 2017/7/21.
 */

public class SearchGoodsAdapter extends BaseAdapter<ShoppingGoodsBaseBean, BaseAdapter.ViewHolder> {

    private String fromTag;
    public static final String FROM_SEARCH_GOODS = "search_goods";
    public static final String FROM_SEARCH_RECOMMEND_GOODS = "search_recommend_goods";

    private List<ShoppingGoodsBaseBean> checkedList = new ArrayList<>();

    public SearchGoodsAdapter(String fromTag) {
        this.fromTag = fromTag;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_goods, parent, false);
        return new GoodsViewHolder(view, checkedList);

    }


    public List<ShoppingGoodsBaseBean> getCheckedList() {
        return checkedList;
    }

    public void setCheckedList(List<ShoppingGoodsBaseBean> checkedList) {
        this.checkedList = checkedList;
    }

}