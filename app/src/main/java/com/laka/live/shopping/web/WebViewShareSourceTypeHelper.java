package com.laka.live.shopping.web;


/**
 * Created by gangqing on 2016/5/17.
 * Email:denggangqing@ta2she.com
 */
public class WebViewShareSourceTypeHelper {
    public static String convertType2String(int sourceType) {
        String result = "word";
        // TODO: 2017/7/14 share
//        if (sourceType == ShareRequest.TYPE_ARTICLE) {
//            result = "word";
//        } else if (sourceType == ShareRequest.TYPE_CIRCLE) {
//            result = "circle";
//        } else if (sourceType == ShareRequest.TYPE_POST) {
//            result = "post";
//        } else if (sourceType == ShareRequest.TYPE_PRODUCT) {
//            result = "product";
//        } else if (sourceType == ShareRequest.TYPE_SHARE_TA_SHE) {
//            result = "h5_app";
//        }
        return result;
    }
}
