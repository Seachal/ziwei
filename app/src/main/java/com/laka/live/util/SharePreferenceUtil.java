package com.laka.live.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.laka.live.application.LiveApplication;
import com.orhanobut.logger.Logger;

/**
 * @Author:Rayman
 * @Date:2018/9/4
 * @Description:SharedPreferenceUtil工具类
 */

public class SharePreferenceUtil {

    private static final String APP_CONFIG = "App_config";

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public static void remarkStartCount() {
        int startCount = -1;
        String startNum = getInfo("startNum");
        if (!TextUtils.isEmpty(startNum)) {
            startCount = Integer.valueOf(startNum);
        }
        startCount++;
        putInfo("startNum", startCount + "");
        Logger.e("记录当前启动次数：" + startCount);
    }

    public static boolean isFirstStart() {
        int startCount = 0;
        String startNum = getInfo("startNum");
        if (!TextUtils.isEmpty(startNum)) {
            startCount = Integer.valueOf(startNum);
        }
        boolean result = startCount == 0;
        Logger.e("是否第一次启动App：" + result);
        return result;
    }


    /**
     * 存储临时数据---默认存储到config下面
     *
     * @param key
     * @param value
     */
    public static void putInfo(String key, String value) {
        putInfo(APP_CONFIG, key, value);
    }

    /**
     * 保存数据到指定的sp中
     *
     * @param spName
     * @param key
     * @param value
     */
    public static void putInfo(String spName, String key, String value) {
        sharedPreferences = LiveApplication.getInstance().getSharedPreferences(spName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 获取
     *
     * @param key
     * @return
     */
    public static String getInfo(String key) {
        return getInfo(APP_CONFIG, key);
    }

    /**
     * 从指定的sp中根据key获取value
     *
     * @param spName
     * @param key
     * @return
     */
    public static String getInfo(String spName, String key) {
        sharedPreferences = LiveApplication.getInstance().getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    /**
     * 清除Config相关配置
     */
    public static void clearConfigInfo() {
        clearInfo(APP_CONFIG);
    }

    /**
     * 清除指定sp的信息
     *
     * @param spName
     */
    public static void clearInfo(String spName) {
        sharedPreferences = LiveApplication.getInstance().getSharedPreferences(spName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
