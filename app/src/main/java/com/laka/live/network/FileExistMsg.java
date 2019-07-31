package com.laka.live.network;
/*
 * @ClassName: MayLikeVideosMsg
 * @Description: 查询猜你喜欢返回实体
 * @Author: chuan
 * @Version: 1.0
 * @Date: 11/22/16
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.msg.Msg;
import com.laka.live.util.Common;

public class FileExistMsg extends Msg {


    @Expose
    @SerializedName(Common.IS_EXIST)
    private int isExist;


    public boolean isExist() {
        if (isExist == 0) {
            return false;
        } else {
            return true;
        }
    }


}
