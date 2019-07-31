package com.laka.live.shopping.framework.upload;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.laka.live.application.LiveApplication;
import com.laka.live.shopping.bean.StsBean;
import com.laka.live.shopping.network.HttpCallbackAdapter;
import com.laka.live.shopping.network.HttpManager;
import com.laka.live.shopping.network.HttpMethod;
import com.laka.live.shopping.network.HttpUrls;
import com.laka.live.util.Log;
import com.laka.live.util.Utils;

import java.util.Date;

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
    public static String folderName; // letter对应私信 ，private对应私人空间
    //    private static final String testBucket =  BuildConfig.UPLOAD_BUCKET_IMG;
    public static final String TAG_NAME = Utils.YM_FORMATER.format(new Date(System.currentTimeMillis()));
//    private static final String uploadObject = "sampleObject";
//    private static final String downloadObject = "sampleObject";
    //    private static final String uploadFilePath = "<upload_file_path>";

    private static UploadManager self;
    private static OSSAsyncTask ossAsyncTask;

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
            oss = new OSSClient(LiveApplication.getInstance(), endpoint, credentialProvider, conf);

        } catch (Exception e) {
            Log.log("UploadManager:" + e.toString());
        }

    }

    public void uploadFile(String folderName,String bucket, String uploadTag, String uploadFilePath, IUploadListener listener) {
        UploadManager.folderName = folderName;
        ossAsyncTask = new PutObjectSamples(oss, bucket, uploadTag, uploadFilePath, listener).asyncPutObjectFromLocalFile();
    }

    public void cancelUpload() {
        if (ossAsyncTask != null) {
            ossAsyncTask.cancel();
        }
    }

    // 获取临时凭证
    public static void getAccessKey() {

        HttpManager.getBusinessHttpManger().request(HttpUrls.GET_STS, HttpMethod.POST, StsBean.class, new HttpCallbackAdapter() {
            @Override
            public <T> void onSuccess(T obj, String result) {

                try {
                    StsBean bean = (StsBean) obj;
                    accessKeyId = bean.data.id;
                    accessKeySecret = bean.data.secret;
                    accessKeyToken = bean.data.token;
                } catch (Exception e) {
                    Log.log("getSts:" + e.toString());
                }

            }

            @Override
            public void onError(String errorStr, int code) {
                super.onError(errorStr, code);
                Log.log("errorStr:" + errorStr);

            }
        });
    }

}
