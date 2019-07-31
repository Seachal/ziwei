package com.laka.live.ui.rankinglist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.adapter.TabsAdapter;

/**
 * Created by zwl on 2016/6/27.
 * Email-1501448275@qq.com
 */
public class RankingListActivity extends BaseActivity implements View.OnClickListener, TabHost.OnTabChangeListener{

    private static final String FLAG_ACCEPT = "ACCEPT";

    private static final String FLAG_FANS = "FANS";

    private static final String FLAG_SEND = "SEND";

    private static final String TAB = "tab";

    private boolean isFirstResume = true;
    private boolean isFirstOnTabChange = true;
    private String mLastTab;

    private TabHost mTabHost;
    private ViewPager mViewPager;
    private TabsAdapter mTabsAdapter;

    private ViewGroup mTabParent;

    public static void startRankingListActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, RankingListActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        init();

        mViewPager.setOffscreenPageLimit(mTabsAdapter.getCount());
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString(TAB));
        } else {
            mTabHost.setCurrentTabByTag(FLAG_ACCEPT);
        }
        mLastTab = mTabHost.getCurrentTabTag();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TAB, mTabHost.getCurrentTabTag());
    }

    private void init() {

        findViewById(R.id.back_icon).setOnClickListener(this);
        findViewById(R.id.accept).setOnClickListener(this);
        findViewById(R.id.fans).setOnClickListener(this);
        findViewById(R.id.send).setOnClickListener(this);

        mTabParent = (ViewGroup) findViewById(R.id.tab_parent);

        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        mViewPager = (ViewPager) findViewById(R.id.pager);

        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager, this);

        mTabsAdapter.addTab(mTabHost.newTabSpec(FLAG_ACCEPT).setIndicator(getString(R.string.ranking_accept_tab_title)),
                RankFragment.class, createBundle(RankingListConstant.RANKING_TAB_TYPE_ACCEPT, 0));
        mTabsAdapter.addTab(mTabHost.newTabSpec(FLAG_FANS).setIndicator(getString(R.string.ranking_fans_tab_title)),
                RankFragment.class, createBundle(RankingListConstant.RANKING_TAB_TYPE_FANS, 1));
        mTabsAdapter.addTab(mTabHost.newTabSpec(FLAG_SEND).setIndicator(getString(R.string.ranking_send_tab_title)),
                RankFragment.class, createBundle(RankingListConstant.RANKING_TAB_TYPE_SEND, 2));
    }

    private Bundle createBundle(int type, int index) {
        Bundle args = new Bundle();
        args.putInt(RankFragment.TYPE, type);
        args.putInt(RankFragment.INDEX, index);
        return args;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_icon:
                finish();
                break;
            case R.id.accept:
                mTabHost.setCurrentTabByTag(FLAG_ACCEPT);
                break;
            case R.id.fans:
                mTabHost.setCurrentTabByTag(FLAG_FANS);
                break;
            case R.id.send:
                mTabHost.setCurrentTabByTag(FLAG_SEND);
                break;
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        int index = mTabHost.getCurrentTab();
        if (isFirstOnTabChange == false) {
            if (TextUtils.isEmpty(mLastTab) == false) {
                AnalyticsReport.onPageEnd(mLastTab);
            }
            AnalyticsReport.onPageStart(tabId);

            reportResume(index);

            EventBusManager.postEvent(index, SubcriberTag.LOAD_RANKING_LIST_DATA);
        }
        mLastTab = tabId;

        isFirstOnTabChange = false;

        int size = mTabParent.getChildCount();
        View child;
        for (int i = 0; i < size; i++) {
            child = mTabParent.getChildAt(i);
            child.setSelected(i == index);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstResume == false) {
            reportResume(mTabHost.getCurrentTab());
        }
        isFirstResume = false;
        AnalyticsReport.onPageStart(mLastTab);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AnalyticsReport.onPageEnd(mLastTab);
    }

    private void reportResume(int index) {
        String id = "";
        switch (index) {
            case 0:
                id = AnalyticsReport.HOME_RANK_GET_SHOW_ID;
                break;
            case 1:
                id = AnalyticsReport.HOME_RANK_FANS_SHOW_ID;
                break;
            case 2:
                id = AnalyticsReport.HOME_RANK_SEND_SHOW_ID;
                break;
        }
        if (!TextUtils.isEmpty(id)) {
            AnalyticsReport.onEvent(RankingListActivity.this, id);
        }
    }
}
