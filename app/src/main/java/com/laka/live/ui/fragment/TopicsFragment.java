package com.laka.live.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.Course;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.HomeCourseMsg;
import com.laka.live.msg.TopicListMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.course.detail.CourseDetailActivity;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.ui.adapter.TopicsAdapter;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.util.Common;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;

/**
 * Created by luwies on 16/3/29.
 */
public class TopicsFragment extends BaseFragment implements BaseAdapter.OnItemClickListener,
        PageListLayout.OnRequestCallBack, PageListLayout.OnResultListener<HomeCourseMsg>,
        EventBusManager.OnEventBusListener {

    private static final String TAG = "TopicsFragment";

    private static final int LIMIT = Integer.MAX_VALUE;

    private static final long INTERVAL = 20000;

    private static final int UPDTAE_MAG = 1000;


    private TopicsAdapter mAdapter;

    private PageListLayout mListView;

    private long mLastUpdateTime = 0;

    private Handler mHandler;

    private boolean isFirstResume = true;

    public TopicsFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusManager.register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mListView = (PageListLayout) inflater.inflate(R.layout.page_list_layout, container, false);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);
        mListView.setLayoutManager(layoutManager);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                if (mAdapter.getItemViewType(position) == TopicsAdapter.TYPE_HEAD) {
                    return 2;
                } else {
                    return 1;
                }

            }
        });

        mListView.setIsReloadWhenEmpty(true);
        mListView.setLoadMoreCount(LIMIT);
        mAdapter = new TopicsAdapter(getContext());
        mListView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mListView.setOnRequestCallBack(this);
        mListView.setOnResultListener(this);
        mListView.loadData(true);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case UPDTAE_MAG:
                        mListView.loadData(false);
                        break;
                }
            }
        };

        EventBusManager.postEvent(0, SubcriberTag.STOP_DOWNLOAD_GIFT_RES);
        return mListView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isFirstResume == false) {
            if (mAdapter.isEmpty()) {
                mListView.loadData(true);
            } else {
                long now = System.currentTimeMillis();
                if (mLastUpdateTime > 0 && now - mLastUpdateTime > INTERVAL) {
                    mListView.loadData(false);
                } else {
                    delayUpdate();
                }
            }
        }

        isFirstResume = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeMessages(UPDTAE_MAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusManager.unregister(this);
    }

    private void delayUpdate() {
        mHandler.removeMessages(UPDTAE_MAG);
        Message msg = Message.obtain();
        msg.what = UPDTAE_MAG;
        mHandler.sendMessageDelayed(msg, INTERVAL);
    }

    @Override
    public void onItemClick(int position) {

        Course course = mAdapter.getItem(position);
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.TOPIC_ID, course.getId());
        AnalyticsReport.onEvent(getContext(), AnalyticsReport.HOME_TOPICS_COVER_CLICK_EVENT_ID);
        CourseDetailActivity.startActivity(getContext(), String.valueOf( course.getId()));
    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {

        if (mAdapter.isTopicsEmpty()) {

            DataProvider.queryHomeTopics(this, new GsonHttpConnection.OnResultListener<TopicListMsg>() {
                @Override
                public void onSuccess(TopicListMsg topicListMsg) {

                    if (!topicListMsg.isEmpty()) {
                        mAdapter.setTopics(topicListMsg.getList());
                    }

                }

                @Override
                public void onFail(int errorCode, String errorMsg, String command) {
                }

            });
        }
        return DataProvider.queryHomeFind(this, 20, page, listener);
    }

    @Override
    public void onResult(HomeCourseMsg roomMsg) {
        mLastUpdateTime = System.currentTimeMillis();
        delayUpdate();
    }

    @Subscribe
    @Override
    public void onEvent(PostEvent event) {

        if (TextUtils.equals(event.tag, SubcriberTag.SQUARE_HOT_DOUBLE_CLICK_EVENT)) {
            handleDoubleClick(mListView);
        }
    }

    public void handleDoubleClick(final PageListLayout listView) {
        RecyclerView recyclerView = listView.getRecyclerView();
        int offset = recyclerView.computeVerticalScrollOffset();

        if (offset > 0) {
            listView.smoothScrollToPosition(0);
            listView.addOnScrollListener(new PullToRefreshRecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        listView.removeOnScrollListener();

                        int offset = recyclerView.computeVerticalScrollOffset();
                        if (offset <= 0) {
                            listView.autoRefresh(false);
                        }
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                }

                @Override
                public void onScroll(RecyclerView recyclerView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });
        } else {
            listView.autoRefresh();
        }
    }

}
