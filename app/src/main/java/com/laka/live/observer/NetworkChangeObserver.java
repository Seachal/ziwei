package com.laka.live.observer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.laka.live.application.LiveApplication;
import com.laka.live.util.NetworkUtil;

import java.util.ArrayList;

/**
 * Created by luwies on 16/5/30.
 */
public class NetworkChangeObserver {

    /**
     * 当前类的单例对象
     */
    private static NetworkChangeObserver mInstance;

    /**
     * 需要监听网络变化的监听者
     */
    private ArrayList<NetworkChangeListener> mListeners;
    /**
     * 广播接收器
     */
    private NetworkBroadcastReceiver mReceiver;

    private NetworkChangeObserver() {
        mListeners = new ArrayList<NetworkChangeObserver.NetworkChangeListener>();
    }

    /**
     * 获取单例对象
     *
     * @return
     */
    public static NetworkChangeObserver getInstance() {
        if (mInstance == null) {
            synchronized (NetworkChangeObserver.class) {
                if (mInstance == null) {
                    mInstance = new NetworkChangeObserver();
                }
            }
        }
        return mInstance;
    }

    /**
     * 注册监听
     *
     * @param listener
     */
    public void registListener(NetworkChangeListener listener) {
        if (listener != null) {
            mListeners.add(listener);

            if (mListeners.size() == 1) {
                // 注册广播
                registBroadcastReceiver();
            }
        }
    }

    /**
     * 反注册监听
     *
     * @param listener
     */
    public void unRegistListener(NetworkChangeListener listener) {
        if (listener != null) {
            mListeners.remove(listener);
            if (mListeners.isEmpty() == true) {
                // 反注册广播
                unRegistBroadcastReceiver();
            }
        }
    }

    /**
     * 通知网络发生变化
     */
    private void notifyNetworkChange(int state) {
        for (NetworkChangeListener listener : mListeners) {
            if (listener != null) {
                listener.onNetworkChange(state);
            }
        }
    }

    /**
     * 动态注册广播接收器
     */
    private void registBroadcastReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        mReceiver = new NetworkBroadcastReceiver();
        LiveApplication.getInstance().registerReceiver(mReceiver, filter);
    }

    /**
     * 反注册广播接收器
     *
     */
    private void unRegistBroadcastReceiver() {
        try {
            LiveApplication.getInstance().unregisterReceiver(mReceiver);
        } catch (Exception e) {

        }
        mReceiver = null;
    }

    /**
     * 用于监听网络变化
     *
     * @author luwies
     */
    public interface NetworkChangeListener {
        /**
         * 当网络连接发生变化时回调此方法
         *
         * @param state 当前的网络状态
         */
        void onNetworkChange(int state);
    }

    /**
     * 用于接收网络变化的广播
     *
     * @author luwies
     */
    private class NetworkBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction()) == true) {
                notifyNetworkChange(NetworkUtil.getNetworkState(context));
            }
        }

    }
}
