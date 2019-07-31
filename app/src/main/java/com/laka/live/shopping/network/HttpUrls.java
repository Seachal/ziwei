/*
 * Copyright (C) 2015-2015 SJY.JIANGSU Corporation. All rights reserved
 */

package com.laka.live.shopping.network;


import com.laka.live.BuildConfig;
import com.laka.live.shopping.common.CommonConst;
import com.laka.live.shopping.system.SystemHelper;

/**
 * Created by zhxu on 2015/11/9.
 * Email:357599859@qq.com
 */
public class HttpUrls {
    //2.2.0测试
    private static final String SHOPPING_SERVER_URL = BuildConfig.SHOPPING_SERVER_URL;
    //主接口配置
//    public final static String SERVER_URL = "http://test.api.ta2she.com:8071";
    public final static String SERVER_URL = SHOPPING_SERVER_URL;

    //审核通过接口
    public static final String AUDITED_URL = SERVER_URL + "/android_audit_new.html";
    //闪屏接口
    public final static String URL_INDEX = SERVER_URL + "/index.php";
    //商城首页接口
    public final static String URL_SHOP = SERVER_URL + "/mall.php";
    //商城首页接口
    public final static String SHOP_URL = SERVER_URL + "/mall/";
    //个人资料接口
    public static final String USER_URL = SERVER_URL + "/user.php";
    //消息资料接口
    public static final String LETTER_URL = SERVER_URL + "/letter.php";
    //    //首页接口
//    public static final String SHOPPING_HOME_URL = SERVER_URL + "/home";
    //社区接口
    public static final String COMMUNITY_URL = SERVER_URL + "/bbs.php";
    //支付接口
    public static final String PAY_URL = SERVER_URL + "/pay.php";
    //购物车接口
    public static final String CART_URL = SERVER_URL + "/cart/";
    //生成订单接口
    public static final String ORDER_URL = SERVER_URL + "/order/";
    //收货地址
    public static final String ADDRESS_URL = SERVER_URL + "/address/";
    //帮助中心接口
    public static final String HELP_URL = SERVER_URL + "/h5.php?a=help";
    //关于她他社
    public static final String ABOUT_URL = SERVER_URL + "/h5.php?a=aboutUs&version=";
    //关于她他社
//    public static final String LOGISTICS_URL = SERVER_URL + "/h5.php?a=kuaidi2&logisticsId=";
    //物流详情
    public static final String LOGISTICS_URL = SERVER_URL + "/h5/logistics/detail?logisticsId=";

    //等级说明接口
    public static final String LEVEL_EXPLAIN = SERVER_URL + "/h5.php?a=howToPlay";
    //应用评分接口
    public static final String APPLICATION_GRADE = "market://details?id=" + SystemHelper.getAppInfo().packageName;
    //设置接口
    public static final String USERCONF_URL = SERVER_URL + "/userconf.php";
    //金额配置接口
    public static final String REWARD_MONEY_CONFIG_URL = SERVER_URL + "/index.php";

    public static final String H5_URL = BuildConfig.SERVER_URL + "/h5.php";
    //分享帖子链接
    public static final String SHARE_CARD_URL = BuildConfig.SERVER_URL + "/h5.php?a=share&apiver=" + CommonConst.API_VERSION + "&post_id=";
    //圈子分享
    public static final String SHARE_CIRCLE_URL = BuildConfig.SERVER_URL + "/h5.php?a=circleShare&apiver=" + CommonConst.API_VERSION + "&id=";
    //直播分享
    public static final String SHARE_LIVE_URL = BuildConfig.SERVER_URL + "/h5.php?a=liveShare&id=";
    //分享文章
    public static final String SHARE_ARTICLE_URL = BuildConfig.SERVER_URL + "/h5.php?a=articleShare&apiver=" + CommonConst.API_VERSION + "&id=";
    //按钮样式分享页面
    public static final String SHARE_STYLE_URL = BuildConfig.SERVER_URL + "/h5.php?a=styleBtn&id=";
    //商品详情webview连接
    public static final String GOODS_DETAIL = BuildConfig.SERVER_URL + "/h5.php?a=goodsDetail&id=";
    //商品测试列表连接
    public static final String PRODUCT_EVALUTORS_URL = BuildConfig.SERVER_URL + "/h5.php?a=GoodsDetailEvTab&id=";
    //圈子介绍
    public static final String CIRCLE_DETAIL_URL = BuildConfig.SERVER_URL + "/h5.php?a=circleDetail&id=";
    //微信支付
    public static final String WECHAT_PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    //社区圈子统计接口
    public static final String COMMUNITY_CIRCLE_STATS_URL = "http://s.api.ta2she.com/t.html";
    //TA币说明
    public static final String TA_COIN_EXPLAIN = BuildConfig.SERVER_URL + "/h5.php?a=coinDetail";
    //积分说明
    public static final String INTEGRAL_EXPLAIN = BuildConfig.SERVER_URL + "/h5.php?a=myPoint";
    //试用介绍
    public static final String TRAIL_URL = BuildConfig.SERVER_URL + "/h5.php?a=evGoodsDetail&id=";
    //榜单列表页面
    public static final String TOP_LIST_DETAIL_URL = SERVER_URL + "/h5.php?a=toplistDetail&id=";
    //分享她他社
    public static final String SHARE_TA_SHE = SERVER_URL + "/h5.php?a=shareApp";
    // 获取sts临时凭证
    public static final String GET_STS = SERVER_URL + "/getStsToken.php";


