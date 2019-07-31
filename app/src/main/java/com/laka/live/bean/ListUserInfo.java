package com.laka.live.bean;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.ui.widget.MarkSimpleDraweeView;
import com.laka.live.util.Common;

import java.io.Serializable;

/**
 * Created by luwies on 16/3/22.
 */
public class ListUserInfo implements Serializable {


    public static final int GENDER_GIRL = 0;

    public static final int GENDER_BOY = 1;

    public static final int GENDER_UNKNOW = 255;

    public static final int FOLLOWED = 1;

    public static final int NO_FOLLOW = 0;

    @Expose
    @SerializedName(Common.ID)
    private int id;


    @Expose
    @SerializedName(Common.NICK_NAME)
    private String nickName;

    /**
     * 头像
     */
    @Expose
    @SerializedName(Common.AVATAR)
    private String avatar;

    @Expose
    @SerializedName(Common.DESCRIPTION)
    private String description;

    /**
     * 性别
     */
    @Expose
    @SerializedName(Common.GENDER)
    private int gender;


    @Expose
    @SerializedName(Common.LEVEL)
    private int level;

    @Expose
    @SerializedName(Common.FOLLOW)
    private int follow;

    /**
     * 贡献
     */
    @Expose
    @SerializedName(Common.EXTRA_COINS)
    private int extraCoins;

    @Expose
    @SerializedName(Common.APPLY_VERIFIED)
    private String applyVerified;

    @Expose
    @SerializedName(Common.VERIFIED)
    private String verified;

    @Expose
    @SerializedName(Common.STAR_VERIFIED)
    private String starVerified;


    @Expose
    @SerializedName(Common.AUTH)
    private short auth;

    @Expose
    @SerializedName(Common.IS_LIVE)
    private int is_live;

    @SerializedName(Common.DOWN_URL)
    @Expose
    private String downUrl;//播放地址

    @SerializedName(Common.STREAM_ID)
    @Expose
    private String streamId;

    @SerializedName(Common.CHANNEL_ID)
    @Expose
    private String channelId;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }


    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public boolean isLive() {
        return is_live == 1;
    }

    public void setIs_live(int is_live) {
        this.is_live = is_live;
    }

    public int getId() {
        return id;
    }

    public String getIdStr() {
        return String.valueOf(id);
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public boolean isFollow() {
        return follow == FOLLOWED;
    }

    public int getExtraCoins() {
        return extraCoins;
    }

    public void setExtraCoins(int extraCoins) {
        this.extraCoins = extraCoins;
    }

    public String getApplyVerified() {
        return applyVerified;
    }

    public void setApplyVerified(String applyVerified) {
        this.applyVerified = applyVerified;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getStarVerified() {
        return starVerified;
    }

    public void setStarVerified(String starVerified) {
        this.starVerified = starVerified;
    }

    public String getVerifyInfo() {
        if (TextUtils.isEmpty(starVerified) == false) {
            return starVerified;
        } else if (TextUtils.isEmpty(verified) == false) {
            return verified;
        } else {
            return applyVerified;
        }
    }

    // 是否已经
    public boolean isVerified() {

        if (TextUtils.isEmpty(starVerified)
                && TextUtils.isEmpty(verified))
            return false;
        else
            return true;
    }

    public short getAuth() {
        return auth;
    }

    public void setAuth(short auth) {
        this.auth = auth;
    }

    public boolean isMyself() {
        return AccountInfoManager.getInstance().getCurrentAccountUserId() == id;
    }


}
