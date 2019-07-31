package com.laka.live.libyuv;

/**
 * Created by luwies on 16/7/26.
 */
public class LibYuv {

    static {
        System.loadLibrary("yuv");
    }

    public static native void convertNV21ToARGB(byte [] yuv, byte [] rgb, int width, int height);

    public static native void convertARGBToNV21(byte [] yuv, byte [] rgb, int width, int height);

    public static native void ARGBMirror(byte [] src, byte [] dst, int width, int height);

    public static native void ARGBRotate(byte [] src, byte [] dst, int width, int height);
}
