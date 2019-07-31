package com.laka.live.ui.course;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.adapter.HomeFollowAdapter;
import com.laka.live.ui.widget.HeadView;

import framework.ioc.annotation.InjectExtra;

/**
 * Created by lyf on 17/4/14.
 * 通用的ListView
 */
public class VListView extends BaseActivity {

    private FragmentManager fragmentManager;
    private VListViewFragment fragment;

    @InjectExtra(name = "type")
    public Integer type;

    public static void startActivity(Context context, int type) {
        Intent intent = new Intent(context, VListView.class);
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_live);
        setSwipeBackEnable(false);
        fragmentManager = getSupportFragmentManager();
        setFragmentPage(0);

        HeadView headView = (HeadView) findViewById(R.id.header);
        headView.setBackShow(false);
        switch (type) {
            case HomeFollowAdapter.TYPE_LIVE:
                headView.setTitle("直播");
                break;
            case HomeFollowAdapter.TYPE_REPLAY:
                headView.setTitle("课程回放");
                break;
            case HomeFollowAdapter.TYPE_VIDEO:
                headView.setTitle("最新视频");
                break;
        }
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
                    fragment = new VListViewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("type",type);
                    fragment.setArguments(bundle);
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
