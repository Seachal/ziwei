package com.laka.live.video.model.http.bean;

import com.google.gson.annotations.SerializedName;
import com.laka.live.msg.Msg;

/**
 * @Author:Rayman
 * @Date:2018/8/7
 * @Description:
 */

public class MaterialDeleteResponseBean extends Msg {

    @SerializedName("data")
    public boolean isDelete;

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }
}
