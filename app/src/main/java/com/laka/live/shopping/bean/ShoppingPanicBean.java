package com.laka.live.shopping.bean;


import com.laka.live.shopping.bean.newversion.ShoppingGoodsBaseBean;

import java.util.List;

/**
 * Created by zhxu on 2015/12/21.
 * Email:357599859@qq.com
 */
public class ShoppingPanicBean {

    private String panicShoppingId;
    private String surplusSeconds;
    private List<ShoppingGoodsBaseBean> goods;

    public String getPanicShoppingId() {
        return panicShoppingId;
    }

    public String getSurplusSeconds() {
        return surplusSeconds;
    }

    public List<ShoppingGoodsBaseBean> getGoods() {
        return goods;
    }
}
