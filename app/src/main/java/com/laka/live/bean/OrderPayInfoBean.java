package com.laka.live.bean;

/**
 * Created by luwies on 16/4/18.
 */
public class OrderPayInfoBean {

    private OrderOrderPayInfo payParams;
    private String payParamsStr;
    private String orderId;
    private String orderNo;

    public OrderOrderPayInfo getPayParams() {
        return payParams;
    }

    public void setPayParams(OrderOrderPayInfo payParams) {
        this.payParams = payParams;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAliPayParams() {
        return payParamsStr;
    }

}
