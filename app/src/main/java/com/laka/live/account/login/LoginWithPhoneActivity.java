package com.laka.live.account.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.UserInfo;
import com.laka.live.msg.Msg;
import com.laka.live.msg.UserMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.widget.AlphaTextView;
import com.laka.live.util.Common;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.UiPreference;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by zwl on 2016/6/27.
 * Email-1501448275@qq.com
 * 手机登录
 */
public class LoginWithPhoneActivity extends BaseActivity implements View.OnClickListener {
    public static final int REQUEST_CODE = str2int("LoginWithPhoneActivity");
    private CountDownTimer codeTimer;
    private Button btnTimer;
    private EditText mPhoneEdit;
    private EditText mPvcEdit;
    private AlphaTextView mGetPcvBtn;
    //    private AlphaTextView mSureBtn;
    private Button mSureBtn;
    private boolean canRegJPushId;

    private static int str2int(String str) {
        int result = 0;
        for (int i = 0; i < str.length(); i++) {
            result += (str.charAt(i));
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_phone_layout);
        initView();
    }

    public static void startLoginWithPhoneActivity(Activity activity) {


        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, LoginWithPhoneActivity.class);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    private void initView() {
        mPhoneEdit = (EditText) findViewById(R.id.phone);
        mPvcEdit = (EditText) findViewById(R.id.pvc);
        mGetPcvBtn = (AlphaTextView) findViewById(R.id.get_pcv);
        mSureBtn = (Button) findViewById(R.id.btn_login);
        mSureBtn.setClickable(false);
        mSureBtn.setOnClickListener(this);
        mGetPcvBtn.setOnClickListener(this);
        btnTimer = (Button) findViewById(R.id.btn_timer);
        btnTimer.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                AnalyticsReport.onEvent(this, AnalyticsReport.PHONE_LOGIN_BUTTON_CLICK_EVENT_ID);
                login();
                break;
            case R.id.get_pcv:
                getPhoneVerifyCode();
                break;
        }
    }

    /**
     * 获取手机验证码
     */
    private void getPhoneVerifyCode() {
        String phone = mPhoneEdit.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            ToastHelper.showToast(R.string.phone_null);
            return;
        }
        showLoadingDialog();
        DataProvider.getPhoneVerifyCode(this, phone, new GsonHttpConnection.OnResultListener<Msg>() {
            @Override
            public void onSuccess(Msg msg) {
                handleOnGetVerifyCodeSuccess();
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                handleOnGetVerifyCodeFailed(errorCode, errorMsg);
            }
        });
    }

    private void handleOnGetVerifyCodeFailed(int errorCode, String errorMsg) {
        dismissLoadingsDialog();
        if (errorCode == ErrorCode.ERROR_CODE_INVALID_PHONE_NUMBER) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.login_error_invalid_phone));
            return;
        }
        ToastHelper.showToast(R.string.get_code_fail);
    }

    private void handleOnGetVerifyCodeSuccess() {
        dismissLoadingsDialog();
        ToastHelper.showToast(R.string.get_code_success);
        mSureBtn.setClickable(true);

        mGetPcvBtn.setVisibility(View.GONE);
        btnTimer.setVisibility(View.VISIBLE);
        codeTimer = new CountDownTimer(1000 * 60, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String time = millisUntilFinished / 1000 + getString(R.string.time_second);
                btnTimer.setText(time);
            }

            @Override
            public void onFinish() {
                mGetPcvBtn.setVisibility(View.VISIBLE);
                btnTimer.setVisibility(View.GONE);
            }
        };
        codeTimer.start();
    }

    private void closeCodeTimer() {
        if (codeTimer == null) {
            return;
        }
        codeTimer.cancel();
        codeTimer = null;
    }

    /**
     * 短信验证码登陆
     */
    private void login() {
        String phone = mPhoneEdit.getText().toString();
        String pvc = mPvcEdit.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            ToastHelper.showToast(R.string.phone_null);
            return;
        }
        if (TextUtils.isEmpty(pvc)) {
            ToastHelper.showToast(R.string.check_code_null);
            return;
        }
        showLoadingDialog();
        setCanRegJPushId();
        DataProvider.loginWithPhone(this, this, phone, pvc, new GsonHttpConnection.OnResultListener<UserMsg>() {
            @Override
            public void onSuccess(UserMsg userMsg) {
                handleOnLoginSuccessResult(userMsg);
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                handleOnLoginFailedResult(errorCode, errorMsg);
            }
        });
    }

    private void handleOnLoginSuccessResult(UserMsg userMsg) {
        dismissLoadingsDialog();
        final UserInfo userInfo = userMsg.getUserInfo();

        if (userInfo == null) {
            ToastHelper.showToast(R.string.login_fail);
            return;
        }

        if (canRegJPushId) {
            UiPreference.putBoolean(Common.HAS_SET_JPUSH_ID, true);
        }
        ToastHelper.showToast(R.string.login_success);

        AccountInfoManager.getInstance().saveAccountInfo(userInfo);
        setResult(RESULT_OK);
        finish();

//        AnalyticsReport.onEvent(LoginWithPhoneActivity.this,
//                AnalyticsReport.PHONE_LOGIN_BUTTON_LOGIN_SUCCESS_EVENT_ID);
        onLoginEvent(R.id.ll_login_phone, "19");
    }

    private void handleOnLoginFailedResult(int errorCode, String errorString) {
        dismissLoadingsDialog();
//        AnalyticsReport.onEvent(LoginWithPhoneActivity.this,
//                AnalyticsReport.PHONE_LOGIN_BUTTON_LOGIN_FAIL_EVENT_ID);
        onLoginEvent(R.id.ll_login_phone, "20");

        if (errorCode == ErrorCode.ERROR_CODE_USER_DENY_LOGIN) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.login_error_limit_login));
            return;
        }
        ToastHelper.showToast(R.string.login_fail);
    }

    private void setCanRegJPushId() {
        String regId = JPushInterface.getRegistrationID(this);
        canRegJPushId = !TextUtils.isEmpty(regId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsReport.onEvent(this, AnalyticsReport.PHONE_LOGIN_SHOW_EVENT_ID);
    }


    private void onLoginEvent(int curLoginType, String request) {
        HashMap<String, String> params = new HashMap<>();
        params.put("Request", request);
        if (curLoginType == R.id.ll_login_weibo) {
            AnalyticsReport.onEvent(this, AnalyticsReport.WEIBO_LOGIN_BUTTON_CLICK_EVENT_ID, params);
        } else if (curLoginType == R.id.ll_login_wechat) {
            AnalyticsReport.onEvent(this, AnalyticsReport.LOGIN_WECHAT_BUTTON_CLICK_EVENT_ID, params);
        } else if (curLoginType == R.id.ll_login_qq) {
            AnalyticsReport.onEvent(this, AnalyticsReport.QQ_LOGIN_BUTTON_CLICK_EVENT_ID, params);
        } else if (curLoginType == R.id.ll_login_phone) {
            AnalyticsReport.onEvent(this, AnalyticsReport.PHONE_LOGIN_BUTTON_CLICK_EVENT_ID, params);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeCodeTimer();
    }
}
