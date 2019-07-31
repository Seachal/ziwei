package com.laka.live.shopping.bean.json2bean.newversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.shopping.bean.BaseBean;
import com.laka.live.shopping.bean.newversion.ShoppingCartCountBean;

import java.io.Serializable;

/**
 * @ClassName: JTBShoppingCartCount
 * @Description: 购物车商品数量
 * @Author: chuan
 * @Version: 1.0
 * @Date: 27/07/2017
 */

public class JTBShoppingCartCount extends BaseBean implements Serializable {
    @Expose
    @SerializedName("data")
    private ShoppingCartCountBean data;

    public ShoppingCartCountBean getData() {
        return data;
    }

    public void setData(ShoppingCartCountBean data) {
        this.data = data;
    }
}
