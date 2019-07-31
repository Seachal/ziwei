package com.laka.live.ui.course;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.Course;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.course.detail.CourseDetailActivity;
import com.laka.live.ui.room.LiveRoomActivity;
import com.laka.live.ui.room.SeeReplayActivity;
import com.laka.live.ui.widget.SelectorButton;
import com.laka.live.util.Common;
import com.laka.live.util.Utils;

import java.util.HashMap;

import framework.ioc.annotation.InjectExtra;
import framework.ioc.annotation.InjectView;

/**
 * Created by Lyf on 2017/4/6.
 */
public class PostSuccessActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(id = R.id.play)
    public SelectorButton play;
    @InjectView(id = R.id.commit)
    public SelectorButton commit;
    @InjectView(id = R.id.share)
    public SelectorButton shareButton;
    @InjectView(id = R.id.tip)
    public TextView tip;
    // 发布成功的类型
    @InjectExtra(name = "type", def = "1")
    public Integer type;
    @InjectExtra(name = "trailerId")
    public String trailerId;

    private Course mCourse;

    public final static int LIVE = 1, VIDEO = 2;
    public static final int POSTLIVESUCCESS = 1; // 预告发布成功
    public static final int UPDATELIVESUCCESS = 2; // 预告更新成功
    public static final int PAYCOURSESUCCESS = 3; // 购买课程成功
    public static final int POSTNEWSSUCCESSS = 4; // 资讯发布成功

    public static void startActivity(Context context, int type) {
        Intent intent = new Intent(context, PostSuccessActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, int type, String trailerId, Course course) {
        Intent intent = new Intent(context, PostSuccessActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putSerializable("course", course);
        bundle.putString("trailerId", trailerId);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_success);
        initView();
    }

    private void initView() {

        // 获取课程详情,用于支付完后，直接观看
        mCourse = (Course) getIntent().getSerializableExtra("course");

        if (mCourse != null && mCourse.getRoom() != null) {
            if (mCourse.type == LIVE && mCourse.status == Course.LIVING) {
                play.setNewText("观看直播");
                play.setVisibility(View.VISIBLE);
            } else if (mCourse.type == VIDEO) {
                play.setNewText("观看视频 ");
                play.setVisibility(View.VISIBLE);
            }else{
                play.setVisibility(View.GONE);
            }
        }else{
            play.setVisibility(View.GONE);
        }


        switch (type) {
            case POSTLIVESUCCESS:
                shareButton.setNewText("通知大家看预告");
                break;
            case UPDATELIVESUCCESS:
                tip.setText("保存课堂成功");
                shareButton.setNewText("通知大家看预告");
                break;
            case PAYCOURSESUCCESS:
                tip.setText("课堂购买成功");
                shareButton.setNewText("邀请好友购买");
                commit.setNewText("查看购买的课堂");
                AnalyticsReport.onEvent(this, AnalyticsReport.BUY_SUCCESS_VIEW, getParams());
                break;
            case POSTNEWSSUCCESSS:
                tip.setText("资讯发布成功");
                shareButton.setVisibility(View.GONE);
                commit.setNewText("查看发布的资讯");
                break;
        }

        activityManager.finishActivity(CourseDetailActivity.class, MyCoursesActivity.class);
    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.play:
                if (mCourse.type == LIVE) {
                    playLive();
                } else if (mCourse.type == VIDEO) {
                    playVideo();
                }
                finish();
                break;
            case R.id.share:
                doShare();
                break;
            case R.id.commit:
                commit();
                finish();
                break;
            case R.id.ll_qq:
            case R.id.ll_wechat:
            case R.id.ll_monent:
            case R.id.ll_weibo:
            case R.id.ll_qzone:
                int Share_type = -1;

                switch (view.getId()) {
                    case R.id.ll_wechat:
                        Share_type = 5;
                        break;
                    case R.id.ll_monent:
                        Share_type = 6;
                        break;
                    case R.id.ll_weibo:
                        Share_type = 7;
                        break;
                    case R.id.ll_qq:
                        Share_type = 8;
                        break;
                    case R.id.ll_qzone:
                        Share_type = 9;
                        break;
                }
                HashMap<String, String> params = getParams();
                params.put("Share_type", String.valueOf(Share_type));
                switch (type) {
                    case POSTLIVESUCCESS:
                        AnalyticsReport.onEvent(this, AnalyticsReport.POST_SUCCESS_SHARE_CLICK, params);
                        break;
                    case PAYCOURSESUCCESS:
                        AnalyticsReport.onEvent(this, AnalyticsReport.BUY_SUCCESS_SHARE_CLICK, params);
                        break;
                }
                break;
        }

    }

    private void doShare() {
        showShareDialog(Common.SHARE_COURSE_URL + mCourse.getId(), mCourse.getTitle(),
                mCourse.getSummary(), mCourse.getPreCover_url(), true, this);

    }

    // 点击第二个按钮
    private void commit() {

        switch (type) {
            case POSTLIVESUCCESS:
                AnalyticsReport.onEvent(this, AnalyticsReport.POST_SUCCESS_LOOK_CLICK, getParams());
                CourseDetailActivity.startActivity(this, mCourse.getId());
                break;
            case UPDATELIVESUCCESS:
                CourseDetailActivity.startActivity(this, mCourse.getId());
                break;
            case PAYCOURSESUCCESS:
                AnalyticsReport.onEvent(this, AnalyticsReport.BUY_SUCCESS_LOOK_CLICK, getParams());
                CourseDetailActivity.startActivity(this, mCourse.getId());
                break;
            case POSTNEWSSUCCESSS:
                Bundle bundle = new Bundle();
                bundle.putInt("code", MyCoursesActivity.MYLIVE);
                bundle.putInt("index", 2);
                Utils.startActivity(this, MyCoursesActivity.class, bundle);
                break;
        }

    }

    // 观看回放
    private void playVideo() {
        SeeReplayActivity.startActivity(this, mCourse.getId(),
                mCourse.getRoom().getDownUrl(), String.valueOf(mCourse.getUser().getId()),
                mCourse.getViews(), mCourse.getRecvCoins(), mCourse.getRoom().getChannelId(), mCourse.getType());
    }

    // 观看直播
    private void playLive() {
        LiveRoomActivity.startPlay(this, mCourse.getUser().getId(), false, mCourse.getTitle(),
                mCourse.getRoom().getStreamId(), mCourse.getRoom().getChannelId(), mCourse.getUser().getAvatar(),
                mCourse.cover_url, Common.FROM_MAIN, mCourse.getId());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }

    private HashMap<String, String> getParams() {

        HashMap<String, String> params = new HashMap<>();
        params.put("Class_type", mCourse.isLive() ? "22" : "23");
        return params;
    }

}
