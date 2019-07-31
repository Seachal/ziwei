package com.laka.live.shopping.bean.newversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @ClassName: ShoppingCategoryBean
 * @Description: 子分类数组
 * @Author: chuan
 * @Version: 1.0
 * @Date: 27/07/2017
 */

public class ShoppingCategoryBean {
    @Expose
    @SerializedName("categories")
    private List<GoodsCate> categories;

    public List<GoodsCate> getCategories() {
        return categories;
    }

    public void setCategories(List<GoodsCate> categories) {
        this.categories = categories;
    }
}
