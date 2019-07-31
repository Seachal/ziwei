package com.laka.live.ui.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.dao.DbManger;
import com.laka.live.manager.BytesReader;
import com.laka.live.manager.OnResultListenerAdapter;
import com.laka.live.manager.RoomManager;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.widget.HeadView;
import com.laka.live.ui.widget.gift.GiftGridView;
import com.laka.live.util.Log;

import java.util.List;

import laka.live.bean.ChatMsg;
import laka.live.bean.ChatSession;

/**
 * Created by crazyguan on 2016/3/30.
 * 私信
 */
public class ChatHomeActivity extends BaseActivity  {

    private static final String TAG = "ChatHomeActivity";
    private ChatSessionView chatSessionView;

    public static void startActivity(Context context) {
        if (context != null) {
            Intent intent = new Intent(context, ChatHomeActivity.class);
            ActivityCompat.startActivity(context, intent, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_home);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG," onResume");
        AnalyticsReport.onEvent(this, LiveReport.MY_LIVE_EVENT_15000);
        chatSessionView.refreshData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG," onRestart");

    }

    private void init() {
        chatSessionView = (ChatSessionView) findViewById(R.id.chat_session_view);
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
