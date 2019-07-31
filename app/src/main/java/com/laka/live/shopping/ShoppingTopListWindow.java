package com.laka.live.shopping;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;


import com.laka.live.BuildConfig;
import com.laka.live.R;
import com.laka.live.shopping.adapter.ShoppingTopListAdapter;
import com.laka.live.shopping.bean.ShoppingTopBean;
import com.laka.live.shopping.bean.ShoppingTopListBean;
import com.laka.live.shopping.bean.json2bean.JTBShoppingTopList;
import com.laka.live.shopping.framework.DefaultWindow;
import com.laka.live.shopping.framework.IDefaultWindowCallBacks;
import com.laka.live.shopping.framework.Notification;
import com.laka.live.shopping.model.ShoppingRequest;
import com.laka.live.shopping.network.IHttpCallBack;
import com.laka.live.shopping.xrecyclerview.XRecyclerView;
import com.laka.live.ui.widget.LoadingLayout;
import com.laka.live.util.ToastHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhxu on 2016/5/10.
 * Email:357599859@qq.com
 */
public class ShoppingTopListWindow extends DefaultWindow implements XRecyclerView.LoadingListener {

    private final static String TAG = "ShoppingTopListWindow";

    private Context mContext;

    private ShoppingRequest mShoppingRequest;

    private LoadingLayout mLoadingLayout;

    private XRecyclerView mRecyclerView;
    private ShoppingTopListAdapter mAdapter;
    private List<ShoppingTopBean> mList = new ArrayList<>();

    private int mRealPage = 1;
    private boolean mIsNext = false;

    private int mCateId = 0;

    public ShoppingTopListWindow(Context context, IDefaultWindowCallBacks callBacks) {
        super(context, callBacks);
        mContext = context;
        setLaunchMode(LAUNCH_MODE_SINGLE_INSTANCE);
        initUI();
    }

    public void setCateId(int mCateId) {
        this.mCateId = mCateId;
    }

    private void initUI() {
        setTitle(R.string.shopping_category_top_list_title);
        View view = View.inflate(mContext, R.layout.shopping_top_list, null);
        getBaseLayer().addView(view, getContentLPForBaseLayer());

        mLoadingLayout = (LoadingLayout) findViewById(R.id.top_loading);
        mLoadingLayout.setDefaultLoading();
        mLoadingLayout.setBtnOnClickListener(new LoadingLayout.OnBtnClickListener() {
            @Override
            public void onClick() {
                loadData(true);
            }
        });

        mAdapter = new ShoppingTopListAdapter(getContext(), mList);
        mRecyclerView = (XRecyclerView) findViewById(R.id.top_list);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLoadingListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    public void loadData(boolean isRefresh) {
        if (isRefresh) {
            mRealPage = 1;
        } else {
            mRealPage++;
            mIsNext = true;
        }
        getShoppingTopList(isRefresh);
    }

    @Override
    public void onWindowStateChange(int stateFlag) {
        super.onWindowStateChange(stateFlag);
        if (stateFlag == STATE_AFTER_PUSH_IN) {
            loadData(true);
        }
    }

    @Override
    public void notify(Notification notification) {
        super.notify(notification);
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    @Override
    public void onLoadMore() {
        loadData(false);
    }

    /*******************
     * 接口访问
     ********************/
    private void getShoppingTopList(final boolean isRefresh) {
        mShoppingRequest = ShoppingRequest.getInstance();
        mShoppingRequest.getShoppingTopList(mCateId, mRealPage, new IHttpCallBack() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                if (BuildConfig.DEBUG) {
                    Log.v(TAG, result);
                }

                finish(isRefresh);

                if (isRefresh) {
                    mList.clear();
                    mAdapter.notifyDataSetChanged();
                }

                handleGetMainSuccess((JTBShoppingTopList) obj, isRefresh);
            }

            @Override
            public void onError(String errorStr, int code) {
                finish(isRefresh);
                error();
            }
        });
    }

    private void handleGetMainSuccess(JTBShoppingTopList shoppingTopList, boolean isRefresh) {
        ShoppingTopListBean bean = shoppingTopList.getData();
        if (bean == null) {
            finish(isRefresh);
            error();
            return;
        }

        List<ShoppingTopBean> list = bean.getToplist();
        if (list != null) {
            if (!list.isEmpty()) {
                mList.addAll(list);
                mAdapter.notifyDataSetChanged();
            }
            mRecyclerView.hasMore(mShoppingRequest.hasMore(list.size()));
        }

        if (mList.size() == 0) {
            mLoadingLayout.setDefaultNoData();
        }
    }

    private void error() {
        ToastHelper.showToast(R.string.shopping_network_error_retry);
        if (mIsNext) {
            mIsNext = false;
            mRealPage--;
        }
    }

    private void finish(boolean isRefresh) {
        mLoadingLayout.hide();
        if (isRefresh) {
            mRecyclerView.refreshComplete();
        } else {
            mRecyclerView.loadMoreComplete();
        }
    }
}
