package com.laka.live.ui.widget.room;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.application.LiveApplication;
import com.laka.live.bean.ListUserInfo;
import com.laka.live.bean.UserInfo;
import com.laka.live.dao.DbManger;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.Msg;
import com.laka.live.msg.UserMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.activity.UserInfoActivity;
import com.laka.live.ui.room.roommanagerlist.LiveRoomAdminManager;
import com.laka.live.ui.room.roommanagerlist.LiveRoomManagerListPanel;
import com.laka.live.ui.widget.LevelText;
import com.laka.live.ui.widget.MarkSimpleDraweeView;
import com.laka.live.ui.widget.dialog.GenericDialog;
import com.laka.live.ui.widget.dialog.IDialogOnClickListener;
import com.laka.live.ui.widget.dialog.SimpleTextDialog;
import com.laka.live.ui.widget.panel.ActionSheetPanel;
import com.laka.live.ui.widget.panel.BasePanel;
import com.laka.live.util.CacheUtil;
import com.laka.live.util.Common;
import com.laka.live.util.FastBlur;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ThreadManager;
import com.laka.live.util.Utils;

import java.util.HashMap;

import laka.live.bean.ChatSession;

/**
 * Created by zwl on 2016/6/24.
 * Email-1501448275@qq.com
 */
public class LiveRoomUserInfoPanel extends BasePanel {
    private static final String TAG = "LiveRoomUserInfoPanel";

    private static final String CACHE_KEY_LIVE_HOST = "curZhuboUserInfo";
    private static final int DEFAULT_ERROR_DATA = -1;
    private static final int ACTION_ITEM_FORBIDDEN = 0;
    private static final int ACTION_ITEM_MANAGER = 1;
    private static final int ACTION_ITEM_MANAGER_LIST = 2;
    private static final int ACTION_ITEM_REPORT = 4;
    public static final int USER_TYPE_USER = 0;
    public static final int USER_TYPE_MANAGER = 1;
    public static final int USER_TYPE_LIVE_HOST = 2;

    private View parentView;
    private ImageView btnPopClose, ivSex;
    private MarkSimpleDraweeView ivHead;
    private TextView tvPopName, tvPopSign, tvPopArea, tvFansCnt;
    private TextView btnPopFollow, btnPopChat, btnPopHomepage;//AlphaTextView
    private TextView mTextUserId, mTextSendCount, mTextCoinsCount, mTextVerified;
    private TextView mReportButton;
    private LevelText tvPopLevel;
    private LinearLayout mVerifyLayout;
    private RelativeLayout mRlTop;
    private HashMap<Integer, UserInfo> mCacheUserInfoList = new HashMap<>();
    private UserInfo userInfo;
    private String mCurrentUserId;
    public String mLivingHostIdentifier;
    private boolean mCurrentUserIsHost = false;
    private boolean mIsReplayLive = false;
    private String mRoomId;
    private String mCourseId;
    private String mButtonType;
    private OnLiveRoomListener mListener;
    private BaseActivity mActivity;
    private SimpleDraweeView mTopBg;//顶部高斯模糊背景

    public LiveRoomUserInfoPanel(Context context, BaseActivity activity,String mCourseId) {
        super(context);
        this.mActivity = activity;
        this.mCourseId = mCourseId;
        setAlpha(0);
        setAnimation(R.style.LiveRoomPanelAnim);
        initView();
    }

    @Override
    protected View onCreateContentView() {
        parentView = View.inflate(mContext, R.layout.live_room_user_info_panel_layout, null);
        return parentView;
    }

    public void setRotation(boolean isPortrait) {
        this.isPortrait = isPortrait;
        if (isPortrait) {
            parentView.setRotation(0);
        } else {
            parentView.setRotation(90);
        }
    }

    boolean isPortrait;
    public void setRotation(boolean isPortrait,boolean isCreate,boolean isFrontCamera) {
        this.isPortrait = isPortrait;
        if (isPortrait) {
            parentView.setRotation(0);
        } else {
            if(!isCreate&&isFrontCamera){
                parentView.setRotation(-90);
            }else{
                parentView.setRotation(90);
            }

        }
    }

