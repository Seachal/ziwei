package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.Banner;
import com.laka.live.bean.Course;
import com.laka.live.bean.Room;
import com.laka.live.util.Common;

import java.util.List;

/**
 * Created by luwies on 16/3/29.
 */
public class LiveListMsg extends ListMag<Course> {


    @Expose
    @SerializedName(Common.DATA)
    private List<Course> data;

    @Override
    public List<Course> getList() {
        return data;
    }

    public void setList(List<Course> data) {
        this.data = data;
    }


    @Override
    public boolean isEmpty() {
        return (data == null || data.isEmpty());
    }
}
