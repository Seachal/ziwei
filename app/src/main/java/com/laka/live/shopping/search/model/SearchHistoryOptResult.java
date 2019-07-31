package com.laka.live.shopping.search.model;

import android.os.Message;

/**
 * Created by linhz on 2016/4/21.
 * Email: linhaizhong@ta2she.com
 */
/*package*/ class SearchHistoryOptResult {

    /* package */ SearchHistoryCallback callback;
    /* package */ Object extras;
    /* package */ boolean success = true;


    /* package */void sendResult(Message msg) {
        if (callback != null && msg != null) {
            msg.obj = this;
            callback.sendCallbackMsg(msg);
        }
    }
}
