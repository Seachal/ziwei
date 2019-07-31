package com.laka.live.shopping;

import android.os.Message;

import com.laka.live.shopping.common.CommonConst;
import com.laka.live.shopping.framework.MsgDispatcher;
import com.laka.live.shopping.framework.adapter.MsgDef;
import com.laka.live.shopping.homepage.HomepageConst;
import com.laka.live.shopping.model.ShoppingCategoryInfo;
import com.laka.live.shopping.model.ShoppingTopicInfo;


/**
 * Created by zhxu on 2016/5/5.
 * Email:357599859@qq.com
 */
public class ShoppingHelper {

    public static void handleClick(ShoppingCategoryInfo categoryInfo) {
        String value = String.valueOf(categoryInfo.typeId);
        String title = categoryInfo.title;
        int type = categoryInfo.type;

        if (type == CommonConst.HOME_TYPE_FEED_PRODUCT) {
            int feedType = HomepageConst.FEED_TYPE_PRODUCT;
            // TODO: 2017/7/14  showFeedDetail
//            showFeedDetail(feedType, value);
        } else if (type == CommonConst.HOME_TYPE_PRODUCT_DETAIL) {
            showProductDetail(value);
        } else if (type == CommonConst.HOME_TYPE_TOPIC) {
            showProductTopic(value, title);
        } else if (type == CommonConst.HOME_TYPE_TRIAL_DETAIL) {
            showTrialDetailWindow(value);
        } else if (type == CommonConst.HOME_TYPE_CATEGORIES) {
            showCategoriesWindow();
        } else if (type == CommonConst.HOME_TYPE_TOP_CATEGORY) {
            showCategoryWindow(categoryInfo);
        } else if (type == CommonConst.HOME_TYPE_BRAND) {
            showCategoryWindow(categoryInfo);
        } else if (type == CommonConst.HOME_TYPE_CATEGORY) {
            showCategoryWindow(categoryInfo);
        } else if (type == CommonConst.HOME_TYPE_TOP_LIST) {
            showShoppingTopListWindow(categoryInfo.typeId);
        } else if (type == CommonConst.HOME_TYPE_TOP_DETAIL) {
            showShoppingTopListDetailWindow(categoryInfo.typeId);
        }
    }

    private static void showCategoryWindow(ShoppingCategoryInfo categoryInfo) {
        if (categoryInfo == null) {
            return;
        }
        Message message = Message.obtain();
        message.what = MsgDef.MSG_SHOW_SHOPPING_CATEGORY_WINDOW;
        message.obj = categoryInfo;
        MsgDispatcher.getInstance().sendMessage(message);
    }

    private static void showCategoriesWindow() {
        MsgDispatcher.getInstance().sendMessage(MsgDef.MSG_SHOW_SHOPPING_CATEGORIES_WINDOW);
    }

    private static void showTrialDetailWindow(String value) {
        Message message = Message.obtain();
        message.obj = value;
        message.what = MsgDef.MSG_SHOW_TRIAL_DETAIL_WINDOW;

        MsgDispatcher.getInstance().sendMessage(message);
    }

    public static void showTrialListWindow() {
        MsgDispatcher.getInstance().sendMessage(MsgDef.MSG_SHOW_TRIAL_LIST_WINDOW);
    }

    public static void showShoppingTopListDetailWindow(int topListId) {
        Message message = Message.obtain();
        message.what = MsgDef.MSG_SHOW_SHOPPING_TOP_LIST_DETAIL_WINDOW;
        message.arg1 = topListId;
        MsgDispatcher.getInstance().sendMessage(message);
    }

    public static void showShoppingTopListWindow(int cateId) {
        Message message = Message.obtain();
        message.what = MsgDef.MSG_SHOW_SHOPPING_TOP_LIST_WINDOW;
        message.arg1 = cateId;
        MsgDispatcher.getInstance().sendMessage(message);
    }

//    private static void showFeedDetail(int feedType, String articleId) {
//        HomepageFeedDetailInfo info = new HomepageFeedDetailInfo();
//        info.type = feedType;
//        info.articleId = articleId;
//
//        Message message = Message.obtain();
//        message.obj = info;
//        message.what = MsgDef.MSG_SHOW_FUN_GUIDE_DETAIL;
//        MsgDispatcher.getInstance().sendMessage(message);
//    }

    private static void showProductDetail(String id) {
        Message msg = Message.obtain();
        msg.what = MsgDef.MSG_SHOW_GOODS_DETAIL_WINDOW;
        msg.obj = id;
        MsgDispatcher.getInstance().sendMessage(msg);
    }

    private static void showProductTopic(String id, String title) {
        ShoppingTopicInfo topicInfo = new ShoppingTopicInfo();
        topicInfo.columnId = id;
        topicInfo.title = title;
        Message message = Message.obtain();
        message.what = MsgDef.MSG_SHOW_SHOPPING_TOPIC_WINDOW;
        message.obj = topicInfo;
        MsgDispatcher.getInstance().sendMessage(message);
    }
}
