package com.laka.taste.wxapi;


import android.content.Intent;
import android.os.Bundle;

import com.laka.live.R;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.util.Log;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;


public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private WXHelper mWXHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_result);
        mWXHelper = new WXHelper(this);
        mWXHelper.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mWXHelper.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }


    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        Log.d(TAG, "支付resp.errCode=" + resp.errCode + " resp.getType()=" + resp.getType());
        if (resp.errCode == 0) {
            EventBusManager.postEvent(WXHelper.WE_CHAT_PAY_TYPE_SUCCESS, SubcriberTag.MSG_RECHARGE_SUCCESS);
            Log.d(TAG, "充值成功");
        } else if (resp.errCode == -2) {
            EventBusManager.postEvent(WXHelper.WE_CHAT_PAY_TYPE_CANCEL, SubcriberTag.MSG_RECHARGE_SUCCESS);
            Log.d(TAG, "充值取消");
        } else {
            EventBusManager.postEvent(WXHelper.WE_CHAT_PAY_TYPE_FAIL, SubcriberTag.MSG_RECHARGE_SUCCESS);
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWXHelper.unRegister();
    }
}