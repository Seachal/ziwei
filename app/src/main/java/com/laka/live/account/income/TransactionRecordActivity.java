package com.laka.live.account.income;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.TabHost;

import com.laka.live.R;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.adapter.TabsAdapter;
import com.laka.live.ui.fragment.CourseIncomeFragment;
import com.laka.live.ui.fragment.GoodsIncomeFragment;
import com.laka.live.ui.fragment.TransoDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的交易记录（交易明细、课程收益、商品收益）
 */
public class TransactionRecordActivity extends BaseActivity implements View.OnClickListener, TabHost.OnTabChangeListener {

    public static final String TRANSO = "transo";
    public static final String COURSE = "course";
    public static final String GOODS = "goods";

    private TabHost mTabHost;
    private ViewPager mViewPager;
    private List<View> mTabList;
    private TabsAdapter mTabsAdapter;

//    private String mLastTab;
//    private boolean isFirstOnTabChange = true;

    public static void startActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, TransactionRecordActivity.class);
            ActivityCompat.startActivity(activity, intent, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_record);
        getWindow().setBackgroundDrawable(null);

        initTitle();
        initViewPager();

        findViewById(R.id.back_icon).setOnClickListener(this);
    }

    private void initTitle() {
        CheckedTextView mTransoCheckText = (CheckedTextView) findViewById(R.id.transo_tab);
        CheckedTextView mCourseCheckText = (CheckedTextView) findViewById(R.id.course_tab);
        CheckedTextView mGoodsCheckText = (CheckedTextView) findViewById(R.id.goods_tab);

        mTransoCheckText.setOnClickListener(this);
        mCourseCheckText.setOnClickListener(this);
        mGoodsCheckText.setOnClickListener(this);

        mTabList = new ArrayList<>();

        mTabList.add(mTransoCheckText);
        mTabList.add(mCourseCheckText);
        mTabList.add(mGoodsCheckText);

        mTransoCheckText.setText(R.string.transo_info);
        mCourseCheckText.setText(R.string.course_income);
        mGoodsCheckText.setText(R.string.goods_income);

    }

    private void initViewPager() {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTabHost.setup();
        mTabsAdapter = new TabsAdapter(this, getSupportFragmentManager(), mTabHost, mViewPager, this);

        mTabsAdapter.addTab(mTabHost.newTabSpec(TRANSO).setIndicator(getString(R.string.transo_info)),
                TransoDetailFragment.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec(COURSE).setIndicator(getString(R.string.course_income)),
                CourseIncomeFragment.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec(GOODS).setIndicator(getString(R.string.goods_income)),
                GoodsIncomeFragment.class, null);

        mViewPager.setOffscreenPageLimit(mTabsAdapter.getCount());

        mTabHost.setCurrentTabByTag(TRANSO);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.transo_tab:
                mTabHost.setCurrentTab(0);
                break;
            case R.id.course_tab:
                mTabHost.setCurrentTab(1);
                break;
            case R.id.goods_tab:
                mTabHost.setCurrentTab(2);
                break;
            case R.id.back_icon:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        int index = mTabHost.getCurrentTab();
        int i = 0;
        for (View view : mTabList) {
            view.setSelected(i == index);
            i++;
        }
    }
}
