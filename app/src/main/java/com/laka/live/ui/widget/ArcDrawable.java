package com.laka.live.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.laka.live.R;
import com.laka.live.util.Utils;

/**
 * Created by luwies on 16/8/11.
 */
public class ArcDrawable extends Drawable {

    private Paint mPaint;

    private float mSweepAngle;

    public ArcDrawable(Context context) {

        mPaint = new Paint();
        mPaint.setStrokeWidth(Utils.dip2px(context, 2f));
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(ContextCompat.getColor(context, R.color.colorF65843));
    }

    public void setProgress(int progress) {
        mSweepAngle = progress * 3.6f;
    }

    @Override
    public void draw(Canvas canvas) {
        RectF rect = new RectF(getBounds());
        float strokeWidth = mPaint.getStrokeWidth();
        rect.left += strokeWidth;
        rect.top += strokeWidth;
        rect.right -= strokeWidth;
        rect.bottom -= strokeWidth;
        canvas.drawArc(rect, -90f, mSweepAngle, false, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
