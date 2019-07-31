package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.Course;
import com.laka.live.bean.Follows;
import com.laka.live.bean.User;
import com.laka.live.ui.adapter.HomeFollowAdapter;
import com.laka.live.util.Common;
import com.laka.live.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lyf on 2017/4/14.
 */

public class FollowsMsg extends ListMag<Course> {

    @Expose
    @SerializedName(Common.DATA)
    public Follows data;

    @Override
    public List<Course> getList() {

        List<Course> mData = new ArrayList<>();

        if (Utils.isNotEmpty(data.living)) {
            addDivide(mData);
            for (Course course : data.living) {
                course.itemType = HomeFollowAdapter.TYPE_LIVE;
                mData.add(course);
            }
        } else {
//            addDivide(mData);
//            Course course = new Course();
//            course.setId(95);
//            course.setTitle("测试标题");
//            course.user_id = 155;
//            course.setPrice(500);
//            course.setStart_time(1492075200);
//            course.setType(1);
//            course.setCover_url("http://img.zwlive.lakatv.com/1704/u155001e9f77d5.jpeg");
//            course.setStatus(60);
//            course.buyer_count = 8;
//            course.duration = "859";
//            course.recv_likes = 28;
//            course.status_text = "回放";
//            course.user = new User();
//            course.user.id = 155;
//            course.user.nickname = "用户姓名";
//            course.itemType = HomeFollowAdapter.TYPE_LIVE;
//            course.user.avatar = "http://img.lakatv.com/a580b0dafacca3esavd5qcx8mom2a6.jpg";
//            mData.add(course);
        }

        if (Utils.isNotEmpty(data.replay)) {
            addDivide(mData);
            for (Course course : data.replay) {
                course.itemType = HomeFollowAdapter.TYPE_REPLAY;
                mData.add(course);
            }
        }

        if (Utils.isNotEmpty(data.video)) {
            addDivide(mData);
            for (Course course : data.video) {
                course.itemType = HomeFollowAdapter.TYPE_VIDEO;
                mData.add(course);
            }
        }

        return mData;
    }

    private void addDivide(List<Course> mData) {
        Course course = new Course();
        course.itemType = HomeFollowAdapter.TYPE_DIVIDE;
        mData.add(course);
    }

}
