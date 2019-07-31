package com.laka.live.download;

/**
 * Created by zwl on 16/8/8.
 */

public enum DownloadState {
    WAITING(0), STARTED(1), FINISHED(2), STOPPED(3), ERROR(4), NONE(5);

    private final int value;

    DownloadState(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static DownloadState valueOf(int value) {
        switch (value) {
            case 0:
                return WAITING;
            case 1:
                return STARTED;
            case 2:
                return FINISHED;
            case 3:
                return STOPPED;
            case 4:
                return ERROR;
            case 5:
                return NONE;
            default:
                return NONE;
        }
    }
}
