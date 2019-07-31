package com.laka.live.shopping.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.laka.live.R;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.shopping.order.model.OrderResultInfo;
import com.laka.live.shopping.orderdetail.OrderDetailWindow;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;


/**
 * Created by Lyf on 2017/7/20.
 * 订单详情
 */

public class OrderDetailActivity extends BaseShopActivity {

    OrderDetailWindow mWindow;
    OrderResultInfo resultInfo;

    public static void startActivity(Context context,OrderResultInfo resultInfo) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra("data",resultInfo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resultInfo = (OrderResultInfo) getIntent().getSerializableExtra("data");
        if(resultInfo == null){
            ToastHelper.showToast(ResourceHelper.getString(R.string.live_manager_data_error_code));
            finish();
            return;
        }
        mWindow = new OrderDetailWindow(this,this,resultInfo);
        setContentView(mWindow);
    }

    @Override
    public void onEvent(PostEvent event) {
        if (SubcriberTag.DO_PAY_THIRD.equals(event.tag)&&isResume()) { // 付款去
            isNeedHandlePay = true;
        }
        super.onEvent(event);
//        if(SubcriberTag.PAY_SUCCESS.equals(event.tag)){
//            finish();
//        } else
       if (  SubcriberTag.MSG_RECHARGE_SUCCESS.equals(event.tag)) {
            int resultCode = (int) event.event;
            mWindow.handleOnWeChatPayResultCallback(resultCode);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWindow.destory();
    }
}
