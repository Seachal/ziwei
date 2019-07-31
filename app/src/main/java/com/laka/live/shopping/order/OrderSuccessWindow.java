/*
 * Copyright (C) 2015-2015 SJY.JIANGSU Corporation. All rights reserved
 */

package com.laka.live.shopping.order;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.application.LiveActivityManager;
import com.laka.live.bean.Course;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.shopping.activity.OrderDetailActivity;
import com.laka.live.shopping.bean.ShoppingAddressBean;
import com.laka.live.shopping.common.CommonConst;
import com.laka.live.shopping.framework.AbstractWindow;
import com.laka.live.shopping.framework.DefaultWindow;
import com.laka.live.shopping.framework.IDefaultWindowCallBacks;
import com.laka.live.shopping.framework.MsgDispatcher;
import com.laka.live.shopping.framework.Notification;
import com.laka.live.shopping.framework.NotificationCenter;
import com.laka.live.shopping.framework.adapter.MsgDef;
import com.laka.live.shopping.framework.adapter.NotificationDef;
import com.laka.live.shopping.order.model.OrderResultInfo;
import com.laka.live.shopping.widget.PriceView;
import com.laka.live.ui.activity.MainActivity;
import com.laka.live.ui.widget.SelectorButton;
import com.laka.live.util.ResourceHelper;


/**
 * Created by zhxu on 2015/11/9.
 * Email:357599859@qq.com
 */
public class OrderSuccessWindow extends DefaultWindow implements View.OnClickListener {

    private Activity mContext;
    private AbstractWindow mWindow;

    private PriceView goodsPrice;
    private TextView tvSendTime, tvArriveTime, tvCommunicate;
    private TextView tvReceiver, tvMobile, tvAddress;

    private SelectorButton btnBack, btnDetail;
    private ShoppingAddressBean mShoppingAddressBean;

    public OrderSuccessWindow(Context context, IDefaultWindowCallBacks callBacks, ShoppingAddressBean mShoppingAddressBean) {
        super(context, callBacks);
        mContext = (Activity) context;
        mWindow = this;
        this.mShoppingAddressBean = mShoppingAddressBean;
        setLaunchMode(LAUNCH_MODE_SINGLE_INSTANCE);
        initUI();
    }

    private void initUI() {

        setTitle(ResourceHelper.getString(R.string.pay_success_tips));
        View view = View.inflate(mContext, R.layout.order_success, null);
        getBaseLayer().addView(view, getContentLPForBaseLayer());

//        旧功能，先屏蔽，以后需要再打开
//        tvSendTime = (TextView) findViewById(R.id.order_success_send_time);
//        tvArriveTime = (TextView) findViewById(R.id.order_success_arrive_time);
//        tvCommunicate = (TextView) findViewById(R.id.order_success_communicate);
//        tvSendTime.setText(Html.fromHtml(String.format(ResourceHelper.getString(R.string.order_success_payable), CommonConst.TOTALPRICE)));
//        tvArriveTime.setText(ResourceHelper.getString(R.string.order_success_thank));
//        tvCommunicate.setText(ResourceHelper.getString(R.string.order_success_send_time));

        tvReceiver = (TextView) findViewById(R.id.tv_receiver);
        tvMobile = (TextView) findViewById(R.id.tv_mobile);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        goodsPrice = (PriceView) findViewById(R.id.goodsPrice);
        goodsPrice.setVisibility(VISIBLE);

        btnBack = (SelectorButton) findViewById(R.id.order_back_to_shopping);
        btnBack.setOnClickListener(this);
        btnDetail = (SelectorButton) findViewById(R.id.order_detail);
        btnDetail.setOnClickListener(this);

        tvReceiver.setText("收货人：" + mShoppingAddressBean.getReceiver());
        tvMobile.setText("收货人：" + mShoppingAddressBean.getMobile());
        tvAddress.setText("收货地址：" + mShoppingAddressBean.getDetailDistrict() + mShoppingAddressBean.getDetailAddr());
        goodsPrice.setText(String.valueOf(CommonConst.TOTALPRICE));

    }

    private void notifyOrderListReload() {
        Notification notification = Notification.obtain(NotificationDef.N_ORDER_LIST_CHANGED);
        NotificationCenter.getInstance().notify(notification);
    }

    @Override
    protected void onWindowStateChange(int stateFlag) {
        super.onWindowStateChange(stateFlag);
        if (stateFlag == STATE_AFTER_PUSH_IN) {
        } else if (stateFlag == STATE_ON_DETACH) {
        }
    }

    @Override
    public void notify(Notification notification) {
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.order_back_to_shopping) {
            // 返回商城事件
            EventBusManager.postEvent(0, SubcriberTag.OPEN_MALL_TAB);
            LiveActivityManager.getInstance().popUntilActivity(MainActivity.class);
        } else if (v.getId() == R.id.order_detail) {
            OrderResultInfo orderResultInfo = new OrderResultInfo();
            orderResultInfo.orderId = CommonConst.ORDERID;
            orderResultInfo.orderNum = CommonConst.ORDERNUM;
            orderResultInfo.accountsPayable = String.valueOf(CommonConst.TOTALPRICE);
            OrderDetailActivity.startActivity(mContext, orderResultInfo);
            finishActivity();
        }

    }

}
