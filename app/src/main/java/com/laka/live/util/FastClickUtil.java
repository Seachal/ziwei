package com.laka.live.util;

/**
 * Created by zwl on 2016/7/12.
 * Email-1501448275@qq.com
 */
public class FastClickUtil {
    private static final int CLICK_DURATION = 500;
    private long mLastTime;
    private static FastClickUtil sInstance;

    public static FastClickUtil getInstance() {
        if (sInstance == null) {
            sInstance = new FastClickUtil();
        }
        return sInstance;
    }

    private FastClickUtil() {
    }

    public boolean isFastClick() {
        long currentTimeMillis = System.currentTimeMillis();
        long time = currentTimeMillis - mLastTime;
        mLastTime = currentTimeMillis;
        return time < CLICK_DURATION;
    }
}
