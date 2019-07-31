package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.HomeFunction;
import com.laka.live.util.Common;

import java.util.List;

/**
 * @ClassName:
 * @Description:
 * @Author: chuan
 * @Version: 1.0
 * @Date: 6/5/17
 */

public class HomeFunctionMsg extends ListMag<HomeFunction> {
    @Expose
    @SerializedName(Common.DATA)
    private List<HomeFunction> data;

    @Override
    public List<HomeFunction> getList() {
        return data;
    }

    public void setData(List<HomeFunction> data) {
        this.data = data;
    }
}
