package com.laka.live.shopping.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.laka.live.account.AccountInfoManager;
import com.laka.live.account.login.LoginActivity;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.shopping.shoppingcar.ShoppingCarWindow;

/**
 * Created by Lyf on 2017/7/14.
 * 购物车
 */
public class ShoppingCarActivity extends BaseShopActivity {

    private ShoppingCarWindow shoppingCarWindow;


    public static void startActivity(Context context) {
        if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
            LoginActivity.startActivity(context, LoginActivity.TYPE_FROM_NEED_LOGIN);
            return;
        }
        Intent intent = new Intent(context, ShoppingCarActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shoppingCarWindow = new ShoppingCarWindow(this, this);
        setContentView(shoppingCarWindow);
    }

    @Override
    public void onEvent(PostEvent event) {
        super.onEvent(event);
        if (SubcriberTag.CLICK_FLOAT_LIVE.equals(event.tag)) {
            finish();
        }
    }
}
