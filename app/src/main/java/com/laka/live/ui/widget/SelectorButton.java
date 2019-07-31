package com.laka.live.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.util.SystemUtil;

import framework.ioc.Ioc;


/**
 * Created by Lyf on 2017/3/14.
 */
public class SelectorButton extends AppCompatButton {

    private Paint mPaint;// 画笔
    private Path mPath;//矩形的圆角路径
    private RectF mRect;// 圆角矩形
    private String text;// 绘制的文本
    private String pressTextColor;// 自定义按钮的按压文本颜色
    private String backGroundColor; // 自定义按钮的背景颜色(字符串)
    private String pressColor; // 自定义按钮的按压背景颜色(字符串)

    private Context mContext;

    private float mRadius = 0f;// 圆角角度
    private float mTopLeftRadius = 0f; // 左上圆角角度
    private float mBottomLeftRadius = 0f; // 左下圆角角度
    private float mTopRightRadius = 0f; // 右上圆角角度
    private float mBottomRightRadius = 0f; // 右下圆角角度
    private int measureWidth;// 按钮的宽度
    private int measureHeight;// 按钮的长度
    private boolean isBold = false; // 是否显示粗体
    private boolean isRadius = true;// 是否显示圆角
    private boolean showShadow = false;//是否显示按压层
    private boolean isStroke; // 是否中空(按钮中间为透明色)
    private boolean onlyStrokeColor;// 按压时是否只改变描边颜色
    private final static String TAG = "SelectorButton";
    private final static int defaultWidth = 300;// 默认的宽度
    private final static int defaultHeight = 600;// 默认的长度
    private final static String defaultText = "确定";// 默认的文本
    private final static String defaultColor = "#ff00ff00"; // 默认的背景颜色
    private final static String defaultTextColor = "#ff000000"; // 默认的背景颜色
    private final static String defaultPressColor = "#ffe9eaeb"; // 默认的按压层背景颜色

