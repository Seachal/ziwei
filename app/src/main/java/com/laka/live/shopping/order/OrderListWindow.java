/*
 * Copyright (C) 2015-2015 SJY.JIANGSU Corporation. All rights reserved
 */

package com.laka.live.shopping.order;

import android.app.Activity;
import android.content.Context;


import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.account.login.LoginActivity;
import com.laka.live.config.SystemConfig;
import com.laka.live.dao.DbManger;
import com.laka.live.shopping.common.Constant;
import com.laka.live.shopping.framework.IDefaultWindowCallBacks;
import com.laka.live.shopping.framework.TabWindow;
import com.laka.live.shopping.framework.adapter.MenuItemIdDef;
import com.laka.live.shopping.framework.ui.DefaultTitleBar;
import com.laka.live.shopping.framework.ui.TitleBarActionItem;
import com.laka.live.shopping.message.TASecretaryDialogHelper;
import com.laka.live.shopping.order.tab.OrderAllTab;
import com.laka.live.shopping.order.tab.OrderWaitDeliverTab;
import com.laka.live.shopping.order.tab.OrderWaitEvaluateTab;
import com.laka.live.shopping.order.tab.OrderWaitPayTab;
import com.laka.live.shopping.order.tab.OrderWaitReceivingTab;
import com.laka.live.ui.chat.ChatMessageActivity;
import com.laka.live.util.Common;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.qiyukf.unicorn.api.Unicorn;

import java.util.ArrayList;

import laka.live.bean.ChatSession;

/**
 * Created by zhxu on 2015/11/9.
 * Email:357599859@qq.com
 */
public class OrderListWindow extends TabWindow {
    private TASecretaryDialogHelper mDialogHelper;
    private Activity mActivity;
    public OrderListWindow(Activity context, IDefaultWindowCallBacks callBacks) {
        super(context, callBacks);
        mActivity = context;
        setTitle(R.string.order_list_title);
        createTabView();
        mDialogHelper = new TASecretaryDialogHelper();
        initActionBar();
        refreshUnread();
    }

    private void createTabView() {
        addTab(new OrderAllTab(getContext()));
        addTab(new OrderWaitPayTab(getContext()));
        addTab(new OrderWaitDeliverTab(getContext()));
        addTab(new OrderWaitReceivingTab(getContext()));
        addTab(new OrderWaitEvaluateTab(getContext()));
    }

    public void setTabType(int tabType) {
        switch (tabType) {
            case Constant.ORDER_ALL:
                setCurrentTab(0);
                break;
            case Constant.ORDER_WAIT_PAY:
                setCurrentTab(1);
                break;
            case Constant.ORDER_WAIT_DEL:
                setCurrentTab(2);
                break;
            case Constant.ORDER_REV:
                setCurrentTab(3);
                break;
            case Constant.ORDER_WAIT_EVA:
                setCurrentTab(4);
                break;
        }
    }

    TitleBarActionItem ivService;
    private void initActionBar() {
        ArrayList<TitleBarActionItem> actionList = new ArrayList<TitleBarActionItem>(1);
        ivService = new TitleBarActionItem(getContext());
        ivService.setDrawable(ResourceHelper.getDrawable(R.drawable.nav_icon_service));
        ivService.setItemId(MenuItemIdDef.TITLEBAR_TA_SECRETARY_SERVICE);
        actionList.add(ivService);
        DefaultTitleBar titleBar = (DefaultTitleBar) getTitleBar();
        titleBar.setActionItems(actionList);


    }


    private void updateServiceNum(int count) {
        Log.d(TAG," updateServiceNum count="+count);
        if (ivService != null) {
            ivService.setRedTipVisibility(count > 0);
            ivService.setRedTipText(count > 99 ? "99+" : String.valueOf(count));
        }
    }

    ChatSession session;
    public void refreshUnread() {
        updateServiceNum(Unicorn.getUnreadCount());
//        session = DbManger.getInstance().getSessionBySessionId(AccountInfoManager.getInstance().getCurrentAccountUserIdStr() + SystemConfig.getInstance().getKefuID());
//        if(session!=null){
//            updateServiceNum(session.getUnreadCnt());
//        }
    }

    @Override
    public void onTitleBarActionItemClick(int itemId) {
        if (itemId == MenuItemIdDef.TITLEBAR_TA_SECRETARY_SERVICE) {
//            mDialogHelper.requestChatDetailsId(null);
            if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                LoginActivity.startActivity(mActivity, LoginActivity.TYPE_FROM_NEED_LOGIN);
                return;
            }
            if(session!=null){
                session.setUnreadCnt(0);
//            DbManger.getInstance().updateSession(session);
            }
            updateServiceNum(0);
           // refreshUnread();
            // TODO: 2017/7/31 客服
            ChatMessageActivity.startActivity(mActivity);
        }
    }
}
