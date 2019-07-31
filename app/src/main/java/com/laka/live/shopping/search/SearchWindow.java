package com.laka.live.shopping.search;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.laka.live.R;
import com.laka.live.shopping.framework.AbstractWindow;
import com.laka.live.shopping.framework.DeviceManager;
import com.laka.live.shopping.framework.UICallBacks;
import com.laka.live.shopping.search.bean.HotSearchBean;
import com.laka.live.shopping.search.bean.KeywordSearchBean;
import com.laka.live.shopping.search.bean.KeywordSearchData;
import com.laka.live.shopping.search.bean.KeywordSearchResult;
import com.laka.live.shopping.search.bean.KeywordSearchTopList;
import com.laka.live.shopping.search.info.SearchConstant;
import com.laka.live.shopping.search.info.SearchResultEmptyInfo;
import com.laka.live.shopping.search.info.SearchResultInfo;
import com.laka.live.shopping.search.request.SearchRequest;
import com.laka.live.shopping.search.widget.SearchClassifyTitleView;
import com.laka.live.shopping.search.widget.SearchInputView;
import com.laka.live.shopping.search.widget.SearchPagerView;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ToastHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gangqing on 2016/4/22.
 * Email:denggangqing@ta2she.com
 */
public class SearchWindow extends AbstractWindow implements SearchInputView.InputViewListener, SearchClassifyTitleView.ClickCallBack, SearchPagerView.LoadingCallBack, SearchRequest.SearchRequestCallBack {
    private SearchInputView mSearchInput;
    private SearchClassifyTitleView mClassifyTitle;
    private SearchPagerView mSearchPagerView;
    private SearchRequest mRequest;
    private String mCurrentSearchContent;
    private int mCurrentSearchType = SearchConstant.TYPE_COMPREHENSIVE;

    public SearchWindow(Context context, UICallBacks callBacks) {
        super(context, callBacks);
        mRequest = new SearchRequest();
        mRequest.setCallBack(this);
        initWindow();
    }

    private void initWindow() {
        View rootView = View.inflate(getContext(), R.layout.search_classify_layout, null);
        mSearchInput = (SearchInputView) rootView.findViewById(R.id.search_input);
        mClassifyTitle = (SearchClassifyTitleView) rootView.findViewById(R.id.classify_title);
        mSearchPagerView = (SearchPagerView) rootView.findViewById(R.id.classify_loading);
        getBaseLayer().addView(rootView, getBaseLayerLP());

        mSearchInput.setSearchInputListener(this);
        mClassifyTitle.setClickCallBack(this);
        mSearchPagerView.setLoadingCallBack(this);
    }

    @Override
    protected void onWindowStateChange(int stateFlag) {
        super.onWindowStateChange(stateFlag);
        if (stateFlag == STATE_AFTER_PUSH_IN) {
            mSearchPagerView.init();
            mSearchPagerView.loadData(SearchConstant.REQUEST_SEARCH_GUIDE);
            DeviceManager.getInstance().showInputMethod();
        } else if (stateFlag == STATE_AFTER_POP_OUT || stateFlag == STATE_ON_HIDE) {
            DeviceManager.getInstance().hideInputMethod();
        }
    }

    @Override
    public void onSearchClick(String content, boolean isClickSearchIco) {
        if (StringUtils.isEmpty(content) && isClickSearchIco) {
            ToastHelper.showToast(  R.string.search_input_hint, Toast.LENGTH_SHORT);
            return;
        }
        if (isClickSearchIco) {
//            StatsModel.stats(StatsKeyDef.SEARCH_CONFIRM, StatsKeyDef.KEY_SEARCH_HOT, content);
        }
        searchRequest(content, isClickSearchIco);
    }

    private void searchRequest(String content, boolean needHideInputMethod) {
        if (needHideInputMethod) {
            DeviceManager.getInstance().hideInputMethod();
        }
        mCurrentSearchContent = content;
        mSearchPagerView.addSearchRecordData(content);
        mSearchInput.setEditText(mCurrentSearchContent);
        setSearchResultStyle();
        mSearchPagerView.loadData(SearchConstant.REQUEST_SEARCH_RESULT);
    }

