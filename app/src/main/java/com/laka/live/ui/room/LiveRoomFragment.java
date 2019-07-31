package com.laka.live.ui.room;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.animated.base.AnimatedDrawable;
import com.google.gson.Gson;
import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.account.login.LoginActivity;
import com.laka.live.account.my.ContributionListActivity;
import com.laka.live.account.my.ContributionListPanel;
import com.laka.live.account.my.MyCoinsActivity;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.application.LiveApplication;
import com.laka.live.audio.BgmManager;
import com.laka.live.bean.ChatEntity;
import com.laka.live.bean.ConnectUserInfo;
import com.laka.live.bean.Course;
import com.laka.live.bean.GiftInfo;
import com.laka.live.bean.ListUserInfo;
import com.laka.live.bean.MusicInfo;
import com.laka.live.bean.RankingUserInfo;
import com.laka.live.bean.UserInfo;
import com.laka.live.config.SystemConfig;
import com.laka.live.control.QavsdkControl;
import com.laka.live.dao.DbManger;
import com.laka.live.gift.GiftAudioManager;
import com.laka.live.gift.GiftResManager;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.NetStateManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.lakalive.LiveCallback;
import com.laka.live.manager.BytesReader;
import com.laka.live.manager.RequestType;
import com.laka.live.manager.RoomManager;
import com.laka.live.msg.CourseDetailMsg;
import com.laka.live.msg.FormulaMsg;
import com.laka.live.msg.GetQinNiuUpLoadTokenMsg;
import com.laka.live.msg.Msg;
import com.laka.live.msg.OpenLiveMsg;
import com.laka.live.msg.QiNiuUploadMsg;
import com.laka.live.msg.QueryRoomMsg;
import com.laka.live.msg.StartRecordMsg;
import com.laka.live.msg.UserMsg;
import com.laka.live.music.MusicListActivity;
import com.laka.live.network.DataProvider;
import com.laka.live.network.DataProviderRoom;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.thread.BackgroundThread;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.activity.WebActivity;
import com.laka.live.ui.chat.ChatMessageActivity;
import com.laka.live.ui.chat.ChatMessageView;
import com.laka.live.ui.chat.ChatSessionPanel;
import com.laka.live.ui.chat.ChatUnfollowPanel;
import com.laka.live.ui.chat.FormulaPanel;
import com.laka.live.ui.fragment.BaseFragment;
import com.laka.live.ui.rankinglist.RankingItemView;
import com.laka.live.ui.room.roommanagerlist.LiveManagerConstant;
import com.laka.live.ui.room.roommanagerlist.LiveRoomAdminManager;
import com.laka.live.ui.room.roommanagerlist.LiveRoomManagerListPanel;
import com.laka.live.ui.widget.FloatWindow;
import com.laka.live.ui.widget.LakaLoading;
import com.laka.live.ui.widget.MarkSimpleDraweeView;
import com.laka.live.ui.widget.ScrollbleViewPager;
import com.laka.live.ui.widget.chatKeyboard.InputRelativeLayout;
import com.laka.live.ui.widget.chatKeyboard.InputWindowListener;
import com.laka.live.ui.widget.dialog.GenericDialog;
import com.laka.live.ui.widget.dialog.IDialogOnClickListener;
import com.laka.live.ui.widget.dialog.SimpleTextDialog;
import com.laka.live.ui.widget.emoji.EmoticonPickerView;
import com.laka.live.ui.widget.emoji.IEmoticonSelectedListener;
import com.laka.live.ui.widget.emoji.MoonUtil;
import com.laka.live.ui.widget.flash.FlashView;
import com.laka.live.ui.widget.gift.ExpressionPagerAdapter;
import com.laka.live.ui.widget.gift.GiftAnimManger;
import com.laka.live.ui.widget.gift.GiftGridView;
import com.laka.live.ui.widget.gift.GiftGridViewLand;
import com.laka.live.ui.widget.gift.GiftShowView;
import com.laka.live.ui.widget.lrcView.LrcView;
import com.laka.live.ui.widget.panel.ActionSheetPanel;
import com.laka.live.ui.widget.panel.GPUImageFilterPanel;
import com.laka.live.ui.widget.room.AddLinkMicPanel;
import com.laka.live.ui.widget.room.AudioEffectPanel;
import com.laka.live.ui.widget.room.DivergeView;
import com.laka.live.ui.widget.room.LiveRoomUserInfoPanel;
import com.laka.live.ui.widget.room.RoomDanmuView;
import com.laka.live.ui.widget.room.ScreenLoadingView;
import com.laka.live.ui.widget.room.ZhuboMorePanel;
import com.laka.live.util.CacheUtil;
import com.laka.live.util.Common;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.LakaUtil;
import com.laka.live.util.Log;
import com.laka.live.util.NetworkUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ShareUtil;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.TypefaceHelper;
import com.laka.live.util.UiPreference;
import com.laka.live.util.Util;
import com.laka.live.util.Utils;
import com.laka.live.zego.ZegoLivePlayer;
import com.laka.live.zego.ZegoLivePusher;
import com.laka.live.zego.utils.ZegoRoomUtil;
import com.laka.live.zego.widgets.ViewLive;
import com.zego.zegoliveroom.entity.ZegoStreamInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import framework.ioc.Ioc;
import laka.live.bean.ChatMsg;
import laka.live.bean.ChatSession;

import static com.laka.live.help.SubcriberTag.AUTO_GO_NEXT_ANCHOR_EVENT;
import static com.laka.live.help.SubcriberTag.ON_USER_INFO_ITEM_CLICK_EVENT;

/**
 * Created by luwies on 16/10/25.
 * 直播过程中主播画面的Fragment
 */

public class LiveRoomFragment extends BaseFragment implements View.OnClickListener,
        GiftGridView.GiftGridListener, EventBusManager.OnEventBusListener
        , IEmoticonSelectedListener {

    public final static String IS_INIT = "IS_INIT";

    private final static int REQUEST_LOCATION_PERMISSION_CODE = 1112;
    private static final String TAG = "LiveRoomFragment";

    private LiveApplication mLiveApplication;
    //    private PowerManager.WakeLock wakeLock;
    private UserInfo mSelfUserInfo;

    public static boolean isSeeingLive;
    /**
     * 直播id
     */
    private String mHostIdentifier;//主播ID
    private String myUserId;//我的ID
    private boolean isCreater;
    private boolean isTestLive;
    private int mRoomId;
    //    private RelativeLayout rlLoading;
//    private TextView loadingTextView;
    private ScreenLoadingView rlLoading;

    private LakaLoading mCircleLoading;

    /**
     * 自定义控件和变量
     */
    private RecyclerView mRecyclerView;
    private SeeingUserAdapter mAudienceAdapter;
    private List<UserInfo> mAudiences = new ArrayList<>();
    private Button btnShare, btnComment, btnCamera, btnMic, btnLetter, btnGiftAudience, btnGift, btnMore, btnFollow, btnBgm, btnOrientation;//btnMusic
    private TextView tvTimer, tvKazuan, tvLakaNo, tvZhiboLive, tvTimeTips;//tvAudienceCnt
    private MarkSimpleDraweeView ivZhuboHead;
    private LinearLayout llBottomBar;//底部功能栏
    private LinearLayout rlComment;
    private ImageView ivLineLeft;
    private EditText etComment;
    private Button btnDanmu, btnSendComment, mBtnForbid;
    private ImageView ivDanmu, btnClose;
    private RelativeLayout rlDanmu, rlLvComment;
    private ObjectAnimator animDanmuOpen, animDanmuClose;

    //表情
    private Button btnFace;
    private EmoticonPickerView emoticonPickerView;

    private int maxLen = 130;
    //土豪进来了相关
    private RelativeLayout rlTuhaoCome;
    private TextView tvLevelCome;
    private TextView tvContentCome;
    private ImageView ivLightCome;
    private SimpleDraweeView ivStar1;// ivStar2;
    //评论列表
    List<ChatEntity> chatList = new ArrayList<>();
    RecyclerView lvComment;
    LiveChatRcvAdapter chatAdapter;
    boolean isAutoScroll = true;
    long AUTO_SCROLL_DELAY = 5000;
    //    ListView lvComment;
//    LiveChatAdapter chatAdapter;
    //点赞动画区域
//    PeriscopeLayout periscopeLayout;
    private DivergeView mDivergeView;
    //全屏触摸处理层
    RelativeLayout rlTouch;
    RelativeLayout rlTopBar;
    //主播界面
    RelativeLayout rlTopBarZhubo;
    //观众界面
    RelativeLayout rlTopBarAudience;
    RelativeLayout rlKazuanBank;
    //礼物选择区
//    private ViewStub vsGift;
    private GiftGridView giftGridView;
    private GiftGridViewLand giftGridViewLand;
    private GiftInfo chooseGift;

    private SimpleDraweeView sdvHotActivity;

    //礼物显示区
    private GiftShowView giftShowView;
    private int totalGiveConins;
    //全局唯一计时器
    Timer mTimer, mCheckInRoomTimer, pushCheckTimer;
    TimerTask roomTimerTask, checkInRoomTimerTask;
    static final long PERIOD_TIMER = 1000;
    static final int TIMER_ROOM_DATA = 5;//查询房间信息间隔秒数
    static final int TIMER_ROOM_USER = 5;//查询房间观众间隔秒数
    int timerRoomData = 0, timerRoomUser = 0, mTimeCheckInRoom = 0;
    private double roomCoins;
    private long roomAudience;
    private TextView tvAudienceCntNow;
    private int curRoomUserPage = 0;
    private int roomUserpageSize = 20;
    private boolean isQueryingRoomUser = false;
    private String mTitle, mCover, mAvatar, mLocation;//标题和封面,头像
    private String topics;
    //    private String downUrl;//拉流地址
    private String streamId, channelId;
    InputRelativeLayout rootView;
    private GiftAnimManger giftAnimManger;   //特殊动画管理

    private boolean isReconnecting;
    private String SDK_ROLE = Common.TEST;
    private String taskId;//录像对应标识
    private boolean isRecFirstFrame = false;//是否获取第一帧播放
    private boolean isEnterVideoRoom = false;//是否进入视频房间
    private boolean isZhiboEnd = false;//是否直播结束
    private boolean isStartLiveSuccess;//是否开启直播成功
    private boolean isStartRecordSuccess;//是否开启录像成功
    private boolean isGetVideoSuccess;//是否获取画面成功成功
    private boolean isSetRemoteHasVideo = false;
    private boolean isZhuboTemporaryLeave;//是否暂时离开
    private boolean isCreateRoom = false;//是否创建房间
    private boolean isEnterRoom = false;//是否进入房间
    private boolean isPushing = false;//是否正在推流
    private boolean isLoadingPlayUrl = false;//是否正在同步地址
    private boolean isSyncPlayUrl = false;//是否同步过播放地址
    //内容滑动
    private ScrollbleViewPager vpContainer;
    private ExpressionPagerAdapter contentAdapter;
    private List<View> contentViews = new ArrayList<>();
    private View emptyView;
    private RelativeLayout roomView;
    private LinearLayout llBg;
    private int mCameraId;

    private Log.MarkerLog mMarkerLog;

    //private boolean isFromActivityReCreate = false; // 执行onCretae是否是带有savedInstanceState启动的
    FlashView mFlashView;
    boolean change;
    private LiveRoomUserInfoPanel mUserInfoPanel;
    private AudioEffectPanel mAudioEffectPanel;
    private ChatSessionPanel mChatSessionPanel;
    private FormulaPanel mFormulaPanel;
    private ChatUnfollowPanel mChatUnfollowPanel;
    private ContributionListPanel mContributionPanel;
    //    private ChatMessagePanel mChatMessagePanel;
    private ChatMessageView mChatMessagePanel;
    private GPUImageFilterPanel mFilterPanel;

    private boolean isChatMessagePanelShow = false;

    private View mMsgRedPoint;

    private View mZhuboMsgRedPoint;

    //歌词相关
    private LrcView mLyricView;
    private LinearLayout mRlEffect, mRlEnd;
    private RelativeLayout mRlLrc;

    //    private String musicPath = Environment.getExternalStorageDirectory()
//            + File.separator + "test.mp3";
    //背景音乐和混音控制
    private BgmManager mBgmManager;
    long lastPushResumeTime = 0;//上次恢复推流时间
    long mLastShowLoadingTime = 0;//上次弹加载框时间
    private boolean isNeedChangeCache = true;

    //连接房间时间间隔
    private static final long CONNECT_ROOM_INTERVAL = 1000 * 5;
    private long lastConnectRoomTime = 0;
    private int retryPlayTimes = 0;

    private View mRootView;

    private BaseActivity mBaseActivity;

    private int curQuality;
    private String mCourseId; // 课程ID
    private Course mCourse; // 课程详情
    private FormulaMsg mFormulaMsg; // 配方做法

    //连麦相关
    private ImageView ivConnectRedAudience, ivConnectRedZhubo;
    private Button btnConnectAudience, btnConnectZhubo;
    private ConnectMicManager connectMicManager;
    private FrameLayout flConnectAudience, flConnectZhubo;
    private LiveSmallView mLiveSmallView;
    private View mZhuboLinkRedpoint;
    // 用户信息
    private UserInfo mUserInfo = Ioc.get(UserInfo.class);
    private RelativeLayout rlDialog;
    //推荐商品
    private GoodsPanel goodsPanel;
    private Button btnGoods;

    //暂停直播
    private RelativeLayout rlPauseLiveDialog;
    private Button btnPauseLive, btnFinishLive;
    private ImageView ivCloseLive;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_live_room, null);
        mBaseActivity = (BaseActivity) getActivity();
        Bundle args = getArguments();
        if (args != null) {

            if (args.getBoolean(IS_INIT, false)) {
                mRootView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initFragment();
                    }
                }, 100L);
            } else if (args.getBoolean(Common.EXTRA_IS_CREATER)) {
                initFragment();
            }
        }
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, " liveRoomActivity onCreate");
        mMarkerLog = new Log.MarkerLog(TAG);
        mMarkerLog.add("onCreate");
        super.onCreate(savedInstanceState);

        /*initDefault();
        init();
        initData();
        LiveRoomManager.getInstance().setCurrentLiveRoomUserId(mHostIdentifier);
        LiveApplication.getInstance().isInLiveRoomActivity = true;*/

    }

    protected void initFragment() {
        Log.d(TAG, " initFragment");
        initDefault();
        init();
        initData();
        getFormula();
        LiveRoomManager.getInstance().setCurrentLiveRoomUserId(mHostIdentifier);
        LiveApplication.getInstance().isInLiveRoomActivity = true;

        hideGiftView();
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

    protected void onNewIntent(Intent intent) {
        setArguments(intent.getExtras());
        initFragment();
    }

    private void initDefault() {
        totalTime = 0;
        retryPlayTimes = 0;
        lastConnectRoomTime = 0;
        isRecFirstFrame = false;//是否获取第一帧播放
        isEnterVideoRoom = false;//是否进入视频房间
        isZhiboEnd = false;//是否直播结束
        isStartLiveSuccess = false;//是否开启直播成功
        isStartRecordSuccess = false;//是否开启录像成功
        isGetVideoSuccess = false;//是否获取画面成功成功
        isSetRemoteHasVideo = false;
        isZhuboTemporaryLeave = false;//是否暂时离开
        isCreateRoom = false;//是否创建房间
        isEnterRoom = false;//是否进入房间
        isPushing = false;//是否正在推流
        isChatMessagePanelShow = false;
        isSyncPlayUrl = false;
        isLoadingPlayUrl = false;
        releaseTimer();
        if (mLivePusher != null) {
            stopPublishRtmp();
            mLivePusher.stopPreview();
            mLivePusher.destroy();
        }
//        if (mCaptureView != null) {
//            mCaptureView.setVisibility(View.GONE);
//        }
        if (mLivePlayer != null) {
            stopPlayRtmp();
//            mLivePlayer.destroy();
        }
//        if (mPlayerView != null) {
//            mPlayerView.setVisibility(View.GONE);
//        }

        if (vlBigView != null) {
            vlBigView.setVisibility(View.VISIBLE);//GONE
        }

        connectMicManager = null;

    }

    private void initData() {
        getZhuboUserInfo();
        if (!isCreater) {
            isSeeingLive = true;
            getMyUserInfo();
            //获取主播资料
            AnalyticsReport.onEvent(getContext(), LiveReport.MY_LIVE_EVENT_11223);
            tvLakaNo.setText(getString(R.string.laka_no) + AccountInfoManager.getInstance().getCurrentAccountUserIdStr());
        } else {
            UserInfo mine = AccountInfoManager.getInstance().getAccountInfo();
            if (mine != null) {
                roomCoins = mine.getTotalCoins();
                tvKazuan.setText(String.valueOf(roomCoins));
            }
//            initFilterPanel();
        }
    }

    private void init() {
        initIntentData();
        if (Utils.isEmpty(mHostIdentifier)) {
            ToastHelper.showToast(R.string.zhubo_not_here);
            finish();
            return;
        }

        initView();
        initLocation();
        initComment();
        //initAudience();//隐藏
        initDanmu();
        initLikeView();
        initGiftView();//延迟加载
        initGiftShowView();
        refreshRoleUI();

        initRoom();
        /*Bundle bundle = getArguments();
        if (bundle != null && bundle.getBoolean(IS_INIT, false) == true) {
            vpContainer.setVisibility(View.VISIBLE);
        } else {
            vpContainer.setVisibility(View.GONE);
        }*/
        initChatMessagePanel();
        initLrcView();

//        String course_id = UiPreference.getString(mUserInfo.getIdStr(), null);
//        // 当前有直播正在进行,继续直播
//        if (course_id != null && isCreater) {
//            releaseOnClickListener.onClick(releaseLiveView.findViewById(R.id.start_live));
//        }

    }

    protected void initRoom() {
        if (roomManger != null) {
            exitLiveRoom();
        }
        initAvSdk();
        initRoomManger();

        if (!isCreater) {
            vpContainer.setVisibility(View.VISIBLE);
        } else {
//            if (mPlayerView != null) {
//                mPlayerView.setVisibility(View.GONE);
//            }
//            if (vlBigView != null) {
//                vlBigView.setVisibility(View.GONE);
//            }
        }
        try {
            EventBusManager.register(this);
        } catch (Exception e) {

        }
    }

    private void exitLiveRoom() {
        Log.d(TAG, " exitLiveRoom");
        roomManger.exitRoom();
        //同时关闭当前副播
        if (connectMicManager != null) {
            if (connectMicManager.isSecondZhubo) {
                connectMicManager.closeLinkMic("");
                connectMicManager.stopPushSecond();
            } else {
                connectMicManager.stopPlaySecond();
            }
        }
    }

    protected void stopRoom() {

        if (vpContainer != null) {
            vpContainer.setVisibility(View.GONE);
        }
//        if (mPlayerView != null) {
//            mPlayerView.setVisibility(View.GONE);
//        }
//        if (vlBigView != null) {
//            vlBigView.setVisibility(View.GONE);
//        }
        stopPlayRtmp();
        /*if (mLivePlayer != null) {
            mLivePlayer.destroy();
        }*/

        /*if (roomManger != null) {
            roomManger.setOnRequestResultCallback(null);
            roomManger.cleareResultListener();
        }*/

        try {
            EventBusManager.unregister(this);
        } catch (Exception e) {

        }
        if (connectMicManager != null) {
            connectMicManager.stop();
        }

        //停止动画
        if (giftShowView != null) {
            giftShowView.stopAndClear();
        }

        if (giftAnimManger != null) {
            giftAnimManger.clearAllAnima();
        }
        if (!Utils.listIsNullOrEmpty(likeList)) {
            likeList.clear();
            likeList = null;
        }
        clearAdminState();
    }

    private void initLrcView() {
        mRlLrc = (RelativeLayout) findViewById(R.id.rl_lrc);
        mLyricView = (LrcView) findViewById(R.id.lyricView);
        if (isCreater) {
            mRlEffect = (LinearLayout) findViewById(R.id.rl_effect);
            mRlEnd = (LinearLayout) findViewById(R.id.rl_end);
            mRlEffect.setOnClickListener(this);
            mRlEnd.setOnClickListener(this);
            mRlLrc.setVisibility(View.INVISIBLE);
        } else {
            mRlLrc.setVisibility(View.INVISIBLE);
        }
    }


    boolean isFlashStop = false;
    int flashCnt = 0;

    private ArrayList<Bitmap> likeList;

    private void initLikeView() {
        mDivergeView = (DivergeView) findViewById(R.id.divergeView);
        mDivergeView.post(new Runnable() {
            @Override
            public void run() {
                mDivergeView.setEndPoint(new PointF(mDivergeView.getMeasuredWidth() / 2, 0));
                mDivergeView.setDivergeViewProvider(new Provider());
            }
        });
        BackgroundThread.post(new Runnable() {
            @Override
            public void run() {
                likeList = new ArrayList<>();
                likeList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.live_icon_like_1, null)).getBitmap());
                likeList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.live_icon_like_2, null)).getBitmap());
                likeList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.live_icon_like_3, null)).getBitmap());
                likeList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.live_icon_like_4, null)).getBitmap());
                likeList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.live_icon_like_5, null)).getBitmap());
                likeList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.live_icon_like_6, null)).getBitmap());
                likeList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.live_icon_like_kiwi, null)).getBitmap());
                likeList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.live_icon_like_lemon, null)).getBitmap());
                likeList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.live_icon_like_popsicle, null)).getBitmap());
                likeList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.live_icon_like_watermelon, null)).getBitmap());
                likeList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.live_icon_like_zan01, null)).getBitmap());
                likeList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.live_icon_like_zan02, null)).getBitmap());
                likeList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.live_icon_like_zan03, null)).getBitmap());
            }
        });

    }

    boolean isVideoFull;

    public void setVideoHeightFull(boolean videoHeightFull) {
        if (isVideoFull != videoHeightFull) {
            isVideoFull = videoHeightFull;
//            if (mCaptureView != null) {
//                ViewGroup.LayoutParams lp = mCaptureView.getLayoutParams();
//                int height = Utils.getScreenHeight(getContext());
//                Log.d(TAG, " 调整video高度 height=" + height);
//                lp.height = height;
//                mCaptureView.setLayoutParams(lp);
//            }
//            if (mPlayerView != null) {
//                ViewGroup.LayoutParams lp = mPlayerView.getLayoutParams();
//                int height = Utils.getScreenHeight(getContext());
//                Log.d(TAG, " 调整playervideo高度 height=" + height);
//                lp.height = height;
//                mPlayerView.setLayoutParams(lp);
//            }
            if (vlBigView != null) {
                ViewGroup.LayoutParams lp = vlBigView.getLayoutParams();
                int height = Utils.getScreenHeight(getContext());
                Log.d(TAG, " 调整playervideo高度 height=" + height);
                lp.height = height;
                vlBigView.setLayoutParams(lp);
            }
        }

    }

    class Provider implements DivergeView.DivergeViewProvider {
        @Override
        public Bitmap getBitmap(Object obj) {
            if (obj instanceof Integer) {
                int index = (int) obj;
                if (index >= 0 && index < likeList.size()) {
                    return likeList.get(index);
                }
            }
            return null;
        }
    }

    private void initGiftShowView() {
        giftShowView = (GiftShowView) findViewById(R.id.gift_show_view);
        giftShowView.isStop = false;
//        giftAnimManger = new GiftAnimManger(getActivity(), (ViewGroup) mRootView.findViewById(R.id.room_content));
        giftAnimManger = new GiftAnimManger(getActivity(), roomView);
    }


    private void setGiftShowViewSoft() {
//        float bottomMargin = Utils.dip2px(this,150);
        /*float bottomMargin = ResourceHelper.getDimen(R.dimen.gift_view_input_show_margin_bottom);
//        Log.d(TAG,"setGiftShowViewDown 当前y="+giftShowView.getY()+" rootView.getHeight()="+ rootView.getHeight()
//        +" bottomMargin="+bottomMargin);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) giftShowView.getLayoutParams();
//        params.bottomMargin = (int) (rootView.getHeight() -bottomMargin);//166
        params.bottomMargin = (int) bottomMargin;
        giftShowView.setLayoutParams(params);*/
//        Log.d(TAG,"setGiftShowViewDown 调整后y="+giftShowView.getY());
    }

    private void setGiftShowViewNormal() {
        /*float bottomMargin = ResourceHelper.getDimen(R.dimen.gift_view_input_hide_margin_bottom);;
//        float bottomMargin = Utils.dip2px(this,280);
//        Log.d(TAG,"setGiftShowViewUp 当前y="+giftShowView.getY()+" rootView.getHeight()="+ rootView.getHeight()
//                +" bottomMargin="+bottomMargin);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) giftShowView.getLayoutParams();
//        params.bottomMargin = (int) (rootView.getHeight()-bottomMargin);//256
        params.bottomMargin = (int) bottomMargin;
        giftShowView.setLayoutParams(params);*/
//        Log.d(TAG,"setGiftShowViewUp 调整后y="+giftShowView.getY());
    }

    RoomManager roomManger;//房间通讯操作类

    /**
     * 初始化房间通信管理
     */
    private void initRoomManger() {
        if (roomManger == null) {
            roomManger = RoomManager.getInstance();
            Log.d(TAG, "intent addResultListener");
            roomManger.addResultListener(roomListener);
        }
        if (isCreater) {

        } else {
            checkInitRoom();
        }

        if (roomManger.isConnected()) {
//            Log.d(TAG, "socket已连接，连接房间");
            if (isCreater) {
//                boolean isUploadLocation = UiPreference.getBoolean(Common.IS_UPLOAD_LOCATION, true);
//                String location = isUploadLocation ? UiPreference.getString(Common.KEY_MY_LOCATION_CITY,
//                        mSelfUserInfo.getRegion()) : "";
//                roomManger.createRoom(mTitle, location, mCover);
            } else {
                enterRoom(mRoomId + "");
                //开启检测
            }
        } else {
            Log.d(TAG, "socket未连接，先连接");
            roomManger.startRoom();
        }

        isCloseRoom = false;
        addRequestCallbackToRoomManager();

        //注册网络监听
//        NetworkChangeObserver.getInstance().registListener(networkChangeListener);
//        int netWorkState = NetworkUtil.getNetworkState(this);
//        lastNetWorkState = netWorkState;
//        if (checkIsMoblieNetWork(netWorkState)) {
//            showMobileNetWorkDialog();
//            Log.d(TAG, "当前是移动网络");
//        } else {
//            Log.d(TAG, "当前不是移动网络");
//        }
    }

    private void enterRoom(String roomID) {
        if (!checkNeedConnect()) return;
        Log.d(TAG, " enterRoom roomID=" + roomID);
        roomManger.enterRoom(roomID);
    }

    private boolean checkNeedConnect() {
        long curTime = System.currentTimeMillis();
        Log.d(TAG, " lastConnectRoomTime=" + lastConnectRoomTime + " curTime=" + curTime);
        if (lastConnectRoomTime > 0 && curTime - lastConnectRoomTime < CONNECT_ROOM_INTERVAL) {
            Log.d(TAG, " 离上次连接房间时间不足30秒，不重连房间");
            return false;
        } else {
            Log.d(TAG, " 离上次连接房间时间超过30秒");
        }
        return true;
    }

    private void checkInitRoom() {
        if (isDestroyed()) {
            return;
        }
        if (isCreater) {
            if (isCreateRoom && roomManger.isConnected()) {
                Log.d(TAG, "checkInitRoom isCreateRoom=" + isCreateRoom + " isPushing=" + isPushing + " 取消计时器");

//                            if(!isPushReady){
//                                hideLoading();
//                                getPushUrl();
//                            }

//                if(!isPushing){
//                    addSystemNotice("未连接上直播服务器，请检查网络");
//                }

                if (mCheckInRoomTimer != null) {
                    mCheckInRoomTimer.cancel();
                    mCheckInRoomTimer = null;
                }

            } else {
                Log.d(TAG, "checkInitRoom isCreateRoom=" + isCreateRoom + " 创房间");
                if (!isPushReady && isResume()) {
                    showToast("未连接上服务器，请调整网络后重试");
                }
                createRoom();
            }
        } else {
            mCheckInRoomTimer = new Timer();
            checkInRoomTimerTask = new TimerTask() {
                @Override
                public void run() {
                    mTimeCheckInRoom++;
                    if (mTimeCheckInRoom > 5) {
                        mTimeCheckInRoom = 0;
                        if (isCreater) {
                            if (isCreateRoom && roomManger.isConnected()) {
                                Log.d(TAG, "checkInitRoom isCreateRoom=" + isCreateRoom + " isPushing=" + isPushing + " 取消计时器");

//                            if(!isPushReady){
//                                hideLoading();
//                                getPushUrl();
//                            }

                                if (!isPushing) {
                                    addSystemNotice("未连接上直播服务器，请检查网络");
                                }

                                if (mCheckInRoomTimer != null) {
                                    mCheckInRoomTimer.cancel();
                                    mCheckInRoomTimer = null;
                                }

                            } else {
                                Log.d(TAG, "checkInitRoom isCreateRoom=" + isCreateRoom + " 创房间");
                                if (!isPushReady && isResume()) {
                                    showToast("未连接上服务器，请调整网络后重试");
                                }
                                createRoom();
                            }
                        } else {
                            if (isEnterRoom && roomManger.isConnected()) {
                                Log.d(TAG, "checkInitRoom isEnterRoom=" + isEnterRoom + " 取消计时器");
                                if (mCheckInRoomTimer != null) {
                                    mCheckInRoomTimer.cancel();
                                    mCheckInRoomTimer = null;
                                }
                            } else {
                                Log.d(TAG, "checkInitRoom isEnterRoom=" + isEnterRoom + " 进房间");
                                enterRoom(mRoomId + "");
                            }
                        }
                    }
                }
            };
            mCheckInRoomTimer.scheduleAtFixedRate(checkInRoomTimerTask, 0, 1000);
        }


    }


    private void addRequestCallbackToRoomManager() {
        if (roomManger == null) {
            return;
        }
        roomManger.setOnRequestResultCallback(new RoomManager.onRequestResultCallback() {
            @Override
            public void onFailed(int errorCode) {
                handleOnRequestResultFail(errorCode);
            }

            @Override
            public void onSuccess(int requestType, Object object) {
                handleOnRequestResultSuccess(requestType, object);
            }
        });
    }

    private void handleOnRequestResultFail(int errorCode) {
        ResponseCodeHelper.showTips(getContext(), errorCode);
    }

    private void handleOnRequestResultSuccess(int requestType, Object object) {
        if (requestType == RequestType.REQUEST_TYPE_ADD_ADMIN) {
            handleOnResultAddRoomAdmin();
            return;
        }
        if (requestType == RequestType.REQUEST_TYPE_REMOVE_ADMIN) {
            handleOnResultRemoveRoomAdmin();
            return;
        }
        if (requestType == RequestType.REQUEST_TYPE_FORBIDDEN) {
            handleOnResultForbidden();
            return;
        }
        if (requestType == RequestType.REQUEST_TYPE_CANCEL_FORBIDDEN) {
            handleOnResultCancelForbidden();
            return;
        }
        if (requestType == RoomManager.TLV_REQ_UNICAST_ADD_ROOM_ADMIN) {
            handleOnReceiveAddRoomAdminNotify();
            return;
        }
        if (requestType == RoomManager.TLV_REQ_UNICAST_DEL_ROOM_ADMIN) {
            handleOnReceiveRemoveRoomAdminNotify();
            return;
        }
        if (requestType == RoomManager.TLV_REQ_MULTICAST_ROOM_PERMIT_SAY) {
            handleOnReceiveCancelForbiddenNotify(object);
            return;
        }
        if (requestType == RoomManager.TLV_REQ_MULTICAST_ROOM_FORBID) {
            handleOnReceiveForbiddenNotify(object);
            return;
        }
    }

    private void handleOnReceiveCancelForbiddenNotify(Object object) {
        if (object == null) {
            return;
        }
        UserInfo userInfo = null;
        if (object instanceof UserInfo) {
            userInfo = (UserInfo) object;
        }
        if (userInfo == null || TextUtils.isEmpty(userInfo.getIdStr()) || TextUtils.isEmpty(userInfo.getNickName())) {
            return;
        }
        ChatEntity item = new ChatEntity();
        item.setSenderName(userInfo.getNickName());
        item.setContent(getResources().getString(R.string.live_manager_be_cancel_forbidden));
        item.setType(ChatEntity.MSG_TYPE_FORBID_SAY);
        addCommentToList(item);
        tryRefreshAudienceForbiddenState(userInfo.getIdStr(), false);
    }

    private void handleOnReceiveForbiddenNotify(Object object) {
        if (object == null) {
            return;
        }
        UserInfo userInfo = null;
        if (object instanceof UserInfo) {
            userInfo = (UserInfo) object;
        }
        if (userInfo == null || TextUtils.isEmpty(userInfo.getIdStr()) || TextUtils.isEmpty(userInfo.getNickName())) {
            return;
        }
        ChatEntity item = new ChatEntity();
        item.setSenderName(userInfo.getNickName());
        item.setContent(getResources().getString(R.string.live_manager_be_forbidden));
        item.setType(ChatEntity.MSG_TYPE_FORBID_SAY);
        addCommentToList(item);
        tryRefreshAudienceForbiddenState(userInfo.getIdStr(), true);
    }

    private void handleOnReceiveRemoveRoomAdminNotify() {
        AccountInfoManager.getInstance().updateCurrentAccountAdminState(false);
        ToastHelper.showToast(getString(R.string.live_manager_cancel_admin_tip));
    }

    private void handleOnReceiveAddRoomAdminNotify() {
        AccountInfoManager.getInstance().updateCurrentAccountAdminState(true);
        ToastHelper.showToast(getString(R.string.live_manager_to_be_admin_tip));
    }

    /**
     * 创建房间的回调
     */
    private RoomManager.OnResultListener roomListener = new RoomManager.OnResultListener() {

        private void insertMessage(String message) {
            Log.d(TAG, message);
        }

        @Override
        public void chatDidConnect() {
            insertMessage("Socket连接成功");
            /*if (!roomManger.isInRoom()) {*/


            if (isCreater) {
//                if (!isCreateRoom)
                createRoom();
//                roomManger.createRoom(mTitle, mSelfUserInfo.getRegion(), mCover);
            } else {
//                if (!isEnterRoom)
                enterRoom(mRoomId + "");
            }
        }

        @Override
        public void chatDidDisconnect() {
            insertMessage("Socket断开成功");
        }

        @Override
        public void chatDidOpenRoom() {
            insertMessage("打开房间成功");
            // 保存正在直播的状态(以课程ID为value)
            UiPreference.putString(mUserInfo.getIdStr(), mCourseId);

            lastConnectRoomTime = System.currentTimeMillis();

            if (connectMicManager != null) {
                connectMicManager.refreshLinkUsers();
            }

            if (isCreater) {
                isCreateRoom = true;

                if (isStartLiveSuccess) {
                    hideLoading();
                }

                if (!isPushReady) {
                    hideLoading();
                    if (isTestLive) {
                        // 直播测试
                        getTestPushUrl();
                    } else {
                        getPushUrl();
                    }

                }
            }
//            roomManger.queryRoomData();
//            refreshRommDataUI();
        }

        @Override
        public void chatDidEnterRoom(BytesReader.Anchor anchor) {
            insertMessage(String.format("进入房间成功 主播:%s 房间总人数:%d 金币数:%d", anchor.nickName, anchor.audienceCount, anchor.coin));
            lastConnectRoomTime = System.currentTimeMillis();
//            isQueryingRoomUser = false;
            roomAudience = anchor.audienceCount;
            roomCoins = anchor.coin;
            refreshRommDataUI();

            timerRoomData = TIMER_ROOM_DATA;
            timerRoomUser = TIMER_ROOM_USER;
            if (!isCreater) {
                isEnterRoom = true;
                initTimer();
                isFromScroll = false;
            }

            if (connectMicManager != null) {
                connectMicManager.refreshLinkUsers();
            }
        }

        @Override
        public void chatDidExitRoom() {
            insertMessage("离开房间成功");
        }

        @Override
        public void chatUserExitRoom(String userID) {
            handleRemoveManagerWhenSomeUserExitRoom(userID);
            insertMessage(String.format("用户离开房间:%s", userID));
        }

        @Override
        public void chatDidReceiveMessage(BytesReader.AudienceMessage message) {
            insertMessage(String.format("收到消息 来自:%s 类型:%d 内容:%s 等级:%d id:%s time:%d  type:%d",
                    message.nickName, message.type, message.content, message.level, message.audienceID, message.time, message.type));
            if (message.type == 0) {
//                if (isResume()) {
//                    ChatMsg chatMsg = createMessagee(message.content, false, message.time, message.audienceID, message.nickName, "");
//                    saveMessage(chatMsg, true);
//                }
                addSystemNotice(message.content);

            } else if (message.type == 1) {
                ChatEntity item = new ChatEntity();
                item.setUserId(message.audienceID);
                item.setSenderName(message.nickName);
                item.setLevel(message.level);
                item.setContent(message.content);
                addCommentToList(item);

                //测试显示弹幕
//                mVDanmu.addDanmu(item);
//                addDanmuCome(message.nickName,message.level,message.audienceID);
//                addDanmu("",message.nickName,message.content,message.audienceID,message.level);
            } else if (message.type == 2) {//关注消息
                ChatEntity item = new ChatEntity();
                item.setLevel(message.level);
                item.setSenderName(message.nickName);
                item.setContent(message.content);
                item.setType(ChatEntity.MSG_TYPE_ATTENTION);
                addCommentToList(item);
            } else if (message.type == 3) {//分享消息
                ChatEntity item = new ChatEntity();
                item.setLevel(message.level);
                item.setSenderName(message.nickName);
                item.setContent(message.content);
                item.setType(ChatEntity.MSG_TYPE_SHARE);
                addCommentToList(item);
            } else {
                ChatEntity item = new ChatEntity();
                item.setLevel(message.level);
                item.setSenderName(message.nickName);
                item.setContent(message.content);
                item.setType(ChatEntity.MSG_TYPE_SYSTEM);
                addCommentToList(item);
            }
        }


        @Override
        public void chatReceiveLike(String userID, int count) {
            insertMessage(String.format("用户点赞:%s 点赞数:%d", userID, count));

//            BytesReader.GiftMessage message = new BytesReader.GiftMessage();
//            message.giftID = "1";
//            giftAnimManger.addGift(message);//测试动画

            if (userID.equals(myUserId)) {
                return;
            }
            addLike(userID, count);
        }

        @Override
        public void chatReceiveGift(BytesReader.GiftMessage message) {
            insertMessage(String.format("收到礼物 来自:%s id:%s 连送数:%d 用户ID:%s", message.nickName, message.giftID, message.count, message.audienceID));

            if (Utils.listIsNullOrEmpty(GiftResManager.getInstance().getGiftList())) {
                GiftResManager.getInstance().checkGiftUpdate();
                Log.d(TAG, "触发更新礼物");
                return;
            }
            if (message.type == RoomManager.TLV_REQ_UNICAST_GIVE) {
                if (isResume()) {
                    ChatMsg giftMessagee = BaseActivity.createGiftMessagee("送" + GiftResManager.getInstance().getUnitByGiftId(message.giftID + "") + GiftResManager.getInstance().getNameByGiftId(message.giftID + ""), false, message.time, message.audienceID, message.nickName, "", Integer.parseInt(message.giftID));
                    BaseActivity.saveMessage(giftMessagee, false);
                }
            } else if (message.type == RoomManager.TLV_REQ_MULTICAST_ROOM_GIVE) {

                flashCnt++;
                //插入礼物评论
                ChatEntity item = new ChatEntity();
                item.setUserId(message.audienceID);
                item.setSenderName(message.nickName);
                item.setLevel(message.level);
                item.setContent("送了" + GiftResManager.getInstance().getUnitByGiftId(message.giftID) + GiftResManager.getInstance().getNameByGiftId(message.giftID));
                item.setGiftRes(GiftResManager.getInstance().getChatResByGiftId(message.giftID));
                item.setGiftUrl(GiftResManager.getInstance().getImageResByGiftId(message.giftID));
                item.setType(ChatEntity.MSG_TYPE_GIFT);
                addCommentToList(item);
                //处理礼物动画区显示
                if (isResume() && isPortrait) {//竖屏才显示礼物
                    giftShowView.showGift(message);
                    giftAnimManger.addGift(message);
                }

                if (!Utils.isEmpty(message.audienceID)) {
                    if (message.audienceID.equals(mSelfUserInfo.getId() + "")) {
                        //我送的，刷新连送进度
                        if (isPortrait) {
                            giftGridView.refreshSpecialBarProgress(message);
                        } else {
                            giftGridViewLand.refreshSpecialBarProgress(message);
                        }


                        totalGiveConins += message.coin;
                        Log.d(TAG, "总送出 totalGiveConins=" + totalGiveConins + " message.coin=" + message.coin);
                    }
                }
            }
        }

        @Override
        public void chatReceiveBullet(BytesReader.BulletMessage message) {
            insertMessage(String.format("收到弹幕 来自:%s 内容:%s 头像:%s", message.nickName, message.content, message.avatar));
//            addDanmu(message.avatar, message.nickName, message.content, message.audienceID, message.level);
            //同时显示在评论栏
            ChatEntity item = new ChatEntity();
            item.setFromUserAvatar(message.avatar);
            item.setUserId(message.audienceID);
            item.setSenderName(message.nickName);
            item.setLevel(message.level);
            item.setContent(message.content);
            addCommentToList(item);
            //显示弹幕 不显示弹幕
//            if (isResume())
//                mVDanmu.addDanmu(item);
        }

        @Override
        public void chatDidQueryRoomUser(int number, ArrayList<BytesReader.Audience> audiences) {
//            insertMessage(String.format("查询房间用户信息成功,返回人数:%d", number) + " page=" + curRoomUserPage);
//            isQueryingRoomUser = false;
//            handler.removeCallbacks(resetQueryingRoomUserRunnable);
//            refreshRommUserUI(audiences);
        }

        @Override
        public void chatDidQueryRoomData(long coin, int audienceCount) {
//            insertMessage(String.format("查询房间信息成功,返回主播总金币数:%d, 房间总人数:%d", coin, audienceCount));
            roomCoins = coin;
            roomAudience = audienceCount;
            refreshRommDataUI();
        }

        @Override
        public void chatDidQueryUserInfo(BytesReader.Audience audience) {
//            insertMessage(String.format("查询用户信息成功:%s 头像:%s 认证:%c 级别:%d", audience.nickName, audience.avatar, audience.auth, audience.level));
        }

        @Override
        public void chatDidCloseRoom(String roomId) {
            insertMessage("关闭房间成功 roomId=" + roomId + " mHostIdentifier=" + mHostIdentifier);
            if (!isCreater) {
                if (mHostIdentifier.equals(roomId)) {
                    isZhiboEnd = true;
                    insertMessage("收到主播关闭 当前房间通知");
                    zhiboEnd(END_TYPE_ZHUBO);
                } else {
                    insertMessage("收到主播关闭 不是当前房间");
                }
            } else {
                if (mHostIdentifier.equals(roomId)) {
                    isZhiboEnd = true;
                    insertMessage("收到主播关闭 当前房间通知");
                    zhiboEnd(END_TYPE_ZHUBO);
                }
            }
        }

        @Override
        public void chatUserEnterRoom(BytesReader.EnterRoomMessage message) {
//            insertMessage(String.format("用户进入房间:%s 级别:%d showPlace:%d", message.nickName, message.level, message.showPlace));

            int showPlace = message.showPlace;
            if (showPlace == 0) {//显示在评论
                //插入用户来了评论
                showComeComment(message);
//                showTuhaoCome(message.nickName, message.level, message.audienceID);
            } else if (showPlace == 1) {//显示在弹幕
                showComeComment(message);

                //隐藏进房特效
//                showTuhaoCome(message.nickName, message.level, message.audienceID);
//                addDanmuCome(message.nickName, message.level, message.audienceID);
            }
        }

        @Override
        public void chatReceiveLight(String userID, String nickName, int level) {
            insertMessage(String.format("点亮了用户ID::%s 昵称:%s 级别:%d", userID, nickName, level));
            if (!userID.equals(myUserId)) {
                addLike(userID, 1);
            }
            //插入用户点亮了评论
//            ChatEntity item = new ChatEntity();
//            item.setUserId(userID);
//            item.setSenderName(nickName);
//            item.setLevel(level);
//            item.setContent(ResourceHelper.getString(R.string.dianliangle));
//            item.setType(ChatEntity.MSG_TYPE_LIKE);
//            addCommentToList(item);
        }

        @Override
        public void chatErrorOccur(int errcode, final String errMsg) {
            insertMessage(String.format("发生错误:%d 描述:%s", errcode, errMsg));
            if (errcode == RoomManager.CHAT_ERROR_SERVER_DISCONNECT) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastHelper.showToast(errMsg);
                    }
                });
            } else if (errcode == RoomManager.TLV_E_USER_NOT_IN_ANY_ROOM) {
                //主播已退出：
                Log.d(TAG, "用户不在任何房间");
                if (!isCreater) {
//                    isZhiboEnd = true;
//                    zhiboEnd(END_TYPE_NO_VIDEO);
                    enterRoom(mHostIdentifier);
                } else {
                    if (!isCreateRoom) {
                        createRoom();
                        Log.d(TAG, "没创建过房间 createRoom");
                    } else {
                        Log.d(TAG, "已创建过房间 主播退出");
                        zhiboEnd(END_TYPE_USER_NOT_IN_ANY_ROOM);
//                        showToast(R.string.user_not_in_any_room);
//                        onCloseVideo();
                    }
                }
            } else if (errcode == RoomManager.TLV_E_ROOM_NOT_EXISTS) {
                Log.d(TAG, "房间不存在");
                if (!isCreater) {
                    //主播已退出：
                    if (isFromScroll) {
                        //go next
                        EventBusManager.postEvent(null, AUTO_GO_NEXT_ANCHOR_EVENT);
                    } else {
                        isZhiboEnd = true;
                        zhiboEnd(END_TYPE_ZHUBO);
                    }
                } else {
                    //主播退出
//                    showToast(R.string.room_not_exist);
//                    onCloseVideo();
                    zhiboEnd(END_TYPE_ROOM_NOT_EXIST);
                }
            } else if (errcode == RoomManager.TLV_E_KAZUAN_NOT_ENOUGH) {
                Log.d(TAG, " TLV_E_KAZUAN_NOT_ENOUGH");
                if (isPortrait) {
                    giftGridView.hideMultiGiftMode();
                    showRechargeDialog(errMsg);
                } else {
                    giftGridViewLand.hideMultiGiftMode();
                    showRechargeDialogLand(errMsg);
                }

                if ("1".equals(errMsg)) {
                    AnalyticsReport.onEvent(LiveApplication.getInstance(), LiveReport.MY_LIVE_EVENT_11248);
                } else if ("2".equals(errMsg)) {
//                    AnalyticsReport.onEvent(LiveApplication.getInstance(), LiveReport.MY_LIVE_EVENT_11250);
                }
//                if(mChatMessagePanel.getVisibility()==View.GONE)


            } else if (errcode == RoomManager.TLV_RSP_FORBID_OPEN_ROOM) {
                Log.d(TAG, "发直播被禁止");
//                showForbitLive();
                LiveFinishActivity.startActivity(getContext(), true, "", END_TYPE_FORBID, mCourseId);
                finish();
            } else if (errcode == RoomManager.TLV_E_USER_IN_USER_ROOM) {
                Log.d(TAG, "TLV_E_USER_IN_USER_ROOM 12 退出直播");
//                showToast(R.string.user_in_other_room);
//                onCloseVideo();
                zhiboEnd(END_TYPE_USER_IN_OTHER_ROOM);
            } else if (errcode == RoomManager.TLV_E_USER_ALREADY_IN_OTHER_ROOM) {
                Log.d(TAG, "TLV_E_USER_ALREADY_IN_OTHER_ROOM 8 退出直播");
//                showToast(R.string.user_in_other_room);
//                onCloseVideo();
                zhiboEnd(END_TYPE_USER_IN_OTHER_ROOM);
            } else if (errcode == RoomManager.TLV_E_USER_NOT_BUY_COURSE) {
                Log.d(TAG, "未购买课程");
                showToast("未购买课程");
                finish();
            } else {
                ToastHelper.showToast(errMsg);
            }
        }

        @Override
        public void chatDidSuccess(int code) {

        }

        @Override
        public void chatOffline(List<ChatMsg> messages) {

        }
    };

    private void addAnalyticsReport(String errMsg) {
        if ("1".equals(errMsg)) {//送礼余额不足
            AnalyticsReport.onEvent(LiveApplication.getInstance(), LiveReport.MY_LIVE_EVENT_11249);
        } else if ("2".equals(errMsg)) {
//                    AnalyticsReport.onEvent(LiveApplication.getInstance(), LiveReport.MY_LIVE_EVENT_11249);
        }
    }

    public void showRechargeDialog(final String errMsg) {
        showButtonDialog(R.string.go_recharge_page_title, R.string.need_recharge, R.string.go_recharge_page, R.string.cancel,
                true, true, new IDialogOnClickListener() {
                    @Override
                    public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                        if (viewId == GenericDialog.ID_BUTTON_YES) {
                            MyCoinsActivity.startActivity(getActivity());
                            addAnalyticsReport(errMsg);
                        }
                        return false;
                    }
                });
    }


    TextView tvDialogTitle, tvDialogContent;
    Button btnDialogNo, btnDialogYes;

    public void showRechargeDialogLand(final String errMsg) {

        if (isDestroyed()) {
            return;
        }
        Log.d(TAG, " showRechargeDialogLand");
        rlDialog.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams lp = rlDialog.getLayoutParams();
        lp.height = mRootView.getWidth();
        lp.width = mRootView.getHeight();
        rlDialog.setLayoutParams(lp);
//            int newX = -(rlDialog.getHeight() - rlDialog.getWidth()) / 2;
//            int newY = (rlDialog.getHeight() - rlDialog.getWidth()) / 2;
//            rlDialog.setX(-(rlDialog.getHeight() - rlDialog.getWidth()) / 2);
//            rlDialog.setY((rlDialog.getHeight() - rlDialog.getWidth()) / 2);
        int newX = -(mRootView.getHeight() - mRootView.getWidth()) / 2;
        int newY = (mRootView.getHeight() - mRootView.getWidth()) / 2;
        rlDialog.setX(-(mRootView.getHeight() - mRootView.getWidth()) / 2);
        rlDialog.setY((mRootView.getHeight() - mRootView.getWidth()) / 2);
        Log.d(TAG, " height=" + lp.height + " width=" + lp.width
                + " x=" + rlDialog.getX() + " y=" + rlDialog.getY() + " newX=" + newX + " newY=" + newY);
        rlDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlDialog.setVisibility(View.GONE);
            }
        });

        tvDialogTitle.setText(R.string.go_recharge_page_title);
        tvDialogContent.setText(R.string.need_recharge);
        btnDialogNo.setText(R.string.cancel);
        btnDialogYes.setText(R.string.go_recharge_page);
        btnDialogYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlDialog.setVisibility(View.GONE);
                MyCoinsActivity.startActivity(getActivity());
            }
        });
        rlDialog.setVisibility(View.VISIBLE);

        if (!isCreater && mFrontCamera) {
            rlDialog.setRotation(-90);
        } else {
            rlDialog.setRotation(90);
        }

