package com.laka.live.util;

import com.laka.live.BuildConfig;

/**
 * Created by luwies on 16/3/4.
 */
public class Common {
    /**
     * 是否屏蔽直播间部分功能
     */
    public static final boolean IS_HIDE_PART_FUNCTION = true;

    /**
     * 是否动态下载礼物资源
     */
    public static final boolean IS_DOWNLOAD_GIFT_RES = true;

    /**
     * 服务器地址
     */
    public static final String SERVER_URL = BuildConfig.SERVER_URL;

    /**
     * 商城部分的测试地址
     */
    public static final String SERVER_SHOP_URL = BuildConfig.SHOPPING_SERVER_URL + "/";


    public static final int ERROR_BASE = -99999999;
    /**
     * 空指针
     */
    public static final int ERROR_NULL_POINTER = ERROR_BASE + 1;

    /**
     *
     */
    public static final int AVSDK_APPID = 1400007109;
    public static final String AVSDK_ACCOUNTTYPE = "3430";

    // APP_ID 替换为你的应用从官方网站申请到的合法appId
    // public static final String WECHAT_SDK_APP_ID = "wxb6f0fbb496e5f9b7";
    public static final String WECHAT_SDK_APP_ID = "wxaa663d9500822ee5";  //贝壳直播
    /**
     * 新浪微博App Key
     */
//    public static final String SINA_WEIBO_APP_KEY = "2439186945";
    public static final String SINA_WEIBO_APP_KEY = "1811891861";  //贝壳直播
    //    public static final String SINA_WEIBO_REDIRECT_URL = "https://api.lakatv.com/login_with_weibo.php";
    public static final String SINA_WEIBO_REDIRECT_URL = "http://112.74.48.33:8888/login_with_weibo.php";
//    public static final String SINA_WEIBO_REDIRECT_URL = SERVER_URL + "login_with_weibo.php";

    public static final String SINA_WEIBO_SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    /**
     * QQ APP Id
     */
//    public static final String QQ_APP_ID = "3379623803";
    public static final String QQ_APP_ID = "1106088776";

    //public static final String QQ_APP_ID = "1105328172";


    public static final int DEMO_ERROR_NULL_POINTER = 0;

    public static final String LOCAL_PHONE = "phonenumber";

    public static final String PLATFORM_ANDROID = "android";

    /**
     * 手机上禁止直播
     */
    public static final String ROOM_MANAGE_OP = SERVER_URL + "room_super_manage_op.php";
    /**
     * 友盟
     */
    public static final String UMANALYTICS_APPKEY = "58fd67328630f55abd00206c";


    public static final int FLV = 1;

    public static final int HLS = 2;

    public static final int RTMP = 5;

    public static final String KEY_MYSELF = "key_myself";

    /**
     * description:课程专题和内容专题分享前缀
     **/
    public static final String TOPIC_SHARE_URL_PREFIX = BuildConfig.TOPIC_SHARE_PREFIX;

    /**
     * description:小视频分享前缀
     **/
    public static final String MINI_VIDEO_SHARE_URL_PREFIX = BuildConfig.MINI_VIDEO_SHARE_PREFIX;

    /**
     * description:资讯H5分享前缀
     **/
    public static final String NEWS_WEB_VIEW_SHARE_URL_PREFIX = BuildConfig.NEW_SHARE_PREFIX;

    /**
     * 手机号
     */
    public static final String PHONE = "phone";

    /**
     * 验证码
     */
    public static final String PVC = "pvc";

    /**
     *
     */
    public static final String CODE = "code";

    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String WECHAT_ID = "wechat_id";

    public static final String WEIBO_ID = "weibo_id";

    public static final String STREAM_ID = "stream_id";

    public static final String QQ_ID = "qq_id";

    public static final String NICK_NAME = "nickname";

    public static final String AVATAR = "avatar";

    public static final String DESCRIPTION = "description";

    public static final String FILE_HASH = "filehash";

    public static final String CHANNEL = "channel";

    public static final String FLAG = "flag";

    public static final String IS_EXIST = "is_exist";

    public static final String GENDER = "gender";

    public static final String GENDER_MAN = "1";

    public static final String GENDER_WOMAN = "0";

    public static final String REGION = "region";

    public static final String KEY = "key";

    public static final String TOKEN = "token";

    public static final String SECRET = "secret";

    public static final String UPLOAD_TOKEN = "upload_token";

    public static final String X_UPLOAD_TOKEN = "x:upload_token";

    public static final String ERROR = "error";

    public static final String ERROR_MSG = "error_msg";

    public static final String CONNECT_CNT = "connect_cnt";

    public static final String IS_CAN_CONNECT = "is_can_connect";

    public static final String IS_OPEN_CONNECT = "is_open_connect";

    public static final String COMMAND = "command";

    public static final String TIME_STAMP = "timestamp";

    public static final String TAG = "tag";

    public static final String APPLY_VERIFIED = "apply_verified";

    public static final String VERIFIED = "verified";

    public static final String STAR_VERIFIED = "star_verified";

    public static final String EXPS = "exps";

    public static final String LEVEL = "level";

    public static final String COINS = "coins";

    public static final String RECV_COINS = "recv_coins";

    public static final String GIVE_COINS = "give_coins";

    public static final String RECV_LIKES = "recv_likes";

    public static final String NEW_COINS = "new_coins";

    public static final String TOTAL_COINS = "total_coins";

    public static final String VIDEO = "video";

    public static final String VIDEOS = "videos";

    public static final String FOLLOWS = "follows";

    public static final String FANS = "fans";

