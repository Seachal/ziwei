package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.util.Common;

import java.io.Serializable;

/**
 * Created by zwl on 2016/6/28.
 * Email-1501448275@qq.com
 */
public class RankingUserInfo implements Serializable {
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
    @SerializedName(Common.FANS)
    private int fans;

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
    private int extra_coins;

    @Expose
    @SerializedName(Common.VERIFIED)
    private String verified;

    @Expose
    @SerializedName(Common.STAR_VERIFIED)
    private String starVerified;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
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

    public int getExtra_coins() {
        return extra_coins;
    }

    public void setExtra_coins(int extra_coins) {
        this.extra_coins = extra_coins;
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

    public boolean isMyself() {
        return AccountInfoManager.getInstance().getCurrentAccountUserId() == id;
    }
}
