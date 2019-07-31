package com.laka.live.util;

import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

public class AnimationHelper {

    //向上平移进入动画
    public static TranslateAnimation getTranslateUpVisible() {
        TranslateAnimation mShowAction1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 3.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction1.setDuration(500);
        return mShowAction1;
    }

    //向下平移进入
    public static TranslateAnimation getTranslateDownVisible() {
        TranslateAnimation mShowAction1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction1.setDuration(500);
        return mShowAction1;
    }

    //向右平移
    public static TranslateAnimation getTranslateRightVisible() {
        TranslateAnimation mShowAction1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction1.setDuration(500);
        return mShowAction1;
    }

    //向左平移
    public static TranslateAnimation getTranslateLeftVisible() {
        TranslateAnimation mShowAction1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction1.setDuration(500);
        return mShowAction1;
    }

    //向左平移
    public static TranslateAnimation getTranslateLeftHide() {
        TranslateAnimation mShowAction1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction1.setDuration(500);
        return mShowAction1;
    }

    public static TranslateAnimation getTranslateUpHide() {
        TranslateAnimation mShowAction1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
        mShowAction1.setDuration(500);
        return mShowAction1;
    }

    //向下移出消失动画
    public static TranslateAnimation getTranslateDownHidden() {
        return getTranslateDownHidden(500);
    }

    public static TranslateAnimation getTranslateDownHidden(int duration) {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 3.0f);
        mHiddenAction.setDuration(duration);
        return mHiddenAction;
    }

    public static TranslateAnimation getTranslateUpHidden() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
        mHiddenAction.setDuration(500);
        return mHiddenAction;
    }

    //点赞伸缩动画
    public static ScaleAnimation getScaleAnimation(boolean isfillafter) {
        ScaleAnimation scale = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setInterpolator(new DecelerateInterpolator());
        if (isfillafter) {
            scale.setFillAfter(true);
        } else {
            scale.setFillAfter(false);
        }
        scale.setDuration(300);
        return scale;
    }

    public static ScaleAnimation getScaleAnimation(float from, float end) {
        ScaleAnimation scale = new ScaleAnimation(from, end, from, end,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setInterpolator(new DecelerateInterpolator());
        scale.setFillAfter(false);
        scale.setDuration(500);
        return scale;
    }

    //偷窥抖动动画
    public static ScaleAnimation getPeepScaleAnimation(boolean isfillafter) {
        ScaleAnimation scale = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setInterpolator(new CycleInterpolator(1f));
        scale.setRepeatCount(5);
        if (isfillafter) {
            scale.setFillAfter(true);
        } else {
            scale.setFillAfter(false);
        }
        scale.setDuration(200);
        return scale;
    }

    public static TranslateAnimation getPeepShakeAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, 10);
        translateAnimation.setInterpolator(new CycleInterpolator(7f));
        translateAnimation.setDuration(300);
        return translateAnimation;
    }

    //点赞伸缩动画
    public static ScaleAnimation getScaleAnimationShow(boolean isfillafter) {
        ScaleAnimation scale = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setInterpolator(new DecelerateInterpolator());
        if (isfillafter) {
            scale.setFillAfter(true);
        } else {
            scale.setFillAfter(false);
        }
        scale.setDuration(500);
        return scale;
    }

    //淡入动画
    public static AlphaAnimation getAlphaAnimationShow() {
        AlphaAnimation alphaShow = new AlphaAnimation(0, 1);
        alphaShow.setDuration(500);
        return alphaShow;
    }

    public static AlphaAnimation getAlphaAnimationShow(int durationMillis) {
        AlphaAnimation alphaShow = new AlphaAnimation(0, 1);
        alphaShow.setDuration(durationMillis);
        return alphaShow;
    }

    public static AlphaAnimation getAlphaAnimationShow(int durationMillis, boolean fillAfter) {
        AlphaAnimation alphaAnimation = getAlphaAnimationShow(durationMillis);
        alphaAnimation.setFillAfter(fillAfter);
        return alphaAnimation;
    }

    public static AlphaAnimation getAlphaAnimationHide(int durationMillis, boolean fillAfter) {
        AlphaAnimation alphaAnimation = getAlphaAnimationHide(durationMillis);
        alphaAnimation.setFillAfter(fillAfter);
        return alphaAnimation;
    }

    //淡出动画
    public static AlphaAnimation getAlphaAnimationHide() {
        AlphaAnimation alphaHide = new AlphaAnimation(1, 0);
        alphaHide.setDuration(500);
        return alphaHide;
    }

    //淡出动画
    public static AlphaAnimation getAlphaAnimationHide(long durationMillis) {
        AlphaAnimation alphaHide = new AlphaAnimation(1, 0);
        alphaHide.setDuration(durationMillis);
        return alphaHide;
    }

    public static RotateAnimation getRotateAnimation() {
        RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(false);
        return rotateAnimation;
    }

    public static RotateAnimation getRotateAnimation(float startAngle, float endAngle) {
        RotateAnimation rotateAnimation = new RotateAnimation(startAngle, endAngle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(200);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setFillAfter(true);
        return rotateAnimation;
    }

    /**
     * 切换动画实现
     */
    public static void animateToggle(final boolean isExpand, int viewHeight,final ViewGroup layoutView,
                                     long animationDuration) {

        ValueAnimator heightAnimation = isExpand ?
                ValueAnimator.ofFloat(0f, viewHeight) : ValueAnimator.ofFloat(viewHeight, 0f);
        heightAnimation.setDuration(animationDuration);

        heightAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();
                ViewUtils.setLayoutParams(layoutView, (int) val);
            }

        });

        heightAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isExpand) {
                    ViewUtils.setLayoutParams(layoutView, 0);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        heightAnimation.start();
    }


}
