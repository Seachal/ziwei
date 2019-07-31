package com.laka.live.shopping.xrecyclerview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laka.live.R;


/**
 * Created by zhxu on 2016/4/21.
 * Email:357599859@qq.com
 */
public class ArrowRefreshHeader extends LinearLayout implements BaseRefreshHeader {

    private View mContainer;
    private ImageView mArrowImageView, mProgressBar;

    private AnimationDrawable mAnimationDrawable;

    private TextView mStatusTextView;
    private int mState = STATE_NORMAL;

    private Runnable mRunnable;

    public int mMeasuredHeight, mCurrPosition = 0;

    private int[] mRefreshImageArray = {R.drawable.new_refresh_1, R.drawable.new_refresh_2, R.drawable.new_refresh_3,
            R.drawable.new_refresh_4, R.drawable.new_refresh_5, R.drawable.new_refresh_6, R.drawable.new_refresh_7,
            R.drawable.new_refresh_8, R.drawable.new_refresh_9, R.drawable.new_refresh_10, R.drawable.new_refresh_11,
            R.drawable.new_refresh_12, R.drawable.new_refresh_13, R.drawable.new_refresh_14, R.drawable.new_refresh_15,
            R.drawable.new_refresh_16, R.drawable.new_refresh_17, R.drawable.new_refresh_18, R.drawable.new_refresh_19, R.drawable.new_refresh_20
    };

    public ArrowRefreshHeader(Context context) {
        this(context, null);
    }

    public ArrowRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        mContainer = LayoutInflater.from(context).inflate(R.layout.x_recycler_view_header, null);
        addView(mContainer, new LayoutParams(LayoutParams.MATCH_PARENT, 0));
        setGravity(Gravity.BOTTOM);

        mStatusTextView = (TextView) findViewById(R.id.refresh_status_text);
        mProgressBar = (ImageView) findViewById(R.id.header_progressbar);
        mArrowImageView = (ImageView) findViewById(R.id.header_arrow);
        mAnimationDrawable = (AnimationDrawable) mProgressBar.getBackground();

        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mMeasuredHeight = getMeasuredHeight();
    }

    public void setState(int state) {
        if (state == mState) return;
        mArrowImageView.setVisibility(View.VISIBLE);
        switch (state) {
            case STATE_NORMAL:
                mStatusTextView.setText(R.string.header_hint_normal);
                break;
            case STATE_RELEASE_TO_REFRESH:
                if (mState != STATE_RELEASE_TO_REFRESH) {
                    mStatusTextView.setText(R.string.header_hint_release);
                }
                break;
            case STATE_REFRESHING:
                startRefreshing();
                mStatusTextView.setText(R.string.refreshing);
                break;
            case STATE_DONE:
                mArrowImageView.setVisibility(View.GONE);
                mStatusTextView.setText(R.string.refresh_done);
                break;
            default:
        }
        mState = state;
    }

    public int getState() {
        return mState;
    }

    public void setShowHeight(int height) {
        if (height < 0) {
            height = 0;
        }
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public int getShowHeight() {
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        return lp.height;
    }

    @Override
    public void refreshComplete() {
        if (mRunnable == null) {
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    setState(STATE_DONE);
                    reset();
                }
            };
            postDelayed(mRunnable, 500);
        }
    }

    @Override
    public void onMove(float delta, int dragY) {
        int changeHeight = mMeasuredHeight / mRefreshImageArray.length;
        int height = getShowHeight();
        mCurrPosition = height / changeHeight;
        if (mCurrPosition >= 0 && mCurrPosition < mRefreshImageArray.length) {
            mArrowImageView.setImageResource(mRefreshImageArray[mCurrPosition]);
        }
        if (height > 0 || delta > 0) {
            setShowHeight((int) delta + height);
            if (mState <= STATE_RELEASE_TO_REFRESH) {
                if (height > mMeasuredHeight) {
                    setState(STATE_RELEASE_TO_REFRESH);
                } else {
                    setState(STATE_NORMAL);
                }
            }
        }
    }

    @Override
    public boolean releaseAction() {
        boolean isOnRefresh = false;
        int destHeight = 0, height = getShowHeight();
        if (height == 0) {
            isOnRefresh = false;
        }
        if (height > mMeasuredHeight && mState < STATE_REFRESHING) {
            setState(STATE_REFRESHING);
            isOnRefresh = true;
        }
        if (mState == STATE_REFRESHING) {
            destHeight = mMeasuredHeight;
        }
        smoothScrollTo(destHeight);
        return isOnRefresh;
    }

    public void reset() {
        smoothScrollTo(0);
        postDelayed(new Runnable() {
            public void run() {
                finish();
                setState(STATE_NORMAL);
                removeCallbacks(mRunnable);
                mRunnable = null;
            }
        }, 500);
    }

    private void startRefreshing() {
        mArrowImageView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        if (mAnimationDrawable != null) {
            mProgressBar.clearAnimation();
            mAnimationDrawable.start();
        }
    }

    private void finish() {
        if (mAnimationDrawable != null && mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
        }
        mProgressBar.clearAnimation();
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private void smoothScrollTo(int destHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(getShowHeight(), destHeight);
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setShowHeight((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }
}
