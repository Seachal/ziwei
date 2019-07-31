package com.laka.live.shopping.order.tab;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.laka.live.BuildConfig;
import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.shopping.activity.OrderLogisticsActivity;
import com.laka.live.shopping.bean.ShoppingOrderDetailBean;
import com.laka.live.shopping.bean.ShoppingOrderDetailGoodsBean;
import com.laka.live.shopping.bean.ShoppingOrderListBean;
import com.laka.live.shopping.bean.ShoppingOrderLogisticsBean;
import com.laka.live.shopping.bean.ShoppingOrdersBean;
import com.laka.live.shopping.bean.json2bean.JTBShareResult;
import com.laka.live.shopping.bean.json2bean.JTBShoppingOrderDetail;
import com.laka.live.shopping.bean.json2bean.JTBShoppingOrders;
import com.laka.live.shopping.common.CommonConst;
import com.laka.live.shopping.framework.INotify;
import com.laka.live.shopping.framework.ITabView;
import com.laka.live.shopping.framework.Notification;
import com.laka.live.shopping.framework.NotificationCenter;
import com.laka.live.shopping.framework.adapter.NotificationDef;
import com.laka.live.shopping.model.TemplateHolder;
import com.laka.live.shopping.network.HttpCallbackAdapter;
import com.laka.live.shopping.network.HttpCode;
import com.laka.live.shopping.network.HttpMethod;
import com.laka.live.shopping.network.HttpUrls;
import com.laka.live.shopping.network.IHttpCallBack;
import com.laka.live.shopping.order.adapter.OrderRecyclerViewAdapter;
import com.laka.live.shopping.order.model.OrderListRequest;
import com.laka.live.shopping.orderdetail.adapter.OrderDetailRecyclerViewAdapter;
import com.laka.live.shopping.orderdetail.model.OrderDetailRequest;
import com.laka.live.shopping.xrecyclerview.XRecyclerView;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.adapter.UserListAdapter;
import com.laka.live.ui.widget.LoadingLayout;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.util.Log;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gangqing on 2016/4/27.
 * Email:denggangqing@ta2she.com
 */
public abstract class BaseOrderTab implements ITabView , XRecyclerView.LoadingListener, INotify, OrderRecyclerViewAdapter.RecyclerAdapterCallBack,EventBusManager.OnEventBusListener {
    private static final String TAG = "BaseOrderTab";
    public Context mContext;
    //    private XRecyclerView mRecyclerView;
    private PageListLayout mRecyclerView;
    private OrderRecyclerViewAdapter mAdapter;
    private List<ShoppingOrderListBean> mOrderList = new ArrayList<>();
    private LoadingLayout mLoadingLayout;
    private OrderListRequest mOrderListRequest;

    private int mRealPage = 1;
    private boolean mIsNext = false;
    private int mCurrentTabType;

    private boolean mIsWindowPushIn = false;

    public BaseOrderTab(Context context) {
        mContext = context;
        mCurrentTabType = getCurrentTabType();
        EventBusManager.register(this);
    }

    @Override
    public void onPrepareContentView() {
        mIsWindowPushIn = true;
    }

