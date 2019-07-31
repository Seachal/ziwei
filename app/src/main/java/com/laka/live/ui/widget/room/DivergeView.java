package com.laka.live.ui.widget.room;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import com.laka.live.util.Log;
import com.laka.live.util.Utils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Linhh on 16/3/28.
 */
public class DivergeView extends View {

    private static final String TAG = "RoomDivergeView";
    private final Random mRandom = new Random();

    private ArrayList<DivergeInfo> mDivergeInfos;
    private boolean mIsDiverge = false;

    private PointF mPtStart;
    private PointF mPtEnd;

    private Paint mPaint;

    private static final float mDuration = 0.005F;//0.010F
    private static final int mDefaultHeight = 100;
//    private static final int mDefaultWidth = 100;
//    private static final int mAlphaOffset = 50;

    private DivergeViewProvider mDivergeViewProvider;

    public DivergeView(Context context) {
        this(context, null);
    }

    public DivergeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DivergeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public interface DivergeViewProvider {
        public Bitmap getBitmap(Object obj);
    }


    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //不需要支持wrap_content

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    public void setDivergeViewProvider(DivergeViewProvider divergeViewProvider) {
        mDivergeViewProvider = divergeViewProvider;
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    public void start(PointF startPoint) {
        setStartPoint(startPoint);
        start();
    }

    public PointF getStartPoint() {
        return mPtStart;
    }

    public boolean isRunning() {
        return mIsDiverge;
    }

    public void setDiverges(Object... objs) {
        if (mDivergeInfos == null) {
            mDivergeInfos = new ArrayList<>();
        }
        for (Object obj : objs) {
            mDivergeInfos.add(createDivergeNode(obj));
        }
    }

    public void start() {
        mIsDiverge = true;
        if (mDivergeInfos == null) {
            mDivergeInfos = new ArrayList<>();
        }
        this.post(mRunnable);
    }

    public void stop() {
        this.removeCallbacks(mRunnable);
        if (mDivergeInfos != null) {
            mDivergeInfos.clear();
        }
        mIsDiverge = false;
    }

    public void release() {
        stop();
        mPtEnd = null;
        mPtStart = null;
        mDivergeInfos = null;
    }

    public void setStartPoint(PointF point) {
        mPtStart = point;
    }

    public void setEndPoint(PointF point) {
        mPtEnd = point;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mIsDiverge) {
            release();
        }
    }

