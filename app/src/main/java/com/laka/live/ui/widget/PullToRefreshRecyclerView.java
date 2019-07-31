package com.laka.live.ui.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.laka.live.ui.widget.ptr.PtrLakaFrameLayout;
import com.laka.live.util.Utils;
import com.lhh.ptrrv.library.footer.loadmore.BaseLoadMoreView;
import com.lhh.ptrrv.library.footer.loadmore.DefaultLoadMoreView;
import com.lhh.ptrrv.library.header.Header;
import com.lhh.ptrrv.library.impl.PrvInterface;

/**
 * Created by linhonghong on 2015/11/11.
 */
public class PullToRefreshRecyclerView extends PtrLakaFrameLayout implements PrvInterface {

//    private static final String TAG = "PTRRV";

    private RecyclerView mRecyclerView;

    //root header
    private Header mRootHeader;

    //main view,contain footer，header etc.
    protected RelativeLayout mRootRelativeLayout;

    //header
    private View mHeader;

    private View mEmptyView;

    //default = 10
    private int mLoadMoreCount = 10;

    private int mCurScroll;

    private boolean mIsSwipeEnable = false;

    private BaseLoadMoreView mLoadMoreFooter;

    private com.lhh.ptrrv.library.PullToRefreshRecyclerView.PagingableListener mPagingableListener;

    private AdapterObserver mAdapterObserver;

    private boolean isLoading = false;
    private boolean hasMoreItems = false;

    private com.lhh.ptrrv.library.PullToRefreshRecyclerView.OnScrollListener mOnScrollLinstener;

    private InterOnScrollListener mInterOnScrollListener;

    //    private PullToRefreshRecyclerViewUtil mPtrrvUtil;
    private CustomPullToRefreshRecyclerViewUtil mCptrrvUtil;

    private boolean isLoadMoreEnable = true;

    private LoadMoreFoot mFootLoadingView;

    private AnimationDrawable mLoadingDrawable;

    public boolean mShowFooterView = true;

    public interface PagingableListener {
        void onLoadMoreItems();
    }

    public interface OnScrollListener {
        void onScrollStateChanged(RecyclerView recyclerView, int newState);

        void onScrolled(RecyclerView recyclerView, int dx, int dy);

        //old-method, like listview 's onScroll ,but it's no use ,right ? by linhonghong 2015.10.29
        void onScroll(RecyclerView recyclerView, int firstVisibleItem, int visibleItemCount, int totalItemCount);
    }

    public PullToRefreshRecyclerView(Context context) {
        super(context);
        this.setup();
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setup();
    }

    /*public PullToRefreshRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setup();
    }*/

    /**
     * main
     */
    private void setup() {
        setupExtra();
        initView();
        setLinster();
    }


