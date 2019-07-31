package com.laka.live.ui.widget.gift;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.laka.live.R;
import com.laka.live.bean.GiftInfo;
import com.laka.live.gift.GiftAudioManager;
import com.laka.live.gift.GiftResManager;
import com.laka.live.manager.BytesReader;
import com.laka.live.util.Log;
import com.laka.live.util.Util;
import com.laka.live.util.Utils;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by crazyguan on 2016/4/21.
 * 礼物动画管理控制
 */
public class GiftAnimManger {

    private static final String TAG = "RoomGiftAnimManger";
    private Queue<BytesReader.GiftMessage> queue = new LinkedBlockingQueue<>();//LinkedBlockingDeque
    private Activity activity;
    private ViewGroup rootView;
    /*private GiftFlashView mReQiQiu;
    private GiftFlashView mCityOfSky;*/
    private GiftFlashView mPlane;
    private GiftFlashView mCake;
    private GiftFlashView mCar;
    private GiftFlashView mFlowers;
    private boolean isAniming;
    private GiftAnimator curAnimator;

    public GiftAnimManger(Activity activity, ViewGroup rootView) {
        this.activity = activity;
        this.rootView = rootView;
    }

//    public static boolean isAnimGift(String giftID) {
//
//        return TextUtils.equals(giftID, "28") || TextUtils.equals(giftID, "29") ||
//                TextUtils.equals(giftID, "30") || TextUtils.equals(giftID, "31") || TextUtils.equals(giftID, "32")
//                || TextUtils.equals(giftID, "37") || TextUtils.equals(giftID, "38")
//                || TextUtils.equals(giftID, "39")  ;
//    }


//    public static boolean isSpecialAnimGift(String giftID) {
//
//        return TextUtils.equals(giftID, "25") || TextUtils.equals(giftID, "28") || TextUtils.equals(giftID, "29") || TextUtils.equals(giftID, "30")
//                || TextUtils.equals(giftID, "31") || TextUtils.equals(giftID, "32") || TextUtils.equals(giftID, "36") || TextUtils.equals(giftID, "37")
//                || TextUtils.equals(giftID, "38") || TextUtils.equals(giftID, "39") || TextUtils.equals(giftID, "40");//是否有特效
//    }

//    public void addGift(BytesReader.GiftMessage gift) {
//        if (isAnimGift(gift.giftID)) {
//            queue.add(gift);
//
////            BytesReader.GiftMessage message = queue.poll();
//            handlerGift(gift);
////            if (!isAniming) {
////                isAniming = true;
////                BytesReader.GiftMessage message = queue.poll();
////                handlerGift(message);
////            } else {
////                Log.d(TAG, "动画正在播放，等待");
////            }
//        }
//    }
//
//
//
//    private void handlerGift(BytesReader.GiftMessage message) {
//        if (mFlashView == null) {
//            //首次初始化flashview;
//            mFlashView = (FlashView) LayoutInflater.from(activity).inflate(R.layout.view_room_flash, null);
//            int height = Util.getScreenHeight(activity);
//            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                    height);
//            rootView.addView(mFlashView, 8, lp);
//            mFlashView.setEventCallback(new FlashDataParser.IFlashViewEventCallback() {
//                @Override
//                public void onEvent(FlashDataParser.FlashViewEvent e, FlashDataParser.FlashViewEventData data) {
//                    switch (e) {
//                        case START:
//                            Log.d(TAG, " mFlashView onEvent START");
//                            isAniming = true;
//                            break;
//                        case FRAME:
////                            Log.d(TAG, " mFlashView onEvent FRAME");
//                            break;
//                        case MARK:
////                            Log.d(TAG, " mFlashView onEvent MARK");
//                            break;
//                        case STOP:
//                            Log.d(TAG, " mFlashView onEvent STOP");
////                            isAniming = false;
////                            mFlashView.play("reqiqiu", FlashLoopTimeOnce);
//                            break;
//                        case ONELOOPEND:
//                            Log.d(TAG, " mFlashView onEvent ONELOOPEND");
////                            rootView.removeView(mFlashView);
//                            //检测队列是否还有
////                            BytesReader.GiftMessage message = queue.poll();
////                            if (message != null) {
////                                Log.d(TAG, "继续播放下个动画 id=" + message.giftID + " queue=" + queue.size());
////                                handlerGift(message);
////                            } else {
////                                Log.d(TAG, "全部动画播放完毕");
////                                isAniming = false;
////                            }
//                            break;
//                    }
//                }
//            });
//
//            new Thread() {
//                @Override
//                public void run() {
//                    while (!isFlashStop) {
//                        try {
//                            Thread.sleep(100);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//                        if (isAniming) {
//                            Log.d(TAG, "上个flash动画正在播放");
//                            continue;
//                        }
//
//                        BytesReader.GiftMessage message = queue.poll();
//                        if (message != null) {
//                            Log.d(TAG, "继续播放下个动画 id=" + message.giftID + " queue=" + queue.size());
////                            handlerGift(message);
//                            isAniming = true;
//                            mFlashView.reload("test", "flashAnims");//加载和播放对应动画
//                            mFlashView.play("reqiqiu", FlashLoopTimeOnce);
//                        } else {
//                            Log.d(TAG, "暂时没动画");
//                            isAniming = false;
//                            continue;
//                        }
//                    }
//                }
//            }.start();
//        }
//        mFlashView.setEventCallback(new FlashDataParser.IFlashViewEventCallback() {
//            @Override
//            public void onEvent(FlashDataParser.FlashViewEvent e, FlashDataParser.FlashViewEventData data) {
//                switch (e) {
//                    case START:
//                        Log.d(TAG, " onEvent START");
//                        break;
//                    case STOP:
//                        Log.d(TAG, " onEvent STOP");
//                        isAniming = false;
//                        break;
//                    case ONELOOPEND:
//                        isAniming = false;
//                        Log.d(TAG, " onEvent ONELOOPEND");
//                        break;
//                }
//            }
//        });
////        mFlashView.reload("test", "flashAnims");//加载和播放对应动画
////        mFlashView.play("reqiqiu", FlashLoopTimeOnce);
//    }

