package com.laka.live.account.edit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.help.ChineseFilter;
import com.laka.live.msg.Msg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.widget.HeadView;
import com.laka.live.ui.widget.Toast;
import com.laka.live.util.Common;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by luwies on 16/3/19.
 */
public class EditSignActivity extends BaseActivity implements TextWatcher,
        GsonHttpConnection.OnResultListener<Msg> {

    private final static int MAX_INPUT_CHAR_COUNT = 30;

    private EditText mInput;
    private TextView mInputTextCount;
    private ChineseFilter mChineseFilter;
    public static void startActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, EditSignActivity.class);
            ActivityCompat.startActivity(activity, intent, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sign);
        init();
    }

    private void init() {
        HeadView headView = (HeadView) findViewById(R.id.head_layout);
        headView.setTipOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        mInput = (EditText) findViewById(R.id.input);
        String sign = AccountInfoManager.getInstance().getCurrentAccountSign();
        if (TextUtils.isEmpty(sign)) {
            sign = "";
        }
        mInput.setText(sign);
        mInput.setSelection(sign.length());
        mInput.addTextChangedListener(this);
//        mInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_INPUT_CHAR_COUNT)});
        mChineseFilter = new ChineseFilter(MAX_INPUT_CHAR_COUNT*2);
        mInput.setFilters(new InputFilter[]{mChineseFilter});
        mInputTextCount = (TextView) findViewById(R.id.input_text_count);
//       updateTextCount(sign.length());
        updateTextCount(mChineseFilter.getLength(sign));
    }

    private void save() {
        String sign = mInput.getText().toString().trim();
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.DESCRIPTION, sign);
        DataProvider.editUserInfo(this, params, this);
    }

    private void updateTextCount(int size) {
        mInputTextCount.setText(String.format(Locale.getDefault(),"%d/%d", size, MAX_INPUT_CHAR_COUNT));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
//        int size = s.length();
//        updateTextCount(size);
        updateTextCount(mChineseFilter.getLength(s.toString()));
    }

    @Override
    public void onSuccess(Msg msg) {
        showToast(R.string.edit_sidn_success, Toast.LENGTH_SHORT);
        AccountInfoManager.getInstance().updateCurrentAccountSign(mInput.getText().toString().trim());
        finish();
    }

    @Override
    public void onFail(int errorCode, String errorMsg, String command) {
        showToast(R.string.edit_sidn_fail, Toast.LENGTH_SHORT);
    }
}