    public static final String FILE = "file";

    public static final String FOLLOW = "follow";

    public static final String FOLLOW_ID = "follow_id";

    public static final String UNFOLLOW_ID = "unfollow_id";

    public static final String LIMIT = "limit";

    public static final String PAGE = "page";

    public static final String CATEGORY_ID = "category_id";

    public static final String ACTION = "action";

    public static final String USERID = "userId";

    public static final String USER_ID = "user_id";

    public static final String CHANNEL_ID = "channel_id";

    public static final String FILE_ID = "file_id";

    public static final String KEYWORD = "keyword";

    public static final String KEYWORDS = "keywords";

    public static final String SORTTYPE = "sortType";

    public static final String USERS = "users";

    public static final String STATE = "state";

    public static final String STATUS = "status";

    public static final String RESULT = "result";

    public static final String CITIES = "cities";

    public static final String HOT_KEYWORDS = "hot_keywords";

    public static final String TAGS = "tags";

    public static final String SUMMARY_IMGS = "summary_imgs";

    public static final String FORMULA_IMGS = "formula_imgs";


    public static final String TITLE = "title";

    public static final String LOCATION = "location";

    public static final String SCREENSHOT = "screenshot";

    public static final String COVER = "cover";


    public static final String ONLINES = "onlines";

    public static final String DURATION = "duration";

    public static final String BEGIN_TIME = "begin_time";

    public static final String DOWN_URL = "down_url";

    public static final String EXTRA_IS_TEST_LIVE = "extra_is_test_live";

    public static final String END_TIME = "end_time";

    public static final String FIELDS = "fields";

    public static final String FIELDS_SEPARATOR = ",";

    public static final String TODAY = "today";

    public static final String MONTH = "month";

    public static final String IS_FIRST = "is_first";

    public static final String VISITOR_TOKEN = "visitor_token";

    public static final String CASH_WITHDRAWAL = "cash_withdrawal";

    public static final String TODAY_CASH_WITHDRAWAL = "today_cash_withdrawal";

    public static final String TOTAL_CASH_WITHDRAWAL = "total_cash_withdrawal";

    public static final String CASH_WITHDRAWAL_TODAY = "cash_withdrawal_today";

    public static final String DAY_CASH_WITHDRAWAL = "day_cash_withdrawal";

    public static final String IS_CASH = "is_cash";

    public static final String DAY_MAX_WITHDRAW_RMB = "day_max_withdraw_rmb";

    public static final String DAY_MAX_WITHDRAW_TIMES = "day_max_withdraw_times";

    public static final String TOTAL_EXCHANGE_COINS = "total_exchange_coins";

    public static final String WEEK_COINS = "week_coins";

    public static final String DAY_CASH_WITHDRAWAL_REMAIN = "day_cash_withdrawal_remain";

    public static final String EXCHANGE_COINS_MAX = "exchange_coins_max";

    public static final String PAY_TYPE = "pay_type";

    public static final String EXTRA_COINS = "extra_coins";

    public static final String PRICE = "price";

    public static final String PRODUCTS = "products";

    public static final String BALANCE = "balance";


    public static final String ANDROID = "android";

    public static final String ALIPAY = "alipay";

    public static final String WEIXIN_PAY = "weixin_pay";

    public static final String ROOMS = "rooms";

    public static final String TYPE = "type";

    public static final String TASK_ID = "task_id";

    public static final String IMAGE = "image";

    public static final String UPSTREAM_ADDRESS = "upstream_address";

    public static final String LIVE_ID = "live_id";

    public static final String LIVING = "living";

    public static final String REPLAY = "replay";

    public static final String SEND_RANKING = "send_ranking";

    public static final String SEND_RANKING_TOP3 = "send_ranking_top3";

    public static final String BLOCK_ID = "block_id";

    public static final String UNBLOCK_ID = "unblock_id";

    public static final String TIME = "time";

    public static final String NUM = "num";

    public static final String NOTE = "note";

    public static final String DONOT = "do_not_disturb_sleep";

    public static final String FOLLOW_ROOM = "remind_follow_open_room";

    public static final String FOLLOW_ME = "remind_follow_me";

    public static final String MESSAGE = "remind_message";

    public static final String VERSION = "version";

    public static final String VERSION_SHOW = "version_show";

    public static final String URL = "url";

    public static final String RATE = "rate";

    public static final String INCOME = "income";

    public static final String INCOME_TYPE = "income_type";

    public static final String W = "w";

    public static final String H = "h";

    public static final String VALUE = "value";

    public static final String IMG = "img";

    public static final String OS_VERSION = "os_version";

    public static final String API_VERSION = "api_version";

    public static final String ACCESS_TOKEN = "access_token";

    public static final String OPENID = "openid";

    public static final String UID = "uid";

    public static final String UIDS = "uids";

    public static final String SLIDES = "slides";

    public static final String APPID = "appid";

    public static final String PARTNERID = "partnerid";

    public static final String PREPAYID = "prepayid";

    public static final String PACKAGE_VALUE = "package";

    public static final String NONCESTR = "noncestr";

    public static final String TIMESTAMP = "timestamp";

    public static final String SIGN = "sign";

    public static final String WITHDRAW_WECHAT_ID = "withdraw_wechat_id";

    public static final String WITHDRAW_PHONE = "withdraw_phone";

    public static final String FROZEN_COINS = "frozen_coins";

    public static final String JPUSH_ID = "jpush_id";

    public static final String DO_NOT_DISTURB_SLEEP = "do_not_disturb_sleep";

    public static final String REMIND_FOLLOW_OPEN_ROOM = "remind_follow_open_room";