    private DivergeInfo createDivergeNode(Object type) {
        int width = getViewWidth();
        PointF endPoint = mPtEnd;
        if (endPoint == null) {
            endPoint = new PointF(mRandom.nextInt(width), 0);
        }
//        int height = mDivergeViewProvider == null ? mDefaultHeight : mDivergeViewProvider.getBitmap(type).getHeight();
        if (mPtStart == null) {
            mPtStart = new PointF(getMeasuredWidth() / 2, getMeasuredHeight() - mDefaultHeight);//默认起始高度
        }
        return new DivergeInfo(
                mPtStart.x,
                mPtStart.y,
                getBreakPointF(2, 3),
                endPoint,
                type);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDivergeViewProvider == null) {
            return;
        }
        if (mDivergeInfos == null) {
            return;
        }
        if (mIsDiverge) {
            for (int i = 0; i < mDivergeInfos.size(); i++) {
                DivergeInfo divergeInfo = mDivergeInfos.get(i);
                if (divergeInfo.mY <= divergeInfo.mEndPoint.y) {
                    mDivergeInfos.remove(i);
                    i--;
                    continue;
                }
//                Log.d(TAG,"divergeInfo.mY="+divergeInfo.mY+" mPtStart.y="+mPtStart.y);



                Bitmap bmp = mDivergeViewProvider.getBitmap(divergeInfo.mType);
                if (bmp != null) {
//                    if (divergeInfo.mY > mPtStart.y - 30) {//增加从小变大和透明度0-1
////                    int alpha = 1;
//                        int alpha = Math.abs((int) (255 * (30 - (divergeInfo.mY + 30 - mPtStart.y)) / 30));
//                        mPaint.setAlpha(alpha);
//                        float scale = (30 - (divergeInfo.mY + 30 - mPtStart.y)) / 30;
//                        scale = Math.abs(scale);
////                    Matrix matrix = canvas.getMatrix();
////                    matrix.preScale(scale,scale);
////                    canvas.setMatrix(matrix);
////                    Log.d(TAG,"step1 divergeInfo.mY="+divergeInfo.mY+" mPtStart.y="+mPtStart.y
////                            +" scale="+scale+" alpha="+alpha);
//                        canvas.drawBitmap(bmp, divergeInfo.mX, divergeInfo.mY, mPaint);
//                    } else {
                    float totalY = (mPtStart.y - mPtEnd.y);
//                       Log.d(TAG," 总高度差="+totalY);

                    float scale = 1f;
                    if (mPtStart.y - divergeInfo.mY < 0.3 * totalY) {//淡入，下部分
                        int alpha = Math.abs((int) (255 * (1 - divergeInfo.mY / mPtStart.y) * 3));
                        mPaint.setAlpha(alpha);

                        scale = (1f- divergeInfo.mY / mPtStart.y)*2+0.4f;
                        scale = Math.abs(scale);
                        if (scale > 1f) {
                            scale = 1f;
                        }



//                        Log.d(TAG, " 淡入 mY=" + divergeInfo.mY +" mX="+divergeInfo.mX+ " deltaX="+divergeInfo.deltaX+ " scale=" + scale+" mPtStart.y="+mPtStart.y);
                    } else if (mPtStart.y - divergeInfo.mY < 0.7 * totalY) {//全显
                        mPaint.setAlpha(255);
//                        Log.d(TAG, " 全显示 mY=" + divergeInfo.mY+" mX="+divergeInfo.mX+ " deltaX="+divergeInfo.deltaX);
                    } else {//淡出，上部分
                        int alpha = Math.abs((int) (255 * (divergeInfo.mY / mPtStart.y * 3)));
                        mPaint.setAlpha(alpha);
//                        Log.d(TAG, " 淡出 mY=" + divergeInfo.mY +" mX="+divergeInfo.mX+ " deltaX="+divergeInfo.deltaX+ " alpha=" + alpha);
                    }

                    Matrix matrix = canvas.getMatrix();
                    canvas.save();
                    matrix.setTranslate(divergeInfo.mX,divergeInfo.mY);
                    matrix.preScale(scale, scale);
                    canvas.concat(matrix);
                    canvas.drawBitmap(bmp, 0, 0, mPaint);
                    canvas.restore();

//                   canvas.drawBitmap(bmp, divergeInfo.mX, divergeInfo.mY, mPaint);

//                    canvas.setMatrix(matrix);

//                    // Matrix类进行图片处理（缩小或者旋转）
//                    Matrix matrix = new Matrix();
//                    matrix.postScale(scale,scale);
//                    // 生成新的图片
//                    bmp = Bitmap.createBitmap(bmp, 0, 0,  bmp.getWidth(),
//                            bmp.getHeight(), matrix, true);

//                    Matrix matrix = canvas.getMatrix();
//                    matrix.preScale(1,1);
//                    canvas.setMatrix(matrix);
//                    Log.d(TAG,"step2 divergeInfo.mY="+divergeInfo.mY+" mPtStart.y="+mPtStart.y+
//                             " alpha="+alpha);

//                    }
                }

                float timeLeft = 1.0F - divergeInfo.mDuration;
                divergeInfo.mDuration += mDuration;
                float x, y;
                //二次贝塞尔
                float time1 = timeLeft * timeLeft;
                float time2 = 2 * timeLeft * divergeInfo.mDuration;
                float time3 = divergeInfo.mDuration * divergeInfo.mDuration;
                x = time1 * (mPtStart.x)
                        + time2 * (divergeInfo.mBreakPoint.x)
                        + time3 * (divergeInfo.mEndPoint.x);

                divergeInfo.deltaX = x -  divergeInfo.mX;//计算变量
                divergeInfo.mX = x;
                y = time1 * (mPtStart.y)
                        + time2 * (divergeInfo.mBreakPoint.y)
                        + time3 * (divergeInfo.mEndPoint.y);
                divergeInfo.mY = y;

            }
            this.post(mRunnable);
        }
    }

    private PointF getBreakPointF(int scale1, int scale2) {
        PointF pointF = new PointF();
        int width = getViewWidth();
        pointF.x = mRandom.nextInt((width- getPaddingRight() + getPaddingLeft()) / scale1) + width / scale2;
        pointF.y = mRandom.nextInt((width - getPaddingBottom() + getPaddingTop()) / scale1) + width / scale2;
        return pointF;
    }

    private int getViewWidth() {
        int width = getMeasuredWidth();
        if(width==0){
            width = Utils.dip2px(getContext(),130);
        }
        return width;
    }

    public class DivergeInfo {
        public float mDuration;
        public PointF mBreakPoint;
        public PointF mEndPoint;
        public float mX;
        public float deltaX;
        public float mY;
        public Object mType;

        public DivergeInfo(float x, float y, PointF breakPoint, PointF endPoint, Object type) {
            mDuration = 0.0f;
            mEndPoint = endPoint;
            mX = x;
            mY = y;
            mBreakPoint = breakPoint;
            mType = type;
        }
    }

}
