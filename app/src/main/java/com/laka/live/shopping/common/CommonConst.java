package com.laka.live.shopping.common;

/**
 * Created by linhz on 2015/11/29.
 * Email: linhaizhong@ta2she.com
 */
public class CommonConst {

    public static final String API_VERSION = "1077";
    //SINA app id
    public static final String APPID_SINA = "1402622580";
    public static final String APPSECRECT_SINA = "e7a27f455036506019b8848fc8cf7d94";
    public static final String REDIRECT_URL_SINA = "http://sns.whalecloud.com";
    // qq appid
    public static final String APPID_QQ = "1104666174";
    public static final String APPSECRET_QQ = "v54Cxvn5vJ9VQ9Gk";
    // wechat appid
    public static final String APPSECRET_WECHAT = "2172dd9a89beb52bd22d3c0bbd54e3df";

    public static String APPID_WECHAT = "wx683c2dc7c163644a";
    public static String SPBILL_CREATE_IP_WECHAT = "127.0.0.1";

    public static double TOTALPRICE = 0;
    public static String ORDERNUM = "";
    public static String ORDERID = "";
    public static String CREATETIME = "";

    public static final int SEX_MAN = 1;
    public static final int SEX_WOMAN = 2;
    public static final int DEFAULT_SEX = SEX_MAN;

    public static final int UPDATE_SEX_COMPLETE = 1;
    public static final int UPDATE_SEX_NO_COMPLETE = 0;

    public static final int MARRIAGE_STATE_SINGLE = 1;
    public static final int MARRIAGE_STATE_INLOVE = 2;
    public static final int MARRIAGE_STATE_MARRIAGED = 4;
    public static final int DEFAULT_MARRIAGE_STATE = MARRIAGE_STATE_SINGLE;
    public static final int FIRST_NIGHT_EXSIT = 1;
    public static final int FIRST_NIGHT_NOT_EXSIT = 2;

    public static final int SEX_SKILL_NONE = 1;
    public static final int SEX_SKILL_LITTLE = 2;
    public static final int SEX_SKILL_NORMAL = 4;
    public static final int SEX_SKILL_EXCELLENT = 8;
    public static final int DEFAULT_SEX_SKILL = SEX_SKILL_NORMAL;

    public static final int START_PAGE_INDEX = 1;
    public static final String START_END_ID = "0";

    public static final int COLLECT_TYPE_POST = 1;
    public static final int COLLECT_TYPE_ARTICLE = 2;
    public static final int COLLECT_TYPE_PRODUCT = 3;

    public static final int COLLECT_ADD = 1;
    public static final int COLLECT_CANCEL = 0;

    public static final int PRAISE_TYPE_POST = 1;//帖子
    public static final int PRAISE_TYPE_ARTICLE = 2;//文章
    public static final int PRAISE_TYPE_POST_COMMENTS = 3;//帖子评论
    public static final int PRAISE_TYPE_ARTICLE_COMMENTS = 4;//文章评论
    public static final int PRAISE_ADD = 1;//添加点赞
    public static final int PRAISE_CANCEL = 0;//取消点赞

    //評論
    public static final int COMMENT_ONLY_HOST = 1;//只看楼主
    public static final int COMMENT_ALL = 0;//看全部评论