    @Override
    protected FrameLayout.LayoutParams getLayoutParams() {
        int width = FrameLayout.LayoutParams.MATCH_PARENT;
        int height = FrameLayout.LayoutParams.WRAP_CONTENT;
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height);
        lp.gravity = Gravity.CENTER;
        return lp;
    }

    public void showPanel(String userId, String headUrl) {
        if (StringUtils.isEmpty(userId)) {
            return;
        }
        setHeadUrl(headUrl);
        initUserInfoData(userId);
        showPanel();
    }

    public void setHeadUrl(String headUrl) {
        if (StringUtils.isEmpty(headUrl)) {
            return;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setAvatar(headUrl);
        setHeadImage(userInfo);
    }

    private void initView() {
        mRlTop = (RelativeLayout) parentView.findViewById(R.id.rl_top);
        mTopBg = (SimpleDraweeView) parentView.findViewById(R.id.iv_top_bg);
        mTextUserId = (TextView) parentView.findViewById(R.id.user_id_text);
        mVerifyLayout = (LinearLayout) parentView.findViewById(R.id.ll_renzheng);
        ivSex = (ImageView) parentView.findViewById(R.id.iv_sex);
        ivHead = (MarkSimpleDraweeView) parentView.findViewById(R.id.iv_head);
        tvPopName = (TextView) parentView.findViewById(R.id.tv_name);
        tvPopSign = (TextView) parentView.findViewById(R.id.tv_sign);
        tvPopArea = (TextView) parentView.findViewById(R.id.tv_area);
        tvFansCnt = (TextView) parentView.findViewById(R.id.tv_fans_cnt);
        mReportButton = (TextView) parentView.findViewById(R.id.btn_report);
        btnPopFollow = (TextView) parentView.findViewById(R.id.tv_follow);
        btnPopChat = (TextView) parentView.findViewById(R.id.tv_chat);
        btnPopHomepage = (TextView) parentView.findViewById(R.id.tv_homepage);
        tvPopLevel = (LevelText) parentView.findViewById(R.id.tv_level);
        btnPopClose = (ImageView) parentView.findViewById(R.id.btn_close);
        mTextCoinsCount = (TextView) parentView.findViewById(R.id.coins_count_text);
        mTextSendCount = (TextView) parentView.findViewById(R.id.send_count_text);
        mTextVerified = (TextView) parentView.findViewById(R.id.user_verified);
        btnPopHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnUserInfoButtonClick();
            }
        });
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnReportButtonClick();
            }
        });
        btnPopChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnChatButtonClick();
            }
        });
        btnPopFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnFollowButtonClick();
            }
        });
        btnPopClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidePanel();
            }
        });

        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/AFFOGATO-BOLD.OTF");
        mTextCoinsCount.setTypeface(typeface);
        mTextSendCount.setTypeface(typeface);
        tvFansCnt.setTypeface(typeface);

        ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInfo == null) {
                    return;
                }
