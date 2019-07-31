package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.BaseComment;

/**
 * @ClassName:
 * @Description:
 * @Author: chuan
 * @Version: 1.0
 * @Date: 14/09/2017
 */

public class CommentMsg extends Msg {
    @Expose
    @SerializedName("data")
    private BaseComment data;

    public BaseComment getData() {
        return data;
    }

    public void setData(BaseComment data) {
        this.data = data;
    }
}
