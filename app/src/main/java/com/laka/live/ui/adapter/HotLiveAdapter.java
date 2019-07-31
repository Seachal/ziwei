package com.laka.live.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.Banner;
import com.laka.live.bean.Course;
import com.laka.live.bean.ListUserInfo;
import com.laka.live.bean.Topic;
import com.laka.live.bean.User;
import com.laka.live.bean.UserInfo;
import com.laka.live.ui.activity.TopicRoomListActivity;
import com.laka.live.ui.activity.UserInfoActivity;
import com.laka.live.ui.course.detail.CourseDetailActivity;
import com.laka.live.ui.text.style.CustomClickableSpan;
import com.laka.live.ui.widget.BannerView;
import com.laka.live.ui.widget.MarkSimpleDraweeView;
import com.laka.live.util.Common;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.TimeUtil;
import com.laka.live.util.Utils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by luwies on 16/3/29.
 */
public class HotLiveAdapter extends BaseAdapter<Object, BaseAdapter.ViewHolder> {

    public static final String FROM_HOT = "from_hot";

    public static final String FROM_FOUND = "from_found";

    public static final String FROM_FRIEND = "from_friend";

    public final static int TYPE_HEAD = 1;

    public final static int TYPE_ITEM = 2;

    private List<Banner> mBanners;

    private ViewGroup mSwipeRefreshLayout;

    private boolean isEnableTagClick = true;
    private boolean isFromTopicDetail = false; // 是否从话题详情列表进来

    private Context context;
    public boolean isShowTopDivider = false;

    public HotLiveAdapter(Context context, ViewGroup swipeRefreshLayout) {
        this.context = context;
        this.mSwipeRefreshLayout = swipeRefreshLayout;
    }

    public HotLiveAdapter(Context context, ViewGroup swipeRefreshLayout, boolean isFromTopicDetail) {
        this.context = context;
        this.isFromTopicDetail = isFromTopicDetail;
        this.mSwipeRefreshLayout = swipeRefreshLayout;
    }

    public void setIsEnableTagClick(boolean isEnableTagClick) {
        this.isEnableTagClick = isEnableTagClick;
    }

    public void setBanners(List<Banner> banners) {
        mBanners = banners;
    }

    public boolean isBannersEmpty() {
        return mBanners == null || mBanners.isEmpty();
    }

    @Override
    public int getItemCount() {
        int bannerSize = 0;
        if (mBanners != null && !mBanners.isEmpty()) {
            bannerSize = 1;
        }
        return super.getItemCount() + bannerSize;
    }

    @Override
    public Object getItem(int position) {
        if (mBanners != null && !mBanners.isEmpty()) {
            if (position == 0) {
                return mBanners;
            } else {
                return super.getItem(position - 1);
            }
        }
        return super.getItem(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (mBanners != null && !mBanners.isEmpty()) {
            if (position == 0) {
                return TYPE_HEAD;
            } else {
                return TYPE_ITEM;
            }
        }
        return TYPE_ITEM;
    }


    @Override
    public BaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEAD:
                Context context = parent.getContext();
                BannerView bannerView = new BannerView(context);
                bannerView.setSwipeRefreshLayout(mSwipeRefreshLayout);

                RecyclerView.LayoutParams params =
                        new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                Utils.getScreenWidth(context) / 3);
                bannerView.setLayoutParams(params);
                return new BannerViewHolder(bannerView);
            case TYPE_ITEM:
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_room_layout, parent, false);
                return new RoomViewHolder(view, isEnableTagClick);
        }

        return null;
    }


    public class RoomViewHolder extends BaseAdapter.ViewHolder<Course> {

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
        private LinearLayout labelContainer;
        private TextView textStatus;
        private View userInfo;
        private View topDivider;
        private boolean isEnableTagClick;
        private View titleLayout;

        public RoomViewHolder(View itemView, boolean isEnableTagClick) {
            super(itemView);

            this.isEnableTagClick = isEnableTagClick;

            face = (MarkSimpleDraweeView) itemView.findViewById(R.id.face);
            name = (TextView) itemView.findViewById(R.id.name);
            likes = (TextView) itemView.findViewById(R.id.likes_count);
            topDivider = itemView.findViewById(R.id.topDivider);
            screenShot = (SimpleDraweeView) itemView.findViewById(R.id.screenshot);

            likes.setTypeface(Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/AFFOGATO-BOLD.OTF"));

            tipics = (TextView) itemView.findViewById(R.id.topics);

            title = (TextView) itemView.findViewById(R.id.title);

            liveTag = (TextView) itemView.findViewById(R.id.live_tag);

            timeTv = (TextView) itemView.findViewById(R.id.livingTime);
            textStatus = (TextView) itemView.findViewById(R.id.livingStatus);
            typeIcon = (ImageView) itemView.findViewById(R.id.typeIcon);
            mDivider = itemView.findViewById(R.id.divider);
            userInfo = itemView.findViewById(R.id.userInfo);
            titleLayout = itemView.findViewById(R.id.title_layout);
            labelContainer = (LinearLayout) itemView.findViewById(R.id.labelContainer);

        }

        @Override
        public void update(BaseAdapter adapter, int position, final Course course) {

            if (position == 1 && isShowTopDivider)
                topDivider.setVisibility(View.VISIBLE);
            else
                topDivider.setVisibility(View.GONE);

            ImageUtil.loadImage(face, course.getUser().avatar);
            name.setText(course.getUser().nickname);

            titleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(Common.ID, String.valueOf(course.getId()));
                    if (course.isLive()) {
                        AnalyticsReport.onEvent(context, AnalyticsReport.HOME_LIVE_NAME_CLICK_EVENT_ID, params);
                    } else {
                        AnalyticsReport.onEvent(context, AnalyticsReport.HOME_VIDEO_NAME_CLICK_EVENT_ID, params);
                    }

                    CourseDetailActivity.startActivity(context, String.valueOf(course.getId()));
                }
            });

            if (course.hasBuy()) {
                likes.setText("已购买");
                likes.setVisibility(View.VISIBLE);
            } else {
                likes.setVisibility(View.GONE);
            }

            ImageUtil.loadImage(screenShot, course.getCover_url());

            updateTopics(tipics, course, isEnableTagClick);

            String titleStr = course.getTitle();
            if (TextUtils.isEmpty(titleStr)) {
                title.setVisibility(View.GONE);
            } else {
                title.setVisibility(View.VISIBLE);
                title.setText(course.getTitle());
            }
            textStatus.setText(course.getStatus_text());
            setLocation(course);
            setTime(course);
            showDivider();

            final User user = course.getUser();