//                String idString = AccountInfoManager.getInstance().getCurrentAccountUserIdStr();
//                if (TextUtils.isEmpty(idString)) {
//                    return;
//                }
//                if (TextUtils.isEmpty(mCurrentUserId)) {
//                    return;
//                }
//                if (checkCurrentLoginUserIsLiveHost()) {
//                    AnalyticsReport.onEvent(mContext, LiveReport.MY_LIVE_EVENT_11226);
//                } else {
//                    AnalyticsReport.onEvent(mContext, LiveReport.MY_LIVE_EVENT_11241);
//                }
                hidePanel();
                UserInfoActivity.startActivity(mActivity, userInfo.getIdStr());
            }
        });
    }

    private void initUserInfoData(String userId) {
        mCurrentUserId = userId;
        if (mCacheUserInfoList.containsKey(Integer.parseInt(mCurrentUserId))) {
            userInfo = mCacheUserInfoList.get(Integer.parseInt(mCurrentUserId));
            if (checkCurrentUserIsManager()) {
                userInfo.setSadmin(true);
            } else {
                userInfo.setSadmin(false);
            }
            refreshUI();
        } else {
            resetUI();
            tryGetUserInfo();
        }
    }

    private void refreshUI() {
        if (userInfo == null) {
            return;
        }
        mCurrentUserId = userInfo.getIdStr();
        setUserData();
    }

    private void setUserData() {
        setUserIdText(userInfo);
        setNickName(userInfo);
        setUserSign(userInfo);
        setUserArea(userInfo);
        setUserFansCount(userInfo);
        setHeadImage(userInfo);
        setUserLevel(userInfo);
        setUserSex(userInfo);
        setCoinsCountText(userInfo);
        setSendCountText(userInfo);
        setUserVerified(userInfo);
        setFollowButton(userInfo);
        setReportButton(userInfo);
        startBlurImage(userInfo.getAvatar());
    }

    private void setUserIdText(UserInfo userInfo) {
        if (userInfo == null) {
            mTextUserId.setText(String.valueOf(ResourceHelper.getString(R.string.user_info_user_id_start_tips) + "0"));
            return;
        }
        mTextUserId.setText(String.valueOf(ResourceHelper.getString(R.string.user_info_user_id_start_tips)
                + userInfo.getId()));
    }

    private void setUserVerified(UserInfo userInfo) {

        RelativeLayout.LayoutParams lpTopBg = (RelativeLayout.LayoutParams) mTopBg.getLayoutParams();
        lpTopBg.width = LiveApplication.screenWidth - Utils.dip2px(mContext, 80);


        if (userInfo == null) {
            mVerifyLayout.setVisibility(View.GONE);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mRlTop.getLayoutParams();
            lp.height = Utils.dip2px(mContext, 171);
            lpTopBg.height = Utils.dip2px(mContext, 171);
            mRlTop.setLayoutParams(lp);
            Log.d(TAG, "没认证");
            return;
        }
        if (StringUtils.isEmpty(userInfo.getVerifyInfo())) {
            mVerifyLayout.setVisibility(View.GONE);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mRlTop.getLayoutParams();
            lp.height = Utils.dip2px(mContext, 171);
            lpTopBg.height = Utils.dip2px(mContext, 171);
            mRlTop.setLayoutParams(lp);
            Log.d(TAG, "没认证");
            return;
        }
        String verified = "";
        if (MarkSimpleDraweeView.getAuthType(userInfo.getStarVerified(), userInfo.getVerified())
                == MarkSimpleDraweeView.AuthType.STAR) {
            verified = ResourceHelper.getString(R.string.verified_start_start_tip) + userInfo.getVerifyInfo();
        } else if (MarkSimpleDraweeView.getAuthType(userInfo.getStarVerified(), userInfo.getVerified())
                == MarkSimpleDraweeView.AuthType.NORMAL) {
            verified = ResourceHelper.getString(R.string.verified_normal_start_tip) + userInfo.getVerifyInfo();
        }
        mVerifyLayout.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mRlTop.getLayoutParams();
        lp.height = Utils.dip2px(mContext, 204);
        lpTopBg.height = Utils.dip2px(mContext, 204);
        mRlTop.setLayoutParams(lp);
        Log.d(TAG, "有认证");

        mTopBg.setLayoutParams(lpTopBg);

        mTextVerified.setText(verified);
    }

    private void setSendCountText(UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        mTextSendCount.setText(String.valueOf(userInfo.getGiveCoins()));
    }

    private void setCoinsCountText(UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        mTextCoinsCount.setText(String.valueOf(userInfo.getTotalCoins()));
    }

    private void setUserFansCount(UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        tvFansCnt.setText(String.valueOf(userInfo.getFans()));
    }

    private void setUserArea(UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        tvPopArea.setText(!Utils.isEmpty(userInfo.getRegion()) ?
                userInfo.getRegion() : ResourceHelper.getString(R.string.user_pop_default_area)
        );
    }

    private void setUserLevel(UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        tvPopLevel.setLevel(userInfo.getLevel());
    }

    private void setUserSex(UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        ivSex.setImageResource(
                userInfo.getGender() == ListUserInfo.GENDER_GIRL ?
                        R.drawable.mine_icon_women : R.drawable.mine_icon_men);
    }

    private void setUserSign(UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        tvPopSign.setText(
                !Utils.isEmpty(userInfo.getDescription()) ?
                        userInfo.getDescription() : ResourceHelper.getString(R.string.user_pop_default_sign)
        );
    }

    private void setNickName(UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        if (StringUtils.isEmpty(userInfo.getNickName())) {
            return;
        }
        tvPopName.setText(userInfo.getNickName());
    }

    private void setHeadImage(UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        if (Utils.isEmpty(userInfo.getAvatar())) {
            ivHead.setImageURI(Uri.parse(
                    ResourceHelper.getString(R.string.fresco_image_res_head) + R.drawable.blank_icon_avatar));
            return;
        }
        int markId = MarkSimpleDraweeView
                .getMarkDrawableId(
                        MarkSimpleDraweeView.getAuthType(
                                userInfo.getAuth()), MarkSimpleDraweeView.SizeType.BIG, userInfo.getLevel()
                );
        ivHead.setMark(markId);
        ImageUtil.loadImage(ivHead, userInfo.getAvatar());
    }

    private void setFollowButton(UserInfo zhuboUserInfo) {
        if (userInfo == null) {
            return;
        }
        if (zhuboUserInfo.getFollow() == ListUserInfo.FOLLOWED) {
            btnPopFollow.setText(R.string.followed);
            btnPopFollow.setTextColor(ResourceHelper.getColor(R.color.color333333));
//            btnPopFollow.setBackgroundResource(R.drawable.live_room_other_button_bg_selector);
//            btnPopFollow.setSelected(true);
            return;
        }
        btnPopFollow.setText(R.string.follow);
        btnPopFollow.setTextColor(ResourceHelper.getColor(R.color.colorF65843));
//        btnPopFollow.setBackgroundResource(R.drawable.live_room_follow_button_bg_selector);
        btnPopFollow.setSelected(false);
    }

    private void setReportButton(UserInfo userInfo) {
        mReportButton.setVisibility(View.VISIBLE);
        if (userInfo == null) {
            return;
        }
        //自己隐藏按钮
        if (userInfo.getIdStr().equals(AccountInfoManager.getInstance().getCurrentAccountUserIdStr())) {
            mReportButton.setVisibility(View.GONE);
            return;
        }


        if (mIsReplayLive) {
            mReportButton.setText(ResourceHelper.getString(R.string.report));
            mButtonType = "1";
            return;
        }
        if (checkCurrentLoginUserStatus() == USER_TYPE_USER) {
            mReportButton.setText(ResourceHelper.getString(R.string.report));
            mButtonType = "1";
            return;
        }
        if (checkCurrentLoginUserStatus() == USER_TYPE_MANAGER) {
            if (mCurrentUserIsHost) {
                mReportButton.setText(ResourceHelper.getString(R.string.report));
                mButtonType = "1";
            } else {
                if (checkCurrentUserIsLoginUser()) {
                    mReportButton.setVisibility(View.GONE);
                } else {
                    mReportButton.setVisibility(View.VISIBLE);
                    mReportButton.setText(ResourceHelper.getString(R.string.live_manager_to_limit_comment));
                    mButtonType = "2";
                }
            }
            return;
        }
        if (checkCurrentLoginUserStatus() == USER_TYPE_LIVE_HOST) {
            if (userInfo.isAdministrator()) {
                mReportButton.setText(ResourceHelper.getString(R.string.live_manager));
                mButtonType = "5";
            } else {
                mReportButton.setText(ResourceHelper.getString(R.string.live_manager_setting));
                mButtonType = "4";
            }
            return;
        }
    }

    private void resetUI() {
        userInfo = null;
        mTextSendCount.setText("0");
        tvFansCnt.setText("0");
        mVerifyLayout.setVisibility(View.GONE);
        mTextCoinsCount.setText("0");
        tvPopArea.setText("");
        tvPopName.setText(ResourceHelper.getString(R.string.nick_name));
        tvPopSign.setText("");
        tvPopLevel.setLevel(0);
        ivHead.setImageURI(Uri.parse(
                ResourceHelper.getString(R.string.fresco_image_res_head) + R.drawable.blank_icon_avatar));
        ImageUtil.loadResImage(R.drawable.mine_face_bg, mTopBg);
    }

    public void setCurrentUserIsHost(boolean isHost) {
        mCurrentUserIsHost = isHost;
    }

    public void setIsReplayLive(boolean isReplayLive) {
        mIsReplayLive = isReplayLive;
    }

    public void setLivingHostId(String livingHostId) {
        mLivingHostIdentifier = livingHostId;
    }

    public void setRoomId(String roomId) {
        this.mRoomId = roomId;
    }

    private boolean checkCurrentLoginUserIsManager() {
        return AccountInfoManager.getInstance().getCurrentAccountAdminState();
    }

    private boolean checkCurrentLoginUserIsLiveHost() {
        boolean isHost = false;
        String currentLoginUserId = AccountInfoManager.getInstance().getCurrentAccountUserIdStr();
        if (TextUtils.isEmpty(currentLoginUserId)) {
            return false;
        }
        if (TextUtils.isEmpty(mLivingHostIdentifier)) {
            return false;
        }
        if (mLivingHostIdentifier.equals(currentLoginUserId)) {
            isHost = true;
        }
        return isHost;
    }

    private boolean checkCurrentUserIsLoginUser() {
        boolean isSelf = false;
        String currentLoginUserId = AccountInfoManager.getInstance().getCurrentAccountUserIdStr();
        if (TextUtils.isEmpty(currentLoginUserId)) {
            return false;
        }
        if (TextUtils.isEmpty(mCurrentUserId)) {
            return false;
        }
        if (mCurrentUserId.equals(currentLoginUserId)) {
            isSelf = true;
        }
        return isSelf;
    }

    private int checkCurrentLoginUserStatus() {
        int userStatus = USER_TYPE_USER;
        if (checkCurrentLoginUserIsManager()) {
            userStatus = USER_TYPE_MANAGER;
            return userStatus;
        }
        if (checkCurrentLoginUserIsLiveHost()) {
            userStatus = USER_TYPE_LIVE_HOST;
            return userStatus;
        }
        return userStatus;
    }

    private boolean checkCurrentUserIsManager() {
        boolean isManager = false;
        for (UserInfo userInfo : LiveRoomAdminManager.getInstance().getAdministrators()) {
            if (userInfo.getIdStr().equals(mCurrentUserId)) {
                isManager = true;
                break;
            }
        }
        return isManager;
    }

    public void updateUserAdminState(String userId, boolean state) {
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        if (mCacheUserInfoList.containsKey(Integer.parseInt(userId))) {
            mCacheUserInfoList.get(Integer.parseInt(userId)).setSadmin(state);
        }
        if (userInfo == null) {
            return;
        }
        if (userId.equals(userInfo.getIdStr())) {
            userInfo.setSadmin(state);
        }
    }

    public void updateUserForbiddenState(String userId, boolean state) {
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        if (mCacheUserInfoList.containsKey(Integer.parseInt(userId))) {
            mCacheUserInfoList.get(Integer.parseInt(userId)).setForbid_say(state);
        }
        if (userInfo == null) {
            return;
        }
        if (userId.equals(userInfo.getIdStr())) {
            userInfo.setForbid_say(state);
        }
    }

    public void updateUserFollowState(String userId, int followState) {
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        if (mCacheUserInfoList.containsKey(Integer.parseInt(userId))) {
            mCacheUserInfoList.get(Integer.parseInt(userId)).setFollow(followState);
        }
    }

    private void tryGetUserInfo() {
        DataProvider.getUserInfo(this, mCurrentUserId, mRoomId, true, new GsonHttpConnection.OnResultListener<UserMsg>() {
            @Override
            public void onSuccess(UserMsg userMsg) {
                handleOnRequestUserInfoSuccess(userMsg);
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                handleOnRequestUserInfoFailed(errorCode, errorMsg);
            }
        });
    }

    private void handleOnRequestUserInfoSuccess(UserMsg userMsg) {
        if (!userMsg.isSuccessFul()) {
            handleOnRequestUserInfoFailed(DEFAULT_ERROR_DATA, "");
            return;
        }
        UserInfo info = userMsg.getUserInfo();
        if (info == null) {
            handleOnRequestUserInfoFailed(DEFAULT_ERROR_DATA, "");
            return;
        }
        mCacheUserInfoList.put(info.getId(), info);
        userInfo = info;
        refreshUI();
    }

    //错误处理
    private void handleOnRequestUserInfoFailed(int errorCode, String errorString) {
        resetUI();
    }

    private void handleOnReportButtonClick() {
        HashMap<String, String> params = new HashMap<>();
        params.put("UserBotton_type", mButtonType);
        if (checkCurrentLoginUserIsLiveHost()) {
            AnalyticsReport.onEvent(mContext, LiveReport.MY_LIVE_EVENT_11225, params);
        } else {
            AnalyticsReport.onEvent(mContext, LiveReport.MY_LIVE_EVENT_11242, params);
        }
        if (userInfo == null) {
            return;
        }
        if (!mIsReplayLive && !mCurrentUserIsHost && checkCurrentLoginUserStatus() != USER_TYPE_USER) {
            tryOpenManagerToolsSheetPanel();
            return;
        }
        hidePanel();

        if(isPortrait){
            String tips = ResourceHelper.getString(R.string.user_info_sure_report_start_tip)
                    + (mCurrentUserIsHost ? ResourceHelper.getString(R.string.user_info_sure_report_live_tip)
                    : ResourceHelper.getString(R.string.user_info_sure_report_user_tip))
                    + ResourceHelper.getString(R.string.user_info_sure_report_end_tip);
            final SimpleTextDialog simpleTextDialog = new SimpleTextDialog(mContext);
            simpleTextDialog.setTitle(ResourceHelper.getString(R.string.report));
            simpleTextDialog.setText(tips);
            simpleTextDialog.addYesNoButton();
            simpleTextDialog.setOnClickListener(new IDialogOnClickListener() {
                @Override
                public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                    if (viewId == GenericDialog.ID_BUTTON_YES) {
                        report(mCurrentUserId);
                    }
                    return false;
                }
            });
            simpleTextDialog.setRecommendButton(GenericDialog.ID_BUTTON_YES);
            simpleTextDialog.setCanceledOnTouchOutside(false);
            simpleTextDialog.show();
        }else{
            //横屏举报
            EventBusManager.postEvent(mCurrentUserId,SubcriberTag.SHOW_REPORT_DIALOG_LAND);
        }

    }

    private void tryOpenManagerToolsSheetPanel() {
        hidePanel();
        ActionSheetPanel panel = new ActionSheetPanel(mContext);
        ActionSheetPanel.ActionSheetItem item;
        item = new ActionSheetPanel.ActionSheetItem();
        item.colorResId = R.color.black;//colorFF5353
        if (userInfo.isForbid_say()) {
            item.title = ResourceHelper.getString(R.string.live_manager_cancel_limit_comment);
        } else {
            item.title = ResourceHelper.getString(R.string.live_manager_to_limit_comment);
        }
        item.id = String.valueOf(ACTION_ITEM_FORBIDDEN);
        panel.addSheetItem(item);

        item = new ActionSheetPanel.ActionSheetItem();
        if (checkCurrentLoginUserStatus() == USER_TYPE_LIVE_HOST) {
            if (!userInfo.isAdministrator()) {
                item.title = ResourceHelper.getString(R.string.live_manager_to_be_manager);
            } else {
                item.title = ResourceHelper.getString(R.string.live_manager_cancel_manager);
            }
            item.id = String.valueOf(ACTION_ITEM_MANAGER);
        } else if (checkCurrentLoginUserStatus() == USER_TYPE_MANAGER) {
            item.title = ResourceHelper.getString(R.string.report);
            item.id = String.valueOf(ACTION_ITEM_REPORT);
        }
        panel.addSheetItem(item);

        if (checkCurrentLoginUserStatus() == USER_TYPE_LIVE_HOST) {
            item = new ActionSheetPanel.ActionSheetItem();
            item.title = ResourceHelper.getString(R.string.live_manager_list_title);
            item.id = String.valueOf(ACTION_ITEM_MANAGER_LIST);
            panel.addSheetItem(item);
        }
        panel.setSheetItemClickListener(new ActionSheetPanel.OnActionSheetClickListener() {
            @Override
            public void onActionSheetItemClick(String id) {
                if (id == null) {
                    return;
                }
                if (id.equals(String.valueOf(ACTION_ITEM_REPORT))) {
                    handleOnReportActionSheetItemClick();
                } else {
                    handleOnActionSheetItemClick(StringUtils.parseInt(id));
                }
            }
        });
        panel.showPanel();
    }

    private void handleOnReportActionSheetItemClick() {
        HashMap<String, String> params = new HashMap<>();
        params.put("UserBotton_type", mButtonType);
        if (checkCurrentLoginUserIsLiveHost()) {
            AnalyticsReport.onEvent(mContext, LiveReport.MY_LIVE_EVENT_11225, params);
        } else {
            AnalyticsReport.onEvent(mContext, LiveReport.MY_LIVE_EVENT_11242, params);
        }
        hidePanel();
        if(isPortrait){
            String reportName = (
                    mCurrentUserIsHost ?
                            ResourceHelper.getString(R.string.user_info_sure_report_live_tip) : (
                            StringUtils.isEmpty(userInfo.getNickName()) ?
                                    ResourceHelper.getString(R.string.user_info_sure_report_user_tip) :
                                    userInfo.getNickName())
            );
            String tips = ResourceHelper.getString(R.string.user_info_sure_report_start_tip) + reportName
                    + ResourceHelper.getString(R.string.user_info_sure_report_end_tip);
            final SimpleTextDialog simpleTextDialog = new SimpleTextDialog(mContext);
            simpleTextDialog.setTitle(ResourceHelper.getString(R.string.report));
            simpleTextDialog.setText(tips);
            simpleTextDialog.addYesNoButton();
            simpleTextDialog.setOnClickListener(new IDialogOnClickListener() {
                @Override
                public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                    if (viewId == GenericDialog.ID_BUTTON_YES) {
                        report(mCurrentUserId);
                    }
                    return false;
                }
            });
            simpleTextDialog.setRecommendButton(GenericDialog.ID_BUTTON_YES);
            simpleTextDialog.setCanceledOnTouchOutside(false);
            simpleTextDialog.show();
        }else{
                //横屏举报
                EventBusManager.postEvent(mCurrentUserId,SubcriberTag.SHOW_REPORT_DIALOG_LAND);
        }
    }

    private void handleOnActionSheetItemClick(int position) {
        if (position == ACTION_ITEM_MANAGER_LIST) {
            handleOnClickManagerListButton();
            return;
        }
        if (position == ACTION_ITEM_FORBIDDEN) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    handleOnForbiddenItemClick();
                }
            }, 500);
            return;
        }
        if (position == ACTION_ITEM_MANAGER) {
            if (mListener != null && userInfo != null) {
                if (userInfo.isAdministrator()) {
                    mListener.onClickManagerButton(userInfo, true);
                } else {
                    mListener.onClickManagerButton(userInfo, false);
                }
            }
        }
    }

    private void handleOnClickManagerListButton() {
        LiveRoomManagerListPanel liveRoomManagerListPanel = new LiveRoomManagerListPanel(mContext);
        liveRoomManagerListPanel.showPanel();
    }

    private void handleOnForbiddenItemClick() {
        if (userInfo == null || TextUtils.isEmpty(userInfo.getNickName())) {
            return;
        }
        if (userInfo.isForbid_say()) {
            if (mListener != null) {
                mListener.onClickCancelForbiddenButton(mCurrentUserId);
            }
            return;
        }
        String contentTip = ResourceHelper.getString(R.string.live_manager_forbidden_tip);
        if (TextUtils.isEmpty(contentTip)) {
            return;
        }
        hidePanel();
        contentTip = contentTip.replace("$nickname$", userInfo.getNickName());
        final SimpleTextDialog simpleTextDialog = new SimpleTextDialog(mContext);
        simpleTextDialog.setTitle(ResourceHelper.getString(R.string.live_manager_to_limit_comment));
        simpleTextDialog.setText(contentTip);
        simpleTextDialog.addYesNoButton();
        simpleTextDialog.setOnClickListener(new IDialogOnClickListener() {
            @Override
            public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                if (viewId == GenericDialog.ID_BUTTON_YES) {
                    if (mListener != null) {
                        mListener.onClickForbiddenButton(mCurrentUserId);
                    }
                }
                return false;
            }
        });
        simpleTextDialog.setRecommendButton(GenericDialog.ID_BUTTON_YES);
        simpleTextDialog.setCanceledOnTouchOutside(false);
        simpleTextDialog.show();
    }

    private void handleOnUserInfoButtonClick() {
        if (userInfo == null) {
//            mActivity.showToast("请稍候");
            return;
        }
        String idString = AccountInfoManager.getInstance().getCurrentAccountUserIdStr();
        if (TextUtils.isEmpty(idString)) {
            return;
        }
        if (TextUtils.isEmpty(mCurrentUserId)) {
            return;
        }
        if (checkCurrentLoginUserIsLiveHost()) {
            AnalyticsReport.onEvent(mContext, LiveReport.MY_LIVE_EVENT_11226);
        } else {
            AnalyticsReport.onEvent(mContext, LiveReport.MY_LIVE_EVENT_11241);
        }
        hidePanel();
        UserInfoActivity.startActivity(mActivity, userInfo.getIdStr());
    }

    //处理私信按钮的点击
    private void handleOnChatButtonClick() {
        if (userInfo == null) {
            mActivity.showToast("请稍候");
            return;
        }
        String idString = AccountInfoManager.getInstance().getCurrentAccountUserIdStr();
        if (TextUtils.isEmpty(idString)) {
            mActivity.showToast("用户信息不存在");
            Log.log("用户信息不存在-1");
            return;
        }
        if (TextUtils.isEmpty(mCurrentUserId)) {
            mActivity.showToast("用户信息不存在");
            Log.log("用户信息不存在-2");
            return;
        }
        int userId = AccountInfoManager.getInstance().getCurrentAccountUserId();
        if (userId == Integer.parseInt(mCurrentUserId)) {
            mActivity.showToast(mActivity.getResources().getString(R.string.chat_with_self_error_tip));
            return;
        }
        if (checkCurrentLoginUserIsLiveHost()) {
            AnalyticsReport.onEvent(mContext, LiveReport.MY_LIVE_EVENT_11224);
        } else {
            AnalyticsReport.onEvent(mContext, LiveReport.MY_LIVE_EVENT_11240);
        }

        hidePanel();
        //改为半屏
        ChatSession session = new ChatSession();
        session.setType(DbManger.SESSION_TYPE_UNFOLLOW);
        session.setUserId(userInfo.getIdStr());
        session.setNickName(userInfo.getNickName());
        session.setAvatar(userInfo.getAvatar());
        EventBusManager.postEvent(session, SubcriberTag.SHOW_MESSAGE_PANEL_CHAT);
    }

    //处理关注按钮的点击
    private void handleOnFollowButtonClick() {
        if (userInfo == null) {
            mActivity.showToast("请稍候");
            return;
        }
        String idString = AccountInfoManager.getInstance().getCurrentAccountUserIdStr();
        if (TextUtils.isEmpty(idString)) {
            return;
        }
        if (checkCurrentLoginUserIsLiveHost()) {
            AnalyticsReport.onEvent(mContext, LiveReport.MY_LIVE_EVENT_11223);
        } else {
            AnalyticsReport.onEvent(LiveApplication.getInstance(), LiveReport.MY_LIVE_EVENT_11239);
        }
        if (userInfo.getFollow() == ListUserInfo.FOLLOWED) {
            //取消关注
//            unFollow();
        } else {
            //去关注
            follow();
        }
    }

    private void report(String userId) {
        DataProvider.report(this, userId, mCourseId, Common.COURSE,new GsonHttpConnection.OnResultListener<Msg>() {
            @Override
            public void onSuccess(Msg msg) {
                mActivity.showToast(mActivity.getString(R.string.report_success));
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                mActivity.showToast(mActivity.getString(R.string.report_success));
            }
        });
    }

    private void unFollow() {
        DataProvider.unFollow(this, userInfo.getId(), new GsonHttpConnection.OnResultListener<Msg>() {
            @Override
            public void onSuccess(Msg msg) {
                userInfo.setFollow(ListUserInfo.NO_FOLLOW);
                setFollowButton(userInfo);
                if (userInfo.getIdStr().equals(mCurrentUserId)) {
                    CacheUtil.addCache(CACHE_KEY_LIVE_HOST, userInfo);
                }
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                mActivity.showToast(R.string.cancel_follow_fail);
            }
        });

    }

    private void follow() {
        if (TextUtils.isEmpty(mCurrentUserId)) {
            return;
        }
        int userId = AccountInfoManager.getInstance().getCurrentAccountUserId();
        if (userId == Integer.parseInt(mCurrentUserId)) {
            mActivity.showToast(mActivity.getResources().getString(R.string.follow_self_error_tip));
            return;
        }
        DataProvider.follow(this, userInfo.getId(), new GsonHttpConnection.OnResultListener<Msg>() {
            @Override
            public void onSuccess(Msg msg) {
                userInfo.setFollow(ListUserInfo.FOLLOWED);
                setFollowButton(userInfo);
                if (userInfo.getIdStr().equals(mCurrentUserId)) {
                    CacheUtil.addCache(CACHE_KEY_LIVE_HOST, userInfo);
                }
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                mActivity.showToast(R.string.follow_fail);
            }
        });
    }

    public void removeExitRoomUser(int userId) {
        if (mCacheUserInfoList.containsKey(userId)) {
            mCacheUserInfoList.remove(userId);
        }
    }

    public void setOnLiveRoomListener(OnLiveRoomListener liveRoomListener) {
        this.mListener = liveRoomListener;
    }

    public interface OnLiveRoomListener {
        void onClickForbiddenButton(String userId);

        void onClickCancelForbiddenButton(String userId);

        void onClickManagerButton(UserInfo userInfo, boolean isCancel);
    }

    private int mReloadBlurImageCount;
    private Bitmap mBlurBitmap = null;
    private static final int BLUR_RADIUS = 20;
    private static final int MAX_BLUR_RELOAD_COUNT = 1;

    private void startBlurImage(String url) {
//        ImageUtil.getBitmapByUrl(url, mTopBg, new IBitmapListener() {
//            @Override
//            public void onSuccess(Bitmap bitmap) {
//                if (bitmap == null) {
//                    return;
//                }
//                bitmap = FastBlur.doBlur(bitmap, BLUR_RADIUS, true);
//                mBlurBitmap = bitmap;
//            }
//
//            @Override
//            public void onFail(int code) {
//
//            }
//        });
        ImageUtil.loadImage(mTopBg, url, null, true, new BasePostprocessor() {
            @Override
            public String getName() {
                return "redMeshPostprocessor";
            }

            @Override
            public void process(Bitmap bitmap) {
                if (bitmap == null) {
                    return;
                }
//                Log.d(TAG,"BasePostprocessor 拿到bitmap图片 高斯模糊处理 bitmap="+Utils.getBitmapsize(bitmap));
//                mBlurBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                bitmap = FastBlur.doBlur(bitmap, BLUR_RADIUS, true);
                mBlurBitmap = bitmap;
//                handleOnBlurImageSuccess(mBlurBitmap);
                /* 在这里进行图片处理 */
            }
        });

    }

    private void handleOnBlurImageSuccess(Bitmap bitmap) {
        if (mBlurBitmap == null) {
            return;
        }
        if (mBlurBitmap.isRecycled()) {
            return;
        }
        ThreadManager.post(ThreadManager.THREAD_WORK, new Runnable() {
            @Override
            public void run() {
                mBlurBitmap = FastBlur.doBlur(mBlurBitmap, BLUR_RADIUS, true);
                Log.d(TAG, "高斯模糊成功");
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        mTopBg.setImageBitmap(tmpBmp);
                    }
                });
            }
        });
    }

    private void handleOnBlurImageFailed() {
        mReloadBlurImageCount++;
        ThreadManager.post(ThreadManager.THREAD_UI, new Runnable() {
            @Override
            public void run() {
                ImageUtil.loadResImage(R.drawable.mine_face_bg, mTopBg);
            }
        });
        if (mReloadBlurImageCount >= MAX_BLUR_RELOAD_COUNT) {
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startBlurImage(userInfo.getAvatar());
            }
        }, 100);
    }

    public void recycleBlurBitmap() {
        if (mBlurBitmap != null && !mBlurBitmap.isRecycled()) {
            mBlurBitmap.recycle();
            mTopBg.setImageBitmap(null);
        }
    }
}
