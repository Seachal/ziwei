package com.laka.live.util;

import java.util.HashMap;

import org.json.JSONObject;

public class CacheUtil {

    private static HashMap<String, Object> cache = new HashMap<String, Object>();
    private static HashMap<String, JSONObject> jsonobjectCache = new HashMap<String, JSONObject>();

    public static void addCache(String dataKey, Object data) {
        if (cache.containsKey(dataKey)) {
            cache.remove(dataKey);
        }
        cache.put(dataKey, data);
    }

    public static Object getCache(String dataKey) {
        return cache.get(dataKey);
    }

    public static void removeCache(String dataKey) {
        cache.remove(dataKey);
    }

    public static boolean containsKey(String dataKey) {
        return cache.containsKey(dataKey);
    }

    public static boolean containAndNotNull(String dataKey) {
        return cache.containsKey(dataKey) && cache.get(dataKey) != null;
    }

    ///////////////////////////////////////////////////////////////////////////////////
    public static void addJsonCache(String dataKey, JSONObject data) {
        if (jsonobjectCache.containsKey(dataKey)) {
            jsonobjectCache.remove(dataKey);
        }
        jsonobjectCache.put(dataKey, data);
    }

    public static JSONObject getJsonCache(String dataKey) {
        return jsonobjectCache.get(dataKey);
    }

    public static boolean jsonNotNull(String dataKey) {
        return jsonobjectCache.containsKey(dataKey) && jsonobjectCache.get(dataKey) != null;
    }

    public static void clear() {
        cache.clear();
        jsonobjectCache.clear();
    }

    public static void clear(String dataKey) {
        if (cache.containsKey(dataKey)) {
            cache.remove(dataKey);
        }
        if (jsonobjectCache.containsKey(dataKey)) {
            jsonobjectCache.remove(dataKey);
        }
    }
}
