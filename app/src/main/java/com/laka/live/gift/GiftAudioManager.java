package com.laka.live.gift;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Environment;

import com.coolerfall.download.DownloadCallback;
import com.coolerfall.download.DownloadRequest;
import com.coolerfall.download.Priority;
import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.application.LiveApplication;
import com.laka.live.bean.GiftInfo;
import com.laka.live.download.DownloadManager;
import com.laka.live.msg.QueryGiftsMsg;
import com.laka.live.network.DataProviderRoom;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.thread.BackgroundThread;
import com.laka.live.util.Log;
import com.laka.live.util.Utils;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by guan on 16/9/19.
 */
public class GiftAudioManager {
    private static final String TAG = "GiftAudioManager";
    private static GiftAudioManager self;
    SoundPool sp;
    HashMap<Integer, Integer> spMap = new HashMap<Integer, Integer>();

    public static GiftAudioManager getInstance() {
        if (self == null) {
            synchronized (GiftAudioManager.class) {
                if (self == null) {
                    self = new GiftAudioManager();
                }
            }
        }
        return self;
    }

    public int curStreamId;

    public GiftAudioManager() {
        sp = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
    }

    public void stopSound(int streamId) {
        try {
            if (streamId != 0) {
                sp.stop(streamId);
            }
        } catch (Exception e) {

        }
    }

    public int playSound(int rawResource) {
        if (!spMap.containsKey(rawResource)) {
            spMap.put(rawResource, sp.load(LiveApplication.getInstance(), rawResource, 1));
        }
        //实例化AudioManager对象，控制声音
        AudioManager am = (AudioManager) LiveApplication.getInstance().getSystemService(LiveApplication.getInstance().AUDIO_SERVICE);
        //最大音量
        float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //当前音量
        float audioCurrentVolumn = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        float volumnRatio = audioCurrentVolumn / audioMaxVolumn;
        //播放
        curStreamId = sp.play(spMap.get(rawResource),     //声音资源
                volumnRatio,         //左声道
                volumnRatio,         //右声道
                1,             //优先级，0最低
                0,         //循环次数，0是不循环，-1是永远循环
                1);            //回放速度，0.5-2.0之间。1为正常速度
        return curStreamId;
    }

    private MediaPlayer mp;

    public void playBgm(String sound, Context context) {
        if (mp != null) {
            stopBgm();
        }
        try {
            mp = new MediaPlayer();
           if(sound.startsWith("sound/")) {
               AssetManager assetManager = context.getAssets();
               AssetFileDescriptor afd = assetManager.openFd(sound);
               mp.setDataSource(afd.getFileDescriptor(),
                       afd.getStartOffset(), afd.getLength());
               mp.prepare();
           }else{
               mp.setDataSource(sound);
               mp.prepare();
           }
            mp.start();//开始播放
        } catch (Exception e) {
            e.printStackTrace();//输出异常信息
        }
    }

    /**
     * 停止播放背景音乐
     */
    public void stopBgm() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }
}
