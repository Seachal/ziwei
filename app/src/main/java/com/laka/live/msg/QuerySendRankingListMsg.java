package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.ListUserInfo;
import com.laka.live.bean.RankingUserInfo;
import com.laka.live.util.Common;

import java.util.List;

/**
 * Created by luwies on 16/4/1.
 */
public class QuerySendRankingListMsg extends ListMag<RankingUserInfo> {

    @Expose
    @SerializedName(Common.DATA)
    private List<RankingUserInfo> list;

    @Override
    public List<RankingUserInfo> getList() {
        return list;
    }
}
