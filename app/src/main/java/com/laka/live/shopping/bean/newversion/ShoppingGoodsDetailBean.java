package com.laka.live.shopping.bean.newversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @ClassName: ShoppingGoodsDetailBean
 * @Description: 商品详情
 * @Author: chuan
 * @Version: 1.0
 * @Date: 27/07/2017
 */

public class ShoppingGoodsDetailBean extends ShoppingGoodsBaseBean {

    @Expose
    @SerializedName("postageDes")
    private String postageDes;

    @Expose
    @SerializedName("imageUrls")
    private List<ShoppingGoodsImageUrlBean> imageUrls;

    @Expose
    @SerializedName("sku")
    private List<ShoppingGoodsSkuBean> sku;

    @Expose
    @SerializedName("cartCount")
    private int cartCount;

    @Expose
    @SerializedName("isCollect")
    private int isCollect;

    public int getPostageType() {
        return postageType;
    }

    public void setPostageType(int postageType) {
        this.postageType = postageType;
    }

    public String getPostageDes() {
        return postageDes;
    }

    public void setPostageDes(String postageDes) {
        this.postageDes = postageDes;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ShoppingGoodsImageUrlBean> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<ShoppingGoodsImageUrlBean> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<ShoppingGoodsSkuBean> getSku() {
        return sku;
    }

    public void setSku(List<ShoppingGoodsSkuBean> sku) {
        this.sku = sku;
    }

    public int getCartCount() {
        return cartCount;
    }

    public void setCartCount(int cartCount) {
        this.cartCount = cartCount;
    }

    public int getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(int isCollect) {
        this.isCollect = isCollect;
    }

    @Override
    public String toString() {
        return "ShoppingGoodsDetailBean{" +
                "postageType=" + postageType +
                ", postageDes='" + postageDes + '\'' +
                ", status='" + status + '\'' +
                ", imageUrls=" + imageUrls +
                ", sku=" + sku +
                ", cartCount=" + cartCount +
                ", isCollect=" + isCollect +
                '}';
    }
}
