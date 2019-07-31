package com.laka.live.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;

import com.laka.live.service.KeepAliveService;
import com.orhanobut.logger.Logger;

/**
 * @Author:Rayman
 * @Date:2018/9/5
 * @Description:*---*（保活工具类）
 */

public class KeepAliveUtil {

    private Context context;
    private boolean isFirstStart = true;
    private boolean isReject = false;
    private boolean isManualDismiss = false;
    private AlertDialog dialog;

    public KeepAliveUtil(Context context) {
        this.context = context;
    }

    /**
     * 处理保活逻辑
     */
    public void handleKeepAliveLogic() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (isReject || !isFirstStart) {
                    return;
                }
                isFirstStart = SharePreferenceUtil.isFirstStart();
                if (isFirstStart) {
                    Logger.e("第一次开启app啊");
                    if (!NotificationsUtils.isNotificationEnabled(context)) {
                        Logger.e("按道理是没权限的啊");
                        showAliveDialog();
                    } else {
                        startKeepAliveService();
                    }
                } else {
                    startKeepAliveService();
                }
            } else {
                startKeepAliveService();
            }
        } catch (Exception ex) {
            return;
        }
    }

    private void startKeepAliveService() {
        Intent intent = new Intent();
        intent.setClass(context, KeepAliveService.class);
        context.startService(intent);
    }

    /**
     * 提示权限框~
     */
    private void showAliveDialog() {
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        dialog = new AlertDialog.Builder(context)
                .setTitle("开启消息推送")
                .setMessage("开启消息推送后，能实时收到第一手热门课程信息和关注主播的课程动态哟~")
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isReject = true;
                        isManualDismiss = true;
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isReject = false;
                        isManualDismiss = true;
                        dialog.dismiss();
                        NotificationsUtils.requestNotificationPermission();

                    }
                }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //点击外部消失的~
                        if (!isManualDismiss) {
                            isReject = true;
                        }
                    }
                }).create();
        dialog.show();
    }
}
