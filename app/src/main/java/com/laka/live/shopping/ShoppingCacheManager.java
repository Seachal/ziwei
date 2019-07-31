package com.laka.live.shopping;

import android.content.Context;

import com.laka.live.shopping.system.SystemHelper;
import com.laka.live.shopping.utils.ACache;
import com.laka.live.util.StringUtils;


/**
 * Created by linhz on 2016/2/19.
 * Email: linhaizhong@ta2she.com
 */
public class ShoppingCacheManager {
    private static final String KEY_CATEGORY = "shopping_category_data";
    private static final String KEY_MAIN = "shopping_main_data";
    private static final String KEY_CATEGORY_GET_IT = "shopping_category_get_it_data";
    private static final String KEY_CATEGORY_PANEL_INIT = "shopping_category_panel_init";
    private static final String VERSION_TAG = "#version_tag#";

    private static final String COMPA_DATA_VERSION = "3.5.0";

    private static ACache mACache;


    private static void ensureCache(Context context) {
        if (mACache == null) {
            mACache = ACache.get(context);
        }
    }

    public static CacheData getMainData(Context context) {
        ensureCache(context);
        String data = mACache.getAsString(KEY_MAIN);
        if (StringUtils.isEmpty(data)) {
            return null;
        }
        CacheData cacheData = new CacheData();
        int index = data.indexOf(VERSION_TAG);
        if (index > 0) {
            cacheData.version = data.substring(0, index);
            if (!isDataVersionCompa(cacheData.version)) {
                return null;
            }
            cacheData.data = data.substring(index + VERSION_TAG.length());
        }
        return cacheData;
    }

    public static CacheData getCategoryGetItData(Context context) {
        ensureCache(context);
        String data = mACache.getAsString(KEY_CATEGORY_GET_IT);
        if (StringUtils.isEmpty(data)) {
            return null;
        }
        CacheData cacheData = new CacheData();
        int index = data.indexOf(VERSION_TAG);
        if (index > 0) {
            cacheData.version = data.substring(0, index);
            if (!isDataVersionCompa(cacheData.version)) {
                return null;
            }
            cacheData.data = data.substring(index + VERSION_TAG.length());
        }
        return cacheData;
    }

    public static void cacheCategoryGetItData(Context context, String data) {
        ensureCache(context);
        StringBuilder builder = new StringBuilder();
        builder.append(SystemHelper.getAppInfo().versionName);
        builder.append(VERSION_TAG);
        builder.append(data);
        mACache.put(KEY_CATEGORY_GET_IT, builder.toString());
    }

    public static boolean panelIsInit(Context context) {
        ensureCache(context);
        String data = mACache.getAsString(KEY_CATEGORY_PANEL_INIT);
        if (StringUtils.isEmpty(data)) {
            panelInit(context);
            return false;
        }
        CacheData cacheData = new CacheData();
        int index = data.indexOf(VERSION_TAG);
        if (index > 0) {
            cacheData.version = data.substring(0, index);
            if (!isDataVersionCompa(cacheData.version)) {
                return false;
            }
            cacheData.data = data.substring(index + VERSION_TAG.length());
        }
        return true;
    }

    public static void panelInit(Context context) {
        ensureCache(context);
        StringBuilder builder = new StringBuilder();
        builder.append(SystemHelper.getAppInfo().versionName);
        builder.append(VERSION_TAG);
        builder.append("init");
        mACache.put(KEY_CATEGORY_PANEL_INIT, builder.toString());
    }

    public static void cacheMainData(Context context, String data) {
        ensureCache(context);
        if (StringUtils.isEmpty(data)) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(SystemHelper.getAppInfo().versionName);
        builder.append(VERSION_TAG);
        builder.append(data);
        mACache.put(KEY_MAIN, builder.toString());
    }

    public static void clearCategoryCache(Context context) {
        ensureCache(context);
        mACache.remove(KEY_CATEGORY);
    }

    public static void clearMainCache(Context context) {
        ensureCache(context);
        mACache.remove(KEY_MAIN);
    }

    private static boolean isDataVersionCompa(String version) {
        return StringUtils.compareVersion(version, COMPA_DATA_VERSION) >= 0;
    }

    public static class CacheData {
        public String version;
        public String data;
    }
}
