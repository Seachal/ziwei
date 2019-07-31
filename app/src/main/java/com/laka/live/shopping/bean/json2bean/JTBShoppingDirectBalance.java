package com.laka.live.shopping.bean.json2bean;


import com.laka.live.shopping.bean.BaseBean;
import com.laka.live.shopping.bean.ShoppingBalanceBean;

import java.io.Serializable;

/**
 * Created by zhxu on 2015/12/22.
 * Email:357599859@qq.com
 */
public class JTBShoppingDirectBalance extends BaseBean implements Serializable {

    private ShoppingBalanceBean data;

    public ShoppingBalanceBean getData() {
        return data;
    }

    public void setData(ShoppingBalanceBean data) {
        this.data = data;
    }
}
