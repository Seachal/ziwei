package com.laka.live.account.login;


/**
 * Created by zwl on 16/8/4.
 */

public class ErrorCode {
    public static final int ERROR_CODE_USER_NOT_EXIST = 13;//请求的token对应用户不存在。
    public static final int ERROR_CODE_OP_USER_NOT_EXIST = 27;//请求的user_id对应用户不存在。
    public static final int ERROR_CODE_USER_DENY_LOGIN = 28;//当前用户已禁止登陆。
    public static final int ERROR_CODE_INVALID_PHONE_NUMBER = 1;//错误的手机号。
    public static final int ERROR_CODE_GET_QQ_TOKEN_FAIL = 17;//获取qq登陆的token失败。
    public static final int ERROR_CODE_GET_WE_CHAT_TOKEN_FAIL = 9;//获取微信登陆token失败。
    public static final int ERROR_CODE_GET_WEI_BO_TOKEN_FAIL = 16;//获取微博登陆token失败。
}
