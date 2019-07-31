package com.laka.live.ui.homepage.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.laka.live.bean.Course;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.ui.homepage.LivingViewHolder;

/**
 * @ClassName: FreeSessionAdapter
 * @Description: 免费课程
 * @Author: chuan
 * @Version: 1.0
 * @Date: 6/1/17
 */

public class TopicDetailAdapter extends BaseAdapter<Course, LivingViewHolder> {
    private Context mContext;

    public TopicDetailAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public LivingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LivingViewHolder(mContext, parent, LivingViewHolder.TYPE_TOPIC_DETAIL);
    }
}
