package com.laka.live.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.Course;
import com.laka.live.bean.HomeLiving;
import com.laka.live.bean.HomeRecommendTitle;
import com.laka.live.bean.HomeSeeAll;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.HomeBannersMsg;
import com.laka.live.msg.HomeFunctionMsg;
import com.laka.live.msg.HomeHotTopicsMsg;
import com.laka.live.msg.HomeLivingMsg;
import com.laka.live.msg.HomeRecommendMsg;
import com.laka.live.msg.Msg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.homepage.adapter.HomePageAdapter;
import com.laka.live.ui.search.SearchActivity;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.ui.widget.ptr.PtrLakaFrameLayout;
import com.laka.live.util.Log;
import com.laka.live.util.Utils;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lyf on 2017/5/26.
 * 精品课 Fragment
 */
public class MainFragment extends BaseFragment implements EventBusManager.OnEventBusListener
        , PageListLayout.OnRequestCallBack, View.OnClickListener {
    private final static String TAG = MainFragment.class.getSimpleName();

    /**
     * description:这里放两个ImageView主要是因为xml布局文件的高度比较难控制
     * 一个作为外层，一个内层
     **/
    private RelativeLayout mTitleBar;
    private ImageView mIvBarSearch;
    private ImageView mIvSearch;
    private int scrollY = -1;

    private final static int PAGE_START = 0;
    private final static int TOPIC_LIMIT = 5;
    private final static int TYPE_LIVE_VIDEO = 0;

    /**
     * description:UI配置
     **/
    private int bannerHeight;
    private float bannerAlpha;
    private boolean isFirstScrolled = true;
    private boolean isDoubleClickRefresh = false;

    private PageListLayout mPageListLayout;

    private HomePageAdapter mAdapter;
    private List<Object> mDataList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, null);
