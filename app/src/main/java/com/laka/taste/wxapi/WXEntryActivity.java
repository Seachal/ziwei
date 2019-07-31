package com.laka.taste.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.laka.live.help.EventBusManager;
import com.laka.live.network.DataProvider;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.laka.live.help.SubcriberTag;
import com.laka.live.util.Log;


/**
 * Created by luwies on 16/4/5.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{

    private static final String TAG ="WXEntryActivity";
    private WXHelper mWXHelper;
    public static int type = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWXHelper = new WXHelper(this);
        mWXHelper.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        mWXHelper.handleIntent(intent, this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                break;
            default:
                break;
        }
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        int type = resp.getType();
         switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                Log.d(TAG,"分享成功 type="+type +" resp="+resp.toString());
                DataProvider.postShareCount(this,WXEntryActivity.type);
                EventBusManager.postEvent(2,SubcriberTag.ROOM_SHARE_SUCCESS);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                break;
            default:
                //unknown errcode
                break;
        }

        Log.error("test", "wechat error code = " + resp.errCode);
        if (resp instanceof SendAuth.Resp) {
            EventBusManager.postEvent(resp, SubcriberTag.MSG_AUTH_RESULT);
        }

        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWXHelper.unRegister();
    }
}