    public static final String REMIND_FOLLOW_ME = "remind_follow_me";

    public static final String REMIND_MESSAGE = "remind_message";

    public static final String LIVE_ONLY_WIFI = "live_only_wifi";

    public static final String ROOM_ID = "room_id";

    public static final String ROOM = "room";

    public static final String VID = "vid";

    public static final String SAVE = "save";

    public static final String USER = "user";

    public static final String AUTH = "auth";

    public static final String BLOCK = "block";

    public static final String CASH = "cash";

    public static final String SUCCESS = "success";

    public static final String BANNER_ID = "Banner_id";

    public static final String HOT_ID = "Hot_id";

    public static final String NEW_ID = "New_id";

    public static final String HAS_SET_JPUSH_ID = "HAS_SET_JPUSH_ID";

    public static final String TM = "tm";

    public static final String VER = "ver";

    public static final String GIFTS = "gifts";

    public static final String DATA = "data";

    public static final String GOODS = "goods";

    public static final String GOODS_ID = "goodsId";

    public static final String SALE_PRICE = "salePrice";

    public static final String SALE_COUNT = "saleCount";

    public static final String POSTAGE_TYPE = "postageType";

    public static final String CATE_ID = "cateId";

    public static final String PRIMARY_CATE_ID = "primaryCateId";

    public static final String MARKET_PRICE = "marketPrice";

    public static final String PROMOTEINCOME = "promoteIncome";

    public static final String CHANNEL_INFO = "channel_info";

    // 认证时间
    public static final String CERTIFICATE_TIME = "certificate_time";

    // 认证状态
    public static final String CERTIFICATE_STATUS = "certificate_status";

    public static final String SN = "sn";

    public static final String UUID = "uuid";

    public static final String DT = "dt";

    public static final String DEVI = "devi";

    public static final String FW = "fw";

    public static final String RESOLUTION = "resolution";

    public static final String PROV = "prov";

    public static final String NW = "nw";

    public static final String NT = "nt";

    public static final String ISP = "isp";

    public static final String T = "t";

    public static final String V = "v";

    public static final String CHARID = "charid";

    public static final String CHAR_NAME = "char_name";

    public static final String TOPICS = "topics";

    public static final String POSITION = "position";

    public static final String VIDEO_LINES = "video_lines";

    public static final String SPEEDS = "speeds";

    public static final String AGENT_PROFITRATIO = "agent_profitratio";

    public static final String NUM_IMAGE = "num_image";

    public static final String IS_FULL = "is_full";

    public static final String IS_SOUND = "is_sound";

    public static final String EDITTIME_REPEAT_GIVE = "edittime_repeat_give";

    public static final String REPEAT_GIVE_INFO = "repeat_give_info";

    public static final String REPEAT_GIVE = "repeat_give";

    public static final String A = "a";

    public static final String B = "b";

    public static final String C = "c";
    //用户身份标识
    public static final String USER_TAG = "room_admin";

    //用户身份标识
    public static final String FORBIDDEN_STATE = "forbid_say";

    public static final String TOP_LIST_TYPE = "toplist_type";

    public static final String LIVE_COUNT = "live_count";

    public static final String IS_LIVE = "is_live";

    public static final String BOOKING_COURSE_COUNT = "booking_course_count";

    public static final String RELEASE_COURSE_COUNT = "release_course_count";

    /**
     * 开启直播
     */
    public static final String OPEN_LIVE_URL = SERVER_URL + "open_live.php";
    /**
     * 开启直播测试
     */
    public static final String OPEN_TEST_LIVE_URL = SERVER_URL + "sdk/opentestlive";

    /**
     * 开启录像
     */
    public static final String START_RECORD_URL = SERVER_URL + "start_record.php";

    /**
     * 房间信息（单个）
     */
    public static final String QUERY_ROOM_URL = SERVER_URL + "query_room.php";

    /**
     * 获取手机验证码接口
     */
    public static final String GET_PHONE_VERIFY_CODE_URL = SERVER_URL + "get_pvc.php";

    /**
     * 手机验证码等领域接口
     */
    public static final String LOGIN_WITH_PHONE_URL = SERVER_URL + "login_with_phone.php";

    public static final String RECOMMEND_LIST_URL = SERVER_URL + "get_recommend_follow.php";
    /**
     * 微信接口
     */
    public static final String LOGIN_WITH_WECHAT_URL = SERVER_URL + "login_with_wechat.php";

    /**
     * 新浪微博登录
     */
    public static final String LOGIN_WITH_SINA_WEIBO_URL = SERVER_URL + "login_with_weibo.php";

    /**
     * QQ登录
     */
    public static final String LOGIN_WITH_QQ_URL = SERVER_URL + "login_with_qq.php";

    /**
     * 登出接口
     */
    public static final String LOGOUT_URL = SERVER_URL + "logout.php";


    /**
     * 动态礼物
     */
    public static final String QUERY_GIFTS_URL = SERVER_URL + "query_gifts.php";

    /**
     * 申请连麦
     */
    public static final String JOIN_LIVE_URL = SERVER_URL + "join_live.php";

    /**
     * 获取用户信息
     */
    public static final String GET_USERINFO_URL = SERVER_URL + "get_user_info.php";
    /**
     * 获取腾讯云视频sig
     */
    public static final String GET_QCLOUD_SIG_URL = SERVER_URL + "get_qcloud_sig.php";
    /**
     * 查询观看人数，获得金币
     */
    public static final String QUERY_ROOM_RECV_URL = SERVER_URL + "query_room_recv.php";
    /**
     * 设置腾讯云视频
     */
    public static final String SET_QCLOUD_URL = SERVER_URL + "set_qcloud.php";
    /**
     * 设置旁路直播channel_id
     */
    public static final String SET_CHANNEL_ID = SERVER_URL + "set_channel_id.php";

