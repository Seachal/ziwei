package com.laka.live.account;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.bean.ConnectUserInfo;
import com.laka.live.bean.ListUserInfo;
import com.laka.live.ui.room.ConnectMicManager;
import com.laka.live.ui.widget.MarkSimpleDraweeView;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;

/**
 * Created by luwies on 16/10/27.
 */

public class ConnectMicUserInfoItemView extends UserInfoItemView implements View.OnClickListener {

    protected ConnectUserInfo mConnectUserInfo;

    public ConnectMicUserInfoItemView(Context context) {
        this(context, null);
    }

    public ConnectMicUserInfoItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConnectMicUserInfoItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        MarginLayoutParams params = (MarginLayoutParams) mFollowButton.getLayoutParams();
        params.width = Utils.dip2px(mContext, 68f);

        mFollowButton.setOnClickListener(this);
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mFollowButton, null, null, null, null);

        mIsLivingView.setVisibility(GONE);

        mUserNameView.setMaxWidth(Utils.dip2px(mContext, 145f));
    }

    public void setUserInfoData(ConnectUserInfo userInfo) {

        if (userInfo == null) {
            return;
        }
        mUserInfo = userInfo;
        mConnectUserInfo = userInfo;

        setHeadIconView(userInfo);
        setUserNameView(userInfo);
//        setUserLevelView(userInfo);
        setUserSignView(userInfo);
//        setUserSexView(userInfo);
        setConnectBtn();
    }

    protected void setHeadIconView(ListUserInfo userInfo) {
        int markId = MarkSimpleDraweeView.getMarkDrawableId(MarkSimpleDraweeView.getAuthType(userInfo.getAuth()), MarkSimpleDraweeView.SizeType.BIG);
        mHeadIconView.setMark(markId);
        ImageUtil.loadImage(mHeadIconView, userInfo.getAvatar());
    }

    protected void setConnectBtn() {

        if (mConnectUserInfo == null) {
            return;
        }
        int state = mConnectUserInfo.getState();

        switch (state) {
            case ConnectUserInfo.STATE_ANCHOR_WAITING_CONFIRM:
                updateWaitingState(R.string.waiting_for_connect_mic);
                break;
            case ConnectUserInfo.STATE_WAITING_SUCCESS:
                updateWaitingState(R.string.connectting_mic);
                break;
            case ConnectUserInfo.STATE_AUDIENCE_WAITING_CONFIRM:
            case ConnectUserInfo.STATE_NONE:
            case ConnectUserInfo.STATE_CONNECT_FAIL:
                updateNoneState();
                break;
            case ConnectUserInfo.STATE_CONNECT_SUCCESS:
                updateSucessState();
                break;
        }

        if (state != ConnectUserInfo.STATE_WAITING_SUCCESS) {
            mFollowButton.setOnClickListener(this);
        } else {
            mFollowButton.setOnClickListener(null);
            mFollowButton.setClickable(false);
        }
    }

    private void updateWaitingState(int textId) {
        mFollowButton.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        mFollowButton.setBackgroundResource(R.drawable.connect_gray_bg);
        mFollowButton.setText(textId);
    }

    private void updateNoneState() {
        mFollowButton.setTextColor(ContextCompat.getColor(mContext, R.color.colorEC5B4F));
        mFollowButton.setBackgroundResource(R.drawable.ranking_follow_button_bg_selector);
        mFollowButton.setText(R.string.confirm_connect_mic);
    }

    private void updateSucessState() {
        mFollowButton.setTextColor(ContextCompat.getColor(mContext, R.color.colorEC5B4F));
        mFollowButton.setBackgroundResource(R.drawable.ranking_follow_button_bg_selector);
        mFollowButton.setText(R.string.close_connect);
    }

    protected void handleOnFollowButtonClick() {
        if (mConnectUserInfo == null) {
            return;
        }
        int state = mConnectUserInfo.getState();
        switch (state) {
            case ConnectUserInfo.STATE_NONE:
            case ConnectUserInfo.STATE_CONNECT_FAIL:
            case ConnectUserInfo.STATE_AUDIENCE_WAITING_CONFIRM:
                //邀请连麦
                handleNoneStateClick();
                break;
            case ConnectUserInfo.STATE_ANCHOR_WAITING_CONFIRM:
                handleWaitingStateClick();
                break;
            case ConnectUserInfo.STATE_WAITING_SUCCESS:
                //展示用户信息'
                break;
            case ConnectUserInfo.STATE_CONNECT_SUCCESS:
                handleSuccessStateClick();
                break;
        }
    }

    private void handleNoneStateClick() {

        AnalyticsReport.onEvent(mContext, LiveReport.MY_LIVE_EVENT_11262);
//        if (connectMicManager.isInConnectMic()) {
//            //已经正在连麦了，那就提示一下
//            ToastHelper.showToast(R.string.in_connect_mic_tip);
//        } else {
            //TLV_REQ_CONFIRM_LINK_MIC
            connectMicManager.zhuboConfirm(mConnectUserInfo.getIdStr());
//        }
    }

    private void handleWaitingStateClick() {
        ToastHelper.showToast(R.string.click_connect_mic_waiting_state_tip);
    }

    private void handleSuccessStateClick() {
        //去断开连麦 TLV_REQ_UNICAST_LINK_MIC
        connectMicManager.closeConnectMic(mConnectUserInfo.getIdStr(),mConnectUserInfo.getNickName() );
    }

    ConnectMicManager connectMicManager;

    public void setConnectMicManager(ConnectMicManager connectMicManager) {
        this.connectMicManager = connectMicManager;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.follow_text:
                handleOnFollowButtonClick();
                break;
        }
    }
}