//        showButtonDialog(R.string.go_recharge_page_title, R.string.need_recharge, R.string.go_recharge_page, R.string.cancel,
//                true, true, new IDialogOnClickListener() {
//                    @Override
//                    public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
//                        if (viewId == GenericDialog.ID_BUTTON_YES) {
//                            MyCoinsActivity.startActivity(getActivity());
//                            addAnalyticsReport(errMsg);
//                        }
//                        return false;
//                    }
//                });
    }


    private void showComeComment(BytesReader.EnterRoomMessage message) {
        ChatEntity item = new ChatEntity();
        item.setUserId(message.audienceID);
        item.setSenderName(message.nickName);
        item.setLevel(message.level);
        item.setContent(getString(R.string.enter_room));
        item.setType(ChatEntity.MSG_TYPE_COME);
        addCommentToList(item);
    }

    //    private void addDanmuCome(BytesReader.EnterRoomMessage message) {
//    private void addDanmuCome(String nickName, int level, String audienceID) {
//        List<Danmu> danmus = new ArrayList<>();
//        Danmu danmu = new Danmu(0, 2, "Come", "", nickName, getString(R.string.jin_lai_le));
//        danmu.level = level;
//        if (audienceID.equals(AccountInfoManager.getInstance().getCurrentAccountUserIdStr())) {
//            danmu.isGuest = false;
//        } else {
//            danmu.isGuest = true;
//        }
//        danmus.add(danmu);
//    }

//    private void addDanmu(String avatar, String nickName, String content, String audienceID, int level) {
//        List<Danmu> danmus = new ArrayList<>();
//        Danmu danmu = new Danmu(0, 2, "Comment", avatar, nickName, content);
//        danmu.level = level;
//        if (audienceID.equals(AccountInfoManager.getInstance().getCurrentAccountUserIdStr())) {
//            danmu.isGuest = false;
//        } else {
//            danmu.isGuest = true;
//        }
//        danmus.add(danmu);
//        mDanmuControl.addDanmuList(danmus);
//    }

    private void refreshRommUserUI(ArrayList<BytesReader.Audience> audiences) {
        if (Utils.listIsNullOrEmpty(audiences)) {
            return;
        }

        List<UserInfo> resultUsers = new ArrayList<>();
        for (int i = 0; i < audiences.size(); i++) {
            BytesReader.Audience audience = audiences.get(i);
            if (audience.id.equals(mHostIdentifier)) {//不显示主播头像在观众栏
                continue;
            }
            UserInfo user = new UserInfo();
            user.setAvatar(audience.avatar);
            try {
                user.setId(Integer.parseInt(audience.id));
            } catch (NumberFormatException e) {
                user.setId(0);
            }

            user.setAuth((short) audience.auth);
            user.setLevel(audience.level);
            resultUsers.add(user);
        }
        if (curRoomUserPage == 0) {
            //替换第一页的
            for (int i = 0; i < roomUserpageSize - 1; i++) {
                if (!Utils.listIsNullOrEmpty(mAudiences))
                    mAudiences.remove(0);
            }
            mAudiences.addAll(0, resultUsers);
        } else {
            mAudiences.addAll(resultUsers);
        }
        mAudienceAdapter.notifyDataSetChanged();

    }

    private void refreshRommDataUI() {
//        if (isCreater) {
//            if (roomAudience < 10000) {
//                tvAudienceCnt.setText(String.valueOf(roomAudience) + "在线看");
//            } else {
//                tvAudienceCnt.setText(String.valueOf(roomAudience) + "人");
//            }
//        } else {
        tvAudienceCntNow.setText(String.valueOf(roomAudience));
//        tvAudienceCntNow.setText(String.valueOf(roomAudience) + "人观看");
//        if (roomAudience < 10000) {
//            tvAudienceCnt.setText(String.valueOf(roomAudience) + "在线看");
//        } else {
//            tvAudienceCnt.setText(String.valueOf(roomAudience) + "人");
//        }
//        }
        tvKazuan.setText(String.valueOf(roomCoins));

        CacheUtil.addCache("totalCoins", roomCoins);
        CacheUtil.addCache("totalAudience", roomAudience);
    }

    private void initTimer() {
        Log.d(TAG, "开启定时器");
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        mTimer = new Timer();
        roomTimerTask = new TimerTask() {
            @Override
            public void run() {
                timerRoomData++;
                timerRoomUser++;

                if (isResume()) {
                    if (timerRoomData > TIMER_ROOM_DATA) {
                        timerRoomData = 0;
                        roomManger.queryRoomData();
                    }
//                    if (timerRoomUser > TIMER_ROOM_USER) {
//                        timerRoomUser = 0;
//                        if (!isQueryingRoomUser)
//                            queryRoomUser(0);
//                    }
                }
//                LiveRoomActivity.this.runOnUiThread(addSecondRunnable);
                handler.post(addSecondRunnable);
            }
        };
        mTimer.scheduleAtFixedRate(roomTimerTask, 0, 1000);
    }


    Runnable addSecondRunnable = new Runnable() {
        @Override
        public void run() {
            addSecond();
        }
    };

    private boolean isFromScroll = false;

    private void initIntentData() {
        Bundle bundle = getArguments();
        mRoomId = bundle.getInt(Common.EXTRA_ROOM_ID, 0);
        mHostIdentifier = bundle.getString(Util.EXTRA_IDENTIFIER);
        isCreater = bundle.getBoolean(Common.EXTRA_IS_CREATER, false);
        Log.log("initIntentData:" + isCreater);
        mTitle = bundle.getString(Common.EXTRA_TITLE);
        topics = bundle.getString(Common.EXTRA_TOPIC);
        mCover = bundle.getString(Common.EXTRA_COVER);
        mCourseId = bundle.getString(Common.EXTRA_COURSE_ID);
//        downUrl = intent.getStringExtra(Common.DOWN_URL);
        mAvatar = bundle.getString(Common.AVATAR);

        isFromScroll = bundle.getBoolean(Common.IS_FROM_SCROLL, false);

        if (!isCreater) {
            streamId = bundle.getString(Common.STREAM_ID);
            channelId = bundle.getString(Common.CHANNEL_ID);
            if (Utils.isEmpty(streamId)) {
                ToastHelper.showToast("没有streamId");
                onCloseVideo();
                return;
            }
            if (Utils.isEmpty(channelId)) {
                ToastHelper.showToast("没有channelId");
                onCloseVideo();
                return;
            }
        }

        CacheUtil.addCache("liveTitle", mTitle);
        Log.debug(TAG, "intent mRoomId=" + mRoomId + " mHostIdentifier=" + mHostIdentifier + " isCreater=" + isCreater
                + " mCover=" + mCover + " mAvatar=" + mAvatar
                + " streamId=" + streamId + " channelId=" + channelId);

        if (!Utils.listIsNullOrEmpty(chatList)) {
            chatList.clear();
            notifyComment();
        }

        UserInfo user = new UserInfo();
        user.setId(Integer.parseInt(mHostIdentifier));
        //保存当前主播
        CacheUtil.addCache("curZhuboUserInfo", user);

        if (!isCreater) {
            if (releaseLiveView != null && releaseLiveView.getVisibility() == View.VISIBLE) {
                vpContainer.setVisibility(View.VISIBLE);
                releaseLiveView.setVisibility(View.GONE);
            }
            if (llBottomBar != null) {
                llBottomBar.setVisibility(View.GONE);
            }
        } else {

        }

        getCourseDetail();
    }

    // 获取课程详情
    private void getCourseDetail() {

        DataProvider.getCourseDetail(this, mCourseId, new GsonHttpConnection.OnResultListener<CourseDetailMsg>() {

            @Override
            public void onSuccess(CourseDetailMsg msg) {

                if (msg != null && msg.data != null && msg.data.course != null) {
                    mCourse = msg.data.course;
                    //记录上次直播时间(继续直播的才需要记录时间)
                    if (mCourse.getStatus() == Course.LIVING) {
                        totalTime = (int) ((System.currentTimeMillis() / 1000 - msg.data.getBeginTime()));
                    } else {
                        totalTime = 0;
                    }
                    Log.d(TAG, " 已直播时间 totalTIme=" + totalTime);
                }

            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {

            }

        });
    }

    QavsdkControl mQavsdkControl;
    //////新直播sdk开始
    ViewLive vlBigView;
    //推流
    private ZegoLivePusher mLivePusher;
    //    private TextureView mCaptureView;
    //    private TXLivePushConfig mLivePushConfig;
//    private TXLivePusher mLivePusher;
//    private TXCloudVideoView mCaptureView;
    private boolean mVideoPublish;
    private boolean mFrontCamera = true;
    private boolean mFlashTurnOn = false;
    private boolean mTouchFocus = true;
    private int mBeautyLevel = 35/*8*/;
    private int mWhiteningLevel = 70/*3*/;
    //    private TXLivePlayConfig mPlayConfig;
    //播放
    private ZegoLivePlayer mLivePlayer = null;
    //    private TextureView mPlayerView;
    //    private TXLivePlayer mLivePlayer = null;
//    private TXCloudVideoView mPlayerView;
    private boolean mVideoPlay;
    private boolean mVideoPause = false;
    private boolean mHWDecode = false;
    private int decodeFailTimes = 0;
    //    private static final int CACHE_STRATEGY_FAST = 1;  //极速
//    private static final int CACHE_STRATEGY_SMOOTH = 2;  //流畅
//    private static final int CACHE_STRATEGY_AUTO = 3;  //自动
//    private static final int CACHE_TIME_FAST = 1;
//    private static final int CACHE_TIME_SMOOTH = 5;
//    private static final int CACHE_TIME_AUTO_MIN = 5;
//    private static final int CACHE_TIME_AUTO_MAX = 10;
    //    private static final int CACHE_TIME_AUTO_MIN = 5;
//    private static final int CACHE_TIME_AUTO_MAX = 10;
//    private int mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_FLV;
    private int mCurrentRenderMode;
    private int mCurrentRenderRotation;

    /**
     * 初始化腾讯云
     */
    private void initAvSdk() {
        mQavsdkControl = LiveApplication.getInstance().getQavsdkControl();
        if (connectMicManager == null) {
            connectMicManager = new ConnectMicManager(this, mLiveSmallView, isCreater, mHostIdentifier);
        }
        connectMicManager.start();
        if (isCreater) {
            mFrontCamera = UiPreference.getBoolean(Common.KEY_IS_FRONT_CAMERA, true);
            isEnableBeauty = UiPreference.getBoolean(Common.KEY_IS_ENABLE_BEAUTY, false);
            Log.d(TAG, "摄像头状态 mFrontCamera=" + mFrontCamera + " isEnableBeauty=" + isEnableBeauty);
            refreshBtnBeauty();
//            mCaptureView = (TextureView) findViewById(R.id.texture_view);
            mVideoPublish = false;

            //推流
            ZegoLivePusher.Builder build = new ZegoLivePusher.Builder();
            build.setLiveChannel(mHostIdentifier);
            build.setPublishStreamID(mHostIdentifier);
            build.setUserId(mHostIdentifier);
//            build.setUserName(mHostIdentifier);
            build.setLocalView(vlBigView);
            mLivePusher = build.build();
            mLivePusher.setFrontCamera(mFrontCamera);
            mLivePusher.setmActivity(getActivity());
            mLivePusher.enableMic(true);
            setPushListener();
            if (vlBigView != null) {
//                mCaptureView.setVisibility(View.VISIBLE);
//                mCaptureView.setCameraFront(mFrontCamera);
                mLivePusher.setFrontCam(mFrontCamera);
            }
            setBeautyEnable();
            mLivePusher.startPreview();
//            mLivePusher.startCameraPreview(mCaptureView);
            Log.d(TAG, "首先打开摄像头预览");
//            hideLoading();
//            showLoading();


        } else {
            showLoading();
            //拉流
//            mPlayerView = (TextureView) findViewById(R.id.texture_view);
            initLivePlayer();

            connectMicManager.setChannelId(channelId);

            //同步最新地址
            syncPlayUrl();

            if (startPlayRtmp()) {
                mVideoPlay = true;
            }

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "5秒后判断是否开始下载礼物 curQuality=" + curQuality);
                    if (curQuality == 0 || curQuality == 1) {
                        EventBusManager.postEvent(0, SubcriberTag.START_DOWNLOAD_GIFT_RES);
                    }
                }
            }, 5000);
        }
    }

    private void initLivePlayer() {
        Log.d(TAG, "initLivePlayer");
        ZegoLivePlayer.Builder build = new ZegoLivePlayer.Builder();
        build.setUserName(mHostIdentifier);
        build.setUserId(mHostIdentifier);
        build.setLiveChannel(channelId);
        build.setStreamID(streamId);
        build.setRemoteView(vlBigView);
//            build.setRemoteView(mPlayerView);
//            build.setRemoteViewIndex(ZegoAVKitCommon.ZegoRemoteViewIndex.First);
        mLivePlayer = build.build();
    }


    private void syncPlayUrl() {
        isLoadingPlayUrl = true;
        DataProvider.queryRoom(this, mHostIdentifier, new GsonHttpConnection.OnResultListener<QueryRoomMsg>() {
            @Override
            public void onSuccess(QueryRoomMsg queryRoomMsg) {
                isLoadingPlayUrl = false;
                isSyncPlayUrl = true;
                if (queryRoomMsg.isSuccessFul()) {
                    String newStreamId = queryRoomMsg.getStreamId();
                    String newChannelId = queryRoomMsg.getChannelId();
                    long beginTime = queryRoomMsg.getBeginTime();
                    Log.d(TAG, "syncPlayUrl newStreamId=" + newStreamId + " streamId=" + streamId + " newChannelId=" + newChannelId + " channelId=" + channelId
                            + " beginTime=" + beginTime + " curTime=" + System.currentTimeMillis());
                    refeshLiveTime(beginTime);
                    if ((!Utils.isEmpty(newStreamId) && !newStreamId.equals(streamId)) || (!Utils.isEmpty(newChannelId) && !newChannelId.equals(channelId))) {
                        Log.d(TAG, " streamId或channelId有更新 streamId=" + streamId + " newStreamId=" + newStreamId);
                        mLivePlayer.reset(newStreamId, newChannelId);
                        streamId = newStreamId;
                        channelId = newChannelId;
                        startPlayRtmp();
                    }


//                    else if (!Utils.isEmpty(newChannelId) && !newChannelId.endsWith(channelId)) {
//                        Log.d(TAG, " channelId有更新 channelId=" + channelId + " newChannelId=" + newChannelId);
//                        streamId = newStreamId;
//                        channelId = newChannelId;
//                        startPlayRtmp();
//                    }
                    else {
                        Log.d(TAG, "streamId和channelId没更新");
                    }

//                    String lastesetUrl = queryRoomMsg.getDownUrl();
//                    if (!Utils.isEmpty(lastesetUrl)) {
//                        if (lastesetUrl.equals(downUrl)) {
//                            Log.d(TAG, "最新地址和rtmpUrl 一致");
//                        } else {
//                            Log.d(TAG, "最新地址和rtmpUrl 不一致，重新拉流");
//                            showLoading();
//                            stopPlayRtmp();
//                            downUrl = lastesetUrl;
//                            if (startPlayRtmp()) {
//                                mVideoPlay = true;
//                            }
//                        }
//                    } else {
//                        Log.d(TAG, "没有lastesetUrl");
//                        zhiboEnd(END_TYPE_ZHUBO);
//                    }
                } else {
                    Log.d(TAG, "syncPlayUrl onFail errorMsg=" + queryRoomMsg.getError() + " code=" + queryRoomMsg.getCode());
                    ToastHelper.showToast(R.string.load_zhubo_fail);
                    onCloseVideo();
                }
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                isLoadingPlayUrl = false;
                Log.d(TAG, "syncPlayUrl onFail errorMsg=" + errorMsg + " errorCode=" + errorCode);
                ToastHelper.showToast(R.string.load_zhubo_fail);
                onCloseVideo();
            }
        });
    }

    private void refeshLiveTime(long beginTime) {
        if (beginTime > 0) {
            long curTime = System.currentTimeMillis();
            if (curTime > beginTime) {
                totalTime = (int) (curTime / 1000 - beginTime);
            }
        }
    }

    int retryStartRecordTimes = 0;

    private void startRecord() {
        DataProvider.startRecord(this, new GsonHttpConnection.OnResultListener<StartRecordMsg>() {
            @Override
            public void onSuccess(StartRecordMsg msg) {
                if (msg.isSuccessFul()) {
                    taskId = msg.getTaskId();
                    Log.d(TAG, "startRecord onSuccess taskId=" + msg.getTaskId());
                } else {
                    retryStartRecord();
                    Log.d(TAG, "startRecord onFail errorMsg=" + msg.getError() + " errorCode=" + msg.getCode());
                }
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                retryStartRecord();
                Log.d(TAG, "startRecord onFail errorMsg=" + errorMsg + " errorCode=" + errorCode);
            }
        });
    }

    private void retryStartRecord() {
        retryStartRecordTimes++;
        if (retryStartRecordTimes < 5) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startRecord();
                }
            }, 1000);
        }

    }

    boolean isPushReady = false;

    private void getPushUrl() {

        Log.d(TAG, "getPushUrl mCourseId=" + mCourseId);

        DataProvider.openLive(this, mCourseId, new GsonHttpConnection.OnResultListener<OpenLiveMsg>() {
            @Override
            public void onSuccess(OpenLiveMsg openLiveMsg) {
                if (openLiveMsg.isSuccessFul()) {
                    Log.d(TAG, "getPushUrl onSuccess isPushReady=" + isPushReady);
                    if (isPushReady) {
                        return;
                    }
                    isPushReady = true;
//                  rtmpUrl = openLiveMsg.getUpstreamAddress();
                    String liveId = openLiveMsg.getLiveId();
                    String streamId = openLiveMsg.getStreamId();
                    String channelId = openLiveMsg.getChannelId();
                    Log.d(TAG, " openLive成功 liveId=" + liveId
                            + " streamId=" + streamId + " channelId=" + channelId);
                    mLivePusher.setLiveId(liveId, streamId, channelId);
                    connectMicManager.setChannelId(channelId);

                    if (pushCheckTimer != null) {
                        pushCheckTimer.cancel();
                        pushCheckTimer = null;
                    }
                    startPublishRtmp();
                    pushCheckTimer = new Timer();
                    pushCheckTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (!isPushing) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        addSystemNotice("直播服务连接重试，请检查网络");
                                    }
                                });
                                Log.d(TAG, " 重连直播服务器");
//                                startPublishRtmp();
                            }
                        }
                    }, 5000, 5000);

