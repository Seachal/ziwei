package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.CourseIncomeSum;
import com.laka.live.util.Common;

/**
 * @ClassName: CourseIncomeSumMsg
 * @Description: 课程收入总计
 * @Author: chuan
 * @Version: 1.0
 * @Date: 01/08/2017
 */

public class CourseIncomeSumMsg extends Msg {
    @Expose
    @SerializedName(Common.DATA)
    private CourseIncomeSum data;

    public CourseIncomeSum getData() {
        return data;
    }

    public void setData(CourseIncomeSum data) {
        this.data = data;
    }
}
