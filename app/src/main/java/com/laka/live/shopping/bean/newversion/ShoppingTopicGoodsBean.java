package com.laka.live.shopping.bean.newversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @ClassName: ShoppingTopicGoodsBean
 * @Description: 专题商品
 * @Author: chuan
 * @Version: 1.0
 * @Date: 27/07/2017
 */

public class ShoppingTopicGoodsBean {
    @Expose
    @SerializedName("topicId")
    private int topicId;

    @Expose
    @SerializedName("title")
    private String title;

    @Expose
    @SerializedName("imageUrl")
    private String imageUrl;

    @Expose
    @SerializedName("goods")
    private List<ShoppingGoodsBaseBean> goods;

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<ShoppingGoodsBaseBean> getGoods() {
        return goods;
    }

    public void setGoods(List<ShoppingGoodsBaseBean> goods) {
        this.goods = goods;
    }

    @Override
    public String toString() {
        return "ShoppingTopicGoodsBean{" +
                "topicId=" + topicId +
                ", title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", goods=" + goods +
                '}';
    }
}
