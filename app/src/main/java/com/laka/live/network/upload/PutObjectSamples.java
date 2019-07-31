package com.laka.live.network.upload;

import java.io.BufferedReader;
import java.io.File;

import android.os.Environment;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Date;
import java.util.Random;
import java.util.HashMap;
import java.io.IOException;

import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.utils.BinaryUtil;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.AppendObjectRequest;
import com.alibaba.sdk.android.oss.model.AppendObjectResult;
import com.alibaba.sdk.android.oss.model.DeleteObjectRequest;
import com.alibaba.sdk.android.oss.model.DeleteObjectResult;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.ResumableUploadRequest;
import com.alibaba.sdk.android.oss.model.ResumableUploadResult;
import com.google.gson.Gson;
import com.laka.live.BuildConfig;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.help.EventBusManager;
import com.laka.live.network.DataProvider;
import com.laka.live.util.TimeUtil;
import com.laka.live.util.UiPreference;
import com.laka.live.util.Utils;

import framework.utils.FileUtil;
import framework.utils.GsonTools;


/**
 * Created by zhouzhuo on 12/3/15.
 */
public class PutObjectSamples {

    private static final String TAG = "PutObjectSamples";
    private OSS oss;
    private String objectKey; // OSS服务器上对应的文件的名, 比如 1706/XXXX
    private String testBucket; // OSS服务器上对应的目录名 ，比如 1706
    private String testObject;
    private String uploadFilePath;

    private IUploadListener uploadListener;


    public PutObjectSamples(OSS client, String testBucket, String testObject, String uploadFilePath, IUploadListener uploadListener) {
        this.oss = client;
        this.testBucket = testBucket;
        this.testObject = testObject;
        this.uploadFilePath = uploadFilePath;
        this.uploadListener = uploadListener;
    }

    // 从本地文件上传，采用阻塞的同步接口
    public void putObjectFromLocalFile() {
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(testBucket, testObject, uploadFilePath);

        try {
            PutObjectResult putResult = oss.putObject(put);

            Log.d("PutObject", "UploadSuccess");

            Log.d("ETag", putResult.getETag());
            Log.d("RequestId", putResult.getRequestId());
        } catch (ClientException e) {
            // 本地异常如网络异常等
            e.printStackTrace();
        } catch (ServiceException e) {
            // 服务异常
            Log.e("RequestId", e.getRequestId());
            Log.e("ErrorCode", e.getErrorCode());
            Log.e("HostId", e.getHostId());
            Log.e("RawMessage", e.getRawMessage());
        }
    }

