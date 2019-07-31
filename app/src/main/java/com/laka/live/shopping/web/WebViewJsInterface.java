
package com.laka.live.shopping.web;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.laka.live.util.StringUtils;
import com.laka.live.util.ThreadManager;

/**
 * Created by zhangwulin on 2016/1/9.
 * email 1501448275@qq.com
 */
public class WebViewJsInterface {
    public static final String COMMAND_SHARE = "share";
    public static final String COMMAND_COLLECT = "collect";
    public static final String COMMAND_GET_IMAGE = "getImage";
    //文章查看更多
    public static final String COMMAND_SEE_MORE = "seeMore";
    public JsInterfaceCallback mCallback;
    private String mArticleId;

    public WebViewJsInterface(JsInterfaceCallback callback, String articleId) {
        mCallback = callback;
        if (!StringUtils.isEmpty(articleId)) {
            mArticleId = articleId;
        } else {
            mArticleId = "0";
        }
    }

    @JavascriptInterface
    public void setImagesResource(final String[] result) {
        if (result == null || result.length == 0) {
            return;
        }
        handleJsCommand(COMMAND_GET_IMAGE, result);
    }

    @JavascriptInterface
    public void actionShare(final String result) {
        ThreadManager.post(ThreadManager.THREAD_UI, new Runnable() {
            @Override
            public void run() {
                if (result == null || result.length() == 0) {
                    return;
                }
                try {
                    // TODO: 2017/7/14 分享
//                    ShareBean shareBean = new Gson().fromJson(result, ShareBean.class);
//                    tryOpenSharePanel(shareBean, mArticleId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @JavascriptInterface
    public void actionCollect(final String result, final String callback) {
        handleJsCommand(COMMAND_COLLECT, result, callback);
    }

    @JavascriptInterface
    public void onSeeMoreTestClick(final String result) {
        ThreadManager.post(ThreadManager.THREAD_UI, new Runnable() {
            @Override
            public void run() {
//                HomepageFeedDetailInfo homepageFeedDetailInfo = new HomepageFeedDetailInfo();
//                homepageFeedDetailInfo.type = HomepageConst.FEED_TYPE_TEST;
//                Message message = Message.obtain();
//                message.what = MsgDef.MSG_HOMEPAGE_SHOW_ARTICLE_LIST;
//                message.obj = homepageFeedDetailInfo;
//                MsgDispatcher.getInstance().sendMessage(message);

//                StatsModel.stats(StatsKeyDef.CONTENT_TEST_MORE_TEST);
            }
        });
    }

    private void handleJsCommand(final String command, final String... result) {
        ThreadManager.post(ThreadManager.THREAD_UI, new Runnable() {
            @Override
            public void run() {
                mCallback.onJsCommand(command, result);
            }
        });
    }

//    private void tryOpenSharePanel(ShareBean shareBean, String articleId) {
//        ShareIntentBuilder builder = ShareIntentBuilder.obtain();
//        builder.setTargetPlatform(shareBean.getTarget());
//        builder.setShareUrl(shareBean.getUrl());
//        builder.setShareSourceType(ShareIntentBuilder.SOURCE_TYPE_SHARE_LINK);
//        builder.setShareMineType(ShareIntentBuilder.MIME_TYPE_TEXT);
//        builder.setShareImageUrl(shareBean.getImageUrl());
//        builder.setShareTitle(shareBean.getTitle());
//        builder.setShareContent(shareBean.getContent());
//
//        Bundle shareBundle = new Bundle();
//        shareBundle.putString("type", WebViewShareSourceTypeHelper.convertType2String(shareBean.getSourceType()));
//        builder.setStatsBundle(shareBundle);
//
//        ShareParameter.title = shareBean.getTitle();
//        ShareParameter.objId = articleId;
//        if (shareBean.getSourceType() == 0) {
//            ShareParameter.objType = ShareRequest.TYPE_ARTICLE;//测试文章
//        } else {
//            ShareParameter.objType = shareBean.getSourceType();
//        }
//
//
//        Message message = Message.obtain();
//        message.what = MsgDef.MSG_SHARE;
//        message.obj = builder.create();
//        MsgDispatcher.getInstance().sendMessage(message);
//    }

    public static void tryLoadGetImageJs(WebView webView) {
        String js = "javascript:(" +
                "function(){" +
                "var objs = document.getElementsByTagName('img');" +
                "var result = new Array();" +
                "for(var i = 0; i<objs.length;i++){" +
                "result[i] = objs[i].getAttribute('data-original');" +
                "};" +
                "window.android.setImagesResource(result);" +
                "})()";
        webView.loadUrl(js);
    }

    public static void tryLoadCallbackJs(WebView webView, String... args) {
        if (args == null || args.length < 1) {
            return;
        }
        String callback = args[0];
        String js = "javascript:(" + callback + "(+" + "))";
        if (args.length > 1) {
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                builder.append(args[i]);
                if (i < args.length - 1) {
                    builder.append(",");
                }
            }
            js = "javascript:(window." + callback + "(" + builder.toString() + "))";
        }
        webView.loadUrl(js);
    }


    public interface JsInterfaceCallback {
        void onJsCommand(String command, String... result);
    }
}
