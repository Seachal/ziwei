package com.laka.live.shopping.widget;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.laka.live.R;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsImageUrlBean;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.Utils;

import java.lang.reflect.Field;
import java.util.List;

public class ImageCycleView extends FrameLayout {

    private static final int CYCLE_DURATION = 5 * 1000;//SECOND
    public static final int INDICATOR_STYLE_CENTER = 1;//指示器居中

    private LoopViewPager mImageViewPager = null;
    private ImageCycleAdapter mImageViewPagerAdapter;
    private IndicatorLayout mIndicatorLayout;
    protected int mCurrentPosition;
    protected int mScrollSpeed = 450;

    private List<ShoppingGoodsImageUrlBean> mImageUrls;

    private ImageCycleListener mListener;

    private boolean mIsTouchDown = false;
    private boolean mIsStop = false;
    private boolean mIsShowTitleView = true;

    public void setCanAutoScroll(boolean canAutoScroll) {
        this.mCanAutoScroll = canAutoScroll;
    }

    private boolean mCanAutoScroll = true;

    private float mRatio = 0;

    public ImageCycleView(Context context) {
        this(context, null);
    }

    public ImageCycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    private void initViews() {
        addImageViewPager();
        addImageIndicatorView();
    }

    public void setIndicatorStyle(int style) {
        if (style == INDICATOR_STYLE_CENTER) {
            setIndicatorStyleCenter();
        }
    }

    private void setIndicatorStyleCenter() {
        LayoutParams lp = (LayoutParams) mIndicatorLayout.getLayoutParams();
        lp.gravity = Gravity.BOTTOM | Gravity.CENTER;
        lp.leftMargin = ResourceHelper.getDimen(R.dimen.space_15);
        mIndicatorLayout.setLayoutParams(lp);
    }

