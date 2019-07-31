package com.laka.live.bean;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;
import com.laka.live.util.Utils;

import java.io.Serializable;

/**
 * Created by Lyf on 2017/7/26.
 * 商品基类
 */
public class Goods implements Serializable{

    @Expose
    @SerializedName(Common.GOODS_ID)
    private int goodsId;
    @Expose
    @SerializedName(Common.TITLE)
    private String title;
    @Expose
    @SerializedName(Common.SALE_PRICE)
    private String salePrice;
    @Expose
    @SerializedName(Common.THUMBURL)
    private String thumbUrl;

    public int getGoodsId() {
        return goodsId;
    }

    // 这个方法名比较好理解。。。
    public String getGoodsName() {
        return getTitle();
    }

    public String getTitle() {

        if (Utils.isEmpty(title)) {
            return "";
        }
        return title;
    }

    public String getSalePrice() {
        if (Utils.isEmpty(salePrice)) {
            return "0";
        }
        return salePrice;
    }

    public String getThumbUrl() {
        if (Utils.isEmpty(thumbUrl)) {
            return "";
        }
        return thumbUrl;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

}
