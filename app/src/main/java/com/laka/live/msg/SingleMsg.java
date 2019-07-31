package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

/**
 * Created by luyi on 2015/9/7.
 */
public class SingleMsg<T> extends Msg {

    @Expose
    @SerializedName(Common.DATA)
    private T result;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
