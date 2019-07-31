package com.laka.live.ui.course;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.util.Utils;

import framework.ioc.annotation.InjectExtra;
import framework.ioc.annotation.InjectView;

/**
 * Created by Lyf on 2017/4/5.
 */

public class EditActivity extends BaseActivity implements TextWatcher {

    @InjectView(id = R.id.title)
    public TextView title;
    @InjectView(id = R.id.back)
    public TextView back;
    @InjectView(id = R.id.count)
    public TextView count;
    @InjectView(id = R.id.edit)
    public EditText edit;
    @InjectView(id = R.id.rightText)
    public TextView rightText;
    // 之前的文本
    @InjectExtra(name = "hint", def = "")
    public String hint;
    // 之前的文本
    @InjectExtra(name = "text", def = "")
    public String text;
    // 编辑的类型(可能是简介、姓名之类的)
    @InjectExtra(name = "type", def = "-1")
    public Integer type;
    // 类型的次序(可能有多个姓名)
    @InjectExtra(name = "index", def = "0")
    public Integer index;

    // 最小的字符限制
    private int minLength = 1;
    // 最大的字符限制
    private int maxLength;
    // 编辑的类型( 0 == 课堂简介)
    public final static int EditIntroduction = 1000, EditFormula = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initEdit();
    }

    // 初始化编辑
    private void initEdit() {

        if (Utils.isEmpty(text))
            text = "";

        rightText.setVisibility(View.VISIBLE);

        switch (type) {

            case EditIntroduction:
                // 编辑课堂简介
                maxLength = 500;
                title.setText("课堂简介");
                rightText.setText("保存");
                edit.setHint(hint);
                break;
            case EditFormula:
                // 编辑配方做法
                maxLength = 500;
                title.setText("配方做法");
                rightText.setText("保存");
                edit.setHint(hint);
                break;
            default:
                maxLength = 500;
                rightText.setText("保存");
                title.setText("课堂简介");
                edit.setHint("请输入内容");
        }

        // 设置之前的文本,如果有。
        edit.setText(text);
        // 设置光标位置
        edit.setSelection(text.length());
        // 显示最大限制长度
        count.setText(text.length() + "/" + maxLength);
        // 限制输入长度
        edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        // 监听输入
        edit.addTextChangedListener(this);
    }


    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.rightText:
                if (Utils.isEmpty(getText(edit))
                        && type != EditFormula) {
                    showToast(edit.getHint().toString());
                    return;
                } else {
                    commit();
                }
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    // 提交内容
    private void commit() {
        // 退出前隐藏软键盘
        hideKeyboard(mContext);
        // 默认是写简历，写完就返回文本就行
        Intent intent = new Intent();
        intent.putExtra("index", index);
        intent.putExtra("text", edit.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        int length = edit.getText().toString().length();
        // 设置当前的字数，包括空格
        count.setText(length + "/" + maxLength);

    }

}
