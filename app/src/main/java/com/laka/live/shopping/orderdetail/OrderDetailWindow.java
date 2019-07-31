/*
 * Copyright (C) 2015-2015 SJY.JIANGSU Corporation. All rights reserved
 */

package com.laka.live.shopping.orderdetail;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.laka.live.BuildConfig;
import com.laka.live.R;
import com.laka.live.config.SystemConfig;
import com.laka.live.dao.DbManger;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.shopping.bean.ShoppingOrderDetailBean;
import com.laka.live.shopping.bean.ShoppingOrderDetailGoodsBean;
import com.laka.live.shopping.bean.ShoppingOrderLogisticsBean;
import com.laka.live.shopping.bean.json2bean.JTBShoppingOrderDetail;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsDetailBean;
import com.laka.live.shopping.common.CommonConst;
import com.laka.live.shopping.framework.AbstractWindow;
import com.laka.live.shopping.framework.DefaultWindow;
import com.laka.live.shopping.framework.IDefaultWindowCallBacks;
import com.laka.live.shopping.framework.MsgDispatcher;
import com.laka.live.shopping.framework.Notification;
import com.laka.live.shopping.framework.NotificationCenter;
import com.laka.live.shopping.framework.adapter.MenuItemIdDef;
import com.laka.live.shopping.framework.adapter.MsgDef;
import com.laka.live.shopping.framework.adapter.NotificationDef;
import com.laka.live.shopping.model.TemplateHolder;
import com.laka.live.shopping.network.HttpCallbackAdapter;
import com.laka.live.shopping.network.HttpCode;
import com.laka.live.shopping.order.model.OrderResultInfo;
import com.laka.live.shopping.order.model.OrderReviewInfo;
import com.laka.live.shopping.order.widget.OrderDialog;
import com.laka.live.shopping.orderdetail.adapter.OrderDetailRecyclerViewAdapter;
import com.laka.live.shopping.orderdetail.model.OrderDetailRequest;
import com.laka.live.shopping.widget.LoadingLayout;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.chat.ChatMessageActivity;
import com.laka.live.ui.widget.AlphaTextView;
import com.laka.live.ui.widget.dialog.GenericDialog;
import com.laka.live.ui.widget.dialog.IDialogOnClickListener;
import com.laka.live.ui.widget.dialog.LoadingDialog;
import com.laka.live.util.Common;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;
import com.laka.taste.wxapi.WXHelper;
import com.tencent.mm.sdk.modelbase.BaseResp;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhxu on 2015/11/9.
 * Email:357599859@qq.com
 */
