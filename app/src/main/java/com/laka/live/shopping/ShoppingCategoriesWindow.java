package com.laka.live.shopping;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;


import com.laka.live.BuildConfig;
import com.laka.live.R;
import com.laka.live.shopping.adapter.ShoppingCategoriesAdapter;
import com.laka.live.shopping.bean.ShoppingCategoriesBean;
import com.laka.live.shopping.bean.ShoppingTopicBean;
import com.laka.live.shopping.bean.json2bean.JTBShoppingCategories;
import com.laka.live.shopping.framework.DefaultWindow;
import com.laka.live.shopping.framework.IDefaultWindowCallBacks;
import com.laka.live.shopping.framework.Notification;
import com.laka.live.shopping.model.ShoppingRequest;
import com.laka.live.shopping.model.TemplateHolder;
import com.laka.live.shopping.network.IHttpCallBack;
import com.laka.live.shopping.xrecyclerview.XRecyclerView;
import com.laka.live.ui.widget.LoadingLayout;
import com.laka.live.util.ToastHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhxu on 2016/4/21.
 * Email:357599859@qq.com
 */
public class ShoppingCategoriesWindow extends DefaultWindow implements XRecyclerView.LoadingListener, View.OnClickListener {

    private final static String TAG = "ShoppingCategories";

    private Context mContext;

    private ShoppingRequest mShoppingRequest;

    private LoadingLayout mLoadingLayout;

    private XRecyclerView mRecyclerView;
    private ShoppingCategoriesAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<TemplateHolder> mList = new ArrayList<>();

    public ShoppingCategoriesWindow(Context context, IDefaultWindowCallBacks callBacks) {
        super(context, callBacks);
        mContext = context;
        initUI();
    }

    private void initUI() {
        setTitle(R.string.shopping_categories_title);
        View view = View.inflate(mContext, R.layout.shopping_categories, null);
        getBaseLayer().addView(view, getContentLPForBaseLayer());

        mLoadingLayout = (LoadingLayout) findViewById(R.id.shopping_categories_loading);
        mLoadingLayout.setDefaultLoading();
        mLoadingLayout.setBtnOnClickListener(new LoadingLayout.OnBtnClickListener() {
            @Override
            public void onClick() {
                loadData();
            }
        });

        mAdapter = new ShoppingCategoriesAdapter(getContext(), mList);
        mRecyclerView = (XRecyclerView) findViewById(R.id.categories_list);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLoadingListener(this);
        mRecyclerView.setLoadingMoreEnabled(false);
        loadTemplate();
    }

    /**
     * 加载模板
     */
    private void loadTemplate() {
        if (mLayoutManager == null) {
            mLayoutManager = new LinearLayoutManager(mContext);
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    public void loadData() {
        getShoppingCategories();
    }

    @Override
    public void onWindowStateChange(int stateFlag) {
        super.onWindowStateChange(stateFlag);
        if (stateFlag == STATE_AFTER_PUSH_IN) {
            loadData();
        }
    }

    @Override
    public void notify(Notification notification) {
        super.notify(notification);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public void onLoadMore() {
    }

    /*******************
     * 接口访问
     ********************/
    private void getShoppingCategories() {
        mShoppingRequest = ShoppingRequest.getInstance();
        mShoppingRequest.getShoppingCategories(new IHttpCallBack() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                if (BuildConfig.DEBUG) {
                    Log.v(TAG, result);
                }

                finish();
                handleGetMainSuccess((JTBShoppingCategories) obj);
            }

            @Override
            public void onError(String errorStr, int code) {
                error();
            }
        });
    }

    private void handleGetMainSuccess(JTBShoppingCategories shoppingCategories) {
        if (mList != null) {
            mList.clear();
            mAdapter.notifyDataSetChanged();
        }

        ShoppingTopicBean mainBean = shoppingCategories.getData();
        if (mainBean == null) {
            error();
            return;
        }

        List<ShoppingCategoriesBean> list = mainBean.getTopic();
        if (list != null && !list.isEmpty()) {
            for (ShoppingCategoriesBean categoriesBean : list) {
                TemplateHolder holder = new TemplateHolder();
                holder.setTempType(categoriesBean.getStyleType());
                holder.setItems(categoriesBean);
                mList.add(holder);
            }
            mAdapter.notifyDataSetChanged();
        }

        if (mList.size() == 0) {
            mLoadingLayout.setDefaultNoData();
        }
    }

    private void error() {
        finish();
        ToastHelper.showToast(  R.string.shopping_network_error_retry);
    }

    private void finish() {
        mLoadingLayout.hide();
        mRecyclerView.refreshComplete();
    }
}
