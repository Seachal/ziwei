/*
 * Copyright (C) 2015-2015 SJY.JIANGSU Corporation. All rights reserved
 */

package com.laka.live.shopping.logistics;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.laka.live.R;
import com.laka.live.shopping.bean.ShoppingOrderDetailBean;
import com.laka.live.shopping.bean.ShoppingOrderDetailGoodsBean;
import com.laka.live.shopping.bean.ShoppingOrderLogisticsBean;
import com.laka.live.shopping.framework.DefaultWindow;
import com.laka.live.shopping.framework.IDefaultWindowCallBacks;
import com.laka.live.shopping.framework.Notification;
import com.laka.live.shopping.logistics.adapter.OrderLogisticsRecyclerViewAdapter;
import com.laka.live.shopping.model.TemplateHolder;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhxu on 2015/11/9.
 * Email:357599859@qq.com
 */
public class OrderLogisticsWindow extends DefaultWindow {

    private final static String TAG = "OrderLogisticsWindow";

    private Activity mContext;

    private RecyclerView mRecyclerView;
    private OrderLogisticsRecyclerViewAdapter mAdapter;
    private List<TemplateHolder> mList = new ArrayList<>();

    private ShoppingOrderDetailBean mOrderDetail;

    public OrderLogisticsWindow(Context context, IDefaultWindowCallBacks callBacks, ShoppingOrderDetailBean detailBean) {
        super(context, callBacks);
        mContext = (Activity) context;
        mOrderDetail = detailBean;

        initUI();
    }

    private void initUI() {
        setTitle(ResourceHelper.getString(R.string.order_logistics));
        View view = View.inflate(mContext, R.layout.order_logistics, null);
        getBaseLayer().addView(view, getContentLPForBaseLayer());

        mRecyclerView = (RecyclerView) findViewById(R.id.order_logistics_recycler);
        mAdapter = new OrderLogisticsRecyclerViewAdapter(mContext, mList);
        mRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void loadData() {
        int logisticsId = StringUtils.parseInt(mOrderDetail.selectLogisticsId);//获取选中的物流ID
        Log.d(TAG," logisticsId="+logisticsId);
        TemplateHolder templateHolderTop;
        int logisticsSize = mOrderDetail.getLogistics().size();
        for (int i = 0; i < logisticsSize; i++) {
            templateHolderTop = new TemplateHolder();//状态
            ShoppingOrderLogisticsBean logisticsBean = mOrderDetail.getLogistics().get(i);
            if (logisticsBean.getLogisticsId() == logisticsId) {
                templateHolderTop.setTempType(OrderLogisticsRecyclerViewAdapter.TYPE_TOP);
                templateHolderTop.setItems(logisticsBean);
                mList.add(templateHolderTop);
            }
        }

        TemplateHolder templateHolderGoods;
        int goodsSize = mOrderDetail.getGoods().size();
        int count = 0;
        for (int i = 0; i < goodsSize; i++) {
            templateHolderGoods = new TemplateHolder();//商品信息
            ShoppingOrderDetailGoodsBean goodsBean = mOrderDetail.getGoods().get(i);
            if (goodsBean.getLogisticsId() == logisticsId) {
                count++;
                templateHolderGoods.setTempType(OrderLogisticsRecyclerViewAdapter.TYPE_GOODS);
                templateHolderGoods.setItems(goodsBean);
                mList.add(templateHolderGoods);
                mAdapter.setCount(count);
            }
        }

        TemplateHolder templateHolderLogistics = new TemplateHolder();//物流跟踪
        templateHolderLogistics.setTempType(OrderLogisticsRecyclerViewAdapter.TYPE_LOGISTICS);
        templateHolderLogistics.setItems(mOrderDetail.selectLogisticsId);
        mList.add(templateHolderLogistics);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onWindowStateChange(int stateFlag) {
        super.onWindowStateChange(stateFlag);
        if (stateFlag == STATE_AFTER_PUSH_IN) {
            loadData();
        } else if (stateFlag == STATE_ON_DETACH) {
        }
    }

    @Override
    public void notify(Notification notification) {
    }
}
