package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.account.replay.ReplayConstant;
import com.laka.live.util.Common;

/**
 * Created by luwies on 16/3/28.
 */
public class Video {

    public static final int VIDEO_STATE_TRANS_CODING=1;
    public static final int VIDEO_STATE_TRANS_FINISH=2;

    @SerializedName(Common.STREAM_ID)
    @Expose
    private String streamId;

    @SerializedName(Common.CHANNEL_ID)
    @Expose
    private String channelId;

    @Expose
    @SerializedName(Common.USER_ID)
    private int id;
    @Expose
    @SerializedName(Common.AVATAR)
    private String avatar;

    @Expose
    @SerializedName(Common.COVER)
    public String cover;

    @Expose
    @SerializedName(Common.LEVEL)
    private int level;

    @Expose
    @SerializedName(Common.AUTH)
    private int auth;

    @Expose
    @SerializedName(Common.TIME)
    private String time;

    @Expose
    @SerializedName(Common.TITLE)
    private String title;

    @Expose
    @SerializedName(Common.VIEWS)
    private int views;

    @Expose
    @SerializedName(Common.URL)
    private String url;

    @Expose
    @SerializedName(Common.RECV_COINS)
    private int recvCoins;

    @Expose
    @SerializedName(Common.VID)
    private String vid;
    @Expose
    @SerializedName(Common.IS_LIVE)
    private int is_live;

    @SerializedName(Common.DOWN_URL)
    @Expose
    private String downUrl;//播放地址
    @Expose
    @SerializedName(Common.LOCATION)
    private String location;

    @Expose
    @SerializedName(Common.STATE)
    private int state = VIDEO_STATE_TRANS_FINISH;


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
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public boolean isLive() {
        return is_live == ReplayConstant.VIDEO_TYPE_LIVE;
    }

    public void setIs_live(int is_live) {
        this.is_live = is_live;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRecvCoins() {
        return recvCoins;
    }

    public void setRecvCoins(int recvCoins) {
        this.recvCoins = recvCoins;
    }

    public int getAuth() {
        return auth;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }
}
