package com.laka.live.video.model.event;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:Rayman
 * @Date:2018/8/27
 * @Description:Container发送生命周期的Event对象（可以作为targetObj判断使用） 也作用于MiniVideoPlayActivity和UserInfoActivity的通信
 */

public class VideoDecorViewEvent {

    private List<View> decorViews;

    public VideoDecorViewEvent(List<View> decorViews) {
        this.decorViews = decorViews;
    }

    public List<View> getDecorViews() {
        if (decorViews == null) {
            return new ArrayList<>();
        }
        return decorViews;
    }

    public void setDecorViews(List<View> decorViews) {
        this.decorViews = decorViews;
    }
}
