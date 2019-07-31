package com.laka.live.application;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.view.Display;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import com.laka.live.BuildConfig;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.GiftInfo;
import com.laka.live.bean.Room;
import com.laka.live.control.QavsdkControl;
import com.laka.live.crash.CrashHandler;
import com.laka.live.help.ActivityLifecycleCb;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.manager.RoomManager;
import com.laka.live.music.MusicManager;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.network.Volley;
import com.laka.live.photopreview.FrescoHelper;
import com.laka.live.shopping.network.HttpManager;
import com.laka.live.shopping.system.SystemHelper;
import com.laka.live.shopping.utils.SettingFlags;
import com.laka.live.ui.widget.FloatWindow;
import com.laka.live.update.UpdateCheckManager;
import com.laka.live.util.Common;
import com.laka.live.util.HardwareUtil;
import com.laka.live.util.HotfixUtil;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.LakaUtil;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.RuntimeCheck;
import com.laka.live.util.ToolProcessPreference;
import com.laka.live.util.UiPreference;
import com.laka.live.util.UnicornHelper;
import com.laka.live.util.Utils;
import com.laka.live.video.mediaplayer.JZExoPlayer;
import com.laka.live.zego.ZegoApiManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.orhanobut.logger.Logger;
import com.qiyukf.unicorn.api.OnMessageItemClickListener;
import com.qiyukf.unicorn.api.StatusBarNotificationConfig;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.YSFOptions;
import com.sina.weibo.sdk.utils.LogUtil;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.jzvd.Jzvd;
import framework.FrameWork;


/**
 * Created by luwies on 16/3/4.
 */
public class LiveApplication extends MultiDexApplication {
    private static final String TAG = "LiveApplication";

    public static RequestQueue mQueue;

    public static LiveApplication application;

    /**
     * 屏幕宽度
     */
    public static int screenWidth;
    /**
     * 屏幕高度
     */
    public static int screenHeight;


    public boolean isInLiveRoomActivity = false;
    /**
     * 缓存最热房间列表，切换房间用
     */
    public List<Room> roomList;

    public List<Room> findRoomList;

    /**
     * 缓存最热礼物列表
     */
    public List<GiftInfo> mGiftList;

    private boolean isBannerCancle = false;

    public boolean isShowGiftAnim = true;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onCreate() {
        super.onCreate();

        // 设置播放内核 ExoPlayer
        Jzvd.setMediaInterface(new JZExoPlayer());

        if (Build.DEVICE.equals("8675-HD")) {
            isShowGiftAnim = false;
        }

        //测试
        initLogger();

        FrameWork.init(this);
        ResourceHelper.init(getApplicationContext());
        HardwareUtil.initialize(getApplicationContext());
        String processName = getProcessName();
        RuntimeCheck.init(processName);

        if (RuntimeCheck.isRemoteProcess()) {
            return;
        }


        if (RuntimeCheck.isToolProcess()) {
            ToolProcessPreference.init(this);
            return;
        }

        application = this;

//        LeakCanary.install(this);//测试不监控
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//             This process is dedicated to LeakCanary for heap analysis.
//             You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
        if (RuntimeCheck.isUiProcess()) {
            initImageLoader();
            initApp();
            initBDMap();
            initNetWork();

            initAvSdk();
            intJpush();
//            initBlockCanary();

//            if (com.laka.live.BuildConfig.DEBUG) {
//                LeakCanary.install(this);
//            }
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(getApplicationContext());
            initAnalyticsReport();
            UpdateCheckManager.getInstance().setIgnoreUpdateNow(false);

            MusicManager.getInstance();//初始化

            if (com.laka.live.BuildConfig.DEBUG) {
                initStrictMode();
            }

            registerActivityLifecycleCallbacks(ActivityLifecycleCb.getInstance());

            chechForHotfix();

            ZegoApiManager.getInstance().initSDK();

            initBuglySdk();

            HttpManager.init(this);

            FrescoHelper.initFresco(getApplicationContext());
            // ImageLoader初始化
            ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(this)
                    .threadPoolSize(15)
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                    .memoryCacheSize(1500000)
                    .defaultDisplayImageOptions(ImageUtil.options)// 设置默认图
                    .denyCacheImageMultipleSizesInMemory()
                    .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                    .build());

            // 商城需要的工具类
            SettingFlags.init(this);
            SystemHelper.init(this);

            Unicorn.init(this, "b201c5dc989ec61bfa0f802be39c53e1", getUnicornOptions(), new ImageUtil());
        }

    }

    // 如果返回值为null，则全部使用默认参数。
    private YSFOptions getUnicornOptions() {

        YSFOptions options = new YSFOptions();
        options.onMessageItemClickListener = new OnMessageItemClickListener() {
            public void onURLClicked(Context context, String url) {
                // 自定义处理，带URL链接的消息的点击事件。
                UnicornHelper.onUrlClicked(context, url);
            }
        };
        options.statusBarNotificationConfig = new StatusBarNotificationConfig();
        return options;
    }

    private void initBuglySdk() {
        CrashReport.initCrashReport(getApplicationContext(), "c79695fb14", com.laka.live.BuildConfig.DEBUG ? true : false);

    }

    private void initStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                /*.detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()*/   // or .detectAll() for all detectable problems
                .detectAll()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
