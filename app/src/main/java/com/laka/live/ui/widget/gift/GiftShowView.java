package com.laka.live.ui.widget.gift;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.OnCompositionLoadedListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.application.LiveApplication;
import com.laka.live.bean.GiftInfo;
import com.laka.live.gift.GiftResManager;
import com.laka.live.manager.BytesReader;
import com.laka.live.ui.widget.LevelText;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by crazyguan on 2016/4/6.
 */
public class GiftShowView extends RelativeLayout implements View.OnClickListener {
    private static final String TAG = "RoomGiftShowView";
    private Context context;
    private LinearLayout view;
    //礼物显示区 第一条
    RelativeLayout rlGiftShow, rlGiftContainer;
    LottieAnimationView ivGiftIcon;
    SimpleDraweeView ivGiftHead;
    TextView tvGiftName, tvGiftContent, tvGiftCount;
    LevelText tvLevel;
    BytesReader.GiftMessage curShowGift;

    //礼物显示区 第二条
    RelativeLayout rlGiftShow1, rlGiftContainer1;
    LottieAnimationView ivGiftIcon1;
    SimpleDraweeView ivGiftHead1;
    TextView tvGiftName1, tvGiftContent1, tvGiftCount1;
    LevelText tvLevel1;
    BytesReader.GiftMessage curShowGift1;

    public GiftShowView(Context context) {
        super(context);
        this.context = context;
        initUI();
    }

