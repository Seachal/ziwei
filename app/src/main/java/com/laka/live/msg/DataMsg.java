package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

/**
 * Created by Lyf on 2017/4/17.
 */

public class DataMsg extends Msg {

    @Expose
    @SerializedName(Common.DATA)
    public String noticeInfo;

}