    /**
     * 设置直播相关信息，现在有save
     */
    public static final String SET_CHANNEL = SERVER_URL + "set_channel.php";
    /**
     * 获取订单字串信息
     */
    public static final String GET_ORDER_INFO_URL = SERVER_URL + "get_order_info.php";
    /**
     * 获取随机标签
     */
    public static final String QUERY_ROOM_TAG_URL = SERVER_URL + "query_room_tag.php";
    /**
     * 编辑用户信息
     */
    public static final String EDIT_USERINFO_URL = SERVER_URL + "set_user_info.php";

    public static final String GET_QINIU_UPLOAD_TOKEN_URL = SERVER_URL + "get_qiniu_upload_token.php";

    public static final String QUERY_FAN_URL = SERVER_URL + "query_fan.php";

    public static final String QUERY_FOLLOW_URL = SERVER_URL + "query_follow.php";

    // 获取已购买用户
    public static final String QUERY_SUBSCRIBE_URL = SERVER_URL + "course/course.php?action=courseUser";

    // 搜索课程
    public static final String SEARCH_URL = SERVER_URL + "search/course";

    // 搜索商品
    public static final String SEARCH_GOODS_URL = SERVER_SHOP_URL + "goods/search";

    public static final String SEARCH_HOT_KEYWORD_URL = SERVER_URL + "get_hot_keyword.php";

    public static final String QUERY_TAG_URL = SERVER_URL + "query_tag.php";

    public static final String QUERY_VIDEO_URL = SERVER_URL + "query_video.php";

    public static final String QUERY_EARNINGS_URL = SERVER_URL + "user.php?action=earnings";

    public static final String QUERY_PRODUCT_URL = SERVER_URL + "query_product.php";

    public static final String QUERY_ROOM_HOT_URL = SERVER_URL + "query_hot_room2.php";

    //获取首页正在直播
    public static final String QUERY_HOME_LIVE_URL = SERVER_URL + "course/livings";

    //获取首页功能
    public static final String QUERY_HOME_FUNCTION_URL = SERVER_URL + "home/portal";

    //获取热门话题
    public static final String QUERY_HOT_TOPICS = SERVER_URL + "hot_topics.php";

    // 查询资讯列表
    public static final String QUERY_NEWS_LIST = SERVER_URL + "news/list.php";

    // 获取直播列表
    public static final String GET_COURSE_LIVE = SERVER_URL + "course/live";

    // 获取视频列表
    public static final String GET_COURSE_VIDEO = SERVER_URL + "course/video";

    // 获取分类内容列表
    public static final String QUERY_COURSE_CATEGORY = SERVER_URL + "course/categoryContent";

    public static final String COURSE_RECOMMEND = SERVER_URL + "course/recommend";

    public static final String QUERY_HOME_BANNERS_URL = SERVER_URL + "home/banner";

    // 获取首页话题
    public static final String QUERY_HOME_TOPICS_URL = SERVER_URL + "home.php?action=topics";

    public static final String QUERY_MY_LIVE = SERVER_URL + "course/course.php?action=getList";

    public static final String QUERY_FOLLOW_VIDEO_URL = SERVER_URL + "query_follow_room2.php";

    public static final String QUERY_ROOM_LATEST_URL = SERVER_URL + "query_new_room.php";

    public static final String QUERY_SEND_RANKING_URL = SERVER_URL + "query_send_ranking.php";

    public static final String QUERY_RANKING_LIST_URL = SERVER_URL + "get_toplist.php";

    public static final String SET_USER_REMIND_URL = SERVER_URL + "set_user_remind.php";

    public static final String FOLLOW_URL = SERVER_URL + "follow.php";

    public static final String FOLLOW_BATCH_URL = SERVER_URL + "follow_batch.php";

    //获取管理员列表
    public static final String QUERY_ROOM_MANAGER_LIST_URL = SERVER_URL + "query_room_admin.php";

    public static final String QUERY_TOPIC_LIST_URL = SERVER_URL + "get_topic_list.php";

    //直播话题推荐
    public static final String QUERY_HOT_TOPIC_URL = SERVER_URL + "query_hot_topic.php";
    /**
     * 拉黑
     */
    public static final String BLOCK_URL = SERVER_URL + "block.php";

    public static final String REPORT_URL = SERVER_URL + "report.php";

    public static final String QUERY_TRANSACTION_LOG_URL = SERVER_URL + "query_transaction_log.php";

    public static final String COURSE_INCOMESUM_URL = SERVER_URL + "course/incomesum";

    public static final String COURSE_INCOMES_URL = SERVER_URL + "course/incomes";

    public static final String COURSE_INCOME_DETAILS = SERVER_URL + "course/incomeDetails";

    public static final String COURSE_INCOME_DETAIL_INFO = SERVER_URL + "course/incomeDetailInfo";

    public static final String QUERY_UPDATE_URL = SERVER_URL + "query_update.php";

    public static final String FEEDBACK_URL = SERVER_URL + "feedback.php";

    public static final String BIND_WECHAT_URL = SERVER_URL + "bind_wechat.php";

    public static final String BIND_PHONE_URL = SERVER_URL + "bind_phone.php";

    public static final String QUERY_WECHAT_PAY_URL = SERVER_URL + "query_wechat_pay.php";

