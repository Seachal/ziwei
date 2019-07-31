package com.laka.live.shopping.bean.json2bean.newversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.shopping.bean.BaseBean;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsIncomeBean;

/**
 * @ClassName: JTBShoppingGoodsIncome
 * @Description: 商品收益
 * @Author: chuan
 * @Version: 1.0
 * @Date: 29/08/2017
 */

public class JTBShoppingGoodsIncome extends BaseBean {
    @Expose
    @SerializedName("data")
    private ShoppingGoodsIncomeBean data;

    public ShoppingGoodsIncomeBean getData() {
        return data;
    }

    public void setData(ShoppingGoodsIncomeBean data) {
        this.data = data;
    }
}
