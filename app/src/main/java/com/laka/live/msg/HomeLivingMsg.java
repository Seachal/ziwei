package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.Course;
import com.laka.live.util.Common;

import java.util.List;

/**
 * @ClassName: HomeLivingMsg
 * @Description: 首页正在直播
 * @Author: chuan
 * @Version: 1.0
 * @Date: 6/2/17
 */

public class HomeLivingMsg extends Msg {
    @Expose
    @SerializedName(Common.DATA)
    private List<Course> data;

    public List<Course> getData() {
        return data;
    }

    public void setData(List<Course> data) {
        this.data = data;
    }
}
