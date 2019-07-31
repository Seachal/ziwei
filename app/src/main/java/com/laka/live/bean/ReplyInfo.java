package com.laka.live.bean;
/*
 * @ClassName: ReplyInfo
 * @Description: 回复实体 ， 继承自BaseComment
 * @Author: chuan
 * @Version: 1.0
 * @Date: 11/23/16
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

public class ReplyInfo extends BaseComment {

    @Expose
    @SerializedName(Common.PARENT_ID)
    private int parentId;

    @Expose
    @SerializedName(Common.REPLY_ID)
    private int replyId;

    @Expose
    @SerializedName(Common.TO_USERID)
    private int toUserid;

    @Expose
    @SerializedName(Common.TO_NICKNAME)
    private String toNickname;

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public int getToUserid() {
        return toUserid;
    }

    public void setToUserid(int toUserid) {
        this.toUserid = toUserid;
    }

    public String getToNickname() {
        return toNickname;
    }

    public void setToNickname(String toNickname) {
        this.toNickname = toNickname;
    }

    @Override
    public String toString() {
        return "ReplyInfo{" +
                "parentId=" + parentId +
                ", replyId=" + replyId +
                ", toUserid=" + toUserid +
                ", toNickname='" + toNickname + '\'' +
                '}';
    }
}
