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
import android.widget.ImageView;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.shopping.activity.OrderDetailActivity;
import com.laka.live.shopping.common.CommonConst;
import com.laka.live.shopping.framework.AbstractWindow;
import com.laka.live.shopping.framework.DefaultWindow;
import com.laka.live.shopping.framework.IDefaultWindowCallBacks;
import com.laka.live.shopping.framework.MsgDispatcher;
import com.laka.live.shopping.framework.Notification;
import com.laka.live.shopping.framework.adapter.MsgDef;
import com.laka.live.shopping.order.model.OrderResultInfo;
import com.laka.live.shopping.order.widget.OrderDialog;
import com.laka.live.ui.widget.dialog.GenericDialog;
import com.laka.live.ui.widget.dialog.IDialogOnClickListener;
import com.laka.live.util.ResourceHelper;


/**
 * Created by zhxu on 2015/11/9.
 * Email:357599859@qq.com
 */
public class OrderFailedWindow extends DefaultWindow implements View.OnClickListener, IDialogOnClickListener {

    private final static String TAG = "OrderFailedWindow";

    private Activity mContext;
    private AbstractWindow mWindow;

    private ImageView mImageViewZFB, mImageViewWX;

    private OrderDialog mOrderDialog;

    private TextView mTvSendTime;

    private Button mBtnPay, mBtnDetail;

    private OrderResultInfo mOrderResultInfo;

    public OrderFailedWindow(Context context, IDefaultWindowCallBacks callBacks) {
        super(context, callBacks);
        mContext = (Activity) context;
        mWindow = this;
        setLaunchMode(LAUNCH_MODE_SINGLE_INSTANCE);

        initUI();

        //统计PRODUCT_ORDER_UNPAY
//        StatsModel.stats(StatsKeyDef.PRODUCT_ORDER_UNPAY);
    }

    private void initUI() {
        setTitle(ResourceHelper.getString(R.string.order_failed_main));
        View view = View.inflate(mContext, R.layout.order_failed, null);
        getBaseLayer().addView(view, getContentLPForBaseLayer());

        mOrderDialog = new OrderDialog(mContext, createDialog());
        mOrderDialog.setOnClickListener(this);

        mTvSendTime = (TextView) findViewById(R.id.order_failed_send_time);
        mTvSendTime.setText(Html.fromHtml(String.format(ResourceHelper.getString(R.string.order_failed_payable), CommonConst.TOTALPRICE)));

        mBtnPay = (Button) findViewById(R.id.order_pay_pos_text);
        mBtnPay.setOnClickListener(this);
        mBtnDetail = (Button) findViewById(R.id.order_detail);
        mBtnDetail.setOnClickListener(this);

        mOrderResultInfo = new OrderResultInfo();
        mOrderResultInfo.orderId = CommonConst.ORDERID;
        mOrderResultInfo.orderNum = CommonConst.ORDERNUM;
        mOrderResultInfo.accountsPayable = String.valueOf(CommonConst.TOTALPRICE);
    }

    private View createDialog() {
        View view = View.inflate(mContext, R.layout.order_dialog_pay, null);
        TextView tvZFB = (TextView) view.findViewById(R.id.tv_zfb);
        tvZFB.setOnClickListener(this);
        TextView tvWX = (TextView) view.findViewById(R.id.tv_wx);
        tvWX.setOnClickListener(this);
        mImageViewZFB = (ImageView) view.findViewById(R.id.iv_zfb);
        mImageViewZFB.setSelected(true);
        mImageViewWX = (ImageView) view.findViewById(R.id.iv_wx);
        mImageViewWX.setSelected(false);
        return view;
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
        if (v.getId() == R.id.order_pay_pos_text) {
            mOrderDialog.show();
        } else if (v.getId() == R.id.order_detail) {
//            Message message = Message.obtain();
//            message.what = MsgDef.MSG_SHOW_ORDER_DETAIL_WINDOW;
//            message.obj = mOrderResultInfo;
//            MsgDispatcher.getInstance().sendMessage(message);
            OrderDetailActivity.startActivity(mContext, mOrderResultInfo);
            mCallBacks.onWindowExitEvent(mWindow, true);
        } else if (v.getId() == R.id.tv_zfb) {
            changePayType(mOrderDialog.ALIPAY);
        } else if (v.getId() == R.id.tv_wx) {
            changePayType(mOrderDialog.WECHAT);
        }
    }

    @Override
    public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
        if (viewId == GenericDialog.ID_BUTTON_NO) {
            dialog.dismiss();
        } else if (viewId == GenericDialog.ID_BUTTON_YES) {
            commonPay();
        }
        return false;
    }

    private void changePayType(int type) {
        mOrderDialog.mPayType = type;
        mImageViewWX.setSelected(type == mOrderDialog.WECHAT);
        mImageViewZFB.setSelected(type == mOrderDialog.ALIPAY);
    }

    /**
     * 第三方支付跳转
     */
    private void commonPay() {
        CommonConst.ORDERNUM = mOrderResultInfo.orderNum;
        CommonConst.TOTALPRICE = Double.parseDouble(mOrderResultInfo.accountsPayable);

        if (mOrderDialog.mPayType == mOrderDialog.WECHAT) {
            //// TODO: 2017/7/13 微信支付
//            MicroPay microPay = new MicroPay(mContext);
//            microPay.sendPayReq();
        } else if (mOrderDialog.mPayType == mOrderDialog.ALIPAY) {
            //// TODO: 2017/7/13 支付宝支付
//            AliPay aliPay = new AliPay(mContext);
//            aliPay.pay();
        }
    }
}