    // 从本地文件上传，使用非阻塞的异步接口
    public OSSAsyncTask asyncPutObjectFromLocalFile() {
        final File f = new File(uploadFilePath);
        if (!f.exists()) {
            if (uploadListener != null) {
                uploadListener.onFailure("-2", "文件不存在");
            }
            return null;
        }

        String path = "";
//        if (testObject.equals(UploadManager.TAG_AVATAR)) {
//            path = testObject + "/" + AccountInfoManager.getInstance().getCurrentAccountUserIdStr() + "_" + System.currentTimeMillis() + "_" + f.getName();
//        } else {
//            String fileName = f.getName();
//            String prefix = fileName.substring(fileName.lastIndexOf("."));
//            path = testObject + "/" + "u" + AccountInfoManager.getInstance().getCurrentAccountUserIdStr() + String.valueOf(System.currentTimeMillis()+fileName.hashCode()).substring(0, 10) + prefix;//Object 格式为 年月/u+用户id+10位数字或字母+文件后缀
//        }
        String fileName = f.getName();
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        path = testObject + "/" + "u" + AccountInfoManager.getInstance().getCurrentAccountUserIdStr() +
                String.valueOf(System.currentTimeMillis() + fileName.hashCode()).substring(0, 10) + prefix;//Object 格式为 年月/u+用户id+10位数字或字母+文件后缀

        final String pre = path;
        Log.d(TAG, " bucket=" + testBucket + " object=" + testObject + " pre=" + pre+",upd="+uploadFilePath);

        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(testBucket, pre, uploadFilePath);

        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
                if (uploadListener != null) {
                    uploadListener.onProgress(currentSize, totalSize);
                }
            }
        });

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("PutObject", "UploadSuccess");
                Log.d("ETag", result.getETag());
                Log.d("RequestId", result.getRequestId());
                if (uploadListener != null) {
                    Log.d(TAG, " 返回 pre=" + pre);
                    String uploadUrl = pre;// BASE_URL + pre
                    uploadListener.onSuccess(uploadUrl);
                }

            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                    if (uploadListener != null) {
                        if (clientExcepion.isCanceledException())
                            uploadListener.onFailure("-1", "已取消上传");
                        else
                            uploadListener.onFailure("-2", "上传失败");
                    }
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                    if (uploadListener != null) {
                        DataProvider.getStsToken(this); // 发送失败时,再重新拿一次。
                        uploadListener.onFailure(serviceException.getErrorCode(), serviceException.getRawMessage());
                    }
                }
            }
        });
        return task;
    }

    // 直接上传二进制数据，使用阻塞的同步接口
    public void putObjectFromByteArray() {
        // 构造测试的上传数据
        byte[] uploadData = new byte[100 * 1024];
        new Random().nextBytes(uploadData);

        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(testBucket, testObject, uploadData);

        try {
            PutObjectResult putResult = oss.putObject(put);

            Log.d("PutObject", "UploadSuccess");

            Log.d("ETag", putResult.getETag());
            Log.d("RequestId", putResult.getRequestId());
        } catch (ClientException e) {
            // 本地异常如网络异常等
            e.printStackTrace();
        } catch (ServiceException e) {
            // 服务异常
            Log.e("RequestId", e.getRequestId());
            Log.e("ErrorCode", e.getErrorCode());
            Log.e("HostId", e.getHostId());
            Log.e("RawMessage", e.getRawMessage());
        }
    }

    // 上传时设置ContentType等，也可以添加自定义meta信息
    public void putObjectWithMetadataSetting() {
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(testBucket, testObject, uploadFilePath);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/octet-stream");
        metadata.addUserMetadata("x-oss-meta-name1", "value1");

        put.setMetadata(metadata);

        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("PutObject", "UploadSuccess");

                Log.d("ETag", result.getETag());
                Log.d("RequestId", result.getRequestId());
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
    }

    // 上传文件可以设置server回调
    public void asyncPutObjectWithServerCallback() {
        // 构造上传请求
        final PutObjectRequest put = new PutObjectRequest(testBucket, testObject, uploadFilePath);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/octet-stream");

        put.setMetadata(metadata);

        put.setCallbackParam(new HashMap<String, String>() {
            {
                put("callbackUrl", "110.75.82.106/mbaas/callback");
                put("callbackBody", "test");
            }
        });

        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("PutObject", "UploadSuccess");

                // 只有设置了servercallback，这个值才有数据
                String serverCallbackReturnJson = result.getServerCallbackReturnBody();

                Log.d("servercallback", serverCallbackReturnJson);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
    }

    public void asyncPutObjectWithMD5Verify() {
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(testBucket, testObject, uploadFilePath);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/octet-stream");
        try {
            // 设置Md5以便校验
            metadata.setContentMD5(BinaryUtil.calculateBase64Md5(uploadFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        put.setMetadata(metadata);

        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("PutObject", "UploadSuccess");

                Log.d("ETag", result.getETag());
                Log.d("RequestId", result.getRequestId());
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
    }

    // 追加文件
    public void appendObject() {
        // 如果bucket中objectKey存在，将其删除
        try {
            DeleteObjectRequest delete = new DeleteObjectRequest(testBucket, testObject);
            DeleteObjectResult result = oss.deleteObject(delete);
        } catch (ClientException clientException) {
            clientException.printStackTrace();
        } catch (ServiceException serviceException) {
            Log.e("ErrorCode", serviceException.getErrorCode());
            Log.e("RequestId", serviceException.getRequestId());
            Log.e("HostId", serviceException.getHostId());
            Log.e("RawMessage", serviceException.getRawMessage());
        }
        AppendObjectRequest append = new AppendObjectRequest(testBucket, testObject, uploadFilePath);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/octet-stream");
        append.setMetadata(metadata);

        // 设置追加位置，只能从文件末尾开始追加，如果是新文件，从0开始
        append.setPosition(0);

        append.setProgressCallback(new OSSProgressCallback<AppendObjectRequest>() {
            @Override
            public void onProgress(AppendObjectRequest request, long currentSize, long totalSize) {
                Log.d("AppendObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        OSSAsyncTask task = oss.asyncAppendObject(append, new OSSCompletedCallback<AppendObjectRequest, AppendObjectResult>() {
            @Override
            public void onSuccess(AppendObjectRequest request, AppendObjectResult result) {
                Log.d("AppendObject", "AppendSuccess");
                Log.d("NextPosition", "" + result.getNextPosition());
            }

            @Override
            public void onFailure(AppendObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
    }

    // 断点续传
    public OSSAsyncTask resumableUploadFile() {

        final File f = new File(uploadFilePath);
        if (!f.exists()) {
            if (uploadListener != null) {
                uploadListener.onFailure("-2", "文件不存在");
            }
            return null;
        }


        String fileName = f.getName();
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        String tempObjectKey;//Object 格式为 年月/u+用户id+10位数字或字母+文件后缀
        String recordDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/oss_record/";

        File recordDir = new File(recordDirectory);

        // 要保证目录存在，如果不存在则主动创建
        if (!recordDir.exists()) {
            recordDir.mkdirs();
        }

        if (Utils.isNotEmpty(objectKey)) {
            tempObjectKey = objectKey;
        } else {
            tempObjectKey = testObject + "/" + "u" + AccountInfoManager.getInstance().getCurrentAccountUserIdStr()
                    + String.valueOf(System.currentTimeMillis() + fileName.hashCode()).substring(0, 10) + prefix;//Object 格式为 年月/u+用户id+10位数字或字母+文件后缀
        }

        Log.d(TAG, " bucketName=" + testBucket + ", testObject=" + testObject + ", tempObjectKey=" + tempObjectKey + ",path =" + uploadFilePath);

        // 保存ObjectKey
        setObjectKey(tempObjectKey);

        // 创建断点上传请求，参数中给出断点记录文件的保存位置，需是一个文件夹的绝对路径
        ResumableUploadRequest request = new ResumableUploadRequest(testBucket, tempObjectKey, uploadFilePath, recordDirectory);

        // 设置上传过程回调
        request.setProgressCallback(new OSSProgressCallback<ResumableUploadRequest>() {
            @Override
            public void onProgress(ResumableUploadRequest request, long currentSize, long totalSize) {

             //   Log.d("resumableUpload", "currentSize: " + currentSize + " totalSize: " + totalSize);
                if (uploadListener != null) {
                    uploadListener.onProgress(currentSize, totalSize);
                }
            }
        });

        OSSAsyncTask resumableTask = oss.asyncResumableUpload(request, new OSSCompletedCallback<ResumableUploadRequest, ResumableUploadResult>() {
            @Override
            public void onSuccess(ResumableUploadRequest request, ResumableUploadResult result) {
                if (uploadListener != null) {
                    uploadListener.onSuccess(request.getObjectKey());
                }
                Log.d("resumableUpload", "success=" + request.getObjectKey());
            }

            @Override
            public void onFailure(ResumableUploadRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 异常处理
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                    if (uploadListener != null) {
                        if (clientExcepion.isCanceledException())
                            uploadListener.onFailure("-1", "已取消上传");
                        else
                            uploadListener.onFailure("-2", "上传失败");
                    }
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                    if (uploadListener != null) {
                        uploadListener.onFailure(serviceException.getErrorCode(), serviceException.getRawMessage());
                    }
                }

            }
        });

       // resumableTask.waitUntilFinished();

        return resumableTask;

    }

    public String getTestBucket() {
        return testBucket;
    }

    public String getTestObject() {
        return testObject;
    }

    public String getUploadFilePath() {
        return uploadFilePath;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public String getObjectKey() {
        return objectKey;
    }

}
