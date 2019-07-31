package com.laka.live.account.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;

import com.laka.live.R;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.ui.rankinglist.RankingItemView;
import com.laka.live.ui.widget.PageListLayout;

/**
 * 贡献榜
 */
public class ContributionListActivity extends BaseActivity implements BaseAdapter.OnItemClickListener
        , PageListLayout.OnRequestCallBack {

    private final static String EXTRA_USER_ID = "EXTRA_USER_ID";
    private final static String FROM_TYPE = "from_type";
//    private final static int DEFULT_USER_ID = -1;
//    private ContributionAdapter mAdapter;
    private String mUserId;
    private String mFromType;
    private ContributionListView mViewContribution;//改成可动态修改全屏/半屏
    public static void startActivity(Activity activity, String userId) {
        startActivity(activity, userId, RankingItemView.FROM_TYPE_MINE);
    }

    public static void startActivity(Activity activity, String userId, String from) {
        if (activity != null) {
            Intent intent = new Intent(activity, ContributionListActivity.class);
            intent.putExtra(EXTRA_USER_ID, userId);
            intent.putExtra(FROM_TYPE, from);
            ActivityCompat.startActivity(activity, intent, null);
        }
    }

    public static void startActivity(Context context, String userId, String from) {
        if (context != null) {
            Intent intent = new Intent(context, ContributionListActivity.class);
            intent.putExtra(EXTRA_USER_ID, userId);
            intent.putExtra(FROM_TYPE, from);
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribution_list);

        mUserId = getIntent().getStringExtra(EXTRA_USER_ID);
        mFromType = getIntent().getStringExtra(FROM_TYPE);

        mViewContribution = (ContributionListView) findViewById(R.id.view_contribution);
        mViewContribution.start(this,mUserId,mFromType);
        mViewContribution.setBackShow(true);
//        PageListLayout listLayout = (PageListLayout) findViewById(R.id.list_layout);
//        listLayout.setLayoutManager(new LinearLayoutManager(this));
//        mAdapter = new ContributionAdapter(this);
//        mAdapter.setFromType(mFromType);
//        listLayout.setAdapter(mAdapter);
//        mAdapter.setOnItemClickListener(this);
//        listLayout.setOnRequestCallBack(this);
//        listLayout.loadData(true);
//        listLayout.setIsLoadMoreEnable(false);
    }

    @Override
    public void onItemClick(int position) {
       /* ListUserInfo info = mAdapter.getItem(position);
        UserInfoActivity.startActivity(ContributionListActivity.this, info);*/
    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {
        return DataProvider.querySendRanking(this, mUserId, listener);
    }
}
