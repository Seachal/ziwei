package com.laka.live.shopping.bean.json2bean;

import com.laka.live.shopping.bean.BaseBean;
import com.laka.live.shopping.bean.ShoppingOrderNumBean;

import java.io.Serializable;

/**
 * Created by zhxu on 2015/12/22.
 * Email:357599859@qq.com
 */
public class JTBShoppingOrderNum extends BaseBean implements Serializable {

    private ShoppingOrderNumBean data;

    public ShoppingOrderNumBean getData() {
        return data;
    }

    public void setData(ShoppingOrderNumBean data) {
        this.data = data;
    }
}
