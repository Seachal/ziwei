package com.laka.live.shopping.bean.json2bean;

import com.laka.live.shopping.bean.BaseBean;
import com.laka.live.shopping.bean.ShoppingOrdersBean;

import java.io.Serializable;

/**
 * Created by zhxu on 2015/12/22.
 * Email:357599859@qq.com
 */
public class JTBShoppingOrders extends BaseBean implements Serializable {

    private ShoppingOrdersBean data;

    public ShoppingOrdersBean getData() {
        return data;
    }

    public void setData(ShoppingOrdersBean data) {
        this.data = data;
    }
}
