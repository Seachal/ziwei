package com.laka.live.account.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.account.income.AliPayHelper;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.PayProduct;
import com.laka.live.bean.Product;
import com.laka.live.bean.WechatPayInfo;
import com.laka.live.config.SystemConfig;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.Msg;
import com.laka.live.msg.OrderInfoMsg;
import com.laka.live.msg.ProductsListMsg;
import com.laka.live.msg.SingleMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.DataProviderRoom;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.adapter.TabsAdapter;
import com.laka.live.ui.fragment.ProductFragment;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.util.Log;
import com.laka.live.util.NetworkUtil;
import com.laka.live.util.NumberUtils;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ToastHelper;
import com.laka.taste.wxapi.WXHelper;
import com.tencent.mm.sdk.modelbase.BaseResp;

import org.greenrobot.eventbus.Subscribe;

import java.text.NumberFormat;

/**
 * 我的钱包
 */
public class MyCoinsActivity extends BaseActivity implements GsonHttpConnection.OnResultListener<ProductsListMsg>,
        TabHost.OnTabChangeListener, ProductFragment.OnListFragmentInteractionListener, View.OnClickListener {
    private static final String TAG = "MyCoinsActivity";
    private static final String WE_CHAT = "we_chat";
    private static final String ALI_PAY = "ali_pay";

    private TextView mCurrentDiamonText;
    private TabHost mTabHost;
    private RadioButton mWeChatRadioButton;
    private RadioButton mAliPayRadioButton;

    private ImageView mWeChatBottom;
    private ImageView mAliPayBottom;

    private boolean mIsInit = false;
    private boolean isStartWeChatPay = false;
    private WXHelper mWXHelper;
    private AliPayHelper mAliPayHelper;

    public static void startActivity(Context activity) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, MyCoinsActivity.class);
