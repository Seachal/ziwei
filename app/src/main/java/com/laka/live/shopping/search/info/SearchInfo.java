package com.laka.live.shopping.search.info;


import com.laka.live.shopping.search.model.SearchHistoryInfo;

import java.util.List;

/**
 * Created by gangqing on 2016/4/29.
 * Email:denggangqing@ta2she.com
 */
public class SearchInfo {
    public static final int VIEW_TYPE_HOT = 0;
    public static final int VIEW_TYPE_HISTORY = 1;
    public static final int VIEW_TYPE_DELETE = 2;
    public int viewType;
    public SearchHistoryInfo historyInfo;
    public List<String> HotSearchList;
}
