package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsBaseBean;
import com.laka.live.shopping.bean.newversion.ShoppingHomeTopics;
import com.laka.live.util.Common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lyf on 2017/9/18.
 */
public class Content {


    @Expose
    @SerializedName(Common.USER_ID)
    private int user_id; // 用户id

    @Expose
    @SerializedName(Common.USER)
    private User user; // 主播信息

    @Expose
    @SerializedName(Common.TYPE)
    private int type; // 直播类型 1 == 直播，2 == 视频

    @Expose
    @SerializedName(Common.STATUS)
    private int status; // 课程状态，10=未开播，20=准备开播，30=调整直播时间中，40=直播中，50=生成回放中，60=已生成回放，70=课程视频已可播放

    @Expose
    @SerializedName(Common.BUY_FLAG)
    private int buy_flag; // 购买标识，1=已购买，2=未购买

    @Expose
    @SerializedName(Common.BUYER_COUNT)
    private int buyer_count; // 购买人数

    @Expose
    @SerializedName(Common.RECV_LIKES)
    private int recv_likes; // 点赞数

    @Expose
    @SerializedName(Common.START_TIME)
    private long start_time; // 开播时间，时间戳，直播课程时此参数必填

    @Expose
    @SerializedName(Common.DISCOUNT_TIME)
    private long discount_time; // 优惠时间

    @Expose
    @SerializedName(Common.DURATION)
    private String duration; // 视频时长

    @Expose
    @SerializedName(Common.PRICE)
    private float price; // 课程价格

    @Expose
    @SerializedName(Common.TRAILER_ID)
    private String trailer_id; // 预告id

    @Expose
    @SerializedName(Common.TITLE)
    private String title; // 课程名

    @Expose
    @SerializedName(Common.SUMMARY)
    private String summary; // 课程简介

    @Expose
    @SerializedName(Common.FORMULA)
    private String formula; // 配方做法

    @Expose
    @SerializedName(Common.STATUS_TEXT)
    private String status_text; // 课程状态文本

    @Expose
    @SerializedName(Common.COVER_URL)
    private String cover_url; // 课程封面url，对应OSS的Object名称，例如：live/image1.jpg


    @SerializedName(Common.LOCATION)
    @Expose
    private String location;

    @Expose
    @SerializedName(Common.EDITABLE_FLAG)
    private int editable_flag; // 可编辑标识，1=可以，2=不可以

    @Expose
    @SerializedName(Common.VIEWS)
    private int views; // 观看人数

    @Expose
    @SerializedName(Common.RECV_COINS)
    private int recvCoins; // 收到的贝币

    @Expose
    @SerializedName("istest")
    private int isTest; // 是否测试课程，1是，0否，默认否

    @Expose
    @SerializedName(Common.ROOM)
    private BannerCourseRoom room;

    @Expose
    @SerializedName(Common.AVATAR)
    private String avatar; // 用户头像url

    @Expose
    @SerializedName(Common.NICK_NAME)
    private String nickname; // 用户昵称

    @Expose
    @SerializedName(Common.TAGS)
    private List<String> tags; // 标签

    @Expose
    @SerializedName(Common.SUMMARY_IMGS)
    private List<ImageBean> summaryImages = new ArrayList<>(); // 课程简介图片

    @Expose
    @SerializedName(Common.FORMULA_IMGS)
    private List<ImageBean> formulaImages = new ArrayList<>(); // 课程简介图片

    @Expose
    @SerializedName(Common.CREATE_TIME)
    private long news_create_time; // 资讯创建时间

    @Expose
    @SerializedName(Common.URL)
    private String news_url; // 资讯链接

    @Expose
    @SerializedName(Common.INCOME)
    private String income;

    @Expose
    @SerializedName("rec_goods")
    private ArrayList<ShoppingGoodsBaseBean> recommendGoods;

    @Expose
    @SerializedName(Common.ID)
    public long id; // 课程预告id

    @Expose
    @SerializedName(Common.DISCOUNT)
    public float discount = 10; // 折扣

    @Expose
    @SerializedName(Common.SAVED_COINS)
    public float savedCoins; // 购买整套的折扣

    @Expose
    @SerializedName(Common.TOPICS)
    public List<Topic> topics; // 话题数组

    @Expose
    @SerializedName(Common.VIDEO_URL)
    public String video_url; // 课程类型为视频时必填，对应OSS的Object名称，例如：live/video.mp4

    @Expose
    @SerializedName(Common.SNAPSHOT_URL)
    public String snapshot_url; // 课程预告视频截图url

    @Expose
    @SerializedName(Common.DISCOUNT_DEADLINE)
    private long discount_deadline; // 折扣截至时间，时间戳

    @Expose
    @SerializedName(Common.DISCOUNT_REMAINING_TIME)
    private long discount_remaining_time; // 折扣剩余时间，时间戳

    @Expose
    @SerializedName("time_limit_type")
    private int time_limit_type;

    @Expose
    @SerializedName("discount_type")
    public String discount_type = "1"; // 折扣类型，1=普通折扣，2=限时折扣


    ///  专题字段
    @Expose
    @SerializedName("topicId")
    private int topicId = -1; // 专题Id

    @Expose
    @SerializedName("coverUrl")
    private String coverUrl; // 专题图片

    @Expose
    @SerializedName(Common.AGENT_PROFITRATIO)
    private String agent_profitratio; // 代理推广收益率，格式如 10%，无设置时为空字符串

    public int getTopicId() {
        return topicId;
    }

    // 抽出课程类
    public Course getCourse() {

        Course course = new Course();
        course.id = id;
        course.discount = discount;
        course.savedCoins = savedCoins;
        course.topics = topics;
        course.video_url = video_url;
        course.snapshot_url = snapshot_url;
        course.discount_type = discount_type;
        course.user_id = user_id;
        course.user = user;
        course.type = type;
        course.status = status;
        course.buy_flag = buy_flag;
        course.buyer_count = buyer_count;
        course.recv_likes = recv_likes;
        course.start_time = start_time;
        course.discount_time = discount_time;
        course.duration = duration;
        course.price = price;
        course.trailer_id = trailer_id;
        course.title = title;
        course.summary = summary;
        course.formula = formula;
        course.status_text = status_text;
        course.cover_url = cover_url;
        course.editable_flag = editable_flag;
        course.views = views;
        course.recvCoins = recvCoins;
        course.room = room;
        course.avatar = avatar;
        course.nickname = nickname;
        course.tags = tags;
        course.news_create_time = news_create_time;
        course.news_url = news_url;
        course.income = income;
        course.setTest(isTest);
        course.setLocation(location);
        course.setFormulaImages(formulaImages);
        course.setSummaryImages(summaryImages);
        course.setRecommendGoods(recommendGoods);
        course.setTime_limit_type(time_limit_type);
        course.setDiscount_deadline(discount_deadline);
        course.setAgent_profitratio(agent_profitratio);
        course.setDiscount_remaining_time(discount_remaining_time);
        return course;
    }

    // 抽出专题类
    public ShoppingHomeTopics getTopics() {
        ShoppingHomeTopics shoppingHomeTopics = new ShoppingHomeTopics();
        shoppingHomeTopics.setTitle(title);
        shoppingHomeTopics.setTopicId(topicId);
        shoppingHomeTopics.setCoverUrl(coverUrl);
        return shoppingHomeTopics;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
