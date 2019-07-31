package com.laka.live.shopping.bean.json2bean;


import com.laka.live.shopping.bean.BaseBean;
import com.laka.live.shopping.bean.ShoppingTopListBean;

import java.io.Serializable;

/**
 * Created by zhxu on 2016/5/11.
 * Email:357599859@qq.com
 */
public class JTBShoppingTopList extends BaseBean implements Serializable {

    private ShoppingTopListBean data;

    public ShoppingTopListBean getData() {
        return data;
    }

    public void setData(ShoppingTopListBean data) {
        this.data = data;
    }
}
