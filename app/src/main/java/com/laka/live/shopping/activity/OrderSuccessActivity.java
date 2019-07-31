package com.laka.live.shopping.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.shopping.bean.ShoppingAddressBean;
import com.laka.live.shopping.order.OrderSuccessWindow;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Lyf on 2017/7/20.
 */
public class OrderSuccessActivity extends BaseShopActivity {

    private ShoppingAddressBean mShoppingAddressBean;
    private OrderSuccessWindow orderSuccessWindow;

    public static void startActivity(Context context, ShoppingAddressBean shoppingAddressBean) {
        Intent intent = new Intent(context, OrderSuccessActivity.class);
        intent.putExtra("data", shoppingAddressBean);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShoppingAddressBean = (ShoppingAddressBean) getIntent().getSerializableExtra("data");
        orderSuccessWindow = new OrderSuccessWindow(mContext, this,mShoppingAddressBean);
        setContentView(orderSuccessWindow);
    }

    @Subscribe
    public void onEvent(PostEvent event) {
        super.onEvent(event);
        if (SubcriberTag.CLICK_FLOAT_LIVE.equals(event.tag)) {
            finish();
        }
    }
}
