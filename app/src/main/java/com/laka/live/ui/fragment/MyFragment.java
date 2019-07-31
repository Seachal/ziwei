package com.laka.live.ui.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.account.edit.MyInfoActivity;
import com.laka.live.account.follow.MyFollowsActivity;
import com.laka.live.account.income.MyIncomeActivity;
import com.laka.live.account.my.ContributionListActivity;
import com.laka.live.account.my.MyCoinsActivity;
import com.laka.live.account.my.MyFansActivity;
import com.laka.live.account.replay.MyLiveReplayActivity;
import com.laka.live.account.setting.SettingActivity;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.bean.Course;
import com.laka.live.bean.ListUserInfo;
import com.laka.live.bean.UserInfo;
import com.laka.live.dao.DbManger;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.NewestCourseMsg;
import com.laka.live.msg.QueryRoomMsg;
import com.laka.live.msg.UserMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.shopping.activity.OrderListActivity;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.activity.WebActivity;
import com.laka.live.ui.chat.ChatHomeActivity;
import com.laka.live.ui.course.MyCoursesActivity;
import com.laka.live.ui.course.detail.CourseDetailActivity;
import com.laka.live.ui.room.LiveRoomActivity;
import com.laka.live.ui.room.SeeReplayActivity;
import com.laka.live.ui.widget.AnimationView;
import com.laka.live.ui.widget.MarkSimpleDraweeView;
import com.laka.live.ui.widget.MyInfoItemView;
import com.laka.live.util.Common;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.TimeUtil;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;
import com.laka.live.video.constant.VideoConstant;
import com.laka.live.video.ui.activity.VideoMaterialActivity;
import com.qiyukf.unicorn.api.Unicorn;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import framework.ioc.Ioc;
import framework.ioc.annotation.Inject;
import framework.ioc.annotation.InjectView;
import laka.live.bean.ChatSession;


public class MyFragment extends BaseFragment implements View.OnClickListener, EventBusManager.OnEventBusListener {


    private static final String TAG = "MyFragment";
    @InjectView(id = R.id.iv_red)
    public View mChatRed; // 私信的红点
    @InjectView(id = R.id.id_value)
    public TextView mIdText;// ID
    @InjectView(id = R.id.name)
    public TextView mNameText;
    @InjectView(id = R.id.signature)
    public TextView mSignText;
    @InjectView(id = R.id.auth_layout)
    public TextView mAutoText;
    @InjectView(id = R.id.approve)
    public MyInfoItemView mApproveItem; // 认证
    @InjectView(id = R.id.my_income)
    public MyInfoItemView mMyIncomeItem; // 我的收益
    @InjectView(id = R.id.my_coin)
    public MyInfoItemView mMyCoinItem; // 我的钱包
    @InjectView(id = R.id.my_subscribe)
    public MyInfoItemView mMySubscribeItem; // 已购买的课堂ServiceMessageActivity
    @InjectView(id = R.id.my_released)
    public MyInfoItemView mMyReleasedItem; // 已发布的课堂
    @InjectView(id = R.id.contribution_list)
    public View contribution;
    @InjectView(id = R.id.setting_img)
    public View setting_img;
    @InjectView(id = R.id.chatting_img)
    public View chatting_img;
    @InjectView(id = R.id.editUserInfo)
    public View editUserInfo;
    @InjectView(id = R.id.contribution_list_right_face)
    public SimpleDraweeView mContributionListRightFace;
    @InjectView(id = R.id.contribution_list_mid_face)
    public SimpleDraweeView mContributionListMidFace;
    @InjectView(id = R.id.contribution_list_left_face)
    public SimpleDraweeView mContributionListLeftFace;
    @InjectView(id = R.id.head_bg)
    public SimpleDraweeView mHeadView;
    @InjectView(id = R.id.iv_head)
    public SimpleDraweeView mIvHead;
    @InjectView(id = R.id.ll_my_buy)
    public LinearLayout mMyBuy;
    @InjectView(id = R.id.ll_my_publish)
    public LinearLayout mMyPublish;
    @InjectView(id = R.id.red_iv)
    public ImageView mRedIv;
    @InjectView(id = R.id.ll_my_income)
    public RelativeLayout mMyIncom;
    @InjectView(id = R.id.ll_my_package)
    public LinearLayout mMyPackage;
    @InjectView(id = R.id.ll_my_order)
    public LinearLayout mMyOrder;
    @InjectView(id = R.id.iv_buy)
    public ImageView ivBuy;
    @InjectView(id = R.id.iv_publish)
    public ImageView ivPublish;
    @InjectView(id = R.id.iv_income)
    public ImageView ivIncome;
    @InjectView(id = R.id.iv_package)
    public ImageView ivPackage;
    @InjectView(id = R.id.iv_order)
    public ImageView ivOrder;
    @InjectView(id = R.id.ll_live)
    public RelativeLayout llLive;
    @InjectView(id = R.id.ll_no_live)
    public LinearLayout llNoLive;
    @InjectView(id = R.id.tv_live_tips)
    public TextView tvLiveTips;
    @InjectView(id = R.id.tv_live_title)
    public TextView tvLiveTitle;
    @InjectView(id = R.id.tv_live_time)
    public TextView tvLiveTime;
    @InjectView(id = R.id.btn_live)
    public TextView btnLive;
    @InjectView(id = R.id.iv_live)
    public SimpleDraweeView ivLive;
    @InjectView(id = R.id.ll_start)
    public LinearLayout llStart;
    @InjectView(id = R.id.ll_my_info)
    public LinearLayout llMyInfo;
    @InjectView(id = R.id.btn_login)
    public Button btnLogin;
    @InjectView(id = R.id.rl_red)
    public RelativeLayout rlRed;
    @InjectView(id = R.id.rl_setting)
    public RelativeLayout rlSetting;
    @InjectView(id = R.id.head_layout)
    public RelativeLayout rlHead;
    @InjectView(id = R.id.animation_yellow)
    public AnimationView animYellow;
    @InjectView(id = R.id.animation_white)
    public AnimationView animWhite;

