package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.RankingUserInfo;
import com.laka.live.ui.rankinglist.RankingItemView;
import com.laka.live.ui.rankinglist.RankingListConstant;
import com.laka.live.ui.rankinglist.RankingUserInfoParams;
import com.laka.live.util.Common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwl on 2016/6/28.
 * Email-1501448275@qq.com
 */
public class QueryRankingListMsg extends ListMag<RankingUserInfoParams> {

    @Expose
    @SerializedName(Common.DATA)
    private List<RankingUserInfo> list;

    private List<RankingUserInfoParams> rankingDataList;

    @Override
    public List<RankingUserInfoParams> getList() {
        return rankingDataList;
    }

    @Override
    public void parase() {
        super.parase();

        if (list == null || list.isEmpty()) {
            rankingDataList = new ArrayList<>(list.size());
            RankingUserInfoParams params = new RankingUserInfoParams();
            params.isEmpty = true;
            rankingDataList.add(params);
            return;
        }
        rankingDataList = new ArrayList<>(list.size());
        int index = 0;
        for (RankingUserInfo rankingUserInfo : list) {
            RankingUserInfoParams params = new RankingUserInfoParams();
            params.index = ++index;
            params.userInfo = rankingUserInfo;
            rankingDataList.add(params);
        }
    }

    public List<RankingUserInfoParams> getRankingDataList() {
        return rankingDataList;
    }
}
