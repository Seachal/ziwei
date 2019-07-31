package com.laka.live.video.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.laka.live.R;
import com.laka.live.video.ui.fragment.BaseVideoFragment;
import com.laka.live.video.ui.fragment.VideoListFragment;
import com.laka.live.util.ResourceHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:Rayman
 * @Date:2018/8/1
 * @Description:小视频主页PagerAdapter
 */

public class VideoHomePagerAdapter extends FragmentPagerAdapter {

    private final static int TAB_COUNT = 3;
    private final static int POSITION_FOLLOW = 0;
    private final static int POSITION_NEW = 1;
    private final static int POSITION_CHOICE = 2;

    private String[] titleList = new String[]{
            ResourceHelper.getString(R.string.video_home_tab_follow),
            ResourceHelper.getString(R.string.video_home_tab_recommend),
            ResourceHelper.getString(R.string.video_home_tab_new)
    };

    private List<BaseVideoFragment> videoFragmentList = new ArrayList<>();

    public VideoHomePagerAdapter(FragmentManager fm) {
        super(fm);
        videoFragmentList.add(VideoListFragment.newInstance(VideoListFragment.VIDEO_TYPE_FOLLOW));
        videoFragmentList.add(VideoListFragment.newInstance(VideoListFragment.VIDEO_TYPE_RECOMMEND));
        videoFragmentList.add(VideoListFragment.newInstance(VideoListFragment.VIDEO_TYPE_LATEST));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList[position];
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return videoFragmentList.get(position);
    }

    public String[] getTitleList() {
        return titleList;
    }

    public List<BaseVideoFragment> getVideoFragmentList() {
        return videoFragmentList;
    }
}
