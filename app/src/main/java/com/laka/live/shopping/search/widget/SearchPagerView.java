package com.laka.live.shopping.search.widget;

import android.content.Context;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;


import com.laka.live.R;
import com.laka.live.application.LiveApplication;
import com.laka.live.shopping.search.adapter.SearchResultRecyclerViewAdapter;
import com.laka.live.shopping.search.info.SearchConstant;
import com.laka.live.shopping.search.info.SearchResultEmptyInfo;
import com.laka.live.shopping.search.info.SearchResultInfo;
import com.laka.live.shopping.xrecyclerview.XRecyclerView;
import com.laka.live.ui.widget.LoadingLayout;
import com.laka.live.util.NetworkUtil;
import com.laka.live.util.ResourceHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gangqing on 2016/4/22.
 * Email:denggangqing@ta2she.com
 */
public class SearchPagerView extends FrameLayout {
    private static final int STATE_LOADING = 0; // 加载中的状态
    private static final int STATE_EMPTY = 1;    // 空的状态
    private static final int STATE_ERROR = 2;   // 错误的状态
    private static final int STATE_SEARCH_RESULT = 3; // 成功的状态
    private static final int STATE_SEARCH = 4;  //输入搜索界面

    private int mCurrentState = STATE_LOADING;

    private LoadingLayout mLoadingView;  // 正在加载中的View
    private SearchEmptyView mEmptyView;    // 数据为空的View
    private LoadingLayout mErrorView;    // 错误页面的View
    private XRecyclerView mSearchResultView;  //搜索成功的view
    private SearchView mSearchView;   //输入搜索界面

    private LoadingCallBack mLoadingCallBack;
    private SearchResultRecyclerViewAdapter mSearchResultAdapter;
    private List<SearchResultInfo> mSearchResultList = new ArrayList<>();

    private int mCurrentRequestType = SearchConstant.REQUEST_SEARCH_GUIDE;

    public SearchPagerView(Context context) {
        super(context);
        initView();
    }

    public SearchPagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        setBackgroundColor(ResourceHelper.getColor(R.color.classify_content_bg));
        //请求界面
        if (mLoadingView == null) {
            mLoadingView = new LoadingLayout(getContext());
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            mLoadingView.setBgContent(R.anim.loading, ResourceHelper.getString(R.string.homepage_loading), false);
            addView(mLoadingView, params);
        }
        //搜索->空界面
        if (mEmptyView == null) {
            mEmptyView = new SearchEmptyView(getContext());
            mEmptyView.setClassifyEmptyHotGoodsCallBack(new SearchEmptyView.ClassifyEmptyHotGoodsCallBack() {
                @Override
                public void onHotGoodsItemClick(View v) {
                    if (mLoadingCallBack != null) {
                        mLoadingCallBack.onHotGoodsItemClick(v);
                    }
                }
            });
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            addView(mEmptyView, params);
        }
        //请求失败界面
        if (mErrorView == null) {
            mErrorView = new LoadingLayout(getContext());
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            mErrorView.setBgContent(R.drawable.no_network, ResourceHelper.getString(R.string.homepage_network_error_retry), true);
            mErrorView.setBtnOnClickListener(new LoadingLayout.OnBtnClickListener() {
                @Override
                public void onClick() {
                    loadData(mCurrentRequestType);
                }
            });
            addView(mErrorView, params);
        }
        //输入搜索界面
        if (mSearchView == null) {
            mSearchView = new SearchView(getContext());
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            mSearchView.setSearchViewCallBack(new SearchView.SearchViewCallBack() {
                @Override
                public void onSearch(String content) {
                    if (mLoadingCallBack != null) {
                        mLoadingCallBack.onInputSearch(content);
                    }
                }
            });
            addView(mSearchView, params);
        }

