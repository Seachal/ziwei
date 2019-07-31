package com.laka.live.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laka.live.R;
import com.laka.live.help.recycleViewDecoration.RecyclerViewItemDecoration;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.shopping.bean.json2bean.newversion.JTBShoppingGoodsIncome;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsIncomeBean;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsIncomeSumBean;
import com.laka.live.shopping.model.ShoppingRequest;
import com.laka.live.shopping.network.IHttpCallBack;
import com.laka.live.shopping.widget.GoodsIncomeHeader;
import com.laka.live.ui.adapter.GoodsIncomeAdapter;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;

/**
 * @ClassName: GoodsIncomeFragment
 * @Description: 商品收益
 * @Author: chuan
 * @Version: 1.0
 * @Date: 25/07/2017
 */

public class GoodsIncomeFragment extends BaseFragment implements PageListLayout.OnRequestCallBack {
    private final static String TAG = GoodsIncomeFragment.class.getSimpleName();

    private PageListLayout mPageListLayout;
    private GoodsIncomeHeader mHeadView;
    private GoodsIncomeAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transo_detail, null);
        mHeadView = new GoodsIncomeHeader(getContext());

        mPageListLayout = (PageListLayout) view.findViewById(R.id.page_list_layout);
        mPageListLayout.addHeaderView(mHeadView);
        mPageListLayout.setLayoutManager(new LinearLayoutManager(getContext()));
        mPageListLayout.getRecyclerView().addItemDecoration(
                new RecyclerViewItemDecoration(RecyclerViewItemDecoration.MODE_HORIZONTAL,
                        ResourceHelper.getColor(R.color.transparent),
                        Utils.dip2px(getContext(), 5), 0, 0));
        mAdapter = new GoodsIncomeAdapter(getContext());
        mPageListLayout.setAdapter(mAdapter);
        mPageListLayout.setOnRequestCallBack(this);
        mPageListLayout.loadData();
        return view;
    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {
        getGoodsIncome(page);
        return null;
    }

    private void getGoodsIncome(final int page) {
        ShoppingRequest.getInstance().getShoppingGoodsIncome(page, new IHttpCallBack() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                mPageListLayout.refreshComplete();
                mPageListLayout.setOnLoadMoreComplete();

                if (obj == null || !(obj instanceof JTBShoppingGoodsIncome)) {
                    if (mAdapter.isEmpty()) {
                        mPageListLayout.showEmpty();
                    } else {
                        mPageListLayout.showData();
                    }
                    mPageListLayout.onFinishLoading(false, false);

                    return;
                }

                ShoppingGoodsIncomeBean shoppingGoodsIncome = ((JTBShoppingGoodsIncome) obj).getData();
                ShoppingGoodsIncomeSumBean incomeSum = shoppingGoodsIncome.getSum();
                mHeadView.updateData(incomeSum.getTotal(), incomeSum.getRecommend(), incomeSum.getAgent());

                if (Utils.listIsNullOrEmpty(shoppingGoodsIncome.getIncomes())) {
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

                mAdapter.addAll(shoppingGoodsIncome.getIncomes());
                mAdapter.notifyDataSetChanged();
                mPageListLayout.addCurrentPage();
                mPageListLayout.showData();
                mPageListLayout.onFinishLoading(shoppingGoodsIncome.getIncomes().size() >= ShoppingRequest.LIMIT, false);
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
