package com.laka.live.shopping.bean.newversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @ClassName: ShoppingGoodsSkuBean
 * @Description: 商品规格
 * @Author: chuan
 * @Version: 1.0
 * @Date: 27/07/2017
 */

public class ShoppingGoodsSkuBean implements Serializable{
    @Expose
    @SerializedName("skuId")
    private int skuId;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("price")
    private float price;

    @Expose
    @SerializedName("surplusCount")
    private String surplusCount;

    @Expose
    @SerializedName("imageUrl")
    private String imageUrl;

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getSurplusCount() {
        return surplusCount;
    }

    public void setSurplusCount(String surplusCount) {
        this.surplusCount = surplusCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "ShoppingGoodsSkuBean{" +
                "skuId=" + skuId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", surplusCount='" + surplusCount + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
