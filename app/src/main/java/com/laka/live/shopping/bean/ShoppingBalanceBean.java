package com.laka.live.shopping.bean;

import java.io.Serializable;

/**
 * Created by zhxu on 2016/1/4.
 * Email:357599859@qq.com
 */
public class ShoppingBalanceBean implements Serializable {

    private String goodsPrice;
    private String postageFee;
    private String totalPrice;
    private String usageCoinCount;
    private String usageCoinPrice;

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public String getPostageFee() {
        return postageFee;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public String getUsageCoinCount() {
        return usageCoinCount;
    }

    public String getUsageCoinPrice() {
        return usageCoinPrice;
    }
}
