package com.laka.live.account.income;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.Msg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.taste.wxapi.WXHelper;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;

import org.greenrobot.eventbus.Subscribe;

public class BindWechatActivity extends BaseActivity implements View.OnClickListener,
        GsonHttpConnection.OnResultListener<Msg> {

    private final static int REQUEST_CODE_WITHDRAWAL = 1;

    private boolean isFirst = true;
    private Button mSureBtn;
    private WXHelper mWXHelper;
    private LinearLayout mStepTwoLayout;
    private LinearLayout mStepTwoTipLayout;

    public static void startActivityForResult(Activity activity, int requestCode, float todayCash, float todayRemain, String tips) {
        if (activity != null) {
            Intent intent = new Intent(activity, BindWechatActivity.class);
            intent.putExtra("tips", tips);
            intent.putExtra("today_cash", todayCash);
            intent.putExtra("today_remain", todayRemain);
            ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_wechat);
        init();
    }

    private void init() {
        mStepTwoLayout = (LinearLayout) findViewById(R.id.step_two);
        mStepTwoTipLayout = (LinearLayout) findViewById(R.id.step_two_tip);
        mSureBtn = (Button) findViewById(R.id.sure);
        mSureBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure:
                sure();
                break;
        }
    }

    private void sure() {
        if (isFirst) {
            bind();
            AnalyticsReport.onEvent(this, AnalyticsReport.BIND_WECHAT_ACTIVITY_SURE_BUTTON_CLICK_EVENT_ID);
        } else {
            if (AccountInfoManager.getInstance().checkCurrentAccountIsBindPhone()) {
                if (getIntent() != null) {
                    WithdrawalActivity.startActivityForResult(this, getIntent().getFloatExtra("today_cash", 0.0f),
                            getIntent().getFloatExtra("today_remain", 1000.0f), getIntent().getStringExtra("tips"), REQUEST_CODE_WITHDRAWAL);
                }
//                finish();
            } else {
                //bind手机
                BindPhoneActivity.startActivity(this);
//                finish();
//                overridePendingTransition(0, 0);
            }
        }
    }

    private void bind() {
        showLoadingDialog();
        if (mWXHelper == null) {
            mWXHelper = new WXHelper(this);
        }
        mWXHelper.sendAuthReq();
    }


    @Subscribe
    public void onEvent(PostEvent event) {
        super.onEvent(event);
        if (SubcriberTag.MSG_AUTH_RESULT.equals(event.tag)) {
            SendAuth.Resp resp = (SendAuth.Resp) event.event;
            if (resp != null) {
                int errorCode = resp.errCode;
                if (errorCode == BaseResp.ErrCode.ERR_OK) {
                    DataProvider.bindWechat(this, resp.code, this);
                    showLoadingDialog();
                } else {
                    showToast(R.string.bind_wechat_fail);
                    dismissLoadingsDialog();
                }
            } else {
                showToast(R.string.bind_wechat_fail);
                dismissLoadingsDialog();
            }
        }
    }

    @Override
    public void onSuccess(Msg msg) {
        dismissLoadingsDialog();
        showToast(R.string.bind_wechat_success);
        showSecondStep();
        AccountInfoManager.getInstance().updateCurrentAccountBindWeChatState(true);
    }

    @Override
    public void onFail(int errorCode, String errorMsg, String command) {
        dismissLoadingsDialog();
        showToast(R.string.bind_wechat_fail);
    }

    private void showSecondStep() {
        mStepTwoLayout.setVisibility(View.VISIBLE);
        mStepTwoTipLayout.setVisibility(View.VISIBLE);
        mSureBtn.setText(R.string.bind_success);
        isFirst = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsReport.onEvent(this, AnalyticsReport.BIND_WECHAT_ACTIVITY_SHOW_EVENT_ID);
    }

    @Override
    protected void onStop() {
        super.onStop();
        dismissLoadingsDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mWXHelper != null) {
            mWXHelper.unRegister();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
        }
        finish();
    }
}
