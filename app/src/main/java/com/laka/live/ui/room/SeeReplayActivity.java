package com.laka.live.ui.room;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.media.PlaybackParams;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.account.my.ContributionListActivity;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.application.LiveApplication;
import com.laka.live.bean.Course;
import com.laka.live.bean.GiftInfo;
import com.laka.live.bean.ListUserInfo;
import com.laka.live.bean.Speed;
import com.laka.live.bean.UserInfo;
import com.laka.live.bean.VideoLine;
import com.laka.live.dao.DbManger;
import com.laka.live.gift.GiftAudioManager;
import com.laka.live.gift.GiftResManager;
import com.laka.live.help.NoDoubleClickManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.manager.BytesReader;
import com.laka.live.manager.OnResultListenerAdapter;
import com.laka.live.manager.RoomManager;
import com.laka.live.msg.CourseDetailMsg;
import com.laka.live.msg.FormulaMsg;
import com.laka.live.msg.Msg;
import com.laka.live.msg.UserMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.receiver.NetWorkChangeReceiver;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsDetailBean;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.chat.ChatMessageActivity;
import com.laka.live.ui.chat.ChatMessageView;
import com.laka.live.ui.chat.ChatSessionPanel;
import com.laka.live.ui.chat.ChatUnfollowPanel;
import com.laka.live.ui.chat.FormulaPanel;
import com.laka.live.ui.rankinglist.RankingItemView;
import com.laka.live.ui.widget.ErrorDialog;
import com.laka.live.ui.widget.MarkSimpleDraweeView;
import com.laka.live.ui.widget.gift.GiftAnimManger;
import com.laka.live.ui.widget.gift.GiftGridView;
import com.laka.live.ui.widget.gift.GiftGridViewLand;
import com.laka.live.ui.widget.gift.GiftShowView;
import com.laka.live.ui.widget.room.LiveRoomUserInfoPanel;
import com.laka.live.util.Common;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.NetworkUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.UiPreference;
import com.laka.live.util.Utils;
import com.laka.live.video.mediaplayer.JZExoPlayer;
import com.laka.live.player.MyExoPlayerPlus;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.jzvd.JZDataSource;
import cn.jzvd.JZMediaManager;
import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdMgr;
import framework.ioc.annotation.InjectView;
import laka.live.bean.ChatMsg;
import laka.live.bean.ChatSession;

/**
 * Created by luwies on 16/3/8.
 * 课程回放
 */
