package com.laka.live.ui.course.detail;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.Topic;
import com.laka.live.ui.activity.TopicRoomListActivity;
import com.laka.live.ui.widget.course.CoursePriceView;
import com.laka.live.ui.widget.course.DiscountTimeView;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;
import com.laka.live.player.MyExoPlayerPlus;

import framework.ioc.annotation.InjectView;

/**
 * Created by Lyf on 2017/9/12.
 */
public class CourseTrailerView extends BaseDetailView implements View.OnClickListener {

    @InjectView(id = R.id.player, click = "")
    private TextView player;
    @InjectView(id = R.id.courseName)
    private TextView courseName;
    @InjectView(id = R.id.rl_trailer)
    private RelativeLayout mRlTrailer;
    @InjectView(id = R.id.trailerCover)
    private ImageView mTrailerCover;
    @InjectView(id = R.id.coursePriceView)
    private CoursePriceView coursePriceView;
    @InjectView(id = R.id.discountTimeView)
    private DiscountTimeView mDiscountTimeView;
    @InjectView(id = R.id.courseTopics)
    private LinearLayout courseTopics;

    public CourseTrailerView(Context context) {
        this(context, null);
    }

    public CourseTrailerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CourseTrailerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setContentView(this, R.layout.view_course_trailer);
    }

    // 设置数据
    @Override
    public void initData(CourseDetailHelper mHelper) {
        super.initData(mHelper);
        initView();
    }

    // 绑定数据
    private void initView() {

        //2018.08.07 修改课程封面为1：1显示
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRlTrailer.getLayoutParams();
        params.height = Utils.getScreenWidth(getContext());
        mRlTrailer.setLayoutParams(params);

        if (!mHelper.isRefresh()) {
            setTopics();
            courseName.setText(mHelper.getCourse().getTitle());
            player.setVisibility(Utils.isNotEmpty(mHelper.getCourseTrailer().getVideoUrl()) ? VISIBLE : GONE);
            ImageUtil.displayImage(mTrailerCover, mHelper.getCourse().getCover_url(), R.drawable.blank_icon_bigimages);
        }

        // 设置开播倒计时和课程状态
        mDiscountTimeView.setStartLiveCountDownData(mHelper.getCourse(), mHelper.getCourseDetail().getBeginTime());
        // 设置折扣倒计时
        mDiscountTimeView.setDiscountTimeData(mHelper);
        // 设置课程价格
        coursePriceView.initData(mHelper.getCourse(), mHelper.getCourseTrailer().getSavedCoins(),
                mHelper.getCourseDetail().hasSimilarCourse(), mHelper.getCourseTrailer().getTime_limit_type());

    }

    // 设置话题
    private void setTopics() {

        // 显示话题
        if (Utils.isNotEmpty(mHelper.getCourseTrailer().getTopics())) {
            courseTopics.setVisibility(View.VISIBLE);
        } else {
            courseTopics.setVisibility(View.GONE);
            return;
        }

        // 添加话题
        for (final Topic topic : mHelper.getCourseTrailer().getTopics()) {

            TextView topicTv = new TextView(mContext);
            topicTv.setText("#" + topic.getName() + "#    ");
            topicTv.setMaxLines(1);
            topicTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            topicTv.setEllipsize(TextUtils.TruncateAt.END);
            topicTv.setTextColor(ResourceHelper.getColor(R.color.colorFF950B));
            topicTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AnalyticsReport.onEvent(mContext, AnalyticsReport.COURSE_DETAIL_PLAY_TOPICS_CLICK, mHelper.getEventParams());
                    TopicRoomListActivity.startActivity(mContext,
                            String.valueOf(topic.getId()), "#" + topic.getName() + "#  ", AnalyticsReport.HOME_VIDEO_VIEW_ID);
                }
            });

            courseTopics.addView(topicTv);
        }

    }

    //TODO 播放预告
    public void playTrailer() {
        // MyExoPlayer 播放
        try {
            if (Utils.isNotEmpty(mHelper.getCourseTrailer().getVideoUrl())) {
                AnalyticsReport.onEvent(mContext, AnalyticsReport.COURSE_DETAIL_PLAY_TRAILER_CLICK, mHelper.getEventParams());
                MyExoPlayerPlus.startFullscreen(mContext, MyExoPlayerPlus.class, mHelper.getCourseTrailer().getVideoUrl(), mHelper.getCourse().getTitle());
                Log.log(" 全屏播放地址=" + mHelper.getCourseTrailer().getVideoUrl() + " isShowBottomProgress=" /*+ MyVideoPlayer.isShowBottomProgress*/);
            }
        } catch (Exception e) {
            Log.log("--播放预告--e:" + e.toString());
        }

    }

    @Override

    public void onClick(View view) {
        super.onClick(view);
    }

    public void onDestroy() {
        if (mDiscountTimeView != null) {
            mDiscountTimeView.onDestroy();
        }
    }


}
