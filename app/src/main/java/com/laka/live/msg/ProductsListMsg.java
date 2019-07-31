package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.PayProduct;
import com.laka.live.util.Common;

/**
 * Created by luwies on 16/3/29.
 */
public class ProductsListMsg extends Msg {

    @Expose
    @SerializedName(Common.PRODUCTS)
    private PayProduct payProduct;

    @Expose
    @SerializedName(Common.BALANCE)
    private float balance;

    public PayProduct getPayProduct() {
        return payProduct;
    }

    public float getBalance() {
        return balance;
    }

}

