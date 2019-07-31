package com.laka.live.video.model.http.bean;

import com.google.gson.annotations.SerializedName;
import com.laka.live.msg.Msg;
import com.laka.live.util.Common;

/**
 * @Author:Rayman
 * @Date:2018/8/22
 * @Description:小视频点赞ResponseBean
 */

public class VideoLikeResponseBean extends Msg {

    @SerializedName(Common.DATA)
    private boolean isLike;

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }
}
