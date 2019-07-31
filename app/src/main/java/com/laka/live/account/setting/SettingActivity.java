package com.laka.live.account.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.account.edit.MyInfoActivity;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.msg.Msg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.thread.BackgroundThread;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.widget.dialog.GenericDialog;
import com.laka.live.ui.widget.dialog.IDialogOnClickListener;
import com.laka.live.util.Util;

/**
 * 设置中心
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener,
        GsonHttpConnection.OnResultListener<Msg> {

    public static void startActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, SettingActivity.class);
            ActivityCompat.startActivity(activity, intent, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
    }

    private void init() {

        findViewById(R.id.my_profile).setOnClickListener(this);
        findViewById(R.id.about_us).setOnClickListener(this);
        findViewById(R.id.clear_cache).setOnClickListener(this);
        findViewById(R.id.msg_to_remind).setOnClickListener(this);
        findViewById(R.id.feedback).setOnClickListener(this);
        findViewById(R.id.logout).setOnClickListener(this);

        //initSwitchBtn();

    }

//    private void initSwitchBtn(){
//        View view = findViewById(R.id.live_only_wifi);
//        TextView textView = (TextView) view.findViewById(R.id.text);
//        textView.setText(R.string.live_only_wifi);
//
//        SwitchButton switchButton = (SwitchButton)view.findViewById(R.id.slide_switch);
//        switchButton.setChecked(AccountInfoManager.getInstance().getLiveOnlyWifiState());
//        switchButton.setOnCheckedChangeListener(this);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_us:
                AboutUsActivity.startActivity(this);
                AnalyticsReport.onEvent(this, AnalyticsReport.SETTING_ABOUT_US_CLICK_EVENT_ID);
                break;
            case R.id.clear_cache:
                clearCache();
                AnalyticsReport.onEvent(this, AnalyticsReport.SETTING_CLEAR_CACHE_CLICK_EVENT_ID);
                break;
            case R.id.msg_to_remind:
                MsgRemindActivity.startActivity(this);
                AnalyticsReport.onEvent(this, AnalyticsReport.SETTING_MSG_REMIND_CLICK_EVENT_ID);
                break;
            case R.id.feedback:
                FeedbackActivity.startActivity(this);
                AnalyticsReport.onEvent(this, AnalyticsReport.SETTING_FEEDBACK_CLICK_EVENT_ID);
                break;
            case R.id.logout:
                handleOnLoginOut();
                AnalyticsReport.onEvent(this, AnalyticsReport.SETTING_LOGOUT_CLICK_EVENT_ID);
                break;
            case R.id.my_profile:
                MyInfoActivity.startActivity(this);
                break;
        }
    }

    private void handleOnLoginOut() {
        showButtonDialog(R.string.account_exit_app, R.string.account_exit_app_tip, R.string.yes, R.string.cancel,
                true, true, new IDialogOnClickListener() {
                    @Override
                    public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                        if (viewId == GenericDialog.ID_BUTTON_YES) {
                            DataProvider.logout(SettingActivity.this);
                            showLoadingDialog(false);
                        }
                        return false;
                    }
                });
    }

    private void clearCache() {
        showLoadingDialog();
        BackgroundThread.post(new Runnable() {
            @Override
            public void run() {
                Util.delectAllCache(SettingActivity.this);
                SettingActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoadingsDialog();
                        showToast(getString(R.string.clear_cache_success));
                    }
                });
            }
        });
    }

    @Override
    public void onSuccess(Msg msg) {
        dismissLoadingsDialog();
        logout();
    }

    @Override
    public void onFail(int errorCode, String errorMsg, String command) {
        dismissLoadingsDialog();
        logout();
    }

    private void logout() {
        AccountInfoManager.getInstance().loginOut();
        finish();
        //LiveApplication.getInstance().exitApp(this);
        //LoginActivity.startActivity(this, LoginActivity.TYPE_FROM_LOGIN_OUT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsReport.onEvent(this, AnalyticsReport.MY_SETTING_SHOW_EVENT_ID);
    }

//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
//        switch (buttonView.getId()){
//            case R.id.slide_switch :
//                showDialog(true);
//                HashMap<String, String> params = new HashMap<>();
//                DataProvider.addToken(params);
//                params.put(Common.LIVE_ONLY_WIFI, String.valueOf(isChecked ? 1 : 0));
//                GsonHttpConnection.getInstance().post(this, Common.SET_USER_REMIND_URL, params, Msg.class, new GsonHttpConnection.OnResultListener<Object>() {
//                    @Override
//                    public void onSuccess(Object o) {
//                        dismissLoadingsDialog();
//                        AccountInfoManager.getInstance().updateLiveOnlyWifiState(isChecked);
//                    }
//
//                    @Override
//                    public void onFail(int errorCode, String errorMsg, String command) {
//                        dismissLoadingsDialog();
//                    }
//                });
//                break;
//            default:
//                break;
//        }
//    }
}
