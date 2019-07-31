package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.PayCourseWrapper;

/**
 * @Author Lyf
 * @CreateTime 2018/1/6
 * @Description 请求支付接口
 **/
public class PayCourseMsg extends Msg {

    @Expose
    @SerializedName("data")
    private PayCourseWrapper data;

    public PayCourseWrapper getData() {
        return data;
    }

    public void setData(PayCourseWrapper data) {
        this.data = data;
    }

}
