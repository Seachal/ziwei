/*
 * Copyright (c) 2015. SJY.JIANGSU Corporation. All rights reserved
 */

package com.laka.live.shopping.framework.adapter;

public class MsgDef {
    private static byte sBaseId = 10;

    private static byte generateId() {
        return sBaseId++;
    }

    public static final int MSG_SHOW_MAIN_WINDOW = generateId();
    public static final int MSG_INIT = generateId();
    public static final int MSG_INIT_ACTION_EVENTS = generateId();
    public static final int MSG_SHARE = generateId();
    public static final int MSG_EXIT = generateId();
    public static final int MSG_SHOW_SPLASH_WINDOW = generateId();
    public static final int MSG_CLOSE_SPLASH_WINDOW = generateId();
    public static final int MSG_GET_SPLASH_WINDOW = generateId();
    public static final int MSG_SHOW_SHOPPING_CATEGORY_WINDOW = generateId();
    public static final int MSG_SHOW_SHOPPING_TOP_LIST_WINDOW = generateId();
    public static final int MSG_SHOW_SHOPPING_TOP_LIST_DETAIL_WINDOW = generateId();
    public static final int MSG_SHOW_SHOPPING_CATEGORIES_WINDOW = generateId();
    public static final int MSG_SHOW_SHOPPING_TOPIC_WINDOW = generateId();
    public static final int MSG_SHOW_ORDER_WINDOW = generateId();
    public static final int MSG_SHOW_ORDER_FAILED_WINDOW = generateId();
    public static final int MSG_SHOW_ORDER_SUCCESS_WINDOW = generateId();
    public static final int MSG_SHOW_ORDER_ADDRESS_WINDOW = generateId();
    public static final int MSG_SHOW_ORDER_LIST_WINDOW = generateId();
    public static final int MSG_SHOW_ORDER_REVIEW_WINDOW = generateId();
    public static final int MSG_SHOW_ORDER_REVIEW_LIST_WINDOW = generateId();
    public static final int MSG_SHOW_ORDER_DETAIL_WINDOW = generateId();
    public static final int MSG_SHOW_ORDER_LOGISTICS_WINDOW = generateId();
    public static final int MSG_GET_PAY_PARAMS = generateId();

    //photo preview
    public static final int MSG_SHOW_PHOTO_PREVIEW_WINDOW = generateId();
    public static final int MSG_SHOW_PHOTO_PICK_PREVIEW_WINDOW = generateId();
    public static final int MSG_SHOW_PHOTO_PICKER_WINDOW = generateId();
    public static final int MSG_CLOSE_PHOTO_PICKER_WINDOW = generateId();
    public static final int MSG_SHOW_PHOTO_CROP_WINDOW = generateId();
    //homepage
    public static final int MSG_GET_HOMEPAGE_TAB = generateId();
    public static final int MSG_HOMEPAGE_SHOW_ARTICLE_DETAIL = generateId();
    public static final int MSG_HOMEPAGE_SHOW_ARTICLE_LIST = generateId();
    //knowledge
    public static final int MSG_SHOW_KNOWLEDGE_MAIN_WINDOW = generateId();
    public static final int MSG_SHOW_KNOWLEDGE_LIST_WINDOW = generateId();
    public static final int MSG_SHOW_KNOWLEDGE_DETAIL_WINDOW = generateId();
    //收藏
    public static final int MSG_SHOW_COLLECT_WINDOW = generateId();
    //记录
    public static final int MSG_SHOW_RECORD_WINDOW = generateId();
    public static final int MSG_SHOW_RECORD_PEEP_WINDOW = generateId();
    public static final int MSG_SHOW_RECORD_SELF_WINDOW = generateId();
    //社区
    public static final int MSG_GET_COMMUNITY_TAB = generateId();
    public static final int MSG_SHOW_COMMUNITY_WINDOW = generateId();
    public static final int MSG_SHOW_COMMUNITY_TALENT_WINDOW = generateId();
    public static final int MSG_SHOW_COMMUNITY_ALL_CIRCLES_WINDOW = generateId();
    public static final int MSG_SHOW_COMMUNITY_CIRCLES_DETAIL_WINDOW = generateId();
    public static final int MSG_SHOW_COMMUNITY_SEND_POST_WINDOW = generateId();
    public static final int MSG_SHOW_COMMUNITY_POST_DETAIL_WINDOW = generateId();
    public static final int MSG_SHOW_COMMUNITY_COMMENT_DETAIL_WINDOW = generateId();
    public static final int MSG_SHOW_USER_INFO_WINDOW = generateId();
    public static final int MSG_SHOW_COMMUNITY_MY_POST_WINDOW = generateId();
    public static final int MSG_SHOW_COMMUNITY_NO_PASS_POST_WINDOW = generateId();
    public static final int MSG_SHOW_COMMUNITY_CIRCLE_INFO_DETAIL_WINDOW = generateId();
    public static final int MSG_SHOW_COMMUNITY_HOT_POST_LIST_WINDOW = generateId();
    public static final int MSG_SHOW_COMMUNITY_TOPIC_LIST_WINDOW = generateId();
    public static final int MSG_SHOW_COMMUNITY_MY_WINDOW = generateId();
    //引导
    public static final int MSG_SHOW_GUIDE_WINDOW = generateId();
    public static final int MSG_SHOW_GUIDE_NEW_USER_WINDOW = generateId();
    public static final int MSG_GET_GUIDE_WINDOW_STATE = generateId();
    //comments
    public static final int MSG_SHOW_COMMENT_LIST_WINDOW = generateId();
    //live
    public static final int MSG_SHOW_LIVE_WINDOW = generateId();
    //shopping
    public static final int MSG_SHOW_SHOPPING_HOME_TAB = generateId();
    public static final int MSG_SHOW_SHOPPING_TAB = generateId();
    public static final int MSG_SHOW_GOODS_DETAIL_WINDOW = generateId();
    public static final int MSG_SHOW_GOODS_COMMENTS_WINDOW = generateId();
    public static final int MSG_SHOW_SHOPPING_CAR_WINDOW = generateId();
    public static final int MSG_SHOPPING_CAR_GO_SHOPPING = generateId();

