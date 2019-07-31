package com.laka.live.update;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import com.coolerfall.download.DownloadCallback;
import com.coolerfall.download.DownloadRequest;
import com.coolerfall.download.Priority;
import com.laka.live.R;
import com.laka.live.application.LiveApplication;
import com.laka.live.bean.VersionInfo;
import com.laka.live.download.DownloadManager;
import com.laka.live.thread.BackgroundThread;
import com.laka.live.util.Log;
import com.laka.live.util.Md5Util;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Util;
import com.laka.live.util.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by heller on 15/1/11.
 * zwl update 16/7/26
 */
public class UpdateHelper {
    private static final String tmpFileName = "laka.tmp";/* 下载包安装路径 */
    private static final String saveFileName = "laka.apk";
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private static final int DOWN_FAIL = 3;
    private static final int CHECK_FAIL = 4;
    private static final String TAG = "UpdateHelper";

    private Activity mActivity = null;
    private File mApkFile;
    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressDialog downloadDialog;
    private int progress;
    private Thread downLoadThread;
    private boolean interceptFlag = false;
    private VersionInfo mVersion;

    private static final int MAX_RETRY_TIME = 5;
    private static final int RETRY_INTERVAL = 2;
    private static final int PROGRESS_INTERVAL = 100;
    public UpdateHelper(Activity context, VersionInfo version) {
        mActivity = context;
        mVersion = version;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    downloadDialog.setProgress(progress);
                    break;
                case DOWN_OVER:
                    installApk();
                    if (downloadDialog.isShowing()) {
                        downloadDialog.dismiss();
                    }
                    break;
                case DOWN_FAIL:
                    //下载失败
                    downloadFail();
                    break;
                case CHECK_FAIL:
                    checkFail();
                    break;
                default:
                    break;
            }
        }

    };

    private File getSDDir() {
        File sdDir = Util.getSDFile();
        if (sdDir == null) {
            return null;
        }
        File dir = new File(sdDir, mActivity.getPackageName());
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir;
    }

    private Runnable mDownApkRunnable = new Runnable() {
        @Override
        public void run() {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                if (StringUtils.isEmpty(mVersion.getUrl())) {
                    mHandler.sendEmptyMessage(DOWN_FAIL);
                    return;
                }

                URL url = new URL(mVersion.getUrl());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    mHandler.sendEmptyMessage(DOWN_FAIL);
                    return;
                }
                int length = conn.getContentLength();
                is = conn.getInputStream();

                File dir = getSDDir();
                if (dir == null) {
                    mHandler.sendEmptyMessage(DOWN_FAIL);
                    showToastInUiThread();
                    return;
                }

                File apkTmpFile = new File(dir, tmpFileName);
                if (apkTmpFile.exists()) {
                    apkTmpFile.delete();
                }

                fos = new FileOutputStream(apkTmpFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numRead = is.read(buf);
                    count += numRead;
                    progress = (int) (((float) count / length) * 100);
                    //更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numRead <= 0) {
                        break;
                    }
                    fos.write(buf, 0, numRead);
                } while (!interceptFlag);//点击取消就停止下载.


                if (interceptFlag && apkTmpFile.exists()) {
                    apkTmpFile.delete();
                } else if (!interceptFlag) {
                    progress = 100;
                    //更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (check(apkTmpFile)) {
                        //下载完成通知安装
                        mApkFile = new File(dir, saveFileName);
                        if (mApkFile.exists()) {
                            mApkFile.delete();
                        }
                        apkTmpFile.renameTo(mApkFile);
                        mHandler.sendEmptyMessage(DOWN_OVER);
                    } else {
                        mHandler.sendEmptyMessage(CHECK_FAIL);
                    }
                }
            } catch (Exception e) {
                mHandler.sendEmptyMessage(DOWN_FAIL);
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    private void showToastInUiThread() {
        if (mActivity == null) {
            return;
        }
     /*   mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastHelper.showToast(mActivity, ResourceHelper.getString(R.string.install_apk_invalid_save_path));
            }
        });*/
        updateFailed();
    }

    private boolean check(File file) {
        if (file == null || !file.exists() || mActivity == null) {
            return false;
        }

        PackageManager pm = mActivity.getPackageManager();
        PackageInfo tmpInfo = pm.getPackageArchiveInfo(file.getPath(), 0);
        if (tmpInfo == null) {
            return false;
        }

        PackageInfo info;
        try {
            info = pm.getPackageInfo(mActivity.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }

        return info.packageName.equals(tmpInfo.packageName) && info.versionCode < tmpInfo.versionCode
                && mVersion.getVersion() == tmpInfo.versionCode /*&& info.signatures[0].equals(tmpInfo.signatures[0])*/;

    }

    /**
     * 下载apk
     */
    public void downloadApk() {
        File dir = getSDDir();
        if (dir == null) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.install_apk_invalid_save_path));
            updateFailed();
            return;
        }
        mApkFile = new File(dir, saveFileName);
        if (check(mApkFile)) {
            installApk();
            return;
        }

        downloadDialog = new ProgressDialog(mActivity);
        downloadDialog.setTitle(mActivity.getString(R.string.downloading));
        downloadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        downloadDialog.setMax(100);
        downloadDialog.setProgress(0);
        downloadDialog.setIndeterminate(false);
        downloadDialog.setCancelable(false);

        final boolean isForce = mVersion.isForce();
        downloadDialog.setButton(AlertDialog.BUTTON_NEGATIVE, mActivity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                interceptFlag = true;
                if (isForce) {
                    LiveApplication.getInstance().killAllProcess(mActivity);
                    updateFailed();
                } else {
                    UpdateCheckManager.getInstance().setIgnoreUpdateNow(true);
                }
            }
        });
        downloadDialog.setCancelable(false);
        downloadDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (isForce) {
                    DownloadManager.getInstance().cancelAll();
                    LiveApplication.getInstance().killAllProcess(mActivity);
                    updateFailed();
                } else {
                    UpdateCheckManager.getInstance().setIgnoreUpdateNow(true);
                }
                return false;
            }
        });
        downloadDialog.show();