//            userInfo.setTag(user);
            userInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    User user = (User) v.getTag();
                    ListUserInfo userInfo = new UserInfo();
                    userInfo.setId(user.getId());
                    userInfo.setNickName(user.getNickname());
                    userInfo.setAvatar(user.getAvatar());
                    UserInfoActivity.startActivity((Activity) v.getContext(), userInfo);
                    HashMap<String, String> params = new HashMap<>();
                    params.put(Common.ID, String.valueOf(user.getId()));

                    if (isFromTopicDetail) {
                        AnalyticsReport.onEvent(face.getContext(), AnalyticsReport.TOPICS_USER_INFO_CLICK_EVENT_ID, params);
                    } else {
                        if (course.isLive()){
                            AnalyticsReport.onEvent(face.getContext(), AnalyticsReport.LIVE_USER_INFO_CLICK_EVENT_ID, params);
                        }
                        else{
                            AnalyticsReport.onEvent(face.getContext(), AnalyticsReport.VIDEO_USER_INFO_CLICK_EVENT_ID, params);
                        }

                    }

                }
            });

            // 添加标签
            if (Utils.isNotEmpty(course.tags)) {
                labelContainer.removeAllViews();
                labelContainer.setVisibility(View.VISIBLE);
                for (String tag : course.tags)
                    labelContainer.addView(getLabel(tag));
            } else {
                labelContainer.setVisibility(View.GONE);
            }

        }

        /**
         * 获取标签
         */
        public TextView getLabel(String labelText) {

            TextView label = new TextView(context);
            label.setText(labelText);
            label.setTextColor(Color.WHITE);
            label.setGravity(Gravity.CENTER);
            label.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResourceHelper.getDimen(R.dimen.sp30));
            label.setBackgroundResource(R.drawable.corners_stroke_white);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.rightMargin = ResourceHelper.getDimen(R.dimen.space_10);

            label.setLayoutParams(layoutParams);

            return label;
        }

        public void showDivider() {
            mDivider.setVisibility(View.VISIBLE);
        }

        public void hideDivider() {
            mDivider.setVisibility(View.GONE);
        }

        private void setLocation(Course course) {

            if (course.buyer_count > 0)
                liveTag.setText(course.getBuyer_count());
            else {
                liveTag.setText("");
            }
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
                timeStr = "时长 " + TimeUtil.getTimeWithStr(Long.parseLong(room.getDuration()));
                typeIcon.setImageDrawable(ResourceHelper.getDrawable(R.mipmap.icon_video));
            }

            timeTv.setText(timeStr);
        }

        private int getColor(int color) {
            return ResourceHelper.getColor(color);
        }

    }


    public void updateTopics(final TextView tipics, final Course course, final boolean isEnableTagClick) {

        if (course.topics == null || course.topics.isEmpty()) {
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

            final Topic topic = course.topics.get(0);

            if (topic != null) {
                final String topicName = topic.getFormatName(context);
                spannableStringBuilder.append(topicName);

                CustomClickableSpan span = new CustomClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        if (isEnableTagClick) {
                            HashMap<String, String> params = new HashMap<>();
                            params.put(Common.ID, String.valueOf(topic.getId()));
                            if (course.isLive()) {
                                AnalyticsReport.onEvent(tipics.getContext(), AnalyticsReport.HOME_LIVE_TOPIC_ITEM_CLICK_EVENT_ID, params);
                            } else {
                                AnalyticsReport.onEvent(tipics.getContext(), AnalyticsReport.HOME_VIDEO_TOPIC_ITEM_CLICK_EVENT_ID, params);
                            }
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

    public static class BannerViewHolder extends BaseAdapter.ViewHolder<List<Banner>> {

        public BannerViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void update(BaseAdapter adapter, int position, List<Banner> banners) {
            BannerView bannerView = (BannerView) itemView;
            bannerView.update(banners);
        }
    }

}
