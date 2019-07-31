package com.laka.live.video.ui.widget.videopager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.laka.live.util.ToastHelper;
import com.laka.live.video.model.bean.MiniVideoBean;
import com.laka.live.video.ui.adapter.MiniVideoRecyclerAdapter;
import com.laka.live.video.ui.widget.layoutmanager.OnViewPagerListener;
import com.laka.live.video.ui.widget.layoutmanager.ViewPagerLayoutManager;
import com.laka.live.video.ui.widget.videoplayer.MiniVideoPlayerView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:Rayman
 * @Date:2018/8/13
 * @Description:可滑动的视频播放ViewPager
 */

public class ScrollVideoPagerView extends FrameLayout implements IScrollVideoPagerView {

    private final String TAG = this.getClass().getSimpleName();

    private Context mContext;

    /**
     * description:数据源配置
     **/
    private RecyclerView mRvVideo;
    private ViewPagerLayoutManager mLayoutManager;
    private MiniVideoRecyclerAdapter mAdapter;

    /**
     * description:参数配置
     **/
    private int orientation = -1;
    private boolean isVertical = true;
    private int currentPosition;
    // 当前显示 item 的 view
    private MiniVideoPlayerView mPlayerView;
    private List<MiniVideoBean> mVideoList;
    private MiniVideoBean mVideo;

    /**
     * description:回调配置
     **/
    private boolean isPagerFirstInit = false;
    private IScrollVideoPagerView.OnPagerChangeListener pagerChangeListener;

    public ScrollVideoPagerView(Context context) {
        this(context, null);
    }

