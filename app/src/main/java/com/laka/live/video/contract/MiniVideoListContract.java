package com.laka.live.video.contract;

import com.laka.live.network.GsonHttpConnection;
import com.laka.live.video.model.http.bean.VideoListResponseBean;

/**
 * @Author:Rayman
 * @Date:2018/8/2
 * @Description: 小视频列表Contract类
 */

public interface MiniVideoListContract {

    interface IVideoListView {

        /**
         * 显示列表
         *
         * @param responseBean
         */
        void showVideoList(VideoListResponseBean responseBean);
    }

    interface IVideoListModel {

        /**
         * 根据Type获取视频列表
         *
         * @param type
         * @param page
         * @param limit
         * @param listener
         */
        void getVideoList(String type, String page, String limit, GsonHttpConnection.OnResultListener<VideoListResponseBean> listener);

    }

    interface IVideoListPresenter {

        /**
         * 根据Type获取视频列表
         *
         * @param type
         * @param page
         * @param limit
         */
        void getVideoList(String type, int page, int limit);
    }

}
