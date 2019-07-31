package com.laka.live.ui.widget.danmu;

import android.graphics.Bitmap;

/**
 * Created by crazyguan on 2016/4/14.
 */
public interface IBitmapListener {
    void onSuccess(Bitmap bitmap);
    void onFail(int code);
}
