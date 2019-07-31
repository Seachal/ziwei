package com.laka.live.ui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;

import com.laka.live.R;
import com.laka.live.util.Log;
import com.laka.live.util.Utils;


public class CountDownProgressBar extends View {
    private final static String TAG = CountDownProgressBar.class.getSimpleName();

//    private final static int START_ANGLE = -90;  //初始角度

    private final static int DEFAULT_CIRCLE_SOLID_COLOR = Color.WHITE;
    private final static int DEFAULT_CIRCLE_STROKE_COLOR = Color.GRAY;
    private final static int DEFAULT_CIRCLE_STROKE_WIDTH = 4;
    private final static int DEFAULT_CIRCLE_RADIUS = 20;

    private final static int DEFAULT_PROGRESS_COLOR = Color.RED;
    private final static int DEFAULT_PROGRESS_WIDTH = 4;

    private final static int DEFAULT_TEXT_COLOR = Color.BLACK;
    private final static int DEFAULT_TEXT_SIZE = 30;
    private final static String DEFAULT_TEXT_VALUE = "跳过";

    //默认圆
    private int circleSolidColor;
    private int circleStrokeColor;
    private int circleStrokeWidth;
    private int circleRadius;
    //进度条
    private int progressColor;
    private int progressWidth;
    //显示文字
    private int textColor;
    private float textSize;
    private String textValue;
    //画笔
    private Paint circleStrokePaint;
    private Paint circlePaint;
    private Paint progressPaint;
    private Paint textPaint;

    private float currentAngle;  //当前角度
    private RectF progressOval;

    private boolean mIsCountDowning = false;
    private ValueAnimator animator;
    private CountDownTimer mCountDownTimer;

    public CountDownProgressBar(Context context) {
        this(context, null);
    }

