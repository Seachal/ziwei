package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.ListUserInfo;
import com.laka.live.util.Common;

import java.util.List;

/**
 * Created by luwies on 16/3/22.
 */
public class FansListMsg extends ListMag {

    @Expose
    @SerializedName(Common.FANS)
    private List<ListUserInfo> mFansList;

    @Override
    public List getList() {
        return mFansList;
    }
}
