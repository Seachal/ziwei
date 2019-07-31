package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

import java.util.List;

/**
 * Created by luwies on 16/3/24.
 */
public class City {

    @Expose
    @SerializedName(Common.STATE)
    private String name;

    @Expose
    @SerializedName(Common.CITIES)
    private List<String> cities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public boolean hasSub() {
        return cities != null && cities.isEmpty() == false;
    }
}
