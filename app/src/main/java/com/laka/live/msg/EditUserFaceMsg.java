package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

/**
 * Created by luwies on 16/3/19.
 */
public class EditUserFaceMsg extends Msg {

    /**
     * 头像
     */
    @Expose
    @SerializedName(Common.AVATAR)
    private String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