    public CountDownProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CountDownProgressBar);
        circleSolidColor = typedArray.getColor(R.styleable.CountDownProgressBar_circle_solid_color, DEFAULT_CIRCLE_SOLID_COLOR);
        circleStrokeColor = typedArray.getColor(R.styleable.CountDownProgressBar_circle_stroke_color, DEFAULT_CIRCLE_STROKE_COLOR);
        circleStrokeWidth = (int) typedArray.getDimension(R.styleable.CountDownProgressBar_circle_stroke_width, DEFAULT_CIRCLE_STROKE_WIDTH);
        circleRadius = (int) typedArray.getDimension(R.styleable.CountDownProgressBar_circler_radius, DEFAULT_CIRCLE_RADIUS);
        progressColor = typedArray.getColor(R.styleable.CountDownProgressBar_progress_color, DEFAULT_PROGRESS_COLOR);
        progressWidth = (int) typedArray.getDimension(R.styleable.CountDownProgressBar_progress_width, DEFAULT_PROGRESS_WIDTH);
        textColor = typedArray.getColor(R.styleable.CountDownProgressBar_text_color, DEFAULT_TEXT_COLOR);
        textSize = (int) typedArray.getDimension(R.styleable.CountDownProgressBar_text_size, DEFAULT_TEXT_SIZE);
        textValue = typedArray.getString(R.styleable.CountDownProgressBar_text_value);

        typedArray.recycle();

        if (Utils.isEmpty(textValue)) {
            textValue = DEFAULT_TEXT_VALUE;
        }

        progressOval = new RectF(0, 0, circleRadius * 2, circleRadius * 2);
    }

    private void setPaint() {
        //默认圆边框
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);//抗锯齿
        circlePaint.setDither(true);//防抖动
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(circleSolidColor);

        //默认圆边框
        circleStrokePaint = new Paint();
        circleStrokePaint.setAntiAlias(true);//抗锯齿
        circleStrokePaint.setDither(true);//防抖动
        circleStrokePaint.setStyle(Paint.Style.STROKE);
        circleStrokePaint.setStrokeWidth(circleStrokeWidth);
        circleStrokePaint.setColor(circleStrokeColor);//这里先画边框的颜色，后续再添加画笔画实心的颜色

        //默认圆上面的进度弧度
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setDither(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setColor(progressColor);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);//设置画笔笔刷样式

        //文字画笔
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setPaint();
        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());
        //默认圆
        canvas.drawCircle(circleRadius, circleRadius, circleRadius, circlePaint);

        //画默认圆边框
        canvas.drawCircle(circleRadius, circleRadius, circleRadius, circleStrokePaint);

        //画进度圆弧
        canvas.drawArc(progressOval, 360 * (1.0f - currentAngle) - 90, 360 * currentAngle, false, progressPaint);
        //画中间文字
        float textWidth = textPaint.measureText(textValue);
        float textHeight = (textPaint.descent() + textPaint.ascent()) / 2;
        canvas.drawText(textValue, circleRadius - textWidth / 2, circleRadius - textHeight, textPaint);

        canvas.restore();
    }

    /**
     * 如果该View布局的宽高开发者没有精确的告诉，则需要进行测量，如果给出了精确的宽高则我们就不管了
     *
     * @param widthMeasureSpec  widthMeasureSpec
     * @param heightMeasureSpec heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize;
        int heightSize;
        int strokeWidth = Math.max(circleStrokeWidth, progressWidth);
        if (widthMode != MeasureSpec.EXACTLY) {
            widthSize = getPaddingLeft() + circleRadius * 2 + strokeWidth + getPaddingRight();
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            heightSize = getPaddingTop() + circleRadius * 2 + strokeWidth + getPaddingBottom();
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

//    //属性动画
//    public void startCountDownTime(long countdownTime, final OnCountdownFinishListener countdownFinishListener) {
//        animator = ValueAnimator.ofFloat(0.0f, 1.0f);
//        //动画时长，让进度条在CountDown时间内正好从0-360走完，这里由于用的是CountDownTimer定时器，倒计时要想减到0则总时长需要多加1000毫秒，所以这里时间也跟着+1000ms
////        animator.setDuration(countdownTime + 1000);
//        animator.setInterpolator(new LinearInterpolator());//匀速
//        animator.setRepeatCount(0);//表示不循环，-1表示无限循环
//        //值从0-1.0F 的动画，动画时长为countdownTime，ValueAnimator没有跟任何的控件相关联，那也正好说明ValueAnimator只是对值做动画运算，而不是针对控件的，我们需要监听ValueAnimator的动画过程来自己对控件做操作
//        //添加监听器,监听动画过程中值的实时变化(animation.getAnimatedValue()得到的值就是0-1.0)
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                /**
//                 * 这里我们已经知道ValueAnimator只是对值做动画运算，而不是针对控件的，因为我们设置的区间值为0-1.0f
//                 * 所以animation.getAnimatedValue()得到的值也是在[0.0-1.0]区间，而我们在画进度条弧度时，设置的当前角度为360*currentAngle，
//                 * 因此，当我们的区间值变为1.0的时候弧度刚好转了360度
//                 */
//
//                Log.d(TAG, "onAnimationUpdate : " + animation.getAnimatedValue());
//                currentAngle = (float) animation.getAnimatedValue();
//
//                invalidate();//实时刷新view，这样我们的进度条弧度就动起来了
//            }
//        });
//
//        //还需要另一个监听，监听动画状态的监听器
//        animator.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                Log.d(TAG, "onAnimationStart");
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                //倒计时结束的时候，需要通过自定义接口通知UI去处理其他业务逻辑
//                Log.d(TAG, "onAnimationEnd");
//                if (countdownFinishListener != null) {
//                    countdownFinishListener.countdownFinished();
//                }
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//                Log.d(TAG, "onAnimationCancel");
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//                Log.d(TAG, "onAnimationRepeat");
//            }
//        });
//
//        //开启动画
//        animator.start();
//    }

    //CountDownTimer
    public void startCountDownTime(final long countdownTime, final OnCountdownFinishListener countdownFinishListener) {

        setCountDowning(true);
        mCountDownTimer = new CountDownTimer(countdownTime, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                currentAngle =(float) millisUntilFinished / countdownTime;
                invalidate();
//                Log.d(TAG, "onTick : " + millisUntilFinished + " ; currentAngle : " + currentAngle);
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "onFinish ");
                setCountDowning(false);
                if (countdownFinishListener != null) {
                    countdownFinishListener.countdownFinished();
                }
            }
        }.start();
    }

    public void stopCountDownTime() {
        Log.d(TAG, "stopCountDownTime");
//        if (animator != null) {
//            animator.cancel();
//        }
        setCountDowning(false);
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    /**
     * @return 是否正在计时
     */
    public boolean isCounDowning() {
        return mIsCountDowning;
    }

    public void setCountDowning(boolean mIsCountDowning) {
        this.mIsCountDowning = mIsCountDowning;
    }

    public interface OnCountdownFinishListener {
        void countdownFinished();
    }
}


