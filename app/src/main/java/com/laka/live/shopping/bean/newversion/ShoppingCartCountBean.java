package com.laka.live.shopping.bean.newversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @ClassName: ShoppingCartCountBean
 * @Description: 购物车商品数量
 * @Author: chuan
 * @Version: 1.0
 * @Date: 27/07/2017
 */
public class ShoppingCartCountBean implements Serializable {

    @Expose
    @SerializedName("quantity")
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
