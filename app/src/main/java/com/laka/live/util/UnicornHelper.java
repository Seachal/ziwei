package com.laka.live.util;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.account.login.LoginActivity;
import com.laka.live.bean.UserInfo;
import com.laka.live.shopping.activity.ShoppingGoodsDetailActivity;
import com.laka.live.ui.activity.WebActivity;
import com.laka.live.ui.course.detail.CourseDetailActivity;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.ProductDetail;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.YSFUserInfo;

import java.util.Map;

import framework.utils.GsonTools;

/**
 * @Author Lyf
 * @CreateTime 2018/3/9
 * @Description 七鱼在线客服，辅助工具类
 **/
public class UnicornHelper {

    private final static String TITLE = "滋味Live客服";
    private final static String URI = "http://ziweilive.qiyukf.com";

    // 这两个参数，用于截取url时用的。
    private final static String GOODS_ID = "goodsId";
    private final static String COURSE_ID = "course_id";

    /**
     * 打开七鱼在线客服聊天页面
     *
     * @param activity
     * @param productDetail
     */
    public static void openServiceActivity(Activity activity, ProductDetail productDetail) {

        if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
            LoginActivity.startActivity(activity, LoginActivity.TYPE_FROM_NEED_LOGIN);
            return;
        }

        // 设置标题和域名
        ConsultSource source = new ConsultSource(URI, TITLE, "");

        // 设置用户信息
        UserInfo userInfo = AccountInfoManager.getInstance().getAccountInfo();
        YSFUserInfo xsfUserInfo = new YSFUserInfo();
        xsfUserInfo.authToken = userInfo.getToken();
        xsfUserInfo.userId = userInfo.getIdStr();
        // 数据格式严格按[{key:type, value:content}]. 而我们常用的是：{type:content}. 这两者，需要注意一下。
        xsfUserInfo.data = "[{\"key\":\"real_name\", \"value\":\"" + "(" + userInfo.getIdStr() + ")" + userInfo.getNickName() + "\"}, {\"key\":\"mobile_phone\", \"value\":\"" + userInfo.getPhone() + "\"}]";
        Log.log("data=" + xsfUserInfo.data);
        Unicorn.setUserInfo(xsfUserInfo);

        // 设置商品信息(如果有的话，进去的时候，会自动发一条关于商品信息的消息给客服)
        if (productDetail != null) {
            source.productDetail = productDetail;

        }

        // 打开聊天页面
        Unicorn.openServiceActivity(activity, TITLE, source);
    }

    /**
     * 自定义，URL链接点击响应。
     * 即自已处理带Url的消息的点击事件。
     *
     * @param context
     * @param url
     */
    public static void onUrlClicked(Context context, String url) {

        if (Utils.isNotEmpty(url)) {

            Map<String, String> params = StringUtils.splitUrl(url);

            //Log.log("params="+ GsonTools.toJson(params));

            if (params != null && params.size() > 0) {

                String courseId = params.get(COURSE_ID);

                // 有课程ID，跳课程ID。
                if (Utils.isNotEmpty(courseId)) {
                    CourseDetailActivity.startActivity(context, courseId);
                    return;
                }

                String goodsId = params.get(GOODS_ID);
                // 有商品ID，跳商品ID。
                if (Utils.isNotEmpty(goodsId)) {
                    ShoppingGoodsDetailActivity.startActivity(context, Integer.parseInt(goodsId));
                    return;
                }

            }

            // 如果没有课程ID和商品ID，就跳WebView。
            WebActivity.startActivity(context, url, "");
        }

    }

}
