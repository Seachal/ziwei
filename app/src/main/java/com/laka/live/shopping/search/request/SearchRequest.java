package com.laka.live.shopping.search.request;


import com.laka.live.shopping.network.HttpManager;
import com.laka.live.shopping.network.HttpMethod;
import com.laka.live.shopping.network.HttpUrls;
import com.laka.live.shopping.network.IHttpCallBack;
import com.laka.live.shopping.network.IHttpManager;
import com.laka.live.shopping.search.bean.HotSearchBean;
import com.laka.live.shopping.search.bean.KeywordSearchBean;
import com.laka.live.shopping.search.bean.UserSearchJsonBean;
import com.laka.live.shopping.search.info.SearchConstant;
import com.laka.live.util.StringUtils;

/**
 * Created by gangqing on 2016/4/25.
 * Email:denggangqing@ta2she.com
 */
public class SearchRequest {
    public SearchRequestCallBack mCallBack;
    private static final String PAGE_SIZE = "10";
    private int mPage = 1;

    /**
     * 关键字搜索
     */
    public void requestSearch(final int requestPager, String keyword, int sortType, final boolean isLoadMore) {
        if (isLoadMore) {
            mPage++;
        } else {
            mPage = 1;
        }
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("a", "homeSearch");
        httpManager.addParams("keyword", keyword);
        httpManager.addParams("sortType", String.valueOf(sortType));
        httpManager.addParams("page", String.valueOf(mPage));
        httpManager.addParams("pageSize", PAGE_SIZE);
        httpManager.request(HttpUrls.SHOPPING_HOME_URL, HttpMethod.POST, KeywordSearchBean.class, new IHttpCallBack() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                if (obj instanceof KeywordSearchBean && mCallBack != null) {
                    KeywordSearchBean data = (KeywordSearchBean) obj;
                    boolean hasMore = false;
                    if (data != null && data.getData() != null && data.getData().getSearchResult() != null && data.getData().getSearchResult().size() == StringUtils.parseInt(PAGE_SIZE)) {
                        hasMore = true;
                    }
                    mCallBack.onKeywordSearchSuccess(requestPager, (KeywordSearchBean) obj, isLoadMore, hasMore);
                }
            }

            @Override
            public void onError(String errorStr, int code) {
                if (mCallBack != null) {
                    mCallBack.onError(requestPager, isLoadMore);
                }
            }
        });
    }

    /**
     * 热门搜索
     */
    public void requestDaySearch(int type) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("a", "homeHotSearch");
        httpManager.addParams("type", String.valueOf(type));
        httpManager.request(HttpUrls.SHOPPING_HOME_URL, HttpMethod.POST, HotSearchBean.class, new IHttpCallBack() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                if (obj instanceof HotSearchBean && mCallBack != null) {
                    mCallBack.onHotSearchSuccess((HotSearchBean) obj);
                }
            }

            @Override
            public void onError(String errorStr, int code) {
                if (mCallBack != null) {
                    mCallBack.onError(SearchConstant.REQUEST_SEARCH_GUIDE, false);
                }
            }
        });
    }

    public void requestUserSearch(String searchContent , String page , IHttpCallBack callBack){
        if (callBack == null){
            return;
        }

        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("a" , "searchUser");
        httpManager.addParams("searchContent" ,searchContent);
        httpManager.addParams("page",page);

        httpManager.request(HttpUrls.LETTER_URL , HttpMethod.POST , UserSearchJsonBean.class , callBack);
    }

    public void setCallBack(SearchRequestCallBack callBack) {
        mCallBack = callBack;
    }

    public interface SearchRequestCallBack<T> {
        void onKeywordSearchSuccess(int type, KeywordSearchBean bean, boolean isLoadMore, boolean hasMore);

        void onHotSearchSuccess(HotSearchBean bean);

        void onError(int type, boolean isLoadMore);
    }
}
