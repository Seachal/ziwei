package com.laka.live.account.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.laka.live.R;
import com.laka.live.application.LiveApplication;
import com.laka.live.msg.UserMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.util.Common;
import com.laka.live.util.NetworkUtil;
import com.laka.live.util.ToastHelper;
import com.laka.taste.wxapi.WXHelper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zwl on 2016/8/1.
 * Email-1501448275@qq.com
 */
public class ThirdPlatformLoginHelper {

    public static final int PLATFORM_QQ = 1;
    public static final int PLATFORM_WEI_BO = 2;
    public static final int PLATFORM_WE_CHAT = 3;
    public static final int AUTHORIZE_CODE_FAIL = -1;
    public static final int AUTHORIZE_CODE_CANCEL = -2;
    public static final int AUTHORIZE_CODE_DENY = -3;
    public static final int LOGIN_CODE_FAIL = -4;
    public static final int LOGIN_CODE_NO_NETWORK = -5;
    public static final int ERROR_CODE_WE_CHAT_NOT_INSTALL = -6;
    public static final int ERROR_CODE_WEI_BO_NOT_INSTALL = -7;
    public static final int ERROR_CODE_QQ_NOT_INSTALL = -8;
    private static final String TEN_CENT_REQUEST_STRING = "get_simple_userinfo";

    private Tencent mTenCent;
    private WXHelper mWXHelper;
    private SsoHandler mSsoHandler;
    private IUiListener mTenCentIUiListener;
    private static ThirdPlatformLoginHelper sHelper;
    private int mCurrentPlatForm;

    private ThirdPlatformLoginHelper() {
    }

    public static ThirdPlatformLoginHelper getInstance() {
        if (sHelper == null) {
            sHelper = new ThirdPlatformLoginHelper();
        }
        return sHelper;
    }

