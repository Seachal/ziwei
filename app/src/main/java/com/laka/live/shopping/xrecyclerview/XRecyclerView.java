package com.laka.live.shopping.xrecyclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by zhxu on 2016/4/21.
 * Email:357599859@qq.com
 */
public class XRecyclerView extends RecyclerView {

    private Context mContext;

    private boolean mIsLoadingData = false;
    private boolean mHasMore = false;

    private ArrayList<View> mHeaderViews = new ArrayList<>();
    private ArrayList<View> mFootViews = new ArrayList<>();
    private View mEmptyView;
    private View mFootView;

    private Adapter mAdapter;
    private Adapter mWrapAdapter;

    private float mLastY = -1, mStartY = -1;
    private static final float DRAG_RATE = 2f;

    private ArrowRefreshHeader mRefreshHeader;
    private boolean mIsPullRefreshEnabled = true;
    private boolean mIsLoadingMoreEnabled = true;

    private LoadingListener mLoadingListener;

    private TouchListener mTouchListener;

    private float mDispatchTouchEventY = 0;
    private boolean mUpChanged = false;
    private int mTouchSlop;

    public XRecyclerView(Context context) {
        this(context, null);
    }

    public XRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        setHasFixedSize(true);
        setItemAnimator(null);

        ArrowRefreshHeader refreshHeader = new ArrowRefreshHeader(mContext);
        mHeaderViews.add(0, refreshHeader);
        mRefreshHeader = refreshHeader;

