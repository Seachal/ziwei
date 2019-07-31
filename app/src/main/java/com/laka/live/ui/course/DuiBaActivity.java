package com.laka.live.ui.course;


import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.account.login.LoginActivity;
import com.laka.live.application.LiveApplication;
import com.laka.live.bean.UserInfo;
import com.laka.live.duiba.CreditActivity;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.CertificateInfoMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.util.Common;
import com.laka.live.util.ToastHelper;

import java.net.URLEncoder;

import framework.ioc.Ioc;

/**
 * Created by Lyf on 2017/5/8.
 * 扩展竞吧类，以后有扩展，可以在这里加
 */
public class DuiBaActivity extends CreditActivity {

    private Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        CreditActivity.creditsListener = new CreditsListener() {
            @Override
            public void onShareClick(WebView webView, String shareUrl, String shareThumbnail, String shareTitle, String shareSubtitle) {
                showShareDialog("http://www.lakatv.com/ziwei/","滋味Live-打造有滋有味的生活",share.getShareTitle(mContext),shareThumbnail,false);
            }

            @Override
            public void onLoginClick(WebView webView, String currentUrl) {

                webView.loadUrl(Common.SERVER_URL + "duiba/autologin.php?token="+Ioc.get(UserInfo.class).getToken()+"&dbredirect="
                        + URLEncoder.encode(currentUrl));
                // 竞吧的token是依附于APP的token，但缓存时间比APP的短。
                // 在竞吧的token过期的同时，也要再判断APP的token是否也过期。
                // 如果APP的没过期，就重新加载竞吧的主页就行(只要APP的token有效)
                // 如果APP过期，checkTokenEXPIRE方法就会强制APP退出登录。
                checkTokenExpire();
            }

            @Override
            public void onCopyCode(WebView mWebView, String code) {

            }

            @Override
            public void onLocalRefresh(WebView mWebView, String credits) {

            }
        };

    }


    // 检查APP的token是否过期
    private void checkTokenExpire() {

        DataProvider.getApprove(this, new GsonHttpConnection.OnResultListener<CertificateInfoMsg>() {

            @Override
            public void onSuccess(CertificateInfoMsg msg) {}

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {

                // 20 用户token过期，TLV_USER_TOKEN_EXPIRE，过期时间为7天
                // 5 错误的用户token，TLV_WRONG_USER_TOEKN
                // 4 用户未登录，TLV_USER_NOT_LOGIN
                if (errorCode == 20 || errorCode == 5 || errorCode == 4) {
                    ToastHelper.showToast(R.string.login_outdate);
                    AccountInfoManager.getInstance().loginOut();
                    LiveApplication.getInstance().exitApp(mContext);
                    LoginActivity.startActivity(mContext);
                    EventBusManager.postEvent(0, SubcriberTag.FINISH_HOME_ACATIVITY);
                }

            }

        });

    }
}