//                    hideLoading();
                    boolean isUploadLocation = UiPreference.getBoolean(Common.IS_UPLOAD_LOCATION, true);
                    mLocation = isUploadLocation ? UiPreference.getString(Common.KEY_MY_LOCATION_CITY,
                            mSelfUserInfo.getRegion()) : "";
                    hideCircleLoading();
                    roomView.setVisibility(View.VISIBLE);
                    releaseLiveView.setVisibility(View.GONE);
                    vpContainer.setVisibility(View.VISIBLE);
                    showCountDown();

//                    createRoom();
//                    checkInitRoom();


                } else {
                    Log.d(TAG, "openLive onFail errorMsg=" + openLiveMsg.getError() + " errorCode=" + openLiveMsg.getCode());
                    if (openLiveMsg.getCode() == 30) {
                        showForbitLive();
                        return;
                    }
//                    closeActivity();
                    showToast("未连接上服务器，请调整网络后尝试");
                }
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                Log.d(TAG, "openLive onFail1 errorMsg=" + errorMsg + " errorCode=" + errorCode);
                if (errorCode == 30) {
                    showForbitLive();
                    return;
                }
                showToast("未连接上服务器，请调整网络后尝试");
//                showLiveFailDialog();
//                closeActivity();
//                if (!Utils.isEmpty(rtmpUrl)) {
//                    Log.d(TAG, "获取地址失败用原地址继续推流");
//                    startPublishRtmp();
//                } else {
//                    Log.d(TAG, "获取地址失败退出");
//                    zhiboEnd(END_TYPE_NET);
//                }
//                showToast(R.string.network_error_tips);
//                onCloseVideo();
            }
        });
    }

    private void getTestPushUrl() {

        Log.d(TAG, "getTestPushUrl mCourseId=" + mCourseId);

        DataProvider.openTestLive(this, 0, new GsonHttpConnection.OnResultListener<OpenLiveMsg>() {
            @Override
            public void onSuccess(OpenLiveMsg openLiveMsg) {
                if (openLiveMsg.isSuccessFul()) {
                    Log.d(TAG, "getTestPushUrl onSuccess isPushReady=" + isPushReady);
                    if (isPushReady) {
                        return;
                    }
                    isPushReady = true;
//                  rtmpUrl = openLiveMsg.getUpstreamAddress();
                    mCourseId = openLiveMsg.getCourseId();
                    String liveId = openLiveMsg.getLiveId();
                    String streamId = openLiveMsg.getStreamId();
                    String channelId = openLiveMsg.getChannelId();
                    Log.d(TAG, " openLive成功 liveId=" + liveId
                            + " streamId=" + streamId + " channelId=" + channelId);
                    mLivePusher.setLiveId(liveId, streamId, channelId);
                    connectMicManager.setChannelId(channelId);

                    if (pushCheckTimer != null) {
                        pushCheckTimer.cancel();
                        pushCheckTimer = null;
                    }

                    startPublishRtmp();
                    pushCheckTimer = new Timer();
                    pushCheckTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (!isPushing) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        addSystemNotice("直播服务连接重试，请检查网络");
                                    }
                                });
                                Log.d(TAG, " 重连直播服务器");
//                                startPublishRtmp();
                            }
                        }
                    }, 5000, 5000);

//                    hideLoading();
                    boolean isUploadLocation = UiPreference.getBoolean(Common.IS_UPLOAD_LOCATION, true);
                    mLocation = isUploadLocation ? UiPreference.getString(Common.KEY_MY_LOCATION_CITY,
                            mSelfUserInfo.getRegion()) : "";
                    hideCircleLoading();
                    roomView.setVisibility(View.VISIBLE);
                    releaseLiveView.setVisibility(View.GONE);
                    vpContainer.setVisibility(View.VISIBLE);
                    showCountDown();

                    // 创建房间
//                    createRoom();
//                    checkInitRoom();


                } else {
                    Log.d(TAG, "openTestLive onFail errorMsg=" + openLiveMsg.getError() + " errorCode=" + openLiveMsg.getCode());
                    if (openLiveMsg.getCode() == 30) {
                        showForbitLive();
                        return;
                    }
//                    closeActivity();
                    showToast("未连接上服务器，请调整网络后尝试");
                }
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                Log.d(TAG, "openTestLive onFail1 errorMsg=" + errorMsg + " errorCode=" + errorCode);
                if (errorCode == 30) {
                    showForbitLive();
                    return;
                }
                showToast("未连接上服务器，请调整网络后尝试");
//                showLiveFailDialog();
//                closeActivity();
//                if (!Utils.isEmpty(rtmpUrl)) {
//                    Log.d(TAG, "获取地址失败用原地址继续推流");
//                    startPublishRtmp();
//                } else {
//                    Log.d(TAG, "获取地址失败退出");
//                    zhiboEnd(END_TYPE_NET);
//                }
//                showToast(R.string.network_error_tips);
//                onCloseVideo();
            }
        });
    }

    private void createRoom() {
//        if (!isPushReady) {
//            return;
//        }
        if (!checkNeedConnect())
            return;

        Log.d(TAG, " mTitle=" + mTitle + " mLocation=" + mLocation + " mCover=" + mCover + " mCourseId=" + mCourseId);
        roomManger.createRoom(mTitle, mLocation, mCover, Long.parseLong(mCourseId));

    }


    private void setBeautyEnable() {
//        if (mQavsdkControl != null) {
//            mQavsdkControl.setEnableBeauty(isEnableBeauty);
//        }
        if (isEnableBeauty) {
            Log.d(TAG, "setBeautyEnable 开启美颜");
            mLivePusher.enableBeautifying(ZegoRoomUtil.getZegoBeauty(3));
//            if (!mLivePusher.setBeautyFilter(mBeautyLevel, mWhiteningLevel)) {
//                Toast.makeText(this, R.string.unsupport_beauty,
//                        Toast.LENGTH_SHORT).show();
//            } else {
//                Log.d(TAG, "setBeautyEnable 开启美颜成功");
//            }
        } else {
            Log.d(TAG, "setBeautyEnable 关闭美颜");
            mLivePusher.enableBeautifying(ZegoRoomUtil.getZegoBeauty(0));
//            mLivePusher.setBeautyFilter(0, 0);
        }
    }

    String rtmpUrl;//推流地址

    private LiveCallback mITXLivePushListener;

    private void setPushListener() {
        if (mITXLivePushListener == null) {
            mITXLivePushListener = new LiveCallback() {
                @Override
                public void onPublishSucc() {
                    if (pushCheckTimer != null) {
                        pushCheckTimer.cancel();
                        pushCheckTimer = null;
                    }
                    Log.d(TAG, "LiveCallback onPublishSucc");
                    hideLoading();
                    isReconnecting = false;
                    isPushing = true;
                    pushLiveSuccess();
                    if (!isStartRecordSuccess) {
                        startRecord();
                        isStartRecordSuccess = true;
                    }
                }

                @Override
                public void onPublishStop(int retCode) {
                    Log.d(TAG, "LiveCallback onPublishStop retCode=" + retCode);
                    if (pushCheckTimer != null) {
                        pushCheckTimer.cancel();
                        pushCheckTimer = null;
                    }
//                    if (retCode != 1) {
//                        showStopLiveDialog(R.string.live_push_network_connect_error);
//                    }
                    isPushing = false;
                    isPushReady = false;
//                    roomView.setVisibility(View.GONE);
                    releaseLiveView.setVisibility(View.VISIBLE);
                    vpContainer.setVisibility(View.GONE);
                    showToast("请检查网络再重试直播");
//                    if (isResume()) {
//                        addSystemNotice(ResourceHelper.getString(R.string.live_reconnect));
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                startPublishRtmp();
//                            }
//                        }, 200);
//                    }
//                    Log.d(TAG, "LiveCallback onPublishStop");
                }

                @Override
                public void onPlaySucc() {
                    Log.d(TAG, "LiveCallback onPlaySucc");
                }

                @Override
                public void onPlayStopEvent(int retCode, String streamID, String liveChannel) {
                    Log.d(TAG, "LiveCallback onPlayStop");
                }

                @Override
                public void onVideoSizeChanged(int width, int height) {
                    Log.d(TAG, "LiveCallback onVideoSizeChanged width=" + width + " height=" + height);
                }

                @Override
                public void onCaptureVideoSize(int width, int height) {
//                    if (mCaptureView.getVisibility() != View.VISIBLE) {
//                        mCaptureView.setVisibility(View.VISIBLE);
//                        hideLoading();
//                    }
                    if (vlBigView.getVisibility() != View.VISIBLE) {
                        vlBigView.setVisibility(View.VISIBLE);
                        hideLoading();
                    }
                    Log.d(TAG, "LiveCallback onCaptureVideoSize width=" + width + " height=" + height);
                }

                @Override
                public void onPlayQualityUpdate(int quality) {
                    Log.d(TAG, "LiveCallback onPlayQualityUpdate quality=" + quality);
                }

                @Override
                public void onPublishQulityUpdate(int quality) {
                    int speed = 0;
                    //网络:优:100 良:80 中:60 差:40 单位:kB/s
                    if (quality == 0) {//优
                        tempNotice = 0;
                        speed = 100;
                    } else if (quality == 1) {//良
                        tempNotice = 0;
                        speed = 80;
                    } else if (quality == 2) {//中
                        tempNotice = 0;
                        speed = 60;
//                        addSystemNotice(ResourceHelper.getString(R.string.live_push_network_warn));
                    } else if (quality == 3) {//差
                        speed = 40;
//                        showStopLiveDialog(R.string.live_push_network_connect_error);
//                        if (tempNotice % 3 == 0) {
//                            addSystemNotice(ResourceHelper.getString(R.string.live_push_network_warn));
//                        }
//                        tempNotice++;
                    }
                    speedTemps.add(speed);
                    int cpu = Utils.readUsage();
                    sendPushStatis(false, cpu, (byte) 15, (short) speed);
                    if (speedTemps.size() >= 20) {
                        int totalSpeed = 0;
                        for (Integer item :
                                speedTemps) {
                            totalSpeed += item;
                        }
                        int speedAvg = totalSpeed / speedTemps.size();
                        if (speedAvg == 40) {
//                            showToast(R.string.live_push_network_not_meat);
                            showStopLiveDialog(R.string.live_push_network_connect_error);
//                            onCloseVideo();  //退出直播
                        }
                        Log.d(TAG, "LiveCallback onPublishQulityUpdate sendPushStatis cpu=" + 50 + " speed=" + speed);
                        speedTemps.clear();
                    }

                    Log.d(TAG, "LiveCallback onPublishQulityUpdate quality=" + quality);


                }
            };
//            mITXLivePushListener = new ITXLivePushListener() {
//                @Override
//                public void onPushEvent(int event, Bundle param) {
//                    String msg = param.getString(TXLiveConstants.EVT_DESCRIPTION);
//                    if (mLivePusher != null) {
//                        mLivePusher.onLogRecord("[event:" + event + "]" + msg + "\n");
//                    }
//                    //错误还是要明确的报一下
//                    Log.d(TAG, "onPushEvent event=" + event + " msg=" + msg);
//                    if (event == TXLiveConstants.PUSH_ERR_NET_DISCONNECT) {
//                        isPushing = false;
//                        showStopLiveDialog(R.string.live_push_network_connect_error);//放弃治疗
//                    } else if (event == TXLiveConstants.PUSH_EVT_OPEN_CAMERA_SUCC) {
//                        isPushing = false;
//                        hideLoading();
//                        QavsdkControl.initCameraParams(mLivePusher);
//                    }
//                    else if (event == TXLiveConstants.PUSH_EVT_PUSH_BEGIN) {
//                        isReconnecting = false;
//                        isPushing = true;
//                        pushLiveSuccess();
//                        if (isCreateRoom)
//                            hideLoading();
//                        if (!isStartRecordSuccess) {
//                            startRecord();
//                            isStartRecordSuccess = true;
//                        }
//                    } else if (event == TXLiveConstants.PUSH_WARNING_RECONNECT) {
//                        if (!isReleaseReady) {
//                            return;
//                        }
//                        isReconnecting = true;
//                        showLoading();
//                    } else if (event == TXLiveConstants.PUSH_WARNING_NET_BUSY) {
//                    } else if (event == TXLiveConstants.PUSH_ERR_UNSUPPORTED_RESOLUTION) {
//                        //不支持该分辨率
//                        Log.d(TAG, "不支持该分辨率 降低分辨率预览");
//                        mLivePushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640);
//                        mLivePusher.setConfig(mLivePushConfig);
//                    } else if (event == TXLiveConstants.PUSH_ERR_OPEN_MIC_FAIL) {
//                        showNoCameraOrMicDialog(R.string.mic_problem_check);
//                    } else if (event == TXLiveConstants.PUSH_ERR_OPEN_CAMERA_FAIL) {
//                        showNoCameraOrMicDialog(R.string.camera_problem_check);
//                    } else if (event < 0) {
//                        if (!isReleaseReady) {
//                            return;
//                        }
//                        isPushing = false;
//                        showLoading();
//                        showToast(R.string.live_reconnect);
//                        stopPublishRtmp();
//                        mVideoPublish = false;
//                        Log.d(TAG, "重新开始推流2");
//                        startPublishRtmp();
//                    }
//                }
//
//                @Override
//                public void onNetStatus(Bundle status) {
//                    String str = LakaUtil.getNetStatusString(status);
//                    checkPushUploadSpeed(status);
//                    Log.d(TAG, "Current status: " + str);
//                }
//            };
        }
        mLivePusher.setLiveCallback(mITXLivePushListener);
    }

    private static final int MAX_JIT = 1000;//上行抖动最高
    private static final int MAX_CAS = 200;//最大缓冲区
    private static final int MIN_UPLOAD_SPEED = 100;//最低上行速度
    private static final int MIN_NORMAL_VIBRATE = 100;//上行抖动最低标准
    private static final int MIN_VIDEO_BIT_FPS = 18;//最少每帧速率

    //    private static final int MAX_VIDEO_BIT_FPS_SQUARE = 10;//最大抖动标准差
    private int pushUploadNotEnoughTimes = 0;//上行均值不足次数
    private double avgSpeed, avgSpeedFPS, squareSpeed;
    private List<Integer> speedTemps = new ArrayList<>();
    private int tempNotice = 0;
    private List<Double> fpsTemps = new ArrayList<>();

//    private void checkPushUploadSpeed(Bundle status) {
//        double JIT = status.getInt(TXLiveConstants.NET_STATUS_NET_JITTER);
//        double CAS = status.getInt(TXLiveConstants.NET_STATUS_CACHE_SIZE);
//        double SPD = status.getInt(TXLiveConstants.NET_STATUS_NET_SPEED);
//        double FPS = status.getInt(TXLiveConstants.NET_STATUS_VIDEO_FPS);
//        String cpu = status.getString(TXLiveConstants.NET_STATUS_CPU_USAGE);
//        int avgCpu =0;
//
//        if(isReconnecting){//重连时发一次
//            isReconnecting = false;
//            sendPushStatis(true,  cpu, (byte) FPS,(short) SPD);
//        }
//
//        if (FPS == 0 || !isPushing) {
//            return;
//        }
////        double speed = SPD / FPS;
//        fpsTemps.add(FPS);
//        speedTemps.add(SPD);
//        if(speedTemps.size() ==3){
//            sendPushStatis(false,  cpu, (byte) FPS,(short) SPD);
//        }
//
//
//        if (speedTemps.size() == 5) {
//            int totalSpeed = 0;
//            for (Double item :
//                    speedTemps) {
//                totalSpeed += item;
//            }
//
//            int totalFPS = 0;
//            for (Double item :
//                    fpsTemps) {
//                totalFPS += item;
//            }
//
//            //抖动判断
//            avgSpeed = totalSpeed / speedTemps.size();
//
//            //均值判断
//            avgSpeed = totalSpeed / speedTemps.size();
//
//            //每帧均值判断
//            avgSpeedFPS = totalSpeed / totalFPS;
//
//            //标准差差判断
//            squareSpeed = Utils.getStandardDiviation(speedTemps);
//
//            if (avgSpeed < 100 && squareSpeed < 100) {
//                //提示框退出直播
//                showStopLiveDialog(R.string.live_push_network_not_meat);
//                return;
//            } else if (avgSpeedFPS < MIN_VIDEO_BIT_FPS) {
//                pushUploadNotEnoughTimes++;
//            } else if (JIT > MAX_JIT) {
//                pushUploadNotEnoughTimes++;
//            } else if (CAS > MAX_CAS) {
//                pushUploadNotEnoughTimes++;
//            }
////                else if(squareSpeed>MAX_VIDEO_BIT_FPS_SQUARE){
////                    pushUploadNotEnoughTimes++;
////                }
//            else {
//                pushUploadNotEnoughTimes = 0;//恢复重置
//            }
//            speedTemps.clear();
//            fpsTemps.clear();
//            Log.d(TAG, "10秒内平均 avgSpeed=" + avgSpeed + " avgSpeedFPS=" + avgSpeedFPS + " pushUploadNotEnoughTimes=" + pushUploadNotEnoughTimes
//                    + " squareSpeed=" + squareSpeed);
//
////                if (pushUploadNotEnoughTimes > 2) {
////                    pushUploadNotEnoughTimes = 0;
////                    addSystemNotice(ResourceHelper.getString(R.string.push_upload_retry_later));
////                } else
//            if (pushUploadNotEnoughTimes > 0) {
//                addSystemNotice(ResourceHelper.getString(R.string.live_push_network_warn));
//            }
//
//        }
//
////        addSystemNotice(R.string.push_upload_not_enough);
//    }

    private void sendPushStatis(boolean isRetry, int cpu, byte FPS, short SPD) {
//        int avgCpu = 0;
//        if (!Utils.isEmpty(cpu)) {
//            cpu = cpu.replace("%", "");
//            String[] cpus = cpu.split("/");
//            int size = cpus.length;
//            int total = 0;
//            for (int i = 0; i < size; i++) {
//                total += Integer.valueOf(cpus[i]);
//            }
//            avgCpu = total / size;
//            Log.d(TAG, " avgCpu=" + avgCpu + " cpu=" + cpu);
//        }
        roomManger.sendAnchorData(isRetry, (byte) cpu, FPS, SPD);
    }


    private void showStopLiveDialog(int msg) {
        if (true) {//1.2.0改为不退出直播
            return;
        }

        showButtonDialog(R.string.wenxin_tips, msg, R.string.yes, 0,
                false, false, true, true, new IDialogOnClickListener() {
                    @Override
                    public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                        if (viewId == GenericDialog.ID_BUTTON_YES) {
                            onCloseVideo();
                        }
                        return false;
                    }
                });
    }

    private void startPublishRtmp() {
//        if (TextUtils.isEmpty(rtmpUrl) || (!rtmpUrl.trim().toLowerCase().startsWith("rtmp://"))) {
//            return;
//        }
        Log.d(TAG, "开始推流 startPublishRtmp ");
        setPushListener();
//        mLivePushConfig.setTouchFocus(true);
//        mLivePusher.setConfig(mLivePushConfig);
//        mLivePusher.startPusher(rtmpUrl.trim());
//        mLivePusher.setLogLevel(TXLiveConstants.LOG_LEVEL_DEBUG);
        mLivePusher.startPublish();
        mVideoPublish = true;
    }

    private void stopPublishRtmp() {
        if (mLivePusher != null) {
//            mLivePusher.setPushListener(null);
//            mLivePusher.stopPusher();
            mLivePusher.setLiveCallback(null);
            mLivePusher.stopPublish();
        }
        Log.d(TAG, "stopPublishRtmp");
    }

    private void pushLiveSuccess() {
        //设置可滑动
        vpContainer.setScrollble(true);
//        llBg.setVisibility(View.INVISIBLE);
        isGetVideoSuccess = true;
        if (isCreater) {
            isStartLiveSuccess = true;
            AnalyticsReport.onEvent(getContext(), LiveReport.MY_LIVE_EVENT_11228_s);
            initTimer();//主播开启timer
        }
    }

    private LiveCallback livePlayListener;

    private void setLivePlayListener() {
        if (livePlayListener == null) {
            livePlayListener = new LiveCallback() {
                @Override

                public void onPublishSucc() {
                    Log.d(TAG, "LiveCallback onPublishSucc");
                    updateCameraDevice();
                }

                @Override
                public void onPublishStop(int retCode) {
                    Log.d(TAG, "LiveCallback onPublishStop retCode=" + retCode);
                }

                @Override
                public void onPlaySucc() {
                    retryPlayTimes = 0;
                    Log.d(TAG, "LiveCallback onPlaySucc");
                }

                @Override
                public void onPlayStopEvent(int retCode, String streamID, String liveChannel) {
                    Log.d(TAG, "LiveCallback onPlayStop retCode=" + retCode + " streamID=" + streamID + " liveChannel=" + liveChannel);

                    if (retCode == 9) {
                        mLivePlayer.setLoginChannel(false);
                        mLivePlayer.startPlay();
                        return;
                    }

                    if (isLoadingPlayUrl) {
                        return;
                    }

                    if (retryPlayTimes < 2 && 6 == retCode || 5 == retCode) {//重新获取地址并重试
                        retryPlayTimes++;
                        showLoading();
                        syncPlayUrl();
                        return;
                    }
                    if (retCode != 1) {
                        if (isSyncPlayUrl) {
                            Log.d(TAG, " 已同步过地址,重试播放");
                            startPlayRtmp();
//                            ToastHelper.showToast(R.string.load_zhubo_fail);
//                            onCloseVideo();
                        } else {
                            Log.d(TAG, " 未同步过地址,同步");
                            syncPlayUrl();
                        }

                    }
//                stopPlayRtmp();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mVideoPlay = false;
//                        Log.d(TAG, "重新拉流");
//                        showLoading();
//                        mLivePlayer.startPlay();
//                    }
//                }, 2000);
                }

                @Override
                public void onVideoSizeChanged(int width, int height) {
//                    mPlayerView.setVisibility(View.VISIBLE);
//                    if (vlBigView != null) {
//                        vlBigView.setVisibility(View.GONE);
//                    }
                    hideLoading();
                    Log.d(TAG, "LiveCallback onVideoSizeChanged width=" + width + " height=" + height);
                }

                @Override
                public void onCaptureVideoSize(int width, int height) {
                    Log.d(TAG, "LiveCallback onCaptureVideoSize width=" + width + " height=" + height);
                }

                @Override
                public void onPlayQualityUpdate(int quality) {
                    Log.d(TAG, "LiveCallback onPlayQualityUpdate quality=" + quality);
                    curQuality = quality;
                    if (quality == 2 || quality == 3) {
                        EventBusManager.postEvent(0, SubcriberTag.STOP_DOWNLOAD_GIFT_RES);
                    }

                }

                @Override
                public void onPublishQulityUpdate(int quality) {
                    Log.d(TAG, "LiveCallback onPublishQulityUpdate quality=" + quality);
                }
            };
        }
        mLivePlayer.setLiveCallback(livePlayListener);
    }

    private void updateCameraDevice() {
        Log.d(TAG, " 更新摄像头方向 mFrontCamera=" + mFrontCamera);
        Map<String, String> mapUrls = new HashMap<>();
        mapUrls.put(Common.CAMERA_DEVICE_POSITION, String.valueOf(mFrontCamera ? 2 : 1));
        Gson gson = new Gson();
        String json = gson.toJson(mapUrls);
        mLivePusher.updateStreamExtraInfo(json);
    }

    private boolean startPlayRtmp() {
        roomView.setVisibility(View.VISIBLE);
        setLivePlayListener();
        mLivePlayer.startPlay();
//        mLivePlayer.setPlayListener(new ITXLivePlayListener() {
//            @Override
//            public void onPlayEvent(int event, Bundle param) {
//                Log.d(TAG, " onPlayEvent event=" + event);
//                if (event == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS || event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {//event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN ||
//                    hideLoading();
//                    if (isNeedChangeCache) {
//                        isNeedChangeCache = false;
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                setCacheStrategy(CACHE_STRATEGY_SMOOTH);//设置缓存策略
//                            }
//                        }, 3000);
//                    }
//
//                } else if (event == TXLiveConstants.PLAY_EVT_PLAY_LOADING) {
//                    showLoading();
//                } else if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT || event == 63235) {//|| event == TXLiveConstants.PLAY_EVT_PLAY_END
//                    stopPlayRtmp();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            mVideoPlay = false;
//                            Log.d(TAG, "重新拉流");
//                            showLoading();
//                            if (startPlayRtmp()) {
//                                mVideoPlay = true;
//                            }
//                            syncPlayUrl();
//                        }
//                    }, 2000);
//                } else if (event == TXLiveConstants.PLAY_WARNING_AUDIO_DECODE_FAIL || event == TXLiveConstants.PLAY_WARNING_VIDEO_DECODE_FAIL) {
//                    decodeFailTimes++;
//                    if (decodeFailTimes > 10) {
//                        stopPlayRtmp();
//                        mVideoPlay = false;
//                        showLoading();
//                        if (startPlayRtmp()) {
//                            mVideoPlay = true;
//                        }
//                        Log.d(TAG, "多次解码失败重新拉流");
//                    }
//                }
//            }
//
//            @Override
//            public void onNetStatus(Bundle bundle) {
//
//            }
//        });
//        mLivePlayer.setPlayerView(mPlayerView);
//        // 硬件加速在1080p解码场景下效果显著，但细节之处并不如想象的那么美好：
//        // (1) 只有 4.3 以上android系统才支持
//        // (2) 兼容性我们目前还仅过了小米华为等常见机型，故这里的返回值您先不要太当真
//        mLivePlayer.enableHardwareDecode(mHWDecode);
//        mLivePlayer.setRenderRotation(mCurrentRenderRotation);
//        mLivePlayer.setRenderMode(mCurrentRenderMode);
//        //设置播放器缓存策略
//        //这里将播放器的策略设置为自动调整，调整的范围设定为1到4s，您也可以通过setCacheTime将播放器策略设置为采用
//        //固定缓存时间。如果您什么都不调用，播放器将采用默认的策略（默认策略为自动调整，调整范围为1到4s）
//        //mLivePlayer.setCacheTime(5);
//        mLivePlayer.setConfig(mPlayConfig);
//        mLivePlayer.setLogLevel(TXLiveConstants.LOG_LEVEL_DEBUG);

//        if (mLivePlayer.startPlay(rtmpUrl, mPlayType) != 0) {
//            Log.d(TAG, " startPlay 返回！=0");
//            return false;
//        }
//        Log.d(TAG, " startPlay  return true");
        return true;
    }

    private void stopPlayRtmp() {
        Log.d(TAG, " stopPlayRtmp");
        if (!isCreater) {
//            if (llBg != null) {
//                llBg.setVisibility(View.VISIBLE);
//            }
        }

//        if (mCaptureView != null) {
//            mCaptureView.setVisibility(View.VISIBLE);
//        }
        if (vlBigView != null) {
            vlBigView.setVisibility(View.VISIBLE);
        }
        if (mLivePlayer != null) {
            mLivePlayer.setLiveCallback(null);
            mLivePlayer.stopPlay();
        }
    }

