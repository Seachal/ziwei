package com.laka.live.help;

import java.util.Calendar;

/**
 * Created by ios on 16/10/16.
 */

public class NoDoubleClickManager {
    public static final int TYPE_ENTER_ZHIBO_ROOM = 1;
    public static final int MIN_CLICK_DELAY_TIME_ZHIBO = 1000;
    private static long lastClickTimeZhibo = 0;

    public static final int TYPE_SHARE_WEIBO = 2;
    public static final int MIN_CLICK_DELAY_TIME_SHARE_WEIBO = 1000;
    private static long lastClickTimeShareWeibo = 0;

    public static boolean isNoDoubleClick(int type){
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if(type==TYPE_ENTER_ZHIBO_ROOM){
            if (currentTime - lastClickTimeZhibo > MIN_CLICK_DELAY_TIME_ZHIBO) {
                lastClickTimeZhibo = currentTime;
                return true;
            }
        }else if(type==TYPE_SHARE_WEIBO){
            if (currentTime - lastClickTimeShareWeibo > MIN_CLICK_DELAY_TIME_SHARE_WEIBO) {
                lastClickTimeShareWeibo = currentTime;
                return true;
            }
        }
        return false;
    }
}
