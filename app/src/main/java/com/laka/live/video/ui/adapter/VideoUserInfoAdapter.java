package com.laka.live.video.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * @Author:Rayman
 * @Date:2018/8/23
 * @Description:使用ViewPager的方式实现拖动，和普通PagerAdapter一样
 */

public class VideoUserInfoAdapter extends PagerAdapter {

    private List<View> mViews;

    public VideoUserInfoAdapter(List<View> mViews) {
        this.mViews = mViews;
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViews.get(position));
        return mViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }
}
