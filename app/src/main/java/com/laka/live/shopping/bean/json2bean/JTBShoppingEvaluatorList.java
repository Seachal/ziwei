package com.laka.live.shopping.bean.json2bean;

import com.laka.live.shopping.bean.BaseBean;
import com.laka.live.shopping.bean.ShoppingEvaluatorListBean;

import java.io.Serializable;

/**
 * Created by zhxu on 2016/3/12.
 * Email:357599859@qq.com
 */
public class JTBShoppingEvaluatorList extends BaseBean implements Serializable {

    private ShoppingEvaluatorListBean data;

    public ShoppingEvaluatorListBean getData() {
        return data;
    }

    public void setData(ShoppingEvaluatorListBean data) {
        this.data = data;
    }
}
