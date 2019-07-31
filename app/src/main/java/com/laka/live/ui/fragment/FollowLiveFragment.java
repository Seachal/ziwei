package com.laka.live.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.Course;
import com.laka.live.bean.UserInfo;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.course.detail.CourseDetailActivity;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.ui.adapter.HomeFollowAdapter;
import com.laka.live.ui.room.LiveRoomActivity;
import com.laka.live.ui.room.SeeReplayActivity;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.util.Common;
import com.laka.live.util.ViewUtils;

import org.greenrobot.eventbus.Subscribe;

import framework.ioc.Ioc;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link  }
 * interface.
 */
public class FollowLiveFragment extends BaseFragment implements BaseAdapter.OnItemClickListener,
        PageListLayout.OnRequestCallBack, EventBusManager.OnEventBusListener, SpacesItemDecoration.ItemDecorationCallBack {

    private final static int LIMIT = 20;

    private HomeFollowAdapter mAdapter;

    private PageListLayout mListView;

    private SpacesItemDecoration mSpacesItemDecoration;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FollowLiveFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusManager.register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mSpacesItemDecoration = new SpacesItemDecoration(this);

        mListView = (PageListLayout) inflater.inflate(R.layout.follow_live_item_list, container, false);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);
        mListView.setLayoutManager(layoutManager);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int type = mAdapter.getItemViewType(position);
                if (type == HomeFollowAdapter.TYPE_LIVE || type == HomeFollowAdapter.TYPE_DIVIDE) {
                    return 2;
                }
                return 1;
            }
        });
        mListView.setIsReloadWhenEmpty(true);
        mAdapter = new HomeFollowAdapter(getContext());
        mListView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mListView.setOnRequestCallBack(this);
        mListView.loadData(true);
        mListView.setLoadMoreCount(LIMIT);

        mListView.getRecyclerView().addItemDecoration(mSpacesItemDecoration);
        return mListView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusManager.unregister(this);
    }

    @Override
    public void onItemClick(int position) {

        Course course = mAdapter.getItem(position);

        int viewType = mAdapter.getItemViewType(position);
        switch (viewType) {
            case HomeFollowAdapter.TYPE_LIVE:
                AnalyticsReport.onEvent(getContext(), AnalyticsReport.FRIEND_DYNAMIC_ITEM_CLICK_ID);
                break;
            case HomeFollowAdapter.TYPE_REPLAY:

                break;
            case HomeFollowAdapter.TYPE_VIDEO:

                break;
            case HomeFollowAdapter.TYPE_DIVIDE:

                break;
        }


        if (course.hasBuy()) {
            // 已购买
            if (course.status == Course.LIVING) {
                // 直播就跳到直播间
                LiveRoomActivity.startPlay(getActivity(), course.user_id,
                        Ioc.get(UserInfo.class).getId() == course.user_id, course.getTitle(),
                        course.room.getStreamId(), course.room.getChannelId(), course.user.avatar,
                        course.cover_url, Common.FROM_MAIN, course.getId());
            } else {
                // 观看回放或视频
                SeeReplayActivity.startActivity(getActivity(),course.getId(),course.room.getDownUrl(),
                        String.valueOf(course.user.id),course.getViews(),course.getRecvCoins(),course.room.getChannelId(),course.getType());
            }

        } else {
            // 未购买一律跳详情
            CourseDetailActivity.startActivity(getContext(), String.valueOf(course.getId()));
        }

    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {

        String data = DataProvider.queryFollowVideo(getActivity(), page, LIMIT, listener);

        if (page == 0)
            mSpacesItemDecoration.refresh();

        return data;
    }

    @Subscribe
    @Override
    public void onEvent(PostEvent event) {
        if (TextUtils.equals(SubcriberTag.MAIN_FRIEND_DOUBLE_CLICK_EVENT, event.tag)) {
            doubleClick();
        }
    }

    private void doubleClick() {
        ViewUtils.handleDoubleClick(mListView);
    }

    @Override
    public int getItemType(int position) {
        return mAdapter.getItemViewType(position);
    }

}