        //搜索成功界面
        if (mSearchResultView == null) {
            mSearchResultView = new XRecyclerView(getContext());
            mSearchResultAdapter = new SearchResultRecyclerViewAdapter(getContext(), mSearchResultList);
            mSearchResultView.setAdapter(mSearchResultAdapter);
            mSearchResultView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            int dimen = ResourceHelper.getDimen(R.dimen.space_5);
            mSearchResultView.addItemDecoration(new SearchResultItemDecoration(dimen));
            mSearchResultView.setLoadingListener(new XRecyclerView.LoadingListener() {
                @Override
                public void onRefresh() {
                    if (mLoadingCallBack != null) {
                        mLoadingCallBack.onSearchResultRefresh();
                    }
                }

                @Override
                public void onLoadMore() {
                    if (mLoadingCallBack != null) {
                        mLoadingCallBack.onSearchResultLoadMore();
                    }
                }
            });
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            params.setMargins(dimen, 0, dimen, ResourceHelper.getDimen(R.dimen.space_15));
            addView(mSearchResultView, params);
        }

        updateUIStyle();
    }

    private void updateUIStyle() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(mCurrentState == STATE_LOADING ? View.VISIBLE : View.GONE);
        }
        if (mEmptyView != null) {
            mEmptyView.setVisibility(mCurrentState == STATE_EMPTY ? View.VISIBLE : View.GONE);
        }
        if (mErrorView != null) {
            mErrorView.setVisibility(mCurrentState == STATE_ERROR ? View.VISIBLE : View.GONE);
        }
        if (mSearchView != null) {
            mSearchView.setVisibility(mCurrentState == STATE_SEARCH ? View.VISIBLE : View.GONE);
        }
        if (mSearchResultView != null) {
            mSearchResultView.setVisibility(mCurrentState == STATE_SEARCH_RESULT ? View.VISIBLE : View.GONE);
        }
    }

    public void loadData(int requestType) {
        mCurrentRequestType = requestType;
        mCurrentState = STATE_LOADING;
        if (mLoadingCallBack != null) {
            if (NetworkUtil.isNetworkOk(LiveApplication.getInstance())) {
                mLoadingCallBack.onStartLoadData(requestType);
            } else {
                mCurrentState = STATE_ERROR;
            }
        }
        updateUIStyle();
    }

    public void setState(LoadedResult result) {
        mCurrentState = result.getState();
        if (mCurrentState == STATE_EMPTY) {
//            StatsModel.stats(StatsKeyDef.SEARCH_RESULT_FAIL);
        }
        updateUIStyle();
    }

    //---------- 设置搜索界面的数据 ----------//
    public void setHotData(List<String> list) {
        if (mSearchView != null) {
            mSearchView.addHotSearchData(list);
        }
    }

    public void addSearchRecordData(String content) {
        if (mSearchView != null) {
            mSearchView.addHistoryInfo(content);
        }
    }

    //---------- 设置搜索结果数据 -----------//

    public void setSearchResultList(List<SearchResultInfo> searchResultList, boolean isLoadMore, boolean hasMore) {
        if (!isLoadMore) {
            mSearchResultList.clear();
            mSearchResultView.refreshComplete();
        } else {
            mSearchResultView.loadMoreComplete();
        }
        mSearchResultView.hasMore(hasMore);
        mSearchResultList.addAll(searchResultList);
        mSearchResultAdapter.notifyDataSetChanged();
    }

    public void setLoadMoreError() {
        mSearchResultView.loadMoreComplete();
    }

    //---------- 设置搜索结果为空时的数据 ----------//
    public void setEmptyData(List<SearchResultEmptyInfo> data) {
        if (mEmptyView != null) {
            mEmptyView.setEmptyData(data);
        }
    }

    //--------- 初始化 --------//

    public void init() {
        if (mSearchView != null) {
            mSearchView.tryLoadHistoryData();
        }
    }

    public enum LoadedResult {
        LOADING(STATE_LOADING), EMPTY(STATE_EMPTY), ERROR(STATE_ERROR), SEARCH_RESULT(STATE_SEARCH_RESULT), SEARCH(STATE_SEARCH);

        int state;

        private LoadedResult(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }
    }

    public void setLoadingCallBack(LoadingCallBack callBack) {
        mLoadingCallBack = callBack;
    }

    public interface LoadingCallBack {
        void onStartLoadData(int requestType);

        void onHotGoodsItemClick(View v);

        void onInputSearch(String content);

        void onSearchResultRefresh();

        void onSearchResultLoadMore();
    }
}
