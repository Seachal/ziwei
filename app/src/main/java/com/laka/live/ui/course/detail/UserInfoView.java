package com.laka.live.ui.course.detail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.User;
import com.laka.live.ui.activity.UserInfoActivity;

import framework.ioc.annotation.InjectView;

/**
 * Created by Lyf on 2017/9/12.
 */
public class UserInfoView extends BaseDetailView implements View.OnClickListener {

    @InjectView(id = R.id.userName, click = "")
    private TextView userName; // 主播名字
    @InjectView(id = R.id.signature, click = "")
    private TextView signature; // 主播签名
    @InjectView(id = R.id.userFace, click = "")
    private SimpleDraweeView userFace; // 主播头像

    public UserInfoView(Context context) {
        this(context, null);
    }

    public UserInfoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setContentView(this,R.layout.view_course_detail_user_info);
    }

    @Override
    protected void initData(CourseDetailHelper mHelper) {
        super.initData(mHelper);
        setUserInfo();
    }

    // 设置主播信息
    private void setUserInfo() {

        User user = mHelper.getCourseDetail().getUser();

        if (user != null) {
            userName.setTag(user.getId());
            userFace.setImageURI(user.getAvatar());
            userName.setText(user.getNickname());
            signature.setText(user.getDescription());
        }

    }

    public void onUserInfoClick() {
        AnalyticsReport.onEvent(mContext, AnalyticsReport.COURSE_DETAIL_USER_INFO_CLICK, mHelper.getEventParams());
        UserInfoActivity.startActivity(mContext, String.valueOf(userName.getTag()));
    }

    @Override
    public void onClick(View v) {}

}
