package com.laka.live.video.contract;

import com.laka.live.msg.Msg;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsBaseBean;
import com.laka.live.video.model.bean.MiniVideoBean;
import com.laka.live.video.model.bean.VideoCommentBean;
import com.laka.live.video.model.http.bean.VideoListResponseBean;
import com.laka.live.video.model.http.bean.VideoRecommendGoodListResponseBean;

import java.util.List;

/**
 * @Author:Rayman
 * @Date:2018/8/9
 * @Description:小视频详情Contract契约类
 */

public interface MiniVideoInfoContract {

    interface IMiniVideoInfoView {

        void showVideoInfo(MiniVideoBean infoBean);

        void updateFollowStatus(boolean isFollow, String msg);

        void updateLikeStatus(boolean isLike, String msg);

        /**
         * 最后还是搬迁到VideoRecommendView上面
         *
         * @param goodsList
         */
        @Deprecated
        void updateRecommendGoods(List<ShoppingGoodsBaseBean> goodsList);

        /**
         * 最后还是搬迁到VideoCommentView上面
         *
         * @param commentList
         */
        @Deprecated
        void updateCommentList(List<VideoCommentBean> commentList);
    }

    interface IMiniVideoInfoModel {

        /**
         * 获取小视频详情
         *
         * @param videoId
         */
        void getMiniVideoInfo(String videoId);

        /**
         * 关注作者
         *
         * @param authorId
         * @param isFollow
         */
        void followVideoAuthor(int authorId, boolean isFollow, GsonHttpConnection.OnResultListener<Boolean> callBack);

        /**
         * 视频点赞
         *
         * @param videoId
         * @param isLike
         */
        void likeVideo(String videoId, boolean isLike, GsonHttpConnection.OnResultListener<Msg> callBack);

        /**
         * 获取评论列表
         *
         * @param videoId
         * @param page
         * @param limit
         * @param callBack
         */
        @Deprecated
        void getCommentList(String videoId, String page, String limit,
                            GsonHttpConnection.OnResultListener<VideoListResponseBean> callBack);

        /**
         * 获取评论的回复列表
         *
         * @param commentId
         * @param page
         * @param limit
         * @param callBack
         */
        @Deprecated
        void getCommentReplyList(String commentId, String page, String limit,
                                 GsonHttpConnection.OnResultListener<VideoListResponseBean> callBack);

        /**
         * 获取小视频推荐商品列表
         *
         * @param videoId
         * @param page
         * @param limit
         * @param callBack
         */
        @Deprecated
        void getRecommendGoodsList(String videoId, String page, String limit,
                                   GsonHttpConnection.OnResultListener<VideoRecommendGoodListResponseBean> callBack);
    }

    interface IMiniVideoInfoPresenter {

        // 小视频信息
        void getMiniVideoInfo(int videoId);

        // 关注
        void followVideoAuthor(int authorId, boolean isFollow);

        // 喜欢
        void likeVideo(int videoId, boolean isLike);

        // 获取评论列表
        @Deprecated
        void getCommentList(int videoId, int page, int limit);

        // 获取评论回复列表
        @Deprecated
        void getCommentReplyList(int commentId, int page, int limit);

        // 推荐商品
        @Deprecated
        void getRecommendGoodsList(int videoId, int page, int limit);
    }

}
