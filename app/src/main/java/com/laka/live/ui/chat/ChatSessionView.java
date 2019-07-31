package com.laka.live.ui.chat;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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
import com.laka.live.util.Utils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import laka.live.bean.ChatSession;

/**
 * Created by ios on 16/6/23.
 */
public class ChatSessionView extends LinearLayout implements View.OnClickListener, EventBusManager.OnEventBusListener
        , ChatSessionAdapter.OnItemContentClickListener {
    protected static final String TAG = "ChatSessionView";
    protected static final int MODE_FULL_SCREEN = 0, MODE_HALF_SCREEN = 1;
    public Context context;
    private View view;
    ChatSessionAdapter adapter;
    ImageView ivUnreadRed;
    public int mode = MODE_FULL_SCREEN;
    RoomManager roomManger;//房间通讯操作类
    View mViewEmpty;

    /**
     * 初始化控件
     */
    public ChatSessionView(Context context) {
        super(context);
        this.context = context;
        initUI();
    }

    public ChatSessionView(Context context, AttributeSet attrs) {
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
        view = LayoutInflater.from(context).inflate(R.layout.view_chat_session, null);
        addView(view);
        ivUnreadRed = (ImageView) findViewById(R.id.iv_red);
        mViewEmpty = findViewById(R.id.view_empty);
        headView = (HeadView) findViewById(R.id.header);
        Context mContext = headView.mContext;
        if (mContext instanceof Activity) {
            headView.setBackShow(true);
        }
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
        findViewById(R.id.rl_unfollow).setOnClickListener(this);
        adapter = new ChatSessionAdapter(context, this);
        ListView listLayout = (ListView) findViewById(R.id.list_layout);
        listLayout.setAdapter(adapter);
    }


    public void refreshData() {
        Log.e(TAG, "refreshData is main : " + (Looper.getMainLooper() == Looper.myLooper()));
        List<ChatSession> sessions = DbManger.getInstance().getSessions(DbManger.SESSION_TYPE_FOLLOW);
        adapter.setList(sessions);

        long unFollowUnreadCnt = DbManger.getInstance().getUnfollowUnreadCnt();
        Log.d(TAG, "unFollowUnreadCnt=" + unFollowUnreadCnt);
        if (unFollowUnreadCnt > 0) {
//            ivUnreadRed.setVisibility(View.VISIBLE);
        } else {
//            ivUnreadRed.setVisibility(View.GONE);
        }

        //查最后一条陌生人消息
        ChatSession lastSession = DbManger.getInstance().getLastStrangerSession();
        if (lastSession != null) {
            lastSession.setUnreadCnt((int) unFollowUnreadCnt);
            lastSession.setType(DbManger.SESSION_TYPE_STRANGER);
            //排序放入列表
            sessions.add(lastSession);
        }

        //排序
        Collections.sort(sessions, new Comparator<ChatSession>() {
            @Override
            public int compare(ChatSession lhs, ChatSession rhs) {
                if (lhs.getDate() > rhs.getDate()) {
                    return -1;
                } else if (lhs.getDate() < rhs.getDate()) {
                    return 1;
                }
                return 0;
            }
        });

        refreshUnreadRed(sessions);

        adapter.notifyDataSetChanged();

        if (Utils.listIsNullOrEmpty(sessions)) {
            mViewEmpty.setVisibility(View.VISIBLE);
        } else {
            mViewEmpty.setVisibility(View.GONE);
        }
    }

    private void refreshUnreadRed(List<ChatSession> sessions) {
        long totalUnreadCnt = 0;
        for (ChatSession item : sessions) {
            totalUnreadCnt += item.getUnreadCnt() == null ? 0 : item.getUnreadCnt();
        }
        EventBusManager.postEvent(totalUnreadCnt, SubcriberTag.REFRESH_BOTTOM_UNREAD_RED);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_unfollow:
//                AnalyticsReport.onEvent((Activity)context, LiveReport.MY_LIVE_EVENT_15002);
//                if(mode==MODE_FULL_SCREEN){
//                    ChatUnFollowActivity.startActivity((Activity)context);
//                }else{
//                    //进入半屏陌生人消息
//                    EventBusManager.postEvent(new PostEvent(0,SubcriberTag.SHOW_UNFOLLOW_PANEL));
//                }

                break;
            case R.id.tip:
                AnalyticsReport.onEvent(getContext(), LiveReport.MY_LIVE_EVENT_15001);
                DbManger.getInstance().ingoreAllUnreadCnt();
                refreshData();
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
        roomManger = RoomManager.getInstance();
        roomManger.startRoom();
    }

    public void stop() {
        if (isRegister) {
            isRegister = false;
            EventBusManager.unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PostEvent event) {
//        Log.d(TAG, "onEvent tag=" + event.tag);
        if (SubcriberTag.RECEIVE_CHAT_GIFT.equals(event.tag)) {
            refreshData();
        } else if (SubcriberTag.RECEIVE_CHAT_MSG.equals(event.tag)) {
            refreshData();
        } else if (SubcriberTag.REFRESH_CHAT_SESSION.equals(event.tag)) {
            refreshData();
        }
    }

    @Override
    public void onItemCotentClick(int position) {
        ChatSession session = adapter.getItem(position);

        Log.d(TAG, " type=" + session.getType() + " sessionId=" + session.getId()
                + " nickname=" + session.getNickName());

        if (session.getType() == DbManger.SESSION_TYPE_STRANGER) {//跳转陌生人消息
            if (mode == MODE_FULL_SCREEN) {
                ChatUnFollowActivity.startActivity((Activity) context);
            } else {
                //进入半屏陌生人消息
                EventBusManager.postEvent(0, SubcriberTag.SHOW_UNFOLLOW_PANEL);
            }

            AnalyticsReport.onEvent(getContext(), LiveReport.MY_LIVE_EVENT_15002);
        } else {
            //更新session未读数为0
            session.setUnreadCnt(0);
            adapter.notifyDataSetChanged();
//                  DbManger.getInstance().updateSession(session);
            if (!session.getUserId().equals(AccountInfoManager.getInstance().getCurrentAccountUserIdStr())) {
                if (mode == MODE_FULL_SCREEN) {
                    if (session.getType() == DbManger.SESSION_TYPE_MISHU) {
                        ChatMessageActivity.startPrivateChatActivity(context, session.getUserId(), session.getNickName(), session.getAvatar(), session.getType(), DbManger.TYPE_CHAT_MISHU_ATTENTION);
                    } else {
                        ChatMessageActivity.startPrivateChatActivity((Activity) context, session.getUserId(), session.getNickName(), session.getAvatar(), session.getType());
                    }
                } else {
                    //进入半屏私信
                    EventBusManager.postEvent(session, SubcriberTag.SHOW_MESSAGE_PANEL);
                }

                AnalyticsReport.onEvent(getContext(), LiveReport.MY_LIVE_EVENT_15003);

                refreshUnreadRed(adapter.getList());
            } else {
                ToastHelper.showToast(R.string.chat_with_self_error_tip);
            }

        }
    }
}
