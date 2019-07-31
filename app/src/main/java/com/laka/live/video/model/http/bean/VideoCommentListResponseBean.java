package com.laka.live.video.model.http.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.msg.Msg;
import com.laka.live.util.Common;
import com.laka.live.video.model.bean.VideoCommentBean;

/**
 * @Author:Rayman
 * @Date:2018/8/13
 * @Description: 小视频评论ResponseBean
 */

public class VideoCommentListResponseBean extends Msg {

    @Expose
    @SerializedName(Common.DATA)
    private VideoCommentBean data;

    public VideoCommentBean getData() {
        return data;
    }

    public void setData(VideoCommentBean comment) {
        this.data = comment;
    }
}