    /**
     * 添加动画队列
     */
    public void addGift(BytesReader.GiftMessage gift) {

        GiftInfo giftInfo = GiftResManager.getInstance().getGift(gift.giftID);
        if (giftInfo == null) {
            return;
        }

//        if (isAnimGift(gift.giftID)) {
        if (giftInfo.isFull())
            queue.add(gift);
        if (!isAniming) {
            handlerNext();
        } else {
            Log.d(TAG, "动画正在播放，等待");
        }

//        if (GiftResManager.getInstance().isSpecialAnimGift(gift.giftID) && GiftResManager.getInstance().isSpecailAnimAmount(gift.giftID, gift.count)) {
//            queue.add(gift);
//            if (!isAniming) {
//                handlerNext();
//            } else {
//                Log.d(TAG, "动画正在播放，等待");
//            }
//        }

//        if (isSpecialAnimGift(gift.giftID) && isSpecailAnimAmount(gift.count)) {
//
//            queue.add(gift);
//            if (!isAniming) {
//                handlerNext();
//            } else {
//                Log.d(TAG, "动画正在播放，等待");
//            }
//        }

    }

//    private boolean isSpecailAnimAmount(int count) {
//        return count == GiftGridView.specail_anim_tag1 || count == GiftGridView.specail_anim_tag2 || count == GiftGridView.specail_anim_tag3;
//    }

    private void handlerNext() {
        //检测队列是否还有
        BytesReader.GiftMessage message = queue.poll();
        if (message != null) {
            Log.d(TAG, "继续播放下个动画 id=" + message.giftID + " queue=" + queue.size());
            handlerGift(message);
        } else {
            Log.d(TAG, "全部动画播放完毕");
            isAniming = false;
        }
    }

    BytesReader.GiftMessage curMessage;

