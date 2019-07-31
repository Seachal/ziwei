package com.laka.live.shopping.tacoin.model;

import android.content.Context;

import com.laka.live.shopping.bean.TACoinMainBean;
import com.laka.live.shopping.network.HttpManager;
import com.laka.live.shopping.network.HttpMethod;
import com.laka.live.shopping.network.HttpUrls;
import com.laka.live.shopping.network.IHttpCallBack;
import com.laka.live.shopping.network.IHttpManager;


/**
 * Created by gangqing on 2016/3/15.
 * Email:denggangqing@ta2she.com
 */
public class TACoinMainRequest {

    /**
     * 每日签到
     */
    public static void requestDayLogin(final Context context) {
//        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
//        httpManager.addParams("a", "dayLogin");
//        httpManager.request(HttpUrls.URL_INDEX, HttpMethod.POST, JTBEvaluateResult.class, new HttpCallbackAdapter(false) {
//            @Override
//            public <T> void onSuccess(T obj, String result) {
//                if (obj instanceof JTBEvaluateResult) {
//                    JTBEvaluateResult evaluateResult = (JTBEvaluateResult) obj;
//                    LevelInfoBean levelInfoBean = evaluateResult.getData();
//                    String title = ResourceHelper.getString(R.string.ta_coin_main_item_login_title);
//                    if (evaluateResult.getStatus() == HttpCode.SUCCESS && levelInfoBean != null) {
//                        ToastHelper.showToast(context, levelInfoBean, title, TaCoinToast.TYPE_LOGIN);
//                    }
//                }
//            }
//
//            @Override
//            public void onError(String errorStr, int code) {
//                super.onError(errorStr, code);
//            }
//        });
    }

    /**
     * TA币主界面数据请求
     *
     * @param callBack
     */
    public void requestMainData(IHttpCallBack callBack) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("a", "myCoinDetail");
        httpManager.request(HttpUrls.URL_INDEX, HttpMethod.POST, TACoinMainBean.class, callBack);
    }

}
