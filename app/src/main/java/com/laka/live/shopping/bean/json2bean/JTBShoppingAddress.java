package com.laka.live.shopping.bean.json2bean;

import com.laka.live.shopping.bean.BaseBean;
import com.laka.live.shopping.bean.ShoppingAddressBean;

import java.io.Serializable;

/**
 * Created by zhxu on 2015/12/22.
 * Email:357599859@qq.com
 */
public class JTBShoppingAddress extends BaseBean implements Serializable {

    private ShoppingAddressBean data;

    public ShoppingAddressBean getData() {
        return data;
    }

    public void setData(ShoppingAddressBean data) {
        this.data = data;
    }
}
