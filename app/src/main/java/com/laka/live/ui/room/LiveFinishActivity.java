package com.laka.live.ui.room;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.account.my.MyActivity;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.application.LiveApplication;
import com.laka.live.bean.ListUserInfo;
import com.laka.live.bean.Room;
import com.laka.live.bean.UserInfo;
import com.laka.live.config.SystemConfig;
import com.laka.live.help.PostEvent;
import com.laka.live.manager.RoomManager;
import com.laka.live.msg.Msg;
import com.laka.live.msg.RoomRecvMsg;
import com.laka.live.msg.UserMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.DataProviderRoom;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.activity.FriendsLiveActivity;
import com.laka.live.ui.activity.MainActivity;
import com.laka.live.ui.chat.ChatHomeActivity;
import com.laka.live.ui.widget.DoubleTextView;
import com.laka.live.ui.widget.danmu.IBitmapListener;
import com.laka.live.ui.widget.dialog.GenericDialog;
import com.laka.live.ui.widget.dialog.IDialogOnClickListener;
import com.laka.live.util.CacheUtil;
import com.laka.live.util.Common;
import com.laka.live.util.FastBlur;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.ShareUtil;
import com.laka.live.util.Utils;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by crazyguan on 2016/3/28.
 */
public class LiveFinishActivity extends BaseActivity implements View.OnClickListener {

    private final static String TAG = "LiveFinishActivity";
    private DoubleTextView views; // 多少人看过
    private DoubleTextView moneyCount; // 钻石数量
    private DoubleTextView durationDTV; //时长
    private DoubleTextView likeDTV;  // 点赞数

    private TextView tvName;
    //    private TextView tvTotalTime;
    private boolean isCreater = false;
    private Button btnChannal, btnGood;
    private boolean followed;
    private UserInfo curZhubo;

    private String mCourseId;
    private SimpleDraweeView ivFace;
    private TextView tvShareto;
    private LinearLayout llShare;
    private Button btnQQ, btnWeibo, btnMoment, btnWx, btnQzone;
    private String taskId;
    private int endType;
    private View mMarkView;
    private boolean mIsChangeChannel = false;

    public static void startActivity(Context context, boolean isCreater) {
        if (context != null) {
            Intent intent = new Intent(context, LiveFinishActivity.class);
            intent.putExtra("isCreater", isCreater);
            context.startActivity(intent);
        }
    }


    public static void startActivity(Context context, boolean isCreater, String taskId, int type,String mCourseId) {
        if (context != null) {
            Intent intent = new Intent(context, LiveFinishActivity.class);
            intent.putExtra("isCreater", isCreater);
            intent.putExtra("mCourseId",mCourseId);
            intent.putExtra("taskId", taskId);
            intent.putExtra("type", type);
            context.startActivity(intent);
        }
    }

    public static void startActivity(Context context, boolean isCreater, String taskId) {
        if (context != null) {
            startActivity(context, isCreater, taskId, 0,taskId);
        }
    }

