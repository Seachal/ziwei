package com.laka.live.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.laka.live.R;
import com.laka.live.ui.activity.MainActivity;


/**
 * @Author:Rayman
 * @Date:2018/9/3
 * @Description:播放音乐保活（=...=）
 */

public class KeepAliveService extends Service {
    private static final int KEEP_ALIVE_STATUS = 1;
    private Notification notification;
    private MediaPlayer mPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.silent_1s);
        mPlayer.setLooping(true);
        new MyMusicThread().start();
        Notification.Builder builder = new Notification.Builder(KeepAliveService.this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(getApplication().getResources().getString(R.string.app_name));
        builder.setContentText("正在运行中");
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);
        Intent intent = new Intent(KeepAliveService.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(KeepAliveService.this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        notification = builder.build();
        startForeground(KEEP_ALIVE_STATUS, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);

        if(null != mPlayer){
            mPlayer.stop();
            mPlayer = null;
        }
        super.onDestroy();
    }

    class MyMusicThread extends Thread {
        @Override
        public void run() {
            //循环播放音频文件，防止service被回收
            while (true) {
                if (null != mPlayer) {
                    mPlayer.start();
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
}
