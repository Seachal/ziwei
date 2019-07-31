package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.Course;
import com.laka.live.bean.IncomeCourseInfo;

import java.util.List;

/**
 * @ClassName:
 * @Description:
 * @Author: chuan
 * @Version: 1.0
 * @Date: 18/10/2017
 */

public class CourseIncomeDetail {
    @Expose
    @SerializedName("course")
    private Course course;

    @Expose
    @SerializedName("infos")
    private List<IncomeCourseInfo> infos;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<IncomeCourseInfo> getInfos() {
        return infos;
    }

    public void setInfos(List<IncomeCourseInfo> infos) {
        this.infos = infos;
    }

    @Override
    public String toString() {
        return "CourseIncomeDetailMsg{" +
                "course=" + course +
                ", infos=" + infos +
                '}';
    }
}
