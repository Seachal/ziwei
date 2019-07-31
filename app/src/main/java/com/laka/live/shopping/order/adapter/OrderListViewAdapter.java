/*
 *  * Copyright (C) 2015-2015 SJY.JIANGSU Corporation. All rights reserved
 */

package com.laka.live.shopping.order.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.shopping.activity.OrderAddressActivity;
import com.laka.live.shopping.bean.ShoppingAddressBean;
import com.laka.live.shopping.bean.ShoppingBalanceBean;
import com.laka.live.shopping.bean.ShoppingCarGoodsBean;
import com.laka.live.shopping.bean.json2bean.JTBShoppingAddress;
import com.laka.live.shopping.bean.json2bean.JTBShoppingDirectBalance;
import com.laka.live.shopping.framework.MsgDispatcher;
import com.laka.live.shopping.framework.adapter.MsgDef;
import com.laka.live.shopping.order.model.OrderAddressInfo;
import com.laka.live.shopping.order.model.OrderTemplateHolder;
import com.laka.live.shopping.widget.PriceView;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;

import java.util.List;

/**
 * Created by zhxu on 2015/12/1.
 * Email:357599859@qq.com
 */
public class OrderListViewAdapter extends BaseAdapter implements View.OnClickListener {
    public static final int ITEM_TYPE_TOP = 0;
    public static final int ITEM_TYPE_GOODS = 1;
    public static final int ITEM_TYPE_STATS = 2;
    private static final int MAX_ITEM_TYPE = 3;

    private int mSum = 0;

    private String mTaCoin = "";

    private Context mContext;

    private View mTopView;
    private View mStatsView;

    private List<OrderTemplateHolder> mTemplateHolder;

    private OrderAddressInfo mAddressInfo;

    public OrderListViewAdapter(Context context, List<OrderTemplateHolder> templateHolder) {
        mContext = context;
        mTemplateHolder = templateHolder;
        mAddressInfo = new OrderAddressInfo();
    }

    public void resetSum() {
        mSum = 0;
    }

    public View createStatsView(ShoppingBalanceBean balance) {
        if (mStatsView == null) {
//            View statsView = View.inflate(mContext, R.layout.order_main_goods_sum, null);//旧模板
            View statsView = View.inflate(mContext, R.layout.order_detail_bottom_view, null);
            mStatsView = statsView;
        }
        if (balance != null) {
            TextView tvSum = (TextView) mStatsView.findViewById(R.id.tv_goods_price);
            TextView tvFreight = (TextView) mStatsView.findViewById(R.id.tv_goods_freight);
            TextView tvGoodsSum = (TextView) mStatsView.findViewById(R.id.tv_goods_sum_pay);
            TextView tvTaCoin = (TextView) mStatsView.findViewById(R.id.tv_ta_coin);
            TextView tvTaCoinTitle = (TextView) mStatsView.findViewById(R.id.tv_ta_coin_title);
            tvTaCoin.setVisibility(View.GONE);
            tvTaCoinTitle.setVisibility(View.GONE);
            tvSum.setText(getText(R.string.shopping_price, Double.parseDouble(balance.getGoodsPrice())));
            String freight = balance.getPostageFee();
            if (Double.parseDouble(freight) == 0) {
                tvFreight.setText(ResourceHelper.getString(R.string.order_free_shipping));
            } else {
                tvFreight.setText(freight);
            }
            if (mTaCoin.length() == 0) {
                mTaCoin = balance.getTotalPrice();
            }
            tvGoodsSum.setText("¥" + mTaCoin);
        }
        return mStatsView;
    }

