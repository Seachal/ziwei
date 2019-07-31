package com.laka.live.account.my;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.account.edit.MyInfoActivity;
import com.laka.live.account.follow.MyFollowsActivity;
import com.laka.live.account.income.MyIncomeActivity;
import com.laka.live.account.replay.MyLiveReplayActivity;
import com.laka.live.account.setting.SettingActivity;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.ListUserInfo;
import com.laka.live.bean.UserInfo;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.UserMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.activity.WebActivity;
import com.laka.live.ui.course.MyCoursesActivity;
import com.laka.live.ui.chat.ChatHomeActivity;
import com.laka.live.ui.widget.CircleImageView;
import com.laka.live.ui.widget.MarkSimpleDraweeView;
import com.laka.live.ui.widget.MyInfoItemView;
import com.laka.live.util.Common;
import com.laka.live.util.FastClickUtil;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Util;
import com.laka.live.util.Utils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by zwl on 16/3/15.
 */
public class MyActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MyActivity";

    private View mChatRed; // 私信的红点
    private TextView mIdText;// ID
    private TextView mNameText;
    private TextView mSignText;
    private TextView mAutoText;
    private TextView mLevelText;
    private TextView mFollowCountText;
    private TextView mFansCountText;
    private MyInfoItemView mApproveItem; // 认证
    private MyInfoItemView mMyIncomeItem; // 我的收益
    private MyInfoItemView mMyCoinItem; // 我的钱包
    private MyInfoItemView mMySubscribeItem; // 已购买的课堂
    private MyInfoItemView mMyReleasedItem; // 已发布的课堂
    private SimpleDraweeView mContributionListRightFace;
    private SimpleDraweeView mContributionListMidFace;
    private SimpleDraweeView mContributionListLeftFace;

    private SimpleDraweeView mHeadView;

    private UserInfo mUserInfo;

    private Typeface typeface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false);
        setContentView(R.layout.activity_my);
        init();
        updateInfo();
    }

    private void init() {

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) findViewById(R.id.head_layout).getLayoutParams();
        layoutParams.height = (int) (Util.getScreenWidth(this) * (864f / 1080f));

        typeface = Typeface.createFromAsset(getAssets(), "fonts/AFFOGATO-BOLD.OTF");

        mHeadView = (SimpleDraweeView) findViewById(R.id.head_bg);

        mNameText = (TextView) findViewById(R.id.name);
        mSignText = (TextView) findViewById(R.id.signature);
        mAutoText = (TextView) findViewById(R.id.auth_layout);

        mLevelText = initUserCount(R.id.level_value, "0", getString(R.string.live_replay));
        mFollowCountText = initUserCount(R.id.follow_count, "0", getString(R.string.follow_user));
        mFansCountText = initUserCount(R.id.fans_count, "0", getString(R.string.fans));

        mIdText = (TextView) findViewById(R.id.id_value);
        mIdText.setTypeface(typeface);

        mChatRed = findViewById(R.id.iv_red);
        mChatRed.setVisibility(View.GONE);
        findViewById(R.id.setting_img).setOnClickListener(this);
        findViewById(R.id.chatting_img).setOnClickListener(this);
        findViewById(R.id.editUserInfo).setOnClickListener(this);

        // 初始化我的页面底部的横条选项
        initInfoItemView();

    }

    // 初始化我的页面底部的横条选项
    private void initInfoItemView() {

        initMyCoin();
        initApprove();
        initMyIncome();
        initMyReleased();
        initMySubscribe();
        initContributionList();

    }

    // 认证
    private void initApprove() {
        mApproveItem = (MyInfoItemView) findViewById(R.id.approve);
        setView(mApproveItem, "");
    }

    // 我的收益
    private void initMyIncome() {
        mMyIncomeItem = (MyInfoItemView) findViewById(R.id.my_income);
        mMyIncomeItem.setValueTextColor(R.color.color3BC36B);
        setView(mMyIncomeItem, "可用:0", "冻结:0");
    }

    // 我的钱包
    private void initMyCoin() {
        mMyCoinItem = (MyInfoItemView) findViewById(R.id.my_coin);
        setView(mMyCoinItem, "0");
    }

    // 我的购买课堂
    private void initMySubscribe() {
        mMySubscribeItem = (MyInfoItemView) findViewById(R.id.my_subscribe);
        setView(mMySubscribeItem, "0");
    }

    // 我发布的课堂
    private void initMyReleased() {
        mMyReleasedItem = (MyInfoItemView) findViewById(R.id.my_released);
        setView(mMyReleasedItem, "0");
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

    /**
     * 初始化贡献榜
     */
    private void initContributionList() {
        findViewById(R.id.contribution_list).setOnClickListener(this);
    }

    private TextView initUserCount(int id, CharSequence count, CharSequence text) {
        View view = findViewById(id);
        TextView countText = (TextView) view.findViewById(R.id.count);
        TextView textView = (TextView) view.findViewById(R.id.text);

        textView.setTypeface(typeface);

        countText.setText(count);
        textView.setText(text);

        view.setOnClickListener(this);
        return countText;
    }


    private void updateInfo() {

        mUserInfo = AccountInfoManager.getInstance().getAccountInfo();
        if (mUserInfo == null) {
            return;
        }

        // 显示红点
        mMyIncomeItem.setRedCount(mUserInfo.getNew_coins());

        ImageUtil.loadImage(mHeadView, mUserInfo.getAvatar());

        mNameText.setText(mUserInfo.getNickName());
        mIdText.setText("ID：" + mUserInfo.getIdStr());

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

        mLevelText.setText(String.valueOf(mUserInfo.getLevel()));
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

        if (mContributionListRightFace == null) {
            mContributionListRightFace = (CircleImageView) findViewById(R.id.contribution_list_right_face);
            mContributionListRightFace.setId(R.id.contribution_list_right_face);
        }

        if (mContributionListMidFace == null) {
            mContributionListMidFace = (CircleImageView) findViewById(R.id.contribution_list_mid_face);
            mContributionListMidFace.setId(R.id.contribution_list_mid_face);
        }

        if (mContributionListLeftFace == null) {
            mContributionListLeftFace = (SimpleDraweeView) findViewById(R.id.contribution_list_left_face);
            mContributionListLeftFace.setId(R.id.contribution_list_left_face);
        }


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

    // 避免拿的太频繁
    private boolean isGetting = false;

    private void getUserInfo() {

        if (isGetting)
            return;
        else
            isGetting = true;

        DataProvider.getUserInfo(this, new GsonHttpConnection.OnResultListener<UserMsg>() {
            @Override
            public void onSuccess(UserMsg userMsg) {
                isGetting = false;
                if (userMsg.isSuccessFul()) {
                    UserInfo info = userMsg.getUserInfo();
                    if (info != null) {
                        info.setToken(AccountInfoManager.getInstance().getCurrentAccountToken());
                        AccountInfoManager.getInstance().updateCurrentAccountInfo(info);
                        updateInfo();
                    }
                }
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                isGetting = false;
                Log.d(TAG, "getUserInfo Error:" + errorCode + ";" + errorMsg);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PostEvent event) {

        if (SubcriberTag.REFRESH_BOTTOM_UNREAD_RED.equals(event.tag)) {
            long unreadCnt = (long) event.event;
            if (unreadCnt > 0) {
                mChatRed.setVisibility(View.VISIBLE);
            } else {
                mChatRed.setVisibility(View.GONE);
            }
        } else if (SubcriberTag.UPDATE_USER_INFO.equals(event.tag)) {
            getUserInfo();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.share_img:
                // 分享
                showShareDialog("", "", "", "", false);
                break;
            case R.id.my_released:
                if (AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                // 我已发布的
                AnalyticsReport.onEvent(this, AnalyticsReport.MY_INCOME_CLICK_EVENT_ID);
                Utils.startActivity(this, MyCoursesActivity.class, MyCoursesActivity.MYLIVE);
                break;
            case R.id.my_subscribe:
                if (AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                // 我已购买的
                AnalyticsReport.onEvent(this, AnalyticsReport.MY_LIVE_CLICK_EVENT_ID);
                Utils.startActivity(this, MyCoursesActivity.class, MyCoursesActivity.SUBSCRIBELIVE);
                break;
            case R.id.user_face:
            case R.id.editUserInfo:
                if (AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                // 修改个人资料
                goMyInfo();
                break;
            case R.id.live_count:
                if (AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                goReplayActivity();
                break;
            case R.id.follow_count:
                if (AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                goFollowActivity();
                break;
            case R.id.fans_count:
                if (AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                goFansActivity();
                break;
            case R.id.my_income:
                if (AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                goMyIncome();
                break;
            case R.id.level:
                goMyLevel();
                break;
            case R.id.recive:
                if (AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                goContributionListActivity();
                break;
            case R.id.my_coin:
                if (AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                goMyCoin();
                break;
            case R.id.setting_img:
                if (AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                goSetting();
                break;
            case R.id.chatting_img:
                if (AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                ChatHomeActivity.startActivity(this);
                break;
            case R.id.contribution_list:
                if (AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                goContributionListActivity();
                break;
            case R.id.level_value:
                if (AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                AnalyticsReport.onEvent(this, AnalyticsReport.MY_LEVEL_CLICK_EVENT_ID);
                WebActivity.startActivity(this, Common.MY_LEVEL_URL + mUserInfo.getIdStr(), getString(R.string.my_level));
                break;
            case R.id.approve:
                if (AccountInfoManager.getInstance().checkUserIsLogin()) {
                    goLogin();
                    return;
                }
                WebActivity.startActivity(this, Common.APPROVE_URL + mUserInfo.getIdStr(), getString(R.string.auth_info));
                break;
            default:
                break;
        }
    }

    private void goReplayActivity() {
        if (FastClickUtil.getInstance().isFastClick()) {
            return;
        }
        MyLiveReplayActivity.startActivity(this);
//        AnalyticsReport.onEvent(this, AnalyticsReport.MY_LIVE_CLICK_EVENT_ID);
    }

    private void goFollowActivity() {
        if (FastClickUtil.getInstance().isFastClick()) {
            return;
        }
        MyFollowsActivity.startActivity(this);
        AnalyticsReport.onEvent(this, AnalyticsReport.MY_FOLLOW_CLICK_EVENT_ID);
    }

    private void goFansActivity() {
        if (FastClickUtil.getInstance().isFastClick()) {
            return;
        }
        ;
        MyFansActivity.startActivity(this);
        AnalyticsReport.onEvent(this, AnalyticsReport.MY_FANS_CLICK_EVENT_ID);
    }

    private void goContributionListActivity() {
        if (FastClickUtil.getInstance().isFastClick()) {
            return;
        }
        ContributionListActivity.startActivity(this, AccountInfoManager.getInstance().getCurrentAccountUserIdStr());
        AnalyticsReport.onEvent(this, AnalyticsReport.MY_CONTRIBUTION_LIST_CLICK_EVENT_ID);
    }

    private void copyId() {
        if (mUserInfo == null) {
            return;
        }
        Util.copy(this, String.valueOf(mUserInfo.getId()));
        showToast(R.string.copy_success);
    }

    private void goMyInfo() {
        MyInfoActivity.startActivity(this);
        AnalyticsReport.onEvent(this, AnalyticsReport.MY_EDIT_CLICK_EVENT_ID);
    }

    private void goMyIncome() {
        if (FastClickUtil.getInstance().isFastClick()) {
            return;
        }
        MyIncomeActivity.startActivity(this);
//        AnalyticsReport.onEvent(this, AnalyticsReport.MY_INCOME_CLICK_EVENT_ID);
    }

    private void goMyLevel() {
        if (FastClickUtil.getInstance().isFastClick()) {
            return;
        }
        String url = Common.MY_LEVEL_URL + "?" + Common.UID + "="
                + AccountInfoManager.getInstance().getCurrentAccountUserId();
        WebActivity.startActivity(this, url, getString(R.string.my_level));

        AnalyticsReport.onEvent(this, AnalyticsReport.MY_LEVEL_CLICK_EVENT_ID);
    }

    private void goMyCoin() {
        if (FastClickUtil.getInstance().isFastClick()) {
            return;
        }
        MyCoinsActivity.startActivity(this);
        AnalyticsReport.onEvent(this, AnalyticsReport.MY_KA_ZUAN_CLICK_EVENT_ID);
    }

    private void goSetting() {
        SettingActivity.startActivity(this);
        AnalyticsReport.onEvent(this, AnalyticsReport.MY_SETTING_CLICK_EVENT_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
        AnalyticsReport.onEvent(this, AnalyticsReport.MY_SHOW_EVENT_ID);
    }

}
