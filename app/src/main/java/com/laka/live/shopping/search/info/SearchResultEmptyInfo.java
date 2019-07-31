package com.laka.live.shopping.search.info;


import com.laka.live.shopping.search.bean.KeywordSearchTopList;

import java.util.List;

/**
 * Created by gangqing on 2016/5/3.
 * Email:denggangqing@ta2she.com
 */
public class SearchResultEmptyInfo {
    public static final int view_type_hot_search = 0;
    public static final int view_type_random_article = 1;
    public int type;
    public String title;
    public List<String> hotSearch;
    public KeywordSearchTopList randomTopList;
}
