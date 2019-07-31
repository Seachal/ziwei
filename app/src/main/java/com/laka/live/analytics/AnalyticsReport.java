package com.laka.live.analytics;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.laka.live.BuildConfig;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.bean.LiveReportData;
import com.laka.live.msg.Msg;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.util.ChannelUtil;
import com.laka.live.util.Common;
import com.laka.live.util.Log;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by luwies on 16/5/19.
 */
public class AnalyticsReport {

    private static final String TAG = "MobclickAgent";

    public static final String KEY = "laka1808";

    public static final String SEPARATOR = "_";

    public static final String DEFAULT_VIEW_ID = "NA";

    public static final String KEY_LIVE_TYPE = "Live_type";

    public static final String VALUE_LIVE_TYPE_REPLAY = "2";

    public static final String VALUE_LIVE_LIVE = "1";


    public static final String PHP_REQUEST_FAIL_ID = "309";

    /**
     * 访问
     */
    public static final String SHOW_EVENT_ID = "401";

    /**
     * 点击
     */
    public static final String CLICK_EVENT_ID = "403";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS_EVENT_ID = "502";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL_EVENT_ID = "503";

    /**
     * 网络环境监控
     */
    public static final String NETWORK_ENVIRONMENT_MONITORING = "308";

    /**
     * 登录页容器ID
     */
    public static final String LOGIN_VIEW_ID = "10000";

    /**
     * 微信登录容器ID
     */
    public static final String LOGIN_WECHAT_BUTTON_VIEW_ID = "10001";

    /**
     * 微信登录按钮
     */
    public static final String LOGIN_QQ_BUTTON_VIEW_ID = "10002";

    /**
     * 微博登录按钮
     */
    public static final String LOGIN_WEIBO_BUTTON_VIEW_ID = "10003";

    /**
     * 手机登录按钮
     */
    public static final String LOGIN_PHONE_BUTTON_VIEW_ID = "10004";


    /**
     * 手机号登录页
     */
    public static final String LOGIN_PHONE_VIEW_ID = "10005";

    /**
     * 手机号登录页-手机登录按钮
     */
    public static final String PHONE_LOGIN_BUTTON_VIEW_ID = "10006";


    /**
     * 首页
     */
    public static final String HOME_VIEW_ID = "10200";

    /**
     * 首页-直播
     */
    public static final String HOME_LIVE_CLICK_ID = "10201";
    /**
     * 首页-视频按钮
     */
    public static final String HOME_VIDEO_CLICK_ID = "10202";

    /**
     * 首页-话题按钮
     */
    public static final String HOME_TOPICS_CLICK_ID = "10203";

    /**
     * 首页-搜索按钮
     */
    public static final String HOME_SEARCH_BUTTON_VIEW_ID = "10204";

    /**
     * 首页-排行榜按钮
     */
    public static final String HOME_RANK_BUTTON_VIEW_ID = "10205";

    /**
     * 首页-banner
     */
    public static final String HOME_BANNER_VIEW_ID = "10206";

    /**
     * 首页-用户资料
     */
    public static final String HOME_USER_INFO_VIEW_ID = "10207";

    /**
     * 首页-热门列表item
     */
    public static final String HOME_HOT_LIST_ITEM_VIEW_ID = "10208";

    /**
     * 首页-话题
     */
    public static final String HOME_LIVE_TOPIC_VIEW_ID = "10209";

    /**
     * 首页-发现
     */
    public static final String HOME_FOUND_VIEW_ID = "10210";

    /**
     * 首页-视频列表
     */
    public static final String HOME_VIDEO_VIEW_ID = "10211";

    /**
     * 首页-视频列表-用户资料
     */
    public static final String HOME_VIDEO_USER_CLICK_VIEW_ID = "10212";

    /**
     * 首页-发现-更多话题
     */
    public static final String HOME_VIDEO_COVER_CLICK_VIEW_ID = "10213";

    public static final String HOME_VIDEO_TOPIC_VIEW_ID = "10214";

    public static final String HOME_VIDEO_NAME_CLICK_VIEW_ID = "10215";

    public static final String HOME_TOPICS_VIEW_ID = "10216";

    public static final String HOME_FOUND_MORE_TOPIC_ACTIVITY_ITEM_VIEW_ID = "10217";

    public static final String HOME_FOUND_TOPIC_SHOW_MORE_VIEW_ID = "10218";

    public static final String HOME_FOUND_TOPIC_SHOW_LIVE_VIEW_ID = "10219";

    public static final String HOME_TOPICS_TOPIC_VIEW_ID = "10220";

    public static final String HOME_TOPICS_MORE_TOPIC_VIEW_ID = "10221";

    public static final String HOME_TOPICS_TOPIC_DETAIL_VIEW_ID = "10222";
    //话题详情课堂封面点击
    public static final String HOME_TOPICS_TOPIC_COVER_CLICK_ID = "10223";

    public static final String HOME_RANK_SEND_VIEW_ID = "10225";

    public static final String HOME_TOPICS_USER_VIEW_ID = "10224";

    public static final String HOME_RANK_GET_VIEW_ID = "10226";

    public static final String HOME_RANK_FANS_VIEW_ID = "10227";

    public static final String FRIEND_DYNAMIC_VIEW_ID = "10229";

    public static final String FRIEND_DYNAMIC_ITEM_VIEW_ID = "10230";


    /**
     * 搜索页面
     */
    public static final String SEARCH_VIEW_ID = "11000";
    public static final String ID_11001 = "11001";
    /**
     * 搜索热词列表item
     */
    public static final String SEARCH_HOT_WORD_ITEM_VIEW_ID = "11002";

    /**
     * 搜索按钮
     */
    public static final String SEARCH_BUTTON_VIEW_ID = "11003";

    /**
     * 搜索结果不为空的页面
     */
    public static final String SEARCH_NOT_NULL_VIEW_ID = "11004";

    /**
     * 搜索结果item
     */
    public static final String SEARCH_RESULT_ITEM_VIEW_ID = "11005";

    /**
     * 搜索关注按钮
     */
    public static final String SEARCH_RESULT_FOLLOW_BUTTON_VIEW_ID = "11006";

    /**
     * 搜索结果为空的页面
     */
    public static final String SEARCH_NULL_VIEW_ID = "11007";

    /**
     * 搜索框
     */
    public static final String SEARCH_EDIT_VIEW_ID = "11008";

    /**
     * 我的主页
     */
    public static final String MY_VIEW_ID = "15400";

    /**
     * 我的-设置
     */
    public static final String MY_SETTING_VIEW_ID = "15401";

    /**
     * 我的-编辑
     */
    public static final String MY_EDIT_VIEW_ID = "15402";

    /**
     * 我的-回放按钮
     */
    public static final String MY_LIVE_VIEW_ID = "15405";

    /**
     * 我的-关注按钮
     */
    public static final String MY_FOLLOW_VIEW_ID = "15403";

    /**
     * 我的-粉丝按钮
     */
    public static final String MY_FANS_VIEW_ID = "15404";

    /**
     * 我的-收益按钮
     */
    public static final String MY_INCOME_VIEW_ID = "15406";

    /**
     * 我的-等级按钮
     */
    public static final String MY_LEVEL_VIEW_ID = "15407";

    /**
     * 我的-钻石按钮
     */
    public static final String MY_KA_ZUAN_VIEW_ID = "15408";

    /**
     * 我的-贡献榜按钮
     */
    public static final String MY_CONTRIBUTION_LIST_VIEW_ID = "15409";

    /**
     * 我的-设置页面
     */
    public static final String MY_SETTING_ACTIVITY_VIEW_ID = "15410";

    public static final String SETTING_ABOUT_US_VIEW_ID = "15411";

    public static final String SETTING_CLEAR_CACHE_VIEW_ID = "15412";

    public static final String SETTING_MSG_REMIND_VIEW_ID = "15413";

    public static final String SETTING_FEEDBACK_VIEW_ID = "15414";

    public static final String SETTING_LOGOUT_VIEW_ID = "15415";

    public static final String MY_LIVE_REPLAY_VIEW_ID = "15422";

    public static final String MY_LIVE_REPLAY_ITEM_VIEW_ID = "15423";

    public static final String MY_FOLLOW_ACTIVITY_VIEW_ID = "15424";

    public static final String MY_FOLLOW_LIST_ITEM_VIEW_ID = "15425";

    public static final String MY_FOLLOW_LIST_ITEM_FOLLOW_BUTTON_VIEW_ID = "15427";

    public static final String MY_FANS_ACTIVITY_VIEW_ID = "15429";

    public static final String MY_FANS_LIST_ITEM_VIEW_ID = "15430";

    public static final String MY_FANS_LIST_ITEM_FOLLOW_BUTTON_VIEW_ID = "15432";

    public static final String MY_INCOME_ACTIVITY_VIEW_ID = "15416";

    public static final String RECOMMEND_ACTIVITY_VIEW_ID = "15452";

    public static final String MY_INCOME_EXCHANGE_DIAMOND_VIEW_ID = "15417";

