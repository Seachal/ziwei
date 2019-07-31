package com.laka.live.shopping.bean.json2bean.newversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.shopping.bean.BaseBean;
import com.laka.live.shopping.bean.newversion.ShoppingCategoryGoodsBean;

/**
 * @ClassName: JTBShoppingGoods
 * @Description:
 * @Author: chuan
 * @Version: 1.0
 * @Date: 27/07/2017
 */

public class JTBShoppingGoods extends BaseBean {
    @Expose
    @SerializedName("data")
    private ShoppingCategoryGoodsBean data;

    public ShoppingCategoryGoodsBean getData() {
        return data;
    }

    public void setData(ShoppingCategoryGoodsBean data) {
        this.data = data;
    }
}
