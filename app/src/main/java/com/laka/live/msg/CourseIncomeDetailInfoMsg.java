package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.IncomeCourseInfoInfo;

/**
 * @ClassName:
 * @Description:
 * @Author: chuan
 * @Version: 1.0
 * @Date: 18/10/2017
 */

public class CourseIncomeDetailInfoMsg extends Msg {
    @Expose
    @SerializedName("data")
    private IncomeCourseInfoInfo data;

    public IncomeCourseInfoInfo getData() {
        return data;
    }

    public void setData(IncomeCourseInfoInfo data) {
        this.data = data;
    }
}
