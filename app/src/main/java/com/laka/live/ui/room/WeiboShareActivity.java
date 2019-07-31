package com.laka.live.ui.room;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.laka.live.R;
import com.laka.live.application.LiveApplication;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.NoDoubleClickManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.network.DataProvider;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.widget.Toast;
import com.laka.live.ui.widget.danmu.IBitmapListener;
import com.laka.live.util.Common;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.utils.Utility;

/**
 * Created by ios on 16/10/19.
 */

public class WeiboShareActivity extends BaseActivity implements IWeiboHandler.Response {

    private static final String TAG = "WeiboShareActivity";
    /**
     * 微博微博分享接口实例
     */
    private IWeiboShareAPI mWeiboShareAPI = null;


    private String title = "";//标题
    private String content = "";//描述
    private String url = "";//分享链接
    private String iconUrl = "";//分享链接
    private int curShareType;

    public static void startActivity(Context context, String title, String content, String url, String iconUrl) {

        if (!NoDoubleClickManager.isNoDoubleClick(NoDoubleClickManager.TYPE_SHARE_WEIBO)) {

            return;
        }

        if (context != null) {
            Intent intent = new Intent(context, WeiboShareActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("content", content);
            intent.putExtra("url", url);
            intent.putExtra("iconUrl", iconUrl);
            context.startActivity(intent);
        }
    }
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                        dismissLoadingsDialog();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weibo_share);
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        url = getIntent().getStringExtra("url");
        iconUrl = getIntent().getStringExtra("iconUrl");
        // 创建微博分享接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Common.SINA_WEIBO_APP_KEY);

        // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
        // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
        // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
        mWeiboShareAPI.registerApp();

        // 当 Activity 被重新初始化时（该 Activity 处于后台时，可能会由于内存不足被杀掉了），
        // 需要调用 {@link IWeiboShareAPI#handleWeiboResponse} 来接收微博客户端返回的数据。
        // 执行成功，返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
        // 失败返回 false，不调用上述回调
        if (savedInstanceState != null) {
            mWeiboShareAPI.handleWeiboResponse(getIntent(),
                    WeiboShareActivity.this);
        }

//        sendMultiMessage(title, description,url);
        share();
    }

    private void share() {
        if (mWeiboShareAPI == null) {
            ToastHelper.showToast(R.string.weibosdk_error, Toast.LENGTH_SHORT);
        } else if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
            final WeiboMessage weiboMessage = new WeiboMessage();
//                weiboMessage.mediaObject = getWebpageObj(title, content, url,iconUrl);

            final WebpageObject mediaObject = new WebpageObject();
            mediaObject.identify = Utility.generateGUID();
            mediaObject.title = title;
            mediaObject.description = content;
            weiboMessage.mediaObject = mediaObject;
            if (Utils.isEmpty(iconUrl)) {
                Bitmap bitmap = BitmapFactory.decodeResource(LiveApplication.getInstance().getResources(), R.mipmap.ic_launcher);
                // 设置 Bitmap 类型的图片到视频对象里   设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
                doWeiboShare(content, url, weiboMessage, mediaObject, bitmap);
            } else {
               showLoadingDialog();
                ImageUtil.getBitmapByUrl(iconUrl, 100, 100, new IBitmapListener() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        mHandler.sendEmptyMessage(0);

                        doWeiboShare(content, url, weiboMessage, mediaObject, bitmap);
                    }

                    @Override
                    public void onFail(int code) {
                        mHandler.sendEmptyMessage(0);
//                            context.showToast("获取图片失败");
                        Bitmap bitmap = BitmapFactory.decodeResource(LiveApplication.getInstance().getResources(), R.mipmap.ic_launcher);
                        // 设置 Bitmap 类型的图片到视频对象里   设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
                        doWeiboShare(content, url, weiboMessage, mediaObject, bitmap);
                    }
                });
            }
        } else {
            ToastHelper.showToast(R.string.weibosdk_demo_not_support_api_hint, Toast.LENGTH_SHORT);
            finish();
        }
    }

    private void doWeiboShare(String content, String url, WeiboMessage weiboMessage, WebpageObject mediaObject, Bitmap bitmap) {
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = url;
        mediaObject.defaultText = content;
        // 2. 初始化从第三方到微博的消息请求
        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.message = weiboMessage;
        // 3. 发送请求消息到微博，唤起微博分享界面
            Log.d(TAG, " doWeiboShare  mWeiboShareAPI.sendRequest");
//            mWeiboShareAPI.handleWeiboResponse(null,context);
            curShareType = 1;
            mWeiboShareAPI.sendRequest(this, request);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        mWeiboShareAPI.handleWeiboResponse(intent, WeiboShareActivity.this);
    }

    @Override
    public void onResponse(BaseResponse baseResp) {
        switch (baseResp.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                Log.d(TAG, "分享微博成功 status=" + 1);
                DataProvider.postShareCount(this,0);
//                Toast.makeText(WeiboShareActivity.this, "分享成功！",
//                        Toast.LENGTH_LONG).show();
                EventBusManager.postEvent(1, SubcriberTag.ROOM_SHARE_SUCCESS);
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                Log.d(TAG, "分享取消 status=" + 1);
//                Toast.makeText(WeiboShareActivity.this, "取消分享",
//                        Toast.LENGTH_LONG).show();
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                Log.d(TAG, "分享失败 status=" + 1);
//                Toast.makeText(WeiboShareActivity.this, "分享错误", Toast.LENGTH_SHORT).show();
                break;
        }
        finish();
    }


    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 注意：当 {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 时，支持同时分享多条消息，
     * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
     *
     * @param hasText    分享的内容是否有文本
     * @param hasImage   分享的内容是否有图片
     * @param hasWebpage 分享的内容是否有网页
     * @param hasMusic   分享的内容是否有音乐
     * @param hasVideo   分享的内容是否有视频
     * @param hasVoice   分享的内容是否有声音
     */

//    private void sendMultiMessage(String ShareTitle, String ShareDescription,String Url) {
//
//        // 1. 初始化微博的分享消息
//        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
//        weiboMessage.textObject = getTextObj(ShareTitle+ShareDescription+Url);
//        weiboMessage.imageObject = getImageObj();
//
//        // 用户可以分享其它媒体资源（网页、音乐、视频、声音中的一种）
//        //      weiboMessage.mediaObject = getWebpageObj(ShareTitle,ShareDescription, Url);
//
//        // 2. 初始化从第三方到微博的消息请求
//        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
//        // 用transaction唯一标识一个请求
//        request.transaction = String.valueOf(System.currentTimeMillis());
//        request.multiMessage = weiboMessage;
//
//
//
//        mWeiboShareAPI.sendRequest(WeiboShareActivity.this, request);//唤起微博客户端回调接口
//
//
//    }

}
