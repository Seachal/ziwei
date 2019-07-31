package com.laka.live.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;
import com.laka.live.util.ViewUtils;

/**
 * Created by Lyf on 2017/6/1.
 */
public class LimitEditText extends RelativeLayout implements TextWatcher {


    private Context mContext;
    private TextView mCount;
    private EditText mEditText;
    private int maxLength = 0; // 当maxLength为0时，代表不做长度限制

    public LimitEditText(Context context) {
        super(context);
        init(context, null);
    }

    public LimitEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measureHeight = getMeasuredHeight();
        // 当有限制大小的时候，要减去mCount控件的高度,不然输入框就会把mCount控件给遮挡了
        if (maxLength > 0)
            measureHeight -= mContext.getResources().getDimension(R.dimen.space_41);
        // 设置EditText的高度
        mEditText.setHeight(measureHeight);

        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
    }

    private void init(Context context, AttributeSet attrs) {

        mContext = context;

        View view = LayoutInflater.from(context).inflate(R.layout.widget_edit, this);
        mCount = ViewUtils.findById(view, R.id.count);
        mEditText = ViewUtils.findById(view, R.id.editText);
        mEditText.addTextChangedListener(this);

        if (attrs != null) {

            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LimitEditText);
            String hint = typedArray.getString(R.styleable.LimitEditText_hint);
            int textColorHint = typedArray.getColor(R.styleable.LimitEditText_textColorHint, Color.parseColor("#BFBFBF"));
            maxLength = typedArray.getInt(R.styleable.LimitEditText_maxLength, 0);

            mEditText.setHint(hint);
            mEditText.setHintTextColor(textColorHint);

            if (maxLength > 0) {
                mCount.setText("0/" + maxLength);
                mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
            } else {
                mCount.setVisibility(GONE);
            }
            typedArray.recycle();
        }
    }


    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (maxLength > 0) {
            int length = s.toString().length();
            mCount.setText(length + "/" + maxLength);
            ViewUtils.setPartTextColor(mCount, R.color.color848484, 0, String.valueOf(length).length());
        }

    }

    /**
     * @return 返回输入的文件
     */
    public String getText() {
        return mEditText.getText().toString().trim();
    }

    /**
     *
     * @param text 设置文本
     */
    public void setText(String text) {

        if (text == null)
            return;

        mEditText.setText(text);
    }


}
