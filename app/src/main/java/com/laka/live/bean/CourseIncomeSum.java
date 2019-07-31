package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @ClassName: CourseIncomeSum
 * @Description: 课程收入总计
 * @Author: chuan
 * @Version: 1.0
 * @Date: 01/08/2017
 */

public class CourseIncomeSum {
    @Expose
    @SerializedName("total")
    private String total;

    @Expose
    @SerializedName("live")
    private String live;

    @Expose
    @SerializedName("video")
    private String video;

    @Expose
    @SerializedName("agent")
    private String agent;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getLive() {
        return live;
    }

    public void setLive(String live) {
        this.live = live;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    @Override
    public String toString() {
        return "CourseIncomeSum{" +
                "total='" + total + '\'' +
                ", live='" + live + '\'' +
                ", video='" + video + '\'' +
                ", agent='" + agent + '\'' +
                '}';
    }
}