    Handler handler = new Handler() {
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_finish);
        isCreater = getIntent().getBooleanExtra("isCreater", false);
        mCourseId = getIntent().getStringExtra("mCourseId");
        log("isCreater:" + isCreater);
        taskId = getIntent().getStringExtra("taskId");
        endType = getIntent().getIntExtra("type", 0);
        initView();
        Log.d(TAG, " onCreate");
    }

    int totalTime = 0;

    private void initView() {
//        share = new ShareUtil(this);
        btnQQ = (Button) findViewById(R.id.btn_share_qq);
        btnQQ.setOnClickListener(this);
        btnWeibo = (Button) findViewById(R.id.btn_share_sina);
        btnWeibo.setOnClickListener(this);
        btnWx = (Button) findViewById(R.id.btn_share_wx);
        btnWx.setOnClickListener(this);
        btnMoment = (Button) findViewById(R.id.btn_share_timeline);
        btnMoment.setOnClickListener(this);
        btnQzone = (Button) findViewById(R.id.btn_share_qzone);
        btnQzone.setOnClickListener(this);

        llShare = (LinearLayout) findViewById(R.id.ll_share);
        tvShareto = (TextView) findViewById(R.id.shareLive);
        ivFace = (SimpleDraweeView) findViewById(R.id.iv_face);
        tvName = (TextView) findViewById(R.id.tv_name);
        btnChannal = (Button) findViewById(R.id.btn_channal);
        btnChannal.setOnClickListener(this);
        btnChannal.setVisibility(View.GONE);
        btnGood = (Button) findViewById(R.id.btn_good);
        btnGood.setOnClickListener(this);
//        tvTotalTime = (TextView) findViewById(R.id.tv_total_time);

        views = findById(R.id.views);
        moneyCount = findById(R.id.money);
        durationDTV = findById(R.id.duration);
        likeDTV = findById(R.id.like);
        findViewById(R.id.btn_good).setOnClickListener(this);
        mMarkView = findViewById(R.id.mark);

        if (CacheUtil.containsKey("totalTime")) {
            totalTime = (int) CacheUtil.getCache("totalTime");
        }

        durationDTV.setCountText(Utils.secToFullTime(totalTime));
        views.setCountText(String.valueOf(0));
        moneyCount.setCountText(String.valueOf(0));
        likeDTV.setCountText(String.valueOf(0));


        if (CacheUtil.containsKey("curZhuboUserInfo")) {
            curZhubo = (UserInfo) CacheUtil.getCache("curZhuboUserInfo");
            if (Utils.isEmpty(curZhubo.getNickName())) {
                getZhuboInfo(curZhubo.getIdStr());

            } else {

            }
        }


        if (curZhubo != null) {
            int follow = curZhubo.getFollow();
            if (follow == ListUserInfo.FOLLOWED) {
                followed = true;
            }
        }

        refreshUI();

        if (isCreater) {
            btnChannal.setText(R.string.save_video);

            checkNeedSave();

            //后台配置不显示分享
            if (!SystemConfig.getInstance().isShowShare()) {
                llShare.setVisibility(View.GONE);
            }
            // 不需要保留视频
            btnChannal.setVisibility(View.GONE);
        } else {

            llShare.setVisibility(View.GONE);
            tvShareto.setVisibility(View.GONE);
            //换台列表移除刚结束的主播
            List<Room> roomList = LiveApplication.getInstance().roomList;
            if (!Utils.listIsNullOrEmpty(roomList) && curZhubo != null) {
                Log.d(TAG, " 移除前可换的台=" + roomList.size());
                for (int i = 0; i < roomList.size(); i++) {
                    Room item = roomList.get(i);
                    if (item.getId() == curZhubo.getId()) {
                        roomList.remove(i);
                        break;
                    }
                }
                Log.d(TAG, " 移除后可换的台=" + roomList.size());
            }

        }

        queryRoomRecv();

        if (endType == LiveRoomFragment.END_TYPE_FORBID) {
            showForbitLive();
        }

//        if (isCreater) {
//            saveVideo(1);
//        }

    }

    private void refreshUI() {
        Log.d(TAG, "刷新主播Ui isCreater=" + isCreater);
        if (!isCreater) {
            if (curZhubo != null) {
                tvName.setText(curZhubo.getNickName());
                ImageUtil.loadImage(ivFace, curZhubo.getAvatar());
//                setBlurBg(curZhubo.getAvatar());
            }

            if (!followed) {
                btnChannal.setText(R.string.follow);
            } else {
                btnChannal.setText(R.string.change_channal);
            }
        } else {
            tvName.setText(AccountInfoManager.getInstance().getCurrentAccountNickName());
            ImageUtil.loadImage(ivFace, AccountInfoManager.getInstance().getCurrentAccountUserAvatar());
//            setBlurBg(AccountInfoManager.getInstance().getCurrentAccountUserAvatar());
        }
    }

    private void getZhuboInfo(String mHostIdentifier) {
        DataProvider.getUserInfo(this, mHostIdentifier, String.valueOf(mHostIdentifier), true, new GsonHttpConnection.OnResultListener<UserMsg>() {
            @Override
            public void onSuccess(UserMsg userMsg) {
                if (userMsg.isSuccessFul()) {
                    UserInfo info = userMsg.getUserInfo();
                    if (info != null) {
                        curZhubo = info;
                        refreshUI();
                    } else {
                        Log.d(TAG, "主播信息为空");
                    }
                }
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {

            }
        });
    }

    private void setBlurBg(String avatar) {
        if (Utils.isEmpty(avatar)) {
            return;
        }
        Log.d(TAG, " setBlurBg width=" + Utils.getScreenWidth(LiveFinishActivity.this) + " height=" + Utils.getScreenHeight(LiveFinishActivity.this));
        ImageUtil.getBitmapByUrl(avatar, Utils.getScreenWidth(LiveFinishActivity.this), Utils.getScreenHeight(LiveFinishActivity.this), new IBitmapListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onSuccess(Bitmap bitmap) {
                Log.d(TAG, "背景 bitmap加载成功 width=" + bitmap.getWidth() + " height=" + bitmap.getHeight());
                if (bitmap != null) {
                    Bitmap blurBitmap = null;
                    if (bitmap.getWidth() < 200) {
                        blurBitmap = big(FastBlur.doBlur(bitmap, 90, true), Utils.getScreenWidth(LiveFinishActivity.this) * 2, Utils.getScreenWidth(LiveFinishActivity.this) * 2);
                    } else {
                        blurBitmap = FastBlur.doBlur(bitmap, 90, true);
                    }

                    final Bitmap finalBlurBitmap = blurBitmap;
                    if (isResume()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ImageView llbg = (ImageView) findViewById(R.id.iv_bg);
                                llbg.setImageBitmap(finalBlurBitmap);
                                mMarkView.setBackgroundResource(R.color.colorCC000000);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFail(int code) {
                Log.d(TAG, "背景 bitmap加载失败");
            }
        });
    }

    public static Bitmap big(Bitmap b, float x, float y) {
        int w = b.getWidth();
        int h = b.getHeight();
        float sx = (float) x / w;//要强制转换，不转换我的在这总是死掉。
        float sy = (float) y / h;
        Matrix matrix = new Matrix();
        matrix.postScale(sx, sy); // 长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(b, 0, 0, w,
                h, matrix, true);
        return resizeBmp;
    }


    private void checkNeedSave() {
        Log.d(TAG, " taskId=" + taskId + " totalTime=" + totalTime);
        if (Utils.isEmpty(taskId) || totalTime < 180) {//没有录像或少于3分钟都不保存
            btnChannal.setVisibility(View.GONE);
        } else {
            btnChannal.setVisibility(View.GONE);
        }
    }

    boolean isDataReady = false;

    private void queryRoomRecv() {
        if (curZhubo == null) {
            Log.d(TAG, "curZhubo==null");
            return;
        }

        DataProviderRoom.queryRoomRecv(this, curZhubo.getIdStr(), new GsonHttpConnection.OnResultListener<RoomRecvMsg>() {
            @Override
            public void onSuccess(RoomRecvMsg result) {
                if (result.isSuccessFul()) {
                    Log.d(TAG, "queryRoomRecv success views=" + result.getData().getViews() + " coins=" + result.getData().getRecv_coins()
                            + " duration=" + result.getData().getDuration() + ",likes:" + result.getData().getRecv_likes());
                    views.setCountText(String.valueOf(result.getData().getViews()));
                    moneyCount.setCountText(String.valueOf(RoomManager.translateKazuan(result.getData().getRecv_coins())));//需要转换
                    likeDTV.setCountText(String.valueOf(result.getData().getRecv_likes()));
                    int duration = result.getData().getDuration();
                    Log.d(TAG, " duration : " + duration);
                    totalTime = duration;
                    if (duration > 0) {
                        HashMap<String, String> parmas1 = new HashMap<>();
                        parmas1.put("Time", String.valueOf(duration));
                        AnalyticsReport.onEvent(LiveFinishActivity.this, LiveReport.MY_LIVE_EVENT_11227, parmas1);

                        isDataReady = true;
                        durationDTV.setCountText(Utils.secToFullTime(duration));
//                        tvTotalTime.setText("总时长  " + Utils.secToFullTime(duration));
                        if (isCreater) {
                            checkNeedSave();
                            HashMap<String, String> parmas = new HashMap<>();
                            parmas.put("Live_time", String.valueOf(duration));
//                            AnalyticsReport.onEvent(LiveFinishActivity.this, LiveReport.MY_LIVE_EVENT_11220, parmas);

                            HashMap<String, String> parmas2 = new HashMap<>();
                            parmas2.put("Live_id", curZhubo.getIdStr());
                            parmas2.put("Laka_id", AccountInfoManager.getInstance().getCurrentAccountUserIdStr());
                            parmas2.put("Viewer_count", views.getCountText());
                            parmas2.put("HaveConins_count", moneyCount.getCountText());
//                          parmas.put("Zan_count", mHostIdentifier);
                            AnalyticsReport.onEvent(LiveFinishActivity.this, LiveReport.MY_LIVE_EVENT_11228_e, parmas);

//                            AnalyticsReport.onEvent(LiveFinishActivity.this, LiveReport.MY_LIVE_EVENT_11221_e, parmas2);
                        }
                    } else {
//                         reQuery();
                    }
                } else {
                    Log.d(TAG, "queryRoomRecv fail");
                }
                reQuery();
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                reQuery();
                Log.d(TAG, "queryRoomRecv fail errorCode=" + " errorMsg=" + errorMsg);
            }
        });
    }

    int retryCount = 0;

    private void reQuery() {
        if (isResume() && !isDataReady && !isDestroyed()) {
            retryCount++;
            if (retryCount > 15) {
                return;
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    queryRoomRecv();
                }
            }, 1500);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, " onResume");
        if (isCreater) {
//            AnalyticsReport.onEvent(this, LiveReport.MY_LIVE_EVENT_11227);
        } else {
            AnalyticsReport.onEvent(this, LiveReport.MY_LIVE_EVENT_11250);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mIsChangeChannel) {
            clearCache();
        }

        Log.d(TAG, " onDestroy");
    }

    private void clearCache() {
        CacheUtil.removeCache("totalTime");
        CacheUtil.removeCache("totalCoins");
        CacheUtil.removeCache("totalAudience");
        CacheUtil.removeCache("curZhuboUserInfo");
        CacheUtil.removeCache("recordFiles");
        CacheUtil.removeCache("liveTitle");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_share_qq:
                shareEvent(4);
                share.shareUrl(LiveFinishActivity.this, share.getShareTitle(this), share.getShareContent(this), getShareLiveEndUrl(), curZhubo.getAvatar(), ShareUtil.QQ_PLATFORM);
                break;
            case R.id.btn_share_wx:
                shareEvent(1);
                share.shareUrl(LiveFinishActivity.this, share.getShareTitle(this), share.getShareContent(this), getShareLiveEndUrl(), curZhubo.getAvatar(), ShareUtil.WEIXIN_PLATFORM);
                break;
            case R.id.btn_share_sina:
                shareEvent(3);
                share.shareUrl(LiveFinishActivity.this, share.getShareTitle(this), share.getShareContent(this), getShareLiveEndUrl(), curZhubo.getAvatar(), ShareUtil.WEIBO_PLATFORM);
                break;
            case R.id.btn_share_qzone:
                shareEvent(5);
                share.shareUrl(LiveFinishActivity.this, share.getShareTitle(this), share.getShareContent(this), getShareLiveEndUrl(), curZhubo.getAvatar(), ShareUtil.QZONE_PLATFORM);
                break;
            case R.id.btn_share_timeline:
                shareEvent(2);
                share.shareUrl(LiveFinishActivity.this, share.getShareTitle(this), share.getShareContent(this), getShareLiveEndUrl(), curZhubo.getAvatar(), ShareUtil.FRIENDSHIP_PLATFORM);
                break;
            case R.id.btn_good:
                backToHome();
                //EventBusManager.postEvent(1, SubcriberTag.BACK_TO_HOMEPAGE);
                //finish();
                break;
            case R.id.btn_channal:
//                showToast("换台");
                if (isCreater) {
                    saveVideo(1);
                } else {
//    2	关注主播
//      3	换台
                    HashMap<String, String> parmas = new HashMap<>();
                    if (!followed) {
                        endButtonEvent(2);
                        doFollow();
                    } else {
                        endButtonEvent(3);
                        changeChannel();
                    }
                }
                break;
        }
    }


