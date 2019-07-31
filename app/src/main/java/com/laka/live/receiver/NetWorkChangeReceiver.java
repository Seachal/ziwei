package com.laka.live.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.util.NetworkUtil;
import com.orhanobut.logger.Logger;

/**
 * @Author:Rayman
 * @Date:2018/8/14
 * @Description:网络监听
 */

public class NetWorkChangeReceiver extends BroadcastReceiver {

    private NetworkInfo wifiNetworkInfo;
    private NetworkInfo mobileNetWorkInfo;

    //因为网络状态的切换，第一次进入app也会回调一次无用的，这里做过来
    private boolean isFirstNetWorkInit = true;


    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        mobileNetWorkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        handleNetWorkChangeEvent(context, wifiNetworkInfo, mobileNetWorkInfo);
    }

    private void handleNetWorkChangeEvent(Context context, NetworkInfo wifiNetworkInfo, NetworkInfo mobileNetWorkInfo) {
        boolean isWifiConnected = NetworkUtil.isWifi(context);
        boolean isMobileConnected = NetworkUtil.isMobile(context);
        if (wifiNetworkInfo != null) {
            isWifiConnected = wifiNetworkInfo.isConnected();
        }

        if (mobileNetWorkInfo != null) {
            isMobileConnected = mobileNetWorkInfo.isConnected();
        }

//        Logger.e("WIFI开启？" + isWifiConnected + "\nMobile开启？" + isMobileConnected);

        if (!isFirstNetWorkInit) {
            if (isWifiConnected && isMobileConnected) { // wifi和移动数据都开启状态
                EventBusManager.postEvent(null, SubcriberTag.CONNECTIVE_WIFI_AND_MOBILE);
            } else if (isWifiConnected && !isMobileConnected) { // wifi
                EventBusManager.postEvent(null, SubcriberTag.CONNECTIVE_WIFI);
            } else if (!wifiNetworkInfo.isConnected() && isMobileConnected) { // 移动数据
                EventBusManager.postEvent(null, SubcriberTag.CONNECTIVE_MOBILE);
            } else { // 其他
                EventBusManager.postEvent(null, SubcriberTag.DISCONNECT_WIFI_AND_MOBILE);
            }
        }
        isFirstNetWorkInit = false;
    }
}