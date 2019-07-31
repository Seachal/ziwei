package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.CourseReply;

/**
 * @ClassName:
 * @Description:
 * @Author: chuan
 * @Version: 1.0
 * @Date: 14/09/2017
 */

public class CourseReplyMsg extends Msg {
    @Expose
    @SerializedName("data")
    private CourseReply data;

    public CourseReply getData() {
        return data;
    }

    public void setData(CourseReply data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CourseReplyMsg{" +
                "data=" + data +
                '}';
    }
}
