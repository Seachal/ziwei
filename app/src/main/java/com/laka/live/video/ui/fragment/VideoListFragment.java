package com.laka.live.video.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.laka.live.R;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.util.NetworkUtil;
import com.laka.live.util.Utils;
import com.laka.live.video.constant.VideoConstant;
import com.laka.live.video.constant.VideoEventConstant;
import com.laka.live.video.contract.MiniVideoListContract;
import com.laka.live.video.model.bean.MiniVideoBean;
import com.laka.live.video.model.http.bean.VideoListResponseBean;
import com.laka.live.video.presenter.MiniVideoListPresenter;
import com.laka.live.video.ui.adapter.VideoListAdapter;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/**
 * @Author:Rayman
 * @Date:2018/8/1
 * @Description:小视频列表Fragment，关注、推荐、最新三个tab都是用该Fragment
 */

public class VideoListFragment extends BaseVideoFragment implements MiniVideoListContract.IVideoListView {

    public static final String VIDEO_TYPE = "VIDEO_TYPE";
    public static final int VIDEO_TYPE_FOLLOW = 0;
    public static final int VIDEO_TYPE_RECOMMEND = 1;
    public static final int VIDEO_TYPE_LATEST = 2;

    @BindView(R.id.page_list_layout)
    PageListLayout mPageListLayout;

    /**
     * description:UI配置
     **/
    private int videoType = -1;
    private String videoTypeStr;
    private boolean isDoubleClickRefresh = false;

    /**
     * description:数据源配置
     **/
    private VideoListAdapter mAdapter;
    private int pageLimit = 10;
    private boolean isLoadMoreData = false;
    private GsonHttpConnection.OnResultListener mResultListener;
    private MiniVideoListContract.IVideoListPresenter mPresenter;

    public static VideoListFragment newInstance(int videoType) {
        VideoListFragment instance = new VideoListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(VIDEO_TYPE, videoType);
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_video_list;
    }

    @Override
    protected void initArguments() {
        videoType = getArguments().getInt(VIDEO_TYPE, VIDEO_TYPE_RECOMMEND);
        switch (videoType) {
            case VIDEO_TYPE_FOLLOW:
                videoTypeStr = VideoConstant.VIDEO_TYPE_FOLLOW;
                break;
            case VIDEO_TYPE_RECOMMEND:
                videoTypeStr = VideoConstant.VIDEO_TYPE_RECOMMEND;
                break;
            case VIDEO_TYPE_LATEST:
                videoTypeStr = VideoConstant.VIDEO_TYPE_NEWEST;
                break;
            default:
                break;
        }
    }

    @Override
    protected void initView(View rootView, Bundle savedInstanceState) {
        switch (videoType) {
            case VIDEO_TYPE_FOLLOW:
                break;
            case VIDEO_TYPE_RECOMMEND:
                break;
            case VIDEO_TYPE_LATEST:
                break;
            default:
                break;
        }


        mPageListLayout.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mPageListLayout.setPaddingTop(Utils.dp2px(mActivity, 6));
        mPageListLayout.setEnableRefresh(true);
        mPageListLayout.setIsLoadMoreEnable(true);
        mPageListLayout.setIsReloadWhenEmpty(true);
        mPageListLayout.showFooter(true);
    }

    @Override
    protected void initData() {
        mPresenter = new MiniVideoListPresenter(this);
        mAdapter = new VideoListAdapter(mActivity);
        mPageListLayout.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        mPageListLayout.setOnRequestCallBack(new PageListLayout.OnRequestCallBack() {
            @Override
            public String request(int page, GsonHttpConnection.OnResultListener listener) {
                mResultListener = listener;
                mPresenter.getVideoList(videoTypeStr, page, pageLimit);
                return null;
            }
        });
        mPageListLayout.addOnScrollListener(new PullToRefreshRecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                //因为主动调用 smoothScrollToPosition() 方法滚动的情况下也会回调这个函数，所以加个Flag判断
                if (isDoubleClickRefresh) { // 双击刷新
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        int offset = recyclerView.computeVerticalScrollOffset();
                        if (offset <= 0) {
                            // 列表滑动到顶部时，刷新列表
                            mPageListLayout.autoRefresh(false);
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (isDoubleClickRefresh && dy > 0) {
                    isDoubleClickRefresh = false;
                }
            }

            @Override
            public void onScroll(RecyclerView recyclerView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        isLoadMoreData = false;
        mPageListLayout.loadData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void onEvent(PostEvent event) {

    }

    /**
     *  双击底部短视频 tab 栏目，刷新当前fragment列表
     */
    @Override
    protected void updateVideoListWithAnim() {
        super.updateVideoListWithAnim();
        int offset = mPageListLayout.getRecyclerView().computeVerticalScrollOffset();
        if (offset > 0) {
            isDoubleClickRefresh = true;
            mPageListLayout.smoothScrollToPosition(0);
        } else {
            mPageListLayout.autoRefresh();
        }
    }

    @Override
    protected void refreshVideoList() {
        super.refreshVideoList();
        if (mPageListLayout.findFirstVisibleItemPosition() == 0) {
            //第一页的时候才刷新
            mPageListLayout.autoRefresh();
        }
    }

    @Override
    protected void updateVideoList() {
        super.updateVideoList();
        mPageListLayout.loadData();
    }

    @Override
    protected void updateVideoList(List<MiniVideoBean> videoBeanList) {
        super.updateVideoList(videoBeanList);
        mAdapter.setmDatas(videoBeanList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void loadVideoList() {
        super.loadVideoList();
        isLoadMoreData = true;
        mPageListLayout.onLoadMoreItems();
    }

    @Override
    public void showVideoList(VideoListResponseBean responseBean) {
        if (responseBean == null) {
            if (NetworkUtil.isNetworkOk(mActivity)) {
                mPageListLayout.showEmpty();
            } else {
                mPageListLayout.showError();
            }
            return;
        }
        mResultListener.onSuccess(responseBean);
        if (isLoadMoreData) {
            //通知小视频播放页面列表，添加更新为当前列表
            EventBusManager.postEvent(mAdapter.getmDatas(), VideoEventConstant.UPDATE_VIDEO_PLAY_LIST);
        }
    }

    @Override
    protected void scrollToPosition(final int position) {
        super.scrollToPosition(position);
        mPageListLayout.scrollToPosition(position, 0);
    }
}