////新直播sdk结束


    public View findViewById(int id) {
        if (id == R.id.vp_container || id == R.id.content || id == R.id.ll_bg || id == R.id.view_release_live || id == R.id.vl_big_view || id == R.id.rl_dialog || id == R.id.rl_pause_live_dialog || id == R.id.btn_finish_live || id == R.id.btn_pause_live || id == R.id.iv_live_close) {//|| id == R.id.room_content id == R.id.texture_view ||
            return mRootView.findViewById(id);
        }

        return roomView.findViewById(id);
    }

    private void initView() {
        if (llBg != null) {
            return;
        }

        //暂停直播弹框
        rlPauseLiveDialog = (RelativeLayout) findViewById(R.id.rl_pause_live_dialog);
        rlPauseLiveDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlPauseLiveDialog.setVisibility(View.GONE);
            }
        });
        btnPauseLive = (Button) findViewById(R.id.btn_pause_live);
        btnPauseLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isZhuboTemporaryLeave = true;
                roomManger.zhuboTemporaryLeave();
                finish();
            }
        });
        btnFinishLive = (Button) findViewById(R.id.btn_finish_live);
        btnFinishLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCloseVideo();
            }
        });
        ivCloseLive = (ImageView) findViewById(R.id.iv_live_close);
        ivCloseLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlPauseLiveDialog.setVisibility(View.GONE);
            }
        });


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

        mLiveApplication = (LiveApplication) getActivity().getApplication();
        llBg = (LinearLayout) findViewById(R.id.ll_bg);
        vlBigView = (ViewLive) findViewById(R.id.vl_big_view);
        vpContainer = (ScrollbleViewPager) findViewById(R.id.vp_container);
//        //设置不可滑动
        vpContainer.setScrollble(false);
        contentAdapter = new ExpressionPagerAdapter(contentViews);
        emptyView = new LinearLayout(getContext());
        emptyView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        roomView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.view_room_content, null);
        contentViews.add(emptyView);
        contentViews.add(roomView);
        vpContainer.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int index) {
                if (index == 0) {
                    //全屏模式
                    AnalyticsReport.onEvent(getContext(), LiveReport.MY_LIVE_EVENT_11221);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        contentAdapter.notifyDataSetChanged();
        vpContainer.setAdapter(contentAdapter);
        vpContainer.setCurrentItem(1);
//        roomView = (RelativeLayout) findViewById(R.id.room_content);
//        roomView.setVisibility(View.GONE);

        rlLoading = (ScreenLoadingView) findViewById(R.id.loading);
        mCircleLoading = (LakaLoading) findViewById(R.id.circle_loading);
//        rlLoading.setVisibility(View.GONE);
//        mLoadingImageView = (LakaLoadingView) findViewById(R.id.loadingImageView);
//        loadingTextView = (TextView) findViewById(R.id.loadingTextView);
//        loadingTextView.setVisibility(View.GONE);
//        llAVVideo = (LinearLayout) findViewById(R.id.av_video_layer_ui);
//        llAVVideo.setVisibility(View.INVISIBLE);

        mSelfUserInfo = AccountInfoManager.getInstance().getAccountInfo();
//        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "TAG");
//        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "TAG");
//        TextView title = (TextView) findViewById(R.id.title);
//        title.setText(getIntent().getStringExtra(Common.EXTRA_TITLE));
        ivZhuboHead = (MarkSimpleDraweeView) findViewById(R.id.iv_head_zhubo);
//        mLoading = findViewById(R.id.loading);
        tvZhiboLive = (TextView) findViewById(R.id.tv_zhibo_live);
        rlComment = (LinearLayout) findViewById(R.id.rl_comment);
        rlComment.setVisibility(View.INVISIBLE);
        tvTimeTips = (TextView) findViewById(R.id.tv_time_tips);
//        tvTimeTips.setText("已进行");
//        tvTimer = (TextView) findViewById(R.id.tv_timer);
        tvTimer = (TextView) findViewById(R.id.my_current);
        findViewById(R.id.tv_time_divide).setVisibility(View.GONE);
        findViewById(R.id.my_total).setVisibility(View.GONE);
//        tvAudienceCnt = (TextView) findViewById(R.id.tv_audience_cnt);
        tvKazuan = (TextView) findViewById(R.id.tv_kazuan);
        Typeface fontFace = TypefaceHelper.getInstance().getTypeface(TypefaceHelper.TYPE_FACE_Georgia_Bold);
        if (fontFace == null) {
            fontFace = Typeface.createFromAsset(getContext().getAssets(),
                    "fonts/georgiab_0.ttf");
            TypefaceHelper.getInstance().putTypeface(TypefaceHelper.TYPE_FACE_Georgia_Bold, fontFace);
        }

        // 字钻不加粗
        //tvKazuan.setTypeface(fontFace);

        tvLakaNo = (TextView) findViewById(R.id.tv_lakano);
        mBtnForbid = (Button) findViewById(R.id.btn_forbid);
        btnClose = (ImageView) findViewById(R.id.close_btn);
        btnClose.setOnClickListener(this);
        etComment = (EditText) findViewById(R.id.et_comment);
        btnSendComment = (Button) findViewById(R.id.btn_send_comment);
        btnSendComment.setOnClickListener(this);
        btnDanmu = (Button) findViewById(R.id.btn_tanmu);
        btnDanmu.setOnClickListener(this);
        rlDanmu = (RelativeLayout) findViewById(R.id.rl_tanmu);
        rlDanmu.setOnClickListener(this);
        ivDanmu = (ImageView) findViewById(R.id.iv_tanmu);
        animDanmuOpen = ObjectAnimator.ofFloat(btnDanmu, "x", 0, Utils.dip2px(getContext(), 17)).setDuration(200);
        animDanmuClose = ObjectAnimator.ofFloat(btnDanmu, "x", Utils.dip2px(getContext(), 17), 0).setDuration(200);
        btnGift = (Button) findViewById(R.id.btn_gift);
        btnGift.setOnClickListener(this);
        rlTouch = (RelativeLayout) findViewById(R.id.rl_touch);

        btnFace = (Button) findViewById(R.id.btn_face);
        btnFace.setOnClickListener(this);

        emoticonPickerView = (EmoticonPickerView) findViewById(R.id.face_view);
        emoticonPickerView.setVisibility(View.GONE);

//        rlTouch.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.d(TAG,"rlTouch onTouch curTouchEvent");
//                curTouchEvent = event;
//                return false;
//            }
//        });;
//        periscopeLayout = (PeriscopeLayout) findViewById(R.id.periscope);
        rlTopBar = (RelativeLayout) findViewById(R.id.ll_topbar_zhubo);
        rlTopBarZhubo = (RelativeLayout) findViewById(R.id.rl_info_zhubo);
        rlTopBarAudience = (RelativeLayout) findViewById(R.id.rl_info_audience);
        rlKazuanBank = (RelativeLayout) findViewById(R.id.rl_kazuan);
        rlKazuanBank.setOnClickListener(this);

        etComment.setOnClickListener(this);
        //处理表情
        etComment.addTextChangedListener(new TextWatcher() {
            private int start;
            private int count;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                this.start = start;
                this.count = count;

                Editable editable = etComment.getText();
                int len = editable.length();
                if (len > maxLen) {
                    ToastHelper.showToast(R.string.textsize_max_130);
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    String str = editable.toString();
                    //截取新字符串
                    String newStr = str.substring(0, maxLen);
                    etComment.setText(newStr);
                    editable = etComment.getText();
                    //新字符串的长度
                    int newLen = editable.length();
                    //旧光标位置超过字符串长度
                    if (selEndIndex > newLen) {
                        selEndIndex = editable.length();
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(editable, selEndIndex);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                MoonUtil.replaceEmoticons(getContext(), s, start, count);
                int editEnd = etComment.getSelectionEnd();
                etComment.removeTextChangedListener(this);
//                while (StringUtil.counterChars(s.toString()) > 5000 && editEnd > 0) {
//                    s.delete(editEnd - 1, editEnd);
//                    editEnd--;
//                }
                etComment.setSelection(editEnd);
                etComment.addTextChangedListener(this);
                String content = s.toString();
//                if (Utils.isEmpty(content)) {
//                    btnSendComment.setBackgroundResource(R.drawable.round_8px_white);
//                } else {
//                    btnSendComment.setBackgroundResource(R.drawable.round_8px_ffda44);
//                }
            }
        });

        tvAudienceCntNow = (TextView) findViewById(R.id.tv_audience_cnt_now);
//        btnFollow = (Button) findViewById(R.id.btn_follow);
//        btnFollow.setOnClickListener(this);
        initUserInfoPanel();//个人资料弹窗
//        initChatSessionPanel();//私信会话弹窗


        rootView = (InputRelativeLayout) findViewById(R.id.root_view);
//qygxsq
//        etComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//
//                Log.d(TAG, "actionId=" + actionId);
//
//                return false;
//            }
//        });
        rlLvComment = (RelativeLayout) findViewById(R.id.rl_comment_touch);//rl_lv_comment

        RelativeLayout mRlLvContainer = (RelativeLayout) findViewById(R.id.rl_lv_comment);//rl_lv_comment
        RelativeLayout.LayoutParams lpLvComment = (RelativeLayout.LayoutParams) mRlLvContainer.getLayoutParams();

        DisplayMetrics displaysMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaysMetrics);
        float density = displaysMetrics.density; // 屏幕密度（0.75/1.0/1.5）
        int densityDpi = displaysMetrics.densityDpi; // 屏幕密度DPI（120/160/240）
        Log.d(TAG, "density=" + density + " densityDpi=" + densityDpi);
        if (densityDpi >= 480) {
            lpLvComment.height = Utils.dip2px(getContext(), 22) * 5;
        } else if (densityDpi >= 240) {
            lpLvComment.height = Utils.dip2px(getContext(), 25) * 5;
        } else {
            lpLvComment.height = Utils.dip2px(getContext(), 25) * 5;
        }
        mRlLvContainer.setLayoutParams(lpLvComment);

//        rlLvComment.setOnClickListener(this);
        rlLvComment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                EventBusManager.postEvent(event, SubcriberTag.ON_TOUCH_VIDEO_VIEW);
                return false;
            }
        });


        mMsgRedPoint = findViewById(R.id.msg_red_poit);
        mZhuboMsgRedPoint = findViewById(R.id.msg_red_poit_zhubo);

        mLiveSmallView = (LiveSmallView) findViewById(R.id.view_live_small);

        sdvHotActivity = (SimpleDraweeView) findViewById(R.id.hot_activity);
        sdvHotActivity.setOnClickListener(this);

//        showAdministrator();

        if (Common.IS_HIDE_PART_FUNCTION) {
            hidePartFunction();
        }


    }

    private void hidePartFunction() {
        if (rlKazuanBank != null)
            rlKazuanBank.setVisibility(View.GONE);
    }

    private void showAdministrator() {

        if (Ioc.get(UserInfo.class).isAdministrator()) {

            mBtnForbid.setVisibility(View.VISIBLE);
            mBtnForbid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActionSheetPanel panel = new ActionSheetPanel(getActivity());
                    ActionSheetPanel.ActionSheetItem item = new ActionSheetPanel.ActionSheetItem();
                    item.id = String.valueOf(1);
                    item.title = "禁播1天";
                    panel.addSheetItem(item);
                    item = new ActionSheetPanel.ActionSheetItem();
                    item.id = String.valueOf(2);
                    item.title = "禁播7天";
                    panel.addSheetItem(item);
                    item = new ActionSheetPanel.ActionSheetItem();
                    item.id = String.valueOf(3);
                    item.title = "永久禁播";
                    panel.addSheetItem(item);
                    item = new ActionSheetPanel.ActionSheetItem();
                    item.id = String.valueOf(4);
                    item.title = "警告";
                    panel.addSheetItem(item);
                    panel.setSheetItemClickListener(new ActionSheetPanel.OnActionSheetClickListener() {
                        @Override
                        public void onActionSheetItemClick(String id) {
                            String type = "0";
                            String title = "";
                            if (id.equals("1")) {
                                type = "1";
                                title = "确定禁播1天?";
                            } else if (id.equals("2")) {
                                type = "7";
                                title = "确定禁播7天?";
                            } else if (id.equals("3")) {
                                type = "3";
                                title = "确定永久禁播?";
                            } else if (id.equals("4")) {
                                type = "2";
                                title = "确定警告主播?";
                            }
//                            showButtonDialog(R.string.account_title);
                            final String finalType = type;
                            showButtonDialog("提示", title, R.string.yes, R.string.cancel,
                                    true, true, true, true, new IDialogOnClickListener() {
                                        @Override
                                        public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                                            if (viewId == GenericDialog.ID_BUTTON_YES) {
                                                DataProviderRoom.roomManageOp(this, mHostIdentifier, finalType, new GsonHttpConnection.OnResultListener() {
                                                    @Override
                                                    public void onSuccess(Object o) {
                                                        Msg result = (Msg) o;
                                                        if (result.isSuccessFul()) {
                                                            showToast("操作成功");
                                                        } else {
                                                            showToast(result.getError());
                                                        }
                                                    }

                                                    @Override
                                                    public void onFail(int errorCode, String errorMsg, String command) {
                                                        showToast(errorMsg);
                                                    }
                                                });
                                            }
                                            return false;
                                        }
                                    });
                        }
                    });
                    panel.showPanel();
                }
            });
        }
    }

    private void initLocation() {
        if (!isCreater) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
    }

    private void layoutRlLrc() {
        mRlLrc.postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewParent viewParent = mRlLrc.getParent();
                if (viewParent != null) {
                    viewParent.requestLayout();
                }
            }
        }, 50L);
    }

    private void initChatSessionPanel() {
        mChatSessionPanel = new ChatSessionPanel(getContext());
    }

    private void initAudioEffectPanel() {
        mAudioEffectPanel = new AudioEffectPanel(getContext());
    }

    private void initChatUnfollowPanel() {
        mChatUnfollowPanel = new ChatUnfollowPanel(getContext());
    }

//    private void initContributionPanel() {
////        if (!isCreater) {
////            ContributionListActivity.startActivity(this, mHostIdentifier, RankingItemView.FROM_TYPE_USER_INFO);
////            AnalyticsReport.onEvent(this, LiveReport.MY_LIVE_EVENT_11232);
////        } else {
////            ContributionListActivity.startActivity(this, mHostIdentifier);
////        }
//        if (isCreater) {
//            mContributionPanel = new ContributionListPanel(this, this, mHostIdentifier, RankingItemView.FROM_TYPE_MINE);
//        } else {
//            mContributionPanel = new ContributionListPanel(this, this, mHostIdentifier, RankingItemView.FROM_TYPE_USER_INFO);
//        }
//
//    }

    private boolean isEnableBeauty = false;

    private void initFilterPanel() {
        mFilterPanel = new GPUImageFilterPanel(getContext());
        /*mFilterPanel.setOnFilterListener(new GPUImageFilterPanel.OnFilterListener() {
            @Override
            public void onFilter(GPUImageFilter filter) {
                if (filter != null) {
                }
            }
        });*/

    }

    private void initChatMessagePanel() {
        mChatMessagePanel = (ChatMessageView) findViewById(R.id.message_view);
        View bgView = new View(getContext());
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

    private void initUserInfoPanel() {
        mUserInfoPanel = new LiveRoomUserInfoPanel(getContext(), (BaseActivity) getActivity(), mCourseId);
        mUserInfoPanel.setLivingHostId(mHostIdentifier);
        mUserInfoPanel.setRoomId(String.valueOf(mRoomId));
        mUserInfoPanel.setOnLiveRoomListener(new LiveRoomUserInfoPanel.OnLiveRoomListener() {
            @Override
            public void onClickForbiddenButton(String userId) {
                handleOnClickForbiddenButton(userId);
            }

            @Override
            public void onClickCancelForbiddenButton(String userId) {
                handleOnClickCancelForbiddenButton(userId);
            }

            @Override
            public void onClickManagerButton(UserInfo userInfo, boolean isCancel) {
                if (isCancel) {
                    handleOnClickCancelManagerButton(userInfo);
                } else {
                    handleOnClickAddManagerButton(userInfo);
                }
            }
        });
    }

    protected void showLoading() {
        long curPushResumeTime = System.currentTimeMillis();
        if (mLastShowLoadingTime == 0) {
            mLastShowLoadingTime = curPushResumeTime;
            return;
        } else if (curPushResumeTime - mLastShowLoadingTime < 1000 * 15) {
            Log.d(TAG, " mLastShowLoadingTime 间隔太短");
            return;
        }
        mLastShowLoadingTime = curPushResumeTime;

        showCircleLoading(R.string.crazy_loading_please_wait);
    }

    private void showLoading(int msgRes) {
        showCircleLoading(msgRes);
    }

    private void hideLoading() {
//        if (rlLoading!=null&&rlLoading.getVisibility() == View.VISIBLE) {
//            rlLoading.setVisibility(View.GONE);
//            rlLoading.stopRoll();
//        }
//        if (llBg != null)
//            llBg.setVisibility(View.INVISIBLE);
        hideCircleLoading();
    }

    protected void showCircleLoading(int msgRes) {
        Log.d(TAG, " showCircleLoading");
        if (mCircleLoading != null && mCircleLoading.getVisibility() == View.GONE) {
            mCircleLoading.show(msgRes);
        }
    }

    private void hideCircleLoading() {
        Log.d(TAG, " hideCircleLoading");
        if (mCircleLoading != null && mCircleLoading.getVisibility() == View.VISIBLE) {
            mCircleLoading.hide();
        }
    }


    /**
     * 初始化礼物选框
     */
    private void initGiftView() {
        giftGridView = (GiftGridView) findViewById(R.id.gift_grid_view);
        giftGridView.setOnClickListener(this);
        giftGridView.setListeners(this, this, true);
        giftGridView.setVisibility(View.GONE);
        giftGridView.setBalance(AccountInfoManager.getInstance().getCurrentAccountCoins());

        giftGridViewLand = (GiftGridViewLand) findViewById(R.id.gift_grid_view_land);
        giftGridViewLand.setOnClickListener(this);
        giftGridViewLand.setListeners(this, new GiftGridViewLand.GiftGridListener() {
            @Override
            public void setChooseGift(GiftInfo gift) {
                chooseGift = gift;
            }
        }, true);
        giftGridViewLand.setVisibility(View.GONE);
        giftGridViewLand.setBalance(AccountInfoManager.getInstance().getCurrentAccountCoins());

    }


    /**
     * 根据主播、观众显示对应界面
     */
    private void refreshRoleUI() {
//        Log.d(TAG,"intent refreshRoleUI isCreater="+isCreater);
        if (isCreater) {
            btnGoods = (Button) findViewById(R.id.btn_goods_zhubo);
            btnGoods.setOnClickListener(this);
            goodsPanel = (GoodsPanel) findViewById(R.id.goods_panel);
            goodsPanel.initData((BaseActivity) getActivity(), mCourseId);

            mBgmManager = BgmManager.getInstance();
//            KSYBgmPlayer.getInstance().setAudioTrackVolume(0f);
            //插入准备界面
            initReleaseLiveView();
            // 显示配方按钮
            Button formulaZhubo = (Button) findViewById(R.id.btn_formula_zhubo);
            formulaZhubo.setVisibility(View.VISIBLE);
            formulaZhubo.setOnClickListener(this);
            vpContainer.setVisibility(View.GONE);
            btnShare = (Button) findViewById(R.id.btn_share);
            btnShare.setOnClickListener(this);
            btnBgm = (Button) findViewById(R.id.btn_bgm);
            btnBgm.setVisibility(View.GONE);
            btnBgm.setOnClickListener(this);
//            btnShare.setVisibility(View.GONE);
//            btnMusic = (Button) findViewById(R.id.btn_music);
//            btnMusic.setVisibility(View.GONE);
//            btnMusic.setOnClickListener(this);
            btnComment = (Button) findViewById(R.id.btn_comment);
            btnComment.setOnClickListener(this);
            btnCamera = (Button) findViewById(R.id.btn_camera);
            btnCamera.setOnClickListener(this);
            btnMic = (Button) findViewById(R.id.btn_mic);
            btnMic.setOnClickListener(this);
            llBottomBar = (LinearLayout) findViewById(R.id.ll_bottombar_zhubo);
            btnOrientation = (Button) findViewById(R.id.btn_orientation);
            btnOrientation.setOnClickListener(this);
//            rlTopBarZhubo.setVisibility(View.VISIBLE);
//            rlTopBarAudience.setVisibility(View.GONE);
//            btnFollow.setVisibility(View.GONE);
            rlTopBarZhubo.setVisibility(View.GONE);
            rlTopBarAudience.setVisibility(View.VISIBLE);
//            rlTopBarAudience.setOnClickListener(this);
            btnMore = (Button) findViewById(R.id.btn_more);
            btnMore.setOnClickListener(this);
            btnLetter = (Button) findViewById(R.id.btn_letter_zhubo);
            btnLetter.setVisibility(View.GONE);
            btnLetter.setOnClickListener(this);

            rlDanmu.setVisibility(View.GONE);
//            rlTouch.setVisibility(View.GONE);
            llBottomBar.setOnClickListener(this);//屏蔽对焦点击
            rlTopBar.setOnClickListener(this);//屏蔽对焦点击

            ivLineLeft = (ImageView) findViewById(R.id.iv_line_left);
            ivLineLeft.setVisibility(View.GONE);

            btnConnectZhubo = (Button) findViewById(R.id.btn_connect_zhubo);
            btnConnectZhubo.setOnClickListener(this);
            flConnectZhubo = (FrameLayout) findViewById(R.id.fl_connect_zhubo);
            flConnectZhubo.setOnClickListener(this);
            // 主播不显示连麦
            // flConnectZhubo.setVisibility(View.VISIBLE);
            mZhuboLinkRedpoint = findViewById(R.id.iv_connect_red_poit_zhubo);
            mZhuboLinkRedpoint.setVisibility(View.GONE);

        } else {
            btnGoods = (Button) findViewById(R.id.btn_goods_audience);
            btnGoods.setOnClickListener(this);
            goodsPanel = (GoodsPanel) findViewById(R.id.goods_panel);
            goodsPanel.initData((BaseActivity) getActivity(), mCourseId);

            // 显示配方按钮
            Button formula = (Button) findViewById(R.id.btn_formula);
            formula.setVisibility(View.VISIBLE);
            formula.setOnClickListener(this);
            vpContainer.setScrollble(true);
//            rlTouch.setOnClickListener(this);
            btnGiftAudience = (Button) findViewById(R.id.btn_gift_audience);
            btnGiftAudience.setOnClickListener(this);
            btnShare = (Button) findViewById(R.id.btn_share_audience);
            btnShare.setOnClickListener(this);
            //后台配置不显示分享
            if (!SystemConfig.getInstance().isShowShare()) {
                btnShare.setVisibility(View.GONE);
            }
            btnComment = (Button) findViewById(R.id.btn_comment_audience);
            btnComment.setOnClickListener(this);
            btnLetter = (Button) findViewById(R.id.btn_letter);
            btnLetter.setVisibility(View.GONE);
            btnLetter.setOnClickListener(this);
//            rlDanmu.setVisibility(View.VISIBLE);
            llBottomBar = (LinearLayout) findViewById(R.id.ll_bottombar_audience);
            btnOrientation = (Button) findViewById(R.id.btn_orientation_audience);
            btnOrientation.setOnClickListener(this);
            rlTopBarZhubo.setVisibility(View.GONE);
            rlTopBarAudience.setVisibility(View.VISIBLE);
            rlTopBarAudience.setOnClickListener(this);

            ivConnectRedAudience = (ImageView) findViewById(R.id.iv_connect_red_poit_audience);
            btnConnectAudience = (Button) findViewById(R.id.btn_connect_audience);
            btnConnectAudience.setOnClickListener(this);
            flConnectAudience = (FrameLayout) findViewById(R.id.fl_connect_audience);
            // 用户端不显示连麦
            // flConnectAudience.setVisibility(View.VISIBLE);

        }
        if (!Utils.isEmpty(mAvatar))
            ImageUtil.loadImage(ivZhuboHead, mAvatar);

        llBottomBar.setVisibility(View.VISIBLE);
    }


    private RoomDanmuView mVDanmu;
    //    private RelativeLayout rlDanmuContainer;
    private boolean isDanmuMode = false;//是否发弹幕模式
//    private IDanmakuView mDanmakuView;
//    private DanmuControl mDanmuControl;

    private void initDanmu() {
        mVDanmu = (RoomDanmuView) findViewById(R.id.room_danmu);
//        rlDanmuContainer = (RelativeLayout) findViewById(R.id.rl_danmaku_container);
//        rlDanmuContainer.setOnClickListener(this);
//        mDanmuControl = new DanmuControl(this);
//        mDanmakuView = (IDanmakuView) findViewById(R.id.sv_danmaku);
//        mDanmuControl.setDanmakuView(mDanmakuView);
    }


    private void showTuhaoCome(String nickName, int level, String audienceID) {
        Log.d(TAG, " showTuhaoCome nickName=" + nickName + " level=" + level + " audienceID=" + audienceID);
        if (Utils.isEmpty(nickName)) {
            return;
        }
        if (isTuhaoComeAniming) {
            Log.d(TAG, " 当前有土豪进来了，不弹出");
            return;
        }
        isTuhaoComeAniming = true;
        tvLevelCome.setText(String.valueOf(level));
        String content = nickName + " " + getString(R.string.longzhong_dengchang);
        SpannableString sp = new SpannableString(content);
        sp.setSpan(new ForegroundColorSpan(Color.parseColor(COLOR_COME_NAME)), 0, nickName.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//        sp.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), nickName.length(), content.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvContentCome.setText(sp);
        handler.removeCallbacks(hideTuhaoRunnable);
//        animGiftShow = new AnimatorSet();
//        animation.play(in).with(giftIn);
//        animGiftShow.play(inLight).after(in);
//        animGiftShow.start();
        ImageUtil.loadAssetsImage("anims/anim_star.webp", ivStar1, new ControllerListener() {
            @Override
            public void onSubmit(String id, Object callerContext) {
            }

            @Override
            public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {
                if (animatable instanceof AnimatedDrawable) {
                    starAnimDrawable = (AnimatedDrawable) animatable;
                    Log.d(TAG, " instanceof starAnimDrawable duration=" + starAnimDrawable.getDuration());
                    starAnimDrawable.getDuration();
                }
            }

            @Override
            public void onIntermediateImageSet(String id, Object imageInfo) {
            }

            @Override
            public void onIntermediateImageFailed(String id, Throwable throwable) {
                Log.d(TAG, " onIntermediateImageFailed");
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                Log.d(TAG, " onFailure throwable=" + throwable.getLocalizedMessage());
            }

            @Override
            public void onRelease(String id) {

            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rlTuhaoCome.setVisibility(View.VISIBLE);
                in.start();
                if (starAnimDrawable != null) {
                    starAnimDrawable.start();
                    Log.d(TAG, "星星动画开始");
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (starAnimDrawable != null) {
                            starAnimDrawable.stop();
                        }
                        //结束时设置第一帧
                        ImageUtil.loadResImage(R.drawable.tuhao_star_default, ivStar1);
                        Log.d(TAG, "星星动画提早结束");
                    }
                }, 5000);
            }
        }, 200);
        in.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Log.d(TAG, " ivLightCome.setVisibility(View.VISIBLE)");
                ivLightCome.setVisibility(View.VISIBLE);
                inLight.start();
//                animStar1.start();
//                animStar2.start();
            }
        });
        inLight.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ivLightCome.setVisibility(View.GONE);
                handler.postDelayed(hideTuhaoRunnable, 3000);
            }
        });

    }

    Runnable hideTuhaoRunnable = new Runnable() {
        @Override
        public void run() {
            isTuhaoComeAniming = false;
            rlTuhaoCome.setVisibility(View.GONE);
//            animStar1.cancel();
//            animStar2.cancel();
        }
    };

    /**
     * 初始化评论列表
     */
    LinearLayoutManager linearLayoutManager;
    private String COLOR_COME_NAME = "#fee43c";
    private AnimatorSet animGiftShow;//animStar1, animStar2;
    private long STAR_ANIM_TIME = 200;
    ObjectAnimator in, inLight, star2xToBig, star2xToNomal, star1xToBig, star1xToNomal, star2yToBig, star2yToNomal, star1yToBig, star1yToNomal;
    boolean isTuhaoComeAniming = false;
    private AnimatedDrawable starAnimDrawable;

    private void initComment() {
        rlTuhaoCome = (RelativeLayout) findViewById(R.id.rl_tuhao_come);
        tvLevelCome = (TextView) findViewById(R.id.tv_level_tu_hao);
        //字体
        Typeface fontFace = TypefaceHelper.getInstance().getTypeface(TypefaceHelper.TYPE_FACE_AFFOGATO_BOLD);
        if (fontFace == null) {
            fontFace = Typeface.createFromAsset(getContext().getAssets(),
                    "fonts/AFFOGATO-BOLD.OTF");
            TypefaceHelper.getInstance().putTypeface(TypefaceHelper.TYPE_FACE_AFFOGATO_BOLD, fontFace);
        }
        tvLevelCome.setTypeface(fontFace);
        tvContentCome = (TextView) findViewById(R.id.tv_tuhao_content);
        ivLightCome = (ImageView) findViewById(R.id.iv_tuhao_light);
        ivStar1 = (SimpleDraweeView) findViewById(R.id.iv_star1);

//        ivStar2 = (ImageView) findViewById(R.id.iv_star2);
//        star1xToBig = ObjectAnimator.ofFloat(ivStar1, "scaleX", 1f, 1.5f).setDuration(STAR_ANIM_TIME);
//        star1yToBig = ObjectAnimator.ofFloat(ivStar1, "scaleY", 1f, 1.5f).setDuration(STAR_ANIM_TIME);
//        star1xToNomal = ObjectAnimator.ofFloat(ivStar1, "scaleX", 1.5f, 1f).setDuration(STAR_ANIM_TIME);
//        star1yToNomal = ObjectAnimator.ofFloat(ivStar1, "scaleY", 1.5f, 1f).setDuration(STAR_ANIM_TIME);
//        star1xToBig.setRepeatCount(100);
//        star1yToBig.setRepeatCount(100);
//        star1xToNomal.setRepeatCount(100);
//        star1yToNomal.setRepeatCount(100);
//        star2xToBig = ObjectAnimator.ofFloat(ivStar2, "scaleX", 1f, 1.5f).setDuration(STAR_ANIM_TIME);
//        star2yToBig = ObjectAnimator.ofFloat(ivStar2, "scaleY", 1f, 1.5f).setDuration(STAR_ANIM_TIME);
//        star2xToNomal = ObjectAnimator.ofFloat(ivStar2, "scaleX", 1.5f, 1f).setDuration(STAR_ANIM_TIME);
//        star2yToNomal = ObjectAnimator.ofFloat(ivStar2, "scaleY", 1.5f, 1f).setDuration(STAR_ANIM_TIME);
//        star2xToBig.setRepeatCount(100);
//        star2yToBig.setRepeatCount(100);
//        star2xToNomal.setRepeatCount(100);
//        star2yToNomal.setRepeatCount(100);
//        animStar1 = new AnimatorSet();
//        animStar1.play(star1xToBig).with(star1yToBig);
//        animStar1.play(star1xToNomal).after(star1xToBig);
//        animStar1.play(star1xToNomal).with(star1yToNomal);
//        animStar2 = new AnimatorSet();
//        animStar2.play(star2xToBig).with(star2yToBig);
//        animStar2.play(star2xToNomal).after(star2xToBig);
//        animStar2.play(star2xToNomal).with(star2yToNomal);

        in = ObjectAnimator.ofFloat(rlTuhaoCome, "x", LiveApplication.screenWidth, Utils.dip2px(getContext(), 10)).setDuration(600);
        inLight = ObjectAnimator.ofFloat(ivLightCome, "x", 0, Utils.dip2px(getContext(), 408)).setDuration(2000);
        myUserId = AccountInfoManager.getInstance().getCurrentAccountUserIdStr();
        lvComment = (RecyclerView) findViewById(R.id.lv_comment);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);
        lvComment.setLayoutManager(linearLayoutManager);
