package com.laka.live.account.edit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.CheckedTextView;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.bean.UserInfo;
import com.laka.live.msg.Msg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.widget.HeadView;
import com.laka.live.ui.widget.Toast;
import com.laka.live.util.Common;

import java.util.HashMap;

public class EditSexActivity extends BaseActivity implements View.OnClickListener,
        GsonHttpConnection.OnResultListener<Msg> {

    private CheckedTextView mBoyCheck;
    private CheckedTextView mGirlCheck;

    public static void startActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, EditSexActivity.class);
            ActivityCompat.startActivity(activity, intent, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sex);
        init();
    }

    private void init() {
        mBoyCheck = (CheckedTextView) findViewById(R.id.boy_item);
        mBoyCheck.setOnClickListener(this);
        mGirlCheck = (CheckedTextView) findViewById(R.id.gril_item);
        mGirlCheck.setOnClickListener(this);
        update(AccountInfoManager.getInstance().getCurrentAccountSex());
        HeadView headView = (HeadView) findViewById(R.id.head_layout);
        headView.setTipOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    private void update(int sex) {
        switch (sex) {
            case UserInfo.GENDER_BOY:
                mBoyCheck.setChecked(true);
                mGirlCheck.setChecked(false);
                break;
            case UserInfo.GENDER_GIRL:
                mBoyCheck.setChecked(false);
                mGirlCheck.setChecked(true);
                break;
            default:
                mBoyCheck.setChecked(false);
                mGirlCheck.setChecked(false);
                break;
        }
    }

    private void save() {
        int gender = getGender();
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.GENDER, String.valueOf(gender));
        DataProvider.editUserInfo(this, params, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.boy_item:
                update(UserInfo.GENDER_BOY);
                break;
            case R.id.gril_item:
                update(UserInfo.GENDER_GIRL);
                break;
        }
    }

    private int getGender() {
        return mBoyCheck.isChecked() ? UserInfo.GENDER_BOY :
                (mGirlCheck.isChecked() ? UserInfo.GENDER_GIRL : UserInfo.GENDER_UNKNOW);
    }

    @Override
    public void onSuccess(Msg Msg) {
        showToast(R.string.edit_gender_success, Toast.LENGTH_SHORT);
        AccountInfoManager.getInstance().updateCurrentAccountSex(getGender());
        finish();
    }

    @Override
    public void onFail(int errorCode, String errorMsg, String command) {
        showToast(R.string.edit_gender_fail, Toast.LENGTH_SHORT);
    }
}