    public static final String SET_JPUSH_ID_URL = SERVER_URL + "set_jpush_id.php";

    public static final String EXCHANGE_URL = SERVER_URL + "exchange.php";

    //获取用户一个最新的课程
    public static final String NEWEST_COURSE_URL = SERVER_URL + "user/newest_course.php";

    //关注动态
    public static final String FOLLOW_NEWS_URL = SERVER_URL + "follow_news.php";

    //获取课程评论列表
    public static final String COURSE_COMMENTS_URL = SERVER_URL + "course/comments";

    //获取课程回复列表
    public static final String COURSE_REPLIES_URL = SERVER_URL + "course/replies";

    //发布课程评论
    public static final String COURSE_COMMENT_URL = SERVER_URL + "course/comment";

    //点赞评论
    public static final String COURSE_PRAISE_URL = SERVER_URL + "course/praiseComment";

    // 获取内容页的一级分类
    public static final String COURSE_CATEGORY_ONE_URL = SERVER_URL + "course/categoryOne";

    // 获取内容页的二级分类
    public static final String COURSE_CATEGORY_TWO_URL = SERVER_URL + "course/categoryTwo";

    // 获取内容页的一级分类
    public static final String COURSE_CONTENT_COLUMNS_URL = SERVER_URL + "content/columns";

    // 获取优选专题详情
    public static final String COURSE_COURSE_TOPICS_DETAIL_URL = SERVER_URL + "course/topicdetail";


    /**
     * 我的等级
     */
    public static final String MY_LEVEL_URL = "http://www.lakatv.com/ziwei/ziweiAPP/mylv.html?uid=";

    /**
     * 我的收益常见问题
     */
    public static final String NQUESTION_URL = "http://www.lakatv.com/nquestion.html";

    public static final String CASH_WITHDRAWAL_URL = SERVER_URL + "cash_withdrawal.php";

    /**
     * 用户资料上传
     */
//    public static final String APPROVE_URL = BuildConfig.DEBUG ?
//            "http://www.lakatv.com/ziwei/ziweiAPP/approve_cs.html?deviceType=android&uid="
//            : "http://www.lakatv.com/ziwei/ziweiAPP/approve.html?deviceType=android&uid=";


    public static final String APPROVE_URL = "http://www.lakatv.com/ziwei/ziweiAPP/authentication" + (BuildConfig.DEBUG ? "_cs" : "") + ".html?uid=";

    /**
     * 服务条款
     */
    public static final String USER_PROTOCOL_URL = "http://www.lakatv.com/ziwei/ziweiAPP/service.html";

    /**
     * 用户协议
     */
    public static final String USER_PRO_URL = "http://www.lakatv.com/ziwei/ziweiAPP/useragre.html";

    /**
     * 关于我们-帮助中心
     */
    public static final String ABOUT_URL = "http://www.lakatv.com/contact_mobile.html";//"http://www.lakatv.com/aboutus.html";
    /**
     * 支付帮助
     */
    public static final String PAY_HELP = "http://www.lakatv.com/ziwei/ziweiAPP/useragre.html";
    /**
     * 官网关于我们页面
     */
    public static final String CONTACT_MOBILE = "http://www.lakatv.com/contact_mobile.html";


    /**
     * 常见问题页面
     */
    public static final String HELP_URL = "http://www.lakatv.com/ziwei/ziweiAPP/help.html";

    public static final String QIN_NIU_UPLOAD_URL = "http://upload.qiniu.com/";

    public static final String REMOVE_VIDEO_URL = SERVER_URL + "remove_video.php";

    public static final String QUERY_CHAR_FIELD_ALL_URL = SERVER_URL + "query_char_field_all.php";

    public static final String QUERY_CHAR_FIELD_URL = SERVER_URL + "query_char_field.php";

    public static final String SET_STAT_URL = SERVER_URL + "set_stat.php";

    public static final String EXTRA_TITLE = "extra_title";

    public static final String EXTRA_COVER = "extra_cover";

    public static final String EXTRA_COURSE_ID = "extra_course_id";
    public static final String EXTRA_TOPIC = "extra_topic";

    public static final String EXTRA_IS_CREATER = "extra_is_creater";

    public static final String EXTRA_ROOM_ID = "extra_room_id";

    public static final String EXTRA_ROOM_UPDATE_EVENT_ID = "extra_room_update_event_id";

    public static final String EXTRA_ROOM_UPDATE_IDENTIFYS = "extra_room_update_identifys";

    /**
     * 观众（主播）同一个角色
     */
    public static final String AUDIENCE = "Audience";

    /**
     * 主播
     */
    public static final String ANCHOR = "Anchor";

    /**
     * 测试角色
     */
    public static final String TEST = "Test";

    /**
     * 无
     */
    public static final int QAV_EVENT_ID_NONE = 0;
    /**
     * 进入房间事件
     */
    public static final int QAV_EVENT_ID_ENDPOINT_ENTER = 1;
    /**
     * 退出房间事件
     */
    public static final int QAV_EVENT_ID_ENDPOINT_EXIT = 2;
    /**
     * 有发摄像头视频事件
     */
    public static final int QAV_EVENT_ID_ENDPOINT_HAS_CAMERA_VIDEO = 3;
    /**
     * 无发摄像头视频事件
     */
    public static final int QAV_EVENT_ID_ENDPOINT_NO_CAMERA_VIDEO = 4;
    /**
     * 有发语音事件
     */
    public static final int QAV_EVENT_ID_ENDPOINT_HAS_AUDIO = 5;
    /**
     * 无发语音事件
     */
    public static final int QAV_EVENT_ID_ENDPOINT_NO_AUDIO = 6;
    /**
     * 有发屏幕视频事件
     */
    public static final int QAV_EVENT_ID_ENDPOINT_HAS_SCREEN_VIDEO = 7;
    /**
     * 无发屏幕视频事件
     */
    public static final int QAV_EVENT_ID_ENDPOINT_NO_SCREEN_VIDEO = 8;


