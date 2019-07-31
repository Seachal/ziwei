package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.CourseDetail;
import com.laka.live.util.Common;

/**
 * Created by Lyf on 2017/4/7.
 */
public class CourseDetailMsg extends Msg {

    @Expose
    @SerializedName(Common.DATA)
    public CourseDetail data; // 课程预告

}
