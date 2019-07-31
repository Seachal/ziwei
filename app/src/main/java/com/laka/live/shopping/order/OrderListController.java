/*
 * Copyright (C) 2015-2015 SJY.JIANGSU Corporation. All rights reserved
 */

package com.laka.live.shopping.order;

import android.os.Message;

import com.laka.live.account.AccountInfoManager;
import com.laka.live.shopping.framework.AbstractWindow;
import com.laka.live.shopping.framework.DefaultWindowController;
import com.laka.live.shopping.framework.adapter.MsgDef;
import com.laka.live.shopping.order.model.OrderReviewInfo;


/**
 * created by zhxu on 2015/11/9.
 * email:357599859@qq.com
 */
public class OrderListController extends DefaultWindowController {

    private final static String TAG = "OrderListController";

    public OrderListController() {
        registerMessage(MsgDef.MSG_SHOW_ORDER_LIST_WINDOW);
        registerMessage(MsgDef.MSG_SHOW_ORDER_REVIEW_LIST_WINDOW);
        registerMessage(MsgDef.MSG_SHOW_ORDER_REVIEW_WINDOW);
    }

    private void showOrderListWindow(int tabType) {
        if (!AccountInfoManager.getInstance().isLogin()) {
            AccountInfoManager.getInstance().tryOpenLoginWindow();
            return;
        }
        AbstractWindow window = getCurrentWindow();
        if (window instanceof OrderListWindow) {
            return;
        }
//        OrderListWindow orderAddressWindow = new OrderListWindow(mContext, this);
//        orderAddressWindow.setTabType(tabType);
//        mWindowMgr.pushWindow(orderAddressWindow);
    }

    private void showOrderReviewWindow(OrderReviewInfo reviewInfo) {
        if (!AccountInfoManager.getInstance().isLogin()) {
            AccountInfoManager.getInstance().tryOpenLoginWindow();
            return;
        }
        AbstractWindow window = getCurrentWindow();
        if (window instanceof OrderReviewWindow) {
            return;
        }
        OrderReviewWindow reviewWindow = new OrderReviewWindow(mContext, this, reviewInfo);
        mWindowMgr.pushWindow(reviewWindow);
    }

    private void showOrderReviewListWindow(OrderReviewInfo reviewInfo) {
        if (!AccountInfoManager.getInstance().isLogin()) {
            AccountInfoManager.getInstance().tryOpenLoginWindow();
            return;
        }
        AbstractWindow window = getCurrentWindow();
        if (window instanceof OrderReviewListWindow) {
            return;
        }
        OrderReviewListWindow reviewListWindow = new OrderReviewListWindow(mContext, this, reviewInfo);
        mWindowMgr.pushWindow(reviewListWindow);
    }

    @Override
    public void handleMessage(Message msg) {
        if (MsgDef.MSG_SHOW_ORDER_LIST_WINDOW == msg.what) {
            showOrderListWindow(msg.arg1);
        } else if (MsgDef.MSG_SHOW_ORDER_REVIEW_LIST_WINDOW == msg.what) {
            showOrderReviewListWindow((OrderReviewInfo) msg.obj);
        } else if (MsgDef.MSG_SHOW_ORDER_REVIEW_WINDOW == msg.what) {
            showOrderReviewWindow((OrderReviewInfo) msg.obj);
        }
    }
}
