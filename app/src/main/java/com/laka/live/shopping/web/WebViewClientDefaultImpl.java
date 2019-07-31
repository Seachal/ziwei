package com.laka.live.shopping.web;

import android.content.Context;
import android.os.Message;

import com.laka.live.shopping.activity.ShoppingGoodsDetailActivity;
import com.laka.live.shopping.framework.MsgDispatcher;
import com.laka.live.shopping.framework.adapter.MsgDef;
import com.laka.live.shopping.model.ShoppingTopicInfo;
import com.laka.live.util.StringUtils;

/**
 * Created by gangqing on 2016/3/28.
 * Email:denggangqing@ta2she.com
 */
public class WebViewClientDefaultImpl extends WebViewClientBaseImpl implements WebViewClientBaseImpl.WebViewClientCallback {
    private Context mContext;
    private WebViewClientAddCallback mAddCallback;

    public WebViewClientDefaultImpl(Context context) {
        super();
        mContext = context;
    }

    public void setWebViewClientAddCallback(WebViewClientAddCallback callback) {
        mAddCallback = callback;
    }

    @Override
    public WebViewClientCallback getCallback() {
        return this;
    }


    @Override
    public void onUrlTypeLoading(int urlType, String value) {
        switch (urlType) {
            case WebViewConst.URL_TYPE_ARTICLE:
//                goToFunGuideDetailWindow(value);
                break;
            case WebViewConst.URL_TYPE_ARTICLE_PRODUCT:
//                goToFunGuideDetailWindow(value);
                break;
            case WebViewConst.URL_TYPE_GOODS:
                goToGoodsDetail(value);
                break;
            case WebViewConst.URL_TYPE_POST:
                goToPostDetail(value);
                break;
            case WebViewConst.URL_TYPE_CIRCLE:
//                goToCircleDetail(value, CommunityConstant.CIRCLE_TYPE_POST);
                break;
            case WebViewConst.URL_TYPE_TA_CIRCLE:
//                goToCircleDetail(value, CommunityConstant.CIRCLE_TYPE_QA_POST);
                break;
            case WebViewConst.URL_TYPE_ARTICLE_TEST:
//                goToFunGuideDetailWindow(value);
                break;
            case WebViewConst.URL_TYPE_GOODS_CATEGORY:
                gotoGoodsCategory(value);
                break;
            case WebViewConst.URL_TYPE_GOODS_TOPIC:
                gotoGoodsTopic(value);
                break;
            case WebViewConst.URL_TYPE_MALL_CART:
                gotoMallCart();
                break;
            case WebViewConst.URL_TYPE_MY_COLLECTION:
                gotoMyCollection();
                break;
            case WebViewConst.URL_TYPE_MY_ORDER:
                gotoMyOrder();
                break;
            case WebViewConst.URL_TYPE_MY_SAYING:
//                gotoMySaying();
                break;
            case WebViewConst.URL_TYPE_MY_TESTING:
                gotoMyTesting();
                break;
            case WebViewConst.URL_TYPE_PAPA_RECORD:
                gotoPapaRecord();
                break;
            case WebViewConst.URL_TYPE_KNOWLEDGE_LIST:
                gotoKnowledge();
                break;
            case WebViewConst.URL_TYPE_BBS_PAGE:
                gotoBbsPage();
                break;
            case WebViewConst.URL_TYPE_HOME_PAGE:
//                goToFunGuideDetailWindow(value);
                break;
            case WebViewConst.URL_TYPE_MALL_PAGE:
                gotoMallPage();
                break;
            case WebViewConst.URL_TYPE_ARTICLE_PIC:
//                goToFunGuideDetailWindow(value);
                break;
            case WebViewConst.URL_TYPE_ARTICLE_PRODUCT_TOPIC:
//                goToFunGuideDetailWindow(value);
                break;
            case WebViewConst.URL_TYPE_KNOWLEDGE_ARTICLE:
//                gotoKnowledgeArticle(value);
                break;
            case WebViewConst.URL_TYPE_USER_INFO:
                gotoUserInfo(value);
                break;
            case WebViewConst.URL_TYPE_HOME_MALL:
                gotoHomeMallPage();
                break;
            case WebViewConst.URL_TYPE_TA_COIN:
                gotoaCoinPage();
                break;
            case WebViewConst.URL_TYPE_EVA_POST_PAGE:
//                gotoEvaluate();
                break;
            case WebViewConst.URL_TYPE_HOT_POST_LIST_PAGE:
//                gotoHotPostListPage();
                break;
        }
        if (mAddCallback != null) {
            mAddCallback.onAddUrlTypeLoading(urlType, value);
        }
    }

