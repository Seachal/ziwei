package com.laka.live.shopping.bean.json2bean.newversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.shopping.bean.BaseBean;
import com.laka.live.shopping.bean.newversion.ShoppingCategoryBean;

/**
 * @ClassName: JTBShoppingCategory
 * @Description: 子分类列表
 * @Author: chuan
 * @Version: 1.0
 * @Date: 27/07/2017
 */

public class JTBShoppingCategory extends BaseBean {
    @Expose
    @SerializedName("data")
    private ShoppingCategoryBean data;

    public ShoppingCategoryBean getData() {
        return data;
    }

    public void setData(ShoppingCategoryBean data) {
        this.data = data;
    }
}
