/*
 * Copyright (C) 2015-2015 SJY.JIANGSU Corporation. All rights reserved
 */

package com.laka.live.shopping.order.model;

import android.util.Log;

import com.laka.live.BuildConfig;
import com.laka.live.shopping.bean.ShoppingCarGoodsBean;
import com.laka.live.shopping.bean.json2bean.JTBShoppingAddress;
import com.laka.live.shopping.bean.json2bean.JTBShoppingDirectBalance;
import com.laka.live.shopping.bean.json2bean.JTBShoppingOrderNum;
import com.laka.live.shopping.common.CommonConst;
import com.laka.live.shopping.network.HttpCallbackAdapter;
import com.laka.live.shopping.network.HttpManager;
import com.laka.live.shopping.network.HttpMethod;
import com.laka.live.shopping.network.HttpUrls;
import com.laka.live.shopping.network.IHttpCallBack;
import com.laka.live.shopping.network.IHttpManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by zhxu on 2015/11/9.
 * Email:357599859@qq.com
 */
public class OrderRequest {

    private final static String TAG = "OrderRequest";

    private static OrderRequest mOrderRequest;

    public OrderRequest() {
    }

    public static OrderRequest getInstance() {
        if (mOrderRequest == null) {
            mOrderRequest = new OrderRequest();
        }
        return mOrderRequest;
    }

    /**
     * 从商品直接结算
     */
    public void postBalanceByGoods(OrderBalanceInfo balanceInfo, HttpCallbackAdapter callBack) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("goodsId", balanceInfo.goodsId + "");
        httpManager.addParams("skuId", balanceInfo.skuId + "");
        httpManager.addParams("count", balanceInfo.goodsCount + "");
        if (balanceInfo.courseId != -1) {
            httpManager.addParams("courseId", balanceInfo.courseId + "");
        }
        if (balanceInfo.videoId != -1) {
            httpManager.addParams("miniVideoId", balanceInfo.videoId + "");
        }
        httpManager.request(HttpUrls.SHOP_URL + "balanceDirectly", HttpMethod.POST, JTBShoppingDirectBalance.class, callBack);
    }

    /**
     * 从购物车直接结算
     */
    public void postBalanceByCart(OrderBalanceInfo balanceInfo, HttpCallbackAdapter callBack) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        String params = getJson(balanceInfo.getList());
        httpManager.addParams("cartParams", params);
        httpManager.request(HttpUrls.SHOP_URL + "balanceCart", HttpMethod.POST, JTBShoppingDirectBalance.class, callBack);
    }

    /**
     * 获取收货人地址
     */
    public void getAddress(HttpCallbackAdapter callBack) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.request(HttpUrls.ADDRESS_URL + "default", HttpMethod.GET, JTBShoppingAddress.class, callBack);
    }

    /**
     * 新增地址
     */
    public void addAddress(OrderAddressInfo addressInfo, HttpCallbackAdapter callBack) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("receiver", addressInfo.receiver);
        httpManager.addParams("mobile", addressInfo.mobile);
        httpManager.addParams("detailAddr", addressInfo.detailAddress);
        httpManager.addParams("districtId", addressInfo.districtId);
        httpManager.request(HttpUrls.ADDRESS_URL + "update", HttpMethod.POST, JTBShoppingAddress.class, callBack);
    }

    /**
     * 订单状态的更新
     */
    public void updateOrder(int payType) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("a", "updateOrder");
        httpManager.addParams("orderId", CommonConst.ORDERID);
        if (payType != 0) {
            httpManager.addParams("payType", payType + "");
        }
        httpManager.request(HttpUrls.URL_SHOP, HttpMethod.GET, new HttpCallbackAdapter() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                if (BuildConfig.DEBUG) {
                    Log.v(TAG, result);
                }
            }

            @Override
            public void onError(String errorStr, int code) {
                super.onError(errorStr, code);
                if (BuildConfig.DEBUG) {
                    Log.v(TAG, errorStr + " code " + code);
                }
            }
        });
    }

    /**
     * 从购物车下订单
     */
    public void postOrderByCart(OrderBalanceInfo balanceInfo, IHttpCallBack callBack) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();

        httpManager.addParams("addressId", balanceInfo.getShoppingAddressBean().getAddressId() + "");
        httpManager.addParams("userRemark", balanceInfo.userRemark);
        httpManager.addParams("payType", balanceInfo.payType + "");
        httpManager.addParams("cartParams", getJson(balanceInfo.getList()));
        httpManager.request(HttpUrls.ORDER_URL + "generateWithCart", HttpMethod.POST, JTBShoppingOrderNum.class, callBack);
    }

    /**
     * 从商品直接下单
     */
    public void postOrderByGoods(OrderBalanceInfo balanceInfo, IHttpCallBack callBack) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("addressId", balanceInfo.getShoppingAddressBean().getAddressId() + "");
        httpManager.addParams("goodsId", balanceInfo.goodsId);
        httpManager.addParams("skuId", balanceInfo.skuId);
        httpManager.addParams("goodsCount", String.valueOf(balanceInfo.goodsCount));
        httpManager.addParams("userRemark", balanceInfo.userRemark);
        httpManager.addParams("payType", balanceInfo.payType + "");
        if (balanceInfo.courseId > 0) {
            httpManager.addParams("courseId", balanceInfo.courseId + "");
        }

        if (balanceInfo.videoId != -1) {
            httpManager.addParams("miniVideoId", balanceInfo.videoId + "");
        }
        httpManager.request(HttpUrls.ORDER_URL + "generateDirectly", HttpMethod.POST, JTBShoppingOrderNum.class, callBack);
    }

    private String getJson(List<ShoppingCarGoodsBean> list) {
        JSONArray json = new JSONArray();
        for (ShoppingCarGoodsBean carGoodsBean : list) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("itemId", carGoodsBean.getItemId());
                jsonObject.put("count", carGoodsBean.getGoodsCount());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            json.put(jsonObject);
        }
        return json.toString();
    }
}