    //1.图片 已经不再使用
    public static final int BANNER_TYPE_IMAGE = 1;
    //2.IOS应用推荐 已经不再使用
    public static final int BANNER_TYPE_APP_ISO = 2;
    //3.产品分类 已经不再使用
    public static final int BANNER_TYPE_PRODUCT_ZHUTI = 3;
    //4.社区热帖
    public static final int BANNER_TYPE_POST = 4;
    //5.商品详情
    public static final int BANNER_TYPE_PRODUCT_DETAIL = 5;
    //6.网页
    public static final int BANNER_TYPE_WEBVIEW = 6;
    //7，android应用推荐
    public static final int BANNER_TYPE_APP_ANDROID = 7;
    //8.评测文章 已经不再使用
    public static final int BANNER_TYPE_TEST = 8;
    //9.广告分类(商品列表，通过调用3.6的广告分类列表获取，列表显示形式和专题栏目点击进的列表一致)
    // 已经不再使用
    public static final int BANNER_TYPE_PRODUCT_ZHUTI_AD = 9;
    //10.女神导购文章 已经不再使用
    public static final int BANNER_TYPE_NVSHEN_SHOPPING = 10;
    //11.信息流：普通文章
    public static final int BANNER_TYPE_FEED_ARTICLE = 11;
    //12.信息流：导购
    public static final int BANNER_TYPE_FEED_PRODUCT = 12;
    //13.信息流：测试
    public static final int BANNER_TYPE_FEED_TEST = 13;
    //14.信息流：涨姿势
    public static final int BANNER_TYPE_FEED_KNOWLEDGE = 14;
    //15 文章----图片流
    public static final int BANNER_TYPE_FEED_PIC = 15;
    //16.商品商品专题
    public static final int BANNER_TYPE_PRODUCT_TOPIC = 16;
    //17.普通圈子
    public static final int BANNER_TYPE_GENERAL_POST = 17;
    //18.问答圈子
    public static final int BANNER_TYPE_QA_POST = 18;
    //19.问答圈子
    public static final int BANNER_TYPE_EXTERNAL_URL = 19;
    //直播详情页
    public static final int BANNER_TYPE_LIVE = 20;
    //免费试用列表页
    public static final int BANNER_TYPE_TRIAL_LIST = 21;
    //试用商品详情页
    public static final int BANNER_TYPE_TRIAL_DETAIL = 22;
    //发布测评页
    public static final int BANNER_TYPE_EVALUATE_POST = 23;
    //测评详情页
    public static final int BANNER_TYPE_EVALUATE_POST_DETAIL = 24;
    //TA币规则介绍页
    public static final int BANNER_TYPE_TA_ICON_INSTRUCTION = 25;
    //积分规则介绍页
    public static final int BANNER_TYPE_POINTS_INSTRUCTION = 26;
    //27：文章----商品专题文章
    public static final int BANNER_TYPE_ARTICLE_PRODUCT_TOPIC = 27;
    //榜单列表页
    public static final int BANNER_TYPE_RANKING_LIST_PAGE = 28;
    //榜单详情页
    public static final int BANNER_TYPE_RANKING_DETAIL_PAGE = 29;
    //登陆跳转
    public static final int BANNER_TYPE_LOGIN = 30;

    public static final String[] WEEK_STRINGS = new String[]{
            "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    public static final int ERROR_TYPE_DATA = 1;
    public static final int ERROR_TYPE_NETWORK = -1000;
    public static final int ERROR_TYPE_REQUESTING = 3;
    public static final int ERROR_TYPE_URL_FAULT = 4;

    public static final int HOME_TYPE_CATEGORIES = 1;
    public static final int HOME_TYPE_TOP_CATEGORY = 2;
    public static final int HOME_TYPE_FEED_PRODUCT = 3;
    public static final int HOME_TYPE_TOPIC = 4;
    public static final int HOME_TYPE_TRIAL_DETAIL = 5;
    public static final int HOME_TYPE_PRODUCT_DETAIL = 6;
    public static final int HOME_TYPE_CATEGORY = 7;
    public static final int HOME_TYPE_BRAND = 8;
    public static final int HOME_TYPE_TOP_LIST = 9;
    public static final int HOME_TYPE_TOP_DETAIL = 10;

    public static final int DEFAULT_PAY_TYPE = 0;
    public static final int REWARD_PAY_TYPE = 1;
    public static final int VIP_PAY_TYPE = 2;

    public static int CURRENT_PAY_TYPE = DEFAULT_PAY_TYPE;

    /********************2.2.0************************/

    //1，预备中；2，已上架；3，下架
    public static final int GOODS_STATE_PRPARING = 1;
    public static final int GOODS_STATE_SALE = 2;
    public static final int GOODS_STATE_INVALIDATE = 3;
}
