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

public class FreeSessionAdapter extends BaseAdapter<Course, LivingViewHolder> {
    private Context mContext;

    public FreeSessionAdapter(Context context) {
        this.mContext = context;
    }

//    @Override
//    protected void onItemClick(int position) {
//        super.onItemClick(position);
//
//        Course course = getItem(position);
//        if (course != null) {
//            course.onClickEvent(mContext);
//        }
//    }

    @Override
    public LivingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LivingViewHolder(mContext, parent, LivingViewHolder.TYPE_FREE_SESSION);
    }
}
