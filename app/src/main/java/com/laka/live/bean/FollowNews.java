package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

/**
 * @ClassName: FollowNews
 * @Description: 关注动态
 * @Author: chuan
 * @Version: 1.0
 * @Date: 6/7/17
 */

public class FollowNews {
    private final static String LIVE = "直播";
    private final static String VIDEO = "视频";
    private final static String NEWS = "资讯";

    public final static int TYPE_LIVE = 1;
    public final static int TYPE_VIDEO = 2;
    public final static int TYPE_NEWS = 3;

    @Expose
    @SerializedName(Common.ID)
    private int id;

    @Expose
    @SerializedName(Common.USER)
    private User user;

    @Expose
    @SerializedName(Common.TYPE)
    private int type;

    @Expose
    @SerializedName(Common.COURSE)
    private CourseTailer courseTailer;

    @Expose
    @SerializedName(Common.NEWS)
    private News news;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public CourseTailer getCourseTailer() {
        return courseTailer;
    }

    public void setCourseTailer(CourseTailer courseTailer) {
        this.courseTailer = courseTailer;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public String getTypeStr() {
        switch (type) {
            case TYPE_LIVE:
                return LIVE;
            case TYPE_VIDEO:
                return VIDEO;
            default:
                return NEWS;
        }
    }


    @Override
    public String toString() {
        return "FollowNews{" +
                "id=" + id +
                ", user=" + user +
                ", type=" + type +
                ", courseTailer=" + courseTailer +
                ", news=" + news +
                '}';
    }
}
