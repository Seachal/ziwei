package com.laka.live.bean;


import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.constants.Constants;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsBaseBean;
import com.laka.live.ui.activity.WebActivity;
import com.laka.live.ui.course.detail.CourseDetailActivity;
import com.laka.live.ui.room.LiveRoomActivity;
import com.laka.live.ui.room.SeeReplayActivity;
import com.laka.live.util.Common;
import com.laka.live.util.TimeUtil;
import com.laka.live.util.Utils;

import java.util.ArrayList;
import java.util.List;

import framework.ioc.Ioc;

/**
 * Created by Lyf on 2017/4/7.
 * 课程的实体类
 */
public class Course extends CourseTrailer {

    // 课程类型, 1=直播，2=视频, 3= 资讯
    public final static int LIVE = 1, VIDEO = 2, NEWS = 3;
    //  课程状态，-1=未购买，1=已取消，10=未开播，20=准备开播，30=调整直播时间中，40=直播中，50=生成回放中，
    //  60=已生成回放，70=课程视频
    public final static int CANCEL = 1, NOTSTART = 10, ALREADY = 20, CHANGETIME = 30, LIVING = 40,
            CREATINGPLAYBACK = 50, CREATEDPLAYBACK = 60, PLAYVIDEO = 70;

    // 关闭限时和打开限时功能
    public final static int CLOSEDLIMITDISCOUNT = 1, OPENEDLIMITISCOUNT = 2;

    private static final int LIVE_INCOME = 1, VIDEO_INCOME = 2, AGENT_INCOME = 3;

    @Expose
    @SerializedName(Common.USER_ID)
    public int user_id; // 用户id

    @Expose
    @SerializedName(Common.USER)
    public User user; // 主播信息

    @Expose
    @SerializedName(Common.TYPE)
    public int type; // 直播类型 1 == 直播，2 == 视频

    @Expose
    @SerializedName(Common.STATUS)
    public int status; // 课程状态，10=未开播，20=准备开播，30=调整直播时间中，40=直播中，50=生成回放中，60=已生成回放，70=课程视频已可播放

    @Expose
    @SerializedName(Common.BUY_FLAG)
    public int buy_flag; // 购买标识，1=已购买，2=未购买

    @Expose
    @SerializedName(Common.BUYER_COUNT)
    public int buyer_count; // 购买人数

    @Expose
    @SerializedName(Common.RECV_LIKES)
    public int recv_likes; // 点赞数

    @Expose
    @SerializedName(Common.START_TIME)
    public long start_time; // 开播时间，时间戳，直播课程时此参数必填

    @Expose
    @SerializedName(Common.DISCOUNT_TIME)
    public long discount_time; // 优惠时间

    @Expose
    @SerializedName(Common.DURATION)
    public String duration; // 视频时长

    @Expose
    @SerializedName(Common.PRICE)
    public float price; // 课程价格

    @Expose
    @SerializedName(Common.TRAILER_ID)
    public String trailer_id; // 预告id

    @Expose
    @SerializedName(Common.TITLE)
    public String title; // 课程名

    @Expose
    @SerializedName(Common.SUMMARY)
    public String summary; // 课程简介

    @Expose
    @SerializedName(Common.FORMULA)
    public String formula; // 配方做法

    @Expose
    @SerializedName(Common.STATUS_TEXT)
    public String status_text; // 课程状态文本

    @Expose
    @SerializedName(Common.COVER_URL)
    public String cover_url; // 课程封面url，对应OSS的Object名称，例如：live/image1.jpg


    @SerializedName(Common.LOCATION)
    @Expose
    private String location;

    @Expose
    @SerializedName(Common.EDITABLE_FLAG)
    public int editable_flag; // 可编辑标识，1=可以，2=不可以

    @Expose
    @SerializedName(Common.VIEWS)
    public int views; // 观看人数

    @Expose
    @SerializedName(Common.RECV_COINS)
    public int recvCoins; // 收到的贝币

    @Expose
    @SerializedName("istest")
    private int isTest; // 是否测试课程，1是，0否，默认否

    @Expose
    @SerializedName(Common.ROOM)
    public BannerCourseRoom room;

    @Expose
    @SerializedName(Common.AVATAR)
    public String avatar; // 用户头像url

    @Expose
    @SerializedName(Common.NICK_NAME)
    public String nickname; // 用户昵称

    @Expose
    @SerializedName(Common.TAGS)
    public List<String> tags; // 标签

    @Expose
    @SerializedName(Common.SUMMARY_IMGS)
    private List<ImageBean> summaryImages = new ArrayList<>(); // 课程简介图片

    @Expose
    @SerializedName(Common.FORMULA_IMGS)
    private List<ImageBean> formulaImages = new ArrayList<>(); // 课程简介图片

    @Expose
    @SerializedName(Common.CREATE_TIME)
    public long news_create_time; // 资讯创建时间

