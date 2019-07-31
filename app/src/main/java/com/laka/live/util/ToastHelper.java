package com.laka.live.util;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.laka.live.application.LiveApplication;

/**
 * Created by zwl on 2016/6/17.
 * Email-1501448275@qq.com
 */
public class ToastHelper {
    private static Toast sToast;
    private static final int DEFAULT_GRAVITY = -1;

    public static void showToast(int resId) {
        showToast(resId, Toast.LENGTH_SHORT);
    }

    public static void showToast(CharSequence text) {
        showToast(text, Toast.LENGTH_SHORT);
    }

    public static void showToast(int resId, int duration) {
        String message = ResourceHelper.getString(resId);
        showToast(message, duration);
    }

    public static void showToast(final CharSequence text, final int duration) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                showToast(text, duration, DEFAULT_GRAVITY, 0, 0);
            }
        });
    }

    public static void showCenterToast(String text) {
        showToast(text, Toast.LENGTH_SHORT, Gravity.CENTER, 0, 0);
    }

    public static void showToast(CharSequence text, int duration, int gravity, int xOffset, int yOffset) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (sToast == null) {
            sToast = Toast.makeText(LiveApplication.getInstance(), text, duration);
        } else {
            sToast.setText(text);
            sToast.setDuration(duration);
        }
        if (DEFAULT_GRAVITY != gravity) {
            sToast.setGravity(gravity, xOffset, yOffset);
        }
        sToast.show();
    }

    /*public static void leakToast() {
        if (sToast == null) {
            return;
        }
        sToast.cancel();
        sToast = null;
    }*/
}
