package com.laka.live.ui.room;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.account.my.ContributionListActivity;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.bean.GiftInfo;
import com.laka.live.bean.ListUserInfo;
import com.laka.live.bean.UserInfo;
import com.laka.live.dao.DbManger;
import com.laka.live.gift.GiftAudioManager;
import com.laka.live.gift.GiftResManager;
import com.laka.live.help.NoDoubleClickManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.manager.BytesReader;
import com.laka.live.manager.OnResultListenerAdapter;
import com.laka.live.manager.RoomManager;
import com.laka.live.msg.FormulaMsg;
import com.laka.live.msg.Msg;
import com.laka.live.msg.UserMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.chat.ChatMessageActivity;
import com.laka.live.ui.chat.ChatMessageView;
import com.laka.live.ui.chat.ChatSessionPanel;
import com.laka.live.ui.chat.ChatUnfollowPanel;
import com.laka.live.ui.chat.FormulaPanel;
import com.laka.live.ui.rankinglist.RankingItemView;
import com.laka.live.ui.widget.MarkSimpleDraweeView;
import com.laka.live.ui.widget.gift.GiftAnimManger;
import com.laka.live.ui.widget.gift.GiftGridView;
import com.laka.live.ui.widget.gift.GiftShowView;
import com.laka.live.ui.widget.room.LiveRoomUserInfoPanel;
import com.laka.live.ui.widget.room.ScreenLoadingView;
import com.laka.live.util.Common;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;

import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import laka.live.bean.ChatMsg;
import laka.live.bean.ChatSession;

/**
 * Created by luwies on 16/3/8.
 */
