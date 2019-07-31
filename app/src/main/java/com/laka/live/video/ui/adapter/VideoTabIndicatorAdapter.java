package com.laka.live.video.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.laka.live.R;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;
import com.laka.live.video.ui.widget.ScaleTransitionPagerTitleView;

import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:Rayman
 * @Date:2018/8/3
 * @Description: 小视频页TabAdapter
 */

public class VideoTabIndicatorAdapter extends CommonNavigatorAdapter {

    private Context mContext;
    private String[] titleList;
    private TabSelectListener tabSelectListener;

    private SimplePagerTitleView titleView;
    private LinePagerIndicator indicator;

    private int normalTextColor;
    private int selectedTextColor;
    private float titleTextSize;

    private int indicatorMode;
    private float indicatorLineHeight;
    private float indicatorLineWidth;
    private float roundRadius;
    private Interpolator startInterpolator;
    private Interpolator endInterpolator;
    private List<Integer> indicatorColors;
    private boolean isEnableSuitableLineWidth = false;


    public VideoTabIndicatorAdapter(Context context, String[] titleList) {
        mContext = context;
        this.titleList = titleList;
        normalTextColor = ResourceHelper.getColor(R.color.color999999);
        selectedTextColor = ResourceHelper.getColor(R.color.colorFF9D1D);
        titleTextSize = 16;

        indicatorMode = LinePagerIndicator.MODE_EXACTLY;
        indicatorLineHeight = UIUtil.dip2px(context, 2);
        indicatorLineWidth = UIUtil.dip2px(context, 14);
        roundRadius = UIUtil.dip2px(context, 1);
        startInterpolator = new AccelerateInterpolator();
        endInterpolator = new DecelerateInterpolator(2.0f);
        indicatorColors = new ArrayList<>();
        indicatorColors.add(ResourceHelper.getColor(R.color.colorFF9D1D));
    }

    @Override
    public int getCount() {
        return titleList.length;
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int i) {
        titleView = new ScaleTransitionPagerTitleView(context);
        titleView.setNormalColor(normalTextColor);
        titleView.setSelectedColor(selectedTextColor);
        titleView.setTextSize(titleTextSize);
        titleView.setText(titleList[i]);
        titleView.getPaint().setFakeBoldText(titleView.isSelected());
        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tabSelectListener != null) {
                    tabSelectListener.onTabSelect(i);
                }
            }
        });
        return titleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        indicator = new LinePagerIndicator(context);
        indicator.setMode(indicatorMode);
        indicator.setLineHeight(indicatorLineHeight);
        if (isEnableSuitableLineWidth) {
            indicator.setLineWidth(titleView.getPaint().measureText(titleView.getText().toString()));
        } else {
            indicator.setLineWidth(indicatorLineWidth);
        }
        indicator.setRoundRadius(roundRadius);
        indicator.setStartInterpolator(startInterpolator);
        indicator.setEndInterpolator(endInterpolator);
        for (Integer color : indicatorColors) {
            indicator.setColors(color);
        }
        return indicator;
    }

    @Override
    public float getTitleWeight(Context context, int index) {
        return 1;
    }

    public void setTabSelectListener(TabSelectListener tabSelectListener) {
        this.tabSelectListener = tabSelectListener;
    }

    public void setTitleView(SimplePagerTitleView titleView) {
        this.titleView = titleView;
        normalTextColor = titleView.getNormalColor();
        selectedTextColor = titleView.getSelectedColor();
        //获取的是px像素值
        titleTextSize = Utils.px2sp(mContext, titleView.getTextSize());
    }

    public void setIndicator(LinePagerIndicator indicator) {
        this.indicator = indicator;
        indicatorMode = indicator.getMode();
        indicatorLineHeight = indicator.getLineHeight();
        indicatorLineWidth = indicator.getLineWidth();
        roundRadius = indicator.getRoundRadius();
        startInterpolator = indicator.getStartInterpolator();
        endInterpolator = indicator.getEndInterpolator();
        indicatorColors = indicator.getColors();
    }

    /**
     * 是否自适应宽度
     *
     * @param isEnable
     */
    public void enableSuitableLineWidth(boolean isEnable) {
        isEnableSuitableLineWidth = isEnable;
    }

    public interface TabSelectListener {
        void onTabSelect(int position);
    }

}
