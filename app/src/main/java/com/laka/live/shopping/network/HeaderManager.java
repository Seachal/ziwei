package com.laka.live.shopping.network;



import com.laka.live.account.AccountInfoManager;
import com.laka.live.shopping.common.CommonConst;
import com.laka.live.shopping.system.AppInfo;
import com.laka.live.shopping.system.DeviceInfo;
import com.laka.live.shopping.system.SystemHelper;

import java.util.HashMap;

/**
 * Created by zhxu on 2015/11/25.
 * Email:357599859@qq.com
 */
public class HeaderManager {
    public final static String OS = "os";
    public final static String OSV = "osv";
    public final static String CH = "ch";
    public final static String VER = "ver";
    public final static String TOKEN = "token";
    public final static String UUID = "uuid";
    public final static String APPBUILD = "appbuild";
    public final static String NET = "net";
    public final static String AC = "ac";
    public final static String MA = "ma";
    public final static String T = "t";
    public final static String IM = "im";
    public final static String DEVID = "devid";
    public final static String DEVNAME = "devna";
    public final static String API_VERSION = "apiver";
    public final static String DEV_TOKEN = "devtoken";

    public static HashMap<String, String> defaultHeader() {
        HashMap<String, String> headerMap = new HashMap<>();
        AppInfo appInfo = SystemHelper.getAppInfo();
        DeviceInfo deviceInfo = SystemHelper.getDeviceInfo();
        String token = AccountInfoManager.getInstance().getToken();
        if (token == null) {
            token = "";
        }
        headerMap.put(OS, "android");
        headerMap.put(APPBUILD, String.valueOf(appInfo.versionCode));
        headerMap.put(OSV, deviceInfo.sdkVersionName);
        headerMap.put(CH, appInfo.marketChannel);
        headerMap.put(VER, appInfo.versionName);
        headerMap.put(TOKEN, token);
        headerMap.put(UUID, deviceInfo.uuid);
        headerMap.put(AC, "");
        headerMap.put(MA, deviceInfo.macAdr);
        headerMap.put(T, String.valueOf(System.currentTimeMillis()));
        headerMap.put(IM, deviceInfo.imei);
        headerMap.put(DEVID, deviceInfo.deviceId);
        headerMap.put(DEVNAME, deviceInfo.deviceName);
        headerMap.put(API_VERSION, CommonConst.API_VERSION);
        String devToken =deviceInfo.devToken;
        if (devToken == null) {
            devToken = "";
        }
        headerMap.put(DEV_TOKEN, devToken);
        return headerMap;
    }

}
