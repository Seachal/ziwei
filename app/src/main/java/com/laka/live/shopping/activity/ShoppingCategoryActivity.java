package com.laka.live.shopping.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.laka.live.R;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.shopping.adapter.ShoppingGoodsAdapter;
import com.laka.live.shopping.adapter.ShoppingHorizontalRecyclerAdapter;
import com.laka.live.shopping.bean.json2bean.newversion.JTBShoppingCategory;
import com.laka.live.shopping.bean.json2bean.newversion.JTBShoppingGoods;
import com.laka.live.shopping.bean.newversion.GoodsCate;
import com.laka.live.shopping.bean.newversion.ShoppingCategoryBean;
import com.laka.live.shopping.bean.newversion.ShoppingCategoryGoodsBean;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsBaseBean;
import com.laka.live.shopping.model.ShoppingRequest;
import com.laka.live.shopping.network.IHttpCallBack;
import com.laka.live.shopping.search.info.SearchConstant;
import com.laka.live.shopping.search.widget.SearchClassifyTitleView;
import com.laka.live.shopping.widget.ShoppingCustomRecycler;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.widget.HeadView;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ShoppingCategoryActivity
 * @Description: 分类商品页
 * @Author: chuan
 * @Version: 1.0
 * @Date: 14/07/2017
 */
