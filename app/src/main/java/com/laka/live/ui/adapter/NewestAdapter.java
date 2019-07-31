package com.laka.live.ui.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laka.live.R;
import com.laka.live.bean.Room;
import com.laka.live.bean.Topic;
import com.laka.live.ui.widget.DividerGridItemDecoration;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;

import java.util.List;

/**
 * Created by luwies on 16/3/30.
 */
public class NewestAdapter extends BaseAdapter<Room, NewestAdapter.ViewHolder>  {

    private static final int TYPE_HEAD = 0;

    private static final int TYPE_NORMAL = 1;

    private List<Topic> mTopics;

    public NewestAdapter() {
        super();
    }

    public void setTopics(List<Topic> topics) {
        mTopics = topics;
    }


    public boolean hasHead() {
        return mTopics != null && mTopics.isEmpty() == false;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + (hasHead() ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        boolean hasHead = hasHead();
        if (hasHead == false) {
            return TYPE_NORMAL;
        }
        return position == 0 ? TYPE_HEAD : TYPE_NORMAL;
    }

    @Override
    public Room getItem(int position) {
        if (hasHead()) {
            position -= 1;
        }
        return super.getItem(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseAdapter.ViewHolder innerHolder ;
        if (viewType == TYPE_HEAD) {
            RecyclerView recyclerView = new RecyclerView(parent.getContext());
            innerHolder = new HeadHolder(recyclerView);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_simple_room, parent, false);
            innerHolder = new SimpleRoomAdapter.ViewHolder(view);
        }
        return new ViewHolder(innerHolder);
    }

    @Override
    public void onBindViewHolder(NewestAdapter.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == TYPE_HEAD) {
            holder.update(this, position, mTopics);
        } else {
            holder.update(this, position, getItem(position));
        }
    }

    public static class ViewHolder extends BaseAdapter.ViewHolder {

        private BaseAdapter.ViewHolder innerHolder;

        public ViewHolder(BaseAdapter.ViewHolder holder) {
            super(holder.itemView);
            innerHolder = holder;
        }

        @Override
        public void update(BaseAdapter adapter, int position, Object object) {
            innerHolder.update(adapter, position, object);
        }
    }

    public static class HeadHolder extends BaseAdapter.ViewHolder<List<Topic>> {

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
            /*ColorDrawable divider = new ColorDrawable();
            divider.setColor(ContextCompat.getColor(context, R.color.color363644));
            decoration.setDivider(divider);
            decoration.setSpacing(1);*/
            itemView.addItemDecoration(decoration);
            topicAdapter = new TopicAdapter(context);
            itemView.setAdapter(topicAdapter);
        }

        @Override
        public void update(BaseAdapter adapter, int position, List<Topic> topics) {
            topicAdapter.clear(false);
            topicAdapter.addAll(topics);
        }
    }
}
