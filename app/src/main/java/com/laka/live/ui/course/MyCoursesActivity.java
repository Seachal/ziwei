package com.laka.live.ui.course;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.TabHost;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.bean.Course;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.adapter.TabsAdapter;
import com.laka.live.ui.fragment.MyCourseFragment;
import com.laka.live.util.ShareUtil;

import java.util.ArrayList;
import java.util.List;

import framework.ioc.annotation.InjectExtra;
import framework.ioc.annotation.InjectView;

/**
 * 已购买与已发布的课堂通用页
 */
public class MyCoursesActivity extends BaseActivity implements View.OnClickListener,
        TabHost.OnTabChangeListener {

    private static final String TAG = "MyCoursesActivity";
    public static final String LIVING = "living";
    public static final String VIDEO = "video";
    public static final String NEWS = "news";
    private static final String TAB = "tab";

    @InjectView(id = R.id.title)
    public TextView title;
    @InjectView(id = R.id.rightText)
    public TextView rightText;
    @InjectView(id = R.id.delete)
    public TextView delete;

    private TabHost mTabHost;
    private ViewPager mViewPager;
    private TabsAdapter mTabsAdapter;
    private CheckedTextView mVideoCheckText;
    private CheckedTextView mLiveCheckText;
    private CheckedTextView mNewsCheckText;
    private String mLastTab;
    private List<View> mTabList;
    private boolean isFirstOnResume = true;
    private boolean isFirstOnTabChange = true;

    @InjectExtra(name = "code", def = "1")
    public Integer status;
    @InjectExtra(name = "index" ,def ="0")
    public Integer index;

    // 1 是已发布 2 是已购买
    public static final int MYLIVE = 1, SUBSCRIBELIVE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_released);

        mTabHost = findById(android.R.id.tabhost);
        mTabHost.setup();

        mViewPager = findById(R.id.pager);
        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager, this);

        init();

        Bundle bundle = new Bundle();
        bundle.putString("status", String.valueOf(status));
        bundle.putInt("course_type", Course.LIVE);
        mTabsAdapter.addTab(mTabHost.newTabSpec(LIVING).setIndicator(getString(R.string.tab_living)),
                MyCourseFragment.class, bundle);

        bundle = new Bundle();
        bundle.putString("status", String.valueOf(status));
        bundle.putInt("course_type", Course.VIDEO);
        mTabsAdapter.addTab(mTabHost.newTabSpec(VIDEO).setIndicator(getString(R.string.tab_video)),
                MyCourseFragment.class, bundle);

        if (status == MYLIVE) {
            title.setText("已发布");
            rightText.setText("编辑");
            rightText.setVisibility(View.VISIBLE);
            bundle = new Bundle();
            bundle.putString("status", String.valueOf(status));
            bundle.putInt("course_type", Course.NEWS);
            mTabsAdapter.addTab(mTabHost.newTabSpec(NEWS).setIndicator(getString(R.string.tab_news)),
                    MyCourseFragment.class, bundle);
        } else {
            title.setText("已购买");
        }

        mViewPager.setOffscreenPageLimit(mTabsAdapter.getCount());

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString(TAB));
        } else {
            mTabHost.setCurrentTabByTag(LIVING);
        }

        // 默认选中index
        mTabHost.setCurrentTab(index);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                rightText.setText("编辑");
                delete.setVisibility(View.GONE);
                EventBusManager.postEvent(null, SubcriberTag.RELEASED_LIVING_CANCEL);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstOnResume == false) {
            if (TextUtils.isEmpty(mLastTab) == false) {
                AnalyticsReport.onPageStart(mLastTab);

                if (TextUtils.equals(VIDEO, mLastTab)) {
                    AnalyticsReport.onEvent(this, AnalyticsReport.HOME_FOUND_SHOW_EVENT_ID);
                } else if (TextUtils.equals(LIVING, mLastTab)) {
                    AnalyticsReport.onEvent(this, AnalyticsReport.HOME_SHOW_EVENT_ID);
                }
            }
        }

        isFirstOnResume = false;

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (!TextUtils.isEmpty(mLastTab)) {
            AnalyticsReport.onPageEnd(mLastTab);
        }
    }

    private void init() {
        findViewById(R.id.back).setOnClickListener(this);

        mVideoCheckText = (CheckedTextView) findViewById(R.id.video_tab);
        mLiveCheckText = (CheckedTextView) findViewById(R.id.live_tab);
        mNewsCheckText = (CheckedTextView) findViewById(R.id.news_tab);

        mVideoCheckText.setOnClickListener(this);
        mLiveCheckText.setOnClickListener(this);

        mTabList = new ArrayList<>();
        mTabList.add(mLiveCheckText);
        mTabList.add(mVideoCheckText);

        mVideoCheckText.setText(R.string.tab_video);
        mLiveCheckText.setText(R.string.tab_living);

        if(status == MYLIVE) {
            mTabList.add(mNewsCheckText);
            mNewsCheckText.setOnClickListener(this);
            mNewsCheckText.setText(R.string.tab_news);
        }else{
            mNewsCheckText.setVisibility(View.GONE);
            mVideoCheckText.setBackgroundResource(R.drawable.right_corners_5_solid_black);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TAB, mTabHost.getCurrentTabTag());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rightText:
                setRightText();
                break;
            case R.id.live_tab:
                mTabHost.setCurrentTabByTag(LIVING);
                if (status == SUBSCRIBELIVE) {
                    AnalyticsReport.onEvent(this, LiveReport.MY_LIVE_EVENT_15451);
                } else {
                    AnalyticsReport.onEvent(this, LiveReport.MY_LIVE_EVENT_15455);
                }
                break;
            case R.id.video_tab:
                mTabHost.setCurrentTabByTag(VIDEO);
                if (status == SUBSCRIBELIVE) {
                    AnalyticsReport.onEvent(this, LiveReport.MY_LIVE_EVENT_15453);
                } else {
                    AnalyticsReport.onEvent(this, LiveReport.MY_LIVE_EVENT_15459);
                }
                break;
            case R.id.news_tab:
                mTabHost.setCurrentTabByTag(NEWS);
                break;
            case R.id.delete:
                // 删除选中课堂
                EventBusManager.postEvent(mTabHost.getCurrentTab() + 1, SubcriberTag.RELEASED_LIVING_DELETE);
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    public void setRightText() {
        // 编辑
        if (rightText.getText().equals("编辑")) {
            rightText.setText("关闭");
            delete.setVisibility(View.VISIBLE);
            if (mTabHost.getCurrentTab() == 2) {
                delete.setText("删除资讯");
            } else {
                delete.setText("取消课程");
            }
            EventBusManager.postEvent(null, SubcriberTag.RELEASED_LIVING_EDITED);
            AnalyticsReport.onEvent(this, LiveReport.MY_LIVE_EVENT_15456);

        } else {
            rightText.setText("编辑");
            delete.setVisibility(View.GONE);
            EventBusManager.postEvent(null, SubcriberTag.RELEASED_LIVING_CANCEL);
        }
    }

    @Override
    public void onTabChanged(String tabId) {

        if (!isFirstOnTabChange) {
            if (!TextUtils.isEmpty(mLastTab)) {
                AnalyticsReport.onPageEnd(mLastTab);
            }
            AnalyticsReport.onPageStart(tabId);

            if (TextUtils.equals(VIDEO, tabId)) {
                AnalyticsReport.onEvent(this, AnalyticsReport.HOME_FOUND_SHOW_EVENT_ID);
            } else if (TextUtils.equals(LIVING, tabId)) {
                AnalyticsReport.onEvent(this, AnalyticsReport.HOME_SHOW_EVENT_ID);
            }

            mLastTab = tabId;
        }

        isFirstOnTabChange = false;

        int index = mTabHost.getCurrentTab();
        int i = 0;
        for (View view : mTabList) {
            view.setSelected(i == index);
            i++;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareUtil.getInstance().destory();
    }



}
