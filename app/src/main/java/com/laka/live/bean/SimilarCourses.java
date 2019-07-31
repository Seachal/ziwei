package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

/**
 * Created by Lyf on 2017/4/7.
 */

public class SimilarCourses{

    @Expose
    @SerializedName(Common.ID)
    public int id; // 课程id

    @Expose
    @SerializedName(Common.TYPE)
    public int type; // 课程类型，1=直播，2=视频

    @Expose
    @SerializedName(Common.PRICE)
    public int price; // 课程价格

    @Expose
    @SerializedName(Common.STATUS)
    public int status; // 课程状态，10=未开播，20=准备开播，30=调整直播时间中，40=直播中，100=直播结束

    @Expose
    @SerializedName(Common.START_TIME)
    public int start_time; // 开播时间，时间戳

    @Expose
    @SerializedName(Common.BUYER_COUNT)
    public int buyer_count; // 购买人数

    @Expose
    @SerializedName(Common.TITLE)
    public String title; // 课程名称

    @Expose
    @SerializedName(Common.COVER_URL)
    public String  cover_url; // 课程封面url

    @Expose
    @SerializedName(Common.STATUS_TEXT)
    public String  status_text; // 课程状态文本


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }

    public String getBuyer_count() {
        return buyer_count + "人已购买";
    }

    public void setBuyer_count(int buyer_count) {
        this.buyer_count = buyer_count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getStatus_text() {
        return status_text;
    }

    public void setStatus_text(String status_text) {
        this.status_text = status_text;
    }

}
