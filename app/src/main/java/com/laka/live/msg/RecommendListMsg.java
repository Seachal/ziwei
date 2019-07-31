package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.ListUserInfo;
import com.laka.live.util.Common;

import java.util.List;

/**
 * Created by luwies on 16/3/23.
 */
public class RecommendListMsg extends ListMag<ListUserInfo> {

    @Expose
    @SerializedName(Common.DATA)
    private List<ListUserInfo> list;

    @Override
    public List<ListUserInfo> getList() {
        return list;
    }
}
