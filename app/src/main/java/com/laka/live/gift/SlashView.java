
package com.laka.live.gift;


import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.laka.live.R;
import com.laka.live.util.Log;
import com.laka.live.util.Utils;

/**
 * 进度条斜杠条纹的view
 *
 * @author xiaoyulong
 * @version 创建时间：2015-7-15 下午5:03:46
 */
public class SlashView extends RelativeLayout {

    private static final String TAG = "SlashView";
    private Context mContext;

    private ImageView startLoadingImg1, startLoadingImg2, startLoadingImg3;
    private TranslateAnimation ta1,ta2,ta3;
    private AnimationSet animationSet1, animationSet2, animationSet3;

    private boolean flag = true;

    public Handler mHandler = new Handler();

    public SlashView(Context context) {
        super(context);
        this.mContext = context;
        initUI();
    }

    public SlashView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initUI();
    }

    private void initUI() {
        startLoadingImg1 = new ImageView(mContext);
        startLoadingImg2 = new ImageView(mContext);
        startLoadingImg3 = new ImageView(mContext);
        startLoadingImg1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        startLoadingImg2.setScaleType(ImageView.ScaleType.CENTER_CROP);
        startLoadingImg3.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
//        layoutParams.height = Utils.dip2px(mContext, 450);
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
//        startLoadingImg1.setBackgroundResource(R.drawable.lowding_left);
//        startLoadingImg2.setBackgroundResource(R.drawable.lowding_right);
//        startLoadingImg3.setBackgroundResource(R.drawable.lowding_left);
        startLoadingImg1.setImageResource(R.drawable.live_special_bar_slash_left);
        startLoadingImg2.setImageResource(R.drawable.live_special_bar_slash_right);
        startLoadingImg3.setImageResource(R.drawable.live_special_bar_slash_left);
        this.addView(startLoadingImg1);
        this.addView(startLoadingImg2);
        this.addView(startLoadingImg3);

        initAnim();

        setVisibility(View.INVISIBLE);
    }

    private void initAnim() {
        animationSet1 = new AnimationSet(true);
        animationSet2 = new AnimationSet(true);
        animationSet3 = new AnimationSet(true);
        ta1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -2f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f);
        ta1.setDuration(24000);
        animationSet1.addAnimation(ta1);
        animationSet1.setFillAfter(true);
        animationSet1.setInterpolator(new LinearInterpolator());
        ta2 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f);
        ta2.setDuration(24000);
        animationSet2.addAnimation(ta2);
        animationSet2.setFillAfter(true);
        animationSet2.setInterpolator(new LinearInterpolator());

        ta3 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 2.0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f);
        ta3.setDuration(24000);
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
//                if (flag) {
//                    mHandler.postDelayed(startLoadingRunnable, 11990);
//                }
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
        if (totalWidth > 0) {//复原
//            LayoutParams lp = (LayoutParams) getLayoutParams();
//            lp.width = totalWidth;
//            setLayoutParams(lp);
            setMove(0);
        }

    }

    int curAmount = 0;
    int tag1 = 99;
    int tag2 = 520;
    int tag3 = 1314;
    int step1 = 0;
    int step2 = 0;
    int step3 = 0;
    int totalWidth;

    public void initProgress(final int tag1, final int tag2, final int tag3) {
        curAmount = 0;
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.tag3 = tag3;
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(totalWidth>0){
                    return;
                }
                totalWidth = startLoadingImg1.getMeasuredWidth();
                if (totalWidth > 0) {
                    step1 = totalWidth / 5 / tag1;
                    step2 = (totalWidth / 5)*2 / (tag2 - tag1);
                    step3 = (totalWidth / 5)*2 / (tag3 - tag2);
                    Log.d(TAG, "initProgress totalWidth=" + totalWidth + " step1=" + step1 + " step2=" + step2 + " step3=" + step3);
                    setMove(totalWidth);

//                    if(totalWidth>0&&startLoadingImg1.getLayoutParams().width<0){
//                        LayoutParams lp1= (LayoutParams) startLoadingImg1.getLayoutParams();
//                        lp1.width = totalWidth*2;
//                        startLoadingImg1.setLayoutParams(lp1);
//
//                        LayoutParams lp2= (LayoutParams) startLoadingImg2.getLayoutParams();
//                        lp2.width = totalWidth*2;
//                        startLoadingImg2.setLayoutParams(lp2);
//
//                        LayoutParams lp3= (LayoutParams) startLoadingImg3.getLayoutParams();
//                        lp3.width = totalWidth*2;
//                        startLoadingImg3.setLayoutParams(lp3);
//
//                        initAnim();
//                        startLoadingImg1.startAnimation(animationSet1);
//                        startLoadingImg2.startAnimation(animationSet2);
//                        startLoadingImg3.startAnimation(animationSet3);
//                    }
                }
//                LayoutParams lp = (LayoutParams) getLayoutParams();
//                lp.leftMargin = -totalWidth;
//                setLayoutParams(lp);
            }
        }, 300);
    }

    private void setMove(int totalWidth) {
        setPadding(0, 0, totalWidth, 0);
    }

//    public void addProgress(int i) {
//        curAmount += i;
//        refreshProgress();
//    }

    private void refreshProgress() {
        setVisibility(View.VISIBLE);
        LayoutParams lp = (LayoutParams) getLayoutParams();
        float four = (float) totalWidth / 5f;
        if (curAmount <= tag1) {
//            int padding = (int) (totalWidth - step1 * (curAmount-1));
            int padding = (int) (totalWidth -four * ((float)curAmount/(float)tag1));
            setMove(padding);
//            lp.leftMargin= (int) (-totalWidth+four/tag1*curAmount);
            Log.d(TAG, " step1  padding=" + padding + " curAmount=" + curAmount+" iv width="+startLoadingImg1.getLayoutParams().width);
        } else if (curAmount <= tag2) {
//            int padding =(int) (totalWidth - four - step2 * (curAmount -1- tag1));
            int padding = (int) (totalWidth - four -four *2* ((float)(curAmount-tag1)/(float)(tag2-tag1)));
            setMove(padding);
//            lp.leftMargin= (int) (-totalWidth+four+four/(tag2-tag1)*(curAmount-tag1));
            Log.d(TAG, " step2  padding=" + padding + " curAmount=" + curAmount+" iv width="+startLoadingImg1.getLayoutParams().width);
        } else if (curAmount <= tag3) {
//            int padding =(int) (totalWidth - four*3 - step3 * (curAmount-1 - tag2));
            int padding = (int) (totalWidth - four*3 -four *2* ((float)(curAmount-tag2)/(float)(tag3-tag2)));
            setMove(padding);
//            lp.leftMargin= (int) (-totalWidth+four+four+four*2/(tag3-tag2)*(curAmount-tag2));
            Log.d(TAG, " step3  padding=" + padding+ " curAmount=" + curAmount+" iv width="+startLoadingImg1.getLayoutParams().width);
        } else {
            Log.d(TAG, "超过三阶段");
        }
//        setLayoutParams(lp);

        invalidate();
        startRoll();
    }

    public void setAmount(int count) {
        curAmount = count;
        refreshProgress();
    }
}
