package com.ksy.recordlib.service.util.audio;

import android.media.AudioTrack;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

public class PcmPlayer {
    public static final String TAG = "KSYBgmPlayer";
    private final boolean VERBOSE = false;
    private HandlerThread handlerThread;
    private Handler handler;
    private AudioTrack audioTrack;
    private int sampleRate = '걄';
    private int channel = 4;
    private int sampleFormat = 2;

    public PcmPlayer() {
        this.init();
    }

    public PcmPlayer(int var1, int var2, int var3) {
        this.sampleRate = var1;
        this.channel = var2;
        this.sampleFormat = var3;
        this.init();
    }

    private void init() {
        this.handlerThread = new HandlerThread("pcm_player_thread");
        this.handlerThread.start();
        this.handler = new Handler(this.handlerThread.getLooper(), new Handler.Callback() {
            public boolean handleMessage(Message var1) {
                return false;
            }
        });
    }

    public void play(final short[] var1) {
        this.handler.post(new Runnable() {
            public void run() {
                if(PcmPlayer.this.audioTrack == null) {
                    int var1x = AudioTrack.getMinBufferSize(PcmPlayer.this.sampleRate, PcmPlayer.this.channel, PcmPlayer.this.sampleFormat);
                    if (var1x == AudioTrack.ERROR_BAD_VALUE) {
                        var1x = channel * PcmPlayer.this.sampleFormat * 1024;
                    }
                    PcmPlayer.this.audioTrack = new AudioTrack(3, PcmPlayer.this.sampleRate, PcmPlayer.this.channel, PcmPlayer.this.sampleFormat, var1x, 1);
                    PcmPlayer.this.audioTrack.play();
                }

                PcmPlayer.this.audioTrack.write(var1, 0, var1.length);
            }
        });
    }

    public void stop() {
        this.handler.removeCallbacksAndMessages((Object)null);
        this.handler.post(new Runnable() {
            public void run() {
                if(PcmPlayer.this.audioTrack != null) {
                    PcmPlayer.this.audioTrack.pause();
                    PcmPlayer.this.audioTrack.flush();
                    PcmPlayer.this.audioTrack.stop();
                    PcmPlayer.this.audioTrack.release();
                    PcmPlayer.this.audioTrack = null;
                }

            }
        });
    }

    public void release() {
        if(this.handlerThread != null) {
            this.stop();
            this.handler.removeCallbacksAndMessages((Object)null);
            this.handlerThread.quit();
            this.handlerThread = null;
            this.handler = null;
        }

    }
}