    private void handlerGift(BytesReader.GiftMessage message) {
        GiftAnimator animatorView = getAnimatorView(message);
        if (animatorView == null) {
            Log.d(TAG, "没有对应的动画");
            handlerNext();
            return;
        }
        curAnimator = animatorView;
        /*if (message.giftID.equals("16")) {
            if (!Utils.isEmpty(message.avatar)) {
                ((Ship) animatorView).setIcon(message.avatar);
            } else {
                ((Ship) animatorView).setIcon(R.drawable.blank_icon_avatar);
            }

        }*/

        Log.d(TAG," 开始播放动画");
//        if (animatorView instanceof GiftFlashView) {
//            ((GiftFlashView) animatorView).setIcon(message.avatar);
//            animatorView.setText(activity.getString(R.string.send_gift_format, message.nickName,
//                    getSendGiftStr(message.giftID)));
//        } else if (animatorView instanceof GiftSpecialView) {
//            ((GiftSpecialView) animatorView).setIcon(message.avatar);
//        }


        Rect outRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        int height = Util.getScreenHeight(activity) - outRect.top;

        int width = Utils.getScreenWidth(activity);
        float scale = width / 750f;
        int viewHeight = height;
        if (TextUtils.equals(message.giftID, "12")) {
            viewHeight = (int) (scale * 1020);
        }

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                viewHeight);

//        if (TextUtils.equals(message.giftID, "12")) {
//            lp.topMargin = (int) (scale * 1020 - height + Utils.dip2px(activity, 50f) /*+ scale * 1020 * 0.3f*/);
//        } else if (TextUtils.equals(message.giftID, "15")) {
//            lp.topMargin = Utils.dip2px(activity, -20f);
//        } else if (TextUtils.equals(message.giftID, "22")) {
//            lp.topMargin = Utils.dip2px(activity, -120f);
//        } else if (TextUtils.equals(message.giftID, "23")) {
//            lp.topMargin = Utils.dip2px(activity, -170f);
//        }

        if (rootView == null) {
            Log.d(TAG," rootView==null");
            return;
        }

        int index = rootView.indexOfChild((View) animatorView);
        if (index < 0) {
            View comment = rootView.findViewById(R.id.message_view);
            int commentIndex = rootView.indexOfChild(comment);
            rootView.addView((View) animatorView, commentIndex + 1, lp);//根据LiveRoom布局层数，放在评论栏后面
            Log.d(TAG," rootView.addView commentIndex="+commentIndex);
        }
        isAniming = true;


        GiftInfo gift = GiftResManager.getInstance().getGift(message.giftID);
        animatorView.start(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isAniming = true;
                Log.d(TAG," onAnimationStart");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                isAniming = false;
                Log.d(TAG," onAnimationCancel");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Log.d(TAG," onAnimationEnd");
                rootView.removeView((View) curAnimator);
                handlerNext();
            }
        });


