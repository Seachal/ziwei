package com.laka.live.ui.adapter;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.Course;

import com.laka.live.shopping.bean.newversion.ShoppingHomeTopics;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.activity.UserInfoActivity;
import com.laka.live.ui.activity.WebActivity;
import com.laka.live.ui.course.detail.CourseDetailActivity;
import com.laka.live.ui.course.helper.CourseStatusHelper;
import com.laka.live.ui.widget.AnimationView;
import com.laka.live.ui.widget.TimeLimitTextView;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.TimeUtil;
import com.laka.live.util.Utils;

import java.util.List;

public class BestTopicsAdapter<T> extends BaseAdapter<T, BaseAdapter.ViewHolder> {

    private final static int TYPE_WRONG = -1;
    private final static int TYPE_COURSE = 2;
    private final static int TYPE_TOPIC = 4;

    private Activity mContext;
    private ShoppingHomeTopics bestTopicBean;

    public BestTopicsAdapter(BaseActivity mContext) {
        this.mContext = mContext;
    }

    public void setBestTopicBean(ShoppingHomeTopics bestTopicBean) {
        this.bestTopicBean = bestTopicBean;
    }

    public void setData(List<T> data) {
        this.mDatas = data;
    }

    @Override
    public int getItemViewType(int position) {
        T data = getItem(position);
        if (data instanceof ShoppingHomeTopics) {
            return TYPE_TOPIC;
        } else if (data instanceof Course) {
            return TYPE_COURSE;
        } else {
            return TYPE_WRONG;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_COURSE:
                return new ContentViewHolder(LayoutInflater
                        .from(mContext).inflate(R.layout.item_topic_course, parent, false));
            case TYPE_TOPIC:
                return new TopicViewHolder(
                        LayoutInflater.from(mContext).inflate(R.layout.item_shopping_home_topic, null));
            default:
                throw new IllegalArgumentException();
        }
    }

    //话题
    private class TopicViewHolder extends ViewHolder<ShoppingHomeTopics>
            implements View.OnClickListener {
        private SimpleDraweeView mThumbSdv;
        private TextView mTitleTv;

        private ShoppingHomeTopics mTopic;

        public TopicViewHolder(View itemView) {
            super(itemView);

            mThumbSdv = (SimpleDraweeView) itemView.findViewById(R.id.thumb_sdv);
            mTitleTv = (TextView) itemView.findViewById(R.id.title_tv);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mThumbSdv.getLayoutParams();
            params.height = Utils.getScreenWidth(mContext) * 4 / 7;
            mThumbSdv.setLayoutParams(params);
            mTitleTv.setVisibility(View.GONE);
            mThumbSdv.setOnClickListener(this);
        }

        @Override
        public void update(BaseAdapter adapter, int position, ShoppingHomeTopics topics) {
            ImageUtil.loadImage(mThumbSdv, bestTopicBean.getCoverUrl());
        }

        @Override
        public void onClick(View v) {
            // 点击专题图片事件
        }
    }

    private class ContentViewHolder extends ViewHolder<Course> implements View.OnClickListener {

        private SimpleDraweeView mThumbSdv;
        private TimeLimitTextView mTitleTLTv;
        private TextView mStatusTv;
        private Chronometer mTimeTv;
        private SimpleDraweeView mHeadSdv;
        private TextView mNicknameTv;
        private AnimationView mWaveView;
        private ImageView mLiveIv;
        private Course mCourse;
        private View mDivider;
        private CourseStatusHelper mCourseStatusHelper;

        ContentViewHolder(View itemView) {
            super(itemView);

            mThumbSdv = (SimpleDraweeView) itemView.findViewById(R.id.thumb_sdv);
            mTitleTLTv = (TimeLimitTextView) itemView.findViewById(R.id.title_tv);
            mStatusTv = (TextView) itemView.findViewById(R.id.time_flag_tv);
            mTimeTv = (Chronometer) itemView.findViewById(R.id.time_tv);
            mHeadSdv = (SimpleDraweeView) itemView.findViewById(R.id.head_sdv);
            mNicknameTv = (TextView) itemView.findViewById(R.id.nickname_tv);
            mWaveView = (AnimationView) itemView.findViewById(R.id.living_ware_view);
            mLiveIv = (ImageView) itemView.findViewById(R.id.live_iv);
            mDivider = itemView.findViewById(R.id.status_divider);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mThumbSdv.getLayoutParams();
//            params.weight = LinearLayout.LayoutParams.MATCH_PARENT;
//            params.height = (Utils.getScreenWidth(mContext) - Utils.dip2px(mContext, 5)) / 4;
            params.dimensionRatio = "1:1";
            mThumbSdv.setLayoutParams(params);
            mThumbSdv.setOnClickListener(this);
            mTitleTLTv.setOnClickListener(this);
            itemView.findViewById(R.id.time_info_ll).setOnClickListener(this);
            itemView.findViewById(R.id.user_info_ll).setOnClickListener(this);
            mCourseStatusHelper = new CourseStatusHelper();
        }

        @Override
        public void update(BaseAdapter adapter, int position, Course course) {

            mCourse = course;

            if (mCourse == null) {
                return;
            }

            // 统一设置课程的时间和状态(兼容所有带有复用的列表)
            mCourseStatusHelper.setCourseStatus(course, mTimeTv, mStatusTv, mWaveView, mDivider);
            // 显示封面
            ImageUtil.loadImage(mThumbSdv, mCourse.getCover_url());
            // 显示标题,不足两行占两行的高度，不然会导致item的高度不一致
            // 这里刷新会引起问题= =
            if (!TextUtils.isEmpty(course.getTitle())) {
                mTitleTLTv.setVisibility(View.VISIBLE);
                mTitleTLTv.setTimeLimitText(mCourse);
                mTitleTLTv.post(new Runnable() {
                    @Override
                    public void run() {
                        int txtPart = mTitleTLTv.getLineCount();
                        if (txtPart == 1) {
                            mTitleTLTv.append("\n");
                        }
                    }
                });
            } else {
                mTitleTLTv.setVisibility(View.GONE);
            }
            // 显示用户信息
            if (mCourse.getUser() != null) {
                ImageUtil.loadImage(mHeadSdv, mCourse.getUser().getAvatar());
                mNicknameTv.setText(mCourse.getUser().getNickname());
            } else {
                ImageUtil.loadImage(mHeadSdv, null);
                mNicknameTv.setText(null);
            }

            // 未开播与直播中才要显示Live标签
            if (mCourse.isLive() && mCourse.status <= Course.LIVING && mCourse.status != Course.CANCEL) {
                mLiveIv.setVisibility(View.VISIBLE);
            } else {
                mLiveIv.setVisibility(View.GONE);
            }

        }

        @Override
        public void onClick(View v) {
            if (mCourse == null) {
                return;
            }

            switch (v.getId()) {
                case R.id.thumb_sdv:
                    AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10243);
                    mCourse.onClickEvent(mContext);
                    break;
                case R.id.title_tv:
                case R.id.time_info_ll:
                    AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10244);
                    if (mCourse.isNews()) {
                        WebActivity.startActivity(mContext, mCourse.getNewsUrl(), mCourse.getTitle());
                    } else {
                        CourseDetailActivity.startActivity(mContext, mCourse.getId());
                    }
                    break;
                case R.id.user_info_ll:
                    AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10245);
                    UserInfoActivity.startActivity(mContext, String.valueOf(mCourse.getUserId()));
                    break;
                default:
                    Log.log("unhandled click . " + v);
                    break;
            }
        }
    }

    @Override
    public void clear() {
        super.clear();
        mDatas.add((T) bestTopicBean);
    }

}
