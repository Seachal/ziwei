/*
 * Copyright (c) 2015. SJY.JIANGSU Corporation. All rights reserved
 */

package com.laka.live.shopping.framework.adapter;

public class NotificationDef {
    private static int sBaseId = 0;

    private static int generateId() {
        return sBaseId++;
    }

    public static final int N_NETWORK_STATE_CHANGE = generateId();
    public static final int N_FULL_SCREEN_MODE_CHANGE = generateId();
    public static final int N_FOREGROUND_CHANGE = generateId();
    public static final int N_SPLASH_FINISHED = generateId();
    public static final int N_ACTIVITY_RESULT = generateId();
    public static final int N_GUIDE_FINISHED = generateId();
    public static final int N_PRIVACY_FINISHED = generateId();
    public static final int N_APP_MD5_CHANGED = generateId();
    public static final int N_MARKET_AUDITED_CHANGED = generateId();

    /**
     * 我
     */
    public static final int N_ACCOUNT_STATE_CHANGED = generateId(); //帐号状态改变
    public static final int N_ACCOUNT_LOGIN_SUCCESS = generateId(); //登录成功
    public static final int N_ACCOUNT_LOGIN_FAILED = generateId();  //登录失败
    public static final int N_ACCOUNT_NEW_USER = generateId(); //新用户登录
    public static final int N_MESSAGE_UNREAD_COUNT_CHANGED = generateId();  //获取到了未读消息数
    public static final int N_CHAT_BLACK_CHANGED = generateId();
    public static final int N_ACCOUNT_CHANGE_USER_IMAGE = generateId(); //修改用户头像

    public static final int N_ACCOUNT_PRIVACY_STATE_CHANGE = generateId();  //隐私保护状态改变

    public static final int N_INPUT_METHOD_SHOW = generateId();
    public static final int N_INPUT_METHOD_HIDE = generateId();
    public static final int N_ORDER_ADDRESS_CHANGED = generateId();
    public static final int N_ORDER_LIST_CHANGED = generateId();
    public static final int N_ORDER_REVIEW_PUSH_SUCCESS = generateId();

    public static final int N_VIP_PAY_SUCCEED = generateId();//vip支付成功

    /**
     * 社区
     */
    public static final int N_COMMUNITY_IS_SEND_SOME_REPLY = generateId();
    public static final int N_COMMUNITY_UPDATE_COMMENT_PRAISE = generateId();
    public static final int N_COMMUNITY_UPDATE_POST_PRAISE = generateId();
    public static final int N_COMMUNITY_SHOW_TIPS = generateId();
    public static final int N_EMOJI_KEYBOARD_SHOW = generateId();
    public static final int N_COMMUNITY_LOAD_CIRCLE_INFO = generateId();
    public static final int N_COMMUNITY_SEND_POST_RESULT = generateId();
    public static final int N_COMMUNITY_RELOAD_CIRCLE_INFO = generateId();
    public static final int N_COMMUNITY_UPDATE_EVALUATE_CONTENT = generateId();
    public static final int N_COMMUNITY_HOT_POST_LIST_PAGE = generateId();

    /**
     * 直播
     */
    public static final int N_LIVE_SEND_REFERENCED_MESSAGE = generateId();
    public static final int N_LIVE_START_VIDEO = generateId();
    public static final int N_LIVE_NEW_MESSAGE_TIP = generateId();
    public static final int N_LIVE_CLEAR_NEW_MESSAGE_TIP = generateId();


    public static final int N_SHOPPING_CAR_COUNT_CHANGE = generateId();

}
