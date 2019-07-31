package com.ksy.recordlib.service.util.audio;

public interface OnProgressListener {
    int BGM_ERROR_NONE = 0;
    int BGM_ERROR_UNKNOWN = 1;
    int BGM_ERROR_NOT_SUPPORTED = 2;
    int BGM_ERROR_IO = 3;
    int BGM_ERROR_MALFORMED = 4;

    void onMusicProgress(long var1, long var3);

    void onMusicStopped();

    void onMusicError(int var1);

}
