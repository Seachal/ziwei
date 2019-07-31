package com.laka.live.shopping.bean;

import java.io.Serializable;

/**
 * Created by zhxu on 2015/12/22.
 * Email:357599859@qq.com
 */
public class ShoppingOrderGoodsBean implements Serializable {
    private int goodsId;
    private int goodsCount;
    private String goodsPrice;
    private int logisticsId;
    private String title;
    private String thumbUrl;
    private String orderGoodsId;
    private String specName;
    private int isPanicShopping;
    private int exprType;
    private String cateId;
    private int postId;

    public int getIsPanicShopping() {
        return isPanicShopping;
    }

    public int getExprType() {
        return exprType;
    }

    public String getCateId() {
        return cateId;
    }

    public int getPostId() {
        return postId;
    }

    public String getOrderGoodsId() {
        return orderGoodsId;
    }

    public String getSpecName() {
        return specName;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public int getGoodsCount() {
        return goodsCount;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public int getLogisticsId() {
        return logisticsId;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }
}
