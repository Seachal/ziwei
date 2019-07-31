package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

/**
 * @ClassName: News
 * @Description: 关注动态返回资讯
 * @Author: chuan
 * @Version: 1.0
 * @Date: 6/9/17
 */

public class News {
    @Expose
    @SerializedName(Common.ID)
    private int id;

    @Expose
    @SerializedName(Common.TITLE)
    private String title;

    @Expose
    @SerializedName(Common.COVER_URL)
    private String coverUrl;

    @Expose
    @SerializedName(Common.URL)
    private String url;

    @Expose
    @SerializedName(Common.CREATE_TIME)
    private int createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", url='" + url + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
