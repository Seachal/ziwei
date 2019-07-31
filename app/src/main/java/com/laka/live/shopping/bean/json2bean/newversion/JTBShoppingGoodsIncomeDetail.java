package com.laka.live.shopping.bean.json2bean.newversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.shopping.bean.BaseBean;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsIncomeDetailsBean;

/**
 * @ClassName: JTBShoppingGoodsIncomeDetail
 * @Description: 商品收益详情
 * @Author: chuan
 * @Version: 1.0
 * @Date: 29/08/2017
 */

public class JTBShoppingGoodsIncomeDetail extends BaseBean {
    @Expose
    @SerializedName("data")
    private ShoppingGoodsIncomeDetailsBean data;

    public ShoppingGoodsIncomeDetailsBean getData() {
        return data;
    }

    public void setData(ShoppingGoodsIncomeDetailsBean data) {
        this.data = data;
    }
}
