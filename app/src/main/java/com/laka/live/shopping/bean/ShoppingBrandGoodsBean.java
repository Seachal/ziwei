package com.laka.live.shopping.bean;

/**
 * Created by zhxu on 2016/5/3.
 * Email:357599859@qq.com
 */
public class ShoppingBrandGoodsBean {

    private int goodsId;
    private String title;
    private String marketPrice;
    private String salePrice;
    private int saleCount;
    private String imageUrl;
    private String thumbUrl;
    private String publishTime;

    public int getGoodsId() {
        return goodsId;
    }

    public String getTitle() {
        return title;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public int getSaleCount() {
        return saleCount;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPublishTime() {
        return publishTime;
    }
}
