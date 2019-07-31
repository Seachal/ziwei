package com.laka.live.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.account.follow.MyFollowsActivity;
import com.laka.live.account.my.ContributionListActivity;
import com.laka.live.account.my.MyFansActivity;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.Course;
import com.laka.live.bean.ListUserInfo;
import com.laka.live.bean.UserInfo;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.ui.activity.WebActivity;
import com.laka.live.ui.fragment.FollowFragment;
import com.laka.live.ui.rankinglist.RankingItemView;
import com.laka.live.util.Common;
import com.laka.live.util.FastClickUtil;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;

import java.util.List;

import static com.laka.live.util.ResourceHelper.getString;

/**
 * Created by chuan on 9/21/16.
 */

public class UserInfoHeader extends FrameLayout implements View.OnClickListener {


    private UserInfo mUserInfo; // 用户信息
    private TextView mIdText; // ID
    private TextView mNameText; // 用户名
    private TextView mSignText; // 个性签名
    private TextView mAuthText; // 认证状态
    private TextView mLevelText; // 个人等级
    private TextView mFansCountText; // 粉丝数
    private TextView mFollowCountText; // 关注数
    private SimpleDraweeView mHeadBg; // 顶部背景
    //    private RadioGroup radioGroup; // radio组合
    private TextView mLiveTv; // 直播按钮
    private TextView mVideoTv; // 视频按钮
    private TextView mNewsTv; // 资讯按钮
    private SimpleDraweeView mContributionListRightFace;
    private SimpleDraweeView mContributionListMidFace;
    private SimpleDraweeView mContributionListLeftFace;
    public SimpleDraweeView mIvHead;
    private Context mContext;
    private Typeface mTypeface;
//    private RadioGroup.OnCheckedChangeListener listener;

    public UserInfoHeader(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public void updateUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        mUserInfo = userInfo;

        String mineUserId = AccountInfoManager.getInstance().getCurrentAccountUserIdStr();

        if (mineUserId.equals(mUserInfo.getIdStr())) {
            mFansCountText = initUserCount(R.id.fans_count, "0", getString(R.string.fans));
        } else {
            mFansCountText = initUserCount(R.id.fans_count, "0", getString(R.string.fans_only));
        }

        initData();
    }

    private void initView() {
        if (mContext == null) {
            return;
        }
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.userinfo_header, this);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) findViewById(R.id.head_layout).getLayoutParams();
//        layoutParams.height = (int) (Util.getScreenWidth(mContext) * (864f / 1080f));

        loadTypeface();

        mLevelText = initUserCount(R.id.level_value, "0", getString(R.string.live_replay));
        mFansCountText = initUserCount(R.id.fans_count, "0", getString(R.string.fans_only));
        mFollowCountText = initUserCount(R.id.follow_count, "0", getString(R.string.follow));
        mHeadBg = (SimpleDraweeView) findViewById(R.id.head_bg);
        mIvHead = (SimpleDraweeView) findViewById(R.id.iv_head);
        mNameText = (TextView) findViewById(R.id.name);
        mSignText = (TextView) findViewById(R.id.signature);
        mAuthText = (TextView) findViewById(R.id.auth_layout);

        mIdText = (TextView) findViewById(R.id.id_value);

//        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mLiveTv = (TextView) findViewById(R.id.live);
        mVideoTv = (TextView) findViewById(R.id.video);
        mNewsTv = (TextView) findViewById(R.id.news);
        mLiveTv.setOnClickListener(this);
        mVideoTv.setOnClickListener(this);
        mNewsTv.setOnClickListener(this);
//        mIdText.setTypeface(mTypeface);

        mFansCountText.setTypeface(mTypeface);
        mFollowCountText.setTypeface(mTypeface);

        findViewById(R.id.contribution_list).setOnClickListener(this);
        findViewById(R.id.share_img).setOnClickListener(this);
