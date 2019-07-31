package com.ksy.recordlib.service.util.audio;


import java.nio.ByteBuffer;

/**
 * Created by luwies on 16/8/8.
 */
public class AudioResample {
    public static final String TAG = "AudioResample";
    private AudioResample.AFormat in;
    private AudioResample.AFormat out;
    private boolean passthrough;
    private long instance;

    public AudioResample(AudioResample.AFormat var1, AudioResample.AFormat var2) {
        this.in = var1;
        this.out = var2;
        this.passthrough = this.isPassthrough();
        if(!this.passthrough) {
            this.instance = this._init(var1.sampleRate, var1.channelNum, var1.sampleFmt, var2.sampleRate, var2.channelNum, var2.sampleFmt);
        }

    }

    public ByteBuffer convert(ByteBuffer var1) {
        if(this.passthrough) {
            return var1;
        } else {
            ByteBuffer var2 = var1;
            if(!var1.isDirect()) {
                var2 = ByteBuffer.allocateDirect(var1.limit());
                var2.put(var1);
            }

            return this._convert(this.instance, var2);
        }
    }

    public void release() {
        if(!this.passthrough) {
            this._release(this.instance);
        }

    }

    private boolean isPassthrough() {
        return this.in.equals(this.out);
    }

    private native long _init(int var1, int var2, int var3, int var4, int var5, int var6);

    private native void _release(long var1);

    private native ByteBuffer _convert(long var1, ByteBuffer var3);

    public static class AFormat {
        public static final int SAMPLE_FMT_U8 = 0;
        public static final int SAMPLE_FMT_S16 = 1;
        public static final int SAMPLE_FMT_S32 = 2;
        public static final int SAMPLE_FMT_FLT = 3;
        public static final int SAMPLE_FMT_DBL = 4;
        public static final int SAMPLE_FMT_U8P = 5;
        public static final int SAMPLE_FMT_S16P = 6;
        public static final int SAMPLE_FMT_S32P = 7;
        public static final int SAMPLE_FMT_FLTP = 8;
        public static final int SAMPLE_FMT_DBLP = 9;
        private int sampleRate;
        private int channelNum;
        private int sampleFmt;

        public static int sampleFmtToBytes(int var0) {
            switch(var0) {
                case 0:
                case 5:
                    return 1;
                case 1:
                case 6:
                    return 2;
                case 2:
                case 3:
                case 7:
                case 8:
                    return 4;
                case 4:
                case 9:
                    return 8;
                default:
                    return 2;
            }
        }

        public AFormat(int var1, int var2, int var3) {
            this.sampleRate = var1;
            this.channelNum = var2;
            this.sampleFmt = var3;
        }

        public boolean equals(AudioResample.AFormat var1) {
            return this.channelNum == var1.channelNum && this.sampleRate == var1.sampleRate && this.sampleFmt == var1.sampleFmt;
        }
    }
}
