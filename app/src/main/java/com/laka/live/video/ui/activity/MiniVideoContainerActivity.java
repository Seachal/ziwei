package com.laka.live.video.ui.activity;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.laka.live.R;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.PostEvent;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.activity.UserInfoActivity;
import com.laka.live.util.Utils;
import com.laka.live.video.constant.VideoConstant;
import com.laka.live.video.constant.VideoEventConstant;
import com.laka.live.video.model.bean.MiniVideoBean;
import com.laka.live.video.model.event.MiniVideoEvent;
import com.laka.live.video.model.event.VideoDecorViewEvent;
import com.laka.live.video.ui.adapter.VideoUserInfoAdapter;
import com.laka.live.video.ui.widget.parallaxpager.ParallaxPager;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:Rayman
 * @Date:2018/8/23
 * @Description:小视频播放页面外层容器（使用了LocalActivityManager实现Activity+ViewPager）
 */

public class MiniVideoContainerActivity extends BaseActivity {

    /**
     * description:Activity切换
     **/
    private int videoId = -1;
    private ArrayList<MiniVideoBean> mVideoList;
    private int videoInListPosition = 0;
    private int pagePosition = 0;
    private LocalActivityManager activityManager;

    /**
     * description:Pager管理
     **/
    private int userId = -1;
    private VideoUserInfoAdapter mAdapter;
    private List<View> mViews;
    private ParallaxPager mVpContainer;

