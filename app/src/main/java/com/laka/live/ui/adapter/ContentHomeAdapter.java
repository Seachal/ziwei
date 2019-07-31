package com.laka.live.ui.adapter;

import android.app.Activity;
import android.graphics.Rect;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
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
import com.laka.live.bean.Content;
import com.laka.live.bean.ContentHomeTitleBean;
import com.laka.live.bean.Course;
import com.laka.live.bean.CourseCategoryOneBean;
import com.laka.live.msg.CourseCategoryOneMsg;
import com.laka.live.pop.CourseCategoryPop;
import com.laka.live.shopping.bean.newversion.ShoppingHomeTopics;
import com.laka.live.ui.activity.UserInfoActivity;
import com.laka.live.ui.activity.WebActivity;
import com.laka.live.ui.course.BestTopicsActivity;
import com.laka.live.ui.course.CourseClassifyActivity;
import com.laka.live.ui.course.NewestCoursesActivity;
import com.laka.live.ui.course.detail.CourseDetailActivity;
import com.laka.live.ui.course.helper.CourseStatusHelper;
import com.laka.live.ui.widget.AnimationView;
import com.laka.live.ui.widget.MoreBtnView;
import com.laka.live.ui.widget.ScrollRecyclerView;
import com.laka.live.ui.widget.TimeLimitTextView;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import framework.ioc.ContentView;

/**
 * 内容页数据适配器
 * @param <T>
 */
public class ContentHomeAdapter<T> extends BaseAdapter<T, BaseAdapter.ViewHolder> {

    public final static int TYPE_WRONG = -1;
    public final static int TYPE_TITLE = 0;
    public final static int TYPE_CATE = 1;
    public final static int TYPE_COURSE = 2;
    public static final int TYPE_NEWS = 3;
    public final static int TYPE_TOPIC = 4;

    private RecyclerView mRecyclerView;
    private Activity mContext;
    private CourseCategoryPop mClassifyPop;

    public ContentHomeAdapter(Activity context) {
        this.mContext = context;
    }

    public void setData(List<T> datas) {
        this.mDatas = datas;
    }


    @Override
    public int getItemViewType(int position) {
        T data = getItem(position);
        if (data instanceof CourseCategoryOneMsg) {
            return TYPE_CATE;
        } else if (data instanceof ContentHomeTitleBean) {
            return TYPE_TITLE;
        } else if (data instanceof Content) {
            if (((Content) data).getType() == Course.NEWS) { // 最新资讯
                return TYPE_NEWS;
                } else if (((Content) data).getTopicId() == -1) {
                return TYPE_COURSE;
            } else {
                return TYPE_TOPIC;
            }
        } else {
            return TYPE_WRONG;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_CATE:  // 分类
                return new ClassifyViewHolder(
                        LayoutInflater.from(mContext).inflate(R.layout.item_content_classify, parent, false));
            case TYPE_TITLE:  // 标题
                return new TitleViewHolder(
                        LayoutInflater.from(mContext).inflate(R.layout.item_shopping_home_title, null, false));
            case TYPE_COURSE:
                return new ContentViewHolder(LayoutInflater
                        .from(mContext).inflate(R.layout.item_home_content, parent, false));
            case TYPE_NEWS: // 最新
                return new ContentViewHolder(LayoutInflater
                        .from(mContext).inflate(R.layout.item_home_content, parent, false), true);
            case TYPE_TOPIC: // 专题
                return new TopicViewHolder(
                        LayoutInflater.from(mContext).inflate(R.layout.item_shopping_home_topic, null));
            default:
                throw new IllegalArgumentException();
        }
    }

    public void bindRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    // 分类
    private class ClassifyViewHolder extends ViewHolder<CourseCategoryOneMsg> {

        private View divider;
        private MoreBtnView moreBtnView;
        private List<CourseCategoryOneBean> mCateList;
        private ScrollRecyclerView mCategoryRecycler;
        private CourseCategoryOneAdapter mCourseCategoryOneAdapter;

