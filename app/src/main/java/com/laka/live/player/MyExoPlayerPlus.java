package com.laka.live.player;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.laka.live.R;
import com.laka.live.util.NetworkUtil;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Timer;
import java.util.TimerTask;

import cn.jzvd.JZDataSource;
import cn.jzvd.JZMediaManager;
import cn.jzvd.JZUserAction;
import cn.jzvd.JZUserActionStd;
import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdMgr;
import cn.jzvd.JzvdStd;

/**
 * Created by 61453 on 2018/10/30.
 * 替换旧版本 MyExoPlayer 的视频播放控件，使用 Jzvd
 */

public class MyExoPlayerPlus extends Jzvd {

    // 目前该参数作用待定
    public boolean isReply = false;

    /**
     * description:用于记录视频是否卡顿
     **/
    private int lastProgress;
    private int freezeTime = 0;
    private static final int MAX_VIDEO_FREEZE_DURATION = 1000;
    private boolean isErrorCountDownStart;
    private CountDownTimer mErrorTimer;

    public static boolean isShowBottomProgress = true;

    private TextView mTitleTextView;
    private ImageView mBackButton;
    public ImageView mThumbImageView;
    private ProgressBar mLoadingProgressBar;
    private ImageView mTinyBackImageView;
    private ProgressBar mBottomProgressBar;


    private DismissControlViewTimerTask mDismissControlViewTimerTask;
    protected static Timer DISMISS_CONTROL_VIEW_TIMER;

    public MyExoPlayerPlus(Context context) {
        super(context);
    }

    public MyExoPlayerPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
//        设置默认视频宽高比
//        heightRatio = 16;
//        widthRatio = 9;

        mErrorTimer = new CountDownTimer(MAX_VIDEO_FREEZE_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Logger.e("ExoPlayer----倒计时tick");
                isErrorCountDownStart = true;
            }