//        lvComment.setLayoutTransition(null);
        lvComment.setItemAnimator(null);//取消item出现动画
        chatAdapter = new LiveChatRcvAdapter(getContext(), chatList);

        chatAdapter.mineUserId = myUserId;
        chatAdapter.zhuboUserId = mHostIdentifier;
        lvComment.setAdapter(chatAdapter);
        lvComment.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 判断是否滚动到底部
                    //得到可见视图项最底层id
                    setAutoScroll(true);
                } else {
                    Log.d(TAG, "scroll onScrollStateChanged isAutoScroll = false");
                    setAutoScroll(false);
                }
            }
        });
        chatAdapter.setOnItemClickLitener(new LiveChatRcvAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "lvComment onItemClick");
                handleTouchScreen();
            }
        });

//        addDefaultNotice();

    }

    Runnable setAutoScroll = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "scroll setAutoScroll true");
            isAutoScroll = true;
        }
    };

//    private void addDefaultNotice() {
//        ChatEntity item = new ChatEntity();
//        item.setSenderName("系统");
//        item.setContent(getString(R.string.zhibo_notice));
//        item.setType(ChatEntity.MSG_TYPE_SYSTEM);
//        addCommentToList(item);
//    }

    private void addSystemNotice(String content) {
        ChatEntity item = new ChatEntity();
        item.setSenderName("系统");
        item.setContent(content);
        item.setType(ChatEntity.MSG_TYPE_SYSTEM);
        addCommentToList(item);
    }

    /**
     * 刷新评论列表
     */
    private void notifyComment() {
//        chatAdapter.notifyDataSetChanged();
        chatAdapter.notifyItemInserted(chatList.size() - 1);//只刷新最后一行
//        chatAdapter.notifyItemChanged(chatList.size() - 1);//只刷新最后一行
//        Log.d(TAG," notifyComment isAutoScroll="+isAutoScroll);
        if (isAutoScroll) {
            lvComment.scrollToPosition(chatList.size() - 1);
        }
    }

    /**
     * 初始化观众列表
     *
     * @param
     */
    private void initAudience() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rcv_head);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAudienceAdapter = new SeeingUserAdapter(getContext(), mAudiences);
        mRecyclerView.setAdapter(mAudienceAdapter);

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            //用来标记是否正在向最后一个滑动，既是否向右滑动或向下滑动
            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();

                    Log.d(TAG, "lastVisibleItem=" + lastVisibleItem + " totalItemCount=" + totalItemCount);
                    // 判断是否滚动到底部，并且是向右滚动
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {

//                        if (totalItemCount < roomUserpageSize) {
//                            Log.d(TAG, "不够一页不需要加载更多");
//                            return;
//                        }
                        //加载更多功能的代码
                        int page = totalItemCount / roomUserpageSize + 1;
                        Log.d(TAG, "加载更多观众 page=" + page);
                        queryRoomUser(page);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                if (dx > 0) {
                    //大于0表示，正在向右滚动
                    isSlidingToLast = true;
                } else {
                    //小于等于0 表示停止或向左滚动
                    isSlidingToLast = false;
                }
            }
        });

        mAudienceAdapter.setOnItemClickLitener(new SeeingUserAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {

                UserInfo item = mAudiences.get(position);
                showUserInfoPanel(item.getIdStr(), item.getAvatar());
                if (isCreater) {
                    AnalyticsReport.onEvent(getContext(), LiveReport.MY_LIVE_EVENT_11220);
                } else {
                    AnalyticsReport.onEvent(getContext(), LiveReport.MY_LIVE_EVENT_11231);
                }
            }
        });
    }

    private void queryRoomUser(int page) {
//        Log.d(TAG, " queryRoomUser查询房间观众列表 page=" + page);
        handler.removeCallbacks(resetQueryingRoomUserRunnable);
        isQueryingRoomUser = true;
        curRoomUserPage = page;
        roomManger.queryRoomUser(page);
        handler.postDelayed(resetQueryingRoomUserRunnable, 5);
    }

    Runnable resetQueryingRoomUserRunnable = new Runnable() {
        @Override
        public void run() {
            if (isQueryingRoomUser)
                isQueryingRoomUser = false;
        }
    };

    private void enableMic(boolean mChecked) {
        if (mChecked) {
            mLivePusher.enableMic(true);
            btnMic.setBackgroundResource(R.drawable.live_btn_mic_selector);
        } else {
            mLivePusher.enableMic(false);
            btnMic.setBackgroundResource(R.drawable.live_btn_no_mic_selector);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume mVideoPlay=" + mVideoPlay);
        if (isCreater) {
            AnalyticsReport.onEvent(getContext(), LiveReport.MY_LIVE_EVENT_11212);
        } else {
            AnalyticsReport.onEvent(getContext(), LiveReport.MY_LIVE_EVENT_11229);
        }
//        wakeLock.acquire();
//        if (mDanmuControl != null) {
//            mDanmuControl.resume();
//        }
//        refreshCameraUI();
        Utils.doTimeCountStart();

        if (isCreater) {
            roomManger.zhuboReturn();
//            pushResume();

            if (mBgmManager != null) {
                mBgmManager.resume();
            }
        } else {

//            if (mPlayerView != null) {
//                mPlayerView.onResume();
//            }
        }

        if (llBottomBar != null && llBottomBar.getVisibility() != View.VISIBLE)
            showTopBar();

        if (isChatMessagePanelShow && mChatMessagePanel != null) {
            mChatMessagePanel.refreshChat();
        }

        if (isCreater && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d(TAG, "需要检查权限");
            List<String> needs = getPermissionNeeds();
            if (!Utils.listIsNullOrEmpty(needs)) {
                Log.d(TAG, " 没有摄像头或录音权限");
                ToastHelper.showToast(R.string.hasno_camera_or_record_permission);
                onCloseVideo();
            } else {
                Log.d(TAG, "有摄像头或录音权限");
            }
        } else {
            Log.d(TAG, "不需要检查权限");
        }

        hideFloatLive();
        if (mVideoPlay && !isCreater) {
//            if(goodsPanel.getVisibility()==View.VISIBLE){
//
//            }else{
//                mLivePlayer.changeVideoOrientation(isPortrait?0:1);
//            }
            Log.d(TAG, " 重新setView");
//            mLivePlayer.stopPlay();
//            mLivePlayer.destroy();
//            mLivePlayer=null;
//            mLivePlayer.updatePlayerView();
            ((FrameLayout) mRootView).removeViewAt(1);
            vlBigView = null;
            vlBigView = (ViewLive) LayoutInflater.from(getContext()).inflate(R.layout.view_room_live, null);
            if (goodsPanel.getVisibility() == View.VISIBLE) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) new FrameLayout.LayoutParams(Utils.getScreenWidth(mContext), Utils.getScreenHeight(mContext) / 3);
                vlBigView.setLayoutParams(lp);
            } else {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) new FrameLayout.LayoutParams(Utils.getScreenWidth(mContext), Utils.getScreenHeight(mContext));
                vlBigView.setLayoutParams(lp);
            }
            ((FrameLayout) mRootView).addView(vlBigView, 1);
            initLivePlayer();
            startPlayRtmp();
        }

    }

    private void pushResume() {
        long curPushResumeTime = System.currentTimeMillis();
        if (curPushResumeTime - lastPushResumeTime < 500) {
            Log.d(TAG, " pushResume间隔太短");
            return;
        }
        if (isZhuboTemporaryLeave) {
            isZhuboTemporaryLeave = false;
            roomManger.zhuboReturn();
        }

        lastPushResumeTime = curPushResumeTime;
//        if (mCaptureView != null) {
//            mLivePusher.startPreview();
//            Log.d(TAG, " pushResume,开启预览");
//        }
//        Log.d(TAG, " mVideoPublish=" + mVideoPublish + " mLivePusher.isPushing()=" + mLivePusher.isPushing());
//        if (mVideoPublish && !mLivePusher.isPushing()) {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    startPublishRtmp();
//                }
//            });
//        }

    }


    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, " onPause");
//        if (isCreater) {
//
//        } else {
//            Log.d(TAG, " mPlayType=" + mPlayType);
//            if (mPlayType == TXLivePlayer.PLAY_TYPE_VOD_FLV) {
//                if (mLivePlayer != null) {
//                    mLivePlayer.pause();//为了不应用内切换保持声音
//                }
//            } else {
//            }
//            if (mPlayerView != null) {
//            }
//        }

//        if(!isCreater){
//            mLivePlayer.stopPlay();
//        }

        //跳商品才show
//        showFloatLive();

    }

    private void destoryFloatLive() {
        Log.d(TAG, " destoryFloatLive");
        if (!isCreater) {
            FloatWindow.getInstance().destoryFloatWindow();
            if (floatLivePlayer != null) {
                floatLivePlayer.stopPlay();
                floatLivePlayer.destroy();
            }
        }
    }

    private void hideFloatLive() {
        Log.d(TAG, " hideFloatLive");
        if (!isCreater) {
            FloatWindow.getInstance().hideFloatWindow();
            if (floatLivePlayer != null) {
                floatLivePlayer.stopPlay();
                floatLivePlayer.destroy();
                floatLivePlayer = null;
                Log.d(TAG, " floatLivePlayer != null");
            } else {
                Log.d(TAG, " floatLivePlayer = null");
            }
        }
    }

    private void showFloatLive() {
//        if(true){
//            return;
//        }
        Log.d(TAG, " showFloatLive");
        if (!isCreater && mVideoPlay) {
//            if (floatLivePlayer == null) {
            Log.d(TAG, " 初始化float");
            isNeedInitFloat = false;
            FloatWindow.getInstance().initFloatWindow();
            RelativeLayout vFloat = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.view_float_live, null);
            vFloat.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideFloatLive();
                }
            });
            vFloat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBusManager.postEvent(0, SubcriberTag.CLICK_FLOAT_LIVE);
                }
            });


            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Utils.getScreenWidth(getContext()) / 4, Utils.getScreenHeight(getContext()) / 4);
            FloatWindow.getInstance().addChildView(vFloat, lp);
            ZegoLivePlayer.Builder build = new ZegoLivePlayer.Builder();
            build.setUserName(mHostIdentifier);
            build.setUserId(mHostIdentifier);
            build.setLiveChannel(channelId);
            build.setStreamID(streamId);
            build.setRemoteView((ViewLive) vFloat.findViewById(R.id.vl_big_view));
            floatLivePlayer = build.build();
//            }
            FloatWindow.getInstance().showFloatWindow();
            floatLivePlayer.startPlay();

//            if (mLivePlayer != null) {
//                mLivePlayer.stopPlay();
//            }
        }
    }

    ZegoLivePlayer floatLivePlayer;

    boolean isNeedInitFloat = true;


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (goodsPanel.getVisibility() == View.VISIBLE) {
                    hideGoodsPanel();
                    return true;
                }
