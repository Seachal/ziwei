package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.R;
import com.laka.live.util.Common;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;

import java.io.Serializable;

/**
 * Created by Lyf on 2017/4/7.
 */

public class User implements Serializable {

    @Expose
    @SerializedName(Common.ID)
    public int id; // 用户id

    @Expose
    @SerializedName(Common.AVATAR)
    public String avatar; // 用户头像url

    @Expose
    @SerializedName(Common.NICK_NAME)
    public String nickname; // 用户昵称

    @Expose
    @SerializedName(Common.DESCRIPTION)
    public String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDescription() {
        return Utils.isEmpty(description) ? ResourceHelper.getString(R.string.default_sign) : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
