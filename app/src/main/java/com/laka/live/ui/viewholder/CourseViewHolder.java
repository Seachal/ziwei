package com.laka.live.ui.viewholder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.bean.Course;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.ui.course.helper.CourseStatusHelper;
import com.laka.live.ui.widget.AnimationView;
import com.laka.live.ui.widget.TimeLimitTextView;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.NumberUtils;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ViewUtils;

/**
 * Created by Lyf on 2017/6/6.
 */
public class CourseViewHolder extends BaseAdapter.ViewHolder<Course>{

    private View mDivider;
    private Chronometer mTimeTv;
    private TimeLimitTextView title;
    private TextView money;
    public CheckBox selected;
    private TextView mStatusTv;
    private TextView cancelTv;
    private AnimationView mWaveView;
    private SimpleDraweeView userFace;
    private View cancelLayout;
    private ImageView mLiveIv;
    private CourseStatusHelper mCourseStatusHelper;

    // 普通的课程item用这个
    public CourseViewHolder(View itemView) {
        super(itemView);
        selected = ViewUtils.findById(itemView, R.id.selected);
        mTimeTv = ViewUtils.findById(itemView, R.id.time);
        title = ViewUtils.findById(itemView, R.id.title);
        money = ViewUtils.findById(itemView, R.id.money);
        mDivider = ViewUtils.findById(itemView, R.id.divider);
        mStatusTv = ViewUtils.findById(itemView, R.id.status);
        userFace = ViewUtils.findById(itemView, R.id.userFace);
        cancelTv = ViewUtils.findById(itemView, R.id.cancelTv);
        mWaveView = ViewUtils.findById(itemView, R.id.animation);
        cancelLayout = ViewUtils.findById(itemView, R.id.cancelLayout);
        mLiveIv = (ImageView) itemView.findViewById(R.id.live_iv);
        mCourseStatusHelper = new CourseStatusHelper();
    }

    @Override
    public void update(BaseAdapter adapter, final int position, final Course course) {

        title.setTimeLimitText(course); // 设置课程标题
        ImageUtil.displayImage(userFace, course.cover_url, R.drawable.blank_icon_banner); // 设置课程封面

        if (course.getType() == Course.LIVE || course.getType() == Course.VIDEO) {
            money.setVisibility(View.VISIBLE);
            money.setText(NumberUtils.getCoursePriceFormat(course.getPrice(), "味豆"));
            setCourseStatus(title, mStatusTv, mTimeTv, money, mDivider, cancelLayout, course);
        } else {
            money.setVisibility(View.GONE);
        }

        // 未开播与直播中才要显示Live标签
        if (course.isLive() && course.status <= Course.LIVING && course.status != Course.CANCEL) {
            mLiveIv.setVisibility(View.VISIBLE);
        }else{
            mLiveIv.setVisibility(View.GONE);
        }

        // 统一设置课程的时间和状态(兼容所有带有复用的列表)
        mCourseStatusHelper.setCourseStatus(course,mTimeTv,mStatusTv,mWaveView,mDivider);
    }

    // 设置课程状态
    private void setCourseStatus(TextView title, TextView textStatus, TextView textTime, TextView money, View divider, View cancelLayout, Course course) {

        // 取消和时间调整显示成灰色样式
        if (course.status == Course.CANCEL) {
            cancelLayout.setVisibility(View.VISIBLE);
            cancelTv.setText(course.getStatus_text().trim());
            title.setTextColor(ResourceHelper.getColor(R.color.color7F333333));
            money.setTextColor(ResourceHelper.getColor(R.color.color7F8B8B8B));
            textTime.setTextColor(ResourceHelper.getColor(R.color.color7F848484));
            textStatus.setTextColor(ResourceHelper.getColor(R.color.color7F848484));
            divider.setBackgroundColor(ResourceHelper.getColor(R.color.color33999999));
        } else {
            if (course.status == Course.CHANGETIME) {
                textTime.setText("");
                divider.setVisibility(View.GONE);
            } else {
                divider.setVisibility(View.VISIBLE);
            }
            cancelLayout.setVisibility(View.GONE);
            title.setTextColor(ResourceHelper.getColor(R.color.color333333));
            money.setTextColor(ResourceHelper.getColor(R.color.colorFF950B));
            textTime.setTextColor(ResourceHelper.getColor(R.color.color848484));
            textStatus.setTextColor(ResourceHelper.getColor(R.color.color848484));
            divider.setBackgroundColor(ResourceHelper.getColor(R.color.color999999));
        }


    }

}