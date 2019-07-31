package com.laka.live.shopping.bean;

/**
 * Created by linhz on 2016/5/12.
 * Email: linhaizhong@ta2she.com
 */
public class ShoppingRecommendGoodsBean {
    private String goodsId;//商品ID
    private String title; //推荐分类所属 商品标题
    private String marketPrice; //推荐分类所属 商品市场价
    private String salePrice; //推荐分类所属 商品销售价
    private String saleCount; //推荐分类所属 商品销售量
    private String thumbUrl; //推荐分类所属 商品图片URL
    private String goodsCateId;//推荐分类所属 商品推荐分类ID

    public String getGoodsId() {
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

    public String getSaleCount() {
        return saleCount;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public String getGoodsCateId() {
        return goodsCateId;
    }
}
