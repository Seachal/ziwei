package com.ksy.recordlib.service.util.audio;

import android.media.AudioTrack;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;

import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.util.Log;
import com.tencent.karaoke.common.media.audiofx.Reverb;

import org.greenrobot.eventbus.EventBus;

import java.io.InputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class KSYBgmPlayer implements Runnable {
    public static final String TAG = "KSYBgmPlayer";
    public static final int BGM_ERROR_NONE = 0;
    public static final int BGM_ERROR_UNKNOWN = 1;
    public static final int BGM_ERROR_NOT_SUPPORTED = 2;
    public static final int BGM_ERROR_IO = 3;
    public static final int BGM_ERROR_MALFORMED = 4;
    private static KSYBgmPlayer instance;
    private final boolean VERBOSE = false;
    private static final Object instanceLock = new Object();
    private KSYBgmPlayer.OnBgmPlayerListener mListener;
    private KSYBgmPlayer.OnBgmPcmListener mPcmListener;
    private HandlerThread mMusicHandlerThread;
    private Handler mMusicHandler;
    private AudioTrack mAudioTrack;
    private float mAudioTrackVolume = 1f;
    private float volume = 0.6F;
    private int sampleRate = '걄';
    private int channel = 4;
    private int sampleBits = 2;
    private boolean mute = false;
    private boolean loop = false;
    private boolean paused = false;
    private boolean stopped = false;
    private long duration = 0L;
    private ArrayBlockingQueue queue;

    private Reverb mReverb;

    private KSYBgmPlayer() {
        this.paused = false;
        this.stopped = false;
        this.queue = new ArrayBlockingQueue(100);
        this.mMusicHandlerThread = new HandlerThread("streamer_music_thread");
        this.mMusicHandlerThread.start();
        this.mMusicHandler = new Handler(this.mMusicHandlerThread.getLooper(), new Callback() {
            public boolean handleMessage(Message var1) {
                return false;
            }
        });
        this._init();
        initReverb();
    }

    public static KSYBgmPlayer getInstance() {
        Object var0 = instanceLock;
        synchronized (instanceLock) {
            if (instance == null) {
                instance = new KSYBgmPlayer();
            }

            return instance;
        }
    }

    public void setAudioTrackVolume(float volume) {
        if (mAudioTrack != null) {
            mAudioTrack.setStereoVolume(volume, volume);
        }
        mAudioTrackVolume = volume;
    }

    public void setVolume(float var1) {
        this.volume = var1;
    }

    public void setMute(boolean var1) {
        this.mute = var1;
    }

    public long getPosition() {
        return this.mAudioTrack != null ? (long) this.mAudioTrack.getPlaybackHeadPosition() * 1000L / (long) this.sampleRate : 0L;
    }

    public long getDuration() {
        return this.duration;
    }


    public synchronized boolean start(String var1, boolean var2) {
        int var3 = this._start(var1, var2);
        if (var3 != 0) {
            return false;
        } else {
            this.loop = var2;
            this.stopped = false;
            this.paused = false;
            this.duration = 0L;
            if (this.mMusicHandler != null) {
                this.mMusicHandler.post(new Runnable() {
                    public void run() {
                        if (KSYBgmPlayer.this.mAudioTrack == null) {
//                        if(true){
                            int var1 = AudioTrack.getMinBufferSize(KSYBgmPlayer.this.sampleRate, KSYBgmPlayer.this.channel, KSYBgmPlayer.this.sampleBits);
                            if (var1 == AudioTrack.ERROR_BAD_VALUE) {
                                var1 = channel * KSYBgmPlayer.this.sampleBits * 1024;
                            }
                            KSYBgmPlayer.this.mAudioTrack = new AudioTrack(3, KSYBgmPlayer.this.sampleRate, KSYBgmPlayer.this.channel, KSYBgmPlayer.this.sampleBits, var1 * 4, 1);

                            mAudioTrack.setStereoVolume(mAudioTrackVolume, mAudioTrackVolume);
//                            int period =  KSYBgmPlayer.this.sampleBits/50;
//                            Log.d(TAG,"回调间隔="+period);
                            KSYBgmPlayer.this.mAudioTrack.setPositionNotificationPeriod(2000);  //设置回调周期
                            KSYBgmPlayer.this.mAudioTrack.play();
                            Log.d(TAG, " mAudioTrack play getMinBufferSize="+var1);
                            KSYBgmPlayer.this.mAudioTrack.setPlaybackPositionUpdateListener(new AudioTrack.OnPlaybackPositionUpdateListener() {
                                @Override
                                public void onMarkerReached(AudioTrack track) {
//                                    Log.d(TAG, " onMarkerReached position=" + track.getPlaybackHeadPosition());
                                }

                                @Override
                                public void onPeriodicNotification(AudioTrack track) {
//                                    Log.d(TAG, " onPeriodicNotification position=" + getPosition());
                                    EventBusManager.postEvent(getPosition() - 200L, SubcriberTag.AUDIO_TRACK_UPDATE_BGM_POSITION);
                                }
                            });
                            EventBusManager.postEvent(0, SubcriberTag.AUDIO_TRACK_PLAY_BGM);
                        }

                    }
                });
            }

            return true;
        }
    }

    public void pause() {
        this.paused = true;
    }

    public void resume() {
        this.paused = false;
        if( this.mMusicHandler!=null)
        this.mMusicHandler.post(this);
    }

    public synchronized void stop() {
        this.stopped = true;
        if (this.mMusicHandler != null) {
            this.mMusicHandler.removeCallbacksAndMessages((Object) null);
            this.mMusicHandler.post(new Runnable() {
                public void run() {
                    if (KSYBgmPlayer.this.mAudioTrack != null) {
                        KSYBgmPlayer.this.mAudioTrack.pause();
                        KSYBgmPlayer.this.mAudioTrack.flush();
                        KSYBgmPlayer.this.mAudioTrack.stop();
                        KSYBgmPlayer.this.mAudioTrack.release();
                        KSYBgmPlayer.this.mAudioTrack = null;
                    }

                }
            });
        }

        this._stop();
        this.queue.clear();
        this.paused = false;

        Log.d(TAG, " mAudioTrack stop");
//        currentPlayTime = 0;
        EventBusManager.postEvent(0, SubcriberTag.AUDIO_TRACK_STOP_BGM);
    }

    public void release() {
        Object var1 = instanceLock;
        synchronized (instanceLock) {
            this.stop();
            this._release();
            if (this.mMusicHandlerThread != null) {
                this.mMusicHandler.removeCallbacksAndMessages((Object) null);
                this.mMusicHandlerThread.quit();
                this.mMusicHandlerThread = null;
                this.mMusicHandler = null;
            }

            instance = null;
        }
        unInitReverb();
    }

    public void run() {
        if (!this.paused) {
            KSYBgmPlayer.BgmAudioFrame var1 = (KSYBgmPlayer.BgmAudioFrame) this.queue.poll();
            if (var1 == null) {
                Log.d(TAG, "Empty input queue");
            } else {
                int var2 = var1.data.length;
                if (var2 == 0) {
                    if (this.mListener != null) {
                        this.mPcmListener.onPcmData(var1.data, var1.pts);
                    }

                    if (this.loop && this.mAudioTrack != null) {
                        this.mAudioTrack.stop();
                        this.mAudioTrack.play();
                    }

                    if (!this.loop && this.mListener != null) {
                        Log.d(TAG,"onCompleted");
                        this.mListener.onCompleted();
                    }

                } else {

                    AudioUtils.setVolume(var1.data, volume);

                    if (this.mAudioTrack != null) {
                        short[] var5 = var1.data;
                        if (this.mute) {
                            var5 = new short[var2];

                            for (int var4 = 0; var4 < var2; ++var4) {
                                var5[var4] = 0;
                            }
                        }

                        this.mAudioTrack.write(var5, 0, var2);
                    }

                    if (!this.stopped) {
                        if (this.mListener != null) {
                            this.mPcmListener.onPcmData(var1.data, var1.pts);
                        }

                    }
                }
            }
        }
    }

    public void onDecoded(short[] var1, long var2, long var4) {
        KSYBgmPlayer.BgmAudioFrame var6 = new KSYBgmPlayer.BgmAudioFrame();
        var6.data = var1;
        var6.count = var1.length;
        var6.pts = var2 / 1000L;
        this.duration = var4 / 1000L;
        boolean var7 = true;

        do {
            try {
                var7 = this.queue.offer(var6, 20L, TimeUnit.MILLISECONDS);
            } catch (InterruptedException var9) {
                Log.d("KSYBgmPlayer", "put data interrupted");
            }
        } while (!var7 && !this.stopped);

        if (var7 && !this.stopped) {
            this.mMusicHandler.post(this);
        }

    }

    public void setOnBgmPlayerListener(KSYBgmPlayer.OnBgmPlayerListener var1) {
        this.mListener = var1;
    }

    public void setOnBgmPcmListener(KSYBgmPlayer.OnBgmPcmListener var1) {
        this.mPcmListener = var1;
    }

    private void initReverb() {
        mReverb = new Reverb();
        mReverb.init(44100, 2);
    }

    private void unInitReverb() {
        if (mReverb != null) {
            mReverb.release();
        }
    }

    public void onError(int var1) {
        if (this.mListener != null) {
            this.mListener.onError(var1);
        }

    }

    public int getSampleRate() {
        return sampleRate;
    }

    public int getChannel(){
        return channel ;
    }

    private native void _init();

    private native void _release();

    private native int _start(String var1, boolean var2);

    private native void _stop();

    static {
        try {

            System.loadLibrary("audio_effect");
            System.loadLibrary("Denoise_export");
            System.loadLibrary("ksyyuv");
            System.loadLibrary("ksystreamer");
        } catch (UnsatisfiedLinkError var1) {
            Log.e("VideoFrame", "load library failed", var1);
        }
    }

    public class BgmAudioFrame {
        public short[] data;
        public int count;
        public long pts;

        public BgmAudioFrame() {
        }
    }

    public interface OnBgmPcmListener {
        void onPcmData(short[] var1, long var2);
    }

    public interface OnBgmPlayerListener {
        void onCompleted();

        void onError(int var1);
    }
}
