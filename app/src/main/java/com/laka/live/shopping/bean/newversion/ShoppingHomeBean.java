package com.laka.live.shopping.bean.newversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @ClassName: ShoppingHomeBean
 * @Description: 商城首页数据请求返回结果
 * @Author: chuan
 * @Version: 1.0
 * @Date: 26/07/2017
 */

public class ShoppingHomeBean {
    @Expose
    @SerializedName("goodsCate")
    private List<GoodsCate> goodsCates;

    @Expose
    @SerializedName("hotGoods")
    private List<ShoppingGoodsBaseBean> hotGoods;

    @Expose
    @SerializedName("topics")
    private List<ShoppingHomeTopics> topics;

    public List<GoodsCate> getGoodsCates() {
        return goodsCates;
    }

    public void setGoodsCates(List<GoodsCate> goodsCates) {
        this.goodsCates = goodsCates;
    }

    public List<ShoppingGoodsBaseBean> getHotGoods() {
        return hotGoods;
    }

    public void setHotGoods(List<ShoppingGoodsBaseBean> hotGoods) {
        this.hotGoods = hotGoods;
    }

    public List<ShoppingHomeTopics> getTopics() {
        return topics;
    }

    public void setTopics(List<ShoppingHomeTopics> topics) {
        this.topics = topics;
    }

    @Override
    public String toString() {
        return "ShoppingHomeBean{" +
                "goodsCates=" + goodsCates +
                ", hotGoods=" + hotGoods +
                ", topics=" + topics +
                '}';
    }
}
