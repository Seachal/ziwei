package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

import java.util.List;

/**
 * Created by Lyf on 2017/9/18.
 */

public class ContentBean {

    @Expose
    @SerializedName(Common.TITLE)
    private String title;

    @Expose
    @SerializedName(Common.TYPE)
    private int type;

    @Expose
    @SerializedName("items")
    private List<Content> items;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Content> getItems() {
        return items;
    }

    public void setItems(List<Content> items) {
        this.items = items;
    }

    //直播课程
    public boolean isLive() {
        return type == 1;
    }

    //视频课程
    public boolean isVideo() {
        return type == 2;
    }

    //资讯
    public boolean isNews() {
        return type == 3;
    }

    //优选专题
    public boolean isTopic() {
        return type == 4;
    }
}
