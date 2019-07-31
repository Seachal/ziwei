package com.laka.live.ui.chat;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.laka.live.R;
import com.laka.live.dao.DbManger;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.widget.panel.BasePanel;
import com.laka.live.util.Utils;

/**
 * Created by ios on 16/6/27.
 */
public class ChatMessagePanel extends BasePanel {

    private static final String TAG ="ChatMessagePanel";
    private View parentView;
    private BaseActivity mActivity;
    private ChatMessageView chatSessionView;
    public ChatMessagePanel(Context context, BaseActivity activity) {
        super(context);
        this.mActivity = activity;
        setAlpha(0);
        setAnimation(R.style.LiveRoomPanelAnim);
        initView();
    }

    private void initView() {
        chatSessionView = (ChatMessageView) parentView.findViewById(R.id.message_view);
        chatSessionView.context =mActivity;
        chatSessionView.setMode(ChatSessionView.MODE_HALF_SCREEN);
    }

    @Override
    protected View onCreateContentView() {
        parentView = View.inflate(mContext, R.layout.panel_chat_message, null);
        return parentView;
    }

    @Override
    protected FrameLayout.LayoutParams getLayoutParams() {
        int width = FrameLayout.LayoutParams.MATCH_PARENT;
        int height = FrameLayout.LayoutParams.MATCH_PARENT;
//        int height = Utils.getScreenHeight(mContext)/2;
        Log.d(TAG," width="+width+" height="+height);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height);
        lp.gravity = Gravity.BOTTOM;
        return lp;
    }

    public void showPanel(String otherUserId,String otherNickName,String otherAvatar) {
        chatSessionView.initData(mActivity,otherUserId,otherNickName,otherAvatar, DbManger.SESSION_TYPE_UNFOLLOW);
        chatSessionView.start();
        super.showPanel();
    }



    public void hidePanel() {
        super.hidePanel();
        chatSessionView.stop();
    }
}