    public static final String MY_ID = "18620143250";

    public static final String U_ID = "13652721971";

    public static final String ORDER_ID = "orderId";
    public static final String PRODUCT_ID = "product_id";

    public static final String ARGUMENT_USER_ID = "argument_user_id";
    public static final String CUSTOMER_SER = "customer_ser";
    public static final String PAY_WEIXIN = "pay_weixin";
    public static final String PAY_APPLE = "pay_apple";
    public static final String PAY_ALIPAY = "pay_alipay";
    public static final String WITHDRAWAL_WEIXIN = "withdrawal_weixin";
    public static final String WITHDRAWAL_LAKA = "withdrawal_laka";
    public static final String FACE_BEAUTY = "face_beauty";
    public static final String SPLASH_SCREEN = "splash_screen";
    public static final String SHARE = "share";
    public static final String UPGRADE = "upgrade";
    //    public static final String GET_SYSTEM_CONFIG_URL = SERVER_URL + "get_system_config.php";
    public static final String GET_SYSTEM_CONFIG_URL = SERVER_URL + "sys/config";
    public static final String CONFIG_VERSION = "config_version";
    public static final String GET_TOPIC_ROOM_LIST = SERVER_URL + "course/course.php?action=topicCourses";

    public static final String TOPIC_ID = "topic_id";
    public static final String TOPICS_LIVE = "topics_live";
    // 首页->发现
    //public static final String QUERY_FIND = SERVER_URL + "query_find2.php";
    public static final String QUERY_FIND = SERVER_URL + "home.php?action=find";
    public static final String TOPIC = "topic";

    public static final String Topic = "Topic";
    public static final String IS_UPLOAD_LOCATION = "IS_UPLOAD_LOCATION";
    public static final String LOGIN_WEIXIN = "login_weixin";
    public static final String LOGIN_QQ = "login_qq";
    public static final String LOGIN_WEIBO = "login_weibo";
    public static final String LOGIN_MOBILE = "login_mobile";
    public static final String SINGER = "singer";
    public static final String COIN_DISPLAY_RATE = "coin_display_rate";
    public static final String COIN_DISPLAY_DECIMAL = "coin_display_decimal";

    public static final String SEARCH_SONGS_URL = SERVER_URL + "search_songs.php";
    public static final String GET_HOT_SONGS_URL = SERVER_URL + "get_hot_songs.php";

    // 发布课程预告
    public static final String RELEASE_TRAILER = SERVER_URL + "course/release_trailer.php";
    // 发布资讯课堂
    public static final String RELEASE_NEWS = SERVER_URL + "news/release.php";
    // 更新课堂预告
    public static final String UPDATE_TRAILER = SERVER_URL + "course/update_trailer.php";
    // 删除课程
    public static final String DELETE_TRAILER = SERVER_URL + "course/course.php?action=cancel";
    // 删除资讯
    public static final String DELETE_NEWS = SERVER_URL + "news/remove.php";
    // 获取课程详情
    public static final String COURSE_DETAIL = SERVER_URL + "course/course_detail.php";
    // 获取支付清单
    public static final String PAY_DETAIL = SERVER_URL + "course/course.php?action=payList";
    // 购买课程
    public static final String PAY_COURSE = SERVER_URL + "/course/course.php?action=buy";
    // 获取个人主页的课程(已购买或已发布列表)
    public static final String COURSE_LIST = SERVER_URL + "course/course.php";
    // 获取我的资讯列表
    public static final String MY_NEWS = SERVER_URL + "news/released.php";
    // 获取预告视频
    public static final String TRAILER_DETAIL = SERVER_URL + "course/trailer_detail.php";
    // 获取认证信息
    public static final String USER_APPROVE = SERVER_URL + "user.php?action=certificateInfo";
    // 获取关注Tab课程
    public static final String GET_FOLLOWS = SERVER_URL + "course/course.php?action=followTab";
    // 获取关注Tab对应的课程列表
    public static final String GET_FOLLOWS_COURSES = SERVER_URL + "course/course.php?action=followCourses";
    // 获取发布的注意事项
    public static final String GET_NOTICE_INFO = SERVER_URL + "course/course.php?action=attention";
    // 请求服务器获取oss上传临时token，app使用token上传视频等文件到阿里云 oss 服务器上
    public static final String GET_STS_TOKEN = SERVER_URL + "getStsToken.php";
    // 获取配方做法
    public static final String GET_FORMULA = SERVER_URL + "course/course_blackboard.php";
    // 观看回放统计
    public static final String WATCH_COUNT = SERVER_URL + "stat/watch_playback.php";
    // 分享统计
    public static final String SHARE_COUNT = SERVER_URL + "stat/share.php";

    //2.4.0
    //闪屏
    public static final String SPLASH_URL = SERVER_URL + "sys/splash";

    //一元课程
    public static final String CHEAP_COURSE_URL = SERVER_URL + "course/cheap";

    //优质课程
    public static final String BEST_COURSE_URL = SERVER_URL + "course/best";

    //免费课程
    public static final String FREE_COURSE_URL = SERVER_URL + "course/free";

