package com.laka.live.video.presenter;

import com.laka.live.msg.Msg;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.video.contract.MiniVideoInfoContract;
import com.laka.live.video.model.http.MiniVideoInfoModel;
import com.laka.live.video.model.http.bean.VideoListResponseBean;
import com.laka.live.video.model.http.bean.VideoRecommendGoodListResponseBean;

/**
 * @Author:Rayman
 * @Date:2018/8/13
 * @Description:小视频详情P层
 */

public class MiniVideoInfoPresenter implements MiniVideoInfoContract.IMiniVideoInfoPresenter {

    private MiniVideoInfoContract.IMiniVideoInfoView mView;
    private MiniVideoInfoContract.IMiniVideoInfoModel mModel;

    public MiniVideoInfoPresenter(MiniVideoInfoContract.IMiniVideoInfoView mView) {
        this.mView = mView;
        mModel = new MiniVideoInfoModel();
    }

    @Override
    public void getMiniVideoInfo(int videoId) {
        mModel.getMiniVideoInfo(videoId + "");
    }

    @Override
    public void followVideoAuthor(int authorId, final boolean isFollow) {
        mModel.followVideoAuthor(authorId, isFollow, new GsonHttpConnection.OnResultListener<Boolean>() {
            @Override
            public void onSuccess(Boolean follow) {
                mView.updateFollowStatus(isFollow, "");
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                String msg = "";
                if (!isFollow) {
                    msg = "取消关注失败";
                } else {
                    msg = "关注失败";
                }
                mView.updateFollowStatus(isFollow, msg);
            }
        });
    }

    @Override
    public void likeVideo(int videoId, final boolean isLike) {
        mModel.likeVideo(videoId + "", isLike, new GsonHttpConnection.OnResultListener<Msg>() {
            @Override
            public void onSuccess(Msg response) {
                mView.updateLikeStatus(isLike, "");
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                String msg = "";
                if (!isLike) {
                    msg = "取消点赞失败";
                } else {
                    msg = "点赞失败";
                }
                mView.updateLikeStatus(isLike, msg);
            }
        });
    }

    @Override
    public void getCommentList(int videoId, int page, int limit) {
        mModel.getCommentList(videoId + "", page + "", limit + "",
                new GsonHttpConnection.OnResultListener<VideoListResponseBean>() {
                    @Override
                    public void onSuccess(VideoListResponseBean responseBean) {

                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg, String command) {

                    }
                });
    }

    @Override
    public void getCommentReplyList(int commentId, int page, int limit) {
        mModel.getCommentReplyList(commentId + "", page + "", limit + "",
                new GsonHttpConnection.OnResultListener<VideoListResponseBean>() {
                    @Override
                    public void onSuccess(VideoListResponseBean responseBean) {

                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg, String command) {

                    }
                });

    }

    @Override
    public void getRecommendGoodsList(int videoId, int page, int limit) {
        mModel.getRecommendGoodsList(videoId + "", page + "", limit + "",
                new GsonHttpConnection.OnResultListener<VideoRecommendGoodListResponseBean>() {
                    @Override
                    public void onSuccess(VideoRecommendGoodListResponseBean videoRecommendGoosListResponseBean) {

                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg, String command) {

                    }
                });
    }
}
