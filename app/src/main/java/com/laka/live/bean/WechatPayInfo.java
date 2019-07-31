package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;
import com.tencent.mm.sdk.modelpay.PayReq;

/**
 * Created by luwies on 16/4/18.
 */
public class WechatPayInfo {

    @Expose
    @SerializedName(Common.APPID)
    private String appId;

    @Expose
    @SerializedName(Common.PARTNERID)
    private String partnerId;

    @Expose
    @SerializedName(Common.PREPAYID)
    private String prepayId;

    @Expose
    @SerializedName(Common.NONCESTR)
    private String nonceStr;

    @Expose
    @SerializedName(Common.TIMESTAMP)
    private String timeStamp;

    @Expose
    @SerializedName(Common.PACKAGE_VALUE)
    private String packageValue;

    @Expose
    @SerializedName(Common.SIGN)
    private String sign;

    public static PayReq toPayReq(WechatPayInfo payInfo) {
        if (payInfo != null) {
            PayReq payReq = new PayReq();
            payReq.appId = payInfo.appId;
            payReq.partnerId = payInfo.partnerId;
            payReq.prepayId = payInfo.prepayId;
            payReq.packageValue = payInfo.packageValue;
            payReq.nonceStr = payInfo.nonceStr;
            payReq.timeStamp = payInfo.timeStamp;
            payReq.sign = payInfo.sign;
            return payReq;
        }
        return null;
    }
}
