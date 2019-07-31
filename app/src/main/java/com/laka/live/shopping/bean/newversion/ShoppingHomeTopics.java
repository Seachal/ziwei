package com.laka.live.shopping.bean.newversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @ClassName: ShoppingHomeTopics
 * @Description: 首页话题
 * @Author: chuan
 * @Version: 1.0
 * @Date: 26/07/2017
 */

public class ShoppingHomeTopics implements Serializable{
    @Expose
    @SerializedName("topicId")
    private int topicId;

    @Expose
    @SerializedName("title")
    private String title;

    @Expose
    @SerializedName("coverUrl")
    private String coverUrl;

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
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

    @Override
    public String toString() {
        return "ShoppingHomeTopics{" +
                "topicId=" + topicId +
                ", title='" + title + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                '}';
    }
}
