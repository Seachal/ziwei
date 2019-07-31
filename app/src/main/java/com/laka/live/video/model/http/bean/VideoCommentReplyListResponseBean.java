package com.laka.live.video.model.http.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.msg.Msg;
import com.laka.live.util.Common;
import com.laka.live.video.model.bean.VideoReplyBean;

/**
 * @Author:Rayman
 * @Date:2018/8/13
 * @Description: 小视频评论回复ResponseBean
 */

public class VideoCommentReplyListResponseBean extends Msg {

    @Expose
    @SerializedName(Common.DATA)
    private VideoReplyBean data;

    public VideoReplyBean getData() {
        return data;
    }

    public void setData(VideoReplyBean mReply) {
        this.data = mReply;
    }
}
