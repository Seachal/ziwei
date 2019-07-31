package com.laka.live.account.follow;

import android.app.Activity;

import com.laka.live.R;
import com.laka.live.common.ResponseCode;
import com.laka.live.msg.Msg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ToastHelper;

/**
 * Created by zwl on 2016/7/27.
 * Email-1501448275@qq.com
 */
public class FollowRequestHelper {

    private Activity mActivity;
    private boolean mIsRequesting = false;
    private boolean mIsCancelFollow = false;
    private boolean mIsAutoToastTips = false;
    private boolean isBlockFollowToast = false;

    public FollowRequestHelper() {
    }

    public void setAutoToastFailTips(boolean autoToastFailTips) {
        mIsAutoToastTips = autoToastFailTips;
    }

    public void startRequest(Activity activity, int userId, boolean isCancelFollow, final FollowRequestCallback callback) {
        if (callback == null) {
            return;
        }
        if (activity == null) {
            return;
        }
        if (mIsRequesting) {
            return;
        }
        mActivity = activity;
        startRequest(userId, isCancelFollow, callback);
    }

    public void startRequest(int userId, boolean isCancelFollow, final FollowRequestCallback callback) {
        mIsRequesting = true;
        mIsCancelFollow = isCancelFollow;
        Object tag = this;
        if (mActivity != null) {
            tag = mActivity;
        }
        if (isCancelFollow) {
            DataProvider.unFollow(tag, userId, new GsonHttpConnection.OnResultListener<Msg>() {
                @Override
                public void onSuccess(Msg msg) {
                    handleOnRequestSuccess(mIsCancelFollow, msg, callback);
                }

                @Override
                public void onFail(int errorCode, String errorMsg, String command) {
                    handleOnRequestFailed(mIsCancelFollow, errorCode, errorMsg, callback);
                }
            });
            return;
        }
        DataProvider.follow(tag, userId, new GsonHttpConnection.OnResultListener<Msg>() {
            @Override
            public void onSuccess(Msg msg) {
                handleOnRequestSuccess(mIsCancelFollow, msg, callback);
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                handleOnRequestFailed(mIsCancelFollow, errorCode, errorMsg, callback);
            }
        });
    }

    private void handleOnRequestSuccess(boolean isCancelFollow, Msg msg, FollowRequestCallback callback) {
        mIsRequesting = false;
        if (msg == null) {
            handleOnRequestFailed(isCancelFollow, Msg.NETWORK_ERROR_UNKNOW_ERROR, "", callback);
            return;
        }
        if (Msg.TLV_OK != msg.getCode()) {
            handleOnRequestFailed(isCancelFollow, msg.getCode(), msg.getError(), callback);
            return;
        }
        if (!isBlockFollowToast) {
            if (!isCancelFollow) {
                ToastHelper.showToast(ResourceHelper.getString(R.string.follow_success));
            }
        }
        callback.requestSuccess(isCancelFollow);
    }

    private void handleOnRequestFailed(boolean isCancelFollow, int errorCode, String errorMsg, FollowRequestCallback callback) {
        mIsRequesting = false;
        callback.requestFailed(isCancelFollow, errorCode, errorMsg);
        tryToastFailedTips(isCancelFollow, errorCode, errorMsg);
    }

    private void tryToastFailedTips(boolean isCancelFollow, int errorCode, String errorMsg) {
        if (!mIsAutoToastTips) {
            return;
        }
        switch (errorCode) {
            case ResponseCode.TLV_FAILD_DO_ONESELF:
                tryToastDefaultFailedTipsWithCode(isCancelFollow, "", ResourceHelper.getString(R.string.follow_self_error_tip));
                break;
            case ResponseCode.TLV_OP_USER_NOT_EXIST_FOLLOW_ID:
            case ResponseCode.TLV_OP_USER_NOT_EXIST_UNFOLLOW_ID:
                tryToastDefaultFailedTipsWithCode(isCancelFollow,
                        ResourceHelper.getString(R.string.follow_cancel_user_not_exist),
                        ResourceHelper.getString(R.string.follow_user_is_not_exist));
                break;
            case ResponseCode.FOLLOW_FAIL_BLOCK:
                tryToastDefaultFailedTipsWithCode(isCancelFollow,
                        "",
                        ResourceHelper.getString(R.string.follow_block_user));
                break;
            case ResponseCode.FOLLOW_FAIL_BLOCK_OTHER:
                tryToastDefaultFailedTipsWithCode(isCancelFollow,
                        "",
                        ResourceHelper.getString(R.string.follow_be_block));
                break;
            default:
                tryToastDefaultFailedTipsWithCode(isCancelFollow, errorMsg);
                break;
        }
    }

    private void tryToastDefaultFailedTipsWithCode(boolean isCancelFollow, String errorString) {
        tryToastDefaultFailedTipsWithCode(isCancelFollow,
                ResourceHelper.getString(R.string.cancel_follow_fail),
                ResourceHelper.getString(R.string.follow_fail));
    }

    private void tryToastDefaultFailedTipsWithCode(boolean isCancelFollow, String cancelTip, String sureTip) {
        if (isCancelFollow) {
            if (StringUtils.isNotEmpty(cancelTip)) {
                ToastHelper.showToast(cancelTip);
            }
        } else {
            if (StringUtils.isNotEmpty(sureTip)) {
                ToastHelper.showToast(sureTip);
            }
        }
    }

    public void setBlockFollowToast(boolean isBlock) {
        this.isBlockFollowToast = isBlock;
    }

    public interface FollowRequestCallback {
        void requestSuccess(boolean isCancelFollow);

        void requestFailed(boolean isCancelFollow, int errorCode, String errorMsg);
    }
}