    public static final String MY_INCOME_WECHAT_WITHDRAWAL_VIEW_ID = "15418";

    public static final String MY_INCOME_TRANSACTION_VIEW_ID = "15419";

    public static final String EXCHANGE_DIAMOND_ACTIVITY_VIEW_ID = "15420";

    public static final String EXCHANGE_DIAMOND_ACTIVITY_SURE_BUTTON_VIEW_ID = "15421";

    public static final String BIND_WECHAT_ACTIVITY_VIEW_ID = "15422";

    public static final String BIND_WECHAT_ACTIVITY_SURE_BUTTON_VIEW_ID = "15423";

    public static final String BIND_PHONE_ACTIVITY_VIEW_ID = "15424";

    public static final String BIND_PHONE_ACTIVITY_PHONE_INPUT_VIEW_ID = "15425";

    public static final String BIND_PHONE_ACTIVITY_GET_PVC_BUTTON_VIEW_ID = "15426";

    public static final String BIND_PHONE_ACTIVITY_PVC_INPUT_VIEW_ID = "15427";

    public static final String BIND_PHONE_ACTIVITY_SURE_BUTTON_VIEW_ID = "15428";

    public static final String WECHAT_WITHDRAWAL_ACTIVITY_VIEW_ID = "15429";

    public static final String WECHAT_WITHDRAWAL_ACTIVITY_SURE_BUTTON_VIEW_ID = "15430";

    public static final String MY_COINS_ACTIVITY_VIEW_ID = "15431";

    public static final String MY_COINS_ACTIVITY_WECHAT_BUTTON_VIEW_ID = "15435";

    public static final String MY_COINS_ACTIVITY_WECHAT_ITEM_VIEW_ID = "15436";

    public static final String MY_COINS_ACTIVITY_ALIPAY_BUTTON_VIEW_ID = "15438";

    public static final String MY_COINS_ACTIVITY_ALIPAY_ITEM_VIEW_ID = "15439";

    public static final String OTHER_USER_INFO_ACTIVITY_VIEW_ID = "15441";

    public static final String OTHER_USER_INFO_ACTIVITY_LIVE_BUTTON_VIEW_ID = "15460";

    public static final String OTHER_USER_INFO_ACTIVITY_FOLLOW_BUTTON_VIEW_ID = "15443";

    public static final String OTHER_USER_INFO_ACTIVITY_LETTER_BUTTON_VIEW_ID = "15444";

    public static final String OTHER_USER_INFO_ACTIVITY_BLOCK_BUTTON_VIEW_ID = "15445";

    public static final String OTHER_USER_INFO_ACTIVITY_FOLLOW_LIST_BUTTON_VIEW_ID = "15447";

    public static final String OTHER_USER_INFO_ACTIVITY_FANS_LIST_BUTTON_VIEW_ID = "15448";

    public static final String OTHER_USER_INFO_ACTIVITY_CONTRIBUTION_LIST_BUTTON_VIEW_ID = "15449";

    public static final String OTHER_USER_INFO_ACTIVITY_LIVE_LIST_VIEW_ID = "15464";

    public static final String OTHER_USER_INFO_ACTIVITY_LIVE_LIST_ITEM_VIEW_ID = "15442";

    public static final String OTHER_USER_INFO_ACTIVITY_FOLLOW_LIST_VIEW_ID = "15466";

    public static final String OTHER_USER_INFO_ACTIVITY_FOLLOW_LIST_ITEM_VIEW_ID = "15467";

    public static final String OTHER_USER_INFO_ACTIVITY_FOLLOW_LIST_ITEM_FOLLOW_VIEW_ID = "15469";

    public static final String OTHER_USER_INFO_ACTIVITY_FOLLOW_LIST_ITEM_CANCEL_FOLLOW_VIEW_ID = "15470";

    public static final String OTHER_USER_INFO_ACTIVITY_FANS_LIST_VIEW_ID = "15471";

    public static final String OTHER_USER_INFO_ACTIVITY_FANS_LIST_ITEM_VIEW_ID = "15472";

    public static final String OTHER_USER_INFO_ACTIVITY_FANS_LIST_ITEM_FOLLOW_VIEW_ID = "15474";

    public static final String OTHER_USER_INFO_ACTIVITY_FANS_LIST_ITEM_CANCEL_FOLLOW_VIEW_ID = "15475";

    public static final String PHP_REQUEST_FAIL = "15455"; //php接口请求失败

    public static final String COURSE_DETAIL = "16000"; // 课堂详情页
    public static final String COURSE_DETAIL_PLAY_TRAILER = "16001"; // 课堂详情页-播放预告
    public static final String COURSE_DETAIL_TOPICS = "16002"; // 课堂详情页-课堂话题
    public static final String COURSE_DETAIL_USER_INFO = "16003"; // 课堂详情页-主播信息
    public static final String COURSE_DETAIL_SUBSCRIBE = "16004"; // 课堂详情页-订阅用户
    public static final String COURSE_DETAIL_SIMILAR = "16005"; // 课堂详情页-课堂其他课程列表
    public static final String COURSE_DETAIL_COMMIT = "16006"; // 课堂详情页-右下角的按钮
    public static final String COURSE_DETAIL_COMMIT_BUY_SINGLE = "16007"; // 课堂详情页-只订阅本节课
    public static final String COURSE_DETAIL_COMMIT_BUY_ENTIRE = "16008"; // 课堂详情页-订阅完整课堂

    public static final String PAY_COURSE = "16009";// 访问支付清单页
    public static final String PAY_COURSE_COMMIT = "16012";// 支付清单-右下角按钮

    public static final String BUY_SUCCESS = "16013";// 访问课堂订阅成功页面
    public static final String BUY_SUCCESS_SHARE = "16014";// 课堂订阅成功-邀请好友订阅
    public static final String BUY_SUCCESS_LOOK = "16015";// 课堂订阅成功-查看订阅的课堂

    public static final String ID_16016 = "16016";  //参与讨论
    public static final String ID_16017 = "16017";  //参与讨论-发布

    public static final String POST_LIVE = "12000"; // 访问发布课堂页面
    public static final String POST_LIVE_ADD_TOPIC = "12001"; // 访问发布课堂页面
    public static final String POST_LIVE_DISCOUNT = "12002"; // 设置折扣
    public static final String POST_LIVE_ADD_COURSE = "12003"; // 添加课时
    public static final String POST_LIVE_POST_TRAILER = "12004"; // 发布预告
    public static final String POST_LIVE_CONFIRM = "12005"; // 确认发布
    public static final String POST_SUCCESS = "12006"; // 发布成功页面
    public static final String POST_SUCCESS_SHARE = "12007"; // 发布成功页面-通知大家
    public static final String POST_SUCCESS_LOOK = "12008"; // 发布成功页面-查看发布的课堂
    public static final String RECOMMEND_VIEW = "19003"; // 推荐页
    public static final String RECOMMEND_IGNORE = "19004"; // 推荐页-跳过
    public static final String RECOMMEND_FOLLOW_ALL = "19005"; // 推荐页

    //2.1.0
    //首页

    public static final String ID_10231 = "10231"; //首页页面
    public static final String ID_10232 = "10232";  //banner
    public static final String ID_10233 = "10233";  //我的购买
    public static final String ID_10234 = "10234";  //关注动态
    public static final String ID_10235 = "10235";  //免费课程
    public static final String ID_10236 = "10236";  //热门资讯
    public static final String ID_10237 = "10237";  //正在直播-课程封面
    public static final String ID_10238 = "10238";  //正在直播-课程标题
    public static final String ID_10239 = "10239";  //正在直播-主播信息
    public static final String ID_10240 = "10240";  //正在直播-more
    public static final String ID_10241 = "10241";  //热门话题-话题封面
    public static final String ID_10242 = "10242"; //热门话题-更多话题
    public static final String ID_10243 = "10243";  //课程推荐-课程封面
    public static final String ID_10244 = "10244";  //课程推荐-课程标题
    public static final String ID_10245 = "10245";  //课程推荐-主播信息
    public static final String ID_10246 = "10246";  //课程推荐-查看全部
    public static final String ID_10247 = "10247";  //免费课程-课程封面
    public static final String ID_10248 = "10248";  //免费课程-课程标题
    public static final String ID_10249 = "10249";  //免费课程-主播信息
    public static final String ID_10250 = "10250";  //热门资讯-资讯封面
    public static final String ID_10251 = "10251";  //热门资讯-主播信息
    public static final String ID_10252 = "10252";  //关注动态-封面
    public static final String ID_10253 = "10253";  //关注动态-标题
    public static final String ID_10254 = "10254";  //关注动态-主播信息
    public static final String ID_10255 = "10255";  //首页功能入口

