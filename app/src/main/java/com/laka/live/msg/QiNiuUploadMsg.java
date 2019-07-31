package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

/**
 * Created by luwies on 16/5/9.
 */
public class QiNiuUploadMsg extends Msg {

    @Expose
    @SerializedName(Common.NAME)
    private String name;

    @Expose
    @SerializedName(Common.SUCCESS)
    private boolean success;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public boolean isSuccessFul() {
        return isSuccess();
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
