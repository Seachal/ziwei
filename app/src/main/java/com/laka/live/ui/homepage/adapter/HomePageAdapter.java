package com.laka.live.ui.homepage.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.laka.live.bean.HomeLiving;
import com.laka.live.bean.HomeRecommendTitle;
import com.laka.live.bean.HomeSeeAll;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.HomeBannersMsg;
import com.laka.live.msg.HomeFunctionMsg;
import com.laka.live.msg.HomeHotTopicsMsg;
import com.laka.live.ui.activity.UserInfoActivity;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.ui.course.detail.CourseDetailActivity;
import com.laka.live.ui.course.helper.CourseStatusHelper;
import com.laka.live.ui.fragment.BaseFragment;
import com.laka.live.ui.homepage.HotTopicsActivity;
import com.laka.live.ui.homepage.LivingViewHolder;
import com.laka.live.ui.widget.AnimationView;
import com.laka.live.ui.widget.BannerView;
import com.laka.live.ui.widget.HomeFunctionView;
import com.laka.live.ui.widget.PageListLayout;
import com.laka.live.ui.widget.TimeLimitTextView;
import com.laka.live.ui.widget.decoration.DividerItemDecoration;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.Util;
import com.laka.live.util.Utils;

/**
 * @ClassName: HomePageAdapter
 * @Description: 首页数据显示的适配器
 * @Author: chuan
 * @Version: 1.0
 * @Date: 5/27/17
 */

public class HomePageAdapter<T> extends BaseAdapter<T, BaseAdapter.ViewHolder> {
    private final static String TAG = HomePageAdapter.class.getSimpleName();

    private final static int TYPE_WRONG = -1;
    private final static int TYPE_BANNER = 0;
    private final static int TYPE_FUNCTION = 1;
    private final static int TYPE_LIVING_FIRST = 2;
    private final static int TYPE_LIVING_OTHER = 7;
    private final static int TYPE_TOPIC = 3;
    private final static int TYPE_RECOMMEND_TITLE = 4;
    private final static int TYPE_RECOMMEND = 5;
    private final static int TYPE_FOOTER = 6;

    private Context mContext;
    private BaseFragment mFragment;

    private BannerViewHolder bannerViewHolder;

    public HomePageAdapter(BaseFragment fragment) {
        this.mFragment = fragment;
        mContext = mFragment.getActivity();
    }


    @Override
    public int getItemViewType(int position) {
        Object o = getItem(position);
        if (o instanceof HomeBannersMsg) {
            return TYPE_BANNER;
        } else if (o instanceof HomeFunctionMsg) {
            return TYPE_FUNCTION;
        } else if (o instanceof HomeLiving) {
            if (((HomeLiving) o).isFirst()) {
                return TYPE_LIVING_FIRST;
            } else {
                return TYPE_LIVING_OTHER;
            }
        } else if (o instanceof HomeHotTopicsMsg) {
            return TYPE_TOPIC;
        } else if (o instanceof HomeRecommendTitle) {
            return TYPE_RECOMMEND_TITLE;
        } else if (o instanceof HomeSeeAll) {
            return TYPE_FOOTER;
        } else if (o instanceof Course) {
            return TYPE_RECOMMEND;
        } else {
            return TYPE_WRONG;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        T item = getItem(position);
        if (item instanceof HomeLiving) {
            holder.update(this, position, ((HomeLiving) item).getCourse());
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_BANNER:
                bannerViewHolder = new BannerViewHolder(new BannerView(mFragment.getContext()));
                return bannerViewHolder;
            case TYPE_FUNCTION:
                return new FunctionViewHolder(LayoutInflater
                        .from(mFragment.getContext()).inflate(R.layout.item_home_fuction, parent, false));
            case TYPE_LIVING_FIRST: // 正在直播
                return new LivingViewHolder(mFragment.getContext(), parent, LivingViewHolder.TYPE_HOME_LIVING);
            case TYPE_LIVING_OTHER:
                return new LivingViewHolder(mFragment.getContext(), parent, LivingViewHolder.TYPE_HOME_LIVING_OTHER);
            case TYPE_TOPIC:
                return new TopicViewHolder(LayoutInflater
                        .from(mFragment.getContext()).inflate(R.layout.item_home_topic, parent, false));
            case TYPE_RECOMMEND_TITLE:
                return new RecommendTitleViewHolder(LayoutInflater.
                        from(mFragment.getContext()).inflate(R.layout.item_home_recommend_title, parent, false));
            case TYPE_RECOMMEND:
                return new RecommendViewHolder(LayoutInflater
                        .from(mFragment.getContext()).inflate(R.layout.item_home_recommend, parent, false));
            case TYPE_FOOTER:
                return new FooterViewHolder(LayoutInflater
                        .from(mFragment.getContext()).inflate(R.layout.item_home_footer, parent, false));
            default:
                Log.e(TAG, "wrong type : " + viewType);
                throw new IllegalArgumentException("wrong type : " + viewType);
        }
    }

    /**
     * 设置不同item的布局参数，实现多样式显示
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();

        if (layoutParams != null
                && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            switch (holder.getItemViewType()) {
                case TYPE_BANNER:
                case TYPE_FUNCTION:
                case TYPE_LIVING_FIRST:
                case TYPE_LIVING_OTHER:
                case TYPE_TOPIC:
                case TYPE_RECOMMEND_TITLE:
                case TYPE_FOOTER:
                    ((StaggeredGridLayoutManager.LayoutParams) layoutParams).rightMargin = Utils.dip2px(mFragment.getContext(), 6);
                    // 当设置为true时，该项将使用所有跨度区域进行布局。这意味着，如果方向是垂直的，视图将具有全宽度；如果方向是水平的，视图将具有全高度。
                    ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
                    break;
                default:
                    ((StaggeredGridLayoutManager.LayoutParams) layoutParams).rightMargin = 0;
                    break;
            }
        }
    }

    /**
     * banner
     */
    private class BannerViewHolder extends BaseAdapter.ViewHolder<HomeBannersMsg> {

        private RecyclerView.LayoutParams params;

        BannerViewHolder(View itemView) {
            super(itemView);

            BannerView bannerView = (BannerView) itemView;

            params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    Utils.getScreenHeight(mFragment.getContext()) / 3);
            bannerView.setLayoutParams(params);
        }

        @Override
        public void update(BaseAdapter adapter, int position, HomeBannersMsg homeBannersMsg) {
            Log.d(TAG, "BannerViewHolder update : " + homeBannersMsg);

            if (homeBannersMsg == null || homeBannersMsg.isEmpty()) {
                return;
            }

            BannerView bannerView = (BannerView) itemView;
            bannerView.update(homeBannersMsg.getList());

        }

        public int getBannerHeight() {
            return params.height;
        }
    }

