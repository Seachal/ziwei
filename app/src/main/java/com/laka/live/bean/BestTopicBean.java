package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Lyf on 2017/9/19.
 */

public class BestTopicBean {
    @Expose
    @SerializedName("topicId")
    private int topicId;

    @Expose
    @SerializedName("title")
    private String title;

    @Expose
    @SerializedName("coverUrl")
    private String coverUrl;

    @Expose
    @SerializedName("courses")
    private List<Course> list;

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


    public List<Course> getList() {
        return list;
    }
}
