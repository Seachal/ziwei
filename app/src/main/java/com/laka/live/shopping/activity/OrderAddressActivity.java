package com.laka.live.shopping.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.laka.live.R;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.shopping.order.OrderAddressWindow;
import com.laka.live.shopping.order.model.OrderAddressInfo;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;

import org.greenrobot.eventbus.Subscribe;


/**
 * Created by Lyf on 2017/7/20.
 */

public class OrderAddressActivity extends BaseShopActivity {

    private OrderAddressWindow orderAddressWindow;
    private OrderAddressInfo orderAddressInfo;

    public static void startActivity(Context context, OrderAddressInfo orderAddressInfo) {
        Intent intent = new Intent(context,OrderAddressActivity.class);
        intent.putExtra("data",orderAddressInfo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        orderAddressInfo = (OrderAddressInfo) getIntent().getSerializableExtra("data");

        if(orderAddressInfo == null){
            ToastHelper.showToast(ResourceHelper.getString(R.string.live_manager_data_error_code));
            return;
        }

        orderAddressWindow = new OrderAddressWindow(this,this,orderAddressInfo);
        setContentView(orderAddressWindow);
    }

    @Subscribe
    public void onEvent(PostEvent event) {
        super.onEvent(event);
        if (SubcriberTag.CLICK_FLOAT_LIVE.equals(event.tag)) {
            finish();
        }
    }
}
