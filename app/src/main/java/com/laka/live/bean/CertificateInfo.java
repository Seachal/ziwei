package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

/**
 * Created by Lyf on 2017/4/14.
 */

public class CertificateInfo {

    @Expose
    @SerializedName(Common.CERTIFICATE_STATUS)
    private int certificate_status; // 认证状态，1=未认证，2=已认证

    @Expose
    @SerializedName(Common.CERTIFICATE_TIME)
    private long certificate_time;

    // 是否已认证
    public boolean isApproved(){
        return certificate_status == 2;
    }

}
