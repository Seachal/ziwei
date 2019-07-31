package com.laka.live.account.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.ui.activity.BaseActivity;

public class MoreActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_RESULT = "EXTRA_RESULT";

    /**
     * 拉黑
     */
    public static final int RESULT_SHIELD = 1;

    /**
     * 举报
     */
    public static final int RESULT_REPORT = 2;

    public static void startActivityForResult(Activity activity, int requestCode) {
        if (activity != null) {
            Intent intent = new Intent(activity, MoreActivity.class);
            ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
            activity.overridePendingTransition(R.anim.fast_fade_in, R.anim.idle);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_choice);

        init();
    }

    private void init() {
        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(R.string.more);

        TextView shieldTextView = (TextView) findViewById(R.id.take_piature);
        shieldTextView.setText(R.string.shield);
        shieldTextView.setOnClickListener(this);

        TextView reportTextView = (TextView) findViewById(R.id.choice_from_gallery);
        reportTextView.setText(R.string.report);
        reportTextView.setOnClickListener(this);

        findViewById(R.id.cancel).setOnClickListener(this);

        findViewById(R.id.root).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_piature:
                shield();
                break;
            case R.id.choice_from_gallery:
                report();
                break;
            case R.id.cancel:
                finish();
                break;
            case R.id.root:
                finish();
                break;
        }
    }

    /**
     * 拉黑
     */
    private void shield() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RESULT, RESULT_SHIELD);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 举报
     */
    private void report() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RESULT, RESULT_REPORT);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.idle, R.anim.fast_fade_out);
    }
}
