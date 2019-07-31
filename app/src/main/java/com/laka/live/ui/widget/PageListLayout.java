package com.laka.live.ui.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.laka.live.R;
import com.laka.live.application.LiveApplication;
import com.laka.live.msg.ListMag;
import com.laka.live.msg.Msg;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.util.Log;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;

import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by luwies on 16/3/22.
 */
public class PageListLayout extends PullToRefreshRecyclerView
        implements com.lhh.ptrrv.library.PullToRefreshRecyclerView.PagingableListener {

    private int mDefaultPage = -1; // 默认的起始页

    private Context mContext;

    private int mCurrentPage = mDefaultPage;

    protected BaseAdapter mAdapter;

    private GsonHttpConnection.OnResultListener mRefreshListener;

    private GsonHttpConnection.OnResultListener mGetNextListener;

    private OnRequestCallBack mOnRequestCallBack;

    private View mDataView;

    private View mErrorView;

    private LoadLoadingView mLoadingView;

    private TextView mErrorTextView;

    private CharSequence mEmptyTipText;

    private CharSequence mNetworkTipText;

    private Drawable mEmptyDrawable;

    private Drawable mNetworkDrawable;

    private CharSequence mDataExceptionTipText;

    private Drawable mDataExceptionDrawable;

    private String mLastUrl;

    private OnResultListener mOnResultListener;

    private ErrorState mErrorState;

    private boolean isShowEmpty = true;
    private boolean isReloadWhenEmpty = false;
    private long mRefreshDelay;
    private boolean enableRefresh = true;


    public PageListLayout(Context context) {
        super(context);
        init(context);
    }

    public PageListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

        mContext = context;

        initPtr();

        mDataView = getRecyclerView();

        mEmptyTipText = mContext.getText(R.string.empty_tips);

        mNetworkTipText = mContext.getText(R.string.network_error_tips);

        mEmptyDrawable = ContextCompat.getDrawable(mContext, R.drawable.public_pic_empty);

        mNetworkDrawable = ContextCompat.getDrawable(mContext, R.drawable.public_pic_nowifi);

        mDataExceptionTipText = context.getText(R.string.empty_tips);
        mDataExceptionDrawable = ContextCompat.getDrawable(mContext, R.drawable.public_pic_empty);

        addErrorView(R.layout.error_layout);

        addLoadingView();

        setPagingableListener(this);

        setSwipeEnable(true);

        setLoadmoreString("loading");

//        setLoadingMinTime(800);

// set loadmore enable, onFinishLoading(can load more? , select before item)
        onFinishLoading(true, false);
        setLoadMoreFooter(new LoadMoreView(mContext, getRecyclerView()));

        /*UpLoadingView upLoadingView = new UpLoadingView(mContext);
        upLoadingView.setScaleType(ImageView.ScaleType.CENTER);*/

        setFooter(new LoadMoreFoot(context));

        disableWhenHorizontalMove(true);
    }

    private void initPtr() {
//        setLastUpdateTimeRelateObject(mContext);
        setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refreshData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return enableRefresh && PtrDefaultHandler.checkContentCanBePulledDown(frame, getRecyclerView(), header);
            }
        });

        setResistance(1.7f);
        setRatioOfHeaderHeightToRefresh(1.2f);
        setDurationToClose(200);
        setDurationToCloseHeader(1000);
        // default is false
        setPullToRefresh(false);
        // default is true
        setKeepHeaderWhenRefresh(true);
        //work with ViewPager
        disableWhenHorizontalMove(true);
        //work with LongPressed
        setInterceptEventWhileWorking(true);
