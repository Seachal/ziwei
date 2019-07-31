package com.laka.live.account.income;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.msg.Msg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;

public class BindPhoneActivity extends BaseActivity implements View.OnClickListener {

    private final static int PHONE_NUMBER_SIZE = 11;
    private final static int PHONE_VERIFY_CODE_SIZE = 4;
    private final static long MILLIS_IN_FUTURE = 60000;
    private final static long COUNT_DOWN_INTERVAL = 1000;

    private EditText mPhoneInput;
    private EditText mPcvInput;
    private TextView mGetPvcText;
    private CountDownTimer mCountDownTimer;
    private Button mSureBtn;

    public static void startActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, BindPhoneActivity.class);
            ActivityCompat.startActivity(activity, intent, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        init();
    }

    private void init() {
        mPhoneInput = (EditText) findViewById(R.id.phone_input);
        mPhoneInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateBtn();
            }
        });
        mPcvInput = (EditText) findViewById(R.id.verify_code);
        mPcvInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateBtn();
            }
        });
        mGetPvcText = (TextView) findViewById(R.id.get_pcv_tip);
        mSureBtn = (Button) findViewById(R.id.sure);
        mSureBtn.setOnClickListener(this);
        mGetPvcText.setOnClickListener(this);
    }

    private void updateBtn() {
        String phone = mPhoneInput.getText().toString();
        String pvc = mPcvInput.getText().toString();
        mSureBtn.setEnabled((!TextUtils.isEmpty(phone)) && (!TextUtils.isEmpty(pvc)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_pcv_tip:
                getPvc();
                AnalyticsReport.onEvent(this, AnalyticsReport.BIND_PHONE_ACTIVITY_GET_PVC_BUTTON_CLICK_EVENT_ID);
                break;
            case R.id.sure:
                bind();
                AnalyticsReport.onEvent(this, AnalyticsReport.BIND_PHONE_ACTIVITY_SURE_BUTTON_CLICK_EVENT_ID);
                break;
            case R.id.phone_input:
                AnalyticsReport.onEvent(this, AnalyticsReport.BIND_PHONE_ACTIVITY_PHONE_INPUT_CLICK_EVENT_ID);
                break;
            case R.id.verify_code:
                AnalyticsReport.onEvent(this, AnalyticsReport.BIND_PHONE_ACTIVITY_PVC_INPUT_CLICK_EVENTW_ID);
                break;
        }
    }

    private void getPvc() {
        String phone = mPhoneInput.getText().toString().trim();
        if (TextUtils.isEmpty(phone) || phone.length() != PHONE_NUMBER_SIZE) {
            showToast(R.string.error_phone_number_tips);
            return;
        }
        mGetPvcText.setEnabled(false);
        showLoadingDialog();
        DataProvider.getPhoneVerifyCode(this, phone, new GsonHttpConnection.OnResultListener<Msg>() {
            @Override
            public void onSuccess(Msg msg) {
                dismissLoadingsDialog();
                startCountDownTimer();

            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                dismissLoadingsDialog();
                showToast(R.string.get_pvc_error);
                mGetPvcText.setEnabled(true);
            }
        });
    }

    private void startCountDownTimer() {
        mCountDownTimer = new CountDownTimer(MILLIS_IN_FUTURE, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                mGetPvcText.setText(getString(R.string.get_pvc_after_seconds, (int) millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                mGetPvcText.setEnabled(true);
                mGetPvcText.setText(R.string.get_pvc);
            }
        };
        mCountDownTimer.start();
    }


    private void bind() {
        final String phone = mPhoneInput.getText().toString().trim();
        String pvc = mPcvInput.getText().toString().trim();

        if (TextUtils.isEmpty(phone) || phone.length() != PHONE_NUMBER_SIZE) {
            showToast(R.string.error_phone_number_tips);
            return;
        }

        if (TextUtils.isEmpty(pvc) || pvc.length() != PHONE_VERIFY_CODE_SIZE) {
            showToast(R.string.error_phone_verify_code_tips);
            return;
        }

        showLoadingDialog();
        DataProvider.bindPhone(this, phone, pvc, new GsonHttpConnection.OnResultListener<Msg>() {
            @Override
            public void onSuccess(Msg msg) {
                dismissLoadingsDialog();
                AccountInfoManager.getInstance().updateCurrentAccountBindPhoneState(true);
                showToast(R.string.bind_phone_success);
                finish();
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                dismissLoadingsDialog();
                if (errorCode == Msg.TLV_WRONG_PHONE_NUMBER_OR_WRONG_PHONE_VERIFICATION_CODE) {
                    showToast(R.string.pvc_error);
                } else {
                    showToast(R.string.bind_phone_fail);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsReport.onEvent(this, AnalyticsReport.BIND_PHONE_ACTIVITY_SHOW_EVENT_ID);
    }
}
