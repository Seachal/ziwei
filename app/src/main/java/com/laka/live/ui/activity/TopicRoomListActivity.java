package com.laka.live.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.homepage.adapter.TopicDetailAdapter;
import com.laka.live.ui.widget.HeadView;
import com.laka.live.ui.widget.PageListLayout;

/**
 * 同一话题的数据列表
 */
public class TopicRoomListActivity extends BaseActivity implements PageListLayout.OnRequestCallBack {

    private static final String EXTRA_TOPIC_ID = "EXTRA_TOPIC_ID";

    private static final String EXTRA_TITLE = "EXTRA_TITLE";

    private static final String EXTRA_FROM = "EXTRA_FROM";

    private static final int LIMIT = 20;

    /**
     * 话题id
     */
    private String mTopicId;

    public static void startActivity(Context activity, String topicId, String title, String from) {
        if (activity != null) {
            Intent intent = new Intent(activity, TopicRoomListActivity.class);
            intent.putExtra(EXTRA_TOPIC_ID, topicId);
            intent.putExtra(EXTRA_TITLE, title);
            intent.putExtra(EXTRA_FROM, from);
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_common_pagelistlayout);
        getWindow().setBackgroundDrawable(null);

        init();
    }

    private void init() {

        Intent intent = getIntent();
        mTopicId = intent.getStringExtra(EXTRA_TOPIC_ID);
        String name = intent.getStringExtra(EXTRA_TITLE);

        HeadView headView = (HeadView) findViewById(R.id.head_view);
        headView.setTitle(name);

        PageListLayout pageListLayout = (PageListLayout) findViewById(R.id.page_list_layout);

        pageListLayout.setLayoutManager(new LinearLayoutManager(this));
        pageListLayout.setIsReloadWhenEmpty(true);
        pageListLayout.setLoadMoreCount(LIMIT);
        pageListLayout.setAdapter(new TopicDetailAdapter(this));
        pageListLayout.setOnRequestCallBack(this);
        pageListLayout.loadData(true);
    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {
        return DataProvider.getTopicRoomList(this, mTopicId, page, LIMIT, listener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        String from = intent.getStringExtra(EXTRA_FROM);
        if (TextUtils.isEmpty(from)) {
            from = "";
        }
        StringBuilder sb = new StringBuilder(from).append(AnalyticsReport.SEPARATOR).append(AnalyticsReport.HOME_VIDEO_TOPIC_VIEW_ID)
                .append(AnalyticsReport.SEPARATOR).append(AnalyticsReport.SHOW_EVENT_ID);
        AnalyticsReport.onEvent(this, sb.toString());
    }
}
