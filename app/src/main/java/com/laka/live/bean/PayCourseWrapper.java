package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.msg.Msg;

/**
 * @Author Lyf
 * @CreateTime 2018/1/6
 * @Description 请求支付接口
 **/
public class PayCourseWrapper extends Msg {

    @Expose
    @SerializedName("remain_coins")
    private String remainCoins;

    @Expose
    @SerializedName("usecoins")
    private int useCoins;

    @Expose
    @SerializedName("pay_type")
    private int payType;

    @Expose
    @SerializedName("pay_status")
    private int payStatus;

    @Expose
    @SerializedName("wechatpayParams")
    private OrderOrderPayInfo wechatPayParams;

    @Expose
    @SerializedName("alipayParamsStr")
    private String alipayParamsStr;

    public String getRemainCoins() {
        return remainCoins;
    }

    public void setRemainCoins(String remainCoins) {
        this.remainCoins = remainCoins;
    }

    public int getUseCoins() {
        return useCoins;
    }

    public void setUseCoins(int useCoins) {
        this.useCoins = useCoins;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public OrderOrderPayInfo getWechatPayParams() {
        return wechatPayParams;
    }

    public void setWechatPayParams(OrderOrderPayInfo wechatPayParams) {
        this.wechatPayParams = wechatPayParams;
    }

    public String getAlipayParamsStr() {
        return alipayParamsStr;
    }

    public void setAlipayParamsStr(String alipayParamsStr) {
        this.alipayParamsStr = alipayParamsStr;
    }
}
