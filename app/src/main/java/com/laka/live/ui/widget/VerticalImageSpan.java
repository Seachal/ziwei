package com.laka.live.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

import com.orhanobut.logger.Logger;

/**
 * @Author:Rayman
 * @Date:2018/8/11
 * @Description:因为ImageSpan只有基线对齐和Bottom对齐 这里拓展一个Vertical对齐的ImageSpan
 */

public class VerticalImageSpan extends ImageSpan {

    public VerticalImageSpan(Context context, int resourceId) {
        super(context, resourceId);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        super.draw(canvas, text, start, end, x, top, y, bottom, paint);
        Drawable drawable = getDrawable();
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        int transY = (y + fontMetricsInt.descent + y + fontMetricsInt.ascent) / 2 - drawable.getBounds().bottom / 2;
        Logger.e("输出descent：" + fontMetricsInt.descent
                + "\nascent：" + fontMetricsInt.ascent
                + "\ndrawable bottom：" + drawable.getBounds().bottom
                + "\ntransY：" + transY);
        canvas.save();
        canvas.translate(x, transY);
        drawable.draw(canvas);
        canvas.restore();
    }
}
