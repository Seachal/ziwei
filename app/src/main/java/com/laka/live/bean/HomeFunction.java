package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

import java.util.List;

/**
 * @ClassName: HomeFunction
 * @Description: 首页功能
 * @Author: chuan
 * @Version: 1.0
 * @Date: 6/2/17
 */

public class HomeFunction {
    @Expose
    @SerializedName(Common.ID)
    private int id;

    @Expose
    @SerializedName(Common.NAME)
    private String name;

    @Expose
    @SerializedName(Common.ICON)
    private String icon;

    @Expose
    @SerializedName(Common.TYPE)
    private int type;

    @Expose
    @SerializedName(Common.VALUE)
    private String value;

    @Expose
    @SerializedName(Common.COURSE)
    private Course course;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourses(Course course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "Function{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", type=" + type +
                ", value='" + value + '\'' +
                ", course=" + course +
                '}';
    }
}
