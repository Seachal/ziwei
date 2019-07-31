package com.laka.live.shopping.bean.newversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @ClassName:
 * @Description:
 * @Author: chuan
 * @Version: 1.0
 * @Date: 18/10/2017
 */

public class ShoppingGoodsIncomeSumBean {
    @Expose
    @SerializedName("total")
    private String total;

    @Expose
    @SerializedName("recommend")
    private String recommend;

    @Expose
    @SerializedName("agent")
    private String agent;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    @Override
    public String toString() {
        return "ShoppingGoodsIncomeSumBean{" +
                "total='" + total + '\'' +
                ", recommend='" + recommend + '\'' +
                ", agent='" + agent + '\'' +
                '}';
    }
}
