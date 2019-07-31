package com.laka.live.shopping.order.model;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.account.income.AliPayHelper;
import com.laka.live.bean.OrderOrderPayInfo;
import com.laka.live.bean.OrderPayInfoMsg;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.shopping.common.CommonConst;
import com.laka.live.shopping.order.widget.OrderDialog;
import com.laka.live.ui.widget.dialog.GenericDialog;
import com.laka.live.ui.widget.dialog.IDialogOnClickListener;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;
import com.laka.taste.wxapi.WXHelper;
import com.tencent.mm.sdk.modelbase.BaseResp;


/**
 * Created by gangqing on 2016/5/9.
 * Email:denggangqing@ta2she.com
 */
public class OrderPayHelper implements IDialogOnClickListener, View.OnClickListener {
    private OrderDialog mOrderDialog;
    private Context mContext;
    private ImageView mImageViewZFB, mImageViewWX;
    private OrderPayHelperCallBack mCallBack;

    public OrderPayHelper(Context context) {
        mContext = context;
        mOrderDialog = new OrderDialog(context, createDialog());
        mOrderDialog.setOnClickListener(this);
    }

    public void dialogShow() {
        mOrderDialog.show();
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
    public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
        if (viewId == GenericDialog.ID_BUTTON_NO) {
            dialog.dismiss();
        } else if (viewId == GenericDialog.ID_BUTTON_YES) {
            commonPay();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_zfb) {
            changePayType(mOrderDialog.ALIPAY);
        } else if (v.getId() == R.id.tv_wx) {
            changePayType(mOrderDialog.WECHAT);
        }
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
        if (mCallBack != null) {
            mCallBack.onCommonPay();
        }
        // TODO: 2017/7/14 支付 
//        if (mOrderDialog.mPayType == mOrderDialog.WECHAT) {
//            MicroPay microPay = new MicroPay(mContext);
//            microPay.sendPayReq();
//        } else if (mOrderDialog.mPayType == mOrderDialog.ALIPAY) {
//            AliPay aliPay = new AliPay(mContext);
//            aliPay.pay();
//        }
//        if (mOrderDialog.mPayType == mOrderDialog.WECHAT) {
//            tryGetWeChatPayInfo(CommonConst.ORDERID);
//        } else if (mOrderDialog.mPayType == mOrderDialog.ALIPAY) {
//            tryGetAliPayOrderInfo(CommonConst.ORDERID);
//        }
        EventBusManager.postEvent(mOrderDialog.mPayType, SubcriberTag.DO_PAY_THIRD);
    }

    public void setCallBack(OrderPayHelperCallBack callBack) {
        mCallBack = callBack;
    }

    public interface OrderPayHelperCallBack {
        void onCommonPay();
    }



}