    // 这三个是在子View中
    @InjectView(id = R.id.level_divider)
    public View levelDivider;
    public View levelView;
    public TextView mLevelText;
    public TextView mFollowCountText;
    public TextView mFansCountText;

    @Inject
    public UserInfo mUserInfo;
    private Typeface typeface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTheme(R.style.NoTranslucentActivityTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        updateInfo();
        EventBusManager.register(this);//注册EventBus

    }


    private void initView() {
//        chatting_img.setOnClickListener(this);
//        setting_img.setOnClickListener(this);
        rlSetting.setOnClickListener(this);
        rlRed.setOnClickListener(this);
        mNameText.setOnClickListener(this);
        mIdText.setOnClickListener(this);
        mSignText.setOnClickListener(this);
        mIvHead.setVisibility(View.GONE);
//        btnLogin.setOnClickListener(this);
        mMyBuy.setOnClickListener(this);
        mMyPublish.setOnClickListener(this);
        mMyIncom.setOnClickListener(this);
        mMyPackage.setOnClickListener(this);
        mMyOrder.setOnClickListener(this);
        ivBuy.setOnClickListener(this);
        ivPublish.setOnClickListener(this);
        ivIncome.setOnClickListener(this);
        ivPackage.setOnClickListener(this);
        ivOrder.setOnClickListener(this);

        typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/AFFOGATO-BOLD.OTF");
//        mIdText.setTypeface(typeface);
        levelView = getActivity().findViewById(R.id.level_value);
        mLevelText = initUserCount(R.id.level_value, "0", getString(R.string.material_store));
        mFollowCountText = initUserCount(R.id.follow_count, "0", getString(R.string.follow));
        mFansCountText = initUserCount(R.id.fans_count, "0", getString(R.string.fans_only));

//        setView(mMyIncomeItem, "可用:0", "冻结:0");
//        setView(mMyCoinItem, "0");
//        setView(mMySubscribeItem, "0");
//        setView(mMyReleasedItem, "0");
//        setView(mApproveItem, "");
//        mMyIncomeItem.setValueTextColor(R.color.color3BC36B);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) rlHead.getLayoutParams();
        lp.height = Utils.dip2px(mContext, 240);
        rlHead.setLayoutParams(lp);

    }


    // 设置初始值和点击事件
    private void setView(MyInfoItemView myInfoItemView, String value) {
        myInfoItemView.setValueText(value);
        myInfoItemView.setOnClickListener(this);
    }

