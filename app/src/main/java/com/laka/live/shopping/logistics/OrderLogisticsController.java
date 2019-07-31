/*
 * Copyright (C) 2015-2015 SJY.JIANGSU Corporation. All rights reserved
 */

package com.laka.live.shopping.logistics;

import android.os.Message;

import com.laka.live.shopping.bean.ShoppingOrderDetailBean;
import com.laka.live.shopping.framework.DefaultWindowController;
import com.laka.live.shopping.framework.adapter.MsgDef;


/**
 * Created by zhxu on 2015/11/9.
 * Email:357599859@qq.com
 */
public class OrderLogisticsController extends DefaultWindowController {

    private final static String TAG = "OrderLogisticsController";

    public OrderLogisticsController() {
        registerMessage(MsgDef.MSG_SHOW_ORDER_LOGISTICS_WINDOW);
    }

    private void showOrderLogisticsWindow(ShoppingOrderDetailBean detailBean) {
        OrderLogisticsWindow orderLogisticsWindow = new OrderLogisticsWindow(mContext, this, detailBean);
        mWindowMgr.pushWindow(orderLogisticsWindow);
    }

    @Override
    public void handleMessage(Message msg) {
        if (MsgDef.MSG_SHOW_ORDER_LOGISTICS_WINDOW == msg.what) {
            showOrderLogisticsWindow((ShoppingOrderDetailBean) msg.obj);
        }
    }
}
