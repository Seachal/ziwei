package com.laka.live.shopping.collect;

import android.content.Context;


import com.laka.live.R;
import com.laka.live.application.LiveApplication;
import com.laka.live.shopping.common.CommonConst;
import com.laka.live.shopping.network.HttpCallbackAdapter;
import com.laka.live.shopping.network.HttpCode;
import com.laka.live.shopping.network.HttpManager;
import com.laka.live.shopping.network.HttpMethod;
import com.laka.live.shopping.network.HttpUrls;
import com.laka.live.shopping.network.IHttpManager;
import com.laka.live.util.NetworkUtil;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ToastHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by linhz on 2015/12/23.
 * Email: linhaizhong@ta2she.com
 */
public class CollectRequest {

    private Context mContext;
    private int mCollectType;
    private boolean mIsRequesting = false;

    private boolean mIsAutoToast = true;

    public CollectRequest(Context context, int collectType) {
        this.mContext = context;
        this.mCollectType = collectType;
    }

    public void setAutoToast(boolean autoToast) {
        mIsAutoToast = autoToast;
    }

    /**
     * 添加收藏
     *
     * @param id
     * @param callback
     */
    public void addCollectRequest(int id, CollectRequestResultCallback callback) {
        sendCollectRequest(String.valueOf(id), CommonConst.COLLECT_ADD, callback);
    }

    /**
     * 取消收藏
     *
     * @param id
     * @param callback
     */
    public void cancelCollectRequest(int id, CollectRequestResultCallback callback) {
        sendCollectRequest(String.valueOf(id), CommonConst.COLLECT_CANCEL, callback);
    }

    public void sendCollectRequest(final String id, final int collectionFlag, final
    CollectRequestResultCallback callback) {
        if (callback == null) {
            return;
        }
        if (!NetworkUtil.isNetworkOk(LiveApplication.getInstance())) {
            callback.onResultFail(CommonConst.ERROR_TYPE_NETWORK);
            return;
        }

        if (mIsRequesting) {
            callback.onResultFail(CommonConst.ERROR_TYPE_REQUESTING);
            return;
        }

        mIsRequesting = true;
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("a", "collectPost");
        httpManager.addParams("postId", id);
        httpManager.addParams("collectType", String.valueOf(mCollectType));
        httpManager.addParams("collectFlag", String.valueOf(collectionFlag));
        httpManager.request(HttpUrls.COMMUNITY_URL, HttpMethod.POST, new HttpCallbackAdapter() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                mIsRequesting = false;
                if (StringUtils.isNotEmpty(result)) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int code = jsonObject.getInt("status");
                        if (HttpCode.SUCCESS == code) {
                            handleCollectSuccess(collectionFlag, callback);
                        } else {
                            handleCollectFailed(collectionFlag, CommonConst.ERROR_TYPE_DATA, callback);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        handleCollectFailed(collectionFlag, CommonConst.ERROR_TYPE_DATA, callback);
                    }
                }
            }

            @Override
            public void onError(String errorStr, int code) {
                mIsRequesting = false;
                handleCollectFailed(collectionFlag, CommonConst.ERROR_TYPE_NETWORK, callback);
                super.onError(errorStr, code);
            }
        });
    }

    private void handleCollectFailed(int collectionFlag, int errorType, CollectRequestResultCallback callback) {
        mIsRequesting = false;
        callback.onResultFail(errorType);
        if (mIsAutoToast) {
            int textRes;
            if (collectionFlag == CommonConst.COLLECT_CANCEL) {
                textRes = R.string.collect_cancel_failed;
            } else {
                textRes = R.string.collect_collect_failed;
            }
            ToastHelper.showToast( textRes);
        }
    }

    private void handleCollectSuccess(int collectionFlag, CollectRequestResultCallback callback) {
        mIsRequesting = false;
        callback.onResultSuccess();
        if (mIsAutoToast) {
            int textRes;
            if (collectionFlag == CommonConst.COLLECT_CANCEL) {
                textRes = R.string.collect_cancel_collect_success;
            } else {
                textRes = R.string.collect_collected;
            }
            ToastHelper.showToast(  textRes);
        }
    }

    public interface CollectRequestResultCallback {
        void onResultFail(int errorType);

        void onResultSuccess();
    }
}
