package com.laka.live.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.ui.widget.Effects.EffectsType;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;
import com.laka.live.util.ViewUtils;


/**
 * 编辑框
 */
public class EditDialog extends BaseDialog {

    private TextView commit;
    private TextView cancel;
    private TextView title;
    private EditText mInputNumberET;

    private String lastNumber;
    // 最小数额
    private int mMinCountNumber = 1;
    // 最大数额
    private int mMaxCountNumber = 99999;

    public EditDialog(FragmentActivity context, final View.OnClickListener callback) {
        super(context);
        setContentView(R.layout.dialog_edit, CENTER);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        setAnimator(EffectsType.FadeIn, EffectsType.FadeOut);

        title = (TextView) findViewById(R.id.title);
        mInputNumberET = (EditText) findViewById(R.id.input);
        commit = (TextView) findViewById(R.id.dialog_commit);
        cancel = (TextView) findViewById(R.id.dialog_cancel);

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utils.isEmpty(mInputNumberET.getEditableText().toString().trim())) {
                    ToastHelper.showToast("请填写购买数量");
                    return;
                } else if (getInputNumber() < getMinCountNumber()) {
                    ToastHelper.showToast("购买数量不能低于" + getMinCountNumber());
                    return;
                } else if (getInputNumber() > getMaxCountNumber()) {
                    ToastHelper.showToast("不能超过库存, 剩余库存量：" + getMaxCountNumber());
                    return;
                }

                v.setTag(getInputNumber());

                callback.onClick(v);
                dismiss();
            }
        });
        setCanceledOnTouchOutside(false);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.dialog_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        findViewById(R.id.reduce).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNotEmpty(mInputNumberET.getEditableText().toString().trim())) {
                    int inputNumber = getInputNumber();
                    if (inputNumber > mMinCountNumber) {
                        --inputNumber;
                        mInputNumberET.setText(String.valueOf(inputNumber));
                    }

                }
            }
        });

        findViewById(R.id.increase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utils.isNotEmpty(mInputNumberET.getEditableText().toString().trim())) {

                    int inputNumber = getInputNumber();
                    if (inputNumber < mMaxCountNumber) {
                        ++inputNumber;
                        mInputNumberET.setText(String.valueOf(inputNumber));
                    } else {
                        ToastHelper.showToast(R.string.shopping_goods_more_than_max_num_tip);
                    }
                }

            }
        });


        mInputNumberET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lastNumber = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Utils.isNotEmpty(mInputNumberET.getEditableText().toString().trim())) {
                    if (getInputNumber() > getMaxCountNumber()) {
                        if(lastNumber != null){
                            mInputNumberET.setText(lastNumber);
                            mInputNumberET.setSelection(mInputNumberET.getEditableText().toString().trim().length());
                        }
                        ToastHelper.showToast("不能超过库存, 剩余库存量：" + getMaxCountNumber());
                    }
                }
            }
        });

    }


    @Override
    public void onShow(DialogInterface dialogInterface) {
        super.onShow(dialogInterface);

        InputMethodManager imm = (InputMethodManager) mInputNumberET.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mInputNumberET, InputMethodManager.SHOW_FORCED);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        HideKeyboard(mInputNumberET);
    }

    //隐藏虚拟键盘
    private void HideKeyboard(View v) {

        try {
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            }
        } catch (Exception e) {

        }

    }


    private int getInputNumber() {
        return Integer.valueOf(mInputNumberET.getEditableText().toString().trim());
    }

    public int getMinCountNumber() {
        return mMinCountNumber;
    }

    public void setMinCountNumber(int mMinCountNumber) {
        this.mMinCountNumber = mMinCountNumber;
    }

    public int getMaxCountNumber() {
        return mMaxCountNumber;
    }

    public void setMaxCountNumber(int mMaxCountNumber) {
        this.mMaxCountNumber = mMaxCountNumber;
    }

    public void setCurrentNumber(int number) {
        mInputNumberET.setText(String.valueOf(number));
        mInputNumberET.setSelection(String.valueOf(number).length());
    }
}
