package com.laka.live.shopping.order.tab;

import android.content.Context;

import com.laka.live.R;
import com.laka.live.shopping.common.Constant;
import com.laka.live.util.ResourceHelper;


/**
 * Created by gangqing on 2016/4/27.
 * Email:denggangqing@ta2she.com
 */
public class OrderWaitReceivingTab extends BaseOrderTab {
    public OrderWaitReceivingTab(Context context) {
        super(context);
    }

    @Override
    public int getCurrentTabType() {
        return Constant.ORDER_REV;
    }

    @Override
    public String getTitle() {
        return ResourceHelper.getString(R.string.order_state4);
    }
}