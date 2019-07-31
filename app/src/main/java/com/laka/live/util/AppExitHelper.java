package com.laka.live.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.laka.live.R;
import com.laka.live.application.LiveApplication;
import com.laka.live.duiba.CreditActivity;

/**
 * Created by zwl on 2016/6/17.
 * Email-1501448275@qq.com
 */
public class AppExitHelper {
    private Context mContext;
    private boolean mIsFirst = true;
    private long mFirst;
    private long mSecond;

    public AppExitHelper(Context context) {
        mContext = context;
    }

    public void exitApplication(Activity mCurrentActivity) {
        mSecond = System.currentTimeMillis();
        if (mIsFirst || mSecond - mFirst >= 1000 * 5) {
            mFirst = System.currentTimeMillis();
            mIsFirst = false;
            ToastHelper.showToast(R.string.exit_application_alert, Toast.LENGTH_SHORT);
        } else if (mSecond - mFirst < 1000 * 3) {
            LiveApplication.getInstance().exitApp(mCurrentActivity);
        } else if (mSecond - mFirst < 1000 * 5) {
            mFirst = mSecond;
            ToastHelper.showToast(R.string.exit_application_alert, Toast.LENGTH_SHORT);
        }
    }
}
