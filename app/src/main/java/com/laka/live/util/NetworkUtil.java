package com.laka.live.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by luwies on 16/5/30.
 */
public class NetworkUtil {

    /**
     * 当前网络状态为：无网络
     */
    public static final int NETWORK_STATE_NONE = 0;
    /**
     * 当前网络状态为：wifi
     */
    public static final int NETWORK_STATE_WIFI = 1;
    /**
     * 当前网络状态为：2g
     */
    public static final int NETWORK_STATE_2G = 2;
    /**
     * 当前网络状态为：3g
     */
    public static final int NETWORK_STATE_3G = 3;
    /**
     * 当前网络状态为：4g
     */
    public static final int NETWORK_STATE_4G = 4;
    /**
     * 当前网络状态为：未知网络
     */
    public static final int NETWORK_STATE_UNKNOW = 5;

    /**
     * 获取当前网络状态
     * @param context
     * @return
     *  {@link NetworkUtil#NETWORK_STATE_NONE},
     *  {@link NetworkUtil#NETWORK_STATE_WIFI},
     *  {@link NetworkUtil#NETWORK_STATE_2G},
     *  {@link NetworkUtil#NETWORK_STATE_3G},
     *  {@link NetworkUtil#NETWORK_STATE_4G},
     *  {@link NetworkUtil#NETWORK_STATE_UNKNOW}
     */
    public static int getNetworkState(Context context) {
        if (context == null) {
            return NETWORK_STATE_UNKNOW;
        }
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = connManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            switch (netInfo.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    return NETWORK_STATE_WIFI;
                case ConnectivityManager.TYPE_MOBILE:
                    switch (netInfo.getSubtype()) {
                        case TelephonyManager.NETWORK_TYPE_GPRS: //联通2g
                        case TelephonyManager.NETWORK_TYPE_CDMA: //电信2g
                        case TelephonyManager.NETWORK_TYPE_EDGE: //移动2g
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager. NETWORK_TYPE_IDEN:
                            return NETWORK_STATE_2G;
                        case TelephonyManager.NETWORK_TYPE_EVDO_A: //电信3g
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            return NETWORK_STATE_3G;
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            return NETWORK_STATE_4G;
                        default:
                            return NETWORK_STATE_UNKNOW;
                    }

                default:
                    return NETWORK_STATE_UNKNOW;
            }
        }
        return NETWORK_STATE_NONE;
    }

    /**
     * 当前网络是否为WiFi
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        return getNetworkState(context) == NETWORK_STATE_WIFI;
    }

    /**
     * 当前网络是否为移动网络
     * @param context
     * @return
     */
    public static boolean isMobile(Context context) {
        return getNetworkState(context) == NETWORK_STATE_2G || getNetworkState(context) == NETWORK_STATE_3G || getNetworkState(context) == NETWORK_STATE_4G;
    }

    public static boolean isNetworkOk(Context context) {
        return getNetworkState(context) != NETWORK_STATE_NONE;
    }


}
