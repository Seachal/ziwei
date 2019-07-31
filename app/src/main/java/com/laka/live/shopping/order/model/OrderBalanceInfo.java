package com.laka.live.shopping.order.model;


import com.laka.live.shopping.bean.ShoppingAddressBean;
import com.laka.live.shopping.bean.ShoppingCarGoodsBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhxu on 2016/1/4.
 * Email:357599859@qq.com
 */
public class OrderBalanceInfo implements Serializable {
    public String panicShoppingId;
    public String goodsId;
    public String skuId;
    public int courseId = -1;
    public int videoId = -1;
    public int goodsCount;

    public List<ShoppingCarGoodsBean> getList() {
        return list;
    }

    public void setList(List<ShoppingCarGoodsBean> list) {
        this.list = list;
    }

    private List<ShoppingCarGoodsBean> list = new ArrayList<>();

    public String totalPrice;
    public String goodsPrice;
    public int payType;
    public String userRemark;
    public String useCoinCount;
    private ShoppingAddressBean shoppingAddressBean;

    public ShoppingAddressBean getShoppingAddressBean() {
        return shoppingAddressBean;
    }

    public void setShoppingAddressBean(ShoppingAddressBean shoppingAddressBean) {
        this.shoppingAddressBean = shoppingAddressBean;
    }

}
