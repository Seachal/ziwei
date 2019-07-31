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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.laka.live.R;
import com.laka.live.util.NetworkUtil;

import java.util.Timer;
import java.util.TimerTask;

import cn.jzvd.JZUserAction;
import cn.jzvd.JZUserActionStd;
import cn.jzvd.JZUtils;
import cn.jzvd.JzvdStd;

/**
 * Created by 61453 on 2018/10/29.
 * 仅供参考，不使用
 */

public class MyExoPlayer extends JzvdStd {

    public boolean isShowBottomProgress = true;
    public static final int MAX_VIDEO_FREEZE_DURATION = 10000;
    public static final int MAX_VIDEO_FREEZE_TIME = 10;
    public boolean isReply = false;
    private CountDownTimer countDownTimer;

    /**
     * description:用于记录视频是否卡顿
     **/
    private int lastProgress;
    private int freezeTime = 0;
    private CountDownTimer errorTimer;
    private boolean isErrorCountDownStart = false;

    /**
     * description:网路状态设置
     **/
    private boolean isFirstMobileDialogShow = false;
    private boolean isEnableVideoThumb = false;

    /**
     * 使用默认父类的控件
     */
    public ImageView backButton;
    public ProgressBar bottomProgressBar, loadingProgressBar;
    public TextView titleTextView;
    public ImageView thumbImageView;
    public ImageView tinyBackImageView;

    public MyExoPlayer(Context context) {
        super(context);
    }