    @Expose
    @SerializedName(Common.URL)
    public String news_url; // 资讯链接

    @Expose
    @SerializedName(Common.INCOME)
    public String income;

    @Expose
    @SerializedName(Common.INCOME_TYPE)
    public int incomeType;

    @Expose
    @SerializedName("rec_goods")
    private ArrayList<ShoppingGoodsBaseBean> recommendGoods;

    @Expose
    @SerializedName(Common.VIDEO_LINES)
    public List<VideoLine> videoLines; // 视频线路

    @Expose
    @SerializedName(Common.SPEEDS)
    public List<Speed> speeds; //可选播放速度

    @Expose
    @SerializedName(Common.AGENT_PROFITRATIO)
    private String agent_profitratio; // 代理推广收益率，格式如 10%，无设置时为空字符串

    public String rec_goods_ids;

    public String objectKey; // 当前缓存Course的objectKey,断点续传，用于标识的

    public int itemType = Constants.TYPE_ITEM;// 布局类型

    public float progress; // 上传进度(仅限本地传递使用)

    public String local_cover_url; // 课程封面url，本地缓存用

    public List<Topic> localTopics; // 话题(仅限本地传递使用)

    public List<VideoLine> getVideoLines() {
        return videoLines;
    }

    public void setVideoLines(List<VideoLine> videoLines) {
        this.videoLines = videoLines;
    }

    public List<Speed> getSpeeds() {
        return speeds;
    }

