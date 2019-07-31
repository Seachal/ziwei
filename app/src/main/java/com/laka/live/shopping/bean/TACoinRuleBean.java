package com.laka.live.shopping.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linhz on 2016/3/31.
 * Email: linhaizhong@ta2she.com
 */
public class TACoinRuleBean {

    private List<String> rewardValues = new ArrayList<>();
    private String coinRate;
    private String goodsDiscount;
    private String rewardLevel;
    private String toRewardLevel;
    private String evaluPostGuideCoinNum;

    public String getEvaluPostGuideCoinNum() {
        return evaluPostGuideCoinNum;
    }

    public String getToRewardLevel() {
        return toRewardLevel;
    }

    public void setToRewardLevel(String toRewardLevel) {
        this.toRewardLevel = toRewardLevel;
    }

    public String getRewardLevel() {
        return rewardLevel;
    }

    public void setRewardLevel(String rewardLevel) {
        this.rewardLevel = rewardLevel;
    }

    public List<String> getRewardValues() {
        return rewardValues;
    }

    public void setRewardValues(List<String> rewardValues) {
        this.rewardValues = rewardValues;
    }

    public String getCoinRate() {
        return coinRate;
    }

    public void setCoinRate(String coinRate) {
        this.coinRate = coinRate;
    }

    public String getGoodsDiscount() {
        return goodsDiscount;
    }
}
