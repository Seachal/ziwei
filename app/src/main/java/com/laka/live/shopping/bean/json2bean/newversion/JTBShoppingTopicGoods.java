package com.laka.live.shopping.bean.json2bean.newversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.shopping.bean.BaseBean;
import com.laka.live.shopping.bean.newversion.ShoppingTopicGoodsBean;

/**
 * @ClassName: JTBShoppingTopicGoods
 * @Description: 专题商品列表
 * @Author: chuan
 * @Version: 1.0
 * @Date: 27/07/2017
 */

public class JTBShoppingTopicGoods extends BaseBean {
    @Expose
    @SerializedName("data")
    private ShoppingTopicGoodsBean data;

    public ShoppingTopicGoodsBean getData() {
        return data;
    }

    public void setData(ShoppingTopicGoodsBean data) {
        this.data = data;
    }
}
