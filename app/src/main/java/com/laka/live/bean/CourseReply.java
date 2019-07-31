package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @ClassName:
 * @Description:
 * @Author: chuan
 * @Version: 1.0
 * @Date: 14/09/2017
 */

public class CourseReply {
    @Expose
    @SerializedName("total_count")
    private int totalCount;

    @Expose
    @SerializedName("replies")
    private List<ReplyInfo> replies;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<ReplyInfo> getReplies() {
        return replies;
    }

    public void setReplies(List<ReplyInfo> replies) {
        this.replies = replies;
    }

    @Override
    public String toString() {
        return "CourseReply{" +
                "totalCount=" + totalCount +
                ", replies=" + replies +
                '}';
    }
}
