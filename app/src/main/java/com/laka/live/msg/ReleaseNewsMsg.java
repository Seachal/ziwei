package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

/**
 * Created by Lyf on 2017/4/7.
 */

public class ReleaseNewsMsg extends Msg {

    @Expose
    @SerializedName(Common.NEWS_ID)
    public int news_id; // 资讯ID

}
