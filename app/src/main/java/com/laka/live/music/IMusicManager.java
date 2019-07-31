package com.laka.live.music;

/**
 * Created by luwies on 16/8/12.
 */
public interface IMusicManager {

    void loadLocalMusic();

    void startDownload();

    void stopDownload();

    void delete();
}
