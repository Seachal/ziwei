package com.laka.live.shopping.shoppingcar;

import com.google.gson.Gson;
import com.laka.live.shopping.bean.ShoppingCarGoodsState;
import com.laka.live.shopping.bean.ShoppingCarParamBean;
import com.laka.live.shopping.bean.json2bean.JTBShoppingCarGoods;
import com.laka.live.shopping.bean.json2bean.newversion.JTBShoppingGoodsCount;
import com.laka.live.shopping.common.CommonConst;
import com.laka.live.shopping.network.HttpCallbackAdapter;
import com.laka.live.shopping.network.HttpManager;
import com.laka.live.shopping.network.HttpMethod;
import com.laka.live.shopping.network.HttpUrls;
import com.laka.live.shopping.network.IHttpCallBack;
import com.laka.live.shopping.network.IHttpManager;
import com.laka.live.util.ToastHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by chenjiawei on 2016/1/4.
 */
public class ShoppingCarRequest {
    private int mPageIndex = CommonConst.START_PAGE_INDEX;
    private boolean mIsRequesting = false;
    private boolean mHasMore = false;
    private ShoppingCarParamBean mShoppingCarParamBean;
    private List<ShoppingCarParamBean> mShoppingCarParamBeans;

    public ShoppingCarRequest() {
    }

    public ShoppingCarRequest(ShoppingCarParamBean entity) {
        this.mShoppingCarParamBean = entity;
    }

    public ShoppingCarRequest(List<ShoppingCarParamBean> entitys) {
        this.mShoppingCarParamBeans = entitys;
    }

    public void startRequest(final RequestResultCallback callback) {
        if (callback == null) {
            return;
        }
        mIsRequesting = true;
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();

        httpManager.request(HttpUrls.CART_URL + "detail", HttpMethod.GET, JTBShoppingCarGoods.class, new HttpCallbackAdapter() {
            @Override
            public <T> void onSuccess(T obj, String result) {

                if (obj == null) {
                    handleGetShoppingCarDataFailed(CommonConst.ERROR_TYPE_DATA, callback);
                    return;
                }

                JTBShoppingCarGoods shoppingCarGoods = (JTBShoppingCarGoods) obj;
                handleGetShoppingCarDataSuccess(shoppingCarGoods.getData(), callback);
            }

            @Override
            public void onError(String errorStr, int code) {
                super.onError(errorStr, code);
                handleGetShoppingCarDataFailed(code, callback);
            }
        });
    }

    public void startShoppingCarAddRequest(int courseId, int videoId, IHttpCallBack callback) {
        if (callback == null) {
            return;
        }
        mIsRequesting = true;
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("goodsId", mShoppingCarParamBean.getGoodsId());
        httpManager.addParams("skuId", mShoppingCarParamBean.getSpecId());
        httpManager.addParams("count", mShoppingCarParamBean.getGoodsCount());

        if (courseId > 0) {
            httpManager.addParams("courseId", String.valueOf(courseId));
        }

        if (videoId > 0) {
            httpManager.addParams("miniVideoId", String.valueOf(videoId));
        }
        httpManager.request(HttpUrls.SHOPPING_ADD_SHOPPING_CAR, HttpMethod.POST, JTBShoppingGoodsCount.class, callback);
    }

    public void startShoppingCarUpdateRequest(final ShoppingCarRequestResultCallback callback) {
        if (callback == null) {
            return;
        }
        mIsRequesting = true;
        Gson gson = new Gson();
        String json = gson.toJson(mShoppingCarParamBeans);
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("items", json.replaceAll("id", "itemId"));
        httpManager.request(HttpUrls.CART_URL + "edit", HttpMethod.POST, new HttpCallbackAdapter() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                int state = 1;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    state = jsonObject.getInt("status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (state == 0) {
                    ToastHelper.showToast("保存成功");
                    handleShoppingCarSuccess(callback);
                } else {
                    handleShoppingCarFailed(CommonConst.ERROR_TYPE_DATA, callback);
                }
            }

            @Override
            public void onError(String errorStr, int code) {
                super.onError(errorStr, code);
                handleShoppingCarFailed(code, callback);
            }
        });
    }

    public void startShoppingCarDeleteRequest(final ShoppingCarRequestResultCallback callback) {
        if (callback == null) {
            return;
        }
        mIsRequesting = true;
        Gson gson = new Gson();
        String json = gson.toJson(mShoppingCarParamBeans);
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("items", json.replaceAll("id", "itemId"));
        httpManager.request(HttpUrls.CART_URL + "delete", HttpMethod.POST, new HttpCallbackAdapter() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                int state = 1;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    state = jsonObject.getInt("status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (state == 0) {
                    handleShoppingCarSuccess(callback);
                } else {
                    handleShoppingCarFailed(CommonConst.ERROR_TYPE_DATA, callback);
                }
            }

            @Override
            public void onError(String errorStr, int code) {
                super.onError(errorStr, code);
                handleShoppingCarFailed(code, callback);
            }
        });
    }

    public void startGetShoppingCarCountRequest(final ShoppingCarCountResultCallback callback) {
        if (callback == null) {
            return;
        }
        mIsRequesting = true;
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("a", "cartCount");
        httpManager.request(HttpUrls.URL_SHOP, HttpMethod.POST, new HttpCallbackAdapter() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                int state = 1;
                int count = 0;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    state = jsonObject.getInt("status");
                    JSONObject jsonObject2 = new JSONObject(jsonObject.getString("data"));
                    count = jsonObject2.getInt("cartCount");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (state == 0) {
                    handleShoppingCarCountSuccess(count, callback);
                } else {
                    handleGetShoppingCarCountFailed(CommonConst.ERROR_TYPE_DATA, callback);
                }
            }

            @Override
            public void onError(String errorStr, int code) {
                super.onError(errorStr, code);
                handleGetShoppingCarCountFailed(code, callback);
            }
        });
    }

    private void handleGetShoppingCarDataSuccess(ShoppingCarGoodsState shoppingCarGoodsBeans, RequestResultCallback callback) {
        callback.onResultSuccess(shoppingCarGoodsBeans);
    }

    private void handleShoppingCarSuccess(ShoppingCarRequestResultCallback callback) {
        callback.onResultSuccess();
    }

    private void handleShoppingCarCountSuccess(int count, ShoppingCarCountResultCallback callback) {
        callback.onResultSuccess(count);
    }

    private void handleGetShoppingCarCountFailed(int errorType, ShoppingCarCountResultCallback callback) {
        callback.onResultFail(errorType);
    }

    private void handleGetShoppingCarDataFailed(int errorType, RequestResultCallback callback) {
        callback.onResultFail(errorType);
    }

    private void handleShoppingCarFailed(int errorType, ShoppingCarRequestResultCallback callback) {
        callback.onResultFail(errorType);
    }

    public interface RequestResultCallback {
        void onResultFail(int errorType);

        void onResultSuccess(ShoppingCarGoodsState shoppingCarGoodsData);
    }


    public interface ShoppingCarRequestResultCallback {
        void onResultFail(int errorType);

        void onResultSuccess();
    }

    public interface ShoppingCarCountResultCallback {
        void onResultFail(int errorType);

        void onResultSuccess(int count);
    }
}
