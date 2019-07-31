package com.laka.live.shopping.shoppingcar.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.laka.live.R;
import com.laka.live.shopping.shoppingcar.adapter.ShoppingCarTopViewAdapter;


/**
 * Created by chenjiawei on 2016/3/24.
 */
public class ShoppingCarTopTextView extends LinearLayout {
    //控件高度
    private float mViewHeight = 0f;
    //间隔时间
    private final int mGap = 4000;
    //动画间隔时间
    private final int mAnimDuration = 1000;
    //显示文字的尺寸
    private ShoppingCarTopViewAdapter mAdapter;
    private final float jdViewHeight = 50;
    //显示的view
    private View mFirstView;
    private View mSecondView;
    //播放的下标
    private int mPosition;
    //线程的标识
    private boolean mIsStarted;
    private OnTextShowCallback mOnTextShowCallback;

    public ShoppingCarTopTextView(Context context) {
        this(context, null);
    }

    public ShoppingCarTopTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShoppingCarTopTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * 初始化属性
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        //设置为垂直方向
        setOrientation(VERTICAL);
        //获取自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ShoppingCarTopView);
        mViewHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, jdViewHeight, getResources().getDisplayMetrics());
        int gap = array.getInteger(R.styleable.ShoppingCarTopView_gap, mGap);
        int animDuration = array.getInteger(R.styleable.ShoppingCarTopView_animDuration, mAnimDuration);

        if (mGap <= mAnimDuration) {
            gap = mGap;
            animDuration = mAnimDuration;
        }
        //关闭清空TypedArray
        array.recycle();
    }

    /**
     * 设置数据
     */
    public void setAdapter(ShoppingCarTopViewAdapter adapter) {
        this.mAdapter = adapter;
        setupAdapter();
    }

    /**
     * 开启线程
     */
    public void start() {
        if (mAdapter == null) {
            return;
        }
        if (!mIsStarted && mAdapter.getCount() > 1) {
            mIsStarted = true;
            postDelayed(mRunnable, mGap);//间隔mgap刷新一次UI
        }
    }

    /**
     * 暂停滚动
     */
    public void stop() {
        //移除handle更新
        removeCallbacks(mRunnable);
        //暂停线程
        mIsStarted = false;
    }

    /**
     * 设置数据适配
     */
    private void setupAdapter() {
        if (mAdapter == null) {
            return;
        }
        //移除所有view
        removeAllViews();
        //只有一条数据,不滚东
        if (mAdapter.getCount() == 1) {
            mFirstView = mAdapter.getView(this);
            mAdapter.setItem(mFirstView, mAdapter.getItem(0));
            addView(mFirstView);
        } else {
            //多个数据
            mFirstView = mAdapter.getView(this);
            mSecondView = mAdapter.getView(this);
            mAdapter.setItem(mFirstView, mAdapter.getItem(0));
            mAdapter.setItem(mSecondView, mAdapter.getItem(1));
            //把2个添加到此控件里
            addView(mFirstView);
            addView(mSecondView);
            mPosition = 1;
            mIsStarted = false;
        }

    }

    /**
     * 测量控件的宽高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (LayoutParams.WRAP_CONTENT == getLayoutParams().height) {
            getLayoutParams().height = (int) mViewHeight;
        } else {
            mViewHeight = getHeight();
        }

        if (mFirstView != null) {
            mFirstView.getLayoutParams().height = (int) mViewHeight;
        }
        if (mSecondView != null) {
            mSecondView.getLayoutParams().height = (int) mViewHeight;
        }
    }

    /**
     * 垂直滚蛋
     */
    public void performSwitch() {
        if (mAdapter == null) {
            return;
        }
        //属性动画控制控件滚动
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(mFirstView, "translationY", mFirstView.getTranslationY() - mViewHeight);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mSecondView, "translationY", mSecondView.getTranslationY() - mViewHeight);
        //动画集
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator1, animator2);//2个动画一起
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {//动画结束
                mFirstView.setTranslationY(0);
                mSecondView.setTranslationY(0);
                View removedView = getChildAt(0);//获得第一个子布局
                mPosition++;
                //设置显示的布局
                mAdapter.setItem(removedView, mAdapter.getItem(mPosition % mAdapter.getCount()));
                //移除前一个view
                removeView(removedView);
                //添加下一个view
                addView(removedView, 1);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                mOnTextShowCallback.onShow();
            }
        });
        set.setDuration(mAnimDuration);
        set.start();
    }

    private AnimRunnable mRunnable = new AnimRunnable();

    private class AnimRunnable implements Runnable {

        @Override
        public void run() {
            performSwitch();
            postDelayed(this, mGap);
        }
    }

    /**
     * 销毁View的时候调用
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    /**
     * 屏幕 旋转
     *
     * @param newConfig
     */
    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public interface OnTextShowCallback {
        void onShow();
    }

    public void setOnTextShowCallback(OnTextShowCallback mOnTextShowCallback) {
        this.mOnTextShowCallback = mOnTextShowCallback;
    }
}
