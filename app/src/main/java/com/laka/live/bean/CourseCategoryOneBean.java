package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

/**
 * Created by Lyf on 2017/9/18.
 */
public class CourseCategoryOneBean {

    @Expose
    @SerializedName(Common.CATE_ID)
    private int cateId;

    @Expose
    @SerializedName(Common.NAME)
    private String name;

    @Expose
    @SerializedName("thumbUrl")
    private String thumbUrl;

    public int getId() {
        return cateId;
    }

    public void setId(int cateId) {
        this.cateId = cateId;
    }

    public String getName() {
        if(name == null) {
            return "";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbUrl() {
        if(thumbUrl == null) {
            return "";
        }
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

}
