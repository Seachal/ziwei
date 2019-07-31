package com.laka.live.shopping.search.bean;

import java.util.List;

/**
 * Created by gangqing on 2016/4/27.
 * Email:denggangqing@ta2she.com
 */
public class KeywordSearchData {
    private List<KeywordSearchResult> searchResult;
    private KeywordSearchTopList toplist;

    private List<String> hotSearch;
    private List<KeywordSearchTopList> randomToplist;

    public List<KeywordSearchResult> getSearchResult() {
        return searchResult;
    }

    public KeywordSearchTopList getToplist() {
        return toplist;
    }

    public List<String> getHotSearch() {
        return hotSearch;
    }

    public List<KeywordSearchTopList> getRandomToplist() {
        return randomToplist;
    }
}
