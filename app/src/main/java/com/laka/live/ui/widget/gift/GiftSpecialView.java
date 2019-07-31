package com.laka.live.ui.widget.gift;

import android.animation.Animator;
import android.content.Context;
import android.graphics.drawable.Animatable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.animated.base.AnimatedDrawable;
import com.laka.live.R;
import com.laka.live.bean.GiftInfo;
import com.laka.live.gift.GiftResManager;
import com.laka.live.manager.BytesReader;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.Utils;


/**
 * Created by luwies on 16/7/13.
 */
public class GiftSpecialView extends FrameLayout implements GiftAnimator {

    private static final String TAG = "GiftSpecialView";

    private final static String USER_TEX_NAME = "d_user.png";

//    private FlashView mFlashView;

    private Context mContext;

//    private HandlerThread mThread;

    private Animator.AnimatorListener mAnimatorListener;

    private Handler mHandler;

    private Handler mMainHandler;

    private View mUserView;

    private SimpleDraweeView mFace, mIvGift;

    private LinearLayout mLlInfo;

    private TextView mName;

    private BytesReader.GiftMessage curGiftMessage;

    public GiftSpecialView(Context context, BytesReader.GiftMessage message) {
        super(context);
        mContext = context;
        curGiftMessage = message;
        mUserView = LayoutInflater.from(mContext).inflate(R.layout.view_gift_special, null);
        mFace = (SimpleDraweeView) mUserView.findViewById(R.id.face);
        mName = (TextView) mUserView.findViewById(R.id.name);
        mIvGift = (SimpleDraweeView) mUserView.findViewById(R.id.iv_gift);
        mLlInfo = (LinearLayout) mUserView.findViewById(R.id.ll_info);

        RelativeLayout.LayoutParams lpGift = (RelativeLayout.LayoutParams) mIvGift.getLayoutParams();
        lpGift.width = Utils.getScreenWidth(context);
        RelativeLayout.LayoutParams lpInfo = (RelativeLayout.LayoutParams) mLlInfo.getLayoutParams();
        GiftInfo gift = GiftResManager.getInstance().getGift(message.giftID);
        if (gift.getIsFull()==1) {
            int height = (int) (Float.parseFloat(Utils.getScreenWidth(context) + "") * 1.78);
            lpGift.height = height;
            lpInfo.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        } else {
            int height = Utils.getScreenWidth(context);
            lpGift.height = height;
            lpInfo.addRule(RelativeLayout.BELOW, R.id.iv_gift);
        }

        mIvGift.setLayoutParams(lpGift);
        mLlInfo.setLayoutParams(lpInfo);

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(mUserView, params);
//        mUserView.setY(1000000000);

        setText(curGiftMessage.nickName + GiftResManager.getInstance().getSpecialSendGiftStr(curGiftMessage.giftID, curGiftMessage.count));
//        ImageUtil.loadImage(mFace,message.avatar);


//        mThread = new HandlerThread("GiftSpecialViewBackground");
//        mThread.start();
//        mHandler = new Handler(mThread.getLooper());
        mMainHandler = new Handler(Looper.getMainLooper());
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                init();
//            }
//        });
    }

//    private boolean isFullScreen(String giftID) {
//        if ("28".equals(giftID) ||"32".equals(giftID) || "39".equals(giftID) || "40".equals(giftID)) {
//            return true;
//        }
//        return false;
//    }



//    private String getSpecialAnimPath(String giftID, int count) {
//
//        if ("25".equals(giftID)) {
//            if (count == GiftGridView.specail_anim_tag1) {
//                Log.d(TAG, " getSpecialAnimRes hua");
//                return "specialAnims/xin.webp";
//            } else if (count == GiftGridView.specail_anim_tag2) {
//                Log.d(TAG, " getSpecialAnimRes xin");
//                return "specialAnims/huaqiang.webp";
//            } else if (count == GiftGridView.specail_anim_tag3) {
//                Log.d(TAG, " getSpecialAnimRes huaqiang");
//                return "specialAnims/dest.webp";
//            }
//        } else {
//            return GiftResManager.getInstance().getBigAnimPath(giftID);
//        }
//
//        return "";
//    }


    private void init() {
//        mFlashView = new FlashView(mContext, mFlashName, mFlashDir, 320);
//
//        if (mMainHandler != null) {
//            mMainHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    if (mFlashView != null) {
//
//
//                        addView(mFlashView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                                ViewGroup.LayoutParams.MATCH_PARENT));
//                    }
//                }
//            });
//        }
    }

