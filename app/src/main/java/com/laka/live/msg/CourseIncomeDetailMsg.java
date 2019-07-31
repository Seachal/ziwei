package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @ClassName:
 * @Description:
 * @Author: chuan
 * @Version: 1.0
 * @Date: 18/10/2017
 */

public class CourseIncomeDetailMsg extends Msg {
    @Expose
    @SerializedName("data")
    private CourseIncomeDetail data;

    public CourseIncomeDetail getData() {
        return data;
    }

    public void setData(CourseIncomeDetail data) {
        this.data = data;
    }
}
