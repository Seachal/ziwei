package com.laka.live.shopping.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.laka.live.config.SystemConfig;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.shopping.order.OrderListWindow;
import com.laka.live.util.Common;
import com.laka.live.util.Log;

import laka.live.bean.ChatMsg;


/**
 * Created by Lyf on 2017/7/20.
 * 订单列表（全部、待付款、代发货、待收货、已完成）
 */

public class OrderListActivity extends BaseShopActivity {

    private static final String TAG = "OrderListActivity";
    OrderListWindow mWindow;

    Handler handler = new Handler(){

    };

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, OrderListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWindow = new OrderListWindow(this,this);
        setContentView(mWindow);
    }

    @Override
    public void onEvent(PostEvent event) {
        if (SubcriberTag.DO_PAY_THIRD.equals(event.tag)&&isResume()) {
            isNeedHandlePay = true;
        }
        super.onEvent(event);
        if (SubcriberTag.MSG_RECHARGE_SUCCESS.equals(event.tag)) {
            int resultCode = (int) event.event;
            handleOnWeChatPayResultCallback(resultCode);
        }else if(SubcriberTag.RECEIVE_CHAT_MSG.equals(event.tag)){
            ChatMsg msg = (ChatMsg) event.event;
            if(msg.getUserId().equals(SystemConfig.getInstance().getKefuID())){
                Log.d(TAG," 需要refreshUnread");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mWindow.refreshUnread();
                    }
                },1000);
            }else{
                Log.d(TAG," 不需要refreshUnread");
            }
        }

    }
}
