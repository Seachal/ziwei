package com.laka.live.util;

import android.graphics.Typeface;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by luwies on 16/9/20.
 */
public class TypefaceHelper {

    public static final String TYPE_FACE_Georgia_Bold = "TYPE_FACE_Georgia_Bold";
    public static final String TYPE_FACE_AFFOGATO_BOLD = "TYPE_FACE_FFOGATO_BOLD";
    public static final String TYPE_FACE_BLACK_JACK = "TYPE_FACE_BLACK_JACK";

    private static TypefaceHelper sInstance;

    private Map<String, Typeface> mTypefaceMap;

    private TypefaceHelper() {
        mTypefaceMap = new LinkedHashMap<>();
    }

    public static TypefaceHelper getInstance() {
        if (sInstance == null) {
            synchronized (TypefaceHelper.class) {
                if (sInstance == null) {
                    sInstance = new TypefaceHelper();
                }
            }
        }
        return sInstance;
    }

    public Typeface getTypeface(String key) {
        return mTypefaceMap.get(key);
    }

    public Typeface putTypeface(String key, Typeface typeface) {
        return mTypefaceMap.put(key, typeface);
    }
}