public class SeeReplayActivity extends BaseActivity implements
        OnClickListener {

    private final static String TAG = "SeeReplayActivity";

    @InjectView(id = R.id.exo_player)
    public MyExoPlayerPlus exoPlayer;
    @InjectView(id = R.id.rl_player)
    public RelativeLayout rlPlayer;
    public boolean isRotationVideo = false;

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
    private Button btnPlay, btnFormula, btnShare, btnLetter, btnGift, btnFollow, btnOrientation;
    private ImageView btnClose;
    private TextView tvKazuan, tvAudienceCntNow, tvLakaNo;
    private RelativeLayout llLiveTopBar;
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
    private GiftGridView giftGridView; // 竖屏的礼物区
    private GiftGridViewLand giftGridViewLand; // 横屏的礼物区
    private GiftInfo chooseGift;
    private TextView tvZhiboLive;
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
//    private ScreenLoadingView rlLoading;
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
    //    private SensorManager mSensorManager;
    private boolean isMine = false;
    private FrameLayout mRootView;
    private RelativeLayout roomView;
    private int type;
    private boolean isHideView = false;//是否隐藏view
    private Timer hideTimer;
    private SeekBar sbProgress;
    private LinearLayout llBottom, llTime;
    private RelativeLayout rlInfo;
    private RelativeLayout rlDialog;
    TextView tvDialogTitle, tvDialogContent;
    Button btnDialogNo, btnDialogYes;
    private RelativeLayout rlBg;
    private View vLoading;
    private Button btnGoods, btnLines;
    private GoodsPanel goodsPanel;
    private TextView tvLinesTips;
    //    private GoodsBuyPanel mGoodsBuyPanel;
    private ShoppingGoodsDetailBean mDetailBean;
    private RecyclerView rcvLines;
    private VideoLinesAdapter videoLinesAdapter;
    private List<Speed> videoSpeeds;
    private int curVideoProgress;//记录切换线路前进度
    public boolean isNeedChangeScreen = false;
    private boolean isPlaying = false;

    /**
     * description:网络监听
     **/
    private NetWorkChangeReceiver netWorkChangeReceiver;
    private boolean isRegister = false;

    public static void startActivity(Context context, String courseId, String videoUrl,
                                     String userId,
                                     int views,
                                     int recvCoins,
                                     String rollBackId, int type) {
        if (TextUtils.isEmpty(videoUrl) || TextUtils.isEmpty(userId)) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.error_data_tip));
            return;
        }

        if (!NoDoubleClickManager.isNoDoubleClick(NoDoubleClickManager.TYPE_ENTER_ZHIBO_ROOM)) {
            return;
        }

        if (context != null) {
            Intent intent = new Intent(context, SeeReplayActivity.class);
            intent.putExtra("courseId", courseId);
            intent.putExtra("videoUrl", videoUrl);
            intent.putExtra("userId", userId);
            intent.putExtra("views", views);
            intent.putExtra("recvCoins", recvCoins);
            intent.putExtra("rollBackId", rollBackId);
            intent.putExtra("type", type);
            context.startActivity(intent);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, " onNewIntent");
        handleParams(intent);
        setPlay();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo
                .SCREEN_ORIENTATION_PORTRAIT);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        setContentView(R.layout.activity_see_replay);
        setSwipeBackEnable(false);

        handleParams(getIntent());
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
        getSeeCount();
        setPlay();
        initNetWorkChangeListener();
    }

    private void handleParams(Intent intent) {
        videoUrl = intent.getStringExtra("videoUrl");
        /**
         * description:测试URL
         **/
//        videoUrl = "http://video.zwlive.lakatv.com/cc/mp4/1806/upload/ntaXawrXIxj1532092006.mp4";
        userId = intent.getStringExtra("userId");
        views = intent.getIntExtra("views", 0);
        recvCoins = intent.getIntExtra("recvCoins", 0);
        rollBackId = intent.getStringExtra("rollBackId");
        mCourseId = intent.getStringExtra("courseId");
        type = intent.getIntExtra("type", 1);

        if (type == 1) {
            isRotationVideo = false;
        } else if (type == 2) {
            isRotationVideo = true;
        }
        Log.d(TAG, " type=" + type + " isRotationVideo=" + isRotationVideo);
        if (userId.equals(AccountInfoManager.getInstance().getCurrentAccountUserIdStr())) {
            isMine = true;
        }

        Common.playPosition = -1;
        isFirstCreate = true;
        if (videoUrl == null || videoUrl.isEmpty()) {
            showToast("回放地址不存在");
            finish();
            return;
        } else {
        }

//        videoPlayer.setUp(videoUrl,false);
//        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);
//        videoPlayer.startPlayLogic();


    }

    int curSpeed = 0;

    private void startHideTimer() {
        rlBg.setVisibility(View.GONE);
        vLoading.setVisibility(View.GONE);
        Log.d(TAG, " startHideTimer");
        if (hideTimer != null) {
            hideTimer.cancel();
            hideTimer = null;
        }
        hideTimer = new Timer();
        hideTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, 3000);


        boolean isShowLinesTips = UiPreference.getBoolean(Common.KEY_IS_SHOW_LINES_TIPS, true);
        if (isShowLinesTips) {
            tvLinesTips.setVisibility(View.VISIBLE);
            UiPreference.putBoolean(Common.KEY_IS_SHOW_LINES_TIPS, false);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvLinesTips.setVisibility(View.GONE);
                }
            }, 3000);
        }
    }

    private void hideController() {
        Log.d(TAG, " hideController");
        sbProgress.setVisibility(View.GONE);
        llBottom.setVisibility(View.GONE);
        llTime.setVisibility(View.GONE);
        rlInfo.setVisibility(View.GONE);
        tvSpeed.setVisibility(View.GONE);
        rcvLines.setVisibility(View.GONE);
    }

    private void showController() {
        sbProgress.setVisibility(View.VISIBLE);
        llBottom.setVisibility(View.VISIBLE);
        llTime.setVisibility(View.VISIBLE);
        rlInfo.setVisibility(View.VISIBLE);
        tvSpeed.setVisibility(View.VISIBLE);
        startHideTimer();
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

            // 解决网络切换引起视频重新播放的问题
            if (exoPlayer != null && exoPlayer.getCurrentPositionWhenPlaying() > 0) {
                if (!NetworkUtil.isNetworkOk(SeeReplayActivity.this)) {
                    if (exoPlayer != null) {
                        exoPlayer.onClickPlayButton();
                        exoPlayer.showNoNetWork();
                    }
                }
                return;
            }

            //恢复播放
            if (isPlaying) {
                exoPlayer.startPlayLogic();
            }
        }

        @Override
        public void chatDidDisconnect() {
            insertMessage("Socket断开成功");
        }

        @Override
        public void chatErrorOccur(int errcode, final String errMsg) {
            insertMessage("Socket errcode=" + errcode + " errMsg=" + errMsg);
            if (errcode == RoomManager.CHAT_ERROR_SERVER_DISCONNECT) {
                SeeReplayActivity.this.runOnUiThread(new Runnable() {
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
        getCourseDetail();


    }

    private List<VideoLine> videoLines;
    private int currLine = -1;
    private int playErrorTime = 0;
    private ErrorDialog errorDialog;

    private Course mCourse;
    private TextView tvSpeed;

    private void setPlay() {
        rlBg.setVisibility(View.VISIBLE);
        vLoading.setVisibility(View.VISIBLE);
        llLiveTopBar = (RelativeLayout) findViewById(R.id.ll_topbar_zhubo);
        llLiveTopBar.setOnClickListener(this);
//        videoUrl = "http://oss.aliyun.lakatv.com/record/lakatv/295.mp4";
//        videoUrl = "http://video.zwlive.lakatv.com/1705/u66685001eb9b32a.MOV";
        Logger.i(TAG, " 播放url=" + videoUrl + " DEVICE=" + Build.DEVICE
                + " HARDWARE=" + Build.HARDWARE + " FINGERPRINT=" + Build.FINGERPRINT
                + " BOARD=" + Build.BOARD + " MODEL=" + Build.MODEL);
        JZUtils.clearSavedProgress(this, videoUrl);

        exoPlayer.setVisibility(View.VISIBLE);
        Logger.d(TAG, "使用exoPlayer curVideoProgress=" + curVideoProgress);
        if (curVideoProgress > 0) {
            exoPlayer.seekToInAdvance = curVideoProgress;
            curVideoProgress = 0;
        } else {
            exoPlayer.seekToInAdvance = -1;
        }
        exoPlayer.isReply = true;
        // exoPlayer.loop = true;
        exoPlayer.widthRatio = screenWidth;
        exoPlayer.heightRatio = screenHeight;
        exoPlayer.progressBar = sbProgress;
        exoPlayer.progressBar.setOnSeekBarChangeListener(exoPlayer);
        exoPlayer.currentTimeTextView = (TextView) findViewById(R.id.my_current);
        exoPlayer.totalTimeTextView = (TextView) findViewById(R.id.my_total);
        JZDataSource jzDataSource = new JZDataSource(videoUrl, "");
        jzDataSource.looping = true; //循环播放
        exoPlayer.setUp(jzDataSource, Jzvd.SCREEN_WINDOW_NORMAL);
        exoPlayer.startPlayLogic();
        exoPlayer.setPlayListener(new MyExoPlayerPlus.PlayListener() {
            @Override
            public void onPlayerPlay() {
                resumeSpeed();
                Logger.d(TAG, " onPlayerPlay ");
                startHideTimer();
                btnPlay.setBackgroundResource(R.drawable.live_btn_stop_selector);
                isPlaying = true;
                if (isNeedChangeScreen) {
                    isNeedChangeScreen = false;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }

            @Override
            public void onPlayerPause() {
                Logger.d(TAG, " onPlayerPause ");
                isPlaying = false;
                btnPlay.setBackgroundResource(R.drawable.live_btn_play_selector);
            }

            @Override
            public void onPlayerFreeze(boolean isFreeze) {
                if (vLoading != null) {
                    if (isFreeze) {
                        if (vLoading.getVisibility() == View.GONE) {
                            vLoading.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (vLoading.getVisibility() == View.VISIBLE) {
                            vLoading.setVisibility(View.GONE);
                        }
                    }
                }
                Logger.e("当前视频卡帧：" + isFreeze);
            }

            @Override
            public void onPlayerError(int time) {
                Logger.d(TAG, " onPlayerError ");
                //后期完善----播放错误，回调切换线路
                //切换线路播放
                curVideoProgress = time;
                handleErrorVideo(videoUrl);
            }

            @Override
            public void onPlayerProgress(int progress, int secProgress, int currentTime, int totalTime) {

            }

        });
    }

    private void resumeSpeed() {
        if (speedItem != null) {
            float speed = speedItem.getRate();
            Log.d(TAG, " 恢复速度speed=" + speed);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PlaybackParams pp = new PlaybackParams();
                pp.setSpeed(speed);
                JZExoPlayer jzExoPlayer = (JZExoPlayer) JZMediaManager.instance().jzMediaInterface;
                jzExoPlayer.getSimpleExoPlayer().setPlaybackParams(pp);
            }
        }
    }

    private void refreshRommDataUI() {
        tvAudienceCntNow.setText(String.valueOf(views));
        tvKazuan.setText(String.valueOf(recvCoins));
//        CacheUtil.addCache("totalCoins", roomCoins);
//        CacheUtil.addCache("totalAudience", roomAudience);
    }

    private void getCourseDetail() {
        DataProvider.getCourseDetail(this, mCourseId, new GsonHttpConnection.OnResultListener<CourseDetailMsg>() {

            @Override
            public void onSuccess(CourseDetailMsg msg) {

                if (msg == null || msg.data == null || msg.data.course == null) {
                    mContext.showToast("课程数据异常!");
                    mContext.finish();
                } else {
                    mCourse = msg.data.getCourse();
                    videoLines = mCourse.getVideoLines();
                    if (!Utils.listIsNullOrEmpty(videoLines)) {
                        videoLines.get(0).isSelected = true;
//                        for (int i = 0; i < videoLines.size(); i++) {
//                            VideoLine item = videoLines.get(i);
//                            if (item.getUrl().equals(videoUrl)) {
//                                item.setSelected(true);
//                                Logger.i("获取视频lines,选中Item为：" + i + "\n数据为" + item);
//                                break;
//                            }
//                        }
                        btnLines.setVisibility(View.VISIBLE);
                        VideoLinesAdapter adapter = new VideoLinesAdapter(SeeReplayActivity.this, videoLines);
                        rcvLines.setAdapter(adapter);
                    }

                    mContext.dismissDialog();

                    videoSpeeds = mCourse.getSpeeds();
                }
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                mContext.showToast(errorMsg);
                mContext.dismissDialog();
            }

        });
    }

    // 获取配方做法
    private void getFormula() {

        DataProvider.getFormula(this, mCourseId, new GsonHttpConnection.OnResultListener<FormulaMsg>() {
            @Override
            public void onSuccess(FormulaMsg msg) {
                mFormulaMsg = msg;
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
            }

        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (exoPlayer.currentState == MyExoPlayerPlus.CURRENT_STATE_PLAYING) {
            onClick(btnPlay);
        }
    }

    public void getSeeCount() {
        // 统计观看数
        DataProvider.postSeePlayCount(this, mCourseId, new GsonHttpConnection.OnResultListener<FormulaMsg>() {
            @Override
            public void onSuccess(FormulaMsg formulaMsg) {

                try {
                    views = Integer.parseInt(formulaMsg.views);
                } catch (Exception e) {
                    views = 0;
                }

                tvAudienceCntNow.setText(String.valueOf(views));

            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                Log.log("onFail:" + errorMsg);
            }
        });
    }

    /**
     * 播放视频
     */
    public void playVideo() {

    }

    boolean isPortrait = true;

    //    MyOrientoinListener myOrientoinListener;
//    SensorEventListener sensorListener;
    public void initViews() {
        tvSpeed = (TextView) findViewById(R.id.tv_speed);
        tvSpeed.setOnClickListener(this);
        rcvLines = (RecyclerView) findViewById(R.id.rcv_lines);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        rcvLines.setLayoutManager(layoutManager);
        rcvLines.addItemDecoration(new SpaceItemDecoration());
        rcvLines.setVisibility(View.GONE);
        goodsPanel = (GoodsPanel) findViewById(R.id.goods_panel);
        goodsPanel.initData(this, mCourseId);
        tvLinesTips = (TextView) findViewById(R.id.tv_lines_tips);
        tvLinesTips.setVisibility(View.GONE);
        btnLines = (Button) findViewById(R.id.btn_lines); // 切换线路
        btnLines.setVisibility(View.GONE);
        btnLines.setOnClickListener(this);
        btnGoods = (Button) findViewById(R.id.btn_goods);
        btnGoods.setOnClickListener(this);
        rlBg = (RelativeLayout) findViewById(R.id.rl_bg);
        vLoading = findViewById(R.id.loading_view);
        rlDialog = (RelativeLayout) findViewById(R.id.rl_dialog);
        tvDialogTitle = (TextView) rlDialog.findViewById(R.id.tv_dialog_title);
        tvDialogContent = (TextView) rlDialog.findViewById(R.id.tv_dialog_content);
        btnDialogYes = (Button) rlDialog.findViewById(R.id.btn_dialog_yes);
        btnDialogNo = (Button) rlDialog.findViewById(R.id.btn_dialog_no);
        btnDialogNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlDialog.setVisibility(View.GONE);
            }
        });

        sbProgress = (SeekBar) findViewById(R.id.my_progress);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        llTime = (LinearLayout) findViewById(R.id.ll_time);
        roomView = (RelativeLayout) findViewById(R.id.rl_room);
        rlInfo = (RelativeLayout) findViewById(R.id.rl_time_info);
        tvAudienceCntNow = (TextView) findViewById(R.id.tv_audience_cnt_now);
        tvKazuan = (TextView) findViewById(R.id.tv_kazuan);
        tvLakaNo = (TextView) findViewById(R.id.tv_lakano);
        mRootView = (FrameLayout) findViewById(R.id.root_view);
        giftAnimManger = new GiftAnimManger(this, roomView);//mRootView
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

        giftGridViewLand = (GiftGridViewLand) findViewById(R.id.gift_grid_view_land);
        giftGridViewLand.setOnClickListener(this);
        giftGridViewLand.setListeners(this, new GiftGridViewLand.GiftGridListener() {
            @Override
            public void setChooseGift(GiftInfo gift) {
                chooseGift = gift;
            }
        }, true);
        giftGridViewLand.setVisibility(View.GONE);

        mSelfUserInfo = AccountInfoManager.getInstance().getAccountInfo();
//      giftGridView.setKazuanCnt(mSelfUserInfo.getCoins());
        giftShowView = (GiftShowView) findViewById(R.id.gift_show_view);
        rlTouch = (RelativeLayout) findViewById(R.id.rl_touch);
        rlTouch.setOnClickListener(this);
        tvZhiboLive = (TextView) findViewById(R.id.tv_zhibo_live);
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
        btnOrientation = (Button) findViewById(R.id.btn_orientation);
        btnOrientation.setOnClickListener(this);
        btnGift = (Button) findViewById(R.id.btn_gift);
        btnGift.setOnClickListener(this);
        if (isMine) {
            btnGift.setVisibility(View.GONE);
        }
        btnClose = (ImageView) findViewById(R.id.close_btn);
        btnClose.setOnClickListener(this);
        btnFollow = (Button) findViewById(R.id.btn_follow);
//        if (TextUtils.equals(userId, AccountInfoManager.getInstance().getCurrentAccountUserIdStr())) {
//            btnFollow.setVisibility(View.GONE);
//        } else {
//            btnFollow.setOnClickListener(this);
//        }
        rlTopBarZhubo = (RelativeLayout) findViewById(R.id.rl_info_zhubo);
        rlTopBarZhubo.setVisibility(View.GONE);
        rlTopBarAudience = (RelativeLayout) findViewById(R.id.rl_info_audience);
        rlTopBarAudience.setVisibility(View.VISIBLE);
        rlKazuanBank = (RelativeLayout) findViewById(R.id.rl_kazuan);
        rlKazuanBank.setOnClickListener(this);


//        showCenterLoading();
        initErrorDialog();
    }

    private void initErrorDialog() {
        errorDialog = new ErrorDialog(this);
        errorDialog.setTitle("播放异常");
        errorDialog.setContent("当前视频播放异常，请重试或联系客服人员进行反馈");
        errorDialog.enablePositive(false);
        errorDialog.setOnDialogClickListener(new ErrorDialog.OnDialogClickListener() {
            @Override
            public void onClick(boolean isPositive) {
                SeeReplayActivity.this.finish();
            }
        });
    }

    private void showLinesPop() {
        if (mCourse == null) {
            showToast("正在获取线路，请稍候");
            getCourseDetail();
            return;
        }

        if (Utils.listIsNullOrEmpty(videoLines)) {
            showToast("没有更多线路");
            return;
        } else {
            Log.d(TAG, " 线路数量=" + videoLines.size());
        }

        rcvLines.setVisibility(View.VISIBLE);
    }

    private void showGoodsPanel() {
        if (!isPortrait) {
            onClick(btnOrientation);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    changeVideoHeight();
                }
            }, 200);
        } else {
            changeVideoHeight();
        }

        Log.d(TAG, " showGoodsPanel");

    }

    private void changeVideoHeight() {
        if (goodsPanel.getVisibility() == View.GONE) {
            goodsPanel.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) rlPlayer.getLayoutParams();
            lp.height = Utils.getScreenHeight(this) * 1 / 3;
            rlPlayer.setLayoutParams(lp);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) exoPlayer.getLayoutParams();
            params.height = Utils.getScreenHeight(this) * 1 / 3;
            exoPlayer.heightRatio = params.height;
            exoPlayer.setLayoutParams(params);
            Log.d(TAG, " exoPlayer height=" + params.height);
        }
    }

    private void hideGoodsPanel() {
        Log.d(TAG, " hideGoodsPanel");
        if (goodsPanel.getVisibility() == View.VISIBLE) {
            goodsPanel.setVisibility(View.GONE);
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) rlPlayer.getLayoutParams();
            lp.height = Utils.getScreenHeight(this);
            rlPlayer.setLayoutParams(lp);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) exoPlayer.getLayoutParams();
            params.height = Utils.getScreenHeight(this);
            exoPlayer.heightRatio = params.height;
            exoPlayer.setLayoutParams(params);
            Log.d(TAG, " exoPlayer height=" + params.height);
        }
    }

    private String getNextVideoUrl(String videoUrl, List<VideoLine> videoLines) {
        int currLine = 0;
        playErrorTime++;
        if (TextUtils.isEmpty(videoUrl)) {
            return null;
        }

        if (Utils.isEmpty(videoLines)) {
            return null;
        }

        for (int i = 0; i < videoLines.size(); i++) {
            VideoLine line = videoLines.get(i);
            if (line.getUrl().equals(videoUrl)) {
                line.setSelected(false);
                currLine = i + 1;
            }
        }

        if (currLine >= videoLines.size()) {
            currLine = 0;
        }

        videoLines.get(currLine).setSelected(true);
        return videoLines.get(currLine).getUrl();
    }

    /**
     * 处理错误URL播放
     *
     * @param videoUrl
     */
    private void handleErrorVideo(String videoUrl) {
        if (TextUtils.isEmpty(videoUrl)) {
            return;
        }

        if (!Utils.isEmpty(videoLines)) {
            if (playErrorTime < videoLines.size()) {
                String nextUrl = getNextVideoUrl(videoUrl, videoLines);
                if (TextUtils.isEmpty(nextUrl) || exoPlayer == null) {
                    showToast("当前线路播放异常");
                    return;
                }
                Logger.e("播放错误：" + videoUrl + "\nNextUrl：" + nextUrl +
                        "\nErrorTime：" + playErrorTime + "\nvideoLines：" + videoLines.size());
                this.videoUrl = nextUrl;
                releasePlayer();
                onClick(btnPlay);
                setPlay();
            } else {
                playErrorTime = 0;
                if (!this.isFinishing() && !this.isDestroyed()) {
                    errorDialog.show();
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && goodsPanel.getVisibility() == View.VISIBLE) {
            hideGoodsPanel();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showFormulaPanel() {
        Log.d(TAG, "showFormulaPanel isPortrait : " + isPortrait);

//        if (mFormulaPanel == null)
        mFormulaPanel = new FormulaPanel(this, isPortrait, isRotationVideo);
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
        if (rcvLines.getVisibility() == View.VISIBLE) {
            rcvLines.setVisibility(View.GONE);
        }
        if (llBottom.getVisibility() == View.GONE) {
            showController();
        }

        if (giftGridView.getVisibility() == View.VISIBLE) {
            giftGridView.hide();
        }

        if (giftGridViewLand.getVisibility() == View.VISIBLE) {
            giftGridViewLand.hide();
        }

        if (goodsPanel.getVisibility() == View.VISIBLE) {
            hideGoodsPanel();
        }
    }

//    boolean isBtnPlay = true;


    /**
     * 屏幕销毁时调用
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, " onDestroy");
        //销毁时取消监听
//        myOrientoinListener.disable();
//        mSensorManager.unregisterListener(sensorListener);
        if (giftAnimManger != null)
            giftAnimManger.clearAllAnima();

        if (roomManger != null) {
            roomManger.removeResultListener(roomListener);
//            roomManger.stopRoom();
        }
        releasePlayer();

        if (isRegister && netWorkChangeReceiver != null) {
            unregisterReceiver(netWorkChangeReceiver);
        }

        if (goodsPanel != null) {
            goodsPanel.destory();
        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            JzvdMgr.completeAll();
            JZMediaManager.instance().releaseMediaPlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
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
//                        int markId = MarkSimpleDraweeView.getMarkDrawableId(MarkSimpleDraweeView.getAuthType(zhuboUserInfo.getAuth()),
//                                MarkSimpleDraweeView.SizeType.SMALL, zhuboUserInfo.getLevel());
//                        ivZhuboHead.setMark(markId);

                        if (zhuboUserInfo.getFollow() == ListUserInfo.FOLLOWED) {
                            btnFollow.setVisibility(View.GONE);
                        }
                        String sName = zhuboUserInfo.getNickName();
                        Log.d(TAG, " sName length=" + sName.length());
                        if (!Utils.isEmpty(sName) && sName.length() > 20) {
                            sName = sName.substring(0, 20) + "...";
                        }
                        tvZhiboLive.setText(sName);
                        tvLakaNo.setText(getString(R.string.ziwei_id) + String.valueOf(zhuboUserInfo.getId()));
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

        if (userId.equals(AccountInfoManager.getInstance().getCurrentAccountUserIdStr())) {
            Log.d(TAG, " 自己的不弹出");
            return;
        }

        Log.d(TAG, " isPortrait=" + isPortrait + " isRotationVideo=" + isRotationVideo);
//        if (mUserInfoPanel == null) {
        mUserInfoPanel = new LiveRoomUserInfoPanel(this, this, mCourseId);
        mUserInfoPanel.setLivingHostId(userId);
        mUserInfoPanel.setRoomId(userId);
//        }
        mUserInfoPanel.setIsReplayLive(true);
        mUserInfoPanel.showPanel(userId, userHead);
        if (!isRotationVideo) {
            mUserInfoPanel.setRotation(isPortrait);
        } else {
            mUserInfoPanel.setRotation(true);
        }

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

    /**
     * 按钮点击事件监听
     */
    @Override
    public void onClick(View v) {
        Log.d(TAG, " onClick");
        switch (v.getId()) {
            case R.id.tv_speed:
                try {
                    changeSpeed();
                } catch (Exception e) {
                    showToast("请正常播放后再切换速度");
                }

                break;
            case R.id.btn_lines:
                showLinesPop();
                break;
            case R.id.btn_goods:
                showGoodsPanel();
                break;
            case R.id.btn_orientation:
                isPortrait = !isPortrait;
                Log.d(TAG, " onClick btn_orientation isPortrait=" + isPortrait);
                if (isPortrait) {//切换到竖屏
                    if (isRotationVideo) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        Log.d(TAG, " 需要转video 竖屏");
                    } else {
                        Log.d(TAG, " 不需要转video 竖屏");
                        btnGift.setVisibility(View.VISIBLE);
                        roomView.setRotation(0);
                        ViewGroup.LayoutParams lp = roomView.getLayoutParams();
                        lp.height = mRootView.getHeight();
                        lp.width = mRootView.getWidth();
                        roomView.setLayoutParams(lp);
                        roomView.setX(0);
                        roomView.setY(0);
                        Log.d(TAG, " 切换到竖屏  height=" + lp.height + " width=" + lp.width
                                + " x=" + roomView.getX() + " y=" + roomView.getY());
                    }
                } else {//切换到横屏
                    if (isRotationVideo) {
                        Log.d(TAG, " 需要转video 横屏");
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    } else {
                        Log.d(TAG, " 不需要转video 横屏");
//                        btnGift.setVisibility(View.GONE);
                        LiveApplication.screenHeight = roomView.getHeight();
                        LiveApplication.screenWidth = roomView.getWidth();
                        roomView.setRotation(90);
                        ViewGroup.LayoutParams lp = roomView.getLayoutParams();
                        lp.height = mRootView.getWidth();
                        lp.width = mRootView.getHeight();
                        roomView.setLayoutParams(lp);
                        roomView.setX(-(roomView.getHeight() - roomView.getWidth()) / 2);
                        roomView.setY((roomView.getHeight() - roomView.getWidth()) / 2);
                        Log.d(TAG, " 切换到横屏  height=" + lp.height + " width=" + lp.width
                                + " x=" + roomView.getX() + " y=" + roomView.getY());
                    }
                }
                roomView.invalidate();
                break;
            case R.id.btn_follow:
                follow();
                break;
            case R.id.btn_play:
                if (exoPlayer.currentState == MyExoPlayerPlus.CURRENT_STATE_PLAYING) {
                    Log.d(TAG, " ExoPlayer 暂停播放 state=");
                    isPlaying = false;
                    btnPlay.setBackgroundResource(R.drawable.live_btn_play_selector);
                } else {
                    Log.d(TAG, " ExoPlayer 恢复播放 state=");
                    isPlaying = true;
                    btnPlay.setBackgroundResource(R.drawable.live_btn_stop_selector);
                }
                exoPlayer.onClickPlayButton();
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
                if (isPortrait) {
                    if (Utils.listIsNullOrEmpty(giftGridView.giftList)) {
                        giftGridView.giftList = GiftResManager.getInstance().getGiftList();
                    }
                    if (Utils.listIsNullOrEmpty(giftGridView.giftList)) {
                        ToastHelper.showToast("获取礼物列表中,请稍候");
                        GiftResManager.getInstance().checkGiftUpdate();
                        return;
                    }
                    giftGridView.show();
                } else {
                    if (Utils.listIsNullOrEmpty(giftGridViewLand.giftList)) {
                        giftGridViewLand.giftList = GiftResManager.getInstance().getGiftList();
                    }
                    if (Utils.listIsNullOrEmpty(giftGridViewLand.giftList)) {
                        ToastHelper.showToast("获取礼物列表中,请稍候");
                        GiftResManager.getInstance().checkGiftUpdate();
                        return;
                    }
                    giftGridViewLand.show();
                }

                break;
            case R.id.close_btn:
                Log.d(TAG, " onclick close_btn");
                finish();
                break;
            case R.id.rl_touch:
                Log.d(TAG, " onclick rl_touch");
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
            default:
                break;
        }
    }

    private void showReportDialogLand(final String userId) {
        ViewGroup.LayoutParams lp = rlDialog.getLayoutParams();
        lp.height = mRootView.getWidth();
        lp.width = mRootView.getHeight();
        rlDialog.setLayoutParams(lp);
        int newX = -(mRootView.getHeight() - mRootView.getWidth()) / 2;
        int newY = (mRootView.getHeight() - mRootView.getWidth()) / 2;
        rlDialog.setX(-(mRootView.getHeight() - mRootView.getWidth()) / 2);
        rlDialog.setY((mRootView.getHeight() - mRootView.getWidth()) / 2);
        tvDialogTitle.setText(R.string.report);
        String tips = ResourceHelper.getString(R.string.user_info_sure_report_start_tip)
                + (ResourceHelper.getString(R.string.user_info_sure_report_live_tip))
                + ResourceHelper.getString(R.string.user_info_sure_report_end_tip);
        tvDialogContent.setText(tips);
        btnDialogNo.setText(R.string.cancel);
        btnDialogYes.setText(R.string.yes);
        btnDialogYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlDialog.setVisibility(View.GONE);
                report(userId);
            }
        });
        rlDialog.setVisibility(View.VISIBLE);
        rlDialog.setRotation(90);
    }

    private void report(String userId) {
        DataProvider.report(this, userId, mCourseId, Common.COURSE, new GsonHttpConnection.OnResultListener<Msg>() {
            @Override
            public void onSuccess(Msg msg) {
                showToast(getString(R.string.report_success));
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                showToast(getString(R.string.report_success));
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
        bgView.setOnClickListener(new View.OnClickListener() {
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
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                hideController();
            }
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, " onConfigurationChanged newConfig.orientation=" + newConfig.orientation);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Nothing need to be done here
            exoPlayer.widthRatio = screenHeight;
            exoPlayer.heightRatio = screenWidth;
        } else {
            // Nothing need to be done here
            exoPlayer.widthRatio = screenWidth;
            exoPlayer.heightRatio = screenHeight;
            if (!isMine) {
                btnGift.setVisibility(View.VISIBLE);
            }
        }


    }

    @Override
    @Subscribe
    public void onEvent(final PostEvent event) {
        super.onEvent(event);
//        Logger.e("输出事件：" + event);

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
                    if (giftGridViewLand != null) {
                        giftGridViewLand.setBalance(result);
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
                        message.avatar = AccountInfoManager.getInstance().getCurrentAccountUserAvatar();
                        //处理礼物动画区显示
                        giftShowView.showGift(message);
                        giftAnimManger.addGift(message);
                    }
                } else if (SubcriberTag.SOCKET_NOT_CONNECT.equals(event.tag)) {
                    if (isResume()) {
                        showToast(R.string.service_not_connect);
                    }
                } else if (SubcriberTag.SHOW_MESSAGE_PANEL_CHAT.equals(event.tag)) {
                    if (!isPortrait && !isRotationVideo) {
                        btnOrientation.performClick();
                    }
                    ChatSession session = (ChatSession) event.event;
                    showMessagePanel(session);
                } else if (event.tag.equals(SubcriberTag.SHOW_REPORT_DIALOG_LAND)) {
                    showReportDialogLand((String) event.event);

                }
//                else if (SubcriberTag.SHOW_GOOD_BUY_PANEL.equals(event.tag)) {
//                    mDetailBean = (ShoppingGoodsDetailBean) event.event;
//                    tryOpenGoodsBuyPanel(ShoppingConstant.PANEL_TYPE_BUY_CAR);
//                }
                else if (SubcriberTag.REFRESH_RECOMMEND_GOODS_COUNT.equals(event.tag)) {
                    int count = (int) event.event;
                    btnGoods.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
                    btnGoods.setText(String.valueOf(count));
                } else if (SubcriberTag.CHOOSE_VIDEO_LINE.equals(event.tag)) {  // 切换线路
                    rcvLines.setVisibility(View.GONE);
                    VideoLine video = (VideoLine) event.event;
                    if (!videoUrl.equals(video.getUrl())) {
                        videoUrl = video.getUrl();
                        curVideoProgress = (int) exoPlayer.getCurrentPositionWhenPlaying();
                        Log.d(TAG, " curVideoProgress=" + curVideoProgress
                                + " isPortrait=" + isPortrait + " isRotationVideo=" + isRotationVideo);
                        releasePlayer();
                        onClick(btnPlay);
                        setPlay();
                        if (!isPortrait && isRotationVideo) {
                            Log.d(TAG, "需要转屏");
                            isNeedChangeScreen = true;
                        }
                    }
                } else if (SubcriberTag.SOCKET_ERROR_TIPS.equals(event.tag)) {
                    if (isResume()) {
                        showToast((String) event.event);
                    }
                } else if (SubcriberTag.CONNECTIVE_MOBILE.equals(event.tag)) { // 移动数据
                    Logger.e("网络切换到4G 提示");
                    curVideoProgress = (int) exoPlayer.getCurrentPositionWhenPlaying();
                    exoPlayer.showWifiDialog();
                    exoPlayer.onClickPlayButton();
                    exoPlayer.currentState = Jzvd.CURRENT_STATE_PAUSE;
                    isPlaying = false;
                }
            }
        });

    }

    /**
     * description:添加网络切换监听
     **/
    private void initNetWorkChangeListener() {
        netWorkChangeReceiver = new NetWorkChangeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkChangeReceiver, intentFilter);
        isRegister = true;
    }

    Speed speedItem;

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void changeSpeed() {
        if (Utils.listIsNullOrEmpty(videoSpeeds)) {
            getCourseDetail();
            showToast("没有可切换速度");
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            showToast("6.0以上系统才支持播放速度调整");
            return;
        }
        curSpeed++;
        if (curSpeed > videoSpeeds.size() - 1) {
            curSpeed = 0;
        }
        speedItem = videoSpeeds.get(curSpeed);
        float speed = speedItem.getRate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PlaybackParams pp = new PlaybackParams();
            pp.setSpeed(speed);

            JZExoPlayer jzExoPlayer = (JZExoPlayer) JZMediaManager.instance().jzMediaInterface;
            jzExoPlayer.getSimpleExoPlayer().setPlaybackParams(pp);
        } else {
            showToast("6.0以上系统才支持播放速度调整");
        }
        tvSpeed.setText(speedItem.getName());
    }

    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        public SpaceItemDecoration() {

        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.right = 0;
            outRect.top = ResourceHelper.getDimen(R.dimen.space_10);
            outRect.bottom = ResourceHelper.getDimen(R.dimen.space_10);
            outRect.left = 0;
        }
    }

}
