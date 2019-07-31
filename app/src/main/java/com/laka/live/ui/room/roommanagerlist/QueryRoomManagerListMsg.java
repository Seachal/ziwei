package com.laka.live.ui.room.roommanagerlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.UserInfo;
import com.laka.live.msg.ListMag;
import com.laka.live.util.Common;

import java.util.List;

/**
 * Created by zwl on 2016/6/15.
 * Email-1501448275@qq.com
 */
public class QueryRoomManagerListMsg extends ListMag<UserInfo> {
    @Expose
    @SerializedName(Common.DATA)
    private List<UserInfo> data;

    @Override
    public List<UserInfo> getList() {
        return data;
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }
}
