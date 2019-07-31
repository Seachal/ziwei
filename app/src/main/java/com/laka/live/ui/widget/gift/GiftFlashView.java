package com.laka.live.ui.widget.gift;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.ui.widget.flash.FlashDataParser;
import com.laka.live.ui.widget.flash.FlashView;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.Utils;

/**
 * Created by luwies on 16/7/13.
 */
public class GiftFlashView extends FrameLayout implements GiftAnimator, FlashDataParser.IFlashViewEventCallback {

    private static final String TAG = "GiftFlashView";

    private final static String USER_TEX_NAME = "d_user.png";

    private FlashView mFlashView;

    private Context mContext;

    private HandlerThread mThread;

    private Animator.AnimatorListener mAnimatorListener;

    private Handler mHandler;

    private String mFlashName;

    private String mFlashDir;

    private String mAnimName;

    private Handler mMainHandler;

    private View mUserView;

    private SimpleDraweeView mFace;

    private TextView mName;

    public GiftFlashView(Context context, String flashName, String flashDir, String animName) {
        super(context);
        mContext = context;

        this.mFlashName = flashName;

        this.mFlashDir = flashDir;

        this.mAnimName = animName;

        mUserView = LayoutInflater.from(mContext).inflate(R.layout.flash_gift_layout, null);
        mFace = (SimpleDraweeView) mUserView.findViewById(R.id.face);
        mName = (TextView) mUserView.findViewById(R.id.name);

        LayoutParams params = new LayoutParams(560, 72);

        addView(mUserView, params);
        mUserView.setY(1000000000);

        mThread = new HandlerThread("FlashViewBackground");
        mThread.start();
        mHandler = new Handler(mThread.getLooper());

        mMainHandler = new Handler(Looper.getMainLooper());

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                init();
            }
        });
    }

//    /**
//     * 这个例子通过将线程实例声明为private static型的内部 类，从而避免导致Activity泄
//     * 露，但是这个线程依旧会跨越配置变化存活下来。DVM有一个指向所有运行中线程的
//     * 引用（无论这些线程是否 可以被垃圾回收），而线程能存活多长时间以及什么时候可
//     * 以被回收跟Activity的生命周期没有任何关系。
//     * 活动线程会一直运行下去，直到系统将你的应用程序销毁。
//     */
//    private static class MyHandlerThread extends HandlerThread {
//        public MyHandlerThread(String name) {
//            super(name);
//        }
//    }

    private void init() {
//        mFlashView = (FlashView) LayoutInflater.from(mContext).inflate(R.layout.view_room_flash, null);
        mFlashView = new FlashView(mContext, mFlashName, mFlashDir, 320);

        if (mMainHandler != null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mFlashView != null) {
                        mFlashView.setEventCallback(GiftFlashView.this);
                        int width = Utils.getScreenWidth(mContext);
                        float scale = width / 750f;
                        mFlashView.setScale(scale, scale, false);
                        addView(mFlashView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
                    }
                }
            });
        }

