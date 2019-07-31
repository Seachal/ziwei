package com.laka.live.video.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.laka.live.video.model.bean.MiniVideoBean;
import com.laka.live.video.ui.widget.videoplayer.MiniVideoPlayerView;

import java.util.List;

/**
 * @Author:Rayman
 * @Date:2018/8/14
 * @Description:垂直方向的RecyclerViewAdapter
 */

public class MiniVideoRecyclerAdapter extends RecyclerView.Adapter<MiniVideoRecyclerAdapter.VideoViewHolder> {

    private List<MiniVideoBean> mVideoList;

    public MiniVideoRecyclerAdapter(List<MiniVideoBean> videoList) {
        this.mVideoList = videoList;
    }


    @Override
    public MiniVideoRecyclerAdapter.VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 创建列表holder
        MiniVideoPlayerView playerView = new MiniVideoPlayerView(parent.getContext());
        playerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return new MiniVideoRecyclerAdapter.VideoViewHolder(playerView);
    }

    @Override
    public void onBindViewHolder(MiniVideoRecyclerAdapter.VideoViewHolder holder, int position) {
        //Logger.e("输出当前Holder：" + holder);
        holder.update(position, mVideoList.get(position));
    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }

    public void updateList(List<MiniVideoBean> playVideoList) {
        mVideoList = playVideoList;
        notifyDataSetChanged();
    }

    public MiniVideoBean getCurrentVideo(int position) {
        return mVideoList.get(position);
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {

        private MiniVideoPlayerView playerView;

        public VideoViewHolder(View itemView) {
            super(itemView);
        }

        public void update(int position, MiniVideoBean videoInfoBean) {
            playerView = (MiniVideoPlayerView) itemView;
            playerView.updateData(videoInfoBean);
//            if (videoInfoBean.getLocalCover() != null) {
//                playerView.setVideoCover(videoInfoBean.getLocalCover());
//            } else {
//                playerView.setVideoCover(videoInfoBean.getVideoCover());
//            }
//            playerView.startVideoPlay(videoInfoBean.getVideoUrl());
        }
    }
}
