package com.laka.live.music;

import com.coolerfall.download.DownloadCallback;
import com.laka.live.bean.MusicInfo;

/**
 * Created by luwies on 16/8/15.
 */
public abstract class MusicDownloadCallback extends DownloadCallback {

    public void onTaskAdded(MusicInfo musicInfo) {

    }

    public void onStop(int downloadId) {
        
    }

    public void onSuccess(int downloadId, String musicFilePath, String lyricsFilePath) {

    }


}
