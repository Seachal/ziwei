package com.laka.live.ui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.util.ResourceHelper;

/**
 * @Author:Rayman
 * @Date:2018/8/7
 * @Description:错误Dialog
 */

public class ErrorDialog extends CenterDialog {

    private TextView mTvTitle;
    private TextView mTvContent;
    private TextView mTvNegative;
    private TextView mTvPositive;

    private OnDialogClickListener onDialogClickListener;

    public ErrorDialog(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_error);
        mTvTitle = findViewById(R.id.tv_notify_title);
        mTvContent = findViewById(R.id.tv_notify_content);
        mTvNegative = findViewById(R.id.tv_notify_negative);
        mTvPositive = findViewById(R.id.tv_notify_positive);

        mTvNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDialogClickListener != null) {
                    onDialogClickListener.onClick(false);
                }
                dismiss();
            }
        });

        mTvPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDialogClickListener != null) {
                    onDialogClickListener.onClick(true);
                }
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        if (isShowing()) {
            dismiss();
        } else {
            super.show();
        }
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
    }

    public void enableTitle(boolean isEnable) {
        mTvTitle.setVisibility(isEnable ? View.VISIBLE : View.GONE);
    }

    public void enableContent(boolean isEnable) {
        mTvContent.setVisibility(isEnable ? View.VISIBLE : View.GONE);
    }

    public void enableNegative(boolean isEnable) {
        mTvNegative.setVisibility(isEnable ? View.VISIBLE : View.GONE);
        if (!isEnable) {
            mTvPositive.setBackground(ResourceHelper.getDrawable(R.drawable.selector_corner_white_7));
        }
    }

    public void enablePositive(boolean isEnable) {
        mTvPositive.setVisibility(isEnable ? View.VISIBLE : View.GONE);
        if (!isEnable) {
            mTvNegative.setBackground(ResourceHelper.getDrawable(R.drawable.selector_corner_white_7));
        }
    }

    public void setTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            return;
        }
        mTvTitle.setText(title);
    }

    public void setContent(String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        mTvContent.setText(content);
    }

    public void setNegativeText(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        mTvNegative.setText(text);
    }

    public void setPositiveText(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        mTvPositive.setText(text);
    }

    public void setOnDialogClickListener(OnDialogClickListener onDialogClickListener) {
        this.onDialogClickListener = onDialogClickListener;
    }

    public interface OnDialogClickListener {
        void onClick(boolean isPositive);
    }
}
