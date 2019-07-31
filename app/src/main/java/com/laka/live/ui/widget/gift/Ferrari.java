package com.laka.live.ui.widget.gift;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.util.Log;
import com.laka.live.util.Utils;

/**
 * Created by luwies on 16/4/14.
 * 法拉利动画效果
 */
public class Ferrari extends FrameLayout implements GiftAnimator {

    private float widthScale;

    private static final long DURATION_1 = 1000L;

    private static final long DURATION_2 = 1000L;

    private static final long DURATION_3 = 1000L;

    private static final long DURATION_4 = 600L;

    private static final long DURATION_5 = 370L;

    private static final long TOTAL_DURATION = DURATION_1 + DURATION_2 + DURATION_3 + DURATION_4 + DURATION_5;

    /*private static final long DURATION_6 = 1000L;

    private static final long DURATION_7 = 1000L;

    private static final long DURATION_8 = 1000L;

    private static final long DURATION_9 = 600L;

    private static final long DURATION_10 = 370L;*/

    private View mComeCar;

    private View mComeCarLight;

    private View mComeFrontWheel;

    private View mComeRearWheel;

    private View mBackCar;

    private View mBackCarLight;

    private View mBackFrontWheel;

    private View mBackRearWheel;

    private ValueAnimator mAnimator;

    private AnimatorSet mComeFrontWheelAnimatorSet;

    private AnimatorSet mComeRearWheelAnimatorSet;

    private AnimatorSet mBackFrontWheelAnimatorSet;

    private AnimatorSet mBackRearWheelAnimatorSet;

    private Animator mComeLightAnimator;

    private Animator mBackLightAnimator;

    private int mComeCarWidth;

    private TextView mComeName;

    private TextView mBackName;

    public Ferrari(Context context) {
        super(context);
        init();
    }

    public Ferrari(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Ferrari(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Ferrari(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        Context context = getContext();

        widthScale = Utils.getScreenWidth(context) / 1080f;

        mComeCarWidth = Utils.dip2px(context, 230f);
        mComeCar = LayoutInflater.from(context)
                .inflate(R.layout.ferrari_front, null, false);
        mComeCarLight = mComeCar.findViewById(R.id.light);
        mComeFrontWheel = mComeCar.findViewById(R.id.front_wheel);
        mComeRearWheel = mComeCar.findViewById(R.id.reae_wheel);
        mComeName = (TextView) mComeCar.findViewById(R.id.font_name).findViewById(R.id.name);
        mComeName.getPaint().setFakeBoldText(true);

        mBackCar = LayoutInflater.from(context)
                .inflate(R.layout.ferrari_tail, null, false);
        mBackCarLight = mBackCar.findViewById(R.id.light);
        mBackFrontWheel = mBackCar.findViewById(R.id.front_wheel);
        mBackRearWheel = mBackCar.findViewById(R.id.reae_wheel);
        mBackName = (TextView)  mBackCar.findViewById(R.id.tail_name).findViewById(R.id.name);
        mBackName.getPaint().setFakeBoldText(true);

        addView(mComeCar/*, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)*/);
        addView(mBackCar/*, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)*/);

        mComeCar.setPivotX(1f);
        mComeCar.setPivotY(0f);

        mBackCar.setPivotX(0f);
        mBackCar.setPivotY(0f);

        mComeCar.setVisibility(GONE);
        mBackCar.setVisibility(GONE);
    }

    @Override
    public void start() {
        start(null);
    }

    @Override
    public void start(Animator.AnimatorListener listener) {

        mComeCar.setVisibility(VISIBLE);

        stop();

        mAnimator = ValueAnimator.ofFloat(0f, 1f);
        if (listener != null) {
            mAnimator.addListener(listener);
        }
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                long currentTime = animation.getCurrentPlayTime();
                Log.error("test", "currentTime = " + currentTime);
                if (currentTime < TOTAL_DURATION) {
                    updateComeCar(currentTime);
                } else {
                    mBackCar.setVisibility(VISIBLE);
                    updateBackCar(currentTime);
                }
            }
        });
        mAnimator.setDuration(TOTAL_DURATION * 2);
        mAnimator.start();

