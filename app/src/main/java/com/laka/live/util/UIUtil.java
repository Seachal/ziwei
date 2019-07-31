package com.laka.live.util;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.WindowManager;
import android.widget.PopupWindow;

import java.lang.reflect.Method;

/**
 * Created by luwies on 16/3/4.
 */
public class UIUtil {
    /**
     *
     * @param popupWindow
     * @param touchModal false 点击可以穿透
     */
    public static void setPopupWindowTouchModal(PopupWindow popupWindow,
                                                boolean touchModal) {
        if (null == popupWindow) {
            return;
        }
        Method method;
        try {

            method = PopupWindow.class.getDeclaredMethod("setTouchModal",
                    boolean.class);
            method.setAccessible(true);
            method.invoke(popupWindow, touchModal);

        }
        catch (Exception e) {
        }

    }

    public static void setPopupWindowWindowLayoutType(PopupWindow popupWindow, int type) {
        if (null == popupWindow) {
            return;
        }
        Method method;
        try {
            method = PopupWindow.class.getDeclaredMethod("setWindowLayoutType", int.class);
            method.setAccessible(true);
            method.invoke(popupWindow, type);
        }
        catch (Exception e) {
        }
    }

    public static IBinder getActivityToken(Activity activity) {
        if (activity == null) {
            return null;
        }
        Method method;
        IBinder token = null;
        try {
            method = Activity.class.getDeclaredMethod("getActivityToken");
            method.setAccessible(true);
            token = (IBinder) method.invoke(activity);
        }
        catch (Exception e) {
        }
        return token;
    }

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        if (context != null) {
            WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);

            int width = wm.getDefaultDisplay().getWidth();
            return width;
        }
        return 0;
    }

    public static int getScreenHeight(Context context) {
        if (context != null) {
            WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);

            int width = wm.getDefaultDisplay().getHeight();
            return width;
        }
        return 0;
    }

}
