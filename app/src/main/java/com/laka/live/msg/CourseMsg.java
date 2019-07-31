package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.Course;
import com.laka.live.util.Common;

import java.util.List;

/**
 * Created by zwl on 2016/6/29.
 * Email-1501448275@qq.com
 */
public class CourseMsg extends ListMag<Course> {

    @Expose
    @SerializedName(Common.DATA)
    private List<Course> data;

    @Override
    public List<Course> getList() {
        return data;
    }

    public void setData(List<Course> data) {
        this.data = data;
    }
}
