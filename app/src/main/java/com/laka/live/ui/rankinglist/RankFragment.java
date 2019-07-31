package com.laka.live.ui.rankinglist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laka.live.R;
import com.laka.live.bean.RankingUserInfo;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.QueryRankingListMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.fragment.BaseFragment;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.util.ResourceHelper;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by luwies on 16/9/18.
 */
public class RankFragment extends BaseFragment implements EventBusManager.OnEventBusListener,
        PageListLayout.OnRequestCallBack {

    public static final String TYPE = "type";

    public static final String INDEX = "index";

    public static final String CURRENT_INDEX = "current_index";

    private static final int TOPLIST_TYPE = RankingListConstant.RANKING_LIST_TAB_TYPE_ALL;

    private PageListLayout mListView;

    private RankingListAdapter mAdapter;

    private int mType = RankingListConstant.RANKING_TAB_TYPE_ACCEPT;

    private int mIndex;

    private int mCurrentIndex = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusManager.register(this);
        Bundle arg = getArguments();
        if (arg != null) {
            mType = arg.getInt(TYPE);
            mIndex = arg.getInt(INDEX);
            mCurrentIndex = arg.getInt(CURRENT_INDEX);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_INDEX, mCurrentIndex);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.rank_fragment_layout, null);
        init(root);
        if (mIndex == mCurrentIndex) {
            mListView.loadData();
        }
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusManager.unregister(this);
    }

    private void init(View root) {
        mListView = (PageListLayout) root.findViewById(R.id.list);
        mListView.setLayoutManager(new LinearLayoutManager(getContext()));
        mListView.setIsLoadMoreEnable(false);
        mListView.setRefreshDelay(200L);
        int white = ContextCompat.getColor(getContext(), R.color.white);
        mListView.setLoadingColor(white);
        mListView.setPtrHeadColor(white);
        mListView.setPtrHeadLoadingColor(white);

        RecyclerView recyclerView = mListView.getRecyclerView();
        recyclerView.setBackgroundResource(R.drawable.rank_list_bg);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) recyclerView.getLayoutParams();
        int padding = ResourceHelper.getDimen(R.dimen.rank_list_horizontal_padding);
        params.leftMargin = padding;
        params.rightMargin = padding;
        int itemType = RankingItemView.TYPE_COINS;
        if (mType == RankingListConstant.RANKING_TAB_TYPE_SEND) {
            itemType = RankingItemView.TYPE_COINS;
        } else if (mType == RankingListConstant.RANKING_TAB_TYPE_FANS) {
            itemType = RankingItemView.TYPE_FANS;
        } else if (mType == RankingListConstant.RANKING_TAB_TYPE_ACCEPT) {
            itemType = RankingItemView.TYPE_COINS_HOST;
        }
        mAdapter = new RankingListAdapter(getActivity(), itemType, mType);
        mListView.setAdapter(mAdapter);

        mListView.setOnRequestCallBack(this);

    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {
        return DataProvider.queryRankingList(this, mType, TOPLIST_TYPE, listener);
    }

    @Subscribe
    @Override
    public void onEvent(PostEvent event) {
        if (event.tag.equals(SubcriberTag.REFRESH_RANKING_LIST_DATA)) {
            handleOnRefreshDataChange((RankingUserInfo) event.event);
        } else if (TextUtils.equals(event.tag, SubcriberTag.LOAD_RANKING_LIST_DATA)) {
            mCurrentIndex = (int) event.event;
            if (mAdapter.isEmpty() && mCurrentIndex == mIndex) {
                mListView.loadData();
            }
        }
    }

    private void notifyDataSetChanged() {


        mAdapter.notifyDataSetChanged();
    }

    private void handleOnRefreshDataChange(RankingUserInfo rankingUserInfo) {
        if (mAdapter.isEmpty()) {
            return;
        }
        for (RankingUserInfoParams userInfoParams : mAdapter.getAll()) {
            if (userInfoParams.userInfo != null && rankingUserInfo != null) {
                if (rankingUserInfo.getId() == userInfoParams.userInfo.getId()) {
                    userInfoParams.userInfo.setFollow(rankingUserInfo.getFollow());
                    break;
                }
            }
        }

        notifyDataSetChanged();

    }


}
