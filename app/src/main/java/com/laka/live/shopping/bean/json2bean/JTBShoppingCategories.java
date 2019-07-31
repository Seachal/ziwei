package com.laka.live.shopping.bean.json2bean;


import com.laka.live.shopping.bean.BaseBean;
import com.laka.live.shopping.bean.ShoppingTopicBean;

import java.io.Serializable;

/**
 * Created by zhxu on 2016/5/3.
 * Email:357599859@qq.com
 */
public class JTBShoppingCategories extends BaseBean implements Serializable {

    private ShoppingTopicBean data;

    public ShoppingTopicBean getData() {
        return data;
    }

    public void setData(ShoppingTopicBean data) {
        this.data = data;
    }
}