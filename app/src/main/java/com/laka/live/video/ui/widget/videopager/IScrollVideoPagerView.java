package com.laka.live.video.ui.widget.videopager;

import android.content.Context;
import android.support.annotation.IntDef;
import android.util.AttributeSet;

import com.laka.live.video.model.bean.MiniVideoBean;

import java.util.List;

/**
 * @Author:Rayman
 * @Date:2018/8/13
 * @Description:滚动VideoPagerView
 */

public interface IScrollVideoPagerView {

    int ORIENTATION_VERTICAL = 1;
    int ORIENTATION_HORIZONTAL = 2;

    @IntDef({
            ORIENTATION_VERTICAL,
            ORIENTATION_HORIZONTAL
    })
    @interface VIDEO_ORIENTATION {
    }

    void initProperties(Context context, AttributeSet attributeSet);

    void initView();

    /**
     * 设置上下切换还是左右切换
     *
     * @param orientation
     * @return
     */
    IScrollVideoPagerView setOrientation(@VIDEO_ORIENTATION int orientation);

    /**
     * 设置播放视频集合
     *
     * @param playVideoList
     * @return
     */
    IScrollVideoPagerView setUpPlayVideoList(List<MiniVideoBean> playVideoList);

    /**
     * 设置当前播放的视频位置
     *
     * @param currentPosition
     * @return
     */
    IScrollVideoPagerView setCurrentPosition(int currentPosition);

    /**
     * 页面滚动回调
     *
     * @param onPagerChangeListener
     * @return
     */
    IScrollVideoPagerView setPagerChangeListener(OnPagerChangeListener onPagerChangeListener);

    /**
     * 考虑到用户不断下滑刷新列表，这里面需要提供API供列表获取
     *
     * @return
     */
    List<MiniVideoBean> getVideoList();

    /**
     * 是否可以滚动
     *
     * @param isEnable
     * @return
     */
    IScrollVideoPagerView enableScroll(boolean isEnable);

    /**
     * 更新当前播放列表
     *
     * @param newData
     */
    void updateData(List<MiniVideoBean> newData);

    /**
     * 设置当前播放列表
     *
     * @param newData
     */
    void setData(List<MiniVideoBean> newData);

    /**
     * 返回当前页数
     *
     * @return
     */
    int getCurrentPage();


    /**
     * 生命周期回调--用于继续播放
     */
    void onResume();

    /**
     * 同上，多一个进度设置
     *
     * @param progress
     */
    void onResume(int progress);

    /**
     * 生命周期回调--用于暂停播放
     */
    void onPause();

    /**
     * 生命周期回调--销毁
     */
    void onDestroy();


    /**
     * 页面滚动回调
     */
    interface OnPagerChangeListener {

        /**
         * 列表切换变化
         *
         * @param position
         * @param isInit
         */
        void onPageChange(int position, boolean isInit);
    }
}
