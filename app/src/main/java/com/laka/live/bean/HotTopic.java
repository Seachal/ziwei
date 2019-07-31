package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

/**
 * @ClassName: HotTopic
 * @Description: 首页热门话题
 * @Author: chuan
 * @Version: 1.0
 * @Date: 6/2/17
 */

public class HotTopic {
    @Expose
    @SerializedName(Common.ID)
    private String id;

    @Expose
    @SerializedName(Common.NAME)
    private String name;

    @Expose
    @SerializedName(Common.COVER_URL)
    private String coverUrl;

    @Expose
    @SerializedName(Common.COURSE_COUNT)
    private int courseCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public int getCourseCount() {
        return courseCount;
    }

    public void setCourseCount(int courseCount) {
        this.courseCount = courseCount;
    }

    @Override
    public String toString() {
        return "HotTopic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", courseCount=" + courseCount +
                '}';
    }
}
