package com.laka.live.ui.widget.Effects;

import android.view.View;

import com.gitonway.lee.niftymodaldialogeffects.lib.effects.BaseEffects;
import com.nineoldandroids.animation.ObjectAnimator;


/**
 * Created by Administrator on 2016/7/7.
 */
public class FadeOut extends BaseEffects {

    @Override
    protected void setupAnimation(View view) {
        getAnimatorSet().playTogether(
                ObjectAnimator.ofFloat(view,"alpha",1,0).setDuration(mDuration)

        );
    }
}