            @Override
            public void onFinish() {
                Logger.e("ExoPlayer----倒计时完成");
                isErrorCountDownStart = false;
                freezeTime = 0;
                lastProgress = 0;
                if (mPlayListener != null && mBottomProgressBar != null) {
                    int time = (int) (mBottomProgressBar.getProgress() * getDuration() / 100);
                    mPlayListener.onPlayerError(time);
                    //播放视频卡顿记录到log里面
                    String errorMsg = "Current Video:" + getCurrentUrl().toString() + "\nCurrentPosition：" + time
                            + "\nNetWorkStatus：" + NetworkUtil.getNetworkState(getContext())
                            + "\nisNetWorkOk：" + NetworkUtil.isNetworkOk(getContext())
                            + "\nis Freeze about 10 sec...throw an exception";
                    Exception exception = new Exception(errorMsg);
                    try {
                        com.laka.live.util.Log.dumpVideoExceptionToSdCard(exception);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Logger.e("ExoPlayer----倒计时finish，输出当前进度：" + time);
                }
            }
        };
    }

    /**
     * 播放视频
     */
    public static void playerVideo(Context activity, String url, String title) {
        if (!url.startsWith("http://video.zwlive.lakatv.com/"))
            url = "http://video.zwlive.lakatv.com/" + url;
        isShowBottomProgress = !url.endsWith(".m3u8");
        startFullscreen(activity, MyExoPlayerPlus.class, url, title);
        com.laka.live.util.Log.log(" 全屏播放地址=" + url + " isShowBottomProgress=" + isShowBottomProgress);
    }


    @Override
    public int getLayoutId() {
        return R.layout.jz_video_player;
    }


    @Override
    public void init(Context context) {
        super.init(context);
        mBottomProgressBar = findViewById(R.id.bottom_progressbar);
        if (!isShowBottomProgress) {
            // 如果是m3u8格式视频，则隐藏mBottomProgressBar
            mBottomProgressBar.setVisibility(INVISIBLE);
        } else {
            mBottomProgressBar.setVisibility(VISIBLE);
        }
        mTitleTextView = findViewById(R.id.title);
        mBackButton = findViewById(R.id.back);
        mThumbImageView = findViewById(R.id.thumb);
        mLoadingProgressBar = findViewById(R.id.loading);
        mTinyBackImageView = findViewById(R.id.back_tiny);

        mThumbImageView.setOnClickListener(this);
        mBackButton.setOnClickListener(this);
        mTinyBackImageView.setOnClickListener(this);
        startButton.setOnClickListener(this);
    }

    @Override
    public void setUp(JZDataSource jzDataSource, int screen) {
        super.setUp(jzDataSource, screen);
        setUpExtra(jzDataSource.title);
    }

    private void setUpExtra(String title) {
        //TODO 更改ui
        setUiWitStateAndScreen(CURRENT_STATE_NORMAL);
        //TODO 注释 if (title.length() == 0) return;
        mTitleTextView.setText(title);
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            fullscreenButton.setImageResource(R.drawable.jz_shrink);
            mBackButton.setVisibility(View.VISIBLE);
            mTinyBackImageView.setVisibility(View.INVISIBLE);
        } else if (currentScreen == SCREEN_WINDOW_NORMAL || currentScreen == SCREEN_WINDOW_LIST) {
            fullscreenButton.setImageResource(R.drawable.jz_enlarge);
            mBackButton.setVisibility(View.GONE);
            mTinyBackImageView.setVisibility(View.INVISIBLE);
        } else if (currentScreen == SCREEN_WINDOW_TINY) {
            mTinyBackImageView.setVisibility(View.VISIBLE);
            if (isEnableVideoThumb) {
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
            } else {
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
            }
        }

        if (mErrorTimer != null) {
            lastProgress = 0;
            freezeTime = 0;
            mErrorTimer.cancel();
            isErrorCountDownStart = false;
        }
    }

    /**
     * description:网路状态设置
     **/
    private boolean isFirstMobileDialogShow = false;
    private boolean isEnableVideoThumb = false;

    /**
     * 显示无网的状态
     */
    public void showNoNetWork() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("当前网络不可用 请检查网络设置");
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                stopPlay();
                if (mStateChangeListener != null) {
                    mStateChangeListener.onNoNetDialogClick();
                }
            }
        });
        builder.create().show();
    }

    /**
     * 开始跟踪触摸
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        super.onStartTrackingTouch(seekBar);
        cancelDismissControlViewTimer();
    }

    /**
     * 停止跟踪触摸
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        super.onStopTrackingTouch(seekBar);
        startDismissControlViewTimer();
    }

    @Override
    public void onPrepared() {
        super.onPrepared();
        setUiWitStateAndScreen(CURRENT_STATE_PLAYING);
        setAllControlsVisible(View.VISIBLE, View.INVISIBLE, View.INVISIBLE,
                View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.VISIBLE);
        startDismissControlViewTimer();
    }

    /**
     * 播放视频
     */
    public void startPlayLogic() {
        if (!getCurrentUrl().toString().startsWith("file") && !NetworkUtil.isNetworkOk(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
            Logger.e(TAG, "MyExoPlayer 显示网络错误---Dialog");
            showNoNetWork();
            isFirstMobileDialogShow = true;
            return;
        } else if (!getCurrentUrl().toString().startsWith("file") && !JZUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
            Logger.e(TAG, "MyExoPlayer 显示移动网络---Dialog");
            showWifiDialog();
            isFirstMobileDialogShow = true;
            return;
        }
        onEvent(JZUserActionStd.ON_CLICK_START_THUMB);
        startVideo();
    }

    // 停止播放
    private void stopPlay() {
        releaseAllVideos();
    }

    /**
     * 设置视频播放的宽高比
     */
    public void setFullScreenRatio() {
        setVideoRatio(mScreenHeight, mScreenWidth);
    }

    /**
     * 设置视频播放的宽高比
     */
    public void setVideoRatio(int heightRatio, int widthRatio) {
        // 父类的属性
        this.heightRatio = heightRatio;
        this.widthRatio = widthRatio;
        requestLayout();
    }

    /**
     * description:比例控制，暂时没有使用到
     **/
    private boolean isUseDefaultRatio = true;

    public void enableDefaultRatio(boolean isEnable) {
        this.isUseDefaultRatio = isEnable;
        if (!isEnable) {
            heightRatio = 0;
            widthRatio = 0;
        }
    }

    /**
     * 模拟点击
     */
    public void onPlayClick() {
        startButton.performClick();
    }

    /**
     * 由于ExoPlayer是封装之后的，默认都把thumb给隐藏掉了，这里多加个flag开启
     */
    public void enableImageThumb(boolean isEnable) {
        isEnableVideoThumb = isEnable;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        if (id == R.id.surface_container) { // 播放视频的容器
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    startDismissControlViewTimer();
                    if (mChangePosition) {
                        try {
                            int duration = (int) getDuration();
                            int progress = (int) (mSeekTimePosition * 100 / (duration == 0 ? 1 : duration));
                            mBottomProgressBar.setProgress(progress);
                        } catch (Exception e) {

                        }
                    }
                    if (!mChangePosition && !mChangeVolume) {
                        onEvent(JZUserActionStd.ON_CLICK_BLANK);
                        onClickUiToggle();
                    }
                    break;
                default:
                    break;
            }
        } else if (id == R.id.bottom_seek_progress) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    cancelDismissControlViewTimer();
                    break;
                case MotionEvent.ACTION_UP:
                    startDismissControlViewTimer();
                    break;
                default:
                    break;
            }

        }
        return super.onTouch(v, event);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.thumb) {
            if (TextUtils.isEmpty(getCurrentUrl().toString())) {
                Toast.makeText(getContext(), getResources().getString(R.string.no_url), Toast.LENGTH_SHORT).show();
                return;
            }
            if (currentState == CURRENT_STATE_NORMAL) {
                if (!getCurrentUrl().toString().startsWith("file") && !NetworkUtil.isNetworkOk(getContext()) && !WIFI_TIP_DIALOG_SHOWED
                        && !isFirstMobileDialogShow) {
                    showNoNetWork();
                    return;
                } else if (!getCurrentUrl().toString().startsWith("file") && !JZUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED
                        && !isFirstMobileDialogShow) {
                    Logger.e(TAG, "点击缩略图播放");
                    showWifiDialog();
                    return;
                }
                onEvent(JZUserActionStd.ON_CLICK_START_THUMB);
                startVideo();
            } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
                onClickUiToggle();
            }
        } else if (i == R.id.surface_container) {
            startDismissControlViewTimer();
        } else if (i == R.id.back) {
            Log.d(TAG, " 返回键");
            backPress();
        } else if (i == R.id.back_tiny) {
            backPress();
        } else if (i == R.id.start) {
            Log.i(TAG, "onClick start [" + this.hashCode() + "] ");
            if (jzDataSource.urlsMap.isEmpty() || jzDataSource.getCurrentUrl() == null) {
                Toast.makeText(getContext(), getResources().getString(cn.jzvd.R.string.no_url), Toast.LENGTH_SHORT).show();
                return;
            }
            if (currentState == CURRENT_STATE_NORMAL) {
                if (!getCurrentUrl().toString().startsWith("file") && !NetworkUtil.isNetworkOk(getContext()) && !WIFI_TIP_DIALOG_SHOWED
                        && !isFirstMobileDialogShow) {
                    Logger.e(TAG, "MyExoPlayer 显示网络错误---Dialog");
                    showNoNetWork();
                    return;
                } else if (!getCurrentUrl().toString().startsWith("file") && !JZUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED
                        && !isFirstMobileDialogShow) {
                    Logger.e(TAG, "MyExoPlayer 显示移动网络---Dialog");
                    showWifiDialog();
                    return;
                }
                if (!jzDataSource.getCurrentUrl().toString().startsWith("file") && !
                        jzDataSource.getCurrentUrl().toString().startsWith("/") &&
                        !JZUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
                    showWifiDialog();
                    return;
                }
                startVideo();
                onEvent(JZUserAction.ON_CLICK_START_ICON);//开始的事件应该在播放之后，此处特殊
            } else if (currentState == CURRENT_STATE_PLAYING) {
                onEvent(JZUserAction.ON_CLICK_PAUSE);
                Log.d(TAG, "pauseVideo [" + this.hashCode() + "] ");
                JZMediaManager.pause();
                onStatePause();
                setUiWitStateAndScreen(CURRENT_STATE_PAUSE);
            } else if (currentState == CURRENT_STATE_PAUSE) {
                onEvent(JZUserAction.ON_CLICK_RESUME);
                JZMediaManager.start();
                onStatePlaying();
                setUiWitStateAndScreen(CURRENT_STATE_PLAYING);
            } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
                onEvent(JZUserAction.ON_CLICK_START_AUTO_COMPLETE);
                startVideo();
            } else if (currentState == CURRENT_STATE_ERROR) {
                Log.d(TAG, " currentState == CURRENT_STATE_ERROR");
                backPress();
            }

        } else if (i == R.id.fullscreen) {
            Log.i(TAG, "onClick fullscreen [" + this.hashCode() + "] ");
            if (currentState == CURRENT_STATE_AUTO_COMPLETE) return;
            if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
                //quit fullscreen
                backPress();
            } else {
                Log.d(TAG, "toFullscreenActivity [" + this.hashCode() + "] ");
                onEvent(JZUserAction.ON_ENTER_FULLSCREEN);
                startWindowFullscreen();
            }
        } else if (i == R.id.surface_container && currentState == CURRENT_STATE_ERROR) {
            Log.i(TAG, "onClick surfaceContainer State=Error [" + this.hashCode() + "] ");
            startVideo();
        }
    }

    public void onClickPlayButton() {
        startButton.performClick();
    }

    /**
     * ui开关
     */
    private void onClickUiToggle() {
        if (currentState == CURRENT_STATE_PREPARING) {  //视频准备状态
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPreparingClear();
            } else {
                changeUiToPreparingShow();
            }
        } else if (currentState == CURRENT_STATE_PLAYING) {  //视频播放中
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPlayingClear();
            } else {
                changeUiToPlayingShow();
            }
        } else if (currentState == CURRENT_STATE_PAUSE) {  // 暂停
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPauseClear();
            } else {
                changeUiToPauseShow();
            }
        } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {  // 视频播放完成
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToCompleteClear();
            } else {
                changeUiToCompleteShow();
            }
        } /*else if (currentState == CURRENT_STATE_PLAYING_BUFFERING_START) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPlayingBufferingClear();
            } else {
                changeUiToPlayingBufferingShow();
            }
        }*/
    }


    public void changeUiToPlayingBufferingShow() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisible(View.VISIBLE, View.VISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.VISIBLE, View.VISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
            default:
                break;
        }
    }

    public void changeUiToPlayingBufferingClear() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.INVISIBLE, View.INVISIBLE, View.VISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.INVISIBLE, View.INVISIBLE, View.VISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
            default:
                break;
        }
    }

    public void changeUiToCompleteShow() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisible(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
            default:
                break;
        }
    }

    public void changeUiToCompleteClear() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.VISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.VISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
            default:
                break;
        }
    }

    public void changeUiToPauseShow() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisible(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
            default:
                break;
        }

    }

    public void changeUiToPauseClear() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
            default:
                break;
        }
    }

    public void changeUiToPlayingShow() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisible(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
            default:
                break;
        }
    }

    public void changeUiToPlayingClear() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.VISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.VISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
            default:
                break;
        }
    }

    public void changeUiToPreparingShow() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisible(View.VISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.VISIBLE, View.VISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.VISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.VISIBLE, View.VISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
            default:
                break;
        }
    }

    public void changeUiToPreparingClear() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisible(View.VISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.VISIBLE, View.VISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.VISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.VISIBLE, View.VISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
            default:
                break;
        }
    }


    public void setAllControlsVisible(int topCon, int bottomCon, int startBtn, int loadingPro,
                                      int thumbImg, int coverImg, int bottomPro) {
        if (isReply) {
            topContainer.setVisibility(View.INVISIBLE);
            bottomContainer.setVisibility(View.INVISIBLE);
            startButton.setVisibility(View.INVISIBLE);
            mLoadingProgressBar.setVisibility(View.INVISIBLE);
            mThumbImageView.setVisibility(View.INVISIBLE);
            mBottomProgressBar.setVisibility(View.INVISIBLE);
            return;
        }

        topContainer.setVisibility(topCon);
        bottomContainer.setVisibility(bottomCon);
        startButton.setVisibility(startBtn);
        mLoadingProgressBar.setVisibility(loadingPro);
        mThumbImageView.setVisibility(thumbImg);
        mBottomProgressBar.setVisibility(bottomPro);

        if (!isShowBottomProgress && currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            // SeekBar seekBar = findViewById(R.id.progress);
            // 旧版本是 R.id.progress ，新版本是 R.id.bottom_seek_progress
            // 如果是m3u8格式视频，则隐藏mBottomProgressBar、seekBar、title
            SeekBar seekBar = findViewById(R.id.bottom_seek_progress);
            seekBar.setVisibility(View.INVISIBLE);
            TextView tvTotal = findViewById(R.id.total);
            tvTotal.setVisibility(View.INVISIBLE);
            mBottomProgressBar.setVisibility(View.GONE);
        } else if (!isShowBottomProgress && currentScreen == SCREEN_WINDOW_NORMAL) {
            mBottomProgressBar.setVisibility(View.GONE);
        }

        Logger.e("setAllControlsVisible----topContainer：" + topCon + "\nbottomContainer:" + bottomCon
                + "\nstartButton:" + startBtn + "\nloadingPro：" + loadingPro + "\nthumbImg：" + thumbImg
                + "\ncoverImg：" + coverImg + "\nbottomPro：" + bottomPro);
    }

    /**
     * 更改播放按钮图片
     */
    public void updateStartImage() {
        if (currentState == CURRENT_STATE_PLAYING) {
            startButton.setImageResource(R.drawable.jz_click_pause_selector);
        } else if (currentState == CURRENT_STATE_ERROR) {
            startButton.setImageResource(R.drawable.jz_click_error_selector);
        } else {
            startButton.setImageResource(R.drawable.jz_click_play_selector);
        }
    }