//        ActivityCompat.startActivity(activity, intent, null);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coins);
        initDefView();
        addErrorViewClickListener();
        loadData();
    }

    private void initAliPayHelper() {
        mAliPayHelper = new AliPayHelper(new AliPayHelper.AliPayCallback() {
            @Override
            public void onSuccess(String payResultInfo) {
                dismissLoadingsDialog();
                ToastHelper.showToast(ResourceHelper.getString(R.string.pay_success_tips));
                loadData();//刷新钻石
            }

            @Override
            public void onCancel() {
                dismissLoadingsDialog();
                ToastHelper.showToast(ResourceHelper.getString(R.string.pay_cancel_tips));
            }

            @Override
            public void onWait() {
                dismissLoadingsDialog();
                ToastHelper.showToast(ResourceHelper.getString(R.string.pay_wait_tips));
            }

            @Override
            public void onFailed() {
                dismissLoadingsDialog();
                ToastHelper.showToast(ResourceHelper.getString(R.string.pay_fail_tips));
            }
        });
    }

    private void addErrorViewClickListener() {
        mErrorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mErrorState == PageListLayout.ErrorState.ERROR_STATE_EMPTY) {
                    return;
                }
                loadData();
            }
        });
    }

    private void init(ProductsListMsg listMsg) {
        if (mIsInit) {
            updateDrillCountText(listMsg);
            return;
        }
        mIsInit = true;
        initAliPayHelper();
        LinearLayout payWayLayout = (LinearLayout) findViewById(R.id.pay_way_layout);
        mCurrentDiamonText = (TextView) findViewById(R.id.coins);
        mWeChatRadioButton = (RadioButton) findViewById(R.id.wechat);
        mAliPayRadioButton = (RadioButton) findViewById(R.id.alipay);
        mWeChatBottom = (ImageView) findViewById(R.id.wechat_bottom);
        mAliPayBottom = (ImageView) findViewById(R.id.alipay_bottom);

        View weChatView = findViewById(R.id.wechat_layout);
        weChatView.setOnClickListener(this);
        View aliPayView = findViewById(R.id.alipay_layout);
        aliPayView.setOnClickListener(this);
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        TabsAdapter tabsAdapter = new TabsAdapter(this, mTabHost, viewPager, this);
        PayProduct payProduct = listMsg.getPayProduct();
        Bundle weChatBundle = new Bundle();
        weChatBundle.putParcelableArrayList(ProductFragment.ARG_DATAS, payProduct.getWechatPayList());
        Bundle aliPayBundle = new Bundle();
        aliPayBundle.putParcelableArrayList(ProductFragment.ARG_DATAS, payProduct.getAliPayList());
        mTabHost.setCurrentTabByTag(WE_CHAT);

        if (!SystemConfig.getInstance().isShowAliPay() && !SystemConfig.getInstance().isShowWeixinPay()) {
            payWayLayout.setVisibility(View.GONE);
        } else {
            payWayLayout.setVisibility(View.VISIBLE);
        }

        if (SystemConfig.getInstance().isShowAliPay()) {
            tabsAdapter.addTab(mTabHost.newTabSpec(ALI_PAY).setIndicator(getString(R.string.alipay)),
                    ProductFragment.class, aliPayBundle);
            mAliPayRadioButton.setVisibility(View.VISIBLE);
            aliPayView.setVisibility(View.VISIBLE);
        } else {
            mAliPayRadioButton.setVisibility(View.GONE);
            aliPayView.setVisibility(View.GONE);
        }

        if (SystemConfig.getInstance().isShowWeixinPay()) {
            mWeChatRadioButton.setVisibility(View.VISIBLE);
            weChatView.setVisibility(View.VISIBLE);
            tabsAdapter.addTab(mTabHost.newTabSpec(WE_CHAT).setIndicator(getString(R.string.wechat)),
                    ProductFragment.class, weChatBundle);
        } else {
            mWeChatRadioButton.setVisibility(View.GONE);
            weChatView.setVisibility(View.GONE);
        }

        updateDrillCountText(listMsg);
    }

    private void updateDrillCountText(ProductsListMsg listMsg) {
        if (listMsg == null) {
            return;
        }

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setGroupingUsed(false);
        mCurrentDiamonText.setText(String.valueOf(numberFormat.format(listMsg.getBalance())));
        AccountInfoManager.getInstance().updateCurrentAccountCoins(listMsg.getBalance());
        EventBusManager.postEvent(listMsg.getBalance(), SubcriberTag.REFRESH_LAST_KAZUAN);
        Log.d(TAG, "充值成功刷新卡钻 REFRESH_LAST_KAZUAN=" + listMsg.getBalance());
    }

    private void loadData() {
        DataProvider.queryProduct(this, this);
        showLoading();
    }


    @Override
    public void onSuccess(ProductsListMsg listMsg) {
        showData();
        init(listMsg);
    }

    @Override
    public void onFail(int errorCode, String errorMsg, String command) {
        if (errorCode == Msg.NETWORK_ERROR_NETWORK_ERROR) {
            showNetworkError();
        } else {
            showDataException();
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        if (TextUtils.equals(tabId, WE_CHAT)) {
            mWeChatRadioButton.setChecked(true);
            mAliPayRadioButton.setChecked(false);
            mWeChatBottom.setVisibility(View.VISIBLE);
            mAliPayBottom.setVisibility(View.GONE);
            AnalyticsReport.onEvent(this, AnalyticsReport.MY_COINS_ACTIVITY_WECHAT_BUTTON_CLICK_EVENT_ID);
        } else {
            mWeChatRadioButton.setChecked(false);
            mAliPayRadioButton.setChecked(true);
            mAliPayBottom.setVisibility(View.VISIBLE);
            mWeChatBottom.setVisibility(View.GONE);
            AnalyticsReport.onEvent(this, AnalyticsReport.MY_COINS_ACTIVITY_ALIPAY_BUTTON_CLICK_EVENT_ID);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wechat_layout:
                mTabHost.setCurrentTabByTag(WE_CHAT);
                break;
            case R.id.alipay_layout:
                mTabHost.setCurrentTabByTag(ALI_PAY);
                break;
        }
    }

    @Override
    public void onListFragmentInteraction(Product item) {
        if (item.getPayType() == Product.PAYTYPE_ALIPAY) {
//            item.setPrice(String.valueOf(1));//测试
            tryGetAliPayOrderInfo(item);
            AnalyticsReport.onEvent(this, AnalyticsReport.MY_COINS_ACTIVITY_ALIPAY_ITEM_CLICK_EVENT_ID);
        } else if (item.getPayType() == Product.PAYTYPE_WXPAY) {
//            item.setPrice(String.valueOf(1));//测试
            tryGetWeChatPayInfo(item);
            AnalyticsReport.onEvent(this, AnalyticsReport.MY_COINS_ACTIVITY_WECHAT_ITEM_CLICK_EVENT_ID);
        }
    }

    private void tryGetAliPayOrderInfo(Product item) {
        if (!NetworkUtil.isNetworkOk(this)) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.network_error_tips));
            return;
        }
        showLoadingDialog();
        DataProviderRoom.getOrderInfo(this, item.getId(), new GsonHttpConnection.OnResultListener<OrderInfoMsg>() {
            @Override
            public void onSuccess(OrderInfoMsg msg) {
                handleOnGetAliPayOrderInfoSuccess(msg);
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                handleOnGetPayInfoFailed(errorCode);
                dismissLoadingsDialog();
            }
        });
    }

    private void handleOnGetPayInfoFailed(int errorCode) {
        if (errorCode == Msg.TLV_PRODUCT_NOT_EXIST) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.product_not_exist));
            return;
        }
        ToastHelper.showToast(ResourceHelper.getString(R.string.my_get_order_info_fail_tips));
    }

    private void handleOnGetAliPayOrderInfoSuccess(OrderInfoMsg msg) {
        if (msg == null) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.my_get_order_info_fail_tips));
            dismissLoadingsDialog();
            return;
        }
        String payInfo = msg.getQs();
        if (StringUtils.isEmpty(payInfo)) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.my_get_order_info_fail_tips));
            return;
        }
        if (mAliPayHelper == null) {
            initAliPayHelper();
        }
        mAliPayHelper.startPayTask(this, payInfo);
    }

    private void tryGetWeChatPayInfo(Product product) {
        if (!NetworkUtil.isNetworkOk(this)) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.network_error_tips));
            return;
        }

        if (mWXHelper == null) {
            mWXHelper = new WXHelper(MyCoinsActivity.this);
        }

        if (!mWXHelper.isInstallWeChat()) {
            showToast(R.string.please_install_weixin);
            return;
        }

        showLoadingDialog();
        DataProvider.getWechatPayInfo(this, String.valueOf(product.getId()),
                new GsonHttpConnection.OnResultListener<SingleMsg<WechatPayInfo>>() {
                    @Override
                    public void onSuccess(SingleMsg<WechatPayInfo> msg) {
                        handleGetWeChatPayInfoSuccess(msg);
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg, String command) {
                        dismissLoadingsDialog();
                    }
                });
    }

    private void handleGetWeChatPayInfoSuccess(SingleMsg<WechatPayInfo> msg) {
        dismissLoadingsDialog();
        if (msg == null) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.my_get_order_info_fail_tips));
            return;
        }
        if (mWXHelper == null) {
            mWXHelper = new WXHelper(MyCoinsActivity.this);
        }
        if (!mWXHelper.isInstallWeChat()) {
            showToast(R.string.please_install_weixin);
            return;
        }
        isStartWeChatPay = true;
        mWXHelper.sendPayReq(WechatPayInfo.toPayReq(msg.getResult()));
    }

    @Subscribe
    public void onEvent(PostEvent event) {
        super.onEvent(event);
        if (isStartWeChatPay && SubcriberTag.MSG_RECHARGE_SUCCESS.equals(event.tag)) {
            isStartWeChatPay = false;
            int resultCode = (int) event.event;
            handleOnWeChatPayResultCallback(resultCode);
        }
    }

    public void handleOnWeChatPayResultCallback(int resultCode) {
        if (resultCode == BaseResp.ErrCode.ERR_OK) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.pay_success_tips));
            loadData();
            return;
        }
        if (resultCode == WXHelper.WE_CHAT_PAY_TYPE_CANCEL) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.pay_cancel_tips));
            return;
        }
        ToastHelper.showToast(ResourceHelper.getString(R.string.pay_fail_tips));
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsReport.onEvent(this, AnalyticsReport.MY_COINS_ACTIVITY_SHOW_EVENT_ID);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isStartWeChatPay) {
            dismissLoadingsDialog();
            isStartWeChatPay = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWXHelper != null) {
            mWXHelper.unRegister();
        }
    }
}
