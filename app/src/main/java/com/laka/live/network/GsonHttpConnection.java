package com.laka.live.network;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.application.LiveApplication;
import com.laka.live.cache.PostCache;
import com.laka.live.cache.PostDiskBasedPostCache;
import com.laka.live.thread.BackgroundThread;
import com.laka.live.util.ChannelUtil;
import com.laka.live.util.Common;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.Util;
import com.laka.live.util.Utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luyi on 2015/8/20.
 */
public class GsonHttpConnection {

    private static final String TAG = "GsonHttpConnection";

    private static final String DEFAULT_CACHE_DIR = "postCache";


    /**
     * 单例
     */
    private static GsonHttpConnection mInstance;
    /**
     * 用于自动解析数据
     */
    private Gson mGson;

    private PostDiskBasedPostCache mDiskCache;

    private GsonHttpConnection() {
//        mGson = new Gson();
        mGson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .setDateFormat(DateFormat.LONG)
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setVersion(1.0)
                .create();

        BackgroundThread.post(new Runnable() {
            @Override
            public void run() {
                File cacheDir = new File(LiveApplication.getInstance().getCacheDir(), DEFAULT_CACHE_DIR);
                mDiskCache = new PostDiskBasedPostCache(cacheDir);
                mDiskCache.initialize();
            }
        });
    }

    public static GsonHttpConnection getInstance() {
        if (mInstance == null) {
            synchronized (GsonHttpConnection.class) {
                if (mInstance == null) {
                    mInstance = new GsonHttpConnection();
                }
            }
        }
        return mInstance;
    }

    public Gson getGson() {
        return mGson;
    }

    /**
     * GET方式去请求数据
     *
     * @param tag
     * @param url
     * @param type
     * @param listener
     * @param <T>
     * @return
     */
    public <T> String get(Object tag, String url, Type type, OnResultListener<T> listener) {
        return get(tag, url, null, type, listener);
    }

    /**
     * GET请求
     *
     * @param tag
     * @param url
     * @param urlParam
     * @param type
     * @param listener
     * @param <T>
     * @return
     */
    public <T> String get(Object tag, String url, Map<String, String> urlParam, Type type, OnResultListener<T> listener) {
        return sendRequest(Request.Method.GET, tag, url, urlParam, null, type, listener, false);
    }

    /**
     * POST请求
     *
     * @param tag
     * @param url
     * @param postParam
     * @param type
     * @param listener
     * @param <T>
     * @return
     */
    public <T> String post(Object tag, String url, Map<String, String> postParam, Type type, OnResultListener<T> listener) {
        return post(tag, url, null, postParam, type, listener);
    }

    public <T> String post(Object tag, String url, Map<String, String> urlParam, Map<String, String> postParam, Type type, OnResultListener<T> listener) {
        return post(tag, url, urlParam, postParam, type, listener, false);
    }

    public <T> String post(Object tag, String url, Map<String, String> urlParam, Map<String, String> postParam, Type type, OnResultListener<T> listener,
                           boolean isForce) {
        return sendRequest(Request.Method.POST, tag, url, urlParam, postParam, type, listener, isForce);
    }

    public <T> String put(Object tag, String url, Map<String, String> postParam, Type type, OnResultListener<T> listener) {
        return put(tag, url, null, postParam, type, listener);
    }

    public <T> String put(Object tag, String url, Map<String, String> urlParam, Map<String, String> postParam, Type type, OnResultListener<T> listener) {
        return put(tag, url, urlParam, postParam, type, listener, false);
    }

    public <T> String put(Object tag, String url, Map<String, String> urlParam, Map<String, String> postParam, Type type, OnResultListener<T> listener
            , boolean isForce) {
        return sendRequest(Request.Method.PUT, tag, url, urlParam, postParam, type, listener, isForce);
    }

    public <T> String delete(Object tag, String url, Map<String, String> postParam, Type type, OnResultListener<T> listener) {
        return delete(tag, url, null, postParam, type, listener);
    }

    public <T> String delete(Object tag, String url, Map<String, String> urlParam, Map<String, String> postParam, Type type, OnResultListener<T> listener) {
        return delete(tag, url, urlParam, postParam, type, listener, false);
    }

    public <T> String delete(Object tag, String url, Map<String, String> urlParam, Map<String, String> postParam, Type type, OnResultListener<T> listener
            , boolean isForce) {
        return sendRequest(Request.Method.DELETE, tag, url, urlParam, postParam, type, listener, isForce);
    }

    public boolean upLoadImage(Object tag, File image, OnResultListener listener)
            throws FileNotFoundException {
        byte cmp[] = ImageUtil.compressUploadBitmap(image);
        if (cmp == null) {
            return upLoadFile(tag, image, listener);
        }
        InputStream inputStream = new ByteArrayInputStream(cmp);
        return upLoadFile(tag, image.getName(), inputStream, listener);
    }

    /**
     * 后台只支持 图片上传, 后缀名必须为图片格式
     *
     * @param fileName
     * @param inputStream
     * @param listener
     * @throws FileNotFoundException
     */
    public boolean upLoadFile(Object tag, String fileName, InputStream inputStream, OnResultListener listener)
            throws FileNotFoundException {
        PartSource partSource = new InputStreamPartSource(fileName, inputStream);
        return upLoadFile(tag, partSource, listener);
    }

    /**
     * 后台只支持 图片上传, 后缀名必须为图片格式
     *
     * @param file
     * @param listener
     * @return true 已经发送上传request
     */
    public boolean upLoadFile(Object tag, File file, OnResultListener listener) throws FileNotFoundException {
        PartSource partSource = new FilePartSource(file);
        return upLoadFile(tag, partSource, listener);
    }

