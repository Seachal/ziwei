package com.laka.live.shopping.bean.newversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @ClassName: ShoppingCategoryGoodsBean
 * @Description: 商品数组
 * @Author: chuan
 * @Version: 1.0
 * @Date: 27/07/2017
 */

public class ShoppingCategoryGoodsBean {
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
