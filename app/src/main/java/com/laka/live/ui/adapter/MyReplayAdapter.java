package com.laka.live.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.Course;
import com.laka.live.ui.activity.WebActivity;
import com.laka.live.ui.course.detail.CourseDetailActivity;
import com.laka.live.ui.viewholder.CourseViewHolder;
import com.laka.live.ui.widget.UserInfoHeader;

import java.util.HashMap;

/**
 * Created by luwies on 16/3/28.
 */
public class MyReplayAdapter extends BaseAdapter<Course, CourseViewHolder> {

    private final static long SECONDS = 1000L;
    private final static long MINUTES = 60 * SECONDS;

    private Context mContext;

    private UserInfoHeader mHeaderView;

    private final static int TYPE_HEAD = 1;

    private final static int TYPE_ITEM = 2;

    private int curTab = Course.LIVE;

    public MyReplayAdapter(Context context, boolean isDragEnable, UserInfoHeader userInfoHeader) {
        mContext = context;
        mHeaderView = userInfoHeader;
    }

    public void setCurTab(int curTab) {
        this.curTab = curTab;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public Course getItem(int position) {
        if (position == 0) {
            return null;
        }
        return super.getItem(position - 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        }
        return TYPE_ITEM;
    }

    public void setHeaderView(UserInfoHeader headerView) {
        mHeaderView = headerView;
    }

    public void showLoading(boolean isShow) {
//        if(viewHolder != null)
//            viewHolder.showLoading.setVisibility(isShow ? View.VISIBLE  : View.GONE);
    }

    public boolean isLoading() {
        return false;//viewHolder != null && (viewHolder.showLoading.getVisibility() == View.VISIBLE);
    }

    @Override
    protected void onItemClick(int position) {
        super.onItemClick(position);
    }

    private CourseViewHolder viewHolder;

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case TYPE_HEAD:
                viewHolder = new CourseViewHolder(mHeaderView);
                return viewHolder;
            case TYPE_ITEM:
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_course, parent, false);
                return new CourseViewHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, final int position) {
        if (position == 0) {
            return;
        }
        final Course course = getItem(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> params = new HashMap<>();
                params.put("ID", course.getId());

                if (curTab == Course.LIVE) {
                    AnalyticsReport.onEvent(mContext, AnalyticsReport.OTHER_USER_INFO_ACTIVITY_LIVE_LIST_ITEM_CLICK_EVENT_ID, params);
                } else {
                    AnalyticsReport.onEvent(mContext, AnalyticsReport.OTHER_USER_INFO_ACTIVITY_VIDEO_LIST_ITEM_CLICK_EVENT_ID, params);
                }

                if (curTab == Course.NEWS) {
                    WebActivity.startActivity(mContext, course.getNewsUrl(), course.getTitle());
                } else {
                    CourseDetailActivity.startActivity(mContext, course.getId());
                }

            }
        });
        holder.update(this, position, course);

    }


}
