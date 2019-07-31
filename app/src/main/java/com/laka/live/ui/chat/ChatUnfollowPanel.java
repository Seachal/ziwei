package com.laka.live.ui.chat;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.ui.widget.panel.BasePanel;
import com.laka.live.util.Utils;

/**
 * Created by ios on 16/6/27.
 */
public class ChatUnfollowPanel extends BasePanel {

    private static final String TAG ="ChatUnfollowPanel";
    private View parentView;
    private ChatUnFollowView chatSessionView;
    public ChatUnfollowPanel(Context context) {
        super(context);
        setAlpha(0);
        initView();
    }

    private void initView() {
        chatSessionView = (ChatUnFollowView) parentView.findViewById(R.id.chat_session_view);
        chatSessionView.context = mContext;
        chatSessionView.mode = ChatSessionView.MODE_HALF_SCREEN;
    }

    @Override
    protected View onCreateContentView() {
        parentView = View.inflate(mContext, R.layout.panel_chat_unfollow, null);
        return parentView;
    }

    @Override
    protected FrameLayout.LayoutParams getLayoutParams() {
        int width = FrameLayout.LayoutParams.MATCH_PARENT;
        int height = Utils.getScreenHeight(mContext)/2;
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height);
        lp.gravity = Gravity.BOTTOM;
        return lp;
    }

    public void showPanel(Activity activity) {
        chatSessionView.start(activity);
        super.showPanel();
        AnalyticsReport.onEvent(mContext, LiveReport.MY_LIVE_EVENT_15004);
    }

    public void hidePanel() {
        super.hidePanel();
        chatSessionView.stop();
    }
}
