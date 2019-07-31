package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

/**
 * Created by luwies on 16/5/9.
 */
public class GetQinNiuUpLoadTokenMsg extends Msg {

    @Expose
    @SerializedName(Common.KEY)
    private String key;

    @Expose
    @SerializedName(Common.TOKEN)
    private String token;


    @Expose
    @SerializedName(Common.UPLOAD_TOKEN)
    private String uploadToken;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUploadToken() {
        return uploadToken;
    }

    public void setUploadToken(String uploadToken) {
        this.uploadToken = uploadToken;
    }
}
