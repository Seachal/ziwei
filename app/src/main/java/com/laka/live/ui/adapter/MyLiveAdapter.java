package com.laka.live.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.laka.live.R;
import com.laka.live.bean.Course;
import com.laka.live.ui.viewholder.CourseViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lyf on 2017/3/30.
 */
public class MyLiveAdapter extends BaseAdapter<Course, CourseViewHolder> {

    private boolean isEdit;
    private HashMap<String, Boolean> courseId = new HashMap<>();

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        // 设置编辑样式
        setEditMode(holder.selected, getItem(position));
    }

    // 设置编辑样式
    private void setEditMode(CheckBox selected, final Course course) {

        // 如果是处于编辑状态,就显示单选框 && i.即将开播、时间调整中，有选择按钮，可进行取消课堂操作
        if (getEditStatus()) {

            //这个主要是解决复用的问题
            boolean isChecked = getCourseId().get(course.getId()) != null;
            selected.setChecked(isChecked);

            switch (course.getType()) {
                case Course.LIVE:
                    //  ( 没人购买 或者 （有人购买但处于未开播状态的）) 并且 不能是取消状态
                    if ((course.hasNotSold() || course.status < Course.LIVING)
                            && (course.status != Course.CANCEL)) {
                        selected.setVisibility(View.VISIBLE);
                    } else {
                        selected.setVisibility(View.INVISIBLE);
                    }
                    break;
                case Course.VIDEO:
                    if (course.hasNotSold() && course.status != Course.CANCEL) {
                        selected.setVisibility(View.VISIBLE);
                    } else {
                        selected.setVisibility(View.INVISIBLE);
                    }
                    break;
                default:
                    selected.setVisibility(View.VISIBLE);
                    break;
            }


        } else {
            selected.setVisibility(View.GONE);
        }


        selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getCourseId().get(course.getId()) == null) {
                    getCourseId().put(course.getId(), true);
                } else {
                    getCourseId().remove(course.getId());
                }

            }
        });
    }

    // 是否处于编辑状态
    public void setEditStatus(boolean isEdit) {
        this.isEdit = isEdit;
    }

    // 获取选中状态
    public boolean getEditStatus() {
        return isEdit;
    }

    // 获取选中的课程的Id
    public List<String> getChooseId() {

        List<String> course_Id = new ArrayList<>();

        for (String id : courseId.keySet()) {
            course_Id.add(id);
        }

        return course_Id;
    }

    public HashMap<String, Boolean> getCourseId() {
        return courseId;
    }

    public void setCourseId(HashMap<String, Boolean> courseId) {
        this.courseId = courseId;
    }

}