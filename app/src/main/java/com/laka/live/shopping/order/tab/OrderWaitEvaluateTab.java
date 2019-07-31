package com.laka.live.shopping.order.tab;

import android.content.Context;

import com.laka.live.R;
import com.laka.live.shopping.common.Constant;
import com.laka.live.util.ResourceHelper;


/**
 * Created by gangqing on 2016/4/27.
 * Email:denggangqing@ta2she.com
 */
public class OrderWaitEvaluateTab extends BaseOrderTab {
    public OrderWaitEvaluateTab(Context context) {
        super(context);
    }

    @Override
    public int getCurrentTabType() {
        return Constant.ORDER_FINISH;
    }

    @Override
    public String getTitle() {
        return ResourceHelper.getString(R.string.order_tab_evaluate_tab_title);
    }
}
