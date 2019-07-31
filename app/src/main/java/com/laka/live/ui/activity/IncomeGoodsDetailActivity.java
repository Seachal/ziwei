package com.laka.live.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;

import com.laka.live.R;
import com.laka.live.help.recycleViewDecoration.RecyclerViewItemDecoration;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.shopping.bean.json2bean.newversion.JTBShoppingGoodsIncomeDetail;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsIncomeDetailsBean;
import com.laka.live.shopping.model.ShoppingRequest;
import com.laka.live.shopping.network.IHttpCallBack;
import com.laka.live.ui.adapter.IncomeGoodsDetailAdapter;
import com.laka.live.ui.widget.HeadView;
import com.laka.live.ui.widget.IncomeGoodsView;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.util.Common;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;

/**
 * @ClassName: IncomeGoodsDetailActivity
 * @Description: 商品收益明细
 * @Author: chuan
 * @Version: 1.0
 * @Date: 29/08/2017
 */

public class IncomeGoodsDetailActivity extends BaseActivity implements PageListLayout.OnRequestCallBack {
    private final static String TAG = IncomeGoodsDetailActivity.class.getSimpleName();

    private boolean isHeadInit = false;
    private int mGoodsId, mIncomeType;

    private IncomeGoodsView mIncomeGoodsView;
    private PageListLayout mPageListLayout;
    private IncomeGoodsDetailAdapter mAdapter;

    public static void startActivity(Context context, int goodsId, int incomeType) {
        Intent intent = new Intent(context, IncomeGoodsDetailActivity.class);
        intent.putExtra(Common.GOODS_ID, goodsId);
        intent.putExtra(Common.INCOME_TYPE, incomeType);
        ActivityCompat.startActivity(context, intent, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_income_goods_detail);
        getWindow().setBackgroundDrawable(null);

        initIntent();
        initPageListLayout();
        mIncomeGoodsView = (IncomeGoodsView) findViewById(R.id.income_view);

        HeadView headView = (HeadView) findViewById(R.id.head_view);
        headView.setBackTextShow(false);
    }

    private void initIntent() {
        if (getIntent() == null) {
            finish();
            return;
        }

        mGoodsId = getIntent().getIntExtra(Common.GOODS_ID, -1);
        mIncomeType = getIntent().getIntExtra(Common.INCOME_TYPE, 1);

        if (mGoodsId <= 0) {
            finish();
        }
    }

    private void initPageListLayout() {
        mPageListLayout = (PageListLayout) findViewById(R.id.page_list_layout);
        mPageListLayout.setLayoutManager(new LinearLayoutManager(this));
        mPageListLayout.getRecyclerView().addItemDecoration(
                new RecyclerViewItemDecoration(RecyclerViewItemDecoration.MODE_HORIZONTAL,
                        ResourceHelper.getColor(R.color.colorE5E5E5),
                        Utils.dip2px(this, 1), 0, 0));
        mAdapter = new IncomeGoodsDetailAdapter(this,mIncomeType);
        mPageListLayout.setAdapter(mAdapter);
        mPageListLayout.setOnRequestCallBack(this);
        mPageListLayout.loadData();
    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {
        getGoodsIncomeDetails(page);
        return null;
    }

    private void getGoodsIncomeDetails(final int page) {
        ShoppingRequest.getInstance().getShoppingGoodsIncomeDetail(mGoodsId, mIncomeType, page, new IHttpCallBack() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                mPageListLayout.refreshComplete();
                mPageListLayout.setOnLoadMoreComplete();

                if (obj == null || !(obj instanceof JTBShoppingGoodsIncomeDetail)) {
                    if (mAdapter.isEmpty()) {
                        mPageListLayout.showEmpty();
                    } else {
                        mPageListLayout.showData();
                    }
                    mPageListLayout.onFinishLoading(false, false);

                    return;
                }

                ShoppingGoodsIncomeDetailsBean shoppingGoodsIncome = ((JTBShoppingGoodsIncomeDetail) obj).getData();

                if (!isHeadInit && shoppingGoodsIncome.getGood() != null) {
                    mIncomeGoodsView.updateData(shoppingGoodsIncome.getGood());
                    isHeadInit = true;
                }

                if (Utils.listIsNullOrEmpty(shoppingGoodsIncome.getDetails())) {
                    if (mAdapter.isEmpty()) {
                        mPageListLayout.showEmpty();
                    } else {
                        mPageListLayout.showData();
                    }
                    mPageListLayout.onFinishLoading(false, false);
                    return;
                }

                if (page == 0) {
                    mAdapter.clear();
                }

                mAdapter.addAll(shoppingGoodsIncome.getDetails());
                mAdapter.notifyDataSetChanged();
                mPageListLayout.onFinishLoading(shoppingGoodsIncome.getDetails().size() >= ShoppingRequest.LIMIT, false);
                mPageListLayout.addCurrentPage();
                mPageListLayout.showData();
            }

            @Override
            public void onError(String errorStr, int code) {
                Log.d(TAG, "getGoodsIncome error : " + errorStr);
                showToast(R.string.homepage_network_error_retry);

                mPageListLayout.refreshComplete();
                mPageListLayout.setOnLoadMoreComplete();

                if (mAdapter.isEmpty()) {
                    mPageListLayout.showNetWorkError();
                } else {
                    mPageListLayout.showData();
                }

                mPageListLayout.onFinishLoading(false, false);
            }
        });
    }
}
