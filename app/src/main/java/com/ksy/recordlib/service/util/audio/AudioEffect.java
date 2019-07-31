package com.ksy.recordlib.service.util.audio;

/**
 * Created by luwies on 16/8/8.
 */
public class AudioEffect {
    long handle = 0L;
    int channel;
    int sampleRate;


    public static final native String getVersionString();

    private static final native void setTempo(long var0, float var2);

    private static final native void setPitchSemiTones(long var0, float var2);

    private static final native void setSpeed(long var0, float var2);

    private static final native int setInputConfig(long var0, int var2, int var3);

    private static final native int putData(long var0, short[] var2, int var3);

    private static final native int getData(long var0, short[] var2, int var3);

    public static final native String getErrorString();

    private static final native long newInstance();

    private static final native void deleteInstance(long var0);

    public AudioEffect(int var1, int var2) {
        this.channel = var1;
        this.sampleRate = var2;
    }

    public void create() {
        this.handle = newInstance();
        setInputConfig(this.handle, this.sampleRate, this.channel);
    }

    public void destroy() {
        deleteInstance(this.handle);
        this.handle = 0L;
    }

    public int putData(short[] var1, int var2) {
        return putData(this.handle, var1, var2);
    }

    public int getData(short[] var1, int var2) {
        return getData(this.handle, var1, var2);
    }

    public void setTempo(float var1) {
        setTempo(this.handle, var1);
    }

    public void setPitchSemiTones(float var1) {
        setPitchSemiTones(this.handle, var1);
    }

    public void setSpeed(float var1) {
        setSpeed(this.handle, var1);
    }
}
