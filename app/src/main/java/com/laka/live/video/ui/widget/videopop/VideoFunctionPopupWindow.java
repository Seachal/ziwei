package com.laka.live.video.ui.widget.videopop;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.laka.live.R;
import com.laka.live.ui.widget.chatKeyboard.KJChatKeyboardComment;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;
import com.laka.live.video.ui.adapter.VideoFunctionPagerAdapter;
import com.laka.live.video.ui.adapter.VideoTabIndicatorAdapter;
import com.laka.live.video.ui.widget.ScaleTransitionPagerTitleView;
import com.laka.live.video.ui.widget.videofunction.VideoFunctionHelper;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

/**
 * @Author:Rayman
 * @Date:2018/8/13
 * @Description:小视频播放页面PopupWindow
 */

public class VideoFunctionPopupWindow extends BasePopupWindow implements View.OnClickListener {

    private Context context;

    //    @BindView(R.id.kjChatKeyboard)
    KJChatKeyboardComment mChatKeyboard;

    /**
     * description:UI配置
     **/
    private View contentView;
    private ViewPager mVpContent;
    private MagicIndicator mIndicator;
    private ImageView mIvClose;
    private VideoFunctionPagerAdapter mAdapter;
    private VideoFunctionHelper mHelper;

    private int targetIndex = 0;
    private SimplePagerTitleView titleView;
    private LinePagerIndicator indicator;

    public VideoFunctionPopupWindow(Context context, VideoFunctionHelper mHelper) {
        super(context);
        this.context = context;
        this.mHelper = mHelper;
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setOutsideTouchable(false);
        setEnableOutSideDismiss(false);
        setWidth(Utils.getScreenWidth(context));
        setHeight((int) (Utils.getScreenHeight(context) * 0.6));
        initView();
        initViewPager();
    }

    private void initView() {
        contentView = super.getPopupContentView();
        mVpContent = contentView.findViewById(R.id.vp_video_function);
        mIndicator = (MagicIndicator) getView(R.id.indicator_video_function);
        mIvClose = (ImageView) getView(R.id.iv_mini_video_function_close);
//        mChatKeyboard = contentView.findViewById(R.id.kjChatKeyboard);
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

    @NonNull
    @Override
    public int initLayout() {
        return R.layout.layout_video_function;
    }

    @Override
    public int initContentView() {
        return R.id.cl_mini_video_function;
    }

    @Override
    public int initAnimationView() {
        return R.id.cl_mini_video_function;
    }

    @Override
    public int initEnterAnimation() {
        return R.anim.push_bottom_in;
    }

    @Override
    public int initExitAnimation() {
        return R.anim.push_bottom_out;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_mini_video_function_close:
                dismiss();
                break;
            default:
                break;
        }
    }
}
