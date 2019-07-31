package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.CourseCategoryOneBean;
import com.laka.live.bean.CourseCategoryTwoBean;

import java.util.List;

/**
 * Created by Lyf on 2017/9/18.
 */

public class CourseCategoryTwoMsg extends Msg {

    @Expose
    @SerializedName("data")
    private CourseCategoryTwoBean data;

    public CourseCategoryTwoBean getData() {
        return data;
    }

    public void setData(CourseCategoryTwoBean data) {
        this.data = data;
    }

}
