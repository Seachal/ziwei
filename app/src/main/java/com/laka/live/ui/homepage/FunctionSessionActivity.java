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
import com.laka.live.ui.homepage.adapter.FreeSessionAdapter;
import com.laka.live.ui.widget.HeadView;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.util.Common;
import com.laka.live.util.Log;

/**
 * @ClassName: FunctionSessionActivity
 * @Description: 免费课程
 * @Author: chuan
 * @Version: 1.0
 * @Date: 6/1/17
 */

public class FunctionSessionActivity extends BaseActivity implements PageListLayout.OnRequestCallBack {
    private final static String TAG = FunctionSessionActivity.class.getSimpleName();

    private final static int GO_FREE_SESSION = 1;
    private final static int GO_NEW_SESSION = 9;  //最新课程
    private final static int GO_HOT_SESSION = 10;  //热门课程
    private final static int GO_BEST_SESSION = 11;  //优质课程
    private final static int GO_CHEAP_SESSION = 12;  //1元课程
    private final static int GO_LIMIT_SESSION = 13;  //限时优惠

    private PageListLayout mPageListLayout;
    private int mType;

    public static void startActivity(Context context, int type, String title) {
        if (context != null) {
            Intent intent = new Intent(context, FunctionSessionActivity.class);
            intent.putExtra(Common.TYPE, type);
            intent.putExtra(Common.TITLE, title);
            ActivityCompat.startActivity(context, intent, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_common_pagelistlayout);
//        getWindow().setBackgroundDrawable(null);  //去除window的背景，减少过度绘制

        if (getIntent() == null) {
            finish();
            return;
        }

        mType = getIntent().getIntExtra(Common.TYPE, -1);

        HeadView mHeadView = (HeadView) findViewById(R.id.head_view);
        mHeadView.setTitle(getIntent().getStringExtra(Common.TITLE));
        mHeadView.setBackShow(true);
        mHeadView.setBackTextShow(false);

        mPageListLayout = (PageListLayout) findViewById(R.id.page_list_layout);
        mPageListLayout.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mPageListLayout.setAdapter(new FreeSessionAdapter(mContext));
        mPageListLayout.setOnRequestCallBack(this);
        mPageListLayout.setIsLoadMoreEnable(true);
        mPageListLayout.setIsReloadWhenEmpty(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mPageListLayout != null && mPageListLayout.isEmpty()) {
            mPageListLayout.loadData(true);
        }
    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {
        if (mType == -1) {
            this.finish();
            return null;
        }

        switch (mType) {
            case GO_NEW_SESSION:
                return DataProvider.queryNewCourse(this, page, listener);
            case GO_HOT_SESSION:
                return DataProvider.queryHotCourse(this, page, listener);
            case GO_BEST_SESSION:
                return DataProvider.queryBestCourse(this, page, listener);
            case GO_CHEAP_SESSION:
                return DataProvider.queryCheapCourse(this, page, listener);
            case GO_LIMIT_SESSION:
                return DataProvider.queryLimitCourse(this, page, listener);
            case GO_FREE_SESSION:
                return DataProvider.queryFreeCourse(this, page, listener);
            default:
                Log.d(TAG, "unhandled go app type : " + mType);
                break;
        }
        return null;
    }
}
