package com.laka.live.account.replay;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.bean.Video;
import com.laka.live.ui.room.SeeReplayActivity;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwl on 2016/7/1.
 * Email-1501448275@qq.com
 */
public class ReplayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity mActivity;
    private List<VideoParams> mVideosList = new ArrayList<>();

    public ReplayAdapter(Activity activity, List<VideoParams> videoList) {
        mActivity = activity;
        mVideosList = videoList;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mVideosList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        setItemData((ViewHolder) holder, mVideosList.get(position));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ReplayItemView itemView = new ReplayItemView(parent.getContext());
        return new ViewHolder(itemView);
    }

    private void setItemData(ViewHolder viewHolder, VideoParams video) {
        viewHolder.mItemView.setData(video);
        viewHolder.mItemView.setOnReplayItemClickListener(new ReplayItemView.OnReplayItemClickListener() {
            @Override
            public void onItemClick(Video video) {
                if (video.getState() == Video.VIDEO_STATE_TRANS_FINISH) {
                    SeeReplayActivity.startActivity(mActivity,video.getId()+"", video.getUrl(),
                            AccountInfoManager.getInstance().getCurrentAccountUserIdStr(),
                            video.getViews(), video.getRecvCoins(), video.getVid(),1);
                } else {
                    ToastHelper.showToast(ResourceHelper.getString(R.string.replay_trans_coding_tips));
                }
            }
        });
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ReplayItemView mItemView;

        public ViewHolder(View itemView) {
            super(itemView);
            mItemView = (ReplayItemView) itemView;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mItemView.setLayoutParams(layoutParams);
        }
    }
}
