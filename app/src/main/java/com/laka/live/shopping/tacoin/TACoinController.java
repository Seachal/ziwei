package com.laka.live.shopping.tacoin;

import android.os.Message;

import com.laka.live.account.AccountInfoManager;
import com.laka.live.shopping.framework.AbstractWindow;
import com.laka.live.shopping.framework.DefaultWindowController;
import com.laka.live.shopping.framework.adapter.MsgDef;
import com.laka.live.shopping.network.HttpUrls;
import com.laka.live.shopping.tacoin.model.TACoinMainRequest;


/**
 * Created by gangqing on 2016/3/10.
 * Email:denggangqing@ta2she.com
 */
public class TACoinController extends DefaultWindowController {
    public TACoinController() {
        registerMessage(MsgDef.MSG_SHOW_TA_COIN_MAIN_WINDOW);
        registerMessage(MsgDef.MSG_SHOW_TA_COIN_INCOME_WINDOW);
        registerMessage(MsgDef.MSG_SHOW_TA_COIN_TOAST);
        registerMessage(MsgDef.MSG_SHOW_TA_DAY_LOGIN);
        registerMessage(MsgDef.MSG_SHOW_TA_EXPLAIN_WINDOW);
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == MsgDef.MSG_SHOW_TA_COIN_MAIN_WINDOW) {
            if (AccountInfoManager.getInstance().isLogin()) {
                showTACoinMainWindow();
            } else {
                AccountInfoManager.getInstance().tryOpenLoginWindow();
            }
        } else if (msg.what == MsgDef.MSG_SHOW_TA_COIN_INCOME_WINDOW) {
            if (AccountInfoManager.getInstance().isLogin()) {
                showTACoinIncomeWindow();
            } else {
                AccountInfoManager.getInstance().tryOpenLoginWindow();
            }
        } else if (msg.what == MsgDef.MSG_SHOW_TA_DAY_LOGIN) {
            handleDayLogin();
        } else if (msg.what == MsgDef.MSG_SHOW_TA_EXPLAIN_WINDOW) {
            showTACoinExplainWindow();
        }
    }

    private void handleDayLogin() {
        if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
            return;
        }
        TACoinMainRequest.requestDayLogin(mContext);
    }

    private void showTACoinMainWindow() {
        // TODO: 2017/7/14 showTACoinMainWindow
//        AbstractWindow window = getCurrentWindow();
//        if (window instanceof TACoinMainWindow) {
//            return;
//        }
//        window = new TACoinMainWindow(mContext, this);
//        mWindowMgr.pushWindow(window);
    }

    private void showTACoinIncomeWindow() {
        if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
            AccountInfoManager.getInstance().tryOpenLoginWindow();
            return;
        }
        AbstractWindow window = getCurrentWindow();
        if (window instanceof TACoinIncomeWindow) {
            return;
        }
        window = new TACoinIncomeWindow(mContext, this);
        mWindowMgr.pushWindow(window);
    }

    private void showTACoinExplainWindow() {
        // TODO: 2017/7/14  showTACoinExplainWindow
//        Message msg = Message.obtain();
//        WebViewBrowserParams params = new WebViewBrowserParams();
//        params.title = ResourceHelper.getString(R.string.ta_coin_explain);
//        params.url = HttpUrls.TA_COIN_EXPLAIN;
//        msg.obj = params;
//        msg.what = MsgDef.MSG_SHOW_WEB_VIEW_BROWSER;
//        MsgDispatcher.getInstance().sendMessage(msg);
    }
}