public class ShoppingCategoryActivity extends BaseActivity implements PageListLayout.OnRequestCallBack
        , ShoppingHorizontalRecyclerAdapter.IShoppingHorizontalRecyclerAdapter {

    private final static String TAG = ShoppingCategoryActivity.class.getSimpleName();

    private final static String TITLE = "title";
    private final static String TYPE_ID = "typeId";

    private List<GoodsCate> mGoodsCateList = new ArrayList<>();
    private ShoppingHorizontalRecyclerAdapter mHorizontalAdapter;
    private ShoppingCustomRecycler mHorizontalRecyclerView;

    private List<ShoppingGoodsBaseBean> mGoodsList = new ArrayList<>();
    private ShoppingGoodsAdapter mAdapter;
    private PageListLayout pageListLayout;

    private int mCateId;
    private String mCateTitle;
    private int mCurrSortType = SearchConstant.TYPE_COMPREHENSIVE;
    private AppBarLayout mAppBarLayout;

    public static void startActivity(Context context, String title, int typeId) {
        Intent intent = new Intent(context, ShoppingCategoryActivity.class);
        intent.putExtra(TITLE, title);
        intent.putExtra(TYPE_ID, typeId);
        ActivityCompat.startActivity(context, intent, null);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shopping_category);
        getWindow().setBackgroundDrawable(null);

        if (getIntent() == null) {
            finish();
        } else {
            mCateTitle = getIntent().getStringExtra(TITLE);
            mCateId = getIntent().getIntExtra(TYPE_ID, -1);
        }

        initUI();
        initData();
    }

    private void initUI() {
        HeadView headView = (HeadView) findViewById(R.id.head_view);
        headView.setBackTextShow(false);
        headView.setTitle(mCateTitle);

        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mHorizontalRecyclerView = (ShoppingCustomRecycler) findViewById(R.id.horizontal_category);
        pageListLayout = (PageListLayout) findViewById(R.id.page_list_layout);

        mHorizontalAdapter = new ShoppingHorizontalRecyclerAdapter(mContext, mGoodsCateList);
        mHorizontalRecyclerView.setAdapter(mHorizontalAdapter);
        mHorizontalAdapter.setCallback(this);

        ((SearchClassifyTitleView) findViewById(R.id.classify_title)).setClickCallBack(new SearchClassifyTitleView.ClickCallBack() {
            @Override
            public void onClick(int type, String title) {
                if (type == mCurrSortType) {
                    return;
                }

                mCurrSortType = type;
                pageListLayout.loadData(true);
            }
        });

        mAdapter = new ShoppingGoodsAdapter(mContext);
        mAdapter.setData(mGoodsList);
        pageListLayout.setAdapter(mAdapter);
        pageListLayout.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        pageListLayout.setOnRequestCallBack(this);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset >= 0) {
                    pageListLayout.setEnableRefresh(true);
                } else {
                    pageListLayout.setEnableRefresh(false);
                }
            }
        });
    }

    private void initData() {
        getShoppingCategory();
        pageListLayout.loadData(true, true);
    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {
        getShoppingCategoryGoods(page);
        return null;
    }

    /**
     * 顶部分类点击
     *
     * @param goodsCate
     */
    @Override
    public void onItemsClick(GoodsCate goodsCate) {
        if (mCateId == goodsCate.getCateId()) {
            return;
        }

        mHorizontalAdapter.notifyDataSetChanged();

        mCateId = goodsCate.getCateId();
        mCateTitle = goodsCate.getTitle();
        pageListLayout.loadData(true);
    }

    /*******************
     * 接口访问
     ********************/
    private void getShoppingCategory() {
        if (mCateId < 0) {
            return;
        }

        ShoppingRequest.getInstance().getShoppingCategory(mCateId, new IHttpCallBack() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                Log.d(TAG, "getShoppingCategory success . " + result);

                if (mGoodsCateList != null) {
                    mGoodsCateList.clear();
                }

                if (obj == null || !(obj instanceof JTBShoppingCategory)) {
                    return;
                }

                ShoppingCategoryBean categoryBean = ((JTBShoppingCategory) obj).getData();
                if (categoryBean == null) {
                    return;
                }

                mGoodsCateList.addAll(categoryBean.getCategories());
                mHorizontalAdapter.notifyDataSetChanged();
                mHorizontalRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String errorStr, int code) {
                ToastHelper.showToast(R.string.shopping_network_error_retry);
            }
        });
    }

    /**
     * 获取商品列表
     */
    private void getShoppingCategoryGoods(final int page) {
        if (mCateId < 0) {
            return;
        }

        ShoppingRequest.getInstance().getShoppingCategoryGoods(mCateId, mCurrSortType, page, new IHttpCallBack() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                Log.d(TAG, "getShoppingCategoryGoods success." + result);

                pageListLayout.refreshComplete();
                pageListLayout.setOnLoadMoreComplete();

                if (page == 0) {
                    mGoodsList.clear();
                }

                if (obj == null || !(obj instanceof JTBShoppingGoods)) {
                    if (Utils.listIsNullOrEmpty(mGoodsList)) {
                        pageListLayout.showEmpty();
                    }

                    pageListLayout.onFinishLoading(false, false);
                    return;
                }

                ShoppingCategoryGoodsBean goodsBean = ((JTBShoppingGoods) obj).getData();

                if (goodsBean == null || Utils.listIsNullOrEmpty(goodsBean.getGoods())) {
                    if (Utils.listIsNullOrEmpty(mGoodsList)) {
                        pageListLayout.showEmpty();
                    }

                    pageListLayout.onFinishLoading(false, false);
                    return;
                }

                pageListLayout.addCurrentPage();

                mGoodsList.addAll(goodsBean.getGoods());
                mAdapter.notifyDataSetChanged();

                mAppBarLayout.setVisibility(View.VISIBLE);
//                pageListLayout.onFinishLoading(goodsBean.getGoods().size() >= ShoppingRequest.LIMIT,
//                        false);
                pageListLayout.onFinishLoading(true,
                        false);
                pageListLayout.showData();

                // if (page == 0) {//滚回顶部
                    // 使用 CoordinatorLayout 替换原来的监听实现动画效果，不再需要这个处理
                    // pageListLayout.smoothScrollToPosition(0);
                //}
            }

            @Override
            public void onError(String errorStr, int code) {
                pageListLayout.refreshComplete();
                pageListLayout.setOnLoadMoreComplete();

                ToastHelper.showToast(R.string.shopping_network_error_retry);

                if (Utils.listIsNullOrEmpty(mGoodsList)) {
                    pageListLayout.onFinishLoading(false, false);
                    pageListLayout.showNetWorkError();
                } else {
                    pageListLayout.onFinishLoading(true, false);
                    pageListLayout.showData();
                }
            }
        });
    }
}
