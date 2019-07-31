package com.laka.live.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.bean.Course;
import com.laka.live.bean.Room;
import com.laka.live.bean.Topic;
import com.laka.live.ui.activity.UserInfoActivity;
import com.laka.live.ui.widget.DividerGridItemDecoration;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.TimeUtil;
import com.laka.live.util.Util;
import com.laka.live.util.Utils;
import com.laka.live.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lyf on 2017/4/13.
 */
public class FollowsAdapter extends BaseAdapter<Course, BaseAdapter.ViewHolder> {


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

    public FollowsAdapter(Context context) {
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
            return new VideoViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.follow_replay_layout, parent, false));
        }
    }


    public class HeadHolder extends ViewHolder<Course> {

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

    private class VideoViewHolder extends ViewHolder<Course> {
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

        private View mItemView;
        private View info;

        public ItemHolder(View itemView) {
            super(itemView);

            mItemView = itemView;
            info = itemView.findViewById(R.id.info);
            cover = (SimpleDraweeView) itemView.findViewById(R.id.cover);
            name = (TextView) itemView.findViewById(R.id.name);
            description = (TextView) itemView.findViewById(R.id.description);
            views = (TextView) itemView.findViewById(R.id.views);
            Context context = itemView.getContext();
            divider = Utils.dip2px(context, 2f);
            mHalfDivider = divider / 2;

            int height = (Util.getScreenWidth(context) - divider) / 2;
            int width = height;

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) cover.getLayoutParams();
            params.width = width;
            params.height = height;
            cover.setLayoutParams(params);

            params = (ViewGroup.MarginLayoutParams) itemView.getLayoutParams();
            params.width = width;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            itemView.setLayoutParams(params);
        }

        @Override
        public void update(BaseAdapter adapter, int position, final Course course) {

            // 显示已购买状态
            if (course.hasBuy()){
                views.setText("已购买");
                views.setVisibility(View.VISIBLE);
            }else {
                views.setVisibility(View.GONE);
            }
            // 设置封面
            ImageUtil.loadImage(cover, course.getCover_url());
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


}
