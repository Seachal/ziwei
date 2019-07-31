package com.laka.live.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luyi on 2015/7/3.
 */
public class PostRequest extends Request<JSONObject> {

    protected static final String PROTOCOL_CHARSET = "utf-8";

    private Map<String, String> mParams;

    private final Response.Listener<JSONObject> mListener;

    public PostRequest(String url, Map<String, String> params, Response.Listener<JSONObject> listener,
                       Response.ErrorListener errorListener) {
        this(Method.POST, url, params, listener, errorListener);
    }

    public PostRequest(int method, String url, Map<String, String> params, Response.Listener<JSONObject> listener,
                       Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mParams = params;
        mListener = listener;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String,String> params = new HashMap<String, String>();
        params.put("Content-Type","application/x-www-form-urlencoded");
        return params;
    }
}
