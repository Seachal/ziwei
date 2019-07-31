package com.laka.live.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.TextViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;

/**
 * Created by mac on 16/3/16.
 */
public class MyInfoItemView extends FrameLayout {

    private Context mContext;
    private TextView mKeyText;
    private TextView mValueText; // 右边顶部的文本(如果SecValue有的话，该文本就在顶部,如果没有，就是居中)
    private TextView mSecValueText; // 右边底部的文本
    private LevelText mLevel;

    public MyInfoItemView(Context context) {
        this(context, null);
    }

    public MyInfoItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyInfoItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init() {
        mContext = getContext();
        LayoutInflater.from(mContext).inflate(R.layout.my_info_item, this);
        mKeyText = (TextView) findViewById(R.id.text);
        mValueText = (TextView) findViewById(R.id.go);
        mSecValueText = (TextView) findViewById(R.id.go2);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {

        init();

        TypedArray a = mContext.obtainStyledAttributes(
                attrs, R.styleable.MyInfoItemView, defStyleAttr, 0);
        mKeyText.setText(a.getString(R.styleable.MyInfoItemView_key));
        mKeyText.setTextColor(a.getColor(R.styleable.MyInfoItemView_keyColor, ResourceHelper.getColor(R.color.white)));
        mKeyText.setTextSize(TypedValue.COMPLEX_UNIT_PX, a.getDimension(R.styleable.MyInfoItemView_keySize, ResourceHelper.getDimen(R.dimen.space_12)));
        mValueText.setText(a.getString(R.styleable.MyInfoItemView_value));
        mValueText.setTextColor(a.getColor(R.styleable.MyInfoItemView_valueColor, ResourceHelper.getColor(R.color.white)));
        // Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/AFFOGATO-BOLD.OTF");
        // mValueText.setTypeface(typeface);
        // mSecValueText.setTypeface(typeface);
        setIconDrawable(a.getDrawable(R.styleable.MyInfoItemView_leftIcon));
        a.recycle();

    }

    public void setIconResource(int iconId) {
        Drawable drawable = mContext.getResources().getDrawable(iconId);
        setIconDrawable(drawable);
    }

    public void setIconDrawable(Drawable drawable) {
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mKeyText, drawable, null, null, null);
    }

    public void setKeyText(int textId) {
        setKeyText(mContext.getString(textId));
    }

    public void setKeyText(CharSequence charSequence) {
        mKeyText.setText(charSequence);
    }

    public void setValueTextColor(int color) {
        mValueText.setTextColor(getResources().getColor(color));
    }

    public void setSecValueTextColor(int color) {
        mSecValueText.setTextColor(getResources().getColor(color));
    }

    public void setValueText(int textId) {
        setValueText(mContext.getString(textId));
    }

    public void setValueText(CharSequence charSequence) {
        mValueText.setText(charSequence);
    }

    public void setSecValueText(CharSequence charSequence) {

        if (charSequence != null && charSequence.length() > 0) {
            mSecValueText.setVisibility(VISIBLE);
            mSecValueText.setText(charSequence.toString());
        }

    }

    public void setLevel(int level) {
        if (mLevel == null) {
            ViewStub viewStub = (ViewStub) findViewById(R.id.level);
            mLevel = (LevelText) viewStub.inflate();
        }
        mLevel.setLevel(level);
    }

    /**
     * 设置红点的数量
     *
     * @param count
     */
    public void setRedCount(double count) {
        TextView countTv = (TextView) findViewById(R.id.count);

        if (count > 0) {
            countTv.setText("+" + count);
            countTv.setVisibility(VISIBLE);
        } else {
            countTv.setVisibility(GONE);
        }

    }

}
