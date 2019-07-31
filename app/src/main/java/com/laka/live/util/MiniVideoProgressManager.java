package com.laka.live.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.laka.live.application.LiveApplication;

/**
 * @Author:summer
 * @Date:2018/12/12
 * @Description: 小视频播放进度管理
 */
public class MiniVideoProgressManager {

    private static final String MINI_VIDEO_PROGRESS_FILE_NAME = "mini_video_progress_file_name";

    /**
     * 通过 url 获取播放进度
     *
     * @param url
     * @return
     */
    public static int getVideoProgressByUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return 0;
        }
        SharedPreferences sp = LiveApplication.getInstance().getSharedPreferences(MINI_VIDEO_PROGRESS_FILE_NAME, Context.MODE_PRIVATE);
        return sp.getInt(url, 0);
    }

    /**
     * 保存播放进度
     *
     * @param progress
     * @param url
     */
    public static void saveVideoProgress(int progress, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        SharedPreferences.Editor edit = LiveApplication.getInstance().getSharedPreferences(MINI_VIDEO_PROGRESS_FILE_NAME, Context.MODE_PRIVATE).edit();
        edit.putInt(url, progress).commit();
    }

}
