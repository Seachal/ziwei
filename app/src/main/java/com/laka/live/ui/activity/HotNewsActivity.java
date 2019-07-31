package com.laka.live.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.Course;
import com.laka.live.ui.fragment.CourseListFragment;
import com.laka.live.ui.widget.HeadView;
import com.laka.live.util.Common;

/**
 * changed by lyf on 17/6/17.
 * 热门资讯
 */
public class HotNewsActivity extends BaseActivity {

    private FragmentManager fragmentManager;
    private CourseListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_live);
        fragmentManager = getSupportFragmentManager();
        setFragmentPage(0);

        HeadView headView = (HeadView) findViewById(R.id.header);
        headView.setTitle("热门资讯");
        headView.setBackShow(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsReport.onEvent(this, AnalyticsReport.FRIEND_DYNAMIC_SHOW_ID);
    }

    private void setFragmentPage(int page) {
        if(isFinishing()){
            return;
        }
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (page) {
            case 0:
                if (fragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    fragment = new CourseListFragment();
                    Bundle bundleNews = new Bundle();
                    bundleNews.putInt(Common.LIST_TYPE, Course.NEWS);
                    fragment.setArguments(bundleNews);
                    transaction.add(R.id.content, fragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(fragment);
                }
                break;
            default:
                break;
        }
        transaction.commitAllowingStateLoss();//.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (fragment != null) {
            transaction.hide(fragment);
        }
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
    }
}
