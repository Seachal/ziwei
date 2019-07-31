package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.Course;
import com.laka.live.bean.ListUserInfo;
import com.laka.live.util.Common;

import java.util.List;

/**
 * Created by luwies on 16/3/24.
 */
public class SearchMsg extends ListMag<Course> {

    @Expose
    @SerializedName(Common.DATA)
    private List<Course> list;

    @Override
    public List<Course> getList() {
        return list;
    }
}