//                .penaltyDeath()
                .build());
    }

    private void chechForHotfix() {
        HotfixUtil.initHotFixManager(this);
        HotfixUtil.queryNewHotPatch();
    }

    private void initAvSdk() {
        mQavsdkControl = new QavsdkControl();

    }

//    private void initBlockCanary() {
//        if (com.laka.live.BuildConfig.DEBUG) {
//            BlockCanary.install(this, new BlockCanaryContext()).start();
//        }
//    }

    public void initHardwareUtils(Context context) {
        final Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        HardwareUtil.screenWidth = display.getWidth();
        HardwareUtil.screenHeight = display.getHeight();
    }


    private void initNetWork() {
        mQueue = Volley.newRequestQueue(this);
        VolleyLog.DEBUG = com.laka.live.BuildConfig.DEBUG;

        GsonHttpConnection.getInstance();
    }

    private void initApp() {
        UiPreference.init(this);
    }

    /**
     * 初始化ImageLoader
     */
    public void initImageLoader() {

        ImagePipelineConfig.Builder builder = ImagePipelineConfig.newBuilder(this);
        if (BuildConfig.DEBUG) {
            Set<RequestListener> requestListeners = new HashSet<>();
            requestListeners.add(new RequestLoggingListener());
            builder.setRequestListeners(requestListeners);
            Fresco.initialize(this, builder.build());
            FLog.setMinimumLoggingLevel(FLog.VERBOSE);
        } else {
            Fresco.initialize(this, builder.build());
        }
    }


    public String getProcessName() {
        File cmdFile = new File("/proc/self/cmdline");

        if (cmdFile.exists() && !cmdFile.isDirectory()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(cmdFile)));
                String procName = reader.readLine();

                if (!TextUtils.isEmpty(procName))
                    return procName.trim();
            } catch (Exception e) {
                Log.error(TAG, e.getMessage());
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e) {
                        Log.error(TAG, e.getMessage());
                    }
                }
            }
        }
        return getApplicationInfo().processName;
    }

    public static LiveApplication getInstance() {
        return application;
    }

    public void exitApp(Activity currentActivity) {
        LiveActivityManager.getInstance().exitApp(currentActivity);
    }

    public void killAllProcess(Activity curentActivity) {
        exitApp(curentActivity);
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> apps = manager.getRunningAppProcesses();
        int currentPid = android.os.Process.myPid();
        String toolProcessNmae = getPackageName() + ":" + RuntimeCheck.LIVE_TOOL_PROCESSNAME;
        String remoteProcessNmae = getPackageName() + ":" + RuntimeCheck.LIVE_REMOTE_PROCESSNAME;
        ArrayList<String> processNames = new ArrayList<>(2);
        processNames.add(toolProcessNmae);
        processNames.add(remoteProcessNmae);
        for (ActivityManager.RunningAppProcessInfo app : apps) {
            if (app != null && app.pid != currentPid) {
                for (String processName : processNames) {
                    if (TextUtils.equals(processName, app.processName)) {
                        Process.killProcess(app.pid);
                        processNames.remove(processName);
                        break;
                    }
                }
                if (processNames.isEmpty()) {
                    break;
                }
            }
        }
        Process.killProcess(currentPid);
    }

    // 极光推送
    private void intJpush() {
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(this);

        //设置标签
        if (BuildConfig.DEBUG) {
            Set<String> tags = new HashSet<>();
            tags.add("test");
            JPushInterface.setTags(this, tags, new TagAliasCallback() {
                @Override
                public void gotResult(int i, String s, Set<String> set) {
                    Log.d(TAG, "setTags . set = " + set.toString() + " ;result = " + i);
                }
            });
        }
    }

    public List<Activity> getActivitys() {
        return LiveActivityManager.getInstance().getActivitys();
    }

    // 初始化友盟统计
    private void initAnalyticsReport() {
        AnalyticsReport.init(this);
    }


    //应用退到前台
    public void applicationDidEnterBackground() {
        Log.d(TAG, "应用退到后台");
//        RoomManager roomManger = RoomManager.getInstance(LiveApplication.this);
//        if (roomManger.isConnected()) {
//            handler.removeCallbacks(closeSocket);
//            //开启定时器
//            handler.postDelayed(closeSocket, 1000 * 30);
//        }

//        FloatWindow.getInstance().goBackground();
    }


    //应用回到前台
    public void applicationDidEnterForeground() {
        Log.d(TAG, "应用回到前台，连接socket");
        RoomManager roomManger = RoomManager.getInstance();
        roomManger.startRoom();

//        if(FloatWindow.getInstance().isShowing())
//        FloatWindow.getInstance().showFloatWindow();
        FloatWindow.getInstance().addRootView();
        EventBusManager.postEvent(0, SubcriberTag.RE_SHOW_FLOAT_LIVE);
    }


    //百度定位开始

    public LocationClient mLocationClient = null;

    public BDLocationListener myListener;

    public GetLocationListener getLocationListener;

    public void setGetLocationListener(GetLocationListener listener) {
        this.getLocationListener = listener;
        if (mLocationClient != null) {
            mLocationClient.start();
            mLocationClient.requestLocation();
        }
    }

    public void stopGetLocation() {
        if (mLocationClient != null) {
            mLocationClient.stop();
            mLocationClient.requestLocation();
        }
    }

    QavsdkControl mQavsdkControl;

    public QavsdkControl getQavsdkControl() {
        return mQavsdkControl;
    }

    public interface GetLocationListener {
        void onReceiveLocation(BDLocation location);
    }

    ;

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            LogUtil.d(TAG, "onReceiveLocation location");
            if (location == null) {
                LogUtil.d(TAG, "onReceiveLocation location== null");
                return;
            }

            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
            }
            curLocation = location;
            // 保存本地地址
            String sLocation = LakaUtil.getFullPlace(curLocation);
            if (!Utils.isEmpty(sLocation)) {
                LogUtil.d(TAG, "保存本地地址=" + sLocation);
                UiPreference.putString(Common.KEY_MY_LOCATION_CITY, sLocation);
            }
            // 保存详细地址、省份，城市、经纬度等
//            try {
//                UiPreference.putString(Keys.LOCATION_ADDR, location.getAddrStr());
//                UiPreference.putString(Keys.PROVINCE, location.getProvince());
//                UiPreference.putString(Keys.CITY, location.getCity());
//                UiPreference.putString(Keys.LONGITUDE,
//                        String.valueOf(location.getLongitude()));
//                UiPreference.putString(Keys.LATITUDE,
//                        String.valueOf(location.getLatitude()));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            if (getLocationListener != null) {
                getLocationListener.onReceiveLocation(getCurLocation());
            }
            LogUtil.d(TAG, "定位=" + sb.toString() + " errorcode=" + location.getLocType());

            // 有结果停止监听
            mLocationClient.stop();
        }
    }

    BDLocation curLocation;

    public BDLocation getCurLocation() {
        return curLocation;
    }

    public void initBDMap() {
        // SDKInitializer.initialize(getApplicationContext());
        mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
        myListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myListener); // 注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        // option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        // option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        mLocationClient.requestLocation();
        LogUtil.d(TAG, "initBDMap");
    }

    //百度定位结束

    private void initLogger() {
        Logger.init("Rayman").setMethodCount(5);
    }
}
