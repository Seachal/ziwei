package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

import java.util.List;

/**
 * Created by luwies on 16/3/26.
 */
public class SearchHotKeywordMsg extends ListMag<String> {

    @Expose
    @SerializedName(Common.HOT_KEYWORDS)
    private List<String> list;

    @Override
    public List<String> getList() {
        return list;
    }
}
