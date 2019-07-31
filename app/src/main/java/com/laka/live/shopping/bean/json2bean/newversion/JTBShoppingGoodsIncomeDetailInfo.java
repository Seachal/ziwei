package com.laka.live.shopping.bean.json2bean.newversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.shopping.bean.BaseBean;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsIncomeDetail;

/**
 * @ClassName: JTBShoppingGoodsIncomeDetailInfo
 * @Description: 商品收益详情
 * @Author: chuan
 * @Version: 1.0
 * @Date: 29/08/2017
 */

public class JTBShoppingGoodsIncomeDetailInfo extends BaseBean {
    @Expose
    @SerializedName("data")
    private ShoppingGoodsIncomeDetail data;

    public ShoppingGoodsIncomeDetail getData() {
        return data;
    }

    public void setData(ShoppingGoodsIncomeDetail data) {
        this.data = data;
    }
}