    // 内容
    public static final String ID_10228 = "10228";  // 内容-一级分类按钮
    public static final String ID_10256 = "10256";  // 内容-最新直播-课程封面
    public static final String ID_10257 = "10257";  // 内容-最新直播-主播信息
    public static final String ID_10258 = "10258";  // 内容-最新直播-更多
    public static final String ID_10259 = "10259";  // 内容-最新视频-课程封面
    public static final String ID_10260 = "10260";  // 内容-最新视频-主播信息
    public static final String ID_10261 = "10261";  // 内容-最新视频-更多
    public static final String ID_10262 = "10262";  // 内容-最新资讯-课程封面
    public static final String ID_10263 = "10263";  // 内容-最新资讯-主播信息
    public static final String ID_10264 = "10264";  // 内容-最新资讯-更多
    public static final String ID_10265 = "10265";  // 内容-优选专题-专题封面
    public static final String ID_10266 = "10266";  // 内容-一级分类-二级分类按钮

    /**
     *内容-最新直播-课程封面
     */
    public static final String EVENT_10256 = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(ID_10256).append(SEPARATOR).append(CLICK_EVENT_ID).toString();
    /**
     *内容-最新直播-主播信息
     */
    public static final String EVENT_10257 = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
                    .append(ID_10257).append(SEPARATOR).append(CLICK_EVENT_ID).toString();
    /**
     *内容-最新直播-更多
     */
    public static final String EVENT_10258 = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
                    .append(ID_10258).append(SEPARATOR).append(CLICK_EVENT_ID).toString();
    /**
     *内容-最新视频-课程封面
     */
    public static final String EVENT_10259 = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
                    .append(ID_10259).append(SEPARATOR).append(CLICK_EVENT_ID).toString();
    /**
     *内容-最新视频-主播信息
     */
    public static final String EVENT_10260 = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
                    .append(ID_10260).append(SEPARATOR).append(CLICK_EVENT_ID).toString();
    /**
     *内容-最新视频-更多
     */
    public static final String EVENT_10261 = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
                    .append(ID_10261).append(SEPARATOR).append(CLICK_EVENT_ID).toString();
    /**
     *内容-最新资讯-课程封面
     */
    public static final String EVENT_10262 = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
                    .append(ID_10262).append(SEPARATOR).append(CLICK_EVENT_ID).toString();
    /**
     *内容-最新资讯-主播信息
     */
    public static final String EVENT_10263 = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
                    .append(ID_10263).append(SEPARATOR).append(CLICK_EVENT_ID).toString();
    /**
     *内容-最新资讯-更多
     */
    public static final String EVENT_10264 = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
                    .append(ID_10264).append(SEPARATOR).append(CLICK_EVENT_ID).toString();
    /**
     *内容-优选专题-专题封面
     */
    public static final String EVENT_10265 = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(ID_10265).append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 内容-一级分类按钮
     */
    public static final String EVENT_10228 = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(ID_10228).append(SEPARATOR).append(CLICK_EVENT_ID).toString();
    /**
     * 内容-二级分类按钮
     */
    public static final String EVENT_10266 = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(ID_10266).append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /***********2.1.0开始***********/
    /**
     * 首页
     */
    public static final String EVENT_10231 = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(ID_10231).append(SEPARATOR).append(SHOW_EVENT_ID).toString();

    /**
     * 首页-banner
     */
    public static final String EVENT_10232 = new StringBuilder(ID_10231).append(SEPARATOR)
            .append(ID_10232).append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页-我的购买
     */
    public static final String EVENT_10233 = new StringBuilder(ID_10231).append(SEPARATOR)
            .append(ID_10233).append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页-关注动态
     */
    public static final String EVENT_10234 = new StringBuilder(ID_10231).append(SEPARATOR)
            .append(ID_10234).append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页-免费课程
     */
    public static final String EVENT_10235 = new StringBuilder(ID_10231).append(SEPARATOR)
            .append(ID_10235).append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页-热门资讯
     */
    public static final String EVENT_10236 = new StringBuilder(ID_10231).append(SEPARATOR)
            .append(ID_10236).append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页-正在直播-课程封面
     */
    public static final String EVENT_10237 = new StringBuilder(ID_10231).append(SEPARATOR)
            .append(ID_10237).append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页-正在直播-课程标题
     */
    public static final String EVENT_10238 = new StringBuilder(ID_10231).append(SEPARATOR)
            .append(ID_10238).append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页-正在直播-主播信息
     */
    public static final String EVENT_10239 = new StringBuilder(ID_10231).append(SEPARATOR)
            .append(ID_10239).append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页-正在直播-more
     */
    public static final String EVENT_10240 = new StringBuilder(ID_10231).append(SEPARATOR)
            .append(ID_10240).append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页-热门话题-话题封面
     */
    public static final String EVENT_10241 = new StringBuilder(ID_10231).append(SEPARATOR)
            .append(ID_10241).append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页-热门话题-更多话题
     */
    public static final String EVENT_10242 = new StringBuilder(ID_10231).append(SEPARATOR)
            .append(ID_10242).append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页-课程推荐-课程封面
     */
    public static final String EVENT_10243 = new StringBuilder(ID_10231).append(SEPARATOR)
            .append(ID_10243).append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页-课程推荐-课程标题
     */
    public static final String EVENT_10244 = new StringBuilder(ID_10231).append(SEPARATOR)
            .append(ID_10244).append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页-课程推荐-主播信息
     */
    public static final String EVENT_10245 = new StringBuilder(ID_10231).append(SEPARATOR)
            .append(ID_10245).append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页-课程推荐-查看全部
     */
    public static final String EVENT_10246 = new StringBuilder(ID_10231).append(SEPARATOR)
            .append(ID_10246).append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页-免费课程-课程封面
     */
    public static final String EVENT_10247 = new StringBuilder(ID_10231).append(SEPARATOR)
            .append(ID_10247).append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页-免费课程-课程标题
     */
    public static final String EVENT_10248 = new StringBuilder(ID_10231).append(SEPARATOR)
            .append(ID_10248).append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页-免费课程-主播信息
     */
    public static final String EVENT_10249 = new StringBuilder(ID_10231).append(SEPARATOR)
            .append(ID_10249).append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页-热门资讯-资讯封面
     */
    public static final String EVENT_10250 = new StringBuilder(ID_10231).append(SEPARATOR)
            .append(ID_10250).append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页-热门资讯-主播信息
     */
    public static final String EVENT_10251 = new StringBuilder(ID_10231).append(SEPARATOR)
            .append(ID_10251).append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页-关注动态-封面
     */
    public static final String EVENT_10252 = new StringBuilder(ID_10231).append(SEPARATOR)
            .append(ID_10252).append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页-关注动态-标题
     */
    public static final String EVENT_10253 = new StringBuilder(ID_10231).append(SEPARATOR)
            .append(ID_10253).append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页-关注动态-主播信息
     */
    public static final String EVENT_10254 = new StringBuilder(ID_10231).append(SEPARATOR)
            .append(ID_10254).append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页-功能入口
     */
    public static final String EVENT_10255 = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(ID_10255).append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /***********2.1.0结束***********/
    /**
     * 推荐页-跳过
     */
    public static final String RECOMMEND_IGNORE_CLICK = new StringBuilder(RECOMMEND_VIEW).append(SEPARATOR)
            .append(RECOMMEND_IGNORE).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 推荐页-一键关注
     */
    public static final String RECOMMEND_FOLLOW_ALL_CLICK = new StringBuilder(RECOMMEND_VIEW).append(SEPARATOR)
            .append(RECOMMEND_FOLLOW_ALL).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 推荐页
     */
    public static final String RECOMMEND_VIEW_LOOK = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(RECOMMEND_VIEW).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();
    /**
     * 发布成功页面-通知大家
     */
    public static final String POST_SUCCESS_SHARE_CLICK = new StringBuilder(POST_SUCCESS).append(SEPARATOR)
            .append(POST_SUCCESS_SHARE).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 发布成功页面-查看发布的课堂
     */
    public static final String POST_SUCCESS_LOOK_CLICK = new StringBuilder(POST_SUCCESS).append(SEPARATOR)
            .append(POST_SUCCESS_LOOK).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 发布成功页面
     */
    public static final String POST_SUCCESS_VIEW = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(POST_SUCCESS).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();

    /**
     * 发布课堂页面-确认发布
     */
    public static final String POST_LIVE_CONFIRM_CLICK = new StringBuilder(POST_LIVE).append(SEPARATOR)
            .append(POST_LIVE_CONFIRM).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 发布课堂页面-发布预告
     */
    public static final String POST_LIVE_POST_TRAILER_CLICK = new StringBuilder(POST_LIVE).append(SEPARATOR)
            .append(POST_LIVE_POST_TRAILER).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 发布课堂页面-添加课时
     */
    public static final String POST_LIVE_ADD_COURSE_CLICK = new StringBuilder(POST_LIVE).append(SEPARATOR)
            .append(POST_LIVE_ADD_COURSE).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 发布课堂页面-设置折扣
     */
    public static final String POST_LIVE_DISCOUNT_CLICK = new StringBuilder(POST_LIVE).append(SEPARATOR)
            .append(POST_LIVE_DISCOUNT).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 发布课堂页面-添加话题
     */
    public static final String POST_LIVE_ADD_TOPIC_CLICK = new StringBuilder(POST_LIVE).append(SEPARATOR)
            .append(POST_LIVE_ADD_TOPIC).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 发布课堂页面
     */
    public static final String POST_LIVE_VIEW = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(POST_LIVE).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();
    /**
     * 课堂订阅成功-邀请好友订阅
     */
    public static final String BUY_SUCCESS_SHARE_CLICK = new StringBuilder(BUY_SUCCESS).append(SEPARATOR)
            .append(BUY_SUCCESS_SHARE).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 课堂订阅成功-查看订阅的课堂
     */
    public static final String BUY_SUCCESS_LOOK_CLICK = new StringBuilder(BUY_SUCCESS).append(SEPARATOR)
            .append(BUY_SUCCESS_LOOK).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 访问课堂订阅成功页面
     */
    public static final String BUY_SUCCESS_VIEW = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(BUY_SUCCESS).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();

