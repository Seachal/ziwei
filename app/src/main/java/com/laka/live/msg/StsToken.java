package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

/**
 * Created by Lyf on 2017/4/24.
 */

public class StsToken extends Msg {

    @Expose
    @SerializedName(Common.ID)
    public String id; // 访问oss的id AccessKeyId
    @Expose
    @SerializedName(Common.TOKEN)
    public String token; // 访问oss的Secret AccessKeySecret
    @Expose
    @SerializedName(Common.SECRET)
    public String secret; //访问oss的Token SecurityToken

}
