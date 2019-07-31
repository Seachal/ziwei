package com.laka.live.ui.homepage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.HotTopic;
import com.laka.live.ui.activity.TopicRoomListActivity;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.util.ImageUtil;

/**
 * @Author:Rayman
 * @Date:2018/8/8
 * @Description:首页热门话题
 */

public class HomeTopicAdapter extends BaseAdapter<HotTopic, BaseAdapter.ViewHolder> {

    private Context mContext;

    public HomeTopicAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TopicItemViewHolder(LayoutInflater.
                from(mContext).inflate(R.layout.item_home_topic_item, parent, false),
                mContext);
    }

    @Override
    protected void onItemClick(int position) {
        super.onItemClick(position);
        HotTopic hotTopic = getItem(position);
        if (hotTopic == null) {
            return;
        }

        TopicRoomListActivity.startActivity(mContext, hotTopic.getId(), hotTopic.getName(), AnalyticsReport.HOME_VIDEO_VIEW_ID);
    }

    private class TopicItemViewHolder extends BaseAdapter.ViewHolder<HotTopic> {

        private TextView mTitleTv;
        private SimpleDraweeView mThumbSdv;

        TopicItemViewHolder(View itemView, Context context) {
            super(itemView);
            mTitleTv = itemView.findViewById(R.id.tv_topic_title);
            mThumbSdv = itemView.findViewById(R.id.iv_topic_cover);

            RoundingParams roundingParams = new RoundingParams();
            roundingParams.setCornersRadii(10, 10, 10, 10);
            GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(context.getResources())
                    .setRoundingParams(roundingParams)
                    .build();

            mThumbSdv.setHierarchy(hierarchy);
        }

        @Override
        public void update(BaseAdapter adapter, int position, HotTopic hotTopic) {
            if (hotTopic == null) {
                return;
            }

            ImageUtil.loadImage(mThumbSdv, hotTopic.getCoverUrl());
            mTitleTv.setText(hotTopic.getName());
        }
    }
}
