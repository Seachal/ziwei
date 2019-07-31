package com.laka.live.shopping.bean.json2bean;

import com.laka.live.shopping.bean.BaseBean;
import com.laka.live.shopping.bean.ShoppingCarGoodsState;
import java.io.Serializable;

/**
 * Created by chenjiawei on 2016/1/4.
 */
public class JTBShoppingCarGoods extends BaseBean implements Serializable {
    private ShoppingCarGoodsState data;

    public ShoppingCarGoodsState getData() {
        return data;
    }

    public void setData(ShoppingCarGoodsState data) {
        this.data = data;
    }
}