//                onCloseVideo();
//                if(isResume()){
//                    if (isCreater && !isStartLiveSuccess) {
//                        finish();
//                    } else {
//                        showExitDialog();
//                    }
//                }
                if (mChatMessagePanel != null && mChatMessagePanel.getVisibility() == View.VISIBLE) {
                    mChatMessagePanel.onClickBack();
                    return true;
                }

                if (isResume()) {
                    if (isCreater) {
                        showPauseLiveDialog();
                    } else {
                        if (isPortrait) {
                            showExitDialog();
                        } else {
                            showExitDialogLand();
                        }
                    }

                }
                return true;
        }
        return false;
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, " onStop");
        if (isCreater) {
//            mLivePusher.stopPreview();
        } else {
            mLivePlayer.stopPlay();
            mLivePlayer.destroy();
        }
    }

    public void finish() {
        Log.d(TAG, " finish");
        if (isCreater) {
            stopPublishRtmp();
            if (mLivePusher != null) {
                mLivePusher.stopPreview();
                mLivePusher.destroy();
            }
        } else {
            if (mLivePlayer != null) {
                stopPlayRtmp();
                mLivePlayer.destroy();
            }
        }
        if (connectMicManager != null) {
            connectMicManager.stop();
        }
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        destoryFloatLive();

        Log.d(TAG, " onDestroy");
        if (mBgmManager != null) {
            mBgmManager.stopMixMusic();
            mBgmManager.destory();
        }
        isSeeingLive = false;

        if (mLyricView != null) {
            mLyricView.destory();
        }

        if (lvComment != null) {
            lvComment.setOnScrollListener(null);
        }

        if (mRecyclerView != null) {
            mRecyclerView.setOnScrollListener(null);
        }

        //解决内存泄露相关
        hideLoading();
        handler.removeCallbacksAndMessages(null);


        LiveRoomManager.getInstance().clearCurrentLiveRoomUser();
        if (roomManger != null) {
            roomManger.setOnRequestResultCallback(null);
            roomManger.cleareResultListener();
            if (isCreater) {
                if (isCreateRoom) {
                    //roomManger.closeRoom();
                } else {
                    Log.d(TAG, "不需要开启房间");
                }

            } else {
                exitLiveRoom();
            }
        }

        if (giftShowView != null) {
            giftShowView.stopAndClear();
        }

        if (giftAnimManger != null) {
            giftAnimManger.clearAllAnima();
        }
        if (!Utils.listIsNullOrEmpty(likeList)) {
            likeList.clear();
            likeList = null;
        }
        clearAdminState();
        isFlashStop = true;
        if (mChatMessagePanel != null) {
            mChatMessagePanel.stop();
        }

        LiveApplication.getInstance().isInLiveRoomActivity = false;

        releaseTimer();

        if (mUserInfoPanel != null) {
            mUserInfoPanel.recycleBlurBitmap();
        }

        EventBusManager.unregister(this);

        if (goodsPanel != null) {
            goodsPanel.destory();
        }
    }


    private void releaseTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (roomTimerTask != null) {
            roomTimerTask.cancel();
        }
        if (mCheckInRoomTimer != null) {
            mCheckInRoomTimer.cancel();
            mCheckInRoomTimer = null;
        }
        if (checkInRoomTimerTask != null) {
            checkInRoomTimerTask.cancel();
        }
    }

    private void clearAdminState() {
        LiveRoomAdminManager.getInstance().setTempUserInfo(null);
        LiveRoomAdminManager.getInstance().clearAdministrators();
        AccountInfoManager.getInstance().updateCurrentAccountAdminState(false);
    }


    private void handleRemoveManagerWhenSomeUserExitRoom(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return;
        }
        if (mUserInfoPanel != null) {
            mUserInfoPanel.removeExitRoomUser(StringUtils.parseInt(userId));
        }
        LiveRoomAdminManager.getInstance().removeAdministrator(userId);
    }

    private void setZhuboVideo() {
        HashMap<String, String> parmas = new HashMap<>();
        parmas.put("Live_id", mHostIdentifier);
        parmas.put("Laka_id", AccountInfoManager.getInstance().getCurrentAccountUserIdStr());
//        AnalyticsReport.onEvent(LiveRoomActivity.this, LiveReport.MY_LIVE_EVENT_11221_s, parmas);
        Log.debug(TAG, "ACTION_ENABLE_CAMERA_COMPLETE mHostIdentifier " + mHostIdentifier);

        if (isReconnecting) {
            Log.d(TAG, "重连成功");
            isReconnecting = false;
        }
    }

    boolean isExitOldRoom;

    private void handleExitRoom() {
//        if (isReconnecting) {
//            int ret = enterRoom(mRoomId);
//            if (ret != AVError.AV_OK) {
//                retryEnterRoom();
//            }
//            return;
//        }
        //停止音频管理
//        mQavsdkControl.uninitAudioService();
        if (isEnterVideoRoom) {
            closeActivity();
            Log.d(TAG, "需要closeActivity isZhiboEnd=" + isZhiboEnd);
        } else {
        }
        isExitOldRoom = true;

    }

    private void showVideo(String identifier) {
        if (isGetVideoSuccess) {
            return;
        }
        Log.debug(TAG, "onReceive ACTION_VIDEO_SHOW  id " + identifier);
        if (!isCreater) {//渲染观众画面
            isSetRemoteHasVideo = true;
        } else {//渲染主播画面

        }

    }


    // 关闭直播，并跳转
    private void closeActivity() {
        if (roomManger != null && isCreater) {
            roomManger.closeRoom();
        }
        LiveFinishActivity.startActivity(getActivity(), isCreater, mCourseId);
        Log.d(TAG, " closeActivity isResume()=" + isResume() + " isStartLiveSuccess=" + isStartLiveSuccess + " isZhiboEnd=" + isZhiboEnd);


//        if (wakeLock.isHeld())
//            wakeLock.release();

//        if (mSelfUserInfo.isCreater() == true) {
//            setResult(Util.SHOW_RESULT_CODE);
//        } else {
//            setResult(Util.VIEW_RESULT_CODE);
//        }

// start tracing to "/sdcard/calc.trace"
//        Debug.startMethodTracing("LiveFinishActivity");

//        Log.log("isCreater:"+isCreater+",isStartLiveSuccess:"+isStartLiveSuccess+",isZhiboEnd:"+isZhiboEnd);
//        if (isResume()) {
//            if (isCreater) {
//                if (isStartLiveSuccess) {
//                    LiveFinishActivity.startActivity(getActivity(), true, taskId);
//                }
//            } else {
//                if (isZhiboEnd) {
//                    //直播结束才需要
//                    LiveFinishActivity.startActivity(getActivity(), false);
//                }
//            }
//        }

        // ...
// stop tracing
//        Debug.stopMethodTracing();

        finish();
    }


    public static final int END_TYPE_ZHUBO = 0;
    public static final int END_TYPE_NET = 1;
    public static final int END_TYPE_NO_VIDEO = 2;
    public static final int END_TYPE_ROOM_NOT_EXIST = 3;
    public static final int END_TYPE_USER_NOT_IN_ANY_ROOM = 4;
    public static final int END_TYPE_USER_IN_OTHER_ROOM = 5;
    public static final int END_TYPE_FORBID = 6;

    /**
     * 直播已结束
     */
    private void zhiboEnd(int type) {
        if (isCloseRoom) {
            Log.d(TAG, "zhiboEnd 已CloseRoom不在close");
            return;
        }
        Log.d(TAG, " zhiboEnd type=" + type);
        if (type == END_TYPE_ZHUBO) {
            ToastHelper.showToast(R.string.zhibo_end);
            EventBusManager.postEvent(mHostIdentifier, SubcriberTag.REMOVE_END_ZHIBO);
        } else if (type == END_TYPE_NET) {
            ToastHelper.showToast(R.string.net_not_well);
        } else if (type == END_TYPE_NO_VIDEO) {
            ToastHelper.showToast(R.string.cannot_get_zhibo);
        } else if (type == END_TYPE_ROOM_NOT_EXIST) {
            ToastHelper.showToast(R.string.room_not_exist);
        } else if (type == END_TYPE_USER_NOT_IN_ANY_ROOM) {
//            showToast(R.string.user_not_in_any_room);
        } else if (type == END_TYPE_USER_IN_OTHER_ROOM) {
            ToastHelper.showToast(R.string.user_in_other_room);
        } else {
            ToastHelper.showToast(R.string.cannot_get_zhibo, Toast.LENGTH_LONG);
        }
        Log.d(TAG, "直播已结束");
        onCloseVideo();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

            }
        }
    };


    boolean isCloseRoom = false;

    private void onCloseVideo() {
        isCloseRoom = true;

        //退出AV那边群
        if (isCreater != true) {
            if (isEnterVideoRoom) {
                closeActivity();
            } else {
                closeActivity();
            }
        } else {
            if (isEnterVideoRoom) {
                EventBusManager.postEvent(mHostIdentifier, SubcriberTag.REMOVE_END_ZHIBO);
                HashMap<String, String> parmas = new HashMap<>();
                parmas.put("Live_id", mHostIdentifier);
                parmas.put("Laka_id", AccountInfoManager.getInstance().getCurrentAccountUserIdStr());
//                parmas.put("View_time", String.valueOf(totalTime));
//                parmas.put("GiveConins_count", String.valueOf(totalGiveConins));
//                parmas.put("Viewer_from", LiveApplication.getInstance().getMyIdString());
                AnalyticsReport.onEvent(getActivity(), LiveReport.MY_LIVE_EVENT_11252_e, parmas);
            }
            closeActivity();
        }

//        if (wakeLock.isHeld())
//            wakeLock.release();
        if (isCreater == true) {
            getActivity().setResult(Util.SHOW_RESULT_CODE);
        } else {
            getActivity().setResult(Util.VIEW_RESULT_CODE);
        }
    }


    UserInfo curPopUser;
    ZhuboMorePanel morePanel;

    boolean isPortrait = true;

    @Override
    public void onClick(View v) {
//        Log.d(TAG," onClick id="+v.getId());
        switch (v.getId()) {
            case R.id.btn_goods_zhubo:
            case R.id.btn_goods_audience:
                showGoodsPanel();
                break;
            //横竖屏切换
            case R.id.btn_orientation:
            case R.id.btn_orientation_audience:
                Log.d(TAG, " onClick btn_orientation");
//                EventBusManager.postEvent(0,SubcriberTag.LIVE_SET_ORIENTATION);
                isPortrait = !isPortrait;
                changeViewOrientation(isPortrait);

                break;
            //连麦开始
            case R.id.btn_connect_zhubo:
//                ToastHelper.showToast("跳转连麦列表activity");
                mZhuboLinkRedpoint.setVisibility(View.GONE);
                handleConnectMicClick();
                break;
            case R.id.btn_connect_audience:
                AddLinkMicPanel linkMicPanel = new AddLinkMicPanel(getContext(), connectMicManager);
                linkMicPanel.showPanel();
//                if (connectMicManager.isSecondZhubo) {
//                    connectMicManager.closeConnectMic("", ""); //取消连麦
//                } else {
//                    connectMicManager.openConnectMic("");//请求连麦
//                }

                break;
            //连麦结束

            case R.id.btn_bgm:
                AnalyticsReport.onEvent(getActivity(), LiveReport.MY_LIVE_EVENT_11254);
//                startBgm(musicPath);
                MusicListActivity.startActivity(getActivity());

                break;
            case R.id.rl_end:
                stopBgm();
                break;
            case R.id.rl_effect://音效
                AnalyticsReport.onEvent(getActivity(), LiveReport.MY_LIVE_EVENT_11255);
                showAudioEffectPanel();
                break;
            case R.id.btn_more:
                //主播更多
                morePanel = new ZhuboMorePanel(getActivity(), isPortrait, (BaseActivity) getActivity());

                morePanel.showPanel();
                btnMore.setBackgroundResource(R.drawable.live_btn_more_down_selector);
                break;
            case R.id.btn_follow:
                handleOnFollowButtonClick();
                break;
            case R.id.rl_kazuan:
                if (!isCreater) {
                    ContributionListActivity.startActivity(getActivity(), mHostIdentifier, RankingItemView.FROM_TYPE_USER_INFO);
                    AnalyticsReport.onEvent(getActivity(), LiveReport.MY_LIVE_EVENT_11232);
                } else {
                    ContributionListActivity.startActivity(getActivity(), mHostIdentifier);
                }
//                showContributionPanel();
                break;
            case R.id.tv_gift_recharge://礼物框跳充值
//                showRechargeDialog();
                AnalyticsReport.onEvent(getActivity(), LiveReport.MY_LIVE_EVENT_11247);
                MyCoinsActivity.startActivity(getActivity());
                break;
            case R.id.btn_send_gift_multi:
                //连送一个并重置时间
//                showAnimGiftAddCount();
                doSendGift();

//                btnSendGiftMulti.startTimer();
                if (isPortrait) {
                    giftGridView.startSendGiftMultiTimer();
                } else {
                    giftGridViewLand.startSendGiftMultiTimer();
                }


                HashMap<String, String> parmas = new HashMap<>();
                parmas.put("ID", chooseGift.getId() + "");
                AnalyticsReport.onEvent(getActivity(), LiveReport.MY_LIVE_EVENT_11246, parmas);
                break;
            case R.id.btn_send_gift://礼物框送礼确认
                if (chooseGift == null) {
                    ToastHelper.showToast(R.string.please_choose_gift);
                    return;
                }
                if (!GiftResManager.getInstance().checkIsResReady(chooseGift)) {
                    ToastHelper.showToast(R.string.gift_readying);
                    return;
                }
                doSendGift();


                HashMap<String, String> parmas2 = new HashMap<>();
                parmas2.put("ID", chooseGift.getId() + "");
                AnalyticsReport.onEvent(getActivity(), LiveReport.MY_LIVE_EVENT_11245, parmas2);
                if (chooseGift.isMulti()) {
                    //开启连送模式
                    showMultiGiftMode();
                    Log.d(TAG, " 开启连送模式");
                } else {
                    Log.d(TAG, " 普通礼物");
                    //普通送礼物
//                    showToast("送礼物成功");
//                      showGiftAnimation();
                }
                break;
            case R.id.rl_info_audience://弹出主播资料
                AnalyticsReport.onEvent(getActivity(), LiveReport.MY_LIVE_EVENT_11230);
                handleOnUserInfoButtonClick();
                break;
            case R.id.btn_gift_audience:
                AnalyticsReport.onEvent(getActivity(), LiveReport.MY_LIVE_EVENT_11236);
                showGiftView();
                break;
            case R.id.btn_gift:
                showGiftView();
                break;
            case R.id.btn_letter:

                if (!isPortrait) {
                    btnOrientation.performClick();
                }
//                showToast("私信");
//                handleOnLetterButtonClick();
                mMsgRedPoint.setVisibility(View.GONE);
                showSessionPanel();
                break;
            case R.id.btn_letter_zhubo:
                if (!isPortrait) {
                    btnOrientation.performClick();
                }
                mZhuboMsgRedPoint.setVisibility(View.GONE);
                showSessionPanel();
                break;
            case R.id.rl_touch:
                Log.d(TAG, "rl_touch");
                handleTouchScreen();
                break;
//            case R.id.rl_danmaku:
//                Log.d(TAG, "rl_danmaku");
//                handleTouchScreen();
//                break;
            case R.id.rl_comment_touch://rl_lv_comment://评论列表遮罩
                Log.d(TAG, "rl_comment_touch");
                handleTouchScreen();
                break;
            case R.id.btn_send_comment://发评论
                sendComment();
                break;
            case R.id.btn_tanmu://弹幕开关
            case R.id.rl_tanmu:
                changeDanmuMode();
                break;

            case R.id.close_btn:
                Log.d(TAG, " close_btn isCreater=" + isCreater);
                if (isCreater) {
                    if (isTestLive) {
                        if (isPortrait) {
                            showExitDialog();
                        } else {
                            showExitDialogLand();
                        }
                    } else {
                        showPauseLiveDialog();
                    }
                } else {
                    if (isCreateRoom) {
                        onCloseVideo();
                    } else {
                        finish();
                    }

                }
                break;
            case R.id.btn_share:
                AnalyticsReport.onEvent(getActivity(), LiveReport.MY_LIVE_EVENT_11213);
                showShareDialog();
                break;
            case R.id.btn_share_audience:
                AnalyticsReport.onEvent(getActivity(), LiveReport.MY_LIVE_EVENT_11233);
                // 分享课程详情
                showShareDialog();
                // 分享预告
                //mBaseActivity.showShareDialog(Common.SHARE_ROOM_URL + mHostIdentifier, mBaseActivity.share.getShareTitle(mBaseActivity), mBaseActivity.share.getShareContent(mBaseActivity), zhuboUserInfo != null ? zhuboUserInfo.getAvatar() : "", true);
                break;
//            case R.id.btn_music:
//                showToast("音乐");
//                break;
            case R.id.btn_comment:
                if (!isPortrait) {
                    btnOrientation.performClick();
                }
//                AnalyticsReport.onEvent(this, LiveReport.MY_LIVE_EVENT_11209);
                //显示评论框
                showCommentInput();
                break;
            case R.id.btn_comment_audience:
                if (!isPortrait) {
                    btnOrientation.performClick();
                }
//                AnalyticsReport.onEvent(this, LiveReport.MY_LIVE_EVENT_11229);
                //显示评论框
                showCommentInput();
                break;
            case R.id.btn_camera:
                //切换摄像头
                onSwitchCamera();
                break;
            case R.id.btn_mic:
                toggleMic();
                break;
            case R.id.hot_activity:
                WebActivity.startActivity(getActivity(), Common.ABOUT_URL, "热门活动");
                break;
            case R.id.btn_face:
                handleOnBtnFaceClick();
                break;
            case R.id.et_comment:
                handleOnCommentClick();
                break;
            case R.id.btn_formula:
            case R.id.btn_formula_zhubo:
                // 配方做法
                showFormulaPanel();
                AnalyticsReport.onEvent(mBaseActivity, LiveReport.MY_LIVE_EVENT_11279);
                break;
            default:
                Log.d(TAG, "default");
                break;
        }
    }

    private void showPauseLiveDialog() {
        rlPauseLiveDialog.setVisibility(View.VISIBLE);
        if (isPortrait) {
            rlPauseLiveDialog.setRotation(0);
            ViewGroup.LayoutParams lp = rlPauseLiveDialog.getLayoutParams();
            lp.width = mRootView.getWidth();
            lp.height = mRootView.getHeight();
            rlPauseLiveDialog.setLayoutParams(lp);
            rlPauseLiveDialog.setX(0);
            rlPauseLiveDialog.setY(0);
        } else {
            ViewGroup.LayoutParams lp = rlPauseLiveDialog.getLayoutParams();
            lp.height = mRootView.getWidth();
            lp.width = mRootView.getHeight();
            rlPauseLiveDialog.setLayoutParams(lp);
            int newX = -(mRootView.getHeight() - mRootView.getWidth()) / 2;
            int newY = (mRootView.getHeight() - mRootView.getWidth()) / 2;
            rlPauseLiveDialog.setX(-(mRootView.getHeight() - mRootView.getWidth()) / 2);
            rlPauseLiveDialog.setY((mRootView.getHeight() - mRootView.getWidth()) / 2);
//            if ( mFrontCamera) {
//                rlPauseLiveDialog.setRotation(-90);
//            } else {
            rlPauseLiveDialog.setRotation(90);
//            }
        }

    }

    private void changeViewOrientation(boolean isPortrait) {
//        isPortrait = !isPortrait;
        Log.d(TAG, " onClick isPortrait=" + isPortrait);
        if (isPortrait) {//切换到竖屏
//            roomView.setRotation(0);
//            ViewGroup.LayoutParams lp = roomView.getLayoutParams();
//            lp.height = mRootView.getHeight();
//            lp.width = mRootView.getWidth();
//            roomView.setLayoutParams(lp);
//            roomView.setX(0);
//            roomView.setY(0);
//            Log.d(TAG, " 切换到竖屏  height=" + lp.height + " width=" + lp.width
//                    + " x=" + roomView.getX() + " y=" + roomView.getY());
            vpContainer.setRotation(0);
            ViewGroup.LayoutParams lp = vpContainer.getLayoutParams();
            lp.height = mRootView.getHeight();
            lp.width = mRootView.getWidth();
            vpContainer.setLayoutParams(lp);
            vpContainer.setX(0);
            vpContainer.setY(0);
        } else {//切换到横屏
//            LiveApplication.screenHeight = roomView.getHeight();
//            LiveApplication.screenWidth = roomView.getWidth();
//            if (isCreater) {
//                roomView.setRotation(90);
//            } else {
//                roomView.setRotation(mFrontCamera ? -90 : 90);
//            }
//            ViewGroup.LayoutParams lp = roomView.getLayoutParams();
//            lp.height = mRootView.getWidth();
//            lp.width = mRootView.getHeight();
//            roomView.setLayoutParams(lp);
//            roomView.setX(-(roomView.getHeight() - roomView.getWidth()) / 2);
//            roomView.setY((roomView.getHeight() - roomView.getWidth()) / 2);
//            Log.d(TAG, " 切换到横屏  height=" + lp.height + " width=" + lp.width
//                    + " x=" + roomView.getX() + " y=" + roomView.getY()
//                    + " mFrontCamera=" + mFrontCamera);
            LiveApplication.screenHeight = vpContainer.getHeight();
            LiveApplication.screenWidth = vpContainer.getWidth();
//            if (isCreater) {
//                vpContainer.setRotation(90);
//            } else {
//                vpContainer.setRotation(mFrontCamera ? -90 : 90);
//            }
            if (isCreater) {
                vpContainer.setRotation(90);
            } else {
                vpContainer.setRotation(mFrontCamera ? -90 : 90);
            }
            ViewGroup.LayoutParams lp = vpContainer.getLayoutParams();
            lp.height = mRootView.getWidth();
            lp.width = mRootView.getHeight();
            vpContainer.setLayoutParams(lp);
            vpContainer.setX(-(vpContainer.getHeight() - vpContainer.getWidth()) / 2);
            vpContainer.setY((vpContainer.getHeight() - vpContainer.getWidth()) / 2);
        }
    }

    private void toggleMic() {
        AnalyticsReport.onEvent(getActivity(), LiveReport.MY_LIVE_EVENT_11216);
        //控制麦克风
        mChecked = !mChecked;
        enableMic(mChecked);
        mQavsdkControl.setOpenMic(mChecked);
    }

    private void handleOnCommentClick() {
        if (isFaceShow) {
            hideFaceView();
        }
    }

    private boolean isFaceShow = false;

    private void handleOnBtnFaceClick() {
        isFaceShow = !isFaceShow;
        if (isFaceShow) {
            transoInputToFace();
        } else {
            transoFaceToInput();
        }
    }

    private void transoInputToFace() {
        mBaseActivity.hideSoftInput(mBaseActivity);
        emoticonPickerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                emoticonPickerView.setVisibility(View.VISIBLE);
                emoticonPickerView.show(LiveRoomFragment.this);
            }
        }, 50);

        btnFace.setBackgroundResource(R.drawable.chat_keyboard_selector);
    }

    private void transoFaceToInput() {
        mBaseActivity.showSoftInput();
        emoticonPickerView.setVisibility(View.GONE);
        btnFace.setBackgroundResource(R.drawable.chat_face_selector);
    }

    private void hideFaceView() {
        isFaceShow = false;
        emoticonPickerView.setVisibility(View.GONE);
        btnFace.setBackgroundResource(R.drawable.chat_face_selector);
    }

    private void startBgm(String musicPath, String lrcPath) {
        Log.d(TAG, " startBgm musicPath=" + musicPath + " lrcPath=" + lrcPath);
        AudioManager mAudioManager = (AudioManager) mBaseActivity.getSystemService(Context.AUDIO_SERVICE);
        int statusFlag = (mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) ? 1 : 0;
        if (statusFlag == 1) {
//            Volume = 0; //表示音量大小的参数，可以把音量调到最小
            mBaseActivity.showToast(R.string.in_silent_mode);
        }
        try {
            mLyricView.setKrcPath(lrcPath);
            mRlLrc.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            Log.d(TAG, "setKrcPath报错:"
                    + sw.toString());

        }
        if (mBgmManager != null)
            mBgmManager.startMixMusic(musicPath);
        //todo bgm
//        mLivePusher.setOnBgmMixerListener(mBgmManager.getOnBgmMixerListener());


        //设置当前音量
        float bgmVolume = UiPreference.getFloat(Common.KEY_AUDIO_BGM_VOLUME, 0.5f);
        float voiceVolume = UiPreference.getFloat(Common.KEY_AUDIO_VOICE_VOLUME, 0.5f);
        int effect = UiPreference.getInt(Common.KEY_AUDIO_EFFECT, 0);
//        if (mLivePusher != null) {
//            mLivePusher.setVolume(voiceVolume, bgmVolume);
//            mLivePusher.setReverbLevel(effect);
//        }
        if (mBgmManager != null)
            mBgmManager.setBgmVolume(bgmVolume);
    }


    private void stopBgm() {
        if (mAudioEffectPanel != null) {
            mAudioEffectPanel.hidePanel();
        }
        mRlLrc.setVisibility(View.INVISIBLE);
        if (mBgmManager != null)
            mBgmManager.stopMixMusic();
    }

    //点击用户信息主页
    private void handleOnUserInfoButtonClick() {
        if (!isResume()) {
            return;
        }
        if (zhuboUserInfo == null) {
            showUserInfoPanel(mHostIdentifier, null);
            return;
        }
        showUserInfoPanel(mHostIdentifier, zhuboUserInfo.getAvatar());
    }

    //点击关注按钮
    private void handleOnFollowButtonClick() {
        if (zhuboUserInfo == null) {
            getZhuboUserInfo();
            mBaseActivity.showToast(R.string.please_wait);
            return;
        }
        AnalyticsReport.onEvent(mBaseActivity, LiveReport.MY_LIVE_EVENT_11224);
        follow();
    }

    //处理点击私信
    private void handleOnLetterButtonClick() {
        if (zhuboUserInfo == null) {
            getZhuboUserInfo();
            mBaseActivity.showToast(R.string.please_wait);
            return;
        }
        ChatMessageActivity.startPrivateChatActivity(mBaseActivity, zhuboUserInfo.getIdStr(), zhuboUserInfo.getNickName(), zhuboUserInfo.getAvatar(), DbManger.SESSION_TYPE_UNFOLLOW);
    }


    private void doSendGift() {
        if (chooseGift == null) {
            return;
        }
        if (!NetworkUtil.isNetworkOk(mBaseActivity)) {
            mBaseActivity.showToast(R.string.network_unok_wait_try);
            return;
        }
        //特效音量
        GiftAudioManager.getInstance().playSound(R.raw.send_gift_audio);

        roomManger.sendGift(String.valueOf(chooseGift.getId()));
        Log.d(TAG, "送礼物id=" + chooseGift.getId() + " 钻石=" + chooseGift.getKazuan());
    }


    private void changeDanmuMode() {
        isDanmuMode = !isDanmuMode;
        if (isDanmuMode) {
//          btnDanmu.setText("关弹幕");
//            btnDanmu.setTextColor(getResources().getColor(R.color.colorFF6180));
            ivDanmu.setBackgroundResource(R.drawable.live_bg_dan_selected);
            animDanmuOpen.start();
            etComment.setHint(R.string.send_hao_danmu);
        } else {
//          btnDanmu.setText("开弹幕");
//            btnDanmu.setTextColor(getResources().getColor(R.color.color333333));
            ivDanmu.setBackgroundResource(R.drawable.live_bg_dan_normal);
            animDanmuClose.start();
            etComment.setHint(R.string.say_something);
        }
    }

    /**
     * 打开连送模式
     */
    private void showMultiGiftMode() {
        if (isPortrait) {
            giftGridView.showMultiGiftMode();
        } else {
            giftGridViewLand.showMultiGiftMode();
        }

    }

    /**
     * 关闭连送模式
     */
    private void hideMultiGiftMode() {
        if (isPortrait) {
            giftGridView.hideMultiGiftMode();
        } else {
            giftGridViewLand.hideMultiGiftMode();
        }

    }

    private void handleTouchScreen() {
//        Log.d(TAG, "handleTouchScreen");
        if (rlComment.getVisibility() == View.VISIBLE) {
            hideCommentInput();
        } else if (giftGridView != null && giftGridView.getVisibility() == View.VISIBLE) {
            hideGiftView();
        } else if (giftGridViewLand != null && giftGridViewLand.getVisibility() == View.VISIBLE) {
            hideGiftView();
        } else if (dialogView != null && dialogView.getVisibility() == View.VISIBLE) {
            dialogView.setVisibility(View.GONE);
        } else {
//            addLike();//发后台
            if (!isCreater) {
//                AnalyticsReport.onEvent(mBaseActivity, LiveReport.MY_LIVE_EVENT_11237);
//                roomManger.like(1);
//                Log.d(TAG, "自己点赞先显示");
                addLike(myUserId, 1);//自己点赞先显示
            } else {
//                if (!isStartLiveSuccess) {
//                    return;
//                }
                //传递事件点击对焦
                Log.d(TAG, "传递事件点击对焦");
//                onClick(mCaptureView.getRootView());
//                mCaptureView.dispatchTouchEvent(curTouchEvent);
            }

        }

        if (goodsPanel.getVisibility() == View.VISIBLE) {
            hideGoodsPanel();
        }
    }

    MotionEvent curTouchEvent;
    boolean isMultiPointing;


    public void dispatchTouchEvent(MotionEvent event) {
        if (!isCreater) {//  !isStartLiveSuccess
            if (!isAutoScroll) {
                final int action = event.getAction();
                switch (action & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "dispatchTouchEvent SCROLL ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "dispatchTouchEvent SCROLL ACTION_UP");
                        setAutoScroll(true);
                        break;
                }
            }
        }
    }

    private void setAutoScroll(boolean isScroll) {
        if (isScroll) {
            int lostPos = linearLayoutManager.findLastVisibleItemPosition();
            if (lostPos == linearLayoutManager.getItemCount() - 1) {
                //Toast.makeText(ShowActivity.this, "最后：" + lostPos, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "scroll 在最后一行 马上可自动滚动 lostPos=" + lostPos);
                isAutoScroll = true;
                handler.removeCallbacks(setAutoScroll);
            } else {
                handler.postDelayed(setAutoScroll, AUTO_SCROLL_DELAY);
                Log.d(TAG, "scroll onScrollStateChanged SCROLL_STATE_IDLE 5秒后可自动滚动");
            }
        } else {
            isAutoScroll = false;
            handler.removeCallbacks(setAutoScroll);
        }

    }


    private void sendComment() {
        String sComment = etComment.getText().toString().trim();
        if (Utils.isEmpty(sComment)) {
            ToastHelper.showToast(R.string.please_enter_content);
            return;
        }

        if (!NetworkUtil.isNetworkOk(getActivity())) {
            ToastHelper.showToast(R.string.network_unok_wait_try);
            return;
        }

        //插入评论列表显示
        etComment.setText("");


        if (isDanmuMode) {
            if (isCreater) {
//                 AnalyticsReport.onEvent(this, LiveReport.MY_LIVE_EVENT_11212);
            } else {
                AnalyticsReport.onEvent(mBaseActivity, LiveReport.MY_LIVE_EVENT_11235);
            }
            roomManger.sendBullet(sComment);
            //发弹幕
//            addDanmaKuShowTextAndImage(false, sComment);
        } else {
            if (isCreater) {
                AnalyticsReport.onEvent(mBaseActivity, LiveReport.MY_LIVE_EVENT_11214);
            } else {
                AnalyticsReport.onEvent(mBaseActivity, LiveReport.MY_LIVE_EVENT_11234);
            }
            roomManger.sendMessage(sComment);
            //发在评论列表
//          addCommentToList(item);
        }

    }

    private void addCommentToList(ChatEntity item) {
        chatList.add(item);
        notifyComment();
    }

    private void showCommentInput() {
        Log.d(TAG, "键盘弹出状态 rootView");

        btnGift.setVisibility(View.GONE);
        llBottomBar.setVisibility(View.GONE);
        rlComment.setVisibility(View.VISIBLE);
        etComment.requestFocus();
        mBaseActivity.showSoftInput();
        if (giftGridView != null) {
            giftGridView.setVisibility(View.GONE);
        }
        if (giftGridViewLand != null) {
            giftGridViewLand.setVisibility(View.GONE);
        }
        if (isCreater) {//刷新歌词view位置
            if (mRlLrc.getVisibility() == View.VISIBLE) {
                mRlLrc.invalidate();
                layoutRlLrc();
            }
        }
        hideTopBar();

        if (vpContainer.getVisibility() == View.VISIBLE)
            setVideoHeightFull(false);

        //设置礼物框位置
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) giftShowView.getLayoutParams();
        lp.setMargins(0, Utils.dip2px(getContext(), -15), 0, Utils.dip2px(getContext(), 197));
        giftShowView.setLayoutParams(lp);
        //刷新礼物栏位置
        giftShowView.invalidate();
    }

    private void hideTopBar() {
        if (rlTopBar.getVisibility() != View.GONE) {
            rlTopBar.setVisibility(View.GONE);
            setGiftShowViewSoft();
        }
    }

    private void showTopBar() {
        if (rlTopBar.getVisibility() != View.VISIBLE) {
            rlTopBar.setVisibility(View.VISIBLE);
            setGiftShowViewNormal();
        }

    }

    private void hideCommentInput() {
        Log.d(TAG, "键盘收起状态 rootView");

        btnGift.setVisibility(View.VISIBLE);
        llBottomBar.setVisibility(View.VISIBLE);
        rlComment.setVisibility(View.INVISIBLE);
        mBaseActivity.hideSoftInput(mBaseActivity);
        if (giftGridView != null) {
            giftGridView.setVisibility(View.GONE);
        }
        if (giftGridViewLand != null) {
            giftGridViewLand.setVisibility(View.GONE);
        }
        hideFaceView();

        if (vpContainer.getVisibility() == View.VISIBLE)
            setVideoHeightFull(true);

        if (isCreater) {//刷新歌词view位置
            if (mRlLrc.getVisibility() == View.VISIBLE) {
                mRlLrc.invalidate();
                layoutRlLrc();
            }
        }

        //设置礼物框位置
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) giftShowView.getLayoutParams();
        lp.setMargins(0, Utils.dip2px(getContext(), -15), 0, Utils.dip2px(getContext(), 293));
        giftShowView.setLayoutParams(lp);
        //刷新礼物栏位置
        giftShowView.invalidate();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showTopBar();
                rlTopBar.invalidate();
            }
        }, 500);

    }

    private void showGiftView() {
        Log.d(TAG, "showGiftView");
        AnalyticsReport.onEvent(mBaseActivity, LiveReport.MY_LIVE_EVENT_11243);
        if (isPortrait) {
            if (giftGridView == null) {
                initGiftView();
            }
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
            if (giftGridViewLand == null) {
                initGiftView();
            }
            if (Utils.listIsNullOrEmpty(giftGridViewLand.giftList)) {
                giftGridViewLand.giftList = GiftResManager.getInstance().getGiftList();
            }
            if (Utils.listIsNullOrEmpty(giftGridViewLand.giftList)) {
                ToastHelper.showToast("获取礼物列表中,请稍候");
                GiftResManager.getInstance().checkGiftUpdate();
                return;
            }
            giftGridViewLand.hideMultiGiftMode();
            giftGridViewLand.show();
        }

        llBottomBar.setVisibility(View.GONE);
        rlComment.setVisibility(View.INVISIBLE);
        mBaseActivity.hideSoftInput(mBaseActivity);
        lvComment.setVisibility(View.INVISIBLE);
        //设置不可滑动
        vpContainer.setScrollble(true);
    }

    private void hideGiftView() {
        if (giftGridView != null) {
            giftGridView.hide();
        }
        if (giftGridViewLand != null) {
            giftGridViewLand.hide();
        }
        vpContainer.setScrollble(true);
        rlComment.setVisibility(View.INVISIBLE);
        llBottomBar.setVisibility(View.VISIBLE);
        lvComment.setVisibility(View.VISIBLE);
    }

    private boolean mChecked = true;

    private void onSwitchCamera() {
        mFrontCamera = !mFrontCamera;
        mQavsdkControl.setIsFrontCamera(mFrontCamera);
        UiPreference.putBoolean(Common.KEY_IS_FRONT_CAMERA, mFrontCamera);
        Log.d(TAG, "切换摄像头 mFrontCamera=" + mFrontCamera);
//        if (mLivePusher.isPushing()) {
//        mLivePusher.switchCamera();
        mLivePusher.setFrontCamera(mFrontCamera);
        mLivePusher.setFrontCam(mFrontCamera);

        if (mFrontCamera) {
            roomManger.postLiveData((short) 4, (short) 1);
        } else {
            roomManger.postLiveData((short) 4, (short) 0);
        }

        updateCameraDevice();
//        } else {
//            mLivePushConfig.setFrontCamera(mFrontCamera);
//        }
    }


    private void addLike(String userID, int count) {
        if (true) {//隐藏点赞
            return;
        }

        if (!isResume() || Utils.listIsNullOrEmpty(likeList) || count <= 0) {
            return;
        }
//        periscopeLayout.addHeart();
        int zhubo = Integer.parseInt(mHostIdentifier);
        int other = Integer.parseInt(userID);
//        int res = (zhubo + other) % 6;
        Object[] likes = new Object[count];
        for (int i = 0; i < count; i++) {
//            likes[i] = res;
            likes[i] = (int) Math.round(Math.random() * (likeList.size() - 1));
        }
        mDivergeView.setDiverges(likes);
        if (!Utils.listIsNullOrEmpty(likeList))
            mDivergeView.start();
    }

    public void showUserInfoPanel(String userId, String userHead) {
        hideGoodsPanel();
        Log.d(TAG, " showUserInfoPanel userId=" + userId + " userHead=" + userHead
                + " isPortrait=" + isPortrait);
        if (!isResume()) {
            return;
        }
        if (isCreater) {
            AnalyticsReport.onEvent(mBaseActivity, LiveReport.MY_LIVE_EVENT_11222);
        } else {
            AnalyticsReport.onEvent(mBaseActivity, LiveReport.MY_LIVE_EVENT_11238);
        }

        if (userId.equals(AccountInfoManager.getInstance().getCurrentAccountUserIdStr())) {
            Log.d(TAG, " 自己的不弹出");
            return;
        }

        if (mUserInfoPanel == null) {
            initUserInfoPanel();
        }
        if (mUserInfoPanel.isShowing()) {
            return;
        }
        if (userId.equals(mHostIdentifier)) {
            mUserInfoPanel.setCurrentUserIsHost(true);
        } else {
            mUserInfoPanel.setCurrentUserIsHost(false);
        }
        mUserInfoPanel.showPanel(userId, userHead);
        mUserInfoPanel.setRotation(isPortrait, isCreater, mFrontCamera);
    }

    private void handleOnClickCancelManagerButton(UserInfo userInfo) {
        if (roomManger == null) {
            return;
        }
        if (userInfo == null) {
            return;
        }
        if (TextUtils.isEmpty(userInfo.getIdStr())) {
            return;
        }
        roomManger.sendRemoveRoomAdmin(userInfo.getIdStr());
        LiveRoomAdminManager.getInstance().setTempUserInfo(userInfo);
    }

    private void handleOnClickAddManagerButton(UserInfo userInfo) {
        if (roomManger == null) {
            return;
        }
        if (userInfo == null) {
            return;
        }
        if (TextUtils.isEmpty(userInfo.getIdStr())) {
            return;
        }
        boolean isManager = false;
        for (UserInfo user : LiveRoomAdminManager.getInstance().getAdministrators()) {
            if (user.getIdStr().equals(userInfo.getIdStr())) {
                isManager = true;
            }
        }
        if (isManager) {
            return;
        }
        if (LiveRoomAdminManager.getInstance().getAdministrators().size() >= LiveManagerConstant.MANAGER_MAX_COUNT) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tryOpenManagerHasEnoughAlertDialog();
                }
            }, 500);
            return;
        }
        roomManger.sendAddRoomAdmin(userInfo.getIdStr());
        LiveRoomAdminManager.getInstance().setTempUserInfo(userInfo);
    }

    private void tryOpenManagerHasEnoughAlertDialog() {
        showButtonDialog(R.string.live_manager_title, R.string.live_manager_has_enough_tip, R.string.live_manager_list_title, R.string.cancel,
                true, true, new IDialogOnClickListener() {
                    @Override
                    public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                        if (viewId == GenericDialog.ID_BUTTON_YES) {
                            handleOnClickManagerListButton();
                        }
                        return false;
                    }
                });
    }

    private void handleOnClickManagerListButton() {
        new LiveRoomManagerListPanel(mBaseActivity).showPanel();
    }

    private void handleOnClickCancelForbiddenButton(String userId) {
        if (roomManger == null) {
            return;
        }
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        roomManger.sendCancelForbidden(userId);
        LiveRoomAdminManager.getInstance().mTempUserId = userId;
    }

    private void handleOnClickForbiddenButton(String userId) {
        if (roomManger == null) {
            return;
        }
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        roomManger.sendForbid(userId);
        LiveRoomAdminManager.getInstance().mTempUserId = userId;
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
                + (mUserInfo.getIdStr().equals(mHostIdentifier) ? ResourceHelper.getString(R.string.user_info_sure_report_live_tip)
                : ResourceHelper.getString(R.string.user_info_sure_report_user_tip))
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
        if (!isCreater && mFrontCamera) {
            rlDialog.setRotation(-90);
        } else {
            rlDialog.setRotation(90);
        }
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

    UserInfo zhuboUserInfo;

    private void showExitDialogLand() {
        ViewGroup.LayoutParams lp = rlDialog.getLayoutParams();
        lp.height = mRootView.getWidth();
        lp.width = mRootView.getHeight();
        rlDialog.setLayoutParams(lp);
        int newX = -(mRootView.getHeight() - mRootView.getWidth()) / 2;
        int newY = (mRootView.getHeight() - mRootView.getWidth()) / 2;
        rlDialog.setX(-(mRootView.getHeight() - mRootView.getWidth()) / 2);
        rlDialog.setY((mRootView.getHeight() - mRootView.getWidth()) / 2);
        tvDialogTitle.setText(R.string.wenxin_tips);
        int contentRes;
        if (isCreater) {
            contentRes = R.string.live_room_host_exit_tip;
        } else {
            contentRes = R.string.live_room_user_exit_tip;
        }
        tvDialogContent.setText(contentRes);
        btnDialogNo.setText(R.string.cancel);
        btnDialogYes.setText(R.string.yes);
        btnDialogYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickYesExitDialog();
                rlDialog.setVisibility(View.GONE);
            }
        });
        rlDialog.setVisibility(View.VISIBLE);
        if (!isCreater && mFrontCamera) {
            rlDialog.setRotation(-90);
        } else {
            rlDialog.setRotation(90);
        }
    }

    private void showExitDialog() {
        int contentRes;
        if (isCreater) {
            contentRes = R.string.live_room_host_exit_tip;
        } else {
            contentRes = R.string.live_room_user_exit_tip;
        }
        showButtonDialog(R.string.live_room_exit_tip, contentRes, R.string.yes, R.string.cancel,
                true, true, new IDialogOnClickListener() {
                    @Override
                    public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                        if (viewId == GenericDialog.ID_BUTTON_YES) {
                            clickYesExitDialog();
                        }
                        return false;
                    }
                });
    }

    private void clickYesExitDialog() {
        // 删除正在直播的课程id
        UiPreference.putString(mUserInfo.getIdStr(), null);

        if (!isCreater) {//用户主动退出直播间，不显示直播结束页面
            isCloseRoom = true;
            finish();
//                                onCloseVideo();//测试心事结束页面
        } else {
            if (isCreateRoom) {
                onCloseVideo();
            } else {
                finish();
            }
        }
    }

    private void showLiveFailDialog() {
        showButtonDialog(R.string.live_tips, R.string.start_live_fail, R.string.yes, 0,
                false, false, true, true, new IDialogOnClickListener() {
                    @Override
                    public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                        if (viewId == GenericDialog.ID_BUTTON_YES) {
                            onCloseVideo();
                        }
                        return false;
                    }
                });
    }

    private void showNoNetDialog() {
        showButtonDialog(R.string.live_tips, R.string.network_unok_suggest_change, R.string.yes, R.string.cancel,
                false, false, true, true, new IDialogOnClickListener() {
                    @Override
                    public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                        return false;
                    }
                });
    }

    private void showNoCameraOrMicDialog(int textId) {
        showButtonDialog(R.string.live_tips, textId, R.string.yes, 0,
                false, false, new IDialogOnClickListener() {
                    @Override
                    public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                        if (viewId == GenericDialog.ID_BUTTON_YES) {
                            onCloseVideo();
                        }
                        return false;
                    }
                });
    }


    /**
     * 回调选择的当前礼物
     */
    @Override
    public void setChooseGift(GiftInfo gift) {
        chooseGift = gift;
    }


    /**
     * 顶部直播计时
     */
    int totalTime;

    /**
     * 更新顶部直播计时器
     */
    private void addSecond() {
        totalTime++;
        tvTimer.setText(Utils.secToFullTime(totalTime));
        CacheUtil.addCache("totalTime", totalTime);
    }

    private void getMyUserInfo() {
//        DataProvider.queryProduct(this, new GsonHttpConnection.OnResultListener<ProductsListMsg>() {
//            @Override
//            public void onSuccess(ProductsListMsg msg) {
//                if (msg.isSuccessFul()) {
//                    giftGridView.setBalance(msg.getBalance());
//                }else{
//                }
//
//            }
//
//            @Override
//            public void onFail(int errorCode, String errorMsg) {
//
//            }
//        });
    }


    private void getZhuboUserInfo() {
        if (TextUtils.isEmpty(mHostIdentifier)) {
            return;
        }
        Log.d(TAG, "intent getZhuboUserInfo");
        ImageUtil.loadResImage(R.drawable.blank_icon_avatar, ivZhuboHead);
        ivZhuboHead.setMark(null);
        tvLakaNo.setText(getString(R.string.ziwei_id));
        DataProvider.getUserInfo(this, mHostIdentifier, String.valueOf(mRoomId), true, new GsonHttpConnection.OnResultListener<UserMsg>() {
            @Override
            public void onSuccess(UserMsg userMsg) {
                if (userMsg.isSuccessFul()) {
                    UserInfo info = userMsg.getUserInfo();
                    if (info != null) {
                        zhuboUserInfo = info;
                        ImageUtil.loadImage(ivZhuboHead, zhuboUserInfo.getAvatar());
//                        int markId = MarkSimpleDraweeView.getMarkDrawableId(MarkSimpleDraweeView.getAuthType(zhuboUserInfo.getStarVerified()
//                                , zhuboUserInfo.getVerified()),
//                                MarkSimpleDraweeView.SizeType.SMALL, zhuboUserInfo.getLevel());
//                        ivZhuboHead.setMark(markId);

                        //不显示等级标识
//                        if (MarkSimpleDraweeView.getAuthType(zhuboUserInfo.getAuth()) != MarkSimpleDraweeView.AuthType.NONE || zhuboUserInfo.getLevel() > 1) {
//                            int markId = MarkSimpleDraweeView.getMarkDrawableId(MarkSimpleDraweeView.getAuthType(zhuboUserInfo.getAuth()),
//                                    MarkSimpleDraweeView.SizeType.SMALL, zhuboUserInfo.getLevel());
//                            ivZhuboHead.setMark(markId);
//                        } else {
//                            ivZhuboHead.setMark(null);
//                        }

                        //保存当前主播
                        CacheUtil.addCache("curZhuboUserInfo", zhuboUserInfo);

                        String sName = zhuboUserInfo.getNickName();
                        Log.d(TAG, " sName length=" + sName.length());
                        if (!Utils.isEmpty(sName) && sName.length() > 20) {
                            sName = sName.substring(0, 20) + "...";
                        }
                        tvZhiboLive.setText(sName);
                        tvLakaNo.setText(getString(R.string.ziwei_id) + String.valueOf(zhuboUserInfo.getId()));
                        if (isCreater) {

                        } else {
//                            if (zhuboUserInfo.getFollow() == ListUserInfo.FOLLOWED) {
//                                btnFollow.setVisibility(View.GONE);
//                            } else {
//                                btnFollow.setVisibility(View.VISIBLE);
//                                handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        btnFollow.setVisibility(View.GONE);
//                                    }
//                                }, 1000 * 30);
//                            }
                        }


                    }
                }
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {

            }
        });
    }


//    @Subscriber(tag = "test")
//    private void eventTest(int code) {
//        Log.d(TAG,"测试 Sticky eventTest ");
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PostEvent event) {
        if (SubcriberTag.SHOW_FLOAT_LIVE.equals(event.tag)) {
            Log.d(TAG, " onEvent SHOW_FLOAT_LIVE");
            showFloatLive();
        } else if (SubcriberTag.PAY_RESULT_BACK.equals(event.tag)) {
            Log.d(TAG, " onEvent PAY_RESULT_BACK 重新开启小窗");
//            showFloatLive();
        } else if (SubcriberTag.RE_SHOW_FLOAT_LIVE.equals(event.tag)) {
            if (!isResume()) {
                Log.d(TAG, " onEvent RE_SHOW_FLOAT_LIVE 重新开启小窗");
                showFloatLive();
            } else {
                Log.d(TAG, " onEvent RE_SHOW_FLOAT_LIVE 不需重新开启小窗");
            }

        }
//        Log.d(TAG, "onEventThread-->" + Thread.currentThread().getId());
        else if (SubcriberTag.SOCKET_ERROR_TIPS.equals(event.tag)) {
            if (isResume() && !isPushing) {
                showToast((String) event.event);
            }
        } else if (SubcriberTag.ROOM_SHARE_SUCCESS.equals(event.tag)) {
            int shareType = (int) event.event;
            if (shareType == 2) {
                shareType = mBaseActivity.share.curShareType;
            }
            roomManger.postLiveData((short) 1, (short) shareType);
        } else if (SubcriberTag.ROOM_SHOW_COMMENT.equals(event.tag)) {
            boolean isShow = (boolean) event.event;
            Log.d(TAG, " ROOM_SHOW_COMMENT isShow=" + isShow);
            lvComment.setVisibility(isShow ? View.VISIBLE : View.GONE);
            if (isShow) {
                rlComment.setVisibility(View.INVISIBLE);
                llBottomBar.setVisibility(View.GONE);
            }

        } else if (SubcriberTag.MULTI_SEND_GIFT_FINISH.equals(event.tag)) {
            hideMultiGiftMode();
        } else if (SubcriberTag.SHOW_USER_POP.equals(event.tag)) {
            String userId = (String) event.event;
//            if (mRoomUserPop == null || !mRoomUserPop.isShowing()) {
            showUserInfoPanel(userId, null);
//            }
        } else if (SubcriberTag.REQUEST_VIEW_SUCCESS.equals(event.tag)) {
            Utils.doTimeCountEnd();
//        mLoading.setVisibility(View.GONE);
            pushLiveSuccess();
//            hideLoading();
//            //设置可滑动
//            vpContainer.setScrollble(true);
//            llAVVideo.setVisibility(View.VISIBLE);
//            llBg.setVisibility(View.INVISIBLE);
//            Log.d(TAG, " llAVVideo.getVisibility()=" + llAVVideo.getVisibility());
//            retryHostReqauestViewCnt = 0;//重置可重试次数
////            rootView.setBackgroundResource(R.color.transparent);
//            isGetVideoSuccess = true;
            if (isCreater) {
//                isStartLiveSuccess = true;
//                AnalyticsReport.onEvent(this, LiveReport.MY_LIVE_EVENT_11228_s);
//                initTimer();//主播开启timer
//                if (!isRecording) {
//                    startRecording();
//                }
//                if (!isMultiVideoing) {
//                    startMultiVideo();
//                }
//
//                //设置自动对焦
//                setAutoFocus();
                //设置上次滤镜
//                GPUImageFilter filter = mFilterPanel.getLastFilter();
//                Log.d(TAG, "设置上次滤镜 filterId=" + filter.getFilterId());
//                onFilterChange(filter);
            } else {
                HashMap<String, String> parmas = new HashMap<>();
                parmas.put("Live_id", mHostIdentifier);
                parmas.put("Laka_id", AccountInfoManager.getInstance().getCurrentAccountUserIdStr());
                AnalyticsReport.onEvent(mBaseActivity, LiveReport.MY_LIVE_EVENT_11252_s, parmas);
            }
            mMarkerLog.add("done");
            mMarkerLog.finish("video show time");
        } else if (SubcriberTag.REFRESH_LAST_KAZUAN.equals(event.tag)) {
//            if(isPortrait){
//                showRechargeDialog("");
//            }else{
//                showRechargeDialogLand("");
//            }
            double result;
            if (event.event instanceof Double) {
                result = (double) event.event;
            } else {
                result = Double.valueOf(event.event + "");
            }

            if (giftGridView != null) {
                giftGridView.setBalance(result);
            }
            if (giftGridViewLand != null) {
                giftGridViewLand.setBalance(result);
            }
            Log.d(TAG, "REFRESH_LAST_KAZUAN coins=" + result);
            AccountInfoManager.getInstance().updateCurrentAccountCoins(result);
        } else if (SubcriberTag.SET_COMMENT_AUTO_SCROLL.equals(event.tag)) {
            boolean flag = (boolean) event.event;
            setAutoScroll(flag);

        } else if (SubcriberTag.SHOW_MESSAGE_PANEL_CHAT.equals(event.tag)) {
            if (!isPortrait) {
                btnOrientation.performClick();
            }
            ChatSession session = (ChatSession) event.event;
            showMessagePanel(session, -1);
        } else if (SubcriberTag.SHOW_MESSAGE_PANEL.equals(event.tag)) {
            if (!isPortrait) {
                btnOrientation.performClick();
            }
            ChatSession session = (ChatSession) event.event;
            showMessagePanel(session, session.getType());
        } else if (SubcriberTag.SHOW_SESSION_PANEL.equals(event.tag)) {
            if (mChatMessagePanel != null) {
                hideChatMessagePanel();
                mChatMessagePanel.isFromSession = -1;
            }
            showSessionPanel();
        } else if (SubcriberTag.SHOW_UNFOLLOW_PANEL.equals(event.tag)) {
            if (mChatMessagePanel != null) {
                hideChatMessagePanel();
                mChatMessagePanel.isFromSession = -1;
            }
            if (mChatSessionPanel != null)
                mChatSessionPanel.hidePanel();
            showUnFollowPanel();
            mBaseActivity.hideKeyboard(mBaseActivity);
        } else if (SubcriberTag.HIDE_SESSION_PANEL.equals(event.tag)) {
            if (mChatSessionPanel != null) {
                mChatSessionPanel.hidePanel();
            }
        } else if (SubcriberTag.HIDE_UNFOLLOW_PANEL.equals(event.tag)) {
            if (mChatUnfollowPanel != null) {
                mChatUnfollowPanel.hidePanel();
            }
            showSessionPanel();
            mBaseActivity.hideKeyboard(mBaseActivity);
        } else if (SubcriberTag.HIDE_CONTRIBUTION_PANEL.equals(event.tag)) {
//            if (mContributionPanel != null) {
//                mContributionPanel.hidePanel();
//            }
        } else if (SubcriberTag.LIVE_OPEN_MIC.equals(event.tag)) {
            toggleMic();
        } else if (SubcriberTag.LIVE_SWITCH_CAMERA.equals(event.tag)) {
//            showToast("切换摄像头");
            //切换摄像头
            AnalyticsReport.onEvent(mBaseActivity, LiveReport.MY_LIVE_EVENT_11215);
            onSwitchCamera();

            //预览按钮
            if (mFrontCamera) {
                checkShowBtnBeauty();
            } else {
//                btnBeauty.setVisibility(View.GONE);
            }
        } else if (SubcriberTag.SHOW_BEAUTY_PANEL.equals(event.tag)) {
            AnalyticsReport.onEvent(mBaseActivity, LiveReport.MY_LIVE_EVENT_11217);
            isEnableBeauty = !isEnableBeauty;
            UiPreference.putBoolean(Common.KEY_IS_ENABLE_BEAUTY, isEnableBeauty);
            Log.d(TAG, "设置美颜 isEnableBeauty=" + isEnableBeauty);
            setBeautyEnable();

            //预览按钮
            refreshBtnBeauty();
        } else if (SubcriberTag.SHOW_FILTER_PANEL.equals(event.tag)) {
            showFilterPanel();
        } else if (SubcriberTag.FLASH_LIGHT_ONOFF.equals(event.tag)) {
            AnalyticsReport.onEvent(mBaseActivity, LiveReport.MY_LIVE_EVENT_11219);
            //开关闪光灯
            flashLightOnOff();
        } else if (SubcriberTag.FORBID_OPEN_ROOM.equals(event.tag)) {
            Log.d(TAG, "收到后台控制用户禁止直播,跳结束");
//            showForbitLive();
            LiveFinishActivity.startActivity(mBaseActivity, true, "", END_TYPE_FORBID, mCourseId);
            finish();
        } else if (SubcriberTag.REFRESH_RANKING_LIST_DATA.equals(event.tag)) {
            handleOnRefreshDataChange(event.event);
        } else if (TextUtils.equals(SubcriberTag.RECEIVE_CHAT_MSG, event.tag)) {
            showRedPoint();

        } else if (TextUtils.equals(SubcriberTag.RECEIVE_CHAT_GIFT, event.tag)) {
            showRedPoint();
        } else if (TextUtils.equals(SubcriberTag.RECEIVE_SYSTEM_MSG, event.tag)) {
            showRedPoint();
//        } else if (SubcriberTag.ADD_TOPIC.equals(event.tag)) {
//            String addTopic = (String) event.event;
//            //插到开头
//            String sTitle = mTitleTv.getText().toString();
//            sTitle = addTopic + " " + sTitle;
//            mTitleTv.setText(sTitle);
        } else if (SubcriberTag.CLOSE_ZHUBO_MORE_PANEL.equals(event.tag)) {
            btnMore.setBackgroundResource(R.drawable.live_btn_more_selector);
        } else if (SubcriberTag.ON_TOUCH_VIDEO_VIEW.equals(event.tag)) {
//            Log.d(TAG, " ON_TOUCH_VIDEO_VIEW");
            if (isCreater) {
//                Log.d(TAG, " 处理点击对焦");
                MotionEvent touch = (MotionEvent) event.event;

//                mCaptureView.dispatchTouchEvent(touch);
                vlBigView.dispatchTouchEvent(touch);
            }
            handleTouchScreen();
        } else if (SubcriberTag.NOTICE_ZHUBO_LEAVE.equals(event.tag)) {
            if (!isCreater) {
                addSystemNotice(ResourceHelper.getString(R.string.zhubo_leave));
                isZhuboTemporaryLeave = true;
                showLoading(R.string.zhubo_tmp_leave);
            }
        } else if (SubcriberTag.NOTICE_ZHUBO_RETURN.equals(event.tag)) {
            if (!isCreater) {
                if (isZhuboTemporaryLeave) {
                    hideLoading();
                }
                isZhuboTemporaryLeave = false;
                addSystemNotice(ResourceHelper.getString(R.string.zhubo_return));
                //同步最新地址
                syncPlayUrl();

                //小窗同步
                if (FloatWindow.getInstance().isShowing()) {
                    showFloatLive();
                }

            }
        } else if (SubcriberTag.ADD_SYSTEM_NOTICE.equals(event.tag)) {
            addSystemNotice((String) event.event);
        } else if (SubcriberTag.USER_ROOM_OFFINE.equals(event.tag)) {
            if (!isCreater) {
                enterRoom(mHostIdentifier);
            } else {
                boolean isUploadLocation = UiPreference.getBoolean(Common.IS_UPLOAD_LOCATION, true);
                mLocation = isUploadLocation ? UiPreference.getString(Common.KEY_MY_LOCATION_CITY,
                        mSelfUserInfo.getRegion()) : "";
                createRoom();
            }
        } else if (SubcriberTag.WIFI_CHANGE_MOBILE.equals(event.tag)) {
            if (NetStateManager.getInstance().getIsAlreadyNotify()) {
                mBaseActivity.showToast(R.string.in_mobile_net_be_careful);
            } else {
                if (isCreater) {
                    NetStateManager.getInstance().showMobileNetWorkDialog(mBaseActivity, R.string.in_mobile_net_suggest_wifi_live, R.string.change_net, R.string.goon_live, new IDialogOnClickListener() {
                        @Override
                        public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                            if (viewId == GenericDialog.ID_BUTTON_YES) {
                                //跳去设置网络
                                NetStateManager.getInstance().goToNetSetting(mBaseActivity);
                            }
                            return false;
                        }
                    });
                } else {
                    NetStateManager.getInstance().showMobileNetWorkDialog(mBaseActivity, R.string.in_mobile_net_suggest_wifi_see, R.string.change_net, R.string.goon_see, new IDialogOnClickListener() {
                        @Override
                        public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                            if (viewId == GenericDialog.ID_BUTTON_YES) {
                                //跳去设置网络
                                NetStateManager.getInstance().goToNetSetting(mBaseActivity);
                            }
                            return false;
                        }
                    });
                }
            }
        } else if (SubcriberTag.HAS_NET_TO_NON_NETWORK.equals(event.tag)) {
            if (isCreater) {
                showNoNetDialog();
            } else {
                mBaseActivity.showToast(R.string.network_unok_wait_try);
            }
        } else if (SubcriberTag.APP_ENTER_BACKGROUND.equals(event.tag)) {

            if (isCreater) {
//                if (mLivePusher != null && mLivePusher.isPushing()) {
//                    stopPublishRtmp();
//                }
                if (mBgmManager != null) {
                    mBgmManager.pause();
                }
                isZhuboTemporaryLeave = true;
                roomManger.zhuboTemporaryLeave();
                Log.d(TAG, "APP_ENTER_BACKGROUND 停止推流,停止bgm");
            } else {
                Log.d(TAG, " Build.VERSION.SDK_INT=" + Build.VERSION.SDK_INT);
                //6.0暂停播放
//                if (Build.VERSION.SDK_INT >= 23 && mPlayerView != null) {
//                    Log.d(TAG, "6.0退后台暂停播放");
//                    stopPlayRtmp();
//                }
            }

        } else if (SubcriberTag.APP_ENTER_FOREGROUND.equals(event.tag)) {
            Log.d(TAG, " SubcriberTag.APP_ENTER_FOREGROUND isResume=" + isResume());
            if (isCreater) {//&& !isResume()
                pushResume();
                if (mBgmManager != null) {
                    mBgmManager.resume();
                }
                Log.d(TAG, "APP_ENTER_FOREGROUND 恢复推流,恢复bgm");
//                startPushResumeCheck();
            } else {
//                if (Build.VERSION.SDK_INT >= 23 && mVideoPlay) {
//                    showLoading();
//                    Log.d(TAG, "6.0回到后台恢复播放");
//                    startPlayRtmp();
//                }
            }
        } else if (SubcriberTag.ROOM_USER_LIST_CHANGE.equals(event.tag)) {
//            timerRoomUser = 0;
//            if (!isQueryingRoomUser)
//                queryRoomUser(0);
        } else if (SubcriberTag.MUSIC_PLAY_EVENT.equals(event.tag)) {
            MusicInfo musicInfo = (MusicInfo) event.event;
            startBgm(musicInfo.getMusicFilePath(), musicInfo.getLyricsFilePath());
        } else if (SubcriberTag.AUDIO_TRACK_STOP_BGM.equals(event.tag)) {
            mRlLrc.setVisibility(View.INVISIBLE);
            if (mAudioEffectPanel != null) {
                mAudioEffectPanel.hidePanel();
            }
        } else if (SubcriberTag.AUDIO_TRACK_PLAY_BGM.equals(event.tag)) {
            mRlLrc.setVisibility(View.VISIBLE);
        } else if (SubcriberTag.SOCKET_NOT_CONNECT.equals(event.tag)) {
            if (isResume()) {
                mBaseActivity.showToast(R.string.service_not_connect);
            }
        } else if (SubcriberTag.SOCKET_TRY_AGAIN_LATER.equals(event.tag)) {
            //重试处理
            int resCode = (int) event.event;
            switch (resCode) {
                case RoomManager.TLV_RSP_ENTER_ROOM:
                    if (!isCreater) {
                        enterRoom(mRoomId + "");
                    }
                    break;
//                case RoomManager.TLV_RSP_QUERY_ROOM_USER:
//                    timerRoomUser = 0;
//                    queryRoomUser(0);
//                    break;
                case RoomManager.TLV_RSP_QUERY_ROOM_DATA:
                    roomManger.queryRoomData();
                    break;
                case RoomManager.TLV_RSP_LEAVE_ROOM:
                    if (!isCreater) {
                        exitLiveRoom();
                    }
                    break;
//                case RoomManager.TLV_RSP_ROOM_SAY:
//                    roomManger.sendMessage();
//                    break;
//                case RoomManager.TLV_RSP_ROOM_GIVE:
//                    break;
//                  case RoomManager.TLV_RSP_ROOM_BULLET:
//                    break;
                case RoomManager.TLV_RSP_ROOM_LIKE:
                    addLike(myUserId, 1);
                    break;
                case RoomManager.TLV_RSP_BROADCATER_LEAVE:
                    roomManger.zhuboTemporaryLeave();
                    break;
                case RoomManager.TLV_RSP_BROADCASTER_RETURN:
                    roomManger.zhuboReturn();
                    break;
            }
        } else if (event.tag.equals(SubcriberTag.RECEIVE_LINK_APPLY)) {
            mZhuboLinkRedpoint.setVisibility(View.VISIBLE);
        } else if (TextUtils.equals(event.tag, ON_USER_INFO_ITEM_CLICK_EVENT)) {
            ConnectUserInfo userInfo = (ConnectUserInfo) event.event;
            showUserInfoPanel(String.valueOf(userInfo.getId()), userInfo.getAvatar());
        } else if (event.tag.equals(SubcriberTag.LINK_STOP_EXIT_ROOM)) {
            showStopLiveDialog((Integer) event.event);
        } else if (event.tag.equals(SubcriberTag.IS_SHOW_LINK_ICON)) {
            boolean isShow = (boolean) event.event;
            if (!isCreater) {
                // 用户端不显示连麦
                //flConnectAudience.setVisibility(isShow ? View.VISIBLE : View.GONE);
            }
        } else if (event.tag.equals(SubcriberTag.LIVE_VIDEO_ORIENTATION)) {
            int flag = (int) event.event;
            if (isCreater) {
                if (mLivePusher != null) {
                    mLivePusher.changeVideoOrientation(flag);
                }
            } else {
                if (mLivePlayer != null) {
                    mLivePlayer.changeVideoOrientation(flag);
                }
            }

        } else if (event.tag.equals(SubcriberTag.ROOM_EXTRA_INFO_UPDATE)) {
            if (!isCreater) {
                ZegoStreamInfo info = (ZegoStreamInfo) event.event;
                Log.d(TAG, " onStreamExtraInfoUpdated info=" + info.extraInfo);
                try {
                    JSONObject json = new JSONObject(info.extraInfo);
                    String cameraDevice = json.getString(Common.CAMERA_DEVICE_POSITION);
                    Log.d(TAG, " cameraDevice=" + cameraDevice);
                    if ("2".equals(cameraDevice)) {
                        mFrontCamera = true;
                    } else if ("1".equals(cameraDevice)) {
                        mFrontCamera = false;
                    }

                    if (!isPortrait) {//如果是横屏，重置一次
                        changeViewOrientation(!isPortrait);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                changeViewOrientation(isPortrait);
                            }
                        }, 200);
                    }

                } catch (JSONException e) {
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw, true));
                    Log.d(TAG, "ROOM_EXTRA_INFO_UPDATE报错:"
                            + sw.toString());
                }

            }
        } else if (event.tag.equals(SubcriberTag.LIVE_CAMERA_CHANGE)) {
            if (!isCreater) {
                int tag = (int) event.event;
                Log.d(TAG, " LIVE_CAMERA_CHANGE mFrontCamera=" + mFrontCamera);
                boolean frontCamera;
                if (tag == 1) {
                    frontCamera = true;
                } else {
                    frontCamera = false;
                }
                if (!isPortrait && mFrontCamera != frontCamera) {//如果是横屏，重置一次
                    mFrontCamera = frontCamera;
                    changeViewOrientation(!isPortrait);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            changeViewOrientation(isPortrait);
                        }
                    }, 200);
                } else {
                    mFrontCamera = frontCamera;
                }

            }

        } else if (event.tag.equals(SubcriberTag.SHOW_REPORT_DIALOG_LAND)) {
            showReportDialogLand((String) event.event);

        } else if (SubcriberTag.REFRESH_RECOMMEND_GOODS_COUNT.equals(event.tag)) {
            int count = (int) event.event;
            btnGoods.setText(String.valueOf(count));
        } else if (SubcriberTag.OPEN_MALL_TAB.equals(event.tag)) {
            Log.d(TAG, " 退出直播");
            finish();//退出直播
        }


    }

    private void showRedPoint() {
        if (mChatMessagePanel != null && mChatMessagePanel.isShown()) {
            return;
        }
        if (mChatSessionPanel != null && mChatSessionPanel.isShowing()) {
            return;
        }
//        mMsgRedPoint.setVisibility(View.VISIBLE);
//        mZhuboMsgRedPoint.setVisibility(View.VISIBLE);
    }

    private void hideChatMessagePanel() {
        mChatMessagePanel.setVisibility(View.GONE);
        mChatMessagePanel.stop();
        isChatMessagePanelShow = false;
        if (!isCreater) {
            EventBusManager.postEvent(true, SubcriberTag.ROOM_CAN_SCROLL);
        }
    }

    private void startPushResumeCheck() {
        if (!NetStateManager.getInstance().getIsNetworkOk(mBaseActivity) || isStartLiveSuccess) {
            return;
        }
        handler.removeCallbacks(pushResumCheckRunnable);
        //开启定时检测时候恢复
        Log.d(TAG, "开启定时检测时候恢复");
        handler.postDelayed(pushResumCheckRunnable, 1000 * 10);
    }

    Runnable pushResumCheckRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isResume()) {
                return;
            }

            if (mLivePusher.isPushing() && isPushing) {
                Log.d(TAG, "onPush 恢复直播成功");
            } else {
                Log.d(TAG, "onPush 恢复直播失败 重新获取推流地址");
//                getPushUrl();
//                showButtonDialog(R.string.wenxin_tips, R.string.push_resume_fail, R.string.yes, 0, false, false,  new IDialogOnClickListener() {
//                    @Override
//                    public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
//                        if (viewId == GenericDialog.ID_BUTTON_YES) {
//                            onCloseVideo();
//                        }
//                        return false;
//                    }
//                });
            }
        }
    };


    private void showForbitLive() {
        SimpleTextDialog dialog = new SimpleTextDialog(mBaseActivity);
        dialog.setTitle(ResourceHelper.getString(R.string.wenxin_tips));
        dialog.setText(ResourceHelper.getString(R.string.you_forbid_live));
        dialog.addSingleButton(GenericDialog.ID_BUTTON_YES, ResourceHelper.getString(R.string.yes));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setRecommendButton(GenericDialog.ID_BUTTON_YES);
        dialog.setOnClickListener(new IDialogOnClickListener() {
            @Override
            public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                if (viewId == GenericDialog.ID_BUTTON_YES) {
                    finish();
                }
                return false;
            }
        });
        dialog.show();
    }

    private void showMessagePanel(ChatSession session, Integer isFromSession) {
        if (mChatSessionPanel != null) {
            mChatSessionPanel.hidePanel();
        }
        if (mChatUnfollowPanel != null) {
            mChatUnfollowPanel.hidePanel();
        }
        showMessagePanel(session.getUserId(), session.getNickName(), session.getAvatar(), session.getType());
        mChatMessagePanel.isFromSession = isFromSession;
        AnalyticsReport.onEvent(mBaseActivity, LiveReport.MY_LIVE_EVENT_15006);
        if (!isCreater) {
            EventBusManager.postEvent(false, SubcriberTag.ROOM_CAN_SCROLL);
        }
    }

    private void handleOnRefreshDataChange(Object object) {
        if (object == null) {
            return;
        }
        if (mUserInfoPanel == null) {
            return;
        }
        RankingUserInfo rankingUserInfo = (RankingUserInfo) object;
        mUserInfoPanel.updateUserFollowState(String.valueOf(rankingUserInfo.getId()), rankingUserInfo.getFollow());
    }

