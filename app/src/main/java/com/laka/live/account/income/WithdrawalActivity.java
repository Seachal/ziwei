package com.laka.live.account.income;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.msg.Msg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.widget.InputFrame;
import com.laka.live.ui.widget.dialog.GenericDialog;
import com.laka.live.ui.widget.dialog.IDialogOnClickListener;
import com.laka.live.ui.widget.dialog.SimpleTextDialog;
import com.laka.live.util.NumberUtils;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ToastHelper;

/**
 * Created by luwies on 16/4/15.
 */
public class WithdrawalActivity extends BaseActivity implements View.OnClickListener,
        GsonHttpConnection.OnResultListener<Msg>, TextWatcher {

    public static final String EXTRA_MAX = "EXTRA_MAX";
    public static final String EXTRA_TIP = "EXTRA_TIP";
    public static final String EXTRA_COUNT = "EXTRA_COUNT";

    private InputFrame mInputFrame;
    private Button mSureBtn;
    private boolean hasWithdrawal = false;

    private float mMax;

    public static void startActivityForResult(Activity activity, float max, float count, String tip, int requestCode) {
        if (activity != null) {
            Intent intent = new Intent(activity, WithdrawalActivity.class);
            intent.putExtra(EXTRA_MAX, max);
            intent.putExtra(EXTRA_TIP, tip);
            intent.putExtra(EXTRA_COUNT, count);
            ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        mMax = intent.getFloatExtra(EXTRA_MAX, 0);
        mInputFrame = (InputFrame) findViewById(R.id.input);
        mInputFrame.setHint("本次可转出:"+ NumberUtils.splitDoubleStr(mMax)+"元");
        mInputFrame.setInputType(InputType.TYPE_CLASS_NUMBER);
        String tips = intent.getStringExtra(EXTRA_TIP);
        TextView tipsView = (TextView) findViewById(R.id.today_tip);
        tipsView.setText(tips);
        mSureBtn = (Button) findViewById(R.id.sure);
        mSureBtn.setOnClickListener(this);
        mSureBtn.setEnabled(false);
        float count = intent.getFloatExtra(EXTRA_COUNT, 0);
        if (count > 0) {
            mInputFrame.addTextWatcher(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure:
                sure();
                AnalyticsReport.onEvent(this, AnalyticsReport.WECHAT_WITHDRAWAL_ACTIVITY_SURE_BUTTON_CLICK_EVENT_ID);
                break;
        }
    }

    private void sure() {
        String cash = mInputFrame.getText().toString();
        if (TextUtils.isEmpty(cash)) {
            return;
        }
        int cashCount = StringUtils.parseInt(cash);

        if (cashCount > mMax) {
            ToastHelper.showToast(R.string.today_can_be_withdrawal_amount_not_enough);
            return;
        }

        showTipDialog(cashCount);
    }

    private void showTipDialog(final int cashCount) {
        showButtonDialog(ResourceHelper.getString(R.string.withdrawal_cash_count), "确定提取"+NumberUtils.splitDoubleStr(cashCount)+"元到微信？", R.string.yes, R.string.cancel,
                true, true, true, false, new IDialogOnClickListener() {
                    @Override
                    public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                        if (viewId == GenericDialog.ID_BUTTON_YES) {
                            tryGetCashWithWeiXin(cashCount);
                        }
                        return false;
                    }
                });
//        String tipContent = ResourceHelper.getString(R.string.withdrawal_cash_count_tip, cashCount);
//        SimpleTextDialog dialog = new SimpleTextDialog(this);
//        dialog.setTitle(ResourceHelper.getString(R.string.withdrawal_cash_count));
//        dialog.setText(tipContent);
//        dialog.addYesNoButton();
//        dialog.setCancelable(true);
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.setRecommendButton(GenericDialog.ID_BUTTON_NO);
//        dialog.setOnClickListener(new IDialogOnClickListener() {
//            @Override
//            public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
//                if (viewId == GenericDialog.ID_BUTTON_YES) {
//                    tryGetCashWithWeiXin(cashCount);
//                }
//                return false;
//            }
//        });
//        dialog.show();
    }

    private void tryGetCashWithWeiXin(int cashCount) {
        showLoadingDialog();
        DataProvider.cashWithdrawal(this, cashCount, this);
    }

    @Override
    public void onSuccess(Msg msg) {
        dismissLoadingsDialog();
        ToastHelper.showToast(ResourceHelper.getString(R.string.withdrawal_success));
        mInputFrame.clearEditText();
        hasWithdrawal = true;
        finish();
    }

    @Override
    public void onFail(int errorCode, String errorMsg, String command) {
        dismissLoadingsDialog();

        ToastHelper.showToast(R.string.withdrawal_fail);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int count = 0;
        try {
            count = Integer.valueOf(s.toString());
        } catch (NumberFormatException e) {
        }
        mSureBtn.setEnabled(TextUtils.isEmpty(s) == false && count > 0);
    }

    @Override
    public void finish() {
        if (hasWithdrawal) {
            setResult(RESULT_OK);
        }
        super.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsReport.onEvent(this, AnalyticsReport.WECHAT_WITHDRAWAL_ACTIVITY_SHOW_EVENT_ID);
    }
}
