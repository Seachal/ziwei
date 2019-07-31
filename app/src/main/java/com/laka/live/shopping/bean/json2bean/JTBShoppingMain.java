package com.laka.live.shopping.bean.json2bean;

import com.laka.live.shopping.bean.BaseBean;
import com.laka.live.shopping.bean.ShoppingMainBean;

import java.io.Serializable;

/**
 * Created by zhxu on 2015/12/21.
 * Email:357599859@qq.com
 */
public class JTBShoppingMain extends BaseBean implements Serializable {

    private ShoppingMainBean data;

    public ShoppingMainBean getData() {
        return data;
    }

    public void setData(ShoppingMainBean data) {
        this.data = data;
    }
}
