package com.ksy.recordlib.service.util.audio;

import java.nio.ByteBuffer;

/**
 * Created by luwies on 16/8/8.
 */
public class AudioUtils {

    public static short[] byteToShortArray(byte[] var0, int var1) {
        short[] var2 = new short[var1];

        for (int var3 = 0; var3 < var2.length; ++var3) {
            var2[var3] = (short) (var0[var3 * 2] & 255 | (var0[var3 * 2 + 1] & 255) << 8);
        }

        return var2;
    }

    public static short[] byteToShortArray(ByteBuffer var0, int var1) {
        short[] var2 = new short[var1];

        for (int var3 = 0; var3 < var2.length; ++var3) {
            var2[var3] = (short) (var0.get(var3 * 2) & 255 | (var0.get(var3 * 2 + 1) & 255) << 8);
        }

        return var2;
    }

    public static byte[] shortToByteArray(short s) {
        byte[] targets = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }

    public static byte[] shortArrayToByteArray(short[] array) {
        byte[] bytes = new byte[array.length * 2];
        int index;
        for (int i = 0; i < array.length; i++) {
            index = i * 2;
            bytes[index] = (byte) (array[i] & 0xff);
            bytes[index + 1] = (byte) ((array[i] >> 8) & 0xff);
        }

        return bytes;
    }

    public static short[] mixVoice(short[] left, short[] right, float var2, float var3, int var4) {
        short[] var5 = new short[var4];

        for (int i = 0; i < var4; ++i) {
            int var7 = left[i] < 0 && right[i] < 0 ? -32768 : 32767;
            int var8 = (int) ((float) left[i] * var2);
            int var9 = (int) ((float) right[i] * var3);
            var5[i] = (short) (var8 + var9 - var8 * var9 / var7);
        }

        return var5;
    }

    public static void mixVoice(short[] src, short[] dst, float leftVolume, float rightVolume) {
        if (src == null || src.length == 0) {
            return;
        }
        if (dst == null || dst.length == 0) {
            return;
        }
        int size = dst.length;
        double f = 1.0f;//衰减因子
        int output;
        float leftValue;
        float rightValue;
        final int max = Short.MAX_VALUE;
        final int min = Short.MIN_VALUE;
        for (int i = 0; i < size; ++i) {
            /*int var7 = src[i] < 0 && dst[i] < 0 ? Byte.MIN_VALUE : Byte.MAX_VALUE;
            int var8 = (int) ((float) src[i] * leftVolume);
            int var9 = (int) ((float) dst[i] * rightVolume);
            dst[i] = (byte) (var8 + var9 - var8 * var9 / var7);*/
//            dst[i] = (byte) (src[i] * leftVolume + dst[i] * rightVolume);

            /*final float maxValue = 127.f;
            float sample1 = src[i] / maxValue * leftVolume;
            float sample2 = dst[i] / maxValue * rightVolume;
            float mixed = sample1 + sample2;
            if (mixed > 1.0f) {
                mixed = 1.0f;
            } else if (mixed < -1.0f) {
                mixed = -1.0f;
            }
            dst[i] = (byte) (mixed * maxValue);*/

            leftValue = src[i] * leftVolume;
            rightValue = dst[i] * rightVolume;

            leftValue = check(leftValue);
            rightValue = check(rightValue);
            output = (int) ((leftValue + rightValue) * f);

            if (output > max) {
                f = (double) max / (double) output;
                output = max;
            } else if (output < min) {
                f = (double) min / (double) (output);
                output = min;
            }

            if (f < 1) {
                f += ((double) 1 - f) / (double) 32;
            }
            dst[i] = (short) output;
        }
    }

    private static float check(float value) {
        if (value > Short.MAX_VALUE) {
            value = Short.MAX_VALUE;
        }
        if (value < Short.MIN_VALUE) {
            value = Short.MIN_VALUE;
        }
        return value;
    }

    public static void setVolume(byte[] audio, float volume) {
        short[] data = byteToShortArray(audio, audio.length / 2);
        int size = data.length;
        float temp;
        for (int i = 0; i < size; i++) {
            temp = data[i] * volume;
            temp = check(temp);
            data[i] = (short) temp;
        }
        byte[] temps = shortArrayToByteArray(data);
        System.arraycopy(temps, 0, audio, 0, audio.length);
    }

    public static void setVolume(short[] data, float volume) {
        int size = data.length;
        float temp;
        for (int i = 0; i < size; i++) {
            temp = data[i] * volume;
            temp = check(temp);
            data[i] = (short) temp;
        }
    }
}
