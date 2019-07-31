package com.laka.live.download;

import com.coolerfall.download.DownloadRequest;

/**
 * Created by luwies on 16/8/13.
 */
public interface IDownloadManager {

     int add(DownloadRequest request);

     boolean isDownloading(int downloadId);


     boolean isDownloading(String url);


     int getTaskSize();

    void cancel(int downloadId);

    void cancelAll();

    void release();


}