    private void setSearchResultStyle() {
        if (!mClassifyTitle.isShown()) {
            mClassifyTitle.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onBackClick() {
        mCallBacks.onWindowExitEvent(this, true);
    }

    @Override
    public void onDeleteClick() {
    }

    @Override
    public void onClick(int type, String title) {
        mCurrentSearchType = type;
        //统计
        stats();
        mSearchPagerView.loadData(SearchConstant.REQUEST_SEARCH_RESULT);
    }

    private void stats() {
        String tab = null;
        //	1综合；2测评；3销量；4最新；5价格降序；6价格升序；
        switch (mCurrentSearchType) {
            case SearchConstant.TYPE_COMPREHENSIVE:
                tab = "综合";
                break;
//            case SearchConstant.TYPE_EVALUATE:
//                tab = "测评";
//                break;
            case SearchConstant.TYPE_SALES:
                tab = "销量";
                break;
            case SearchConstant.TYPE_NEW:
                tab = "最新";
                break;
            case SearchConstant.TYPE_DOWN_PRICE:
                tab = "价格降序";
                break;
            case SearchConstant.TYPE_UP_PRICE:
                tab = "价格升序";
                break;
        }
//        StatsModel.stats(StatsKeyDef.SEARCH_TAB, StatsKeyDef.KEY_SEARCH_HOT, tab);
    }

    @Override
    public void onStartLoadData(int requestType) {
        switch (requestType) {
            case SearchConstant.REQUEST_SEARCH_GUIDE:
                mRequest.requestDaySearch(SearchConstant.HOME_HOT_SEARCH_DEFAULT);
                break;
            case SearchConstant.REQUEST_SEARCH_RESULT:
                mRequest.requestSearch(SearchConstant.REQUEST_SEARCH_RESULT, mCurrentSearchContent, mCurrentSearchType, false);
                break;
        }
    }

    @Override
    public void onHotGoodsItemClick(View v) {
        searchRequest(((TextView) v).getText().toString(), true);
    }

    @Override
    public void onInputSearch(String content) {
        searchRequest(content, true);
    }

    @Override
    public void onSearchResultRefresh() {
        mRequest.requestSearch(SearchConstant.REQUEST_SEARCH_RESULT, mCurrentSearchContent, mCurrentSearchType, false);
    }

    @Override
    public void onSearchResultLoadMore() {
        mRequest.requestSearch(SearchConstant.REQUEST_SEARCH_RESULT, mCurrentSearchContent, mCurrentSearchType, true);
    }

    @Override
    public void onKeywordSearchSuccess(int type, KeywordSearchBean bean, boolean isLoadMore, boolean hasMore) {
        switch (type) {
            case SearchConstant.REQUEST_SEARCH_GUIDE:
                mSearchPagerView.setState(SearchPagerView.LoadedResult.SEARCH);
                break;
            case SearchConstant.REQUEST_SEARCH_RESULT:
                if (isLoadMore) {
                    mSearchPagerView.setSearchResultList(createSearchResultInfoList(bean), isLoadMore,hasMore);
                } else if (bean.getData().getSearchResult() == null || bean.getData().getSearchResult().size() == 0) {
                    mSearchPagerView.setState(SearchPagerView.LoadedResult.EMPTY);
                    mSearchPagerView.setEmptyData(createSearchResultEmptyInfoList(bean.getData()));
                } else {
                    mSearchPagerView.setState(SearchPagerView.LoadedResult.SEARCH_RESULT);
                    mSearchPagerView.setSearchResultList(createSearchResultInfoList(bean), isLoadMore,hasMore);
                }
                break;
        }
    }

    private List<SearchResultEmptyInfo> createSearchResultEmptyInfoList(KeywordSearchData bean) {
        List<SearchResultEmptyInfo> emptyInfoList = new ArrayList<>();
        SearchResultEmptyInfo info = new SearchResultEmptyInfo();
        info.hotSearch = bean.getHotSearch();
        info.type = SearchResultEmptyInfo.view_type_hot_search;
        info.title = mCurrentSearchContent;
        emptyInfoList.add(info);

        List<KeywordSearchTopList> randomToplist = bean.getRandomToplist();
        if (randomToplist != null) {
            for (int i = 0; i < randomToplist.size(); i++) {
                SearchResultEmptyInfo itemInfo = new SearchResultEmptyInfo();
                itemInfo.randomTopList = randomToplist.get(i);
                itemInfo.type = SearchResultEmptyInfo.view_type_random_article;
                emptyInfoList.add(itemInfo);
            }
        }
        return emptyInfoList;
    }

    private List<SearchResultInfo> createSearchResultInfoList(KeywordSearchBean bean) {
        List<SearchResultInfo> searchResultList = new ArrayList<>();
        SearchResultInfo info = new SearchResultInfo();
        KeywordSearchTopList toplist = bean.getData().getToplist();
        if (toplist != null && toplist.toplistId != null) {
            info.setToplist(toplist);
            info.setViewType(SearchResultInfo.TYPE_MATCH_SECONDARY_CLASSIFY);
            info.setSecondaryClassifyType(mCurrentSearchType);
            searchResultList.add(info);
        }
        List<KeywordSearchResult> searchResult = bean.getData().getSearchResult();
        if (searchResult != null) {
            for (int i = 0; i < searchResult.size(); i++) {
                SearchResultInfo itemInfo = new SearchResultInfo();
                itemInfo.setSearchResultItem(searchResult.get(i));
                itemInfo.setViewType(SearchResultInfo.TYPE_DEFAULT);
                itemInfo.setSecondaryClassifyType(mCurrentSearchType);
                searchResultList.add(itemInfo);
            }
        }
        return searchResultList;
    }

    @Override
    public void onHotSearchSuccess(HotSearchBean bean) {
        mSearchPagerView.setState(SearchPagerView.LoadedResult.SEARCH);
        mSearchPagerView.setHotData(bean.getData().getHotSearch());
    }

    @Override
    public void onError(int type, boolean isLoadMore) {
        switch (type) {
            case SearchConstant.REQUEST_SEARCH_RESULT:
                if (isLoadMore) {
                    mSearchPagerView.setLoadMoreError();
                } else {
                    mSearchPagerView.setState(SearchPagerView.LoadedResult.ERROR);
                }
                break;
            default:
                mSearchPagerView.setState(SearchPagerView.LoadedResult.ERROR);
                break;
        }
    }
}
