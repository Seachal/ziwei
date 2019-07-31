package com.laka.live.shopping.bean;

import java.io.Serializable;

/**
 * Created by zhxu on 2016/1/4.
 * Email:357599859@qq.com
 */
public class ShoppingAddressBean implements Serializable {

    private String detailAddr;
    private String mobile;
    private String receiver;
    private String detailDistrict;
    private int addressId;
    private int provinceId;
    private int cityId;
    private int districtId;

    public String getDetailAddr() {
        return detailAddr;
    }

    public String getMobile() {
        return mobile;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getDetailDistrict() {
        return detailDistrict;
    }

    public int getAddressId() {
        return addressId;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public int getCityId() {
        return cityId;
    }

    public int getDistrictId() {
        return districtId;
    }
}
