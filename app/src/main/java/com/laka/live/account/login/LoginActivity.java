package com.laka.live.account.login;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.application.LiveApplication;
import com.laka.live.bean.Splash;
import com.laka.live.bean.UserInfo;
import com.laka.live.bean.VersionInfo;
import com.laka.live.config.SystemConfig;
import com.laka.live.gift.GiftResManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.listener.IControllerListener;
import com.laka.live.msg.SplashMsg;
import com.laka.live.msg.UserMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.observer.NetworkChangeObserver;
import com.laka.live.shopping.activity.ShoppingGoodsDetailActivity;
import com.laka.live.shopping.activity.ShoppingTopicActivity;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.activity.MainActivity;
import com.laka.live.ui.activity.UserInfoActivity;
import com.laka.live.ui.activity.WebActivity;
import com.laka.live.ui.course.BestTopicsActivity;
import com.laka.live.ui.course.detail.CourseDetailActivity;
import com.laka.live.ui.systembartint.SystemBarTintManager;
import com.laka.live.ui.widget.CountDownProgressBar;
import com.laka.live.ui.widget.SelectorImage;
import com.laka.live.ui.widget.dialog.GenericDialog;
import com.laka.live.ui.widget.dialog.IDialogOnClickListener;
import com.laka.live.ui.widget.dialog.SimpleTextDialog;
import com.laka.live.update.UpdateCheck;
import com.laka.live.update.UpdateCheckManager;
import com.laka.live.update.UpdateHelper;
import com.laka.live.util.AnimationHelper;
import com.laka.live.util.AppExitHelper;
import com.laka.live.util.Common;
import com.laka.live.util.FastClickUtil;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.NetworkUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ThreadManager;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.UiPreference;
import com.laka.live.util.Utils;
import com.laka.live.util.ViewUtils;
import com.laka.live.video.ui.activity.MiniVideoContainerActivity;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.modelmsg.SendAuth;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by luwies on 16/3/8.
 * zwl update
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener,
        NetworkChangeObserver.NetworkChangeListener, SurfaceHolder.Callback {
    private static final String TAG = "LoginActivity";
    private static final String EXTRA_START_METHOD = "EXTRA_START_METHOD";
    private static final String WITHIN_THE_APPLICATION = "WITHIN_THE_APPLICATION";

    private final static int REQUEST_EXTERNAL_STORAGE = 200;

    private static final String FROM_KEY = "start_from";
    private static final int TIME_DELAY = 500;//改为0.5秒了
    public static final int TYPE_FROM_NEED_LOGIN = 2;
    public static final int TYPE_FROM_LOGIN_OUT = 1;
    private static final int TYPE_FROM_DEFAULT = 0;
    private static final int ANIMATION_DURATION = 1000 * 10;

    private boolean canRegJPushId;
    private AppExitHelper mExitHelper;
    private int mFromWhere;
    private boolean mIsLimitCheckUpdate = false;
    private int mLastNetWorkState;
    private boolean mIsUpdateCheckWhenNetChange = false;
    private SimpleTextDialog mUpdateInfoDialog;
    private boolean mHasWeChatCallback = false;

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    //private MediaPlayer mMediaPlayer;

    private View mBottomLayout;

    private boolean isVideoCompleted = false;

    //private TextView mSkip;

    private ImageView mBgSimpleDraweeView;

    private VersionInfo mVersionInfo;
    private SelectorImage ivClose;

    private Splash mSplash;
    private final static long SPLASH_TIME = 3000;
    private long mStartTime;

    private SimpleDraweeView mSplashSdv;
    private CountDownProgressBar mCountDownProgressBar;

    public static void startActivityForResult(Activity activity, int requestCode) {
        if (activity != null) {
            LiveApplication.getInstance().exitApp(activity);
            Intent intent = new Intent(activity, LoginActivity.class);
            intent.putExtra(EXTRA_START_METHOD, WITHIN_THE_APPLICATION);
            activity.startActivityForResult(intent, requestCode);
        }
    }

    public static void startActivity(Activity activity) {
        startActivity(activity, TYPE_FROM_LOGIN_OUT);
    }

    public static void startActivity(Activity activity, int from) {
        if (activity != null) {
            Intent intent = new Intent(activity, LoginActivity.class);
            intent.putExtra(EXTRA_START_METHOD, WITHIN_THE_APPLICATION);
            intent.putExtra(FROM_KEY, from);
            ActivityCompat.startActivity(activity, intent, null);
        }
    }

    public static void startActivity(Context context, int from) {
        if (context != null) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtra(EXTRA_START_METHOD, WITHIN_THE_APPLICATION);
            intent.putExtra(FROM_KEY, from);
            ActivityCompat.startActivity(context, intent, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //更新配置信息
        SystemConfig.getInstance().update();
        LiveApplication.getInstance().initHardwareUtils(this);
        addAnalytics();
        initWindow();
        initIntent();
        registerNetWorkBroadcast();
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false);
        startStep();

        //// TODO 检查礼物版本
        if (Common.IS_DOWNLOAD_GIFT_RES) {
            GiftResManager.getInstance().checkGiftUpdate();
        }

    }


    @Override
    public void onNetworkChange(int state) {
        if ((mLastNetWorkState == NetworkUtil.NETWORK_STATE_NONE ||
                mLastNetWorkState == NetworkUtil.NETWORK_STATE_UNKNOW)
                && NetworkUtil.isNetworkOk(LoginActivity.this)) {
            if (!mIsUpdateCheckWhenNetChange) {
                checkUpdate();
                mIsUpdateCheckWhenNetChange = true;
            }
        }
        mLastNetWorkState = state;
    }

    private void registerNetWorkBroadcast() {
        NetworkChangeObserver.getInstance().registListener(this);
    }

    private void unRegisterNetWorkBroadcast() {
        NetworkChangeObserver.getInstance().unRegistListener(this);
    }

    private void initIntent() {
        mFromWhere = getIntent().getIntExtra(FROM_KEY, TYPE_FROM_DEFAULT);
    }

    private void initSplash() {
        mStartTime = System.currentTimeMillis();
        // 下载启动页图片，如果下载失败，则直接进入主页，如成功，则延时3000ms进入主页
        // 这样做是存在缺点的，如果网络很差的情况下，就会卡在启动页停留很久。
        DataProvider.querySplash(this, new GsonHttpConnection.OnResultListener<SplashMsg>() {
            @Override
            public void onSuccess(SplashMsg splashMsg) {
                Log.d(TAG, "querySplash success . SplashMsg : " + splashMsg.toString());

                mSplash = splashMsg.getData();

                ImageUtil.loadImage(mSplashSdv, mSplash.getImg(), new IControllerListener() {

                    @Override
                    public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {
//                        long delayedTime = SPLASH_TIME - (System.currentTimeMillis() - mStartTime);
//                        Log.d(TAG, "onFinalImageSet . delayedTime : " + delayedTime);
                        ThreadManager.postDelayed(ThreadManager.THREAD_UI, new Runnable() {
                            @Override
                            public void run() {
                                mCountDownProgressBar.startCountDownTime(SPLASH_TIME, new CountDownProgressBar.OnCountdownFinishListener() {
                                    @Override
                                    public void countdownFinished() {
                                        goToMainActivity(false, false);
                                    }
                                });
                            }
                        }, 0);
                    }

                    @Override
                    public void onFailure(String id, Throwable throwable) {
                        goToMainActivity(true, false);
                    }
                });
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                Log.d(TAG, "querySplash failed . errorMsg : " + errorMsg);
                goToMainActivity(true, false);
            }
        });
    }

    private void goToMainActivity(boolean countTime, final boolean isClickSplash) {
        long delayedTime = 0;
        if (countTime) {
            delayedTime = SPLASH_TIME - (System.currentTimeMillis() - mStartTime);
        }

        ThreadManager.postDelayed(ThreadManager.THREAD_UI, new Runnable() {
            @Override
            public void run() {
                tryOpenMainActivity();

                if (isClickSplash && mSplash != null && mSplash.getValue() != null) {
                    switch (mSplash.getType()) {
                        case Splash.TYPE_COURSE:
                            CourseDetailActivity.startActivity(mContext, mSplash.getValue());
                            break;
                        case Splash.TYPE_URL:
                            WebActivity.startActivity(mContext, mSplash.getValue(), mSplash.getTitle());
                            break;
                        default:
                            break;
                    }

                } else {
                    Uri uri = getIntent().getData();
                    if (uri != null) {
                        String url = uri.toString();
                        if (FastClickUtil.getInstance().isFastClick()) {
                            return;
                        }
                        if (url.contains("/course_detail")) {
                            String course_id = uri.getQueryParameter("course_id");
                            String user_id = uri.getQueryParameter("user_id");
                            if (!TextUtils.isEmpty(course_id) && !TextUtils.isEmpty(user_id)) {
                                CourseDetailActivity.startActivity(mContext, course_id);
                            }
                            Log.d(TAG, " 跳转/course_detail course_id=" + course_id + " user_id=" + user_id);
                        } else if (url.contains("/user_detail")) {
                            String userId = uri.getQueryParameter("uid");
                            Log.d(TAG, " 跳转/user_detail uid=" + userId);
                            if (!Utils.isEmpty(userId)) {
                                UserInfoActivity.startActivity(mContext, userId);
                            }
                        } else if (url.contains("/good_detail")) {
                            String goodsId = uri.getQueryParameter("goodsId");
                            Log.d(TAG, " 跳转/good_detail goodsId=" + goodsId);
                            if (!Utils.isEmpty(goodsId)) {
                                ShoppingGoodsDetailActivity.startActivity(mContext, Integer.parseInt(goodsId));
                            }
                        } else if (url.contains("/mini_video")) {
                            String videoId = uri.getQueryParameter("video_id");
                            MiniVideoContainerActivity.startActivity(mContext, Integer.valueOf(videoId));
                        } else if (url.contains("/course_topic")) {
                            String topicId = uri.getQueryParameter("topic_id");
                            BestTopicsActivity.startActivity(mContext, Integer.valueOf(topicId));
                        } else if (url.contains("/shopping_topic")) {
                            String topicId = uri.getQueryParameter("topic_id");
                            ShoppingTopicActivity.startActivity(mContext, Integer.valueOf(topicId));
                        }
                    }
                }
                finish();
            }
        }, delayedTime);
    }

    private void startStep() {
        if (mFromWhere == TYPE_FROM_DEFAULT) {//无需登录用这个（应用启动）
            mIsLimitCheckUpdate = true;
            startSplashActivity();
            initSplash();
            return;
        }
        mIsLimitCheckUpdate = false;
        if (mFromWhere == TYPE_FROM_LOGIN_OUT || mFromWhere == TYPE_FROM_NEED_LOGIN) {
            startLoginActivity();
            return;
        }
        ThreadManager.postDelayed(ThreadManager.THREAD_UI, new Runnable() {
            @Override
            public void run() {
                startLoginActivity();
            }
        }, TIME_DELAY);
    }

    /**
     * 展示启动页面
     */
    private void startSplashActivity() {
        setContentView(R.layout.activity_splash);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mSplashSdv = (SimpleDraweeView) findViewById(R.id.splash_sdv);
        mSplashSdv.setOnClickListener(this);
        mCountDownProgressBar = (CountDownProgressBar) findViewById(R.id.count_down_progress_bar);
        mCountDownProgressBar.setOnClickListener(this);
    }

    /**
     * 展示登录页面
     */
    private void startLoginActivity() {
        setContentView(R.layout.activity_login);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();

    }

    /*设置窗口无title*/
    private void initWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    private void addAnalytics() {
        Intent intent = getIntent();
        boolean isReportLive = false;
        if (intent != null) {
            String startMethod = intent.getStringExtra(EXTRA_START_METHOD);
            if (TextUtils.isEmpty(startMethod)) {
                isReportLive = true;
            }
        } else {
            isReportLive = true;
        }
        if (isReportLive) {
            AnalyticsReport.reportOnLive();
        }
    }

    private void initView() {
        ivClose = (SelectorImage) findViewById(R.id.iv_close);
        ivClose.setOnClickListener(this);
        LinearLayout mWeiBoButton = (LinearLayout) findViewById(R.id.ll_login_weibo);
        LinearLayout mWeChatButton = (LinearLayout) findViewById(R.id.ll_login_wechat);
        LinearLayout mQQButton = (LinearLayout) findViewById(R.id.ll_login_qq);
        LinearLayout mPhoneButton = (LinearLayout) findViewById(R.id.ll_login_phone);
        mWeiBoButton.setOnClickListener(this);
        mWeChatButton.setOnClickListener(this);
        mQQButton.setOnClickListener(this);
        mPhoneButton.setOnClickListener(this);
        initAgreementTextView();
//        startImageAnimation();
        if (SystemConfig.getInstance().isShowLoginQQ()) {
            mQQButton.setVisibility(View.VISIBLE);
        } else {
            mQQButton.setVisibility(View.GONE);
        }
        if (SystemConfig.getInstance().isShowLoginMobile()) {
            mPhoneButton.setVisibility(View.VISIBLE);
        } else {
            mPhoneButton.setVisibility(View.GONE);
        }
        if (SystemConfig.getInstance().isShowLoginWeibo()) {
            mWeiBoButton.setVisibility(View.VISIBLE);
        } else {
            mWeiBoButton.setVisibility(View.GONE);
        }
        if (SystemConfig.getInstance().isShowLoginWeixin()) {
            mWeChatButton.setVisibility(View.VISIBLE);
        } else {
            mWeChatButton.setVisibility(View.GONE);
        }

        SystemBarTintManager.SystemBarConfig config = new SystemBarTintManager.SystemBarConfig(this,
                false, false);

        isVideoCompleted = UiPreference.getBoolean(Common.KEY_IS_PLAY_LOGIN_VIDEO, false);
        mBottomLayout = findViewById(R.id.ll_login_bottom);
        mBottomLayout.setVisibility(isVideoCompleted ? View.VISIBLE : View.GONE);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mBottomLayout.getLayoutParams();
        params.bottomMargin = config.getNavigationBarHeight();

        initSurfaceView();

        mBgSimpleDraweeView = (ImageView) findViewById(R.id.login_bg);

        if (isVideoCompleted) {
            mSurfaceView.setVisibility(View.GONE);
            mBgSimpleDraweeView.setVisibility(View.VISIBLE);
        } else {
            mBgSimpleDraweeView.setVisibility(View.GONE);
        }

        // 其它登录方式的布局
        final LinearLayout llOtherLoginWays = (LinearLayout) findViewById(R.id.ll_other_login_ways);

        final int viewHeight = ViewUtils.getGoneViewHeight(llOtherLoginWays);

        ViewUtils.setLayoutParams(llOtherLoginWays, 0);

        llOtherLoginWays.setVisibility(View.VISIBLE);

        // 收缩控件
        CheckBox cbExpandLoginWaysLayout = (CheckBox) findViewById(R.id.cb_expand_other_login_ways);

        // 展开或收缩其它登录方式
        cbExpandLoginWaysLayout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AnimationHelper.animateToggle(isChecked, viewHeight, llOtherLoginWays, 300);
            }
        });

    }


    private void initSurfaceView() {
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceVIew);

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
//        mSurfaceHolder.setFixedSize();
//        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        showLoginLayoutNow();
//        mMediaPlayer = new MediaPlayer();
//        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_SYSTEM);
//        mMediaPlayer.setDisplay(mSurfaceHolder);
//
//        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                showLoginLayoutNow();
//            }
//        });
////        mMediaPlayer.setLooping(true);
////        mMediaPlayer.setVolume(50, 50);
//        AssetFileDescriptor fd = null;
//        try {
//            fd = getResources()
//                    .openRawResourceFd(R.raw.login_preview_mv);
//            mMediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
//        } catch (IOException e) {
//            Log.error(TAG, "IOException ", e);
//        } finally {
//            if (fd != null) {
//                try {
//                    fd.close();
//                } catch (IOException e) {
//                }
//            }
//        }
//
//        try {
//            mMediaPlayer.prepare();
//            mMediaPlayer.setVolume(0.1f, 0.1f);
//            if (isVideoCompleted) {
//                Log.error(TAG, "mMediaPlayer.getDuration() : " + mMediaPlayer.getDuration());
//                mMediaPlayer.seekTo(mMediaPlayer.getDuration() - getVideoOffset());
//            }
//            mMediaPlayer.start();
//        } catch (Exception e) {
//            Log.error(TAG, "IOException ", e);
//        }
//        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//            @Override
//            public boolean onError(MediaPlayer mp, int what, int extra) {
//                Log.d(TAG,"onError what="+what+" extra="+extra );
//                return false;
//            }
//        });
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
//        if (mMediaPlayer.isPlaying()) {
//            mMediaPlayer.stop();
//        }
//        mMediaPlayer.release();
    }

    /**
     * 富文本
     */
    private void initAgreementTextView() {
        TextView mAgreementTextView = (TextView) findViewById(R.id.tv_agreement);
        mAgreementTextView.setOnClickListener(this);
        SpannableStringBuilder builder = new SpannableStringBuilder(mAgreementTextView.getText()
                .toString());
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorFF950B));
        builder.setSpan(colorSpan, 8, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        UnderlineSpan span = new UnderlineSpan();
        builder.setSpan(span, 8, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mAgreementTextView.setText(builder);
    }


    int curLoginType;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.count_down_progress_bar:
                if (mCountDownProgressBar != null) {
                    mCountDownProgressBar.stopCountDownTime();
                }
                goToMainActivity(false, false);
                break;
            case R.id.splash_sdv:
                if (mCountDownProgressBar != null) {
                    mCountDownProgressBar.stopCountDownTime();
                }
                goToMainActivity(false, true);
                break;
            case R.id.iv_close:
                finish();
                break;
            case R.id.tv_agreement:
                WebActivity.startActivity(LoginActivity.this, Common.USER_PROTOCOL_URL, getString(R.string.login_agreement_short));
                break;
            case R.id.ll_login_weibo:
                curLoginType = id;
//                AnalyticsReport.onEvent(this, AnalyticsReport.WEIBO_LOGIN_BUTTON_CLICK_EVENT_ID);
                handleOnWeiBoButtonClick();
                break;
            case R.id.ll_login_wechat:
                curLoginType = id;
//                AnalyticsReport.onEvent(this, AnalyticsReport.LOGIN_WECHAT_BUTTON_CLICK_EVENT_ID);
                handleOnWeChatButtonClick();
                break;
            case R.id.ll_login_qq:
                curLoginType = id;
//                AnalyticsReport.onEvent(this, AnalyticsReport.QQ_LOGIN_BUTTON_CLICK_EVENT_ID);
                handleOnQQLoginButtonClick();
                break;
            case R.id.ll_login_phone:
                curLoginType = id;
                LoginWithPhoneActivity.startLoginWithPhoneActivity(this);
//                AnalyticsReport.onEvent(this, AnalyticsReport.PHONE_LOGIN_BUTTON_CLICK_EVENT_ID);
                break;
            default:
                break;
        }
    }

    public int getVideoOffset() {
        Log.error(TAG, "Build.MODEL : " + Build.MODEL);
        if (TextUtils.equals(Build.MODEL, "MX5")) {
            return 1000;
        }
        return 0;
    }

    private void handleOnWeChatButtonClick() {
        if (checkIsNetworkOk() == false) {
            return;
        }
        showLoadingDialog();
        setCanRegJPushId();
        ThirdPlatformLoginHelper.getInstance().loginWithWeChatAuthorize(this,
                new ThirdPlatformLoginHelper.AuthorizeCallback() {
                    @Override
                    public void handleOnCallbackIsNull() {
                        dismissLoadingsDialog();
                    }
                });
    }

    /**
     * 授权登录成功（QQ、微博）
     * @param userMsg
     */
    private void handleOnLoginSuccessResult(UserMsg userMsg) {
        dismissLoadingsDialog();
        if (userMsg == null) {
            ToastHelper.showToast(R.string.login_fail);
            return;
        }
        final UserInfo userInfo = userMsg.getUserInfo();
        if (userInfo == null) {
            ToastHelper.showToast(R.string.login_fail);
            return;
        }
        if (canRegJPushId) {
            UiPreference.putBoolean(Common.HAS_SET_JPUSH_ID, true);
        }
        ToastHelper.showToast(R.string.login_success);


        AccountInfoManager.getInstance().saveAccountInfo(userInfo);
        tryOpenMainActivity();

        showRecommendIfNewUser();

        String command = userMsg.getCommand();
        if (StringUtils.isNotEmpty(command)) {
            if (Common.LOGIN_WITH_WECHAT_URL.contains(command)) {
                onLoginEvent(R.id.ll_login_wechat, "19");
            } else if (Common.LOGIN_WITH_SINA_WEIBO_URL.contains(command)) {
                onLoginEvent(R.id.ll_login_weibo, "19");
            } else if (Common.LOGIN_WITH_QQ_URL.contains(command)) {
                onLoginEvent(R.id.ll_login_qq, "19");
            } else if (Common.LOGIN_WITH_PHONE_URL.contains(command)) {
                onLoginEvent(R.id.ll_login_phone, "19");
            }
        }
    }

    /**
     * 友盟统计
     * */
    private void onLoginEvent(int curLoginType, String request) {
        HashMap<String, String> params = new HashMap<>();
        params.put("Request", request);
        if (curLoginType == R.id.ll_login_weibo) {
            AnalyticsReport.onEvent(this, AnalyticsReport.WEIBO_LOGIN_BUTTON_CLICK_EVENT_ID, params);
        } else if (curLoginType == R.id.ll_login_wechat) {
            AnalyticsReport.onEvent(this, AnalyticsReport.LOGIN_WECHAT_BUTTON_CLICK_EVENT_ID, params);
        } else if (curLoginType == R.id.ll_login_qq) {
            AnalyticsReport.onEvent(this, AnalyticsReport.QQ_LOGIN_BUTTON_CLICK_EVENT_ID, params);
        } else if (curLoginType == R.id.ll_login_phone) {
            AnalyticsReport.onEvent(this, AnalyticsReport.PHONE_LOGIN_BUTTON_CLICK_EVENT_ID, params);
        }
    }

    /**
     * 新用户则显示推荐页面
     */
    private void showRecommendIfNewUser() {
        UserInfo userInfo = AccountInfoManager.getInstance().getAccountInfo();
        if (userInfo != null && userInfo.getIsFirst() == 1) {
            RecommendActivity.startActivity(this);
            userInfo.setIsFirst(0);
            AccountInfoManager.getInstance().saveAccountInfo(userInfo);
        }
    }

    /**
     * 授权登录失败（QQ、微博）
     * */
    private void handleOnLoginFailedResult(int errorCode, int platform) {
        dismissLoadingsDialog();
        if (errorCode == ErrorCode.ERROR_CODE_USER_DENY_LOGIN) {
            ToastHelper.showToast(R.string.login_error_limit_login);
        } else if (errorCode == ThirdPlatformLoginHelper.AUTHORIZE_CODE_FAIL) {
            if (platform == ThirdPlatformLoginHelper.PLATFORM_QQ) {
                ToastHelper.showToast(ResourceHelper.getString(R.string.login_qq_fail));
            } else if (platform == ThirdPlatformLoginHelper.PLATFORM_WE_CHAT) {
                ToastHelper.showToast(ResourceHelper.getString(R.string.login_we_chat_fail));
            } else if (platform == ThirdPlatformLoginHelper.PLATFORM_WEI_BO) {
                ToastHelper.showToast(ResourceHelper.getString(R.string.login_wei_bo_fail));
            }
        } else if (errorCode == ThirdPlatformLoginHelper.AUTHORIZE_CODE_CANCEL) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.login_cancel));
        } else {
            ToastHelper.showToast(R.string.login_fail);
        }
        if (platform == ThirdPlatformLoginHelper.PLATFORM_QQ) {
            onLoginEvent(R.id.ll_login_qq, "20");
        } else if (platform == ThirdPlatformLoginHelper.PLATFORM_WEI_BO) {
            onLoginEvent(R.id.ll_login_weibo, "20");
        } else if (platform == ThirdPlatformLoginHelper.PLATFORM_WE_CHAT) {
            onLoginEvent(R.id.ll_login_wechat, "20");
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Subscribe
    public void onEvent(PostEvent event) {
        super.onEvent(event);
        if (SubcriberTag.MSG_AUTH_RESULT.equals(event.tag)) {
            mHasWeChatCallback = true;
            SendAuth.Resp resp = (SendAuth.Resp) event.event;
            handleOnWeChatAuthorizeCallback(resp);
        }
    }

    private void handleOnWeChatAuthorizeCallback(SendAuth.Resp resp) {
        ThirdPlatformLoginHelper.getInstance().handleOnWeChatAuthorize(this, resp,
                new ThirdPlatformLoginHelper.LoginResultCallback() {
                    @Override
                    public void loginSuccess(int platform, UserMsg userMsg) {
                        handleOnLoginSuccessResult(userMsg);
                    }

                    @Override
                    public void loginFailed(int platform, int errorCode) {
                        handleOnLoginFailedResult(errorCode, platform);
                    }
                });
    }

    private void handleOnWeiBoButtonClick() {
        if (checkIsNetworkOk() == false) {
            return;
        }
        showLoadingDialog();
        setCanRegJPushId();
        ThirdPlatformLoginHelper.getInstance().loginWithWeiBoAuthorize(this,
                new ThirdPlatformLoginHelper.LoginResultCallback() {
                    @Override
                    public void loginSuccess(int platform, UserMsg userMsg) {
                        handleOnLoginSuccessResult(userMsg);
                    }

                    @Override
                    public void loginFailed(int platform, int errorCode) {
                        handleOnLoginFailedResult(errorCode, platform);
                    }
                });
    }

    private void handleOnQQLoginButtonClick() {
        if (!checkIsNetworkOk()) {
            return;
        }
        showLoadingDialog();
        setCanRegJPushId();
        ThirdPlatformLoginHelper.getInstance().loginWithQQAuthorize(this,
                new ThirdPlatformLoginHelper.LoginResultCallback() {
                    @Override
                    public void loginSuccess(int platform, UserMsg userMsg) {
                        handleOnLoginSuccessResult(userMsg);
                    }

                    @Override
                    public void loginFailed(int platform, int errorCode) {
                        handleOnLoginFailedResult(errorCode, platform);
                    }
                });
    }

    private boolean checkIsNetworkOk() {
        if (NetworkUtil.isNetworkOk(this)) {
            return true;
        }
        showToast(R.string.network_error_tips);
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsReport.onEvent(this, AnalyticsReport.LOGIN_SHOW_EVENT_ID);
        checkUpdate();
        if (!mHasWeChatCallback) {
            dismissLoadingsDialog();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UpdateCheck.getInstance().clearUpdateCheck();
        unRegisterNetWorkBroadcast();
        mIsLimitCheckUpdate = false;
        ThirdPlatformLoginHelper.getInstance().clearHelper();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            //监控/拦截/屏蔽返回键 processExit();
//            if (mExitHelper == null) {
//                mExitHelper = new AppExitHelper(this);
//            }
//            mExitHelper.exitApplication(this);
//        }
//        return true;
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        handleOnActivityResult(requestCode, resultCode, data);
    }

    private void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        ThirdPlatformLoginHelper.getInstance()
                .handleOnWeiBoResultCallbackData(requestCode, resultCode, data,
                        new ThirdPlatformLoginHelper.AuthorizeCallback() {
                            @Override
                            public void handleOnCallbackIsNull() {
                                dismissLoadingsDialog();
                                ToastHelper.showToast(ResourceHelper.getString(R.string.login_wei_bo_fail));
                            }
                        });
        if ((requestCode == Constants.REQUEST_API || requestCode == Constants.REQUEST_LOGIN)) {
            ThirdPlatformLoginHelper.getInstance().handleOnTenCentResultData(data,
                    new ThirdPlatformLoginHelper.AuthorizeCallback() {
                        @Override
                        public void handleOnCallbackIsNull() {
                            dismissLoadingsDialog();
                            ToastHelper.showToast(ResourceHelper.getString(R.string.login_qq_fail));
                        }
                    });
        } else if (requestCode == LoginWithPhoneActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            //手机登录成功的回调
            tryOpenMainActivity();
            showRecommendIfNewUser();
        }
    }

    private void tryOpenMainActivity() {
        if (MainActivity.getInstance() == null) {
            Utils.startActivity(this, MainActivity.class);
        }
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void setCanRegJPushId() {
        String regId = JPushInterface.getRegistrationID(this);
        canRegJPushId = !TextUtils.isEmpty(regId);
    }

    private void checkUpdate() {
        if (mIsLimitCheckUpdate) {
            return;
        }
        if (UpdateCheckManager.getInstance().isIgnoreUpdateNow()) {
            return;
        }
        UpdateCheck.getInstance().setIsShowFailedToast(false);
        UpdateCheck.getInstance().checkEnableUpdate(new UpdateCheck.UpdateCallback() {
            @Override
            public void hasUpdate(UpdateCheck.UpdateResult updateResult, VersionInfo versionInfo) {
                handleOnCheckUpdateSuccess(updateResult, versionInfo);
            }

            @Override
            public void checkFailed() {

            }
        }, UpdateCheck.UPDATE_FLAG_AUTO);
    }

    private void handleOnCheckUpdateSuccess(UpdateCheck.UpdateResult result, final VersionInfo versionInfo) {
        mIsLimitCheckUpdate = true;
        if (result == UpdateCheck.UpdateResult.NONE) {
            return;
        }
        ThreadManager.postDelayed(ThreadManager.THREAD_UI, new Runnable() {
            @Override
            public void run() {
                tryShowUpdateInfoDialog(versionInfo);
            }
        }, TIME_DELAY);
    }

    private void tryShowUpdateInfoDialog(final VersionInfo versionInfo) {
        mVersionInfo = versionInfo;
        if (mUpdateInfoDialog == null) {
            mUpdateInfoDialog = new SimpleTextDialog(this);
            mUpdateInfoDialog.setTitle(ResourceHelper.getString(R.string.update_tips));
            mUpdateInfoDialog.setText(versionInfo.getDescription());
            mUpdateInfoDialog.addYesNoButton(ResourceHelper.getString(R.string.upgrade), ResourceHelper.getString(R.string.cancel));
            mUpdateInfoDialog.setRecommendButton(GenericDialog.ID_BUTTON_YES);
            mUpdateInfoDialog.setCanceledOnTouchOutside(false);
            mUpdateInfoDialog.setCancelable(false);
            mUpdateInfoDialog.setOnClickListener(new IDialogOnClickListener() {
                @Override
                public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                    if (viewId == GenericDialog.ID_BUTTON_YES) {
                        if (Utils.checkPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            handleOnClickSureUpdateApp(versionInfo);
                            Log.d(TAG, "有权限");
                        } else {
                            ActivityCompat.requestPermissions(LoginActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
                            Log.d(TAG, "申请权限");
                        }
                        return true;
                    } else {
                        handleOnClickCancelUpdate(versionInfo);
                    }
                    return false;
                }
            });
        }
        if (mUpdateInfoDialog.isShowing()) {
            return;
        }
        mUpdateInfoDialog.show();
    }

    private void handleOnClickSureUpdateApp(VersionInfo versionInfo) {
        UpdateHelper helper = new UpdateHelper(LoginActivity.this, versionInfo);
        helper.downloadApk();
        if (!versionInfo.isForce()) {
            UpdateCheckManager.getInstance().setIgnoreUpdateNow(true);
        }
    }

    private void handleOnClickCancelUpdate(VersionInfo versionInfo) {
        if (versionInfo.isForce()) {
            LiveApplication.getInstance().killAllProcess(LoginActivity.this);
        } else {
            UpdateCheckManager.getInstance().setIgnoreUpdateNow(true);
        }
    }

    private void showLoginLayout() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mBottomLayout, "alpha", 0f, 1f);
        animator.setDuration(500L);
        animator.start();
    }

    private void showLoginLayoutNow() {

        if (!isVideoCompleted) {
            isVideoCompleted = true;
            UiPreference.putBoolean(Common.KEY_IS_PLAY_LOGIN_VIDEO, true);
            mBottomLayout.setVisibility(View.VISIBLE);
            showLoginLayout();
//            mSurfaceView.setVisibility(View.GONE);
            showBg();
        }
    }

    private void showBg() {
        mBgSimpleDraweeView.setVisibility(View.VISIBLE);
        final ObjectAnimator animator = ObjectAnimator.ofFloat(mBgSimpleDraweeView, "alpha", 0f, 1f);
        animator.setDuration(500L);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mSurfaceView.setVisibility(View.GONE);
                animator.removeAllListeners();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults != null && grantResults.length >= 2) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                            grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        handleOnClickSureUpdateApp(mVersionInfo);
                    } else {
                        showToast(R.string.no_storage_permission_tip);
                    }
                }
                break;
        }
    }

    @Override
    protected void activityAnim() {
        overridePendingTransition(0, 0);//无动画
    }

    @Override
    public void onBackPressed() {
        if (mCountDownProgressBar != null && mCountDownProgressBar.isCounDowning()) {
            mCountDownProgressBar.stopCountDownTime();
            goToMainActivity(false, false);
        } else {
            super.onBackPressed();
        }
    }

}
