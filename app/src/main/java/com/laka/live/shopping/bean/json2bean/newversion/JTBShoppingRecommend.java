package com.laka.live.shopping.bean.json2bean.newversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.shopping.bean.BaseBean;
import com.laka.live.shopping.bean.newversion.ShoppingRecommendBean;

/**
 * @ClassName: JTBShoppingRecommend
 * @Description: 商品推荐
 * @Author: chuan
 * @Version: 1.0
 * @Date: 14/07/2017
 */
public class JTBShoppingRecommend extends BaseBean {
    @Expose
    @SerializedName("data")
    private ShoppingRecommendBean data;

    public ShoppingRecommendBean getData() {
        return data;
    }

    public void setData(ShoppingRecommendBean data) {
        this.data = data;
    }
}
