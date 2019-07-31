package com.laka.live.account;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.ui.room.ConnectMicManager;
import com.laka.live.util.Log;

/**
 * Created by luwies on 16/10/28.
 */

public class OnlineUserInfoItemView extends ConnectMicUserInfoItemView {

    private static final String TAG = "OnlineUserInfoItemView";

    public OnlineUserInfoItemView(Context context) {
        this(context, null);
    }

    public OnlineUserInfoItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OnlineUserInfoItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mFollowButton.setTextColor(ContextCompat.getColor(mContext, R.color.colorEC5B4F));
        mFollowButton.setBackgroundResource(R.drawable.ranking_follow_button_bg_selector);
        mFollowButton.setText(R.string.invite_connect_mic);
    }

    @Override
    public void setConnectBtn() {
        if (mConnectUserInfo == null) {
            return;
        }
        if (AccountInfoManager.getInstance().getCurrentAccountUserId() ==
                mConnectUserInfo.getId() || mConnectUserInfo.isInConnectMicList()) {
            mFollowButton.setVisibility(GONE);
        } else {
            mFollowButton.setVisibility(VISIBLE);
        }
    }

    @Override
    protected void handleOnFollowButtonClick() {
        mConnectUserInfo.setInConnectMicList(true);
        setConnectBtn();
        connectMicManager.addConnectMic(mConnectUserInfo.getIdStr());

        AnalyticsReport.onEvent(mContext, LiveReport.MY_LIVE_EVENT_11266);
    }

}
