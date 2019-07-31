package com.laka.live.ui.course.detail;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.RadioGroup;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.Course;
import com.laka.live.bean.CourseDetail;
import com.laka.live.bean.CourseTrailer;
import com.laka.live.bean.UserInfo;
import com.laka.live.config.SystemConfig;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.CourseDetailMsg;
import com.laka.live.msg.Msg;
import com.laka.live.msg.PayCourseMsg;
import com.laka.live.msg.QueryRoomMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.course.PayCourseActivity;
import com.laka.live.ui.course.PostCourseActivity;
import com.laka.live.ui.room.LiveRoomActivity;
import com.laka.live.ui.room.SeeReplayActivity;
import com.laka.live.ui.widget.GradientScrollView;
import com.laka.live.ui.widget.chatKeyboard.KJChatKeyboardComment;
import com.laka.live.util.Common;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import framework.ioc.InjectUtil;
import framework.ioc.annotation.Inject;
import framework.ioc.annotation.InjectView;
import framework.utils.GsonTools;
import framework.utils.PermissionUtils;
import laka.live.bean.ChatMsg;

/**
 * Created by Lyf on 2017/8/30.
 * 点击事件统一在CourseDetailActivity里面处理(动态添加的列表的Item除外)
 */
public class CourseDetailHelper implements RadioGroup.OnCheckedChangeListener {


    @InjectView(id = R.id.titleBar, click = "")
    private TitleBarView mTitleBarView; // 标题
    @InjectView(id = R.id.scrollView)
    private GradientScrollView mScrollView; // 滚动条
    @InjectView(id = R.id.userInfoView)
    private UserInfoView mUserInfoView; // 主播信息
    @InjectView(id = R.id.courseTrailerView, click = "")
    private CourseTrailerView mCourseTrailerView; // 预告
    @InjectView(id = R.id.courseDetailView, click = "")
    private CourseDetailView mCourseDetailView; // 课程详情
    @InjectView(id = R.id.commentAreaView, click = "")
    private CommentAreaView mCommentAreaView; // 讨论区
    @InjectView(id = R.id.recommendGoodsView, click = "")
    private RecommendGoodsView recommendGoodsView; // 讨论区
    @InjectView(id = R.id.buttonStatusView)
    private ButtonStatusView mButtonStatusView; // 底部的按钮状态
    @InjectView(id = R.id.moduleGroup)
    private RadioGroup mModuleGroup;
    @InjectView(id = R.id.kjChatKeyboard)
    private KJChatKeyboardComment mChatKeyboard;

    private View mRootView; // 课程详情的layout
    private Course mCourse; // 当前课程的详情
    private CourseDetail mCourseDetail; // 包含课程和预告详情
    private CourseTrailer mCourseTrailer; // 预告详情
    private CourseDetailActivity mContext;

    private boolean isMine; // 是否是我自已的课
    private boolean isRefresh = false; // 是否是刷新数据
    private HashMap<String, String> mEventParams;

    @Inject
    public UserInfo mUserInfo; // 我的信息


    public CourseDetailHelper(CourseDetailActivity mContext) {
        this.mContext = mContext;
        this.mRootView = mContext.getWindow().getDecorView();
        InjectUtil.injectView(this, mRootView, true);  // 注入视图
    }

    // 点击事件(将点击事件分发给Activity)
    public void onClick(View view) {
        mContext.onClick(view);
    }

    // 获取数据
    public void getData(String courseId) {

        mContext.showNewDialog("正在加载中...");
        DataProvider.getCourseDetail(this, courseId, new GsonHttpConnection.OnResultListener<CourseDetailMsg>() {

            @Override
            public void onSuccess(CourseDetailMsg msg) {

                if (msg == null || msg.data == null || msg.data.course == null) {
                    mContext.showToast("课程数据异常!");
                    mContext.finish();
                } else {
                    initData(msg.data); // 绑定数据
                    mContext.dismissDialog();
                }
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                mContext.showToast(errorMsg);
                mContext.dismissDialog();
            }

        });

    }

    // 绑定数据
    private void initData(CourseDetail courseDetail) {

        mCourseDetail = courseDetail;
        mCourse = courseDetail.getCourse();
        mCourseTrailer = courseDetail.getCourseTrailer();
        isMine = (mCourseDetail.user.id == getUserInfo().getId());
        initView();
    }

