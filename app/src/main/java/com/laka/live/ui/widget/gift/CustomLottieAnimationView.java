package com.laka.live.ui.widget.gift;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.OnCompositionLoadedListener;
import com.laka.live.gift.GiftResManager;
import com.laka.live.util.Log;
import com.laka.live.util.Utils;

/**
 * @ClassName: 类（或接口）名
 * @Description: 描述
 * @Author 关健
 * @Version 客户端版本
 * @Date 2017/9/1
 */

public class CustomLottieAnimationView extends LottieAnimationView implements GiftAnimator {
    private static final String TAG = "CustomLottieAnimationView";

    public CustomLottieAnimationView(Context context) {
        super(context);
    }

    public CustomLottieAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLottieAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void start() {

    }

    @Override
    public void start(final Animator.AnimatorListener listener) {
        String animPath = GiftResManager.getInstance().getBigAnimPath(giftID);
        if (Utils.isEmpty(animPath)) {
            Log.d(TAG, " 没动画");
            listener.onAnimationEnd(null);
            return;
        }
        Log.d(TAG, " animPath1=" + animPath);
        setImageAssetsFolder("giftAnims/" + giftID + "/images");
        LottieComposition.Factory.fromAssetFileName(getContext(), animPath, new OnCompositionLoadedListener() {
            @Override
            public void onCompositionLoaded(@Nullable LottieComposition composition) {
                if (composition == null) {
                    Log.d(TAG, " 加载动画失败");
                } else {
                    Log.d(TAG, " 加载动画成功");
                    setComposition(composition);
                    loop(false);
                    setProgress(0f);
                    playAnimation();
                    addAnimatorListener(listener);
                }
            }
        });
    }

    @Override
    public void stop() {
        cancelAnimation();
    }

    @Override
    public void setText(CharSequence text) {

    }

    String giftID;

    public void setGiftID(String giftID) {
        this.giftID = giftID;

    }
}
