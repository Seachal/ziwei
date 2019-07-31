package com.laka.live.shopping.bean.json2bean;

import com.laka.live.shopping.bean.BaseBean;
import com.laka.live.shopping.bean.ShoppingNewMainBean;

import java.io.Serializable;

/**
 * Created by zhxu on 2016/3/23.
 * Email:357599859@qq.com
 */
public class JTBShoppingNewMain extends BaseBean implements Serializable {

    private ShoppingNewMainBean data;

    public ShoppingNewMainBean getData() {
        return data;
    }

    public void setData(ShoppingNewMainBean data) {
        this.data = data;
    }
}
