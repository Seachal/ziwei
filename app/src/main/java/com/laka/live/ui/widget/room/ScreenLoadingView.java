
package com.laka.live.ui.widget.room;


import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.laka.live.R;
import com.laka.live.util.Utils;

/**
 * 直播加载荧屏滚动的view
 * 
 * @author xiaoyulong
 * @version 创建时间：2015-7-15 下午5:03:46
 */
public class ScreenLoadingView extends RelativeLayout {

    private Context mContext;

    private ImageView startLoadingImg1, startLoadingImg2, startLoadingImg3;

    private AnimationSet animationSet1, animationSet2, animationSet3;

    private boolean flag = true;

    public Handler mHandler = new Handler();

    public ScreenLoadingView(Context context) {
        super(context);
        this.mContext = context;
        initUI();
    }

    public ScreenLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initUI();
    }

    private void initUI() {
        startLoadingImg1 = new ImageView(mContext);
        startLoadingImg2 = new ImageView(mContext);
        startLoadingImg3 = new ImageView(mContext);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        layoutParams.height = Utils.dip2px(mContext, 450);
        startLoadingImg1.setLayoutParams(layoutParams);
        startLoadingImg2.setLayoutParams(layoutParams);
        startLoadingImg3.setLayoutParams(layoutParams);
        // Bitmap bitmapLeft = BitmapFactory.decodeResource(getResources(),
        // R.drawable.lowding_left);
        // Bitmap bitmapRight = BitmapFactory.decodeResource(getResources(),
        // R.drawable.lowding_right);
        // BitmapDrawable bdLeft = new BitmapDrawable(bitmapLeft);
        // BitmapDrawable bdRight = new BitmapDrawable(bitmapRight);
        // bdLeft.setTileModeXY(TileMode.CLAMP, TileMode.CLAMP);
        // bdRight.setTileModeXY(TileMode.CLAMP, TileMode.CLAMP);
        // startLoadingImg1.setBackgroundDrawable(bdLeft);
        // startLoadingImg2.setBackgroundDrawable(bdRight);
        startLoadingImg1.setBackgroundResource(R.drawable.lowding_left);
        startLoadingImg2.setBackgroundResource(R.drawable.lowding_right);
        startLoadingImg3.setBackgroundResource(R.drawable.lowding_left);
        this.addView(startLoadingImg1);
        this.addView(startLoadingImg2);
        this.addView(startLoadingImg3);

        animationSet1 = new AnimationSet(true);
        animationSet2 = new AnimationSet(true);
        animationSet3 = new AnimationSet(true);

        TranslateAnimation ta1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -2f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f);
        ta1.setDuration(12000);
        animationSet1.addAnimation(ta1);
        animationSet1.setFillAfter(true);
        animationSet1.setInterpolator(new LinearInterpolator());
        TranslateAnimation ta2 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f);
        ta2.setDuration(12000);
        animationSet2.addAnimation(ta2);
        animationSet2.setFillAfter(true);
        animationSet2.setInterpolator(new LinearInterpolator());

        TranslateAnimation ta3 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 2.0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f);
        ta3.setDuration(12000);
        animationSet3.addAnimation(ta3);
        animationSet3.setFillAfter(true);
        animationSet3.setInterpolator(new LinearInterpolator());
    }

    private Runnable startLoadingRunnable = new Runnable() {

        @Override
        public void run() {
            if (animationSet1 != null && animationSet2 != null && animationSet3 != null
                    && startLoadingImg1 != null && startLoadingImg2 != null
                    && startLoadingImg3 != null) {
                startLoadingImg1.startAnimation(animationSet1);
                startLoadingImg2.startAnimation(animationSet2);
                startLoadingImg3.startAnimation(animationSet3);
                if (flag) {
                    mHandler.postDelayed(startLoadingRunnable, 11990);
                }
            }
        }

    };

    /**
     * 开始滚动
     */
    public void startRoll() {
        this.flag = true;
        if (mHandler != null && startLoadingRunnable != null) {
            mHandler.post(startLoadingRunnable);
        }
    }

    /**
     * 停止滚动
     */
    public void stopRoll() {
        this.flag = false;
        if (mHandler != null && startLoadingRunnable != null) {
            mHandler.removeCallbacks(startLoadingRunnable);
        }
    }
}
