package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

/**
 * Created by luwies on 16/3/31.
 */
public class HomeTitle {

    @Expose
    @SerializedName(Common.TITLE)
    private String title;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
