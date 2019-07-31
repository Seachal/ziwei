package com.laka.live.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.analytics.LiveReport;
import com.laka.live.bean.Course;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.LiveListMsg;
import com.laka.live.msg.Msg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.activity.WebActivity;
import com.laka.live.ui.course.detail.CourseDetailActivity;
import com.laka.live.ui.course.MyCoursesActivity;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.ui.adapter.MyLiveAdapter;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import framework.utils.GsonTools;


/**
 * Created by Lyf on 2017/3/30.
 */
public class MyCourseFragment extends BaseFragment implements BaseAdapter.OnItemClickListener,
        PageListLayout.OnRequestCallBack, PageListLayout.OnResultListener<LiveListMsg>,
        EventBusManager.OnEventBusListener, GsonHttpConnection.OnResultListener<Msg> {


    private static final int LIMIT = 10;
    private static final long INTERVAL = 1000;
    private static final int UPDTAE_MAG = 1000;

    private Handler mHandler;
    private MyLiveAdapter mAdapter;
    private PageListLayout mListView;

    private long mLastUpdateTime = 0;
    private boolean isFirstResume = true;

    // 1=直播，2=视频, 3=资讯
    private int course_type;
    // 1=已发布，2：已购买
    private String status;

    public MyCourseFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusManager.register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        status = getArguments().getString("status");
        course_type = getArguments().getInt("course_type");

        mListView = (PageListLayout) inflater.inflate(R.layout.page_list_layout, container, false);
        mListView.setLayoutManager(new LinearLayoutManager(getContext()));

//        if (isLive()) {
//            mListView.setEmptyTipText(R.string.home_not_live_tip);
//        } else if (isVideo()) {
//            mListView.setEmptyTipText(R.string.home_not_video_tip);
//        } else {
//            mListView.setEmptyTipText(R.string.home_not_news_tip);
//        }

        mListView.setIsReloadWhenEmpty(true);
        mListView.setLoadMoreCount(LIMIT);
        mAdapter = new MyLiveAdapter();
        mListView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mListView.setOnRequestCallBack(this);
        mListView.setOnResultListener(this);
        mListView.setIsLoadMoreEnable(true);
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
    public void onItemClick(int position) {

        Course course = mAdapter.getItem(position);
        HashMap<String, String> params = new HashMap<>();
        params.put("ID", course.getId());
        String eventId; // 事件Id

        if (isMyPushed()) {
            if (isLive()) {
                eventId = LiveReport.MY_LIVE_EVENT_15458;
            } else {
                eventId = LiveReport.MY_LIVE_EVENT_15460;
            }
        } else {
            if (isLive()) {
                eventId = LiveReport.MY_LIVE_EVENT_15452;
            } else {
                eventId = LiveReport.MY_LIVE_EVENT_15454;
            }
        }

        AnalyticsReport.onEvent(getContext(), eventId, params);

        if (isNews()) {
            WebActivity.startActivity(mContext, course.getNewsUrl(), course.getTitle());
        } else {
            CourseDetailActivity.startActivity(getActivity(), course.getId());
        }

    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {

        if (isNews()) {
            return DataProvider.queryMyNews(this,
                    String.valueOf(AccountInfoManager.getInstance().getCurrentAccountUserId()), String.valueOf(page), listener);
        } else {
            return DataProvider.queryMyLive(this,
                    String.valueOf(AccountInfoManager.getInstance().getCurrentAccountUserId()),
                    String.valueOf(course_type), String.valueOf(page), status, listener);
        }

    }

    @Override
    public void onResult(LiveListMsg roomListMsg) {
        mLastUpdateTime = System.currentTimeMillis();
    }

    @Subscribe
    @Override
    public void onEvent(PostEvent event) {

        if (event.tag.equals(SubcriberTag.RELEASED_LIVING_EDITED)) {
            // 设置为编辑状态
            mAdapter.setEditStatus(true);
            mAdapter.notifyDataSetChanged();
        } else if (event.tag.equals(SubcriberTag.RELEASED_LIVING_CANCEL)) {
            if (mAdapter.getEditStatus()) {
                // 取消编辑状态
                mAdapter.setEditStatus(false);
                mAdapter.notifyDataSetChanged();
            }
        } else if (event.tag.equals(SubcriberTag.RELEASED_LIVING_DELETE)) {
            // 取消所选课堂
            if (course_type == (int) event.event) {
                deleteCourse();
            }

        }

    }

    // 取消所选课堂
    private void deleteCourse() {

        if (Utils.isEmpty(mAdapter.getChooseId())) {
            ToastHelper.showToast("请选择要" + (isNews() ? "删除的资讯" : "取消的课程"));
            return;
        }

        ((MyCoursesActivity) getActivity()).showNewDialog("正在取消...");

        HashMap<String, String> params = new HashMap<>();
        params.put("ID", GsonTools.toJson(mAdapter.getChooseId()));
        AnalyticsReport.onEvent(getContext(), LiveReport.MY_LIVE_EVENT_15457, params);
        params.clear();

        if (isNews()) {
            params.put("ids", GsonTools.toJson(mAdapter.getChooseId()));
            DataProvider.deleteNews(this, params, this);
        } else {
            params.put("course_ids", GsonTools.toJson(mAdapter.getChooseId()));
            DataProvider.deleteCourses(this, params, this);
        }

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

    // 是否是直播类型
    private boolean isLive() {
        return course_type == Course.LIVE;
    }

    // 是否是视频类型
    private boolean isVideo() {
        return course_type == Course.VIDEO;
    }

    // 是否是资讯类型
    private boolean isNews() {
        return course_type == Course.NEWS;
    }

    // 是否是我发布的类型
    private boolean isMyPushed() {
        return status.equals("1");
    }

    @Override
    public void onSuccess(Msg msg) {

        ((MyCoursesActivity) getActivity()).dismissDialog();
        List<String> mData = mAdapter.getChooseId();
        Iterator<Course> iterator = mAdapter.getmDatas().iterator();

        while (iterator.hasNext()) {

            Course course = iterator.next();

            for (String id : mData) {
                if (course.getId().equals(id)) {
                    if (isNews()) {
                        iterator.remove();
                    } else {
                        course.setStatus_text("已取消");
                        course.setStatus(Course.CANCEL);
                    }
                }
            }

        }
        if (isNews())
            showToast("所选资讯已删除");
        else
            showToast("所选课堂已取消");

        mAdapter.getChooseId().clear();
        mAdapter.getCourseId().clear();
        mAdapter.notifyDataSetChanged();
        ((MyCoursesActivity) getActivity()).setRightText();
    }

    @Override
    public void onFail(int errorCode, String errorMsg, String command) {
        showToast(errorMsg);
        ((MyCoursesActivity) getActivity()).setRightText();
        ((MyCoursesActivity) getActivity()).dismissDialog();
    }

}
