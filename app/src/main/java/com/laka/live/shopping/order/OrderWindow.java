/*
 * Copyright (C) 2015-2015 SJY.JIANGSU Corporation. All rights reserved
 */

package com.laka.live.shopping.order;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.laka.live.BuildConfig;
import com.laka.live.R;
import com.laka.live.account.income.AliPayHelper;
import com.laka.live.application.LiveActivityManager;
import com.laka.live.bean.OrderOrderPayInfo;
import com.laka.live.bean.OrderPayInfoMsg;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.shopping.activity.BaseShopActivity;
import com.laka.live.shopping.activity.OrderDetailActivity;
import com.laka.live.shopping.activity.OrderSuccessActivity;
import com.laka.live.shopping.bean.ShoppingBalanceBean;
import com.laka.live.shopping.bean.ShoppingCarGoodsBean;
import com.laka.live.shopping.bean.json2bean.JTBShoppingAddress;
import com.laka.live.shopping.bean.json2bean.JTBShoppingDirectBalance;
import com.laka.live.shopping.bean.json2bean.JTBShoppingOrderNum;
import com.laka.live.shopping.common.CommonConst;
import com.laka.live.shopping.framework.AbstractWindow;
import com.laka.live.shopping.framework.DefaultWindow;
import com.laka.live.shopping.framework.DeviceManager;
import com.laka.live.shopping.framework.IDefaultWindowCallBacks;
import com.laka.live.shopping.framework.Notification;
import com.laka.live.shopping.framework.adapter.NotificationDef;
import com.laka.live.shopping.network.HttpCallbackAdapter;
import com.laka.live.shopping.network.HttpCode;
import com.laka.live.shopping.order.adapter.OrderListViewAdapter;
import com.laka.live.shopping.order.model.OrderBalanceInfo;
import com.laka.live.shopping.order.model.OrderRequest;
import com.laka.live.shopping.order.model.OrderResultInfo;
import com.laka.live.shopping.order.model.OrderTemplateHolder;
import com.laka.live.shopping.order.widget.OrderPointDialog;
import com.laka.live.shopping.widget.LoadingLayout;
import com.laka.live.ui.activity.MainActivity;
import com.laka.live.ui.room.LiveRoomActivity;
import com.laka.live.ui.room.SeeReplayActivity;
import com.laka.live.ui.widget.dialog.GenericDialog;
import com.laka.live.ui.widget.dialog.IDialogOnClickListener;
import com.laka.live.util.Log;
import com.laka.live.util.NetworkUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;
import com.laka.live.util.ViewUtils;
import com.laka.taste.wxapi.WXHelper;
import com.tencent.mm.sdk.modelbase.BaseResp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhxu on 2015/11/9.
 * Email:357599859@qq.com
 */
