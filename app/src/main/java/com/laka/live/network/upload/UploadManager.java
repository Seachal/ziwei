package com.laka.live.network.upload;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.laka.live.network.DataProvider;
import com.laka.live.util.Log;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;


import java.util.Date;
import java.util.HashMap;

import framework.ioc.Ioc;

/**
 *  
 *  * @ClassName: UploadManager
 *  * @Description: 上传文件工具了
 *  * @Author: 关健   
 *  * @Version: 1.0.0
 *  * @Date 2016/11/16
 */

public class UploadManager {

    private OSS oss;
    // 运行sample前需要配置以下字段为有效的值
    private static final String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
    public static String accessKeyId;
    public static String accessKeySecret;
    public static String accessKeyToken;
    //    private static final String testBucket =  BuildConfig.UPLOAD_BUCKET_IMG;
    public static final String TAG_NAME = Utils.YM_FORMATER.format(new Date(System.currentTimeMillis()));
//    private static final String uploadObject = "sampleObject";
//    private static final String downloadObject = "sampleObject";
    //    private static final String uploadFilePath = "<upload_file_path>";

    private static UploadManager self;
    private static OSSAsyncTask asyncResumableTask;
    private static OSSAsyncTask ossAsyncTask;

    // 重置key,这样下次使用，才会重新创建。
    public static void resetKey() {
        self = null;
    }

    public static UploadManager getInstance() {
        if (self == null) {
            self = new UploadManager();
        }
        return self;
    }

    public UploadManager() {

        try {
            //OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
            OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(accessKeyId, accessKeySecret, accessKeyToken);
            ClientConfiguration conf = new ClientConfiguration();
            conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
            conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
            conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
            conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
            OSSLog.enableLog();
            oss = new OSSClient(Ioc.getApplication(), endpoint, credentialProvider, conf);

        } catch (Exception e) {
            Log.log("UploadManager:" + e.toString());
        }

    }

    public void uploadFile(String bucket, String uploadTag, String uploadFilePath, IUploadListener listener) {
        if (oss == null) {
            DataProvider.getStsToken(this);
            listener.onFailure("-3", "当前网络状态不好,请稍候重试！");
            return;
        }
        ossAsyncTask = new PutObjectSamples(oss, bucket, uploadTag, uploadFilePath, listener).asyncPutObjectFromLocalFile();
    }

    // objectKey ，上次上传中断的文件的objectKey
    public String resumedUploadFile(String bucket, String uploadTag, String objectKey, String uploadFilePath, IUploadListener listener) {

        if (asyncResumableTask == null || asyncResumableTask.isCompleted()) {
            if (oss == null) {
                DataProvider.getStsToken(this);
                listener.onFailure("-3", "当前网络状态不好,请稍候重试！");
                return null;
            }
            PutObjectSamples putObjectSamples = new PutObjectSamples(oss, bucket, uploadTag, uploadFilePath, listener);
            if (objectKey != null)
                putObjectSamples.setObjectKey(objectKey);
            asyncResumableTask = putObjectSamples.resumableUploadFile();
            return putObjectSamples.getObjectKey();
        }
        return null;
    }

    public void cancelUpload() {
        if (ossAsyncTask != null) {
            ossAsyncTask.cancel();
        }
    }

    public void cancelResumableTask() {

        if (asyncResumableTask != null) {
            asyncResumableTask.cancel();
        }
    }


}
