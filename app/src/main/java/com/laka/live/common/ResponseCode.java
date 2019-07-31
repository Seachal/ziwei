package com.laka.live.common;

/**
 * Created by zwl on 2016/7/27.
 * Email-1501448275@qq.com
 */
public class ResponseCode {

    public final static int FILE_NOT_FOUND_ERROR = -4;

    /**
     * 未知错误
     */
    public final static int NETWORK_ERROR_UNKNOW_ERROR = -3;

    /**
     * 网络出错
     */
    public final static int NETWORK_ERROR_PARSE_ERROR = -2;

    /**
     * 网络出错
     */
    public final static int NETWORK_ERROR_NETWORK_ERROR = -1;


    /**
     * 无错误
     */
    public final static int TLV_OK = 0;


    /**
     * 错误的手机号
     */
    public final static int TLV_INVALID_PHONE_NUMBER = 1;
    /**
     * reds连接失败
     */
    public final static int TLV_REDIS_CONNECT_FAILED = 2;
    /**
     * 获取手机验证码太过频繁
     */
    public final static int TLV_GET_PVC_TOO_FREQUENT = 3;

    /**
     * 用户未登录
     */
    public final static int TLV_USER_NOT_LOGIN = 4;

    /**
     * 错误的用户token
     */
    public final static int TLV_WRONG_USER_TOEKN = 5;
    /**
     * MySQL连接失败
     */
    public final static int TLV_MYSQL_CONNECT_FAILED = 6;
    /**
     * MySQL查询失败
     */
    public final static int TLV_MYSQL_QUERY_FAILED = 7;
    /**
     * 错误的手机号或者错误的手机验证码
     */
    public final static int TLV_WRONG_PHONE_NUMBER_OR_WRONG_PHONE_VERIFICATION_CODE = 8;
    /**
     * 获取微信access token失败
     */
    public final static int TLV_GET_WECHAT_ACCESS_TOKEN_FAILED = 9;

    /**
     * 获取腾讯云视频签名失败
     */
    public final static int TLV_QCLOUD_SIGN_FAILED = 10;

    /**
     * 发送手机验证码失败
     */
    public final static int TLV_SEND_PVC_FAILED = 11;

    /**
     * 商品不存在
     */
    public final static int TLV_PRODUCT_NOT_EXIST = 12;

    /**
     * 用户不存在
     */
    public final static int TLV_USER_NOT_EXIST = 13;

    /**
     * 空的搜索关键字
     */
    public final static int TLV_EMPTY_SEARCH_KEYWORD = 14;

    /**
     * 错误的参数
     */
    public final static int TLV_INVALID_PARAMETER = 15;

    /**
     * 获取微博access token失败
     */
    public final static int TLV_GET_WEIBO_ACCESS_TOKEN_FAILED = 16;

    /**
     * 获取qq登陆access token失败
     */
    public final static int TLV_GET_QQ_ACCESS_TOKEN_FAILED = 17;

    /**
     * 不能对自己进行操作
     */
    public final static int TLV_NOT_OP_ONESELF = 18;

    //重复操作
    public final static int TLV_OP_DUP = 19;

    /**
     * 用户token过期
     */
    public final static int TLV_USER_TOKEN_EXPIRE = 20;

    /**
     * 用户已经绑定过提现手机号
     */
    public final static int TLV_USER_WITHDRAW_PHONE_EXIST = 21;

    /**
     * 调用微信支付失败
     */
    public final static int TLV_GET_WECHAT_PAY_FAILED = 22;

    /**
     * 用户已经绑定过提现微信号
     */
    public final static int TLV_USER_WITHDRAW_WECHAT_EXIST = 23;

    /**
     * 不能对自己操作
     */
    public final static int TLV_FAILD_DO_ONESELF = 26;

    public final static int TLV_EXCHANGE_NOT_ENOUGH = 1010;

    /**
     * 关注的用户不存在
     */
    public final static int TLV_OP_USER_NOT_EXIST_FOLLOW_ID = 1032;

    /**
     * 取消关注的用户不存在
     */
    public final static int TLV_OP_USER_NOT_EXIST_UNFOLLOW_ID = 1033;

    /**
     * 关注失败 已把对方拉黑
     */
    public final static int FOLLOW_FAIL_BLOCK = 1039;

    /**
     * 关注失败 已被对方拉黑
     */
    public final static int FOLLOW_FAIL_BLOCK_OTHER = 1040;
}
