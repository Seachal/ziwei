package com.laka.live.ui.homepage;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.Course;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.ui.activity.UserInfoActivity;
import com.laka.live.ui.activity.WebActivity;
import com.laka.live.ui.course.detail.CourseDetailActivity;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.ui.course.helper.CourseStatusHelper;
import com.laka.live.ui.widget.AnimationView;
import com.laka.live.ui.widget.PriceView;
import com.laka.live.ui.widget.TimeLimitTextView;
import com.laka.live.util.Common;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.TimeUtil;
import com.laka.live.util.Utils;

/**
 * 正在直播 viewHolder
 */
public class LivingViewHolder extends BaseAdapter.ViewHolder<Course> implements View.OnClickListener {
    private final static String TAG = LivingViewHolder.class.getSimpleName();

    public final static int TYPE_HOME_LIVING = 10;
    public final static int TYPE_HOME_LIVING_OTHER = 22;
    public final static int TYPE_FREE_SESSION = 11;
    public final static int TYPE_TOPIC_DETAIL = 12;

    private TextView mLivingTitleTv;
    private SimpleDraweeView mThumbSdv;
    private TimeLimitTextView mTitleTv;
    private TextView mStatusTv;
    private Chronometer mTimeTv;
    //    private CourseTagView mTagView;
    private AnimationView mWaveView;
    private SimpleDraweeView mHeadSdv;
    private TextView mNickNameTv;
    private TextView mBuyCountTv;
    private PriceView mPriceView;
    private ImageView mLiveIv;

    private Context mContext;
    private int mType;
    private Course mCourse;
    private View mDivider;
    private View topDivider;
    private boolean isShowTopDivider = true; // 第一个item是否要显示分割线,默认是显示
    private CourseStatusHelper mCourseStatusHelper;

    private LivingViewHolder(View itemView) {
        super(itemView);
    }

    public LivingViewHolder(Context context, ViewGroup parent, int type, boolean isShowTopDivider) {
        this(context, parent, type);
        this.isShowTopDivider = isShowTopDivider;
    }

    public LivingViewHolder(Context context, ViewGroup parent, int type) {
        this(LayoutInflater.from(context).inflate(R.layout.item_home_living, parent, false));

        this.mType = type;
        this.mContext = context;

        mThumbSdv = (SimpleDraweeView) itemView.findViewById(R.id.thumb_sdv);
        mTitleTv = (TimeLimitTextView) itemView.findViewById(R.id.title_tv);
        mStatusTv = (TextView) itemView.findViewById(R.id.time_flag_tv);
        mTimeTv = (Chronometer) itemView.findViewById(R.id.time_tv);
        mWaveView = (AnimationView) itemView.findViewById(R.id.living_ware_view);
//        mTagView = (CourseTagView) itemView.findViewById(R.id.tag_ctv);
        mHeadSdv = (SimpleDraweeView) itemView.findViewById(R.id.head_sdv);
        mNickNameTv = (TextView) itemView.findViewById(R.id.nickname_tv);
        mBuyCountTv = (TextView) itemView.findViewById(R.id.buyer_count_tv);
        mLivingTitleTv = (TextView) itemView.findViewById(R.id.living_tv);
//        View priceTagView = itemView.findViewById(R.id.price_tag_view);
        topDivider = itemView.findViewById(R.id.topDivider);
        mPriceView = (PriceView) itemView.findViewById(R.id.price_view);
        mDivider = itemView.findViewById(R.id.tv_time_divide);

        mLiveIv = (ImageView) itemView.findViewById(R.id.live_iv);

        RelativeLayout mLivingRl = (RelativeLayout) itemView.findViewById(R.id.living_rl);
        View mSessionLl = itemView.findViewById(R.id.session_ll);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mThumbSdv.getLayoutParams();
        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
        params.height = (Utils.getScreenWidth(mContext) - Utils.dip2px(mContext, 30)) / 2;
        mThumbSdv.setLayoutParams(params);

        switch (type) {
            case TYPE_HOME_LIVING:
                mLivingRl.setVisibility(View.VISIBLE);
                mSessionLl.setVisibility(View.GONE);
//                mTagView.setVisibility(View.GONE);
                itemView.findViewById(R.id.more_layout).setOnClickListener(this);
                break;
            case TYPE_HOME_LIVING_OTHER:
                mSessionLl.setVisibility(View.VISIBLE);
                break;
            case Course.LIVE:
            case Course.VIDEO:
                mSessionLl.setVisibility(View.VISIBLE);
                break;
            case Course.NEWS:
                mSessionLl.setVisibility(View.VISIBLE);
                mPriceView.setVisibility(View.GONE);
                mBuyCountTv.setVisibility(View.GONE);
                break;
            case TYPE_TOPIC_DETAIL:
            default:
                mLivingRl.setVisibility(View.GONE);
                mSessionLl.setVisibility(View.VISIBLE);
                break;
        }

        mThumbSdv.setOnClickListener(this);
        mTitleTv.setOnClickListener(this);
//        priceTagView.setOnClickListener(this);
        itemView.findViewById(R.id.user_info_rl).setOnClickListener(this);
        itemView.findViewById(R.id.time_info_ll).setOnClickListener(this);
        mCourseStatusHelper = new CourseStatusHelper();

    }

    @Override
    public void update(BaseAdapter adapter, int position, Course course) {
        mCourse = course;
        if (course == null) {
            return;
        }

        if (!isShowTopDivider && position == 0) {
            topDivider.setVisibility(View.GONE);
        } else {
            topDivider.setVisibility(View.VISIBLE);
        }

        if (mType == Course.NEWS) {
            setNews(position, course);
        } else {
            setCourse(position, course);
        }

    }

