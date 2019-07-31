package com.laka.live.shopping.bean.newversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @ClassName: ShoppingGoodsIncomeDetail
 * @Description: 商品收益明细
 * @Author: chuan
 * @Version: 1.0
 * @Date: 29/08/2017
 */

public class ShoppingGoodsIncomeDetail {
    private final static int INCOME_RECOMMEND = 1, INCOME_AGENT = 2;
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("nickname")
    private String nickName;

    @Expose
    @SerializedName("createtime")
    private long createTime;

    @Expose
    @SerializedName("totalPrice")
    private float totalPrice;

    @Expose
    @SerializedName("income")
    private float income;

    @Expose
    @SerializedName("income_type")
    private int incomeType;

    @Expose
    @SerializedName("recommender")
    private String recommender;

    @Expose
    @SerializedName("recommender_income")
    private float recommentIncome;

    @Expose
    @SerializedName("agent")
    private String agent;

    @Expose
    @SerializedName("agent_income")
    private float agentIncome;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public float getIncome() {
        return income;
    }

    public void setIncome(float income) {
        this.income = income;
    }

    public String getIncomeTypeStr() {
        switch (incomeType) {
            case INCOME_RECOMMEND:
                return "推荐收入";
            case INCOME_AGENT:
                return "代理收入";
            default:
                return "";
        }
    }

    public int getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(int incomeType) {
        this.incomeType = incomeType;
    }

    public String getRecommender() {
        return recommender;
    }

    public void setRecommender(String recommender) {
        this.recommender = recommender;
    }

    public float getRecommentIncome() {
        return recommentIncome;
    }

    public void setRecommentIncome(float recommentIncome) {
        this.recommentIncome = recommentIncome;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public float getAgentIncome() {
        return agentIncome;
    }

    public void setAgentIncome(float agentIncome) {
        this.agentIncome = agentIncome;
    }

    @Override
    public String toString() {
        return "ShoppingGoodsIncomeDetail{" +
                "id=" + id +
                ", nickName='" + nickName + '\'' +
                ", createTime=" + createTime +
                ", totalPrice=" + totalPrice +
                ", income=" + income +
                ", incomeType=" + incomeType +
                ", recommender='" + recommender + '\'' +
                ", recommentIncome=" + recommentIncome +
                ", agent='" + agent + '\'' +
                ", agentIncome=" + agentIncome +
                '}';
    }
}
