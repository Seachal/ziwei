package com.laka.live.shopping.bean.newversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

/**
 * @ClassName: GoodsCate
 * @Description: 首页一级分类
 * @Author: chuan
 * @Version: 1.0
 * @Date: 26/07/2017
 */

public class GoodsCate {
    @Expose
    @SerializedName(Common.CATE_ID)
    private int cateId;

    @Expose
    @SerializedName("title")
    private String title;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("imageUrl")
    private String imageUrl;

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "GoodsCate{" +
                "cateId=" + cateId +
                ", title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