//    ================================== UI更改状态监听 start ==========================================

    /**
     * 退出全屏和小窗的方法
     */
    @Override
    public void playOnThisJzvd() {
        super.playOnThisJzvd();
        setUiWitStateAndScreen(currentState);
    }

    /**
     * 全屏播放视频
     */
    @Override
    public void startWindowFullscreen() {
        Log.i(TAG, "startWindowFullscreen " + " [" + this.hashCode() + "] ");
        hideSupportActionBar(getContext());

        ViewGroup vp = (JZUtils.scanForActivity(getContext()))//.getWindow().getDecorView();
                .findViewById(Window.ID_ANDROID_CONTENT);
        View old = vp.findViewById(cn.jzvd.R.id.jz_fullscreen_id);
        if (old != null) {
            vp.removeView(old);
        }
        textureViewContainer.removeView(JZMediaManager.textureView);
        try {
            // 更改
            Constructor<MyExoPlayerPlus> constructor = (Constructor<MyExoPlayerPlus>) MyExoPlayerPlus.this.getClass().getConstructor(Context.class);
            Jzvd jzvd = constructor.newInstance(getContext());
            jzvd.setId(cn.jzvd.R.id.jz_fullscreen_id);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            vp.addView(jzvd, lp);
            jzvd.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN);
            jzvd.setUp(jzDataSource, JzvdStd.SCREEN_WINDOW_FULLSCREEN);

            //TODO 更改ui
            setUiWitStateAndScreen(currentState);

            jzvd.setState(currentState);
            jzvd.addTextureView();
            JzvdMgr.setSecondFloor(jzvd);
//            final Animation ra = AnimationUtils.loadAnimation(getContext(), R.anim.start_fullscreen);
//            jzVideoPlayer.setAnimation(ra);
            JZUtils.setRequestedOrientation(getContext(), FULLSCREEN_ORIENTATION);

            onStateNormal();
            jzvd.progressBar.setSecondaryProgress(progressBar.getSecondaryProgress());
            jzvd.startProgressTimer();
            CLICK_QUIT_FULLSCREEN_TIME = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompletion() {
        //TODO 普通状态
        setUiWitStateAndScreen(CURRENT_STATE_NORMAL);
        super.onCompletion();
    }

    @Override
    public void startVideo() {
        super.startVideo();
        //TODO 视频准备状态
        setUiWitStateAndScreen(CURRENT_STATE_PREPARING);
    }

    @Override
    public void startWindowTiny() {
        Log.i(TAG, "startWindowTiny " + " [" + this.hashCode() + "] ");
        onEvent(JZUserAction.ON_ENTER_TINYSCREEN);
        if (currentState == CURRENT_STATE_NORMAL || currentState == CURRENT_STATE_ERROR || currentState == CURRENT_STATE_AUTO_COMPLETE)
            return;
        ViewGroup vp = (JZUtils.scanForActivity(getContext()))//.getWindow().getDecorView();
                .findViewById(Window.ID_ANDROID_CONTENT);
        View old = vp.findViewById(cn.jzvd.R.id.jz_tiny_id);
        if (old != null) {
            vp.removeView(old);
        }
        textureViewContainer.removeView(JZMediaManager.textureView);

        try {
            // 更改
            Constructor<MyExoPlayerPlus> constructor = (Constructor<MyExoPlayerPlus>) MyExoPlayerPlus.this.getClass().getConstructor(Context.class);
            Jzvd jzvd = constructor.newInstance(getContext());
            jzvd.setId(cn.jzvd.R.id.jz_tiny_id);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(400, 400);
            lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
            vp.addView(jzvd, lp);
            jzvd.setUp(jzDataSource, JzvdStd.SCREEN_WINDOW_TINY);

            //TODO 更改ui
            setUiWitStateAndScreen(currentState);

            jzvd.setState(currentState);
            jzvd.addTextureView();
            JzvdMgr.setSecondFloor(jzvd);
            onStateNormal();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 自动播放完成
     */
    @Override
    public void onAutoCompletion() {
        Runtime.getRuntime().gc();
        Log.i(TAG, "onAutoCompletion " + " [" + this.hashCode() + "] ");
        onEvent(JZUserAction.ON_AUTO_COMPLETE);
        dismissVolumeDialog();
        dismissProgressDialog();
        dismissBrightnessDialog();
        onStateAutoComplete();
        //TODO 更改UI
        setUiWitStateAndScreen(CURRENT_STATE_AUTO_COMPLETE);
        if (currentScreen == SCREEN_WINDOW_FULLSCREEN || currentScreen == SCREEN_WINDOW_TINY) {
            backPress();
        }
        JZMediaManager.instance().releaseMediaPlayer();
        JZUtils.saveProgress(getContext(), jzDataSource.getCurrentUrl(), 0);
    }

    @Override
    public void onError(int what, int extra) {
        Log.e(TAG, "onError " + what + " - " + extra + " [" + this.hashCode() + "] ");
        if (what != 38 && extra != -38 && what != -38 && extra != 38 && extra != -19) {
            onStateError();
            if (isCurrentPlay()) {
                JZMediaManager.instance().releaseMediaPlayer();
            }
        }
        //TODO 更改ui
        setUiWitStateAndScreen(CURRENT_STATE_ERROR);
    }

    @Override
    public void setProgressAndText(int progress, long position, long duration) {
        super.setProgressAndText(progress, position, duration);
        if (mPlayListener != null) {
            mPlayListener.onPlayerProgress(progress, progress, (int) position, (int) duration);
        }
    }

    //    ================================== UI更改状态监听 end   ==========================================


    public boolean isCurrentJzvd() {//虽然看这个函数很不爽，但是干不掉
        return JzvdMgr.getCurrentJzvd() != null && JzvdMgr.getCurrentJzvd() == this;
    }

    /**
     * 根据播放状态切换UI显示
     */
    public void setUiWitStateAndScreen(int state) {
        currentState = state;
        switch (currentState) {
            case CURRENT_STATE_NORMAL:  // 普通状态
                cancelProgressTimer();
                if (isCurrentJzvd()) {//这个if是无法取代的，否则进入全屏的时候会releaseMediaPlayer
                    JZMediaManager.instance().releaseMediaPlayer();
                }
                changeUiToNormal();
                break;
            case CURRENT_STATE_PREPARING: // 准备状态
                resetProgressAndTime();
                changeUiToPreparingShow();
                startDismissControlViewTimer();
                break;
            case CURRENT_STATE_PLAYING:  // 播放中
                startProgressTimer();
                if (mPlayListener != null) {
                    mPlayListener.onPlayerPlay();
                }
                changeUiToPlayingShow();
                startDismissControlViewTimer();
                break;
            case CURRENT_STATE_PAUSE: // 暂停
                startProgressTimer();
                changeUiToPauseShow();
                cancelDismissControlViewTimer();
                break;
            case CURRENT_STATE_ERROR: // 错误
                Log.d(TAG, " CURRENT_STATE_ERROR");
                cancelProgressTimer();
                if (mPlayListener != null && mBottomProgressBar != null) {
                    int time = (int) (mBottomProgressBar.getProgress() * getDuration() / 100);
                    mPlayListener.onPlayerError(time);
                }
                changeUiToError();
                //退出
                backPress();
                break;
            case CURRENT_STATE_AUTO_COMPLETE:
                cancelProgressTimer();
                progressBar.setProgress(100);
                currentTimeTextView.setText(totalTimeTextView.getText());

                changeUiToCompleteShow();
                cancelDismissControlViewTimer();
                mBottomProgressBar.setProgress(100);
                break;
            /*todo case CURRENT_STATE_PLAYING_BUFFERING_START:
                changeUiToPlayingBufferingShow();
                break;
            default:
                break;*/
        }
    }

    public void changeUiToError() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
            default:
                break;
        }
    }

    public void cancelDismissControlViewTimer() {
        if (DISMISS_CONTROL_VIEW_TIMER != null) {
            DISMISS_CONTROL_VIEW_TIMER.cancel();
        }
        if (mDismissControlViewTimerTask != null) {
            mDismissControlViewTimerTask.cancel();
        }
    }

    /**
     * */
    public void startDismissControlViewTimer() {
        cancelDismissControlViewTimer();
        DISMISS_CONTROL_VIEW_TIMER = new Timer();
        mDismissControlViewTimerTask = new DismissControlViewTimerTask();
        DISMISS_CONTROL_VIEW_TIMER.schedule(mDismissControlViewTimerTask, 2500);
    }

    public void changeUiToNormal() {
        switch (currentScreen) {
            case SCREEN_WINDOW_NORMAL:
            case SCREEN_WINDOW_LIST:
                setAllControlsVisible(View.VISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.VISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.VISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.VISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
            default:
                break;
        }
    }


    public class DismissControlViewTimerTask extends TimerTask {
        @Override
        public void run() {
            if (currentState != CURRENT_STATE_NORMAL
                    && currentState != CURRENT_STATE_ERROR
                    && currentState != CURRENT_STATE_AUTO_COMPLETE) {
                if (getContext() != null && getContext() instanceof Activity) {
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bottomContainer.setVisibility(View.INVISIBLE);
                            topContainer.setVisibility(View.INVISIBLE);
                            startButton.setVisibility(View.INVISIBLE);
                            if (currentScreen != SCREEN_WINDOW_TINY) {
                                if (!isShowBottomProgress) {
                                    // 如果是m3u8格式视频，则隐藏mBottomProgressBar
                                    mBottomProgressBar.setVisibility(View.GONE);
                                } else {
                                    mBottomProgressBar.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });
                }
            }
        }
    }


    @Override
    public void showWifiDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getResources().getString(R.string.tips_not_wifi));
        builder.setPositiveButton(getResources().getString(R.string.tips_not_wifi_confirm),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //第一次进入播放页
                        if (isFirstMobileDialogShow) {
                            onEvent(JZUserActionStd.ON_CLICK_START_THUMB);
                            isFirstMobileDialogShow = false;
                            //TODO 节操使用 prepareMediaPlayer(); 饺子使用 startVideo()
                            startVideo();
                        } else {
                            //假若当前已经播放，切换到4G
                            onEvent(JZUserActionStd.ON_CLICK_RESUME);
                            onClickPlayButton();
                        }
                        WIFI_TIP_DIALOG_SHOWED = true;
                        dialog.dismiss();
                        if (mStateChangeListener != null) {
                            mStateChangeListener.onWifiDialogClick(true);
                        }
                    }
                });
        builder.setNegativeButton(getResources().getString(R.string.tips_not_wifi_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //停止播放
                isFirstMobileDialogShow = false;
                currentState = CURRENT_STATE_PAUSE;
                updateStartImage();
                cancelDismissControlViewTimer();
                if (mPlayListener != null) {
                    mPlayListener.onPlayerPause();
                }
                dialog.dismiss();
                if (mStateChangeListener != null) {
                    mStateChangeListener.onWifiDialogClick(false);
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }


    protected Dialog mProgressDialog;
    protected ProgressBar mDialogProgressBar;
    protected TextView mDialogSeekTime;
    protected TextView mDialogTotalTime;
    protected ImageView mDialogIcon;


    @Override
    public void showProgressDialog(float deltaX, String seekTime, long seekTimePosition, String totalTime, long totalTimeDuration) {
        super.showProgressDialog(deltaX, seekTime, seekTimePosition, totalTime, totalTimeDuration);
        if (mProgressDialog == null) {
            View localView = LayoutInflater.from(getContext()).inflate(R.layout.jz_dialog_progress, null);
            mDialogProgressBar = localView.findViewById(R.id.duration_progressbar);
            mDialogSeekTime = localView.findViewById(R.id.tv_current);
            mDialogTotalTime = localView.findViewById(R.id.tv_duration);
            mDialogIcon = localView.findViewById(R.id.duration_image_tip);
            mProgressDialog = new Dialog(getContext(), R.style.jz_style_dialog_progress);
            mProgressDialog.setContentView(localView);
            mProgressDialog.getWindow().addFlags(Window.FEATURE_ACTION_BAR);
            mProgressDialog.getWindow().addFlags(32);
            mProgressDialog.getWindow().addFlags(16);
            mProgressDialog.getWindow().setLayout(-2, -2);
            WindowManager.LayoutParams localLayoutParams = mProgressDialog.getWindow().getAttributes();
            localLayoutParams.gravity = 49;
            localLayoutParams.y = getResources().getDimensionPixelOffset(R.dimen.jz_progress_dialog_margin_top);
            mProgressDialog.getWindow().setAttributes(localLayoutParams);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }

        mDialogSeekTime.setText(seekTime);
        mDialogTotalTime.setText(" / " + totalTime);
        mDialogProgressBar.setProgress(totalTimeDuration <= 0 ? 0 : (int) (seekTimePosition * 100 / totalTimeDuration));
        if (deltaX > 0) {
            mDialogIcon.setBackgroundResource(R.drawable.jz_forward_icon);
        } else {
            mDialogIcon.setBackgroundResource(R.drawable.jz_backward_icon);
        }
    }

    @Override
    public void dismissProgressDialog() {
        super.dismissProgressDialog();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    protected Dialog mVolumeDialog;
    protected ProgressBar mDialogVolumeProgressBar;

    @Override
    public void showVolumeDialog(float deltaY, int volumePercent) {
        super.showVolumeDialog(deltaY, volumePercent);
        if (mVolumeDialog == null) {
            View localView = LayoutInflater.from(getContext()).inflate(R.layout.jz_dialog_volume, null);
            mDialogVolumeProgressBar = localView.findViewById(R.id.volume_progressbar);
            mVolumeDialog = new Dialog(getContext(), R.style.jz_style_dialog_progress);
            mVolumeDialog.setContentView(localView);
            mVolumeDialog.getWindow().addFlags(8);
            mVolumeDialog.getWindow().addFlags(32);
            mVolumeDialog.getWindow().addFlags(16);
            mVolumeDialog.getWindow().setLayout(-2, -2);
            WindowManager.LayoutParams localLayoutParams = mVolumeDialog.getWindow().getAttributes();
            localLayoutParams.gravity = 19;
            localLayoutParams.x = getContext().getResources().getDimensionPixelOffset(R.dimen.jz_volume_dialog_margin_left);
            mVolumeDialog.getWindow().setAttributes(localLayoutParams);
        }
        if (!mVolumeDialog.isShowing()) {
            mVolumeDialog.show();
        }
        mDialogVolumeProgressBar.setProgress(volumePercent);
    }

    @Override
    public void dismissVolumeDialog() {
        super.dismissVolumeDialog();
        if (mVolumeDialog != null) {
            mVolumeDialog.dismiss();
        }
    }


    /**
     * 播放视频
     */
    public void startFullPlayer(String title, String url, String cover) {
        try {
            startFullscreen(getContext(), MyExoPlayerPlus.class, url, title);
            com.laka.live.util.Log.d(TAG, " 全屏播放地址=" + url + " isShowBottomProgress=");
        } catch (Exception e) {
            com.laka.live.util.Log.d(TAG, "播放失败 e:" + e.toString());
        }
    }

    @Override
    public long getDuration() {
        return super.getDuration();
    }

    @Override
    public void resetProgressAndTime() {
        super.resetProgressAndTime();
        // Logger.e(TAG, "MyEXOPlayer----重置视频进度和时间");
        mBottomProgressBar.setProgress(0);
        mBottomProgressBar.setSecondaryProgress(0);
    }


    private PlayListener mPlayListener;
    private PlayDialogClickListener mStateChangeListener;

    public void setPlayListener(PlayListener playListener) {
        mPlayListener = playListener;
    }

    public void setPlayDialogClickListener(PlayDialogClickListener playDialogClickListener) {
        mStateChangeListener = playDialogClickListener;
    }

    public void removePlayListener() {
        mStateChangeListener = null;
        mPlayListener = null;
    }

    public interface PlayListener {

        /**
         * 播放回调
         */
        void onPlayerPlay();

        /**
         * 暂停回调
         */
        void onPlayerPause();

        /**
         * 播放视频卡帧回调
         *
         * @param isFreeze
         */
        void onPlayerFreeze(boolean isFreeze);

        /**
         * 播放失败回调
         *
         * @param currentProgress
         */
        void onPlayerError(int currentProgress);

        /**
         * 播放进度回调
         *
         * @param progress    progressBar的显示进度
         * @param secProgress progressBar的缓存进度
         * @param currentTime 当前视频播放进度的position（时间¬）
         * @param totalTime   当前视频播放的总进度（时间）
         */
        void onPlayerProgress(int progress, int secProgress, int currentTime, int totalTime);

    }

    public interface PlayDialogClickListener {

        void onNoNetDialogClick();

        void onWifiDialogClick(boolean isContinue);
    }

}
