package com.laka.live.ui.rankinglist.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by zwl on 2016/7/14.
 * Email-1501448275@qq.com
 */
public class DrawableCenterTextView extends TextView {

    public DrawableCenterTextView(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
    }

    public DrawableCenterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawableCenterTextView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        Drawable drawableLeft = drawables[0];
        float textWidth = getPaint().measureText(getText().toString());
        float bodyWidth = 0f;
        if (drawableLeft != null) {
            int drawablePadding = getCompoundDrawablePadding();
            int drawableWidth = 0;
            drawableWidth = drawableLeft.getIntrinsicWidth();
            bodyWidth = textWidth + drawableWidth + drawablePadding;
        } else {
            bodyWidth = textWidth;
        }
        canvas.translate((getWidth() - bodyWidth) / 2, 0);
        super.onDraw(canvas);
    }
}
