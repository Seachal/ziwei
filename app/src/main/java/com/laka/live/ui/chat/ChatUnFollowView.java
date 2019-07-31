package com.laka.live.ui.chat;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.dao.DbManger;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.manager.RoomManager;
import com.laka.live.ui.widget.HeadView;
import com.laka.live.util.Log;
import com.laka.live.util.ToastHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import laka.live.bean.ChatSession;

/**
 * Created by ios on 16/6/23.
 */
public class ChatUnFollowView extends LinearLayout implements View.OnClickListener,EventBusManager.OnEventBusListener
,ChatSessionAdapter.OnItemContentClickListener{
    protected static final String TAG = "ChatUnFollowView";
    protected static final int MODE_FULL_SCREEN = 0, MODE_HALF_SCREEN = 1;
    public int mode = MODE_FULL_SCREEN;
    protected Context context;
    private View view;
    ChatSessionAdapter adapter;
    RoomManager roomManger;//房间通讯操作类

    /**
     * 初始化控件
     */
    public ChatUnFollowView(Context context) {
        super(context);
        this.context = context;
        initUI();
    }

    public ChatUnFollowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initUI();
    }

    HeadView headView;

    /**
     * 绑定监听
     *
     * @param
     */
    private void initUI() {
        view = LayoutInflater.from(context).inflate(R.layout.view_chat_unfollow, null);
        addView(view);
        headView = (HeadView) findViewById(R.id.header);
        headView.setTipOnClickListener(this);
        headView.setBackOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == MODE_FULL_SCREEN) {
                    Context mContext = headView.mContext;
                    if (mContext instanceof Activity) {
                        ((Activity) mContext).finish();
                    }
                } else {
                    EventBusManager.postEvent(0, SubcriberTag.HIDE_UNFOLLOW_PANEL);

                }
            }
        });
        adapter = new ChatSessionAdapter(context , this);
        ListView listLayout = (ListView) findViewById(R.id.list_layout);
        listLayout.setAdapter(adapter);
        roomManger = RoomManager.getInstance();
        roomManger.startRoom();
    }


    public void refreshData() {
        List<ChatSession> sessions = DbManger.getInstance().getSessions(DbManger.SESSION_TYPE_UNFOLLOW);
        adapter.setList(sessions);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tip:
                AnalyticsReport.onEvent(context, LiveReport.MY_LIVE_EVENT_15005);
                DbManger.getInstance().ingoreUnreadCnt(0);
                List<ChatSession> sessions = DbManger.getInstance().getSessions(DbManger.SESSION_TYPE_UNFOLLOW);
                adapter.setList(sessions);
                break;
        }
    }

    boolean isRegister = false;

    public void start(Activity activity) {
        adapter.setActivity(activity);
        refreshData();
        if (!isRegister) {
            isRegister = true;
            EventBusManager.register(this);
        }

    }

    public void stop() {
        if (isRegister) {
            isRegister = false;
            EventBusManager.unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PostEvent event) {
        Log.d(TAG, "onEvent tag=" + event.tag);
        if (SubcriberTag.RECEIVE_CHAT_GIFT.equals(event.tag)) {
            refreshData();
        } else if (SubcriberTag.RECEIVE_CHAT_MSG.equals(event.tag)) {
            refreshData();
        }else if(SubcriberTag.REFRESH_CHAT_SESSION.equals(event.tag)){
            refreshData();
        }
    }

    @Override
    public void onItemCotentClick(int position) {
        ChatSession session = adapter.getItem(position);
        if(!session.getUserId().equals(AccountInfoManager.getInstance().getCurrentAccountUserIdStr())){
            //更新session未读数为0
            session.setUnreadCnt(0);
            adapter.notifyDataSetChanged();
            if (mode == MODE_FULL_SCREEN) {
                ChatMessageActivity.startPrivateChatActivity((Activity) context, session.getUserId(), session.getNickName(), session.getAvatar(), session.getType());
            } else {
                //进入半屏私信
                EventBusManager.postEvent(session, SubcriberTag.SHOW_MESSAGE_PANEL);
            }
        }else{
            ToastHelper.showToast(R.string.chat_with_self_error_tip);
        }
    }
}
