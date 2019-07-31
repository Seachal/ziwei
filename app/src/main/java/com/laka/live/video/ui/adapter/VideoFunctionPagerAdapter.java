package com.laka.live.video.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.laka.live.R;
import com.laka.live.util.ResourceHelper;
import com.laka.live.video.ui.widget.videofunction.VideoCommentView;
import com.laka.live.video.ui.widget.videofunction.VideoFunctionHelper;
import com.laka.live.video.ui.widget.videofunction.VideoRecommendGoodView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:Rayman
 * @Date:2018/8/13
 * @Description:小视频页面下方功能Adapter
 */

public class VideoFunctionPagerAdapter extends PagerAdapter {

    private final static int TAB_COUNT = 2;

    private String[] titleList = new String[]{
            ResourceHelper.getString(R.string.video_function_comment),
            ResourceHelper.getString(R.string.video_function_good)
    };
    private VideoCommentView commentView;
    private VideoRecommendGoodView recommendGoodView;

    private List<View> functionViews = new ArrayList<>();

    public VideoFunctionPagerAdapter(Context context, VideoFunctionHelper mHelper) {
        commentView = new VideoCommentView(context);
        recommendGoodView = new VideoRecommendGoodView(context);
        commentView.setVideoUIHelper(mHelper);
        recommendGoodView.setVideoUIHelper(mHelper);
        functionViews.add(commentView);
        functionViews.add(recommendGoodView);

        //已经确定是同一个对象了
        //Logger.i("输出functionView的对象----commentView:" + commentView + "\nRecommendView：" + recommendGoodView);
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
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(functionViews.get(position));
        //已经确定是同一个对象了。但是为什么RecommendView和CommentView会出现多个
        //Logger.i("instantiateItem----" + functionViews.get(position));
        return functionViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(functionViews.get(position));
    }

    public String[] getTitleList() {
        return titleList;
    }

    public void onRelease() {
        ((VideoCommentView)functionViews.get(0)).onRelease();
        ((VideoRecommendGoodView)functionViews.get(1)).onRelease();
    }
}
