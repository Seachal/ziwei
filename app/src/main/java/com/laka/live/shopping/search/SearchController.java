package com.laka.live.shopping.search;

import android.os.Message;

import com.laka.live.shopping.framework.AbstractWindow;
import com.laka.live.shopping.framework.DefaultWindowController;
import com.laka.live.shopping.framework.adapter.MsgDef;


/**
 * Created by gangqing on 2016/4/21.
 * Email:denggangqing@ta2she.com
 */
public class SearchController extends DefaultWindowController {

    public SearchController() {
        registerMessage(MsgDef.MSG_SHOW_SEARCH_WINDOW);
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == MsgDef.MSG_SHOW_SEARCH_WINDOW) {
            showSearchWindow();
        }
    }

    /**
     * 打开搜索窗口
     */
    private void showSearchWindow() {
        AbstractWindow currentWindow = getCurrentWindow();
        if (currentWindow instanceof SearchWindow) {
            return;
        }
        currentWindow = new SearchWindow(mContext, this);
        mWindowMgr.pushWindow(currentWindow);
    }
}
