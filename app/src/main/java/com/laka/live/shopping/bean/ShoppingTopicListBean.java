package com.laka.live.shopping.bean;

import java.io.Serializable;

/**
 * Created by zhxu on 2015/12/23.
 * Email:357599859@qq.com
 */
public class ShoppingTopicListBean implements Serializable {

    private int goodsId;
    private String title;
    private String marketPrice;
    private String salePrice;
    private int saleCount;
    private String description;
    private String thumbUrl;
    private String tag;

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

    public String getDescription() {
        return description;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public String getTag() {
        return tag;
    }
}
