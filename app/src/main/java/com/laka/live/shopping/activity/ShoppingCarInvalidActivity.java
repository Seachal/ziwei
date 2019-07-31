package com.laka.live.shopping.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.laka.live.shopping.bean.ShoppingCarGoodsBean;
import com.laka.live.shopping.order.model.OrderBalanceInfo;
import com.laka.live.shopping.shoppingcar.ShoppingCarInvalidWindow;
import com.laka.live.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lyf on 2017/7/20.
 */
@SuppressWarnings("ALL")
public class ShoppingCarInvalidActivity extends BaseShopActivity {


    private ShoppingCarInvalidWindow shoppingCarInvalidWindow;
    private ArrayList<ShoppingCarGoodsBean> mShoppingCarGoodsInvalidBeans;

    public static void startActivity(Context context, ArrayList<ShoppingCarGoodsBean> mShoppingCarGoodsInvalidBeans) {
        Intent intent = new Intent(context, ShoppingCarInvalidActivity.class);
        intent.putExtra("data", mShoppingCarGoodsInvalidBeans);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mShoppingCarGoodsInvalidBeans = (ArrayList<ShoppingCarGoodsBean>) getIntent().getSerializableExtra("data");
        shoppingCarInvalidWindow = new ShoppingCarInvalidWindow(this,this);
        shoppingCarInvalidWindow.setShoppingCarGoodsBeans(mShoppingCarGoodsInvalidBeans);
        setContentView(shoppingCarInvalidWindow);
    }

}
