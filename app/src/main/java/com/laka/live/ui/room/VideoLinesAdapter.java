package com.laka.live.ui.room;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.bean.VideoLine;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwl on 2016/7/1.
 * Email-1501448275@qq.com
 */
public class VideoLinesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "VideoLinesAdapter";
    private Activity mActivity;
    private List<VideoLine> mVideosList = new ArrayList<>();

    public VideoLinesAdapter(Activity activity, List<VideoLine> videoList) {
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_lines, parent,false);
        return new ViewHolder(itemView);
    }

    private void setItemData(final ViewHolder viewHolder, final VideoLine video) {
        Log.d(TAG," name="+video.getName());

        viewHolder.tvName.setText(video.getName().replace(" ",""));
        if(video.isSelected){
            viewHolder.tvName.setTextColor(ResourceHelper.getColor(R.color.colorFFC40E));
        }else{
            viewHolder.tvName.setTextColor(ResourceHelper.getColor(R.color.white));
        }

        viewHolder.mItemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN ||event.getAction() == MotionEvent.ACTION_MOVE) {
                    viewHolder.tvName.setTextColor(ResourceHelper.getColor(R.color.color999999));
                }else {
                    viewHolder.tvName.setTextColor(ResourceHelper.getColor(R.color.white));
                }
                return false;
            }
        });

        viewHolder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (VideoLine item : mVideosList) {
                    item.isSelected = false;
                }
                video.isSelected = true;
                notifyDataSetChanged();
                EventBusManager.postEvent(video, SubcriberTag.CHOOSE_VIDEO_LINE);
            }
        });
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        public View mItemView;
        public TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
//            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            itemView.setLayoutParams(lp);
            mItemView = itemView;
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
