package com.laka.live.shopping.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.laka.live.R;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.shopping.order.OrderWindow;
import com.laka.live.shopping.order.model.OrderBalanceInfo;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Lyf on 2017/7/18.
 * 确认订单页
 */
public class OrderActivity extends BaseShopActivity {


    private OrderWindow mOrderWindow;
    private OrderBalanceInfo mOrderBalanceInfo;


    public static void startActivity(Context context, OrderBalanceInfo mOrderBalanceInfo) {
        Intent intent = new Intent(context, OrderActivity.class);
        intent.putExtra("data", mOrderBalanceInfo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mOrderBalanceInfo = (OrderBalanceInfo) getIntent().getSerializableExtra("data");

        if (mOrderBalanceInfo == null) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.live_manager_data_error_code));
            return;
        }

        mOrderWindow = new OrderWindow(this, this, mOrderBalanceInfo);
        setContentView(mOrderWindow);
    }

    @Subscribe
    public void onEvent(PostEvent event) {
        super.onEvent(event);
        if (mOrderWindow.isStartedPay() && SubcriberTag.MSG_RECHARGE_SUCCESS.equals(event.tag)) {
            mOrderWindow.setStartedPay(false);
            int resultCode = (int) event.event;
            mOrderWindow.handleOnWeChatPayResultCallback(resultCode);
        } else if (SubcriberTag.CLICK_FLOAT_LIVE.equals(event.tag)) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mOrderWindow != null) {
            mOrderWindow.setStartedPay(false);
        }
    }
}
