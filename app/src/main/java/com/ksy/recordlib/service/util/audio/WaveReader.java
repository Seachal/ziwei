package com.ksy.recordlib.service.util.audio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class WaveReader {
    private static final int WAV_HEADER_CHUNK_ID = 1380533830;
    private static final int WAV_FORMAT = 1463899717;
    private static final int WAV_FORMAT_CHUNK_ID = 1718449184;
    private static final int WAV_DATA_CHUNK_ID = 1684108385;
    private static final int STREAM_BUFFER_SIZE = 4096;
    private File mInFile;
    private BufferedInputStream mInStream;
    private int mSampleRate;
    private int mChannels;
    private int mSampleBits;
    private int mFileSize;
    private int mDataSize;

    public WaveReader(String var1, String var2) {
        this.mInFile = new File(var1 + File.separator + var2);
    }

    public WaveReader(File var1) {
        this.mInFile = var1;
    }

    public void openWave() throws IOException {
        FileInputStream var1 = new FileInputStream(this.mInFile);
        this.mInStream = new BufferedInputStream(var1, 4096);
        int var2 = readUnsignedInt(this.mInStream);
        if (var2 != 1380533830) {
            throw new InvalidWaveException(String.format("Invalid WAVE header chunk ID: %d", new Object[]{Integer.valueOf(var2)}));
        } else {
            this.mFileSize = readUnsignedIntLE(this.mInStream);
            int var3 = readUnsignedInt(this.mInStream);
            if (var3 != 1463899717) {
                throw new InvalidWaveException("Invalid WAVE format");
            } else {
                int var4 = readUnsignedInt(this.mInStream);
                if (var4 != 1718449184) {
                    throw new InvalidWaveException("Invalid WAVE format chunk ID");
                } else {
                    int var5 = readUnsignedIntLE(this.mInStream);
                    if (var5 != 16) {
                        ;
                    }

                    short var6 = readUnsignedShortLE(this.mInStream);
                    if (var6 != 1) {
                        throw new InvalidWaveException("Not PCM WAVE format");
                    } else {
                        this.mChannels = readUnsignedShortLE(this.mInStream);
                        this.mSampleRate = readUnsignedIntLE(this.mInStream);
                        int var7 = readUnsignedIntLE(this.mInStream);
                        short var8 = readUnsignedShortLE(this.mInStream);
                        this.mSampleBits = readUnsignedShortLE(this.mInStream);
                        int var9 = readUnsignedInt(this.mInStream);
                        if (var9 != 1684108385) {
                            throw new InvalidWaveException("Invalid WAVE data chunk ID");
                        } else {
                            this.mDataSize = readUnsignedIntLE(this.mInStream);
                        }
                    }
                }
            }
        }
    }

    public int getSampleRate() {
        return this.mSampleRate;
    }

    public int getChannels() {
        return this.mChannels;
    }

    public int getPcmFormat() {
        return this.mSampleBits;
    }

    public int getFileSize() {
        return this.mFileSize + 8;
    }

    public int getDataSize() {
        return this.mDataSize;
    }

    public int getLength() {
        return this.mSampleRate != 0 && this.mChannels != 0 && (this.mSampleBits + 7) / 8 != 0 ? this.mDataSize / (this.mSampleRate * this.mChannels * ((this.mSampleBits + 7) / 8)) : 0;
    }

    public int read(short[] var1, int var2) throws IOException {
        if (this.mChannels != 1) {
            return -1;
        } else {
            byte[] var3 = new byte[var2 * 2];
            int var4 = 0;
            int var5 = this.mInStream.read(var3, 0, var2 * 2);

            for (int var6 = 0; var6 < var5; var6 += 2) {
                var1[var4] = byteToShortLE(var3[var6], var3[var6 + 1]);
                ++var4;
            }

            return var4;
        }
    }

    public int read(short[] var1, short[] var2, int var3) throws IOException {
        if (this.mChannels != 2) {
            return -1;
        } else {
            byte[] var4 = new byte[var3 * 4];
            int var5 = 0;
            int var6 = this.mInStream.read(var4, 0, var3 * 4);

            for (int var7 = 0; var7 < var6; var7 += 2) {
                short var8 = byteToShortLE(var4[0], var4[var7 + 1]);
                if (var7 % 4 == 0) {
                    var1[var5] = var8;
                } else {
                    var2[var5] = var8;
                    ++var5;
                }
            }

            return var5;
        }
    }

    public void closeWaveFile() throws IOException {
        if (this.mInStream != null) {
            this.mInStream.close();
        }

    }

    private static short byteToShortLE(byte var0, byte var1) {
        return (short) (var0 & 255 | (var1 & 255) << 8);
    }

    private static int readUnsignedInt(BufferedInputStream var0) throws IOException {
        byte[] var2 = new byte[4];
        int var1 = var0.read(var2);
        return var1 == -1 ? -1 : (var2[0] & 255) << 24 | (var2[1] & 255) << 16 | (var2[2] & 255) << 8 | var2[3] & 255;
    }

    private static int readUnsignedIntLE(BufferedInputStream var0) throws IOException {
        byte[] var2 = new byte[4];
        int var1 = var0.read(var2);
        return var1 == -1 ? -1 : var2[0] & 255 | (var2[1] & 255) << 8 | (var2[2] & 255) << 16 | (var2[3] & 255) << 24;
    }

    private static short readUnsignedShortLE(BufferedInputStream var0) throws IOException {
        byte[] var2 = new byte[2];
        int var1 = var0.read(var2, 0, 2);
        return var1 == -1 ? -1 : byteToShortLE(var2[0], var2[1]);
    }
}

