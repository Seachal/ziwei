package com.laka.live.ui.activity;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.application.LiveApplication;
import com.laka.live.bean.UserInfo;
import com.laka.live.dao.DbManger;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.manager.OnResultListenerAdapter;
import com.laka.live.manager.RoomManager;
import com.laka.live.network.DataProvider;
import com.laka.live.ui.course.NewestCoursesActivity;
import com.laka.live.ui.fragment.BaseFragment;
import com.laka.live.ui.fragment.ContentFragment;
import com.laka.live.ui.fragment.MainFragment;
import com.laka.live.ui.fragment.MyFragment;
import com.laka.live.ui.shopping.ShoppingHomeFragment;
import com.laka.live.ui.widget.HomeTabItemView;
import com.laka.live.update.UpdateCheckManager;
import com.laka.live.util.AppExitHelper;
import com.laka.live.util.KeepAliveUtil;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.SharePreferenceUtil;
import com.laka.live.util.ToastHelper;
import com.laka.live.video.ui.fragment.VideoHomeFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import framework.ioc.annotation.Inject;
import framework.ioc.annotation.InjectView;
import laka.live.bean.ChatSession;


public class MainActivity extends BaseActivity implements View.OnClickListener
        , EventBusManager.OnEventBusListener, ActivityCompat.OnRequestPermissionsResultCallback/*, RadioGroup.OnCheckedChangeListener*/ {

    public static final int REQUST_OVER_LAY_CODE = 1024;

    @InjectView(id = R.id.tab_video)
    public HomeTabItemView tabVideo;
    @InjectView(id = R.id.tab_main)
    public HomeTabItemView tabMain;
    @InjectView(id = R.id.tab_content)
    public HomeTabItemView tabContent;
    @InjectView(id = R.id.tab_shopping)
    public HomeTabItemView tabShopping;
    @InjectView(id = R.id.tab_my)
    public HomeTabItemView tabMy;


    private String lastFragment;
    private RoomManager roomManger;
    private AppExitHelper mExitHelper;
    private FragmentManager mFragmentManager;
    private BaseFragment mainFragment, contentFragment, mallFragment, myFragment;
    private BaseFragment videoFragment;

    private KeepAliveUtil keepAliveUtil;

    @Inject
    public UserInfo mUserInfo;

    private List<HomeTabItemView> mTabViewList = new ArrayList<>();
    // 推送进入需要使用到
    public final static int TAB_VIDEO = R.id.tab_video, TAB_MAIN = R.id.tab_main,
            TAB_CONTENT = R.id.tab_content, TAB_MALL = R.id.tab_shopping, TAB_MINE = R.id.tab_my;
    static MainActivity self;

    public static MainActivity getInstance() {
        return self;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
        SharePreferenceUtil.remarkStartCount();
        setContentView(R.layout.activity_main);
        setSwipeBackEnable(false);
        // 当异常崩溃的时候,lastFragment会被重置,导致出现空白
        // 所以,在崩溃时,要保存一下状态,在恢复时恢复这个值。
        if (savedInstanceState != null) {
            lastFragment = savedInstanceState.getString("lastFragment");
        }
        mTabViewList.add(tabVideo);
        mTabViewList.add(tabMain);
        mTabViewList.add(tabContent);
        mTabViewList.add(tabShopping);
        mTabViewList.add(tabMy);
        initView();
        initData();
        initScreen();
        findById(R.id.tab_video).setOnClickListener(this);
        findById(R.id.tab_main).setOnClickListener(this);
        findById(R.id.tab_content).setOnClickListener(this);
        findById(R.id.tab_shopping).setOnClickListener(this);
        findById(R.id.tab_my).setOnClickListener(this);

        int tabId = getIntent().getIntExtra("tabId", -1);
        if (tabId != -1) {
            openTabById(tabId);
        }
    }

    // 初始化UI
    private void initView() {
        keepAliveUtil = new KeepAliveUtil(this);
        keepAliveUtil.handleKeepAliveLogic();
        initFragment();
        initRoomManger();
    }

    private void initScreen() {

        DisplayMetrics metric = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels; // 屏幕宽度（像素）
        int height = metric.heightPixels; // 屏幕高度（像素）
        float density = metric.density; // 屏幕密度（0.75/1.0/1.5）
        int densityDpi = metric.densityDpi; // 屏幕密度DPI（120/160/240）
        LiveApplication.screenWidth = width;
        LiveApplication.screenHeight = height;

    }

    // 初始化数据
    private void initData() {

        postJPushId();
        getStsToken();
        UpdateCheckManager.checkUpdate(this);
        mExitHelper = new AppExitHelper(this);
    }

    private void initFragment() {

        lastFragment = "videoFragment"; // 默认是首页
        mFragmentManager = getSupportFragmentManager();
        videoFragment = new VideoHomeFragment();
        mainFragment = new MainFragment();
        contentFragment = new ContentFragment();
        mallFragment = new ShoppingHomeFragment();
        myFragment = new MyFragment();
        setCurrentFragment("videoFragment", videoFragment);
        mSelectTabId = R.id.tab_video;
        onSwitchTabForStyle(mSelectTabId); // 某人选中第一个tab
    }

    // 改变tab样式
    private void onSwitchTabForStyle(int selectTabId) {
        for (int i = 0; i < mTabViewList.size(); i++) {
            HomeTabItemView homeTabItemView = mTabViewList.get(i);
            if (homeTabItemView.getId() == selectTabId) {
                homeTabItemView.setSelected(true);
            } else {
                homeTabItemView.setSelected(false);
            }
        }
    }

    private void initRoomManger() {
        roomManger = RoomManager.getInstance();
        roomManger.addResultListener(socketListener);
        roomManger.startRoom();
    }

    // 选中的tab
    private int mSelectTabId;

    @Override
    public void onClick(View v) {

        if (mSelectTabId != v.getId()) { // 单击 tab 切換
            onSwitchTabForAnima(v.getId());
            return;
        }
        // 双击刷新
        onDoubleClickRefresh(v);
    }

    /**
     * 双击tab刷新
     *
     * @param v
     */
    private void onDoubleClickRefresh(View v) {
        long lastClick = v.getTag() == null ? 0 : (long) v.getTag();
        switch (v.getId()) {
            case R.id.tab_video:
                if (isDoubleClick(lastClick)) {
                    EventBusManager.postEvent(SubcriberTag.VIDEO_TAB_DOUBLE_CLICK_EVENT);
                }
                break;
            case R.id.tab_main:
                if (isDoubleClick(lastClick)) {
                    EventBusManager.postEvent(SubcriberTag.MAIN_TAB_DOUBLE_CLICK_EVENT);
                }
                break;
            case R.id.tab_content:
                if (isDoubleClick(lastClick)) {
                    EventBusManager.postEvent(SubcriberTag.CONTENT_TAB_DOUBLE_CLICK_EVENT);
                }
                break;
            default:
                break;
        }
        v.setTag(System.currentTimeMillis());
    }

    /**
     * tab 切换
     *
     * @param checkedId
     */
    private void onSwitchTabForAnima(int checkedId) {
        mSelectTabId = checkedId;
        onSwitchTabForStyle(mSelectTabId);
        switch (checkedId) {
            case R.id.tab_video:
                setAnimation(tabVideo.getImageIcon(), R.drawable.tab_main_anim_h);
                setAnimation(tabMain.getImageIcon(), R.drawable.tab_hot_anim_n);
                setAnimation(tabMy.getImageIcon(), R.drawable.tab_mine_anim_n);
                setAnimation(tabShopping.getImageIcon(), R.drawable.tab_mall_anim_n);
                setAnimation(tabContent.getImageIcon(), R.drawable.tab_course_anim_n);
                setCurrentFragment("videoFragment", videoFragment);
                break;
            case R.id.tab_main:
                setAnimation(tabMain.getImageIcon(), R.drawable.tab_hot_anim_h);
                setAnimation(tabVideo.getImageIcon(), R.drawable.tab_main_anim_n);
                setAnimation(tabMy.getImageIcon(), R.drawable.tab_mine_anim_n);
                setAnimation(tabShopping.getImageIcon(), R.drawable.tab_mall_anim_n);
                setAnimation(tabContent.getImageIcon(), R.drawable.tab_course_anim_n);
                setCurrentFragment("mainFragment", mainFragment);
                break;
            case R.id.tab_content:
                setAnimation(tabContent.getImageIcon(), R.drawable.tab_course_anim_h);
                setAnimation(tabMain.getImageIcon(), R.drawable.tab_hot_anim_n);
                setAnimation(tabVideo.getImageIcon(), R.drawable.tab_main_anim_n);
                setAnimation(tabMy.getImageIcon(), R.drawable.tab_mine_anim_n);
                setAnimation(tabShopping.getImageIcon(), R.drawable.tab_mall_anim_n);
                setCurrentFragment("contentFragment", contentFragment);
                break;
            case R.id.tab_shopping:
                setAnimation(tabShopping.getImageIcon(), R.drawable.tab_mall_anim_h);
                setAnimation(tabMain.getImageIcon(), R.drawable.tab_hot_anim_n);
                setAnimation(tabContent.getImageIcon(), R.drawable.tab_course_anim_n);
                setAnimation(tabVideo.getImageIcon(), R.drawable.tab_main_anim_n);
                setAnimation(tabMy.getImageIcon(), R.drawable.tab_mine_anim_n);
                setCurrentFragment("mallFragment", mallFragment);
                break;
            case R.id.tab_my:
                setAnimation(tabMy.getImageIcon(), R.drawable.tab_mine_anim_h);
                setAnimation(tabMain.getImageIcon(), R.drawable.tab_hot_anim_n);
                setAnimation(tabVideo.getImageIcon(), R.drawable.tab_main_anim_n);
                setAnimation(tabShopping.getImageIcon(), R.drawable.tab_mall_anim_n);
                setAnimation(tabContent.getImageIcon(), R.drawable.tab_course_anim_n);
                setCurrentFragment("myFragment", myFragment);
                break;
            default:
                break;
        }
    }


    // 是否是双击
    private boolean isDoubleClick(long lastClick) {
        long now = System.currentTimeMillis();
        return Math.abs(now - lastClick) <= 500;
    }

    public OnResultListenerAdapter socketListener = new OnResultListenerAdapter() {
        @Override
        public void chatDidConnect() {
            super.chatDidConnect();
            roomManger.getGuanfangNotice();
            roomManger.getOffineMessage(5);
            roomManger.removeResultListener(socketListener);
        }
    };


    // 上传极光Id
    private void postJPushId() {
        if (AccountInfoManager.getInstance().checkUserIsLogin()) {
            DataProvider.postJPushId(JPushInterface.getRegistrationID(this));
        }
    }

    // 获取OSS的相关密钥
    public void getStsToken() {
        DataProvider.getStsToken(this);
    }

    // 设置当前的Fragment
    public void setCurrentFragment(String fragmentTag, BaseFragment fragment) {

        //开启事务
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        //判断当前fragment是否有被添加
        boolean isAdded = mFragmentManager.findFragmentByTag(fragmentTag) != null;

        //获取上一个显示的fragment
        BaseFragment lastFragment = (BaseFragment) mFragmentManager
                .findFragmentByTag(getLastFragment());

        //隐藏上一个显示的fragment
        if (lastFragment != null) {
            fragmentTransaction.hide(lastFragment);
            lastFragment.onHide();
        }

        if (isAdded) {
            //显示当前的fragment
            fragmentTransaction.show(fragment);
            fragment.onResume();
        } else {
            //增加新的fragment
            fragmentTransaction.add(R.id.frameLayout, fragment, fragmentTag);
        }

        //设置当前fragment为上一个fragment
        setLastFragment(fragmentTag);

        //提交事务
        fragmentTransaction.commitAllowingStateLoss();

    }

    private void setLastFragment(String fragmentTag) {
        this.lastFragment = fragmentTag;
    }

    private String getLastFragment() {
        return lastFragment;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // 崩溃的时候要保存一下，不然会出现空白
        outState.putString("lastFragment", lastFragment);
    }

    @Override
    public void onBackPressed() {
        mExitHelper.exitApplication(this);
    }

    // 取消进出动画
    @Override
    protected void activityAnim() {
        overridePendingTransition(0, 0);//无动画
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PostEvent event) {
        super.onEvent(event);

        if (event.tag.equals(SubcriberTag.OPEN_COURSE_TAB)) {
            // 跳到最新课程列表
            NewestCoursesActivity.startActivity(mContext, (Integer) event.event);
        } else if (event.tag.equals(SubcriberTag.OPEN_MALL_TAB)) {
            // 跳到商城标签
            openTabById(TAB_MALL);
        } /*else if (SubcriberTag.REFRESH_BOTTOM_UNREAD_RED.equals(event.tag)) {
            // 未读消息
            long unreadCnt = (long) event.event;
            if (unreadCnt > 0) {
                tabMy.showRedPoint(View.VISIBLE);
            } else {
                tabMy.showRedPoint(View.GONE);
            }
        }*/else if (SubcriberTag.RECEIVE_CHAT_MSG.equals(event.tag)) {
            // 接收消息
            refreshUnreadRed();
        }
    }

    public void refreshUnreadRed() {

        List<ChatSession> sessions = DbManger.getInstance().getSessions(DbManger.SESSION_TYPE_FOLLOW);
        long unFollowUnreadCnt = DbManger.getInstance().getUnfollowUnreadCnt();
        //查最后一条陌生人消息
        ChatSession lastSession = DbManger.getInstance().getLastStrangerSession();
        if (lastSession != null) {
            lastSession.setUnreadCnt((int) unFollowUnreadCnt);
            lastSession.setType(DbManger.SESSION_TYPE_STRANGER);
            //排序放入列表
            sessions.add(lastSession);
        }

        //排序
        Collections.sort(sessions, new Comparator<ChatSession>() {
            @Override
            public int compare(ChatSession lhs, ChatSession rhs) {
                if (lhs.getDate() > rhs.getDate()) {
                    return -1;
                } else if (lhs.getDate() < rhs.getDate()) {
                    return 1;
                }
                return 0;
            }
        });

//        long totalUnreadCnt = Unicorn.getUnreadCount();
//       获取未读会话数
        long totalUnreadCnt = DbManger.getInstance().getTotalUnreadCnt();
        if (totalUnreadCnt > 0) {
            tabMy.showRedPoint(View.VISIBLE);
        } else {
            tabMy.showRedPoint(View.GONE);
        }

        EventBusManager.postEvent(totalUnreadCnt, SubcriberTag.REFRESH_BOTTOM_UNREAD_RED);
    }

    /**
     * @param tabId
     */
    public void openTabById(int tabId) {
        onSwitchTabForAnima(tabId);
    }

    private Handler handler = new Handler();

    // 设置动画
    private void setAnimation(final ImageView tab, int resId) {

        final AnimationDrawable drawableTop = (AnimationDrawable) ResourceHelper.getDrawable(resId);

        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int index = drawableTop.getNumberOfFrames() - 1; index >= 0; --index) {
                    final int finalIndex = index;
                    handler.postDelayed(new Runnable() {

                        public void run() {
                            //此处调用第二个动画播放方法
                            Drawable drawable = drawableTop.getFrame(finalIndex);
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                            tab.setImageDrawable(drawable);
                        }

                    }, drawableTop.getDuration(index));
                }
            }
        }).start();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i = 0; i < permissions.length; ++i) {

            if (permissions[i].equals(Manifest.permission.RECORD_AUDIO) && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                ToastHelper.showToast("没有录音权限");
                return;
            } else if (permissions[i].equals(Manifest.permission.CAMERA) && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                ToastHelper.showToast("没有摄像头权限");
                return;
            }

        }

        // 如果权限都有，开始测试直播
        if (contentFragment != null) {
            ((ContentFragment) contentFragment).startTestLive();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (keepAliveUtil != null) {
            keepAliveUtil.handleKeepAliveLogic();
        }
        refreshUnreadRed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        self = null;
    }
}