    public View createTopView(ShoppingAddressBean address) {
        if (mTopView == null) {
//            View topView = View.inflate(mContext, R.layout.order_main_address, null);//旧模板
            View topView = View.inflate(mContext, R.layout.order_detail_address, null);
            mTopView = topView;
        }
        LinearLayout layoutAdd = (LinearLayout) mTopView.findViewById(R.id.layout_address_add);
        LinearLayout layoutAddress = (LinearLayout) mTopView.findViewById(R.id.layout_address);
        TextView tvReceiver = (TextView) mTopView.findViewById(R.id.tv_receiver);
        TextView tvMobile = (TextView) mTopView.findViewById(R.id.tv_mobile);
        TextView tvAddress = (TextView) mTopView.findViewById(R.id.tv_address);
        ImageView ivAdd = (ImageView) mTopView.findViewById(R.id.tv_address_add);
        ImageView iconLocation = (ImageView) mTopView.findViewById(R.id.icon_location);
        layoutAdd.setOnClickListener(this);
        if (address != null) {
            tvReceiver.setText(getText(R.string.order_goods_receiver, address.getReceiver()));
            tvMobile.setText(address.getMobile());
            tvAddress.setText(getText(R.string.order_goods_address, address.getDetailDistrict() + address.getDetailAddr()));
            iconLocation.setVisibility(View.GONE);
            layoutAddress.setVisibility(View.VISIBLE);
            mAddressInfo.receiver = address.getReceiver();
            mAddressInfo.mobile = address.getMobile();
            mAddressInfo.detailAddress = address.getDetailAddr();
            mAddressInfo.districtId = String.valueOf(address.getDistrictId());
            mAddressInfo.cityId = String.valueOf(address.getCityId());
            mAddressInfo.provinceId = String.valueOf(address.getProvinceId());
        } else {
            layoutAddress.setVisibility(View.GONE);
            iconLocation.setVisibility(View.VISIBLE);
            tvAddress.setText(ResourceHelper.getString(R.string.order_no_address));
        }
        return mTopView;
    }

    public View createGoodsItems(ShoppingCarGoodsBean goodsInfo) {

        View view = View.inflate(mContext, R.layout.shopping_car_goods_main, null);
        if (goodsInfo != null) {
            view.findViewById(R.id.edit_layout).setVisibility(View.GONE);
            SimpleDraweeView sdvGoodsThumb = (SimpleDraweeView) view.findViewById(R.id.shopping_car_goods_items_img);
            int padding = ResourceHelper.getDimen(R.dimen.space_5);
            view.findViewById(R.id.layout).setPadding(ResourceHelper.getDimen(R.dimen.space_15),padding,padding,padding);
            TextView tvTitle = (TextView) view.findViewById(R.id.shopping_car_goods_items_title);
            TextView tvSpec = (TextView) view.findViewById(R.id.shopping_car_goods_items_spec);
            TextView tvCount = (TextView) view.findViewById(R.id.shopping_car_goods_items_count);
            PriceView tvPrice = (PriceView) view.findViewById(R.id.shopping_car_goods_items_price);
            final String thumbUrl = goodsInfo.getThumbUrl();
            if (!StringUtils.isEmpty(thumbUrl)) {
                sdvGoodsThumb.setImageURI(Uri.parse(thumbUrl));
            }
            final String title = goodsInfo.getTitle();
            if (!StringUtils.isEmpty(title)) {
                tvTitle.setText(title);
            }
            final String specName = goodsInfo.getSpecName();
            if (!StringUtils.isEmpty(specName)) {
                tvSpec.setText(getText(R.string.order_goods_spec_name, specName));
            }

            tvCount.setText(getText(R.string.order_detail_goods_count, goodsInfo.getGoodsCount()));
            tvPrice.setText(goodsInfo.getSalePrice());

        }
        return view;
    }

    private CharSequence getText(int resId, Object val) {
        return String.format(ResourceHelper.getString(resId), val);
    }

    private View createConvertView(int position) {
        int type = mTemplateHolder.get(position).getTempType();
        Object items = mTemplateHolder.get(position).getItems();
        if (type == ITEM_TYPE_TOP) {
            JTBShoppingAddress address = (JTBShoppingAddress) items;
            return createTopView(address.getData());
        } else if (type == ITEM_TYPE_GOODS) {
            return createGoodsItems((ShoppingCarGoodsBean) items);
        } else if (type == ITEM_TYPE_STATS) {
            JTBShoppingDirectBalance directBalance = (JTBShoppingDirectBalance) items;
            return createStatsView(directBalance.getData());
        } else {
            return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mTemplateHolder.get(position).getTempType();
    }

    @Override
    public int getViewTypeCount() {
        return MAX_ITEM_TYPE;
    }

    @Override
    public int getCount() {
        return mTemplateHolder.size();
    }

    @Override
    public Object getItem(int position) {
        return mTemplateHolder.get(position).getItems();
    }

    @Override
    public long getItemId(int position) {
        return mTemplateHolder.get(position).getTempType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = createConvertView(position);
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        // 跳到编辑地址的页面
        OrderAddressActivity.startActivity(mContext,mAddressInfo);
    }

}
