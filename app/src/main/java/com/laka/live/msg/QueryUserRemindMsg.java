package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.MsgRemindConfig;
import com.laka.live.util.Common;

/**
 * Created by luwies on 16/4/7.
 */
public class QueryUserRemindMsg extends Msg {

    @Expose
    @SerializedName(Common.DATA)
    private MsgRemindConfig remindConfig;

    public MsgRemindConfig getRemindConfig() {
        return remindConfig;
    }

    public void setRemindConfig(MsgRemindConfig remindConfig) {
        this.remindConfig = remindConfig;
    }
}
