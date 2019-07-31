package com.ksy.recordlib.service.util.audio;

import java.util.concurrent.ArrayBlockingQueue;

public class MixerSync {
    public static final String TAG = "MixerSync";
    private final boolean VERBOSE = false;
    private final int ESTIMATED_DELAY_MS = 100;
    private final int TOLERANCY_MS = 40;
    private int sampleRate = '걄';
    private int chnNum = 1;
    private int sampleBytes = 2;
    private ArrayBlockingQueue queue = new ArrayBlockingQueue(40);
    private boolean eos = false;

    public MixerSync() {
    }

    public MixerSync(int var1, int var2, int var3) {
        this.sampleRate = var1;
        this.chnNum = var2;
        this.sampleBytes = var3;
    }

    public void put(short[] var1, long var2) {
        if(var1 != null) {
            if(var1.length == 0) {
                this.eos = true;
            }

            MixerSync.AFrame var4 = new MixerSync.AFrame(var1, var2);

            while(!this.queue.offer(var4)) {
                this.queue.poll();
            }

        }
    }

    public short[] get(int size, long position) {
        boolean var4 = /*false*/true;
        MixerSync.AFrame var5 = (MixerSync.AFrame)this.queue.peek();

        /*while(var5 != null && !var4) {
            long var6 = var5.pts + this.samplesToTime(size);
            long var8 = position - 100L - var6;
            if(var5.data.length != 0 && !this.eos) {
                if(Math.abs(var8) <= 40L) {
                    var4 = true;
                } else if(var8 > 40L) {
                    this.queue.poll();
                    var5 = (MixerSync.AFrame)this.queue.peek();
                } else {
                    if(var8 >= -2000L) {
                        return null;
                    }

                    this.queue.poll();
                    var5 = (MixerSync.AFrame)this.queue.peek();
                }
            } else {
                var4 = true;
                if(var5.data.length == 0) {
                    this.eos = false;
                    this.queue.poll();
                    var5 = (MixerSync.AFrame)this.queue.peek();
                }
            }
        }*/

        if(!var4) {
            return null;
        } else {
            int var10 = 0;
            int var7 = size;
            short[] var11 = new short[size];

            while(var7 > 0 && var5 != null) {
                int var9 = Math.min(var7, var5.length);
                System.arraycopy(var5.data, var5.offset, var11, var10, var9);
                var10 += var9;
                var7 -= var9;
                var5.offset += var9;
                var5.length -= var9;
                var5.pts += this.samplesToTime(var9);
                if(var5.length == 0) {
                    this.queue.poll();
                    if(var7 > 0) {
                        var5 = (MixerSync.AFrame)this.queue.peek();
                        if(var5 != null && var5.data.length == 0) {
                            this.eos = false;
                            this.queue.poll();
                            var5 = (MixerSync.AFrame)this.queue.peek();
                        }
                    }
                }
            }

            return var11;
        }
    }

    public void flush() {
        this.queue.clear();
        this.eos = false;
    }

    private long samplesToTime(int var1) {
        return (long)var1 * 1000L / (long)this.sampleRate;
    }

    private class AFrame {
        public short[] data;
        public int offset;
        public int length;
        public long pts;

        public AFrame(short[] var2, long var3) {
            this.data = var2;
            this.offset = 0;
            this.length = var2.length;
            this.pts = var3;
        }
    }
}
