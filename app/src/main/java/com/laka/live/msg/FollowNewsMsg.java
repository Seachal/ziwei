package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.FollowNews;
import com.laka.live.util.Common;

import java.util.List;

/**
 * @ClassName: FollowNewsMsg
 * @Description: 关注动态
 * @Author: chuan
 * @Version: 1.0
 * @Date: 6/7/17
 */

public class FollowNewsMsg extends ListMag<FollowNews> {
    @Expose
    @SerializedName(Common.DATA)
    private List<FollowNews> data;

    @Override
    public List<FollowNews> getList() {
        return data;
    }

    public void steData(List<FollowNews> data) {
        this.data = data;
    }
}
