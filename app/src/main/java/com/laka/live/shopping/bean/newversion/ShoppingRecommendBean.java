package com.laka.live.shopping.bean.newversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @ClassName: ShoppingRecommendBean
 * @Description: 商品相关推荐
 * @Author: chuan
 * @Version: 1.0
 * @Date: 28/07/2017
 */

public class ShoppingRecommendBean {
    @Expose
    @SerializedName("goods")
    private List<ShoppingGoodsBaseBean> goods;

    public List<ShoppingGoodsBaseBean> getGoods() {
        return goods;
    }

    public void setGoods(List<ShoppingGoodsBaseBean> goods) {
        this.goods = goods;
    }
}