        public ClassifyViewHolder(final View itemView) {
            super(itemView);
            mCateList = new ArrayList<>();
            moreBtnView = itemView.findViewById(R.id.more);
            divider = itemView.findViewById(R.id.divider);
            itemView.findViewById(R.id.more);
            mCategoryRecycler = itemView.findViewById(R.id.category);
            mCategoryRecycler.addItemDecoration(new SpaceItemDecoration(mCateList, moreBtnView));
            mCourseCategoryOneAdapter = new CourseCategoryOneAdapter(mContext);
            mCourseCategoryOneAdapter.setmDatas(mCateList);
            mCategoryRecycler.setAdapter(mCourseCategoryOneAdapter);
            mCourseCategoryOneAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    HashMap<String, String> mEventParams = new HashMap<>();
                    mEventParams.put("ID", String.valueOf(mCateList.get(position).getId()));
                    AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10228, mEventParams);
                    CourseClassifyActivity.startActivity(mContext, mCateList.get(position).getName(), mCateList.get(position).getId());
                }
            });

            moreBtnView.setVisibility(View.GONE);
            moreBtnView.post(new Runnable() {
                @Override
                public void run() {
                    moreBtnView.setTag(moreBtnView.getMeasuredWidth());
                }
            });
            moreBtnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClassifyPop.showAsDropDown(divider);
                }
            });

        }

        @Override
        public void update(BaseAdapter adapter, int position, CourseCategoryOneMsg categoryOneMsg) {

            if (categoryOneMsg != null && categoryOneMsg.getCategories() != null) {
                mCateList.clear();
                mCateList.addAll(categoryOneMsg.getCategories());
                mClassifyPop = CourseCategoryPop.getCourseClassifyOnePop(mContext, mCateList);
                mClassifyPop.setOnVisibleChangedListener(moreBtnView);
                moreBtnView.setVisibility(View.VISIBLE);
                mCourseCategoryOneAdapter.notifyDataSetChanged();
            }

        }

        private class SpaceItemDecoration extends RecyclerView.ItemDecoration {

            private View moreBtnView;
            private List<CourseCategoryOneBean> mData;

            public SpaceItemDecoration(List<CourseCategoryOneBean> mData, View moreBtnView) {
                this.mData = mData;
                this.moreBtnView = moreBtnView;
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);

                int pos = parent.getChildAdapterPosition(view);

                outRect.right = 0;
                outRect.top = 0;
                outRect.bottom = 0;
                outRect.left = 0;

                if (mData.size() - 1 == pos) {
                    outRect.right = (moreBtnView.getTag() == null) ? ResourceHelper.getDimen(R.dimen.space_25) : (int) moreBtnView.getTag();
                }

            }
        }

    }

    //标题
    private class TitleViewHolder extends ViewHolder<ContentHomeTitleBean> {

        private TextView mMoreTv;
        private TextView mTitleTv;
        private ImageView mIconIv;

        TitleViewHolder(View itemView) {
            super(itemView);
            mMoreTv = (TextView) itemView.findViewById(R.id.more_tv);
            mTitleTv = (TextView) itemView.findViewById(R.id.title_tv);
            mIconIv = (ImageView) itemView.findViewById(R.id.icon_iv);
            mMoreTv.setVisibility(View.VISIBLE);
        }

        @Override
        public void update(BaseAdapter adapter, int position, final ContentHomeTitleBean contentHomeTitleBean) {
            mTitleTv.setText(contentHomeTitleBean.getTitle());
            mIconIv.setImageDrawable(ResourceHelper.getDrawable(R.drawable.mall_icon_commodity));
            if (contentHomeTitleBean.getType() == ContentHomeTitleBean.TYPE_TOPICS) {
                mMoreTv.setVisibility(View.GONE);
            } else {
                mMoreTv.setVisibility(View.VISIBLE);
            }
            mMoreTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (contentHomeTitleBean.getType() == Course.LIVE) {
                        AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10258);
                    } else if (contentHomeTitleBean.getType() == Course.VIDEO) {
                        AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10261);
                    } else {
                        AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10264);
                    }
                    // 最新直播、视频、资讯 列表页
                    NewestCoursesActivity.startActivity(mContext, contentHomeTitleBean.getType());
                }
            });
            mMoreTv.setPadding(mMoreTv.getPaddingLeft(), mMoreTv.getPaddingTop(), ResourceHelper.getDimen(R.dimen.space_6), mMoreTv.getPaddingBottom());
        }

    }

    //话题
    private class TopicViewHolder extends ViewHolder<Content>
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

            mTitleTv.setOnClickListener(this);
            mThumbSdv.setOnClickListener(this);
        }

        @Override
        public void update(BaseAdapter adapter, int position, Content content) {
            mTopic = content.getTopics();
            ImageUtil.loadImage(mThumbSdv, mTopic.getCoverUrl());
            mTitleTv.setText(mTopic.getTitle());
            mTitleTv.setPadding(mTitleTv.getPaddingLeft(), mTitleTv.getPaddingTop(), ResourceHelper.getDimen(R.dimen.space_6), mTitleTv.getPaddingBottom());
        }

        @Override
        public void onClick(View v) {
            if (mTopic != null) {
                AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10265);
                BestTopicsActivity.startActivity(mContext, mTopic.getTopicId());
            }
        }
    }

    private class ContentViewHolder extends BaseAdapter.ViewHolder<Content> implements View.OnClickListener {
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
        private int titleHeight = -1;
        private float titleTextMeasureWidth = -1;

        ContentViewHolder(View itemView) {
            this(itemView, false);
        }

        ContentViewHolder(View itemView, boolean isNews) {
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
//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mThumbSdv.getLayoutParams();
//            params.weight = LinearLayout.LayoutParams.MATCH_PARENT;
//            params.height = (Utils.getScreenWidth(mContext) - Utils.dip2px(mContext, 5)) / 4;
//            mThumbSdv.setLayoutParams(params);
            mThumbSdv.setOnClickListener(this);
            mTitleTLTv.setOnClickListener(this);
            itemView.findViewById(R.id.time_info_ll).setOnClickListener(this);
            itemView.findViewById(R.id.user_info_ll).setOnClickListener(this);
            mCourseStatusHelper = new CourseStatusHelper();

            //新改动，资讯保留2：1，其他改成1：1
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mThumbSdv.getLayoutParams();
            if (isNews) {
                params.dimensionRatio = "2:1";
            } else {
                params.dimensionRatio = "1:1";
            }
            mThumbSdv.setLayoutParams(params);
        }

        @Override
        public void update(BaseAdapter adapter, int position, Content content) {
            mCourse = content.getCourse();

            if (mCourse == null) {
                return;
            }

            ImageUtil.loadImage(mThumbSdv, mCourse.getCover_url());
            mTitleTLTv.setTimeLimitText(mCourse);
            //之前想实现根据内容的大小实现Item高度的统一，但是发现复用问题不能解决，只能xml限定死Title的高度
//            mTitleTLTv.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//            titleHeight = mTitleTLTv.getLayoutParams().height;
//            titleHeight = mTitleTLTv.getMeasuredHeight();
//            titleTextMeasureWidth = mTitleTLTv.getMeasureTextWidth();
//            Logger.e("输出Position:" + position + mCourse);
//            if (position % 2 == 1 && mRecyclerView != null) {
//                //与前一个ViewHolder里面的TitleView比较，存在高度最大值就设置两者为高度最大值，不存在就默认显示
//                Logger.e("基数Item：" + position);
//                for (int i = 0; i < mRecyclerView.getChildCount(); i++) {
//                    View childView = mRecyclerView.getChildAt(i);
//                    if (mRecyclerView.getChildViewHolder(childView) instanceof ContentHomeAdapter.ContentViewHolder) {
//                        Logger.e("输出ContentViewHolder的position：" + i);
//                        ContentViewHolder childViewHolder = (ContentViewHolder) mRecyclerView.getChildViewHolder(childView);
//                        Logger.e("输出当前Item的titleBar高度：" + titleHeight + "\n前一个Item的titleBar高度：" + childViewHolder.getTitleHeight()
//                                + "\nText内容：" + mTitleTLTv.getText() + "\n前一个Item的内容：" + childViewHolder.getTitleText()
//                                + "\nText内容的宽度：" + titleTextMeasureWidth + "\n前一个Item的内容宽度：" + childViewHolder.getTitleMeasureTextWidth()
//                                + "\n输出屏幕宽度：" + Utils.getScreenWidth(mContext));
//                        if (childViewHolder.getTitleHeight() > titleHeight) {
//                            setTitleViewHeight(childViewHolder.getTitleHeight());
//                        } else if (titleHeight > childViewHolder.getTitleHeight()) {
//                            childViewHolder.setTitleViewHeight(titleHeight);
//                        }
//                    }
//                }
//            }
            mCourseStatusHelper.setCourseStatus(mCourse, mTimeTv, mStatusTv, mWaveView, mDivider);

            if (mCourse.getUser() != null) {
                ImageUtil.loadImage(mHeadSdv, mCourse.getUser().getAvatar());
                mNicknameTv.setText(mCourse.getUser().getNickname());
            } else {
                ImageUtil.loadImage(mHeadSdv, null);
                mNicknameTv.setText(null);
            }

            if (mCourse.isLive()
                    && mCourse.status != Course.CREATINGPLAYBACK
                    && mCourse.status != Course.PLAYVIDEO
                    && mCourse.status != Course.CREATEDPLAYBACK) {
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
                    if (mCourse.isLive()) {
                        AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10256);
                    } else if (mCourse.isVideo()) {
                        AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10259);
                    } else {
                        AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10262);
                    }
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
                    if (mCourse.isLive()) {
                        AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10257);
                    } else if (mCourse.isVideo()) {
                        AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10260);
                    } else {
                        AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10263);
                    }
                    UserInfoActivity.startActivity(mContext, String.valueOf(mCourse.getUserId()));
                    break;
                default:
                    Log.log("unhandled click . " + v);
                    break;
            }
        }

        public int getTitleHeight() {
            return titleHeight;
        }

        public void setTitleViewHeight(int height) {
            mTitleTLTv.getLayoutParams().height = height;
        }

        public float getTitleMeasureTextWidth() {
            return titleTextMeasureWidth;
        }

        public String getTitleText() {
            return mTitleTLTv.getText().toString();
        }
    }
}
