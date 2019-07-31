package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.Splash;
import com.laka.live.util.Common;

/**
 * @ClassName: SplashMsg
 * @Description: 闪屏
 * @Author: chuan
 * @Version: 1.0
 * @Date: 18/09/2017
 */

public class SplashMsg extends Msg {
    @Expose
    @SerializedName(Common.DATA)
    private Splash data;

    public Splash getData() {
        return data;
    }

    public void setData(Splash data) {
        this.data = data;
    }
}