    @Override
    public View getTabView() {
        View view = View.inflate(mContext, R.layout.order_list, null);

        mLoadingLayout = (LoadingLayout) view.findViewById(R.id.loading_layout);
//        mLoadingLayout.setDefaultLoading();
//        mLoadingLayout.setBtnOnClickListener(new LoadingLayout.OnBtnClickListener() {
//            @Override
//            public void onClick() {
//                getOrderList(true);
//                mRecyclerView.loadData(true, true);
//            }
//        });
//        mRecyclerView = (XRecyclerView) view.findViewById(R.id.order_list);
        mRecyclerView = (PageListLayout) view.findViewById(R.id.order_list);
        mAdapter = new OrderRecyclerViewAdapter(mContext, mOrderList);
        mAdapter.setRecyclerAdapterCallBack(this);
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setLoadingListener(this);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setIsReloadWhenEmpty(true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setOnRequestCallBack(new PageListLayout.OnRequestCallBack() {
            @Override
            public String request(int page, GsonHttpConnection.OnResultListener listener) {
                Log.d(TAG," request page="+page);
                mRealPage = page;
                if(page==0){
                    getOrderList(true);
                }else{
                    getOrderList(false);
                }
                return null;
            }
        });
        mLoadingLayout.setDefaultLoading();
        mRecyclerView.loadData();

        return view;
    }

    @Override
    public void onTabChanged(int tabChangedFlag) {
        if (tabChangedFlag == TAB_TO_SHOW) {
            NotificationCenter.getInstance().register(this, NotificationDef.N_ORDER_LIST_CHANGED);
            firstLoad();
        } else if (tabChangedFlag == TAB_TO_HIDE) {
            NotificationCenter.getInstance().unregister(this, NotificationDef.N_ORDER_LIST_CHANGED);
        } else if (tabChangedFlag == TAB_AFTER_PUSH_IN) {
            firstLoad();
        }
    }

    private void firstLoad() {
        if (mIsWindowPushIn && AccountInfoManager.getInstance().checkUserIsLogin()) {
            loadData(true);
        } else {
            if (mLoadingLayout.isShown()) {
                mLoadingLayout.hide();
            }
        }
    }

    public abstract int getCurrentTabType();


    @Override
    public void onRefresh() {
        loadData(true);
    }

    @Override
    public void onLoadMore() {
        loadData(false);
    }

    /**
     * @param isRefresh 清空List 重新加载接口数据
     */
    public void loadData(boolean isRefresh) {
//        if (isRefresh) {
//            mRealPage = 1;
//        } else {
//            mRealPage++;
//            mIsNext = true;
//        }
//        getOrderList(isRefresh);
    }

    @Override
    public void notify(Notification notification) {
        if (notification.id == NotificationDef.N_ORDER_LIST_CHANGED) {
//            loadData(true);
            getOrderList(true);
        }
    }

    /*******************
     * 接口访问
     ********************/
    private void getOrderList(final boolean isRefresh) {
        mOrderListRequest = OrderListRequest.getInstance();
        mOrderListRequest.getOrderList(mRealPage, mCurrentTabType, new HttpCallbackAdapter(true) {
            @Override
            public <T> void onSuccess(T obj, String result) {
                Log.d(TAG," onSuccess result="+result);
                mRecyclerView.refreshComplete();
                mRecyclerView.setOnLoadMoreComplete();
//                finish(isRefresh);
                mLoadingLayout.hide();
                if (isRefresh) {
                    mOrderList.clear();
                    mAdapter.notifyDataSetChanged();
                }

                handleGetMainSuccess((JTBShoppingOrders) obj);
            }

            @Override
            public void onError(String errorStr, int code) {
                super.onError(errorStr, code);
                mLoadingLayout.hide();
                Log.d(TAG," onError errorStr="+errorStr+" code="+code);
//                netWorkError(code);
//                finish(isRefresh);
//                mRecyclerView.hasMore(false);
                mRecyclerView.refreshComplete();
                mRecyclerView.setOnLoadMoreComplete();
                ToastHelper.showToast(R.string.shopping_network_error_retry);

                if (Utils.listIsNullOrEmpty(mOrderList)) {
                    mRecyclerView.showNetWorkError();
                    mRecyclerView.onFinishLoading(false, false);
                } else {
                    mRecyclerView.showData();
                    mRecyclerView.onFinishLoading(true, false);
                }
            }
        });
    }

    private void handleGetMainSuccess(JTBShoppingOrders shoppingOrders) {
        if (shoppingOrders == null) {
            netWorkError(0);
            return;
        }
        ShoppingOrdersBean ordersBean = shoppingOrders.getData();
        if (ordersBean == null) {
            netWorkError(0);
            return;
        }
        List<ShoppingOrderListBean> orderList = ordersBean.getOrder();
        if (orderList != null) {
            if (!orderList.isEmpty()) {
                mOrderList.addAll(orderList);
                mAdapter.notifyDataSetChanged();
            }
            if(Utils.listIsNullOrEmpty(mOrderList)){
                mRecyclerView.showEmpty();
            }else{
                mRecyclerView.showData();
            }

            mRecyclerView.onFinishLoading(false, mOrderListRequest.hasMore(orderList.size()));
//            mRecyclerView.hasMore(mOrderListRequest.hasMore(orderList.size()));
        } else if (mOrderList.size() == 0||orderList == null){
//            mLoadingLayout.setDefaultNoData();
            if(mOrderList.size() == 0)
            mRecyclerView.showEmpty();
            mRecyclerView.onFinishLoading(false, false);
        }
    }

    @Override
    public void onRequest(final int position, final String requestType) {
//        mLoadingLayout.setDefaultLoading();
//        mLoadingLayout.setVisibility(View.VISIBLE);
        mOrderListRequest.dealWithOrder(String.valueOf(mOrderList.get(position).getOrderId()), requestType, new IHttpCallBack() {
            @Override
            public <T> void onSuccess(T obj, String result) {
//                mLoadingLayout.hide();
//                mOrderList.remove(mOrderList.get(position));
//                mAdapter.notifyDataSetChanged();
//                if (mOrderList.size() == 0) {
////                    mLoadingLayout.setVisibility(View.VISIBLE);
////                    mLoadingLayout.setDefaultNoData();
//                    mRecyclerView.showEmpty();
//                }
                if (OrderRecyclerViewAdapter.ORDER_CANCEL_ORDER.equals(requestType)) {
                   ToastHelper.showToast("取消成功");
                } else if (OrderRecyclerViewAdapter.ORDER_CONFIRM_RECEIVING.equals(requestType)) {
                    ToastHelper.showToast("确认收货成功");
                } else if (OrderRecyclerViewAdapter.ORDER_DELETE_ORDER.equals(requestType)) {
                    ToastHelper.showToast("删除成功");
                }
                mRecyclerView.loadData();
            }

            @Override
            public void onError(String errorStr, int code) {
                mLoadingLayout.hide();
                ToastHelper.showToast( R.string.order_deal_with_fail_hint, Toast.LENGTH_SHORT);
            }
        });
    }

    private void netWorkError(int code) {
        if (mOrderList.isEmpty() && code == HttpCode.ERROR_NETWORK) {
//            mLoadingLayout.setDefaultNetworkError(true);
            mRecyclerView.showError();
        }
        if (mIsNext) {
            mIsNext = false;
            mRealPage--;
        }
    }

    private void finish(boolean isRefresh) {
//        mLoadingLayout.hide();
//        if (isRefresh) {
//            mRecyclerView.refreshComplete();
//        } else {
//            mRecyclerView.loadMoreComplete();
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PostEvent event) {
        if(SubcriberTag.PAY_SUCCESS.equals(event.tag)){
            //刷新列表
            mRealPage = 0;
            getOrderList(true);
        }else if(SubcriberTag.REFRESH_ORDER_LIST.equals(event.tag)){
            //刷新列表
            mRealPage = 0;
            getOrderList(true);
        }
    }

    private OrderDetailRequest mOrderDetailRequest;
    private void getOrderDetail(String orderId) {

        Log.d(TAG," ");
        mOrderDetailRequest = OrderDetailRequest.getInstance();
//        CommonConst.ORDERID = mOrderResultInfo.orderId;//设置当前订单

        mOrderDetailRequest.getOrderDetail(orderId, new HttpCallbackAdapter() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                if (BuildConfig.DEBUG) {
                    android.util.Log.v(TAG, result);
                }
                com.laka.live.util.Log.d(TAG," getOrderDetail result="+result);
                mLoadingLayout.hide();
//                mList.clear();

                JTBShoppingOrderDetail orderDetail = (JTBShoppingOrderDetail) obj;
                ShoppingOrderDetailBean orderDetailBean = orderDetail.getData();
                if (orderDetailBean == null||Utils.listIsNullOrEmpty(orderDetailBean.getLogistics())) {
//                    httpError(0);
                    ToastHelper.showToast("没有物流信息");
                    return;
                }
                orderDetailBean.selectLogisticsId = orderDetailBean.getLogistics().get(0).getLogisticsId()+"";

                OrderLogisticsActivity.startActivity(mContext,orderDetailBean);
            }

            @Override
            public void onError(String errorStr, int code) {
                ToastHelper.showToast("查看物流失败");
            }
        });
    }

    @Override
    public void onClickLogistics(String orderId) {
        getOrderDetail(orderId);
    }
}
