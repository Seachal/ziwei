package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.HotTopic;
import com.laka.live.util.Common;

import java.util.List;

/**
 * @ClassName: HomeHotTopicsMsg
 * @Description: 首页热门话题
 * @Author: chuan
 * @Version: 1.0
 * @Date: 6/2/17
 */

public class HomeHotTopicsMsg extends ListMag<HotTopic> {
    @Expose
    @SerializedName(Common.DATA)
    private List<HotTopic> data;

    @Override
    public List<HotTopic> getList() {
        return data;
    }

    public void setData(List<HotTopic> data) {
        this.data = data;
    }
}