    /**
     * qq登录授权
     *
     * @param activity activity
     * @param callback LoginResultCallback
     */
    public void loginWithQQAuthorize(final Activity activity, final LoginResultCallback callback) {
        if (callback == null) {
            return;
        }
        if (!NetworkUtil.isNetworkOk(activity)) {
            callback.loginFailed(PLATFORM_QQ, LOGIN_CODE_NO_NETWORK);
            return;
        }
        if (mTenCent == null) {
            mTenCent = Tencent.createInstance(Common.QQ_APP_ID,
                    LiveApplication.getInstance().getApplicationContext());
        }
        if (mTenCent.isSessionValid()) {
            callback.loginFailed(PLATFORM_QQ, AUTHORIZE_CODE_FAIL);
            return;
        }
        if (mTenCentIUiListener == null) {
            mTenCentIUiListener = new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    JSONObject jsonObject = (JSONObject) o;
                    try {
                        String openId = jsonObject.getString(Common.OPENID);
                        String accessToken = jsonObject.getString(Common.ACCESS_TOKEN);
                        tryLoginWithQQ(activity, accessToken, openId, callback);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        handleOnLoginFailed(PLATFORM_QQ, AUTHORIZE_CODE_FAIL, callback);
                    }
                }

                @Override
                public void onError(UiError uiError) {
                    handleOnLoginFailed(PLATFORM_QQ, AUTHORIZE_CODE_FAIL, callback);
                }

                @Override
                public void onCancel() {
                    handleOnLoginFailed(PLATFORM_QQ, AUTHORIZE_CODE_CANCEL, callback);
                }
            };
        }
        mCurrentPlatForm = PLATFORM_QQ;
        mTenCent.login(activity, TEN_CENT_REQUEST_STRING, mTenCentIUiListener);
    }

    /**
     * 微博登录授权
     *
     * @param activity activity
     * @param callback LoginResultCallback (待测试)
     */
    public void loginWithWeiBoAuthorize(final Activity activity, final LoginResultCallback callback) {
        if (callback == null) {
            return;
        }
        if (!NetworkUtil.isNetworkOk(activity)) {
            callback.loginFailed(PLATFORM_WEI_BO, LOGIN_CODE_NO_NETWORK);
            return;
        }
        AuthInfo authInfo = new AuthInfo(activity, Common.SINA_WEIBO_APP_KEY,
                Common.SINA_WEIBO_REDIRECT_URL, Common.SINA_WEIBO_SCOPE);
        if (mSsoHandler == null) {
            mSsoHandler = new SsoHandler(activity, authInfo);
        }
//        if (!mSsoHandler.isWeiboAppInstalled()) {
//            callback.loginFailed(PLATFORM_WEI_BO, ERROR_CODE_WEI_BO_NOT_INSTALL);
//            ToastHelper.showToast(ResourceHelper.getString(R.string.login_error_not_install_wei_bo));
//            return;
//        }
        mCurrentPlatForm = PLATFORM_WEI_BO;
        mSsoHandler.authorize(new WeiboAuthListener() {
            @Override
            public void onComplete(Bundle bundle) {
                Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(bundle);
                tryLoginWithWeiBo(activity, accessToken.getToken(), accessToken.getUid(), callback);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                handleOnLoginFailed(PLATFORM_WEI_BO, AUTHORIZE_CODE_FAIL, callback);
            }

            @Override
            public void onCancel() {
                handleOnLoginFailed(PLATFORM_WEI_BO, AUTHORIZE_CODE_CANCEL, callback);
            }
        });
    }

    public void loginWithWeChatAuthorize(Context context, AuthorizeCallback callback) {
        if (mWXHelper == null) {
            mWXHelper = new WXHelper(context);
        }
        if (!mWXHelper.isInstallWeChat()) {
            ToastHelper.showToast(R.string.please_install_wechat);
            callback.handleOnCallbackIsNull();
            return;
        }
        mCurrentPlatForm = PLATFORM_WE_CHAT;
        mWXHelper.sendAuthReq();
    }

    public void handleOnWeChatAuthorize(Activity activity, SendAuth.Resp resp, LoginResultCallback callback) {
        if (resp == null) {
            callback.loginFailed(PLATFORM_WE_CHAT, AUTHORIZE_CODE_FAIL);
            return;
        }
        int errorCode = resp.errCode;
        if (errorCode == BaseResp.ErrCode.ERR_OK) {
            tryLoginWithWeChat(activity, resp.code, callback);
            return;
        }
        if (errorCode == BaseResp.ErrCode.ERR_AUTH_DENIED) {
            callback.loginFailed(PLATFORM_WE_CHAT, AUTHORIZE_CODE_DENY);
            return;
        }
        if (errorCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
            callback.loginFailed(PLATFORM_WE_CHAT, AUTHORIZE_CODE_CANCEL);
            return;
        }
        callback.loginFailed(PLATFORM_WE_CHAT, AUTHORIZE_CODE_FAIL);

    }

    private void tryLoginWithWeChat(Activity activity, String tokenCode, final LoginResultCallback callback) {
        DataProvider.loginWithWechat(activity, activity, tokenCode, new GsonHttpConnection.OnResultListener<UserMsg>() {
            @Override
            public void onSuccess(UserMsg userMsg) {
                handleOnLoginSuccess(PLATFORM_WE_CHAT, userMsg, callback);
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                handleOnLoginFailed(PLATFORM_WE_CHAT, errorCode, callback);
            }
        });
    }

    private void tryLoginWithQQ(Activity activity, String token, String openId, final LoginResultCallback callback) {
        DataProvider.loginWithQQ(activity, activity, token, openId, new GsonHttpConnection.OnResultListener<UserMsg>() {
            @Override
            public void onSuccess(UserMsg userMsg) {
                handleOnLoginSuccess(PLATFORM_QQ, userMsg, callback);
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                handleOnLoginFailed(PLATFORM_QQ, errorCode, callback);
            }
        });
    }

    private void tryLoginWithWeiBo(Activity activity, String accessToken, String uid, final LoginResultCallback callback) {
        DataProvider.loginWithSinaWeibo(activity, activity, accessToken, uid, new GsonHttpConnection.OnResultListener<UserMsg>() {
            @Override
            public void onSuccess(UserMsg userMsg) {
                handleOnLoginSuccess(PLATFORM_WEI_BO, userMsg, callback);
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                handleOnLoginFailed(PLATFORM_WEI_BO, errorCode, callback);
            }
        });
    }

    private void handleOnLoginSuccess(int platform, UserMsg userMsg, LoginResultCallback callback) {
        callback.loginSuccess(platform, userMsg);
    }

    private void handleOnLoginFailed(int platform, int errorCode, LoginResultCallback callback) {
        callback.loginFailed(platform, errorCode);
    }

    public void handleOnTenCentResultData(Intent data, AuthorizeCallback callback) {
        if (callback == null) {
            return;
        }
        if (mTenCent == null) {
            if (mCurrentPlatForm == PLATFORM_QQ) {
                callback.handleOnCallbackIsNull();
            }
            return;
        }
        mTenCent.handleResultData(data, mTenCentIUiListener);
    }

    public void handleOnWeiBoResultCallbackData(int requestCode, int resultCode, Intent data, AuthorizeCallback callback) {
        if (callback == null) {
            return;
        }
        if (mSsoHandler == null) {
            if (mCurrentPlatForm == PLATFORM_WEI_BO) {
                callback.handleOnCallbackIsNull();
            }
            return;
        }
        mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
    }

    public void clearHelper() {
        mTenCent = null;
        if (mWXHelper != null) {
            mWXHelper.unRegister();
            mWXHelper = null;
        }
        mSsoHandler = null;
        sHelper = null;
        mCurrentPlatForm = 0;
    }

    public interface AuthorizeCallback {
        void handleOnCallbackIsNull();
    }

    public interface LoginResultCallback {
        void loginSuccess(int platform, UserMsg userMsg);

        void loginFailed(int platform, int errorCode);
    }
}
