package com.laka.live.account.edit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.EditText;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.msg.Msg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.widget.HeadView;
import com.laka.live.ui.widget.Toast;
import com.laka.live.util.Common;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ToastHelper;

import java.util.HashMap;

/**
 * Created by mac on 16/3/17.
 * 编辑昵称
 */
public class EditNickNameActivity extends BaseActivity implements View.OnClickListener,
        GsonHttpConnection.OnResultListener<Msg> {

    private final static int MAX_LENGTH = 16;
    private EditText mInput;

    public static void startActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, EditNickNameActivity.class);
            ActivityCompat.startActivity(activity, intent, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nick_name);
        init();
    }

    private void init() {
        findViewById(R.id.clear).setOnClickListener(this);
        mInput = (EditText) findViewById(R.id.edit_name);
        String name = AccountInfoManager.getInstance().getCurrentAccountNickName();
        mInput.setText(name);
        mInput.requestFocus();
       /* mInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_LENGTH)});*/
        HeadView headView = (HeadView) findViewById(R.id.head_layout);
        headView.setTipOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    private void save() {
        if (StringUtils.isEmpty(getName())) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.nick_name_should_not_null));
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.NICK_NAME, getName());
        DataProvider.editUserInfo(this, params, this);
        showLoaidng();
    }

    private void clear() {
        mInput.setText("");
    }

    private void showLoaidng() {
        showLoadingDialog();
    }

    private void hideLoaidng() {
        dismissLoadingsDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear:
                clear();
                break;
        }
    }

    private String getName() {
        return mInput.getText().toString().trim();
    }

    @Override
    public void onSuccess(Msg msg) {
        hideLoaidng();
        showToast(R.string.edit_nick_name_success, Toast.LENGTH_SHORT);
        AccountInfoManager.getInstance().updateCurrentAccountNickName(getName());
        finish();
    }

    @Override
    public void onFail(int errorCode, String errorMsg, String command) {
        hideLoaidng();
        showToast(R.string.edit_nick_name_fail, Toast.LENGTH_SHORT);
    }
}
