package com.laka.live.account.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.ListUserInfo;
import com.laka.live.msg.Msg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.ui.adapter.RecommendUserListAdapter;
import com.laka.live.ui.widget.HeadView;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.util.ToastHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendActivity extends BaseActivity implements PageListLayout.OnRequestCallBack
        , BaseAdapter.OnItemClickListener {

    private PageListLayout mListLayout;
    private RecommendUserListAdapter mAdapter;
    private HeadView headView;

    public static void startActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, RecommendActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        AnalyticsReport.onEvent(this, AnalyticsReport.RECOMMEND_VIEW_LOOK);
        headView = (HeadView) findViewById(R.id.head);
        headView.setBackShow(false);
        headView.setTipOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsReport.onEvent(RecommendActivity.this, AnalyticsReport.RECOMMEND_IGNORE_CLICK);
                AnalyticsReport.onEvent(RecommendActivity.this, AnalyticsReport.RECOMMEND_ACTIVITY_SKIP_EVENT_ID);
                finish();
            }
        });

        mListLayout = (PageListLayout) findViewById(R.id.recommend_list);
        mAdapter = new RecommendUserListAdapter(this);

        int tipId = R.string.my_follow_null_tip;
        mListLayout.setIsLoadMoreEnable(false);
        mListLayout.setIsReloadWhenEmpty(true);
        mAdapter = new RecommendUserListAdapter(this);
        mAdapter.setOnItemClickListener(this);
        mListLayout.setAdapter(mAdapter);
        mListLayout.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        mListLayout.setOnRequestCallBack(this);
        mListLayout.loadData();
    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {
        return DataProvider.recommendList(this, listener);
    }

    @Override
    public void onItemClick(int position) {
        mAdapter.onItemClick(position);
    }

    public void batchFollow(View v) {

        AnalyticsReport.onEvent(this, AnalyticsReport.RECOMMEND_FOLLOW_ALL_CLICK);

        List<ListUserInfo> list = mAdapter.getCheckedUserList();
        List<Integer> ids = new ArrayList<>();
        for (ListUserInfo userInfo : list) {
            ids.add(userInfo.getId());
        }
        DataProvider.batchFollow(this, ids, new GsonHttpConnection.OnResultListener<Msg>() {
            @Override
            public void onSuccess(Msg msg) {
                finish();
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                ToastHelper.showToast("关注失败！");
            }
        });
        Map<String, String> map = new HashMap<>();
        map.put(AnalyticsReport.KEY_FOLLOW_COUNT, ids.size() + "");
        AnalyticsReport.onEvent(RecommendActivity.this, AnalyticsReport.RECOMMEND_ACTIVITY_FOLLOW_EVENT_ID, map);
    }

    @Override
    public void onBackPressed() {
        //屏蔽返回键
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsReport.onEvent(this, AnalyticsReport.RECOMMEND_ACTIVITY_SHOW_EVENT_ID);
    }
}
