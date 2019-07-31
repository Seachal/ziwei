package com.laka.live.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.account.follow.FollowRequestHelper;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.application.LiveApplication;
import com.laka.live.bean.Course;
import com.laka.live.bean.ListUserInfo;
import com.laka.live.bean.RankingUserInfo;
import com.laka.live.bean.UserInfo;
import com.laka.live.dao.DbManger;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.Msg;
import com.laka.live.msg.UserMsg;
import com.laka.live.msg.VideosListMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.adapter.MyReplayAdapter;
import com.laka.live.ui.chat.ChatMessageActivity;
import com.laka.live.ui.course.detail.CourseDetailActivity;
import com.laka.live.ui.widget.AlphaTextView;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.ui.widget.UserInfoHeader;
import com.laka.live.ui.widget.dialog.GenericDialog;
import com.laka.live.ui.widget.dialog.IDialogOnClickListener;
import com.laka.live.util.Common;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;
import com.laka.live.video.constant.VideoEventConstant;
import com.laka.live.video.model.event.MiniVideoEvent;
import com.laka.live.video.model.event.VideoDecorViewEvent;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * @Author:summer
 * @Date:2018/12/7
 * @Description: 用户详情页面
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener, PageListLayout.OnRequestCallBack {
    private static final String TAG = "UserInfoActivity";
//    private static final int MAX_BLUR_RELOAD_COUNT = 1;
//    private static final int BLUR_RADIUS = 20;

    private int mCurrentType = Course.LIVE;

    private static final String LIVE = "live";
    private static final String FOLLOW = "follow";
    private static final String FANS = "fans";
    public static final String EXTRA_USER_ID = "EXTRA_USER_ID";
    public static final String ENABLE_VIEW_MODE = "ENABLE_VIEW_MODE";
    private static final String EXTRA_USER_INFO = "EXTRA_USER_INFO";

    private static final int HEAD_BG_HEIGHT = Utils.dp2px(LiveApplication.getInstance().getApplicationContext(), 288f);
    private static final int BACK_HEIGHT = Utils.dp2px(LiveApplication.getInstance().getApplicationContext(), 45f);
    private static final int HEAD_TO_BACK = HEAD_BG_HEIGHT - BACK_HEIGHT;
    private static final int LIMIT = 20;

    private TextView mName;

    private UserInfo mUserInfo;

    private LinearLayout mOperation;
    private TextView mFollowItem;
    private TextView mBlackItem;

    //private TextView mLetterItem ;

    private FrameLayout mFollow;
    private FrameLayout mBlack;
    private FrameLayout mLetter;

    private View mBackBg;
    private AlphaTextView mBackIcon;
    private View mDivider;
    //private View mErrorView;
    //private TextView mErrorText;
    //private MusicLoading mLoadingView;
    //private View mDataView;

    private PageListLayout mPageListLayout;
    private UserInfoHeader mUserInfoHeader;
    private MyReplayAdapter myReplayAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

//    private PhotoPreviewPanel mPhotoPreviewPanel;
//    private int mReloadBlurImageCount;
//    private Bitmap mBlurBitmap = null;

    private String mUserId;

    /**
     * description:是否启用View模式（不带手势finish和头部返回）
     **/
    private boolean enableViewMode = false;

    private int scrollHeight = 0;

    private boolean mIsUserInfoComplete = false;

    private FollowRequestHelper mFollowRequestHelper;

    /**
     * description:数据对象设置
     **/
    private List<View> mContainerChildViews;

    public static void startActivity(Context activity, String userId) {
        if (activity != null) {
            Intent intent = new Intent(activity, UserInfoActivity.class);
            intent.putExtra(EXTRA_USER_ID, userId);
//            ActivityCompat.startActivity(activity, intent, null);
            activity.startActivity(intent);
        }
    }

    public static void startActivity(Activity activity, ListUserInfo userInfo) {
        startActivity(activity, tryChangeListUserInfoToUserInfo(userInfo));
    }

    /**
     * 建议使用该方法 在调用该方法前构造UserInfo对象（转换逻辑应该在外部完成）
     *
     * @param activity preActivity
     * @param userInfo userInfo
     */
    public static void startActivity(Activity activity, UserInfo userInfo) {
        if (activity != null) {
            Intent intent = new Intent(activity, UserInfoActivity.class);
            intent.putExtra(EXTRA_USER_ID, userInfo.getIdStr());
            intent.putExtra(EXTRA_USER_INFO, userInfo);
            ActivityCompat.startActivity(activity, intent, null);
        }
    }

    public static UserInfo tryChangeListUserInfoToUserInfo(ListUserInfo listUserInfo) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(listUserInfo.getId());
        userInfo.setNickName(listUserInfo.getNickName());
        userInfo.setAvatar(listUserInfo.getAvatar());
        userInfo.setVerified(listUserInfo.getVerified());
        userInfo.setStarVerified(listUserInfo.getStarVerified());
        userInfo.setApplyVerified(listUserInfo.getApplyVerified());
        userInfo.setGender(listUserInfo.getGender());
        userInfo.setLevel(listUserInfo.getLevel());
        return userInfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        mFollowRequestHelper = new FollowRequestHelper();
        mFollowRequestHelper.setAutoToastFailTips(true);
        initIntentData();
        initView();
        tryRequestUserInfo();

    }

    private void initIntentData() {
        Intent intent = getIntent();
        mUserId = intent.getStringExtra(EXTRA_USER_ID);
        mUserInfo = (UserInfo) intent.getSerializableExtra(EXTRA_USER_INFO);
        enableViewMode = intent.getBooleanExtra(ENABLE_VIEW_MODE, false);
        if (enableViewMode) {
            setSwipeBackEnable(false);
        }
        if (mUserInfo != null) {
            mUserId = mUserInfo.getIdStr();
        }
    }

    private void initView() {

        initPageList();
        mOperation = (LinearLayout) findViewById(R.id.operation);
        mFollow = (FrameLayout) findViewById(R.id.follow);
        mBlack = (FrameLayout) findViewById(R.id.black);
        mLetter = (FrameLayout) findViewById(R.id.msg_layout);
        mBlackItem = (TextView) findViewById(R.id.black_item);
        mFollowItem = (TextView) findViewById(R.id.follow_item);
        mOperation.setVisibility(View.GONE);
        mName = (TextView) findViewById(R.id.name);
        mBlack.setOnClickListener(this);
        mFollow.setOnClickListener(this);
        mLetter.setOnClickListener(this);
        mBackBg = findViewById(R.id.back_bg);
        mBackIcon = (AlphaTextView) findViewById(R.id.back_icon);
        if (!enableViewMode) {
            mBackIcon.setVisibility(View.VISIBLE);
            mBackIcon.setOnClickListener(this);
        } else {
            mBackIcon.setVisibility(View.GONE);
        }
        mDivider = findViewById(R.id.divider);
    }

    private void initPageList() {

        mPageListLayout = (PageListLayout) findViewById(R.id.data_layout);
        mPageListLayout.setShowRefreshing(false);
        mPageListLayout.setLayoutManager(new LinearLayoutManager(this));

        mUserInfoHeader = new UserInfoHeader(this);
        if (!enableViewMode) {
            mUserInfoHeader.showBack();
        } else {
            mUserInfoHeader.hideSetting();
        }
//        mUserInfoHeader.hideShare();
        myReplayAdapter = new MyReplayAdapter(this, TextUtils.equals(mUserId,
                AccountInfoManager.getInstance().getCurrentAccountUserIdStr()), mUserInfoHeader);
        mPageListLayout.setAdapter(myReplayAdapter);
        mPageListLayout.setLoadMoreCount(LIMIT);
        mPageListLayout.setEnableRefresh(false);
        mPageListLayout.setShowEmpty(false);
        mPageListLayout.setOnResultListener(new PageListLayout.OnResultListener() {
            @Override
            public void onResult(Object o) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myReplayAdapter.showLoading(false);
                        mPageListLayout.showData();
                    }
                });
            }
        });

        mPageListLayout.setOnRequestCallBack(this);

        mPageListLayout.addOnScrollListener(new PullToRefreshRecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //int offSet = recyclerView.computeVerticalScrollOffset();
                scrollHeight = scrollHeight + dy;
                if (scrollHeight >= HEAD_BG_HEIGHT) {
                    mBackIcon.setTextColor(getResources().getColor(R.color.color333333));
                    mBackBg.setAlpha(1f);
                    mName.setAlpha(1f);
                    mDivider.setVisibility(View.VISIBLE);
                    mName.setVisibility(View.VISIBLE);
                } else if (scrollHeight <= HEAD_TO_BACK) {
                    mBackIcon.setTextColor(getResources().getColor(R.color.white));
                    mBackIcon.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.title_icon_back02_selector), null, null, null);
                    mBackBg.setAlpha(0f);
                    mDivider.setVisibility(View.INVISIBLE);
                    mName.setVisibility(View.GONE);
                } else {
                    mBackIcon.setTextColor(getResources().getColor(R.color.color333333));
                    mBackIcon.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.title_icon_back_selector), null, null, null);
                    float alpha = 1.0f - (HEAD_BG_HEIGHT - scrollHeight) * 1.0f / BACK_HEIGHT;
                    mBackBg.setAlpha(alpha);
                    mName.setAlpha(alpha);
                    mDivider.setVisibility(View.VISIBLE);
                    mName.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScroll(RecyclerView recyclerView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {

        if (!mIsUserInfoComplete) {
            tryRequestUserInfo();
            return null;
        }
        Log.d(TAG, " request page=" + page);

        if (page == 0) {
            myReplayAdapter.clear();
            myReplayAdapter.notifyDataSetChanged();
        }

        if (mCurrentType == Course.NEWS) {
            return DataProvider.queryMyNews(this, mUserId, String.valueOf(page), listener);
        } else {
            return DataProvider.queryMyLive(this, mUserId, String.valueOf(mCurrentType), String.valueOf(page), String.valueOf(1), listener);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.follow:
                AnalyticsReport.onEvent(this, AnalyticsReport.OTHER_USER_INFO_ACTIVITY_FOLLOW_BUTTON_CLICK_EVENT_ID);
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                handleOnFollowButtonClick();
                break;
            case R.id.back_icon:
                finish();
                break;
//            case R.id.iv_share:
//                shareUser();
//                break;
            case R.id.msg_layout:
                AnalyticsReport.onEvent(this, AnalyticsReport.OTHER_USER_INFO_ACTIVITY_LETTER_BUTTON_CLICK_EVENT_ID);
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                handleOnLetterButtonClick();

                break;
            case R.id.black:
                AnalyticsReport.onEvent(this, AnalyticsReport.OTHER_USER_INFO_ACTIVITY_BLOCK_BUTTON_CLICK_EVENT_ID);
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                handleOnBlockButtonClick();
                break;
            default:
                break;
        }
    }

    private void shareUser() {
        if (mUserInfo == null) {
            return;
        }
        showShareDialog(Common.SHARE_USER_URL + mUserInfo.getId(), "[" + mUserInfo.getNickName() + "]邀请您来看直播啦！|滋味Live", "发现有滋有味的生活", mUserInfo.getAvatar(), true);
    }

    private void updateUserInfo() {
        if (mUserInfo == null) {
            return;
        }
        mName.setText(mUserInfo.getNickName());
        mUserInfoHeader.updateUserInfo(mUserInfo);
        updateFollowState(mUserInfo.isFollow());
        updateBlock(mUserInfo.isBlock());
    }

    private void updateBlock(boolean isBlack) {
        mBlackItem.setText(isBlack ? "已拉黑" : "拉黑");
        if (isBlack) {
            mBlackItem.setTextColor(ResourceHelper.getColor(R.color.color999999));
            mBlackItem.setCompoundDrawablesWithIntrinsicBounds(
                    ResourceHelper.getDrawable(R.drawable.user_icon_blacklist2_selector), null, null, null);
        } else {
            mBlackItem.setTextColor(ResourceHelper.getColor(R.color.color333333));
            mBlackItem.setCompoundDrawablesWithIntrinsicBounds(
                    ResourceHelper.getDrawable(R.drawable.user_icon_blacklist_selector), null, null, null);
        }
    }

    private void updateFollowState(boolean isFollow) {
        mFollowItem.setText(isFollow ? R.string.has_follow : R.string.follow);
        if (isFollow) {
            mFollowItem.setTextColor(ResourceHelper.getColor(R.color.color999999));
            mFollowItem.setCompoundDrawablesWithIntrinsicBounds(
                    ResourceHelper.getDrawable(R.drawable.user_icon_follow2_selector), null, null, null);
        } else {
            mFollowItem.setTextColor(ResourceHelper.getColor(R.color.color333333));
            mFollowItem.setCompoundDrawablesWithIntrinsicBounds(
                    ResourceHelper.getDrawable(R.drawable.user_icon_follow_selector), null, null, null);
        }
    }

    // 获取用户信息
    private void tryRequestUserInfo() {
        mOperation.setVisibility(View.GONE);
        mPageListLayout.showLoading();
        mIsUserInfoComplete = false;
        DataProvider.getUserInfo(this, mUserId, true, true, false, new GsonHttpConnection.OnResultListener<UserMsg>() {
            @Override
            public void onSuccess(UserMsg userMsg) {

                if (userMsg.isSuccessFul()) {
                    mUserInfo = userMsg.getUserInfo();
                    updateUserInfo();
                    mIsUserInfoComplete = true;
                    mOperation.setVisibility(View.VISIBLE);
                    mPageListLayout.loadData();
                    mPageListLayout.showData();
                } else {
                    mPageListLayout.showEmpty();
                }
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                Log.log("onFail:" + errorMsg);
                if (errorCode == Msg.TLV_USER_NOT_EXIST) {
                    mPageListLayout.showEmpty();
                } else {
                    if (errorCode == Msg.NETWORK_ERROR_NETWORK_ERROR) {
                        mPageListLayout.showNetWorkError();
                    } else {
                        mPageListLayout.showDataExcaption();
                    }
                }
            }
        });
    }

    private void handleOnReplayItemClick(Course item) {
        if (item == null) {
            return;
        }
        CourseDetailActivity.startActivity(this, item.getId());

    }

    private void handleOnLetterButtonClick() {
        if (mUserInfo == null) {
            return;
        }
        if (mUserInfo.getIdStr().equals(AccountInfoManager.getInstance().getCurrentAccountUserIdStr())) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.chat_with_self_error_tip));
            return;
        }
        ChatMessageActivity.startPrivateChatActivity(this,
                mUserInfo.getIdStr(),
                mUserInfo.getNickName(),
                mUserInfo.getAvatar(),
                DbManger.SESSION_TYPE_UNFOLLOW);

        Log.d(TAG, "ChatMessageActivity.startActivity  mUserInfo.getIdStr()=" + mUserInfo.getIdStr());
    }

    private void handleOnFollowButtonClick() {
        if (mUserInfo == null) {
            return;
        }
        if (mUserInfo.getIdStr().equals(AccountInfoManager.getInstance().getCurrentAccountUserIdStr())) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.follow_self_error_tip));
            return;
        }
        showLoadingDialog();
        mFollowRequestHelper.startRequest(this, mUserInfo.getId(), mUserInfo.isFollow(), mFollowRequestCallback);
    }

    private void handleOnBlockButtonClick() {
        if (mUserInfo == null) {
            return;
        }

        if (mUserInfo.getId() == AccountInfoManager.getInstance().getCurrentAccountUserId()) {
            showToast(R.string.can_not_block_myself);
            return;
        }

        if (mUserInfo.isBlock()) {
            unBlock();
            return;
        }
        String userName = ResourceHelper.getString(R.string.user_info_pull_block_default_name);
        if (StringUtils.isNotEmpty(mUserInfo.getNickName())) {
            userName = mUserInfo.getNickName();
        }
        String tipContent = ResourceHelper.getString(R.string.user_info_pull_block_tip_content);
        if (StringUtils.isNotEmpty(tipContent)) {
            tipContent = tipContent.replace("#user#", userName);
        }

        showButtonDialog(ResourceHelper.getString(R.string.user_info_pull_block_tip), tipContent, R.string.yes, R.string.cancel,
                true, true, true, false, new IDialogOnClickListener() {
                    @Override
                    public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                        if (viewId == GenericDialog.ID_BUTTON_YES) {
                            block();
                        }
                        return false;
                    }
                });
