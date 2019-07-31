package com.laka.live.shopping.shoppingcar.adapter;

import android.content.Context;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.laka.live.R;
import com.laka.live.shopping.bean.ShoppingCarGoodsBean;
import com.laka.live.shopping.framework.MsgDispatcher;
import com.laka.live.shopping.framework.adapter.MsgDef;
import com.laka.live.shopping.shoppingcar.ShoppingCarItemHolder;
import com.laka.live.shopping.shoppingcar.ShoppingCarState;

import java.util.List;

/**
 * Created by chenjiawei on 2016/1/4.
 */
public class ShoppingCarItemAdapter extends BaseAdapter {
    private int pageState = 1;
    private Context mContext;
    private List<ShoppingCarGoodsBean> mCarGoodsList;
    private OnGoodsCostPriseListener mOnGoodsCostPriseListener;
    private ShoppingCarItemHolder.OnGoodsOperationListener mOnGoodsOperationListener;

    public ShoppingCarItemAdapter(Context context, List<ShoppingCarGoodsBean> shoppingCarGoodsBeans) {
        this.mContext = context;
        this.mCarGoodsList = shoppingCarGoodsBeans;
    }

    @Override
    public int getCount() {
        return mCarGoodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCarGoodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ShoppingCarItemHolder shoppingCarItemHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.shopping_car_list_item, null);
            shoppingCarItemHolder = new ShoppingCarItemHolder();
            shoppingCarItemHolder.initHolder(convertView, pageState);
            convertView.setTag(shoppingCarItemHolder);
        } else {
            shoppingCarItemHolder = (ShoppingCarItemHolder) convertView.getTag();
            if (shoppingCarItemHolder.getPageState() != pageState) {
                shoppingCarItemHolder = new ShoppingCarItemHolder();
                shoppingCarItemHolder.initHolder(convertView, pageState);
                convertView.setTag(shoppingCarItemHolder);
            }
        }
        shoppingCarItemHolder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ShoppingCarState.isSelected.get(position);
                ShoppingCarState.isSelected.put(position, !isChecked);
                mOnGoodsCostPriseListener.onChange();
            }
        });
        shoppingCarItemHolder.setHolderData(mCarGoodsList.get(position), ShoppingCarState.isSelected, position, mCarGoodsList);

        shoppingCarItemHolder.setOnGoodsOperationListener(new ShoppingCarItemHolder.OnGoodsOperationListener() {
            @Override
            public void onGoodsOperationChange(List<ShoppingCarGoodsBean> mShoppingCarGoodsBeans) {
                setShoppingCarGoodsBeans(mShoppingCarGoodsBeans);
                mOnGoodsOperationListener.onGoodsOperationChange(mShoppingCarGoodsBeans);
                mOnGoodsCostPriseListener.onChange();
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public void setPageState(int pageState) {
        this.pageState = pageState;
    }

    public interface OnGoodsCostPriseListener {
        void onChange();
    }


    public void setOnGoodsCostPristListener(OnGoodsCostPriseListener mOnGoodsCostPriseListener) {
        this.mOnGoodsCostPriseListener = mOnGoodsCostPriseListener;
    }

    public void setShoppingCarGoodsBeans(List<ShoppingCarGoodsBean> mShoppingCarGoodsBeans) {
        this.mCarGoodsList = mShoppingCarGoodsBeans;
    }

    public void setOnGoodsOperationListener(ShoppingCarItemHolder.OnGoodsOperationListener mOnGoodsOperationListener) {
        this.mOnGoodsOperationListener = mOnGoodsOperationListener;
    }


    public void onItemDelete(int position, boolean isCheckedAll) {

        if (mOnGoodsCostPriseListener != null) {
            if (!isCheckedAll) {
                ShoppingCarState.isSelected.put(position, false);
            }
        }

    }

    public void onChange() {
        if (mOnGoodsCostPriseListener != null) {
            mOnGoodsCostPriseListener.onChange();
        }
    }

}