    private void addImageViewPager() {
        mImageViewPager = new LoopViewPager(getContext());
        mImageViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        if (mCanAutoScroll) {
                            startImageCycle();
                        }
                        break;
                    default:
                        stopImageCycle();
                        break;
                }
                return false;
            }
        });
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.addView(mImageViewPager, lp);
    }

    private void addImageIndicatorView() {
        mIndicatorLayout = new IndicatorLayout(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, getResources().getDimensionPixelSize(R.dimen.image_cycle_view_indicator_height));
        lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        this.addView(mIndicatorLayout, lp);
    }

    public void setAspectRatio(float ratio) {
        mRatio = ratio;
    }

    public void startImageCycle() {
        if (!isValid()) {
            return;
        }
        stopImageCycle();
        postDelayed(runnable, CYCLE_DURATION);
        mIsStop = false;
    }

    public void stopImageCycle() {
        mIsStop = true;
        removeCallbacks(runnable);
    }

    protected boolean isValid() {
        if (mImageViewPager == null) {
            return false;
        }
        if (mImageUrls.size() == 0 || mImageUrls.size() == 1) {
            return false;
        }
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopImageCycle();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mCurrentPosition++;
            mIndicatorLayout.setSelectedIndicator(mCurrentPosition);
            mImageViewPager.setCurrentItem(mCurrentPosition, true);
            if (!mIsStop) {
                startImageCycle();
            }
        }
    };

    public void setImageResources(List<ShoppingGoodsImageUrlBean> imageUrls, ImageCycleListener listener) {
        mImageUrls = imageUrls;
        if (Utils.listIsNullOrEmpty(mImageUrls)) {
            return;
        }
        mListener = listener;
        mIndicatorLayout.setupIndicator(mImageUrls.size());

        mImageViewPagerAdapter = new ImageCycleAdapter();
        mImageViewPager.setAdapter(mImageViewPagerAdapter);
        mImageViewPager.setOffscreenPageLimit(mImageUrls.size());
        if (mChangeListener != null) {
            mImageViewPager.removeOnPageChangeListener(mChangeListener);
        }
        mImageViewPager.addOnPageChangeListener(mChangeListener);
        setScrollSpeed();
        startImageCycle();
    }

    /**
     * set scroll speed
     */
    private void setScrollSpeed() {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
            FixedSpeedScroller myScroller = new FixedSpeedScroller(getContext(), interpolator, mScrollSpeed);
            mScroller.set(mImageViewPager, myScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            mIsTouchDown = true;
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            mIsTouchDown = false;
        }
        return super.dispatchTouchEvent(ev);
    }

    public boolean isTouchDown() {
        return mIsTouchDown;
    }

    private ViewPager.OnPageChangeListener mChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrollStateChanged(int state) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int index) {
            mCurrentPosition = index % mImageUrls.size();
            mIndicatorLayout.setSelectedIndicator(mCurrentPosition);
        }
    };

    private class ImageCycleAdapter extends PagerAdapter {
        public ImageCycleAdapter() {
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mImageUrls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final String url = mImageUrls.get(position).getImageUrl();
            final ImageItemView imageView = new ImageItemView(getContext());
            imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            if (mRatio > 0.001) {
                imageView.setAspectRatio(mRatio);
            }
            if (position > 0) {
                imageView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageURI(url);
                    }
                }, 2000);
            } else {
                imageView.setImageURI(url);
            }
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageItemView view = (ImageItemView) object;
            container.removeView(view);
        }
    }

    /**
     * 设置图片
     *
     * @param draweeView
     * @param imageUrl
     */
    private void setImageUri(SimpleDraweeView draweeView, String imageUrl) {
        if (StringUtils.isEmpty(imageUrl)) {
            return;
        }
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imageUrl))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .build();
        draweeView.setController(controller);
    }

    private class ImageItemView extends FrameLayout {
        private SimpleDraweeView mImageView;
        private GenericDraweeHierarchy mHierarchy;

        public ImageItemView(Context context) {
            super(context);
            if (mHierarchy == null) {
                createHierarchy();
            }
            addImageView();
        }

        private void addImageView() {
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            mImageView = new SimpleDraweeView(getContext());
            mImageView.setHierarchy(mHierarchy);
            this.addView(mImageView, lp);
        }

        private void createHierarchy() {
            mHierarchy = new GenericDraweeHierarchyBuilder(getResources())
                    .setPlaceholderImage(R.drawable.blank_icon_bigimages)
                    .setBackground(ResourceHelper.getDrawable(R.drawable.blank_icon_bigimages))
                    .setFailureImage(R.drawable.blank_icon_bigimages)
                    .setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP)
                    .setFailureImageScaleType(ScalingUtils.ScaleType.CENTER_CROP)
                    .setPlaceholderImageScaleType(ScalingUtils.ScaleType.CENTER_CROP)
                    .build();
        }

        public void setAspectRatio(float ratio) {
            mImageView.setAspectRatio(ratio);
        }

        public void setImageURI(String url) {
            setImageUri(mImageView, url);
        }

        public ImageView getImageView() {
            return mImageView;
        }
    }

    private class IndicatorLayout extends LinearLayout {

        public IndicatorLayout(Context context) {
            super(context);
            setOrientation(LinearLayout.HORIZONTAL);
        }

        public void setupIndicator(int size) {
            this.removeAllViews();
            if (size <= 1) {
                return;
            }
            ImageView indicatorView;
            int gap = getResources().getDimensionPixelSize(R.dimen.image_cycle_view_indicator_item_gap);
            LayoutParams lp;
            for (int i = 0; i < size; i++) {
                indicatorView = new ImageView(getContext());
                indicatorView.setScaleType(ScaleType.CENTER_CROP);
                indicatorView.setImageResource(R.drawable.dot_selector);
                lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.gravity = Gravity.CENTER | Gravity.CENTER_VERTICAL;
                if (i == 0) {
                    indicatorView.setSelected(true);
                } else {
                    indicatorView.setSelected(false);
                    lp.leftMargin = gap;
                }
                this.addView(indicatorView, lp);
            }
        }

        public void setSelectedIndicator(int index) {
            int childCount = this.getChildCount();
            if (childCount <= index) {
                return;
            }
            for (int i = 0; i < childCount; i++) {
                ImageView indicatorView = (ImageView) this.getChildAt(i);
                if (i == index) {
                    indicatorView.setSelected(true);
                } else {
                    indicatorView.setSelected(false);
                }
            }
        }
    }

    public static class ImageInfo {
        public String url;
        public String title;
        public Object value;
    }

    public interface ImageCycleListener {
        void onImageClick(int position, View imageView);
    }
}
