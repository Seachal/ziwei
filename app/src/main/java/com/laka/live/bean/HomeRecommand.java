package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName:
 * @Description:
 * @Author: chuan
 * @Version: 1.0
 * @Date: 4/13/17
 */

public class HomeRecommand implements Serializable {
    @Expose
    @SerializedName(Common.ID)
    public int id; // 课程id

    @Expose
    @SerializedName(Common.TITLE)
    public String title; // 课程名

    @Expose
    @SerializedName(Common.PRICE)
    public int price; // 课程价格

    @Expose
    @SerializedName(Common.START_TIME)
    public int start_time; // 开播时间，时间戳，直播课程时此参数必填

    @Expose
    @SerializedName(Common.TYPE)
    public int type; // 直播类型 1 == 直播，2 == 视频

    @Expose
    @SerializedName(Common.COVER_URL)
    public String cover_url; // 课程封面url，对应OSS的Object名称，例如：live/image1.jpg

    @Expose
    @SerializedName(Common.STATUS)
    public int status; // 课程状态，10=未开播，20=准备开播，30=调整直播时间中，40=直播中，50=生成回放中，60=已生成回放，70=课程视频已可播放

    @Expose
    @SerializedName(Common.STATUS_TEXT)
    public String status_text; // 课程状态文本

    @Expose
    @SerializedName(Common.RECV_LIKES)
    public int recvLikes;

    @Expose
    @SerializedName(Common.DURATION)
    public int duration;

    @Expose
    @SerializedName(Common.BUYER_COUNT)
    public int buyer_count; // 购买人数

    @Expose
    @SerializedName(Common.USER)
    public User user;

    @Expose
    @SerializedName(Common.TOPICS)
    public List<Topic> topics;

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatus_text() {
        return status_text;
    }

    public void setStatus_text(String status_text) {
        this.status_text = status_text;
    }

    public int getRecvLikes() {
        return recvLikes;
    }

    public void setRecvLikes(int recvLikes) {
        this.recvLikes = recvLikes;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getBuyer_count() {
        return buyer_count;
    }

    public void setBuyer_count(int buyer_count) {
        this.buyer_count = buyer_count;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }
}
