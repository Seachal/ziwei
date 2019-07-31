package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

/**
 * Created by luwies on 16/3/19.
 */
public class OrderInfoMsg extends Msg {

    /**
     * sig
     */
    @Expose
    @SerializedName(Common.DATA)
    private String qs;

    public String getQs() {
        return qs;
    }

    public void setQs(String qs) {
        this.qs = qs;
    }
}
