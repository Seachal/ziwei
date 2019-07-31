package com.laka.live.bean;
/*
 * @ClassName: CommentInfo
 * @Description: 评论实体 , 继承自BaseComment
 * @Author: chuan
 * @Version: 1.0
 * @Date: 11/23/16
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

import java.util.List;


public class CommentInfo extends BaseComment {

    @Expose
    @SerializedName(Common.REPLY_COUNT)
    private int replyCount;

    @Expose
    @SerializedName(Common.REPLIES)
    private List<ReplyInfo> replys;

    public List<ReplyInfo> getReplys() {
        return replys;
    }

    public void setReplys(List<ReplyInfo> replys) {
        this.replys = replys;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }
}