    public GiftShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initUI();
    }

    Handler handler = new Handler() {

    };

    private void initUI() {
        giftResManager = GiftResManager.getInstance();
        view = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.layout_room_gift_show, null);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        addView(view, params);

        rlGiftContainer = (RelativeLayout) findViewById(R.id.rl_gift);
        rlGiftShow = (RelativeLayout) findViewById(R.id.rl_gift_show);
        rlGiftShow.setVisibility(View.INVISIBLE);
        ivGiftHead = (SimpleDraweeView) findViewById(R.id.iv_gift_head);
        ivGiftHead.setOnClickListener(this);
        ivGiftIcon = (LottieAnimationView) findViewById(R.id.iv_gift_icon);
        hideIvGift();
        tvGiftName = (TextView) findViewById(R.id.tv_gift_name);
        tvLevel = (LevelText) findViewById(R.id.tv_level);
        tvGiftContent = (TextView) findViewById(R.id.tv_gift_content);
        tvGiftCount = (TextView) findViewById(R.id.tv_gift_count);
        Typeface fontFace = Typeface.createFromAsset(context.getAssets(),
                "fonts/CakeSans-bold.ttf");
        tvGiftCount.setTypeface(fontFace);

        //第二条
        rlGiftContainer1 = (RelativeLayout) findViewById(R.id.rl_gift1);
        rlGiftShow1 = (RelativeLayout) findViewById(R.id.rl_gift_show1);
        rlGiftShow1.setVisibility(View.INVISIBLE);
        ivGiftHead1 = (SimpleDraweeView) findViewById(R.id.iv_gift_head1);
        ivGiftHead1.setOnClickListener(this);
        ivGiftIcon1 = (LottieAnimationView) findViewById(R.id.iv_gift_icon1);
        hideIvGift1();
        tvGiftName1 = (TextView) findViewById(R.id.tv_gift_name1);
        tvLevel1 = (LevelText) findViewById(R.id.tv_level1);
        tvGiftContent1 = (TextView) findViewById(R.id.tv_gift_content1);
        tvGiftCount1 = (TextView) findViewById(R.id.tv_gift_count1);
        tvGiftCount1.setTypeface(fontFace);

        giftShowX = Utils.dip2px(context, 6);
        getTranslateXDistance();
        view.setAnimationCacheEnabled(false);
    }

    private float GIFT_COUNT_SCALE = 1.3f;
    private float GIFT_COUNT_BIG_SCALE = 2.5f;
    /**
     * 礼物出现动画
     */
    private AnimatorSet animGiftShow, animGiftHide, animGiftAddCount, animGiftShow1, animGiftHide1, animGiftAddCount1;//整个显示和消息动画

    private ValueAnimator out;
    private ValueAnimator out1;
    private float giftIconDesX, rlGiftDesY, giftIconDesX1, rlGiftDesY1;
    private long giftAnimDuration = 300;
    private int giftShowCnt, giftShowCnt1;
    ObjectAnimator in, giftIn, anim1x, anim1y, anim2x, anim2y, anim3x, anim3y, anim4n, outHide, in1, giftIn1, anim1x1, anim1y1, anim2x1, anim2y1, anim3x1, anim3y1, anim4n1, outHide1;
    private int giftShowX;
    private GiftResManager giftResManager;

    private void getTranslateXDistance() {
    }

    long DEFAULT_GIFT_COUNT_DURATION = 600;
    long MIN_GIFT_COUNT_DURATION = 100;
    long giftCountDuration = DEFAULT_GIFT_COUNT_DURATION / 4;
    long giftCountDuration1 = DEFAULT_GIFT_COUNT_DURATION / 4;

    private void initShowGiftAnimation() {
        in = ObjectAnimator.ofFloat(rlGiftShow, "x", -300, giftShowX).setDuration(giftAnimDuration);//0
        giftIn = ObjectAnimator.ofFloat(ivGiftIcon, "x", 0, giftIconDesX).setDuration(giftAnimDuration);
        anim1x = ObjectAnimator.ofFloat(tvGiftCount, "scaleX", GIFT_COUNT_BIG_SCALE, 1f).setDuration(giftCountDuration);
        anim1y = ObjectAnimator.ofFloat(tvGiftCount, "scaleY", GIFT_COUNT_BIG_SCALE, 1f).setDuration(giftCountDuration);
        anim2x = ObjectAnimator.ofFloat(tvGiftCount, "scaleX", 1f, GIFT_COUNT_SCALE).setDuration(giftCountDuration);
        anim2y = ObjectAnimator.ofFloat(tvGiftCount, "scaleY", 1f, GIFT_COUNT_SCALE).setDuration(giftCountDuration);
        anim3x = ObjectAnimator.ofFloat(tvGiftCount, "scaleX", GIFT_COUNT_SCALE, 1f).setDuration(giftCountDuration);
        anim3y = ObjectAnimator.ofFloat(tvGiftCount, "scaleY", GIFT_COUNT_SCALE, 1f).setDuration(giftCountDuration);
        anim4n = ObjectAnimator.ofFloat(tvGiftCount, "scaleY", 1f, 1f).setDuration(giftCountDuration);
        out = ObjectAnimator.ofFloat(rlGiftShow, "y", rlGiftDesY, rlGiftDesY - 100).setDuration(giftAnimDuration);
        outHide = ObjectAnimator.ofFloat(rlGiftShow, "alpha", 1.0f, 1.0f).setDuration(giftAnimDuration);
        in.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                rlGiftShow.setVisibility(View.VISIBLE);
                hideIvGift();
                tvGiftCount.setVisibility(View.INVISIBLE);
                isAniming = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                showIvGift();
//                Log.d(TAG,"ivGiftIcon show");
                showAnimGiftAddCount(curShowGift);
            }
        });

        animGiftAddCount = new AnimatorSet();
        animGiftAddCount.play(anim1x).with(anim1y);
        animGiftAddCount.play(anim2x).after(anim1y);
        animGiftAddCount.play(anim2y).after(anim1y);
        animGiftAddCount.play(anim3x).after(anim2x);
        animGiftAddCount.play(anim3y).after(anim2x);
        animGiftAddCount.play(anim4n).after(anim3y);
        animGiftAddCount.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isAniming = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
