package com.laka.live.shopping.search.info;


import com.laka.live.shopping.search.bean.KeywordSearchResult;
import com.laka.live.shopping.search.bean.KeywordSearchTopList;

/**
 * Created by gangqing on 2016/4/28.
 * Email:denggangqing@ta2she.com
 */
public class SearchResultInfo {
    public static final int TYPE_MATCH_SECONDARY_CLASSIFY = 0;
    public static final int TYPE_DEFAULT = 1;
    private int viewType = TYPE_DEFAULT;
    private int secondaryClassifyType = SearchConstant.TYPE_COMPREHENSIVE;
    private KeywordSearchResult searchResultItem;
    private KeywordSearchTopList toplist;

    public void setViewType(int type) {
        this.viewType = type;
    }

    public int getViewType() {
        return viewType;
    }

    public void setSecondaryClassifyType(int type) {
        this.secondaryClassifyType = type;
    }

    public int getSecondaryClassifyType() {
        return secondaryClassifyType;
    }

    public void setSearchResultItem(KeywordSearchResult searchResultItem) {
        this.searchResultItem = searchResultItem;
    }

    public KeywordSearchResult getSearchResultItem() {
        return searchResultItem;
    }

    public void setToplist(KeywordSearchTopList toplist) {
        this.toplist = toplist;
    }

    public KeywordSearchTopList getToplist() {
        return toplist;
    }
}
