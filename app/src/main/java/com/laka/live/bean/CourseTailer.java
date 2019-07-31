package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

/**
 * @ClassName: CourseTailer
 * @Description: 关注动态返回的课程
 * @Author: chuan
 * @Version: 1.0
 * @Date: 6/7/17
 */

public class CourseTailer {

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
    @SerializedName(Common.CREATE_TIME)
    private String createTime;

    @Expose
    @SerializedName(Common.VIDEO_URL)
    private String videoUrl;

    @Expose
    @SerializedName(Common.PRICE)
    private float price;

    @Expose
    @SerializedName(Common.ACTUAL_PRICE)
    private float actualPrice;

    @Expose
    @SerializedName(Common.DISCOUNT)
    private float discount;

    @Expose
    @SerializedName(Common.NUM)
    private int num;

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(float actualPrice) {
        this.actualPrice = actualPrice;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }


    @Override
    public String toString() {
        return "CourseTailer{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", createTime='" + createTime + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", price=" + price +
                ", actualPrice=" + actualPrice +
                ", discount=" + discount +
                ", num=" + num +
                '}';
    }
}