//        radioGroup.setOnCheckedChangeListener(listener);
    }

    private TextView initUserCount(int id, CharSequence count, CharSequence text) {
        View view = findViewById(id);
        TextView countText = (TextView) view.findViewById(R.id.count);
        TextView textView = (TextView) view.findViewById(R.id.text);
        countText.setTypeface(mTypeface);
        countText.setText(count);
        textView.setText(text);

        view.setOnClickListener(this);
        return countText;
    }

    private void loadTypeface() {
        mTypeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/AFFOGATO-BOLD.OTF");
    }

    public void showBack() {
        ImageView back = (ImageView) findViewById(R.id.setting_img);
        back.setImageResource(R.drawable.title_icon_back02);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) mContext).finish();
            }
        });
    }

    public void hideSetting() {
        findViewById(R.id.setting_img).setVisibility(GONE);
    }

    public void hideShare() {
        findViewById(R.id.share_img).setVisibility(GONE);
    }

    private void initData() {
        if (mUserInfo == null) {
            return;
        }

        String info = mUserInfo.getVerifyInfo();
        if (TextUtils.isEmpty(info)) {
            mAuthText.setVisibility(View.GONE);
        } else {
            mAuthText.setVisibility(View.VISIBLE);
            String verified = "";
            if (MarkSimpleDraweeView.getAuthType(mUserInfo.getStarVerified(), mUserInfo.getVerified())
                    == MarkSimpleDraweeView.AuthType.STAR) {
                verified = ResourceHelper.getString(R.string.verified_start_start_tip) + info;
            } else if (MarkSimpleDraweeView.getAuthType(mUserInfo.getStarVerified(), mUserInfo.getVerified())
                    == MarkSimpleDraweeView.AuthType.NORMAL) {
                verified = ResourceHelper.getString(R.string.verified_normal_start_tip) + info;
            }
            mAuthText.setText(verified);
        }

        if(!Utils.isEmpty(mUserInfo.getAvatar())){
            ImageUtil.loadImage(mHeadBg, mUserInfo.getAvatar());
        }
        ImageUtil.loadImage(mIvHead, mUserInfo.getAvatar());
        mNameText.setText(mUserInfo.getNickName());

        mSignText.setText(mUserInfo.getDescription());
        mFollowCountText.setText(String.valueOf(mUserInfo.getFollows()));
        mFansCountText.setText(String.valueOf(mUserInfo.getFans()));
        mLevelText.setText(String.valueOf(mUserInfo.getLevel()));
        mIdText.setText("ID：" + mUserInfo.getIdStr());
        updateContributionListFace();
    }

    private void updateContributionListFace() {
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

        List<ListUserInfo> listUserInfoList = mUserInfo.getSendRankList();
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
            ImageUtil.loadImage(simpleDraweeView, "");
            return;
        }
        ImageUtil.loadImage(simpleDraweeView, listUserInfo.getAvatar());
    }


    private void goMyLevel() {
        if (FastClickUtil.getInstance().isFastClick()) {
            return;
        }

        if (mUserInfo != null)
            WebActivity.startActivity(mContext, Common.MY_LEVEL_URL + mUserInfo.getIdStr(), getString(R.string.my_level));
        else
            ToastHelper.showToast("用户ID为空");

        AnalyticsReport.onEvent(mContext, AnalyticsReport.MY_LEVEL_CLICK_EVENT_ID);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_img:
                EventBusManager.postEvent(0, SubcriberTag.SHARE_USER_INFO);
                break;
            case R.id.follow_count:
                AnalyticsReport.onEvent(mContext, AnalyticsReport.OTHER_USER_INFO_ACTIVITY_FOLLOW_LIST_BUTTON_CLICK_EVENT_ID);
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    EventBusManager.postEvent(0, SubcriberTag.USERINFO_GO_LOGIN);
                    return;
                }
                MyFollowsActivity.startActivity(mContext, mUserInfo.getIdStr(), FollowFragment.FROM_USER_INFO);
                break;
            case R.id.fans_count:
                AnalyticsReport.onEvent(mContext, AnalyticsReport.OTHER_USER_INFO_ACTIVITY_FANS_LIST_BUTTON_CLICK_EVENT_ID);
                if (!AccountInfoManager.getInstance().checkUserIsLogin()) {
                    EventBusManager.postEvent(0, SubcriberTag.USERINFO_GO_LOGIN);
                    return;
                }
                MyFansActivity.startActivity(mContext, mUserInfo.getIdStr(), FollowFragment.FROM_USER_INFO);
                break;
            case R.id.contribution_list:
                ContributionListActivity.startActivity(mContext, mUserInfo.getIdStr(), RankingItemView.FROM_TYPE_USER_INFO);
                AnalyticsReport.onEvent(mContext, AnalyticsReport.OTHER_USER_INFO_ACTIVITY_CONTRIBUTION_LIST_BUTTON_CLICK_EVENT_ID);
                break;
            case R.id.level_value:
                goMyLevel();
                break;
            case R.id.live:
                EventBusManager.postEvent(Course.LIVE, SubcriberTag.USERINFO_TAB_CHANGE);
                break;
            case R.id.video:
                EventBusManager.postEvent(Course.VIDEO, SubcriberTag.USERINFO_TAB_CHANGE);
                break;
            case R.id.news:
                EventBusManager.postEvent(Course.NEWS, SubcriberTag.USERINFO_TAB_CHANGE);
                break;
            default:
                break;
        }
    }

    public void setTabEnable(int type) {
        switch (type) {
            case Course.LIVE:
                if (mLiveTv != null) {
                    mLiveTv.setEnabled(false);
                }
                if (mVideoTv != null) {
                    mVideoTv.setEnabled(true);
                }
                if (mNewsTv != null) {
                    mNewsTv.setEnabled(true);
                }
                break;
            case Course.VIDEO:
                if (mVideoTv != null) {
                    mVideoTv.setEnabled(false);
                }
                if (mLiveTv != null) {
                    mLiveTv.setEnabled(true);
                }
                if (mNewsTv != null) {
                    mNewsTv.setEnabled(true);
                }
                break;
            case Course.NEWS:
                if (mNewsTv != null) {
                    mNewsTv.setEnabled(false);
                }
                if (mVideoTv != null) {
                    mVideoTv.setEnabled(true);
                }
                if (mLiveTv != null) {
                    mLiveTv.setEnabled(true);
                }
                break;
        }
    }
}
