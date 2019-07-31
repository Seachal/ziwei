package com.laka.live.account.my;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.chat.ChatSessionView;
import com.laka.live.ui.widget.panel.BasePanel;
import com.laka.live.util.Utils;

/**
 * Created by ios on 16/6/27.
 */
public class ContributionListPanel extends BasePanel {

    private static final String TAG = "ContributionListPanel";
    private View parentView;
    private BaseActivity mActivity;
    private ContributionListView mViewContribution;
    private String mUserId;
    private String mFromType;
    public ContributionListPanel(Context context, BaseActivity activity,String userId,String mFromType) {
        super(context);
        this.mActivity = activity;
        setAlpha(0);
        initView();
        this.mUserId = userId;
        this.mFromType = mFromType;
    }

    private void initView() {
        mViewContribution = (ContributionListView) parentView.findViewById(R.id.view_contribution);
        mViewContribution.context = mActivity;
        mViewContribution.mode = ContributionListView.MODE_HALF_SCREEN;
    }

    @Override
    protected View onCreateContentView() {
        parentView = View.inflate(mContext, R.layout.panel_contribution_list, null);
        return parentView;
    }

    @Override
    protected FrameLayout.LayoutParams getLayoutParams() {
        int width = FrameLayout.LayoutParams.MATCH_PARENT;
        int height = Utils.getScreenHeight(mContext) / 2;
        Log.d(TAG, " width=" + width + " height=" + height);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height);
        lp.gravity = Gravity.BOTTOM;
        return lp;
    }

    public void showPanel(Activity activity) {
        AnalyticsReport.onEvent(mContext, LiveReport.MY_LIVE_EVENT_15000);
        mViewContribution.start(activity,mUserId,mFromType);
        super.showPanel();
    }

    public void hidePanel() {
        super.hidePanel();
        mViewContribution.stop();
    }
}
