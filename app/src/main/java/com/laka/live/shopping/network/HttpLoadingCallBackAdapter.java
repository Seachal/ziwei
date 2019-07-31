package com.laka.live.shopping.network;


import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.util.ToastHelper;

/**
 * Created by linhz on 2016/3/19.
 * Email: linhaizhong@ta2she.com
 */
public abstract class HttpLoadingCallBackAdapter implements IHttpLoadingCallBack {
    private boolean mIsAutoLogin = true;

    public HttpLoadingCallBackAdapter() {
    }

    public HttpLoadingCallBackAdapter(boolean autoOpenLoginWindow) {
        mIsAutoLogin = autoOpenLoginWindow;
    }

    @Override
    public void onError(String errorStr, int code) {
        if (HttpCode.ERROR_CODE_INVALID_TOKEN == code) {
            if (AccountInfoManager.getInstance().checkUserIsLogin()) {
                ToastHelper.showToast(R.string.community_invalided_token);
            }
            AccountInfoManager.getInstance().loginOut();
            if (mIsAutoLogin) {
                AccountInfoManager.getInstance().tryOpenLoginWindow();
            }
        } else if (HttpCode.ERROR_PROHIBIT_LOGIN == code) {
            ToastHelper.showToast(R.string.community_prohibit_login);
            AccountInfoManager.getInstance().loginOut();
            AccountInfoManager.getInstance().tryOpenLoginWindow();
        } else if (HttpCode.ERROR_CODE_FROZEN_USER == code) {
            ToastHelper.showToast(R.string.community_frozen_account);
        } else if (HttpCode.ERROR_CODE_DEVICE_FRROZEN == code) {
            ToastHelper.showToast(R.string.community_device_frozen_account);
        }
    }
}
