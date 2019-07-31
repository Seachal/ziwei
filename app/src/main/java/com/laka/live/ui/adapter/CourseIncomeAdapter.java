package com.laka.live.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.laka.live.bean.Course;
import com.laka.live.ui.activity.IncomeCourseDetailActivity;
import com.laka.live.ui.activity.IncomeGoodsDetailActivity;
import com.laka.live.ui.course.detail.CourseDetailActivity;
import com.laka.live.ui.widget.IncomeCourseView;
import com.laka.live.ui.widget.IncomeGoodsView;

/**
 * @ClassName: CourseIncomeAdapter
 * @Description: 课程收益
 * @Author: chuan
 * @Version: 1.0
 * @Date: 25/07/2017
 */

public class CourseIncomeAdapter extends BaseAdapter<Course, CourseIncomeAdapter.ViewHolder> {

    private Context mContext;

    public CourseIncomeAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(new IncomeCourseView(mContext, IncomeCourseView.FROM_COURSE_LIST));
    }

    public static class ViewHolder extends BaseAdapter.ViewHolder<Course> {
        private IncomeCourseView mIncomeCourseView;

        public ViewHolder(View itemView) {
            super(itemView);

            if (itemView instanceof IncomeCourseView) {
                mIncomeCourseView = (IncomeCourseView) itemView;
            }
        }

        @Override
        public void update(BaseAdapter adapter, int position, final Course course) {
            if (mIncomeCourseView != null) {
                mIncomeCourseView.updateData(course);
                mIncomeCourseView.setOnViewClickListener(new IncomeCourseView.OnViewClickListener() {
                    @Override
                    public void onTitleClick() {
                        CourseDetailActivity.startActivity(mIncomeCourseView.getContext(), course.getId());
                    }

                    @Override
                    public void onThumbClick() {
                        CourseDetailActivity.startActivity(mIncomeCourseView.getContext(), course.getId());
                    }

                    @Override
                    public void onMoreClick() {
                        IncomeCourseDetailActivity.startActivity(mIncomeCourseView.getContext(), course.getId(),course.getIncomeType());
                    }
                });
            }
        }
    }

}
