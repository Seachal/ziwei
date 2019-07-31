package com.laka.live.account.my;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.laka.live.R;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.QuerySendRankingListMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.ui.rankinglist.RankingItemView;
import com.laka.live.ui.rankinglist.RankingListAdapter;
import com.laka.live.ui.rankinglist.RankingListConstant;
import com.laka.live.ui.widget.HeadView;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.util.ResourceHelper;

import org.greenrobot.eventbus.EventBus;

/**
 * 贡献榜
 */
public class ContributionListView extends LinearLayout implements BaseAdapter.OnItemClickListener
        , PageListLayout.OnRequestCallBack {
    protected static final String TAG = "ContributionListView";
    protected static final int MODE_FULL_SCREEN = 0, MODE_HALF_SCREEN = 1;
    public int mode = MODE_FULL_SCREEN;
    private final static String EXTRA_USER_ID = "EXTRA_USER_ID";
    private final static String FROM_TYPE = "from_type";
    private final static int DEFULT_USER_ID = -1;
    private RankingListAdapter mAdapter;

    private String mUserId;
    private String mFromType;

    public Context context;
    private View view;
    PageListLayout listLayout;
    HeadView headView;

    /**
     * 初始化控件
     */
    public ContributionListView(Context context) {
        super(context);
        this.context = context;
        initUI();
    }

    public ContributionListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initUI();
    }

    private void initUI() {
        view = LayoutInflater.from(context).inflate(R.layout.view_contribution_list, null);
        addView(view);
//        mUserId = getIntent().getStringExtra(EXTRA_USER_ID);
//        mFromType = getIntent().getStringExtra(FROM_TYPE);
        listLayout = (PageListLayout) findViewById(R.id.list_layout);
        RecyclerView recyclerView = listLayout.getRecyclerView();
        recyclerView.setBackgroundResource(R.drawable.rank_list_bg);
        int padding = ResourceHelper.getDimen(R.dimen.rank_list_horizontal_padding);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) recyclerView.getLayoutParams();
        params.leftMargin = padding;
        params.rightMargin = padding;

        listLayout.setLayoutManager(new LinearLayoutManager(context));
        listLayout.setOnRequestCallBack(this);
    }

    public void start(Activity activity, String userId, String mFromType) {
        this.mUserId = userId;
        this.mFromType = mFromType;
//        mAdapter = new ContributionAdapter(activity);
        mAdapter = new RankingListAdapter(activity, RankingItemView.TYPE_COINS, RankingListConstant.RANKING_TAB_TYPE_SEND);
//        mAdapter.setFromType(mFromType);
        listLayout.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        listLayout.loadData(true);
        listLayout.setIsLoadMoreEnable(false);
        int white = ContextCompat.getColor(getContext(), R.color.white);
        listLayout.setLoadingColor(white);
        listLayout.setPtrHeadColor(white);
        listLayout.setPtrHeadLoadingColor(white);

        headView = (HeadView) findViewById(R.id.header);
        headView.setBackShow(false);
        headView.setBackOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == MODE_FULL_SCREEN) {
                    Context mContext = headView.mContext;
                    if (mContext instanceof Activity) {
                        ((Activity) mContext).finish();
                    }
                } else {
                    EventBusManager.postEvent(0, SubcriberTag.HIDE_CONTRIBUTION_PANEL);
                }
            }
        });
    }

    public void stop() {
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

    public void setBackShow(boolean isShow) {
        headView.setBackShow(isShow);
    }
}
