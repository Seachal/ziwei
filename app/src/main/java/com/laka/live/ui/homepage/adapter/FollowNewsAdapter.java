package com.laka.live.ui.homepage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.FollowNews;
import com.laka.live.ui.activity.UserInfoActivity;
import com.laka.live.ui.activity.WebActivity;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.ui.course.detail.CourseDetailActivity;
import com.laka.live.ui.widget.PriceView;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.TimeUtil;
import com.laka.live.util.Utils;
import com.laka.live.player.MyExoPlayerPlus;

/**
 * @ClassName: FollowNewsAdapter
 * @Description: 关注动态
 * @Author: chuan
 * @Version: 1.0
 * @Date: 6/7/17
 */

public class FollowNewsAdapter extends BaseAdapter<FollowNews, FollowNewsAdapter.NewsViewHolder> {
    private final static String TAG = FollowNewsAdapter.class.getSimpleName();
    private Context mContext;

    public FollowNewsAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsViewHolder(LayoutInflater
                .from(mContext).inflate(R.layout.item_follow_news, parent, false));
    }

    class NewsViewHolder extends BaseAdapter.ViewHolder<FollowNews> implements View.OnClickListener {
        private SimpleDraweeView mHeadSdv;
        private TextView mNicknameTv;
        private TextView mPublishTv;
        private TextView mTimeTv;

        private LinearLayout mSmallLl;
        private SimpleDraweeView mThumbSmallSdv;
        private ImageView mPlayIv;
        private LinearLayout mSessionLl;
        private TextView mCountTv;
        private TextView mTitleSmallTv;
        private PriceView mPriceView;

        private LinearLayout mBigLl;
        private TextView mTitleBigTv;
        private SimpleDraweeView mThumbBigSdv;

        private FollowNews mFollowNews;

        NewsViewHolder(View itemView) {
            super(itemView);

            mHeadSdv = (SimpleDraweeView) itemView.findViewById(R.id.head_sdv);
            mNicknameTv = (TextView) itemView.findViewById(R.id.nickname_tv);
            mPublishTv = (TextView) itemView.findViewById(R.id.publish_tv);
            mTimeTv = (TextView) itemView.findViewById(R.id.time_tv);

            mSmallLl = (LinearLayout) itemView.findViewById(R.id.small_ll);
            mThumbSmallSdv = (SimpleDraweeView) itemView.findViewById(R.id.thumb_sdv);
            mPlayIv = (ImageView) itemView.findViewById(R.id.play_iv);
            mSessionLl = (LinearLayout) itemView.findViewById(R.id.session_ll);
            mCountTv = (TextView) itemView.findViewById(R.id.count_tv);
            mTitleSmallTv = (TextView) itemView.findViewById(R.id.title_tv);
            mPriceView = (PriceView) itemView.findViewById(R.id.price_view);

            mBigLl = (LinearLayout) itemView.findViewById(R.id.big_ll);
            mTitleBigTv = (TextView) itemView.findViewById(R.id.title_big_tv);
            mThumbBigSdv = (SimpleDraweeView) itemView.findViewById(R.id.thumb_big_sdv);

            mHeadSdv.setOnClickListener(this);
            mNicknameTv.setOnClickListener(this);
            mBigLl.setOnClickListener(this);
            mSmallLl.setOnClickListener(this);
            mThumbBigSdv.setOnClickListener(this);
            mThumbSmallSdv.setOnClickListener(this);
        }

        @Override
        public void update(BaseAdapter adapter, int position, FollowNews followNews) {
            this.mFollowNews = followNews;
            if (followNews == null) {
                return;
            }

            if (followNews.getUser() != null) {
                ImageUtil.loadImage(mHeadSdv, followNews.getUser().getAvatar());
                mNicknameTv.setText(followNews.getUser().getNickname());
            }

            mPublishTv.setText(ResourceHelper.getString(R.string.publish_course, followNews.getTypeStr()));

            switch (followNews.getType()) {
                case FollowNews.TYPE_VIDEO:
                case FollowNews.TYPE_LIVE:
                    if (followNews.getCourseTailer() == null) {
                        itemView.setVisibility(View.GONE);
                        return;
                    }

                    itemView.setVisibility(View.VISIBLE);
                    mSmallLl.setVisibility(View.VISIBLE);
                    mBigLl.setVisibility(View.GONE);

                    if (Utils.isEmpty(followNews.getCourseTailer().getVideoUrl())) {
                        mPlayIv.setVisibility(View.GONE);
                    } else {
                        mPlayIv.setVisibility(View.VISIBLE);
                    }

                    if (followNews.getCourseTailer().getNum() > 1) {
                        mSessionLl.setVisibility(View.VISIBLE);
                    } else {
                        mSessionLl.setVisibility(View.GONE);
                    }

                    mTimeTv.setText(TimeUtil.getTimeMax3(Long.parseLong(followNews.getCourseTailer().getCreateTime())));
                    ImageUtil.loadImage(mThumbSmallSdv, followNews.getCourseTailer().getCoverUrl());
                    mCountTv.setText(String.valueOf(followNews.getCourseTailer().getNum()));
                    mTitleSmallTv.setText(followNews.getCourseTailer().getTitle());
                    mPriceView.setPrice(followNews.getCourseTailer().getActualPrice(), followNews.getCourseTailer().getPrice());
                    break;

                default:
                    if (followNews.getNews() == null) {
                        itemView.setVisibility(View.GONE);
                        return;
                    }

                    itemView.setVisibility(View.VISIBLE);
                    mSmallLl.setVisibility(View.GONE);
                    mBigLl.setVisibility(View.VISIBLE);

                    mTimeTv.setText(TimeUtil.getTimeMax3(followNews.getNews().getCreateTime()));
                    ImageUtil.loadImage(mThumbBigSdv, followNews.getNews().getCoverUrl());
                    mTitleBigTv.setText(followNews.getNews().getTitle());
                    break;
            }

        }

        @Override
        public void onClick(View v) {
            if (mFollowNews == null) {
                return;
            }
            switch (v.getId()) {
                case R.id.head_sdv:
                case R.id.nickname_tv:
                    AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10254);
                    UserInfoActivity.startActivity(mContext, String.valueOf(mFollowNews.getUser().getId()));
                    break;
                case R.id.thumb_sdv:
                case R.id.thumb_big_sdv:
                    handleOnThumbClick();
                    break;
                case R.id.big_ll:
                case R.id.small_ll:
                    handleOnLayoutClick();
                    break;
                default:
                    Log.d(TAG, "unhandled click. " + v);
                    break;

            }
        }

        private void handleOnThumbClick() {
            AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10252);
            switch (mFollowNews.getType()) {
                case FollowNews.TYPE_LIVE:
                case FollowNews.TYPE_VIDEO:
                    if (!Utils.isEmpty(mFollowNews.getCourseTailer().getVideoUrl())) {

                        //TODO MyVideoPlayer 播放视频
                        MyExoPlayerPlus.playerVideo(mContext, mFollowNews.getCourseTailer().getVideoUrl(), mFollowNews.getCourseTailer().getTitle());

                    } else {
                        CourseDetailActivity.startActivity(mContext, String.valueOf(mFollowNews.getCourseTailer().getId()));
                    }
                    break;
                default:
                    if (!Utils.isEmpty(mFollowNews.getNews().getUrl())) {
                        WebActivity.startActivity(mContext, mFollowNews.getNews().getUrl(), mFollowNews.getNews().getTitle());
                    }
                    break;
            }
        }

        private void handleOnLayoutClick() {
            AnalyticsReport.onEvent(mContext, AnalyticsReport.EVENT_10253);
            switch (mFollowNews.getType()) {
                case FollowNews.TYPE_LIVE:
                case FollowNews.TYPE_VIDEO:
                    CourseDetailActivity.startActivity(mContext, String.valueOf(mFollowNews.getCourseTailer().getId()));
                    break;
                default:
                    if (!Utils.isEmpty(mFollowNews.getNews().getUrl())) {
                        WebActivity.startActivity(mContext, mFollowNews.getNews().getUrl(), mFollowNews.getNews().getTitle());
                    }
                    break;
            }
        }
    }
}
