package com.laka.live.shopping.order.model;


import com.laka.live.shopping.bean.json2bean.JTBShareResult;
import com.laka.live.shopping.bean.json2bean.JTBShoppingOrders;
import com.laka.live.shopping.network.HttpCallbackAdapter;
import com.laka.live.shopping.network.HttpManager;
import com.laka.live.shopping.network.HttpMethod;
import com.laka.live.shopping.network.HttpUrls;
import com.laka.live.shopping.network.IHttpCallBack;
import com.laka.live.shopping.network.IHttpManager;
import com.laka.live.shopping.order.adapter.OrderRecyclerViewAdapter;

/**
 * Created by zhxu on 2016/1/7.
 * Email:357599859@qq.com
 */
public class OrderListRequest {

    private static OrderListRequest mOrderListRequest;
    private int mPageSize = 10;

    public OrderListRequest() {
    }

    public static OrderListRequest getInstance() {
        if (mOrderListRequest == null) {
            mOrderListRequest = new OrderListRequest();
        }
        return mOrderListRequest;
    }

    public boolean hasMore(int size) {
        return mPageSize == size;
    }

    /**
     * 获取订单列表
     */
    public void getOrderList(int page, int requestType, HttpCallbackAdapter callBack) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("a", "orderList");
        httpManager.addParams("status", String.valueOf(requestType));
        httpManager.addParams("page", String.valueOf(page));
        httpManager.addParams("pageSize", String.valueOf(mPageSize));
        httpManager.request(HttpUrls.GET_ORDER_LIST_URL, HttpMethod.GET, JTBShoppingOrders.class, callBack);
    }

    /**
     * 发布商品评价
     */
    public void postGoodsReview(OrderReviewInfo reviewInfo, IHttpCallBack callBack) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("a", "postGoodsReview");
        httpManager.addParams("orderGoodsId", reviewInfo.orderGoodsId + "");
        httpManager.addParams("content", reviewInfo.content);
        httpManager.addParams("rating", String.valueOf(reviewInfo.rating));
        httpManager.addParams("isAnony", String.valueOf(reviewInfo.isAnony));
        httpManager.addParams("orderId", String.valueOf(reviewInfo.orderId));
        httpManager.request(HttpUrls.URL_SHOP, HttpMethod.POST, JTBShareResult.class, callBack);
    }

    /**
     * 取消、确定订单
     *
     * @param orderId
     * @param requestType
     * @param callBack
     */
    public void dealWithOrder(String orderId, String requestType, IHttpCallBack callBack) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("a", requestType);
        httpManager.addParams("orderId", orderId);
        if (OrderRecyclerViewAdapter.ORDER_CANCEL_ORDER.equals(requestType)) {
            httpManager.request(HttpUrls.ORDER_CANCEL, HttpMethod.POST, JTBShareResult.class, callBack);
        } else if (OrderRecyclerViewAdapter.ORDER_CONFIRM_RECEIVING.equals(requestType)) {
            httpManager.request(HttpUrls.ORDER_CONFIRM, HttpMethod.POST, JTBShareResult.class, callBack);
        } else if (OrderRecyclerViewAdapter.ORDER_DELETE_ORDER.equals(requestType)) {
            httpManager.request(HttpUrls.ORDER_DELETE, HttpMethod.POST, JTBShareResult.class, callBack);
        }
    }
}
