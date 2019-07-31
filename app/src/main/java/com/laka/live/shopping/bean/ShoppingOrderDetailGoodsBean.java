package com.laka.live.shopping.bean;

import java.io.Serializable;

/**
 * Created by zhxu on 2016/1/8.
 * Email:357599859@qq.com
 */
public class ShoppingOrderDetailGoodsBean implements Serializable {

    private String goodsId;
    private int goodsCount;
    private String goodsPrice;
    private int orderGoodsId;
    private int isReview;
    private int isPanicShopping;
    private int logisticsId;
    private String title;
    private String thumbUrl;
    private String specName;
    private String cateId;
    private String freetryGoodsId;
    private int exprType;

    public String getGoodsId() {
        return goodsId;
    }

    public int getGoodsCount() {
        return goodsCount;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public int getOrderGoodsId() {
        return orderGoodsId;
    }

    public int getIsReview() {
        return isReview;
    }

    public void setIsReview(int isReview) {
        this.isReview = isReview;
    }

    public int getIsPanicShopping() {
        return isPanicShopping;
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

    public String getSpecName() {
        return specName;
    }

    public String getCateId() {
        return cateId;
    }

    public String getFreetryGoodsId() {
        return freetryGoodsId;
    }

    public int getExprType() {
        return exprType;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public void setOrderGoodsId(int orderGoodsId) {
        this.orderGoodsId = orderGoodsId;
    }

    public void setIsPanicShopping(int isPanicShopping) {
        this.isPanicShopping = isPanicShopping;
    }

    public void setLogisticsId(int logisticsId) {
        this.logisticsId = logisticsId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public void setCateId(String cateId) {
        this.cateId = cateId;
    }

    public void setFreetryGoodsId(String freetryGoodsId) {
        this.freetryGoodsId = freetryGoodsId;
    }

    public void setExprType(int exprType) {
        this.exprType = exprType;
    }
}
