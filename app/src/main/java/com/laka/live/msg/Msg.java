package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

/**
 * Created by luyi on 2015/8/20.
 */
public class Msg {

    public final static int DEFAULT_STATUS = -100000;

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


    public final static int TLV_EXCHANGE_NOT_ENOUGH = 1010;

    @Expose
    @SerializedName(Common.CODE)
    private int code = NETWORK_ERROR_UNKNOW_ERROR;

    @Expose
    @SerializedName(Common.STATUS)
    private int status = DEFAULT_STATUS;

    @Expose
    @SerializedName(Common.ERROR)
    private String error;

    @Expose
    @SerializedName(Common.COMMAND)
    private String command;

    @Expose
    @SerializedName(Common.TIME_STAMP)
    private long timestamp;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isSuccessFul() {

        if (status != DEFAULT_STATUS) {
          // 如果status不为DEFAULT_STATUS,则代表该接口是商城新接口;
            return status == TLV_OK;
        } else {
            return code == TLV_OK;
        }

    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void parase() {

    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}