package com.laka.live.network;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

/**
 * Created by luyi on 2015/7/8.
 */
public class MultipartRequest<T> extends GsonRequest<T> {


    private MultipartEntity mEntity;

    public MultipartRequest(String url, GsonHttpConnection.OnResultListener listener, MultipartEntity entity, Gson gson, Type type) throws FileNotFoundException {
        super(Method.POST, url, listener, gson, type);

        setRetryPolicy(new DefaultRetryPolicy(120000, 2, 2));
        mEntity = entity;

    }

    @Override
    protected Map<String, String> getParams() {
        return null;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return Collections.emptyMap();
    }

    @Override
    public String getBodyContentType() {
        return mEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mEntity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

}