    /****************2.2.0***************************/


    //首页接口
    public static final String SHOPPING_HOME_URL = SHOPPING_SERVER_URL + "/home";

    //子分类列表
    public static final String SHOPPING_SUBCATEGORIES = SHOPPING_SERVER_URL + "/category/subcategories";

    //分类商品列表
    public static final String SHOPPING_CATEGORY_GOODS = SHOPPING_SERVER_URL + "/goods/categoryGoods";

    //专题商品列表
    public static final String SHOPPING_TOPIC_GOODS = SHOPPING_SERVER_URL + "/goods/topicGoods";

    //商品详情
    public static final String SHOPPING_GOODS_DETAIL = SHOPPING_SERVER_URL + "/goods/detail";

    //商品相关推荐
    public static final String SHOPPING_GOODS_RECOMMEND = SHOPPING_SERVER_URL + "/goods/recommend";

    //商品关键字
    public static final String SEARCH_GOODS_HOT_KEYWORD_URL = SHOPPING_SERVER_URL + "/goods/search/hotwords";

    //获取推荐商品
    public static final String GET_RECOMMENDS_GOODS_URL = SHOPPING_SERVER_URL + "/course/recommendGoods";

    //商品详情webview连接
    public static final String SHOPPING_GOODS_DETAIL_H5 = SHOPPING_SERVER_URL + "/h5/goods/detail?goodsId=";

    //购物车加入商品
    public static final String SHOPPING_ADD_SHOPPING_CAR = SHOPPING_SERVER_URL + "/cart/add";

    //获取订单列表
    public static final String GET_ORDER_LIST_URL = SHOPPING_SERVER_URL + "/order/list";

    //取消订单接口
    public static final String ORDER_CANCEL = SHOPPING_SERVER_URL + "/order/cancel";

    //确认收货接口
    public static final String ORDER_CONFIRM = SHOPPING_SERVER_URL + "/order/confirmReceipt";

    //删除订单接口
    public static final String ORDER_DELETE = SHOPPING_SERVER_URL + "/order/delete";

    //获取订单详情接口
    public static final String ORDER_DETAIL = SHOPPING_SERVER_URL + "/order/detail";

    //购物车商品数量
    public static final String SHOPPING_CART_COUNT = SHOPPING_SERVER_URL + "/cart/count";

    // 获取微信支付参数的
    public static final String QUERY_ORDER_WECHAT_PAY_URL = SERVER_URL + "/order/pay/wxpayParams";

    // 获取支付宝支付参数的
    public static final String QUERY_ORDER_ALI_PAY_URL = SERVER_URL + "/order/pay/alipayParams";

    //获取商品收益列表
    public static final String GOODS_INCOMES = SERVER_URL + "/user/goodsIncomes";

    //获取商品收益明细
    public static final String GOODS_INCOMES_DETAILS = SERVER_URL + "/user/goodsIncomesDetails";

    //获取商品收益明细详情
    public static final String GOODS_INCOMES_DETAILS_INFO = SERVER_URL + "/user/goodsIncomesDetailInfo";

}
