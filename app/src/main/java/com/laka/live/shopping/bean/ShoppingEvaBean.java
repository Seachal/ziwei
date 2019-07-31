package com.laka.live.shopping.bean;

import java.util.List;

/**
 * Created by linhz on 2016/5/11.
 * Email: linhaizhong@ta2she.com
 */
public class ShoppingEvaBean {

    private String score; //我的分数
    private String parterScore; //伴侣分数
    private String evUserCount;
    private List<ShoppingEvaExprsBean> exprs;
    private List<ShoppingEvaluatorBean> users;

    public String getScore() {
        return score;
    }

    public String getParterScore() {
        return parterScore;
    }

    public List<ShoppingEvaExprsBean> getExprs() {
        return exprs;
    }

    public List<ShoppingEvaluatorBean> getUsers() {
        return users;
    }

    public String getEvUserCount() {
        return evUserCount;
    }

}
