package com.laka.live.shopping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.laka.live.R;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsBaseBean;
import com.laka.live.shopping.viewholder.GoodsViewHolder;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.util.RippleEffectHelper;

import java.util.List;

/**
 * @ClassName: ShoppingGoodsAdapter
 * @Description: 一级分类商品
 * @Author: chuan
 * @Version: 1.0
 * @Date: 26/07/2017
 */
public class ShoppingGoodsAdapter extends BaseAdapter<ShoppingGoodsBaseBean, GoodsViewHolder> {

    private Context mContext;

    public ShoppingGoodsAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<ShoppingGoodsBaseBean> data){
        mDatas = data;
    }

    @Override
    public GoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodsViewHolder(RippleEffectHelper.addRippleEffectInView(
                LayoutInflater.from(mContext).inflate(R.layout.item_shopping_good, null)));
    }
}
