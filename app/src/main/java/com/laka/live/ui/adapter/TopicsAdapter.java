package com.laka.live.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.Course;
import com.laka.live.bean.Room;
import com.laka.live.bean.Topic;
import com.laka.live.ui.activity.TopicRoomListActivity;
import com.laka.live.ui.text.style.CustomClickableSpan;
import com.laka.live.ui.widget.DividerGridItemDecoration;
import com.laka.live.util.Common;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.TimeUtil;
import com.laka.live.util.Util;
import com.laka.live.util.Utils;
import com.laka.live.util.ViewUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lyf on 2017/4/13.
 */
public class TopicsAdapter extends BaseAdapter<Course, BaseAdapter.ViewHolder> {


    private List<Topic> topics;
    private boolean isEnableTagClick = true;
    // 第一种类型是header，第二种是item
    public final static int TYPE_HEAD = 1, TYPE_ITEM = 2;
    static final String FROM_FOUND = "from_found";

    private RecyclerView recyclerView;
    private int mTitlePosition = 0;
    private Context context;

    public void setTitlePosition(int titlePosition) {
        this.mTitlePosition = titlePosition;
    }

    public int getTitlePosition() {
        return mTitlePosition;
    }

    public TopicsAdapter(Context context) {
        this.context = context;
    }

    public void setIsEnableTagClick(boolean isEnableTagClick) {
        this.isEnableTagClick = isEnableTagClick;
    }

    // 判断话题是否为空
    public boolean isTopicsEmpty() {
        return topics == null || topics.isEmpty();
    }

