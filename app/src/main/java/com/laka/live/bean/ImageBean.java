package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

import java.io.Serializable;

/**
 * Created by Lyf on 2017/7/4.
 */

public class ImageBean implements Serializable{

    @Expose
    @SerializedName(Common.URL)
    private String url; // 图片地址

    @Expose
    @SerializedName(Common.W)
    private int width; // 图片宽度

    @Expose
    @SerializedName(Common.H)
    private int height; // 图片高度

    public ImageBean(String url, int width, int height) {
        this.url = url;
        this.width = width;
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
