package com.laka.live.account.income;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.Income;
import com.laka.live.config.SystemConfig;
import com.laka.live.msg.EarningsMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.activity.WebActivity;
import com.laka.live.ui.widget.HeadView;
import com.laka.live.ui.widget.LoadingLayout;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.util.Common;
import com.laka.live.util.NumberUtils;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;
import com.laka.taste.wxapi.WXHelper;

import framework.ioc.annotation.InjectView;

/**
 * 我的收益
 */
public class MyIncomeActivity extends BaseActivity implements View.OnClickListener {

    private final static int REQUEST_CODE_EXCHANGE = 1;
    private final static int REQUEST_CODE_BIND = 1;

    @InjectView(id = R.id.money)
    public TextView money;  // 冻结账户的金额

    private Income mIncome;
    private WXHelper mWXHelper;
    private TextView mWeeklyCoins;
    private TextView mCashWithdrawal;
    private TextView mTodayCashWithdrawal;
    private Button mExChangeCashButton;
    private Button mExChangeDrillButton;

    private LoadingLayout mLoadingLayout;

    public static void startActivity(Context activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, MyIncomeActivity.class);
//            ActivityCompat.startActivity(activity, intent, null);
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_income);
        init();
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mErrorView != null) {
            mErrorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mErrorState == PageListLayout.ErrorState.ERROR_STATE_EMPTY) {
                        return;
                    }
                    tryGetCurrentAccountEarnings(true);
                }
            });
        }
    }

    private void init() {
        initDefView();
        mWeeklyCoins = (TextView) findViewById(R.id.weekly_tickets_income);
        mCashWithdrawal = (TextView) findViewById(R.id.cash_withdrawal_layout).findViewById(R.id.money);
        View todayCashWithLayout = findViewById(R.id.today_cash_withdrawal_layout);
        TextView todayCashWithdrawalText = (TextView) todayCashWithLayout.findViewById(R.id.text);
        todayCashWithdrawalText.setText(R.string.today_can_be_withdrawal_amount);
        mTodayCashWithdrawal = (TextView) todayCashWithLayout.findViewById(R.id.money);
        HeadView headView = (HeadView) findViewById(R.id.head_layout);
        headView.setBackTextShow(false);
        headView.setTipOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionRecordActivity.startActivity(MyIncomeActivity.this);
                AnalyticsReport.onEvent(MyIncomeActivity.this, AnalyticsReport.MY_INCOME_TRANSACTION_CLICK_EVENT_ID);
            }
        });
        mExChangeDrillButton = (Button) findViewById(R.id.for_diamond);
        mExChangeDrillButton.setVisibility(View.GONE);
        mExChangeDrillButton.setOnClickListener(this);
        mExChangeCashButton = (Button) findViewById(R.id.wechat_withdrawal);
        mExChangeCashButton.setOnClickListener(this);
        findViewById(R.id.common_problems).setOnClickListener(this);


        if (SystemConfig.getInstance().isShowWithdrawalWeixin()) {
            mExChangeCashButton.setVisibility(View.VISIBLE);
        } else {
            mExChangeCashButton.setVisibility(View.GONE);
        }

        mLoadingLayout = (LoadingLayout) findViewById(R.id.loading_layout);

    }

    private void update(Income income) {
        mIncome = income;

        if (mIncome == null) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.error_data_tip));
            finish();
            return;
        }

        // 本周收益
        mWeeklyCoins.setText(NumberUtils.splitDoubleStr(income.week_coins));
        // 可用帐户
        mCashWithdrawal.setText(NumberUtils.splitDoubleStr(income.recv_coins));
        // 可提现收益
        mTodayCashWithdrawal.setText(NumberUtils.splitDoubleStr(income.total_cash_withdrawal));
        // 设置冻结账户的金额
        money.setText(NumberUtils.splitDoubleStr(income.frozenCoins));

        if (income.isCashMoney()) {
            mExChangeCashButton.setVisibility(View.GONE);
            mExChangeDrillButton.setVisibility(View.GONE);
        }

    }

    private void tryGetCurrentAccountEarnings(final boolean isShowLoading) {

        mLoadingLayout.setDefaultLoading();

        DataProvider.queryEarnings(this, new GsonHttpConnection.OnResultListener<EarningsMsg>() {
            @Override
            public void onSuccess(EarningsMsg msg) {
                if (mLoadingLayout != null) {
                    mLoadingLayout.setVisibility(View.GONE);
                }
                handleOnRequestEarningsSuccess(msg, isShowLoading);
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                if (mLoadingLayout != null) {
                    mLoadingLayout.setVisibility(View.GONE);
                }
//                mLoadingLayout.setDefaultNetworkError(false);

                showToast(R.string.homepage_network_error);
            }
        });

    }

    private void handleOnRequestEarningsSuccess(EarningsMsg msg, boolean isShowLoading) {

        if (msg == null) {
            return;
        }
        update(msg.getIncome());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.for_diamond:
                handleOnClickExchangeDiamond();
                AnalyticsReport.onEvent(this, AnalyticsReport.MY_INCOME_EXCHANGE_DIAMOND_CLICK_EVENT_ID);
                break;
            case R.id.wechat_withdrawal:
                handleOnClickWeChatWithdrawalButton();
                AnalyticsReport.onEvent(this, AnalyticsReport.MY_INCOME_WECHAT_WITHDRAWAL_CLICK_EVENT_ID);
                break;
            case R.id.common_problems:
                WebActivity.startActivity(this, Common.HELP_URL, getString(R.string.common_problems));
                break;
        }
    }

    private long mLastClickTime = 0;

    private void handleOnClickExchangeDiamond() {
        long currentTime = System.currentTimeMillis();
        if (mIncome != null && (currentTime - mLastClickTime) > 500) {
            ExchangeDiamondActivity.startActivityForResult(this, mIncome.total_exchange_coins, REQUEST_CODE_EXCHANGE);
        }

        mLastClickTime = currentTime;

    }

    private void handleOnClickWeChatWithdrawalButton() {
        if (mWXHelper == null) {
            mWXHelper = new WXHelper(this);
        }
        if (!mWXHelper.isInstallWeChat()) {
            ToastHelper.showToast(R.string.please_install_wechat);
            return;
        }
        if (mIncome == null) {
            showToast(R.string.homepage_network_error);
            return;
        }

//        if (!AccountInfoManager.getInstance().getCurrentAccountBindWeChatState()) {
        String tips = ResourceHelper.getString(R.string.withdrawal_tips,
                NumberUtils.splitDoubleStr(mIncome.day_max_withdraw_rmb),
                mIncome.day_max_withdraw_times);

        BindWechatActivity.startActivityForResult(this, REQUEST_CODE_BIND, mIncome.getTodayCashWithdrawal(),
                mIncome.getDay_max_withdraw_times(), tips); //去绑定微信页面
//            return;
//        }
//        if (!AccountInfoManager.getInstance().getCurrentAccountBindPhoneState()) {
//            BindPhoneActivity.startActivity(this);//去绑定手机号页面
//            return;
//        }
//
////        String tips = "每天限额"+NumberUtils.splitDoubleStr(mIncome.day_max_withdraw_rmb)+"元, 本日还可转" +
////                NumberUtils.splitDoubleStr(mIncome.day_cash_withdrawal_remain)+"次，15个工作日内审核";
//        String tips = ResourceHelper.getString(R.string.withdrawal_tips, NumberUtils.splitDoubleStr(mIncome.day_max_withdraw_rmb), (int)mIncome.day_cash_withdrawal_remain);
//        WithdrawalActivity.startActivityForResult(this, mIncome.getTodayCashWithdrawal(),
//                mIncome.getDay_cash_withdrawal_remain(), tips, REQUEST_CODE_WITHDRAWAL);

    }

    @Override
    protected void onResume() {
        super.onResume();
        tryGetCurrentAccountEarnings(true);
        AnalyticsReport.onEvent(this, AnalyticsReport.MY_INCOME_ACTIVITY_SHOW_EVENT_ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWXHelper != null) {
            mWXHelper.unRegister();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            tryGetCurrentAccountEarnings(false);
        }
    }
}