//                isAniming = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (checkQueue()) {
                    return;
                }
                isAniming = false;
                handler.postDelayed(hideGiftRunnable, 4000);

            }
        });
    }

    private void hideIvGift() {
        ivGiftIcon.setVisibility(View.INVISIBLE);
        if (curGiftDrawable != null) {
            curGiftDrawable.stop();
        }
        if(ivGiftIcon.isAnimating()){
            ivGiftIcon.cancelAnimation();
        }
    }

    AnimationDrawable curGiftDrawable;

    private void showIvGift() {
        ivGiftIcon.setVisibility(View.VISIBLE);
        Log.d(TAG, "showIvGift ivGiftIcon");
//        if(ivGiftIcon.getDrawable() instanceof AnimationDrawable){
//            curGiftDrawable = (AnimationDrawable) ivGiftIcon.getDrawable();
//            curGiftDrawable.start();
//        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showGiftAnimation(BytesReader.GiftMessage message) {
        if (isStop) {
            return;
        }
//        int giftIndex = Integer.parseInt(message.giftID) - 25;
//        if (giftIndex < 0 || giftIndex >= GiftGridView.giftIconRes.length) {
//            return;
//        }
//        ivGiftIcon.setImageResource(GiftGridView.giftIconRes[giftIndex]);
//        ImageUtil.loadAssetsImage(GiftGridView.getSmallAnimPath(message.giftID), ivGiftIcon, null);

        String animPath = giftResManager.getSmallAnimPath(message.giftID);
        if(Utils.isEmpty(animPath)){
            Log.d(TAG," 没动画");
            return;
        }
        Log.d(TAG," animPath1="+animPath);
        ivGiftIcon.setImageAssetsFolder("giftAnims/"+message.giftID+"/images");

        if(LiveApplication.getInstance().isShowGiftAnim){
            LottieComposition.Factory.fromAssetFileName(getContext(), animPath, new OnCompositionLoadedListener() {
                @Override
                public void onCompositionLoaded(@Nullable LottieComposition composition) {
                    if(composition==null){
                        Log.d(TAG," 加载动画失败");
                    }else{
                        Log.d(TAG," 加载动画成功");
                        ivGiftIcon.setComposition(composition);
                        ivGiftIcon.loop(true);
                        ivGiftIcon.setProgress(0f);
                        ivGiftIcon.playAnimation();
                    }
                }
            });
        }else{
            ivGiftIcon.setImageResource(giftResManager.getGift(message.giftID).getImageRes());
        }


//        if (!Utils.isEmpty(animPath)) {
//            if (animPath.startsWith("giftAnims")) {
//                ImageUtil.loadAssetsImage(animPath, ivGiftIcon, null);
//            } else {
//                ImageUtil.loadLocalImage(animPath, ivGiftIcon, null);
//            }
//        } else {
//            //没有动画资源 显示静态图
////            ImageUtil.loadResImage(giftResManager.);
//            ImageUtil.loadImage(ivGiftIcon, giftResManager.getImageResByGiftId(message.giftID));
//            //补充下载动画
//            giftResManager.downloadGift(message.giftID);
//        }


//        ivGiftIcon.setImageDrawable(ImageUtil.getDrawable(context,GiftGridView.giftAnimRes[giftIndex]));
        tvGiftContent.setText("送" + giftResManager.getUnitByGiftId(message.giftID) + giftResManager.getNameByGiftId(message.giftID));
        tvGiftCount.setText("x " + message.count);
        tvGiftName.setText(message.nickName);
        tvLevel.setLevel(message.level);
        giftIconDesX = ivGiftIcon.getLeft() + ResourceHelper.getDimen(R.dimen.space_10);
        if (rlGiftDesY == 0) {
            rlGiftDesY = rlGiftShow.getTop();
        }
        initShowGiftAnimation();
//        giftShowCnt = 1;
//        tvGiftCount.setText("x" + giftShowCnt);
        handler.removeCallbacks(hideGiftRunnable);
//        Log.d(TAG,"showGiftAnimation giftIconDesX="+giftIconDesX+" rlGiftDesX="+rlGiftDesX);
        if (animGiftShow != null && animGiftShow.isRunning()) {
            animGiftShow.cancel();
            animGiftShow.end();
            animGiftShow = null;
        }

//        rlGiftShow.setY(getBottom() - rlGiftShow.getHeight() - Utils.dip2px(context, 60f));
        rlGiftShow.setVisibility(View.VISIBLE);
//        ivGiftIcon.setImageResource(chooseGift.getIconRes());
        ImageUtil.loadImage(ivGiftHead, message.avatar);

        animGiftShow = new AnimatorSet();
//        animation.play(in).with(giftIn);
//        animGiftShow.play(giftIn).after(in);
        animGiftShow.play(in);
        animGiftShow.start();
        animGiftShow.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isAniming = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //4秒后自动隐藏
//                handler.postDelayed(hideGiftRunnable, 4000);
            }
        });

        //如果数字刷新了，重新计算取消时间
    }

    public void showAnimGiftAddCount(BytesReader.GiftMessage message) {
        if (isStop) {
            return;
        }
        rlGiftShow.setVisibility(View.VISIBLE);
        isAniming = true;
//        if (animGiftAddCount != null && animGiftAddCount.isRunning()) {
//            animGiftAddCount.cancel();
//            animGiftAddCount.end();
//            animGiftAddCount = null;
//        }
        handler.removeCallbacks(hideGiftRunnable);
//        if (message == null) {
//            message = curShowGift;
//        }
        if (message != null)
            tvGiftCount.setText("x " + message.count);//String.format(ResourceHelper.getString(R.string.x_n)

        tvGiftCount.setVisibility(View.VISIBLE);
//        animGiftAddCount = new AnimatorSet();
//        animGiftAddCount.play(anim1x).with(anim1y);
//        animGiftAddCount.play(anim2x).after(anim1y);
//        animGiftAddCount.play(anim2y).after(anim1y);
//        animGiftAddCount.play(anim3x).after(anim2x);
//        animGiftAddCount.play(anim3y).after(anim2x);
        if (animGiftAddCount != null)
            animGiftAddCount.start();
//        giftShowCnt++;
//        Log.d(TAG,"giftShowCnt="+giftShowCnt+" tvGiftCount="+tvGiftCount.getText().toString());
    }

    private boolean checkQueue() {
        BytesReader.GiftMessage message = queue.poll();
        if (message != null) {
            curShowGift = message;
            Log.d(TAG, "继续播放下个动画 id=" + message.giftID + " queue=" + queue.size() + " count=" + message.count);
            //设置动画时间
            setNumberAnimDuration(0);
            showAnimGiftAddCount(message);
            return true;
        }
        return false;
    }

    private void setNumberAnimDuration(int index) {
        if (index == 0) {
            int lastSize = queue.size();
            long defaultDuration = DEFAULT_GIFT_COUNT_DURATION / 4;// 600ms/4
            if (lastSize < 2) {
                giftCountDuration = defaultDuration;
            } else if (lastSize < 5) {
                giftCountDuration = (long) (defaultDuration * 0.5f);//2倍速
            } else if (lastSize < 10) {
                giftCountDuration = (long) (defaultDuration * 0.33f);//3倍速
            } else {
                giftCountDuration = (long) (defaultDuration * 0.1f);//10倍速
            }
            anim1x.setDuration(giftCountDuration);
            anim1y.setDuration(giftCountDuration);
            anim2x.setDuration(giftCountDuration);
            anim2y.setDuration(giftCountDuration);
            anim3x.setDuration(giftCountDuration);
            anim3y.setDuration(giftCountDuration);
            anim4n.setDuration(giftCountDuration);
            Log.d(TAG, "lastSize=" + lastSize + " giftCountDuration=" + giftCountDuration);
        } else {
            int lastSize = queue1.size();
            long defaultDuration = DEFAULT_GIFT_COUNT_DURATION / 4;
            if (lastSize < 2) {
                giftCountDuration1 = defaultDuration;
            } else if (lastSize < 5) {
                giftCountDuration1 = (long) (defaultDuration * 0.5f);//2倍速
            } else if (lastSize < 10) {
                giftCountDuration1 = (long) (defaultDuration * 0.33f);//3倍速
            } else {
                giftCountDuration1 = (long) (defaultDuration * 0.1f);//10倍速
            }
            anim1x1.setDuration(giftCountDuration1);
            anim1y1.setDuration(giftCountDuration1);
            anim2x1.setDuration(giftCountDuration1);
            anim2y1.setDuration(giftCountDuration1);
            anim3x1.setDuration(giftCountDuration1);
            anim3y1.setDuration(giftCountDuration1);
            anim4n1.setDuration(giftCountDuration1);
            Log.d(TAG, "lastSize=" + lastSize + " giftCountDuration1=" + giftCountDuration1);
        }

    }


    Runnable hideGiftRunnable = new Runnable() {
        @Override
        public void run() {
            hideGiftAnim();
        }
    };

    /**
     * 礼物消失动画
     */
    private void hideGiftAnim() {
//        if(btnSendGiftMulti.getVisibility()==View.VISIBLE){
//            return;
//        }
//        Log.d(TAG,"hideGiftAnima");
//        out = ObjectAnimator.ofFloat(rlGiftShow, "y", rlGiftDesY, rlGiftDesY - 100).setDuration(giftAnimDuration);
        out = ValueAnimator.ofInt(0, 100);
        out.setDuration(giftAnimDuration);
        final int offset = Utils.dip2px(getContext(), 60f);
        out.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
//                rlGiftShow.setY(getBottom() - value - offset - rlGiftShow.getHeight());
            }
        });
        outHide = ObjectAnimator.ofFloat(rlGiftShow, "alpha", 1.0f, 0f).setDuration(giftAnimDuration);
        animGiftHide = new AnimatorSet();
        animGiftHide.play(out).with(outHide);
        animGiftHide.start();
        animGiftHide.addListener(new AnimatorListenerAdapter() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onAnimationEnd(Animator animation) {
//                rlGiftShow.setY(rlGiftDesY);//复位
//                rlGiftShow.setTranslationY(getBottom() - offset - rlGiftShow.getHeight());
                rlGiftShow.setAlpha(1f);
                rlGiftShow.setVisibility(View.INVISIBLE);
                giftShowCnt = 1;
                tvGiftCount.setText("x " + giftShowCnt);
                curShowGift = null;
            }
        });
    }

    long animCount1Start, animCount1End;

    ////////////礼物区2开始/////////////////
    private void initShowGiftAnimation1() {
        in1 = ObjectAnimator.ofFloat(rlGiftShow1, "x", -300, giftShowX).setDuration(giftAnimDuration);
        giftIn1 = ObjectAnimator.ofFloat(ivGiftIcon1, "x", 0, giftIconDesX1).setDuration(giftAnimDuration);
        anim1x1 = ObjectAnimator.ofFloat(tvGiftCount1, "scaleX", GIFT_COUNT_BIG_SCALE, 1f).setDuration(giftCountDuration1);
        anim1y1 = ObjectAnimator.ofFloat(tvGiftCount1, "scaleY", GIFT_COUNT_BIG_SCALE, 1f).setDuration(giftCountDuration1);
        anim2x1 = ObjectAnimator.ofFloat(tvGiftCount1, "scaleX", 1f, GIFT_COUNT_SCALE).setDuration(giftCountDuration1);
        anim2y1 = ObjectAnimator.ofFloat(tvGiftCount1, "scaleY", 1f, GIFT_COUNT_SCALE).setDuration(giftCountDuration1);
        anim3x1 = ObjectAnimator.ofFloat(tvGiftCount1, "scaleX", GIFT_COUNT_SCALE, 1f).setDuration(giftCountDuration1);
        anim3y1 = ObjectAnimator.ofFloat(tvGiftCount1, "scaleY", GIFT_COUNT_SCALE, 1f).setDuration(giftCountDuration1);
        anim4n1 = ObjectAnimator.ofFloat(tvGiftCount1, "scaleY", 1f, 1f).setDuration(giftCountDuration1);
//        out1 = ObjectAnimator.ofFloat(rlGiftShow1, "y", rlGiftDesY1, rlGiftDesY1 - 100).setDuration(giftAnimDuration);
        outHide1 = ObjectAnimator.ofFloat(rlGiftShow1, "alpha", 1.0f, 1.0f).setDuration(giftAnimDuration);
        in1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                rlGiftShow1.setVisibility(View.VISIBLE);
                hideIvGift1();
                tvGiftCount1.setVisibility(View.INVISIBLE);
                isAniming1 = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                showIvGift1();
                showAnimGiftAddCount1(curShowGift1);
            }
        });

        animGiftAddCount1 = new AnimatorSet();
        animGiftAddCount1.play(anim1x1).with(anim1y1);
        animGiftAddCount1.play(anim2x1).after(anim1y1);
        animGiftAddCount1.play(anim2y1).after(anim1y1);
        animGiftAddCount1.play(anim3x1).after(anim2x1);
        animGiftAddCount1.play(anim3y1).after(anim2x1);
        animGiftAddCount1.play(anim4n1).after(anim3y1);
        animGiftAddCount1.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isAniming1 = true;
                animCount1Start = System.currentTimeMillis();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
