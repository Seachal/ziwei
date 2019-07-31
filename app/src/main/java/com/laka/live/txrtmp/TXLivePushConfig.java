package com.laka.live.txrtmp;

/**
 * Created by luwies on 16/7/23.
 */

import android.graphics.Bitmap;

public class TXLivePushConfig {
    public static final int DEFAULT_MAX_VIDEO_BITRATE = 1200;
    public static final int DEFAULT_MIN_VIDEO_BITRATE = 800;
    int mCustomModeType = 0;
    int mAudioSample = '걄';
    int mAudioBitrate;
    int mAudioChannels;
    int mVideoFPS = 20;
    int mVideoResolution = 0;
    int mVideoBitrate = 800;
    int mMaxVideoBitrate = 1200;
    int mMinVideoBitrate = 800;
    int mBeautyLevel = 0;
    int mWhiteningLevel = 0;
    int mConnectRetryCount = 3;
    int mConnectRetryInterval = 3;
    Bitmap mWatermark;
    int mWatermarkX = 0;
    int mWatermarkY = 0;
    int mVideoEncodeGop = 3;
    String mVideoPreProcessLibrary;
    String mVideoPreProcessFuncName;
    String mAudioPreProcessLibrary;
    String mAudioPreProcessFuncName;
    boolean mFrontCamera = true;
    boolean mEnableANS = false;
    boolean mAutoAdjustBitrate = true;
    boolean mAutoAdjustResolution = true;
    boolean mHardwareAccel = false;
    boolean mTouchFocus = true;
    int mHomeOrientation = 1;

    public TXLivePushConfig() {
    }

    public void setCustomModeType(int modeType) {
        this.mCustomModeType = modeType;
    }

    public void setAudioSampleRate(int sample) {
        this.mAudioSample = sample;
    }

    public void setAudioChannels(int channels) {
        this.mAudioChannels = channels;
    }

    public void setAudioBitrate(int bitrate) {
        this.mAudioBitrate = bitrate;
    }

    public void setANS(boolean enable) {
        this.mEnableANS = enable;
    }

    public void setVideoFPS(int fps) {
        this.mVideoFPS = fps;
    }

    public void setVideoResolution(int resolution) {
        this.mVideoResolution = resolution;
    }

    public void setAutoAdjustResolution(boolean enable) {
        this.mAutoAdjustResolution = enable;
    }

    public void setVideoBitrate(int bitrate) {
        this.mVideoBitrate = bitrate;
    }

    public void setAutoAdjustBitrate(boolean enable) {
        this.mAutoAdjustBitrate = enable;
    }

    public void setMaxVideoBitrate(int maxBitrate) {
        this.mMaxVideoBitrate = maxBitrate;
    }

    public void setMinVideoBitrate(int minBitrate) {
        this.mMinVideoBitrate = minBitrate;
    }

    public void setBeautyFilter(int beautyLevel, int whiteningLevel) {
        this.mBeautyLevel = beautyLevel;
        this.mWhiteningLevel = whiteningLevel;
    }

    public void setHardwareAcceleration(boolean enable) {
        this.mHardwareAccel = enable;
    }

    public void setFrontCamera(boolean front) {
        this.mFrontCamera = front;
    }

    public void setConnectRetryCount(int count) {
        this.mConnectRetryCount = count;
    }

    public void setConnectRetryInterval(int interval) {
        this.mConnectRetryInterval = interval;
    }

    public void setTouchFocus(boolean enable) {
        this.mTouchFocus = enable;
    }

    public void setWatermark(Bitmap watermark, int x, int y) {
        this.mWatermark = watermark;
        this.mWatermarkX = x;
        this.mWatermarkY = y;
    }

    public void setVideoEncodeGop(int gop) {
        this.mVideoEncodeGop = gop;
    }

    public void setHomeOrientation(int homeOrientation) {
        this.mHomeOrientation = homeOrientation;
    }

    public void setCustomVideoPreProcessLibrary(String libraryPath, String funcName) {
        this.mVideoPreProcessLibrary = libraryPath;
        this.mVideoPreProcessFuncName = funcName;
    }

    public void setCustomAudioPreProcessLibrary(String libraryPath, String funcName) {
        this.mAudioPreProcessLibrary = libraryPath;
        this.mAudioPreProcessFuncName = funcName;
    }
}
