package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

import java.io.Serializable;

/**
 * @ClassName:
 * @Description:
 * @Author: chuan
 * @Version: 1.0
 * @Date: 4/18/17
 */

public class BannerCourseRoom implements Serializable{
    @Expose
    @SerializedName(Common.STATE)
    private int state;
    @SerializedName(Common.DOWN_URL)
    @Expose
    private String downUrl;//播放地址

    @SerializedName(Common.STREAM_ID)
    @Expose
    private String streamId;

    @SerializedName(Common.CHANNEL_ID)
    @Expose
    private String channelId;

    @SerializedName(Common.BEGIN_TIME)
    @Expose
    private long beginTime;


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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }
}
