package com.laka.live.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.account.follow.FollowRequestHelper;
import com.laka.live.bean.Course;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.viewholder.CourseViewHolder;
import com.laka.live.ui.viewholder.GoodsViewHolder;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.TimeUtil;
import com.laka.live.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luwies on 16/3/22.
 * zwl update 16/7/25
 */
public class UserListCourseAdapter extends BaseAdapter<Course, BaseAdapter.ViewHolder> {

    public static final String FROM_SEARCH = "search";
    public static final String FROM_MY_FOLLOW = "my_follow";
    public static final String FROM_MY_FANS = "my_fans";
    public static final String FROM_SEARCH_GOODS = "search_goods";
    public static final String FROM_USER_INFO_FOLLOW = "user_info_follow";
    public static final String FROM_USER_INFO_FANS = "user_info_fans";

    private String fromTag;

    public UserListCourseAdapter(BaseActivity baseActivity, String fromTag) {
        super();
        this.fromTag = fromTag;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);


    }


}