    // 设置课程
    private void setCourse(int position, Course course) {

        ImageUtil.loadImage(mThumbSdv, course.cover_url);

        // 统一设置课程的时间和状态(兼容所有带有复用的列表)
        mCourseStatusHelper.setCourseStatus(course, mTimeTv, mStatusTv, mWaveView, mDivider);
        // 显示课程标题
        mTitleTv.setTimeLimitText(course);
        // 不是直播中的显示成即将开始,直播中的显示成正在直播
        if (mCourse.getStatus() != Course.LIVING) {
            mLivingTitleTv.setText(R.string.pre_start);
        } else {
            mLivingTitleTv.setText(R.string.living);
        }
        //mTagView.updateData(course.tags); 显示tag标签
        mPriceView.setPrice(course.getPrice());
        if (course.getUser() != null) {
            ImageUtil.loadImage(mHeadSdv, course.getUser().avatar);
            mNickNameTv.setText(course.getUser().getNickname());
        } else {
            ImageUtil.loadImage(mHeadSdv, null);
            mNickNameTv.setText(null);
        }
        mBuyCountTv.setText(course.getBuyer_count());
        if (course.isLive()
                && course.status != Course.CREATINGPLAYBACK
                && course.status != Course.PLAYVIDEO
                && course.status != Course.CREATEDPLAYBACK) {
            mLiveIv.setVisibility(View.VISIBLE);
        } else {
            mLiveIv.setVisibility(View.GONE);
        }
    }

    // 设置资讯
    private void setNews(int position, Course course) {

        mTitleTv.setText(course.title);
        ImageUtil.loadImage(mThumbSdv, course.cover_url);

        if (course.getUser() != null) {
            ImageUtil.loadImage(mHeadSdv, course.getUser().avatar);
        }

        mLivingTitleTv.setText(R.string.push_time);
        mStatusTv.setText(R.string.push_time);
        mTimeTv.setText(course.getFormatStartTime());
        mWaveView.setVisibility(View.GONE);
        mNickNameTv.setText(course.getUser().getNickname());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_info_rl:
                if (mCourse == null) {
                    return;
                }

                switch (mType) {
                    case TYPE_HOME_LIVING_OTHER:
                    case TYPE_HOME_LIVING:
                        AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10239);
                        break;
                    case TYPE_FREE_SESSION:
                        AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10249);
                        break;
                    case Course.NEWS:
                        AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10251);
                        break;
                    default:
                        break;
                }

                UserInfoActivity.startActivity(mContext, String.valueOf(mCourse.getUserId()));
                break;
            case R.id.thumb_sdv:
                if (mCourse == null) {
                    return;
                }

                switch (mType) {
                    case Course.NEWS:
                        AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10250);
                        //跳转到WebActivity
//                        String modifyUrl = "";
//                        if (!TextUtils.isEmpty(mCourse.getNewsUrl()) && mCourse.getNewsUrl().contains("https://mp.weixin.qq.com")) {
//                            String wxUrlPath = mCourse.getNewsUrl().substring("https://mp.weixin.qq.com".length());
//                            modifyUrl = "http://www.lakatv.com/ziwei/wx_share_cs/share_mobile_temporary.html?zxsrc="
//                                    + wxUrlPath;
//                        }
//                        WebActivity.startActivity(mContext, mCourse.getNewsUrl(), modifyUrl, mCourse.getTitle());
                        WebActivity.startActivity(mContext, mCourse.getNewsUrl(), mCourse.getTitle());
                        break;
                    case TYPE_HOME_LIVING_OTHER:
                    case TYPE_HOME_LIVING:
                        AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10237);
                        mCourse.onClickEvent(mContext);
                        break;
                    case TYPE_FREE_SESSION:
                        AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10247);
                        mCourse.onClickEvent(mContext);
                        break;
                    default:
                        mCourse.onClickEvent(mContext);
                        break;

                }
                break;
            case R.id.title_tv:
            case R.id.time_info_ll:
            case R.id.price_view:
                if (mCourse == null) {
                    return;
                }
                switch (mType) {
                    case Course.NEWS:
                        String modifyUrl = "";
//                        if (!TextUtils.isEmpty(mCourse.getNewsUrl()) && mCourse.getNewsUrl().contains("https://mp.weixin.qq.com")) {
//                            String wxUrlPath = mCourse.getNewsUrl().substring("https://mp.weixin.qq.com".length());
//                            modifyUrl = "http://www.lakatv.com/ziwei/wx_share_cs/share_mobile_temporary.html?zxsrc="
//                                    + wxUrlPath;
//                        }
//                        WebActivity.startActivity(mContext, mCourse.getNewsUrl(), modifyUrl, mCourse.getTitle());
                        WebActivity.startActivity(mContext, mCourse.getNewsUrl(), mCourse.getTitle());
                        break;
                    case TYPE_HOME_LIVING_OTHER:
                    case TYPE_HOME_LIVING:
                        AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10238);
                        CourseDetailActivity.startActivity(mContext, mCourse.getId());
                        break;
                    case TYPE_FREE_SESSION:
                        AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10248);
                        CourseDetailActivity.startActivity(mContext, mCourse.getId());
                        break;
                    default:
                        CourseDetailActivity.startActivity(mContext, mCourse.getId());
                        break;

                }
                break;
            case R.id.more_layout:
                AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10240);
                EventBusManager.postEvent(Course.LIVE, SubcriberTag.OPEN_COURSE_TAB);
                break;
            default:
                Log.d(TAG, "unhandled click : " + v);
                break;
        }
    }
}