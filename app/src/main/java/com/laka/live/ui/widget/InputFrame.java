package com.laka.live.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.util.Utils;

/**
 * Created by luwies on 16/4/15.
 */
public class InputFrame extends LinearLayout {

    private TextView mKeyText;

    private EditText mEditContent;

    public InputFrame(Context context) {
        this(context, null);
    }

    public InputFrame(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputFrame(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public InputFrame(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        Context context = getContext();

        setOrientation(HORIZONTAL);

        mKeyText = new TextView(context);
        mEditContent = new EditText(context);

        int margin = context.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);

        mKeyText.setGravity(Gravity.CENTER_VERTICAL);
        mKeyText.setTextColor(ContextCompat.getColor(context, R.color.color333333));
        mKeyText.setBackgroundResource(R.color.white);
        mKeyText.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.dip2px(context, 15f));
        LayoutParams keyTextParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        keyTextParams.leftMargin = margin;
        keyTextParams.rightMargin = margin;
        addView(mKeyText, keyTextParams);

        mEditContent.setBackgroundResource(R.color.white);
        mEditContent.setGravity(Gravity.CENTER_VERTICAL);
        mEditContent.setSingleLine();
        mEditContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, Utils.dip2px(context, 14f));
        mEditContent.setTextColor(ContextCompat.getColor(context, R.color.color333333));
        mEditContent.setHintTextColor(ContextCompat.getColor(context, R.color.colorAAAAAA));
        LayoutParams contentParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        contentParams.rightMargin = margin;
        addView(mEditContent, contentParams);

        setBackgroundResource(R.drawable.white);

        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(
                    attrs, R.styleable.InputFrame, defStyleAttr, 0);
            mKeyText.setText(a.getString(R.styleable.InputFrame_keyText));
        }
    }

    public void setKeyText(int resId) {
        mKeyText.setText(resId);
    }

    public void setKeyText(CharSequence charSequence) {
        mKeyText.setText(charSequence);
    }

    public void clearEditText() {
        mEditContent.setText("");
    }

    public void setHint(int resid) {
        mEditContent.setHint(resid);
    }

    public void setHint(CharSequence charSequence) {
        mEditContent.setHint(charSequence);
    }

    public void setInputType(int type) {
        mEditContent.setInputType(type);
    }

    public Editable getText() {
        return mEditContent.getText();
    }

    public void addTextWatcher(TextWatcher textWatcher) {
        mEditContent.addTextChangedListener(textWatcher);
    }

}
