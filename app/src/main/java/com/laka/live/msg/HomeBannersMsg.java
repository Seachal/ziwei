package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.Banner;
import com.laka.live.util.Common;

import java.util.List;

/**
 * Created by luwies on 16/3/29.
 */
public class HomeBannersMsg extends ListMag<Banner> {
    @Expose
    @SerializedName(Common.DATA)
    private List<Banner> data;

    @Override
    public List<Banner> getList() {
        return data;
    }

    public void setList(List<Banner> data) {
        this.data = data;
    }


    @Override
    public boolean isEmpty() {
        return (data == null || data.isEmpty());
    }
}
