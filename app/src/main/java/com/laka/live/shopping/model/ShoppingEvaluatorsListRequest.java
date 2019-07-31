package com.laka.live.shopping.model;


import com.laka.live.shopping.CommunityConstant;
import com.laka.live.shopping.bean.ShoppingEvaluatorBean;
import com.laka.live.shopping.bean.json2bean.JTBShoppingEvaluatorList;
import com.laka.live.shopping.network.HttpCallbackAdapter;
import com.laka.live.shopping.network.HttpManager;
import com.laka.live.shopping.network.HttpMethod;
import com.laka.live.shopping.network.HttpUrls;
import com.laka.live.shopping.network.IHttpManager;

import java.util.List;

/**
 * Created by zhangwulin on 2016/3/29.
 * email 1501448275@qq.com
 */
public class ShoppingEvaluatorsListRequest {
    private String mGoodsId;
    private String mEndId = CommunityConstant.START_END_ID;
    private String mPageSize = "20";
    private boolean mHasMore = true;
    private boolean mIsRequesting = false;

    public ShoppingEvaluatorsListRequest(String goodsId) {
        this.mGoodsId = goodsId;
    }

    public void startRequestByType(boolean isLoadMore, RequestEvaluatorsListCallback callback) {
        if (!isLoadMore) {
            mEndId = CommunityConstant.START_END_ID;
        }
        startRequest(mEndId, callback);
    }

    private void startRequest(String endId, final RequestEvaluatorsListCallback callBack) {
        if (callBack == null) {
            return;
        }
        if (mIsRequesting) {
            return;
        }
        mIsRequesting = true;
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("a", "evaluatorList");
        httpManager.addParams("goodsId", mGoodsId);
        httpManager.addParams("endId", endId);
        httpManager.addParams("pageSize", mPageSize);
        httpManager.request(HttpUrls.URL_SHOP, HttpMethod.POST, JTBShoppingEvaluatorList.class, new HttpCallbackAdapter() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                handleOnSuccess((JTBShoppingEvaluatorList) obj, callBack);
            }

            @Override
            public void onError(String errorStr, int code) {
                super.onError(errorStr, code);
                handleOnFail(code, callBack);
            }
        });
    }

    private void handleOnFail(int code, RequestEvaluatorsListCallback callback) {
        mIsRequesting = false;
        callback.onFailed(code);
    }

    private void handleOnSuccess(JTBShoppingEvaluatorList jtbShoppingEvaluatorList, RequestEvaluatorsListCallback callback) {
        mIsRequesting = false;
        if (jtbShoppingEvaluatorList == null || jtbShoppingEvaluatorList.getData() == null) {
            handleOnFail(CommunityConstant.ERROR_TYPE_DATA, callback);
            return;
        }
        mEndId = jtbShoppingEvaluatorList.getData().getEndId();
        if (jtbShoppingEvaluatorList.getData().getEvaluators().size() == 0) {
            mHasMore = false;
        } else {
            mHasMore = true;
        }
        callback.onSuccess(jtbShoppingEvaluatorList.getData().getEvaluators());
    }

    public boolean isHasMoreData() {
        return mHasMore;
    }

    public interface RequestEvaluatorsListCallback {
        void onFailed(int errorCode);

        void onSuccess(List<ShoppingEvaluatorBean> evaluatorBeans);
    }
}