        LoadingMoreFooter footView = new LoadingMoreFooter(mContext);
        addFootView(footView);

        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
    }

    public void addHeaderView(View view) {
        if (mIsPullRefreshEnabled && !(mHeaderViews.get(0) instanceof ArrowRefreshHeader)) {
            ArrowRefreshHeader refreshHeader = new ArrowRefreshHeader(mContext);
            mHeaderViews.add(0, refreshHeader);
            mRefreshHeader = refreshHeader;
        }
        mHeaderViews.add(view);
        if (mWrapAdapter != null) {
            ((XRecyclerViewAdapter) mWrapAdapter).addHeaderTypes();
        }
    }

    public void setmFootView(View mFootView) {
        this.mFootView = mFootView;
        setLoadingMoreEnabled(false);
    }

    public void addFootView(final View view) {
        mFootViews.clear();
        mFootViews.add(view);
        mFootViews.get(0).getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    public void hasMore(boolean hasMore) {
        mHasMore = hasMore;
        if (!hasMore) {
            onLoadMore(LoadingMoreFooter.STATE_NO_MORE);
        }
    }

    public void loadMoreComplete() {
        if (mIsLoadingData) {
            onLoadMore(LoadingMoreFooter.STATE_COMPLETE);
        }
    }

    public void refreshComplete() {
        if (!isRefreshHeaderNormal()) {
            mRefreshHeader.refreshComplete();
        }
    }

    private void onLoadMore(int state) {
        mIsLoadingData = false;
        if (state == LoadingMoreFooter.STATE_LOADING) {
            mIsLoadingData = true;
            mLoadingListener.onLoadMore();
        }
        View footView = mFootViews.get(0);
        ((LoadingMoreFooter) footView).setState(state);
    }

    public void setPullRefreshEnabled(boolean enabled) {
        mIsPullRefreshEnabled = enabled;
        mUpChanged = !enabled;
    }

    public void setLoadingMoreEnabled(boolean enabled) {
        mIsLoadingMoreEnabled = enabled;
        if (!enabled) {
            if (mFootViews.size() > 0) {
                mFootViews.get(0).getLayoutParams().height = 0;
            }
        }
    }

    public void setRefreshing(boolean refreshing) {
        if (refreshing && mIsPullRefreshEnabled && mLoadingListener != null) {
            mRefreshHeader.setState(ArrowRefreshHeader.STATE_REFRESHING);
            mRefreshHeader.onMove(mRefreshHeader.getMeasuredHeight(), 0);
            mLoadingListener.onRefresh();
        }
    }

    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
        mDataObserver.onChanged();
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
        mWrapAdapter = new XRecyclerViewAdapter(mHeaderViews, mFootViews, adapter);
        super.setAdapter(mWrapAdapter);
        mAdapter.registerAdapterDataObserver(mDataObserver);
        mDataObserver.onChanged();
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE && mLoadingListener != null && !mIsLoadingData && mIsLoadingMoreEnabled) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;

            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager _layoutManager = ((StaggeredGridLayoutManager) layoutManager);
                int[] into = new int[_layoutManager.getSpanCount()];
                _layoutManager.findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }

            int childCount = layoutManager.getChildCount();
            int itemCount = layoutManager.getItemCount();
            if (childCount > 0 && lastVisibleItemPosition >= itemCount - 1 && itemCount >= childCount && mHasMore && isRefreshHeaderNormal()) {
                onLoadMore(LoadingMoreFooter.STATE_LOADING);
            }

            if (!mHasMore && isRefreshHeaderNormal() && mFootView != null) {
                Log.v("mFootView", "mFootView");
            }
        }
        onDownwardStroke(state);
    }

    private boolean isRefreshHeaderNormal() {
        return mRefreshHeader.getState() == ArrowRefreshHeader.STATE_NORMAL || mRefreshHeader.getState() == ArrowRefreshHeader.STATE_DONE;
    }

    /**
     * 下拉回调
     *
     * @param state
     */
    private void onDownwardStroke(int state) {
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            View view = getChildAt(0);
            if (view != null) {
                if (isOnTop() && view.getTop() == 0) {
                    if (mUpChanged) {
                        mUpChanged = false;
                        if (mTouchListener != null) {
                            mIsPullRefreshEnabled = true;
                            mTouchListener.onDownwardStroke();
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDispatchTouchEventY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float currY = mDispatchTouchEventY - ev.getY();
                if (currY > 0 && Math.abs(currY) > mTouchSlop && mAdapter.getItemCount() > 4) {
                    if (!mUpChanged) {
                        mUpChanged = true;
                        if (mTouchListener != null) {
                            mIsPullRefreshEnabled = false;
                            mTouchListener.onUpwardStroke();
                        }
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                mStartY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (isOnTop() && mIsPullRefreshEnabled) {
                    mRefreshHeader.onMove(deltaY / DRAG_RATE, (int) (ev.getY() - mStartY));
                    if (mRefreshHeader.getShowHeight() > 0 && mRefreshHeader.getState() < ArrowRefreshHeader.STATE_REFRESHING) {
                        return false;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mLastY = -1;
                if (isOnTop() && mIsPullRefreshEnabled) {
                    if (mRefreshHeader.releaseAction()) {
                        if (mLoadingListener != null) {
                            mLoadingListener.onRefresh();
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private boolean isOnTop() {
        if (mHeaderViews == null || mHeaderViews.isEmpty()) {
            return false;
        }

        View view = mHeaderViews.get(0);
        if (view.getParent() != null) {
            return true;
        } else {
            return false;
        }
    }

    private final AdapterDataObserver mDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            Adapter<?> adapter = getAdapter();
            if (adapter != null && mEmptyView != null) {
                int emptyCount = 0;
                if (mIsPullRefreshEnabled) {
                    emptyCount++;
                }
                if (mIsLoadingMoreEnabled) {
                    emptyCount++;
                }
                if (adapter.getItemCount() == emptyCount) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    setVisibility(View.VISIBLE);
                }
            }
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    };

    public void setLoadingListener(LoadingListener listener) {
        mLoadingListener = listener;
    }

    public void setTouchListener(TouchListener mTouchListener) {
        this.mTouchListener = mTouchListener;
    }

    public interface LoadingListener {
        void onRefresh();

        void onLoadMore();
    }

    public interface TouchListener {
        void onUpwardStroke();

        void onDownwardStroke();
    }
}
