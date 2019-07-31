package com.laka.live.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.util.ViewUtils;

/**
 * Created by Lyf on 2017/3/31.
 */

public class DoubleTextView extends LinearLayout {


    private TextView mCount; // 顶部的数
    private TextView mType; // 底部的类型
    // 布局填充者
    private LayoutInflater inflater;

    public DoubleTextView(Context context) {
        super(context);
        init(context,null);
    }

    public DoubleTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public DoubleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {

        inflater = LayoutInflater.from(context);

        View rootView = inflater.inflate(R.layout.item_texts,this);

        mType = ViewUtils.findById(rootView,R.id.type);
        mCount = ViewUtils.findById(rootView,R.id.count);

        if(attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DoubleTextView);
            int count = typedArray.getInteger(R.styleable.DoubleTextView_count, 0);
            String type = typedArray.getString(R.styleable.DoubleTextView_type);

            mType.setText(type);
            mCount.setText(String.valueOf(count));
        }

    }

    // 设置顶部控件的文本
    public void setCountText(CharSequence charSequence) {
        mCount.setText(charSequence);
    }

    // 设置底部控件的文本
    public void seTypeText(CharSequence charSequence) {
        mType.setText(charSequence);
    }

    // 获取顶部控件的文本
    public String getCountText(){
        return mCount.getText().toString().trim();
    }
    // 获取底部控件的文本
    public String getTypeText(){
        return mType.getText().toString().trim();
    }
}
