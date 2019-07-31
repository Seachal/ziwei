package com.laka.live.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.laka.live.BuildConfig;
import com.laka.live.R;
import com.laka.live.application.LiveApplication;
import com.laka.live.bean.UserInfo;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.network.DataProvider;
import com.laka.live.photopreview.FrescoHelper;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.room.WeiboShareActivity;
import com.laka.live.ui.widget.danmu.IBitmapListener;
import com.laka.taste.wxapi.WXEntryActivity;
import com.laka.taste.wxapi.WXHelper;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

import framework.ioc.Ioc;


/**
 * Created by crazyguan on 2016/4/27.
 */
public class ShareUtil {
    public static final int WEIBO_PLATFORM = 1, WEIXIN_PLATFORM = 2, FRIENDSHIP_PLATFORM = 3, QQ_PLATFORM = 4, QZONE_PLATFORM = 5;
    private static final String TAG = "RoomShareUtil";

    /**
     * 微博分享的接口实例
     */
//    private IWeiboShareAPI mWeiboShareAPI;
    public Tencent mTencent;
    public WXHelper wxHelper;
    private static ShareUtil self;
    //    private BaseActivity context;
    public IUiListener mTencentIUiListener;
    public int curShareType = 0;

    public static ShareUtil getInstance() {
        if (self == null) {
            self = new ShareUtil(LiveApplication.getInstance());
//            WXHelper.getInstance().register(context);//微信
//            self.mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(LiveApplication.getInstance(), Common.SINA_WEIBO_APP_KEY);//微博
//            self.mWeiboShareAPI.registerApp();
//            self.mTencent = Tencent.createInstance(Common.QQ_APP_ID, LiveApplication.getInstance());
        }
        return self;
    }

    //    public void setActivity(BaseActivity activity){
//        this.context = activity;
//    }
    public void destory() {
//        if (mWeiboShareAPI != null)
//            mWeiboShareAPI = null;
        mTencent = null;
        wxHelper = null;
        self = null;
    }

