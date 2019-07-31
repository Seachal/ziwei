package com.laka.live.shopping.shoppingcar;

import android.os.Message;


import com.laka.live.account.AccountInfoManager;
import com.laka.live.shopping.bean.ShoppingCarGoodsBean;
import com.laka.live.shopping.framework.AbstractWindow;
import com.laka.live.shopping.framework.DefaultWindowController;
import com.laka.live.shopping.framework.adapter.MsgDef;

import java.util.List;

/**
 * Created by chenjiawei on 2016/1/4.
 */
public class ShoppingCarController extends DefaultWindowController {
    public ShoppingCarController() {
        registerMessage(MsgDef.MSG_SHOW_SHOPPING_CAR_WINDOW);
        registerMessage(MsgDef.MSG_SHOPPING_CAR_INVALID_WINDOW_OPEN);
        registerMessage(MsgDef.MSG_SHOPPING_CAR_GO_SHOPPING);
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == MsgDef.MSG_SHOW_SHOPPING_CAR_WINDOW) {
            showShoppingCarWindow();
        } else if (msg.what == MsgDef.MSG_SHOPPING_CAR_INVALID_WINDOW_OPEN) {
            List<ShoppingCarGoodsBean> mShoppingCarGoodsInvalidBeans = (List<ShoppingCarGoodsBean>) msg.obj;
            showShoppingCarInvalidWindow(mShoppingCarGoodsInvalidBeans);
        } else if (msg.what == MsgDef.MSG_SHOPPING_CAR_GO_SHOPPING) {
            mWindowMgr.popToRootWindow(true);
        }
    }

    private void showShoppingCarInvalidWindow(List<ShoppingCarGoodsBean> mShoppingCarGoodsInvalidBeans) {
        AbstractWindow window = getCurrentWindow();
        if (window instanceof ShoppingCarInvalidWindow) {
            return;
        }
        ShoppingCarInvalidWindow shoppingCarInvalidWindow = new ShoppingCarInvalidWindow(mContext, this);
        shoppingCarInvalidWindow.setShoppingCarGoodsBeans(mShoppingCarGoodsInvalidBeans);
        mWindowMgr.pushWindow(shoppingCarInvalidWindow);
    }

    private void showShoppingCarWindow() {
        if (!AccountInfoManager.getInstance().isLogin()) {
            AccountInfoManager.getInstance().tryOpenLoginWindow();
            return;
        }
        AbstractWindow window = getCurrentWindow();
        if (window instanceof ShoppingCarWindow) {
            return;
        }

        ShoppingCarWindow shoppingCarWindow = new ShoppingCarWindow(mContext, this);
        mWindowMgr.pushWindow(shoppingCarWindow);
    }
}
