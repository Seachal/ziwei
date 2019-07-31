package com.laka.live.shopping.tacoin;

import android.content.Context;


import com.laka.live.R;
import com.laka.live.ui.rankinglist.widget.TabPager;
import com.laka.live.shopping.framework.IDefaultWindowCallBacks;
import com.laka.live.shopping.framework.MsgDispatcher;
import com.laka.live.shopping.framework.TabWindow;
import com.laka.live.shopping.framework.adapter.MsgDef;
import com.laka.live.shopping.framework.ui.DefaultTitleBar;
import com.laka.live.shopping.framework.ui.TitleBarActionItem;
import com.laka.live.util.ResourceHelper;

import java.util.ArrayList;

/**
 * Created by gangqing on 2016/3/9.
 * Email:denggangqing@ta2she.com
 */
public class TACoinIncomeWindow extends TabWindow implements TabPager.IScrollable {
    private static final int ACTION_BAR_TYPE_EXPLAIN = 0;

    public TACoinIncomeWindow(Context context, IDefaultWindowCallBacks callBacks) {
        super(context, callBacks);
        setTitle(ResourceHelper.getString(R.string.ta_coin_title));
        initActionBar();
        setTabbarInTitlebar(false);
        createTabView();
    }

    private void initActionBar() {
        ArrayList<TitleBarActionItem> actionList = new ArrayList<TitleBarActionItem>(1);
        TitleBarActionItem mItem = new TitleBarActionItem(getContext());
        mItem.setId(ACTION_BAR_TYPE_EXPLAIN);
        mItem.setText(ResourceHelper.getString(R.string.account_personal_level_action));
        actionList.add(mItem);

        DefaultTitleBar titleBar = (DefaultTitleBar) getTitleBar();
        titleBar.setActionItems(actionList);
    }

    @Override
    public void onTitleBarActionItemClick(int itemId) {
        switch (itemId) {
            case ACTION_BAR_TYPE_EXPLAIN:
                MsgDispatcher.getInstance().sendMessage(MsgDef.MSG_SHOW_TA_EXPLAIN_WINDOW);
                break;
        }
    }

    @Override
    public boolean canScroll(boolean scrollLeft) {
        return false;
    }

    private void createTabView() {
//        addTab(new TACoinIncomeView(getContext()));
//        addTab(new TACoinDisburseView(getContext()));
    }
}
