package com.laka.live.ui.topic;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.Topic;
import com.laka.live.ui.activity.TopicRoomListActivity;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.util.Common;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;

import java.util.HashMap;

/**
 * Created by zwl on 2016/6/29.
 * Email-1501448275@qq.com
 */
public class TopicListAdapter extends BaseAdapter<Topic, TopicListAdapter.ViewHolder> {
    private static final String TAG = "TopicListAdapter";
    private Activity mActivity;

    public TopicListAdapter(Activity activity) {
        mActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_topic_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Topic topic = getItem(position);
        holder.update(this, position, topic);
    }

    public class ViewHolder extends BaseAdapter.ViewHolder<Topic> {

        private LinearLayout rootLayout;
        private TextView topicName;
        private TextView liveCount;

        public ViewHolder(View itemView) {
            super(itemView);
            rootLayout = (LinearLayout) itemView.findViewById(R.id.topic_root_layout);
            topicName = (TextView) itemView.findViewById(R.id.topic_name);
            liveCount = (TextView) itemView.findViewById(R.id.replay_video_count);
        }

        public void update(final BaseAdapter adapter, final int position, final Topic topic) {
            topicName.setText(topic.getFormatName(itemView.getContext()));
            liveCount.setText(String.valueOf(topic.getLive_count() + ResourceHelper.getString(R.string.live_replay_count_tips)));
            rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TopicRoomListActivity.startActivity(mActivity, topic.getId(),
                            topic.getFormatName(mActivity),
                            AnalyticsReport.HOME_TOPICS_VIEW_ID);
                    HashMap<String, String> params = new HashMap<>();
                    params.put(Common.Topic, topic.getName());
                    params.put(Common.ID, String.valueOf(topic.getId()));
                    AnalyticsReport.onEvent(mActivity, AnalyticsReport.HOME_TOPICS_TOPIC_DETAIL);
                    AnalyticsReport.onEvent(mActivity, AnalyticsReport.HOME_FOUND_MORE_TOPIC_ACTIVITY_ITEM_CLICK_ID,params);
                }
            });
        }
    }
}
