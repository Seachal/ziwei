package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @ClassName:
 * @Description:
 * @Author: chuan
 * @Version: 1.0
 * @Date: 18/10/2017
 */

public class IncomeCourseInfo {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("buyer_id")
    private int buyerId;

    @Expose
    @SerializedName("buyer_name")
    private String buyerName;

    @Expose
    @SerializedName("buy_time")
    private long buyerTime;

    @Expose
    @SerializedName("income")
    private float income;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public long getBuyerTime() {
        return buyerTime;
    }

    public void setBuyerTime(long buyerTime) {
        this.buyerTime = buyerTime;
    }

    public float getIncome() {
        return income;
    }

    public void setIncome(float income) {
        this.income = income;
    }


    @Override
    public String toString() {
        return "IncomeCourseInfo{" +
                "id=" + id +
                ", buyerId=" + buyerId +
                ", buyerName='" + buyerName + '\'' +
                ", buyerTime=" + buyerTime +
                ", income='" + income + '\'' +
                '}';
    }
}