    // 设置初始值和点击事件
    private void setView(MyInfoItemView myInfoItemView, String value, String secValue) {
        myInfoItemView.setValueText(value);
        myInfoItemView.setSecValueText(secValue);
        myInfoItemView.setOnClickListener(this);
    }

    private TextView initUserCount(int id, CharSequence count, CharSequence text) {

        View view = getActivity().findViewById(id);
        TextView countText = (TextView) view.findViewById(R.id.count);
        TextView textView = (TextView) view.findViewById(R.id.text);

        textView.setTypeface(typeface);

        countText.setText(count);
        textView.setText(text);

        view.setOnClickListener(this);
        return countText;
    }


    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PostEvent event) {

        if (SubcriberTag.REFRESH_BOTTOM_UNREAD_RED.equals(event.tag)) {
            long unreadCnt = (long) event.event;
            if (unreadCnt > 0) {
                mChatRed.setVisibility(View.VISIBLE);
            } else {
                mChatRed.setVisibility(View.GONE);
            }
        } /*else if (SubcriberTag.RECEIVE_CHAT_MSG.equals(event.tag)) {
            //ChatMsg msg = (ChatMsg) event.event;
            // 不管是客服还是其他用户都要显示红点
            refreshUnreadRed();
        }*/
    }

    public void refreshUnreadRed() {
        // long totalUnreadCnt = Unicorn.getUnreadCount();
        // 获取未读会话数
        long totalUnreadCnt = DbManger.getInstance().getTotalUnreadCnt();
        if (totalUnreadCnt > 0) {
            mChatRed.setVisibility(View.VISIBLE);
        } else {
            mChatRed.setVisibility(View.GONE);
        }
    }

    private Course curCourse;

    private void getUserInfo() {

        String mineId = AccountInfoManager.getInstance().getCurrentAccountUserIdStr();
        Log.d(TAG, " getUserInfo mineId=" + mineId);
        DataProvider.getUserInfo(this, mineId, "", false, false, true, new GsonHttpConnection.OnResultListener<UserMsg>() {
            @Override
            public void onSuccess(UserMsg userMsg) {
                if (userMsg.isSuccessFul()) {
                    UserInfo info = userMsg.getUserInfo();
                    if (info != null) {
                        mUserInfo = info;
                        Log.d(TAG, " 新用户");
                        info.setToken(AccountInfoManager.getInstance().getCurrentAccountToken());
                        AccountInfoManager.getInstance().updateCurrentAccountInfo(info);
                        updateInfo();
                    }
                }
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
            }
        });

        DataProvider.getNewestCourse(this, AccountInfoManager.getInstance().getCurrentAccountUserIdStr(), new GsonHttpConnection.OnResultListener<NewestCourseMsg>() {
            @Override
            public void onSuccess(NewestCourseMsg result) {
                if (result.isSuccessFul()) {
                    curCourse = result.data;
                    showLive(true, result.data);
                } else {
                    showLive(false, null);
                }
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                Log.d(TAG, " getNewestCourse onFail");
                showLive(false, null);

            }
        });
    }

    private void showLive(boolean isShow, Course course) {
        if (llLive == null || llNoLive == null) {
            return;
        }
        if (isShow && course != null) {
            Log.d(TAG, " 有课程");
            llLive.setVisibility(View.VISIBLE);
            llNoLive.setVisibility(View.GONE);
            llStart.setVisibility(View.VISIBLE);
            animYellow.setVisibility(View.GONE);
            animWhite.setVisibility(View.GONE);
            if (AccountInfoManager.getInstance().getCurrentAccountUserId() == course.getUser().getId()) {
                Log.d(TAG, " 我是主播 status=" + course.getStatus());
                if (course.getStatus() == Course.ALREADY) {
                    tvLiveTime.setText("开播时间 | " + TimeUtil.getLiveTime2(course.start_time));
                    btnLive.setText("开播");
                    llStart.setClickable(true);
                    animYellow.setVisibility(View.VISIBLE);
                    animWhite.setVisibility(View.VISIBLE);
//                    llStart.setBackgroundColor(ResourceHelper.getColor(R.color.colorFFC40E));
                } else if (course.getStatus() == Course.CHANGETIME) {
                    tvLiveTime.setText("开播时间 | " + TimeUtil.getLiveTime2(course.start_time));
                    btnLive.setText("调整");
                    llStart.setClickable(true);
                    llStart.setVisibility(View.GONE);
//                    llStart.setBackgroundColor(ResourceHelper.getColor(R.color.colorFFC40E));
                } else if (course.getStatus() == Course.NOTSTART) {

                    if (TimeUtil.isToday(course.start_time * 1000)) {
                        setCountDownTimer(course.start_time);
                    } else {
                        tvLiveTime.setText("开播时间 | " + TimeUtil.getLiveTime2(course.start_time));
                    }
                    btnLive.setText("未开播");
                    llStart.setClickable(false);
                    llStart.setVisibility(View.GONE);
//                    llStart.setBackgroundColor(ResourceHelper.getColor(R.color.bg_gray));
                } else if (course.getStatus() == Course.CREATEDPLAYBACK) {//可播放回放
                    tvLiveTime.setText("回放时长 | " + TimeUtil.getTimeWithStr(Integer.parseInt(course.getDuration())));
                    btnLive.setText("播放");
                    llStart.setClickable(true);
//                    llStart.setBackgroundColor(ResourceHelper.getColor(R.color.colorFFC40E));
                } else if (course.getStatus() == Course.PLAYVIDEO) {//可播放视频
                    tvLiveTime.setText("视频时长 | " + TimeUtil.getTimeWithStr(Integer.parseInt(course.getDuration())));
                    btnLive.setText("播放");
                    llStart.setClickable(true);
//                    llStart.setBackgroundColor(ResourceHelper.getColor(R.color.colorFFC40E));
                }
            } else {
                Log.d(TAG, " 我是用户 status=" + course.getStatus());

                // 倒计时
                if (course.getStatus() == Course.LIVING) {
                    tvLiveTime.setText("开播时间 | " + TimeUtil.getLiveTime2(course.start_time));
                    btnLive.setText("观看");
                    llStart.setClickable(true);
                    llStart.setBackgroundColor(ResourceHelper.getColor(R.color.colorFFC40E));
                    animYellow.setVisibility(View.VISIBLE);
                    animWhite.setVisibility(View.VISIBLE);
                } else if (course.getStatus() == Course.NOTSTART || course.getStatus() == Course.CHANGETIME || course.getStatus() == Course.ALREADY) {

                    if (TimeUtil.isToday(course.start_time * 1000) && course.getStatus() != Course.CHANGETIME) {
                        setCountDownTimer(course.start_time);
                    } else {
                        tvLiveTime.setText("开播时间 | " + TimeUtil.getLiveTime2(course.start_time));
                    }

                    btnLive.setText("未开播");
                    llStart.setClickable(false);
                    llStart.setVisibility(View.GONE);
//                    llStart.setBackgroundColor(ResourceHelper.getColor(R.color.bg_gray));
                } else if (course.getStatus() == Course.CREATEDPLAYBACK) {//可播放回放
                    tvLiveTime.setText("回放时长 | " + TimeUtil.getTimeWithStr(Integer.parseInt(course.getDuration())));
                    btnLive.setText("播放");
                    llStart.setClickable(true);
//                    llStart.setBackgroundColor(ResourceHelper.getColor(R.color.colorFFC40E));
                } else if (course.getStatus() == Course.PLAYVIDEO) {//可播放视频
                    tvLiveTime.setText("视频时长 | " + TimeUtil.getTimeWithStr(Integer.parseInt(course.getDuration())));
                    btnLive.setText("播放");
                    llStart.setClickable(true);
//                    llStart.setBackgroundColor(ResourceHelper.getColor(R.color.colorFFC40E));
                }

            }
//            tvLiveTips.setText();

            llStart.setTag(course);
            llStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AnalyticsReport.onEvent(getContext(), LiveReport.MY_LIVE_EVENT_15463);
                    if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                        goLogin();
                        return;
                    }

                    Course course = (Course) v.getTag();
                    if (AccountInfoManager.getInstance().getCurrentAccountUserId() == course.getUser().getId()) {
                        Log.d(TAG, " 我是主播 status=" + course.getStatus());
                        if (course.getStatus() == Course.ALREADY) {

                            queryLivingState();
//                            LiveRoomActivity.startLive(getContext(),
//                                    AccountInfoManager.getInstance().getCurrentAccountUserId(),
//                                    true, course.getTitle(),
//                                    AccountInfoManager.getInstance().getCurrentAccountUserIdStr(), "",
//                                    AccountInfoManager.getInstance().getCurrentAccountUserAvatar(),
//                                    Common.FROM_MAIN, course.getId(), course.getTopicsFormat(getContext()));
                        } else if (course.getStatus() == Course.CREATEDPLAYBACK) {//可播放回放
                            SeeReplayActivity.startActivity(getContext(), course.getId(), course.room.getDownUrl(), String.valueOf(course.getUser().getId()), course.getViews(), course.getRecvCoins(), course.getRoom().getChannelId(), course.getType());
//                            Intent intent = new Intent(this, SeeReplayActivity.class);
//                            intent.putExtra("courseId", mData.course.getId());
//                            intent.putExtra("videoUrl", mData.course.video_url);
//                            intent.putExtra("userId", mData.user.id);
//                            intent.putExtra("views", mData.course.getViews());
//                            intent.putExtra("recvCoins", mData.course.getRecvCoins());
//                            intent.putExtra("rollBackId", mData.room.getChannelId());
//                            startActivity(intent);
//                            player.startFullPlayer(course.getTitle(), course.room.getDownUrl(), course.getCover_url());
                        } else if (course.getStatus() == Course.PLAYVIDEO) {//可播放视频
                            SeeReplayActivity.startActivity(getContext(), course.getId(), course.room.getDownUrl(), String.valueOf(course.getUser().getId()), course.getViews(), course.getRecvCoins(), course.getRoom().getChannelId(), course.getType());
//                            player.startFullPlayer(course.getTitle(), course.room.getDownUrl(), course.getCover_url());
                        } else if (course.getStatus() == Course.CHANGETIME) {
                            showToast("请先调整直播时间");
                            CourseDetailActivity.startActivity(getContext(), course.getId());
                        }
                    } else {
                        Log.d(TAG, " 我是用户 status=" + course.getStatus());
                        if (course.getStatus() == Course.LIVE) {
                            LiveRoomActivity.startPlay(getContext(), course.user_id,
                                    Ioc.get(UserInfo.class).getId() == course.user_id, course.getTitle(),
                                    course.room.getStreamId(), course.room.getChannelId(), course.user.avatar,
                                    course.cover_url, Common.FROM_MAIN, course.getId());
                        } else if (course.getStatus() == Course.CREATEDPLAYBACK) {//可播放回放
                            SeeReplayActivity.startActivity(getContext(), course.getId(), course.room.getDownUrl(), String.valueOf(course.getUser().getId()), course.getViews(), course.getRecvCoins(), course.getRoom().getChannelId(), course.getType());
//                            player.startFullPlayer(course.getTitle(), course.room.getDownUrl(), course.getCover_url());
                        } else if (course.getStatus() == Course.PLAYVIDEO) {//可播放视频
//                            player.startFullPlayer(course.getTitle(), course.room.getDownUrl(), course.getCover_url());
                            SeeReplayActivity.startActivity(getContext(), course.getId(), course.room.getDownUrl(), String.valueOf(course.getUser().getId()), course.getViews(), course.getRecvCoins(), course.getRoom().getChannelId(), course.getType());
                        }
                    }
                }
            });
            tvLiveTitle.setText(course.getTitle());
            ImageUtil.loadImage(ivLive, course.getCover_url());

            ivLive.setOnClickListener(this);
            tvLiveTitle.setOnClickListener(this);

        } else {
            llLive.setVisibility(View.GONE);
            llNoLive.setVisibility(View.VISIBLE);
            llStart.setVisibility(View.GONE);
        }
    }

    private CountDownTimer mCountDownTimer;

    // 设置倒计时
    private void setCountDownTimer(long start_time) {

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

        long millisInFuture = start_time * 1000 - System.currentTimeMillis();

        if (millisInFuture <= 0) {
            tvLiveTime.setText("直播准备中");
            return;
        }

        mCountDownTimer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvLiveTime.setText("倒计时 " + TimeUtil.getTimeWithStr(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                tvLiveTime.setText("直播准备中");
            }
        };

        mCountDownTimer.start();

    }

    private void updateInfo() {
        if (getActivity() == null) {
            return;
        }

        if (mUserInfo == null) {
            return;
        }

        // 显示红点
        mMyIncomeItem.setRedCount(mUserInfo.getNew_coins());

        if (mUserInfo.getNew_coins() > 0) {
            mRedIv.setVisibility(View.VISIBLE);
        } else {
            mRedIv.setVisibility(View.GONE);
        }

        ImageUtil.loadImage(mHeadView, mUserInfo.getAvatar());
//        ImageUtil.loadImage(mIvHead, mUserInfo.getAvatar());
        mNameText.setText(mUserInfo.getNickName());
        mIdText.setText("ID：" + mUserInfo.getIdStr());
        mSignText.setTextColor(ResourceHelper.getColor(R.color.colorE5E5E5));
        mSignText.setText(mUserInfo.getDescription());

        String info = mUserInfo.getVerifyInfo();
        if (TextUtils.isEmpty(info)) {
            mAutoText.setVisibility(View.GONE);
        } else {
            mAutoText.setVisibility(View.VISIBLE);
            String verified = "";
            if (MarkSimpleDraweeView.getAuthType(mUserInfo.getStarVerified(), mUserInfo.getVerified())
                    == MarkSimpleDraweeView.AuthType.STAR) {
                verified = ResourceHelper.getString(R.string.verified_start_start_tip) + info;
            } else if (MarkSimpleDraweeView.getAuthType(mUserInfo.getStarVerified(), mUserInfo.getVerified())
                    == MarkSimpleDraweeView.AuthType.NORMAL) {
                verified = ResourceHelper.getString(R.string.verified_normal_start_tip) + info;
            }
            mAutoText.setText(verified);
        }

        //仅有在讲师身份下，才具备素材库功能
        if (mUserInfo.isVerified()) {
            levelView.setVisibility(View.VISIBLE);
            levelDivider.setVisibility(View.VISIBLE);
            mLevelText.setText(String.valueOf(mUserInfo.getMaterialCount()));
        } else {
            levelView.setVisibility(View.GONE);
            levelDivider.setVisibility(View.GONE);
        }

        mFollowCountText.setText(String.valueOf(mUserInfo.getFollows()));
        mFansCountText.setText(String.valueOf(mUserInfo.getFans()));
        setView(mMyIncomeItem, "可用:" + String.valueOf(mUserInfo.getRecvCoins()),
                "冻结:" + String.valueOf(mUserInfo.getFrozenCoins()));
        mMyCoinItem.setValueText(String.valueOf(mUserInfo.getCoins()));
        mMyReleasedItem.setValueText(mUserInfo.getReleaseCourseCount());
        mMySubscribeItem.setValueText(mUserInfo.getBookingCourseCount());

        List<ListUserInfo> listUserInfoList = mUserInfo.getSendRankList();
        updateContributionListFace(listUserInfoList);

        // 已认证的就不要显示认证入口
        if (mUserInfo.isVerified()) {
            mApproveItem.setVisibility(View.GONE);
        }

    }

    private void updateContributionListFace(List<ListUserInfo> listUserInfoList) {

        mContributionListRightFace.setId(R.id.contribution_list_right_face);
        mContributionListMidFace.setId(R.id.contribution_list_mid_face);
        mContributionListLeftFace.setId(R.id.contribution_list_left_face);

        ListUserInfo firstInfo = null;
        ListUserInfo secondInfo = null;
        ListUserInfo thirdInfo = null;
        if (listUserInfoList != null) {
            int size = listUserInfoList.size();
            if (size > 0) {
                firstInfo = listUserInfoList.get(0);
            }
            if (size > 1) {
                secondInfo = listUserInfoList.get(1);
            }
            if (size > 2) {
                thirdInfo = listUserInfoList.get(2);
            }
        }
        updateContributionFace(firstInfo, mContributionListLeftFace);
        updateContributionFace(secondInfo, mContributionListMidFace);
        updateContributionFace(thirdInfo, mContributionListRightFace);
    }

    private void updateContributionFace(ListUserInfo listUserInfo, SimpleDraweeView simpleDraweeView) {
        if (listUserInfo == null) {
            return;
        }
        ImageUtil.loadImage(simpleDraweeView, listUserInfo.getAvatar());
    }

    @Override
    public void onClick(View v) {

//        if (FastClickUtil.getInstance().isFastClick()) {
//            return;
//        }

        Log.d(TAG, " onClick v=" + v.getId());
        switch (v.getId()) {
            case R.id.iv_live:
                AnalyticsReport.onEvent(getContext(), LiveReport.MY_LIVE_EVENT_15461);
                if (curCourse == null) {
                    return;
                }
                CourseDetailActivity.startActivity(mContext, String.valueOf(curCourse.getId()));
                break;
            case R.id.tv_live_title:
                AnalyticsReport.onEvent(getContext(), LiveReport.MY_LIVE_EVENT_15462);
                if (curCourse == null) {
                    return;
                }
                CourseDetailActivity.startActivity(mContext, String.valueOf(curCourse.getId()));
                break;
            case R.id.btn_login:
                goLogin();
                break;
            case R.id.share_img:
                // 分享
                //showShareDialog("", "", "", "", false);
                break;
//             case R.id.my_released: // 我已发布的
            case R.id.ll_my_publish:
            case R.id.iv_publish:
                AnalyticsReport.onEvent(mContext, AnalyticsReport.MY_INCOME_CLICK_EVENT_ID);
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                goMyReleased();
                break;
//            case R.id.my_subscribe: // 我已购买的
            case R.id.ll_my_buy:
            case R.id.iv_buy:
                AnalyticsReport.onEvent(mContext, AnalyticsReport.MY_LIVE_CLICK_EVENT_ID);
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                goMySubscribe();
                break;
            case R.id.user_face:
                Log.d(TAG, " onClick R.id.user_face");
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                goMyInfo();
                break;
            case R.id.editUserInfo:
                Log.d(TAG, " onClick R.id.editUserInfo");
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                goMyInfo();
                break;
            case R.id.name:
                Log.d(TAG, " onClick R.id.name");
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                goMyInfo();
                break;
            case R.id.signature:
                Log.d(TAG, " onClick R.id.signature");
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                goMyInfo();
                break;
            case R.id.id_value:
                Log.d(TAG, " onClick R.id.id_value");
                AnalyticsReport.onEvent(mContext, AnalyticsReport.MY_EDIT_CLICK_EVENT_ID);
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                goMyInfo();
                break;
            case R.id.live_count:
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                goReplayActivity();
                break;
            case R.id.follow_count:
                AnalyticsReport.onEvent(mContext, AnalyticsReport.MY_FOLLOW_CLICK_EVENT_ID);
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                goFollowActivity();
                break;
            case R.id.fans_count:
                AnalyticsReport.onEvent(mContext, AnalyticsReport.MY_FANS_CLICK_EVENT_ID);
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                goFansActivity();
                break;
//            case R.id.my_income:
            case R.id.ll_my_income:
            case R.id.iv_income:
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                goMyIncome();
                break;
            case R.id.level:
            case R.id.level_value:
                AnalyticsReport.onEvent(mContext, AnalyticsReport.MY_LEVEL_CLICK_EVENT_ID);
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                goToMaterialStore();
//                goMyLevel();
                break;
//            case R.id.my_coin:
            case R.id.ll_my_package:
            case R.id.iv_package:
                AnalyticsReport.onEvent(mContext, AnalyticsReport.MY_KA_ZUAN_CLICK_EVENT_ID);
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                goMyCoin();
                break;
            case R.id.rl_setting:
            case R.id.setting_img:
                AnalyticsReport.onEvent(mContext, AnalyticsReport.MY_SETTING_CLICK_EVENT_ID);
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                goSetting();
                break;
            case R.id.chatting_img:
            case R.id.rl_red:
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                ChatHomeActivity.startActivity(mContext);
                break;
            case R.id.recive:
            case R.id.contribution_list:
                AnalyticsReport.onEvent(mContext, AnalyticsReport.MY_CONTRIBUTION_LIST_CLICK_EVENT_ID);
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                goContributionListActivity();
                break;
            case R.id.approve:
                WebActivity.startActivity(mContext, Common.APPROVE_URL + mUserInfo.getIdStr(), getString(R.string.auth_info));
                break;
            case R.id.ll_my_order:
            case R.id.iv_order:
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                OrderListActivity.startActivity(getActivity());
                break;
            default:
                break;
        }
    }

    private void goMyReleased() {
        Utils.startActivity(mContext, MyCoursesActivity.class, MyCoursesActivity.MYLIVE);
    }

    private void goMySubscribe() {
        Utils.startActivity(mContext, MyCoursesActivity.class, MyCoursesActivity.SUBSCRIBELIVE);
    }

    private void goSetting() {
        SettingActivity.startActivity(mContext);
    }

    private void goMyInfo() {
        MyInfoActivity.startActivity(mContext);
    }

    private void goMyIncome() {
        MyIncomeActivity.startActivity(mContext);
        mRedIv.setVisibility(View.GONE);
    }

    private void goReplayActivity() {
        MyLiveReplayActivity.startActivity(mContext);
    }

    private void goFollowActivity() {
        MyFollowsActivity.startActivity(mContext);
    }

    private void goFansActivity() {
        MyFansActivity.startActivity(mContext);
    }

    private void goMyLevel() {
        String url = Common.MY_LEVEL_URL + "?" + Common.UID + "="
                + AccountInfoManager.getInstance().getCurrentAccountUserId();
        WebActivity.startActivity(mContext, url, getString(R.string.my_level));

    }

    private void goToMaterialStore() {
        VideoMaterialActivity.startActivity(mContext, VideoConstant.MATERIAL_TYPE_CONTROL);
    }

    private void goMyCoin() {
        MyCoinsActivity.startActivity(mContext);
    }

    private void goContributionListActivity() {
        ContributionListActivity.startActivity(mContext, AccountInfoManager.getInstance().getCurrentAccountUserIdStr());
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshUnreadRed();
        Log.d(TAG, " onResume");
        if (AccountInfoManager.getInstance().checkUserIsLogin()) {
            Log.d(TAG, " 已登录");
            getUserInfo();
            llMyInfo.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);
            btnLogin.setOnClickListener(null);
        } else {
            Log.d(TAG, " 未登录");
            llMyInfo.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            btnLogin.setOnClickListener(this);
            mRedIv.setVisibility(View.GONE);
            levelView.setVisibility(View.GONE);
            levelDivider.setVisibility(View.GONE);
            //置空最新课程和关注等
            showLive(false, null);
            mFansCountText.setText("0");
            mFollowCountText.setText("0");
//            mHeadView.setImageResource(R.drawable.blank_icon_avatar_big);
            ImageUtil.loadResImage(R.drawable.mine_bg_picture, mHeadView);
        }

        AnalyticsReport.onEvent(mContext, AnalyticsReport.MY_SHOW_EVENT_ID);
    }

    // 查询直播状态
    private void queryLivingState() {

        ((BaseActivity) getActivity()).showNewDialog("正在查询直播状态...");

        DataProvider.queryRoom(this, mUserInfo.getIdStr(), new GsonHttpConnection.OnResultListener<QueryRoomMsg>() {
            @Override
            public void onSuccess(QueryRoomMsg queryRoomMsg) {
                ((BaseActivity) getActivity()).dismissDialog();
                if (queryRoomMsg != null && queryRoomMsg.isLiving()) {
                    Log.d(TAG, " 正在 courseId=" + queryRoomMsg.getCourselId() + " curCourse=" + curCourse.getId());
                    // 当前有直播正在进行,但与当前课程是不同一个直播
                    if (queryRoomMsg.getCourselId().equals(curCourse.getId())) {

                        Log.d(TAG, " 当前有直播正在进行,但与当前课程是同一个直播");
                        startLive();
                    } else {
                        showToast("您尚有直播正在进行");
                    }

                } else {
                    startLive();
                }

            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                ((BaseActivity) getActivity()).dismissDialog();
                ToastHelper.showToast(errorMsg);
            }
        });
    }

    /**
     * 开始直播
     */
    private void startLive() {
        LiveRoomActivity.startLive(getContext(),
                AccountInfoManager.getInstance().getCurrentAccountUserId(),
                true, curCourse.getTitle(),
                AccountInfoManager.getInstance().getCurrentAccountUserIdStr(), "",
                AccountInfoManager.getInstance().getCurrentAccountUserAvatar(),
                Common.FROM_MAIN, curCourse.getId(), curCourse.getTopicsFormat(getContext()));
    }
}
