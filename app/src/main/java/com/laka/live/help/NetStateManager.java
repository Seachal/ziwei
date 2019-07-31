package com.laka.live.help;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.laka.live.R;
import com.laka.live.observer.NetworkChangeObserver;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.widget.dialog.GenericDialog;
import com.laka.live.ui.widget.dialog.IDialogOnClickListener;
import com.laka.live.ui.widget.dialog.SimpleTextDialog;
import com.laka.live.util.Log;
import com.laka.live.util.NetworkUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by ios on 16/7/28.
 */
public class NetStateManager {
    private static final String TAG = "NetStateManager";
    private boolean isAlreadyNotify = false;//是否已提醒
    static NetStateManager self;

    public static NetStateManager getInstance() {
        if (self == null) {
            self = new NetStateManager();
        }
        return self;
    }

    public void start() {
        NetworkChangeObserver.getInstance().registListener(networkChangeListener);
    }

    public void stop() {
        NetworkChangeObserver.getInstance().unRegistListener(networkChangeListener);
    }

    public boolean getIsWifiNow(Context context) {
        int netWorkState = NetworkUtil.getNetworkState(context);
        return checkIsWifiWork(netWorkState);
    }

    public boolean getIsNetworkOk(Context context) {
        int netWorkState = NetworkUtil.getNetworkState(context);
        return checkIsNetWorkOk(netWorkState);
    }

    public boolean getIs2G(Context context) {
        int netWorkState = NetworkUtil.getNetworkState(context);
        switch (netWorkState) {
            case NetworkUtil.NETWORK_STATE_2G:
                Log.d(TAG, "当前2g网络");
                return true;
        }
        return false;
    }

    public boolean getIs3G(Context context) {
        int netWorkState = NetworkUtil.getNetworkState(context);
        switch (netWorkState) {
            case NetworkUtil.NETWORK_STATE_3G:
                Log.d(TAG, "当前3g网络");
                return true;
        }
        return false;
    }

    public boolean getIsAlreadyNotify() {
        return isAlreadyNotify;
    }

    SimpleTextDialog mobileNetWorkDialog;
    public void showMobileNetWorkDialog(Activity activity, int textId, int yesTextId, int noTextId, IDialogOnClickListener listener) {
        if(mobileNetWorkDialog!=null&&mobileNetWorkDialog.isShowing()){
            mobileNetWorkDialog.dismiss();
        }
        mobileNetWorkDialog = new SimpleTextDialog(activity);
        mobileNetWorkDialog.setTitle(ResourceHelper.getString(R.string.network_notice));
        mobileNetWorkDialog.setText(ResourceHelper.getString(textId));
        mobileNetWorkDialog.addYesNoButton(ResourceHelper.getString(yesTextId),
                ResourceHelper.getString(noTextId));
        mobileNetWorkDialog.setCanceledOnTouchOutside(true);
        mobileNetWorkDialog.setRecommendButton(GenericDialog.ID_BUTTON_NO);
        mobileNetWorkDialog.setOnClickListener(listener);
//        new IDialogOnClickListener() {
//            @Override
//            public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
//                if (viewId == GenericDialog.ID_BUTTON_YES) {
//                }
//                return false;
//            }
//        };
        if (!mobileNetWorkDialog.isShowing()) {
            mobileNetWorkDialog.show();
        }
    }

    private boolean checkIsNetWorkOk(int netWorkState) {
        switch (netWorkState) {
            case NetworkUtil.NETWORK_STATE_2G:
            case NetworkUtil.NETWORK_STATE_3G:
            case NetworkUtil.NETWORK_STATE_4G:
            case NetworkUtil.NETWORK_STATE_WIFI:
                Log.d(TAG, "当前有网络");
                return true;
        }
        return false;
    }

    private boolean checkIsWifiWork(int netWorkState) {
        switch (netWorkState) {
            case NetworkUtil.NETWORK_STATE_WIFI:
                Log.d(TAG, "当前是wifi网络");
                return true;
        }
        return false;
    }

    private boolean checkIsMoblieNetWork(int netWorkState) {
        switch (netWorkState) {
            case NetworkUtil.NETWORK_STATE_2G:
            case NetworkUtil.NETWORK_STATE_3G:
            case NetworkUtil.NETWORK_STATE_4G:
                Log.d(TAG, "当前是移动网络");
                return true;
        }
        return false;
    }

    int lastNetWorkState;
    boolean isNoNetwork = false;
    NetworkChangeObserver.NetworkChangeListener networkChangeListener = new NetworkChangeObserver.NetworkChangeListener() {
        @Override
        public void onNetworkChange(int state) {
            Log.d(TAG, " onNetworkChange lastNetWorkState=" + lastNetWorkState + " 当前state=" + state);
            if (state == NetworkUtil.NETWORK_STATE_UNKNOW || state == NetworkUtil.NETWORK_STATE_NONE) {
                isNoNetwork = true;
                if(lastNetWorkState!=NetworkUtil.NETWORK_STATE_UNKNOW &&lastNetWorkState != NetworkUtil.NETWORK_STATE_NONE)
                EventBusManager.postEvent(0, SubcriberTag.HAS_NET_TO_NON_NETWORK);
                return;
            }
            //从wifi切换移动
            if (lastNetWorkState == NetworkUtil.NETWORK_STATE_WIFI && checkIsMoblieNetWork(state)) {
//                showMobileNetWorkDialog();
                Log.d(TAG, "wifi切换到移动网络 isAlreadyNotify=" + isAlreadyNotify);
                if (!isAlreadyNotify) {
                    isAlreadyNotify = true;
                }
                EventBusManager.postEvent(0, SubcriberTag.WIFI_CHANGE_MOBILE);
            }
            if (isNoNetwork && (checkIsMoblieNetWork(state) || checkIsWifiWork(state))) {
                isNoNetwork = false;
                EventBusManager.postEvent(0, SubcriberTag.NON_NETWORK_TO_HAS_NET);
            }
            lastNetWorkState = state;
        }
    };

    public  void goToNetSetting(Context context) {
        try{
            Intent intent = null;
            // 先判断当前系统版本
            if(android.os.Build.VERSION.SDK_INT > 10){  // 3.0以上
                intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
            }else{
                intent = new Intent();
                intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
            }
            context.startActivity(intent);
        }catch (Exception e){
            ToastHelper.showToast(R.string.please_set_in_system_setting);
        }

    }
}
