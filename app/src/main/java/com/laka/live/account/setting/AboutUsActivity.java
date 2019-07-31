package com.laka.live.account.setting;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.account.login.LoginActivity;
import com.laka.live.application.LiveApplication;
import com.laka.live.bean.VersionInfo;
import com.laka.live.config.SystemConfig;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.activity.WebActivity;
import com.laka.live.ui.widget.dialog.GenericDialog;
import com.laka.live.ui.widget.dialog.IDialogOnClickListener;
import com.laka.live.ui.widget.dialog.SimpleTextDialog;
import com.laka.live.update.UpdateCheck;
import com.laka.live.update.UpdateCheckManager;
import com.laka.live.update.UpdateHelper;
import com.laka.live.util.Common;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;

public class AboutUsActivity extends BaseActivity implements View.OnClickListener {
    private SimpleTextDialog mUpdateInfoDialog;

    private final static int REQUEST_EXTERNAL_STORAGE = 200 ;

    private VersionInfo mVersionInfo;

    public static void startActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, AboutUsActivity.class);
            ActivityCompat.startActivity(activity, intent, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        init();
    }

    private void init() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            TextView version = (TextView) findViewById(R.id.version);
            version.setText(getString(R.string.version_info, info.versionName));
        } catch (PackageManager.NameNotFoundException e) {

        }
        findViewById(R.id.user_protocol).setOnClickListener(this);
        findViewById(R.id.help_center).setOnClickListener(this);
        findViewById(R.id.about_laka).setOnClickListener(this);

        TextView checkUpdateButton = (TextView) findViewById(R.id.check_update);
        checkUpdateButton.setOnClickListener(this);

        if (SystemConfig.getInstance().isShowUpgrade()) {
            checkUpdateButton.setVisibility(View.VISIBLE);
        } else {
            checkUpdateButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_protocol:
                WebActivity.startActivity(this, Common.USER_PRO_URL, getString(R.string.user_protocol));
                break;
            case R.id.check_update:
                checkUpdate();
                break;
            case R.id.help_center:
                WebActivity.startActivity(this, Common.HELP_URL, getString(R.string.help_center));
                break;
            case R.id.about_laka:
                WebActivity.startActivity(this, Common.CONTACT_MOBILE, getString(R.string.about_laka));
                break;
        }
    }

    private void checkUpdate() {
        showLoadingDialog();
        UpdateCheck.getInstance().setIsShowFailedToast(true);
        UpdateCheck.getInstance().checkEnableUpdate(new UpdateCheck.UpdateCallback() {
            @Override
            public void hasUpdate(UpdateCheck.UpdateResult updateResult, VersionInfo versionInfo) {
                handleOnCheckUpdateResult(updateResult, versionInfo);
            }

            @Override
            public void checkFailed() {
                dismissLoadingsDialog();
            }
        },UpdateCheck.UPDATE_FLAG_ACTIVE);

    }

    private void handleOnCheckUpdateResult(UpdateCheck.UpdateResult result, VersionInfo versionInfo) {
        dismissLoadingsDialog();
        if (result == UpdateCheck.UpdateResult.NONE) {
            showToast(R.string.has_no_new_version_tip);
        } else {
            showUpdateInfoDialog(versionInfo);
        }
    }

    public void showUpdateInfoDialog(final VersionInfo versionInfo) {
        mVersionInfo = versionInfo;
        showButtonDialog(ResourceHelper.getString(R.string.update_tips), versionInfo.getDescription(), R.string.upgrade, R.string.cancel,
                false, false,true,true, new IDialogOnClickListener() {
                    @Override
                    public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                        if (viewId == GenericDialog.ID_BUTTON_YES) {
                            if (Utils.checkPermission(AboutUsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                handleOnClickSureUpdateApp(versionInfo);
                            } else {
                                ActivityCompat.requestPermissions(AboutUsActivity.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_EXTERNAL_STORAGE);
                                return true;
                            }
                        } else {
                            handleOnClickCancelUpdate(versionInfo);
                        }
                        return false;
                    }
                });
//        if (mUpdateInfoDialog == null) {
//            mUpdateInfoDialog = new SimpleTextDialog(this);
//            mUpdateInfoDialog.setTitle(ResourceHelper.getString(R.string.update_tips));
//            mUpdateInfoDialog.setText(versionInfo.getDescription());
//            mUpdateInfoDialog.addYesNoButton(ResourceHelper.getString(R.string.upgrade), ResourceHelper.getString(R.string.cancel));
//            mUpdateInfoDialog.setRecommendButton(GenericDialog.ID_BUTTON_YES);
//            mUpdateInfoDialog.setCanceledOnTouchOutside(false);
//            mUpdateInfoDialog.setCancelable(false);
//            mUpdateInfoDialog.setOnClickListener(new IDialogOnClickListener() {
//                @Override
//                public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
//                    if (viewId == GenericDialog.ID_BUTTON_YES) {
//                        handleOnClickSureUpdateApp(versionInfo);
//                    } else {
//                        handleOnClickCancelUpdate(versionInfo);
//                    }
//                    return false;
//                }
//            });
//        }
//        if (mUpdateInfoDialog.isShowing()) {
//            return;
//        }
//        mUpdateInfoDialog.show();
    }

    private void handleOnClickSureUpdateApp(VersionInfo versionInfo) {
        UpdateHelper helper = new UpdateHelper(AboutUsActivity.this, versionInfo);
        helper.downloadApk();
        if (!versionInfo.isForce()) {
            UpdateCheckManager.getInstance().setIgnoreUpdateNow(true);
        }
    }

    private void handleOnClickCancelUpdate(VersionInfo versionInfo) {
        if (versionInfo.isForce()) {
            LiveApplication.getInstance().killAllProcess(AboutUsActivity.this);
        } else {
            UpdateCheckManager.getInstance().setIgnoreUpdateNow(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults != null && grantResults.length >= 2) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                            grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        handleOnClickSureUpdateApp(mVersionInfo);
                    } else {
                        showToast(R.string.no_storage_permission_tip);
                    }
                }
                break;
        }
    }
}
