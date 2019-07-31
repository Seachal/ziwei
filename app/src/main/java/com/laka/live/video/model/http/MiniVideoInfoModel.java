package com.laka.live.video.model.http;

import com.laka.live.account.follow.FollowRequestHelper;
import com.laka.live.msg.Msg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.util.Common;
import com.laka.live.video.constant.VideoApiConstant;
import com.laka.live.video.contract.MiniVideoInfoContract;
import com.laka.live.video.model.http.bean.VideoListResponseBean;
import com.laka.live.video.model.http.bean.VideoRecommendGoodListResponseBean;

import java.util.HashMap;

/**
 * @Author:Rayman
 * @Date:2018/8/13
 * @Description:小视频详情Model类
 */

public class MiniVideoInfoModel implements MiniVideoInfoContract.IMiniVideoInfoModel {

    @Override
    public void getMiniVideoInfo(String videoId) {

    }

    @Override
    public void followVideoAuthor(int authorId, boolean isFollow, final GsonHttpConnection.OnResultListener<Boolean> callBack) {
        FollowRequestHelper mFollowRequestHelper = new FollowRequestHelper();
        mFollowRequestHelper.setBlockFollowToast(true);
        mFollowRequestHelper.setAutoToastFailTips(false);
        mFollowRequestHelper.startRequest(authorId, !isFollow, new FollowRequestHelper.FollowRequestCallback() {
            @Override
            public void requestSuccess(boolean isCancelFollow) {
                callBack.onSuccess(isCancelFollow);
            }

            @Override
            public void requestFailed(boolean isCancelFollow, int errorCode, String errorMsg) {
                callBack.onSuccess(false);
            }
        });
    }

    @Override
    public void likeVideo(String videoId, boolean isLike, GsonHttpConnection.OnResultListener<Msg> callBack) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.MINI_VIDEO_ID, videoId);
        DataProvider.addToken(params);
        GsonHttpConnection.getInstance().post(this,
                VideoApiConstant.LIKE_VIDEO_API, params, Msg.class, callBack);
    }

    @Override
    public void getCommentList(String videoId, String page, String limit, GsonHttpConnection.OnResultListener<VideoListResponseBean> callBack) {

    }

    @Override
    public void getCommentReplyList(String commentId, String page, String limit, GsonHttpConnection.OnResultListener<VideoListResponseBean> callBack) {

    }

    @Override
    public void getRecommendGoodsList(String videoId, String page, String limit, GsonHttpConnection.OnResultListener<VideoRecommendGoodListResponseBean> callBack) {

    }
}
