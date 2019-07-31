package com.laka.live.video.presenter;

import com.laka.live.network.GsonHttpConnection;
import com.laka.live.video.contract.MiniVideoListContract;
import com.laka.live.video.model.http.MiniVideoListModel;
import com.laka.live.video.model.http.bean.VideoListResponseBean;

/**
 * @Author:Rayman
 * @Date:2018/8/2
 * @Description:小视频列表Presenter层
 */

public class MiniVideoListPresenter implements MiniVideoListContract.IVideoListPresenter {

    private MiniVideoListContract.IVideoListView mView;
    private MiniVideoListContract.IVideoListModel mModel;

    public MiniVideoListPresenter(MiniVideoListContract.IVideoListView mView) {
        this.mView = mView;
        mModel = new MiniVideoListModel();
    }

    @Override
    public void getVideoList(String type, int page, int limit) {
        mModel.getVideoList(type, page + "", limit + "",
                new GsonHttpConnection.OnResultListener<VideoListResponseBean>() {
                    @Override
                    public void onSuccess(VideoListResponseBean responseBean) {
                        mView.showVideoList(responseBean);
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg, String command) {
                        mView.showVideoList(null);
                    }
                });
    }
}
