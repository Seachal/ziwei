package com.laka.live.util;

import android.content.Context;

import com.laka.live.R;
import com.laka.live.ui.widget.LoadingDialog;

/**
 * Created by zwl on 2016/7/25.
 * Email-1501448275@qq.com
 */
public class LoadingDialogHelper {
    private static LoadingDialog mLoadingDialog;

    public static void showLoadingDialog(Context context) {
        if (context == null) {
            return;
        }
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
        mLoadingDialog = new LoadingDialog(context, R.style.loading_dialog, "");
        mLoadingDialog.setCancelable(true);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.show();//android 5.0以上 window leaked 不会崩溃，5.0以下待确认
    }

    public static void closeLoadingDialog() {
        if (mLoadingDialog == null) {
            return;
        }
        mLoadingDialog.dismiss();
        mLoadingDialog = null;
    }
}
