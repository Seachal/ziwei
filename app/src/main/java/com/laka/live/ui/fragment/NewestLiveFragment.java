package com.laka.live.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laka.live.R;
import com.laka.live.bean.Topic;
import com.laka.live.msg.QueryLatestRoomListMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.adapter.NewestAdapter;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.ui.widget.NewestItemDecoration;

import java.util.List;

/**
 * Created by luwies on 16/3/30.
 */
public class NewestLiveFragment extends BaseFragment implements PageListLayout.OnRequestCallBack
, PageListLayout.OnResultListener<QueryLatestRoomListMsg> {

    private static final int LIMIT = 36;

    private PageListLayout mListView;

    private NewestAdapter mAdapter;

    private NewestItemDecoration mDecoration;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mListView = (PageListLayout) inflater.inflate(R.layout.fragment_newest_layout, container, false);

        GridLayoutManager manager = new GridLayoutManager(getContext(), 3,
                LinearLayoutManager.VERTICAL, false);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mAdapter.hasHead() && position == 0 ? 3 : 1;
            }
        });
        mListView.setLayoutManager(manager);
//        mListView.setEmptyTipText(R.string.home_not_live_tip);
        mListView.setIsReloadWhenEmpty(true);
        mAdapter = new NewestAdapter();
        mListView.setAdapter(mAdapter);
//        mAdapter.setOnItemClickListener(this);
        mListView.setOnRequestCallBack(this);
        mListView.setOnResultListener(this);
        mListView.setLoadMoreCount(LIMIT);
        mListView.loadData(true);

        mDecoration = new NewestItemDecoration(3, getContext().getResources()
                .getDimensionPixelSize(R.dimen.feature_divider_width));
        mListView.getRecyclerView().addItemDecoration(mDecoration);

        mListView.getRecyclerView().setHasFixedSize(true);

        return mListView;

    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {
        return DataProvider.queryLatestRoom(this, page, LIMIT, listener);
    }

    @Override
    public void onResult(QueryLatestRoomListMsg msg) {
        if (msg != null) {

            List<Topic> topics = msg.getTopics();
            if (topics != null && topics.isEmpty() == false) {
                topics.add(Topic.makeMoreTopic(getContext()));
            }
            mAdapter.setTopics(topics);
            mDecoration.setHasHead(mAdapter.hasHead());
        }
    }
}
