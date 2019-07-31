package com.laka.live.shopping.bean.newversion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @ClassName:
 * @Description:
 * @Author: chuan
 * @Version: 1.0
 * @Date: 29/08/2017
 */

public class ShoppingGoodsIncomeBean {
    @Expose
    @SerializedName("incomes")
    private List<ShoppingGoodsBaseBean> incomes;

    @Expose
    @SerializedName("sum")
    private ShoppingGoodsIncomeSumBean sum;

    public List<ShoppingGoodsBaseBean> getIncomes() {
        return incomes;
    }

    public void setIncomes(List<ShoppingGoodsBaseBean> incomes) {
        this.incomes = incomes;
    }

    public ShoppingGoodsIncomeSumBean getSum() {
        return sum;
    }

    public void setSum(ShoppingGoodsIncomeSumBean sum) {
        this.sum = sum;
    }
}