public class SeeReplayActivityBackup extends BaseActivity implements
        OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener {

    private final static String TAG = "SeeReplayActivity";

    /**
     *
     */
    private SurfaceView surfaceView;
//    private TextureView surfaceView;
//    private Surface mSurface;
    /**
     * surfaceView播放控制
     */
    private SurfaceHolder surfaceHolder;
    /**
     * 播放视频
     */
    private MediaPlayer mediaPlayer;
    /**
     * 播放控制条
     */
    private SeekBar seekBar;

    /**
     * 暂停播放按钮
     */
    private Button playButton;

    /**
     * 重新播放按钮
     */
    private Button replayButton;

    /**
     * 截图按钮
     */
    private Button screenShotButton;

    /**
     * 改变视频大小button
     */
    private Button videoSizeButton;

    /**
     * 加载进度显示条
     */
//    private ProgressBar progressBar;


    /**
     * 记录当前播放的位置
     */
    private int playPosition = -1;

    /**
     * seekBar是否自动拖动
     */
    private boolean seekBarAutoFlag = false;

    /**
     * 视频时间显示
     */
    private TextView vedioTiemTextView;

    private RelativeLayout rlOverBg;
    /**
     * 播放总时间
     */
    private String videoTimeString;

    private long videoTimeLong;

    /**
     * 播放路径
     */
    private String videoUrl;

    /**
     * 课程Id
     */
    private String mCourseId;

    /**
     * 配方做法
     */
    private FormulaMsg mFormulaMsg;

    /**
     * 屏幕的宽度和高度
     */
    private int screenWidth, screenHeight;

    /**
     * 主播ID
     */
    private String userId;
    private Button btnPlay, btnFormula, btnShare, btnLetter, btnGift, btnFollow;
    private ImageView btnClose;
    private TextView tvKazuan, tvAudienceCntNow, tvLakaNo;
    /**
     * 回放ID
     */
    private String rollBackId;
    /**
     * 观看人数
     */
    private int views;
    /**
     * 收到卡钻
     */
    private int recvCoins;
    //主播界面
    RelativeLayout rlTopBarZhubo;
    //观众界面
    RelativeLayout rlTopBarAudience, rlKazuanBank;
    UserInfo zhuboUserInfo;
    private MarkSimpleDraweeView ivZhuboHead;
    private GiftGridView giftGridView;
    private GiftInfo chooseGift;
    //全屏触摸处理层
    RelativeLayout rlTouch;
    //礼物显示区
    private GiftShowView giftShowView;
    private UserInfo mSelfUserInfo;
    RoomManager roomManger;//房间通讯操作类
    private String otherNickName, otherAvatar;//对方内容  otherUserId,
    //    ChatMsg curGiftMsg;
    private GiftAnimManger giftAnimManger;   //特殊动画管理
    //    private View mLoading;
    private ScreenLoadingView rlLoading;
    //    private LakaLoadingView mLoadingImageView;
//    private TextView loadingTextView;
    private boolean isFirstCreate = true;
    private ChatMessageView mChatMessagePanel;
    private ChatSessionPanel mChatSessionPanel;
    private ChatUnfollowPanel mChatUnfollowPanel;
    private FormulaPanel mFormulaPanel;
    HashMap<Integer, ChatMsg> sendingGifts = new HashMap<>();
    int seqGift = 1;
    private boolean mStartSeek;
    private int duration = 0;
    private boolean isChatMessagePanelShow = false;

    public static void startActivity(Context context, String videoUrl,
                                     String userId,
                                     int views,
                                     int recvCoins,
                                     String rollBackId) {
        if (TextUtils.isEmpty(videoUrl) || TextUtils.isEmpty(userId)) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.error_data_tip));
            return;
        }

        if (!NoDoubleClickManager.isNoDoubleClick(NoDoubleClickManager.TYPE_ENTER_ZHIBO_ROOM)) {
            return;
        }

        if (context != null) {
            Intent intent = new Intent(context, SeeReplayActivityBackup.class);
            intent.putExtra("videoUrl", videoUrl);
            intent.putExtra("userId", userId);
            intent.putExtra("views", views);
            intent.putExtra("recvCoins", recvCoins);
            intent.putExtra("rollBackId", rollBackId);
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_see_replay_backup);
        setSwipeBackEnable(false);
        videoUrl = getIntent().getStringExtra("videoUrl");
        userId = getIntent().getStringExtra("userId");
        views = getIntent().getIntExtra("views", 0);
        recvCoins = getIntent().getIntExtra("recvCoins", 0);
        rollBackId = getIntent().getStringExtra("rollBackId");
        mCourseId = getIntent().getStringExtra("courseId");

        Common.playPosition = -1;
        isFirstCreate = true;
        if (videoUrl == null || videoUrl.isEmpty()) {
            showToast("回放地址不存在");
            finish();
            return;
        } else {
        }
        // 获取屏幕的宽度和高度
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        initViews();
        initData();
        initRoomManger();
        initChatSessionPanel();
        initChatUnfollowPanel();
        getFormula();

    }

    private void initRoomManger() {

        roomManger = RoomManager.getInstance();
        roomManger.addResultListener(roomListener);
        roomManger.startRoom();
    }

    private RoomManager.OnResultListener roomListener = new OnResultListenerAdapter() {
        private void insertMessage(String message) {
            Log.d(TAG, message);

        }

        @Override
        public void chatDidConnect() {
            insertMessage("Socket连接成功");
            //更新对方用户数据
//            roomManger.queryUserInfo(otherUserId);
        }

        @Override
        public void chatDidDisconnect() {
            insertMessage("Socket断开成功");
        }

        @Override
        public void chatErrorOccur(int errcode, final String errMsg) {
            insertMessage("Socket errcode=" + errcode + " errMsg=" + errMsg);
            if (errcode == RoomManager.CHAT_ERROR_SERVER_DISCONNECT) {
                SeeReplayActivityBackup.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast(errMsg);
                    }
                });
            } else if (errcode == RoomManager.TLV_E_KAZUAN_NOT_ENOUGH) {
                showRechargeDialog(errMsg);
            }
        }

        @Override
        public void chatReceiveGift(BytesReader.GiftMessage message) {
            insertMessage(String.format("收到礼物 来自:%s id:%s 连送数:%d 用户ID:%s", message.nickName, message.giftID, message.count, message.audienceID));
            if (message.type == RoomManager.TLV_REQ_UNICAST_GIVE && isResume()) {
                ChatMsg giftMessagee = createGiftMessagee("送" +
                                GiftResManager.getInstance().getUnitByGiftId(message.giftID + "") +
                                GiftResManager.getInstance().getNameByGiftId(message.giftID + ""),
                        false,
                        message.time,
                        userId,
                        otherNickName,
                        otherAvatar,
                        Integer.parseInt(message.giftID));
//                showMessage(giftMessagee);
                saveMessage(giftMessagee, false);


            }
        }

        @Override
        public void chatDidReceiveMessage(BytesReader.AudienceMessage message) {
            insertMessage(String.format("收到消息 来自:%s 类型:%d 内容:%s 等级:%d id:%s time:%d  type:%d  ", message.nickName, message.type, message.content, message.level
                    , message.audienceID, message.time, message.type));
            if (message.type == 0) {
                if (message.audienceID.equals(userId)) {
                    Log.d(TAG, "是当前用户消息 插入并显示");
                    ChatMsg chatMsg = createMessagee(message.content, false, message.time, message.audienceID, otherNickName, otherAvatar);
                    saveMessage(chatMsg, true);
//                    showMessage(chatMsg);
                } else {
                    Log.d(TAG, "不是当前用户消息 插入但不显示");
                    ChatMsg chatMsg = createMessagee(message.content, false, message.time, message.audienceID, message.nickName, "");
                    saveMessage(chatMsg, true);
                }
            }
        }


        @Override
        public void chatDidSuccess(int code) {
//            if (code == RoomManager.TLV_RSP_SAY) {
//                saveMessage(curChatMsg,false);
//                showMessage(curChatMsg);
//            }
            if (code == RoomManager.TLV_RSP_GIVE) {
//                if (curGiftMsg == null) {
//                    return;
//                }
//                saveMessage(curGiftMsg, false);
//                BytesReader.GiftMessage message = new BytesReader().getGiftMessage();
//                message.audienceID = AccountInfoManager.getInstance().getCurrentAccountUserId() + "";
//                message.count = 1;
//                message.giftID = curGiftMsg.getGiftId() + "";
//                message.nickName = AccountInfoManager.getInstance().getCurrentAccountNickName();
//                //处理礼物动画区显示
//                giftShowView.showGift(message);
//                giftAnimManger.addGift(message);

            }
        }

        @Override
        public void chatDidQueryUserInfo(BytesReader.Audience audience) {
            Log.d(TAG, "chatDidQueryUserInfo 获取用户信息成功  " + audience);
            //更新会话
//            adapter.otherAvatar = audience.avatar;
//            adapter.notifyDataSetChanged();
//            DbManger.getInstance().updateSession(myUserInfo.getIdStr(), audience.id, audience.nickName, audience.avatar, audience.level, (short) audience.auth,(int)audience.isFollow);
        }
    };

    private void initData() {
        getZhuboUserInfo();
        refreshRommDataUI();
    }

    private void refreshRommDataUI() {
        tvAudienceCntNow.setText(String.valueOf(views) + "人观看");
        tvKazuan.setText(String.valueOf(recvCoins));
//        CacheUtil.addCache("totalCoins", roomCoins);
//        CacheUtil.addCache("totalAudience", roomAudience);
    }

    public void initViews() {
        tvAudienceCntNow = (TextView) findViewById(R.id.tv_audience_cnt_now);
        tvKazuan = (TextView) findViewById(R.id.tv_kazuan);
        tvLakaNo = (TextView) findViewById(R.id.tv_lakano);
        rlLoading = (ScreenLoadingView) findViewById(R.id.loading);
//        mLoadingImageView = (LakaLoadingView) findViewById(R.id.loadingImageView);
//        loadingTextView = (TextView) findViewById(R.id.loadingTextView);
//        loadingTextView.setVisibility(View.GONE);
        rlLoading.setVisibility(View.GONE);
        giftAnimManger = new GiftAnimManger(this, (ViewGroup) findViewById(R.id.root_view));
        rlTopBarAudience = (RelativeLayout) findViewById(R.id.rl_info_audience);
        rlTopBarAudience.setOnClickListener(this);
        giftGridView = (GiftGridView) findViewById(R.id.gift_grid_view);
        giftGridView.setOnClickListener(this);
        giftGridView.setListeners(this, new GiftGridView.GiftGridListener() {
            @Override
            public void setChooseGift(GiftInfo gift) {
                chooseGift = gift;
            }
        }, true);
        giftGridView.setVisibility(View.GONE);
        mSelfUserInfo = AccountInfoManager.getInstance().getAccountInfo();
//      giftGridView.setKazuanCnt(mSelfUserInfo.getCoins());
        giftShowView = (GiftShowView) findViewById(R.id.gift_show_view);
        rlTouch = (RelativeLayout) findViewById(R.id.rl_touch);
        rlTouch.setOnClickListener(this);
        TextView tvZhiboLive = (TextView) findViewById(R.id.tv_zhibo_live);
        tvZhiboLive.setText("回放");
        ivZhuboHead = (MarkSimpleDraweeView) findViewById(R.id.iv_head_zhubo);
        btnPlay = (Button) findViewById(R.id.btn_play);
        btnPlay.setOnClickListener(this);
        btnFormula = (Button) findViewById(R.id.btn_formula);
        btnFormula.setOnClickListener(this);
        btnShare = (Button) findViewById(R.id.btn_share);
        btnShare.setOnClickListener(this);
        btnLetter = (Button) findViewById(R.id.btn_letter);
        btnLetter.setOnClickListener(this);
        btnGift = (Button) findViewById(R.id.btn_gift);
        btnGift.setOnClickListener(this);
        btnClose = (ImageView) findViewById(R.id.close_btn);
        btnClose.setOnClickListener(this);
        btnFollow = (Button) findViewById(R.id.btn_follow);
        if (TextUtils.equals(userId, AccountInfoManager.getInstance().getCurrentAccountUserIdStr())) {
            btnFollow.setVisibility(View.GONE);
        } else {
            btnFollow.setOnClickListener(this);
        }
        rlTopBarZhubo = (RelativeLayout) findViewById(R.id.rl_info_zhubo);
        rlTopBarZhubo.setVisibility(View.GONE);
        rlTopBarAudience = (RelativeLayout) findViewById(R.id.rl_info_audience);
        rlTopBarAudience.setVisibility(View.VISIBLE);
        rlKazuanBank = (RelativeLayout) findViewById(R.id.rl_kazuan);
        rlKazuanBank.setOnClickListener(this);

//        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        playButton = (Button) findViewById(R.id.button_play);
        replayButton = (Button) findViewById(R.id.button_replay);

        rlOverBg = (RelativeLayout) findViewById(R.id.rl_overbg);
//        vedioTiemTextView = (TextView) findViewById(R.id.textView_showTime);
        vedioTiemTextView = (TextView) findViewById(R.id.tv_time);
        screenShotButton = (Button) findViewById(R.id.button_screenShot);
        videoSizeButton = (Button) findViewById(R.id.button_videoSize);

        // 初始化控件
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
//        surfaceView = (TextureView) findViewById(R.id.surfaceView);
//        surfaceView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
//            @Override
//            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
//                Log.d(TAG," onSurfaceTextureAvailable");
//                mSurface = new Surface(surface);
//                playVideo();
//            }
//
//            @Override
//            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
//
//            }
//
//            @Override
//            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
//                surfaceView = null;
//                mSurface = null;
//                mediaPlayer.stop();
//                mediaPlayer.release();
//                return true;
//            }
//
//            @Override
//            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//
//            }
//        });
        // 设置surfaceHolder
        surfaceHolder = surfaceView.getHolder();
        // 设置Holder类型,该类型表示surfaceView自己不管理缓存区,虽然提示过时，但最好还是要设置
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // 设置surface回调
        surfaceHolder.addCallback(new SurfaceCallback());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean bFromUser) {
                vedioTiemTextView.setText(String.format("%02d:%02d", progress / 60, progress % 60) + "/" + String.format("%02d:%02d", duration / 60, duration % 60));
                if (bFromUser) {
                    Log.d(TAG, "手动拖动 progress=" + progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mStartSeek = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                if (mLivePlayer != null) {
//                    Log.d(TAG, "onStopTrackingTouch progress =" + seekBar.getProgress());
//                    recoveryProgress = seekBar.getProgress();
//                    mLivePlayer.seek(seekBar.getProgress());
//                }
//                mTrackingTouchTS = System.currentTimeMillis();
                if (mediaPlayer != null)
                    mediaPlayer.seekTo(seekBar.getProgress());
                mStartSeek = false;
            }
        });

        showCenterLoading();
    }


    // 获取配方做法
    private void getFormula() {

        DataProvider.getFormula(this, mCourseId, new GsonHttpConnection.OnResultListener<FormulaMsg>() {
            @Override
            public void onSuccess(FormulaMsg msg) {

                mFormulaMsg = msg;
                if (Utils.isNotEmpty(mFormulaMsg.formula)) {
                    findViewById(R.id.btn_formula).setVisibility(View.VISIBLE);
                    findViewById(R.id.btn_formula).setOnClickListener(SeeReplayActivityBackup.this);
                }

            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
            }

        });
    }

    // SurfaceView的callBack
    private class SurfaceCallback implements SurfaceHolder.Callback {
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            isSurfaveCreated = true;
        }

        public void surfaceCreated(SurfaceHolder holder) {
//            surfaceView.setVisibility(View.INVISIBLE);
            Log.d(TAG, " surfaceCreated");
            isSurfaveCreated = true;
//            rlOverBg.setVisibility(View.VISIBLE);
            // surfaceView被创建
            // 设置播放资源
            if (isFirstCreate) {
                playVideo();
            } else {
                Log.d(TAG, "恢复播放");
                mediaPlayer.setDisplay(holder);
                if (!isRunning) {
                    new Thread(runnable).start();
                }
//              mediaPlayer.seekTo(Common.playPosition);
//              mediaPlayer.start();
            }


        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.d(TAG, " surfaceDestroyed");
            // surfaceView销毁,同时销毁mediaPlayer
//            if (null != mediaPlayer) {
//                mediaPlayer.release();
//                mediaPlayer = null;
//            }

        }

    }

    /**
     * 播放视频
     */
    public void playVideo() {
        isFirstCreate = false;
        // 初始化MediaPlayer
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();
//        mediaPlayer.setSurface(mSurface);
        // 重置mediaPaly,建议在初始滑mediaplay立即调用。
        mediaPlayer.reset();
        // 设置声音效果
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        // 设置播放完成监听
        mediaPlayer.setOnCompletionListener(this);
        // 设置媒体加载完成以后回调函数。
        mediaPlayer.setOnPreparedListener(this);
        // 错误监听回调函数
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                Log.d(TAG, "onInfo what=" + what + " extra=" + extra);
                if (mp.isPlaying()) {
                    hideCenterLoading();
                }
                return false;
            }
        });
        // 设置缓存变化监听
        mediaPlayer.setOnBufferingUpdateListener(this);
        Uri uri = Uri.parse(videoUrl);
        Log.d(TAG, "播放地址 videoUrl=" + videoUrl);
        //
        try {
            // mediaPlayer.reset();
//            mediaPlayer.setDataSource(pathString);
            mediaPlayer.setDataSource(this, uri);
            // mediaPlayer.setDataSource(SurfaceViewTestActivity.this, uri);
            // 设置异步加载视频，包括两种方式 prepare()同步，prepareAsync()异步
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "加载视频错误！", Toast.LENGTH_LONG).show();
        }
    }

    boolean isSurfaveCreated = false;

    /**
     * 视频加载完毕监听
     *
     * @param mp
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        if (isDestroyed()) {
            return;
        }
        rlOverBg.setVisibility(View.GONE);
//        surfaceView.setVisibility(View.VISIBLE);
        // 当视频加载完毕以后，隐藏加载进度条
//        progressBar.setVisibility(View.GONE);
        hideCenterLoading();
        // 判断是否有保存的播放位置,防止屏幕旋转时，界面被重新构建，播放位置丢失。
        if (Common.playPosition >= 0) {
            mediaPlayer.seekTo(Common.playPosition);
            Common.playPosition = -1;
            // surfaceHolder.unlockCanvasAndPost(Constants.getCanvas());
        }
        seekBarAutoFlag = true;
        // 设置控制条,放在加载完成以后设置，防止获取getDuration()错误
        duration = mediaPlayer.getDuration();
        seekBar.setMax(mediaPlayer.getDuration());
        // 设置播放时间
        videoTimeLong = mediaPlayer.getDuration();
        videoTimeString = getShowTime(videoTimeLong);
        vedioTiemTextView.setText("00:00:00/" + videoTimeString);
        // 设置拖动监听事件
        seekBar.setOnSeekBarChangeListener(new SeekBarChangeListener());
        // 设置按钮监听事件
        // 重新播放
        replayButton.setOnClickListener(SeeReplayActivityBackup.this);
        // 暂停和播放
        playButton.setOnClickListener(SeeReplayActivityBackup.this);
        // 截图按钮
        screenShotButton.setOnClickListener(SeeReplayActivityBackup.this);
        // 视频大小
        videoSizeButton.setOnClickListener(SeeReplayActivityBackup.this);
        // 播放视频
        mediaPlayer.start();

        while (!this.isSurfaveCreated) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 设置显示到屏幕
        try {
            mediaPlayer.setDisplay(surfaceHolder);
        } catch (Exception e) {

        }
        surfaceHolder.setKeepScreenOn(true);
        // 开启线程 刷新进度条
        new Thread(runnable).start();
        // 设置surfaceView保持在屏幕上
        mediaPlayer.setScreenOnWhilePlaying(true);

    }


    boolean isRunning = false;
    /**
     * 滑动条变化线程
     */
    private Runnable runnable = new Runnable() {

        public void run() {
            isRunning = true;
            // TODO Auto-generated method stub
            // 增加对异常的捕获，防止在判断mediaPlayer.isPlaying的时候，报IllegalStateException异常
            try {
                while (seekBarAutoFlag) {
                    /*
                     * mediaPlayer不为空且处于正在播放状态时，使进度条滚动。
                     * 通过指定类名的方式判断mediaPlayer防止状态发生不一致
                     */
                    Thread.sleep(500);
                    if (null != SeeReplayActivityBackup.this.mediaPlayer
                            && SeeReplayActivityBackup.this.mediaPlayer.isPlaying()) {
//                        Log.d(TAG," seekBar.setProgress="+mediaPlayer.getCurrentPosition());
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            isRunning = false;
        }
    };

    /**
     * seekBar拖动监听类
     *
     * @author shenxiaolei
     */
    @SuppressWarnings("unused")
    private class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            // TODO Auto-generated method stub
            if (progress >= 0) {
                // 如果是用户手动拖动控件，则设置视频跳转。
                if (fromUser) {
                    Log.d(TAG, "用户手动拖动控件 progress=" + progress);
                    mediaPlayer.seekTo(progress);
                    Common.playPosition = mediaPlayer.getCurrentPosition();
                }

                if (isCompletion) {
                    isCompletion = false;
                    doReplay();
                }

                // 设置当前播放时间
                vedioTiemTextView.setText(getShowTime(progress) + "/" + videoTimeString);
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub

        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub

        }

    }

    /**
     * 按钮点击事件监听
     */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_follow:
                follow();
                break;
            case R.id.btn_play:
                onClickPlay();
                break;
            case R.id.btn_share:
                showShareDialog(Common.SHARE_COURSE_URL + mCourseId, share.getShareTitle(this), share.getShareContent(this), zhuboUserInfo != null ? zhuboUserInfo.getAvatar() : "", false);
                break;
            case R.id.btn_formula:
                showFormulaPanel();
                break;
            case R.id.btn_letter:
                if (zhuboUserInfo == null) {
                    showToast("请稍候");
                    return;
                }
                ChatMessageActivity.startPrivateChatActivity(this, zhuboUserInfo.getIdStr(), zhuboUserInfo.getNickName(), zhuboUserInfo.getAvatar(), DbManger.SESSION_TYPE_UNFOLLOW);
                break;
            case R.id.btn_gift:
                giftGridView.show();
                break;
            case R.id.close_btn:
                finish();
                break;
            case R.id.rl_touch:
                handleTouchScreen();
                break;
            case R.id.tv_gift_recharge://礼物框跳充值
                jumpToRecharge();
                break;
            case R.id.btn_send_gift://礼物框送礼确认
                if (chooseGift == null) {
                    showToast(R.string.please_choose_gift);
                    return;
                }
                if (!GiftResManager.getInstance().checkIsResReady(chooseGift)) {
                    showToast(R.string.gift_readying);
                    return;
                }
                doSendGift();
                break;
            case R.id.rl_kazuan:
                if (StringUtils.isNotEmpty(userId) && userId.equals(AccountInfoManager.getInstance().getCurrentAccountUserIdStr())) {
                    ContributionListActivity.startActivity(this, userId, RankingItemView.FROM_TYPE_MINE);
                } else {
                    ContributionListActivity.startActivity(this, userId, RankingItemView.FROM_TYPE_USER_INFO);
                }
                break;
            case R.id.rl_info_audience:
                //主播弹出层
                if (zhuboUserInfo != null) {
                    showUserInfoPanel(zhuboUserInfo.getIdStr(), zhuboUserInfo.getAvatar());
                } else {
                    showUserInfoPanel(userId, null);
                }
                break;
        }
        // TODO Auto-generated method stub
        // 重新播放
        if (v == replayButton) {
            // mediaPlayer不空，则直接跳转
//            if (null != mediaPlayer) {
//                System.out.println(" MediaPlayer和进度条都跳转到开始位置");
//                // MediaPlayer和进度条都跳转到开始位置
//                mediaPlayer.seekTo(0);
//                seekBar.setProgress(0);
//                doReplay();
//            } else {
//                // 为空则重新设置mediaPlayer
//                playVideo();
//            }

        }
        // 播放、暂停按钮
        if (v == playButton) {
            onClickPlay();
        }
//        // 视频截图
//        if (v == screenShotButton) {
//            if (null != mediaPlayer) {
//                // 视频正在播放，
//                if (mediaPlayer.isPlaying()) {
//                    // 获取播放位置
//                    Common.playPosition = mediaPlayer.getCurrentPosition();
//                    // 暂停播放
//                    mediaPlayer.pause();
//                    //
//                    playButton.setText("播放");
//                }
//                // 视频截图
//                savaScreenShot(Common.playPosition);
//            } else {
//                Toast.makeText(SeeReplayActivity.this, "视频暂未播放！", Toast.LENGTH_SHORT).show();
//            }
//        }
//        if (v == videoSizeButton) {
//            // 调用改变大小的方法
//            changeVideoSize();
//        }
    }


    private void showFormulaPanel() {

        if (mFormulaPanel == null)
            mFormulaPanel = new FormulaPanel(this);

        mFormulaPanel.showPanel(mFormulaMsg);
    }

    private void doSendGift() {
        Log.d(TAG, "sendGift id=" + chooseGift.getId() + " otherUserId=" + userId);
//                ChatMsg message = new ChatMsg();
//                message.setIsSend(true);
//                datas.add(message);
        //特效音量
        GiftAudioManager.getInstance().playSound(R.raw.send_gift_audio);
        ChatMsg curGiftMsg = createGiftMessagee("送" + GiftResManager.getInstance().getUnitByGiftId(chooseGift.getId() + "") + GiftResManager.getInstance().getNameByGiftId(chooseGift.getId() + ""), true, System.currentTimeMillis() / 1000, userId, otherNickName, otherAvatar, chooseGift.getId());
        sendingGifts.put(seqGift, curGiftMsg);
        roomManger.inboxGift(String.valueOf(chooseGift.getId()), userId, seqGift);
        seqGift++;


    }

    private void handleTouchScreen() {
        if (giftGridView.getVisibility() == View.VISIBLE) {
            giftGridView.hide();
        }
    }

    boolean isBtnPlay = true;

    private void onClickPlay() {
        if (null != mediaPlayer) {
            // 正在播放
            if (mediaPlayer.isPlaying()) {
                Log.d(TAG, "onClickPlay mediaPlayer.isPlaying()");
                Common.playPosition = mediaPlayer.getCurrentPosition();
                // seekBarAutoFlag = false;
                mediaPlayer.pause();
                isBtnPlay = false;
//                playButton.setText("播放");
                btnPlay.setBackgroundResource(R.drawable.live_btn_play_selector);
            } else {
                if (Common.playPosition >= 0) {
                    Log.d(TAG, "onClickPlay Common.playPosition >= 0");
                    seekBarAutoFlag = true;
                    mediaPlayer.seekTo(Common.playPosition);
                    mediaPlayer.start();
//                    playButton.setText("暂停");
                    Common.playPosition = -1;
                    if (!isRunning) {
                        new Thread(runnable).start();
                    }
                } else {
                    Log.d(TAG, "onClickPlay doReplay");
                    doReplay();
                }
                isBtnPlay = true;
                btnPlay.setBackgroundResource(R.drawable.live_btn_stop_selector);
            }
        } else {
            Log.d(TAG, "onClickPlay playVideo");
            playVideo();
        }
    }


    boolean isStartPlaySuccess = false;

    private void doReplay() {
        seekBarAutoFlag = true;
        // 开启线程 刷新进度条
        if (!isRunning) {
            new Thread(runnable).start();
        }
        // 如果不处于播放状态，则开始播放
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    boolean isCompletion = false;

    /**
     * 播放完毕监听
     *
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        // 设置seeKbar跳转到最后位置
//        seekBar.setProgress(Integer.parseInt(String.valueOf(videoTimeLong)));
        mediaPlayer.seekTo(0);
        seekBar.setProgress(0);
        // 设置播放标记为false
        seekBarAutoFlag = false;
        isCompletion = true;
        btnPlay.setBackgroundResource(R.drawable.live_btn_play_selector);
        Common.playPosition = -1;
    }

    /**
     * 视频缓存大小监听,当视频播放以后 在started状态会调用
     */
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

        int playPercent = (int) (Float.parseFloat(mp.getCurrentPosition() + "") / Float.parseFloat(mp.getDuration() + ""));

        // percent 表示缓存加载进度，0为没开始，100表示加载完成，在加载完成以后也会一直调用该方法
        Log.d(TAG, "onBufferingUpdate-->" + percent + " mp.isPlaying()=" + mp.isPlaying()
                + " 当前播放进度 playPercent=" + playPercent);
//        // 可以根据大小的变化来

        if (playPercent <= percent) {
            hideCenterLoading();
        } else {
            showCenterLoading();
        }
//        if(percent==0){
//            showCenterLoading();
//        }
    }

    /**
     * 错误监听
     *
     * @param mp
     * @param what
     * @param extra
     * @return
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.d(TAG, " onError what=" + what + " extra=" + extra);
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
//                playVideo();
//                Toast.makeText(this, "MEDIA_ERROR_UNKNOWN", Toast.LENGTH_SHORT).show();
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
//                Toast.makeText(this, "MEDIA_ERROR_SERVER_DIED", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        switch (extra) {
            case MediaPlayer.MEDIA_ERROR_IO:
//                Toast.makeText(this, "MEDIA_ERROR_IO", Toast.LENGTH_SHORT).show();
                break;
            case MediaPlayer.MEDIA_ERROR_MALFORMED:
//                Toast.makeText(this, "MEDIA_ERROR_MALFORMED", Toast.LENGTH_SHORT).show();
                break;
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
//                Toast.makeText(this, "MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK",
//                        Toast.LENGTH_SHORT).show();
                break;
            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
//                Toast.makeText(this, "MEDIA_ERROR_TIMED_OUT", Toast.LENGTH_SHORT).show();
                break;
            case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
//                Toast.makeText(this, "MEDIA_ERROR_UNSUPPORTED", Toast.LENGTH_SHORT).show();
                break;
        }
        if (!isDestroyed() && what != -38) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    retryPlay();
                }
            }, 3000);
        }
//        finish();
        return true;
    }

    private void retryPlay() {
        retryTimes++;
        if (retryTimes < 4) {
            releaseMediaPlayer();
            playVideo();
            ToastHelper.showToast("播放失败正在重试");
        } else {
            ToastHelper.showToast("无法播放,请稍候再试");
            finish();
        }

    }

    int retryTimes = 0;

    /**
     * 从暂停中恢复
     */
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Log.d(TAG, "onResume playPosition=" + Common.playPosition);
        // 判断播放位置
        if (Common.playPosition >= 0) {
            if (null != mediaPlayer) {

                if (isBtnPlay) {
                    Log.d(TAG, "退后台前是播放,需要恢复播放");
                    seekBarAutoFlag = true;
                    mediaPlayer.seekTo(Common.playPosition);
                    mediaPlayer.start();
                } else {
                    Log.d(TAG, "退后台前是暂停, 不需要恢复播放");
                }

//                showCenterLoading();
            } else {
                playVideo();
            }

        }

        if (isChatMessagePanelShow && mChatMessagePanel != null) {
            mChatMessagePanel.refreshChat();
        }
    }

    /**
     * 页面处于暂停状态
     */
    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (null != mediaPlayer && mediaPlayer.isPlaying()) {
                Common.playPosition = mediaPlayer.getCurrentPosition();
                mediaPlayer.pause();
                seekBarAutoFlag = false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * 发生屏幕旋转时调用
     */
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
//        if (null != mediaPlayer) {
//            // 保存播放位置
//            Common.playPosition = mediaPlayer.getCurrentPosition();
//        }
    }

    /**
     * 屏幕旋转完成时调用
     */
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onRestoreInstanceState(savedInstanceState);

    }

    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 屏幕销毁时调用
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 由于MediaPlay非常占用资源，所以建议屏幕当前activity销毁时，则直接销毁
        releaseMediaPlayer();
        if (giftAnimManger != null)
            giftAnimManger.clearAllAnima();

        if (roomManger != null) {
            roomManger.removeResultListener(roomListener);
//            roomManger.stopRoom();
        }
    }

    private void releaseMediaPlayer() {
        try {
            if (null != SeeReplayActivityBackup.this.mediaPlayer) {
                // 提前标志为false,防止在视频停止时，线程仍在运行。
                seekBarAutoFlag = false;
                // 如果正在播放，则停止。
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                Common.playPosition = -1;
                // 释放mediaPlayer
                SeeReplayActivityBackup.this.mediaPlayer.release();
                SeeReplayActivityBackup.this.mediaPlayer = null;
            }
            Log.d(TAG, "onDestroy 资源释放成功");
        } catch (Exception e) {
            /*StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));*/
            Log.error(TAG, "onDestroy 报错:", e);

        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    /**
     * 转换播放时间
     *
     * @param milliseconds 传入毫秒值
     * @return 返回 hh:mm:ss或mm:ss格式的数据
     */
    @SuppressLint("SimpleDateFormat")
    public String getShowTime(long milliseconds) {
        // 获取日历函数
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        SimpleDateFormat dateFormat = null;
        // 判断是否大于60分钟，如果大于就显示小时。设置日期格式
        if (milliseconds / 60000 > 60) {
            dateFormat = new SimpleDateFormat("hh:mm:ss");
        } else {
            dateFormat = new SimpleDateFormat("mm:ss");
        }
        return dateFormat.format(calendar.getTime());
    }

    /**
     * 改变视频的显示大小，全屏，窗口，内容
     */
    public void changeVideoSize() {
        // 改变视频大小
        String videoSizeString = videoSizeButton.getText().toString();
        // 获取视频的宽度和高度
        int width = mediaPlayer.getVideoWidth();
        int height = mediaPlayer.getVideoHeight();
        // 如果按钮文字为窗口则设置为窗口模式
        if ("窗口".equals(videoSizeString)) {
            /*
             * 如果为全屏模式则改为适应内容的，前提是视频宽高小于屏幕宽高，如果大于宽高 我们要做缩放
             * 如果视频的宽高度有一方不满足我们就要进行缩放. 如果视频的大小都满足就直接设置并居中显示。
             */
            if (width > screenWidth || height > screenHeight) {
                // 计算出宽高的倍数
                float vWidth = (float) width / (float) screenWidth;
                float vHeight = (float) height / (float) screenHeight;
                // 获取最大的倍数值，按大数值进行缩放
                float max = Math.max(vWidth, vHeight);
                // 计算出缩放大小,取接近的正值
                width = (int) Math.ceil((float) width / max);
                height = (int) Math.ceil((float) height / max);
            }
            // 设置SurfaceView的大小并居中显示
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width,
                    height);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            surfaceView.setLayoutParams(layoutParams);
            videoSizeButton.setText("全屏");
        } else if ("全屏".equals(videoSizeString)) {
            // 设置全屏
            // 设置SurfaceView的大小并居中显示
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(screenWidth,
                    screenHeight);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            surfaceView.setLayoutParams(layoutParams);
            videoSizeButton.setText("窗口");
        }
    }


    private void getZhuboUserInfo() {
        DataProvider.getUserInfo(this, userId, true, new GsonHttpConnection.OnResultListener<UserMsg>() {
            @Override
            public void onSuccess(UserMsg userMsg) {
                if (userMsg.isSuccessFul()) {
                    UserInfo info = userMsg.getUserInfo();
                    if (info != null) {
                        zhuboUserInfo = info;
//                        otherUserId = zhuboUserInfo.getIdStr();
                        otherNickName = zhuboUserInfo.getNickName();
                        otherAvatar = zhuboUserInfo.getAvatar();
//                        if (popZhubo != null) {
//                            refreshPopUI();
//                        }
                        ImageUtil.loadImage(ivZhuboHead, zhuboUserInfo.getAvatar());
                        Log.d(TAG, " zhuboUserInfo getAvatar=" + zhuboUserInfo.getAvatar());
                        int markId = MarkSimpleDraweeView.getMarkDrawableId(MarkSimpleDraweeView.getAuthType(zhuboUserInfo.getAuth()),
                                MarkSimpleDraweeView.SizeType.SMALL, zhuboUserInfo.getLevel());
                        ivZhuboHead.setMark(markId);

                        if (zhuboUserInfo.getFollow() == ListUserInfo.FOLLOWED) {
                            btnFollow.setVisibility(View.GONE);
                        }
                        tvLakaNo.setText(getString(R.string.laka_no) + String.valueOf(zhuboUserInfo.getId()));
                        tvKazuan.setText(String.valueOf(zhuboUserInfo.getTotalCoins()));
                    }
                }
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {

            }
        });
    }


    private LiveRoomUserInfoPanel mUserInfoPanel;

    private void showUserInfoPanel(String userId, String userHead) {
        if (!isResume()) {
            return;
        }
        if (mUserInfoPanel == null) {
            mUserInfoPanel = new LiveRoomUserInfoPanel(this, this, mCourseId);
            mUserInfoPanel.setLivingHostId(userId);
            mUserInfoPanel.setRoomId(userId);
        }
        mUserInfoPanel.setIsReplayLive(true);
        mUserInfoPanel.showPanel(userId, userHead);

    }

    private void follow() {
        if (zhuboUserInfo == null) {
            return;
        }
        DataProvider.follow(this, zhuboUserInfo.getId(), new GsonHttpConnection.OnResultListener<Msg>() {
            @Override
            public void onSuccess(Msg msg) {
                zhuboUserInfo.setFollow(ListUserInfo.FOLLOWED);
                btnFollow.setVisibility(View.GONE);
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                showToast("关注失败");
            }
        });
    }

    @Subscribe
    public void onEvent(final PostEvent event) {
        super.onEvent(event);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                if(SubcriberTag.ROOM_SHOW_COMMENT.equals(event.tag)){
//                    boolean isShow = (boolean) event.event;
//                    rlComment.setVisibility(isShow?View.VISIBLE:View.GONE);
//                } else
                if (SubcriberTag.REFRESH_LAST_KAZUAN.equals(event.tag)) {
                    double result = (double) event.event;
                    if (giftGridView != null) {
                        giftGridView.setBalance(result);
                    }
                    AccountInfoManager.getInstance().updateCurrentAccountCoins(result);
                } else if (SubcriberTag.SEND_CHAT_GIFT_SUCCESS.equals(event.tag)) {
                    int reqGift = (int) event.event;
                    ChatMsg curGiftMsg = sendingGifts.get(reqGift);
                    if (curGiftMsg != null) {
                        saveMessage(curGiftMsg, false);
                        BytesReader.GiftMessage message = new BytesReader().getGiftMessage();
                        message.audienceID = AccountInfoManager.getInstance().getCurrentAccountUserId() + "";
                        message.count = 1;
                        message.giftID = curGiftMsg.getGiftId() + "";
                        message.nickName = AccountInfoManager.getInstance().getCurrentAccountNickName();
                        //处理礼物动画区显示
                        giftShowView.showGift(message);
                        giftAnimManger.addGift(message);
                    }
                } else if (SubcriberTag.SOCKET_NOT_CONNECT.equals(event.tag)) {
                    if (isResume()) {
                        showToast(R.string.service_not_connect);
                    }
                } else if (SubcriberTag.SHOW_MESSAGE_PANEL_CHAT.equals(event.tag)) {
                    ChatSession session = (ChatSession) event.event;
                    showMessagePanel(session);
                }
            }
        });

    }

    private void showMessagePanel(ChatSession session) {

        if (mChatSessionPanel != null) {
            mChatSessionPanel.hidePanel();
        }
        if (mChatUnfollowPanel != null) {
            mChatUnfollowPanel.hidePanel();
        }

        showMessagePanel(session.getUserId(), session.getNickName(), session.getAvatar(), session.getType());
        //mChatMessagePanel.isFromSession = isFromSession;
        AnalyticsReport.onEvent(mContext, LiveReport.MY_LIVE_EVENT_15006);
//        if (!isCreater) {
//            EventBusManager.postEvent(false, SubcriberTag.ROOM_CAN_SCROLL);
//        }

    }

    private void showMessagePanel(String userId, String nickName, String avatar, int sessionType) {

        if (mChatMessagePanel == null) {
            try {
                initChatMessagePanel();
            } catch (Exception e) {
                Log.log("showMessagePanel:" + e.toString());
            }
        }

        mChatMessagePanel.setVisibility(View.VISIBLE);
        mChatMessagePanel.start();
        mChatMessagePanel.initData(mContext, userId, nickName, avatar, sessionType);
    }

    private void initChatMessagePanel() {
        mChatMessagePanel = (ChatMessageView) findViewById(R.id.message_view);
        View bgView = new View(mContext);
        ChatMessageView.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.addRule(RelativeLayout.ABOVE, R.id.chat_message_layout);
        mChatMessagePanel.addView(bgView, 0, params);
        bgView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideChatMessagePanel();
            }
        });
    }

    private void hideChatMessagePanel() {
        mChatMessagePanel.setVisibility(View.GONE);
        mChatMessagePanel.stop();
        isChatMessagePanelShow = false;
    }

    private void showSessionPanel() {
        if (mChatSessionPanel == null) {
            initChatSessionPanel();
        }
        mChatSessionPanel.showPanel(mContext);
    }

    private void initChatSessionPanel() {
        mChatSessionPanel = new ChatSessionPanel(mContext);
    }


    private void initChatUnfollowPanel() {
        mChatUnfollowPanel = new ChatUnfollowPanel(mContext);
    }

//    @Subscriber(tag = SubcriberTag.REFRESH_LAST_KAZUAN)
//    private void eventRefreshLastKazuan(int result) {
//        if(giftGridView!=null){
//            giftGridView.setKazuanCnt(result);
//        }
//        UserInfo mSelfUserInfo = LiveApplication.getInstance().getMyselfUserInfo();
//        if(mSelfUserInfo!=null){
//            mSelfUserInfo.setCoins(result);
//        }
//    }

    Handler handler = new Handler() {

    };

    private void showCenterLoading() {
        if (rlLoading.getVisibility() == View.GONE) {
            rlLoading.startRoll();
            rlLoading.setVisibility(View.VISIBLE);
//            Log.d(TAG, "打开动画");
//            mLoadingImageView.start();
        }
    }

    private void hideCenterLoading() {
        if (rlLoading.getVisibility() == View.VISIBLE) {
            rlLoading.setVisibility(View.GONE);
            rlLoading.stopRoll();
//            mLoadingImageView.stop();
        }
    }


}
