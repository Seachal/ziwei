package com.laka.live.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.laka.live.R;

/**
 * This class adds a stroke to the generic TextView allowing the text to stand out better against
 * the background (ie. in the AllApps button).
 */
public class StrokedTextView extends TextView {
    private final Canvas mCanvas = new Canvas();
    private final Paint mPaint = new Paint();
    private Bitmap mCache;
    private boolean mUpdateCachedBitmap;
    private int mStrokeColor;
    private float mStrokeWidth;
    private int mTextColor;

    public StrokedTextView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public StrokedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public StrokedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StrokedTextView,
                defStyle, 0);
        mStrokeColor = a.getColor(R.styleable.StrokedTextView_strokeColor, 0xFF000000);
//        mStrokeWidth = a.getFloat(R.styleable.StrokedTextView_strokeWidth, 0.0f);
        mStrokeWidth = a.getDimension(R.styleable.StrokedTextView_strokeWidth, 0f);
        mTextColor = a.getColor(R.styleable.StrokedTextView_strokeTextColor, 0xFFFFFFFF);
        a.recycle();
        mUpdateCachedBitmap = true;
        // Setup the text paint
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mPaint.setFakeBoldText(true);
    }

    public void setStrokeColor(int color) {
        mStrokeColor = color;
        invalidate();
    }

    public void setStrokeWidth(int width) {
        mStrokeWidth = width;
        invalidate();
    }

    public void setStrokeTextColor(int color) {
        mTextColor = color;
        invalidate();
    }

    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        super.onTextChanged(text, start, before, after);
        mUpdateCachedBitmap = true;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0) {
            mUpdateCachedBitmap = true;
            if (mCache != null && mCache.isRecycled() == false) {
                mCache.recycle();
            }
            mCache = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        } else {
            mCache = null;
        }
    }

    public void onDraw(Canvas canvas) {
        if (mCache != null) {
            if (mUpdateCachedBitmap) {
                final int w = getMeasuredWidth();
                final int h = getMeasuredHeight();
                String text = getText().toString();
                final Rect textBounds = new Rect();
                final Paint textPaint = getPaint();
                final int textWidth = (int) textPaint.measureText(text);
                textPaint.getTextBounds(text, 0, 1, textBounds);
                // Clear the old cached imageImage
                mCanvas.setBitmap(mCache);
                mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
                // Draw the drawable
                final int drawableLeft = getPaddingLeft();
                final int drawableTop = getPaddingTop();
                final Drawable[] drawables = getCompoundDrawables();
                for (int i = 0; i < drawables.length; ++i) {
                    if (drawables[i] != null) {
                        drawables[i].setBounds(drawableLeft, drawableTop,
                                drawableLeft + drawables[i].getIntrinsicWidth(),
                                drawableTop + drawables[i].getIntrinsicHeight());
                        drawables[i].draw(mCanvas);
                    }
                }


                final int left = 0/*w - getPaddingRight() - textWidth*/;
                /*final*/
                int bottom = (h + textBounds.height()) / 2;
                bottom = getBaseline();
                // Draw the outline of the text
                mPaint.setStrokeWidth(mStrokeWidth);
                mPaint.setColor(mStrokeColor);
                mPaint.setTextSize(getTextSize());

                TextPaint tempTextPaint = new TextPaint();
                tempTextPaint.setTextSize(getTextSize());
                text = TextUtils.ellipsize(text, tempTextPaint, w, TextUtils.TruncateAt.MIDDLE).toString();

                mCanvas.drawText(text, left, bottom, mPaint);
                // Draw the text itself
                mPaint.setStrokeWidth(0);
                mPaint.setColor(mTextColor);
                mCanvas.drawText(text, left, bottom, mPaint);
                mUpdateCachedBitmap = false;
            }
            canvas.drawBitmap(mCache, 0, 0, mPaint);
        } else {
            super.onDraw(canvas);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mCache != null && mCache.isRecycled() == false) {
            mCache.recycle();
            mCache = null;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mCache == null || mCache.isRecycled()) {
            int w = getWidth();
            int h = getHeight();
            if (w > 0 && h > 0) {
                mCache = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            }
        }

    }

    public Bitmap getCache() {
        return mCache;
    }
}
