package com.laka.live.update;

import android.content.Context;

import com.laka.live.R;
import com.laka.live.application.LiveApplication;
import com.laka.live.bean.VersionInfo;
import com.laka.live.msg.QueryUpdateMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;

/**
 * Created by luwies on 16/4/8.
 * zwl update 16/7/26
 */
public class UpdateCheck implements GsonHttpConnection.OnResultListener<QueryUpdateMsg> {

    public static final int UPDATE_FLAG_AUTO = 0;//自动检测
    public static final int UPDATE_FLAG_ACTIVE = 1;//主动检测
    private Context mContext;
    private UpdateCallback mCallback;
    private boolean mIsShowFailedToast = true;
    private static UpdateCheck sUpdateCheck;

    private UpdateCheck() {
        mContext = LiveApplication.getInstance().getApplicationContext();
    }

    public static synchronized UpdateCheck getInstance() {
        if (sUpdateCheck == null) {
            sUpdateCheck = new UpdateCheck();
        }
        return sUpdateCheck;
    }

    public void checkEnableUpdate(UpdateCallback callback, int updateFlag) {
        mCallback = callback;
        DataProvider.updateCheck(mContext, updateFlag, this);
    }

    public void setIsShowFailedToast(boolean isShowFailedToast) {
        mIsShowFailedToast = isShowFailedToast;
    }

    @Override
    public void onSuccess(QueryUpdateMsg msg) {
        handleOnUpdateRequestSuccess(msg);
    }

    @Override
    public void onFail(int errorCode, String errorMsg, String command) {
        handleOnUpdateRequestFailed();
    }

    private void handleOnUpdateRequestSuccess(QueryUpdateMsg msg) {
        if (msg == null) {
            handleOnUpdateRequestFailed();
            return;
        }
        VersionInfo versionInfo = msg.getInfo();
        if (versionInfo == null) {
            if (mCallback != null) {
                mCallback.hasUpdate(UpdateResult.NONE, null);
            }
            return;
        }
        /*int localVersionCode = Utils.getVersionCode(mContext);
        if (localVersionCode >= versionInfo.getVersion()) {
            if (mCallback != null) {
                mCallback.hasUpdate(UpdateResult.NONE, versionInfo);
            }
            return;
        }*/
        if (mCallback != null) {
            mCallback.hasUpdate(versionInfo.isForce() ? UpdateResult.FORCE : UpdateResult.NORMAL, versionInfo);
        }
    }

    private void handleOnUpdateRequestFailed() {
        if (mIsShowFailedToast) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.check_update_fail));
        }
        if (mCallback != null) {
            mCallback.checkFailed();
        }
    }

    public enum UpdateResult {
        NONE,
        NORMAL,
        FORCE
    }

    public void clearUpdateCheck() {
        if (sUpdateCheck == null) {
            return;
        }
        sUpdateCheck = null;
    }

    public void setUpdateCallback(UpdateCallback callback) {
        mCallback = callback;
    }

    public interface UpdateCallback {
        void hasUpdate(UpdateResult updateResult, VersionInfo versionInfo);

        void checkFailed();
    }
}
