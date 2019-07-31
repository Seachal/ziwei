package com.laka.live.ui.course;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.TabHost;
import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.Course;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.adapter.TabsAdapter;
import com.laka.live.ui.fragment.CourseListFragment;
import com.laka.live.util.Common;

import java.util.ArrayList;
import java.util.List;

import framework.ioc.annotation.InjectExtra;
import framework.ioc.annotation.InjectView;

/**
 * 最新课程列表
 */
public class NewestCoursesActivity extends BaseActivity implements TabHost.OnTabChangeListener, View.OnClickListener {

    @InjectView(id = android.R.id.tabhost)
    public TabHost mTabHost;
    @InjectView(id = R.id.back)
    public View back;
    @InjectView(id = R.id.viewPager)
    public ViewPager mViewPager;
    private List<View> mTabList;
    public TabsAdapter mTabsAdapter;

    @InjectExtra(name = "courseType" ,def = "1")
    public Integer courseType;

    private String mLastTab;
    private boolean isFirstOnTabChange = true;

    // 直播、视频、资讯
    public static final String LIVE = "live", VIDEO = "video", NEWS = "news", TAB = "tab";


    /**
     * @param curType 打开时切换到哪个分支
     */
    public static void startActivity(Context context, int curType) {
        Intent intent = new Intent(context, NewestCoursesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("courseType", curType);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        initView(savedInstanceState);
    }


    private void initView(Bundle savedInstanceState) {

        initTitle();
        initViewPager(savedInstanceState);
        setCurrentTab(courseType);
    }

    private void initTitle() {

        CheckedTextView mNewsCheckText;
        CheckedTextView mVideoCheckText;
        CheckedTextView mLiveCheckText;

        mNewsCheckText = (CheckedTextView)findViewById(R.id.news_tab);
        mLiveCheckText = (CheckedTextView)findViewById(R.id.live_tab);
        mVideoCheckText = (CheckedTextView)findViewById(R.id.video_tab);

        mNewsCheckText.setOnClickListener(this);
        mLiveCheckText.setOnClickListener(this);
        mVideoCheckText.setOnClickListener(this);

        mTabList = new ArrayList<>();

        mTabList.add(mLiveCheckText);
        mTabList.add(mVideoCheckText);
        mTabList.add(mNewsCheckText);


        mLiveCheckText.setText(R.string.tab_living);
        mVideoCheckText.setText(R.string.tab_video);
        mNewsCheckText.setText(R.string.tab_news);

    }

    private void initViewPager(Bundle savedInstanceState) {

        mTabHost.setup();
        mTabsAdapter = new TabsAdapter(mContext, getSupportFragmentManager(), mTabHost, mViewPager, this);

        Bundle bundleLive = new Bundle();
        bundleLive.putInt(Common.LIST_TYPE, Course.LIVE);
        mTabsAdapter.addTab(mTabHost.newTabSpec(LIVE).setIndicator(getString(R.string.tab_living)),
                CourseListFragment.class, bundleLive);
        Bundle bundleVideo = new Bundle();

        bundleVideo.putInt(Common.LIST_TYPE, Course.VIDEO);
        mTabsAdapter.addTab(mTabHost.newTabSpec(VIDEO).setIndicator(getString(R.string.tab_video)),
                CourseListFragment.class, bundleVideo);
        Bundle bundleNews = new Bundle();
        bundleNews.putInt(Common.LIST_TYPE, Course.NEWS);
        mTabsAdapter.addTab(mTabHost.newTabSpec(NEWS).setIndicator(getString(R.string.tab_news)),
                CourseListFragment.class, bundleNews);

        mViewPager.setOffscreenPageLimit(mTabsAdapter.getCount());

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString(TAB));
        } else {
            mTabHost.setCurrentTabByTag(LIVE);
        }

    }

    @Override
    public void onTabChanged(String tabId) {

        if (!isFirstOnTabChange) {
            if (!TextUtils.isEmpty(mLastTab)) {
                AnalyticsReport.onPageEnd(mLastTab);
            }
            AnalyticsReport.onPageStart(tabId);
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
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.live_tab:
                mTabHost.setCurrentTab(0); // 设置成直播标签
                break;
            case R.id.video_tab:
                mTabHost.setCurrentTab(1);
                break;
            case R.id.news_tab:
                mTabHost.setCurrentTab(2);
                break;
            case R.id.back:
                finish();
                break;
        }

    }


    /**
     * @param courseType 对应Course里的LIVE VIDEO NEWS
     */
    public void setCurrentTab(int courseType) {

        if (mTabHost == null) {
            // 保存下标，等页面初始化完成后再跳
            this.courseType = courseType;
            return;
        }

        // 跳到课堂的子标签
        switch (courseType) {

            case Course.LIVE:
                // 切换到直播标签
                mTabHost.setCurrentTab(0);
                break;
            case Course.VIDEO:
                // 切换到视频标签
                mTabHost.setCurrentTab(1);
                break;
            case Course.NEWS:
                // 切换到资讯标签
                mTabHost.setCurrentTab(2);
                break;
        }

        // 重置下标
        this.courseType = 0;
    }

    // 返回当前页面的类型
    public int getCurrentType() {
        return mTabHost.getCurrentTab() + 1;
    }

}