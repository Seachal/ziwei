package com.laka.live.shopping.bean.newversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @ClassName: ShoppingGoodsIncomeDetailsBean
 * @Description:
 * @Author: chuan
 * @Version: 1.0
 * @Date: 29/08/2017
 */

public class ShoppingGoodsIncomeDetailsBean {
    @Expose
    @SerializedName("details")
    private List<ShoppingGoodsIncomeDetail> details;

    @Expose
    @SerializedName("good")
    private ShoppingGoodsBaseBean good;

    public List<ShoppingGoodsIncomeDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ShoppingGoodsIncomeDetail> details) {
        this.details = details;
    }

    public ShoppingGoodsBaseBean getGood() {
        return good;
    }

    public void setGood(ShoppingGoodsBaseBean good) {
        this.good = good;
    }
}
