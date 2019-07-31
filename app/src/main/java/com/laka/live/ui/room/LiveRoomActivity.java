package com.laka.live.ui.room;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.Toast;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.application.LiveApplication;
import com.laka.live.bean.Room;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.NetStateManager;
import com.laka.live.help.NoDoubleClickManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.RoomListMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.widget.VerticalViewPager;
import com.laka.live.ui.widget.dialog.GenericDialog;
import com.laka.live.ui.widget.dialog.IDialogOnClickListener;
import com.laka.live.util.Common;
import com.laka.live.util.Log;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Util;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.laka.live.help.SubcriberTag.AUTO_GO_NEXT_ANCHOR_EVENT;
import static com.laka.live.help.SubcriberTag.ROOM_CAN_SCROLL;

/**
 * 直播间（测试、正式）
 */
public class LiveRoomActivity extends BaseActivity implements TabHost.OnTabChangeListener
        , ViewPager.OnPageChangeListener, GsonHttpConnection.OnResultListener<RoomListMsg> {
    private static final String TAG = "LiveRoomActivity";

    private static final long INTERVAL = 20000;

    private static final int UPDTAE_MAG = 1000;

    private TabHost mTabHost;
    private VerticalViewPager mViewPager;
    private TabsAdapter mTabsAdapter;


    private FragmentManager mFragmentManager;
    private LiveRoomFragment mFragment;

    private boolean isCreater;

    private boolean isChanged;

    private int mCurrentItem = 1;

    private List<Room> mCurrentRoomList;

    private int mCurrentRoomId;


    private boolean isScrollTop;

    private String mFrom;

    private Handler mHandler;

    private long mLastUpdateTime = 0;

    private boolean isEnableScroll = true;

    // 观看直播
    public static void startPlay(final Context context, final int roomId, final boolean isCreate,
                                 final String title, final String streamId, final String channelId,
                                 final String avatar, final String cover, final String from, final String courseId) {
        if (!NoDoubleClickManager.isNoDoubleClick(NoDoubleClickManager.TYPE_ENTER_ZHIBO_ROOM)) {
            return;
        }

        if (!NetStateManager.getInstance().getIsNetworkOk(context)) {
            ToastHelper.showToast(R.string.notify_no_network);
            return;
        }
        if (!NetStateManager.getInstance().getIsWifiNow(context)) {
            if (NetStateManager.getInstance().getIsAlreadyNotify()) {
                Toast.makeText(context, R.string.current_mobile_net_be_careful, Toast.LENGTH_SHORT).show();
            } else {
                final Activity container;
                if (context instanceof Activity) {
                    container = (Activity) context;

                    NetStateManager.getInstance().showMobileNetWorkDialog(container, R.string.in_mobile_net_suggest_wifi_see, R.string.change_net, R.string.goon_see, new IDialogOnClickListener() {
                        @Override
                        public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                            if (viewId == GenericDialog.ID_BUTTON_YES) {
                                //切换网络，跳去设置网络
                                NetStateManager.getInstance().goToNetSetting(container);
                            } else if (viewId == GenericDialog.ID_BUTTON_NO) {
                                // 继续观看
                                checkLiveStatus(container, roomId, title, streamId, cover, channelId, avatar, from, null, courseId);
                            }
                            return false;
                        }
                    });
                    return;
                }
            }
        }
        checkLiveStatus(context, roomId, title, streamId, cover, channelId, avatar, from, null, courseId);
    }

    // 开始直播
    public static void startLive(Context context, int userId, boolean isCreate,
                                 String title, String identifier, String cover,
                                 String avatar, String from, String courseId,
                                 String topics) {
        if (!NoDoubleClickManager.isNoDoubleClick(NoDoubleClickManager.TYPE_ENTER_ZHIBO_ROOM)) {
            return;
        }

        startActivity(context, userId, isCreate, title, identifier, cover, "", avatar, from, courseId, topics, false);
    }

    // 开始直播测试
    public static void startTestLive(Context context, String courseId) {
        if (!NoDoubleClickManager.isNoDoubleClick(NoDoubleClickManager.TYPE_ENTER_ZHIBO_ROOM)) {
            return;
        }

        startActivity(context, AccountInfoManager.getInstance().getCurrentAccountUserId(), true, "", AccountInfoManager.getInstance().getCurrentAccountUserIdStr(), "", "", "", "", courseId, "", true);
    }


    private static void startActivity(Context context, int roomId, boolean isCreate,
                                      String title, String identifier, String cover,
                                      String downUrl, String avatar, String from,
                                      String courseId, String topics, boolean testLive) {
        if (context != null) {
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                activity.overridePendingTransition(R.anim.live_room_in, R.anim.live_room_out);
            }
            Intent intent = new Intent(context, LiveRoomActivity.class);
            intent.putExtra(Common.EXTRA_ROOM_ID, roomId);
            intent.putExtra(Common.EXTRA_IS_CREATER, isCreate);
            intent.putExtra(Common.EXTRA_TITLE, title);
            intent.putExtra(Util.EXTRA_IDENTIFIER, identifier);
            intent.putExtra(Common.EXTRA_COVER, cover);
            intent.putExtra(Common.DOWN_URL, downUrl);
            intent.putExtra(Common.AVATAR, avatar);
            intent.putExtra(Common.FROM, from);
            intent.putExtra(Common.EXTRA_IS_TEST_LIVE, testLive);
            intent.putExtra(Common.EXTRA_COURSE_ID, courseId);
            intent.putExtra(Common.EXTRA_TOPIC, topics);
            context.startActivity(intent);

        }
    }

    private static void checkLiveStatus(final Context context, final int roomId, final String title, final String streamId,
                                        final String cover, final String channelId, final String avatar,
                                        final String from, String topics, final String courseId) {

        if (!AccountInfoManager.getInstance().isLogin()) {
            AccountInfoManager.getInstance().tryOpenLoginWindow();
            return;
        }

        // 只有观看直播会走到这里，所以不用管这个直播是不是我的。
        // 判断当前用户是否有正在进行的直播，如果有，则不能开启另一个直播
        if (AccountInfoManager.getInstance().getAccountInfo().isLiving()) {
            ToastHelper.showToast("您尚有直播正在进行");
        } else {
            startPlayActivity(context, roomId, title, streamId, cover, channelId, avatar, from, null, courseId);
        }

    }

    private static void startPlayActivity(Context context, int roomId, String title, String streamId,
                                          String cover, String channelId, String avatar,
                                          String from, String topics, String courseId) {
        if (context != null) {
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                activity.overridePendingTransition(R.anim.live_room_in, R.anim.live_room_out);
            }
            Intent intent = new Intent(context, LiveRoomActivity.class);
            intent.putExtra(Common.EXTRA_ROOM_ID, roomId);
            intent.putExtra(Common.EXTRA_IS_CREATER, false);
            intent.putExtra(Common.EXTRA_TITLE, title);
            intent.putExtra(Util.EXTRA_IDENTIFIER, roomId + "");
            intent.putExtra(Common.STREAM_ID, streamId);
            intent.putExtra(Common.CHANNEL_ID, channelId);
            intent.putExtra(Common.EXTRA_COVER, cover);
            intent.putExtra(Common.AVATAR, avatar);
            intent.putExtra(Common.FROM, from);
            intent.putExtra(Common.EXTRA_COURSE_ID, courseId);
            intent.putExtra(Common.EXTRA_TOPIC, topics);
            context.startActivity(intent);

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, " onNewIntent 进了其他直播间");

        setIntent(intent);
        LiveRoomFragment currentFragment = getCurrentFragment();
        if (currentFragment != null) {
            currentFragment.onNewIntent(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, " onCreate");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_live_room);
        setSwipeBackEnable(false);
        initView();

    }

    private void initView() {
        //        ZegoAvConfig config;
//        if (isCreater) {
//            config = new ZegoAvConfig(ZegoAvConfig.Level.High);
//        } else {
//            config = new ZegoAvConfig(ZegoAvConfig.Level.VeryLow);
//        }
//        ZegoApiManager.getInstance().setAVConfig(config);

        Intent intent = getIntent();
        isCreater = intent.getBooleanExtra(Common.EXTRA_IS_CREATER, false);

        if (isCreater) {
            initAnchor(intent.getBooleanExtra(Common.EXTRA_IS_TEST_LIVE, false));
        } else {
            initAudience();
        }
    }

    /**
     * 初始化主播
     */
    private void initAnchor(boolean testLive) {
        ViewStub viewStub = (ViewStub) findViewById(R.id.anchor_layout);
        viewStub.inflate();

        mFragmentManager = getSupportFragmentManager();
        setFragmentPage(0, testLive);
    }

    private void setFragmentPage(int page, boolean testLive) {
        if (isFinishing()) {
            return;
        }
        // 开启一个Fragment事务
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (page) {
            case 0:
                if (mFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    mFragment = new LiveRoomFragment();
                    Bundle bundle = getIntent().getExtras();
                    bundle.putBoolean(Common.EXTRA_IS_CREATER, true);
                    bundle.putBoolean(Common.EXTRA_IS_TEST_LIVE, testLive);
                    mFragment.setArguments(bundle);
                    transaction.add(R.id.content, mFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(mFragment);
                }
                break;
            default:
                break;
        }
        transaction.commitAllowingStateLoss();//.commit();
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (mFragment != null) {
            transaction.hide(mFragment);
        }
    }

    /**
     * 初始化观众
     */
    private void initAudience() {
        Intent intent = getIntent();

        mCurrentRoomId = intent.getIntExtra(Common.EXTRA_ROOM_ID, 0);
        mFrom = intent.getStringExtra(Common.FROM);
        mCurrentRoomList = new ArrayList<>();
        if (TextUtils.equals(mFrom, Common.FROM_FIND)) {
            mCurrentRoomList.addAll(LiveApplication.getInstance().findRoomList);
        } else if (TextUtils.equals(mFrom, Common.FROM_HOT)) {
            mCurrentRoomList.addAll(LiveApplication.getInstance().roomList);
        } else {
            mCurrentRoomList = new ArrayList<>();
        }

        ViewStub viewStub = (ViewStub) findViewById(R.id.audience_layout);

        View root = viewStub.inflate();

        mViewPager = (VerticalViewPager) root.findViewById(R.id.viewpager);

        mTabHost = (TabHost) root.findViewById(android.R.id.tabhost);
        mTabHost.setup();
        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager, this);
        mViewPager.setOffscreenPageLimit(2);
        mTabsAdapter.setOnPageChangeListener(this);

        Bundle args = intent.getExtras();

        args.putBoolean(LiveRoomFragment.IS_INIT, true);
        args.putBoolean(Common.IS_FROM_SCROLL, false);

        mTabsAdapter.addTab(mTabHost.newTabSpec("live").setIndicator("live"),
                LiveRoomFragment.class, args);
        mTabsAdapter.insertTab(mTabHost.newTabSpec("empty1").setIndicator("empty1"),
                EmptyLiveFragment.class, null, 0);
        mTabsAdapter.insertTab(mTabHost.newTabSpec("empty2").setIndicator("empty2"),
                EmptyLiveFragment.class, null, 2);

        enableScroll();
        mCurrentItem = 1;
        mViewPager.setCurrentItem(mCurrentItem, false);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case UPDTAE_MAG:
                        updateRoomList();
                        break;
                }
            }
        };
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (isCreater == false) {
            long now = System.currentTimeMillis();
            if (now - mLastUpdateTime > INTERVAL) {
                updateRoomList();
            } else {
                delayUpdate(now - mLastUpdateTime);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mHandler != null) {
            mHandler.removeMessages(UPDTAE_MAG);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusManager.postEvent(5000, SubcriberTag.START_DOWNLOAD_GIFT_RES);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LiveRoomFragment currentFragment = getCurrentFragment();

        if (currentFragment != null) {
            return currentFragment.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        LiveRoomFragment currentFragment = getCurrentFragment();
        if (currentFragment != null) {
            currentFragment.dispatchTouchEvent(event);
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onTabChanged(String tabId) {

        /*final LiveRoomFragment fragment = getCurrentFragment();
        if (fragment != null) {

            mViewPager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fragment.initFragment();
                }
            }, 500L);
        }

        mViewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopFragment(getLastFragment());
                stopFragment(getNextFragment());
            }
        }, 300L);*/
    }

    private void stopFragment(LiveRoomFragment fragment) {
        if (fragment != null) {
            fragment.stopRoom();
        }
    }

    public LiveRoomFragment getCurrentFragment() {
        if (isCreater) {
            return mFragment;
        } else {
            if (mTabsAdapter.getCount() > 1) {
                return (LiveRoomFragment) mTabsAdapter.getCurrentTab(1);
            } else if (mTabsAdapter.getCount() == 1) {
                return (LiveRoomFragment) mTabsAdapter.getCurrentTab(0);
            } else {
                return null;
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        isChanged = true;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (ViewPager.SCROLL_STATE_IDLE == state && isChanged) {
            isChanged = false;
            int position = mTabHost.getCurrentTab();
            int index = findIndex(mCurrentRoomId);

            if (mCurrentRoomList == null || mCurrentRoomList.isEmpty()) {
                mViewPager.setCurrentItem(mCurrentItem, false);
                return;
            }
            Room room;
            if (position < mCurrentItem) {
                //pre
                isScrollTop = true;
                index -= 1;
                if (index < 0) {
                    room = mCurrentRoomList.get(mCurrentRoomList.size() - 1);
                } else {
                    room = mCurrentRoomList.get(index);
                }
            } else if (position > mCurrentItem) {
                //next
                index += 1;
                if (index >= mCurrentRoomList.size()) {
                    room = mCurrentRoomList.get(0);
                } else {
                    room = mCurrentRoomList.get(index);
                }
                isScrollTop = false;
            } else {
                mViewPager.setCurrentItem(mCurrentItem, false);
                return;
            }

            mCurrentRoomId = room.getId();
            Bundle bundle = new Bundle();
            bundle.putInt(Common.EXTRA_ROOM_ID, mCurrentRoomId);
            bundle.putBoolean(Common.EXTRA_IS_CREATER, false);
            bundle.putString(Common.EXTRA_TITLE, room.getTitle());
            bundle.putString(Util.EXTRA_IDENTIFIER, String.valueOf(room.getId()));
            bundle.putString(Common.STREAM_ID, room.getStreamId());
            bundle.putString(Common.CHANNEL_ID, room.getStreamId());
            bundle.putString(Common.EXTRA_COVER, room.getScreenShot());
            bundle.putString(Common.AVATAR, room.getAvatar());
            bundle.putBoolean(Common.IS_FROM_SCROLL, true);

            /*mTabsAdapter.removeTab(mCurrentItem);

            String key = "live" + mKey;
            mTabsAdapter.insertTab(mTabHost.newTabSpec(key).setIndicator(key),
                    LiveRoomFragment.class, bundle, 1);*/

            LiveRoomFragment fragment = getCurrentFragment();
            if (fragment != null) {
                fragment.stopRoom();
                fragment.setArguments(bundle);
                fragment.initFragment();
            }
            mViewPager.setCurrentItem(mCurrentItem, false);

            mViewPager.enableScroll(false);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mViewPager.enableScroll(true);
                }
            }, 500);
        }
    }

    private int findIndex(int roomId) {
        int index = 0;
        for (Room room : mCurrentRoomList) {
            if (room != null && room.getId() == roomId) {
                return index;
            }
            index++;
        }

        return 0;
    }

    private void delayUpdate(long delay) {
        mHandler.removeMessages(UPDTAE_MAG);
        Message msg = Message.obtain();
        msg.what = UPDTAE_MAG;
        mHandler.sendMessageDelayed(msg, delay);
    }

    private void updateRoomList() {
        if (TextUtils.equals(mFrom, Common.FROM_FIND)) {
            DataProvider.queryHotRoom(this, false, this);
        } else if (TextUtils.equals(mFrom, Common.FROM_HOT)) {
            DataProvider.queryFind(this, this);
        }
    }

    @Override
    public void onSuccess(RoomListMsg roomListMsg) {
        if (roomListMsg != null) {
            List<Room> list = roomListMsg.getList();
            if (list != null) {
                mCurrentRoomList.clear();
                mCurrentRoomList.addAll(list);
                enableScroll();
            }
        }
        delayUpdate(INTERVAL);
    }

    @Override
    public void onFail(int errorCode, String errorMsg, String command) {
        delayUpdate(INTERVAL);
    }

    private void enableScroll() {
        if (isEnableScroll) {
            int size = mCurrentRoomList.size();

            if (size == 2 && mCurrentRoomList.get(0).getId() == mCurrentRoomList.get(1).getId()) {
                mViewPager.enableScroll(false);
                return;
            }

            if (size > 1 || (size == 1 && mCurrentRoomList.get(0) != null
                    && mCurrentRoomList.get(0).getId() != mCurrentRoomId)) {
                //可以滑动切换
                mViewPager.enableScroll(true);
            } else {
                mViewPager.enableScroll(false);
            }
        } else {
            mViewPager.enableScroll(false);
        }
    }

    private void autoGoNext() {
        if (isScrollTop) {
            mViewPager.setCurrentItem(0);
        } else {
            mViewPager.setCurrentItem(2);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PostEvent event) {
        super.onEvent(event);

        if (TextUtils.equals(AUTO_GO_NEXT_ANCHOR_EVENT, event.tag)) {
            autoGoNext();
        } else if (TextUtils.equals(ROOM_CAN_SCROLL, event.tag)) {
            boolean canScroll = (boolean) event.event;
            Log.d(TAG, " ROOM_CAN_SCROLL canScroll=" + canScroll);
            mViewPager.enableScroll(canScroll);
        } else if (TextUtils.equals(ROOM_CAN_SCROLL, event.tag)) {
            isEnableScroll = (boolean) event.event;
            enableScroll();
        } else if (TextUtils.equals(SubcriberTag.REQUEST_LIVE_PERMISSION, event.tag)) {
            List<String> permissionsNeeds = new ArrayList<>();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED) {
                permissionsNeeds.add(Manifest.permission.CAMERA);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_DENIED) {
                permissionsNeeds.add(Manifest.permission.RECORD_AUDIO);
            }

            if (permissionsNeeds.isEmpty()) {
            } else {
                ActivityCompat.requestPermissions(this
                        , permissionsNeeds.toArray(new String[permissionsNeeds.size()]), 123);
            }
        } else if (TextUtils.equals(SubcriberTag.LIVE_SET_ORIENTATION, event.tag)) {
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, " onConfigurationChanged");
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // land do nothing is ok
            EventBusManager.postEvent(1, SubcriberTag.LIVE_VIDEO_ORIENTATION);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // port do nothing is ok
            EventBusManager.postEvent(0, SubcriberTag.LIVE_VIDEO_ORIENTATION);
        }
    }
}

