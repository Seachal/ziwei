package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.PayCourse;
import com.laka.live.util.Common;

/**
 * Created by Lyf on 2017/4/12.
 */

public class QueryPayCourseMsg extends Msg {

    @Expose
    @SerializedName(Common.DATA)
    private PayCourse data; // 课程预告

    public PayCourse getData() {
        return data;
    }

    public void setData(PayCourse data) {
        this.data = data;
    }
}
