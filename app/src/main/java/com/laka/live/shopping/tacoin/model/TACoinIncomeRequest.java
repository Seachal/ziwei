package com.laka.live.shopping.tacoin.model;


import com.laka.live.shopping.bean.TACoinIncomeBean;
import com.laka.live.shopping.network.HttpCallbackAdapter;
import com.laka.live.shopping.network.HttpManager;
import com.laka.live.shopping.network.HttpMethod;
import com.laka.live.shopping.network.HttpUrls;
import com.laka.live.shopping.network.IHttpManager;

/**
 * Created by gangqing on 2016/3/16.
 * Email:denggangqing@ta2she.com
 */
public class TACoinIncomeRequest {
    public void requestIncomeData(String type, HttpCallbackAdapter callbackAdapter, int endId, int pageSize) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("a", "coinDetailList");
        httpManager.addParams("type", type);
        httpManager.addParams("pageSize", String.valueOf(pageSize));
        httpManager.addParams("endId", String.valueOf(endId));
        httpManager.request(HttpUrls.URL_INDEX, HttpMethod.POST, TACoinIncomeBean.class, callbackAdapter);
    }
}
