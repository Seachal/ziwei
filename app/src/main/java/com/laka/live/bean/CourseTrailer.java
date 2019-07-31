package com.laka.live.bean;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;
import com.laka.live.util.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lyf on 2017/4/7.
 */
public class CourseTrailer implements Serializable {

    @Expose
    @SerializedName(Common.ID)
    public long id; // 课程预告id

    @Expose
    @SerializedName(Common.DISCOUNT)
    public float discount = 10; // 折扣

    @Expose
    @SerializedName(Common.SAVED_COINS)
    public float savedCoins; // 购买整套的折扣

    @Expose
    @SerializedName(Common.TOPICS)
    public List<Topic> topics; // 话题数组

    @Expose
    @SerializedName(Common.VIDEO_URL)
    public String video_url; // 课程类型为视频时必填，对应OSS的Object名称，例如：live/video.mp4

    @Expose
    @SerializedName(Common.SNAPSHOT_URL)
    public String snapshot_url; // 课程预告视频截图url

    @Expose
    @SerializedName(Common.DISCOUNT_DEADLINE)
    private long discount_deadline; // 折扣截至时间，时间戳

    @Expose
    @SerializedName(Common.DISCOUNT_REMAINING_TIME)
    private long discount_remaining_time; // 折扣剩余时间，时间戳

    @Expose
    @SerializedName("time_limit_type")
    private int time_limit_type;

    @Expose
    @SerializedName("discount_type")
    public String discount_type = "1"; // 折扣类型，1=普通折扣，2=限时折扣

    public String local_video_url; // 课程视频，本地缓存用

    public String getTopicsFormat(Context context) {
        if (!Utils.listIsNullOrEmpty(topics)) {
            StringBuilder sb = new StringBuilder();
            for (Topic item :
                    topics) {
                sb.append(item.getFormatName(context));
                sb.append(" ");
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    public String getId() {
        return String.valueOf(id);
    }

    public boolean isDiscount() {
        return discount < 10;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public String getLocalVideoUrl(){
        return local_video_url;
    }

    public void setLocalVideoUrl(String local_video_url) {
        this.local_video_url = local_video_url;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    // 设置预告内容
    public void setCourseTrailer(CourseTrailer courseTrailer) {

        this.id = courseTrailer.id;
        this.topics = courseTrailer.topics == null ? new ArrayList<Topic>() : courseTrailer.topics; // 确定这个值不为空
        this.discount = courseTrailer.discount;
        this.video_url = courseTrailer.video_url;
        this.savedCoins = courseTrailer.savedCoins;
        this.snapshot_url = courseTrailer.snapshot_url;
        this.local_video_url = courseTrailer.local_video_url;
    }

    public long getDiscountDeadline() {
        return discount_deadline;
    }

    public void setDiscount_deadline(long discount_deadline) {
        this.discount_deadline = discount_deadline;
    }

    public long getDiscountRemainingTime() {
        return discount_remaining_time;
    }

    public void setDiscount_remaining_time(long discount_remaining_time) {
        this.discount_remaining_time = discount_remaining_time;
    }


    public String getDiscountType() {
        return discount_type;
    }

    public void setDiscountType(int discount_type) {
        this.discount_type = String.valueOf(discount_type);
    }

    public boolean isOpenLimitDiscount() {
        return Integer.valueOf(discount_type) == Course.OPENEDLIMITISCOUNT;
    }

    public int getTime_limit_type() {
        return time_limit_type;
    }

    public void setTime_limit_type(int time_limit_type) {
        this.time_limit_type = time_limit_type;
    }

    public String getVideoUrl() {
        return video_url;
    }

    public float getSavedCoins() {
        return savedCoins;
    }
}