    private ShareUtil(final Context context) {
//        this.context = context;
        mTencent = Tencent.createInstance(Common.QQ_APP_ID, context);
        if (wxHelper == null) {
            wxHelper = new WXHelper(context);
        }

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    if (mWeiboShareAPI == null) {
//                        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, Common.SINA_WEIBO_APP_KEY);//微博
//                        // 获取微博客户端相关信息，如是否安装、支持 SDK 的版本
//                        boolean isInstalledWeibo = mWeiboShareAPI.isWeiboAppInstalled();
//                        int supportApiLevel = mWeiboShareAPI.getWeiboAppSupportAPI();
//                        Log.d(TAG, " 微博客户端 isInstalledWeibo=" + isInstalledWeibo + " supportApiLevel=" + supportApiLevel);
//                        boolean isRegister = mWeiboShareAPI.registerApp();
//                        Log.d(TAG, "mWeiboShareAPI isRegister=" + isRegister);
//                    } else {
//                        Log.d(TAG, " mWeiboShareAPI ！=null");
//                    }
//                } catch (Exception e) {
//                    Log.d(TAG, " mWeiboShareAPI 注册失败");
//                }
//            }
//        }).start();
    }

//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 0:
//                    if (context != null)
//                        context.dismissLoadingsDialog();
//                    break;
//            }
//        }
//    };

    /**
     *  分享，没有使用集成分享SDK，而是每个单独实现的
     * @param context
     * @param title
     * @param content
     * @param finalUrl
     * @param iconUrl
     * @param platformName
     */
    public void shareUrl(BaseActivity context,
                         String title, final String content, final String finalUrl, String iconUrl, final int platformName) {
        Log.d(TAG, "shareUrl title=" + title + " url=" + finalUrl + " iconUrl=" + iconUrl + " platformName=" + platformName);

        String url = finalUrl;

        if (BuildConfig.DEBUG) {
            if (url != null) {
                if (url.contains("?")) {
                    url = url + "&v=" + System.currentTimeMillis();
                } else {
                    url = url + "?v=" + System.currentTimeMillis();
                }
            }
        }

        if (url != null) {
            // 增加代理id
            if (!url.contains("proxyUserId")) {
                if (url.contains("?")) {
                    url = url + "&proxyUserId=" + Ioc.get(UserInfo.class).getIdStr();
                } else {
                    url = url + "?proxyUserId=" + Ioc.get(UserInfo.class).getIdStr();
                }
            }
        }

        if (platformName == WEIXIN_PLATFORM)
            WXEntryActivity.type = 4;
        else if (platformName == FRIENDSHIP_PLATFORM)
            WXEntryActivity.type = 1;

        if (platformName == WEIXIN_PLATFORM || platformName == FRIENDSHIP_PLATFORM) {

            //微信好友和朋友圈
//            WXHelper.getInstance().register(context);
            if (wxHelper == null) {
                wxHelper = new WXHelper(LiveApplication.getInstance().getApplicationContext());
            }

            if (!wxHelper.isInstallWeChat()) {
                ToastHelper.showToast(R.string.please_install_wechat);
                return;
            }
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = url;
            final WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = title;
            msg.description = content;
            if (Utils.isEmpty(iconUrl)) {
                Bitmap thumb = BitmapFactory.decodeResource(LiveApplication.getInstance().getResources(), R.mipmap.ic_launcher);
                msg.thumbData = ImageUtil.bmpToByteArray(thumb, true);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("webpage");
                req.message = msg;
                if (platformName == WEIXIN_PLATFORM) {
                    curShareType = 5;
                    req.scene = SendMessageToWX.Req.WXSceneSession;
                } else {
                    curShareType = 2;
                    req.scene = SendMessageToWX.Req.WXSceneTimeline;
                }
//                WXHelper wxHelper = new WXHelper(context);

                wxHelper.sendReq(req);
            } else {
//                if(context!=null){
//                    context.showDialog();
//                }
                ImageUtil.loadImage(iconUrl, new ImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                        Bitmap thumb = BitmapFactory.decodeResource(LiveApplication.getInstance().getResources(), R.mipmap.ic_launcher);
                        msg.thumbData = ImageUtil.bmpToByteArray(thumb, true);
                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = buildTransaction("webpage");
                        req.message = msg;
                        if (platformName == WEIXIN_PLATFORM) {
                            curShareType = 5;
                            req.scene = SendMessageToWX.Req.WXSceneSession;
                        } else {
                            curShareType = 2;
                            req.scene = SendMessageToWX.Req.WXSceneTimeline;
                        }

                        wxHelper.sendReq(req);
                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                        msg.setThumbImage(bitmap);
                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = buildTransaction("webpage");
                        req.message = msg;
                        if (platformName == WEIXIN_PLATFORM) {
                            curShareType = 5;
                            req.scene = SendMessageToWX.Req.WXSceneSession;
                        } else {
                            curShareType = 2;
                            req.scene = SendMessageToWX.Req.WXSceneTimeline;
                        }

//                        WXHelper wxHelper = new WXHelper(context);
                        wxHelper.sendReq(req);

                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });


            }
        } else if (platformName == WEIBO_PLATFORM) {
            //新浪微博
            Log.d(TAG, "分享微博");
            if (context != null) {
                WeiboShareActivity.startActivity(context, title, content, url, iconUrl);
            } else {
                Log.d(TAG, "分享微博 context==null");
            }

//            if (mWeiboShareAPI == null) {
//               ToastHelper.showToast( R.string.weibosdk_error, Toast.LENGTH_SHORT);
//            } else if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
//                final WeiboMessage weiboMessage = new WeiboMessage();
////                weiboMessage.mediaObject = getWebpageObj(title, content, url,iconUrl);
//
//
//                final WebpageObject mediaObject = new WebpageObject();
//                mediaObject.identify = Utility.generateGUID();
//                mediaObject.title = title;
//                mediaObject.description = content;
//                weiboMessage.mediaObject = mediaObject;
//                if (Utils.isEmpty(iconUrl)) {
//                    Bitmap bitmap = BitmapFactory.decodeResource(LiveApplication.getInstance().getResources(), R.mipmap.ic_launcher);
//                    // 设置 Bitmap 类型的图片到视频对象里   设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
//                    doWeiboShare(content, url, weiboMessage, mediaObject, bitmap);
//                } else {
//                    if(context!=null) {
//                        context.showDialog();
//                    }
//                    ImageUtil.getBitmapByUrl( iconUrl, 100, 100, new IBitmapListener() {
//                        @Override
//                        public void onSuccess(Bitmap bitmap) {
//                            mHandler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if(context!=null){
//                                        context.dismissLoadingsDialog();
//                                    }
//                                }
//                            });
//
//                            doWeiboShare(content, url, weiboMessage, mediaObject, bitmap);
//                        }
//
//                        @Override
//                        public void onFail(int code) {
//                            if(context!=null){
//                                context.dismissLoadingsDialog();
//                            }
////                            context.showToast("获取图片失败");
//                            Bitmap bitmap = BitmapFactory.decodeResource(LiveApplication.getInstance().getResources(), R.mipmap.ic_launcher);
//                            // 设置 Bitmap 类型的图片到视频对象里   设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
//                            doWeiboShare(content, url, weiboMessage, mediaObject, bitmap);
//                        }
//                    });
//                }
//            } else {
//               ToastHelper.showToast(R.string.weibosdk_demo_not_support_api_hint, Toast.LENGTH_SHORT);
//            }

        } else if (platformName == QQ_PLATFORM) {
            //QQ
            if (mTencent == null)
                mTencent = Tencent.createInstance(Common.QQ_APP_ID, LiveApplication.getInstance().getApplicationContext());
            curShareType = 3;
            Log.d(TAG, "分享QQ_PLATFORM url=" + url + " iconUrl=" + iconUrl);
            final Bundle params = new Bundle();
//            if (shareType != QQShare.SHARE_TO_QQ_TYPE_IMAGE) {
            params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, content);
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);//QQShare.SHARE_TO_QQ_TYPE_APP

            if (Utils.isEmpty(iconUrl)) {
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, Common.SHARE_IMAGE_URL_DEFAULT);
            } else {
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, iconUrl);
            }