        startWheelAnimator();

//        mComeCarLight.setAlpha(0f);
        mComeLightAnimator = getCarLightAnimator(mComeCarLight);
        mComeLightAnimator.setStartDelay(DURATION_1);
        mComeLightAnimator.start();

//        mBackCarLight.setAlpha(0f);
        mBackLightAnimator = getCarLightAnimator(mBackCarLight);
        mBackLightAnimator.setStartDelay(TOTAL_DURATION + DURATION_1);
        mBackLightAnimator.start();
    }

    private void updateComeCar(long time) {
        updateComeCarScale(time);
        updateComeCarX(time);
        updateComeCarY(time);
    }

    private void updateComeCarX(long currentTime) {
        float x;
        if (currentTime < DURATION_1) {
            x = -0.49104005f * currentTime + 1522.0801f;
        } else if (currentTime < DURATION_1 + DURATION_2) {
            x = 1031.04f;
        } else if (currentTime < DURATION_1 + DURATION_2 + DURATION_3) {
            x = -0.043200012f * currentTime + 1117.4401f;
        } else if (currentTime < DURATION_1 + DURATION_2 + DURATION_3 + DURATION_4) {
            x = -0.095999956f * currentTime + 1275.8398f;
        } else {
//            x = -2.5063784f * currentTime + 9953.202f;
//            x = -2.5141623f * currentTime + 9981.225f;
            x = -2.9033513f * currentTime + 11382.305f;
        }
        x = widthScale * x;
        x -= mComeCarWidth * mComeCar.getScaleX();
        Log.error("test", "x = " + x);
        mComeCar.setTranslationX(x);
    }

    private void updateComeCarY(long currentTime) {
        float y;
        if (currentTime < DURATION_1) {
            y = 0.12672003f * currentTime + 305.28f;
        } else if (currentTime < DURATION_1 + DURATION_2) {
            y = 432.00003f;
        } else if (currentTime < DURATION_1 + DURATION_2 + DURATION_3) {
            y = 0.01728f * currentTime + 397.44003f;
        } else if (currentTime < DURATION_1 + DURATION_2 + DURATION_3 + DURATION_4) {
            y = 0.035999957f * currentTime + 341.28015f;
        } else {
            y = 0.77837837f * currentTime - 2331.2822f;
        }

        y = widthScale * y;

        mComeCar.setTranslationY(y);
    }

    private Animator getCarLightAnimator(View target) {

        long duration = DURATION_2 + DURATION_3 + DURATION_4;
        int frameSize = (int) duration / 100;
        float alphas[] = new float[frameSize + 4];
        alphas[0] = 0f;
        alphas[1] = 1;
        int index;
        for (int i = 0; i < frameSize; i++) {
            index = i + 2;
            alphas[index] = index % 2 == 0 ? 0.65f : 1f;
        }
        index = frameSize + 2;
        alphas[index] = 1f;
        alphas[index + 1] = 0f;
        ObjectAnimator valueAnimator = ObjectAnimator.ofFloat(target, "alpha", alphas);
        valueAnimator.setDuration(duration);
        return valueAnimator;

    }

    private void updateComeCarScale(long currentTime) {
        float scale;
        if (currentTime < DURATION_1) {
            scale = 0.00039999998f * currentTime + 0.6f;
        } else if (currentTime < DURATION_1 + DURATION_2 + DURATION_3 + DURATION_4) {
            scale = 1.0f;
        } else {
            scale = 0.0008108104f * currentTime-1.9189174f;
        }
        mComeCar.setScaleX(scale);
        mComeCar.setScaleY(scale);
    }

    private void updateBackCar(long currentTime) {
        currentTime = currentTime - TOTAL_DURATION;
        updateBackCarX(currentTime);
        updateBackCarY(currentTime);
        updateBackCarScale(currentTime);
    }

    private void updateBackCarX(long currentTime) {
        float x;
        if (currentTime < DURATION_1) {
            x = 0.9446401f * currentTime - 864.00006f;
        } else if (currentTime < DURATION_1 + DURATION_2) {
            x = 80.64f;
        } else if (currentTime < DURATION_1 + DURATION_2 + DURATION_3) {
            x = 0.084960006f * currentTime - 89.280014f;
        } else if (currentTime < DURATION_1 + DURATION_2 + DURATION_3 + DURATION_4) {
            x = 0.117599994f * currentTime - 187.19998f;
        } else {
            x = 2.2806485f * currentTime - 7974.175f;
        }

        x = Utils.getScreenWidth(getContext()) / 1080f * x;
        mBackCar.setTranslationX(x);
    }

    private void updateBackCarY(long currentTime) {
        float y;
        if (currentTime < DURATION_1) {
            y = -0.10656f * currentTime + 835.2f;
        } else if (currentTime < DURATION_1 + DURATION_2) {
            y = 728.64f;
        } else if (currentTime < DURATION_1 + DURATION_2 + DURATION_3) {
            y = -0.012959961f * currentTime + 754.55994f;
        } else if (currentTime < DURATION_1 + DURATION_2 + DURATION_3 + DURATION_4) {
            y = -0.019200033f * currentTime + 773.28015f;
        } else {
            y = -0.2685405f * currentTime + 1670.9058f;
        }
        y = Utils.getScreenWidth(getContext()) / 1080f * y;
        mBackCar.setTranslationY(y);
    }

    private void updateBackCarScale(long currentTime) {
        float scale;
        if (currentTime < DURATION_1) {
            scale = -0.00020000005f * currentTime + 1.2f;
        } else if (currentTime < DURATION_1 + DURATION_2 + DURATION_3 + DURATION_4) {
            scale = 1.0f;
        } else {
//            scale = -0.0013513509f * currentTime + 5.864863f;
            scale = -0.00081081057f * currentTime + 3.918918f;
        }
        Log.error("test", " scale = " + scale);
        mBackCar.setScaleX(scale);
        mBackCar.setScaleY(scale);
    }

    private void startWheelAnimator() {
        mComeFrontWheelAnimatorSet = getComeCarWheelAnimatorSet(mComeFrontWheel);
//        mComeFrontWheelAnimatorSet.setDuration(TOTAL_DURATION);
        mComeRearWheelAnimatorSet = getComeCarWheelAnimatorSet(mComeRearWheel);
//        mComeRearWheelAnimatorSet.setDuration(TOTAL_DURATION);

        mBackFrontWheelAnimatorSet = getBackCarWheelAnimatorSet(mBackFrontWheel);

        mBackRearWheelAnimatorSet = getBackCarWheelAnimatorSet(mBackRearWheel);

        mComeFrontWheelAnimatorSet.start();
        mComeRearWheelAnimatorSet.start();
        mBackRearWheelAnimatorSet.start();
        mBackFrontWheelAnimatorSet.start();
    }

    private void endAnimator(Animator animator) {
        if (animator != null) {
            animator.end();
        }
    }

    private AnimatorSet getComeCarWheelAnimatorSet(View view) {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "rotation", 0f, -720f);
        animator1.setDuration(DURATION_1);

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "rotation", -720f, -1440f);
        animator2.setStartDelay(DURATION_2);
        animator2.setDuration(DURATION_3 + DURATION_4 + DURATION_5);
        animator2.setInterpolator(new AccelerateInterpolator());

        AnimatorSet set = new AnimatorSet();
        set.playSequentially(animator1, animator2);
        return set;
    }

    private AnimatorSet getBackCarWheelAnimatorSet(View view) {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "rotation", 0f, 720f);
        animator1.setStartDelay(TOTAL_DURATION);
        animator1.setDuration(DURATION_1);

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "rotation", 720f, 1440);
        animator2.setStartDelay(DURATION_2);
        animator2.setDuration(DURATION_3 + DURATION_4 + DURATION_5);
        animator2.setInterpolator(new AccelerateInterpolator());

        AnimatorSet set = new AnimatorSet();
        set.playSequentially(animator1, animator2);
        return set;
    }

    @Override
    public void stop() {
        if (mAnimator != null) {
            mAnimator.removeAllListeners();
        }
        endAnimator(mComeFrontWheelAnimatorSet);
        endAnimator(mComeRearWheelAnimatorSet);
        endAnimator(mBackFrontWheelAnimatorSet);
        endAnimator(mBackRearWheelAnimatorSet);
        endAnimator(mAnimator);

        endAnimator(mComeLightAnimator);
        endAnimator(mBackLightAnimator);
    }

    @Override
    public void setText(CharSequence text) {
        mComeName.setText(text);
        mBackName.setText(text);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }
}
