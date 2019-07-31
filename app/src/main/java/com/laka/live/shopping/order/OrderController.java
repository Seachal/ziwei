/*
 * Copyright (C) 2015-2015 SJY.JIANGSU Corporation. All rights reserved
 */

package com.laka.live.shopping.order;

import android.os.Message;

import com.laka.live.account.AccountInfoManager;
import com.laka.live.shopping.common.CommonConst;
import com.laka.live.shopping.framework.AbstractWindow;
import com.laka.live.shopping.framework.DefaultWindowController;
import com.laka.live.shopping.framework.Notification;
import com.laka.live.shopping.framework.NotificationCenter;
import com.laka.live.shopping.framework.adapter.MsgDef;
import com.laka.live.shopping.framework.adapter.NotificationDef;
import com.laka.live.shopping.order.model.OrderAddressInfo;
import com.laka.live.shopping.order.model.OrderBalanceInfo;
import com.laka.live.util.ToastHelper;


/**
 * Created by zhxu on 2015/11/9.
 * Email:357599859@qq.com
 */
public class OrderController extends DefaultWindowController {

    private final static String TAG = "OrderController";

    public OrderController() {
        registerMessage(MsgDef.MSG_SHOW_ORDER_WINDOW);
        registerMessage(MsgDef.MSG_SHOW_ORDER_SUCCESS_WINDOW);
        registerMessage(MsgDef.MSG_SHOW_ORDER_FAILED_WINDOW);
        registerMessage(MsgDef.MSG_SHOW_ORDER_ADDRESS_WINDOW);
        registerMessage(MsgDef.MSG_REWARD_PAY_SUCCEED);
        registerMessage(MsgDef.MSG_REWARD_PAY_FAILED);
        registerMessage(MsgDef.MSG_VIP_PAY_FAILED);
        registerMessage(MsgDef.MSG_VIP_PAY_SUCCEED);
    }

    private void showOrderAddressWindow(OrderAddressInfo addressInfo) {
        if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
            AccountInfoManager.getInstance().tryOpenLoginWindow();
            return;
        }
        AbstractWindow window = getCurrentWindow();
        if (window instanceof OrderAddressWindow) {
            return;
        }
        OrderAddressWindow orderAddressWindow = new OrderAddressWindow(mContext, this, addressInfo);
        mWindowMgr.pushWindow(orderAddressWindow);
    }

    private void showOrderFailedWindow() {
        AbstractWindow window = getCurrentWindow();
        if (window instanceof OrderFailedWindow) {
            return;
        }
        OrderFailedWindow orderFailedWindow = new OrderFailedWindow(mContext, this);
        mWindowMgr.pushWindow(orderFailedWindow);
    }

    private void showOrderSuccessWindow() {
        AbstractWindow window = getCurrentWindow();
        if (window instanceof OrderSuccessWindow) {
            return;
        }

    }

    private void showRewardSuccessWindow() {

        AbstractWindow window = getCurrentWindow();
        // TODO: 2017/7/14  showRewardSuccessWindow
//        if (window instanceof UserInfoWindow) {
//            UserInfoWindow userInfoWindow = (UserInfoWindow) window;
//            userInfoWindow.showNum();
//        } else if (window instanceof SecretZoneWindow) {
//            SecretZoneWindow secretZoneWindow = (SecretZoneWindow) window;
//            secretZoneWindow.paySuccess();
//        }

    }

    private void showOrderWindow(OrderBalanceInfo orderBalanceInfo) {
        if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
            AccountInfoManager.getInstance().tryOpenLoginWindow();
            return;
        }

        AbstractWindow window = getCurrentWindow();
        if (window instanceof OrderWindow) {
            return;
        }
        OrderWindow orderWindow = new OrderWindow(mContext, this, orderBalanceInfo);
        mWindowMgr.pushWindow(orderWindow);
    }

    @Override
    public void handleMessage(Message msg) {
        if (MsgDef.MSG_SHOW_ORDER_WINDOW == msg.what) {
            showOrderWindow((OrderBalanceInfo) msg.obj);
        } else if (MsgDef.MSG_SHOW_ORDER_SUCCESS_WINDOW == msg.what) {
            showOrderSuccessWindow();
        } else if (MsgDef.MSG_SHOW_ORDER_FAILED_WINDOW == msg.what) {
            showOrderFailedWindow();
        } else if (MsgDef.MSG_SHOW_ORDER_ADDRESS_WINDOW == msg.what) {
            showOrderAddressWindow((OrderAddressInfo) msg.obj);
        } else if (MsgDef.MSG_REWARD_PAY_SUCCEED == msg.what) {
            CommonConst.CURRENT_PAY_TYPE = CommonConst.DEFAULT_PAY_TYPE;
            ToastHelper.showToast("打赏成功");
            showRewardSuccessWindow();
        } else if (MsgDef.MSG_REWARD_PAY_FAILED == msg.what) {
            CommonConst.CURRENT_PAY_TYPE = CommonConst.DEFAULT_PAY_TYPE;
            ToastHelper.showToast("打赏失败 ， 请稍后重试");
        } else if (MsgDef.MSG_VIP_PAY_FAILED == msg.what) {
            CommonConst.CURRENT_PAY_TYPE = CommonConst.DEFAULT_PAY_TYPE;
            ToastHelper.showToast("支付失败");
        } else if (MsgDef.MSG_VIP_PAY_SUCCEED == msg.what) {
            CommonConst.CURRENT_PAY_TYPE = CommonConst.DEFAULT_PAY_TYPE;
            ToastHelper.showToast("支付成功");
            NotificationCenter.getInstance().notify(Notification.obtain(NotificationDef.N_VIP_PAY_SUCCEED));
        }
    }
}