    // 初始化视图
    private void initView() {

        if (isRefresh()) {
            queryLivingRoom();
        } else {
            mUserInfoView.initData(this);
            mTitleBarView.initData(this);
        }

        mCommentAreaView.initData(this);
        recommendGoodsView.initData(this);
        mCourseDetailView.initData(this);
        mButtonStatusView.initData(this);
        mCourseTrailerView.initData(this);
        mModuleGroup.setOnCheckedChangeListener(this);
        setRefresh(true);   // 初始化过一次后，都算是刷新的
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

        switch (checkedId) {

            case R.id.courseDetail:
                mCommentAreaView.setVisibility(View.GONE);
                recommendGoodsView.setVisibility(View.GONE);
                mCourseDetailView.setVisibility(View.VISIBLE);
                break;
            case R.id.discussArea:
                mCourseDetailView.setVisibility(View.GONE);
                recommendGoodsView.setVisibility(View.GONE);
                mCommentAreaView.setVisibility(View.VISIBLE);
                break;
            case R.id.recGoods:
                mCommentAreaView.setVisibility(View.GONE);
                mCourseDetailView.setVisibility(View.GONE);
                recommendGoodsView.setVisibility(View.VISIBLE);
                break;
        }

    }

    // 开始直播
    public void startLive() {
        LiveRoomActivity.startLive(mContext,
                AccountInfoManager.getInstance().getCurrentAccountUserId(),
                isMine, mCourse.getTitle(),
                AccountInfoManager.getInstance().getCurrentAccountUserIdStr(), "",
                AccountInfoManager.getInstance().getCurrentAccountUserAvatar(),
                Common.FROM_MAIN, mCourse.getId(), mCourseTrailer.getTopicsFormat(mContext));
    }

    // 观看回放
    private void playVideo() {
        SeeReplayActivity.startActivity(mContext, mCourseDetail.course.getId(),
                mCourseDetail.room.getDownUrl(), String.valueOf(mCourseDetail.user.id),
                mCourseDetail.course.getViews(), mCourseDetail.course.getRecvCoins(), mCourseDetail.room.getChannelId(),
                mCourseDetail.course.getType());
    }

