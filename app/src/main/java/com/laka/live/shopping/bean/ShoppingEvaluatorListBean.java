package com.laka.live.shopping.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhxu on 2016/3/12.
 * Email:357599859@qq.com
 */
public class ShoppingEvaluatorListBean {

    private String endId;
    private int evaluatorCount;
    private List<ShoppingEvaluatorBean> evaluators = new ArrayList<>();

    public String getEndId() {
        return endId;
    }

    public int getEvaluatorCount() {
        return evaluatorCount;
    }

    public List<ShoppingEvaluatorBean> getEvaluators() {
        return evaluators;
    }
}
