package com.laka.live.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.laka.live.R;

/**
 * Created by Lyf on 2017/5/31.
 * app:default_image
 * app:press_image
 * 必须设置这两个参数
 */
public class SelectorImageButton extends AppCompatButton {

    private int defaultImage; // 默认图片
    private int pressImage; // 按压图片

    public SelectorImageButton(Context context) {
        super(context);
    }

    public SelectorImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    // 初始化相关对象
    protected void init(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SelectorImage);
        defaultImage = typedArray.getResourceId(R.styleable.SelectorImage_default_image, 0);
        pressImage = typedArray.getResourceId(R.styleable.SelectorImage_press_image, 0);
        setBackgroundResource(defaultImage);
//        setScaleType(ScaleType.FIT_XY);
        typedArray.recycle();

    }

    // 触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                // 按压时改变
                setBackgroundResource(pressImage);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 松开时恢复
                setBackgroundResource(defaultImage);
                invalidate();
                break;
        }

        return true;
    }


}