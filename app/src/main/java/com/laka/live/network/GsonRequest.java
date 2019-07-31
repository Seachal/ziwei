package com.laka.live.network;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.application.LiveApplication;
import com.laka.live.cache.PostCache;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.Msg;
import com.laka.live.util.Common;
import com.laka.live.util.Log;
import com.laka.live.util.Utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luyi on 2015/8/20.
 * 使用gson解析数据
 */
public class GsonRequest<T> extends Request<T> {

    private static final String TAG = "GsonRequest";

    protected static final String PROTOCOL_CHARSET = "utf-8";

    private static final int DEFAULT_TIMEOUT_MS = 5000;

    private static final int DEFAULT_MAX_RETRIES = 1;

    /**
     * The default backoff multiplier
     */
    private static final float DEFAULT_BACKOFF_MULT = 1f;

    private GsonHttpConnection.OnResultListener<T> mListener;

    private Gson mGson;

    private Type mType;

    private Map<String, String> mPost;

    private boolean isForce = false;

    public GsonRequest(String url, GsonHttpConnection.OnResultListener<T> listener, Gson gson, Type type) {
        this(Method.GET, url, listener, gson, type);
    }

    public GsonRequest(int method, String url, GsonHttpConnection.OnResultListener<T> listener, Gson gson, Type type) {
        this(method, url, listener, gson, type, null);
    }

    public GsonRequest(final int method, final String url, final GsonHttpConnection.OnResultListener<T> listener, Gson gson, Type type,
                       Map<String, String> post) {
        this(method, url, listener, gson, type, post, false);
    }

    public GsonRequest(final int method, final String url, final GsonHttpConnection.OnResultListener<T> listener, Gson gson, Type type,
                       final Map<String, String> post, final boolean isForce) {
        super(method, url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.error(TAG, "onErrorResponse : ", error);
                if (error != null && error.networkResponse != null) {
                    Log.error(TAG, "error code : " + error.networkResponse.statusCode);
                }
                URL url1 = null;
                String name = "";
                try {
                    url1 = new URL(url);
                    File file = new File(url1.getFile());
                    name = file.getName();
                } catch (MalformedURLException e) {

                }

                if (listener != null) {
                    if (error instanceof ParseError) {
                        listener.onFail(Msg.NETWORK_ERROR_PARSE_ERROR,
                                LiveApplication.application.getString(R.string.parse_error_tips), name);
                    } else {
                        listener.onFail(Msg.NETWORK_ERROR_NETWORK_ERROR,
                                LiveApplication.application.getString(R.string.network_error_tips), name);
                    }
                }

                Map<String, String> map = new HashMap<>();
                Throwable cause = error.getCause();
                cause = cause == null ? error : cause;
                map.put(Common.ERROE_CODE, cause.getClass().getSimpleName());
                AnalyticsReport.onEvent(LiveApplication.application, AnalyticsReport.PHP_REQUEST_FAIL_EVENT_ID, map);

                Log.error(TAG, "error url : " + url);
                if (isForce) {
                    PostCache.Entry entry = new PostCache.Entry();
                    String key = new StringBuilder(url).append(System.currentTimeMillis()).toString();
                    entry.method = method;
                    entry.requestUrl = url;
                    entry.postData = post;
                    GsonHttpConnection.getInstance().savePostCache(key, entry);
                }
            }
        });

        setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT));
        mListener = listener;

        mGson = gson;

        mType = type;

        mPost = post;

        this.isForce = isForce;
    }

    @Override
    protected void deliverResponse(T response) {
        boolean isSuccess;
        Msg msg = null;
        if (response != null && response instanceof Msg) {
            msg = (Msg) response;
            isSuccess = msg.isSuccessFul();
        } else {
            isSuccess = false;
            if (isForce) {
                PostCache.Entry entry = new PostCache.Entry();
                String url = getUrl();
                String key = new StringBuilder(url).append(System.currentTimeMillis()).toString();
                entry.method = getMethod();
                entry.requestUrl = url;
                entry.postData = mPost;
                GsonHttpConnection.getInstance().savePostCache(key, entry);
            }
        }
        if (mListener != null) {
            if (isSuccess) {
                mListener.onSuccess(response);
            } else {
                if (msg == null) {
                    mListener.onFail(Msg.NETWORK_ERROR_NETWORK_ERROR,
                            LiveApplication.application.getString(R.string.network_error_tips), "msg is null");
                } else {
                    mListener.onFail(msg.getCode(), msg.getError(), msg.getCommand());

                    int code = msg.getCode();
                    //  未登录向发送未登录事件
                    if (code == Msg.TLV_USER_NOT_LOGIN || code == Msg.TLV_WRONG_USER_TOEKN
                            || code == Msg.TLV_USER_TOKEN_EXPIRE) {
                        EventBusManager.postEvent(0, SubcriberTag.UNLOGIN);
                    }
                }
            }
        }

    }


    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        String jsonString = "";
        try {
            jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

            Log.debug(TAG, "jsonString : " + jsonString);

            if (mGson == null) {
                mGson = new Gson();
            }
            Object result = mGson.fromJson(jsonString, mType);

            if (result instanceof Msg) {
                ((Msg) result).parase();

            }

            return Response.success(result,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {

            Log.error(TAG, "JsonSyntaxException : ", e);

            /*try {
                Object result = parseJsonSyntaxException(jsonString);
                return Response.success(result,
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (JsonSyntaxException e1) {

            }*/

            return Response.error(new ParseError(e));
        }
    }

    /*private Object parseJsonSyntaxException(String json) {

        Msg msg = mGson.fromJson(json, Msg.class);

        return msg;
    }*/

    @Override
    protected Map<String, String> getParams() {
        return mPost;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<>();
        params.put("Content-Type", "application/x-www-form-urlencoded");
        addPublicHeader(params);
        return params;
    }

    @Override
    public String getCacheKey() {
        StringBuilder sb = new StringBuilder();
        sb.append(getMethod());
        sb.append(":");
        sb.append(getUrl());
        sb.append(":");
        String paramUrl = GsonHttpConnection.packRequestURL(getParams());
        sb.append(paramUrl);
        return sb.toString();
    }

    public static void addPublicHeader(Map<String, String> params) {
        if (params != null) {
            params.put(Common.PLATFORM, Common.ANDROID);
            params.put(Common.VERSION, String.valueOf(Utils.getVersionCode(LiveApplication.getInstance())));
        }
    }
}
