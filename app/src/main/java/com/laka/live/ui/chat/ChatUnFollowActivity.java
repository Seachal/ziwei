package com.laka.live.ui.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.application.LiveApplication;
import com.laka.live.dao.DbManger;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.manager.BytesReader;
import com.laka.live.manager.OnResultListenerAdapter;
import com.laka.live.manager.RoomManager;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.ui.widget.HeadView;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.ui.widget.gift.GiftGridView;
import com.laka.live.util.Log;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import laka.live.bean.ChatMsg;
import laka.live.bean.ChatSession;

/**
 * Created by crazyguan on 2016/3/30.
 */
public class ChatUnFollowActivity extends BaseActivity {
    private static final String TAG ="ChatUnFollowActivity";
    private ChatUnFollowView chatSessionView;

    public static void startActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, ChatUnFollowActivity.class);
            ActivityCompat.startActivity(activity, intent, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_unfollow);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsReport.onEvent(this, LiveReport.MY_LIVE_EVENT_15004);
        chatSessionView.refreshData();
    }
    private void init() {
        chatSessionView = (ChatUnFollowView) findViewById(R.id.chat_session_view);
        chatSessionView.start(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(chatSessionView!=null){
            chatSessionView.stop();
        }
    }

}