//        SimpleTextDialog dialog = new SimpleTextDialog(this);
//        dialog.setTitle(ResourceHelper.getString(R.string.user_info_pull_block_tip));
//        dialog.setText(tipContent);
//        dialog.addYesNoButton();
//        dialog.setCancelable(true);
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.setRecommendButton(GenericDialog.ID_BUTTON_NO);
//        dialog.setOnClickListener(new IDialogOnClickListener() {
//            @Override
//            public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
//                if (viewId == GenericDialog.ID_BUTTON_YES) {
//                    block();
//                }
//                return false;
//            }
//        });
//        dialog.show();
    }

    private void tryNotifyDataChangedWhenFollow() {
        RankingUserInfo userInfo = new RankingUserInfo();
        userInfo.setId(mUserInfo.getId());
        userInfo.setFollow(mUserInfo.getFollow());
        EventBusManager.postEvent(userInfo, SubcriberTag.REFRESH_RANKING_LIST_DATA);
    }

    private FollowRequestHelper.FollowRequestCallback mFollowRequestCallback = new FollowRequestHelper.FollowRequestCallback() {
        @Override
        public void requestSuccess(boolean isCancelFollow) {
            dismissLoadingsDialog();
            mUserInfo.setFollow(isCancelFollow ? ListUserInfo.NO_FOLLOW : ListUserInfo.FOLLOWED);
            updateFollowState(isCancelFollow == false);
            tryNotifyDataChangedWhenFollow();
        }

        @Override
        public void requestFailed(boolean isCancelFollow, int errorCode, String errorMsg) {
            dismissLoadingsDialog();
        }
    };

    private void block() {
        DataProvider.shield(this, mUserId, mBlocklistener);
        showLoadingDialog();
    }

    private void unBlock() {
        DataProvider.unShield(this, mUserId, mUnblocklistener);
        showLoadingDialog();
    }

    private GsonHttpConnection.OnResultListener<Msg> mBlocklistener = new GsonHttpConnection.OnResultListener<Msg>() {
        @Override
        public void onSuccess(Msg msg) {
            dismissLoadingsDialog();
            mUserInfo.setIsBlock(true);
            updateBlock(true);
            showToast(R.string.shield_success);
        }

        @Override
        public void onFail(int errorCode, String errorMsg, String command) {
            dismissLoadingsDialog();
            showToast(R.string.shield_fail);
        }
    };

    private GsonHttpConnection.OnResultListener<Msg> mUnblocklistener = new GsonHttpConnection.OnResultListener<Msg>() {
        @Override
        public void onSuccess(Msg msg) {
            dismissLoadingsDialog();
            mUserInfo.setIsBlock(false);
            updateBlock(false);
            showToast(R.string.cancel_shield_success);
        }

        @Override
        public void onFail(int errorCode, String errorMsg, String command) {
            dismissLoadingsDialog();
            showToast(R.string.cancel_shield_fail);
        }
    };

    private GsonHttpConnection.OnResultListener<VideosListMsg> mReplayListener = new GsonHttpConnection.OnResultListener<VideosListMsg>() {
        @Override
        public void onSuccess(VideosListMsg o) {
            mOperation.setVisibility(View.VISIBLE);
        }

        @Override
        public void onFail(int errorCode, String errorMsg, String command) {
            mOperation.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsReport.onEvent(this, AnalyticsReport.OTHER_USER_INFO_ACTIVITY_SHOW_EVENT_ID);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String id = intent.getStringExtra(EXTRA_USER_ID);
        UserInfo userInfo = (UserInfo) intent.getSerializableExtra(EXTRA_USER_INFO);
        if (userInfo != null) {
            id = userInfo.getIdStr();
        }
        if (TextUtils.equals(id, mUserId) == false) {
            mUserId = id;
            mUserInfo = userInfo;
            tryRequestUserInfo();
        }

    }

    @Override
    public void onEvent(PostEvent event) {
        super.onEvent(event);
        if (SubcriberTag.USERINFO_TAB_CHANGE.equals(event.getTag())) {
            int type = (int) event.event;
            if (!mPageListLayout.isCompeleted()) {
                mPageListLayout.setCompeleted(true);
            }

            mCurrentType = type;
            if (mUserInfoHeader != null) {
                mUserInfoHeader.setTabEnable(type);
            }
            mPageListLayout.loadData(true, false);
            myReplayAdapter.setCurTab(type);

        } else if (SubcriberTag.USERINFO_GO_LOGIN.equals(event.getTag())) {
            goLogin();
        } else if (SubcriberTag.SHARE_USER_INFO.equals(event.getTag())) {
            shareUser();
        } else if (VideoEventConstant.UPDATE_CONTAINER_VIEW.equals(event.getTag())) {
            List<View> decorViews = (List<View>) event.getEvent();
            if (decorViews.contains(getWindow().getDecorView())) {
                //更新Container变量
                mContainerChildViews = decorViews;
            }
        }

        MiniVideoEvent miniVideoEvent = null;
        if (event.getEvent() instanceof MiniVideoEvent) {
            miniVideoEvent = (MiniVideoEvent) event.getEvent();
            if (miniVideoEvent.getTargetObj() != null) {
                if (miniVideoEvent.getTargetObj() instanceof VideoDecorViewEvent) {
                    VideoDecorViewEvent decorViewEvent = (VideoDecorViewEvent) miniVideoEvent.getTargetObj();
                    handleTargetEvent(decorViewEvent.getDecorViews(),
                            miniVideoEvent.getTag(),
                            miniVideoEvent.getEvent() + "");
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Logger.e("UserInfoActivity-----onBackPressed");
        //存在两种情况，一种是从小视频页面包含的，从页面包含的情况下，不响应backPressed，传递给Container处理
        if (Utils.isEmpty(mContainerChildViews)) {
            super.onBackPressed();
        } else {
            EventBusManager.postEvent(VideoEventConstant.BACK_TO_MINI_PLAY);
        }
    }

    /**
     * 处理生命周期（VideoContainerActivity发出）
     *
     * @param decorViews VideoContainer持有的Views，用于判断对象
     * @param tag        事件对象
     */
    private void handleTargetEvent(List<View> decorViews, String tag, String userId) {
        if (!decorViews.contains(getWindow().getDecorView())) {
            return;
        }
        Logger.e("UserInfoActivity-------处理生命周期和列表切换事件：" + decorViews
                + "\ncurrent decorView:" + getWindow().getDecorView()
                + "\ntag:" + tag);
        switch (tag) {
            case VideoEventConstant.UPDATE_RELATIVE_USER_INFO:
                mUserId = userId;
                tryRequestUserInfo();
                break;
            case VideoEventConstant.VIDEO_RESUME:
                onResume();
                break;
            case VideoEventConstant.VIDEO_PAUSE:
                onPause();
                break;
            case VideoEventConstant.VIDEO_DESTROY:
                finish();
                onDestroy();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
