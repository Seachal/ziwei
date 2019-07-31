package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

import java.util.List;

/**
 * Created by Lyf on 2017/4/14.
 */
public class Follows {

    @Expose
    @SerializedName(Common.LIVING)
    public List<Course> living; // 直播中的课程

    @Expose
    @SerializedName(Common.REPLAY)
    public List<Course> replay; // 回放课程

    @Expose
    @SerializedName(Common.VIDEO)
    public List<Course> video; // 视频课程

}
