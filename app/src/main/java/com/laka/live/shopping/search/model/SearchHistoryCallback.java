package com.laka.live.shopping.search.model;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

/**
 * Created by linhz on 2016/4/21.
 * Email: linhaizhong@ta2she.com
 */
public class SearchHistoryCallback {
    private InnerHandler mHandler;

    public SearchHistoryCallback() {
        mHandler = new InnerHandler(Looper.getMainLooper(), this);
    }

    public SearchHistoryCallback(Looper looper) {
        if (looper == null) {
            looper = Looper.getMainLooper();
        }
        mHandler = new InnerHandler(looper, this);
    }


    /* package */void sendCallbackMsg(Message msg) {
        if (msg == null) {
            return;
        }
        Message newMsg = Message.obtain(msg);
        mHandler.sendMessage(newMsg);
    }

    public void onGetAllHistoryResult(ArrayList<SearchHistoryInfo> list) {
        // override by subclass
    }

    public void onAddHistoryResult(boolean success, String text) {
        // override by subclass
    }

    public void onClearHistoryResult(boolean success) {
        // override by subclass
    }

    private static class InnerHandler extends Handler {
        private SoftReference<SearchHistoryCallback> mCallback;

        public InnerHandler(Looper looper, SearchHistoryCallback callback) {
            super(looper);
            mCallback = new SoftReference<>(callback);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            SearchHistoryCallback callback = mCallback.get();
            if (callback == null) {
                return;
            }
            SearchHistoryOptResult optResult = null;
            if (msg.obj instanceof SearchHistoryOptResult) {
                optResult = (SearchHistoryOptResult) msg.obj;
            }

            if (optResult == null) {
                return;
            }

            switch (msg.what) {
                case SearchHistoryManager.MSG_GET_ALL_HISTORY:
                    ArrayList<SearchHistoryInfo> dataList = null;
                    if (optResult.extras instanceof ArrayList<?>) {
                        dataList = (ArrayList<SearchHistoryInfo>) optResult.extras;
                    }
                    callback.onGetAllHistoryResult(dataList);
                    break;
                case SearchHistoryManager.MSG_ADD_HISTORY:
                    String text = null;
                    if (optResult.extras instanceof String) {
                        text = (String) optResult.extras;
                    }
                    callback.onAddHistoryResult(optResult.success, text);
                    break;
                case SearchHistoryManager.MSG_CLEAR_HISTORY:
                    callback.onClearHistoryResult(optResult.success);
                    break;

            }
        }
    }


}