    //限时优惠课程
    public static final String LIMIT_COURSE_URL = SERVER_URL + "course/timelimitOffer";

    //最新课程
    public static final String NEW_COURSE_URL = SERVER_URL + "course/newest";

    //热门视频
    public static final String HOTTEST_COURSE_URL = SERVER_URL + "course/hottest";

    public static final String IS_USER_SIGNS = "is_user_signs";
    public static final String IS_FROM_SCROLL = "is_from_scroll";
    public static final String BUY_FLAG = "buy_flag";
    public static final String BUYER_COUNT = "buyer_count";
    public static final String SADMIN = "sadmin";
    public static final String CAMERA_DEVICE_POSITION = "kCameraDevicePosition";


    public static int playPosition;

    public static final String SHARE_IMAGE_URL_DEFAULT = "http://img3.imgtn.bdimg.com/it/u=1322706771,463753758&fm=15&gp=0.jpg";

    public static final String SHARE_PAGE_URL_DEFAULT = "http://www.lakatv.com/";

    //分享直播结束
    public static final String SHARE_LIVE_END = "http://lakatv.com/laka_share.html?zbname=%s&bannerUrl=%s&lkzuan=%d&gkrs=%s&fs=%d";

    //分享课程
//    public static final String SHARE_COURSE_URL = BuildConfig.DEBUG ? "http://www.lakatv.com/ziwei/wx_share/share_mobile_cake_cs.html?deviceType=android&course_id=" : "http://www.lakatv.com/ziwei/wx_share/share_mobile_cake.html?deviceType=android&course_id=";
    public static final String SHARE_COURSE_URL = "http://www.lakatv.com/ziwei/wx_share" + (BuildConfig.DEBUG ? "_cs" : "") + "/share_mobile_cake.html?course_id=";

    //分享个人主页
    public static final String SHARE_USER_URL = "http://www.lakatv.com/ziwei/wx_share" + (BuildConfig.DEBUG ? "_cs" : "") + "/share_mobile_percenter.html?uid=";

    //分享商品
    public static final String SHARE_GOODS_URL = "http://www.lakatv.com/ziwei/wx_share" + (BuildConfig.DEBUG ? "_cs" : "") + "/share_mobile_goodsDetail.html?goodsId=";

    //分享直播房间
//    public static final String SHARE_ROOM_URL = "http://www.lakatv.com/share_mobile.html?uid=";
    //public static final String SHARE_ROOM_URL = "http://www.lakatv.com/ziwei/wx_share/share_mobile_cake.html?trailer_id=";
    //分享回放
    public static final String SHARE_REPLAY_URL = "http://www.lakatv.com/share_mobile.html?uid=%s&rollbackid=%s";
    //分享直播默认标题
//    public static final String SHARE_ROOM_DEFAULT_TITLE = ResourceHelper.getString(R.string.share_room_default_title);
    //分享直播默认内容
//    public static final String SHARE_ROOM_DEFAULT_CONTENT = ResourceHelper.getString(R.string.share_room_default_content);

    //当前滤镜id
    public static final String KEY_FILTER_ID = "key_filter_id";
    //是否前置摄像头
    public static final String KEY_IS_FRONT_CAMERA = "key_is_front_camera";
    //是否开启美颜
    public static final String KEY_IS_ENABLE_BEAUTY = "key_is_enable_beauty";
    //我的定位城市
    public static final String KEY_MY_LOCATION_CITY = "key_my_location_city";
    //当前音效
    public static final String KEY_AUDIO_EFFECT = "key_cur_audio_effect";
    //人声
    public static final String KEY_AUDIO_VOICE_VOLUME = "key_audio_voice_volume";
    //伴奏
    public static final String KEY_AUDIO_BGM_VOLUME = "key_audio_bgm_volume";
    //本地礼物版本号
    public static final String KEY_LOCAL_GIFT_VERSION = "key_local_gift_version";

    public static final String KEY_IS_PLAY_LOGIN_VIDEO = "key_is_play_login_video";

    public static final String KEY_IS_SHOW_LINES_TIPS = "key_is_show_lines_tips";

    //动态图片列表
    public static final String KEY_DYNC_GIFT_LIST = "key_dync_gift_list";
    public static final String KEY_NEW_GIFT_LIST = "key_new_gift_list";
    //上传图片类型
    public static final String UPLOAD_TYPE_AVATAR = "";
    public static final String UPLOAD_TYPE_COVER = "cover";
    public static final String Laka_id = "Laka_id";
    public static final String Live_id = "Live_id";
    public static final String Live_type = "Live_type";
    public static final String Result_type = "Result_type";
    public static final String ERROE_CODE = "ERROE_CODE";

    public static final String FROM = "FROM";

    public static final String FROM_HOT = "FROM_HOT";

    public static final String FROM_FIND = "FROM_FIND";

    public static final String FROM_BANNER = "FROM_BANNER";

    public static final String FROM_USER_INFO = "FROM_USER_INFO";

    public static final String FROM_FINISH = "FROM_FINISH";

    public static final String FROM_MAIN = "FROM_MAIN";

    public static final String FROM_TOPIC = "FROM_TOPIC";

    public static final String FROM_NEWST = "FROM_NEWST";

    public static final String FROM_PUSH = "FROM_PUSH";

    public static final String FROM_FOLLOW_LIVE = "FROM_FOLLOW_LIVE";
    public static final String FROM_SEARCH = "FROM_SEARCH";

    public static final String NO = "NO";

    public static final String WIDTH = "width";

    public static final String HEIGHT = "height";

    public static final String DESCRIBE = "describe";

    public static final String VIDEO_URL = "video_url";

