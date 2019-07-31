package com.laka.live.bean;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.R;
import com.laka.live.ui.adapter.TopicsAdapter;
import com.laka.live.util.Common;

import java.io.Serializable;

/**
 * n
 * Created by luwies on 16/6/29.
 */
public class VideoLine implements Serializable {


    @Expose
    @SerializedName(Common.NAME)
    private String name;

    @Expose
    @SerializedName(Common.URL)
    private String url;

    public boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "VideoLine{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}