    /**
     * 参与讨论
     */

    public static final String EVENT_16016 = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(ID_16016).append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 参与讨论-发布
     */
    public static final String EVENT_16017 = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(ID_16017).append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 支付清单右下角按钮
     */
    public static final String PAY_COURSE_COMMIT_CLICK = new StringBuilder(PAY_COURSE).append(SEPARATOR)
            .append(PAY_COURSE_COMMIT).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 访问支付清单页
     */
    public static final String PAY_COURSE_VIEW = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(PAY_COURSE).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();

    /**
     * 只订阅本节课
     */
    public static final String COURSE_DETAIL_COMMIT_BUY_SINGLE_CLICK = new StringBuilder(COURSE_DETAIL).append(SEPARATOR)
            .append(COURSE_DETAIL_COMMIT_BUY_SINGLE).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 订阅完整课堂
     */
    public static final String COURSE_DETAIL_COMMIT_BUY_ENTIRE_CLICK = new StringBuilder(COURSE_DETAIL).append(SEPARATOR)
            .append(COURSE_DETAIL_COMMIT_BUY_ENTIRE).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 课堂详情页右下角的按钮
     */
    public static final String COURSE_DETAIL_COMMIT_CLICK = new StringBuilder(COURSE_DETAIL).append(SEPARATOR)
            .append(COURSE_DETAIL_COMMIT).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 课堂其他课程列表
     */
    public static final String COURSE_DETAIL_SIMILAR_CLICK = new StringBuilder(COURSE_DETAIL).append(SEPARATOR)
            .append(COURSE_DETAIL_SIMILAR).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 访问课堂详情页
     */
    public static final String COURSE_DETAIL_SHOW_EVENT_ID = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(COURSE_DETAIL).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();

    /**
     * 播放课堂详情页面的预告
     */
    public static final String COURSE_DETAIL_PLAY_TRAILER_CLICK = new StringBuilder(COURSE_DETAIL).append(SEPARATOR)
            .append(COURSE_DETAIL_PLAY_TRAILER).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 播放课堂详情页面的课堂话题
     */
    public static final String COURSE_DETAIL_PLAY_TOPICS_CLICK = new StringBuilder(COURSE_DETAIL).append(SEPARATOR)
            .append(COURSE_DETAIL_TOPICS).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 播放课堂详情页面的主播信息
     */
    public static final String COURSE_DETAIL_USER_INFO_CLICK = new StringBuilder(COURSE_DETAIL).append(SEPARATOR)
            .append(COURSE_DETAIL_USER_INFO).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 播放课堂详情页面的订阅用户
     */
    public static final String COURSE_DETAIL_SUBSCRIBE_CLICK = new StringBuilder(COURSE_DETAIL).append(SEPARATOR)
            .append(COURSE_DETAIL_SUBSCRIBE).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();


    /**
     * 登录页访问
     */
    public static final String LOGIN_SHOW_EVENT_ID = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(LOGIN_VIEW_ID).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();

    /**
     * 登录页-微信登录按钮点击
     */
    public static final String LOGIN_WECHAT_BUTTON_CLICK_EVENT_ID = new StringBuilder(LOGIN_VIEW_ID).append(SEPARATOR)
            .append(LOGIN_WECHAT_BUTTON_VIEW_ID).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

//    /**
//     * 登录页-微信登录按钮登录成功
//     */
//    public static final String LOGIN_WECHAT_BUTTON_LOGIN_SUCCESS_EVENT_ID = new StringBuilder(LOGIN_VIEW_ID).append(SEPARATOR)
//            .append(LOGIN_WECHAT_BUTTON_VIEW_ID).append(SEPARATOR).append(LOGIN_SUCCESS_EVENT_ID)
//            .toString();
//
//    /**
//     * 登录页-微信登录按钮登录失败
//     */
//    public static final String LOGIN_WECHAT_BUTTON_LOGIN_FAIL_EVENT_ID = new StringBuilder(LOGIN_VIEW_ID).append(SEPARATOR)
//            .append(LOGIN_WECHAT_BUTTON_VIEW_ID).append(SEPARATOR).append(LOGIN_FAIL_EVENT_ID)
//            .toString();
//
    /**
     * 手机号登录页访问
     */
    public static final String PHONE_LOGIN_SHOW_EVENT_ID = new StringBuilder(LOGIN_VIEW_ID).append(SEPARATOR)
            .append(LOGIN_PHONE_VIEW_ID).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();


    /**
     * 手机号登录按钮点击
     */
    public static final String PHONE_LOGIN_BUTTON_CLICK_EVENT_ID = new StringBuilder(LOGIN_PHONE_VIEW_ID).append(SEPARATOR)
            .append(PHONE_LOGIN_BUTTON_VIEW_ID).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
//
//    /**
//     * 手机号登录按钮登录成功
//     */
//    public static final String PHONE_LOGIN_BUTTON_LOGIN_SUCCESS_EVENT_ID = new StringBuilder(LOGIN_PHONE_VIEW_ID).append(SEPARATOR)
//            .append(PHONE_LOGIN_BUTTON_VIEW_ID).append(SEPARATOR).append(LOGIN_SUCCESS_EVENT_ID)
//            .toString();
//
//    /**
//     * 手机号登录按钮登录失败
//     */
//    public static final String PHONE_LOGIN_BUTTON_LOGIN_FAIL_EVENT_ID = new StringBuilder(LOGIN_PHONE_VIEW_ID).append(SEPARATOR)
//            .append(PHONE_LOGIN_BUTTON_VIEW_ID).append(SEPARATOR).append(LOGIN_FAIL_EVENT_ID)
//            .toString();
//
    /**
     * 微博登录登录按钮点击
     */
    public static final String WEIBO_LOGIN_BUTTON_CLICK_EVENT_ID = new StringBuilder(LOGIN_VIEW_ID).append(SEPARATOR)
            .append(LOGIN_WEIBO_BUTTON_VIEW_ID).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
//
//    /**
//     * 微博登录登录按钮登录成功
//     */
//    public static final String WEIBO_LOGIN_BUTTON_LOGIN_SUCCESS_EVENT_ID = new StringBuilder(LOGIN_VIEW_ID).append(SEPARATOR)
//            .append(LOGIN_WEIBO_BUTTON_VIEW_ID).append(SEPARATOR).append(LOGIN_SUCCESS_EVENT_ID)
//            .toString();
//
//    /**
//     * 微博登录登录按钮登录失败
//     */
//    public static final String WEIBO_LOGIN_BUTTON_LOGIN_FAIL_EVENT_ID = new StringBuilder(LOGIN_VIEW_ID).append(SEPARATOR)
//            .append(LOGIN_WEIBO_BUTTON_VIEW_ID).append(SEPARATOR).append(LOGIN_FAIL_EVENT_ID)
//            .toString();
//
    /**
     * 其他登录方式QQ登录按钮点击
     */
    public static final String QQ_LOGIN_BUTTON_CLICK_EVENT_ID = new StringBuilder(LOGIN_VIEW_ID).append(SEPARATOR)
            .append(LOGIN_QQ_BUTTON_VIEW_ID).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
//
//    /**
//     * 其他登录方式QQ登录按钮登录成功
//     */
//    public static final String QQ_LOGIN_BUTTON_LOGIN_SUCCESS_EVENT_ID = new StringBuilder(LOGIN_VIEW_ID).append(SEPARATOR)
//            .append(LOGIN_QQ_BUTTON_VIEW_ID).append(SEPARATOR).append(LOGIN_SUCCESS_EVENT_ID)
//            .toString();
//
//    /**
//     * 其他登录方式QQ登录按钮登录失败
//     */
//    public static final String QQ_LOGIN_BUTTON_LOGIN_FAIL_EVENT_ID = new StringBuilder(LOGIN_VIEW_ID).append(SEPARATOR)
//            .append(LOGIN_QQ_BUTTON_VIEW_ID).append(SEPARATOR).append(LOGIN_FAIL_EVENT_ID)
//            .toString();

