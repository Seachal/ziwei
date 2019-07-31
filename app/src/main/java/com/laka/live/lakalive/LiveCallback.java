package com.laka.live.lakalive;

/**
 * Created by luwies on 16/10/8.
 */
public interface LiveCallback {

    void onPublishSucc();

    void onPublishStop(int retCode);

    void onPlaySucc();

    void onPlayStopEvent(int retCode, String streamID, String liveChannel);

    void onVideoSizeChanged(int width, int height);

    void onCaptureVideoSize(int width, int height);

    void onPlayQualityUpdate(int quality);

    void onPublishQulityUpdate(int quality);

}
