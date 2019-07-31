package com.laka.live.shopping.bean.json2bean.newversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.shopping.bean.BaseBean;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsDetailBean;

import java.util.List;

/**
 * @ClassName: JTBShoppingGoodsDetail
 * @Description: 商品详情
 * @Author: chuan
 * @Version: 1.0
 * @Date: 27/07/2017
 */

public class JTBShoppingGoodsDetail extends BaseBean {
    @Expose
    @SerializedName("data")
    private ShoppingGoodsDetailBean data;

    public ShoppingGoodsDetailBean getData() {
        return data;
    }

    public void setData(ShoppingGoodsDetailBean data) {
        this.data = data;
    }
}
