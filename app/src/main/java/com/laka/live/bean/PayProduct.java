package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

import java.util.ArrayList;

/**
 * Created by luwies on 16/3/29.
 */
public class PayProduct {

    @Expose
    @SerializedName(Common.ALIPAY)
    private ArrayList<Product> aliPayList;

    @Expose
    @SerializedName(Common.WEIXIN_PAY)
    private ArrayList<Product> wechatPayList;

    public ArrayList<Product> getAliPayList() {
        return aliPayList;
    }

    public void setAliPayList(ArrayList<Product> aliPayList) {
        this.aliPayList = aliPayList;
    }

    public ArrayList<Product> getWechatPayList() {
        return wechatPayList;
    }

    public void setWechatPayList(ArrayList<Product> wechatPayList) {
        this.wechatPayList = wechatPayList;
    }
}