public class OrderDetailWindow extends DefaultWindow implements View.OnClickListener, IDialogOnClickListener,
        LoadingLayout.OnBtnClickListener,EventBusManager.OnEventBusListener {

    private final static String TAG = "OrderDetailWindow";

    private Activity mContext;
    private AbstractWindow mWindow;

    private RecyclerView mRecyclerView;
    private OrderDetailRecyclerViewAdapter mAdapter;
    private List<TemplateHolder> mList = new ArrayList<>();

    private OrderDialog mOrderDialog;
    private ImageView mImageViewZFB, mImageViewWX;

    private AlphaTextView mAtvCancel, mAtvDel, mAtvGet, mAtvPoint, mAtvPay;

    private OrderDetailRequest mOrderDetailRequest;
    private OrderResultInfo mOrderResultInfo;
    private OrderReviewInfo mOrderReviewInfo;

    private LoadingDialog mDialog;
    private OrderOperationDialog mTextDialog;
    private LoadingLayout mLoadingLayout;

    private final int STATE_2 = 2, STATE_3 = 3, STATE_4 = 4, STATE_5 = 5, STATE_6 = 6, STATE_7 = 7, STATE_0 = 0;
    private int mCurrState = STATE_2;
    BaseActivity mActivity;
    public OrderDetailWindow(Context context, IDefaultWindowCallBacks callBacks, OrderResultInfo resultInfo) {
        super(context, callBacks);
        mContext = (Activity) context;
        mWindow = this;
        setLaunchMode(LAUNCH_MODE_SINGLE_INSTANCE);

        mOrderResultInfo = resultInfo;
        mOrderReviewInfo = new OrderReviewInfo();

        initUI();
        initActionBar();
        EventBusManager.register(this);
    }
    LinearLayoutManager layoutManager;
    private void initUI() {
        setTitle(ResourceHelper.getString(R.string.order_detail_title));
        View view = View.inflate(mContext, R.layout.order_detail, null);
        getBaseLayer().addView(view, getContentLPForBaseLayer());

        mLoadingLayout = (LoadingLayout) findViewById(R.id.order_detail_loading);
        mLoadingLayout.setDefaultLoading();
        mLoadingLayout.setBtnOnClickListener(this);

        mDialog = new LoadingDialog(mContext);
        mDialog.setMessage(R.string.order_pay_init);
        mTextDialog = new OrderOperationDialog(mContext);
        mTextDialog.setOnClickListener(this);
        mOrderDialog = new OrderDialog(mContext, createDialog());
        mOrderDialog.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.order_recycler_view);
        mAdapter = new OrderDetailRecyclerViewAdapter(mContext, mList);
        mRecyclerView.setAdapter(mAdapter);
        layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);

        mAtvCancel = (AlphaTextView) findViewById(R.id.order_btn_cancel);
        mAtvCancel.setOnClickListener(this);
        mAtvDel = (AlphaTextView) findViewById(R.id.order_btn_delete);
        mAtvDel.setOnClickListener(this);
        mAtvGet = (AlphaTextView) findViewById(R.id.order_btn_get);
        mAtvGet.setOnClickListener(this);
        mAtvPoint = (AlphaTextView) findViewById(R.id.order_btn_review);
        mAtvPoint.setOnClickListener(this);
        mAtvPay = (AlphaTextView) findViewById(R.id.order_btn_pay);
        mAtvPay.setOnClickListener(this);

        mOrderDetailRequest = OrderDetailRequest.getInstance();
    }

    private void initActionBar() {
//        ArrayList<TitleBarActionItem> actionList = new ArrayList<>(1);
//        TitleBarActionItem item = new TitleBarActionItem(getContext());
//        item.setText("客服");
//        item.setTextDrawable(0, R.drawable.ta_she_secretary_service_gray);
//        item.setItemId(MenuItemIdDef.TITLEBAR_SHOPPING_SECRETARY);
//        actionList.add(item);
//        DefaultTitleBar titleBar = (DefaultTitleBar) getTitleBar();
//        titleBar.setActionItems(actionList);
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

    private void changePayType(int type) {
        mOrderDialog.mPayType = type;
        mImageViewWX.setSelected(type == mOrderDialog.WECHAT);
        mImageViewZFB.setSelected(type == mOrderDialog.ALIPAY);
    }

    private void update() {
        mAdapter.notifyDataSetChanged();
        layoutManager.scrollToPosition(0);
    }

    private void notifyOrderListReload() {
        Notification notification = Notification.obtain(NotificationDef.N_ORDER_LIST_CHANGED);
        NotificationCenter.getInstance().notify(notification);
    }

    private void notifyRefreshShoppingCar() {
        Notification notification = Notification.obtain(NotificationDef.N_SHOPPING_CAR_COUNT_CHANGE);
        NotificationCenter.getInstance().notify(notification);
    }

    @Override
    protected void onWindowStateChange(int stateFlag) {
        super.onWindowStateChange(stateFlag);
        if (stateFlag == STATE_AFTER_PUSH_IN) {
            NotificationCenter.getInstance().register(this, NotificationDef.N_ORDER_LIST_CHANGED);
            getOrderDetail();
        } else if (stateFlag == STATE_ON_DETACH) {
            NotificationCenter.getInstance().unregister(this, NotificationDef.N_ORDER_LIST_CHANGED);
        }
    }

    @Override
    public void notify(Notification notification) {
        if (notification.id == NotificationDef.N_ORDER_LIST_CHANGED && mCurrState != STATE_5) {//状态为5时 删除信息  直接关闭 不需要刷新当前页
            getOrderDetail();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_zfb) {//Dialog 支付宝
            changePayType(mOrderDialog.ALIPAY);
        } else if (v.getId() == R.id.tv_wx) {//Dialog 微支付
            changePayType(mOrderDialog.WECHAT);
        } else if (v.getId() == R.id.order_btn_cancel) {

            //统计PRODUC_ORDER_CANCEL
//            StatsModel.stats(StatsKeyDef.PRODUC_ORDER_CANCEL);

            mCurrState = STATE_2;
            mTextDialog.show();
        } else if (v.getId() == R.id.order_btn_delete) {
            mCurrState = STATE_5;
            mTextDialog.show();
        } else if (v.getId() == R.id.order_btn_get) {

            //统计PRODUCT_ORDER_RECEIVE
//            StatsModel.stats(StatsKeyDef.PRODUCT_ORDER_RECEIVE);

            mCurrState = STATE_4;
            mTextDialog.show();
        } else if (v.getId() == R.id.order_btn_pay) {
            mCurrState = STATE_0;
//            mOrderDialog.show();
            goToPay();
        } else if (v.getId() == R.id.order_btn_review) {

            //统计PRODUCT_ORDER_REVIEW
//            StatsModel.stats(StatsKeyDef.PRODUCT_ORDER_REVIEW);

            //统计PRODUCT_REVIEWLIST_VIEW  正在进行中的订单  IOS有  ANDROID没有

//            StatsModel.stats(StatsKeyDef.PRODUCT_REVIEWLIST_VIEW);
            Message message = Message.obtain();
            message.what = MsgDef.MSG_SHOW_ORDER_REVIEW_LIST_WINDOW;
            message.obj = mOrderReviewInfo;
            MsgDispatcher.getInstance().sendMessage(message); //旧的跳转 到商品评价界面

//            OpenEvaluateHelper helper = new OpenEvaluateHelper();
//            helper.fromType = CommunityConstant.OPEN_FROM_TYPE_HOMEPAGE;
//            helper.orderId = mOrderResultInfo.orderId;
//            Message message = new Message();
//            message.what = MsgDef.MSG_SHOW_EVALUATE_WINDOW;
//            message.obj = helper;
//            MsgDispatcher.getInstance().sendMessage(message);
        }
    }

    private void goToPay() {
        int payType = mAdapter.getCurPayType();
        if (payType == 0) {
            ToastHelper.showToast("请选择支付方式");
            return;
        }
        EventBusManager.postEvent(payType, SubcriberTag.DO_PAY_THIRD);
    }

    @Override
    public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
        if (viewId == GenericDialog.ID_BUTTON_NO) {
            dialog.dismiss();
        } else if (viewId == GenericDialog.ID_BUTTON_YES) {
            if (mCurrState == STATE_2) {
                postOrderState(OrderDetailRequest.ACTION_CANCEL);
            } else if (mCurrState == STATE_4) {
                postOrderState(OrderDetailRequest.ACTION_CONFIRM);
            } else if (mCurrState == STATE_5) {
                postOrderState(OrderDetailRequest.ACTION_DELETE);
            } else if (mCurrState == STATE_0) {
                commonPay();
            } else if (mCurrState == STATE_7) {
            }
        }
        return false;
    }

    @Override
    public void onTitleBarActionItemClick(int itemId) {
        if (itemId == MenuItemIdDef.TITLEBAR_SHOPPING_SECRETARY) {
            goToTASecretaryDialog();
        }
    }

    private void goToTASecretaryDialog() {
        if (mOrderReviewInfo.goodsList.isEmpty()) {
            return;
        }

        ShoppingOrderDetailGoodsBean goodsBean = mOrderReviewInfo.goodsList.get(0);
        if (goodsBean == null) {
            return;
        }
        ShoppingGoodsDetailBean shopGood = new ShoppingGoodsDetailBean();
//        shopGood.setGoodsId(Integer.parseInt(goodsBean.getGoodsId()));
        shopGood.setTitle(goodsBean.getTitle());
        shopGood.setThumbUrl(goodsBean.getThumbUrl());
        ChatMessageActivity.startActivity(mContext,shopGood);
//        TASecretaryDialogHelper helper = new TASecretaryDialogHelper();
//        if (mOrderReviewInfo.goodsList.isEmpty()) {
//            return;
//        }
//
//        ShoppingOrderDetailGoodsBean goodsBean = mOrderReviewInfo.goodsList.get(0);
//        if (goodsBean == null) {
//            return;
//        }
//        ChatParams chatParams = new ChatParams();
//        chatParams.setImageUrl(goodsBean.getThumbUrl());
//        chatParams.setPrice(mOrderResultInfo.accountsPayable);
//        chatParams.setSpId(goodsBean.getGoodsId());
//        chatParams.setTitle(goodsBean.getTitle());
//        chatParams.setSpId(mOrderResultInfo.orderNum);
//        chatParams.setTime(mOrderResultInfo.createTime);
//        chatParams.setType(ChatParams.TYPE_ORDER);//订单类
//        helper.requestChatDetailsId(chatParams);
    }

    @Override
    public void onClick() {
        getOrderDetail();
    }

    /*******************
     * 接口访问
     ********************/
    private void postOrderState(String action) {
        mOrderDetailRequest.postOrderState(action, mOrderResultInfo.orderId, new HttpCallbackAdapter() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                if (BuildConfig.DEBUG) {
                    Log.v(TAG, result);
                }
                if (mCurrState == STATE_5) {
                    ToastHelper.showToast(  ResourceHelper.getString(R.string.order_state_changed_del));
//                    mCallBacks.onWindowExitEvent(mWindow, true);
                    mActivity.finish();
                    EventBusManager.postEvent(0, SubcriberTag.REFRESH_ORDER_LIST);
                } else {
                    ToastHelper.showToast(   ResourceHelper.getString(R.string.order_state_changed));
                    getOrderDetail();
                }
                notifyOrderListReload();
                notifyRefreshShoppingCar();
            }
        });
    }

    private void btnDisplayByState(int state) {
        mAtvCancel.setVisibility(GONE);
        mAtvDel.setVisibility(GONE);
        mAtvGet.setVisibility(GONE);
        mAtvPay.setVisibility(GONE);
        mAtvPoint.setVisibility(GONE);
        if (state == STATE_2) {

            //统计PRODUCT_ORDER_UNPAY
//            StatsModel.stats(StatsKeyDef.PRODUCT_ORDER_UNPAY);

            mAtvCancel.setVisibility(VISIBLE);
            mAtvPay.setVisibility(VISIBLE);
        } else if (state == STATE_4) {

            //统计PRODUCT_ORDER_DELIEVERED
//            StatsModel.stats(StatsKeyDef.PRODUCT_ORDER_DELIEVERED);

            mAtvGet.setVisibility(VISIBLE);
        } else if (state == STATE_5) {
            mAtvDel.setVisibility(VISIBLE);
        } else if (state == STATE_7) {

            //统计PRODUCT_ORDER_UNREVIEWED
//            StatsModel.stats(StatsKeyDef.PRODUCT_ORDER_UNREVIEWED);

            mAtvPoint.setVisibility(VISIBLE);
        }
    }

    private void getOrderDetail() {

        CommonConst.ORDERID = mOrderResultInfo.orderId;//设置当前订单

        mOrderDetailRequest.getOrderDetail(mOrderResultInfo.orderId, new HttpCallbackAdapter() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                if (BuildConfig.DEBUG) {
                    Log.v(TAG, result);
                }
                com.laka.live.util.Log.d(TAG," getOrderDetail result="+result);
                mLoadingLayout.hide();
                mList.clear();

                JTBShoppingOrderDetail orderDetail = (JTBShoppingOrderDetail) obj;
                ShoppingOrderDetailBean orderDetailBean = orderDetail.getData();
                if (orderDetailBean == null) {
                    httpError(0);
                    return;
                }

                mOrderResultInfo.accountsPayable = orderDetailBean.getTotalPrice();
                mOrderResultInfo.orderNum = orderDetailBean.getOrderNo();
                mOrderResultInfo.createTime = Utils.LONG_DATE_FORMATER.format(new Date(orderDetailBean.getCreatetime() * 1000));

                mOrderReviewInfo.orderId = StringUtils.parseInt(orderDetail.getData().getOrderId());//点评传参
                mOrderReviewInfo.goodsList = orderDetailBean.getGoods();

                btnDisplayByState(orderDetailBean.getOrderStatus());//按钮状态

                TemplateHolder templateHolderTop = new TemplateHolder();//状态
                templateHolderTop.setTempType(OrderDetailRecyclerViewAdapter.TYPE_TOP);
                templateHolderTop.setItems(orderDetailBean);
                mList.add(templateHolderTop);

                for (ShoppingOrderLogisticsBean orderLogisticsBean : orderDetailBean.getLogistics()) {//物流信息
                    TemplateHolder templateHolder = new TemplateHolder();
                    templateHolder.setTempType(OrderDetailRecyclerViewAdapter.TYPE_LOGISTICS);
                    templateHolder.setItems(orderLogisticsBean);
                    mList.add(templateHolder);
                }

                TemplateHolder templateHolderAddress = new TemplateHolder();//收货人信息
                templateHolderAddress.setTempType(OrderDetailRecyclerViewAdapter.TYPE_ADDRESS);
                templateHolderAddress.setItems(orderDetailBean);
                mList.add(templateHolderAddress);

                if (!StringUtils.isEmpty(orderDetailBean.getUserRemark())) {
                    TemplateHolder templateHolderMessage = new TemplateHolder();//买家留言
                    templateHolderMessage.setTempType(OrderDetailRecyclerViewAdapter.TYPE_MESSAGE);
                    templateHolderMessage.setItems(orderDetailBean);
                    mList.add(templateHolderMessage);
                }

                for (ShoppingOrderDetailGoodsBean shoppingGoodsBean : orderDetailBean.getGoods()) {//订单商品
                    TemplateHolder templateHolder = new TemplateHolder();
                    templateHolder.setTempType(OrderDetailRecyclerViewAdapter.TYPE_GOODS);
                    templateHolder.setItems(shoppingGoodsBean);
                    mList.add(templateHolder);
                }

                TemplateHolder templateHolderBottom = new TemplateHolder();//付款信息
                templateHolderBottom.setTempType(OrderDetailRecyclerViewAdapter.TYPE_BOTTOM);
                templateHolderBottom.setItems(orderDetailBean);
                mList.add(templateHolderBottom);

                TemplateHolder templateHolderOrder = new TemplateHolder();//订单信息
                templateHolderOrder.setTempType(OrderDetailRecyclerViewAdapter.TYPE_ORDER);
                templateHolderOrder.setItems(orderDetailBean);
                mList.add(templateHolderOrder);

                update();
            }

            @Override
            public void onError(String errorStr, int code) {
                super.onError(errorStr, code);
                httpError(code);
            }
        });
    }

    /**
     * 第三方支付跳转
     */
    private void commonPay() {
        // TODO: 2017/7/14  第三方支付跳转
//        CommonConst.ORDERNUM = mOrderResultInfo.orderNum;
//        CommonConst.TOTALPRICE = Double.parseDouble(mOrderResultInfo.accountsPayable);
//
//        if (mOrderDialog.mPayType == mOrderDialog.WECHAT) {
//            MicroPay microPay = new MicroPay(mContext);
//            microPay.sendPayReq();
//        } else if (mOrderDialog.mPayType == mOrderDialog.ALIPAY) {
//            AliPay aliPay = new AliPay(mContext);
//            aliPay.pay();
//        }
    }

    private void httpError(int code) {
        if (mList.isEmpty() && code == HttpCode.ERROR_NETWORK) {
            mLoadingLayout.setDefaultNetworkError(true);
        } else {
            mLoadingLayout.setDefaultNoData();
        }
    }

    public void handleOnWeChatPayResultCallback(int resultCode) {
        if (resultCode == BaseResp.ErrCode.ERR_OK) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.pay_success_tips));
            paySuccess();
            return;
        }
        if (resultCode == WXHelper.WE_CHAT_PAY_TYPE_CANCEL) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.pay_cancel_tips));
            payCancel();
            return;
        }
        ToastHelper.showToast(ResourceHelper.getString(R.string.pay_fail_tips));
        payFailed();
    }

    // 支付成功
    private void paySuccess() {
//        ToastHelper.showToast(ResourceHelper.getString(R.string.pay_success_tips));
        getOrderDetail();
//        OrderSuccessActivity.startActivity(mContext,mBalanceInfo.getShoppingAddressBean());
//        LiveActivityManager.getInstance().popUntilActivity(MainActivity.class);

        EventBusManager.postEvent(0, SubcriberTag.PAY_RESULT_BACK);
    }

    // 支付失败
    private void payFailed() {
//        ToastHelper.showToast("购买失败，请重新尝试！");
//        OrderResultInfo orderResultInfo = new OrderResultInfo();
//        orderResultInfo.orderId = CommonConst.ORDERID;
//        OrderDetailActivity.startActivity(mContext, orderResultInfo);
//        LiveActivityManager.getInstance().popUntilActivity(MainActivity.class);
        EventBusManager.postEvent(0, SubcriberTag.PAY_RESULT_BACK);
    }

    // 支付失败
    private void payCancel() {
//        ToastHelper.showToast("支付取消");
//        OrderResultInfo orderResultInfo = new OrderResultInfo();
//        orderResultInfo.orderId = CommonConst.ORDERID;
//        OrderDetailActivity.startActivity(mContext, orderResultInfo);
//        LiveActivityManager.getInstance().popUntilActivity(MainActivity.class);
        EventBusManager.postEvent(0, SubcriberTag.PAY_RESULT_BACK);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PostEvent event) {
        if(SubcriberTag.PAY_SUCCESS.equals(event.tag)){
            getOrderDetail();
        }else if(SubcriberTag.PAY_FAIL.equals(event.tag)){
            payFailed();
        }else if(SubcriberTag.PAY_CANCEL.equals(event.tag)){
            payCancel();
        }
    }

    public void destory() {
        EventBusManager.unregister(this);
    }
}
