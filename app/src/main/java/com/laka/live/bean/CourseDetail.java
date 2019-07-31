package com.laka.live.bean;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;
import com.laka.live.util.Utils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Lyf on 2017/4/7.
 */
public class CourseDetail implements Serializable{

    @Expose
    @SerializedName(Common.USER)
    public User user; // 用户信息

    @Expose
    @SerializedName(Common.BUYERS)
    public List<User> buyers; // 购买用户

    @Expose
    @SerializedName(Common.COURSE)
    public Course course; // 课程信息

    @Expose
    @SerializedName(Common.COURSES)
    public List<Course> courses;  // 同类课程(用于编辑)

    @Expose
    @SerializedName(Common.COURSE_TRAILER)
    public CourseTrailer course_trailer; // 课程预告

    @Expose
    @SerializedName(Common.SIMILAR_COURSES)
    private List<Course> similarCourses; // 同类课程

    @Expose
    @SerializedName(Common.ROOM)
    public Room room; // 房间

    public List<String> course_ids;// 购买的课堂id;(仅本地使用)

    // 这个时间是以秒为单位
    public long getBeginTime() {
        if (room == null) {
            return 0;
        } else {
            return room.getBeginTime();
        }
    }

    /**
     *
     * @return True 是套课。 False 不是套课
     */
    public boolean hasSimilarCourse() {
        return Utils.isNotEmpty(similarCourses);
    }

    public Course getCourse() {
        return course;
    }

    public User getUser() {
        return user;
    }

    public CourseTrailer getCourseTrailer() {
        return course_trailer;
    }

    public List<Course> getSimilarCourses() {
        return similarCourses;
    }

}
