package com.laka.live.shopping.network;

/**
 * Created by zhangwulin on 2016/1/4.
 * email 1501448275@qq.com
 */
public interface IHttpLoadingCallBack extends IHttpCallBack {

    void onStart();

    void onWaiting();

    void onLoading(long total, long current, boolean isUploading);
}