    //我
    public static final int MSG_SHOW_TREND_EVALUATION_WINDOW = generateId(); // 帖子的评论页
    public static final int MSG_SHOW_USER_TRENDS_WINDOW = generateId(); // TA的圈子的列表页
    public static final int MSG_SHOW_SECRET_ZONE_WINDOW = generateId(); // 私密空间
    public static final int MSG_GET_ACCOUNT_TAB = generateId();
    public static final int MSG_SHOW_LOGIN_REGISTER_WINDOW = generateId();
    public static final int MSG_SHOW_RETRIEVE_PASSWORD_WINDOW = generateId();
    public static final int MSG_SHOW_SETTING_WINDOW = generateId();
    public static final int MSG_SHOW_VERIFICATION_WINDOW = generateId();
    public static final int MSG_SHOW_BIND_PHONE_WINDOW = generateId();
    public static final int MSG_SHOW_PERSONAL_WINDOW = generateId();
    public static final int MSG_SHOW_WEB_VIEW_BROWSER = generateId();
    public static final int MSG_SHOW_CHANGE_PASSWORD_WINDOW = generateId();
    public static final int MSG_SHOW_CHANGE_NICKNAME_WINDOW = generateId();
    public static final int MSG_SHOW_CHANGE_SIGN_WINDOW = generateId();
    public static final int MSG_SHOW_CHANGE_PHONE_WINDOW = generateId();
    public static final int MSG_SHOW_LEVEL_WINDOW = generateId();
    public static final int MSG_SHOW_RELATIONSHIP_WINDOW = generateId();
    public static final int MSG_SHOW_FEEDBACK_WINDOW = generateId();
    public static final int MSG_SHOW_LETTERS_BLACKLIST_WINDOW = generateId();
    public static final int MSG_SHOW_FEEDBACK_CONTENT_WINDOW = generateId();
    public static final int MSG_SHOW_PRIVACY_PROTECTION_WINDOW = generateId();
    public static final int MSG_GET_APP_PRIVACY_STATE = generateId();
    public static final int MSG_SHOW_ALERT_SETTING_WINDOW = generateId();
    public static final int MSG_SHOW_TEST_RESULT_WINDOW = generateId();
    public static final int MSG_SHOW_MY_EVALUATE_WINDOW = generateId();
    public static final int MSG_SHOW_MY_INCOME_WINDOW = generateId();
    public static final int MSG_SHOW_WITHDRAW_WINDOW = generateId();
    public static final int MSG_SHOW_VIP_WINDOW = generateId();  //vip
    public static final int MSG_SHOW_APPROVE_TIP_WINDOW = generateId();//认证页面
    public static final int MSG_SHOW_APPROVE_PHOTO_WINDOW = generateId();//认证拍照页面
    public static final int MSG_CLOSE_APPROVE_PHOTO_WINDOW = generateId();//关闭自拍认证页面

