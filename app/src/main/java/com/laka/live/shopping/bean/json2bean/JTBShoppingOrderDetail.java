package com.laka.live.shopping.bean.json2bean;

import com.laka.live.shopping.bean.BaseBean;
import com.laka.live.shopping.bean.ShoppingOrderDetailBean;

import java.io.Serializable;

/**
 * Created by zhxu on 2016/1/8.
 * Email:357599859@qq.com
 */
public class JTBShoppingOrderDetail extends BaseBean implements Serializable {

    private ShoppingOrderDetailBean data;

    public ShoppingOrderDetailBean getData() {
        return data;
    }

    public void setData(ShoppingOrderDetailBean data) {
        this.data = data;
    }
}
