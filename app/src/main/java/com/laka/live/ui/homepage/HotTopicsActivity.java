package com.laka.live.ui.homepage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.LinearLayout;

import com.laka.live.R;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.homepage.adapter.TopicItemAdapter;
import com.laka.live.ui.widget.HeadView;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.ui.widget.WaterFallDividerItemDecoration;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;

/**
 * @ClassName: HotTopicsActivity
 * @Description: 热门话题列表
 * @Author: chuan
 * @Version: 1.0
 * @Date: 6/6/17
 */

public class HotTopicsActivity extends BaseActivity implements PageListLayout.OnRequestCallBack {

    private final static int LIMIT = 20;

    public static void startActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, HotTopicsActivity.class);
            ActivityCompat.startActivity(activity, intent, null);
        }
    }

    private PageListLayout pageListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_common_pagelistlayout);
        getWindow().setBackgroundDrawable(null);

        HeadView headView = (HeadView) findViewById(R.id.head_view);
        headView.setTitle(ResourceHelper.getString(R.string.hot_topic));
        headView.setBackTextShow(false);
        headView.setBackShow(true);

        pageListLayout = (PageListLayout) findViewById(R.id.page_list_layout);
        pageListLayout.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) pageListLayout.getLayoutParams();
        params.rightMargin = Utils.dip2px(this, -6);
        pageListLayout.setLayoutParams(params);
        pageListLayout.getRecyclerView().addItemDecoration(new WaterFallDividerItemDecoration(this, R.drawable.transparent_divider_6dp, true));
        pageListLayout.setAdapter(new TopicItemAdapter(mContext, TopicItemAdapter.TYPE_TOPIC));
        pageListLayout.setIsReloadWhenEmpty(true);
//        pageListLayout.showFooter(false);
        pageListLayout.setOnRequestCallBack(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (pageListLayout != null && pageListLayout.isEmpty()) {
            pageListLayout.loadData(true);
        }
    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {
        return DataProvider.queryHomeHotTopics(this, page, LIMIT, listener);
    }
}
