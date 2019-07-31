package com.laka.live.video.ui.widget.videopop;

import android.app.Activity;
import android.content.Context;

/**
 * @Author:Rayman
 * @Date:2018/7/10
 * @Description:监测Context有效性
 */

public class ContextUtil {

    public static boolean isValidContext(final Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing()) {
                return false;
            }
        }
        return true;
    }
}