    // 免费课程，直接购买
    private void payFreeCourse() {

        mContext.showNewDialog("正在购买...");
        List<String> course_ids = new ArrayList<>();
        course_ids.add(mCourse.getId());
        HashMap<String, String> params = new HashMap<>();
        params.put("trailer_id", mCourseTrailer.getId());// 预告id
        params.put("course_ids", GsonTools.toJson(course_ids));// 购买的数组
        DataProvider.payCourse(this, params, new GsonHttpConnection.OnResultListener<PayCourseMsg>() {
            @Override
            public void onSuccess(PayCourseMsg msg) {
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mContext.showNewDialog("购买成功，正在刷新数据...");
                        ToastHelper.showToast("购买成功");
                        getData(mCourse.getId());
                    }
                });
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                mContext.dismissDialog();
                mContext.showToast("请稍后再试");
            }

        });

    }

    // 购买课程
    private void payCourse(int viewId) {

        mCourseDetail.course_ids = new ArrayList<>();

        if (viewId == R.id.rightBtn) {
            for (Course course : mCourseDetail.getSimilarCourses()) {
                // 未购买的才要加入
                if (!course.hasBuy())
                    mCourseDetail.course_ids.add(course.getId());
            }
            AnalyticsReport.onEvent(mContext, AnalyticsReport.COURSE_DETAIL_COMMIT_BUY_ENTIRE_CLICK, getEventParams());
        } else {
            mCourseDetail.course_ids.add(mCourse.getId());
            AnalyticsReport.onEvent(mContext, AnalyticsReport.COURSE_DETAIL_COMMIT_BUY_SINGLE_CLICK, getEventParams());
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", mCourseDetail);
        Utils.startActivity(mContext, PayCourseActivity.class, bundle);
    }

    // 处理我的课程的点击事件
    private void handleMyClick(int status) {

        switch (status) {

            case Course.CANCEL:  // 已取消不能编辑
            case Course.NOTSTART:// 未开播状态,就可以去编辑课堂
            case Course.CHANGETIME:// 调整直播时间，一样去编辑课堂
                break;
            case Course.CREATEDPLAYBACK: // 已生成回放
                playVideo(); // 观看回放
                break;
            case Course.LIVING: // 正在直播
                if (PermissionUtils.hasGrant(mContext, Manifest.permission.RECORD_AUDIO)
                        && PermissionUtils.hasGrant(mContext, Manifest.permission.CAMERA)) {
                    startLive();// 继续直播，不用查询
                } else {
                    PermissionUtils.checkPermissions(mContext, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA);
                }
                break;
            case Course.ALREADY: // 准备中，可以直播
                if (PermissionUtils.hasGrant(mContext, Manifest.permission.RECORD_AUDIO)
                        && PermissionUtils.hasGrant(mContext, Manifest.permission.CAMERA)) {
                    queryLivingState();// 需要查询有没有在直播
                } else {
                    PermissionUtils.checkPermissions(mContext, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA);
                }
                break;
            case Course.PLAYVIDEO: // 课程视频已可播放
                playVideo();
                break;

        }

    }

    // 处理其他主播的课程的点击事件
    private void handleOtherClick(int status) {

        switch (status) {

            case Course.CANCEL: // 已取消
            case Course.NOTSTART:// 未开播状态
            case Course.CHANGETIME:// 调整直播时间
            case Course.ALREADY: // 准备中
            case Course.CREATINGPLAYBACK: // 生成回放中
                break;
            case Course.CREATEDPLAYBACK: // 已生成回放
                playVideo(); // 观看回放
                break;
            case Course.LIVING: // 正在直播
                LiveRoomActivity.startPlay(mContext, mCourse.user_id, false, mCourse.getTitle(),
                        mCourseDetail.room.streamId, mCourseDetail.room.channelId, mCourseDetail.user.avatar,
                        mCourse.cover_url, Common.FROM_MAIN, mCourse.getId());
                break;
            case Course.PLAYVIDEO: // 视频课程类型，直接播放
                playVideo();
                break;
        }

    }

    // 查询直播状态
    public void queryLivingState() {

        mContext.showNewDialog("正在查询直播状态...");

        DataProvider.queryRoom(this, mUserInfo.getIdStr(), new GsonHttpConnection.OnResultListener<QueryRoomMsg>() {
            @Override
            public void onSuccess(QueryRoomMsg queryRoomMsg) {
                mContext.dismissDialog();
                if (queryRoomMsg != null && queryRoomMsg.isLiving()) {
                    Log.log(" 正在 courseId=" + queryRoomMsg.getCourselId() + " mData.course=" + mCourse.getId());
                    // 当前有直播正在进行,但与当前课程是不同一个直播
                    if (queryRoomMsg.getCourselId().equals(mCourse.getId())) {
                        Log.log(" 当前有直播正在进行,但与当前课程是同一个直播");
                        startLive();
                    } else {
                        mContext.showToast("您尚有直播正在进行");
                    }
                } else {
                    startLive();
                }

            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                mContext.dismissDialog();
                ToastHelper.showToast(errorMsg);
            }
        });
    }

    // 查询直播状态(仅在获取时用来改直播状态用)
    private void queryLivingRoom() {

        if (isMine && (getCourseStatus() == Course.CREATEDPLAYBACK || getCourseStatus() == Course.CREATINGPLAYBACK)) {
            DataProvider.queryRoom(this, getUserInfo().getIdStr(), new GsonHttpConnection.OnResultListener<QueryRoomMsg>() {
                @Override
                public void onSuccess(QueryRoomMsg queryRoomMsg) {
                    mContext.dismissDialog();
                    if (queryRoomMsg != null && queryRoomMsg.isLiving()) {
                        Log.log(" 正在 courseId=" + queryRoomMsg.getCourselId() + " mData.course=" + getCourseId());
                        // 当前有直播正在进行,但与当前课程是不同一个直播
                        if (queryRoomMsg.getCourselId().equals(getCourseId())) {
                            Log.log(" 当前有直播正在进行,但与当前课程是同一个直播，改为继续直播");
                            setCourseStatus(Course.LIVING);
                            mButtonStatusView.updateMyCourseStatus(getCourseStatus());
                        } else {
                            Log.log(" 当前有直播正在进行,但与当前课程不是同一个直播，不需处理");
                        }

                    } else {
                        Log.log("没有直播中房间，不需要处理");
                    }

                }

                @Override
                public void onFail(int errorCode, String errorMsg, String command) {
                    Log.log(" queryRoom onFail errorCode=" + errorCode + " errorMsg" + errorMsg);
                    mContext.dismissDialog();
                }
            });
        }

    }

    // 播放预告视频
    public void onTrailerClick() {
        mCourseTrailerView.playTrailer();
    }

    // 分享课程
    public void onShareClick() {
        // 右上角的分享图标
        if (mCourse.isTestLive()) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.test_live_share_tip));
            return;
        }

        mContext.showShareDialog(Common.SHARE_COURSE_URL + mCourse.getId(),
                mCourse.getTitle(), mCourse.getSummary(), mCourse.getCover_url(), true, mCourse.getAgentProfitratio(), false);
    }

    // 点击客服按钮
    public void onServiceClick() {
        mTitleBarView.onServiceClick();
    }

    // 点击用户信息
    public void onUserInfoClick() {
        mUserInfoView.onUserInfoClick();
    }

    // 点击配方锁
    public void onFormulaLockedClick() {
        if (mCourseDetail.hasSimilarCourse()) {
            if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                mContext.goLogin();
                return;
            }
            payCourse(R.id.rightBtn); // 点配方，购买整套
        } else {
            onLeftBtnClick();
        }
    }

    // 左边按钮的点击事件
    public void onLeftBtnClick() {

        if (isMine) {
            // 我的课程，左边的一直是编辑
            PostCourseActivity.startActivity(mContext, mCourse.type, mCourseTrailer.getId());
        } else {
            if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                mContext.goLogin();
                return;
            }
            // 他人的课程，只有支付。已支付后，这个按钮不显示。
            if (mCourse.getPrice() == 0) {
                payFreeCourse();
            } else {
                payCourse(R.id.leftBtn);
            }

        }

    }

    // 右边的点击事件
    public void onRightBtnClick() {

        if (isMine) {
            // 我的课程，右边的根据不同状态处理
            handleMyClick(mCourse.status);
        } else {
            if (mCourse.hasBuy()) {
                handleOtherClick(mCourse.status);
            } else {
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    mContext.goLogin();
                    return;
                }
                payCourse(R.id.rightBtn);
            }
        }
    }

    // Activity退出时的回调
    public void onDestroy() {
        mCourseTrailerView.onDestroy();
    }

    // 获取拼接的参数
    public HashMap<String, String> getEventParams() {

        if (mEventParams == null) {
            mEventParams = new HashMap<>();
        } else {
            mEventParams.clear();
        }

        if (mCourse != null) {
            mEventParams.put("Class_type", mCourse.type == Course.LIVE ? "22" : "23");
        }

        return mEventParams;
    }

    // 获取课程状态
    public int getCourseStatus() {
        if (mCourse != null) {
            return mCourse.status;
        } else {
            return 0;
        }
    }

    // 获取课程状态
    public void setCourseStatus(int status) {
        if (mCourse != null) {
            mCourse.status = status;
        }
    }

    // 获取课程状态
    public String getCourseId() {

        if (mCourse != null) {
            return mCourse.getId();
        } else {
            return "0";
        }
    }

    // 获取折扣
    public float getDiscount() {

        if (mCourseTrailer == null) {
            return 10f;
        } else {
            return mCourseTrailer.discount;
        }
    }

    // EventBus事件
    public void onEvent(PostEvent event) {

        switch (event.tag) {
            case SubcriberTag.RECEIVE_CHAT_MSG:
                ChatMsg msg = (ChatMsg) event.event;
                if (msg.getUserId().equals(SystemConfig.getInstance().getKefuID())) {
                    Log.log(" 需要refreshUnread");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mTitleBarView.refreshUnread();
                        }
                    }, 1500);
                } else {
                    Log.log(" 不需要refreshUnread");
                }
                break;
            default:
                break;
        }
    }

    public void showKJKeyboard(String nickName, boolean alsoComment) {
        mChatKeyboard.showKeyboard(nickName, alsoComment);
        AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_16016);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public boolean isMyCourse() {
        return isMine;
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public View getRootView() {
        return mRootView;
    }

    public TitleBarView getTitleBarView() {
        return mTitleBarView;
    }

    public Course getCourse() {
        return mCourse;
    }

    public CourseDetail getCourseDetail() {
        return mCourseDetail;
    }

    public CourseTrailer getCourseTrailer() {
        return mCourseTrailer;
    }

    public GradientScrollView getScrollView() {
        return mScrollView;
    }

    public CourseDetailActivity getActivity() {
        return mContext;
    }

    public KJChatKeyboardComment getChatKeyboard() {
        return mChatKeyboard;
    }
}