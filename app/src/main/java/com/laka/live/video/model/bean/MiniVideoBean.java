package com.laka.live.video.model.bean;

import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:Rayman
 * @Date:2018/8/2
 * @Description:视频列表Bean
 */

public class MiniVideoBean implements Serializable {

    @SerializedName("id")
    private int videoId;
    @SerializedName("title")
    private String videoTitle;
    @SerializedName("snapshot_url")
    private String videoCover;
    @SerializedName("video_duration")
    private String videoDuration;
    @SerializedName("video_mp4_url")
    private String videoUrl;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("width")
    private int videoWidth;
    @SerializedName("height")
    private int videoHeight;
    @SerializedName("user")
    private User user;
    @SerializedName("praise_count")
    private String likeCount;
    @SerializedName("comments")
    private String commentCount;
    @SerializedName("followed")
    private boolean isFollow;
    @SerializedName("good_count")
    private String recommendGoodsCount;
    @SerializedName("rec_goods")
    private List<VideoRecommendGoods> recommendGoodsList;
    @SerializedName("liked")
    private boolean isLike;
    private String shareUrl;
    private int videoProgress;

    //本地加载之后的Bitmap
//    private byte[] localCover;

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getVideoTitle() {
        return videoTitle == null ? "" : videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoCover() {
        return videoCover == null ? "" : videoCover;
    }

    public void setVideoCover(String videoCover) {
        this.videoCover = videoCover;
    }

    public String getVideoDuration() {
        return videoDuration == null ? "" : videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    public String getVideoUrl() {
        return videoUrl == null ? "" : videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCreateTime() {
        return createTime == null ? "0" : createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLikeCount() {
        return likeCount == null ? "" : likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getCommentCount() {
        return commentCount == null ? "" : commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public String getRecommendGoodsCount() {
        return recommendGoodsCount == null ? "" : recommendGoodsCount;
    }

    public void setRecommendGoodsCount(String recommendGoodsCount) {
        this.recommendGoodsCount = recommendGoodsCount;
    }

    public String getShareUrl() {
        return shareUrl == null ? "" : shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }

    public List<VideoRecommendGoods> getRecommendGoodsList() {
        if (recommendGoodsList == null) {
            return new ArrayList<>();
        }
        return recommendGoodsList;
    }

    public void setRecommendGoodsList(List<VideoRecommendGoods> recommendGoodsList) {
        this.recommendGoodsList = recommendGoodsList;
    }

    public int getVideoProgress() {
        return videoProgress;
    }

    public void setVideoProgress(int videoProgress) {
        this.videoProgress = videoProgress;
    }

    //不能直接传Bitmap，就转换成Byte数组
//    public Bitmap getLocalCover() {
//        if (localCover != null) {
//            return BitmapFactory.decodeByteArray(localCover, 0, localCover.length);
//        } else {
//            return null;
//        }
//    }
//
//    public void setLocalCover(Bitmap localCover) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        localCover.compress(Bitmap.CompressFormat.PNG, 0, baos);
//        this.localCover = baos.toByteArray();
//    }
}
