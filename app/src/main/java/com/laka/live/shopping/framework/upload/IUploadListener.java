package com.laka.live.shopping.framework.upload;

/**
 * Created by ios on 16/11/18.
 */

public interface IUploadListener {
    void onSuccess(String url);
    void onFailure(String code, String msg);
    void onProgress(long currentSize, long totalSize);
}
