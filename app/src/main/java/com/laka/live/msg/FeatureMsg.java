package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.Feature;
import com.laka.live.util.Common;

import java.util.List;

/**
 * Created by luwies on 16/6/28.
 */
public class FeatureMsg extends ListMag<Feature> {

    @Expose
    @SerializedName(Common.ROOMS)
    private List<Feature> features;

    @Override
    public List<Feature> getList() {
        return features;
    }
}