//        if (gift.isSound()) {
//            curMessage = message;
//
//            String soundPath = GiftResManager.getInstance().getSoundPath(message.giftID);
//            if (!Utils.isEmpty(soundPath)) {
//                if (soundPath.startsWith("sound/")) {
//                    Log.d(TAG, "播放本地音频");
//                    GiftAudioManager.getInstance().playBgm(soundPath, activity);
//                } else {
//                    Log.d(TAG, "播放sd卡音频");
//                    GiftAudioManager.getInstance().playBgm(soundPath, activity);
//                }
//            } else {
//                Log.d(TAG, "音频不存在");
//            }
//
//        }
//
//        animatorView.start(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                super.onAnimationStart(animation);
//                isAniming = true;
//                Log.d(TAG," onAnimationStart");
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//                super.onAnimationCancel(animation);
//                isAniming = false;
//                Log.d(TAG," onAnimationCancel");
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                Log.d(TAG," onAnimationEnd");
//                rootView.removeView((View) curAnimator);
//                handlerNext();
//            }
//        });
    }

    public String getSendGiftStr(String giftID) {
        if (Utils.isEmpty(giftID)) {
            return "";
        }

        int giftIndex = Integer.parseInt(giftID) - 9;
        String[] strs = activity.getResources().getStringArray(R.array.gift_content);
        if (giftIndex > 0 && strs != null && strs.length > 0 && giftIndex < strs.length) {
            return strs[giftIndex];
        }

        return "";
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private GiftAnimator getAnimatorView(BytesReader.GiftMessage message) {
        String giftID = message.giftID;
//        if (GiftResManager.getInstance().isFullAnimGift(giftID)) {
//            return new GiftSpecialView(activity, message);
//        }
        if (GiftResManager.getInstance().isFullAnimGift(giftID)) {
            CustomLottieAnimationView animationView = new CustomLottieAnimationView(activity);
            animationView.setGiftID(giftID);
            return animationView;
        }


        /*if (giftID.equals("13")) {
            if (porsche == null) {
                porsche = new Porsche(activity);
            }
            return porsche;
        } else if (giftID.equals("14")) {
            if (ferrari == null) {
                ferrari = new Ferrari(activity);
            }
            return ferrari;
        } else if (giftID.equals("15")) {
            if (plane == null) {
                plane = new Plane(activity);
            }
            return plane;
        } else if (giftID.equals("16")) {
            if (ship == null) {
                ship = new Ship(activity);
            }
            return ship;
        }
        return null;*/
        /*if (TextUtils.equals(giftID, "7")) {
            if (mReQiQiu == null) {
                mReQiQiu = new GiftFlashView(activity, "hotairballoon", "flashAnims", "reqiqiu");
            }
            return mReQiQiu;
        }
        if (TextUtils.equals(giftID, "8")) {
            if (mCityOfSky == null) {
                mCityOfSky = new GiftFlashView(activity, "cityofsky", "flashAnims", "tkzc");
            }
            return mCityOfSky;
        }*/

        /*if (TextUtils.equals(giftID, "1")) {
            if (mPlane == null) {
                mPlane = new GiftFlashView(activity, "plane", "flashAnims", "plane");
//                mPlane.setClipBounds(new Rect(0,0,375, 450));
            }
            return mPlane;
        }*/

        //2.2.0版本
//        if (TextUtils.equals(giftID, "12")) {
//            if (mPlane == null) {
//                mPlane = new GiftFlashView(activity, "plane", "flashAnims", "plane");
//            }
//            return mPlane;
//        } else if (TextUtils.equals(giftID, "15")) {
//            if (mCake == null) {
//                mCake = new GiftFlashView(activity, "cake", "flashAnims", "cake");
//            }
//            return mCake;
//        } else if ((TextUtils.equals(giftID, "23"))) {
//            if (mCar == null) {
//                mCar = new GiftFlashView(activity, "car", "flashAnims", "car");
//            }
//            return mCar;
//        } else if (TextUtils.equals(giftID, "22")) {
//            if (mFlowers == null) {
//                mFlowers = new GiftFlashView(activity, "hua", "flashAnims", "meiguihuatexiao");
//            }
//            return mFlowers;
//        }

        return null;
    }

    /**
     * 清空动画队列
     */
    public void clearAllAnima() {
        queue.clear();
        if (curAnimator != null && isAniming) {
            curAnimator.stop();
        }

        /*if (mReQiQiu != null) {
            mReQiQiu.onDestroy();
        }

        if (mCityOfSky != null) {
            mCityOfSky.onDestroy();
        }*/
        if (mPlane != null) {
            mPlane.stop();
            mPlane.onDestroy();
            mPlane = null;
        }

        if (mCar != null) {
            mCar.stop();
            mCar.onDestroy();
            mCar = null;
        }

        if (mCake != null) {
            mCake.stop();
            mCake.onDestroy();
            mCake = null;
        }

        if (mFlowers != null) {
            mFlowers.stop();
            mFlowers.onDestroy();
            mFlowers = null;
        }
        queue.clear();
        if (curMessage != null) {
//            if ("39".equals(curMessage.giftID)) {
//                GiftAudioManager.getInstance().stopBgm();
//            }
            GiftAudioManager.getInstance().stopBgm();
        }
    }


}
