package com.laka.live.bean;

/**
 * Created by luwies on 16/4/18.
 */
public class OrderPayInfoMsg {

    private int status;
    private String msg;
    private OrderPayInfoBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public OrderPayInfoBean getData() {
        return data;
    }

    public void setData(OrderPayInfoBean data) {
        this.data = data;
    }
}
