package com.laka.live.video.ui.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.NumberUtils;
import com.laka.live.util.TimeUtil;
import com.laka.live.video.model.bean.MiniVideoBean;
import com.laka.live.video.ui.activity.MiniVideoContainerActivity;

import java.util.ArrayList;


/**
 * @Author:Rayman
 * @Date:2018/8/1
 * @Description:小视频列表Adapter
 */

public class VideoListAdapter extends BaseAdapter<MiniVideoBean, BaseAdapter.ViewHolder> {

    private static final int CLICK_DURATION = 1000;
    private long mLastTime;
    private Context mContext;

    public VideoListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.item_home_video, parent, false);
        return new VideoListViewHolder(contentView);
    }

    private class VideoListViewHolder extends BaseAdapter.ViewHolder<MiniVideoBean> {

        private SimpleDraweeView mIvCover;
        private TextView mTvTime;
        private TextView mTvTitle;
        private SimpleDraweeView mIvUserAvatar;
        private TextView mTvUserName;
        private ImageView mIvLike;
        private TextView mTvLikeCount;

        public VideoListViewHolder(View itemView) {
            super(itemView);
            mIvCover = itemView.findViewById(R.id.iv_video_cover);
            mTvTime = itemView.findViewById(R.id.tv_video_time);
            mTvTitle = itemView.findViewById(R.id.tv_video_title);
            mIvUserAvatar = itemView.findViewById(R.id.iv_video_user_avatar);
            mTvUserName = itemView.findViewById(R.id.tv_video_user_name);
            mIvLike = itemView.findViewById(R.id.iv_video_like);
            mTvLikeCount = itemView.findViewById(R.id.tv_video_like_count);
        }

        @Override
        public void update(BaseAdapter adapter, int position, final MiniVideoBean item) {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mIvCover.getLayoutParams();
            layoutParams.dimensionRatio = "h," + item.getVideoWidth() + ":" + item.getVideoHeight();
            mIvCover.setLayoutParams(layoutParams);
            ImageUtil.loadImage(mIvCover, item.getVideoCover());
            ImageUtil.loadImage(mIvUserAvatar, item.getUser().getAvatar());
            mTvTime.setText(TimeUtil.covertSecondToMS(item.getVideoDuration()));
            mTvTitle.setText(item.getVideoTitle());
            mTvUserName.setText(item.getUser().getNickname());
            if (item.isLike()) {
                mIvLike.setImageResource(R.drawable.icon_mini_video_like_check);
            } else {
                mIvLike.setImageResource(R.drawable.icon_mini_video_like);
            }
            mTvLikeCount.setText(NumberUtils.getVideoSummeryCount(item.getLikeCount()));
        }
    }

    @Override
    protected void onItemClick(int position) {
        super.onItemClick(position);
        long currentTimeMillis = System.currentTimeMillis();
        long time = currentTimeMillis - mLastTime;
        mLastTime = currentTimeMillis;
        if (time > CLICK_DURATION) {
            MiniVideoContainerActivity.startActivity(mContext, (ArrayList<MiniVideoBean>) getmDatas(), position);
        }
    }
}
