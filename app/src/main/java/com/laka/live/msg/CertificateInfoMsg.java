package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.CertificateInfo;
import com.laka.live.util.Common;

/**
 * Created by Lyf on 2017/4/14.
 */

public class CertificateInfoMsg extends Msg{

    @Expose
    @SerializedName(Common.DATA)
    public CertificateInfo data;

}
