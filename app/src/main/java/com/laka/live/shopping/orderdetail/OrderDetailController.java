/*
 * Copyright (C) 2015-2015 SJY.JIANGSU Corporation. All rights reserved
 */

package com.laka.live.shopping.orderdetail;

import android.os.Message;

import com.laka.live.shopping.framework.AbstractWindow;
import com.laka.live.shopping.framework.DefaultWindowController;
import com.laka.live.shopping.framework.adapter.MsgDef;
import com.laka.live.shopping.order.model.OrderResultInfo;


/**
 * Created by zhxu on 2015/11/9.
 * Email:357599859@qq.com
 */
public class OrderDetailController extends DefaultWindowController {

    private final static String TAG = "OrderDetailController";

    public OrderDetailController() {
        registerMessage(MsgDef.MSG_SHOW_ORDER_DETAIL_WINDOW);
    }

    private void showOrderDetailWindow(OrderResultInfo resultInfo) {
        AbstractWindow window = getCurrentWindow();
        if (window instanceof OrderDetailWindow) {
            return;
        }
        OrderDetailWindow orderDetailWindow = new OrderDetailWindow(mContext, this, resultInfo);
        mWindowMgr.pushWindow(orderDetailWindow);
    }

    @Override
    public void handleMessage(Message msg) {
        if (MsgDef.MSG_SHOW_ORDER_DETAIL_WINDOW == msg.what) {
            showOrderDetailWindow((OrderResultInfo) msg.obj);
        }
    }
}
