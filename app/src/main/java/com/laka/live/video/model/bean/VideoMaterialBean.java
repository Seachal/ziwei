package com.laka.live.video.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.laka.live.util.TimeUtil;

import java.io.Serializable;

/**
 * @Author:Rayman
 * @Date:2018/8/3
 * @Description:小视频素材Bean
 */

public class VideoMaterialBean implements Serializable {

    @SerializedName("id")
    private String videoId;
    @SerializedName("snapshot_url")
    private String videoCover;
    @SerializedName("title")
    private String videoTitle;
    @SerializedName("video_duration")
    private String videoDuration;
    @SerializedName("video_mp4_url")
    private String videoUrl;
    private boolean isCheck = false;

    public VideoMaterialBean() {
    }

    protected VideoMaterialBean(Parcel in) {
        videoId = in.readString();
        videoCover = in.readString();
        videoTitle = in.readString();
        videoDuration = in.readString();
        isCheck = in.readByte() != 0;
    }

    public String getVideoId() {
        return videoId == null ? "" : videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoCover() {
        return videoCover == null ? "" : videoCover;
    }

    public void setVideoCover(String videoCover) {
        this.videoCover = videoCover;
    }

    public String getVideoTitle() {
        return videoTitle == null ? "" : videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoDuration() {
        return videoDuration == null ? "0" : videoDuration;
    }

    public String getVideoDurationStr() {
        return TimeUtil.covertSecondToHMS(getVideoDuration());
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

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    @Override
    public String toString() {
        return "VideoMaterialBean{" +
                "videoId='" + videoId + '\'' +
                ", videoCover='" + videoCover + '\'' +
                ", videoTitle='" + videoTitle + '\'' +
                ", videoDuration='" + videoDuration + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", isCheck=" + isCheck +
                '}';
    }
}
