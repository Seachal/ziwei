package com.laka.live.help;


import android.app.Activity;
import android.view.View;

import java.util.Calendar;

/**
 * Created by ios on 16/10/15.
 */

public abstract class NoDoubleClickListener implements View.OnClickListener {
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v,null);
        }
    }

    public void onClick(Activity activity) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(null,activity);
        }
    }

    protected abstract void onNoDoubleClick(View v,Activity activity);
}
