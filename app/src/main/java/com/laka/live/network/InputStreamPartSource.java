package com.laka.live.network;

import com.laka.live.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by luyi on 2015/7/8.
 */
public class InputStreamPartSource implements PartSource {

    private String mName;

    private InputStream mInputStream;


    public InputStreamPartSource(String name, InputStream inputStream) {
        mName = name;
        mInputStream = inputStream;
    }

    @Override
    public long getLength() {
        if(mInputStream == null) {
            return 0;
        }
        try {
            return mInputStream.available();
        } catch (IOException e) {
            Log.error("test", "ERROR", e);
        }
        return 0;
    }

    @Override
    public String getFileName() {
        return mName;
    }

    @Override
    public InputStream createInputStream() throws IOException {
        return mInputStream;
    }
}
