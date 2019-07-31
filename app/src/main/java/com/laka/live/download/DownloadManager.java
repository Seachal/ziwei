package com.laka.live.download;

import com.coolerfall.download.DownloadRequest;
import com.coolerfall.download.URLDownloader;
import com.laka.live.application.LiveApplication;

/**
 * Created by zwl on 16/8/8.
 */

public class DownloadManager implements IDownloadManager {

    private static volatile DownloadManager sInstance;

    private com.coolerfall.download.DownloadManager mDownloadManager;

    private final static int MAX_DOWNLOAD_THREAD = 1;

    private DownloadManager() {
        mDownloadManager =
                new com.coolerfall.download.DownloadManager.Builder().context(LiveApplication.getInstance())
                        .downloader(URLDownloader.create())
                        .threadPoolSize(MAX_DOWNLOAD_THREAD)
                        .build();
    }

    public static DownloadManager getInstance() {
        if (sInstance == null) {
            synchronized (DownloadManager.class) {
                if (sInstance == null) {
                    sInstance = new DownloadManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * Add one download request into the queue.
     *
     * @param request download request
     * @return download id, if the id is not set, then manager will generate one.
     * if the request is in downloading, then -1 will be returned
     */
    public int add(DownloadRequest request) {
        return mDownloadManager.add(request);
    }


    @Override
    public boolean isDownloading(int downloadId) {
        return mDownloadManager.isDownloading(downloadId);
    }

    @Override
    public boolean isDownloading(String url) {
        return mDownloadManager.isDownloading(url);
    }

    @Override
    public int getTaskSize() {
        return mDownloadManager.getTaskSize();
    }

    @Override
    public void cancel(int downloadId) {
        mDownloadManager.cancel(downloadId);
    }

    @Override
    public void cancelAll() {
        mDownloadManager.cancelAll();
    }

    @Override
    public void release() {
        mDownloadManager.release();
    }
}
