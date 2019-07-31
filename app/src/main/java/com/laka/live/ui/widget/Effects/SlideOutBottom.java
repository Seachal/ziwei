package com.laka.live.ui.widget.Effects;


import android.view.View;

import com.gitonway.lee.niftymodaldialogeffects.lib.effects.BaseEffects;
import com.nineoldandroids.animation.ObjectAnimator;


/**
 * Created by Administrator on 2016/7/7.
 */
public class SlideOutBottom  extends BaseEffects {

    @Override
    protected void setupAnimation(View view) {
        getAnimatorSet().playTogether(
                ObjectAnimator.ofFloat(view, "translationY", 0, 300).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 1, 0).setDuration(mDuration*3/2)

        );
    }
}