    /**
     * initView
     */
    private void initView() {
        mRootRelativeLayout = (RelativeLayout) LayoutInflater.from(getContext()).inflate(com.lhh.ptrrv.library.R.layout.ptrrv_root_view, null);

        this.addView(mRootRelativeLayout);

        /*this.setColorSchemeResources(com.lhh.ptrrv.library.R.color.swap_holo_green_bright, com.lhh.ptrrv.library.R.color.swap_holo_bule_bright,
                com.lhh.ptrrv.library.R.color.swap_holo_green_bright, com.lhh.ptrrv.library.R.color.swap_holo_bule_bright);*/
//        setColorSchemeResources(R.color.colorF8C617);

        mRecyclerView = (RecyclerView) mRootRelativeLayout.findViewById(com.lhh.ptrrv.library.R.id.recycler_view);

//        mLinearLayoutManager = new LinearLayoutManager(mContext);
//        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        if (!mIsSwipeEnable) {
            this.setEnabled(false);
        }

    }

    /**
     * Init
     */
    private void setupExtra() {
        isLoading = false;
        hasMoreItems = false;
//        mPtrrvUtil = new PullToRefreshRecyclerViewUtil();
        mCptrrvUtil = new CustomPullToRefreshRecyclerViewUtil();
    }

    private void setLinster() {
        mInterOnScrollListener = new InterOnScrollListener();
        mRecyclerView.addOnScrollListener(mInterOnScrollListener);
    }

    @Override
    public void setOnRefreshComplete() {
//        this.setRefreshing(false);
        if (isRefreshing()) {
            refreshComplete();
        }
    }

    @Override
    public void setOnLoadMoreComplete() {
        setHasMoreItems(false);
    }

    @Override
    public void setPagingableListener(com.lhh.ptrrv.library.PullToRefreshRecyclerView.PagingableListener pagingableListener) {
        mPagingableListener = pagingableListener;
    }

    @Override
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
        if (mAdapterObserver == null) {
            mAdapterObserver = new AdapterObserver();
        }
        if (adapter != null) {
            adapter.registerAdapterDataObserver(mAdapterObserver);
            mAdapterObserver.onChanged();
        }
    }

    @Override
    public void addHeaderView(View view) {
        //2015.11.17 finish method
        if (mHeader != null) {
            mRootRelativeLayout.removeView(mHeader);
        }

        mHeader = view;

        if (mHeader == null) {
            return;
        }

        mHeader.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                mHeader.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                } else {
//                    mHeader.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                }

                if (getRecyclerView() == null || mHeader == null) {
                    return;
                }
                if (mRootHeader == null) {
                    mRootHeader = new Header();
                }
                mRootHeader.setHeight(mHeader.getMeasuredHeight());
                getRecyclerView().removeItemDecoration(mRootHeader);
                getRecyclerView().addItemDecoration(mRootHeader);
                getRecyclerView().getAdapter().notifyDataSetChanged();
            }
        });

        mRootRelativeLayout.addView(mHeader);
    }


    @Override
    public void removeHeader() {
        if (mRootHeader != null) {
            getRecyclerView().removeItemDecoration(mRootHeader);
            mRootHeader = null;
        }
        if (mHeader != null) {
            mRootRelativeLayout.removeView(mHeader);
            mHeader = null;
        }
    }

    @Override
    public void setFooter(View view) {
        // now is empty, you can do in extra adapter
    }

    public void setFooter(LoadMoreFoot footer) {
        mFootLoadingView = footer;
    }

    @Override
    public void setLoadMoreFooter(BaseLoadMoreView loadMoreFooter) {
        mLoadMoreFooter = loadMoreFooter;
    }

    @Override
    public void addOnScrollListener(com.lhh.ptrrv.library.PullToRefreshRecyclerView.OnScrollListener onScrollLinstener) {
        mOnScrollLinstener = onScrollLinstener;
    }

    public void removeOnScrollListener() {
        mOnScrollLinstener = null;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        if (mRecyclerView != null) {
            return mRecyclerView.getLayoutManager();
        }
        return null;
    }

    @Override
    public void onFinishLoading(boolean hasMoreItems, boolean needSetSelection) {

        if (getLayoutManager() == null) {
            return;
        }

        /*if (!hasMoreItems && mLoadMoreFooter != null) {
            //if it's last line, minus the extra height of loadmore
            setCurScroll(mCurScroll - mLoadMoreFooter.getLoadMorePadding());
        }*/

        // if items is too short, don't show loadingview
//        if (getLayoutManager().getItemCount() < mLoadMoreCount) {
//            hasMoreItems = false;
//        }
        setHasMoreItems(hasMoreItems);

        isLoading = false;

        if (needSetSelection) {
            int first = findFirstVisibleItemPosition();
            mRecyclerView.scrollToPosition(--first);
        }

    }

