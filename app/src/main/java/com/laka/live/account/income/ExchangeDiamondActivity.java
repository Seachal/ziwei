package com.laka.live.account.income;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.msg.Msg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.widget.InputFrame;
import com.laka.live.ui.widget.dialog.GenericDialog;
import com.laka.live.ui.widget.dialog.IDialogOnClickListener;
import com.laka.live.util.NumberUtils;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ToastHelper;

/**
 * 兑换砖石
 */
public class ExchangeDiamondActivity extends BaseActivity implements View.OnClickListener,
        GsonHttpConnection.OnResultListener<Msg>, TextWatcher {

    private final static String EXTRA_MAX = "extra_max";

    private InputFrame mInput;
    private float mMax;
    private long mLastNumber;
    private Button mSureBtn;
    private boolean hasExchange = false;

    public static void startActivityForResult(Activity activity, float max, int reauestCode) {
        if (activity != null) {
            Intent intent = new Intent(activity, ExchangeDiamondActivity.class);
            intent.putExtra(EXTRA_MAX, max);
            ActivityCompat.startActivityForResult(activity, intent, reauestCode, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_diamond);
        mInput = (InputFrame) findViewById(R.id.input);
        mInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        mInput.addTextWatcher(this);
        Intent intent = getIntent();
        mMax = intent.getFloatExtra(EXTRA_MAX, 0);
        updateHint();
        mSureBtn = (Button) findViewById(R.id.sure);
        mSureBtn.setOnClickListener(this);
        mSureBtn.setEnabled(false);
    }

    private void updateHint() {
        mInput.setHint("可兑换: "+ NumberUtils.splitDoubleStr(mMax)+"颗味豆");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure:
                exchange();
                AnalyticsReport.onEvent(this, AnalyticsReport.EXCHANGE_DIAMOND_ACTIVITY_SURE_BUTTON_CLICK_EVENT_ID);
                break;
        }
    }

    private void exchange() {
        String input = mInput.getText().toString().trim();
        if (TextUtils.isEmpty(input)) {
            showToast(R.string.exchange_tips);
            return;
        }
        long number;
        try {
            number = Long.parseLong(input);
        } catch (NumberFormatException e) {
            showToast(R.string.exchange_too_max_tips);
            return;
        }
        if (number > mMax) {
            showToast(R.string.exchange_too_max_tips);
            return;
        }
        mLastNumber = number;
        showTipDialog(number);
    }

    @Override
    public void onSuccess(Msg msg) {
        ToastHelper.showToast(ResourceHelper.getString(R.string.exchange_success));
        dismissLoadingsDialog();
        mMax = mMax - mLastNumber;
        updateHint();
        mInput.clearEditText();
        hasExchange = true;
    }

    private void showTipDialog(final long drillCount) {
        showButtonDialog(ResourceHelper.getString(R.string.exchange_count), "确定兑换"+drillCount+"颗味豆？", R.string.yes, R.string.cancel,
                true, true,true,false, new IDialogOnClickListener() {
                    @Override
                    public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                        if (viewId == GenericDialog.ID_BUTTON_YES) {
                            tryExChangeRequest(drillCount);
                        }
                        return false;
                    }
                });
//        String tipContent = ResourceHelper.getString(R.string.exchange_count_tip, drillCount);
//        SimpleTextDialog dialog = new SimpleTextDialog(this);
//        dialog.setTitle(ResourceHelper.getString(R.string.exchange_count));
//        dialog.setText(tipContent);
//        dialog.addYesNoButton();
//        dialog.setCancelable(true);
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.setRecommendButton(GenericDialog.ID_BUTTON_NO);
//        dialog.setOnClickListener(new IDialogOnClickListener() {
//            @Override
//            public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
//                if (viewId == GenericDialog.ID_BUTTON_YES) {
//                    tryExChangeRequest(drillCount);
//                }
//                return false;
//            }
//        });
//        dialog.show();
    }

    private void tryExChangeRequest(long kaZuanCount) {
        showLoadingDialog();
        DataProvider.exchange(this, kaZuanCount, this);
    }

    @Override
    public void onFail(int errorCode, String errorMsg, String command) {
        if (errorCode == Msg.TLV_EXCHANGE_NOT_ENOUGH) {
            errorMsg = getString(R.string.exchange_not_enough);
        } else {
            errorMsg = getString(R.string.exchange_error);
        }
        ToastHelper.showToast(errorMsg);
        dismissLoadingsDialog();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int count = 0 ;
        try {
            count = Integer.valueOf(s.toString());
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        mSureBtn.setEnabled(StringUtils.isNotEmpty(s.toString()) && count > 0);
    }

    @Override
    public void finish() {
        if (hasExchange) {
            setResult(RESULT_OK);
        }
        super.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsReport.onEvent(this, AnalyticsReport.EXCHANGE_DIAMOND_ACTIVITY_SHOW_EVENT_ID);
    }
}
