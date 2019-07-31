package com.laka.live.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.Topic;
import com.laka.live.ui.activity.TopicRoomListActivity;
import com.laka.live.ui.topic.MoreTopicActivity;
import com.laka.live.util.Common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by luwies on 16/6/29.
 */
public class TopicAdapter extends BaseAdapter<Topic, TopicAdapter.ViewHolder> {

    private Context mContext;

    public TopicAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_topic_grid_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        int count = super.getItemCount();
        if (count <= 0) {
            return 0;
        }
        return count + 1;
    }

    @Override
    public Topic getItem(int position) {
        if (position == getItemCount() - 1) {
            return null;
        }
        return super.getItem(position);
    }

    @Override
    protected void onItemClick(int position) {
        if (position == getItemCount() - 1) {
            MoreTopicActivity.startActivity((Activity) mContext);
            AnalyticsReport.onEvent(mContext,
                    AnalyticsReport.HOME_TOPICS_MORE_TOPIC_CLICK_EVENT_ID);
        } else {
            Topic topic = getItem(position);
            TopicRoomListActivity.startActivity(mContext,
                    topic.getId(), topic.getFormatName(mContext), AnalyticsReport.HOME_VIDEO_VIEW_ID);
            Map<String, String> params = new HashMap<>();
            params.put(Common.ID, topic.getId());
            AnalyticsReport.onEvent(mContext,
                    AnalyticsReport.HOME_FOUND_RMD_TOPIC_ITEM_CLICK_EVENT_ID, params);
        }
    }

    public static class ViewHolder extends BaseAdapter.ViewHolder<Topic> {
        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void update(BaseAdapter adapter, int position, Topic topic) {
            TextView textView = (TextView) itemView;
            Context context = itemView.getContext();

            if (position < adapter.getItemCount() - 1) {
                textView.setText(topic.getFormatName(context));
            } else  {
                textView.setText(R.string.more_topic);
            }
        }
    }

}
