package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by luwies on 16/3/19.
 */
public class QcloudSigMsg extends Msg {

    /**
     * sig
     */
    @Expose
    @SerializedName("sig")
    private String sig;

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }
}