//        setLoadingMinTime(0);
        setEnabledNextPtrAtOnce(true);
    }

    public void setEnableRefresh(boolean enableRefresh) {
        this.enableRefresh = enableRefresh;
    }

    public void setAdapter(BaseAdapter adapter) {
        super.setAdapter(adapter);
        mAdapter = adapter;
    }

    public void setOnRequestCallBack(OnRequestCallBack callBack) {
        mOnRequestCallBack = callBack;
    }

    @Override
    public void onLoadMoreItems() {
        getNextPage();
    }

    public void loadData() {
        loadData(true);
    }

    public void loadData(boolean isClear) {
        if (isClear) {
            mAdapter.clear();
            showLoading();
        }
        refreshData();

    }

    /**
     * 重新刷新，但不显示刷新状态
     *
     * @param isClear
     * @param isShowLoading
     */
    public void loadData(boolean isClear, boolean isShowLoading) {
        if (isClear) {
            if (isShowLoading) {
                showLoading();
            }
        }
        refreshData();
    }

    private String request(int page, GsonHttpConnection.OnResultListener listener) {
        if (mOnRequestCallBack != null) {
            return mOnRequestCallBack.request(page, listener);
        }
        return "";
    }

    /**
     * 刷新数据，加载第一页
     */
    private void refreshData() {

        if (!TextUtils.isEmpty(mLastUrl)) {
            LiveApplication.mQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return TextUtils.equals(request.getUrl(), mLastUrl);
                }
            });
        }

        mCurrentPage = mDefaultPage;
        if (mRefreshListener == null) {

            mRefreshListener = new GsonHttpConnection.OnResultListener<ListMag>() {
                @Override
                public void onSuccess(ListMag listMag) {
                    onFirSuccess(listMag);
                }

                @Override
                public void onFail(int errorCode, String errorMsg, String command) {
                    ToastHelper.showToast(errorMsg);
                    setOnRefreshComplete();
                    onResult(null);
                    if (errorCode == Msg.NETWORK_ERROR_NETWORK_ERROR) {
                        if (mAdapter.isEmpty()) {
                            showNetWorkError();
                        }
                        onFinishLoading(false, false);
                    } else {
                        showDataExcaption();
                        mAdapter.clear();
                        onFinishLoading(false, false);
                    }
                }

            };
        }

        mLastUrl = request(mCurrentPage + 1, mRefreshListener);
    }

    private void getNextPage() {
        if (mGetNextListener == null) {

            mGetNextListener = new GsonHttpConnection.OnResultListener<ListMag>() {
                @Override
                public void onSuccess(ListMag listMag) {

                    boolean hasMore;
                    List list = listMag.getList();
                    if (list != null && list.isEmpty() == false) {
                        mAdapter.addAll(list);
                        mCurrentPage++;
                        hasMore = true;
                    } else {
                        hasMore = false;
                    }

                    onFinishLoading(hasMore, false);
                }

                @Override
                public void onFail(int errorCode, String errorMsg, String command) {
                    ToastHelper.showToast(errorMsg);

                    boolean hasMore = errorCode == Msg.NETWORK_ERROR_NETWORK_ERROR;
                    onFinishLoading(hasMore, false);
                }
            };

        }
        request(mCurrentPage + 1, mGetNextListener);
    }

    /**
     * 第一次加载数据成功处理
     *
     * @param listMag
     */
    protected void onFirSuccess(final ListMag listMag) {
        onResult(listMag);

        if (mRefreshDelay > 0) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    update(listMag);
                }
            }, mRefreshDelay);
        } else {
            update(listMag);
        }

    }


    private void update(ListMag listMag) {
        setOnRefreshComplete();

        if (listMag.isEmpty()) {

            if (isShowEmpty) {
                showEmpty();
            }

            onFinishLoading(false, false);
            return;
        }

        mAdapter.clear();
        showData();

        List list = listMag.getList();
        mAdapter.addAll(list);

        // 如果拿到的长度大于或等于设定的长度，就代表有下一页
        onFinishLoading((getLoadMoreCount() <= list.size()), false);

        mCurrentPage++;
    }

    public void setRefreshDelay(long delay) {
        mRefreshDelay = delay;
    }

    public void setEmptyTipText(CharSequence emptyTipText) {
        mEmptyTipText = emptyTipText;
    }

    public void showEmpty() {
        mErrorState = ErrorState.ERROR_STATE_EMPTY;
        showError();
        if (mErrorView != null) {
            mErrorTextView.setText(mEmptyTipText);
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mErrorTextView, null, mEmptyDrawable,
                    null, null);
            hideLoadMoreFoot(); // 隐藏掉加载更多布局
        }

    }


    public void showDataExcaption() {
        mErrorState = ErrorState.ERROR_STATE_DATA_EXCEPTION;
        showError();
        if (mErrorView != null) {
            mErrorTextView.setText(mDataExceptionTipText);
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mErrorTextView, null, mDataExceptionDrawable,
                    null, null);
            hideLoadMoreFoot();  // 隐藏掉加载更多布局
        }
    }

    public void showFooter(boolean showFooter) {
        mShowFooterView = showFooter;
    }

    public void showData() {
        mErrorState = null;
        setSwipeEnable(true);
        mDataView.setVisibility(VISIBLE);

        if (mErrorView != null) {
            mErrorView.setVisibility(GONE);
        }

        if (mLoadingView != null) {
            mLoadingView.setVisibility(GONE);
        }
    }

    public void showError() {
        setSwipeEnable(false);
        mDataView.setVisibility(GONE);

        if (mErrorView != null) {
            mErrorView.setVisibility(VISIBLE);
        }

        if (mLoadingView != null) {
            mLoadingView.setVisibility(GONE);
        }
    }

    public void showNetWorkError() {
        mErrorState = ErrorState.ERROR_STATE_NETWORK_ERROR;
        showError();
        if (mErrorView != null) {
            mErrorTextView.setText(mNetworkTipText);
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mErrorTextView, null, mNetworkDrawable,
                    null, null);
            hideLoadMoreFoot();  // 隐藏掉加载更多布局
        }
    }

    public void showLoading() {
        mErrorState = null;
        setSwipeEnable(false);
        mDataView.setVisibility(GONE);

        if (mErrorView != null) {
            mErrorView.setVisibility(GONE);
        }

        if (mLoadingView != null) {
            mLoadingView.setVisibility(VISIBLE);
        }
    }

    public void addErrorView(int id) {
        if (mErrorView != null) {
            mRootRelativeLayout.removeView(mErrorView);
            mErrorView = null;
        }
        mErrorView = LayoutInflater.from(getContext()).inflate(id, null);
        if (mErrorView != null) {
            mErrorTextView = (TextView) mErrorView.findViewById(R.id.tip);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            mRootRelativeLayout.addView(mErrorView, params);
            mErrorView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mErrorState == ErrorState.ERROR_STATE_EMPTY && isReloadWhenEmpty == false) {
                        return;
                    }
                    loadData();
                }
            });
        }
    }

    private void addLoadingView() {
        if (mLoadingView != null) {
            mRootRelativeLayout.removeView(mLoadingView);
            mLoadingView = null;
        }

        mLoadingView = new LoadLoadingView(mContext);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                Utils.dip2px(getContext(), 30),
                Utils.dip2px(getContext(), 30));
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mRootRelativeLayout.addView(mLoadingView, layoutParams);
    }

    public interface OnRequestCallBack {
        String request(int page, GsonHttpConnection.OnResultListener listener);
    }

    public interface OnResultListener<T> {
        void onResult(T t);
    }

    public void setOnResultListener(OnResultListener listener) {
        mOnResultListener = listener;
    }

    private void onResult(Object result) {
        if (mOnResultListener != null) {
            mOnResultListener.onResult(result);
        }
    }

    public boolean isEmpty() {
        return mAdapter == null || mAdapter.isEmpty();
    }

    public enum ErrorState {
        ERROR_STATE_EMPTY,
        ERROR_STATE_DATA_EXCEPTION,
        ERROR_STATE_NETWORK_ERROR
    }

    public void setIsReloadWhenEmpty(boolean isReload) {
        isReloadWhenEmpty = isReload;
    }

    @Override
    public void onViewRemoved(View child) {
        super.onViewRemoved(child);
        Log.e("test", "onViewRemoved");
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        Log.e("test", "removeView");
    }

    @Override
    public void removeViewInLayout(View view) {
        super.removeViewInLayout(view);
        Log.e("test", "removeViewInLayout");
    }

    @Override
    public void removeViewsInLayout(int start, int count) {
        super.removeViewsInLayout(start, count);
        Log.e("test", "removeViewsInLayout");
    }

    @Override
    public void removeViews(int start, int count) {
        super.removeViews(start, count);
        Log.e("test", "removeViews");
    }

    @Override
    public void removeViewAt(int index) {
        super.removeViewAt(index);
        Log.e("test", "removeViewAt");
    }

    @Override
    public void removeAllViews() {
        super.removeAllViews();
        Log.e("test", "removeAllViews");
    }

    @Override
    public void removeAllViewsInLayout() {
        super.removeAllViewsInLayout();
        Log.e("test", "removeAllViewsInLayout");
    }

    @Override
    protected void removeDetachedView(View child, boolean animate) {
        super.removeDetachedView(child, animate);
        Log.e("test", "removeDetachedView");
    }

    public void setLoadingColor(int color) {
    }

    public boolean isShowEmpty() {
        return isShowEmpty;
    }

    public void setShowEmpty(boolean showEmpty) {
        isShowEmpty = showEmpty;
    }

    public void addCurrentPage() {
        mCurrentPage++;
    }

    public int getmDefaultPage() {
        return mDefaultPage;
    }

    // 设置初始页
    public void setDefaultPage(int mDefaultPage) {
        this.mDefaultPage = (mDefaultPage - 1); // 请求的时候，会自增
    }

    public void setPaddingTop(int paddingTop) {
        getRecyclerView().setPadding(0, paddingTop, 0, 0);
        getRecyclerView().setClipToPadding(false);
    }
}
