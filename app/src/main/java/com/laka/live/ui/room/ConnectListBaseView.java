package com.laka.live.ui.room;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.laka.live.bean.ConnectUserInfo;
import com.laka.live.help.EventBusManager;
import com.laka.live.msg.ListMag;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.util.Log;

import java.util.List;

/**
 * Created by luwies on 16/11/1.
 */

public abstract class ConnectListBaseView extends FrameLayout implements
        EventBusManager.OnEventBusListener{

    protected GsonHttpConnection.OnResultListener mOnResultListener;

    public ConnectListBaseView(Context context) {
        super(context);
    }

    public ConnectListBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ConnectListBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void register() {
        EventBusManager.register(this);
    }

    public void unRegister() {
        EventBusManager.unregister(this);
        mOnResultListener = null;
    }

    protected void onResult(final List<ConnectUserInfo> list) {
        post(new Runnable() {
            @Override
            public void run() {
                Log.error("ConnectListBaseView", "mOnResultListener == null "
                        + String.valueOf(mOnResultListener == null));
                if (mOnResultListener != null) {
                    ListMag listMag = new ListMag() {
                        @Override
                        public List getList() {
                            return list;
                        }
                    };
                    mOnResultListener.onSuccess(listMag);
                    mOnResultListener = null;
                }
            }
        });

    }

    public abstract void refresh();
}