//                isAniming1 = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animCount1End = System.currentTimeMillis();
                Log.d(TAG, "onAnimationEnd animCount1耗时=" + (animCount1End - animCount1Start));
                //检测队列是否还有
                if (checkQueue1()) {
                    return;
                }
                isAniming1 = false;
                handler.postDelayed(hideGiftRunnable1, 4000);
            }
        });
    }

    AnimationDrawable curGiftDrawable1;

    private void hideIvGift1() {
        ivGiftIcon1.setVisibility(View.INVISIBLE);
        if (curGiftDrawable1 != null) {
            curGiftDrawable1.stop();
        }
        if(ivGiftIcon1.isAnimating()){
            ivGiftIcon1.cancelAnimation();
        }
    }

    private void showIvGift1() {
        ivGiftIcon1.setVisibility(View.VISIBLE);
        Log.d(TAG, "showIvGift1 ivGiftIcon1");
//        if(ivGiftIcon1.getDrawable() instanceof AnimationDrawable){
//            curGiftDrawable1 = (AnimationDrawable) ivGiftIcon1.getDrawable();
//            curGiftDrawable1.start();
//        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showGiftAnimation1(BytesReader.GiftMessage message) {
        if (isStop) {
            return;
        }
        Log.d("showGiftAnimation1", "设置内容前" + ivGiftIcon1.getLeft());
//        int giftIndex = Integer.parseInt(message.giftID) - 25;
//        if (giftIndex < 0 || giftIndex >= GiftGridView.giftIconRes.length) {
//            return;
//        }
//        ivGiftIcon1.setImageResource(GiftGridView.giftIconRes[giftIndex]);
//        ivGiftIcon1.setImageDrawable(ImageUtil.getDrawable(context,GiftGridView.giftAnimRes[giftIndex]));
//        Log.d(TAG," loadAssetsImage="+GiftGridView.getSmallAnimPath(message.giftID));
//        ImageUtil.loadAssetsImage(GiftGridView.getSmallAnimPath(message.giftID), ivGiftIcon1, null);
        String animPath = giftResManager.getSmallAnimPath(message.giftID);
        if(Utils.isEmpty(animPath)){
            Log.d(TAG," 没动画");
            return;
        }
        Log.d(TAG," animPath1="+animPath);
        ivGiftIcon1.setImageAssetsFolder("giftAnims/"+message.giftID+"/images");

        if(LiveApplication.getInstance().isShowGiftAnim){
            LottieComposition.Factory.fromAssetFileName(getContext(), animPath, new OnCompositionLoadedListener() {
                @Override
                public void onCompositionLoaded(@Nullable LottieComposition composition) {
                    if(composition==null){
                        Log.d(TAG," 加载动画失败");
                    }else{
                        Log.d(TAG," 加载动画成功");
                        ivGiftIcon1.setComposition(composition);
                        ivGiftIcon1.loop(true);
                        ivGiftIcon1.setProgress(0f);
                        ivGiftIcon1.playAnimation();
                    }
                }
            });
        }else{
            ivGiftIcon1.setImageResource(giftResManager.getGift(message.giftID).getImageRes());
        }



//        if (!Utils.isEmpty(animPath)) {
//            if (animPath.startsWith("giftAnims")) {
//                ImageUtil.loadAssetsImage(animPath, ivGiftIcon1, null);
//            } else {
//                ImageUtil.loadLocalImage(animPath, ivGiftIcon1, null);
//            }
//        } else {
//            //没有动画资源 显示静态图
////            ImageUtil.loadResImage(giftResManager.);
//            ImageUtil.loadImage(ivGiftIcon1, giftResManager.getImageResByGiftId(message.giftID));
//            //补充下载动画
//            giftResManager.downloadGift(message.giftID);
//        }


        tvLevel1.setLevel(message.level);
        tvGiftContent1.setText("送" + giftResManager.getUnitByGiftId(message.giftID) + giftResManager.getNameByGiftId(message.giftID));
        tvGiftCount1.setText("x " + message.count);
        tvGiftName1.setText(message.nickName);
        rlGiftShow1.setVisibility(View.VISIBLE);
        giftIconDesX1 = ivGiftIcon1.getLeft() + ResourceHelper.getDimen(R.dimen.space_10);
        if (rlGiftDesY1 == 0) {
            rlGiftDesY1 = rlGiftShow1.getTop();
        }
        initShowGiftAnimation1();
        handler.removeCallbacks(hideGiftRunnable1);
        if (animGiftShow1 != null && animGiftShow1.isRunning()) {
            animGiftShow1.cancel();
            animGiftShow1.end();
            animGiftShow1 = null;
        }
//        rlGiftShow1.setY(getBottom() - rlGiftShow1.getHeight());

        ImageUtil.loadImage(ivGiftHead1, message.avatar);
        animGiftShow1 = new AnimatorSet();
//        animGiftShow1.play(giftIn1).after(in1);
        animGiftShow1.play(in1);
        animGiftShow1.start();
        animGiftShow1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isAniming1 = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //4秒后自动隐藏
//                handler.postDelayed(hideGiftRunnable1, 4000);
            }
        });
        //如果数字刷新了，重新计算取消时间
    }

    public void showAnimGiftAddCount1(BytesReader.GiftMessage message) {
        if (isStop) {
            return;
        }
        rlGiftShow1.setVisibility(View.VISIBLE);
        isAniming1 = true;
//        if (animGiftAddCount1 != null && animGiftAddCount1.isRunning()) {
//            animGiftAddCount1.cancel();
//            animGiftAddCount1.end();
//            animGiftAddCount1 = null;
//        }
        handler.removeCallbacks(hideGiftRunnable1);
//        if (message == null) {
//            message = curShowGift1;
//        }
        if (message != null)
            tvGiftCount1.setText("x " + message.count);
        tvGiftCount1.setVisibility(View.VISIBLE);
//        animGiftAddCount1 = new AnimatorSet();
//        animGiftAddCount1.play(anim1x1).with(anim1y1);
//        animGiftAddCount1.play(anim2x1).after(anim1y1);
//        animGiftAddCount1.play(anim2y1).after(anim1y1);
//        animGiftAddCount1.play(anim3x1).after(anim2x1);
//        animGiftAddCount1.play(anim3y1).after(anim2x1);

        Log.d(TAG, "showAnimGiftAddCount1");
        if (animGiftAddCount1 != null)
            animGiftAddCount1.start();
//        giftShowCnt++;
//        Log.d(TAG,"giftShowCnt="+giftShowCnt+" tvGiftCount="+tvGiftCount.getText().toString());
    }

    private boolean checkQueue1() {
        BytesReader.GiftMessage message = queue1.poll();
        if (message != null) {
            curShowGift1 = message;
            Log.d(TAG, "继续播放下个动画 id=" + message.giftID + " queue=" + queue1.size() + " count=" + message.count);
            //设置动画时间
            setNumberAnimDuration(1);
            showAnimGiftAddCount1(message);
            return true;
        }
        return false;
    }

    Runnable hideGiftRunnable1 = new Runnable() {
        @Override
        public void run() {
            hideGiftAnim1();
        }
    };

    /**
     * 礼物消失动画
     */
    private void hideGiftAnim1() {
//        if(btnSendGiftMulti.getVisibility()==View.VISIBLE){
//            return;
//        }
//        Log.d(TAG,"hideGiftAnima");
//        out1 = ObjectAnimator.ofFloat(rlGiftShow1, "y", rlGiftDesY1, rlGiftDesY1 - 100).setDuration(giftAnimDuration);
        out1 = ValueAnimator.ofFloat(0f, 100f);
        out1.setDuration(giftAnimDuration);
        out1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                int bottom = getBottom();
                int height = rlGiftShow1.getHeight();
//                rlGiftShow1.setY(bottom - value - height);
            }
        });
        outHide1 = ObjectAnimator.ofFloat(rlGiftShow1, "alpha", 1.0f, 0f).setDuration(giftAnimDuration);
        animGiftHide1 = new AnimatorSet();
        animGiftHide1.play(out1).with(outHide1);
        animGiftHide1.start();
        animGiftHide1.addListener(new AnimatorListenerAdapter() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onAnimationEnd(Animator animation) {
//                rlGiftShow1.setY(rlGiftDesY1);//复位

                rlGiftShow1.setAlpha(1f);
                rlGiftShow1.setVisibility(View.INVISIBLE);
                giftShowCnt1 = 1;
                tvGiftCount1.setText("x " + giftShowCnt1);
                curShowGift1 = null;
            }
        });
    }
    ////////////礼物区2结束/////////////////

    //队列
    private Queue<BytesReader.GiftMessage> queue1 = new LinkedBlockingQueue<>();
    private Queue<BytesReader.GiftMessage> queue = new LinkedBlockingQueue<>();
    private boolean isAniming, isAniming1;

    public void showGift(BytesReader.GiftMessage message) {
        //特效礼物不显示在礼物栏
//        if ( GiftAnimManger.isAnimGift(message.giftID)) {
//            return;
//        }
        GiftInfo gift = GiftResManager.getInstance().getGift(message.giftID);
        if (gift == null) {
            return;
        }
        if (gift.isFull()) {
            return;
        }

        if (curShowGift1 != null && curShowGift1.audienceID.equals(message.audienceID) && curShowGift1.giftID.equals(message.giftID) && isContinueGift(message.giftID)) {
            Log.d(TAG, "区域2 只需要刷新数量");
            if (curShowGift1.time > message.time && curShowGift1.count >= message.count) {
                Log.d(TAG, "新礼物数字比当前少，抛弃");
                return;
            }
            if (isAniming1) {
                //先放进队列等待
                queue1.add(message);
            } else {
                curShowGift1 = message;
                showAnimGiftAddCount1(message);
            }
        } else if (curShowGift != null && curShowGift.audienceID.equals(message.audienceID) && curShowGift.giftID.equals(message.giftID) && isContinueGift(message.giftID)) {
            Log.d(TAG, "区域1 只需要刷新数量");
            if (curShowGift.time > message.time && curShowGift.count >= message.count) {
                Log.d(TAG, "新礼物数字比当前少，抛弃");
                return;
            }
            if (isAniming) {
                //先放进队列等待
                queue.add(message);
            } else {
                curShowGift = message;
                showAnimGiftAddCount(message);
            }
        } else if (curShowGift1 == null || (!isAniming1 && queue1.size() == 0)) {
            if (curShowGift != null && curShowGift.audienceID.equals(message.audienceID) && curShowGift.giftID.equals(message.giftID)) {
                Log.d(TAG, "curShowGift 此用户已占有一个位置，忽略");
                return;
            }
            curShowGift1 = message;
            showGiftAnimation1(message);
            Log.d(TAG, "区域2 插入礼物动画");
        } else if (curShowGift == null || (!isAniming && queue.size() == 0)) {
            if (curShowGift1 != null && curShowGift1.audienceID.equals(message.audienceID) && curShowGift1.giftID.equals(message.giftID)) {
                Log.d(TAG, "curShowGift 此用户已占有一个位置，忽略");
                return;
            }
            curShowGift = message;
            showGiftAnimation(message);
            Log.d(TAG, "区域1 插入礼物动画");
        } else {
            Log.d(TAG, "礼物区1,2同时被占用，不显示");
//                    curShowGift1 = message;
//                    showGiftAnimation(message);
        }
    }


    private boolean isContinueGift(String giftId) {
//        int gift = Integer.parseInt(giftId);

        if (giftResManager.isMultiGift(giftId)) {
            return true;
        }
        return false;
    }

    /*@Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        updateHeight(rlGiftShow, h, oldh, Utils.dip2px(context, 60f));
        updateHeight(rlGiftShow1, h, oldh, 0);


    }*/

    /*private void updateHeight(View view, int h, int oldh, int offset) {
        int changeH = h - oldh;
        Log.e("test", "h : " + h);
        Log.e("test", "oldh : " + oldh);
        Log.e("test", "view.getY() : " + view.getY());
//        view.setY((view.getY() + changeH));
        view.setY(getBottom() - changeH - view.getHeight() - offset);
//        view.setTranslationY(changeH - Utils.dip2px(context, 40f));
//        view.setTranslationY(view.getTranslationY() + changeH);

        Log.e("test", "view.getY() : " + view.getY());
    }*/


    public boolean isStop = false;

    public void stopAndClear() {
        isStop = true;
        if (queue1 != null) {
            queue1.clear();
        }
        if (queue != null) {
            queue.clear();
        }
        rlGiftShow1.setVisibility(View.INVISIBLE);
        rlGiftShow.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.iv_gift_head:
//                if(curShowGift!=null){
//                    Log.d(TAG," curShowGift="+curShowGift.audienceID);
//                    EventBusManager.postEvent(curShowGift.audienceID, SubcriberTag.SHOW_USER_POP);
//                }
//                break;
//            case R.id.iv_gift_head1:
//                if(curShowGift1!=null){
//                    Log.d(TAG," curShowGift1="+curShowGift1.audienceID);
//                    EventBusManager.postEvent(curShowGift1.audienceID, SubcriberTag.SHOW_USER_POP);
//                }
//                break;
        }
    }
}
