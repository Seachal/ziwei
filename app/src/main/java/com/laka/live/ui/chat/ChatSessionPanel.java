package com.laka.live.ui.chat;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
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
public class ChatSessionPanel extends BasePanel {

    private static final String TAG = "ChatSessionPanel";
    private View parentView;
    private ChatSessionView chatSessionView;

    public ChatSessionPanel(Context context) {
        super(context);
        setAlpha(0);
        initView();
    }

    private void initView() {
        chatSessionView = (ChatSessionView) parentView.findViewById(R.id.chat_session_view);
        chatSessionView.context = mContext;
        chatSessionView.mode = ChatSessionView.MODE_HALF_SCREEN;
    }

    @Override
    protected View onCreateContentView() {
        parentView = View.inflate(mContext, R.layout.panel_chat_session, null);
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
        chatSessionView.start(activity);
        super.showPanel();
    }

    public void hidePanel() {
        super.hidePanel();
        chatSessionView.stop();
    }
}
