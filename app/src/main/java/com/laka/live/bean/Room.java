package com.laka.live.bean;

/**
 * Created by luwies on 16/3/29.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.R;
import com.laka.live.ui.widget.span.CustomTypefaceSpan;
import com.laka.live.util.Common;
import com.laka.live.util.TypefaceHelper;
import com.laka.live.util.Utils;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class Room extends BaseRoom implements Serializable {
    public final static int STATUS_LIVE_CANCEL = 1;
    public final static int STATUS_LIVE_NOT = 10;
    public final static int STATUS_LIVE_PREPARE = 20;
    public final static int STATUS_LIVE_CHANGE = 30;
    public final static int STATUS_LIVE_ING = 40;
    public final static int STATUS_LIVE_PLAYBACKING = 50;
    public final static int STATUS_LIVE_PLAYBACK = 60;
    public final static int STATUS_LIVE_CAN_PLAY = 70;

    @SerializedName(Common.NICK_NAME)
    @Expose
    private String nickName;

    @SerializedName(Common.ONLINES)
    @Expose
    private int onlines;

    @SerializedName(Common.LOCATION)
    @Expose
    private String location;

    @SerializedName(Common.COVER)
    @Expose
    private String screenShot;

    @SerializedName(Common.LEVEL)
    @Expose
    private int level;

    @SerializedName(Common.VERIFIED)
    @Expose
    private String verified;

    @SerializedName(Common.STAR_VERIFIED)
    @Expose
    private String starVerified;

    @Expose
    @SerializedName(Common.GENDER)
    private int gender;

    @Expose
    @SerializedName(Common.URL)
    private String videoUrl;

    @Expose
    @SerializedName(Common.VID)
    private String vid;

    @Expose
    @SerializedName(Common.IS_LIVE)
    private int liveTag;

    @Expose
    @SerializedName(Common.TOPICS)
    private List<Topic> topics;

    @Expose
    @SerializedName(Common.VIEWS)
    private int views;

    @Expose
    @SerializedName(Common.RECV_COINS)
    private int recvCoins;

    @Expose
    @SerializedName(Common.TIME)
    private String time;

    @Expose
    @SerializedName(Common.RECV_LIKES)
    private int recvLikes;

    @Expose
    @SerializedName(Common.DESCRIPTION)
    private String description;

    @Expose
    @SerializedName(Common.VIDEOS)
    private int videos;

    @Expose
    @SerializedName(Common.USER_ID)
    private int userId;

    @Expose
    @SerializedName(Common.PRICE)
    public float price; // 课程价格

    @Expose
    @SerializedName(Common.START_TIME)
    public int start_time; // 开播时间，时间戳，直播课程时此参数必填

    @Expose
    @SerializedName(Common.TYPE)
    public int type; // 直播类型 1 == 直播，2 == 视频

    @Expose
    @SerializedName(Common.COVER_URL)
    public String cover_url; // 课程封面url，对应OSS的Object名称，例如：live/image1.jpg

    @Expose
    @SerializedName(Common.STATUS)
    public int status; // 课程状态，10=未开播，20=准备开播，30=调整直播时间中，40=直播中，50=生成回放中，60=已生成回放，70=课程视频已可播放

    @Expose
    @SerializedName(Common.STATUS_TEXT)
    public String status_text; // 课程状态文本

    @Expose
    @SerializedName(Common.DURATION)
    public int duration;

    @Expose
    @SerializedName(Common.BUYER_COUNT)
    public int buyer_count; // 购买人数

    @Expose
    @SerializedName(Common.USER)
    public User user;

    @SerializedName(Common.BEGIN_TIME)
    @Expose
    private long beginTime;

    public long getBeginTime() {
        return beginTime;
    }

    public String getAvatar() {
        return user == null ? avatar : user.avatar;
    }

    public String getNickName() {
        return user == null ? nickName : user.nickname;
    }

    /**
     * @param nickName The nickName
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    /**
     * @return The onlines
     */
    public int getOnlines() {
        return onlines;
    }

    /**
     * @param onlines The view_count
     */
    public void setOnlines(int onlines) {
        this.onlines = onlines;
    }

    /**
     * @return The location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location The location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return The screenShot
     */
    public String getScreenShot() {
        return screenShot;
    }

    /**
     * @param screenShot The screen_shot
     */
    public void setScreenShot(String screenShot) {
        this.screenShot = screenShot;
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getStarVerified() {
        return starVerified;
    }

    public void setStarVerified(String starVerified) {
        this.starVerified = starVerified;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public int getLiveTag() {
        return liveTag;
    }

    public void setLiveTag(int liveTag) {
        this.liveTag = liveTag;
    }

    public void setIsLive(boolean isLive) {
        liveTag = isLive ? 1 : 0;
    }

    public boolean isLive() {
        return type == 1;
    }

    public int getViews() {
        return views;
    }

    public CharSequence getViewsCharSequence(Context context) {
        return getViewsCharSequence(context, true, false);
    }

    public CharSequence getViewsCharSequence(Context context, boolean videosTag) {
        return getViewsCharSequence(context, true, true);
    }

    public CharSequence getViewsCharSequence(Context context, boolean isChangeTypeface, boolean videosTag) {
        int viewStrId;

        if (videosTag) {
            viewStrId = R.string.replay_count_room;
        } else if (isLive()) {
            viewStrId = R.string.people_on_see;
        } else {
            viewStrId = R.string.people_has_see;
        }
        String onlinesStr;

        String numberStr;

        int numberValue;

        if (videosTag) {
            numberValue = videos;
        } else {
            numberValue = views;
        }

        if (numberValue >= 10000) {
            numberStr = String.format(Locale.US, "%.1f", numberValue / 10000.f);
            onlinesStr = context.getString(R.string.thousand, numberStr);
        } else {
            numberStr = onlinesStr = String.valueOf(numberValue);
        }

        onlinesStr = context.getString(viewStrId, onlinesStr);

        if (isChangeTypeface) {
            Typeface fontFace = TypefaceHelper.getInstance().getTypeface(TypefaceHelper.TYPE_FACE_Georgia_Bold);
            if (fontFace == null) {
                fontFace = Typeface.createFromAsset(context.getAssets(),
                        "fonts/georgiab_0.ttf");
                TypefaceHelper.getInstance().putTypeface(TypefaceHelper.TYPE_FACE_Georgia_Bold, fontFace);
            }
            SpannableString ss = new SpannableString(onlinesStr);

            CustomTypefaceSpan span = new CustomTypefaceSpan("Georgia Bold", fontFace);
            ss.setSpan(span, 0, numberStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            return ss;
        }
        return onlinesStr;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getRecvCoins() {
        return recvCoins;
    }

    public void setRecvCoins(int recvCoins) {
        this.recvCoins = recvCoins;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getRecvLikes() {
        return recvLikes;
    }

    public void setRecvLikes(int recvLikes) {
        this.recvLikes = recvLikes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVideos() {
        return videos;
    }

    public void setVideos(int videos) {
        this.videos = videos;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCover_url() {
        return Utils.isEmpty(cover_url) ? screenShot : cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatus_text() {
        return status_text;
    }

    public void setStatus_text(String status_text) {
        this.status_text = status_text;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getBuyer_count() {
        return buyer_count;
    }

    public void setBuyer_count(int buyer_count) {
        this.buyer_count = buyer_count;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