    @Override
    public void onPageStarted(String url) {
        if (mAddCallback != null) {
            mAddCallback.onPageStarted(url);
        }
    }

    @Override
    public void onPageFinished(String url) {
        if (mAddCallback != null) {
            mAddCallback.onPageFinished(url);
        }
    }

    @Override
    public void onReceivedError() {
        if (mAddCallback != null) {
            mAddCallback.onReceivedError();
        }
    }

//    private void gotoKnowledgeArticle(String id) {
//        KnowledgeDetailInfo info = new KnowledgeDetailInfo();
//        info.articleId = StringUtils.parseInt(id);
//        Message msg = Message.obtain();
//        msg.obj = info;
//        msg.what = MsgDef.MSG_SHOW_KNOWLEDGE_DETAIL_WINDOW;
//        MsgDispatcher.getInstance().sendMessage(msg);
//    }

    private void gotoUserInfo(String id) {
        if (StringUtils.isEmpty(id)) {
            return;
        }
        Message message = Message.obtain();
        message.what = MsgDef.MSG_SHOW_USER_INFO_WINDOW;
        message.arg1 = StringUtils.parseInt(id);
        MsgDispatcher.getInstance().sendMessage(message);
    }

    /**
     * 测评列表页
     */
//    private void gotoEvaluate() {
//        EvaluateWindowParams helper = new EvaluateWindowParams();
//        helper.fromType = EvaluateWindowParams.FROM_TYPE_DEFAULT;
//        Message message = new Message();
//        message.what = MsgDef.MSG_START_EVALUATE;
//        message.obj = helper;
//        MsgDispatcher.getInstance().sendMessage(message);
//    }

    /**
     * 啪啪记录
     */
    private void gotoPapaRecord() {
        MsgDispatcher.getInstance().sendMessage(MsgDef.MSG_SHOW_RECORD_WINDOW);
    }

    /**
     * 涨姿势
     */
    private void gotoKnowledge() {
        MsgDispatcher.getInstance().sendMessage(MsgDef.MSG_SHOW_KNOWLEDGE_MAIN_WINDOW);
    }

    /**
     * 社区
     */
    private void gotoBbsPage() {
        MsgDispatcher.getInstance().sendMessage(MsgDef.MSG_SHOW_COMMUNITY_TAB);
    }

    /**
     * ta币页面
     */
    private void gotoaCoinPage() {
        MsgDispatcher.getInstance().sendMessage(MsgDef.MSG_SHOW_TA_COIN_MAIN_WINDOW);
    }

    /**
     * 商城首页
     */
    private void gotoHomeMallPage() {
        MsgDispatcher.getInstance().sendMessage(MsgDef.MSG_SHOW_SHOPPING_TAB);
    }

    /**
     * 社区热帖页面
     */
//    private void gotoHotPostListPage() {
//        NotificationCenter.getInstance().
//                notify(Notification.obtain(NotificationDef.N_COMMUNITY_HOT_POST_LIST_PAGE));
//    }

    /**
     * 首页
     */
    private void gotoHomePage() {
        MsgDispatcher.getInstance().sendMessage(MsgDef.MSG_SHOW_GUIDE_TAB);
    }

    /**
     * 商品
     */
    private void gotoMallPage() {
        MsgDispatcher.getInstance().sendMessage(MsgDef.MSG_SHOW_SHOPPING_CATEGORIES_WINDOW);
    }

    /**
     * 跳转文章详情
     *
     * @param articleId
     * @param type
     */
//    private void goToFeedDetail(String articleId, int type) {
//        HomepageFeedDetailInfo info = new HomepageFeedDetailInfo();
//        info.articleId = articleId;
//        info.type = type;
//        Message msg = Message.obtain();
//        msg.what = MsgDef.MSG_HOMEPAGE_SHOW_ARTICLE_DETAIL;
//        msg.obj = info;
//        MsgDispatcher.getInstance().sendMessage(msg);
//    }

