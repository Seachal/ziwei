package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.Income;
import com.laka.live.util.Common;

/**
 * Created by luwies on 16/3/28.
 */
public class EarningsMsg extends Msg {

    @Expose
    @SerializedName(Common.DATA)
    private Income income;

    public Income getIncome() {
        return income;
    }

    public void setIncome(Income income) {
        this.income = income;
    }
}
