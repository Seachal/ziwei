package com.laka.live.shopping.bean.json2bean.newversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.shopping.bean.BaseBean;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsCountBean;

/**
 * @ClassName: JTBShoppingGoodsCount
 * @Description: 购物车商品总数量
 * @Author: chuan
 * @Version: 1.0
 * @Date: 31/07/2017
 */

public class JTBShoppingGoodsCount extends BaseBean {
    @Expose
    @SerializedName("data")
    private ShoppingGoodsCountBean data;

    public ShoppingGoodsCountBean getData() {
        return data;
    }

    public void setData(ShoppingGoodsCountBean data) {
        this.data = data;
    }
}
