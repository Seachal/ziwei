package com.laka.live.ui.homepage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;

import com.laka.live.R;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.homepage.adapter.FollowNewsAdapter;
import com.laka.live.ui.widget.HeadView;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.util.ResourceHelper;
import com.laka.live.player.MyExoPlayerPlus;

/**
 * @ClassName: FollowNewsActivity
 * @Description: 关注动态
 * @Author: chuan
 * @Version: 1.0
 * @Date: 6/7/17
 */

public class FollowNewsActivity extends BaseActivity implements PageListLayout.OnRequestCallBack {

    private final static int LIMIT = 20;

    private PageListLayout mPageListLayout;

    public static void startActivity(Context context) {
        if (context != null) {
            Intent intent = new Intent(context, FollowNewsActivity.class);
            ActivityCompat.startActivity(context, intent, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_common_pagelistlayout);
        getWindow().setBackgroundDrawable(null);

        HeadView headView = (HeadView) findViewById(R.id.head_view);
        headView.setTitle(ResourceHelper.getString(R.string.follow_news));
        headView.setBackTextShow(false);
        headView.setBackShow(true);

        mPageListLayout = (PageListLayout) findViewById(R.id.page_list_layout);
        mPageListLayout.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mPageListLayout.setIsReloadWhenEmpty(true);
        mPageListLayout.setOnRequestCallBack(this);
        mPageListLayout.setAdapter(new FollowNewsAdapter(this));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mPageListLayout != null && mPageListLayout.isEmpty()) {
            mPageListLayout.loadData(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // MyVideoPlayer 释放所有Video
        MyExoPlayerPlus.releaseAllVideos();
    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {
        return DataProvider.queryFollowNews(this, page, LIMIT, listener);
    }
}