    // 设置话题数据
    public void setTopics(List<Topic> topics) {
        this.topics = topics;
        Course course = new Course();
        course.localTopics = new ArrayList<>();
        course.localTopics.addAll(topics);
        mDatas.add(0, course);
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        super.clear();
        Course course = new Course();
        course.localTopics = new ArrayList<>();
        if (topics != null) {
            course.localTopics.addAll(topics);
            mDatas.add(0, course);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Course course = getItem(position);
        return (course.localTopics != null) ? TYPE_HEAD : TYPE_ITEM;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEAD) {
            RecyclerView recyclerView = new RecyclerView(parent.getContext());
            return new HeadHolder(recyclerView);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_found_topic_newest_room, parent, false);
            return new RoomViewHolder(view, isEnableTagClick);
        }
    }


    public class HeadHolder extends BaseAdapter.ViewHolder<Course> {

        private TopicAdapter topicAdapter;

        public HeadHolder(RecyclerView itemView) {
            super(itemView);

            Context context = itemView.getContext();
            itemView.setBackgroundResource(R.drawable.found_head_bg);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.bottomMargin = ResourceHelper.getDimen(R.dimen.found_fragment_big_vertical_margin);
            itemView.setLayoutParams(layoutParams);
            itemView.setPadding(0, 1, 0, 1);
            itemView.setLayoutManager(new GridLayoutManager(context, 2,
                    LinearLayoutManager.VERTICAL, false));
            DividerGridItemDecoration decoration = new DividerGridItemDecoration(context);
            decoration.setDivider(ContextCompat.getDrawable(itemView.getContext(),
                    R.drawable.topic_divider));
            int decorationMargin = ResourceHelper.getDimen(R.dimen.space_5);
            decoration.setHorizontalMargin(decorationMargin);
            decoration.setVerticalMargin(decorationMargin);
            itemView.addItemDecoration(decoration);
            topicAdapter = new TopicAdapter(context);
            itemView.setAdapter(topicAdapter);
        }

        @Override
        public void update(BaseAdapter adapter, int position, Course course) {

            if (course.localTopics != null) {
                topicAdapter.clear(false);
                topicAdapter.addAll(course.localTopics);
                topicAdapter.notifyDataSetChanged();
            }
        }

    }


    public class RoomViewHolder extends BaseAdapter.ViewHolder<Course> {

        private int divider;
        private int mHalfDivider;
        private View infoLayout;
        private TextView timeTv;
        private TextView views;
        private TextView statusTv;
        private TextView introduction;
        private ImageView typeIcon;
        private RelativeLayout title_layout;
        private SimpleDraweeView screenShot;

        public RoomViewHolder(View itemView, boolean isEnableTagClick) {
            super(itemView);

            Context context = itemView.getContext();
            views = ViewUtils.findById(itemView, R.id.views);
            timeTv = ViewUtils.findById(itemView, R.id.livingTime);
            typeIcon = ViewUtils.findById(itemView, R.id.typeIcon);
            statusTv = (TextView) itemView.findViewById(R.id.livingStatus);
            title_layout = ViewUtils.findById(itemView, R.id.title_layout);
            screenShot = (SimpleDraweeView) itemView.findViewById(R.id.screen_shot);
            infoLayout = itemView.findViewById(R.id.info_layout);
            introduction = ViewUtils.findById(itemView, R.id.introduction);
            divider = Utils.dip2px(context, 2f);
            mHalfDivider = divider / 2;
            int height  = (Util.getScreenWidth(context) - divider) / 2;
            int width = height - divider / 2;

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) screenShot.getLayoutParams();
            params.width = width;
            params.height = height;
            screenShot.setLayoutParams(params);

            params = (ViewGroup.MarginLayoutParams) itemView.getLayoutParams();
            params.width = width;
            params.height = width + divider;
            itemView.setLayoutParams(params);

        }

        @Override
        public void update(BaseAdapter adapter, int position, final Course course) {


            updateTopics(introduction, course.topics,course.getId(), isEnableTagClick);
            introduction.append(course.getTitle());
            statusTv.setText(course.getStatus_text());

            if (course.hasBuy()) {
                views.setText("已购买");
                views.setVisibility(View.VISIBLE);
            } else {
                views.setVisibility(View.GONE);
            }

            ImageUtil.loadImage(screenShot,course.getCover_url());


            String timeStr = "";
            if (course.isLive()) {
                switch (course.getStatus()) {
                    case Room.STATUS_LIVE_PLAYBACKING:
                        timeStr = "时长 " + TimeUtil.getTimeWithStr(Integer.parseInt(course.getDuration()));
                        typeIcon.setImageDrawable(ResourceHelper.getDrawable(R.mipmap.icon_living_saving));
                        break;
                    case Room.STATUS_LIVE_CAN_PLAY:
                    case Room.STATUS_LIVE_PLAYBACK:
                        timeStr = "时长 " + TimeUtil.getTimeWithStr(Integer.parseInt(course.getDuration()));
                        typeIcon.setImageDrawable(ResourceHelper.getDrawable(R.mipmap.icon_living_replay));
                        break;
                    case Room.STATUS_LIVE_ING:
                        typeIcon.setImageDrawable(ResourceHelper.getDrawable(R.mipmap.icon_living));
                        timeStr = "已进行 " + TimeUtil.getTimeWithStr(TimeUtil.getDiffTime(course.start_time));
                        break;
                    case Room.STATUS_LIVE_CHANGE:
                        timeStr = "开播时间待定";
                        typeIcon.setImageDrawable(ResourceHelper.getDrawable(R.mipmap.icon_living_not_start));
                        break;
                    case Room.STATUS_LIVE_PREPARE:
                        timeStr = TimeUtil.getLiveTime2(course.start_time);
                        typeIcon.setImageDrawable(ResourceHelper.getDrawable(R.mipmap.icon_living_prepare));
                        break;
                    default:
                        timeStr = TimeUtil.getLiveTime2(course.start_time);
                        typeIcon.setImageDrawable(ResourceHelper.getDrawable(R.mipmap.icon_living_not_start));
                        break;
                }

            } else {
                timeStr = "时长 " + TimeUtil.getTimeWithStr(Long.parseLong(course.getDuration()));
                typeIcon.setImageDrawable(ResourceHelper.getDrawable(R.mipmap.icon_video));
            }


            timeTv.setText(timeStr);
        }

        public void updateTopics(final TextView tipics, List<Topic> topics,final String courseId, final boolean isEnableTagClick) {

            if (topics == null || topics.isEmpty()) {
                tipics.setText("");
            } else {
                tipics.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //阻止点击传递
                    }
                });
                tipics.setMovementMethod(LinkMovementMethod.getInstance());

                int yellowCollor = ContextCompat.getColor(context, R.color.color62B6E8);
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

                final Topic topic = topics.get(0);

                if (topic != null) {
                    final String topicName = topic.getFormatName(context);
                    spannableStringBuilder.append(topicName);

                    CustomClickableSpan span = new CustomClickableSpan() {
                        @Override
                        public void onClick(View widget) {

                            if (isEnableTagClick) {
                                HashMap<String, String> params = new HashMap<>();
                                params.put(Common.TOPIC_ID, courseId);
                                AnalyticsReport.onEvent(tipics.getContext(),
                                        AnalyticsReport.HOME_LIVE_TOPIC_ITEM_CLICK_EVENT_ID,params);
                                TopicRoomListActivity.startActivity(context, topic.getId(), topicName,
                                        AnalyticsReport.HOME_TOPICS_TOPIC_CLICK_EVENT_ID);
                            }

                        }
                    };
                    span.setUnderlineText(false);
                    span.setLinkColor(yellowCollor);
                    spannableStringBuilder.setSpan(span, 0, topicName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }

                tipics.setText(spannableStringBuilder);
                tipics.append("  ");

            }
        }


    }

}
