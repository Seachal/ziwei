package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.BestTopicBean;
import com.laka.live.bean.Course;
import com.laka.live.util.Log;

import java.util.List;

/**
 * Created by Lyf on 2017/9/19.
 */
public class BestTopicMsg extends ListMag {

    @Expose
    @SerializedName("data")
    private BestTopicBean data;

    @Override
    public List<Course> getList() {

        if(data == null){
            return null;
        }
        return data.getList();
    }

    public BestTopicBean getData() {
        return data;
    }
}