    private Typeface boldType = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
    private Typeface normalType = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);

    public SelectorButton(Context context) {
        super(context);
        init(context, null);
    }

    public SelectorButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SelectorButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureWidth = getMeasuredWidth() > 0 ? getMeasuredWidth() : defaultWidth;
        measureHeight = getMeasuredHeight() > 0 ? getMeasuredHeight() : defaultHeight;
        setMeasuredDimension(measureWidth, measureHeight);
    }

    // 初始化相关对象
    protected void init(Context context, AttributeSet attrs) {

        this.mContext = context;

        // 初始化相关变量
        mRect = new RectF();
        mPaint = new Paint();
        mPath = new Path();

        // 设置背景为透明,避免用户使用android:backGround进行设置背景
        setBackgroundColor(Color.TRANSPARENT);
        // 拿到android:text的文本
        text = getText().toString();
        // 将android:text清空,不然,Button会再绘一个相同的文本
        setText("");
        // 如果没有设置android:text文本，显示默认的确定文本。
        if (TextUtils.isEmpty(text))
            text = defaultText;

        // 属性相关
        if (attrs != null) {

            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SelectorButton);

            mRadius = typedArray.getDimension(R.styleable.SelectorButton_radius, 0f);
            mTopLeftRadius = typedArray.getDimension(R.styleable.SelectorButton_topLeftRadius, 0f);
            mTopRightRadius = typedArray.getDimension(R.styleable.SelectorButton_topRightRadius, 0f);
            mBottomLeftRadius = typedArray.getDimension(R.styleable.SelectorButton_bottomLeftRadius, 0f);
            mBottomRightRadius = typedArray.getDimension(R.styleable.SelectorButton_bottomRightRadius, 0f);
            isRadius = typedArray.getBoolean(R.styleable.SelectorButton_is_radius, true);
            isStroke = typedArray.getBoolean(R.styleable.SelectorButton_stroke, false);
            isBold = SystemUtil.getAttributeBooleanValue(attrs, R.attr.bold, false);
            backGroundColor = typedArray.getString(R.styleable.SelectorButton_color);
            pressColor = typedArray.getString(R.styleable.SelectorButton_press_color);
            pressTextColor = typedArray.getString(R.styleable.SelectorButton_press_text_color);
            onlyStrokeColor = typedArray.getBoolean(R.styleable.SelectorButton_onlyStrokeColor, false);

            // 如果没有设置颜色,就用默认颜色
            if (backGroundColor == null) {
                backGroundColor = defaultColor;
            }
            // 如果没有设置文本的按压颜色,就用默认颜色
            if (pressTextColor == null) {
                pressTextColor = defaultTextColor;
            }

            // 如果没设置按压效果颜色,根据所设置的颜色，自动计算出按压层颜色
            if (pressColor == null) {
                pressColor = getShadowColor(backGroundColor);
            }

            // mRadius优先使用
            if (mRadius > 0) {
                mTopLeftRadius = mRadius;
                mTopRightRadius = mRadius;
                mBottomLeftRadius = mRadius;
                mBottomRightRadius = mRadius;
            }

            typedArray.recycle();
        }

    }

    // 绘制UI
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 显示按压层
        if (showShadow) {

            // 设置画笔颜色
            mPaint.setColor(Color.parseColor(pressColor));

            // 只改变描边颜色
            if (onlyStrokeColor) {
                mPaint.setStyle(Paint.Style.STROKE);
                // 中空的，按压层才需要小于边框线
                mRect.set(0, 0, measureWidth - 1, measureHeight - 1);
            }
            // 开始绘制圆角矩形
            drawRoundRect(canvas);
            // 绘制文本
            drawText(canvas, Color.parseColor(pressTextColor));

        } else {
            // 设置圆角矩形的长宽为默认或用户设置的长宽
            mRect.set(0, 0, measureWidth, measureHeight);
            // 设置矩形的背景颜色
            mPaint.setColor(Color.parseColor(backGroundColor));
            // 设置中空效果
            if (isStroke) {
                mPaint.setStyle(Paint.Style.STROKE);
            }
            // 抗锯齿
            mPaint.setAntiAlias(true);
            // 绘制按钮
            drawRoundRect(canvas);
            // 绘制文本
            drawText(canvas, getCurrentTextColor());
        }


    }


    // 绘制按钮
    private void drawRoundRect(Canvas canvas) {

        // 开始绘制圆角矩形
        if (isRadius) {
            float[] cornersRadius = {mTopLeftRadius, mTopLeftRadius,
                    mTopRightRadius, mTopRightRadius,
                    mBottomRightRadius, mBottomRightRadius,
                    mBottomLeftRadius, mBottomLeftRadius};

            mPath.addRoundRect(mRect, cornersRadius, Path.Direction.CW);
            canvas.drawPath(mPath, mPaint);
        } else {
            canvas.drawRoundRect(mRect, 0, 0, mPaint);
        }

    }

    // 绘制文本
    private void drawText(Canvas canvas, int color) {

        // 设置文本的样式
        mPaint.setStyle(Paint.Style.FILL);// 充满
        mPaint.setColor(color);
        mPaint.setFakeBoldText(isBold);
        mPaint.setTextSize(getTextSize());
        mPaint.setTextAlign(Paint.Align.CENTER);
        // 设置文本居中
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        //为基线到字体上边框的距离
        float top = fontMetrics.top;
        //为基线到字体下边框的距离
        float bottom = fontMetrics.bottom;
        //基线中间点的y轴计算公式
        int baseLineY = (int) (mRect.centerY() - top / 2 - bottom / 2);
        // 开始绘制文本
        canvas.drawText(text, mRect.centerX(), baseLineY, mPaint);

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
                // 按压时改变颜色
                showShadow = true;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 松开时恢复颜色
                showShadow = false;
                invalidate();
                break;
        }

        return true;
    }

    private String tempBGColor = null; // 临时颜色码

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if (enabled) {
            if (tempBGColor != null) {
                backGroundColor = tempBGColor;
                tempBGColor = null;
            }
        } else {
            tempBGColor = backGroundColor;
            backGroundColor = "#d6d8db";
            setTextColor(Color.WHITE);
        }

        invalidate();
    }

    // 计算按压层颜色
    public static String getShadowColor(String color) {

        if (color == null)
            return color;

        String[] colors = new String[3];

        if (color.length() > 7) {
            // 暂不处理透明值[1-3]
            colors[0] = color.substring(3, 5);
            colors[1] = color.substring(5, 7);
            colors[2] = color.substring(7, 9);
        } else {
            colors[0] = color.substring(1, 3);
            colors[1] = color.substring(3, 5);
            colors[2] = color.substring(5, 7);
        }

        color = "#";

        for (int i = 0; i < 3; ++i) {

            int temp10Hex = to10Hex(colors[i]);

            if (temp10Hex >= 30) {
                temp10Hex = temp10Hex - 30;
            }
            color += temp10Hex < 16 ? "0" + to16Hex(temp10Hex) : to16Hex(temp10Hex);
        }

        return color;
    }

    // 10进制转16进制
    public static String to16Hex(int num) {
        return Integer.toHexString(num);
    }

    // 16进制转10进制
    public static int to10Hex(String num) {
        return Integer.parseInt(num, 16);
    }

    // 简化打印Log
    private static void log(String msg) {
        Log.d(TAG, msg);
    }

    // 设置文本
    public void setNewText(String text) {
        this.text = text;
        invalidate();
    }

    // 设置背景色
    public void setbackGroundColor(String backGroundColor) {

        this.backGroundColor = backGroundColor;
        // 背景颜色改的时候,按压颜色也要改
        if (!isStroke) {
            pressColor = getShadowColor(backGroundColor);
        }
        invalidate();
    }

    public String getNewText() {
        return text;
    }

    // 设置背景色
    public void setbackGroundColor(int color) {

        this.backGroundColor = "#" + Integer.toHexString(mContext.getResources().getColor(color));

        // 背景颜色改的时候,按压颜色也要改
        if (!isStroke) {
            pressColor = getShadowColor(backGroundColor);
        }
        invalidate();
    }


    public void setBoldType(boolean isBold) {
        if (isBold) {
            mPaint.setTypeface(boldType);
        } else {
            mPaint.setTypeface(normalType);
        }
        invalidate();
    }


//    public void setTypeface(Typeface typeface) {
//
//        if (typeface != null) {
//            mPaint.setTypeface(typeface);
//            invalidate();
//        }
//
//    }

}