public class OrderWindow extends DefaultWindow implements View.OnClickListener, LoadingLayout.OnBtnClickListener, CompoundButton.OnCheckedChangeListener,
        DialogInterface.OnDismissListener {

    private static final String HOLDER_TA = "#TA#";
    private static final String HOLDER_PRICE = "#price#";
    private static final String HOLDER_PERCENTAGE = "#percentage#";

    private Activity mContext;
    private AbstractWindow mWindow;

    public List<OrderTemplateHolder> mTemplateHolder = new ArrayList<>();
    private OrderListViewAdapter mAdapter;
    private OrderListView mListView;

    public TextView mTvPay, mTvTaCoin, mTvOrderTaCoinDesc, mTvTaCoinDesc, mWxPay, mAliPay;
    private TextView mBtnCommit;
    private ImageView mImageViewZFB, mImageViewWX;
    private EditText mEditText, mEditTaCoin;
    private TextView mOrderMsgText;
    private CheckBox mCheckBox, mCBWxPay, mCBAliPay;

    private OrderBalanceInfo mBalanceInfo;

    private OrderPointDialog mOrderPointDialog;

    private OrderRequest mOrderRequest;

    private LoadingLayout mLoadingLayout;
    private String mTotalPrice;
    private int mMaxUsageCoinCount = 0;

    private WXHelper mWXHelper;
    private AliPayHelper mAliPayHelper;
    private boolean isStartedPay = false;

    public final int WECHAT = 1, ALIPAY = 2;


    public OrderWindow(Context context, IDefaultWindowCallBacks callBacks, OrderBalanceInfo orderBalanceInfo) {
        super(context, callBacks);
        mContext = (Activity) context;
        mWindow = this;
        mBalanceInfo = orderBalanceInfo;
        initUI();
    }

    private void initUI() {
        setTitle(ResourceHelper.getString(R.string.order_main));
        final View view = View.inflate(mContext, R.layout.order_main, null);
        getBaseLayer().addView(view, getContentLPForBaseLayer());
        mLoadingLayout = (LoadingLayout) findViewById(R.id.order_main_loading);
        mLoadingLayout.setDefaultLoading();
        mLoadingLayout.setBtnOnClickListener(this);

        mListView = (OrderListView) findViewById(R.id.order_main_list);
        mAdapter = new OrderListViewAdapter(mContext, mTemplateHolder);
        mListView.setAdapter(mAdapter);

        mTvPay = (TextView) findViewById(R.id.order_main_amount_payable);
        mBtnCommit = (TextView) findViewById(R.id.order_main_commit);
        mBtnCommit.setOnClickListener(this);

        mTvTaCoin = (TextView) findViewById(R.id.order_main_ta_coin);
        changeTaCoin(0);
        mTvOrderTaCoinDesc = (TextView) findViewById(R.id.order_main_ta_coin_desc);
        mWxPay = (TextView) findViewById(R.id.wxPay);
        mAliPay = (TextView) findViewById(R.id.aliPay);
        mCBWxPay = (CheckBox) findViewById(R.id.cb_wxPay);
        mCBAliPay = (CheckBox) findViewById(R.id.cb_aliPay);
        mCheckBox = (CheckBox) findViewById(R.id.order_main_ta_coin_ckb);
        mWxPay.setOnClickListener(this);
        mAliPay.setOnClickListener(this);
        mCheckBox.setOnCheckedChangeListener(this);

        mOrderPointDialog = new OrderPointDialog(mContext, createPointDialog());
        mOrderPointDialog.setOnClickListener(new IDialogOnClickListener() {
            @Override
            public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                if (viewId == GenericDialog.ID_BUTTON_YES) {
                    String taCoin = mEditTaCoin.getText().toString();
                    if (StringUtils.isNotEmpty(taCoin)) {
                        if (StringUtils.parseInt(taCoin) > mMaxUsageCoinCount) {
                            ToastHelper.showToast(R.string.order_coin_msg);
                            return true;
                        }
                        changeTaCoin(StringUtils.parseInt(taCoin));
                    }
                } else {
                    mEditTaCoin.setText("");
                }
                dialog.dismiss();
                return false;
            }
        });
        mOrderPointDialog.setOnDismissListener(this);

        mBalanceInfo.payType = WECHAT;

        mEditText = (EditText) findViewById(R.id.order_main_message);
        mOrderMsgText = (TextView) findViewById(R.id.order_main_message_tv);
        mEditText.setOnClickListener(this);

        ViewUtils.setKeyBoardStateListener(view, new ViewUtils.keyboardListener() {
            @Override
            public void onKeyBoardChange(boolean isVisible) {
                if (isVisible) {
                    mEditText.requestFocus();
                    mOrderMsgText.setVisibility(GONE);
                    mEditText.setSelection(mEditText.getText().toString().length());
                } else if (!TextUtils.isEmpty(mEditText.getText())) {
                    mEditText.clearFocus();
                    mOrderMsgText.setText(mEditText.getText().toString());
                    mOrderMsgText.setVisibility(VISIBLE);
                }
            }
        });

    }

    private void changeTaCoin(int taCoin) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String totalPrice = decimalFormat.format((StringUtils.parseFloat(mTotalPrice)));
        mTvPay.setText("￥" + totalPrice);
        mBalanceInfo.totalPrice = totalPrice;/**设置支付金额**/
        mBalanceInfo.useCoinCount = String.valueOf(taCoin);/**使用的TA币数量**/
    }

    private void loadData() {

        if (isStartedPay()) {
            return;
        }
        mAdapter.resetSum();
        mTemplateHolder.clear();
        mOrderRequest = OrderRequest.getInstance();
        getAddress(mBalanceInfo);
    }

    public void update() {
        mAdapter.notifyDataSetChanged();
        mListView.resetListViewHeight();
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

    private View createPointDialog() {
        View view = View.inflate(mContext, R.layout.order_dialog_point, null);
        mEditTaCoin = (EditText) view.findViewById(R.id.order_main_ta_coin);
        mTvTaCoinDesc = (TextView) view.findViewById(R.id.tv_ta_coin_desc);
        return view;
    }


    @Override
    public void onWindowStateChange(int stateFlag) {
        super.onWindowStateChange(stateFlag);
        if (stateFlag == STATE_ON_SHOW) {
            loadData();
        } else if (stateFlag == STATE_AFTER_PUSH_IN) {
            loadData();
        }
    }

    @Override
    public void notify(Notification notification) {
        if (notification.id == NotificationDef.N_ACCOUNT_LOGIN_SUCCESS || notification.id == NotificationDef.N_ORDER_ADDRESS_CHANGED) {
            loadData();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.order_main_commit:

                if (mBalanceInfo.getShoppingAddressBean() == null) {
                    ToastHelper.showToast(ResourceHelper.getString(R.string.order_no_address_msg));
                    return;
                }
                mBalanceInfo.userRemark = mEditText.getText().toString(); // 设置用户留言

                showLoadingDialog(ResourceHelper.getString(R.string.order_pay_init));

                if (isFromGoods(mBalanceInfo)) {
                    postOrderByGoods(mBalanceInfo);
                } else {
                    postOrderByCart(mBalanceInfo);
                }
                break;
            case R.id.aliPay:
                mCBAliPay.setChecked(true);
                mCBWxPay.setChecked(false);
                mBalanceInfo.payType = ALIPAY; // 设置支付类型
                break;
            case R.id.wxPay:
                mCBAliPay.setChecked(false);
                mCBWxPay.setChecked(true);
                mBalanceInfo.payType = WECHAT; // 设置支付类型
                break;
            case R.id.order_main_message:
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                v.requestFocus();
                DeviceManager.getInstance().showInputMethod();
                break;
        }

    }

    @Override
    public void onClick() {
        loadData();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        String taCoin = mEditTaCoin.getText().toString();
        boolean hasVal = (mEditTaCoin != null && StringUtils.isNotEmpty(taCoin));
        if (!isChecked && hasVal) {
            buttonView.setChecked(false);
            mEditTaCoin.setText("");
            changeTaCoin(0);
        } else if (isChecked) {
            mOrderPointDialog.show();
        }

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        DeviceManager.getInstance().hideInputMethod();
        String taCoin = mEditTaCoin.getText().toString();
        boolean hasVal = (mEditTaCoin != null && StringUtils.isNotEmpty(taCoin));
        mCheckBox.setChecked(hasVal);

        mEditText.setFocusable(false);
        mEditText.setFocusableInTouchMode(false);
        mEditText.requestFocus();
    }

    /*******************
     * 接口访问
     ********************/
    private void getAddress(final OrderBalanceInfo balanceInfo) {

        showLoadingDialog("正在加载订单数据...");

        mOrderRequest.getAddress(new HttpCallbackAdapter() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                if (BuildConfig.DEBUG) {
                    Log.log(result);
                }
                mLoadingLayout.hide();

                JTBShoppingAddress address = (JTBShoppingAddress) obj;
                if (Utils.isEmpty(address.getData().getReceiver())) {
                    addReceiverItems(null);
                } else {
                    addReceiverItems(address);
                    mBalanceInfo.setShoppingAddressBean(address.getData());
                }

                getGoods(balanceInfo);
            }

            @Override
            public void onError(String errorStr, int code) {
                super.onError(errorStr, code);

                if (code == HttpCode.ERROR_CODE_FAILD_OPT) {
                    addReceiverItems(null);
                    getGoods(balanceInfo);
                } else {
                    dismissLoadingDialog();
                    httpError(code);
                }
            }
        });
    }

    /**
     * 封装收货人信息
     *
     * @param _address
     */
    private void addReceiverItems(JTBShoppingAddress _address) {

        if (_address == null) {
            _address = new JTBShoppingAddress();
        }

        OrderTemplateHolder templateHolder = new OrderTemplateHolder();
        templateHolder.setTempType(OrderListViewAdapter.ITEM_TYPE_TOP);
        templateHolder.setItems(_address);
        mTemplateHolder.add(templateHolder);
    }

    private void getGoods(OrderBalanceInfo balanceInfo) {
        for (ShoppingCarGoodsBean goods : balanceInfo.getList()) {
            OrderTemplateHolder templateHolder = new OrderTemplateHolder();//订单商品信息
            templateHolder.setTempType(OrderListViewAdapter.ITEM_TYPE_GOODS);
            templateHolder.setItems(goods);
            mTemplateHolder.add(templateHolder);
        }

        if (isFromGoods(balanceInfo)) {
            getDirectBalance(balanceInfo);
        } else {
            getBalance(balanceInfo);
        }
    }

    /**
     * 是否商品直接提交
     *
     * @param balanceInfo
     * @return
     */
    private boolean isFromGoods(OrderBalanceInfo balanceInfo) {

        ShoppingCarGoodsBean carGoodsBean = balanceInfo.getList().get(0);

        if (StringUtils.isEmpty(carGoodsBean.getItemId())) {
            return true;
        } else {
            return false;
        }
    }

    private void getDirectBalance(OrderBalanceInfo balanceInfo) {
        mOrderRequest.postBalanceByGoods(balanceInfo, new HttpCallbackAdapter() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                dismissLoadingDialog();

                if (BuildConfig.DEBUG) {
                    Log.log(result);
                }
                common((JTBShoppingDirectBalance) obj);
            }

            @Override
            public void onError(String errorStr, int code) {
                dismissLoadingDialog();
                httpError(code);
                super.onError(errorStr, code);
            }
        });
    }


    private void getBalance(OrderBalanceInfo balanceInfo) {

        mOrderRequest.postBalanceByCart(balanceInfo, new HttpCallbackAdapter() {
            @Override
            public <T> void onSuccess(T obj, String result) {

                dismissLoadingDialog();

                if (BuildConfig.DEBUG) {
                    Log.log(result);
                }
                common((JTBShoppingDirectBalance) obj);
            }

            @Override
            public void onError(String errorStr, int code) {
                super.onError(errorStr, code);
                dismissLoadingDialog();
                httpError(code);
            }
        });
    }

    private void common(JTBShoppingDirectBalance balance) {
        mLoadingLayout.hide();

        if (balance == null) {
            return;
        }

        ShoppingBalanceBean balanceBean = balance.getData();

        if (balanceBean == null) {
            return;
        }

        OrderTemplateHolder templateHolder = new OrderTemplateHolder();//统计信息
        templateHolder.setTempType(OrderListViewAdapter.ITEM_TYPE_STATS);
        templateHolder.setItems(balance);
        mTemplateHolder.add(templateHolder);

        mTotalPrice = balanceBean.getTotalPrice();
        mTvPay.setText("￥" + mTotalPrice);
        if (!StringUtils.isEmpty(balanceBean.getUsageCoinCount())) {
            mMaxUsageCoinCount = StringUtils.parseInt(balanceBean.getUsageCoinCount());
            String countStr = ResourceHelper.getString(R.string.order_main_usage_coin_count);
            countStr = countStr.replace("#count#", "" + mMaxUsageCoinCount);
            mTvTaCoinDesc.setText(countStr);
        }

        mBalanceInfo.totalPrice = mTotalPrice;/**设置支付金额**/
        mBalanceInfo.goodsPrice = balanceBean.getGoodsPrice();/**设置商品单个金额**/

        update();
    }

    /**
     * 获取第三方支付参数的方法
     *
     * @param balanceInfo
     */
    public void postOrderByCart(OrderBalanceInfo balanceInfo) {

        mOrderRequest.postOrderByCart(balanceInfo, new HttpCallbackAdapter() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                if (BuildConfig.DEBUG) {
                    Log.log(result);
                }
                dismissLoadingDialog();
                commonPay((JTBShoppingOrderNum) obj);
            }

            @Override
            public void onError(String errorStr, int code) {
                dismissLoadingDialog();
                error(code);
                super.onError(errorStr, code);
            }
        });
    }

    /**
     * 从商品直接下单
     */
    public void postOrderByGoods(OrderBalanceInfo balanceInfo) {

        mOrderRequest.postOrderByGoods(balanceInfo, new HttpCallbackAdapter() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                if (BuildConfig.DEBUG) {
                    Log.log(result);
                }
                dismissLoadingDialog();
                commonPay((JTBShoppingOrderNum) obj);
            }

            @Override
            public void onError(String errorStr, int code) {
                super.onError(errorStr, code);
                dismissLoadingDialog();
                error(code);
            }
        });
    }

    private void error(int code) {

        if (code == HttpCode.ERROR_CODE_201) {
            ToastHelper.showToast(R.string.order_post_error_604);
        } else if (code == HttpCode.ERROR_CODE_200 || code == HttpCode.ERROR_CODE_202) {
            ToastHelper.showToast(R.string.order_post_error_605);
        } else if (code == HttpCode.ERROR_CODE_INVALID_TOKEN) {
            ToastHelper.showToast(R.string.community_invalided_token);
        } else if (code == HttpCode.ERROR_CODE_104) {
            ToastHelper.showToast(R.string.order_post_error_607);
        } else if (code == HttpCode.ERROR_CODE_FROZEN_USER) {
            ToastHelper.showToast(R.string.community_frozen_account);
        } else {
            ToastHelper.showToast(R.string.shopping_network_error_retry);
        }
    }

    /**
     * 第三方支付跳转
     *
     * @param orderNum
     */
    private void commonPay(JTBShoppingOrderNum orderNum) {

        CommonConst.ORDERID = orderNum.getData().getOrderId();
        CommonConst.ORDERNUM = orderNum.getData().getOrderNo();
        CommonConst.TOTALPRICE = Double.parseDouble(mBalanceInfo.totalPrice);

        if (!NetworkUtil.isNetworkOk(mContext)) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.network_error_tips));
            return;
        }

        if (mBalanceInfo.payType == WECHAT) {
            tryGetWeChatPayInfo(CommonConst.ORDERID);
        } else if (mBalanceInfo.payType == ALIPAY) {
            tryGetAliPayOrderInfo(CommonConst.ORDERID);
        }

    }


    // 获取微信支付参数
    private void tryGetWeChatPayInfo(String orderId) {

        if (mWXHelper == null) {
            mWXHelper = new WXHelper(mContext);
        }

        if (!mWXHelper.isInstallWeChat()) {
            ToastHelper.showToast(R.string.please_install_weixin);
            return;
        }

        showLoadingDialog("正在加载...");
        DataProvider.getOrderWechatPayInfo(orderId,
                new GsonHttpConnection.OnResultListener<OrderPayInfoMsg>() {
                    @Override
                    public void onSuccess(OrderPayInfoMsg msg) {
                        handleGetWeChatPayInfoSuccess(msg);
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg, String command) {
                        dismissLoadingDialog();
                    }
                });
    }

    // 调起微信支付
    private void handleGetWeChatPayInfoSuccess(OrderPayInfoMsg msg) {
        dismissLoadingDialog();
        if (msg == null) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.my_get_order_info_fail_tips));
            return;
        }
        if (mWXHelper == null) {
            mWXHelper = new WXHelper(mContext);
        }
        if (!mWXHelper.isInstallWeChat()) {
            ToastHelper.showToast(R.string.please_install_weixin);
            return;
        }
        setStartedPay(true);
        mWXHelper.sendPayReq(OrderOrderPayInfo.toPayReq(msg.getData().getPayParams()));
    }

    // 微信支付成功的回调
    public void handleOnWeChatPayResultCallback(int resultCode) {
        if (resultCode == BaseResp.ErrCode.ERR_OK) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.pay_success_tips));
            paySuccess();
            return;
        }
        if (resultCode == WXHelper.WE_CHAT_PAY_TYPE_CANCEL) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.pay_cancel_tips));
            payFailed();
            return;
        }

        payFailed();

    }

    // 调起支付宝支付
    private void tryGetAliPayOrderInfo(String orderId) {

        showLoadingDialog("正在加载...");
        DataProvider.getOrderAliPayInfo(orderId,
                new GsonHttpConnection.OnResultListener<OrderPayInfoMsg>() {
                    @Override
                    public void onSuccess(OrderPayInfoMsg msg) {
                        setStartedPay(true);
                        mAliPayHelper.startPayTask(mContext, msg.getData().getAliPayParams());
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg, String command) {
                        dismissLoadingDialog();
                    }
                });

        mAliPayHelper = new AliPayHelper(new AliPayHelper.AliPayCallback() {
            @Override
            public void onSuccess(String payResultInfo) {
                dismissLoadingDialog();
                ToastHelper.showToast(ResourceHelper.getString(R.string.pay_success_tips));
                // 支付宝支付成功后的回调
                paySuccess();
            }

            @Override
            public void onCancel() {
                dismissLoadingDialog();
                ToastHelper.showToast(ResourceHelper.getString(R.string.pay_cancel_tips));
                payFailed();
            }

            @Override
            public void onWait() {
                dismissLoadingDialog();
                ToastHelper.showToast(ResourceHelper.getString(R.string.pay_wait_tips));
            }

            @Override
            public void onFailed() {
                dismissLoadingDialog();
                ToastHelper.showToast(ResourceHelper.getString(R.string.pay_fail_tips));
                payFailed();
            }
        });

    }

    // 支付失败
    private void payFailed() {
        OrderResultInfo orderResultInfo = new OrderResultInfo();
        orderResultInfo.orderId = CommonConst.ORDERID;
        OrderDetailActivity.startActivity(mContext, orderResultInfo);
        LiveActivityManager.getInstance().popAndIgnoredActivity(MainActivity.class, LiveRoomActivity.class, SeeReplayActivity.class);
        setStartedPay(false);

        EventBusManager.postEvent(0, SubcriberTag.PAY_RESULT_BACK);
    }

    // 支付成功
    private void paySuccess() {
        OrderSuccessActivity.startActivity(mContext, mBalanceInfo.getShoppingAddressBean());
        LiveActivityManager.getInstance().popAndIgnoredActivity(MainActivity.class, LiveRoomActivity.class, SeeReplayActivity.class);
        setStartedPay(false);
        EventBusManager.postEvent(0, SubcriberTag.PAY_RESULT_BACK);
    }

    private void httpError(int code) {
        if (mTemplateHolder.isEmpty() && code == HttpCode.ERROR_NETWORK) {
            mLoadingLayout.setDefaultNetworkError(true);
        } else {
            error(code);
        }
    }

    public void showLoadingDialog(String msg) {
        ((BaseShopActivity) mContext).showNewDialog(msg);
    }

    public void dismissLoadingDialog() {
        ((BaseShopActivity) mContext).dismissDialog();
    }

    public boolean isStartedPay() {
        return isStartedPay;
    }

    public void setStartedPay(boolean startedPay) {
        isStartedPay = startedPay;
    }


}