    public ScrollVideoPagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initProperties(context, attrs);
        initView();
    }

    @Override
    public void initProperties(Context context, AttributeSet attributeSet) {

    }

    @Override
    public void initView() {
        mVideoList = new ArrayList<>();
        initRecyclerView();
        addView(mRvVideo, new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void initRecyclerView() {
        mRvVideo = new RecyclerView(mContext);
        initLayoutManager(isVertical);
        mRvVideo.setLayoutManager(mLayoutManager);
        mAdapter = new MiniVideoRecyclerAdapter(mVideoList);
        mRvVideo.setAdapter(mAdapter);
    }

    @Override
    public IScrollVideoPagerView setOrientation(int orientation) {
        this.orientation = orientation;
        if (orientation == IScrollVideoPagerView.ORIENTATION_HORIZONTAL) {
            isVertical = false;
        } else {
            isVertical = true;
        }
        initLayoutManager(isVertical);
        return this;
    }

    @Override
    public IScrollVideoPagerView setUpPlayVideoList(List<MiniVideoBean> playVideoList) {
        mVideoList = playVideoList;
        //Logger.i(TAG, "设置播放视频列表：" + playVideoList);
        mAdapter.updateList(playVideoList);
        return this;
    }

    @Override
    public IScrollVideoPagerView setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        updateCurrentPosition(currentPosition);
        return this;
    }

    @Override
    public IScrollVideoPagerView setPagerChangeListener(OnPagerChangeListener onPagerChangeListener) {
        this.pagerChangeListener = onPagerChangeListener;
        return this;
    }

    @Override
    public List<MiniVideoBean> getVideoList() {
        return mVideoList;
    }

    @Override
    public IScrollVideoPagerView enableScroll(boolean isEnable) {
        mLayoutManager.setEnableScroll(isEnable);
        return this;
    }

    @Override
    public void updateData(List<MiniVideoBean> newData) {
        mVideoList.clear();
        mVideoList.addAll(newData);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setData(List<MiniVideoBean> newData) {
        mVideoList.clear();
        mVideoList.addAll(newData);
        Logger.e("PagerView输出列表：" + newData);
    }

    @Override
    public int getCurrentPage() {
        return currentPosition;
    }

    @Override
    public void onResume() {
        if (mPlayerView != null) {
            mPlayerView.resumeVideoPlay();
        }
    }

    @Override
    public void onResume(int progress) {
        if (mPlayerView != null) {
            mPlayerView.resumeVideoPlay(progress);
        }
    }

    @Override
    public void onPause() {
        if (mPlayerView != null) {
            mPlayerView.pauseVideoPlay();
        }
    }

    @Override
    public void onDestroy() {
        onSaveCurrentVideoProgress();
        pagerChangeListener = null;
        mAdapter = null;
        if (mRvVideo != null) {
            mRvVideo.clearOnScrollListeners();
            mRvVideo = null;
        }
        if (mLayoutManager != null) {
            mLayoutManager.setOnViewPagerListener(null);
            mLayoutManager = null;
        }
        if (mPlayerView != null) {
            mPlayerView.totallyRelease();
            mPlayerView = null;
        }
    }

    private void initLayoutManager(boolean isVertical) {
        mLayoutManager = new ViewPagerLayoutManager(mContext, isVertical ? LinearLayoutManager.VERTICAL
                : LinearLayoutManager.HORIZONTAL);
        mLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {

            // 页面滚动时候是否到顶部(底部)继续滚动
            @Override
            public void onPagerStillScroll(boolean isUpScroll) {
                if (!isUpScroll && currentPosition == 0) {
                    ToastHelper.showCenterToast("已经到顶啦~");
                } else if (isUpScroll && currentPosition == mVideoList.size() - 1) {
                    ToastHelper.showCenterToast("没有更多小视频啦~");
                }
            }

            // 页面加载完成
            @Override
            public void onInitComplete() {
                //Logger.i("LayoutManager----onInitComplete");
                MiniVideoBean video = mVideoList.get(currentPosition);
                mVideo = mVideoList.get(0);
                mPlayerView = (MiniVideoPlayerView) mRvVideo.getChildAt(0);
                mPlayerView.startVideoPlay(video.getVideoCover(), video.getVideoUrl());
                notifyPagerChange(0, true);
            }

            // 滑动销毁
            @Override
            public void onPageRelease(boolean isNext, int position) {
                int index = 0;
                if (isNext) {
                    index = 0;
                } else {
                    index = 1;
                }

                /**
                 * description:这里的一个判断是根据手势判断的，
                 * 因为页面不完全滚动，即滚到一小段然后放手，那么页面会重置。但是这里position为2，所以需要加判断
                 **/
                if (currentPosition == position) {
                    mPlayerView = (MiniVideoPlayerView) mRvVideo.getChildAt(index);
                    // 保存播放进度
                    onSaveCurrentVideoProgress();
                    mPlayerView.release();
                }
            }

            // 选中的监听以及判断是否滑动到底部
            @Override
            public void onPageSelected(int position, boolean isBottom) {
                /**
                 * description:这个和release的情况相似，页面不完全滚动，position会和currentPosition初始化的变量一致
                 * 所以也需要加判断
                 **/
                if (currentPosition != position) {
                    mVideo = mVideoList.get(position);
                    currentPosition = position;
                    mPlayerView = (MiniVideoPlayerView) mRvVideo.getChildAt(0);
                    mPlayerView.startVideoPlay(mVideo.getVideoCover(), mVideo.getVideoUrl());
                    notifyPagerChange(position, false);

                }
            }
        });

        mRvVideo.setLayoutManager(mLayoutManager);
    }

    /**
     * 保存当前播放视频进度
     */
    public void onSaveCurrentVideoProgress() {
        // 保存播放进度
        if (mPlayerView != null) {
            mPlayerView.saveCurrentProgress();
        }
    }

    /**
     * 选中position
     *
     * @param currentPosition
     */
    private void updateCurrentPosition(int currentPosition) {
        mRvVideo.scrollToPosition(currentPosition);
    }

    public MiniVideoBean getCurrentVideo() {
        return mAdapter.getCurrentVideo(currentPosition);
    }

    /**
     * 获取当前的PlayerView
     *
     * @return
     */
    public MiniVideoPlayerView getPlayerView() {
        return mPlayerView;
    }

    private void notifyPagerChange(int position, boolean isInit) {
        if (pagerChangeListener != null) {
            pagerChangeListener.onPageChange(position, isInit);
        }
    }


}
