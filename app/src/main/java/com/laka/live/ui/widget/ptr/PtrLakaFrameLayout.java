package com.laka.live.ui.widget.ptr;

import android.content.Context;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.ui.widget.RefreshLoadingView;
import com.laka.live.util.Log;
import com.laka.live.util.Utils;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by luwies on 16/9/8.
 */
public class PtrLakaFrameLayout extends PtrFrameLayout implements PtrUIHandler {

    private View mHeadView;

//    private MusicLoading mLoadingView;

    private RefreshLoadingView mLoadingView;

    // 是否要显示刷新状态
    private boolean showRefreshing = true;
    private TextView mHeadText;

    private PtrIndicator mPtrIndicator;
    protected boolean isCompeleted = true;

    /**
     * description:刷新回调
     **/
    private OnPullStateChangeListener onPullStateChangeListener;

    public boolean isCompeleted() {
        return isCompeleted;
    }

    public void setCompeleted(boolean compeleted) {
        isCompeleted = compeleted;
    }

    public boolean isShowRefreshing() {
        return showRefreshing;
    }

    public void setShowRefreshing(boolean showRefreshing) {
        this.showRefreshing = showRefreshing;
        if (showRefreshing) {
            setHeaderView(mHeadView);
        } else {
            LayoutParams lp = new LayoutParams(0, 0);
            mHeadView.setLayoutParams(lp);
        }
    }

    public PtrLakaFrameLayout(Context context) {
        super(context);
        initViews();
    }

    public PtrLakaFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public PtrLakaFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    private void initViews() {

        mPtrIndicator = new PtrIndicator();
        setPtrIndicator(mPtrIndicator);

//        PtrFrameLayout.DEBUG = true;
        Context context = getContext();
        mHeadView = LayoutInflater.from(context).inflate(R.layout.ptr_head_layout, null);
        mLoadingView = (RefreshLoadingView) mHeadView.findViewById(R.id.loading);
//        mLoadingView.setLineMargin(Utils.dip2px(context, 5f));
//        mLoadingView.setBigLineWidth(Utils.dip2px(context, 2f));
//        mLoadingView.setLittleLineWidth(Utils.dip2px(context, 1.5f));
//        mLoadingView.setLineColor(ContextCompat.getColor(context, R.color.colorF65843));
//        mLoadingView.setLineHeight(Utils.dip2px(context, 20f));
//        mLoadingView.setDuration(960L);
//        mLoadingView.stopLoading();
//
//        setLoadingMinTime((int) mLoadingView.getTotalDuration());

        mHeadText = (TextView) mHeadView.findViewById(R.id.text);

        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                Utils.dip2px(context, 60f));
        mHeadView.setLayoutParams(lp);


        setHeaderView(mHeadView);
        addPtrUIHandler(this);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
//        mLoadingView.reset();
        mLoadingView.stop();
        mHeadText.setText(R.string.pull_down_to_refresh);
        if (onPullStateChangeListener != null) {
            onPullStateChangeListener.onPullStateChange(OnPullStateChangeListener.STATE_REFRESH_RESET);
        }
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        mHeadText.setText(R.string.pull_down_to_refresh);
        if (onPullStateChangeListener != null) {
            onPullStateChangeListener.onPullStateChange(OnPullStateChangeListener.STATE_PULLING);
        }
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        setCompeleted(false);
//        mLoadingView.startLoading();
        mLoadingView.start();
        mHeadText.setText(R.string.refreshing);
        if (onPullStateChangeListener != null) {
            onPullStateChangeListener.onPullStateChange(OnPullStateChangeListener.STATE_REFRESHING);
        }
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        setCompeleted(true);
//        mLoadingView.stopLoading();
        mLoadingView.stop();
        mHeadText.setText(R.string.pull_down_to_refresh);
        Log.error("PtrLakaFrameLayout", "mPtrIndicator.isUnderTouch() : " + mPtrIndicator.isUnderTouch());
        if (onPullStateChangeListener != null) {
            onPullStateChangeListener.onPullStateChange(OnPullStateChangeListener.STATE_REFRESH_COMPLETE);
        }
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status,
                                   PtrIndicator ptrIndicator) {
        final int mOffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();

        if (currentPos < mOffsetToRefresh && lastPos >= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                crossRotateLineFromBottomUnderTouch(frame);
            }
        } else if (currentPos > mOffsetToRefresh && lastPos <= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                crossRotateLineFromTopUnderTouch(frame);
            }
        }
    }

    private void crossRotateLineFromTopUnderTouch(PtrFrameLayout frame) {
        if (!frame.isPullToRefresh()) {
            mHeadText.setText(R.string.go_to_refresh);
        }
    }

    private void crossRotateLineFromBottomUnderTouch(PtrFrameLayout frame) {
        mHeadText.setText(R.string.pull_down_to_refresh);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        boolean result = super.dispatchTouchEvent(e);
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mPtrIndicator.onRelease();
                break;
        }
        return result;
    }

    public void setPtrHeadColor(int color) {
//        mLoadingView.setLineColor(color);
    }

    public void setPtrHeadLoadingColor(int color) {
        mHeadText.setTextColor(color);
    }

    public void setOnPullStateChangeListener(OnPullStateChangeListener onPullStateChangeListener) {
        this.onPullStateChangeListener = onPullStateChangeListener;
    }

    /**
     * 下拉状态刷新回调
     */
    public interface OnPullStateChangeListener {


        /**
         * description:对应下拉、正在刷新、刷新完毕、刷新重置四个状态
         **/

        int STATE_PULLING = 1;
        int STATE_REFRESHING = 2;
        int STATE_REFRESH_COMPLETE = 3;
        int STATE_REFRESH_RESET = 4;

        @IntDef({
                STATE_PULLING, STATE_REFRESHING,
                STATE_REFRESH_COMPLETE, STATE_REFRESH_RESET
        })
        @interface REFRESH_STATE {
        }

        /**
         * 回调
         *
         * @param state
         */
        void onPullStateChange(@REFRESH_STATE int state);
    }
}
