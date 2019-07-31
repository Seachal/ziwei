package com.laka.live.shopping.common;

/**
 * Created by gangqing on 2015/11/26.
 * Email:denggangqing@ta2she.com
 */
public class Constant {
    public static final int TYPE_LOGIN = 0;
    public static final int TYPE_REGISTER = 1;

    public static final String PHONE = "phone";
    public static final String NICKNAME = "nickname";
    public static final String PASSWORD = "password";
    public static final int FOLLOW = 1;
    public static final int FANS = 2;
    public static final int EVALUATORS = 3;
    public static final int RELATIONSHIP_PAGER_SIZE = 20;
    //我-取消关注
    public static final String CANCEL_ATTENTION = "0";
    //我-点击关注
    public static final String ATTENTIONING = "1";
    //我-消息-用户角色-她他小秘
    public static final String TA_SHE_SECRETARY = "6";

    public static final String EXTERNAL_POST = "1";
    public static final String EXTERNAL_CIRCLE = "2";
    public static final String EXTERNAL_ARTICLE = "3";
    public static final String EXTERNAL_GOOD = "4";
    public static final String EXTERNAL_COMMODITY_PROJECT = "5";
    public static final String EXTERNAL_LINKS = "6";
    public static final String EXTERNAL_POSTURE = "7";
    public static final String EXTERNAL_TEST = "8";
    public static final String EXTERNAL_SHOPPING_GUIDE = "9";
    public static final String EXTERNAL_IMAGE = "10";
    public static final String EXTERNAL_QUESTION_ANSWER = "11";
    public static final String EXTERNAL_ORDER = "12";
    public static final String EXTERNAL_TRY_GOODS_EVALUATION = "13";
    public static final String EXTERNAL_TRY_GOODS_DETAILS = "14";
    public static final String EXTERNAL_REDBAG = "15";

    public static final String TYPE_TEST_ARTICAL_COMMENT = "11";
    public static final String TYPE_SHOPPING_ARTICAL_COMMENT = "12";
    public static final String TYPE_RISEGESTURE_ARTICAL_COMMENT = "13";
    public static final String TYPE_PIC_ARTICAL_COMMENT = "14";
    public static final String TYPE_GOODS_ARTICAL_COMMENT = "15";
    public static final String TYPE_GOODS_ARTICAL = "16";

    public static final int TYPE_REPLY_ME = 1;
    public static final int TYPE_SAME = 2;

    public static final int CONTENT_TYPE_TEXT = 1;
    public static final int CONTENT_TYPE_IMAGE = 2;
    public static final int CONTENT_TYPE_VIDEO = 3;
    public static final int CONTENT_TYPE_VIDEO_OOS = 4;

    //发送方标识，自己：1；对方：2
    public static final int SENDER_FLAG_SELF = 1;
    public static final int SENDER_FLAG_OTHERS = 2;
    //小秘用户id
    public static final String USER_ID_SHE_SECRETARY = "0";
    public static final String USER_TYPE_NORMAL = "1";
    public static final String USER_TYPE_SECRETARY = "2";

    public static final String BLACK = "1"; //拉黑
    public static final String CANCEL_BLACK = "0";  //取消拉黑

    public static final int PRIVACY_OPEN_PASSWORD = 0;   //开启隐私保护
    public static final int PRIVACY_CLOSE_PASSWORD = 1;    //取消隐私保护
    public static final int PRIVACY_INTO_APPLICATION = 2;   //进入应用

    public static final int OPEN_FROM_TYPE_GUIDE = 1; //从登陆引导页打开
    public static final int OPEN_FROM_TYPE_OTHER = 2; //从其他页打开

    public static final int LOGIN_FORM_TYPE_LOGIN = 0;  //从登录页登录第三方
    public static final int LOGIN_FORM_TYPE_REGISTER = 1;   //从注册页登录第三方
    public static int LOGIN_FORM_TYPE = 0;

    //订单状态； 1=>'待受理',2 =>'未付款',3=>'待发货',4=>'已发货',5 =>'已取消',6=>'完成',7=>'待评价') 0为全部；
    public static final int ORDER_ALL = 0;
    public static final int ORDER_WAIT_DEAL = 1;
    public static final int ORDER_WAIT_PAY = 2;
    public static final int ORDER_WAIT_DEL = 3;
    public static final int ORDER_REV = 4;
    public static final int ORDER_CALCEN = 5;
    public static final int ORDER_FINISH = 6;
    public static final int ORDER_WAIT_EVA = 7;

    public static final String TYPE_LINK_VIDEO = "16"; // 视频


}
