package com.laka.live.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.laka.live.bean.Banner;
import com.laka.live.ui.homepage.LivingViewHolder;
import com.laka.live.ui.widget.BannerView;
import com.laka.live.util.Utils;

import java.util.List;


public class CourseAdapter extends BaseAdapter<Object, BaseAdapter.ViewHolder> {

    public static final String FROM_HOT = "from_hot";

    public static final String FROM_FOUND = "from_found";

    public static final String FROM_FRIEND = "from_friend";

    public final static int TYPE_HEAD = 1;

    public final static int TYPE_ITEM = 2;

    private List<Banner> mBanners;

    private ViewGroup mSwipeRefreshLayout;

    private boolean isEnableTagClick = true;
    private boolean isFromTopicDetail = false; // 是否从话题详情列表进来

    private Context context;
    private int mType;
    private boolean isShowTopDivider = true;

    public CourseAdapter(Context context, ViewGroup swipeRefreshLayout, int mType) {
        this.mType = mType;
        this.context = context;
        this.mSwipeRefreshLayout = swipeRefreshLayout;
    }

    public CourseAdapter(Context context, ViewGroup swipeRefreshLayout, boolean isFromTopicDetail) {
        this.context = context;
        this.isFromTopicDetail = isFromTopicDetail;
        this.mSwipeRefreshLayout = swipeRefreshLayout;
    }

    public void setIsEnableTagClick(boolean isEnableTagClick) {
        this.isEnableTagClick = isEnableTagClick;
    }

    public void setBanners(List<Banner> banners) {
        mBanners = banners;
    }

    public boolean isBannersEmpty() {
        return mBanners == null || mBanners.isEmpty();
    }

    @Override
    public int getItemCount() {
        int bannerSize = 0;
        if (mBanners != null && !mBanners.isEmpty()) {
            bannerSize = 1;
        }
        return super.getItemCount() + bannerSize;
    }

    @Override
    public Object getItem(int position) {
        if (mBanners != null && !mBanners.isEmpty()) {
            if (position == 0) {
                return mBanners;
            } else {
                return super.getItem(position - 1);
            }
        }
        return super.getItem(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (mBanners != null && !mBanners.isEmpty()) {
            if (position == 0) {
                return TYPE_HEAD;
            } else {
                return TYPE_ITEM;
            }
        }
        return TYPE_ITEM;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEAD:
                Context context = parent.getContext();
                BannerView bannerView = new BannerView(context);
                bannerView.setSwipeRefreshLayout(mSwipeRefreshLayout);

                RecyclerView.LayoutParams params =
                        new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                Utils.getScreenWidth(context) / 3);
                bannerView.setLayoutParams(params);
                return new BannerViewHolder(bannerView);
            case TYPE_ITEM:
                return new LivingViewHolder(parent.getContext(), parent, mType,isShowTopDivider);
        }

        return null;
    }


    public static class BannerViewHolder extends ViewHolder<List<Banner>> {

        public BannerViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void update(BaseAdapter adapter, int position, List<Banner> banners) {
            BannerView bannerView = (BannerView) itemView;
            bannerView.update(banners);
        }
    }

    public boolean isShowTopDivider() {
        return isShowTopDivider;
    }

    public void setShowTopDivider(boolean showTopDivider) {
        isShowTopDivider = showTopDivider;
    }
}