//    private String getShareTitle() {
////        String shareTitle = getIntent().getStringExtra("liveTitle");
////        if (Utils.isEmpty(shareTitle)) {
////            shareTitle = getResources().getString(R.string.i_in_laka_live);
////        }
//        String[] items = getResources().getStringArray(R.array.share_default_title);
//        int index = (int) Math.round(Math.random() * (items.length - 1));
//        Log.d(TAG," getShareTitle index="+index);
//        String  shareTitle = items[index];
//        return shareTitle;
//    }


    private String getShareLiveEndUrl() {
        String url = String.format(Common.SHARE_LIVE_END, curZhubo.getNickName(), curZhubo.getAvatar(), curZhubo.getCoins(), views.getCountText(), curZhubo.getFans());

        return url;
    }

    private void shareEvent(int shareType) {
        HashMap<String, String> parmas = new HashMap<>();
        parmas.put("Share_type", String.valueOf(shareType));
        AnalyticsReport.onEvent(this, LiveReport.MY_LIVE_EVENT_11253, parmas);
    }

    private void endButtonEvent(int type) {
        HashMap<String, String> parmas = new HashMap<>();
        parmas.put("EndButton_type", String.valueOf(type));
        AnalyticsReport.onEvent(this, LiveReport.MY_LIVE_EVENT_11251, parmas);

    }


    private void doFollow() {
        if (curZhubo == null) {
            return;
        }
        DataProvider.follow(this, curZhubo.getId(), new GsonHttpConnection.OnResultListener<Msg>() {
            @Override
            public void onSuccess(Msg msg) {
                curZhubo.setFollow(ListUserInfo.FOLLOWED);
                btnChannal.setText(R.string.change_channal);
                followed = true;
                showToast(R.string.follow_success);
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                if (errorCode == 19) {
                    curZhubo.setFollow(ListUserInfo.FOLLOWED);
                    btnChannal.setText(R.string.change_channal);
                    followed = true;
                    showToast(R.string.follow_success);
                } else {
                    showToast(R.string.follow_fail);
                }
            }
        });
    }

    private void saveVideo(int flag) {//1保存

        DataProviderRoom.setChannel(this, taskId, "1", new GsonHttpConnection.OnResultListener<Msg>() {

            @Override
            public void onSuccess(Msg msg) {
                if (msg.isSuccessFul()) {
                    showToast(R.string.save_video_success);
                } else {
                    showToast(R.string.save_video_fail);
                }

            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                showToast(R.string.save_video_fail);
                Log.d(TAG, "保存录像失败 errorCode=" + errorCode + " errorMsg=" + errorMsg);
            }
        });
//        if (Utils.isEmpty(fileIds)) {
//            Log.d(TAG, "没有录像文件");
//            showToast("没有可以保存的视频");
//            return;
//        }
//
//        DataProviderRoom.setQcloud(LiveFinishActivity.this, curZhubo.getIdStr(), fileIds, "1", new GsonHttpConnection.OnResultListener() {
//            @Override
//            public void onSuccess(Object o) {
//                Log.d(TAG, "同步录像成功");
//            }
//
//            @Override
//            public void onFail(int errorCode, String errorMsg, String command) {
//                Log.d(TAG, "同步录像失败 errorCode=" + errorCode + " errorMsg=" + errorMsg);
//            }
//        });
//        showToast("保存录像成功");
        finish();
    }

    private void changeChannel() {
        List<Room> roomList = LiveApplication.getInstance().roomList;
        if (!Utils.listIsNullOrEmpty(roomList)) {
            clearCache();
            mIsChangeChannel = true;
            int size = roomList.size() - 1;
            if (size > 0) {
                int random = new Random().nextInt(size);
                Log.d(TAG, "换台 size=" + size + " random=" + random);
                Room room = roomList.get(random);
                LiveRoomActivity.startPlay(this, room.getId(), false,
                        room.getTitle(), room.getStreamId(), room.getChannelId(),
                        room.getAvatar(), room.getVideoUrl(), Common.FROM_FINISH,mCourseId);
            } else {
                Log.d(TAG, "换台 size=" + size + " random=" + 0);
                Room room = roomList.get(0);
                LiveRoomActivity.startPlay(this, room.getId(), false,
                        room.getTitle(), room.getStreamId(), room.getChannelId(),
                        room.getAvatar(), room.getVideoUrl(), Common.FROM_FINISH,mCourseId);
            }
            finish();
        } else {
            showToast(R.string.none_change_channel);
        }
    }


    @Subscribe
    public void onEvent(PostEvent event) {
        super.onEvent(event);
//        if (SubcriberTag.STOP_RECORD_SUCCESS.equals(event.tag)) {
//            String fileId = (String) event.event;
//            Log.d(TAG, "收到 fileId=" + fileId);
//            fileIds = fileId;
//            checkNeedSave();
//        }
    }

//    @Subscriber(tag = SubcriberTag.STOP_RECORD_SUCCESS)
//    private void eventStopRecordSuccess(String fileId) {
//        Log.d(TAG,"收到 fileId="+fileId);
//        fileIds = fileId;
//    }

    private void showForbitLive() {
        showButtonDialog(R.string.wenxin_tips, R.string.you_forbid_live, R.string.yes, 0,
                false, false, true, true, new IDialogOnClickListener() {
                    @Override
                    public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                        return false;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backToHome();
    }

    // 返回主页
    private void backToHome() {
        activityManager.popUntilActivity(MyActivity.class, MainActivity.class,
                FriendsLiveActivity.class, ChatHomeActivity.class);
    }

}
