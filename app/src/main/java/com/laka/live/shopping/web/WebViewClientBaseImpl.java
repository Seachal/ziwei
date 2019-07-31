package com.laka.live.shopping.web;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import java.util.HashMap;
import java.util.Map;

import framework.utils.FileUtil;


/**
 * Created by zhxu on 2015/11/20.
 * Email:357599859@qq.com
 */
public abstract class WebViewClientBaseImpl extends WebViewClient {

    private WebViewClientCallback mCallback;

    public WebViewClientBaseImpl() {
        mCallback = getCallback();
    }

    public abstract WebViewClientCallback getCallback();

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (mCallback != null) {
            mCallback.onPageStarted(url);
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (mCallback != null) {
            mCallback.onPageFinished(url);
        }
        WebViewJsInterface.tryLoadGetImageJs(view);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        if (mCallback != null) {
            mCallback.onReceivedError();
        }
    }

    private Map<String, String> getParameter(String url) {
        Map<String, String> params = new HashMap<String, String>();
        // http://api.ta2she.com/home.php?a=jumpLink&type=tags&id=32&title=分类标题
        String str = url.substring(url.indexOf("?") + 1);
        String[] ary = str.split("&");
        for (int i = 0; i < ary.length; i++) {
            String[] param = ary[i].split("=");
            if (param.length == 2) {
                params.put(param[0], param[1]);
            }
        }
        return params;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        int urlType = -1;
        String extension = FileUtil.getExtension(url);
        if (extension != null) {
            if (extension.equalsIgnoreCase("gif") || extension.equalsIgnoreCase("jpg") || extension
                    .equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("png")) {
                mCallback.onUrlTypeLoading(WebViewConst.URL_TYPE_IMAGE, url);
                return true;
            }
        }

        Map<String, String> param = getParameter(url);
        if (!"jumpLink".equals(param.get("a"))) {
            return super.shouldOverrideUrlLoading(view, url);
        }
        String type = param.get("type");

        if ("goods".equals(type)) {
            urlType = WebViewConst.URL_TYPE_GOODS;
        } else if ("post".equals(type)) {
            urlType = WebViewConst.URL_TYPE_POST;
        } else if ("circle".equals(type)) {
            urlType = WebViewConst.URL_TYPE_CIRCLE;
        } else if ("commonArticle".equals(type)) {
            urlType = WebViewConst.URL_TYPE_ARTICLE;
        } else if ("testArticle".equals(type)) {
            urlType = WebViewConst.URL_TYPE_ARTICLE_TEST;
        } else if ("taCircle".equals(type)) {
            urlType = WebViewConst.URL_TYPE_TA_CIRCLE;
        } else if ("taUserInfo".equals(type)) {
            urlType = WebViewConst.URL_TYPE_USER_INFO;
        } else if ("guideArticle".equals(type)) {
            urlType = WebViewConst.URL_TYPE_ARTICLE_PRODUCT;
        } else if ("tags".equals(type)) {
            urlType = WebViewConst.URL_TYPE_TAGS;
        } else if ("goodsCategory".equals(type)) {
            urlType = WebViewConst.URL_TYPE_GOODS_CATEGORY;
        } else if ("goodsTopic".equals(type)) {
            urlType = WebViewConst.URL_TYPE_GOODS_TOPIC;
        } else if ("mallCart".equals(type)) {
            urlType = WebViewConst.URL_TYPE_MALL_CART;
        } else if ("myCollection".equals(type)) {
            urlType = WebViewConst.URL_TYPE_MY_COLLECTION;
        } else if ("myOrder".equals(type)) {
            urlType = WebViewConst.URL_TYPE_MY_ORDER;
        } else if ("mySaying".equals(type)) {
            urlType = WebViewConst.URL_TYPE_MY_SAYING;
        } else if ("myTesting".equals(type)) {
            urlType = WebViewConst.URL_TYPE_MY_TESTING;
        } else if ("papaRecord".equals(type)) {
            urlType = WebViewConst.URL_TYPE_PAPA_RECORD;
        } else if ("knowledgeList".equals(type)) {
            urlType = WebViewConst.URL_TYPE_KNOWLEDGE_LIST;
        } else if ("bbsPage".equals(type)) {
            urlType = WebViewConst.URL_TYPE_BBS_PAGE;
        } else if ("homePage".equals(type)) {
            urlType = WebViewConst.URL_TYPE_HOME_PAGE;
        } else if ("mallPage".equals(type)) {
            urlType = WebViewConst.URL_TYPE_MALL_PAGE;
        } else if ("knowledgeArticle".equals(type)) {
            urlType = WebViewConst.URL_TYPE_KNOWLEDGE_ARTICLE;
        } else if ("imageArticle".equals(type)) {
            urlType = WebViewConst.URL_TYPE_ARTICLE_PIC;
        } else if ("goodsArticle".equals(type)) {
            urlType = WebViewConst.URL_TYPE_ARTICLE_PRODUCT_TOPIC;
        } else if ("articleCommentsPage".equals(type)) {
            urlType = WebViewConst.URL_TYPE_ARTICLE_COMMENT;
        } else if ("homeMallPage".equals(type)) {
            urlType = WebViewConst.URL_TYPE_HOME_MALL;
        } else if ("taCoinPage".equals(type)) {
            urlType = WebViewConst.URL_TYPE_TA_COIN;
        } else if ("evaPostPage".equals(type)) {
            urlType = WebViewConst.URL_TYPE_EVA_POST_PAGE;
        } else if ("hotPostListPage".equals(type)) {
            urlType = WebViewConst.URL_TYPE_HOT_POST_LIST_PAGE;
        }

        if (urlType != -1) {
            if (mCallback != null) {
                if (urlType == WebViewConst.URL_TYPE_TAGS) {
                    String id = param.get("id");
                    String title = param.get("title");
                    String params = id + "&" + title;
                    mCallback.onUrlTypeLoading(urlType, params);
                } else {
                    String id = param.get("id");
                    mCallback.onUrlTypeLoading(urlType, id);
                }
                return true;
            }
        }

        return super.shouldOverrideUrlLoading(view, url);
    }

    public interface WebViewClientCallback {
        void onUrlTypeLoading(int urlType, String value);

        void onPageStarted(String url);

        void onPageFinished(String url);

        void onReceivedError();
    }
}