    public MyExoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
//        countDownTimer = new CountDownTimer(MAX_VIDEO_FREEZE_DURATION, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                isErrorCountDownStart = true;
//            }
//
//            @Override
//            public void onFinish() {
//                isErrorCountDownStart = false;
//                freezeTime = 0;
//                lastProgress = 0;
//                if (playListener != null && bottomProgressBar != null) {
//                    // 十秒钟后视频播放位置
//                    int time = (int) (bottomProgressBar.getProgress() * getDuration() / 100);
//                    playListener.onPlayerError(time);
//                }
//            }
//        };
    }

    /**
     * 全屏播放
     * */
    @Override
    public void startWindowFullscreen() {
        super.startWindowFullscreen();
    }

    /**
     * Get AppCompatActivity from context
     *
     * @param context
     * @return AppCompatActivity if it's not null
     */
    public static Activity getAppActivity(Context context) {
        if (context == null) return null;
        if (context instanceof Activity) {
            return (Activity) context;
        }
        return null;
    }

    /**
     * 初始化
     */
    @Override
    public void init(Context context) {
        super.init(context);
        bottomProgressBar = findViewById(R.id.bottom_progressbar);
        if (!isShowBottomProgress) {
            bottomProgressBar.setVisibility(INVISIBLE);
        } else {
            bottomProgressBar.setVisibility(VISIBLE);
        }
        titleTextView = findViewById(R.id.title);
        backButton = findViewById(R.id.back);
        thumbImageView = findViewById(R.id.thumb);
        loadingProgressBar = findViewById(R.id.loading);
        tinyBackImageView = findViewById(R.id.back_tiny);

        thumbImageView.setOnClickListener(this);
        backButton.setOnClickListener(this);
        startButton.setOnClickListener(this);
    }

    @Override
    public void setUp(String url, String title, int screen) {
        super.setUp(url, title, screen);
    }

    /*@Override
    public int getLayoutId() {
        return R.layout.jz_video_player;
    }*/

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        if (id == R.id.surface_container) {
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
                            bottomProgressBar.setProgress(progress);
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
        } else if (id == R.id.bottom_seek_progress) {//progress
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
                if (!getCurrentUrl().toString().startsWith("file") && !NetworkUtil.isNetworkOk(getContext()) && !WIFI_TIP_DIALOG_SHOWED && !isFirstMobileDialogShow) {
                    showNoNetWork();
                    return;
                } else if (!getCurrentUrl().toString().startsWith("file") && !JZUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED
                        && !isFirstMobileDialogShow) {
//                    Logger.e(TAG, "点击缩略图播放");
                    showWifiDialog();
                    return;
                }
                onEvent(JZUserActionStd.ON_CLICK_START_THUMB);
                // onPrepared();
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
            if (TextUtils.isEmpty(getCurrentUrl().toString())) {
                Toast.makeText(getContext(), getResources().getString(R.string.no_url), Toast.LENGTH_SHORT).show();
                return;
            }
            if (currentState == CURRENT_STATE_NORMAL) {
                if (!getCurrentUrl().toString().startsWith("file") && !NetworkUtil.isNetworkOk(getContext()) && !WIFI_TIP_DIALOG_SHOWED && !isFirstMobileDialogShow) {
//                    Logger.e(TAG, "MyExoPlayer 显示网络错误---Dialog");
                    showNoNetWork();
                    return;
                } else if (!getCurrentUrl().toString().startsWith("file") && !JZUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED && !isFirstMobileDialogShow) {
//                    Logger.e(TAG, "MyExoPlayer 显示移动网络---Dialog");
                    showWifiDialog();
                    return;
                }
                // onPrepared();
                onEvent(currentState != CURRENT_STATE_ERROR ? JZUserAction.ON_CLICK_START_ICON : JZUserAction.ON_CLICK_START_ERROR);
            } else if (currentState == CURRENT_STATE_PLAYING) {
                onEvent(JZUserAction.ON_CLICK_PAUSE);
                Log.d(TAG, "pauseVideo [" + this.hashCode() + "] ");
//                JCMediaManager.instance().simpleExoPlayer.setPlayWhenReady(false);
//                新sdk中没有该方法
//                setUiWitStateAndScreen(CURRENT_STATE_PAUSE);
            } else if (currentState == CURRENT_STATE_PAUSE) {
                //存在一种情况就是我用4G，然后不加载。那么这个simpleExoPlayer会为null
//                if (JCMediaManager.instance().simpleExoPlayer == null) {
//                    return;
//                }
                onEvent(JZUserAction.ON_CLICK_RESUME);
//                JCMediaManager.instance().simpleExoPlayer.setPlayWhenReady(true);
//                新sdk中没有该方法
//                setUiWitStateAndScreen(CURRENT_STATE_PLAYING);
            } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
                onEvent(JZUserAction.ON_CLICK_START_AUTO_COMPLETE);
                // onPrepared();
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
            // onPrepared();
        }
    }


    public void onClickPlayButton() {
        startButton.performClick();
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
                            // prepareMediaPlayer();
                            // onPrepared();
                        } else {
                            //假若当前已经播放，切换到4G
                            onEvent(JZUserActionStd.ON_CLICK_RESUME);
                            onClickPlayButton();
                        }
                        WIFI_TIP_DIALOG_SHOWED = true;
                        dialog.dismiss();
                        if (stateChangeListener != null) {
                            stateChangeListener.onWifiDialogClick(true);
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
                if (playListener != null) {
                    playListener.onPlayerPause();
                }
                dialog.dismiss();
                if (stateChangeListener != null) {
                    stateChangeListener.onWifiDialogClick(false);
                }
            }
        });
        AlertDialog dialog = builder.create();
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    /**
     * 显示无网状态
     */
    public void showNoNetWork() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("当前网络不可用 请检查网络设置");
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                stopPlay();
                if (stateChangeListener != null) {
                    stateChangeListener.onNoNetDialogClick();
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        super.onStartTrackingTouch(seekBar);
        cancelDismissControlViewTimer();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        super.onStopTrackingTouch(seekBar);
        startDismissControlViewTimer();
    }


    public void startPlayLogic() {
        if (!getCurrentUrl().toString().startsWith("file") && !NetworkUtil.isNetworkOk(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
            showNoNetWork();
            isFirstMobileDialogShow = true;
            return;
        } else if (!getCurrentUrl().toString().startsWith("file") && !JZUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
            showWifiDialog();
            isFirstMobileDialogShow = true;
            return;
        }
        onEvent(JZUserActionStd.ON_CLICK_START_THUMB);

        // prepareMediaPlayer();
        // onPrepared();
    }


    public void stopPlay() {
        releaseAllVideos();
    }

    public void onClickUiToggle() {
        if (currentState == CURRENT_STATE_PREPARING) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPreparingClear();
            } else {
                changeUiToPreparingShow();
            }
        } else if (currentState == CURRENT_STATE_PLAYING) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPlayingClear();
            } else {
                changeUiToPlayingShow();
            }
        } else if (currentState == CURRENT_STATE_PAUSE) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPauseClear();
            } else {
                changeUiToPauseShow();
            }
        } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToCompleteClear();
            } else {
                changeUiToCompleteShow();
            }
        } else if (currentState == CURRENT_STATE_PLAYING) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPlayingBufferingClear();
            } else {
                changeUiToPlayingBufferingShow();
            }
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


    @Override
    public void onPrepared() {
        super.onPrepared();
        setAllControlsVisible(View.VISIBLE, View.INVISIBLE, View.INVISIBLE,
                View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.VISIBLE);
        startDismissControlViewTimer();
    }

    @Override
    public void resetProgressAndTime() {
        super.resetProgressAndTime();
        //        Logger.e(TAG, "MyEXOPlayer----重置视频进度和时间");
        bottomProgressBar.setProgress(0);
        bottomProgressBar.setSecondaryProgress(0);
    }


    //Unified management Ui
    public void changeUiToNormal() {
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

    /**
     * 设置控制控件可见
     * */
    public void setAllControlsVisible(int topCon, int bottomCon, int startBtn, int loadingPro,
                                      int thumbImg, int coverImg, int bottomPro) {
        if (isReply) {
            topContainer.setVisibility(View.INVISIBLE);
            bottomContainer.setVisibility(View.INVISIBLE);
            startButton.setVisibility(View.INVISIBLE);
            loadingProgressBar.setVisibility(View.INVISIBLE);
            thumbImageView.setVisibility(View.INVISIBLE);
            bottomProgressBar.setVisibility(View.INVISIBLE);
            return;
        }

        topContainer.setVisibility(topCon);
        bottomContainer.setVisibility(bottomCon);
        startButton.setVisibility(startBtn);
        loadingProgressBar.setVisibility(loadingPro);
        thumbImageView.setVisibility(thumbImg);
        bottomProgressBar.setVisibility(bottomPro);

        if (!isShowBottomProgress && currentScreen == SCREEN_WINDOW_FULLSCREEN) {
            SeekBar seekBar = (SeekBar) findViewById(R.id.progress);
            seekBar.setVisibility(View.INVISIBLE);
            TextView tvTotal = (TextView) findViewById(R.id.total);
            tvTotal.setVisibility(View.INVISIBLE);
            bottomProgressBar.setVisibility(View.GONE);
        } else if (!isShowBottomProgress && currentScreen == SCREEN_WINDOW_NORMAL) {
            bottomProgressBar.setVisibility(View.GONE);
        }
    }

    /**
     * 更新开始图片
     * */
    public void updateStartImage() {
        if (currentState == CURRENT_STATE_PLAYING) {
            startButton.setImageResource(R.drawable.jz_click_pause_selector);
        } else if (currentState == CURRENT_STATE_ERROR) {
            startButton.setImageResource(R.drawable.jz_click_replay_selector);
        } else {
            startButton.setImageResource(R.drawable.jz_click_play_selector);
        }
    }


    protected Dialog mProgressDialog;
    protected ProgressBar mDialogProgressBar;
    protected TextView mDialogSeekTime;
    protected TextView mDialogTotalTime;
    protected ImageView mDialogIcon;

    /**
     * 显示进度dialog
     * */
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
            localLayoutParams.y = getResources().getDimensionPixelOffset(R.dimen.jz_start_button_w_h_normal);
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

    /**
     * 隐藏进度dialog
     * */
    @Override
    public void dismissProgressDialog() {
        super.dismissProgressDialog();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    protected Dialog mVolumeDialog;
    protected ProgressBar mDialogVolumeProgressBar;

    /**
     * 显示音量dialog
     * */
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
            localLayoutParams.x = getContext().getResources().getDimensionPixelOffset(R.dimen.jz_start_button_w_h_normal);
            mVolumeDialog.getWindow().setAttributes(localLayoutParams);
        }
        if (!mVolumeDialog.isShowing()) {
            mVolumeDialog.show();
        }

        mDialogVolumeProgressBar.setProgress(volumePercent);
    }


    /**
     * 音量弹窗关闭
     * */
    @Override
    public void dismissVolumeDialog() {
        super.dismissVolumeDialog();
        if (mVolumeDialog!=null){
            mVolumeDialog.dismiss();
        }
    }


    protected DismissControlViewTimerTask mDismissControlViewTimerTask;
    protected static Timer DISMISS_CONTROL_VIEW_TIMER;

    public void startDismissControlViewTimer() {
        cancelDismissControlViewTimer();
        DISMISS_CONTROL_VIEW_TIMER = new Timer();
        mDismissControlViewTimerTask = new DismissControlViewTimerTask();
        DISMISS_CONTROL_VIEW_TIMER.schedule(mDismissControlViewTimerTask, 2500);
    }

    public void cancelDismissControlViewTimer() {
        if (DISMISS_CONTROL_VIEW_TIMER != null) {
            DISMISS_CONTROL_VIEW_TIMER.cancel();
        }
        if (mDismissControlViewTimerTask != null) {
            mDismissControlViewTimerTask.cancel();
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
                                    bottomProgressBar.setVisibility(View.GONE);
                                } else {
                                    bottomProgressBar.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });
                }
            }
        }
    }


    // 播放视频
    public void startFullPlayer(String title, String url, String cover) {
        try {
            startFullscreen(getContext(), MyExoPlayer.class, url, title);
        } catch (Exception e) {
        }
    }



    /**
     * 主要暴露API供Activity缩放播放器时候调用，因为内部MyExoPlayer继承的JCVideoPlayer默认设置了高度变化
     *
     * @param isEnable
     */
    public void enableDefaultRatio(boolean isEnable) {
        // enableDefaultRatio(isEnable);
    }



    @Override
    public long getDuration() {
        return super.getDuration();
    }

    /**
     * 由于ExoPlayer是封装之后的，默认都把thumb给隐藏掉了，这里多加个flag开启
     *
     * @param isEnable
     */
    public void enableImageThumb(boolean isEnable) {
        isEnableVideoThumb = isEnable;
    }


    /**
     * 自定义监听器
     */
    PlayListener playListener;
    PlayDialogClickListener stateChangeListener;

    public void setPlayListener(PlayListener listener) {
        playListener = listener;
    }

    public void removePlayListener() {
        playListener = null;
        stateChangeListener = null;
    }

    public void setPlayDialogClick(PlayDialogClickListener stateChangeListener) {
        this.stateChangeListener = stateChangeListener;
    }



    //    监听器
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
