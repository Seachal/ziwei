package com.laka.live.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.shopping.bean.json2bean.newversion.JTBShoppingGoodsIncomeDetailInfo;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsIncomeDetail;
import com.laka.live.shopping.model.ShoppingRequest;
import com.laka.live.shopping.network.IHttpCallBack;
import com.laka.live.ui.widget.HeadView;
import com.laka.live.ui.widget.LoadingLayout;
import com.laka.live.util.Common;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;

import java.util.Date;

/**
 * @ClassName:
 * @Description:
 * @Author: chuan
 * @Version: 1.0
 * @Date: 18/10/2017
 */

public class IncomeGoodsDetailInfoActivity extends BaseActivity {
    private final static String TAG = IncomeGoodsDetailInfoActivity.class.getSimpleName();

    private LoadingLayout mLoadingLayout;

    private TextView mIncomeTypeTv;
    private TextView mIncomeTv;
    private TextView mBuyerUserTv;
    private TextView mBuyTimeTv;
    private TextView mUserPayTv;
    private RelativeLayout mRecommendUserRl;
    private TextView mRecommendUserTv;
    private RelativeLayout mRecommendIncomeRl;
    private TextView mRecommendIncomeTv;
    private RelativeLayout mAgentUserRl;
    private TextView mAgentUserTv;
    private RelativeLayout mAgentIncomeRl;
    private TextView mAgentIncomeTv;

    private int mId, mIncomeType;

    public static void startActivity(Context context, int id, int incomeType) {
        Intent intent = new Intent(context, IncomeGoodsDetailInfoActivity.class);
        intent.putExtra(Common.ID, id);
        intent.putExtra(Common.INCOME_TYPE, incomeType);

        ContextCompat.startActivity(context, intent, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_goods_detail_info);
        getWindow().setBackgroundDrawable(null);

        initIntent();
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getData();
    }

    private void initUI() {
        HeadView headView = (HeadView) findViewById(R.id.head_view);
        headView.setBackTextShow(false);

        mLoadingLayout = (LoadingLayout) findViewById(R.id.loading_layout);
        mLoadingLayout.setBtnOnClickListener(new LoadingLayout.OnBtnClickListener() {
            @Override
            public void onClick() {
                getData();
            }
        });

        mIncomeTypeTv = (TextView) findViewById(R.id.income_type_tv);
        mIncomeTv = (TextView) findViewById(R.id.income_tv);
        mBuyerUserTv = (TextView) findViewById(R.id.buy_user_tv);
        mBuyTimeTv = (TextView) findViewById(R.id.buy_time_tv);
        mUserPayTv = (TextView) findViewById(R.id.user_pay_tv);
        mRecommendUserRl = (RelativeLayout) findViewById(R.id.recommend_user_rl);
        mRecommendUserTv = (TextView) findViewById(R.id.recommend_user_tv);
        mRecommendIncomeRl = (RelativeLayout) findViewById(R.id.recommend_income_rl);
        mRecommendIncomeTv = (TextView) findViewById(R.id.recommend_income_tv);
        mAgentUserRl = (RelativeLayout) findViewById(R.id.agent_user_rl);
        mAgentUserTv = (TextView) findViewById(R.id.agent_user_tv);
        mAgentIncomeRl = (RelativeLayout) findViewById(R.id.agent_income_rl);
        mAgentIncomeTv = (TextView) findViewById(R.id.agent_income_tv);
    }

    private void initIntent() {
        if (getIntent() == null) {
            finish();
            return;
        }

        mId = getIntent().getIntExtra(Common.ID, -1);
        mIncomeType = getIntent().getIntExtra(Common.INCOME_TYPE, 1);

        if (mId <= 0) {
            finish();
        }
    }

    private void getData() {
        mLoadingLayout.setDefaultLoading();

        ShoppingRequest.getInstance().getShoppingGoodsIncomeDetailInfo(mId, mIncomeType, new IHttpCallBack() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                if (obj == null || !(obj instanceof JTBShoppingGoodsIncomeDetailInfo)) {
                    mLoadingLayout.setDefaultNetworkError(true);
                    ToastHelper.showToast(R.string.shopping_network_error_retry);
                    return;
                }

                ShoppingGoodsIncomeDetail incomeDetail = ((JTBShoppingGoodsIncomeDetailInfo) obj).getData();
                if (incomeDetail == null) {
                    mLoadingLayout.setDefaultNetworkError(true);
                    ToastHelper.showToast(R.string.shopping_network_error_retry);
                    return;
                }

                mLoadingLayout.hide();

                mIncomeTypeTv.setText(incomeDetail.getIncomeTypeStr());
                mIncomeTv.setText(String.valueOf(Utils.float2String(incomeDetail.getIncome())));
                mBuyerUserTv.setText(incomeDetail.getNickName());
                mBuyTimeTv.setText(Utils.LONG_DATE_FORMATER.format(new Date(incomeDetail.getCreateTime() * 1000)));
                mUserPayTv.setText(ResourceHelper.getString(R.string.income_money, Utils.float2String(incomeDetail.getTotalPrice())));

                if (Utils.isEmpty(incomeDetail.getRecommender())) {
                    mRecommendUserRl.setVisibility(View.GONE);
                    mRecommendIncomeRl.setVisibility(View.GONE);
                } else {
                    mRecommendUserTv.setText(incomeDetail.getRecommender());
                    mRecommendIncomeTv.setText(ResourceHelper.getString(R.string.income_money, Utils.float2String(incomeDetail.getRecommentIncome())));
                }

                if (Utils.isEmpty(incomeDetail.getAgent())) {
                    mAgentUserRl.setVisibility(View.GONE);
                    mAgentIncomeRl.setVisibility(View.GONE);
                } else {
                    mAgentUserTv.setText(incomeDetail.getAgent());
                    mAgentIncomeTv.setText(ResourceHelper.getString(R.string.income_money, Utils.float2String(incomeDetail.getAgentIncome())));
                }
            }

            @Override
            public void onError(String errorStr, int code) {
                mLoadingLayout.setDefaultNetworkError(true);
                ToastHelper.showToast(R.string.shopping_network_error_retry);
                return;
            }
        });
    }
}