//        getActivity().setTheme(R.style.TranslucentActivityTheme);
//        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_);


        mTitleBar = view.findViewById(R.id.titleBar);
        mIvBarSearch = view.findViewById(R.id.iv_search);
        mIvSearch = view.findViewById(R.id.search);
        mIvBarSearch.setOnClickListener(this);
        mIvSearch.setOnClickListener(this);

        mPageListLayout = (PageListLayout) view.findViewById(R.id.page_list_layout);
        mPageListLayout.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mPageListLayout.setEnableRefresh(true);
        mPageListLayout.setIsLoadMoreEnable(true);
        mPageListLayout.setIsReloadWhenEmpty(true);
        mPageListLayout.showFooter(false);
        mAdapter = new HomePageAdapter(this);
        mPageListLayout.setAdapter(mAdapter);
        mPageListLayout.setOnRequestCallBack(this);

        mDataList = new ArrayList<>();
        mPageListLayout.loadData(true);

        handleScrollState();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBusManager.register(this);//注册EventBus

    }

    @Override
    public void onResume() {
        super.onResume();
        AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10231);
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PostEvent event) {
        if (event.tag.equals(SubcriberTag.MAIN_TAB_DOUBLE_CLICK_EVENT)) {
            RecyclerView recyclerView = mPageListLayout.getRecyclerView();
            int offset = recyclerView.computeVerticalScrollOffset();
            if (offset > 0) {
                //假若页面没有置顶，先滚动到顶部，然后根据doubleClickRefresh刷新
                isDoubleClickRefresh = true;
                mPageListLayout.smoothScrollToPosition(0);
            } else {
                mPageListLayout.autoRefresh();
            }
        }
    }

    /**
     * 处理滚动
     */
    private void handleScrollState() {
        mPageListLayout.addOnScrollListener(new PullToRefreshRecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                //因为滚动的情况下也会回调这个函数，所以加个Flag判断，然后再调用刷新的函数
                if (isDoubleClickRefresh) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        int offset = recyclerView.computeVerticalScrollOffset();
                        if (offset <= 0) {
                            mPageListLayout.autoRefresh(false);
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //页面上滑的时候重置flag状态
                if (isDoubleClickRefresh && dy > 0) {
                    isDoubleClickRefresh = false;
                }

                //第一次进入页面也会回调这个方法，所以第一次需要return，不然标题栏会根据回调隐藏了= =
                if (isFirstScrolled) {
                    isFirstScrolled = false;
                    return;
                }

                scrollY += dy;
                bannerHeight = mAdapter.getBannerHeight();
                //从Banner高度的1/2开始渐变，到3/4结束
                bannerAlpha = (float) scrollY / (bannerHeight - bannerHeight / 4);
//                Logger.e("输出dy：" + dy + "\nScrollY：" + scrollY);
//                if (scrollY >= bannerHeight / 2 && scrollY < (bannerHeight - bannerHeight / 4)) {
//                    //滚动到Banner的一半之后
//                    mIvSearch.setVisibility(View.GONE);
//                } else if (scrollY >= bannerHeight / 2 && scrollY < (bannerHeight - bannerHeight / 4)
//                        && dy < 0) {
//                    mIvSearch.setVisibility(View.VISIBLE);
//                }
                mTitleBar.setAlpha(bannerAlpha);
                if (mTitleBar.getAlpha() <= 0) {
                    mIvSearch.setVisibility(View.VISIBLE);
                } else {
                    if (mIvSearch.getVisibility() == View.VISIBLE) {
                        mIvSearch.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onScroll(RecyclerView recyclerView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        mPageListLayout.setOnPullStateChangeListener(new PtrLakaFrameLayout.OnPullStateChangeListener() {
            @Override
            public void onPullStateChange(int state) {
                switch (state) {
                    case PtrLakaFrameLayout.OnPullStateChangeListener.STATE_PULLING:
                        //隐藏搜索
                        mIvSearch.setVisibility(View.GONE);
                        break;
                    case PtrLakaFrameLayout.OnPullStateChangeListener.STATE_REFRESHING:
                        break;
                    case PtrLakaFrameLayout.OnPullStateChangeListener.STATE_REFRESH_COMPLETE:
                        break;
                    case PtrLakaFrameLayout.OnPullStateChangeListener.STATE_REFRESH_RESET:
                        mIvSearch.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
            case R.id.search:
                SearchActivity.startActivity(getActivity());
                AnalyticsReport.onEvent(mContext, AnalyticsReport.HOME_SEARCH_BUTTON_CLICK_EVENT_ID);
                break;
            default:
                break;
        }
    }

    /**
     * 重置TitleBar
     */
    private void resetTopTitleBar() {
        mTitleBar.setAlpha(0);
        mIvSearch.setVisibility(View.VISIBLE);
    }

    @Override
    public String request(int page, GsonHttpConnection.OnResultListener listener) {
        mDataList.clear();
        if (page == 1) {
            resetTopTitleBar();
        }
        queryBanner();
        return null;
    }

    /**
     * 获取banner
     */
    private void queryBanner() {
        DataProvider.queryHomeBanners(this, new GsonHttpConnection.OnResultListener<HomeBannersMsg>() {
            @Override
            public void onSuccess(HomeBannersMsg homeBannersMsg) {
                Log.d(TAG, "queryHomeBanners Success : " + homeBannersMsg);
                if (homeBannersMsg != null && !homeBannersMsg.isEmpty()) {
                    mDataList.add(homeBannersMsg);
                }

                queryFunction();
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                Log.d(TAG, "queryHomeBanners Fail . errorMsg : " + errorMsg + " ; command : " + command);

                showToast(errorMsg);

                if (errorCode == Msg.NETWORK_ERROR_NETWORK_ERROR) {
                    showNetworkError();
                } else {
                    queryFunction();
                }
            }
        });
    }

    /**
     * 功能数据（课程分类）
     */
    private void queryFunction() {

        DataProvider.queryHomeFunction(this, new GsonHttpConnection.OnResultListener<HomeFunctionMsg>() {
            @Override
            public void onSuccess(HomeFunctionMsg homeFunctionMsg) {
                Log.d(TAG, "queryHomeFunction Success : " + homeFunctionMsg);

                if (homeFunctionMsg != null && !homeFunctionMsg.isEmpty()) {
                    mDataList.add(homeFunctionMsg);
                }

                queryLiving();
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                Log.d(TAG, "queryHomeFunction Fail . errorMsg : " + errorMsg + " ; command : " + command);

                showToast(errorMsg);

                if (errorCode == Msg.NETWORK_ERROR_NETWORK_ERROR) {
                    showNetworkError();
                } else {
                    queryLiving();
                }

            }
        });
    }

    /**
     * 获取正在直播
     */
    private void queryLiving() {
        DataProvider.queryHomeLiving(this, new GsonHttpConnection.OnResultListener<HomeLivingMsg>() {
            @Override
            public void onSuccess(HomeLivingMsg homeLivingMsg) {
                Log.d(TAG, "queryHomeLiving Success : " + homeLivingMsg);

                if (homeLivingMsg != null && !Utils.listIsNullOrEmpty(homeLivingMsg.getData())) {
                    HomeLiving homeLiving;
                    boolean isFirst = true;
                    for (Course course : homeLivingMsg.getData()) {
                        homeLiving = new HomeLiving();
                        homeLiving.setCourse(course);
                        if (isFirst) {
                            homeLiving.setFirst(isFirst);
                            isFirst = false;
                        }
                        mDataList.add(homeLiving);
                    }
                }

                queryHotTopic();
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                Log.d(TAG, "queryHomeLiving Fail . errorMsg : " + errorMsg + " ; command : " + command);

                showToast(errorMsg);

                if (errorCode == Msg.NETWORK_ERROR_NETWORK_ERROR) {
                    showNetworkError();
                } else {
                    queryHotTopic();
                }
            }
        });
    }

    private void showNetworkError() {
        if (mAdapter.isEmpty()) {
            mPageListLayout.showNetWorkError();
        }
        mPageListLayout.setOnRefreshComplete();
        mPageListLayout.onFinishLoading(false, false);
    }

    /**
     * 获取热门话题
     */
    private void queryHotTopic() {
        DataProvider.queryHomeHotTopics(this, PAGE_START, TOPIC_LIMIT, new GsonHttpConnection.OnResultListener<HomeHotTopicsMsg>() {
            @Override
            public void onSuccess(HomeHotTopicsMsg homeHotTopicsMsg) {
                Log.d(TAG, "queryHomeHotTopics Success : " + homeHotTopicsMsg);

                if (homeHotTopicsMsg != null && !homeHotTopicsMsg.isEmpty()) {
                    mDataList.add(homeHotTopicsMsg);
                }

                queryRecommend();
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                Log.d(TAG, "queryHomeHotTopics Fail . errorMsg : " + errorMsg + " ; command : " + command);

                showToast(errorMsg);

                if (errorCode == Msg.NETWORK_ERROR_NETWORK_ERROR) {
                    showNetworkError();
                } else {
                    queryRecommend();
                }
            }
        });
    }

    /**
     * 获取推荐课程
     */
    private void queryRecommend() {
        DataProvider.queryHomeRecommend(this, TYPE_LIVE_VIDEO,
                new GsonHttpConnection.OnResultListener<HomeRecommendMsg>() {
                    @Override
                    public void onSuccess(HomeRecommendMsg homeRecommendMsg) {
                        Log.d(TAG, "queryHomeRecommend Success : " + homeRecommendMsg);

                        mPageListLayout.setOnRefreshComplete();

                        if (homeRecommendMsg != null
                                && homeRecommendMsg.getList() != null
                                && !Utils.isEmpty(homeRecommendMsg.getList())) {
                            mDataList.add(new HomeRecommendTitle(mDataList.size()));
                            mDataList.addAll(homeRecommendMsg.getList());
                            mDataList.add(new HomeSeeAll(mDataList.size()));
                            mPageListLayout.addCurrentPage();
                        }

                        mPageListLayout.onFinishLoading(false, false);

                        mAdapter.clear();
                        mAdapter.addAll(mDataList);

                        if (Utils.isEmpty(mDataList)) {
                            mPageListLayout.showEmpty();
                        } else {
                            mPageListLayout.showData();
                        }
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg, String command) {
                        Log.d(TAG, "queryHomeRecommend Fail . errorMsg : " + errorMsg + " ; command : " + command);

                        showToast(errorMsg);

                        mPageListLayout.setOnRefreshComplete();
                        mPageListLayout.onFinishLoading(false, false);

                        if (Utils.isEmpty(mDataList)) {
                            mPageListLayout.showNetWorkError();
                        } else {
                            mAdapter.clear();
                            mAdapter.addAll(mDataList);
                            mPageListLayout.showData();
                        }
                    }
                });
    }
}
