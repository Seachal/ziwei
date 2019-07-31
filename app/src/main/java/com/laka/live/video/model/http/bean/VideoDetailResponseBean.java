package com.laka.live.video.model.http.bean;

import com.google.gson.annotations.SerializedName;
import com.laka.live.msg.Msg;
import com.laka.live.util.Common;
import com.laka.live.video.model.bean.MiniVideoBean;

/**
 * @Author:Rayman
 * @Date:2018/8/22
 * @Description:小视频详情页ResponseBean
 */

public class VideoDetailResponseBean extends Msg {

    @SerializedName(Common.DATA)
    private MiniVideoBean data;

    public MiniVideoBean getData() {
        return data;
    }

    public void setData(MiniVideoBean data) {
        this.data = data;
    }
}