    /**
     * 功能
     */
    private class FunctionViewHolder extends BaseAdapter.ViewHolder<HomeFunctionMsg> {
        private HomeFunctionView mHomeFunctionView;

        FunctionViewHolder(View itemView) {
            super(itemView);

            mHomeFunctionView = (HomeFunctionView) itemView.findViewById(R.id.home_function_view);
        }

        @Override
        public void update(BaseAdapter adapter, int position, HomeFunctionMsg homeFunctionMsg) {
            Log.d(TAG, "FunctionViewHolder update : " + homeFunctionMsg);

            if (homeFunctionMsg == null || homeFunctionMsg.isEmpty()) {
                return;
            }

            mHomeFunctionView.updateData(homeFunctionMsg.getList());
        }

    }

    /**
     * 热门话题
     */
    private class TopicViewHolder extends BaseAdapter.ViewHolder<HomeHotTopicsMsg> implements View.OnClickListener {
        private PageListLayout mPageListLayout;
        //        private TopicItemAdapter mAdapter;
        private HomeTopicAdapter mAdapter;

        TopicViewHolder(View itemView) {
            super(itemView);
            mPageListLayout = itemView.findViewById(R.id.page_list_layout);
            mPageListLayout.setPullToRefresh(false);
            mPageListLayout.setEnableRefresh(false);
            mPageListLayout.setIsLoadMoreEnable(false);
            mPageListLayout.showFooter(false);
            mPageListLayout.setLayoutManager(new LinearLayoutManager(mFragment.getContext(),
                    LinearLayoutManager.HORIZONTAL, false));

            DividerItemDecoration decoration = new DividerItemDecoration(mContext)
                    .setOrientation(DividerItemDecoration.HORIZONTAL_LIST)
                    .setDividerHeight(Utils.dp2px(mContext, 10))
                    .setUseStartDivider(true);
            mPageListLayout.setItemDecoration(decoration);
            mAdapter = new HomeTopicAdapter(mFragment.getContext());
            mPageListLayout.setAdapter(mAdapter);

            itemView.findViewById(R.id.more_layout).setOnClickListener(this);

            TextView titleTv = (TextView) itemView.findViewById(R.id.title_tv);
            titleTv.setText(R.string.hot_topic);
        }

