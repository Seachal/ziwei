package com.laka.taste.wxapi;

import android.content.Context;
import android.content.Intent;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.laka.live.util.Common;

/**
 * Created by luwies on 16/4/5.
 */
public class WXHelper {

    public static final int WE_CHAT_PAY_TYPE_SUCCESS = 0;
    public static final int WE_CHAT_PAY_TYPE_FAIL = 1;
    public static final int WE_CHAT_PAY_TYPE_CANCEL = 2;

    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI mApi;

    public WXHelper(Context context) {
        register(context);
    }

    public void register(Context context) {
        if (mApi == null) {
            // 通过WXAPIFactory工厂，获取IWXAPI的实例
            mApi = WXAPIFactory.createWXAPI(context, Common.WECHAT_SDK_APP_ID, false);
        }
        mApi.registerApp(Common.WECHAT_SDK_APP_ID);
    }

    public void unRegister() {
        if (mApi != null) {
            mApi.unregisterApp();
            mApi.detach();
            mApi = null;
        }
    }

    public void handleIntent(Intent intent, IWXAPIEventHandler handler) {
        mApi.handleIntent(intent, handler);
    }

    public void sendAuthReq() {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_laka_live";
        mApi.sendReq(req);
    }

    public void sendPayReq(PayReq payReq) {
        mApi.sendReq(payReq);
    }

    public boolean isInstallWeChat() {
        return mApi.isWXAppInstalled();
    }

    public void sendReq(SendMessageToWX.Req req) {
        mApi.sendReq(req);
    }

}
