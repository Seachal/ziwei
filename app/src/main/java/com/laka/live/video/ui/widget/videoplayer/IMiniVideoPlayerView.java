package com.laka.live.video.ui.widget.videoplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

import com.laka.live.video.model.bean.MiniVideoBean;

/**
 * @Author:Rayman
 * @Date:2018/8/13
 * @Description:小视频播放器,实际上和项目SeeReplayActivity配置一样
 */

public interface IMiniVideoPlayerView {

    void initProperties(Context context, AttributeSet attributeSet);

    void initView();

    void updateData(MiniVideoBean videoInfoBean);

    void setVideoCover(Bitmap bitmap);

    void setVideoCover(String url);

    void startVideoPlay(String coverUrl, String videoUrl);

    void startVideoPlay(String videoUrl);

    void pauseVideoPlay();

    void resumeVideoPlay();

    void resumeVideoPlay(int progress);

    /**
     * 释放当前播放器
     */
    void release();

    /**
     * 释放当前播放器及其内存
     */
    void totallyRelease();

    int getCurrentProgress();

}