    public static final String VIDEO_HLS_URL = "video_hls_url";

    public static final String PLATFORM = "platform";

    public static final String IF_FAVOR = "if_favor";

    // 两个不同写法的字段
    public static final String THUMB_URL = "thumb_url";
    public static final String THUMBURL = "thumbUrl";

    public static final String EDIT_TIME = "edittime";

    public static final String VIDEO_DURATION = "video_duration";

    public static final String VIEWS = "views";

    public static final String DOWNS = "downs";

    public static final String HOTS = "hots";

    public static final String PUBLISH_TIME = "publish_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String FAVOR_TIME = "favor_time";

    public static final String FAVOR_NUM = "favor_num";

    public static final String WECHAT_SHARES = "wechatshares";

    public static final String COMMENTS = "comments";

    public static final String COMMENT = "comment";

    public static final String COMMENT_ID = "comment_id";

    public static final String AUTHOR_ID = "author_id";

    public static final String AUTHOR = "author";

    public static final String AUTHOR_AVATAR = "author_avatar";

    public static final String AUTHOR_VNUM = "author_vnum";

    public static final String FILE_SIZE = "filesize";

    public static final String COMMENT_ITEMS = "comment_items";

    public static final String REVIEW_REMARKS = "review_remarks";

    public static final String IS_FOLLOWED = "is_followed";

    public static final String ARCHIVE_ID = "archive_id";

    public static final String CONTENT = "content";

    public static final String CREATE_TIME = "create_time";

    public static final String PRAISE_COUNT = "praise_count";

    public static final String REPLY = "reply";

    public static final String REPLY_COUNT = "reply_count";

    public static final String REPLIES = "replies";

    public static final String PARENT_ID = "parent_id";

    public static final String REPLY_ID = "reply_id";

    public static final String TO_USERID = "to_userid";

    public static final String TO_NICKNAME = "to_nickname";

    public static final String ICON = "icon";

    public static final String BUYERS = "buyers";
    public static final String COURSE = "course";
    public static final String COURSES = "courses";
    public static final String COURSES_DETAIL = "course_detail";
    public static final String SUMMARY = "summary";
    public static final String FORMULA = "formula";
    public static final String STATUS_TEXT = "status_text";
    public static final String START_TIME = "start_time";
    public static final String DISCOUNT_TIME = "discount_time";
    public static final String COURSE_TRAILER = "course_trailer";
    public static final String SIMILAR_COURSES = "similar_courses";
    public static final String COVER_URL = "cover_url";
    public static final String SNAPSHOT_URL = "snapshot_url";
    public static final String DISCOUNT_DEADLINE = "discount_deadline";
    public static final String DISCOUNT_REMAINING_TIME = "discount_remaining_time";
    public static final String SIGNATURE = "signature";
    public static final String TRAILER_ID = "trailer_id";
    public static final String COURSE_ID = "course_id";
    public static final String DISCOUNT = "discount";
    public static final String SAVED_COINS = "saved_coins";
    public static final String EDITABLE_FLAG = "editable_flag";
    public static final String START = "start";

    public static final String LIST_TYPE = "list_type";
    public static final String LIST_SUBTYPE = "list_subtype";

    public static final String TOTAL_FEE = "total_fee";
    public static final String ENOUGH_COINS = "enough_coins";
    public static final String ACTUAL_TOTAL_FEE = "actual_total_fee";
    public static final String PURCHASE_STATEMENT = "purchase_statement";
    public static final String COURSE_COUNT = "course_count";
    public static final String BUY_COURSE_COUNT = "buy_course_count";
    public static final String NOTOWNCOURSECOUNT = "not_own_course_count";
    public static final String NEWS_ID = "news_id";

    //FollowNews
    public static final String NEWS_URL = "news_url";
    public static final String NEWS = "news";
    public static final String TARGET_ID = "target_id";
    public static final String ACTUAL_PRICE = "actual_price";
    public static final String COURSE_TAILER = "course_tailer";


    //withdrawal
    public static final String REST_COINS = "rest_coins";
    public static final String TRANS_COINS = "trans_coins";

    //userinfo
    public static final String ACTION_COURSE_DETAIL = "action_course_detail";

    //2.2.0
    //商城


    public final static int FROM_PRIVATE_MSG = 1;
    //    public final static String KEFU_USER_ID = "72859";
    public final static String KEFU_USER_NICKNAME = "客服";


    public final static int TIME_LIMIT_TYPE_NO = 1, TIME_LIMIT_TYPE_CUT = 2, TIME_LIMIT_TYPE_FREE = 3;

//    static int i = 0;
//    static String[] contents = new String[]{"美味生活，就在滋味LIVE", "来滋味Live，遇见最美的生活方式", "来滋味Live，与美味生活不期而遇", "生活妙不可言，就在滋味Live"};
//    public static String getShareContent() {
//        i++;
//        if (i > 3) {
//            i = 0;
//        }
//        return contents[i];
//    }

    /**
     * description:素材库
     **/
    public static final String MATERIAL_VIDEO_ID = "mini_video_id";

    /**
     * description:课程专题分享关键字
     **/
    public static final String COURSE_TOPIC_ID = "course_topic_id";

    /**
     * description:商品专题分享关键字
     **/
    public static final String GOODS_TOPIC_ID = "goods_topic_id";

    /**
     * description:小视频分享关键字
     **/
    public static final String MINI_VIDEO_ID = "mini_video_id";

    /**
     * description:小视频实体类传递
     **/
    public static final String MINI_VIDEO = "MINI_VIDEO";

}
