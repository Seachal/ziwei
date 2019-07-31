package com.laka.live.ui.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.laka.live.R;


/**
 * 自定义圆形加载view
 */
public class WxCircleLoading extends AppCompatTextView {

    private static final int OUTER_LAYOUT_CIRCLE_COLOR = Color.parseColor("#FFFFFF");
    private static final int OUTER_LAYOUT_CIRCLE_STROKE_WIDTH = 2;
    private static final int TRIANGLE_COLOR = Color.parseColor("#FFFFFF");
    private static final int RADIUS = 30;
    private static final String TAG = "WxCircleLoading";

    private int outerCircleColor = OUTER_LAYOUT_CIRCLE_COLOR;
    private int outerCircleStrokeWidth = dp2px(OUTER_LAYOUT_CIRCLE_STROKE_WIDTH);
    private int mTriangleColor = TRIANGLE_COLOR;
    private int mRadius = dp2px(RADIUS);

    private Paint mPaint;
    private Paint outerCirclePaint;
    private Paint mArcPaint;
    private Paint mTrianglePaint;
    private float mArcAngle;
    private float mDistance;
    private Path mPath;
    private float mTriangleLength;//三角形边长
    private Status mStatus = Status.End;

    public WxCircleLoading(Context context) {
        this(context, null);
    }

    public WxCircleLoading(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WxCircleLoading(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.WxCircleLoading);
        int indexCount = array.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.WxCircleLoading_outer_layout_circle_color:
                    outerCircleColor = array.getColor(attr, OUTER_LAYOUT_CIRCLE_COLOR);
                    break;
                case R.styleable.WxCircleLoading_outer_layout_circle_stroke_width:
                    outerCircleStrokeWidth = (int) array.getDimension(attr, outerCircleStrokeWidth);
                    break;
                case R.styleable.WxCircleLoading_triangle_color:
                    mTriangleColor = array.getColor(attr, mTriangleColor);
                    break;
                case R.styleable.WxCircleLoading_circle_radius:
                    mRadius = (int) array.getDimension(attr, mRadius);
                    break;
            }
        }
        //回收
        array.recycle();
        Log.d(TAG, " mRadius=" + mRadius);
        mDistance = (float) (mRadius * 0.06);
        mTriangleLength = mRadius;
        //设置画笔
        setPaint();
        //画三角形
        mPath = new Path();
        mPaint = new Paint();
        float mFirstPointX = (float) (mRadius - Math.sqrt(3.0) / 4 * mRadius);//勾股定理
        float mNiceFirstPointX = (float) (mFirstPointX + mFirstPointX * 0.2);
        float mFirstPointY = mRadius - mTriangleLength / 2;
        mPath.moveTo(mNiceFirstPointX, mFirstPointY);
        mPath.lineTo(mNiceFirstPointX, mRadius + mTriangleLength / 2);
        mPath.lineTo((float) (mNiceFirstPointX + Math.sqrt(3.0) / 2 * mRadius), mRadius);
        mPath.lineTo(mNiceFirstPointX, mFirstPointY);
    }

    private void setPaint() {
        outerCirclePaint = new Paint();
        outerCirclePaint.setAntiAlias(true);
        outerCirclePaint.setDither(true);
        outerCirclePaint.setStyle(Paint.Style.STROKE);
        outerCirclePaint.setColor(outerCircleColor);
        outerCirclePaint.setStrokeWidth(outerCircleStrokeWidth);
        outerCirclePaint.setStrokeCap(Paint.Cap.ROUND);

        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);
        mArcPaint.setStyle(Paint.Style.FILL);
        mArcPaint.setColor(outerCircleColor);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);

        mTrianglePaint = new Paint();
        mTrianglePaint.setAntiAlias(true);
        mTrianglePaint.setDither(true);
        mTrianglePaint.setColor(outerCircleColor);
        mTrianglePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width;
        int height;
        if (widthMode != MeasureSpec.EXACTLY) {
            width = getPaddingLeft() + mRadius * 2 + outerCircleStrokeWidth * 2 + getPaddingRight();
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            height = getPaddingTop() + mRadius * 2 + outerCircleStrokeWidth * 2 + getPaddingBottom();
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.d(TAG, " onDraw mArcAngle=" + mArcAngle);
        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());

        //画圆
        canvas.drawCircle(mRadius, mRadius, mRadius, outerCirclePaint);

        if (mStatus == Status.End) {
            //画三角形
            canvas.drawPath(mPath, mTrianglePaint);
        } else {//正在进行状态
            //画扇形
            canvas.drawArc(new RectF(0 + mDistance, 0 + mDistance, mRadius * 2 - mDistance, mRadius * 2 - mDistance), -90, 360 * mArcAngle, true, mArcPaint);
        }

        // 绘制进度数
        drawText(canvas, Color.BLACK);

        canvas.restore();
    }

    // 绘制文本
    private void drawText(Canvas canvas, int color) {

        // 设置文本的样式
        mPaint.setStyle(Paint.Style.FILL);// 充满
        mPaint.setColor(color);
        mPaint.setTextSize(getTextSize());
        mPaint.setTextAlign(Paint.Align.CENTER);
        // 设置文本居中
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        //为基线到字体上边框的距离
        float top = fontMetrics.top;
        //为基线到字体下边框的距离
        float bottom = fontMetrics.bottom;
        //基线中间点的y轴计算公式
        int baseLineY = (int) (mRadius - top / 2 - bottom / 2);
        // 开始绘制文本,之所以要-1，是因为在100%的时候，还要等上一会。。干脆显示成99%
        int progress = (int) (mArcAngle * 100) - 1;
        // 这样才不会在一开始显示个-1
        if (progress < 0)
            progress = 0;
        canvas.drawText(String.valueOf(progress) + "%", mRadius, baseLineY, mPaint);

    }


    public void animatorAngle() {
        setClickable(false);
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1.0f);
        animator.setDuration(6000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mArcAngle = (float) animation.getAnimatedValue();
                Log.d(TAG, " mArcAngle=" + mArcAngle);
                //刷新View
                invalidate();
            }
        });
        //开启动画
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //动画结束后修改状态变化
                mStatus = Status.End;
                setClickable(true);

                setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    public enum Status {
        End,
        Starting
    }

    public Status getStatus() {
        return mStatus;
    }

    // 设置可见与否
    public void setVisible(int isVisible) {
        this.mStatus = isVisible == VISIBLE ? Status.Starting : Status.End;
        setVisibility(isVisible);
        invalidate();
    }

    public void setProgress(float progress) {
        mArcAngle = progress;
        Log.d(TAG, " mArcAngle=" + mArcAngle);
        invalidate();
    }

    public float getProgress() {
        return mArcAngle;
    }
}
