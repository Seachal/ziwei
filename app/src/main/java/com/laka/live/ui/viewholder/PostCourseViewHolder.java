package com.laka.live.ui.viewholder;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.bean.Course;
import com.laka.live.constants.Constants;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.ui.course.AddLiveActivity;
import com.laka.live.ui.course.AddVideoActivity;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.ui.adapter.PostCourseAdapter;
import com.laka.live.ui.widget.toggle.CourseToggle;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.NumberUtils;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;
import com.laka.live.util.ViewUtils;
import com.orhanobut.logger.Logger;

/**
 * Created by Lyf on 2017/9/1.
 */
public class PostCourseViewHolder extends BaseAdapter.ViewHolder<Course> {

    private View addView, titleLayout, discountLayout;
    private TextView discount, discountTv, totalCount;
    private TextView courseCount, courseTitle, discountTime;
    private ImageView courseCover, editCourse;
    private CourseToggle toggle;
    private LinearLayout discountTimeLayout;
    private int height; // 截止时间的那一块布局的高度
    private int bottomY; // RecyclerView的底部垂直坐标
    private int padding;
    private PostCourseAdapter mAdapter;

    public PostCourseViewHolder(View itemView, PostCourseAdapter mAdapter) {
        super(itemView);

        this.mAdapter = mAdapter;
        toggle = ViewUtils.findById(itemView, R.id.toggle);
        addView = ViewUtils.findById(itemView, R.id.addView);
        discount = ViewUtils.findById(itemView, R.id.discount);
        discountTv = ViewUtils.findById(itemView, R.id.discountTv);
        totalCount = ViewUtils.findById(itemView, R.id.totalCount);
        courseCover = ViewUtils.findById(itemView, R.id.courseCover);
        titleLayout = ViewUtils.findById(itemView, R.id.titleLayout);
        courseCount = ViewUtils.findById(itemView, R.id.courseCount);
        courseTitle = ViewUtils.findById(itemView, R.id.courseTitle);
        editCourse = ViewUtils.findById(itemView, R.id.editCourse);
        discountTime = ViewUtils.findById(itemView, R.id.discountTime);
        discountLayout = ViewUtils.findById(itemView, R.id.discountLayout);
        discountTimeLayout = ViewUtils.findById(itemView, R.id.discountTimeLayout);
        discountTimeLayout.setClickable(true);
        height = ResourceHelper.getDimen(R.dimen.space_70);
        bottomY = height - 1;// 之所以要-1，是因为华为机，在滚到最底部的时候，会自带一个动画，避免这个动画出现就减1.

    }

    @Override
    public void update(BaseAdapter adapter, int position, Course course) {

        switch (course.getItemType()) {

            case Constants.TYPE_ITEM:
                setItemView(course, position);
                break;
            case Constants.TYPE_ADD:
                setAddView(course);
                break;
            case Constants.TYPE_EDIT:
                addView.setVisibility(View.GONE);
                setAddView(course);
                break;
            default:
                break;
        }

    }

    private void initToggle(Course course) {

        // 没购买的才可以编辑
        if (mAdapter.hasNotSold()) {
            toggle.setClickable(true);
        } else {
            toggle.setClickable(false);
            discountTimeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastHelper.showToast(ResourceHelper.getString(
                            R.string.edit_course_tip, "限时优惠"));
                }
            });
        }

        // 初始化开关控件
        toggle.initToggle(course, mAdapter.isAutoScroll(), mAdapter.getRecyclerView(), discountTimeLayout.getChildAt(2));

    }

    // 设置普通item类型的布局
    private void setItemView(final Course course, final int position) {

        courseCount.setText("第" + position + "节课");
        courseTitle.setText(course.getTitle());

//        Logger.e("Adapter 加载课程：" + course);
        if (!TextUtils.isEmpty(course.getLocalCoverUrl())) {
            courseCover.setImageURI(Uri.parse(course.getLocalCoverUrl()));
        } else if (!TextUtils.isEmpty(course.getCover_url())) {
            ImageUtil.displayImage(courseCover, course.getCover_url(), R.drawable.blank_icon_banner);
        } else {
            courseCover.setImageResource(R.drawable.blank_icon_banner);
        }

        editCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                course.setBuyerCount(mAdapter.hasNotSold() ? 0 : 1);
                bundle.putSerializable("data", course);
                bundle.putSerializable("editPosition", position);

                if (mAdapter.getCourseType() == Course.LIVE) {
                    // 修改直播课程
                    Utils.startActivityForResult(mAdapter.getContext(), AddLiveActivity.class, bundle, Constants.REQUEST_EDIT_COURSE);
                } else if (mAdapter.getCourseType() == Course.VIDEO) {
                    // 修改视频课程
                    AddVideoActivity.startActivityForResult(mAdapter.getContext(), bundle, Constants.REQUEST_EDIT_COURSE);
                }

            }
        });
    }

    // 设置add类型的布局
    private void setAddView(final Course course) {

//        Logger.e("Adapter 添加或修改课程：" + course);
        initToggle(course);
        titleLayout.setVisibility(View.GONE);
        courseTitle.setVisibility(View.GONE);
        discountLayout.setVisibility(View.VISIBLE);
        discountTv.setText(course.getDiscountText());
        discountTime.setText(course.getFormatDiscountTime());
        totalCount.setText("课堂优惠(" + mAdapter.getCourseCount() + "节)");

        if (course.isDiscount()) {
            scrollToBottom(course);
            discountTimeLayout.setVisibility(View.VISIBLE);
            if (course.getDiscount() > 0) {
                discount.setText(NumberUtils.splitDoubleStr(course.getDiscount(), "折"));
                discount.setVisibility(View.VISIBLE);
            } else {
                discount.setVisibility(View.GONE);
            }
        } else {
            toggle.toggleOff();
            discount.setVisibility(View.GONE);
            discountTimeLayout.setVisibility(View.GONE);
        }

        discountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBusManager.postEvent(0, SubcriberTag.CLICK_DISCOUNT);
            }
        });

        addView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mAdapter.getCourseType() == Course.LIVE) {
                    // 添加直播课程
                    Utils.startActivityForResult(mAdapter.getContext(), AddLiveActivity.class, Constants.REQUEST_ADD_COURSE);
                } else if (mAdapter.getCourseType() == Course.VIDEO) {
                    // 添加视频课程
                    AddVideoActivity.startActivityForResult(mAdapter.getContext(), Constants.REQUEST_ADD_COURSE);
                }

            }
        });

        discountTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mAdapter.hasNotSold()) {
                    mAdapter.getTimePicker().show(v);
                } else {
                    ToastHelper.showToast(ResourceHelper.getString(
                            R.string.edit_course_tip, "优惠的截止时间"));
                }

            }
        });
        ViewUtils.setPartTextColor(totalCount, R.color.colorFFC401, 5, totalCount.getText().length() - 2);
    }


    // 滚到底部
    private void scrollToBottom(final Course course) {

        if (mAdapter.isAutoScroll()) {
            ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int tempBottomY = course.isOpenLimitDiscount() ? (bottomY + height) : (bottomY + padding);
                    if (mAdapter.getRecyclerView().computeVerticalScrollOffset() != tempBottomY) {
                        mAdapter.getRecyclerView().smoothScrollBy(0, tempBottomY);
                        discountTimeLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            };
            discountTimeLayout.getViewTreeObserver().addOnGlobalLayoutListener(listener);
        } else {
            mAdapter.setAutoScroll(true);
            padding = ResourceHelper.getDimen(R.dimen.space_8);
        }

    }

}

