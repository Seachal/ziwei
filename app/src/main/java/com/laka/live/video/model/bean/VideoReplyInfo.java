package com.laka.live.video.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.ReplyInfo;
import com.laka.live.util.Common;

/**
 * @Author:Rayman
 * @Date:2018/8/21
 * @Description:小视频回复评论实体类，真正参与逻辑调用的类
 */

public class VideoReplyInfo extends ReplyInfo {

    @Expose
    @SerializedName(Common.MINI_VIDEO_ID)
    private int videoId;

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }
}
