package com.laka.live.shopping.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.laka.live.R;
import com.laka.live.shopping.bean.ShoppingOrderDetailBean;
import com.laka.live.shopping.logistics.OrderLogisticsWindow;
import com.laka.live.shopping.order.model.OrderResultInfo;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;


/**
 * Created by Lyf on 2017/7/20.
 */

public class OrderLogisticsActivity extends BaseShopActivity {

    OrderLogisticsWindow mWindow;
    ShoppingOrderDetailBean data;
    public static void startActivity(Context context,ShoppingOrderDetailBean data) {
        Intent intent = new Intent(context, OrderLogisticsActivity.class);
        intent.putExtra("data",data);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        data = (ShoppingOrderDetailBean) getIntent().getSerializableExtra("data");
        if(data == null){
            ToastHelper.showToast(ResourceHelper.getString(R.string.live_manager_data_error_code));
            finish();
            return;
        }
        mWindow = new OrderLogisticsWindow(this,this,data);
        setContentView(mWindow);
    }

}
