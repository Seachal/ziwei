package com.laka.live.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.Course;
import com.laka.live.bean.Topic;
import com.laka.live.ui.activity.TopicRoomListActivity;
import com.laka.live.ui.activity.UserInfoActivity;
import com.laka.live.ui.course.VListView;
import com.laka.live.ui.text.style.CustomClickableSpan;
import com.laka.live.ui.widget.MarkSimpleDraweeView;
import com.laka.live.util.Common;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.TimeUtil;
import com.laka.live.util.Util;
import com.laka.live.util.Utils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by luwies on 16/3/31.
 */
public class HomeFollowAdapter extends BaseAdapter<Course, BaseAdapter.ViewHolder> {


    // 布局类型(直播、回放、视频)
    public final static int TYPE_LIVE = 0;
    public final static int TYPE_REPLAY = 1;
    public final static int TYPE_VIDEO = 2;
    public final static int TYPE_DIVIDE = 3;

    private Context context;
    private static int mLiveCount = 0;

    public HomeFollowAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void addAll(List data) {
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Course course = getItem(position);
        return course.itemType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {

            case TYPE_DIVIDE:
                return new DividerVH(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_title_divide, parent, false));
            case TYPE_LIVE:
                return new LiveViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.follow_live_layout, parent, false));
            case TYPE_VIDEO:
                return new VideoViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.follow_replay_layout, parent, false));
            case TYPE_REPLAY:
                return new ReplayViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.follow_replay_layout, parent, false));

        }
        return null;
    }

    private class LiveViewHolder extends BaseAdapter.ViewHolder<Course> {

        RoomViewHolder viewHolder;

        public LiveViewHolder(View itemView) {
            super(itemView);
            viewHolder = new RoomViewHolder(itemView.findViewById(R.id.content), true);
            viewHolder.showDivider();
        }

        @Override
        public void update(BaseAdapter adapter, int position, Course course) {
            viewHolder.update(adapter, position, course);
            viewHolder.hideDivider();
        }
    }

    private class ReplayViewHolder extends BaseAdapter.ViewHolder<Course> {

        ItemHolder viewHolder;

        public ReplayViewHolder(View itemView) {
            super(itemView);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            itemView.setLayoutParams(params);
            View content = itemView.findViewById(R.id.content);
            viewHolder = new ItemHolder(content);
        }

        @Override
        public void update(BaseAdapter adapter, int position, Course course) {
            viewHolder.update(adapter, position, course);
        }

    }

    private class DividerVH extends BaseAdapter.ViewHolder<Course> {

        TextView more;
        TextView headText;

        public DividerVH(View itemView) {
            super(itemView);
            more = (TextView) itemView.findViewById(R.id.more);
            headText = (TextView) itemView.findViewById(R.id.head);
            ++mLiveCount;
        }

        @Override
        public void update(BaseAdapter adapter, int position, Course course) {

            // 如果当前为分割线
            final Course finalCourse = getItem(position + 1);
            switch (finalCourse.itemType) {

                case TYPE_LIVE:
                    headText.setText(R.string.living);
                    break;
                case TYPE_VIDEO:
                    headText.setText(R.string.newest_video);
                    break;
                case TYPE_REPLAY:
                    headText.setText(R.string.watching_replay);
                    break;

            }

            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (finalCourse.itemType) {

                        case TYPE_LIVE:
                            VListView.startActivity(context, TYPE_LIVE);
                            break;
                        case TYPE_VIDEO:
                            VListView.startActivity(context, TYPE_VIDEO);
                            break;
                        case TYPE_REPLAY:
                            VListView.startActivity(context, TYPE_REPLAY);
                            break;

                    }
                }
            });


        }

    }

    private class VideoViewHolder extends BaseAdapter.ViewHolder<Course> {
        ItemHolder viewHolder;

        public VideoViewHolder(View itemView) {
            super(itemView);

            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            itemView.setLayoutParams(params);

            View content = itemView.findViewById(R.id.content);
            viewHolder = new ItemHolder(content);

        }

        @Override
        public void update(BaseAdapter adapter, int position, Course course) {
            viewHolder.update(adapter, position, course);
        }
    }

    private class ItemHolder extends BaseAdapter.ViewHolder<Course> {

        private SimpleDraweeView cover;

        private TextView name;

        private TextView description;

        private TextView views;

        private int divider;

        private int mHalfDivider;
        private SimpleDraweeView face;
        private View mItemView;
        private View info;

        public ItemHolder(View itemView) {
            super(itemView);

            mItemView = itemView;
            info = itemView.findViewById(R.id.info);
            face = (SimpleDraweeView) itemView.findViewById(R.id.face);
            cover = (SimpleDraweeView) itemView.findViewById(R.id.cover);
            name = (TextView) itemView.findViewById(R.id.name);
            description = (TextView) itemView.findViewById(R.id.description);
            views = (TextView) itemView.findViewById(R.id.views);
            Context context = itemView.getContext();
            divider = Utils.dip2px(context, 2f);
            mHalfDivider = divider / 2;

            int height = (Util.getScreenWidth(context)) / 2;
            int width = height;

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) cover.getLayoutParams();
            params.width = width;
            params.height = height;
            cover.setLayoutParams(params);

//            params = (ViewGroup.MarginLayoutParams) itemView.getLayoutParams();
//            params.width = width;
//            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            itemView.setLayoutParams(params);
        }

        @Override
        public void update(BaseAdapter adapter, int position, final Course course) {

            // 显示已购买状态
            if (course.hasBuy()) {
                views.setText("已购买");
                views.setVisibility(View.VISIBLE);
            } else {
                views.setVisibility(View.GONE);
            }
            // 设置封面
            ImageUtil.loadImage(cover, course.getCover_url());
            // 设置头像
            ImageUtil.loadImage(face, course.getUser().avatar);
            // 设置姓名
            name.setText(course.user.nickname);
            // 设置标题
            description.setText(course.getTitle());
            // 个人资料
            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserInfoActivity.startActivity(context, String.valueOf(course.user_id));
                }
            });

        }

    }

    private class RoomViewHolder extends BaseAdapter.ViewHolder<Course> {

        private MarkSimpleDraweeView face;

        private TextView name;

        private TextView likes;

        private SimpleDraweeView screenShot;

        private TextView tipics;

        private TextView title;

        private View mDivider;

        private TextView liveTag;

        private TextView timeTv;

        private ImageView typeIcon;

        private TextView textStatus;

        private View userInfo;

        private boolean isEnableTagClick;

        public RoomViewHolder(View itemView, boolean isEnableTagClick) {
            super(itemView);

            this.isEnableTagClick = isEnableTagClick;

            face = (MarkSimpleDraweeView) itemView.findViewById(R.id.face);
            name = (TextView) itemView.findViewById(R.id.name);
            likes = (TextView) itemView.findViewById(R.id.likes_count);
            screenShot = (SimpleDraweeView) itemView.findViewById(R.id.screenshot);

            likes.setTypeface(Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/AFFOGATO-BOLD.OTF"));

            tipics = (TextView) itemView.findViewById(R.id.topics);

            title = (TextView) itemView.findViewById(R.id.title);

            liveTag = (TextView) itemView.findViewById(R.id.live_tag);

            timeTv = (TextView) itemView.findViewById(R.id.livingTime);
            textStatus = (TextView) itemView.findViewById(R.id.livingStatus);
            typeIcon = (ImageView) itemView.findViewById(R.id.typeIcon);
            userInfo = itemView.findViewById(R.id.userInfo);
            mDivider = itemView.findViewById(R.id.divider);

        }

        @Override
        public void update(BaseAdapter adapter, int position, final Course course) {

            // 设置主播头像
            ImageUtil.loadImage(face, course.user.avatar);
            // 设置主播姓名
            name.setText(course.user.nickname);
            // 设置购买标识
            if (course.hasBuy()) {
                likes.setText("已购买");
                likes.setVisibility(View.VISIBLE);
            } else {
                likes.setVisibility(View.GONE);
            }
            // 显示封面图
            ImageUtil.loadImage(screenShot, course.getCover_url());
            // 显示话题
            updateTopics(tipics, course.topics, isEnableTagClick);
            // 设置标题
            String titleStr = course.getTitle();
            if (TextUtils.isEmpty(titleStr)) {
                title.setVisibility(View.GONE);
            } else {
                title.setVisibility(View.VISIBLE);
                title.setText(course.getTitle());
            }

            setLocation(course);
            setTime(course);

            textStatus.setText(course.getStatus_text());
            showDivider();

            userInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserInfoActivity.startActivity(context, String.valueOf(course.user_id));
                }
            });
        }

        public void showDivider() {
            mDivider.setVisibility(View.VISIBLE);
        }

        public void hideDivider() {
            mDivider.setVisibility(View.GONE);
        }

        private void setLocation(Course course) {

            liveTag.setText(course.buyer_count + "人已购买");
        }

        private void setTime(Course room) {

            String timeStr = "";


            //  课程状态，-1=未购买，1=已取消，10=未开播，20=准备开播，30=调整直播时间中，
            // 40=直播中，50=生成回放中，60=已生成回放，70=课程视频已可播放, 100=播放视频(课程为视频类型)
            if (room.isLive()) {

                switch (room.getStatus()) {

                    case Course.CREATINGPLAYBACK:// 生成回放中
                        textStatus.setTextColor(getColor(R.color.colorD6D8DB));
                        timeStr = "时长 " + TimeUtil.getTimeWithStr(Long.parseLong(room.getDuration()));
                        typeIcon.setImageDrawable(ResourceHelper.getDrawable(R.mipmap.icon_living_saving));
                        break;
                    case Course.CREATEDPLAYBACK: // 已生成回放
                    case Course.PLAYVIDEO: // 课程视频已可播放
                        textStatus.setTextColor(getColor(R.color.color3A92FE));
                        timeStr = "时长 " + TimeUtil.getTimeWithStr(Long.parseLong(room.getDuration()));
                        typeIcon.setImageDrawable(ResourceHelper.getDrawable(R.mipmap.icon_living_replay));
                        break;
                    case Course.LIVING:// 直播中
                        textStatus.setTextColor(getColor(R.color.color3BC36B));
                        typeIcon.setImageDrawable(ResourceHelper.getDrawable(R.mipmap.icon_living));
                        timeStr = "已进行 " + TimeUtil.getTimeWithStr(TimeUtil.getDiffTime(room.start_time));
                        break;
                    case Course.CHANGETIME: // 调整直播时间
                        timeStr = "开播时间待定";
                        textStatus.setTextColor(getColor(R.color.colorF76720));
                        typeIcon.setImageDrawable(ResourceHelper.getDrawable(R.mipmap.icon_living_not_start));
                        break;
                    case Course.NOTSTART: // 未开播
                        timeStr = TimeUtil.getLiveTime2(room.start_time);
                        textStatus.setTextColor(getColor(R.color.colorF76720));
                        typeIcon.setImageDrawable(ResourceHelper.getDrawable(R.mipmap.icon_living_not_start));
                        break;
                    case Course.ALREADY: // 准备中
                        timeStr = TimeUtil.getLiveTime2(room.start_time);
                        textStatus.setTextColor(getColor(R.color.colorF76720));
                        typeIcon.setImageDrawable(ResourceHelper.getDrawable(R.mipmap.icon_living_prepare));
                        break;
                    default:
                        timeStr = TimeUtil.getLiveTime2(room.start_time);
                        textStatus.setTextColor(getColor(R.color.color3BC36B));
                        typeIcon.setImageDrawable(ResourceHelper.getDrawable(R.mipmap.icon_living_not_start));
                        break;
                }

            } else {
                timeStr = "时长 " + TimeUtil.getTimeWithStr(Integer.parseInt(room.getDuration()));
                typeIcon.setImageDrawable(ResourceHelper.getDrawable(R.mipmap.icon_video));
            }

            timeTv.setText(timeStr);
        }

        private int getColor(int color) {
            return ResourceHelper.getColor(color);
        }

    }

    public void updateTopics(final TextView tipics, List<Topic> topics, final boolean isEnableTagClick) {

        if (topics == null || topics.isEmpty()) {
            tipics.setVisibility(View.GONE);
        } else {

            tipics.setVisibility(View.VISIBLE);
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
                            params.put(Common.ID, String.valueOf(topic.getId()));
                            AnalyticsReport.onEvent(tipics.getContext(), AnalyticsReport.HOME_LIVE_TOPIC_ITEM_CLICK_EVENT_ID,params);
                            TopicRoomListActivity.startActivity(context, topic.getId(), topicName,
                                    AnalyticsReport.HOME_LIVE_TOPIC_VIEW_ID);
                        }

                    }
                };
                span.setUnderlineText(false);
                span.setLinkColor(yellowCollor);
                spannableStringBuilder.setSpan(span, 0, topicName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }

            tipics.setText(spannableStringBuilder);
        }
    }

}
