package com.laka.live.shopping.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhxu on 2015/12/22.
 * Email:357599859@qq.com
 */
public class ShoppingOrdersBean implements Serializable {

    private int orderCount;
    private List<ShoppingOrderListBean> orders;

    public int getOrderCount() {
        return orderCount;
    }

    public List<ShoppingOrderListBean> getOrder() {
        return orders;
    }
}
