package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.PayProduct;
import com.laka.live.util.Common;

import java.util.List;

/**
 * Created by luwies on 16/3/29.
 */
public class RoomTagMsg extends Msg {

    @Expose
    @SerializedName(Common.DATA)
    private List<String> tags;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

}

