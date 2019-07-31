package com.laka.live.shopping.bean.newversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @ClassName: ShoppingGoodsImageUrlBean
 * @Description: 图片url
 * @Author: chuan
 * @Version: 1.0
 * @Date: 27/07/2017
 */

public class ShoppingGoodsImageUrlBean implements Serializable {
    @Expose
    @SerializedName("imageUrl")
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