//        downLoadThread = new Thread(mDownApkRunnable);
//        downLoadThread.start();

        //改为用下载管理类下载
        DownloadRequest request = new DownloadRequest.Builder()
                .url(mVersion.getUrl())
                .retryTime(MAX_RETRY_TIME)
                .retryInterval(RETRY_INTERVAL, TimeUnit.SECONDS)
                .progressInterval(PROGRESS_INTERVAL, TimeUnit.MILLISECONDS)
                .priority(Priority.HIGH)
                .allowedNetworkTypes(DownloadRequest.NETWORK_WIFI | DownloadRequest.NETWORK_MOBILE)
//                .destinationDirectory(MUSIC_DIR)
                .destinationFilePath(mApkFile.getAbsolutePath())
//                .destinationDirectory(GIFT_DIR)
                .downloadCallback(new DownloadCallback() {
                    @Override
                    public void onFailure(int downloadId, int statusCode, String errMsg) {
                        super.onFailure(downloadId, statusCode, errMsg);
                        Log.d(TAG, " onFailure errMsg=" + errMsg);
                        progress = 0;
                        mHandler.sendEmptyMessage(DOWN_FAIL);
                        showToastInUiThread();
                    }

                    @Override
                    public void onProgress(int downloadId, long bytesWritten, long totalBytes) {
                        super.onProgress(downloadId, bytesWritten, totalBytes);

                        Log.d(TAG, " onProgress downloadId=" + downloadId + " percent=" + (float) bytesWritten / (float) totalBytes);
                        progress = (int) (((float) bytesWritten / (float) totalBytes)*100);
                        mHandler.sendEmptyMessage(DOWN_UPDATE);
                    }

                    @Override
                    public void onRetry(int downloadId) {
                        super.onRetry(downloadId);
                        progress = 0;
                        mHandler.sendEmptyMessage(DOWN_UPDATE);
//                        Log.d(TAG, " onRetry downloadId="+downloadId);
                    }

                    @Override
                    public void onStart(int downloadId, long totalBytes) {
                        super.onStart(downloadId, totalBytes);
                        Log.d(TAG, " onStart downloadId=" + downloadId);
                    }

                    @Override
                    public void onSuccess(final int downloadId, final String filePath) {
                        super.onSuccess(downloadId, filePath);
                        Log.d(TAG, " onSuccess downloadId=" + downloadId + " filePath=" + filePath);
                        progress = 100;
                        //更新进度
                        mHandler.sendEmptyMessage(DOWN_UPDATE);
                        if (check(new File(filePath))) {
                            //下载完成通知安装
//                            mApkFile = new File(dir, saveFileName);
//                            if (mApkFile.exists()) {
//                                mApkFile.delete();
//                            }
//                            apkTmpFile.renameTo(mApkFile);
                            mHandler.sendEmptyMessage(DOWN_OVER);
                        } else {
                            mHandler.sendEmptyMessage(CHECK_FAIL);
                        }

                    }
                })
                .build();
        DownloadManager.getInstance().cancelAll();
        int downloadId = DownloadManager.getInstance().add(request);


    }

    /**
     * 安装apk
     */
    private void installApk() {
        if (mApkFile != null && !mApkFile.exists()) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.install_apk_not_exist));
            updateFailed();
            return;
        }
        try {
            UpdateCheckManager.getInstance().setIsLimitCheckUpdateOnResume(false);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + mApkFile.toString()), "application/vnd.android.package-archive");
            mActivity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ToastHelper.showToast(ResourceHelper.getString(R.string.install_apk_fail));
            updateFailed();
        }
    }

    private void checkFail() {
        downloadDialog.setTitle(R.string.check_fail);
        updateFailed();
    }

    private void updateFailed() {
    }

    private void downloadFail() {
        if (downloadDialog == null) {
            return;
        }
        downloadDialog.setTitle(R.string.download_fail);
    }
}
