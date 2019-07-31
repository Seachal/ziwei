package com.laka.live.shopping.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhxu on 2016/1/8.
 * Email:357599859@qq.com
 */
public class ShoppingOrderDetailBean implements Serializable {

    private String orderId;
    private String orderNo;
    private long createtime;
    private String startSeconds;
    private String orderPrice;
    private String postageFee;
    private String goodsPrice;
    private int payType;
    private int status;
    private String userRemark;
//    private int addrId;
//    private String detailAddr;
//    private String mobile;
//    private String receiver;
//    private String detailDistrict;
//    private int provinceId;
//    private int cityId;
//    private int districtId;
    private String surplusSeconds;
    private List<ShoppingOrderLogisticsBean> logistics;
    private List<ShoppingOrderDetailGoodsBean> goods;
    public String selectLogisticsId;
    private String usageCoinCount;
    private String usageCoinPrice;
    private String cirdleId;
    private ShoppingAddressBean address;
    public String getOrderId() {
        return orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public long getCreatetime() {
        return createtime;
    }

    public String getStartSeconds() {
        return startSeconds;
    }

    public String getTotalPrice() {
        return orderPrice;
    }

    public String getPostageFee() {
        return postageFee;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public int getPayType() {
        return payType;
    }

    public int getOrderStatus() {
        return status;
    }

    public String getUserRemark() {
        return userRemark;
    }

    public int getAddrId() {
        if(address!=null){
            return address.getAddressId();
        }
        return 0;

    }

    public String getDetailAddr() {
        if(address!=null){
            return address.getDetailAddr();
        }
        return "";
    }

    public String getMobile() {
        if(address!=null){
            return address.getMobile();
        }
        return "";
    }

    public String getReceiver() {
        if(address!=null){
            return address.getReceiver();
        }
        return "";
    }

    public String getDetailDistrict() {
        if(address!=null){
            return address.getDetailDistrict();
        }
        return "";
    }

    public int getProvinceId() {
        if(address!=null){
            return address.getProvinceId();
        }
        return 0;
    }

    public int getCityId() {
        if(address!=null){
            return address.getCityId();
        }
        return 0;
    }

    public int getDistrictId() {
        if(address!=null){
            return address.getDistrictId();
        }
        return 0;
    }

    public String getSurplusSeconds() {
        return surplusSeconds;
    }

    public List<ShoppingOrderLogisticsBean> getLogistics() {
        return logistics;
    }

    public List<ShoppingOrderDetailGoodsBean> getGoods() {
        return goods;
    }

    public String getUsageCoinCount() {
        return usageCoinCount;
    }

    public String getUsageCoinPrice() {
        return usageCoinPrice;
    }

    public String getCirdleId() {
        return cirdleId;
    }
}
