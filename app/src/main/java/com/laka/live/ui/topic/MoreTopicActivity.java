package com.laka.live.ui.topic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.ui.widget.PageListLayout;

/**
 * Created by zwl on 2016/6/29.
 * Email-1501448275@qq.com
 */
public class MoreTopicActivity extends BaseActivity {

    private TopicListAdapter mAdapter;

    private final static int LIMIT = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_topic_layout);
        initView();
    }

    public static void startActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, MoreTopicActivity.class);
        activity.startActivity(intent);
    }

    private void initView() {
        PageListLayout topicList = (PageListLayout) findViewById(R.id.topics_list);
        mAdapter = new TopicListAdapter(this);
        topicList.setAdapter(mAdapter);
        topicList.setLayoutManager(new LinearLayoutManager(this));
        topicList.setOnRequestCallBack(new PageListLayout.OnRequestCallBack() {
            @Override
            public String request(int page, GsonHttpConnection.OnResultListener listener) {
                return DataProvider.queryTopicList(this, page, LIMIT, listener);
            }
        });
        topicList.loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsReport.onEvent(this, AnalyticsReport.HOME_FOUND_MORE_TOPIC_ACTIVITY_SHOW_ID);
    }
}