    public void setSpeeds(List<Speed> speeds) {
        this.speeds = speeds;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPrice() {
        return price;
    }

    public String getDiscountText() {
        if (discount == 0) {
            return "免费";
        } else if (discount == 10) {
            return "无优惠";
        }
        return "优惠";
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setPrice(String price) {
        if (Utils.isEmpty(price))
            this.price = 0;
        else
            this.price = Float.valueOf(price);
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFormatStartTime() {

        if (isLive()) {
            if (status >= Course.CREATINGPLAYBACK) {
                return TimeUtil.getTimeWithStr(Integer.parseInt(getDuration()));
            } else
                return TimeUtil.getTime("MM.dd HH:mm", start_time * 1000);
        } else if (isVideo()) {
            return TimeUtil.getTimeWithStr(Long.parseLong(getDuration()));
        } else
            return TimeUtil.getTime("MM.dd HH:mm", news_create_time * 1000);

    }

    public String getFormatStartTime(String format) {

        if (isLive()) {
            if (status >= Course.CREATINGPLAYBACK) {
                return TimeUtil.getTimeWithStr(Integer.parseInt(getDuration()));
            } else
                return TimeUtil.getTime(format, start_time * 1000);
        } else if (isVideo()) {
            return TimeUtil.getTimeWithStr(Long.parseLong(getDuration()));
        } else
            return TimeUtil.getTime(format, news_create_time * 1000);

    }

    public String getFormatDiscountTime() {
        if (getDiscount_time() == 0) {
            return "请选择";
        }
        return TimeUtil.getTime("MM.dd HH:mm", discount_time * 1000);
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String   getCover_url() {
        return cover_url;
    }

    // 带域名
    public String getPreCover_url() {
        if (cover_url == null) {
            return "";
        }
        return cover_url.startsWith("http://img.zwlive.lakatv.com") ? cover_url : "http://img.zwlive.lakatv.com/" + cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatus_text() {
        return " " + status_text;
    }

    public void setStatus_text(String status_text) {
        this.status_text = status_text;
    }

    public String getSnapshot_url() {
        return snapshot_url;
    }

    public void setSnapshot_url(String snapshot_url) {
        this.snapshot_url = snapshot_url;
    }

    public String getBuyer_count() {
        return buyer_count + "人已购";
    }

    public String getTrailer_id() {
        return trailer_id;
    }

    public void setTrailer_id(String trailer_id) {
        this.trailer_id = trailer_id;
    }

    // 判断我有没有买，超级管理员，不用买
    public boolean hasBuy() {

        if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
            // 未登录一律是未购买
            return false;
        } else if (Ioc.get(UserInfo.class).isAdministrator()) {
            // 管理员一律是已购买
            return true;
        }

        return buy_flag == 1;  // 购买标识，1=已购买，2=未购买
    }

    // 判断别人有没有买
    public boolean hasNotSold() {
        return buyer_count == 0;
    }

    // 判断别人有没有买
    public boolean hasSold() {
        return buyer_count > 0;
    }

    public void setBuy_flag(int buy_flag) {
        this.buy_flag = buy_flag;
    }

    public String getDuration() {
        return Utils.isEmpty(duration) ? "0" : duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    // 是否是直播类型
    public boolean isLive() {
        return type == LIVE;
    }

    // 是否是视频类型
    public boolean isVideo() {
        return type == VIDEO;
    }

    // 是否是资讯
    public boolean isNews() {
        return type == NEWS;
    }

    public List<Topic> getLocalTopics() {
        return localTopics;
    }

    public void setLocalTopics(List<Topic> localTopics) {
        this.localTopics = localTopics;
    }

    public String getRecv_likes() {
        return recv_likes + "赞";
    }

    public int getUserId() {
        return user == null ? 0 : user.id;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getRecvCoins() {
        return recvCoins;
    }

    public int getViews() {
        return views;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BannerCourseRoom getRoom() {
        return room;
    }

    public void setRoom(BannerCourseRoom room) {
        this.room = room;
    }

    public void setRoom(Room room) {

        if (room == null) {
            return;
        }

        if (this.room == null) {
            this.room = new BannerCourseRoom();
        }

        this.room.setDownUrl(room.getDownUrl());
        this.room.setState(room.getStatus());
        this.room.setBeginTime(room.getBeginTime());
        this.room.setChannelId(room.getChannelId());
        this.room.setStreamId(room.getStreamId());

    }

    // 这个时间是以秒为单位
    public long getBeginTime() {
        if (room == null) {
            return start_time;
        } else {
            return room.getBeginTime();
        }
    }

    // 是否可编辑
    public boolean isEditable() {
        return editable_flag == 1;
    }

    public String getLocalCoverUrl() {
        return local_cover_url;
    }

    public void setLocalCoverUrl(String local_cover_url) {
        this.local_cover_url = local_cover_url;
    }

    public String getLocalVideoUrl() {
        return local_video_url;
    }

    public void setLocalVideoUrl(String local_video_url) {
        this.local_video_url = local_video_url;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public boolean isUpLoading() {
        return progress > 0;
    }

    public long getNewsCreateTime() {
        return news_create_time;
    }

    public String getNewsUrl() {
        return news_url;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public ArrayList<ShoppingGoodsBaseBean> getRecommendGoods() {

        if (recommendGoods == null) {
            recommendGoods = new ArrayList<>();
        }

        return recommendGoods;
    }

    public void setRecommendGoods(ArrayList<ShoppingGoodsBaseBean> recommendGoods) {
        this.recommendGoods = recommendGoods;
    }

    public List<ImageBean> getSummaryImages() {
        return summaryImages;
    }

    public void setSummaryImages(List<ImageBean> summaryImages) {
        this.summaryImages = summaryImages;
    }

    public void addSummaryImage(ImageBean summaryImage) {
        this.summaryImages.add(summaryImage);
    }

    public List<ImageBean> getFormulaImages() {
        return formulaImages;
    }

    public void setFormulaImages(List<ImageBean> formulaImages) {
        this.formulaImages = formulaImages;
    }

    public void addFormulaImage(ImageBean formulaImage) {
        this.formulaImages.add(formulaImage);
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public int getIncomeType() {
        return incomeType;
    }

    public String getIncomeTypeStr() {
        switch (incomeType) {
            case LIVE_INCOME:
                return "直播收入";
            case VIDEO_INCOME:
                return "视频收入";
            case AGENT_INCOME:
                return "代理收入";
            default:
                return "";
        }
    }

    public void setIncomeType(int incomeType) {
        this.incomeType = incomeType;
    }

    public void onClickEvent(Context context) {

        // 如果是资讯就直接跳web
        if (isNews()) {
            WebActivity.startActivity(context, getNewsUrl(), getTitle());
            return;
        }

        if (hasBuy()) {
            // 已购买
            if (status == Course.LIVING) {
                // 直播就跳到直播间
                LiveRoomActivity.startPlay(context, user_id,
                        Ioc.get(UserInfo.class).getId() == user_id, getTitle(),
                        room.getStreamId(), room.getChannelId(), user.avatar,
                        cover_url, Common.FROM_MAIN, getId());
            } else if (status == Course.PLAYVIDEO || status == Course.CREATEDPLAYBACK) {
                // 观看回放或视频
                SeeReplayActivity.startActivity(context, getId(), room.getDownUrl(),
                        String.valueOf(user.id), getViews(), getRecvCoins(), room.getChannelId(), type);
            } else {
                // 其它状态跳详情
                CourseDetailActivity.startActivity(context, String.valueOf(id));
            }

        } else {
            // 未购买一律跳详情
            CourseDetailActivity.startActivity(context, String.valueOf(id));
        }
    }

    public long getDiscount_time() {
        return discount_time;
    }

    public void setDiscount_time(long discount_time) {
        this.discount_time = discount_time;
    }

    public boolean isTestLive() {
        return isTest == 1;
    }

    public void setBuyerCount(int buyer_count) {
        this.buyer_count = buyer_count;
    }

    public int setTest(int isTest) {
        return this.isTest = isTest;
    }

    public String getAgentProfitratio() {
        return agent_profitratio;
    }

    public void setAgent_profitratio(String agent_profitratio) {
        this.agent_profitratio = agent_profitratio;
    }
}
