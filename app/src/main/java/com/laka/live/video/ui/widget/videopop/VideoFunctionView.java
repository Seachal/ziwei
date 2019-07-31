package com.laka.live.video.ui.widget.videopop;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.laka.live.R;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;
import com.laka.live.video.ui.adapter.VideoFunctionPagerAdapter;
import com.laka.live.video.ui.adapter.VideoTabIndicatorAdapter;
import com.laka.live.video.ui.widget.ScaleTransitionPagerTitleView;
import com.laka.live.video.ui.widget.videofunction.VideoFunctionHelper;
import com.orhanobut.logger.Logger;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

/**
 * @Author:Rayman
 * @Date:2018/8/13
 * @Description:小视频播放页面功能面板 实际上只是一个框的作用，真正的业务逻辑处理都在 VideoCommentView
 * 和VideoRecommendGoodView
 */

public class VideoFunctionView extends FrameLayout implements View.OnClickListener {

    public static final float FUNCTION_PANEL_PERCENT = (float) 0.6;
    public static final int FUNCTION_PANEL_ANIM_DURATION = 300;
    public static final int FUNCTION_COMMENT = 0;
    public static final int FUNCTION_RECOMMEND = 1;

    private Context context;

    /**
     * description:动画配置
     **/
    private ValueAnimator animator;
    private float mScreenHeight;
    private float targetScreenHeight;

    /**
     * description:UI配置
     **/
    private View contentView;
    private ViewPager mVpContent;
    private MagicIndicator mIndicator;
    private ImageView mIvClose;
    private VideoFunctionHelper mHelper;
    private boolean isShowing = false;
    private OnFunctionAnimationListener onFunctionAnimationListener;

    /**
     * description:TAB控制
     **/
    private int targetIndex = 0;
    private SimplePagerTitleView titleView;
    private LinePagerIndicator indicator;
    private VideoFunctionPagerAdapter mAdapter;

    public VideoFunctionView(@NonNull Context context) {
        this(context, null);
    }

    public VideoFunctionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoFunctionView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(context).inflate(R.layout.layout_video_function, this, true);
        mScreenHeight = Utils.getScreenHeight(context);
        targetScreenHeight = mScreenHeight * FUNCTION_PANEL_PERCENT;
        setTranslationY(targetScreenHeight);
        mVpContent = findViewById(R.id.vp_video_function);
        mIndicator = findViewById(R.id.indicator_video_function);
        mIvClose = findViewById(R.id.iv_mini_video_function_close);
        mIvClose.setOnClickListener(this);
    }

    private void initViewPager() {
        mAdapter = new VideoFunctionPagerAdapter(context, mHelper);
        mVpContent.setAdapter(mAdapter);
        initIndicator(mAdapter.getTitleList());
        //需要在配置之后才set，不然位置会错乱
        mVpContent.setCurrentItem(targetIndex);
    }

    private void initIndicator(String[] titleList) {
        titleView = new ScaleTransitionPagerTitleView(context);
        titleView.setNormalColor(ResourceHelper.getColor(R.color.color999999));
        titleView.setSelectedColor(ResourceHelper.getColor(R.color.color333333));
        titleView.setTextSize(14);

        indicator = new LinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
        indicator.setLineHeight(UIUtil.dip2px(context, 2));
        indicator.setLineWidth(UIUtil.dip2px(context, 14));
        indicator.setRoundRadius(UIUtil.dip2px(context, 1));
        indicator.setStartInterpolator(new AccelerateInterpolator());
        indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
        indicator.setColors(ResourceHelper.getColor(R.color.colorFF9D1D));

        CommonNavigator navigator = new CommonNavigator(context);
        VideoTabIndicatorAdapter indicatorAdapter = new VideoTabIndicatorAdapter(context, titleList);
        indicatorAdapter.setTabSelectListener(new VideoTabIndicatorAdapter.TabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mVpContent.setCurrentItem(position);
            }
        });
        indicatorAdapter.setTitleView(titleView);
        indicatorAdapter.setIndicator(indicator);
        indicatorAdapter.enableSuitableLineWidth(true);
        navigator.setAdapter(indicatorAdapter);
        mIndicator.setNavigator(navigator);
        // must after setNavigator
        LinearLayout titleContainer = navigator.getTitleContainer();
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerDrawable(new ColorDrawable() {
            @Override
            public int getIntrinsicWidth() {
                return UIUtil.dip2px(context, 20);
            }
        });
        ViewPagerHelper.bind(mIndicator, mVpContent);
    }

    public void setVideoUiHelper(VideoFunctionHelper helper) {
        this.mHelper = helper;
        initViewPager();
        //Logger.i("设置VideoUIHelper：" + this + "VP：" + mVpContent + "Adapter：" + mAdapter);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Logger.e("VideoFunctionView-----dispatchTouchEvent------ActionDown");
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                Logger.e("VideoFunctionView-----dispatchTouchEvent------ActionMove");
                getParent().requestDisallowInterceptTouchEvent(isShowing);
                break;
            case MotionEvent.ACTION_UP:
                Logger.e("VideoFunctionView-----dispatchTouchEvent------ActionUp");
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_mini_video_function_close:
                dismissFunctionView();
                break;
            default:
                break;
        }
    }

    public void showFunctionView(int position) {
        if (position > 1 || position < 0) {
            return;
        }
        //执行动画
        mVpContent.setCurrentItem(position);
        animator = ValueAnimator.ofFloat(targetScreenHeight, 0);
        animator.setDuration(FUNCTION_PANEL_ANIM_DURATION);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setTranslationY((Float) animation.getAnimatedValue());
            }
        });
        if (onFunctionAnimationListener != null) {
            onFunctionAnimationListener.onAnimStart(true, animator);
        }
        animator.start();
        isShowing = true;
    }

    public void dismissFunctionView() {
        if (!isShowing) {
            return;
        }
        //执行动画
        animator = ValueAnimator.ofFloat(0, targetScreenHeight);
        animator.setDuration(FUNCTION_PANEL_ANIM_DURATION);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setTranslationY((Float) animation.getAnimatedValue());
            }
        });
        if (onFunctionAnimationListener != null) {
            onFunctionAnimationListener.onAnimStart(false, animator);
        }
        animator.start();
        isShowing = false;
    }

    public void setOnFunctionAnimationListener(OnFunctionAnimationListener onFunctionAnimationListener) {
        this.onFunctionAnimationListener = onFunctionAnimationListener;
    }

    public interface OnFunctionAnimationListener {
        void onAnimStart(boolean isEnter, Animator animation);
    }

    public boolean isShowing() {
        return isShowing;
    }

    public void onDestroy() {
        //释放资源
        onFunctionAnimationListener = null;
        if (mAdapter != null) {
            mAdapter.onRelease();
            mAdapter = null;
        }
        mHelper = null;
        mVpContent = null;
    }
}
