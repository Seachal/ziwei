package com.laka.live.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.Course;
import com.laka.live.bean.Room;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.HomeRecommendMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.WebActivity;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.ui.adapter.CourseAdapter;
import com.laka.live.ui.course.CourseClassifyActivity;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.util.Common;
import com.laka.live.util.Log;
import com.laka.live.util.Utils;
import com.laka.live.util.ViewUtils;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;

/**
 * 课程列表通用的Fragment
 */
public class CourseClassifyFragment extends BaseFragment implements BaseAdapter.OnItemClickListener,
        PageListLayout.OnRequestCallBack, PageListLayout.OnResultListener<HomeRecommendMsg>,
        EventBusManager.OnEventBusListener, PullToRefreshRecyclerView.OnScrollListener {

    private static final String TAG = "CourseClassifyFragment";
    private static final int LIMIT = Integer.MAX_VALUE;
    private static final long INTERVAL = 20000;
    private static final int UPDTAE_MAG = 1000;
    private static final int PAGE_LIMIT = 10; // 每页多少条

    private int cateId = -1;
    private int mType = Course.LIVE;
    private CourseAdapter mAdapter;
    private View mRootView, mMaskView;
    private PageListLayout mListView;
    private long mLastUpdateTime = 0;
    private Handler mHandler;
    private onCateVisibleChangedListener onCateVisibleChangedCallBack;
    private boolean isFirstResume = true, isShowTopDivider = true;

    public CourseClassifyFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            cateId = bundle.getInt(Common.CATE_ID);
            mType = bundle.getInt(Common.LIST_TYPE);
            isShowTopDivider = bundle.getBoolean("isShowTopDivider", true);
        }
        EventBusManager.register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_course_classify, container, false);
        mMaskView = mRootView.findViewById(R.id.mask);
        mListView = (PageListLayout) mRootView.findViewById(R.id.page_list_layout);
        mListView.setLayoutManager(new LinearLayoutManager(getContext()));
        mListView.setIsReloadWhenEmpty(true);
        if (mType == Course.NEWS) {
            mListView.setLoadMoreCount(10);
        } else {
            mListView.setLoadMoreCount(LIMIT);
        }
        mAdapter = new CourseAdapter(getContext(), mListView, mType);
        mAdapter.setShowTopDivider(isShowTopDivider);
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

        // 只有扩展onCateVisibleChangedListener，才能监听到滚动事件
        if (getActivity() instanceof onCateVisibleChangedListener) {
            onCateVisibleChangedCallBack = (onCateVisibleChangedListener) getActivity();
        }

        EventBusManager.postEvent(0, SubcriberTag.STOP_DOWNLOAD_GIFT_RES);
        mListView.addOnScrollListener(this);

        return mRootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        if (!isFirstResume) {
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

    private static long lastClick = 0;

    @Override
    public void onItemClick(int position) {

        //两次点击的时间差在1000毫秒之内,不做任何处理
        if (System.currentTimeMillis() - lastClick < 1000) {
            lastClick = System.currentTimeMillis();
            return;
        } else {
            lastClick = System.currentTimeMillis();
        }
        Course course = (Course) mAdapter.getItem(position);

        HashMap<String, String> params = new HashMap<>();
        params.put("id", course.getId());

        if (course.isLive()) {
            AnalyticsReport.onEvent(getContext(), AnalyticsReport.HOME_LIVE_COVER_CLICK_EVENT_ID, params);
        } else {
            AnalyticsReport.onEvent(getContext(), AnalyticsReport.HOME_VIDEO_COVER_CLICK_EVENT_ID, params);
        }

        if (mType == Course.NEWS) {
            WebActivity.startActivity(mContext, course.getNewsUrl(), course.getTitle());
        } else {
            course.onClickEvent(getContext());
        }

    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {
        return DataProvider.getCourseCategory(this, mType, cateId, page, PAGE_LIMIT, listener);
    }

    @Override
    public void onResult(HomeRecommendMsg homeRecommendMsg) {
        mLastUpdateTime = System.currentTimeMillis();
        // 只有没数据时，才要再刷新
        if (homeRecommendMsg == null)//|| homeRecommendMsg.isEmpty()
            delayUpdate();
    }

    @Subscribe
    @Override
    public void onEvent(PostEvent event) {

        if (TextUtils.equals(event.getTag(), SubcriberTag.START_ANIMATING)) {
            mMaskView.setClickable((Boolean) event.getEvent());
        } else if (TextUtils.equals(event.getTag(), SubcriberTag.CHANGED_CATEGORY)) {
            cateId = (int) event.getEvent();
            if (!mListView.isCompeleted())
                mListView.setCompeleted(true);
            mListView.loadData(true,false);
        } else if (TextUtils.equals(event.tag, SubcriberTag.CONTENT_TAB_DOUBLE_CLICK_EVENT)) {

            CourseClassifyActivity newestCoursesActivity = (CourseClassifyActivity) getActivity();
            // 只刷新当前页面
            if (newestCoursesActivity.getCurrentType() == mType) {
                ViewUtils.handleDoubleClick(mListView);
            }

        } else if (TextUtils.equals(event.tag, SubcriberTag.REMOVE_END_ZHIBO)) {
            String endZhuboId = (String) event.event;
            List<Object> rooms = mAdapter.getAll();
            if (!Utils.listIsNullOrEmpty(rooms)) {
                for (int i = 0; i < rooms.size(); i++) {
                    if (rooms.get(i) instanceof Room) {
                        Course room = (Course) rooms.get(i);
                        if (String.valueOf(room.getId()).equals(endZhuboId)) {
                            rooms.remove(i);
                            Log.d(TAG, "移除已结束room id=" + endZhuboId);
                            mAdapter.notifyDataSetChanged();
                            return;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    }


    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
    }

    @Override
    public void onScroll(RecyclerView recyclerView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (onCateVisibleChangedCallBack != null) {
            if (mListView.findFirstCompletelyVisibleItemPosition() == 0) {
                onCateVisibleChangedCallBack.onCateVisibleChanged(true, mType);
            } else if (mListView.findFirstCompletelyVisibleItemPosition() > 0) {
                onCateVisibleChangedCallBack.onCateVisibleChanged(false, mType);
            }
        }

    }

    public void onTabChanged() {

        if (onCateVisibleChangedCallBack != null) {
            if (mListView.findFirstCompletelyVisibleItemPosition() == 0) {
                onCateVisibleChangedCallBack.onCateVisibleChanged(true, mType);
            } else if (mListView.findFirstCompletelyVisibleItemPosition() > 0) {
                onCateVisibleChangedCallBack.onCateVisibleChanged(false, mType);
            }
        }
    }


    /**
     * 分类显示和隐藏的监听接口
     */
    public interface onCateVisibleChangedListener {
        void onCateVisibleChanged(boolean isVisible, int which);
    }


}