    //消息
    public static final int MSG_SHOW_SEARCH_USER_WINDOW = generateId(); // 搜索页面
    public static final int MSG_GET_MESSAGE_TAB = generateId();
    public static final int MSG_SHOW_MESSAGE_TAB = generateId();
    public static final int MSG_SHOW_MESSAGE_CHAT_WINDOW = generateId();
    public static final int MSG_SHOW_REPLY_ME_WINDOW = generateId();

    public static final int MSG_CLOSE_FEEDBACK_WINDOW = generateId();
    public static final int MSG_CLOSE_SETTING_WINDOW = generateId();
    public static final int MSG_CLOSE_LOGIN_WINDOW = generateId();
    public static final int MSG_CLOSE_BIND_PHONE_WINDOW = generateId();
    public static final int MSG_CLOSE_RETRIEVE_PASSWORD_WINDOW = generateId();
    public static final int MSG_CLOSE_VERIFICATION_WINDOW = generateId();
    public static final int MSG_CLOSE_PERSONAL_WINDOW = generateId();
    public static final int MSG_CLOSE_PASSWORD_CHANGE_WINDOW = generateId();
    public static final int MSG_CLOSE_CHANGE_NICKNAME_WINDOW = generateId();

    public static final int MSG_VERSION_CHECK_FINISH = generateId();
    public static final int MSG_RELATIONSHIP_ACCOUNT_DATA_CHANGE = generateId();
    public static final int MSG_PRIVACY_PROTECTION_STATE_CHANGE = generateId();

    public static final int MSG_SHOPPING_CAR_INVALID_WINDOW_OPEN = generateId();
    public static final int MSG_SHOW_RECOMMEND_APP_WINDOW = generateId();

    public static final int MSG_START_VIDEO_RECORDER = generateId();
    //免费试用
    public static final int MSG_SHOW_TRIAL_LIST_WINDOW = generateId();
    public static final int MSG_SHOW_TRIAL_APPLY_LIST_WINDOW = generateId();
    public static final int MSG_SHOW_TRIAL_DETAIL_WINDOW = generateId();
    public static final int MSG_SHOW_TRIAL_APPLY_WINDOW = generateId();
    public static final int MSG_SHOW_TRIAL_APPLY_DETAIL_WINDOW = generateId();
    //写评测
    public static final int MSG_START_EVALUATE = generateId();
    public static final int MSG_SHOW_EVALUATOR_LIST_WINDOW = generateId();
    public static final int MSG_SHOW_EVALUATE_GOODS_PICKER_WINDOW = generateId();
    public static final int MSG_SHOW_EVALUATE_EDIT_WINDOW = generateId();
    public static final int MSG_START_EVALUATE_EDIT_WINDOW = generateId();
    public static final int MSG_SHOW_REEDIT_EVALUATE_WINDOW = generateId();

    //TA币
    public static final int MSG_SHOW_TA_COIN_MAIN_WINDOW = generateId();
    public static final int MSG_SHOW_TA_COIN_INCOME_WINDOW = generateId();
    public static final int MSG_SHOW_TA_COIN_TOAST = generateId();
    public static final int MSG_SHOW_TA_DAY_LOGIN = generateId();
    public static final int MSG_SHOW_TA_EXPLAIN_WINDOW = generateId();
    //
    public static final int MSG_SHOW_GUIDE_TAB = generateId();
    public static final int MSG_SHOW_COMMUNITY_TAB = generateId();

    //搜索
    public static final int MSG_SHOW_SEARCH_WINDOW = generateId();


    //情趣指南
    public static final int MSG_SHOW_FUN_GUIDE_TAB = generateId();
    public static final int MSG_SHOW_FUN_GUIDE_DETAIL = generateId();

    //打赏
    public static final int MSG_REWARD_PAY_SUCCEED = generateId();
    public static final int MSG_REWARD_PAY_FAILED = generateId();

    //vip
    public static final int MSG_VIP_PAY_SUCCEED = generateId();
    public static final int MSG_VIP_PAY_FAILED = generateId();
}
