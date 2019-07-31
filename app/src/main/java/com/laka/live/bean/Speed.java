package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

import java.io.Serializable;

/**
 * n
 * Created by luwies on 16/6/29.
 */
public class Speed implements Serializable {


    @Expose
    @SerializedName(Common.NAME)
    private String name;

    @Expose
    @SerializedName(Common.RATE)
    private float rate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
