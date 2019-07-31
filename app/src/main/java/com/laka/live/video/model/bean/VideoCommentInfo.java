package com.laka.live.video.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.CommentInfo;
import com.laka.live.util.Common;

/**
 * @Author:Rayman
 * @Date:2018/8/21
 * @Description:小视频评论Bean
 */

public class VideoCommentInfo extends CommentInfo {

    @Expose
    @SerializedName(Common.MINI_VIDEO_ID)
    private int videoId;

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    @Override
    public String toString() {
        return "VideoCommentInfo{" +
                "videoId=" + videoId +
                '}';
    }
}
