package com.laka.live.video.ui.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.msg.Msg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.util.Common;
import com.laka.live.util.ToastHelper;
import com.laka.live.video.constant.VideoApiConstant;
import com.laka.live.video.model.http.VideoDataProvider;
import com.orhanobut.logger.Logger;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

/**
 * @Author:Rayman
 * @Date:2018/8/1
 * @Description:二维码扫描页面
 */

public class ScanQrCodeActivity extends BaseActivity {

    @BindView(R.id.iv_scan_close)
    ImageView mIvClose;
    @BindView(R.id.tv_scan_web_hint)
    TextView mTvWeb;
    @BindView(R.id.tv_scan_web_hint_line)
    TextView mTvWebLine;
    @BindView(R.id.tv_scan_copy)
    TextView mTvCopy;
    @BindView(R.id.qr_view)
    ZBarView mQrView;

    /**
     * description:声音震动控制
     **/
    private static final float BEEP_VOLUME = 0.10f;
    private boolean playBeep = true;
    private MediaPlayer mediaPlayer;
    private boolean vibrate = true;
    private static final long VIBRATE_DURATION = 200L;

    /**
     * description:其他配置
     **/
    private ClipboardManager clipboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setTheme(R.style.ActivityTheme);
        setContentView(R.layout.activity_scan_qr_code);
        ButterKnife.bind(this);

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        mTvWeb.setText(Common.SERVER_URL + "mini");
        mTvWebLine.setText(VideoApiConstant.SCAN_WEB_LINK.substring(4));
        initQrView();
        initBeepSound();
    }

    private void initData() {

    }

    private void initEvent() {

    }

    @OnClick({
            R.id.iv_scan_close,
            R.id.tv_scan_copy
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_scan_close:
                this.finish();
                break;
            case R.id.tv_scan_copy:
                clipboardManager.setText(mTvWeb.getText().toString() + mTvWebLine.getText().toString());
                ToastHelper.showToast("复制成功~");
                break;
            default:
                break;
        }
    }

    private void initQrView() {
        mQrView.setupScanner();
        mQrView.setDelegate(new QRCodeView.Delegate() {
            @Override
            public void onScanQRCodeSuccess(String result) {
                if (result.startsWith("ziwei=")) {
                    playBeepSoundAndVibrate();
                    result = result.substring(6);
                    VideoDataProvider.loginWebSiteUpLoadVideo(ScanQrCodeActivity.this, result, new GsonHttpConnection.OnResultListener<Msg>() {
                        @Override
                        public void onSuccess(Msg msg) {
                            ToastHelper.showToast("登陆成功~");
                            ScanQrCodeActivity.this.finish();
                        }

                        @Override
                        public void onFail(int errorCode, String errorMsg, String command) {
                            ToastHelper.showToast(errorMsg);
                            ScanQrCodeActivity.this.finish();
                        }
                    });
                }
            }

            @Override
            public void onScanQRCodeOpenCameraError() {
                ToastHelper.showToast("打开相机错误");
            }
        });
        mQrView.showScanRect();
        mQrView.showScanRect();
        mQrView.startSpot();
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);
            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.bee);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //重新加载
        if (mQrView != null) {
            initQrView();
        }
    }

    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };
}
