package com.laka.live.shopping.bean;


import java.io.Serializable;

/**
 * Created by zhxu on 2016/1/6.
 * Email:357599859@qq.com
 */
public class ShoppingOrderNumBean implements Serializable {

    private String orderNo;
    private String orderId;
    private CoinBean coin;

    public String getOrderNo() {
        return orderNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public CoinBean getCoin() {
        return coin;
    }
}