//    private void forbidOpenRoom() {
//        showToast(R.string.forbid_open_room);
//        onCloseVideo();
//    }

    boolean isLightOn = false;

    private void flashLightOnOff() {
        if (mLivePusher == null) {
            return;
        }
        if (mFrontCamera) {
            Log.d(TAG, "前置摄像头不支持闪光灯");
            return;
        }

        mFlashTurnOn = !mFlashTurnOn;
        if (mLivePusher != null) {
            mLivePusher.enableTorch(mFlashTurnOn);
        }
//        if (!mLivePusher.turnOnFlashLight(mFlashTurnOn)) {
////            Toast.makeText(this,
////                    "打开闪光灯失败（1）大部分前置摄像头并不支持闪光灯（2）该接口需要在启动预览之后调用", Toast.LENGTH_SHORT).show();
//        }

    }

    private void showMessagePanel(String userId, String nickName, String avatar, int sessionType) {

        if (mChatMessagePanel == null) {
            initChatMessagePanel();
        }
        mChatMessagePanel.setVisibility(View.VISIBLE);
        mChatMessagePanel.start();
        isChatMessagePanelShow = true;
        mChatMessagePanel.initData(mBaseActivity, userId, nickName, avatar, sessionType);
    }

//    private void showContributionPanel() {
//        if (mContributionPanel == null) {
//            initContributionPanel();
//        }
//        mContributionPanel.showPanel(this);
//    }

    private void showSessionPanel() {
        if (mChatSessionPanel == null) {
            initChatSessionPanel();
        }
        mChatSessionPanel.showPanel(mBaseActivity);
    }

    private void showFormulaPanel() {

//        if (mFormulaPanel == null)
//            mFormulaPanel = new FormulaPanel(mBaseActivity);
        mFormulaPanel = new FormulaPanel(getContext(), isPortrait, isCreater, mFrontCamera);
        mFormulaPanel.showPanel(mFormulaMsg);
    }

    private void showAudioEffectPanel() {
        if (mAudioEffectPanel == null) {
            initAudioEffectPanel();
        }
        //todo bgm音效设置
//        mAudioEffectPanel.showPanel(mLivePusher);
    }


    private void showUnFollowPanel() {
        if (mChatUnfollowPanel == null) {
            initChatUnfollowPanel();
        }
        mChatUnfollowPanel.showPanel(mBaseActivity);
    }

    private void showFilterPanel() {
        if (mFilterPanel == null) {
            initFilterPanel();
        }
        mFilterPanel.showPanel();
    }


    //添加管理员成功处理
    private void handleOnResultAddRoomAdmin() {
        UserInfo userInfo = LiveRoomAdminManager.getInstance().getTempUserInfo();
        if (userInfo == null) {
            return;
        }
        LiveRoomAdminManager.getInstance().addAdministrator(userInfo);
        LiveRoomAdminManager.getInstance().clearTempUserInfo();
        if (mUserInfoPanel != null) {
            mUserInfoPanel.updateUserAdminState(userInfo.getIdStr(), true);
        }
        mBaseActivity.showToast(getString(R.string.live_manager_add_admin_success));
    }

    //移除管理员成功处理
    private void handleOnResultRemoveRoomAdmin() {
        UserInfo userInfo = LiveRoomAdminManager.getInstance().getTempUserInfo();
        if (userInfo == null) {
            return;
        }
        LiveRoomAdminManager.getInstance().removeAdministrator(userInfo);
        LiveRoomAdminManager.getInstance().clearTempUserInfo();
        if (mUserInfoPanel != null) {
            mUserInfoPanel.updateUserAdminState(userInfo.getIdStr(), false);
        }
        mBaseActivity.showToast(getString(R.string.live_manager_cancel_admin_success));
    }

    //禁言成功处理
    private void handleOnResultForbidden() {
        String userId = LiveRoomAdminManager.getInstance().mTempUserId;
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        tryRefreshAudienceForbiddenState(userId, true);
        mBaseActivity.showToast(getString(R.string.live_manager_forbidden_success));
    }

    /**
     * @param userId      用户id
     * @param isForbidden 是否被禁言 true 是 ，false 否
     */
    private void tryRefreshAudienceForbiddenState(String userId, boolean isForbidden) {
        if (mUserInfoPanel == null) {
            return;
        }
        mUserInfoPanel.updateUserForbiddenState(userId, isForbidden);
    }

    //取消禁言成功处理
    private void handleOnResultCancelForbidden() {
        String userId = LiveRoomAdminManager.getInstance().mTempUserId;
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        tryRefreshAudienceForbiddenState(userId, false);
        mBaseActivity.showToast(getString(R.string.live_manager_cancel_forbidden_success));
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
                mBaseActivity.showToast("关注失败");
            }
        });
    }


    ////////准备直播view开始
    boolean isReleaseReady = false;
    InputRelativeLayout releaseLiveView;
    //拍照封面相关
    private static final int PHOTO_REQUEST_TAKE_PHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int REQUEST_IMAGE = 0;
    private static final int REQUEST_REGION = 1;
    private static final String EXTRA_IS_CROP = "EXTRA_IS_CROP";
    private static final int REQUEST_TAKE_PHOTO = 4;
    private static final int REQUEST_CHOICE_PHOTO = 5;
    private static final int REQUEST_CROP_SMALL_PICTURE = 3;
    private static final int PHOTO_ASPECT = 240;
    public static final String EXTRA_RESULT = "EXTRA_RESULT";
    private File mImageFile;
    private EditText etFocus;
    private TextView mTitleTv;
    private SimpleDraweeView ivCover;
    private boolean isUploadingCover;
    private String coverUrl;
    private Button btnQQ, btnWeibo, btnMoment, btnLocate, btnWx, btnQzone, btnCameraRelease, btnBeauty, btnFilter;
    private TextView tvLocation, tvAddTopic;
    private LinearLayout llShare;//llAddPic, llAddTopic;
    private RelativeLayout rlAddPic;
    String Analytics_Title = "1", Analytics_Cover = "1";
    HashSet<String> shareTypes = new HashSet<>();

    private void initReleaseLiveView() {
        releaseLiveView = (InputRelativeLayout) findViewById(R.id.view_release_live);
        releaseLiveView.setVisibility(View.VISIBLE);
        releaseLiveView.setListener(new InputWindowListener() {
            @Override
            public void show() {
                Log.d(TAG, "releaseLiveView键盘弹出状态");
                // rlAddPic.setVisibility(View.INVISIBLE);
                setVideoHeightFull(false);
            }

            @Override
            public void hidden() {
                Log.d(TAG, "releaseLiveView键盘收起状态");
                // rlAddPic.setVisibility(View.VISIBLE);
                setVideoHeightFull(true);
            }
        });
//        share = new ShareUtil(this);
        llShare = (LinearLayout) releaseLiveView.findViewById(R.id.ll_share);
        btnQQ = (Button) releaseLiveView.findViewById(R.id.btn_share_qq);
        btnQQ.setOnClickListener(releaseOnClickListener);
        btnWeibo = (Button) releaseLiveView.findViewById(R.id.btn_share_sina);
        btnWeibo.setOnClickListener(releaseOnClickListener);
        btnWx = (Button) releaseLiveView.findViewById(R.id.btn_share_wx);
        btnWx.setOnClickListener(releaseOnClickListener);
        btnMoment = (Button) releaseLiveView.findViewById(R.id.btn_share_timeline);
        btnMoment.setOnClickListener(releaseOnClickListener);
        btnQzone = (Button) releaseLiveView.findViewById(R.id.btn_share_qzone);
        btnQzone.setOnClickListener(releaseOnClickListener);

        btnCamera = (Button) releaseLiveView.findViewById(R.id.btn_camera);
        btnCamera.setOnClickListener(releaseOnClickListener);
        btnBeauty = (Button) releaseLiveView.findViewById(R.id.btn_beauty);
        btnBeauty.setOnClickListener(releaseOnClickListener);


        btnFilter = (Button) releaseLiveView.findViewById(R.id.btn_filter);
        btnFilter.setOnClickListener(releaseOnClickListener);
        btnLocate = (Button) releaseLiveView.findViewById(R.id.btn_location);
        btnLocate.setOnClickListener(releaseOnClickListener);


        tvLocation = (TextView) releaseLiveView.findViewById(R.id.tv_location);
        tvLocation.setOnClickListener(releaseOnClickListener);

        tvAddTopic = (TextView) releaseLiveView.findViewById(R.id.tv_addtopic);
//        tvAddTopic.setOnClickListener(releaseOnClickListener);

        tvAddTopic.setText(topics);

        rlAddPic = (RelativeLayout) releaseLiveView.findViewById(R.id.rl_addpic);
        rlAddPic.setOnClickListener(releaseOnClickListener);
        mTitleTv = (TextView) releaseLiveView.findViewById(R.id.title);
        mTitleTv.setText(mTitle);
//        mTitleTv.setOnFocusChangeListener(new android.view.View.
//                OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                Log.d(TAG, "mTitleTv onFocusChange=" + hasFocus);
//                String titleStr = mTitleTv.getText().toString();
//
//                if (hasFocus) {
//                    // 此处为得到焦点时的处理内容
//                    AnalyticsReport.onEvent(mBaseActivity, LiveReport.MY_LIVE_EVENT_11202);
//                    if (TextUtils.isEmpty(titleStr)) {
//                        mTitleTv.setHint("");
//                    }
//                } else {
//                    // 此处为失去焦点时的处理内容
//                    if (TextUtils.isEmpty(titleStr)) {
//                        mTitleTv.setHint(R.string.write_topic_more_people);
//                    }
//                }
//            }
//        });
        etFocus = (EditText) releaseLiveView.findViewById(R.id.et_focus);
        ivCover = (SimpleDraweeView) releaseLiveView.findViewById(R.id.iv_cover);
        ivCover.setVisibility(View.GONE);
        releaseLiveView.findViewById(R.id.btn_close).setOnClickListener(releaseOnClickListener);
        releaseLiveView.findViewById(R.id.start_live).setOnClickListener(releaseOnClickListener);

        //后台配置不显示分享
        if (!SystemConfig.getInstance().isShowShare()) {
            llShare.setVisibility(View.GONE);
        }
        mFrontCamera = UiPreference.getBoolean(Common.KEY_IS_FRONT_CAMERA, mFrontCamera);
        if (mLivePusher != null) {
            mLivePusher.setFrontCam(mFrontCamera);
        }
        Log.d(TAG, "设置摄像头 isFrontCamera=" + mFrontCamera);
        checkShowBtnBeauty();
        if (mFrontCamera) {
        } else {
//            btnBeauty.setVisibility(View.GONE);
        }

        boolean isShowLocation = UiPreference.getBoolean(Common.IS_UPLOAD_LOCATION, true);
        btnLocate.setSelected(isShowLocation);
        String locationCity = UiPreference.getString(Common.KEY_MY_LOCATION_CITY, "");
        if (isShowLocation) {
            if (Utils.isEmpty(locationCity)) {
                tvLocation.setText(getString(R.string.ta_from_huoxing));
            } else {
                tvLocation.setText(locationCity);
            }
        } else {
            tvLocation.setText(getString(R.string.do_not_show_location));
        }

        // 我是主播
        isTestLive = getArguments().getBoolean(Common.EXTRA_IS_TEST_LIVE, false);

        if (isTestLive) {
            // 显示直播测试提示
            releaseLiveView.findViewById(R.id.testLiveTip).setVisibility(View.VISIBLE);
        }

    }

    View.OnClickListener releaseOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_addpic://添加封面
                    handleAddpic();
                    break;
