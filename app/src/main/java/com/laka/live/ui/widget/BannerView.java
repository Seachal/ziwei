package com.laka.live.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.bean.Banner;
import com.laka.live.ui.adapter.BannerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luwies on 16/4/16.
 */
public class BannerView extends FrameLayout implements ViewPager.OnPageChangeListener,
        BannerAdapter.OnItemClickListener,
        CustomViewPager.OnTouchListener {

    private static final int MSG = 1;

    private final static long ADVERT_PLAY_DELAY_TIME = 5000L;

    private boolean mCancelled = true;

    private Context mContext;

    private CustomViewPager mPager;

    private LinearLayout mDotsLayout;

    private int mCurrentItem;

    private ArrayList<Banner> mBanners;

    private ArrayList<String> mUrls;

    private ArrayList<ImageView> mDotImages;

    private ArrayList<SimpleDraweeView> mBannerImages;

    private BannerAdapter mAdapter;

    private boolean isChanged = false;

    private ViewGroup mSwipeRefreshLayout;

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.banner_layout, this);

        mPager = (CustomViewPager) findViewById(R.id.vp_home_advert_player);
        mDotsLayout = (LinearLayout) findViewById(R.id.linearlayout_home_dots);
        mBanners = new ArrayList<>();

        mUrls = new ArrayList<>();

        mDotImages = new ArrayList<>();

        mBannerImages = new ArrayList<>();

        mAdapter = new BannerAdapter(mContext);

        mAdapter.setAdvertList(mBannerImages, mUrls);
        mAdapter.setOnItemClickListener(this);
        mPager.setAdapter(mAdapter);
        mPager.setOnPageChangeListener(this);
        mAdapter.notifyDataSetChanged();

        mPager.setOntouchListener(this);
    }

    public void update(List<Banner> list) {
        if (list == null || list.isEmpty()) {
            return;
        }


//        Banner lastBanner = list.get(list.size() - 1);
//        mBanners.add(lastBanner);

//        mBanners.addAll(list);

//        Banner firstBanner = list.get(0);
//        mBanners.add(firstBanner);

//        list.add(0, lastBanner);
//        list.add(firstBanner);
        updateDots(list.size());
        updateViewPager(list.size());

        List<String> urls = new ArrayList<>();
        for (Banner banner : list) {
            if (banner != null) {
                urls.add(banner.getImage());
            }
        }

        mBanners.clear();
        mBanners.addAll(list);

        mUrls.clear();
        mUrls.addAll(urls);

        mAdapter.setAdvertList(mBannerImages, mUrls);
        mPager.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        mCurrentItem = 0;
        mPager.setCurrentItem(mCurrentItem);
        selectDot(mCurrentItem);
        start();
    }

    private void updateDots(int size) {
        int createSize = 0;
        boolean isEmpty = mDotImages.isEmpty();
        if (isEmpty) {
            createSize = size;
        } else {
            createSize = size - mDotImages.size();
        }

        if (createSize > 0) {
            int margin = mContext.getResources().getDimensionPixelSize(R.dimen.dot_margin);
            for (int i = 0; i < createSize; i++) {
                ImageView dot = new ImageView(mContext);
                dot.setImageResource(R.drawable.dot_selector);
//                dot.setScaleType(ImageView.ScaleType.CENTER);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (isEmpty == false || i != 0) {
                    params.leftMargin = margin;
                }
                dot.setLayoutParams(params);
                mDotImages.add(dot);
            }
        }

        int count = mDotsLayout.getChildCount();
        int addSize = size - count;
        if (addSize > 0) {
            //说明dot数量不够，需要多加几个
            for (int i = 0; i < addSize; i++) {
                mDotsLayout.addView(mDotImages.get(count + i));
            }
        } else if (addSize < 0) {
            //说明dot数量太多，需要删除几个
            int removeSize = -addSize;
            mDotsLayout.removeViews(count - removeSize, removeSize);
        }
    }

    private void updateViewPager(int size) {
        int createSize = 0;
        if (mBannerImages.isEmpty()) {
            createSize = size;
        } else {
            createSize = size - mBannerImages.size();
        }

        if (createSize > 0) {
            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = LinearLayout.LayoutParams.MATCH_PARENT;
            for (int i = 0; i < createSize; i++) {
                SimpleDraweeView imageView = new SimpleDraweeView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(mContext.getResources())
                        .setPlaceholderImage(mContext.getResources().getDrawable(R.drawable.blank_icon_banner),
                                ScalingUtils.ScaleType.FIT_XY)
                        .setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP)
                        .build();
                imageView.setHierarchy(hierarchy);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
                imageView.setLayoutParams(params);
                mBannerImages.add(imageView);
            }
        } else {

        }

    }

    private void selectDot(int position) {
        for (int i = 0; i < mDotImages.size(); i++) {
            if (i == position) {
                mDotImages.get(i).setSelected(true);
            } else {
                mDotImages.get(i).setSelected(false);
            }
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    /**
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        isChanged = true;

        mCurrentItem = position;

        selectDot(mCurrentItem);

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        enableDisableSwipeRefresh(state == ViewPager.SCROLL_STATE_IDLE);
    }

    public void setSwipeRefreshLayout(ViewGroup layout) {
        mSwipeRefreshLayout = layout;
    }

    private void enableDisableSwipeRefresh(boolean enable) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setEnabled(enable);
        }
    }

    @Override
    public void onItemClick(int position) {
        Banner banner = mBanners.get(position);
        if (banner != null) {
            banner.onClick((Activity) mContext);
        }
    }


    public synchronized final void cancel() {
        mCancelled = true;
        mHandler.removeMessages(MSG);
    }

    public synchronized final void start() {
        if (mCancelled == true) {
            mCancelled = false;
            mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG), ADVERT_PLAY_DELAY_TIME);
        }
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (mCancelled) {
                return;
            }

            if (mBanners.isEmpty() == false) {
                mCurrentItem = (mCurrentItem + 1) % mBanners.size();
                mPager.setCurrentItem(mCurrentItem);
            } else {
                cancel();
                return;
            }

            removeMessages(MSG);
            sendMessageDelayed(obtainMessage(MSG), ADVERT_PLAY_DELAY_TIME);

        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void onTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            cancel();
        } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        cancel();
//        mPager.removeAllViews();
//        mBannerImages.clear();
//        mBanners.clear();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }
}