//        mFlashView.reload(mFlashName, mFlashDir, 320);

    }

    @Override
    public void start() {
        start(null);
    }

    @Override
    public void start(Animator.AnimatorListener listener) {
        mAnimatorListener = listener;

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mFlashView != null) {
                    mFlashView.play(mAnimName, FlashDataParser.FlashLoopTimeOnce);
                }
            }
        });
    }

    @Override
    public void stop() {
        if (mFlashView != null) {
            mFlashView.stop();
        }
    }

    @Override
    public void setText(CharSequence text) {

        mName.setText(text);
        mName.invalidate();

        replaceUserBitmap();
    }

    private void replaceUserBitmap() {
        if(isDestory){
            return;
        }
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mUserView.setDrawingCacheEnabled(true);
                final Bitmap tmpBmp = mUserView.getDrawingCache();
                if (tmpBmp == null) {
                    return;
                }
                final Bitmap bitmap = Bitmap.createBitmap(tmpBmp, 0, 0, tmpBmp.getWidth(), tmpBmp.getHeight());
                mUserView.setDrawingCacheEnabled(false);
                if (mHandler != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mFlashView != null) {
                                mFlashView.replaceBitmap(USER_TEX_NAME, bitmap);
                            }
                        }
                    });
                }
            }
        }, 500L);


    }

    public void setIcon(String url) {
        ImageUtil.loadImage(mFace, url, new ControllerListener() {
            @Override
            public void onSubmit(String id, Object callerContext) {

            }

            @Override
            public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {
                replaceUserBitmap();
            }

            @Override
            public void onIntermediateImageSet(String id, Object imageInfo) {

            }

            @Override
            public void onIntermediateImageFailed(String id, Throwable throwable) {

            }

            @Override
            public void onFailure(String id, Throwable throwable) {

            }

            @Override
            public void onRelease(String id) {

            }
        });
    }

    boolean isDestory = false;
    public void onDestroy() {
        isDestory = true;
        mThread.quit();
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        mMainHandler.removeCallbacksAndMessages(null);
        mMainHandler = null;
        mFlashView = null;
    }


    @Override
    public void onEvent(FlashDataParser.FlashViewEvent e, FlashDataParser.FlashViewEventData data) {
        switch (e) {
            case START:
                if (mAnimatorListener != null) {
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mFlashView != null) {
                                mFlashView.setVisibility(VISIBLE);
                            }
                            mAnimatorListener.onAnimationStart(null);
                        }
                    });
                }
                break;
            case FRAME:
                break;
            case ONELOOPEND:
                if (mAnimatorListener != null) {
                    if (mFlashView != null) {
                        mFlashView.stopAt(mAnimName, 0);
                        mFlashView.setVisibility(GONE);
                    }
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAnimatorListener.onAnimationEnd(null);
                        }
                    });
                }
                break;
            case STOP:
                /*if (mAnimatorListener != null) {
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAnimatorListener.onAnimationCancel(null);
                        }
                    });
                }*/
                break;
            case MARK:
                break;
        }
    }

    private static Bitmap getCircularBitmap(Bitmap bitmapimg, final int color, int width, int strokeWidth) {

        bitmapimg = ImageUtil.zoomImage(bitmapimg, width, width);

        Bitmap output = Bitmap.createBitmap(bitmapimg.getWidth(),
                bitmapimg.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        // 从canvas层面去除锯齿
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG));

        final Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        final Rect rect = new Rect(0, 0, bitmapimg.getWidth(),
                bitmapimg.getHeight());

        paint.setAntiAlias(true);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        canvas.drawARGB(0, 0, 0, 0);

        paint.setColor(Color.RED);
        int radius = bitmapimg.getWidth() / 2;
        canvas.drawCircle(radius, radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmapimg, rect, rect, paint);

        paint.setColor(color);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth + 1);
        canvas.drawCircle(radius, radius, radius, paint);

        return output;
    }

    /*private static Bitmap getStrokeBitmap(String text, int width) {

        final int w = width;
        final int h = width;

        Bitmap mCache = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        Canvas mCanvas = new Canvas();

        final Rect textBounds = new Rect();
        final Paint textPaint = new Paint();
        final int textWidth = (int) textPaint.measureText(text);
        textPaint.getTextBounds(text, 0, 1, textBounds);
        // Clear the old cached image
        mCanvas.setBitmap(mCache);
        mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);

        Paint.FontMetrics fm = textPaint.getFontMetrics();
        final int left = 0;
        int bottom = ;
        // Draw the outline of the text


        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(mStrokeColor);
        mPaint.setTextSize(getTextSize());
        mCanvas.drawText(text, left, bottom, mPaint);
        // Draw the text itself
        mPaint.setStrokeWidth(0);
        mPaint.setColor(mTextColor);
        mCanvas.drawText(text, left, bottom, mPaint);

        return mCache;
    }*/
}
