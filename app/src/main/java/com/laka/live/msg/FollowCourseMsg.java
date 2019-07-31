package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.bean.Course;
import com.laka.live.bean.ListUserInfo;
import com.laka.live.ui.adapter.UserListAdapter;
import com.laka.live.util.Common;
import com.laka.live.util.Log;

import java.util.List;

/**
 * Created by luwies on 16/3/23.
 */
public class FollowCourseMsg extends ListMag<ListUserInfo> {

    @Expose
    @SerializedName(Common.DATA)
    private List<ListUserInfo> userInfoList;

    @Override
    public List<ListUserInfo> getList() {
        Log.log("FollowCourseMsg:"+userInfoList);
        return userInfoList;
    }

}