//                case R.id.tv_addtopic://添加话题
//                    AnalyticsReport.onEvent(mBaseActivity, LiveReport.MY_LIVE_EVENT_11204);
//                    AddTopicActivity.startActivity(mBaseActivity);
//                    break;
                case R.id.btn_camera:
                    //切摄像头
                    EventBusManager.postEvent(0, SubcriberTag.LIVE_SWITCH_CAMERA);
                    break;
                case R.id.btn_beauty:
                    //美颜
                    toggleBeauty();
                    break;
                case R.id.btn_filter:
                    //滤镜
//                mFilterPanel.showPanel();
                    break;
                case R.id.btn_location:
                case R.id.tv_location:
                    //定位
                    btnLocate.setSelected(btnLocate.isSelected() == false);
                    boolean isSelected = btnLocate.isSelected();
                    UiPreference.putBoolean(Common.IS_UPLOAD_LOCATION, isSelected);
                    if (isSelected) {
                        requestLocation();
                    } else {
                        tvLocation.setText(R.string.do_not_show_location);
                    }
                    break;
                case R.id.btn_share_qq:
                    shareTypes.add("4");
                    showShareDialog(ShareUtil.QQ_PLATFORM);
                    break;
                case R.id.btn_share_wx:
                    shareTypes.add("1");
                    Log.log("btn_share_wx");
                    showShareDialog(ShareUtil.WEIXIN_PLATFORM);
                    break;
                case R.id.btn_share_sina:
                    shareTypes.add("3");
                    showShareDialog(ShareUtil.WEIBO_PLATFORM);
                    break;
                case R.id.btn_share_qzone:
                    shareTypes.add("5");
                    showShareDialog(ShareUtil.QZONE_PLATFORM);
                    break;
                case R.id.btn_share_timeline:
                    shareTypes.add("2");
                    showShareDialog(ShareUtil.FRIENDSHIP_PLATFORM);
                    break;
                case R.id.btn_close:
                    if (isCreater) {
                        showPauseLiveDialog();
                    } else {
                        if (isPortrait) {
                            showExitDialog();
                        } else {
                            showExitDialogLand();
                        }
                    }
                    AnalyticsReport.onEvent(mBaseActivity, LiveReport.MY_LIVE_EVENT_11209);
                    break;
                case R.id.start_live:
                    List<String> permissionsNeeds = getPermissionNeeds();
                    if (permissionsNeeds.isEmpty()) {
//                        startLive();
                    } else {
                        mBaseActivity.showToast(R.string.hasno_camera_or_record_permission);
                        return;
//                        if (ContextCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION)
//                                == PackageManager.PERMISSION_DENIED){
//                            permissionsNeeds.add(Manifest.permission.LOCATION_HARDWARE);
//                        }
//                        ActivityCompat.requestPermissions(MainActivity.this
//                                , permissionsNeeds.toArray(new String[permissionsNeeds.size()]), REQUEST_PERMISSION_CODE);
                    }


                    //判断网络
                    if (!isStartLiveCheckNet) {
                        isStartLiveCheckNet = true;
                        if (!NetStateManager.getInstance().getIsWifiNow(mBaseActivity)) {
                            NetStateManager.getInstance().showMobileNetWorkDialog(mBaseActivity, R.string.in_mobile_net_suggest_wifi_live, R.string.change_net, R.string.goon_live, new IDialogOnClickListener() {
                                @Override
                                public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                                    if (viewId == GenericDialog.ID_BUTTON_YES) {
                                        //跳去设置网络
                                        NetStateManager.getInstance().goToNetSetting(mBaseActivity);
                                    } else if (viewId == GenericDialog.ID_BUTTON_NO) {
                                        createLive();
                                    }
                                    return false;
                                }
                            });
                            return;
                        }
                    }

                    if (isStartLiveCheckNet) {
                        if (!NetStateManager.getInstance().getIsWifiNow(mBaseActivity)) {
                            mBaseActivity.showToast(R.string.current_mobile_net_be_careful);
                        }
                    }
                    createLive();
                    break;
            }
        }
    };

    @NonNull
    private List<String> getPermissionNeeds() {
        List<String> permissionsNeeds = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(mBaseActivity, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            permissionsNeeds.add(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(mBaseActivity, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_DENIED) {
            permissionsNeeds.add(Manifest.permission.RECORD_AUDIO);
        }
        return permissionsNeeds;
    }


    boolean isStartLiveCheckNet = false;

    private void checkShowBtnBeauty() {
        if (!SystemConfig.getInstance().isShowFaceBeauty()) {
            Log.d(TAG, " checkShowBtnBeauty btnBeauty GONE");
            btnBeauty.setVisibility(View.GONE);
        } else {
            Log.d(TAG, " checkShowBtnBeauty btnBeauty VISIBLE");
            btnBeauty.setVisibility(View.VISIBLE);
        }
    }

    private void toggleBeauty() {
        AnalyticsReport.onEvent(mBaseActivity, LiveReport.MY_LIVE_EVENT_11210);
//        isEnableBeauty = !isEnableBeauty;
//        UiPreference.putBoolean(Common.KEY_IS_ENABLE_BEAUTY,isEnableBeauty);
//        mQavsdkControl.setEnableBeauty(isEnableBeauty);

        EventBusManager.postEvent(0, SubcriberTag.SHOW_BEAUTY_PANEL);
    }

    private void refreshBtnBeauty() {
        if (isEnableBeauty) {
            btnBeauty.setBackgroundResource(R.drawable.live_begin_btn_beauty_selector);
        } else {
            btnBeauty.setBackgroundResource(R.drawable.live_begin_btn_no_beauty_selector);
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(mBaseActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION_CODE);
        }
    }

    private void requestLocation() {
        tvLocation.setText(R.string.request_location);
        LiveApplication.getInstance().setGetLocationListener(new LiveApplication.GetLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation curLocation) {
                boolean isShow = UiPreference.getBoolean(Common.IS_UPLOAD_LOCATION, true);
                if (isShow) {
                    if (curLocation != null) {
                        String location = LakaUtil.getFullPlace(curLocation);
                        Log.d(TAG, " location=" + location);
                        if (Utils.isEmpty(location)) {
                            UiPreference.putString(Common.KEY_MY_LOCATION_CITY, "");
                            tvLocation.setText(R.string.ta_from_huoxing);
                        } else {
                            tvLocation.setText(location);
                        }


                        LiveApplication.getInstance().stopGetLocation();
                    } else {
                        tvLocation.setText(R.string.ta_from_huoxing);
                    }
                }
            }
        });
    }


    private void handleAddpic() {
        AnalyticsReport.onEvent(mBaseActivity, LiveReport.MY_LIVE_EVENT_11203);
        ActionSheetPanel panel = new ActionSheetPanel(mBaseActivity);

        ActionSheetPanel.ActionSheetItem item = new ActionSheetPanel.ActionSheetItem();
        item.id = String.valueOf(PHOTO_REQUEST_GALLERY);
        item.title = ResourceHelper.getString(R.string.choice_from_gallery);
        panel.addSheetItem(item);

//        item = new ActionSheetPanel.ActionSheetItem();
//        item.id = String.valueOf(PHOTO_REQUEST_TAKE_PHOTO);
//        item.title = ResourceHelper.getString(R.string.take_picture);
//        panel.addSheetItem(item);

        panel.setSheetItemClickListener(new ActionSheetPanel.OnActionSheetClickListener() {
            @Override
            public void onActionSheetItemClick(String id) {
                if (String.valueOf(PHOTO_REQUEST_GALLERY).equals(id)) {
                    choiceImage();
                } else if (String.valueOf(PHOTO_REQUEST_TAKE_PHOTO).equals(id)) {
                    takePicture();
                }
            }
        });

        panel.showPanel();
    }

    private void takePicture() {
        try {
            mImageFile = Util.createImageFile(mBaseActivity, Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES));
        } catch (IOException e) {
            Log.error("test", "ex ", e);
        }
        Util.takePhoto(mBaseActivity, mImageFile, REQUEST_TAKE_PHOTO);
    }

    private void choiceImage() {
        if (Util.choicePhoto(mBaseActivity, REQUEST_CHOICE_PHOTO) == false) {
            mBaseActivity.showToast(R.string.can_not_find_gallery);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CHOICE_PHOTO:
                    Uri selectedImage = data.getData();
                    String picturePath = Util.getRealPathFromURI(mBaseActivity, selectedImage);
                    if (!Utils.isEmpty(picturePath)) {
                        mImageFile = new File(picturePath);
                        Util.cropImageUri(mBaseActivity, Uri.fromFile(mImageFile), PHOTO_ASPECT, PHOTO_ASPECT, REQUEST_CROP_SMALL_PICTURE);
//                        onImageResult(mImageFile);
                    } else {
                        mBaseActivity.showToast(R.string.get_photo_fail);
                    }
                    break;
                case REQUEST_TAKE_PHOTO:
                    Util.cropImageUri(mBaseActivity, Uri.fromFile(mImageFile), PHOTO_ASPECT, PHOTO_ASPECT, REQUEST_CROP_SMALL_PICTURE);
//                    if (isCrop) {
//                        Util.cropImageUri(this, Uri.fromFile(mImageFile), 240, 240, REQUEST_CROP_SMALL_PICTURE);
//                    } else {
//                    onImageResult(mImageFile);
//                    }
                    break;
                case REQUEST_CROP_SMALL_PICTURE:
                    onImageResult(mImageFile);
                    break;
            }
        }
    }

    private void onImageResult(File imageFile) {
        if (imageFile == null || !imageFile.isFile()) {
            mBaseActivity.showToast(R.string.upload_cover_fail);
            return;
        }
        if (TextUtils.isEmpty(imageFile.getAbsolutePath())) {
            mBaseActivity.showToast(R.string.upload_cover_fail);
        }
        Log.d(TAG, "设置封面显示 file =" + imageFile.getAbsolutePath());
        ImageUtil.loadLocalImage(imageFile.getAbsolutePath(), ivCover);
        ivCover.setVisibility(View.VISIBLE);
        getQinNiuUploadToken(imageFile.getAbsolutePath());
    }

    private void getQinNiuUploadToken(final String filePath) {
        isUploadingCover = true;
        mBaseActivity.showLoadingDialog();
        DataProvider.getQinNiuUpLoadToken(this, Common.UPLOAD_TYPE_COVER, new GsonHttpConnection.OnResultListener<GetQinNiuUpLoadTokenMsg>() {
            @Override
            public void onSuccess(GetQinNiuUpLoadTokenMsg msg) {

                File imageFile = new File(filePath);
                try {
                    DataProvider.uploadImageToQiNiu(this, msg.getToken(), msg.getKey(), msg.getUploadToken(),
                            imageFile, new GsonHttpConnection.OnResultListener<QiNiuUploadMsg>() {
                                @Override
                                public void onSuccess(QiNiuUploadMsg qiNiuUploadMsg) {
                                    mBaseActivity.dismissLoadingsDialog();
                                    mBaseActivity.showToast(R.string.upload_cover_success, com.laka.live.ui.widget.Toast.LENGTH_SHORT);
                                    coverUrl = qiNiuUploadMsg.getName();
                                    Log.d(TAG, " 上传封面成功 name=" + qiNiuUploadMsg.getName() + " command=" + qiNiuUploadMsg.getCommand());
                                    isUploadingCover = false;
                                }

                                @Override
                                public void onFail(int errorCode, String errorMsg, String command) {
                                    mBaseActivity.showToast(R.string.upload_cover_fail, com.laka.live.ui.widget.Toast.LENGTH_SHORT);
                                    mBaseActivity.dismissLoadingsDialog();
                                    isUploadingCover = false;
                                }
                            });
                } catch (FileNotFoundException e) {
                    Log.error("test", "ex ", e);

                    mBaseActivity.showToast(R.string.upload_cover_fail);
                    mBaseActivity.dismissLoadingsDialog();
                }
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                isUploadingCover = false;
                mBaseActivity.dismissLoadingsDialog();
                mBaseActivity.showToast(R.string.upload_cover_fail, com.laka.live.ui.widget.Toast.LENGTH_SHORT);
            }
        });
    }


    private void createLive() {
//        mLivePusher.startPreview();
        if (isUploadingCover) {
            mBaseActivity.showToast(R.string.upload_cover_wait);
            return;
        }

//        mTitle = mTitleTv.getText().toString();

        if (!Utils.isEmpty(mTitle)) {
            Analytics_Title = "2";
        } else {
            Analytics_Title = "1";
        }

        if (!Utils.isEmpty(coverUrl)) {
            Analytics_Cover = "2";
        } else {
            Analytics_Cover = "1";
        }

//        if (Utils.isEmpty(title) && !Utils.listIsNullOrEmpty(mList)) {
//            if (mAdapter.getSelectedPosition() >= 0) {
//                title = mList.get(mAdapter.getSelectedPosition());
//                Analytics_Lable = "2";
//            } else {
//                Analytics_Lable = "1";
//            }
//        }

        String[] items = getResources().getStringArray(R.array.share_default_title);
        if (Utils.isEmpty(mTitle)) {
//            mTitle = getResources().getString(R.string.i_in_laka_live);
            mTitle = items[(int) Math.round(Math.random() * (items.length - 1))];
        }

        StringBuilder sb = new StringBuilder();
        if (shareTypes.size() > 0) {
            for (String type :
                    shareTypes) {
                sb.append(type).append(AnalyticsReport.SEPARATOR);
            }
            if (sb.length() > 0) {
                sb.substring(0, sb.length() - 2);
            }
        }
        Map<String, String> params = new HashMap<>();
        params.put("Share_type", sb.toString());
//        params.put("Title", Analytics_Title);
//        params.put("Cover", Analytics_Cover);
        AnalyticsReport.onEvent(mBaseActivity, LiveReport.MY_LIVE_EVENT_11208, params);

        CacheUtil.addCache("liveTitle", mTitle);
        startLiveTrue();
//        LiveRoomActivity.startLive(this, LiveApplication.getInstance().getMyId(), true, title, LiveApplication.getInstance().getMyIdString(),coverUrl,LiveApplication.getInstance().getMyselfUserInfo().getCover());
//        finish();
    }

    private void startLiveTrue() {
        Log.d(TAG, " startLiveTrue");
        isReleaseReady = true;
//        vpContainer.setVisibility(View.VISIBLE);
//        roomView.setVisibility(View.VISIBLE);
//        releaseLiveView.setVisibility(View.GONE);
        mCover = coverUrl;
        //showLoading();
        //showCountDown();
        showCircleLoading(R.string.zhibo_connecting);

        createRoom();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkInitRoom();
            }
        }, 1500);

//        getPushUrl();

//        pushLiveSuccess();//因为直播成功没有回调
        //创建房间
//        createRoom();
//        checkInitRoom();
//        //开始推流
//        startPublishRtmp();
    }

    ////////准备直播view结束

    private void showCountDown() {
        new LiveCountDownTimer(8000, 1000).start();
    }

    private class LiveCountDownTimer extends CountDownTimer {

        private TextView mTextView;
        private Animation mAnimation;

        public LiveCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            mAnimation = AnimationUtils.loadAnimation(mBaseActivity, R.anim.live_count_down);
            mTextView = new TextView(mBaseActivity);
            mTextView.setTextColor(getResources().getColor(R.color.white));
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40);

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

            roomView.addView(mTextView, layoutParams);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int time = (int) millisUntilFinished / 1000;
            switch (time) {
                case 7:
                    mTextView.setText("直播开始");
                    break;
                case 1:
                    mTextView.setText("开始");
                    break;
                default:
                    mTextView.setText(String.valueOf(time - 1));
                    mTextView.startAnimation(mAnimation);
                    break;
            }
        }

        @Override
        public void onFinish() {
            roomView.removeView(mTextView);
        }
    }

//    @Override
//    public void onResponse(BaseResponse baseResp) {
//        if (baseResp != null) {
//            switch (baseResp.errCode) {
//                case WBConstants.ErrorCode.ERR_OK:
//                    Log.d(TAG, "分享成功 status=" + 1);
//                    EventBusManager.postEvent(1, SubcriberTag.ROOM_SHARE_SUCCESS);
////                    Toast.makeText(this, "ERR_OK", Toast.LENGTH_LONG).show();
//                    break;
//                case WBConstants.ErrorCode.ERR_CANCEL:
//                    Log.d(TAG, "分享取消");
////                    Toast.makeText(this, "CANCEL", Toast.LENGTH_LONG).show();
//                    break;
//                case WBConstants.ErrorCode.ERR_FAIL:
//                    Log.d(TAG, "分享失败");
////                    Toast.makeText(this, "FAIL" + "Error Message: " + baseResp.errMsg, Toast.LENGTH_LONG).show();
//                    break;
//            }
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    requestLocation();
                } else {
                    Toast.makeText(mBaseActivity, "请打开定位权限", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onEmojiSelected(String key) {
        Editable mEditable = etComment.getText();
        if (key.equals("/DEL")) {
            etComment.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
        } else {
            int start = etComment.getSelectionStart();
            int end = etComment.getSelectionEnd();
            start = (start < 0 ? 0 : start);
            end = (start < 0 ? 0 : end);
            mEditable.replace(start, end, key);
        }
    }

    @Override
    public void onStickerSelected(String categoryName, String stickerName) {
    }

    private void handleConnectMicClick() {

        if (isCreater) {
            AnalyticsReport.onEvent(getContext(), LiveReport.MY_LIVE_EVENT_11260);
            handleAnchorConnectMicClick();
        } else {
            AnalyticsReport.onEvent(getContext(), LiveReport.MY_LIVE_EVENT_11269);
            handleAudienceConnectMicClick();
        }
    }

    private void handleAnchorConnectMicClick() {
//        ConnectMicActivity.startActivity(getActivity());
        ConnectMicPanel panel = new ConnectMicPanel(getContext(), connectMicManager, zhuboUserInfo);
        panel.showPanel();
    }

    private void handleAudienceConnectMicClick() {

    }


    View dialogView;
    ShareUtil share;

    /**
     * @param shareType -1代表需要弹出分享框来选择，其它的值，分别代表不同的分享类型
     */
    private void showShareDialog(final int shareType) {
        if (!AccountInfoManager.getInstance().isLogin()) {
            LoginActivity.startActivity(mContext);
            return;
        }

        if (!AccountInfoManager.getInstance().isLogin()) {
            LoginActivity.startActivity(mContext);
            return;
        }

        if (mCourse == null) {

            DataProvider.getCourseDetail(this, mCourseId, new GsonHttpConnection.OnResultListener<CourseDetailMsg>() {

                @Override
                public void onSuccess(CourseDetailMsg msg) {

                    if (msg != null && msg.data != null && msg.data.course != null) {

                        mCourse = msg.data.course;

                        if (mCourse.isTestLive()) {
                            ToastHelper.showToast(ResourceHelper.getString(R.string.test_live_share_tip));
                            return;
                        }

                        if (shareType != -1) {
                            mBaseActivity.share.shareUrl(mBaseActivity, mCourse.getTitle(), mCourse.getSummary(),
                                    Common.SHARE_COURSE_URL + mCourseId, mCourse.getCover_url(), shareType);
                        } else {
                            showShareDialog(Common.SHARE_COURSE_URL + mCourseId,
                                    mCourse.getTitle(),
                                    mCourse.getSummary(),
                                    mCourse.getCover_url(), true);
                        }

                    } else {
                        ToastHelper.showToast("获取不到课程的分享信息");
                    }

                }

                @Override
                public void onFail(int errorCode, String errorMsg, String command) {
                    ToastHelper.showToast("获取不到课程的分享信息");
                }

            });
        } else {
            if (mCourse.isTestLive()) {
                ToastHelper.showToast(ResourceHelper.getString(R.string.test_live_share_tip));
                return;
            }

            if (shareType != -1) {
                mBaseActivity.share.shareUrl(mBaseActivity, mCourse.getTitle(), mCourse.getSummary(),
                        Common.SHARE_COURSE_URL + mCourseId, mCourse.getCover_url(), shareType);
            } else {
                showShareDialog(Common.SHARE_COURSE_URL + mCourseId,
                        mCourse.getTitle(),
                        mCourse.getSummary(),
                        mCourse.getCover_url(), true);

            }
        }
    }

    // 确保有课程信息
    private void showShareDialog() {
        showShareDialog(-1);
    }

    /**
     * 分享左侧弹出框
     */
    public void showShareDialog(final String shareUrl, final String title,
                                final String content, final String imageUrl, final boolean isLive) {

        Log.d(TAG, " showShareDialogLeft shareUrl=" + shareUrl);

        if (dialogView == null) {
            dialogView = findViewById(R.id.share_dialog);
            dialogView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogView.setVisibility(View.VISIBLE);
                }
            });
        }

        Button tvCancle = (Button) dialogView.findViewById(R.id.tv_cancle);
        LinearLayout llWechat = (LinearLayout) dialogView
                .findViewById(R.id.ll_wechat);
        LinearLayout llMoment = (LinearLayout) dialogView
                .findViewById(R.id.ll_monent);
        LinearLayout llQQ = (LinearLayout) dialogView.findViewById(R.id.ll_qq);
        LinearLayout llWeibo = (LinearLayout) dialogView
                .findViewById(R.id.ll_weibo);
        LinearLayout llQzone = (LinearLayout) dialogView
                .findViewById(R.id.ll_qzone);
        if (share == null) {
            share = ShareUtil.getInstance();
        }

        llWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialogView.setVisibility(View.GONE);
                share.shareUrl(mBaseActivity,
                        title, content, shareUrl, imageUrl, ShareUtil.WEIXIN_PLATFORM);
                if (isLive) {
//                    AnalyticsReport.onEvent(BaseActivity.this, LiveReport.MY_LIVE_EVENT_11232);
                }
            }
        });
        llMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialogView.setVisibility(View.GONE);
                share.shareUrl(mBaseActivity,
                        title, content, shareUrl, imageUrl, ShareUtil.FRIENDSHIP_PLATFORM);
                if (isLive) {
//                    AnalyticsReport.onEvent(BaseActivity.this, LiveReport.MY_LIVE_EVENT_11236);
                }
            }
        });
        llQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialogView.setVisibility(View.GONE);
                share.shareUrl(mBaseActivity,
                        title, content, shareUrl, imageUrl, ShareUtil.QQ_PLATFORM);
                if (isLive) {
//                    AnalyticsReport.onEvent(BaseActivity.this, LiveReport.MY_LIVE_EVENT_11234);
                }
            }
        });
        llQzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialogView.setVisibility(View.GONE);
                share.shareUrl(mBaseActivity,
                        title, content, shareUrl, imageUrl, ShareUtil.QZONE_PLATFORM);
                if (isLive) {
//                    AnalyticsReport.onEvent(BaseActivity.this, LiveReport.MY_LIVE_EVENT_11235);
                }
            }
        });
        llWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialogView.setVisibility(View.GONE);
                share.shareUrl(mBaseActivity,
                        title, content, shareUrl, imageUrl, ShareUtil.WEIBO_PLATFORM);
                if (isLive) {
//                    AnalyticsReport.onEvent(BaseActivity.this, LiveReport.MY_LIVE_EVENT_11233);
                }
            }
        });
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialogView.setVisibility(View.GONE);
            }
        });
//        shareDialog = BottomMenuDialog.createNewDialog(this, dialogView);
//        shareDialog.show();
        dialogView.setVisibility(View.VISIBLE);
    }


    private void showGoodsPanel() {
        if (!isPortrait) {
            isPortrait = !isPortrait;
            changeViewOrientation(isPortrait);
        }
        Log.d(TAG, " showGoodsPanel");
        if (goodsPanel.getVisibility() == View.GONE) {
            goodsPanel.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) vlBigView.getLayoutParams();
            lp.height = Utils.getScreenHeight(mContext) * 1 / 3;
            lp.width = Utils.getScreenWidth(mContext);
            vlBigView.setLayoutParams(lp);
            if (!isCreater) {
                mLivePlayer.setViewMode(false);
            } else {
                mLivePusher.setViewMode(false);
            }
        }
    }

    private void hideGoodsPanel() {
        Log.d(TAG, " hideGoodsPanel");
        if (goodsPanel.getVisibility() == View.VISIBLE) {
            goodsPanel.setVisibility(View.GONE);
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) vlBigView.getLayoutParams();
            lp.height = Utils.getScreenHeight(mContext);
            lp.width = Utils.getScreenWidth(mContext);
            vlBigView.setLayoutParams(lp);
            if (!isCreater) {
                mLivePlayer.setViewMode(true);
            } else {
                mLivePusher.setViewMode(true);
            }
        }
    }
}