        @Override
        public void update(BaseAdapter adapter, int position, HomeHotTopicsMsg homeHotTopicsMsg) {
            Log.d(TAG, "TopicViewHolder update : " + homeHotTopicsMsg);

            if (homeHotTopicsMsg == null || homeHotTopicsMsg.isEmpty()) {
                return;
            }

            mAdapter.clear();
            mAdapter.addAll(homeHotTopicsMsg.getList());
            mPageListLayout.showData();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.more_layout:
                    AnalyticsReport.onEvent(mFragment.getContext(), AnalyticsReport.EVENT_10242);
                    HotTopicsActivity.startActivity(mFragment.getActivity());
                    break;
                default:
                    Log.d(TAG, "unhandled click : " + v);
                    break;
            }
        }
    }

    /**
     * 推荐课程的title
     */
    private class RecommendTitleViewHolder extends BaseAdapter.ViewHolder {
        RecommendTitleViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void update(BaseAdapter adapter, int position, Object o) {

        }
    }

    /**
     * 推荐课程
     */

    private class RecommendViewHolder extends BaseAdapter.ViewHolder<Course> implements View.OnClickListener {
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

        RecommendViewHolder(View itemView) {
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
//            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mThumbSdv.getLayoutParams();
//            params.weight = LinearLayout.LayoutParams.MATCH_PARENT;
//            params.height = (Utils.getScreenWidth(mFragment.getContext()) - Utils.dip2px(mFragment.getContext(), 5)) / 4;
//            mThumbSdv.setLayoutParams(params);
            mThumbSdv.setOnClickListener(this);
            mTitleTLTv.setOnClickListener(this);
            itemView.findViewById(R.id.time_info_ll).setOnClickListener(this);
            itemView.findViewById(R.id.user_info_ll).setOnClickListener(this);
            mCourseStatusHelper = new CourseStatusHelper();
        }

        @Override
        public void update(BaseAdapter adapter, int position, Course course) {
            this.mCourse = course;

            if (course == null) {
                return;
            }

            ImageUtil.loadImage(mThumbSdv, course.getCover_url());
            mTitleTLTv.setTimeLimitText(course);
            mCourseStatusHelper.setCourseStatus(course, mTimeTv, mStatusTv, mWaveView, mDivider);

            if (course.isLive()
                    && course.status != Course.CREATINGPLAYBACK
                    && course.status != Course.PLAYVIDEO
                    && course.status != Course.CREATEDPLAYBACK) {
                mLiveIv.setVisibility(View.VISIBLE);
            } else {
                mLiveIv.setVisibility(View.GONE);
            }

            if (course.getUser() != null) {
                ImageUtil.loadImage(mHeadSdv, course.getUser().getAvatar());
                mNicknameTv.setText(course.getUser().getNickname());
            } else {
                ImageUtil.loadImage(mHeadSdv, null);
                mNicknameTv.setText(null);
            }


        }

        @Override
        public void onClick(View v) {
            if (mCourse == null) {
                return;
            }

            switch (v.getId()) {
                case R.id.thumb_sdv: // 判断课程类型，跳转不同页面（直播间、课程回放、课程详情、web页面）
                    AnalyticsReport.onEvent(mFragment.getContext(), AnalyticsReport.EVENT_10243);
                    mCourse.onClickEvent(mFragment.getContext());
                    break;
                case R.id.title_tv:
                case R.id.time_info_ll: // 课程详情
                    AnalyticsReport.onEvent(mFragment.getContext(), AnalyticsReport.EVENT_10244);
                    CourseDetailActivity.startActivity(mFragment.getContext(), mCourse.getId());
                    break;
                case R.id.user_info_ll: // 用户详情
                    AnalyticsReport.onEvent(mFragment.getContext(), AnalyticsReport.EVENT_10245);
                    UserInfoActivity.startActivity(mFragment.getContext(), String.valueOf(mCourse.getUserId()));
                    break;
                default:
                    Log.d(TAG, "unhandled click . " + v);
                    break;
            }
        }

    }

    /**
     * 底部see all
     */
    private class FooterViewHolder extends BaseAdapter.ViewHolder implements View.OnClickListener {

        FooterViewHolder(View itemView) {
            super(itemView);

            itemView.findViewById(R.id.see_all_tv).setOnClickListener(this);
        }

        @Override
        public void update(BaseAdapter adapter, int position, Object o) {

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.see_all_tv:
                    AnalyticsReport.onEvent(mFragment.getContext(), AnalyticsReport.EVENT_10246);
                    EventBusManager.postEvent(Course.VIDEO, SubcriberTag.OPEN_COURSE_TAB);
                    break;
                default:
                    Log.d(TAG, "unhandled click : " + v);
                    break;
            }
        }
    }

    public int getBannerHeight() {
        if (bannerViewHolder == null) {
            return 0;
        }
        return bannerViewHolder.getBannerHeight();
    }
}