//    public int findFirstVisibleItemPosition() {
//        return mPtrrvUtil.findFirstVisibleItemPosition(getLayoutManager());
//    }
//
//    public int findLastVisibleItemPosition() {
//        return mPtrrvUtil.findLastVisibleItemPosition(getLayoutManager());
//    }
//
//    public int findFirstCompletelyVisibleItemPosition() {
//        return mPtrrvUtil.findFirstCompletelyVisibleItemPosition(getLayoutManager());
//    }

    public int findFirstVisibleItemPosition() {
        int[] positions = mCptrrvUtil.findFirstVisibleItemPosition(getLayoutManager());
        return Utils.retureMinNum(positions);
    }

    public int findLastVisibleItemPosition() {
        int[] positions = mCptrrvUtil.findLastVisibleItemPosition(getLayoutManager());
        return Utils.retureMaxNum(positions);
    }

    public int findFirstCompletelyVisibleItemPosition() {
        int[] positions = mCptrrvUtil.findFirstCompletelyVisibleItemPosition(getLayoutManager());
        return Utils.retureMinNum(positions);
    }

    @Override
    public void setSwipeEnable(boolean enable) {
        //just like extra setEnable(boolean).but it's more easy to use, like super.setEnable
        mIsSwipeEnable = enable;
        this.setEnabled(mIsSwipeEnable);
    }

    @Override
    public boolean isSwipeEnable() {
        return mIsSwipeEnable;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return this.mRecyclerView;
    }

    @Override
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(layoutManager);
        }
    }

    public void setItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        if (mRecyclerView != null) {
            mRecyclerView.addItemDecoration(itemDecoration);
        }
    }

    @Override
    public void setLoadMoreCount(int count) {
        mLoadMoreCount = count;
    }

    @Override
    public void release() {

    }

    public void setLoadmoreString(String str) {
        if (mLoadMoreFooter != null) {
            mLoadMoreFooter.setLoadmoreString(str);
        }
    }

    private void setHasMoreItems(boolean hasMoreItems) {

        if (!isLoadMoreEnable) {
            hasMoreItems = isLoadMoreEnable;
        }
        this.hasMoreItems = hasMoreItems;
        if (mLoadMoreFooter == null) {
            mLoadMoreFooter = new DefaultLoadMoreView(getContext(), getRecyclerView());
        }
        if (mShowFooterView) {
            //add loadmore
            mRecyclerView.removeItemDecoration(mLoadMoreFooter);
            mRecyclerView.addItemDecoration(mLoadMoreFooter);
            if (mFootLoadingView != null) {
                mRootRelativeLayout.removeView(mFootLoadingView);

                if (hasMoreItems) {
                    mFootLoadingView.showLoading();
                } else {
                    mFootLoadingView.hideLoading();
                }

                if (mLoadMoreFooter instanceof LoadMoreView) {

                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            mLoadMoreFooter.getLoadMorePadding());
                    mRootRelativeLayout.addView(mFootLoadingView, params);
                    ((LoadMoreView) mLoadMoreFooter).setLoadMoreView(mFootLoadingView);
                }
            }
        }
    }

    private void setCurScroll(int curScroll) {
        mCurScroll = curScroll;

        if (mHeader != null) {
            mHeader.setTranslationY(-mCurScroll);
        }
    }

    private class InterOnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            //do super before callback
            if (mOnScrollLinstener != null) {
                mOnScrollLinstener.onScrollStateChanged(recyclerView, newState);
            }

            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            if (imagePipeline != null) {
                if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    imagePipeline.pause();
                } else {
                    imagePipeline.resume();
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            setCurScroll(dy + mCurScroll);

            if (mOnScrollLinstener != null) {
                mOnScrollLinstener.onScrolled(recyclerView, dx, dy);
            }

            //do super before callback
            if (getLayoutManager() == null) {
                //here layoutManager is null
                return;
            }

            int firstVisibleItem, visibleItemCount, totalItemCount, lastVisibleItem;
            visibleItemCount = getLayoutManager().getChildCount();
            totalItemCount = getLayoutManager().getItemCount();
            firstVisibleItem = findFirstVisibleItemPosition();
            //sometimes ,the last item is too big so as that the screen cannot show the item fully
            lastVisibleItem = findLastVisibleItemPosition();
//            lastVisibleItem = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();

            if (mIsSwipeEnable) {
                if (findFirstCompletelyVisibleItemPosition() != 0) {
                    //here has a bug, if the item is too big , use findFirstCompletelyVisibleItemPosition will cannot swipe
                    PullToRefreshRecyclerView.this.setEnabled(false);
                } else {
                    PullToRefreshRecyclerView.this.setEnabled(true);
                }
            }

            if (totalItemCount < mLoadMoreCount) {
                setHasMoreItems(false);
                isLoading = false;
            } else if (isLoadMoreEnable && !isLoading && hasMoreItems) {
                if ((lastVisibleItem + 1) == totalItemCount) {
                    if (mFootLoadingView != null) {
                        mFootLoadingView.setVisibility(VISIBLE);
                    }
                    if (mPagingableListener != null) {
                        isLoading = true;
                        mPagingableListener.onLoadMoreItems();
                    }
                } else {
                    if (mFootLoadingView != null) {
                        mFootLoadingView.setVisibility(GONE);
                    }
                }

            }


            if (mOnScrollLinstener != null) {
                mOnScrollLinstener.onScroll(recyclerView, firstVisibleItem, visibleItemCount, totalItemCount);
            }

        }

    }

    private class AdapterObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            //adapter has change
            if (mRecyclerView == null) {
                //here must be wrong ,recyclerView is null????
                return;
            }

            RecyclerView.Adapter<?> adapter = mRecyclerView.getAdapter();
            if (adapter != null && mEmptyView != null) {
                if (adapter.getItemCount() == 0) {
                    if (mIsSwipeEnable) {
                        PullToRefreshRecyclerView.this.setEnabled(false);
                    }
                    mEmptyView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                } else {
                    if (mIsSwipeEnable) {
                        PullToRefreshRecyclerView.this.setEnabled(true);
                    }
                    mEmptyView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void setIsLoadMoreEnable(boolean isEnable) {
        isLoadMoreEnable = isEnable;
    }

    @Override
    public void scrollToPosition(int position) {
        if (getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager manager = (LinearLayoutManager) getLayoutManager();
            manager.scrollToPosition(position);
        } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) getLayoutManager();
            staggeredGridLayoutManager.scrollToPosition(position);
        }
    }

    @Override
    public void smoothScrollToPosition(int position) {
        mRecyclerView.smoothScrollToPosition(position);
    }

    public void scrollToPosition(int position, int offset) {
        if (getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager manager = (LinearLayoutManager) getLayoutManager();
            manager.scrollToPositionWithOffset(position, offset);
        } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) getLayoutManager();
            staggeredGridLayoutManager.scrollToPositionWithOffset(position, offset);
        }
    }

    private int getLastItemBottom() {
        final int childSize = mRecyclerView.getChildCount();
        final View child = mRecyclerView.getChildAt(childSize - 1);
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
        return child.getBottom() + layoutParams.bottomMargin;
    }

    public int getCurScroll() {
        return mCurScroll;
    }

    public int getLoadMoreCount() {
        return mLoadMoreCount;
    }

    public void hideLoadMoreFoot() {
        mFootLoadingView.setVisibility(GONE);
    }

}

