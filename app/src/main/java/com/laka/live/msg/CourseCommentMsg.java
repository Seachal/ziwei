package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.CourseComment;

/**
 * @ClassName:
 * @Description:
 * @Author: chuan
 * @Version: 1.0
 * @Date: 14/09/2017
 */

public class CourseCommentMsg extends Msg {
    @Expose
    @SerializedName("data")
    private CourseComment data;

    public CourseComment getData() {
        return data;
    }

    public void setData(CourseComment data) {
        this.data = data;
    }
}