    /**
     * 首页展示
     */
    public static final String HOME_SHOW_EVENT_ID = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR).append(HOME_VIEW_ID)
            .append(SEPARATOR).append(SHOW_EVENT_ID).toString();

    /**
     * 首页发现按钮点击
     */
    public static final String HOME_FOUND_BUTTON_CLICK_EVENT_ID = new StringBuilder(HOME_VIEW_ID).append(SEPARATOR).append(HOME_TOPICS_CLICK_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页热门按钮点击
     */
    public static final String HOME_HOT_BUTTON_CLICK_EVENT_ID = new StringBuilder(HOME_VIEW_ID).append(SEPARATOR).append(HOME_VIDEO_CLICK_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页排行榜按钮点击
     */
    public static final String HOME_RANK_BUTTON_CLICK_EVENT_ID = new StringBuilder(HOME_VIEW_ID).append(SEPARATOR).append(HOME_RANK_BUTTON_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页搜索按钮点击
     */
    public static final String HOME_SEARCH_BUTTON_CLICK_EVENT_ID = new StringBuilder(HOME_VIEW_ID).append(SEPARATOR).append(HOME_SEARCH_BUTTON_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();


    /**
     * 首页banner点击
     */
    public static final String HOME_BANNER_CLICK_EVENT_ID = new StringBuilder(HOME_VIEW_ID).append(SEPARATOR).append(HOME_BANNER_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 直播用户信息点击
     */
    public static final String LIVE_USER_INFO_CLICK_EVENT_ID = new StringBuilder(HOME_VIEW_ID).append(SEPARATOR).append(HOME_USER_INFO_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 视频用户信息点击
     */
    public static final String VIDEO_USER_INFO_CLICK_EVENT_ID = new StringBuilder(HOME_VIDEO_VIEW_ID).append(SEPARATOR).append(HOME_VIDEO_USER_CLICK_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 话题详情用户信息点击
     */
    public static final String TOPICS_USER_INFO_CLICK_EVENT_ID = new StringBuilder(HOME_TOPICS_TOPIC_DETAIL_VIEW_ID).append(SEPARATOR).append(HOME_TOPICS_USER_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();


    /**
     * 话题详情访问
     */
    public static final String TOPIC_DETAIL = new StringBuilder(DEFAULT_VIEW_ID)
            .append(SEPARATOR).append(HOME_TOPICS_TOPIC_DETAIL_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 话题详情课堂封面点击
     */
    public static final String TOPICS_COVER_CLICK_EVENT_ID = new StringBuilder(HOME_TOPICS_TOPIC_DETAIL_VIEW_ID)
            .append(SEPARATOR).append(HOME_TOPICS_TOPIC_COVER_CLICK_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页直播话题点击
     */
    public static final String HOME_LIVE_TOPIC_ITEM_CLICK_EVENT_ID = new StringBuilder(HOME_VIEW_ID).append(SEPARATOR).append(HOME_LIVE_TOPIC_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();
    /**
     * 首页视频话题点击
     */
    public static final String HOME_VIDEO_TOPIC_ITEM_CLICK_EVENT_ID = new StringBuilder(HOME_VIDEO_VIEW_ID).append(SEPARATOR).append(HOME_VIDEO_TOPIC_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();


    public static final String HOME_FOUND_SHOW_EVENT_ID = new StringBuilder(HOME_VIEW_ID).append(SEPARATOR).append(HOME_FOUND_VIEW_ID)
            .append(SEPARATOR).append(SHOW_EVENT_ID).toString();

    /**
     * 首页-发现-话题推荐位点击事件
     */
    public static final String HOME_FOUND_RMD_TOPIC_ITEM_CLICK_EVENT_ID = new StringBuilder(HOME_TOPICS_VIEW_ID).append(SEPARATOR).append(HOME_FOUND_MORE_TOPIC_ACTIVITY_ITEM_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页-发现-更多话题点击事件
     */
    public static final String HOME_TOPICS_MORE_TOPIC_CLICK_EVENT_ID = new StringBuilder(HOME_TOPICS_VIEW_ID).append(SEPARATOR).append(HOME_FOUND_TOPIC_SHOW_MORE_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();


    /**
     * 首页-发现-最新列表点击事件
     */
    public static final String HOME_FOUND_NEWEST_ITEM_CLICK_EVENT_ID = new StringBuilder(HOME_FOUND_VIEW_ID).append(SEPARATOR).append(HOME_VIDEO_COVER_CLICK_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();


    public static final String HOME_FOUND_TOPIC_DETAIL_ITEM_CLICK_VIEW_ID = new StringBuilder(HOME_VIDEO_TOPIC_VIEW_ID).append(SEPARATOR).append(HOME_VIDEO_NAME_CLICK_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();


    public static final String HOME_FOUND_MORE_TOPIC_ACTIVITY_SHOW_ID = new StringBuilder(HOME_FOUND_VIEW_ID).append(SEPARATOR).append(HOME_TOPICS_VIEW_ID)
            .append(SEPARATOR).append(SHOW_EVENT_ID).toString();

    public static final String HOME_FOUND_MORE_TOPIC_ACTIVITY_ITEM_CLICK_ID = new StringBuilder(HOME_TOPICS_VIEW_ID)
            .append(SEPARATOR).append(HOME_TOPICS_MORE_TOPIC_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    public static final String HOME_TOPICS_TOPIC_DETAIL = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR).append(HOME_TOPICS_TOPIC_DETAIL_VIEW_ID)
            .append(SEPARATOR).append(SHOW_EVENT_ID).toString();

    public static final String HOME_FOUND_TOPIC_SHOW_MORE_CLICK_ID = new StringBuilder(HOME_FOUND_VIEW_ID).append(SEPARATOR).append(HOME_FOUND_TOPIC_SHOW_MORE_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();


    public static final String HOME_FOUND_TOPIC_SHOW_LIVE_CLICK_ID = new StringBuilder(HOME_FOUND_VIEW_ID).append(SEPARATOR).append(HOME_FOUND_TOPIC_SHOW_LIVE_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();


    public static final String HOME_RANK_SEND_SHOW_ID = new StringBuilder(HOME_VIEW_ID).append(SEPARATOR).append(HOME_RANK_SEND_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    public static final String HOME_RANK_GET_SHOW_ID = new StringBuilder(HOME_VIEW_ID).append(SEPARATOR).append(HOME_RANK_GET_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    public static final String HOME_RANK_FANS_SHOW_ID = new StringBuilder(HOME_VIEW_ID).append(SEPARATOR).append(HOME_RANK_FANS_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    public static final String FRIEND_DYNAMIC_SHOW_ID = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR).append(FRIEND_DYNAMIC_VIEW_ID)
            .append(SEPARATOR).append(SHOW_EVENT_ID).toString();

    public static final String FRIEND_DYNAMIC_ITEM_CLICK_ID = new StringBuilder(FRIEND_DYNAMIC_VIEW_ID).append(SEPARATOR).append(FRIEND_DYNAMIC_ITEM_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();


    /**
     * 搜索页展示
     */
    public static final String SEARCH_SHOW_EVENT_ID = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR).append(SEARCH_VIEW_ID)
            .append(SEPARATOR).append(SHOW_EVENT_ID).toString();

    /**
     * 搜索-取消
     */
    public static final String EVENT_11001 = new StringBuilder(SEARCH_VIEW_ID).append(SEPARATOR).append(ID_11001)
            .append(SEPARATOR).append(SHOW_EVENT_ID).toString();


    /**
     * 搜索页结果非空展示
     */
    public static final String SEARCH_NOT_NULL_SHOW_EVENT_ID = new StringBuilder(SEARCH_VIEW_ID).append(SEPARATOR).append(SEARCH_NOT_NULL_VIEW_ID)
            .append(SEPARATOR).append(SHOW_EVENT_ID).toString();

    /**
     * 搜索页结果空展示
     */
    public static final String SEARCH_NULL_SHOW_EVENT_ID = new StringBuilder(SEARCH_VIEW_ID).append(SEPARATOR).append(SEARCH_NULL_VIEW_ID)
            .append(SEPARATOR).append(SHOW_EVENT_ID).toString();

    /**
     * 搜索热词item点击
     */
    public static final String SEARCH_HOT_WORD_ITEM_CLICK_EVENT_ID = new StringBuilder(SEARCH_VIEW_ID).append(SEPARATOR).append(SEARCH_HOT_WORD_ITEM_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 搜索按钮点击
     */
    public static final String SEARCH_BUTTON_CLICK_EVENT_ID = new StringBuilder(SEARCH_VIEW_ID).append(SEPARATOR).append(SEARCH_BUTTON_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 搜索结果列表点击
     */
    public static final String SEARCH_RESULT_ITEM_CLICK_EVENT_ID = new StringBuilder(SEARCH_NOT_NULL_VIEW_ID).append(SEPARATOR).append(SEARCH_RESULT_ITEM_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 搜索结果列表的关注按钮点击
     */
    public static final String SEARCH_RESULT_FOLLOW_BUTTON_CLICK_EVENT_ID = new StringBuilder(SEARCH_NOT_NULL_VIEW_ID).append(SEPARATOR)
            .append(SEARCH_RESULT_FOLLOW_BUTTON_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    public static final String SEARCH_NULL_EDIT_CLICK_EVENT_ID = new StringBuilder(SEARCH_NULL_VIEW_ID).append(SEPARATOR)
            .append(SEARCH_EDIT_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 我的展示
     */
    public static final String MY_SHOW_EVENT_ID = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(MY_VIEW_ID).append(SEPARATOR).append(SHOW_EVENT_ID).toString();

    /**
     * 设置点击事件
     */
    public static final String MY_SETTING_CLICK_EVENT_ID = new StringBuilder(MY_VIEW_ID).append(SEPARATOR)
            .append(MY_SETTING_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 我的-直播点击事件
     */
    public static final String MY_LIVE_CLICK_EVENT_ID = new StringBuilder(MY_VIEW_ID).append(SEPARATOR)
            .append(MY_LIVE_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 我的-关注点击事件
     */
    public static final String MY_FOLLOW_CLICK_EVENT_ID = new StringBuilder(MY_VIEW_ID).append(SEPARATOR)
            .append(MY_FOLLOW_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 我的-粉丝点击事件
     */
    public static final String MY_FANS_CLICK_EVENT_ID = new StringBuilder(MY_VIEW_ID).append(SEPARATOR)
            .append(MY_FANS_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 我的编辑点击事件
     */
    public static final String MY_EDIT_CLICK_EVENT_ID = new StringBuilder(MY_VIEW_ID).append(SEPARATOR)
            .append(MY_EDIT_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 我的收益点击事件
     */
    public static final String MY_INCOME_CLICK_EVENT_ID = new StringBuilder(MY_VIEW_ID).append(SEPARATOR)
            .append(MY_INCOME_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 我的等级点击事件
     */
    public static final String MY_LEVEL_CLICK_EVENT_ID = new StringBuilder(MY_VIEW_ID).append(SEPARATOR)
            .append(MY_LEVEL_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 我的钻石点击事件
     */
    public static final String MY_KA_ZUAN_CLICK_EVENT_ID = new StringBuilder(MY_VIEW_ID).append(SEPARATOR)
            .append(MY_KA_ZUAN_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 我的贡献榜点击事件
     */
    public static final String MY_CONTRIBUTION_LIST_CLICK_EVENT_ID = new StringBuilder(MY_VIEW_ID).append(SEPARATOR)
            .append(MY_CONTRIBUTION_LIST_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 设置页面展示
     */
    public static final String MY_SETTING_SHOW_EVENT_ID = new StringBuilder(MY_VIEW_ID).append(SEPARATOR)
            .append(MY_SETTING_ACTIVITY_VIEW_ID)
            .append(SEPARATOR).append(SHOW_EVENT_ID).toString();

    /**
     * 关于滋味Live
     */
    public static final String SETTING_ABOUT_US_CLICK_EVENT_ID = new StringBuilder(MY_SETTING_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(SETTING_ABOUT_US_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();
    /**
     * 清理缓存
     */
    public static final String SETTING_CLEAR_CACHE_CLICK_EVENT_ID = new StringBuilder(MY_SETTING_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(SETTING_CLEAR_CACHE_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();
    /**
     * 消息提醒
     */
    public static final String SETTING_MSG_REMIND_CLICK_EVENT_ID = new StringBuilder(MY_SETTING_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(SETTING_MSG_REMIND_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();
    /**
     * 意见反馈
     */
    public static final String SETTING_FEEDBACK_CLICK_EVENT_ID = new StringBuilder(MY_SETTING_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(SETTING_FEEDBACK_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();
    /**
     * 退出登录
     */
    public static final String SETTING_LOGOUT_CLICK_EVENT_ID = new StringBuilder(MY_SETTING_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(SETTING_LOGOUT_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

   /* public static final String MY_LIVE_REPLAY_SHOE_EVENT_ID = new StringBuilder(MY_VIEW_ID).append(SEPARATOR)
            .append(MY_LIVE_REPLAY_VIEW_ID)
            .append(SEPARATOR).append(SHOW_EVENT_ID).toString();

    public static final String MY_LIVE_REPLAY_ITEM_CLICK_EVENT_ID = new StringBuilder(MY_LIVE_REPLAY_VIEW_ID).append(SEPARATOR)
            .append(MY_LIVE_REPLAY_ITEM_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    public static final String MY_FOLLOW_ACTIVITY_SHOW_EVENT_ID = new StringBuilder(MY_VIEW_ID).append(SEPARATOR)
            .append(MY_FOLLOW_ACTIVITY_VIEW_ID)
            .append(SEPARATOR).append(SHOW_EVENT_ID).toString();

    public static final String MY_FOLLOW_ACTIVITY_ITEM_CLICK_EVENT_ID = new StringBuilder(MY_FOLLOW_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(MY_FOLLOW_LIST_ITEM_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    public static final String MY_FOLLOW_ACTIVITY_ITEM_FOLLOW_BUTTON_CLICK_EVENT_ID = new StringBuilder(MY_FOLLOW_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(MY_FOLLOW_LIST_ITEM_FOLLOW_BUTTON_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    public static final String MY_FANS_ACTIVITY_SHOW_EVENT_ID = new StringBuilder(MY_VIEW_ID).append(SEPARATOR)
            .append(MY_FANS_ACTIVITY_VIEW_ID)
            .append(SEPARATOR).append(SHOW_EVENT_ID).toString();

    public static final String MY_FANS_ACTIVITY_ITEM_CLICK_EVENT_ID = new StringBuilder(MY_FANS_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(MY_FANS_LIST_ITEM_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    public static final String MY_FANS_ACTIVITY_ITEM_FOLLOW_BUTTON_CLICK_EVENT_ID = new StringBuilder(MY_FANS_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(MY_FANS_LIST_ITEM_FOLLOW_BUTTON_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();
*/
    /**
     * 我的收益页面展示
     */
    public static final String MY_INCOME_ACTIVITY_SHOW_EVENT_ID = new StringBuilder(MY_VIEW_ID).append(SEPARATOR)
            .append(MY_INCOME_ACTIVITY_VIEW_ID)
            .append(SEPARATOR).append(SHOW_EVENT_ID).toString();
    /**
     * 我的收益-钻石兑换
     */
    public static final String MY_INCOME_EXCHANGE_DIAMOND_CLICK_EVENT_ID = new StringBuilder(MY_INCOME_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(MY_INCOME_EXCHANGE_DIAMOND_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();
    /**
     * 我的收益-微信提现
     */
    public static final String MY_INCOME_WECHAT_WITHDRAWAL_CLICK_EVENT_ID = new StringBuilder(MY_INCOME_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(MY_INCOME_WECHAT_WITHDRAWAL_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 我的收益-交易记录
     */
    public static final String MY_INCOME_TRANSACTION_CLICK_EVENT_ID = new StringBuilder(MY_INCOME_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(MY_INCOME_TRANSACTION_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();
    /**
     * 兑换钻石界面展示
     */
    public static final String EXCHANGE_DIAMOND_ACTIVITY_SHOW_EVENT_ID = new StringBuilder(MY_INCOME_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(EXCHANGE_DIAMOND_ACTIVITY_VIEW_ID)
            .append(SEPARATOR).append(SHOW_EVENT_ID).toString();
    /**
     * 点击兑换按钮
     */
    public static final String EXCHANGE_DIAMOND_ACTIVITY_SURE_BUTTON_CLICK_EVENT_ID = new StringBuilder(EXCHANGE_DIAMOND_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(EXCHANGE_DIAMOND_ACTIVITY_SURE_BUTTON_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();
    /**
     * 绑定微信界面
     */
    public static final String BIND_WECHAT_ACTIVITY_SHOW_EVENT_ID = new StringBuilder(MY_INCOME_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(BIND_WECHAT_ACTIVITY_VIEW_ID)
            .append(SEPARATOR).append(SHOW_EVENT_ID).toString();
    /**
     * 绑定微信按钮点击
     */
    public static final String BIND_WECHAT_ACTIVITY_SURE_BUTTON_CLICK_EVENT_ID = new StringBuilder(BIND_WECHAT_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(BIND_WECHAT_ACTIVITY_SURE_BUTTON_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 绑定手机界面
     */
    public static final String BIND_PHONE_ACTIVITY_SHOW_EVENT_ID = new StringBuilder(MY_INCOME_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(BIND_PHONE_ACTIVITY_VIEW_ID)
            .append(SEPARATOR).append(SHOW_EVENT_ID).toString();
    /**
     * 绑定手机-手机号输入框点击
     */
    public static final String BIND_PHONE_ACTIVITY_PHONE_INPUT_CLICK_EVENT_ID = new StringBuilder(BIND_PHONE_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(BIND_PHONE_ACTIVITY_PHONE_INPUT_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();
    /**
     * 绑定手机-获取验证码按钮点击
     */
    public static final String BIND_PHONE_ACTIVITY_GET_PVC_BUTTON_CLICK_EVENT_ID = new StringBuilder(BIND_PHONE_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(BIND_PHONE_ACTIVITY_GET_PVC_BUTTON_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();
    /**
     * 绑定手机-验证码输入框点击
     */
    public static final String BIND_PHONE_ACTIVITY_PVC_INPUT_CLICK_EVENTW_ID = new StringBuilder(BIND_PHONE_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(BIND_PHONE_ACTIVITY_PVC_INPUT_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();
    /**
     * 绑定手机-确认绑定按钮点击
     */
    public static final String BIND_PHONE_ACTIVITY_SURE_BUTTON_CLICK_EVENT_ID = new StringBuilder(BIND_PHONE_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(BIND_PHONE_ACTIVITY_SURE_BUTTON_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();
    /**
     * 微信提现界面
     */
    public static final String WECHAT_WITHDRAWAL_ACTIVITY_SHOW_EVENT_ID = new StringBuilder(MY_INCOME_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(WECHAT_WITHDRAWAL_ACTIVITY_VIEW_ID)
            .append(SEPARATOR).append(SHOW_EVENT_ID).toString();
    /**
     * 微信提现按钮点击
     */
    public static final String WECHAT_WITHDRAWAL_ACTIVITY_SURE_BUTTON_CLICK_EVENT_ID = new StringBuilder(WECHAT_WITHDRAWAL_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(WECHAT_WITHDRAWAL_ACTIVITY_SURE_BUTTON_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();
    /**
     * 我的钻石界面
     */
    public static final String MY_COINS_ACTIVITY_SHOW_EVENT_ID = new StringBuilder(MY_VIEW_ID).append(SEPARATOR)
            .append(MY_COINS_ACTIVITY_VIEW_ID)
            .append(SEPARATOR).append(SHOW_EVENT_ID).toString();

    /**
     * 微信支付tab点击
     */
    public static final String MY_COINS_ACTIVITY_WECHAT_BUTTON_CLICK_EVENT_ID = new StringBuilder(MY_COINS_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(MY_COINS_ACTIVITY_WECHAT_BUTTON_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 微信支付列表
     */
    public static final String MY_COINS_ACTIVITY_WECHAT_ITEM_CLICK_EVENT_ID = new StringBuilder(MY_COINS_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(MY_COINS_ACTIVITY_WECHAT_ITEM_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 支付宝tab点击
     */
    public static final String MY_COINS_ACTIVITY_ALIPAY_BUTTON_CLICK_EVENT_ID = new StringBuilder(MY_COINS_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(MY_COINS_ACTIVITY_ALIPAY_BUTTON_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();
    /**
     * 支付宝列表
     */
    public static final String MY_COINS_ACTIVITY_ALIPAY_ITEM_CLICK_EVENT_ID = new StringBuilder(MY_COINS_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(MY_COINS_ACTIVITY_ALIPAY_ITEM_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 用户信息页
     */
    public static final String OTHER_USER_INFO_ACTIVITY_SHOW_EVENT_ID = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(OTHER_USER_INFO_ACTIVITY_VIEW_ID)
            .append(SEPARATOR).append(SHOW_EVENT_ID).toString();

  /*  public static final String OTHER_USER_INFO_ACTIVITY_LIVE_BUTTON_CLICK_EVENT_ID = new StringBuilder(OTHER_USER_INFO_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(OTHER_USER_INFO_ACTIVITY_LIVE_BUTTON_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();*/
    /**
     * 用户信息页-关注按钮点击
     */
    public static final String OTHER_USER_INFO_ACTIVITY_FOLLOW_BUTTON_CLICK_EVENT_ID = new StringBuilder(OTHER_USER_INFO_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(OTHER_USER_INFO_ACTIVITY_FOLLOW_BUTTON_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();
    /**
     * 用户信息页-私信按钮点击
     */
    public static final String OTHER_USER_INFO_ACTIVITY_LETTER_BUTTON_CLICK_EVENT_ID = new StringBuilder(OTHER_USER_INFO_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(OTHER_USER_INFO_ACTIVITY_LETTER_BUTTON_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();
    /**
     * 用户信息页-拉黑按钮点击
     */
    public static final String OTHER_USER_INFO_ACTIVITY_BLOCK_BUTTON_CLICK_EVENT_ID = new StringBuilder(OTHER_USER_INFO_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(OTHER_USER_INFO_ACTIVITY_BLOCK_BUTTON_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 用户信息页-关注列表按钮点击
     */
    public static final String OTHER_USER_INFO_ACTIVITY_FOLLOW_LIST_BUTTON_CLICK_EVENT_ID = new StringBuilder(OTHER_USER_INFO_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(OTHER_USER_INFO_ACTIVITY_FOLLOW_LIST_BUTTON_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 用户信息页-粉丝列表按钮点击
     */
    public static final String OTHER_USER_INFO_ACTIVITY_FANS_LIST_BUTTON_CLICK_EVENT_ID = new StringBuilder(OTHER_USER_INFO_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(OTHER_USER_INFO_ACTIVITY_FANS_LIST_BUTTON_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();
    /**
     * 用户信息页-贡献榜列表按钮点击
     */
    public static final String OTHER_USER_INFO_ACTIVITY_CONTRIBUTION_LIST_BUTTON_CLICK_EVENT_ID = new StringBuilder(OTHER_USER_INFO_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(OTHER_USER_INFO_ACTIVITY_CONTRIBUTION_LIST_BUTTON_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

  /*  public static final String OTHER_USER_INFO_ACTIVITY_LIVE_LIST_SHOW_EVENT_ID = new StringBuilder(OTHER_USER_INFO_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(OTHER_USER_INFO_ACTIVITY_LIVE_LIST_VIEW_ID)
            .append(SEPARATOR).append(SHOW_EVENT_ID).toString();*/

    public static final String OTHER_USER_INFO_ACTIVITY_LIVE_LIST_ITEM_CLICK_EVENT_ID = new StringBuilder(OTHER_USER_INFO_ACTIVITY_VIEW_ID)
            .append(SEPARATOR)
            .append(OTHER_USER_INFO_ACTIVITY_LIVE_LIST_ITEM_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    public static final String OTHER_USER_INFO_ACTIVITY_VIDEO_LIST_ITEM_CLICK_EVENT_ID = new StringBuilder(OTHER_USER_INFO_ACTIVITY_VIEW_ID)
            .append(SEPARATOR)
            .append("15450")
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    public static final String BITMAP_LRUCACHE_ENTRYREMOVE_METHOD_CALLED = "bitmap_lruCache_entryRemoved_method_called";

    /**
     * 首页-直播访问
     */
    public static final String HOME_LIVE_SHOW_EVENT_ID = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR).append(HOME_VIEW_ID)
            .append(SEPARATOR).append(SHOW_EVENT_ID).toString();

    /**
     * 首页-视频访问
     */
    public static final String HOME_VIDEO_SHOW_EVENT_ID = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR).append(HOME_VIDEO_VIEW_ID)
            .append(SEPARATOR).append(SHOW_EVENT_ID).toString();

    /**
     * 首页-话题访问
     */
    public static final String HOME_TOPICS_SHOW_EVENT_ID = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR).append(HOME_TOPICS_VIEW_ID)
            .append(SEPARATOR).append(SHOW_EVENT_ID).toString();

    /**
     * 首页-直播点击
     */
    public static final String HOME_LIVE_CLICK_EVENT_ID = new StringBuilder(HOME_VIEW_ID).append(SEPARATOR).append(HOME_LIVE_CLICK_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页-视频点击
     */
    public static final String HOME_VIDEO_CLICK_EVENT_ID = new StringBuilder(HOME_VIEW_ID).append(SEPARATOR).append(HOME_VIDEO_CLICK_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页-直播-课堂封面点击
     */
    public static final String HOME_LIVE_COVER_CLICK_EVENT_ID = new StringBuilder(HOME_VIEW_ID).append(SEPARATOR).append(HOME_HOT_LIST_ITEM_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();


    /**
     * 首页-话题-课堂封面点击
     */
    public static final String HOME_TOPICS_COVER_CLICK_EVENT_ID = new StringBuilder(HOME_TOPICS_VIEW_ID).append(SEPARATOR).append(HOME_FOUND_TOPIC_SHOW_LIVE_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页-话题-话题点击
     */
    public static final String HOME_TOPICS_TOPIC_CLICK_EVENT_ID = new StringBuilder(HOME_TOPICS_VIEW_ID).append(SEPARATOR).append(HOME_TOPICS_TOPIC_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();
    /**
     * 首页-话题点击
     */
    public static final String HOME_TOPICS_CLICK_EVENT_ID = new StringBuilder(HOME_VIEW_ID).append(SEPARATOR).append(HOME_TOPICS_CLICK_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();


    /**
     * 首页-直播-课程名称点击
     */
    public static final String HOME_LIVE_NAME_CLICK_EVENT_ID = new StringBuilder(HOME_VIEW_ID).append(SEPARATOR).append(HOME_FOUND_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 首页-视频-课堂封面点击
     */
    public static final String HOME_VIDEO_COVER_CLICK_EVENT_ID = new StringBuilder(HOME_VIDEO_VIEW_ID).append(SEPARATOR).append(HOME_VIDEO_COVER_CLICK_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();


    /**
     * 首页-视频-课程名称点击
     */
    public static final String HOME_VIDEO_NAME_CLICK_EVENT_ID = new StringBuilder(HOME_VIDEO_VIEW_ID).append(SEPARATOR).append(HOME_VIDEO_NAME_CLICK_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();
    //  here


   /* public static final String OTHER_USER_INFO_ACTIVITY_FOLLOW_LIST_SHOW_EVENT_ID = new StringBuilder(OTHER_USER_INFO_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(OTHER_USER_INFO_ACTIVITY_FOLLOW_LIST_VIEW_ID)
            .append(SEPARATOR).append(SHOW_EVENT_ID).toString();*/

   /* public static final String OTHER_USER_INFO_ACTIVITY_FOLLOW_LIST_ITEM_CLICK_EVENT_ID = new StringBuilder(OTHER_USER_INFO_ACTIVITY_FOLLOW_LIST_VIEW_ID)
            .append(SEPARATOR)
            .append(OTHER_USER_INFO_ACTIVITY_FOLLOW_LIST_ITEM_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();*/

    /*public static final String OTHER_USER_INFO_ACTIVITY_FOLLOW_LIST_ITEM_FOLLOW_CLICK_EVENT_ID = new StringBuilder(OTHER_USER_INFO_ACTIVITY_FOLLOW_LIST_VIEW_ID)
            .append(SEPARATOR)
            .append(OTHER_USER_INFO_ACTIVITY_FOLLOW_LIST_ITEM_FOLLOW_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();
*/
   /* public static final String OTHER_USER_INFO_ACTIVITY_FOLLOW_LIST_ITEM_CANCEL_FOLLOW_CLICK_EVENT_ID =
            new StringBuilder(OTHER_USER_INFO_ACTIVITY_FOLLOW_LIST_VIEW_ID)
                    .append(SEPARATOR)
                    .append(OTHER_USER_INFO_ACTIVITY_FOLLOW_LIST_ITEM_CANCEL_FOLLOW_VIEW_ID)
                    .append(SEPARATOR).append(CLICK_EVENT_ID).toString();*/

   /* public static final String OTHER_USER_INFO_ACTIVITY_FANS_LIST_SHOW_EVENT_ID = new StringBuilder(OTHER_USER_INFO_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(OTHER_USER_INFO_ACTIVITY_FANS_LIST_VIEW_ID)
            .append(SEPARATOR).append(SHOW_EVENT_ID).toString();

    public static final String OTHER_USER_INFO_ACTIVITY_FANS_LIST_ITEM_CLICK_EVENT_ID = new StringBuilder(OTHER_USER_INFO_ACTIVITY_FANS_LIST_VIEW_ID)
            .append(SEPARATOR)
            .append(OTHER_USER_INFO_ACTIVITY_FANS_LIST_ITEM_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    public static final String OTHER_USER_INFO_ACTIVITY_FANS_LIST_ITEM_FOLLOW_CLICK_EVENT_ID = new StringBuilder(OTHER_USER_INFO_ACTIVITY_FANS_LIST_VIEW_ID)
            .append(SEPARATOR)
            .append(OTHER_USER_INFO_ACTIVITY_FANS_LIST_ITEM_FOLLOW_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    public static final String OTHER_USER_INFO_ACTIVITY_FANS_LIST_ITEM_CANCEL_FOLLOW_CLICK_EVENT_ID =
            new StringBuilder(OTHER_USER_INFO_ACTIVITY_FANS_LIST_VIEW_ID)
                    .append(SEPARATOR)
                    .append(OTHER_USER_INFO_ACTIVITY_FANS_LIST_ITEM_CANCEL_FOLLOW_VIEW_ID)
                    .append(SEPARATOR).append(CLICK_EVENT_ID).toString();*/

    public static final String RECOMMEND_SKIP_VIEW_ID = "15453";
    public static final String RECOMMEND_FOLLOW_VIEW_ID = "15454";
    public static final String KEY_FOLLOW_COUNT = "Follow_count";

    /**
     * 推荐主播界面
     */
    public static final String RECOMMEND_ACTIVITY_SHOW_EVENT_ID = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(RECOMMEND_ACTIVITY_VIEW_ID)
            .append(SEPARATOR).append(SHOW_EVENT_ID).toString();

    /**
     * 推荐主播界面>跳过
     */
    public static final String RECOMMEND_ACTIVITY_SKIP_EVENT_ID = new StringBuilder(RECOMMEND_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(RECOMMEND_SKIP_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * 推荐主播界面>一键关注
     */
    public static final String RECOMMEND_ACTIVITY_FOLLOW_EVENT_ID = new StringBuilder(RECOMMEND_ACTIVITY_VIEW_ID).append(SEPARATOR)
            .append(RECOMMEND_FOLLOW_VIEW_ID)
            .append(SEPARATOR).append(CLICK_EVENT_ID).toString();

    /**
     * php接口请求失败ID
     */
    public static final String PHP_REQUEST_FAIL_EVENT_ID = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(PHP_REQUEST_FAIL).append(SEPARATOR).append(PHP_REQUEST_FAIL_ID)
            .toString();

    //2.1.0


    public static void init(Context context) {
        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(context, Common.UMANALYTICS_APPKEY
                , ChannelUtil.getChannel(context), MobclickAgent.EScenarioType.E_UM_NORMAL, true);
        MobclickAgent.startWithConfigure(config);
        MobclickAgent.openActivityDurationTrack(true);
    }

    public static void onResume(Context context) {
        MobclickAgent.onResume(context);
        Log.error(TAG, context == null ? " " : context.toString() + " onResume");
    }

    public static void onPause(Context context) {
        MobclickAgent.onPause(context);
        Log.error(TAG, context == null ? " " : context.toString() + " onPause");
    }

    public static void onPageStart(String s) {
        MobclickAgent.onPageStart(s);
        Log.error(TAG, s == null ? " " : s == null ? "" : s + " onPageStart");
    }

    public static void onPageEnd(String s) {
        MobclickAgent.onPageEnd(s);
        Log.error(TAG, s == null ? " " : s == null ? "" : s + " onPageEnd");
    }

    public static void onEvent(Context context, String eventId) {
        onEvent(context, eventId, new HashMap<String, String>());
//        Log.error(TAG, "onEvent id = " + eventId == null ? " " : eventId);
    }

    public static void onEvent(Context context, String eventId, Map<String, String> map) {

        addPublicData(map);

        MobclickAgent.onEvent(context, eventId, map);

        Log.error(TAG, "onEvent id = " + eventId == null ? " " : eventId + " map : " + map);
    }

    public static void addPublicData(Map<String, String> map) {
        if (map != null) {
            String userId = AccountInfoManager.getInstance().getCurrentAccountUserIdStr();
            map.put(Common.USER_ID, TextUtils.isEmpty(userId) ? "" : userId);
        }
    }

    /**
     * @param id  为事件ID
     * @param map 为当前事件的属性和取值
     * @param du  为当前事件的数值为当前事件的数值，
     *            取值范围是-2,147,483,648 到 +2,147,483,647 之间的有符号整数，
     *            即int 32类型，如果数据超出了该范围，会造成数据丢包，影响数据统计的准确性。
     */
    public static void onEventValue(Context context, String id, Map<String, String> map, int du) {
        MobclickAgent.onEventValue(context, id, map, du);

        Log.error(TAG, "onEvent id = " + id == null ? " " : id + " map : " + map + " du : " + du);
    }

    /**
     * 上报我们自己的
     */
    public static void reportOnLive() {

        HashMap<String, String> params = new HashMap<>();
        LiveReportData data = LiveReportData.getData();

        List<LiveReportData> datas = new ArrayList<>();
        datas.add(data);
        Gson gson = new Gson();
        String jsonStr = gson.toJson(datas);

        Log.error("test", "report data json : " + jsonStr);
        params.put(Common.DATA, jsonStr);
        params.put(Common.T, String.valueOf(System.currentTimeMillis() / 1000L));
        String sign = String.valueOf(GsonHttpConnection.generateSign(params));
        if (TextUtils.isEmpty(sign) == false) {
            params.put(Common.V, sign);
        }
        String userId = AccountInfoManager.getInstance().getCurrentAccountUserIdStr();
        params.put(Common.USER_ID, userId == null ? "" : userId);
        GsonHttpConnection.getInstance().post(null, Common.SET_STAT_URL, null, params, Msg.class, null, true);

        //上报之前因为失败而缓存起来的数据
        GsonHttpConnection.getInstance().postCache();
    }

    /**
     * 上报自定义错误
     *
     * @param context
     * @param error
     */
    public static void reportError(Context context, String error) {
        MobclickAgent.reportError(context, error);
        android.util.Log.d(TAG, "report error : " + error);
    }

    public static String getUUID(Context context) {
        if (context == null) {
            return null;
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_DENIED) {
            return "";
        }
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String tmDevice, tmSerial, androidId;
        tmDevice = tm.getDeviceId();
        tmDevice = tmDevice == null ? "" : tmDevice;
        tmSerial = tm.getSimSerialNumber();
        tmSerial = tmSerial == null ? "" : tmSerial;
        androidId = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        androidId = androidId == null ? "" : androidId;

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();
    }
}
