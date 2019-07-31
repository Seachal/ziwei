/*
 * Copyright (C) 2015-2015 SJY.JIANGSU Corporation. All rights reserved
 */

package com.laka.live.shopping.orderdetail.model;


import com.laka.live.shopping.bean.json2bean.JTBShareResult;
import com.laka.live.shopping.bean.json2bean.JTBShoppingOrderDetail;
import com.laka.live.shopping.network.HttpCallbackAdapter;
import com.laka.live.shopping.network.HttpManager;
import com.laka.live.shopping.network.HttpMethod;
import com.laka.live.shopping.network.HttpUrls;
import com.laka.live.shopping.network.IHttpCallBack;
import com.laka.live.shopping.network.IHttpManager;
import com.laka.live.shopping.order.adapter.OrderRecyclerViewAdapter;

/**
 * Created by zhxu on 2015/11/9.
 * Email:357599859@qq.com
 */
public class OrderDetailRequest {

    public final static String ACTION_CANCEL = "cancelOrder";
    public final static String ACTION_DELETE = "deleteOrder";
    public final static String ACTION_CONFIRM = "confirmOrder";

    private static OrderDetailRequest mOrderRequest;

    public OrderDetailRequest() {
    }

    public static OrderDetailRequest getInstance() {
        if (mOrderRequest == null) {
            mOrderRequest = new OrderDetailRequest();
        }
        return mOrderRequest;
    }

    /**
     * 获取订单详情
     */
    public void getOrderDetail(String orderId, IHttpCallBack callBack) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("a", "orderDetail");
        httpManager.addParams("orderId", orderId);
        httpManager.request(HttpUrls.ORDER_DETAIL, HttpMethod.GET, JTBShoppingOrderDetail.class, callBack);
    }

    /**
     * 订单取消，删除，确认
     */
    public void postOrderState(String action, String orderId, HttpCallbackAdapter callBack) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("a", action);
        httpManager.addParams("orderId", orderId);

        if (ACTION_CANCEL.equals(action)) {
            httpManager.request(HttpUrls.ORDER_CANCEL, HttpMethod.POST, JTBShareResult.class, callBack);
        } else if (ACTION_CONFIRM.equals(action)) {
            httpManager.request(HttpUrls.ORDER_CONFIRM, HttpMethod.POST, JTBShareResult.class, callBack);
        }else if (ACTION_DELETE.equals(action)) {
            httpManager.request(HttpUrls.ORDER_DELETE, HttpMethod.POST, JTBShareResult.class, callBack);
        }
//        httpManager.request(HttpUrls.URL_SHOP, HttpMethod.GET, JTBShoppingOrderResult.class, callBack);
    }
}
