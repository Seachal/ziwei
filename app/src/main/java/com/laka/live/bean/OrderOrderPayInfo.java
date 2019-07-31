package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tencent.mm.sdk.modelpay.PayReq;

/**
 * Created by luwies on 16/4/18.
 */
public class OrderOrderPayInfo {

    @Expose
    @SerializedName("appId")
    private String appId;

    @Expose
    @SerializedName("partnerId")
    private String partnerId;

    @Expose
    @SerializedName("prepayId")
    private String prepayId;

    @Expose
    @SerializedName("nonceStr")
    private String nonceStr;

    @Expose
    @SerializedName("timestamp")
    private String timestamp;

    @Expose
    @SerializedName("packageStr")
    private String packageStr;

    @Expose
    @SerializedName("sign")
    private String sign;

    public static PayReq toPayReq(OrderOrderPayInfo payInfo) {
        if (payInfo != null) {
            PayReq payReq = new PayReq();
            payReq.appId = payInfo.appId;
            payReq.partnerId = payInfo.partnerId;
            payReq.prepayId = payInfo.prepayId;
            payReq.packageValue = payInfo.packageStr;
            payReq.nonceStr = payInfo.nonceStr;
            payReq.timeStamp = payInfo.timestamp;
            payReq.sign = payInfo.sign;
            return payReq;
        }
        return null;
    }
}
