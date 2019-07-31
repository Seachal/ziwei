package com.laka.live.account.setting;

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
import com.laka.live.msg.Msg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.widget.HeadView;

import java.util.Locale;

public class FeedbackActivity extends BaseActivity implements TextWatcher,
        GsonHttpConnection.OnResultListener<Msg> {

    private static final int MAX_INPUT_CHAR_COUNT = 200;

    private EditText mFeedbackInput;

    private TextView mFeedbackSize;

    private EditText mContactInput;

    public static void startActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, FeedbackActivity.class);
            ActivityCompat.startActivity(activity, intent, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        init();
    }

    private void init() {
        HeadView headView = (HeadView) findViewById(R.id.head);
        headView.setTipOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        mFeedbackInput = (EditText) findViewById(R.id.feedback_input);
        mFeedbackInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_INPUT_CHAR_COUNT)});
        mFeedbackInput.addTextChangedListener(this);
        mFeedbackSize = (TextView) findViewById(R.id.text_size);
        mFeedbackSize.setText("0/0");
        mContactInput = (EditText) findViewById(R.id.input_contact);

        updateSizeText(0);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    private void updateSizeText(int size) {
        mFeedbackSize.setText(String.format(Locale.getDefault(), "%d/%d", size, MAX_INPUT_CHAR_COUNT));
    }

    @Override
    public void afterTextChanged(Editable s) {
        updateSizeText(s.length());
    }

    private void submit() {
        String feedback = mFeedbackInput.getText().toString();
        if (TextUtils.isEmpty(feedback)) {
            showToast(R.string.feedback_null_tips);
            return;
        }
        String contact = mContactInput.getText().toString();
        DataProvider.feedback(this, feedback, contact, this);
        showLoadingDialog();
    }

    @Override
    public void onSuccess(Msg msg) {
        dismissLoadingsDialog();
        showToast(R.string.feedback_success);
        finish();
    }

    @Override
    public void onFail(int errorCode, String errorMsg, String command) {
        dismissLoadingsDialog();
    }
}
