package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.ListUserInfo;
import com.laka.live.util.Common;
import com.laka.live.util.Log;

import java.util.List;

/**
 * Created by luwies on 16/3/23.
 */
public class FollowsListMsg extends ListMag<ListUserInfo> {

    @Expose
    @SerializedName(Common.FOLLOWS)
    private List<ListUserInfo> userInfoList;

    @Override
    public List<ListUserInfo> getList() {
        return userInfoList;
    }
}