//          }
//            if (shareType == QQShare.SHARE_TO_QQ_TYPE_IMAGE) {
//                params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imageUrl.getText().toString());
//            } else {
//                params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl.getText().toString());
//            }
//            params.putString(shareType == QQShare.SHARE_TO_QQ_TYPE_IMAGE ? QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL
//                    : QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl.getText().toString());
//            params.putString(QQShare.SHARE_TO_QQ_APP_NAME, appName.getText().toString());
//            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, shareType);
//            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, mExtarFlag);
            mTencentIUiListener = new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    DataProvider.postShareCount(this, 2);
                    Log.d(TAG, "onActivityResult shareToQQ 分享成功");
                    EventBusManager.postEvent(3, SubcriberTag.ROOM_SHARE_SUCCESS);
                }

                @Override
                public void onError(UiError uiError) {
                    Log.d(TAG, "onActivityResult shareToQQ 失败 code=" + uiError.errorCode + " message=" + uiError.errorMessage);
                    ToastHelper.showToast(uiError.errorMessage);
                }

                @Override
                public void onCancel() {
                    DataProvider.postShareCount(this, 2);
                    Log.d(TAG, "onActivityResult shareToQQ 取消");
                }
            };
            if (context != null && mTencent != null) {
                mTencent.shareToQQ(context, params, mTencentIUiListener);
            }


        } else if (platformName == QZONE_PLATFORM) {//QZONE
            if (mTencent == null)
                mTencent = Tencent.createInstance(Common.QQ_APP_ID, LiveApplication.getInstance().getApplicationContext());
            curShareType = 4;
            Log.d(TAG, "分享QZone");
            final Bundle params = new Bundle();
            params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);//SHARE_TO_QQ_TYPE_DEFAULT
            params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
            params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, content);
            params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);
            ArrayList<String> pics = new ArrayList<>();
            if (Utils.isEmpty(iconUrl)) {
                pics.add(Common.SHARE_IMAGE_URL_DEFAULT);
                params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, pics);
            } else {
                pics.add(iconUrl);
                params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, pics);
            }
            mTencentIUiListener = new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    DataProvider.postShareCount(this, 3);
                    Log.d(TAG, "onActivityResult shareToQzone 分享成功");
                    EventBusManager.postEvent(3, SubcriberTag.ROOM_SHARE_SUCCESS);
                }

                @Override
                public void onError(UiError uiError) {
                    Log.d(TAG, "onActivityResult shareToQzone 失败");
                    ToastHelper.showToast(uiError.errorMessage);
                }

                @Override
                public void onCancel() {
                    DataProvider.postShareCount(this, 2);
                    Log.d(TAG, "onActivityResult shareToQzone 取消");
                }
            };
            if (context != null && mTencent != null) {
                mTencent.shareToQzone(context, params, mTencentIUiListener);
            }
        } else {
            Log.d(TAG, "不支持的分享平台");
        }
    }

//    private void doWeiboShare(String content, String url, WeiboMessage weiboMessage, WebpageObject mediaObject, Bitmap bitmap) {
//        mediaObject.setThumbImage(bitmap);
//        mediaObject.actionUrl = url;
//        mediaObject.defaultText = content;
//        // 2. 初始化从第三方到微博的消息请求
//        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
//        // 用transaction唯一标识一个请求
//        request.transaction = String.valueOf(System.currentTimeMillis());
//        request.message = weiboMessage;
//        // 3. 发送请求消息到微博，唤起微博分享界面
//        if(context!=null){
//            Log.d(TAG," doWeiboShare  mWeiboShareAPI.sendRequest");
////            mWeiboShareAPI.handleWeiboResponse(null,context);
//            curShareType = 1;
//            mWeiboShareAPI.sendRequest(context, request);
//        }else{
//            Log.d(TAG," doWeiboShare  context=null");
//        }
//    }

//    public void shareWeiboResponse(Intent intent, IWeiboHandler.Response response){
//        if(mWeiboShareAPI!=null)
//        mWeiboShareAPI.handleWeiboResponse(intent,response);
//    }

    //wx
    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    //weibo

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
//    private WebpageObject getWebpageObj(String title, String description, String url,String iconUrl) {
//
//    }
    public String getShareTitle(Context context) {
//        String shareTitle = mTitleEdit.getText().toString();
//        if (Utils.isEmpty(shareTitle)) {
//            shareTitle = getResources().getString(R.string.i_in_laka_live);
//        }
//        String shareTitle = ResourceHelper.getString(R.string.share_room_default_title);
        String[] items = context.getResources().getStringArray(R.array.share_default_title);
        int index = (int) Math.round(Math.random() * (items.length - 1));
        Log.d(TAG, " getShareTitle index=" + index);
        String shareTitle = items[index];
        return shareTitle;
    }

    public String getShareContent(Context context) {
        String[] items = context.getResources().getStringArray(R.array.share_default_title);
        int index = (int) Math.round(Math.random() * (items.length - 1));
        Log.d(TAG, " getShareTitle index=" + index);
        String shareTitle = items[index];
        return shareTitle;
    }
}
