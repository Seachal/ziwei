package com.laka.live.shopping.search.bean;

/**
 * @ClassName:
 * @Description:
 * @Author: chuan
 * @Version: 1.0
 * @Date: 5/17/17
 */

public class UserSearchBean {
    private String id;
    private String nickname;
    private int certificateStatus;
    private String headimageUrl;
    private String level;
    private int sex;
    private String signature;
    private int isVip;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getCertificateStatus() {
        return certificateStatus;
    }

    public void setCertificateStatus(int certificateStatus) {
        this.certificateStatus = certificateStatus;
    }

    public String getHeadimageUrl() {
        return headimageUrl;
    }

    public void setHeadimageUrl(String headimageUrl) {
        this.headimageUrl = headimageUrl;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public boolean isApproved() {
        return certificateStatus == 2;
    }

    public boolean isVip() {
        return isVip == 1;
    }

    public void setVip(int vip) {
        this.isVip = vip;
    }
}
