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

public class IncomeCourseInfoInfo extends IncomeCourseInfo {
    public static final int LIVE_INCOME = 1, VIDEO_INCOME = 2, AGENT_INCOME = 3;

    @Expose
    @SerializedName("buyer_paid")
    private String buyerPaid;

    @Expose
    @SerializedName("income_type")
    private int incomeType;

    @Expose
    @SerializedName("anchor_id")
    private int anchorId;

    @Expose
    @SerializedName("anchor_name")
    private String anchorName;

    @Expose
    @SerializedName("anchor_income")
    private float anchorIncome;

    public String getBuyerPaid() {
        return buyerPaid;
    }

    public void setBuyerPaid(String buyerPaid) {
        this.buyerPaid = buyerPaid;
    }

    public int getIncomeType() {
        return incomeType;
    }

    public String getIncomeTypeStr() {
        switch (incomeType) {
            case LIVE_INCOME:
                return "直播收入";
            case VIDEO_INCOME:
                return "视频收入";
            case AGENT_INCOME:
                return "代理收入";
            default:
                return "";
        }
    }

    public void setIncomeType(int incomeType) {
        this.incomeType = incomeType;
    }

    public int getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(int anchorId) {
        this.anchorId = anchorId;
    }

    public String getAnchorName() {
        return anchorName;
    }

    public void setAnchorName(String anchorName) {
        this.anchorName = anchorName;
    }

    public float getAnchorIncome() {
        return anchorIncome;
    }

    public void setAnchorIncome(float anchorIncome) {
        this.anchorIncome = anchorIncome;
    }

    @Override
    public String toString() {
        return "IncomeCourseInfoInfo{" +
                "buyerPaid='" + buyerPaid + '\'' +
                ", incomeType=" + incomeType +
                ", anchorId=" + anchorId +
                ", anchorName='" + anchorName + '\'' +
                ", anchorIncome='" + anchorIncome + '\'' +
                '}';
    }
}