    public boolean upLoadFile(Object tag, PartSource partSource, OnResultListener listener) throws FileNotFoundException {
        /*User user = UserInfoManager.getInstance().getCurrentUser();
        if (user == null) {
            return false;
        }*/
/*        StringPart memberIdPart = new StringPart("member_id", String.valueOf(user.getId()));
        StringPart tokenPart = new StringPart("token", user.getToken());*/

        /*StringPart verPart = new StringPart("v", MXHttpConnection.HTTP_VERSION);
        StringPart signPart = new StringPart("sign", MXHttpConnection.generateSign(Common.KEY, "v=" + MXHttpConnection.HTTP_VERSION));
        FilePart filePart = new FilePart("file", partSource);

        Part[] parts = {*//*memberIdPart, tokenPart, *//*verPart, signPart, filePart};
        MultipartEntity entity = new MultipartEntity(parts);

        MXMultipartRequest request = new MXMultipartRequest(Common.URL_UPLOAD, listener,
                entity, mGson, new TypeToken<SingleMsg<MXUploadResult>>() {
        }.getType());
        request.setTag(tag);
        LiveApplication.mQueue.add(request);*/


        return true;
    }


    /**
     * @param method   HTTP请求方式s
     * @param tag      设置一个tag用于取消这个网络请求
     * @param url
     * @param urlParam url附带的参数
     * @param postData post参数
     * @param type     返回数据的Type,例如 new TypeToken<List<Item>>(){}.getType();返回一个列表数据
     * @param listener 请求结果的回调
     * @param <T>
     * @return
     */
    public <T> String sendRequest(int method, Object tag, String url, Map<String, String> urlParam, Map<String, String> postData,
                                  Type type, OnResultListener<T> listener, boolean isForce) {

        String requestUrl;
        String version = String.valueOf(ChannelUtil.getVersionCode(LiveApplication.getInstance()));
        String platForm = Common.PLATFORM_ANDROID;
        String channel =ChannelUtil.getChannel(LiveApplication.getInstance());
        Log.d(TAG," version="+version+" platForm="+platForm+" channel="+channel);
        if(postData==null){
            postData = new HashMap<>();
        }
        postData.put(Common.PLATFORM,platForm);
        postData.put(Common.VERSION, version);
        postData.put(Common.CHANNEL,channel);

        if(urlParam==null){
            urlParam = new HashMap<>();
        }
        urlParam.put(Common.PLATFORM,platForm);
        urlParam.put(Common.VERSION, version);
        urlParam.put(Common.CHANNEL,channel);

        if (method == Request.Method.GET) {
            if (urlParam == null) {
                urlParam = new HashMap<>();
            }

            StringBuilder sb = new StringBuilder(url);

            // 如果有含？拼参时就直接加&
            if (sb.toString().contains("?"))
                sb.append("&");
            else
                sb.append("?");

            String paramUrl = packRequestURL(urlParam);
            sb.append(paramUrl);
            requestUrl = sb.toString();
        } else {
            requestUrl = url;
            if (postData == null) {
                postData = new HashMap<>();
            }
        }

        Log.info(TAG, "[Send Request]: " + requestUrl);
        Log.info(TAG, "Http Method : " + method);
        Log.info(TAG, "[PostData]: " + postData);

        GsonRequest<T> request = new GsonRequest<>(method, requestUrl, listener, mGson, type, postData, isForce);
        request.setTag(tag);
        //设置超时15s
        request.setRetryPolicy(new DefaultRetryPolicy(15 * 1000, 1, 1.0f));
        LiveApplication.mQueue.add(request);

        return requestUrl;
    }

    public void sendRequest(GsonRequest request) {
        LiveApplication.mQueue.add(request);
    }

    public interface OnResultListener<T> {

        void onSuccess(T t);

        /**
         * 网络错误的时候会回调这个方法
         *
         * @param errorCode
         * @param errorMsg
         * @param command
         */
        void onFail(int errorCode, String errorMsg, String command);
    }

    /**
     * 组合Http Get请求URL
     *
     * @param paramMap 请求参数
     * @return
     */
    public static String packRequestURL(Map<String, String> paramMap) {
        String requestString = "";
        if (paramMap != null) {
            Object[] keys = paramMap.keySet().toArray();
            Arrays.sort(keys);
            int index = 0;
            for (Object key : keys) {
                String val = paramMap.get(key) + "";
                requestString += key + "=" + val;
                index++;

                if (index != keys.length)
                    requestString += "&";
            }
        }

        Log.debug(TAG, "url_log:" + requestString);
        return requestString;
    }

    public void savePostCache(final String key, final PostCache.Entry entry) {
        if (mDiskCache != null) {
            BackgroundThread.post(new Runnable() {
                @Override
                public void run() {
                    mDiskCache.put(key, entry);
                }
            });
        }
    }

    public void postCache() {
        if (mDiskCache != null) {
            mDiskCache.postAll();
        }
    }

    public static String generateSign(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        if (params != null) {
            Object[] keys = params.keySet().toArray();
            Arrays.sort(keys);

            int index = 0;
            for (Object key : keys) {
                String val = params.get(key);
                sb.append(val);
                index++;

                if (index != keys.length)
                    sb.append("&");
            }
        }

        String result = sb.toString();

        Log.error(TAG, "generateSign : " + result);
        if (result == null) {
            return result;
        }

        return Util.md5(result + AnalyticsReport.KEY);
    }

}