    public static void startActivity(Context context, int videoId) {
        Intent intent = new Intent();
        intent.putExtra(VideoConstant.VIDEO_INFO_EXTRA_ID, videoId);
        intent.setClass(context, MiniVideoContainerActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String videoUrl) {
        Intent intent = new Intent();
        intent.putExtra(VideoConstant.VIDEO_INFO_EXTRA_URL, videoUrl);
        intent.setClass(context, MiniVideoContainerActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, ArrayList<MiniVideoBean> videoList, int position) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(VideoConstant.VIDEO_INFO_EXTRA_LIST, videoList);
        bundle.putInt(VideoConstant.VIDEO_INFO_EXTRA_POSITION, position);
        intent.putExtras(bundle);
        intent.setClass(context, MiniVideoContainerActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_container);
        activityManager = new LocalActivityManager(this, true);
        activityManager.dispatchCreate(savedInstanceState);
        initIntent(getIntent());
        initView();
    }

    private void initIntent(Intent intent) {
        videoInListPosition = intent.getIntExtra(VideoConstant.VIDEO_INFO_EXTRA_POSITION, 1);
        videoId = intent.getIntExtra(VideoConstant.VIDEO_INFO_EXTRA_ID, -1);
        if (intent.getExtras() != null) {
            mVideoList = (ArrayList<MiniVideoBean>) intent.getExtras().getSerializable(VideoConstant.VIDEO_INFO_EXTRA_LIST);
            if (mVideoList == null) {
                mVideoList = new ArrayList<>();
            }
        }
    }

    private void initView() {
        mVpContainer = findById(R.id.vp_video_container);
        mViews = new ArrayList<>();
        if (!Utils.isEmpty(mVideoList)) {
            mViews.add(getMiniVideoPlayView(mVideoList, videoInListPosition));
        } else {
            mViews.add(getMiniVideoPlayView(videoId));
        }
        mViews.add(getUserInfoView(userId));
        EventBusManager.postEvent(mViews, VideoEventConstant.UPDATE_CONTAINER_VIEW);
        mVpContainer.setUpChildView(mViews);
        mAdapter = new VideoUserInfoAdapter(mViews);
        mVpContainer.setAdapter(mAdapter);
        mVpContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //当页面切换到第二页的时候，暂停播放
                pagePosition = position;
                if (position == 1) {
                    setSwipeBackEnable(false);
                    handleActivityLifeCycle(VideoEventConstant.VIDEO_PAUSE);
                } else {
                    setSwipeBackEnable(true);
                    handleActivityLifeCycle(VideoEventConstant.VIDEO_RESUME);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private View getMiniVideoPlayView(int videoId) {
        Intent intent = new Intent();
        intent.putExtra(VideoConstant.VIDEO_INFO_EXTRA_ID, videoId);
        intent.setClass(this, MiniVideoPlayActivity.class);
        return activityManager.startActivity("MiniVideoPlayActivity", intent).getDecorView();
    }

    private View getMiniVideoPlayView(ArrayList<MiniVideoBean> videoList, int position) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(VideoConstant.VIDEO_INFO_EXTRA_LIST, videoList);
        bundle.putInt(VideoConstant.VIDEO_INFO_EXTRA_POSITION, position);
        intent.putExtras(bundle);
        intent.setClass(this, MiniVideoPlayActivity.class);
        return activityManager.startActivity("MiniVideoPlayActivity", intent).getDecorView();
    }

    private View getUserInfoView(int userId) {
        Intent intent = new Intent(this, UserInfoActivity.class);
        intent.putExtra(UserInfoActivity.EXTRA_USER_ID, userId);
        intent.putExtra(UserInfoActivity.ENABLE_VIEW_MODE, true);
        intent.setClass(this, UserInfoActivity.class);
        return activityManager.startActivity("UserInfoActivity", intent).getDecorView();
    }

    @Override
    public void onEvent(PostEvent event) {
        switch (event.getTag()) {
            case VideoEventConstant.BACK_TO_MINI_PLAY:
                if (!this.isDestroyed() && !this.isFinishing()) {
                    mVpContainer.setCurrentItem(0);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Logger.e("ContainerActivity----onBackPressed");
        if (mVpContainer != null && mVpContainer.getCurrentItem() == 1) {
            mVpContainer.setCurrentItem(0);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.e("ContainerActivity-----onResume：" + videoInListPosition);

        //activityManager.dispatchResume();//测试后发现失效
        if (pagePosition == 0) {
            Logger.e("ContainerActivity----dispatch---onResume");
            handleActivityLifeCycle(VideoEventConstant.VIDEO_RESUME);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.e("ContainerActivity-----onPause：" + videoInListPosition);
        //activityManager.dispatchPause(false);//测试后发现失效
        if (pagePosition == 0) {
            Logger.e("ContainerActivity----dispatch---onPause");
            handleActivityLifeCycle(VideoEventConstant.VIDEO_PAUSE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.e("MiniVideoContainerActivity-------onDestroy" + this);
        mVideoList = null;
        mAdapter = null;
        mVpContainer = null;
        //貌似方法失效了
//        activityManager.dispatchDestroy(true);
//        activityManager.removeAllActivities();
        //需要释放内存，不然Activity会一直被ActivityManger持有
        activityManager = null;
        handleActivityLifeCycle(VideoEventConstant.VIDEO_DESTROY);
        mViews = null;
    }

    /**
     * description:由于H5的跳转也会产生新的页面B，假若之前就存在小视频播放的页面A
     * 那么B在生命周期回调的时候也会发送事件，然后A也会收到这个事件，所以就引起了AB同时接收到事件
     * 所以这边发送一个View去判断是否执行PlayerActivity和UserInfoActivity的生命周期回调
     * 例如销毁的时候，AB页面会因为对象没判断唯一性就一同销毁
     **/
    private void handleActivityLifeCycle(String lifeCycle) {
        if (mViews != null && !Utils.isEmpty(mViews)) {
            MiniVideoEvent event = new MiniVideoEvent();
            event.setTag(lifeCycle);
            VideoDecorViewEvent videoDecorViewEvent = new VideoDecorViewEvent(mViews);
            event.setTargetObj(videoDecorViewEvent);
            //暂时只有UserInfoActivity需要这个userId
            event.setEvent(userId);
            EventBusManager.postEvent(event);
        }
    }
}
