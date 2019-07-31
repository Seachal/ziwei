package com.laka.live.video.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.msg.ListMag;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:Rayman
 * @Date:2018/8/9
 * @Description:小视频评论接口中间层Bean，真正的评论内容在VideoCommentInfo里面
 */

public class VideoCommentBean extends ListMag<VideoCommentInfo> {

    @Expose
    @SerializedName("total_count")
    private int totalCount;

    @Expose
    @SerializedName("comments")
    private List<VideoCommentInfo> comments;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }


    public List<VideoCommentInfo> getComments() {
        if (comments == null) {
            return new ArrayList<>();
        }
        return comments;
    }

    public void setComments(List<VideoCommentInfo> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "VideoCommentBean{" +
                "totalCount=" + totalCount +
                ", comments=" + comments +
                '}';
    }

    @Override
    public List<VideoCommentInfo> getList() {
        return comments;
    }
}