    @Override
    public void start() {
        start(null);
    }


    AnimatedDrawable curAnimDrawable;
    @Override
    public void start(Animator.AnimatorListener listener) {
        mAnimatorListener = listener;
//        Log.d(TAG, "开始播放连击特效动画 开始倒计使结束");
        ControllerListener controllerListener = new ControllerListener() {
            @Override
            public void onSubmit(String id, Object callerContext) {
            }

            @Override
            public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {
//                Log.d(TAG, " onFinalImageSet");
                setVisibility(View.VISIBLE);
                if (animatable instanceof AnimatedDrawable) {
                    AnimatedDrawable animDrawable = (AnimatedDrawable) animatable;
                    Log.d(TAG, " instanceof AnimatedDrawable duration=" + animDrawable.getDuration());
                    animDrawable.getDuration();
                    curAnimDrawable = animDrawable;
//                    animDrawable.start();
                    mMainHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mAnimatorListener.onAnimationEnd(null);
                        }
                    }, animDrawable.getDuration());
                } else {
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAnimatorListener.onAnimationEnd(null);
                        }
                    });
                }
            }

            @Override
            public void onIntermediateImageSet(String id, Object imageInfo) {

            }

            @Override
            public void onIntermediateImageFailed(String id, Throwable throwable) {
                Log.d(TAG, " onIntermediateImageFailed");
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                Log.error(TAG, "onFailure", throwable);

                Log.d(TAG, "解码失败删除旧文件并重新下载");
                GiftResManager.getInstance().reDownloadGift(curGiftMessage.giftID);
//                Log.d(TAG, " onFailure throwable=" + throwable.getLocalizedMessage());
                mAnimatorListener.onAnimationEnd(null);
            }

            @Override
            public void onRelease(String id) {

            }
        };

        setVisibility(View.GONE);
        String animPath = "";
        if( GiftResManager.getInstance().isSpecailAnimAmount(curGiftMessage.giftID,curGiftMessage.count)){
            animPath  =GiftResManager.getInstance().getSpecialAnimPath(curGiftMessage.count,curGiftMessage.giftID);
        }else{
            animPath = GiftResManager.getInstance().getBigAnimPath(curGiftMessage.giftID);
        }
        if (!Utils.isEmpty(animPath) && (animPath.startsWith("giftAnims") || animPath.startsWith("specialAnims"))) {
            Log.d(TAG, "加载assets动画");
            ImageUtil.loadAssetsImage(animPath, mIvGift, controllerListener);
        } else {
            if (!Utils.isEmpty(animPath)) {
                Log.d(TAG, "加载sd卡动画成功");
                ImageUtil.loadLocalImage(animPath, mIvGift, controllerListener);
            } else {
                Log.d(TAG, "本地动画不存在跳过本次动画");

                if( GiftResManager.getInstance().isSpecailAnimAmount(curGiftMessage.giftID,curGiftMessage.count)){
                    Log.d(TAG," 补充下载特效动画");
                    GiftResManager.getInstance().downloadGift(curGiftMessage.giftID,curGiftMessage.count);
                }else{
                    GiftResManager.getInstance().downloadGift(curGiftMessage.giftID);
                    Log.d(TAG," 补充下载大动画");
                }


                mAnimatorListener.onAnimationEnd(null);
                return;
            }

        }


//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (mFlashView != null) {
//                    mFlashView.play(mAnimName, FlashDataParser.FlashLoopTimeOnce);
//                }
//            }
//        });
    }

    @Override
    public void stop() {
//        if (mFlashView != null) {
//            mFlashView.stop();
//        }

        //停止动画
        if(curAnimDrawable!=null){
            curAnimDrawable.stop();
            setVisibility(View.GONE);
        }
    }

    @Override
    public void setText(CharSequence text) {
        mName.setText(text);
        mName.invalidate();
    }

    public void setIcon(String url) {
        ImageUtil.loadImage(mFace, url);
    }


    boolean isDestory = false;

    public void onDestroy() {
        isDestory = true;
//        mThread.quit();
//        mHandler.removeCallbacksAndMessages(null);
//        mHandler = null;
        mMainHandler.removeCallbacksAndMessages(null);
        mMainHandler = null;
//        mFlashView = null;
    }


}
