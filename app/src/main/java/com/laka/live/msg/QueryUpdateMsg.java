package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.VersionInfo;
import com.laka.live.util.Common;

/**
 * Created by luwies on 16/4/8.
 */
public class QueryUpdateMsg extends Msg{

    @Expose
    @SerializedName(Common.DATA)
    private VersionInfo info;

    public VersionInfo getInfo() {
        return info;
    }

    public void setInfo(VersionInfo info) {
        this.info = info;
    }
}
