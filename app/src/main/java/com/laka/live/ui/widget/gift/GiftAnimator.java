package com.laka.live.ui.widget.gift;

import android.animation.Animator;

/**
 * Created by luwies on 16/4/14.
 */
public interface GiftAnimator  {

    void start();

    void start(Animator.AnimatorListener listener);

    void stop();

    void setText(CharSequence text);
}
