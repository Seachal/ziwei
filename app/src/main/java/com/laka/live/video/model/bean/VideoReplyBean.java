package com.laka.live.video.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @Author:Rayman
 * @Date:2018/8/9
 * @Description:小视频评论回复Bean
 */

public class VideoReplyBean {

    @Expose
    @SerializedName("total_count")
    private int totalCount;

    @Expose
    @SerializedName("replies")
    private List<VideoReplyInfo> replies;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<VideoReplyInfo> getReplies() {
        return replies;
    }

    public void setReplies(List<VideoReplyInfo> replies) {
        this.replies = replies;
    }

    @Override
    public String toString() {
        return "VideoReplyBean{" +
                "totalCount=" + totalCount +
                ", replies=" + replies +
                '}';
    }
}
