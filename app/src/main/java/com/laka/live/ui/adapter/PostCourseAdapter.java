package com.laka.live.ui.adapter;


import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.pickerview.TimePickerView;
import com.laka.live.R;
import com.laka.live.bean.Course;
import com.laka.live.constants.Constants;
import com.laka.live.ui.viewholder.PostCourseHeaderViewHolder;
import com.laka.live.ui.viewholder.PostCourseViewHolder;
import com.laka.live.ui.widget.MyTimePickerView;
import com.laka.live.util.ToastHelper;

import java.util.Date;

/**
 * Created by Lyf on 2017/5/31.
 */
public class PostCourseAdapter extends BaseAdapter<Course, BaseAdapter.ViewHolder> {

    private Activity mContext;
    private int mCourseType;
    private boolean autoScroll = true;
    private RecyclerView mRecyclerView;
    private MyTimePickerView mTimePicker;

    public PostCourseAdapter(Activity mContext, int mCourseType, RecyclerView mRecyclerView) {
        this.mContext = mContext;
        this.mCourseType = mCourseType;
        this.mRecyclerView = mRecyclerView;
        initAirTimePicker();
    }

    // 初始化时间选择器
    private void initAirTimePicker() {

        //时间选择器
        mTimePicker = new MyTimePickerView.Builder(mContext,
                new MyTimePickerView.OnTimeSelectListener() {
                    @Override
                    public boolean onTimeSelect(Date date, View view) {
                        if (date.getTime() < System.currentTimeMillis()) {
                            ToastHelper.showToast("优惠截止时间不能早于当前时间,请重新选择");
                            return false;
                        }
                        mDatas.get(getItemCount() - 1).setDiscount_time(date.getTime() / 1000);
                        notifyItemChanged(getItemCount() - 1);
                        return true;
                    }
                }).setType(TimePickerView.Type.YEAR_MONTH_DAY_HOUR_MIN)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(20)
                .setSubmitColor(Color.BLACK)
                .setCancelColor(Color.BLACK)
                .setLabel("", "", "", "", "", "") // 隐藏年月日时分的字样
                .build();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {

            case Constants.TYPE_HEAD:
                return new PostCourseHeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_course_head, null), this);
            case Constants.TYPE_ADD:
            case Constants.TYPE_EDIT:
                return new PostCourseViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_post_course, null), this);
            case Constants.TYPE_ITEM:
                return new PostCourseViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_post_course, null), this);
            default:
                break;
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getItemType();
    }

    // 获取课节数
    public int getCourseCount() {

        // 用总课程数减去非TYPE_ITEM类型
        if (mDatas.get(getItemCount() - 1).getItemType() == Constants.TYPE_ADD
                || mDatas.get(getItemCount() - 1).getItemType() == Constants.TYPE_EDIT) {
            return getItemCount() - 2;
        } else {
            return getItemCount() - 1;
        }

    }

    /**
     * @return 是否没人购买
     */
    public boolean hasNotSold() {
        for (int i = 0; i < getItemCount(); ++i) {
            if (mDatas.get(i).hasSold()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param autoScroll 编辑课堂进来时，就不需要自动滚动到底部
     */
    public void setAutoScroll(boolean autoScroll) {
        this.autoScroll = autoScroll;
    }

    public boolean isAutoScroll() {
        return autoScroll;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public Activity getContext() {
        return mContext;
    }

    public int getCourseType() {
        return mCourseType;
    }

    public MyTimePickerView getTimePicker() {
        return mTimePicker;
    }

}
