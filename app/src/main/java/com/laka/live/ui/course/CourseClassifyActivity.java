package com.laka.live.ui.course;


import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.Course;
import com.laka.live.bean.CourseCategoryOneBean;
import com.laka.live.bean.CourseCategoryTwoBean;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.CourseCategoryTwoMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.pop.CourseCategoryPop;
import com.laka.live.shopping.widget.ShoppingCustomRecycler;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.adapter.CourseCategoryAdapter;
import com.laka.live.ui.adapter.TabsAdapter;
import com.laka.live.ui.fragment.CourseClassifyFragment;
import com.laka.live.ui.fragment.CourseListFragment;
import com.laka.live.ui.widget.MoreBtnView;
import com.laka.live.util.Common;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import framework.ioc.annotation.InjectExtra;
import framework.ioc.annotation.InjectView;

/**
 * 分类课程（比如：烘焙基础、烘焙进阶等）
 */
public class CourseClassifyActivity extends BaseActivity implements TabHost.OnTabChangeListener,
        View.OnClickListener, CourseClassifyFragment.onCateVisibleChangedListener {

    @InjectView(id = android.R.id.tabhost)
    public TabHost mTabHost;
    @InjectView(id = R.id.viewPager)
    public ViewPager mViewPager;
    @InjectView(id = R.id.title)
    public TextView titleTv;
    @InjectView(id = R.id.back)
    public ImageView back;
    @InjectView(id = R.id.categoryLayout)
    public View categoryLayout;
    @InjectView(id = R.id.moreClassify)
    public MoreBtnView mMoreClassify;
    @InjectView(id = R.id.category)
    public ShoppingCustomRecycler mCategoryRecycler;


    @InjectExtra(name = "cateId", def = "-1")
    public Integer cateId;
    @InjectExtra(name = "title", def = "课程分类")
    public String title;

    private int viewHeight;
    private String mLastTab;
    private boolean isFirstOnTabChange = true;
    private boolean isAnimating = false;
    private List<View> mTabList;
    private TabsAdapter mTabsAdapter;
    private CourseCategoryAdapter mCategoryAdapter;
    private List<CourseCategoryTwoBean.Category> mCategoryData = new ArrayList<>();

    // 直播、视频、资讯
    public static final String LIVE = "live", VIDEO = "video", NEWS = "news", TAB = "tab";


    public static void startActivity(Context context, String title, int cateId) {
        Intent intent = new Intent(context, CourseClassifyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Common.CATE_ID, cateId);
        bundle.putString("title", title);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_classify);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {

        initTitle();
        initCategory();
        initViewPager(savedInstanceState);
    }

    private void initTitle() {

        titleTv.setText(title);
        CheckedTextView mNewsCheckText;
        CheckedTextView mVideoCheckText;
        CheckedTextView mLiveCheckText;

        mNewsCheckText = (CheckedTextView) findViewById(R.id.news_tab);
        mLiveCheckText = (CheckedTextView) findViewById(R.id.live_tab);
        mVideoCheckText = (CheckedTextView) findViewById(R.id.video_tab);

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

    private void initCategory() {

        mCategoryAdapter = new CourseCategoryAdapter(mContext, mCategoryData);
        mCategoryRecycler.setAdapter(mCategoryAdapter);
        mCategoryRecycler.addItemDecoration(new SpaceItemDecoration(mCategoryData));
        mCategoryAdapter.setCallback(new CourseCategoryAdapter.onItemClickListner() {
            @Override
            public void onItemsClick(CourseCategoryTwoBean.Category category) {
                HashMap<String,String> mEventParams = new HashMap<>();
                mEventParams.put("ID", String.valueOf(category.getId()));
                AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10266, mEventParams);
                EventBusManager.postEvent(category.getId(), SubcriberTag.CHANGED_CATEGORY);
                mCategoryAdapter.notifyDataSetChanged();
            }
        });
        categoryLayout.setTag(true);
        categoryLayout.post(new Runnable() {
            @Override
            public void run() {
                viewHeight = categoryLayout.getMeasuredHeight();
            }
        });

        getCategoryData();

    }

    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private List<CourseCategoryTwoBean.Category> mData;

        public SpaceItemDecoration(List<CourseCategoryTwoBean.Category> mData) {
            this.mData = mData;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            int pos = parent.getChildAdapterPosition(view);

            outRect.right = 0;
            outRect.top = 0;
            outRect.bottom = 0;
            outRect.left = 0;
            if (mData.size() - 1 == pos) {
                outRect.right = ResourceHelper.getDimen(R.dimen.space_35);
            }

        }
    }

    private void initViewPager(Bundle savedInstanceState) {

        mTabHost.setup();
        mTabsAdapter = new TabsAdapter(mContext, getSupportFragmentManager(), mTabHost, mViewPager, this);

        mTabsAdapter.addTab(mTabHost.newTabSpec(LIVE).setIndicator(getString(R.string.tab_living)),
                CourseClassifyFragment.class, getBundle(Course.LIVE));
        mTabsAdapter.addTab(mTabHost.newTabSpec(VIDEO).setIndicator(getString(R.string.tab_video)),
                CourseClassifyFragment.class, getBundle(Course.VIDEO));
        mTabsAdapter.addTab(mTabHost.newTabSpec(NEWS).setIndicator(getString(R.string.tab_news)),
                CourseClassifyFragment.class, getBundle(Course.NEWS));
        mViewPager.setOffscreenPageLimit(mTabsAdapter.getCount());

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString(TAB));
        } else {
            mTabHost.setCurrentTabByTag(LIVE);
        }

    }


    private Bundle getBundle(int listType) {

        Bundle bundle = new Bundle();
        bundle.putInt(Common.CATE_ID, cateId);
        bundle.putBoolean("isShowTopDivider", false);
        bundle.putInt(Common.LIST_TYPE, listType);

        return bundle;
    }

    private void getCategoryData() {

        DataProvider.getCourseCategoryTwo(this, cateId, new GsonHttpConnection.OnResultListener<CourseCategoryTwoMsg>() {
            @Override
            public void onSuccess(CourseCategoryTwoMsg courseCategoryTwoMsg) {
                if (courseCategoryTwoMsg != null && courseCategoryTwoMsg.getData()
                        != null && courseCategoryTwoMsg.getData().getCategory() != null) {
                    mMoreClassify.setVisibility(View.VISIBLE);
                    mCategoryData.addAll(courseCategoryTwoMsg.getData().getCategory());
                    mCategoryAdapter.notifyDataSetChanged();

                } else {
                    mMoreClassify.setVisibility(View.GONE);
                    ToastHelper.showToast("没有当前的分类课程");
                }

            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                ToastHelper.showToast(errorMsg);
            }

        });

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

        if (mTabsAdapter.getCurrentTab(index) != null) {
            ((CourseClassifyFragment) mTabsAdapter.getCurrentTab(index)).onTabChanged();
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
            case R.id.moreClassify:
                CourseCategoryPop mClassifyPop = CourseCategoryPop.getCourseClassifyTwoPop(mContext, mCategoryData, mCategoryAdapter.getSelectedPosition());
                mClassifyPop.setOnVisibleChangedListener(mMoreClassify);
                mClassifyPop.showAsDropDown(findById(R.id.divider));
                break;
            case R.id.back:
                finish();
                break;
        }

    }

    // 返回当前页面的类型
    public int getCurrentType() {
        return mTabHost.getCurrentTab() + 1;
    }

    @Override
    public void onCateVisibleChanged(boolean isVisible, int which) {
        boolean curState = (boolean) categoryLayout.getTag();

        if (isAnimating() || curState == isVisible || which != getCurrentType()) {
            return;
        }
        doAnimation(categoryLayout, isVisible);
    }


    public void doAnimation(final View mView, final boolean isOpen) {
        setAnimating(true);
        categoryLayout.setTag(isOpen);
        final double countTime = 300.0;
        new CountDownTimer((long) countTime, 1) {

            @Override
            public void onTick(long millisUntilFinished) {
                double rate = millisUntilFinished / countTime;
                final int tempHeight = isOpen ? (int) (viewHeight - viewHeight * rate) : (int) (viewHeight * rate);
                ViewGroup.LayoutParams layoutParams = mView.getLayoutParams();
                layoutParams.height = tempHeight;
                mView.setLayoutParams(layoutParams);
            }

            @Override
            public void onFinish() {
                final int tempHeight = isOpen ? viewHeight : 0;
                ViewGroup.LayoutParams layoutParams = mView.getLayoutParams();
                layoutParams.height = tempHeight;
                mView.setLayoutParams(layoutParams);
                setAnimating(false);
            }

        }.start();

    }

    public synchronized boolean isAnimating() {
        return isAnimating;
    }

    public synchronized void setAnimating(boolean animating) {
        isAnimating = animating;
        EventBusManager.postEvent(animating, SubcriberTag.START_ANIMATING);
    }

    @Subscribe
    @Override
    public void onEvent(PostEvent event) {
        if (TextUtils.equals(event.getTag(), SubcriberTag.CHANGED_CATEGORY)) {
            cateId = (int) event.getEvent();
            mCategoryAdapter.setSelectedCateId(cateId);
        }
    }

}