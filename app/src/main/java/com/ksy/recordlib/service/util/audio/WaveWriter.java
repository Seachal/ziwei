package com.ksy.recordlib.service.util.audio;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class WaveWriter {
    private static final int OUTPUT_STREAM_BUFFER = 16384;
    private File mOutFile;
    private BufferedOutputStream mOutStream;
    private int mSampleRate;
    private int mChannels;
    private int mSampleBits;
    private int mBytesWritten;

    public WaveWriter(String var1, String var2, int var3, int var4, int var5) {
        this.mOutFile = new File(var1 + File.separator + var2);
        this.mSampleRate = var3;
        this.mChannels = var4;
        this.mSampleBits = var5;
        this.mBytesWritten = 0;
    }

    public WaveWriter(File var1, int var2, int var3, int var4) {
        this.mOutFile = var1;
        this.mSampleRate = var2;
        this.mChannels = var3;
        this.mSampleBits = var4;
        this.mBytesWritten = 0;
    }

    public boolean createWaveFile() throws IOException {
        if (this.mOutFile.exists()) {
            this.mOutFile.delete();
        }

        if (this.mOutFile.createNewFile()) {
            FileOutputStream var1 = new FileOutputStream(this.mOutFile);
            this.mOutStream = new BufferedOutputStream(var1, 16384);
            this.mOutStream.write(new byte[44]);
            return true;
        } else {
            return false;
        }
    }

    public void write(short[] var1, int var2, int var3) throws IOException {
        if (this.mChannels == 1) {
            if (var2 > var3) {
                throw new IndexOutOfBoundsException(String.format("offset %d is greater than length %d", new Object[]{Integer.valueOf(var2), Integer.valueOf(var3)}));
            } else {
                for (int var4 = var2; var4 < var3; ++var4) {
                    writeUnsignedShortLE(this.mOutStream, var1[var4]);
                    this.mBytesWritten += 2;
                }

            }
        }
    }

    public void write(short[] var1, short[] var2, int var3, int var4) throws IOException {
        if (this.mChannels == 2) {
            if (var3 > var4) {
                throw new IndexOutOfBoundsException(String.format("offset %d is greater than length %d", new Object[]{Integer.valueOf(var3), Integer.valueOf(var4)}));
            } else {
                for (int var5 = var3; var5 < var4; ++var5) {
                    writeUnsignedShortLE(this.mOutStream, var1[var5]);
                    writeUnsignedShortLE(this.mOutStream, var2[var5]);
                    this.mBytesWritten += 4;
                }

            }
        }
    }

    public void closeWaveFile() throws IOException {
        if (this.mOutStream != null) {
            this.mOutStream.flush();
            this.mOutStream.close();
        }

        this.writeWaveHeader();
    }

    private void writeWaveHeader() throws IOException {
        RandomAccessFile var1 = new RandomAccessFile(this.mOutFile, "rw");
        var1.seek(0L);
        int var2 = (this.mSampleBits + 7) / 8;
        var1.writeBytes("RIFF");
        var1.writeInt(Integer.reverseBytes(this.mBytesWritten + 36));
        var1.writeBytes("WAVE");
        var1.writeBytes("fmt ");
        var1.writeInt(Integer.reverseBytes(16));
        var1.writeShort(Short.reverseBytes((short) 1));
        var1.writeShort(Short.reverseBytes((short) this.mChannels));
        var1.writeInt(Integer.reverseBytes(this.mSampleRate));
        var1.writeInt(Integer.reverseBytes(this.mSampleRate * this.mChannels * var2));
        var1.writeShort(Short.reverseBytes((short) (this.mChannels * var2)));
        var1.writeShort(Short.reverseBytes((short) this.mSampleBits));
        var1.writeBytes("data");
        var1.writeInt(Integer.reverseBytes(this.mBytesWritten));
        var1.close();
        var1 = null;
    }

    private static void writeUnsignedShortLE(BufferedOutputStream var0, short var1) throws IOException {
        var0.write(var1);
        var0.write(var1 >> 8);
    }
}
