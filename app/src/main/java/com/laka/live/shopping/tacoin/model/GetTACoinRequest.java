package com.laka.live.shopping.tacoin.model;


import com.laka.live.shopping.bean.SelfTaCoinJsonBean;
import com.laka.live.shopping.network.HttpCallbackAdapter;
import com.laka.live.shopping.network.HttpCode;
import com.laka.live.shopping.network.HttpManager;
import com.laka.live.shopping.network.HttpMethod;
import com.laka.live.shopping.network.HttpUrls;
import com.laka.live.shopping.network.IHttpManager;

/**
 * Created by zhangwulin on 2016/3/16.
 * email 1501448275@qq.com
 */
public class GetTACoinRequest {

    private boolean isRequesting = false;

    public GetTACoinRequest() {
    }

    public void startRequestCoin(final RequestSelfCoinCallback callback) {
        if (callback == null) {
            return;
        }
        if (isRequesting) {
            return;
        }
        isRequesting = true;
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("a", "myCoinCount");
        httpManager.request(HttpUrls.URL_INDEX, HttpMethod.POST, SelfTaCoinJsonBean.class, new HttpCallbackAdapter(false) {
            @Override
            public void onError(String errorStr, int code) {
                super.onError(errorStr, code);
                handlerOnFail(code, "", callback);
            }

            @Override
            public <T> void onSuccess(T obj, String result) {
                handlerOnSuccess((SelfTaCoinJsonBean) obj, callback);
            }
        });
    }

    private void handlerOnFail(int errorCode, String errorTip, RequestSelfCoinCallback callback) {
        isRequesting = false;
        callback.resultFail(errorCode, errorTip);
    }

    private void handlerOnSuccess(SelfTaCoinJsonBean coinJsonBean, RequestSelfCoinCallback callback) {
        isRequesting = false;
        if (coinJsonBean.getStatus() != HttpCode.SUCCESS) {
            callback.resultFail(coinJsonBean.getStatus(), coinJsonBean.getMsg());
            return;
        }
        callback.resultSuccess(coinJsonBean.getData().getCoinCount());
    }

    public interface RequestSelfCoinCallback {
        void resultSuccess(String count);

        void resultFail(int errorCode, String errorTip);
    }
}