    /**
     * 跳转文章详情
     *
     * @param param
     */
//    private void goToFunGuideDetailWindow(String param) {
//        if (StringUtils.isEmpty(param)) {
//            return;
//        }
//        try {
//            String decodeValue = URLDecoder.decode(param, "UTF-8");
//            param = decodeValue;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        String articleId = param;
//        HomepageFeedDetailInfo detailInfo = new HomepageFeedDetailInfo();
//        detailInfo.type = 0;
//        detailInfo.articleId = articleId;
//        Message msg = Message.obtain();
//        msg.what = MsgDef.MSG_SHOW_FUN_GUIDE_DETAIL;
//        msg.obj = detailInfo;
//        MsgDispatcher.getInstance().sendMessage(msg);
//    }

    /**
     * 跳转商品详情
     *
     * @param goodsId
     */
    private void goToGoodsDetail(String goodsId) {
        ShoppingGoodsDetailActivity.startActivity(mContext, Integer.parseInt(goodsId));
    }

    /**
     * 跳转帖子详情
     *
     * @param postId
     */
    private void goToPostDetail(String postId) {
        Message message = Message.obtain();
        message.what = MsgDef.MSG_SHOW_COMMUNITY_POST_DETAIL_WINDOW;
        message.arg1 = StringUtils.parseInt(postId);
        MsgDispatcher.getInstance().sendMessage(message);
    }

    /**
     * 跳转圈子详情
     *
     * @param postId
     */
//    private void goToCircleDetail(String postId, int type) {
//        CommunityOpenCircleDetailHelperBean helperBean = new CommunityOpenCircleDetailHelperBean();
//        helperBean.setCircleId(StringUtils.parseInt(postId));
//        helperBean.setCircleType(type);
//        Message message = Message.obtain();
//        message.what = MsgDef.MSG_SHOW_COMMUNITY_CIRCLES_DETAIL_WINDOW;
//        message.obj = helperBean;
//        MsgDispatcher.getInstance().sendMessage(message);
//    }

    /**
     * 商品分类列表
     */
    private void gotoGoodsCategory(String categoryId) {
//        ShoppingCategoryInfo categoryInfo = new ShoppingCategoryInfo();
//        Message message = Message.obtain();
//        message.what = MsgDef.MSG_SHOW_SHOPPING_CATEGORY_WINDOW;
//        message.obj = categoryInfo;
//        MsgDispatcher.getInstance().sendMessage(message);
    }

    /**
     * 我的测试
     */
    private void gotoMyTesting() {
        MsgDispatcher.getInstance().sendMessage(MsgDef.MSG_SHOW_TEST_RESULT_WINDOW);
    }

    /**
     * 我的发言
     */
//    private void gotoMySaying() {
//        Message message = Message.obtain();
//        message.what = MsgDef.MSG_SHOW_COMMUNITY_MY_POST_WINDOW;
//        message.arg1 = CommunityConstant.CIRCLE_TYPE_POST;
//        MsgDispatcher.getInstance().sendMessage(message);
//    }

    /**
     * 全部订单
     */
    private void gotoMyOrder() {
        MsgDispatcher.getInstance().sendMessage(MsgDef.MSG_SHOW_ORDER_LIST_WINDOW);
    }

    /**
     * 我的收藏
     */
    private void gotoMyCollection() {
        MsgDispatcher.getInstance().sendMessage(MsgDef.MSG_SHOW_COLLECT_WINDOW);
    }

    /**
     * 购物车
     */
    private void gotoMallCart() {
        MsgDispatcher.getInstance().sendMessage(MsgDef.MSG_SHOW_SHOPPING_CAR_WINDOW);
    }

    /**
     * 商品专题
     */
    private void gotoGoodsTopic(String goodsId) {
        ShoppingTopicInfo topicInfo = new ShoppingTopicInfo();
        topicInfo.columnId = goodsId;
        Message message = Message.obtain();
        message.what = MsgDef.MSG_SHOW_SHOPPING_TOPIC_WINDOW;
        message.obj = topicInfo;
        MsgDispatcher.getInstance().sendMessage(message);
    }

    public interface WebViewClientAddCallback {
        void onAddUrlTypeLoading(int urlType, String value);

        void onPageStarted(String url);

        void onPageFinished(String url);

        void onReceivedError();
    }
}
