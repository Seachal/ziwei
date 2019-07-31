package com.laka.live.update;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.support.v4.app.ActivityCompat;

import com.laka.live.R;
import com.laka.live.application.LiveActivityManager;
import com.laka.live.application.LiveApplication;
import com.laka.live.bean.VersionInfo;
import com.laka.live.ui.activity.MainActivity;
import com.laka.live.ui.widget.dialog.GenericDialog;
import com.laka.live.ui.widget.dialog.IDialogOnClickListener;
import com.laka.live.ui.widget.dialog.SimpleTextDialog;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;

import framework.utils.GsonTools;

/**
 * Created by zwl on 2016/7/26.
 * Email-1501448275@qq.com
 */
public class UpdateCheckManager {

    private static UpdateCheckManager sUpdateCheckManager;
    private VersionInfo mVersionInfo;
    private Activity mActivity;
    private SimpleTextDialog mUpdateInfoDialog;
    private boolean mIsHasAutoCheckedUpdate;
    private boolean mLimitCheckUpdate = false;
    private boolean mIsLimitCheckUpdateOnResume = false;
    private boolean mIgnoreUpdateNow = false;
    private boolean mHasShowUpdateDialogInMainActivity = false;

    private UpdateCheckManager() {
        mIsHasAutoCheckedUpdate = false;
    }

    public static synchronized UpdateCheckManager getInstance() {
        if (sUpdateCheckManager == null) {
            sUpdateCheckManager = new UpdateCheckManager();
        }
        return sUpdateCheckManager;
    }

    public boolean isHasAutoCheckedUpdate() {
        return mIsHasAutoCheckedUpdate;
    }

    public void setIsHasAutoCheckedUpdate(boolean hasAutoCheckedUpdate) {
        mIsHasAutoCheckedUpdate = hasAutoCheckedUpdate;
    }

    public boolean isLimitCheckUpdate() {
        return mLimitCheckUpdate;
    }

    public void setLimitCheckUpdate(boolean limitCheckUpdate) {
        mLimitCheckUpdate = limitCheckUpdate;
    }

    public boolean isLimitCheckUpdateOnResume() {
        return mIsLimitCheckUpdateOnResume;
    }

    public void setIsLimitCheckUpdateOnResume(boolean isLimitCheckUpdateOnResume) {
        this.mIsLimitCheckUpdateOnResume = isLimitCheckUpdateOnResume;
    }

    public boolean isIgnoreUpdateNow() {
        return mIgnoreUpdateNow;
    }

    public void setIgnoreUpdateNow(boolean ignoreUpdateNow) {
        this.mIgnoreUpdateNow = ignoreUpdateNow;
    }

    public boolean isHasShowUpdateDialogInMainActivity() {
        return mHasShowUpdateDialogInMainActivity;
    }

    public void setHasShowUpdateDialogInMainActivity(boolean hasShowUpdateDialogInMainActivity) {
        this.mHasShowUpdateDialogInMainActivity = hasShowUpdateDialogInMainActivity;
    }

    /**
     * 查询更新
     * @param activity
     */
    public static void checkUpdate(Activity activity) {

        // 保存更新所在的Activity
        getInstance().setActivity(activity);

        if (getInstance().isIgnoreUpdateNow() || getInstance().isHasShowUpdateDialogInMainActivity()) {
            return;
        }
        if (UpdateCheckManager.getInstance().isIgnoreUpdateNow()) {
            return;
        }
        if (UpdateCheckManager.getInstance().isHasShowUpdateDialogInMainActivity()) {
            return;
        }
        UpdateCheck.getInstance().setIsShowFailedToast(false);
        UpdateCheck.getInstance().checkEnableUpdate(new UpdateCheck.UpdateCallback() {
            @Override
            public void hasUpdate(UpdateCheck.UpdateResult updateResult, VersionInfo versionInfo) {
                getInstance().handleOnCheckUpdateSuccess(updateResult, versionInfo);
            }

            @Override
            public void checkFailed() {}

        }, UpdateCheck.UPDATE_FLAG_AUTO);

    }

    private void handleOnCheckUpdateSuccess(UpdateCheck.UpdateResult result, VersionInfo versionInfo) {
        if (result == UpdateCheck.UpdateResult.NONE) {
            return;
        }
        showUpdateInfoDialog(versionInfo);
        UpdateCheckManager.getInstance().setHasShowUpdateDialogInMainActivity(true);
    }

    private void showUpdateInfoDialog(final VersionInfo versionInfo) {

        mVersionInfo = versionInfo;

        if (mUpdateInfoDialog == null) {

            mUpdateInfoDialog = new SimpleTextDialog(getActivity());
            mUpdateInfoDialog.setTitle(ResourceHelper.getString(R.string.update_tips));
            mUpdateInfoDialog.setText(versionInfo.getDescription());
            mUpdateInfoDialog.addYesNoButton(ResourceHelper.getString(R.string.upgrade), ResourceHelper.getString(R.string.cancel));
            mUpdateInfoDialog.setRecommendButton(GenericDialog.ID_BUTTON_YES);
            mUpdateInfoDialog.setCanceledOnTouchOutside(false);
            mUpdateInfoDialog.setCancelable(false);
            mUpdateInfoDialog.setOnClickListener(new IDialogOnClickListener() {
                @Override
                public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                    if (viewId == GenericDialog.ID_BUTTON_YES) {
                        handleOnClickSureUpdateApp(versionInfo);
                    } else {
                        handleOnClickCancelUpdate(versionInfo);
                    }
                    return false;
                }
            });
        }
        if (mUpdateInfoDialog.isShowing()) {
            return;
        }
        mUpdateInfoDialog.show();
    }

    private void handleOnClickSureUpdateApp(VersionInfo versionInfo) {
        UpdateHelper helper = new UpdateHelper(getActivity(), versionInfo);
        helper.downloadApk();
        if (!versionInfo.isForce()) {
            UpdateCheckManager.getInstance().setIgnoreUpdateNow(true);
        }
    }

    private void handleOnClickCancelUpdate(VersionInfo versionInfo) {
        if (versionInfo.isForce()) {
            LiveApplication.getInstance().killAllProcess(getActivity());
        } else {
            UpdateCheckManager.getInstance().setIgnoreUpdateNow(true);
        }
    }

    public Activity getActivity() {
        // 如果mActivity为空，就获取处于栈顶的activity，确保activity不为空
        if(mActivity == null)
            mActivity = LiveActivityManager.getInstance().getTopActivity();

        return mActivity;
    }

    public void setActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }
}
