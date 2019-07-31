package com.laka.live.video.ui.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.laka.live.R;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.video.constant.VideoEventConstant;
import com.laka.live.video.model.bean.MiniVideoBean;
import com.laka.live.video.ui.adapter.VideoHomePagerAdapter;
import com.laka.live.video.ui.adapter.VideoTabIndicatorAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Author:Rayman
 * @Date:2018/8/1
 * @Description:小视频主页Fragment
 */

public class VideoHomeFragment extends BaseVideoFragment {

    public static final int DEFAULT_PAGER_LOAD = 2;

    @BindView(R.id.indicator_video)
    MagicIndicator mIndicator;
    @BindView(R.id.vp_video_home)
    ViewPager mVpVideoHome;

    private BaseVideoFragment currentFragment;

    /**
     * description:列表配置
     **/
    private int targetIndex = 1;
    private FragmentManager mManager;
    private VideoHomePagerAdapter mAdapter;

    @Override
    protected int setContentView() {
        return R.layout.fragment_home_video;
    }

    @Override
    protected void initArguments() {

    }

    @Override
    protected void initView(View rootView, Bundle savedInstanceState) {
        initViewPager();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    private void initViewPager() {
        mManager = getChildFragmentManager();
        mAdapter = new VideoHomePagerAdapter(mManager);
        mVpVideoHome.setAdapter(mAdapter);
        mVpVideoHome.setOffscreenPageLimit(DEFAULT_PAGER_LOAD);
        initIndicator(mAdapter.getTitleList());
        //需要在配置之后才set，不然位置会错乱
        mVpVideoHome.setCurrentItem(targetIndex);
        currentFragment = (BaseVideoFragment) mAdapter.getItem(targetIndex);
        mVpVideoHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentFragment = (BaseVideoFragment) mAdapter.getItem(position);
                currentFragment.refreshVideoList();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initIndicator(String[] titleList) {
        CommonNavigator navigator = new CommonNavigator(mActivity);
        VideoTabIndicatorAdapter indicatorAdapter = new VideoTabIndicatorAdapter(mActivity, titleList);
        indicatorAdapter.setTabSelectListener(new VideoTabIndicatorAdapter.TabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mVpVideoHome.setCurrentItem(position);
            }
        });
        navigator.setAdapter(indicatorAdapter);
        mIndicator.setNavigator(navigator);
        // must after setNavigator
        LinearLayout titleContainer = navigator.getTitleContainer();
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerDrawable(new ColorDrawable() {
            @Override
            public int getIntrinsicWidth() {
                return UIUtil.dip2px(mActivity, 20);
            }
        });
        ViewPagerHelper.bind(mIndicator, mVpVideoHome);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void onEvent(PostEvent event) {
        switch (event.getTag()) {
            case SubcriberTag.VIDEO_TAB_DOUBLE_CLICK_EVENT:  // 双击底部短视频 tab 栏目，刷新当前fragment列表
                currentFragment.updateVideoListWithAnim();
                break;
            case VideoEventConstant.UPDATE_VIDEO_LIST:  // MiniVideoPlayActivity 页面发送来的更新视频列表的通知
                if (event.getEvent() != null && event.getEvent() instanceof ArrayList) {
                    currentFragment.updateVideoList((List<MiniVideoBean>) event.getEvent());
                } else {
                    int currentPosition = (int) event.getEvent();
                    currentFragment.updateVideoList();
                }
                break;
            case VideoEventConstant.LOAD_VIDEO_LIST:    // MiniVideoPlayActivity 页面通知 VideoListFragment 加载更多视频数据
                currentFragment.loadVideoList();
                break;
            case VideoEventConstant.UPDATE_VIDEO_POSITION: // 刷新列表位置
                currentFragment.scrollToPosition((Integer) event.getEvent());
                break;
            default:
                break;
        }
    }
}
