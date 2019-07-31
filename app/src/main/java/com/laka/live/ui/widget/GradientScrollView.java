package com.laka.live.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.shopping.framework.ui.TitleBarActionItem;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.SystemUtil;
import com.laka.live.util.Utils;
import com.laka.live.util.ViewUtils;

/**
 * Created by Lyf on 2017/7/4.
 */

public class GradientScrollView extends ScrollView {

    private int red = 255;
    private int green = 255;
    private int blue = 255;

    private ScrollViewListener scrollViewListener = null;

    public GradientScrollView(Context context) {
        super(context);
    }

    public GradientScrollView(Context context, AttributeSet attrs,
                              int defStyle) {
        super(context, attrs, defStyle);
    }

    public GradientScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    private View mTitleBar,mDivider;
    private int mTitleBarHeight;
    private TextView titleTv, proxyShare;
    private ImageView back, share;//service
    private TitleBarActionItem service;

    // 绑定渐变式状态栏
    public void setGradientTitleBar(Context context, String agentProfitratio, View mTitleBar, View mask) {

        mDivider = ViewUtils.findById(mTitleBar, R.id.divider);
        back = ViewUtils.findById(mTitleBar, R.id.back);
        share = ViewUtils.findById(mTitleBar, R.id.share);
        service = ViewUtils.findById(mTitleBar, R.id.iv_service);
        service.setDrawable(ResourceHelper.getDrawable(R.drawable.nav_btn_service_selector));
        titleTv = ViewUtils.findById(mTitleBar, R.id.title);
        proxyShare = ViewUtils.findById(mTitleBar, R.id.proxyShare);

        if (Build.VERSION.SDK_INT >= 19) {
            this.mTitleBar = mTitleBar;
            mTitleBarHeight = mTitleBar.getLayoutParams().height;
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mTitleBar.getLayoutParams();
            layoutParams.height += SystemUtil.getStatusBarHeight(context) / 2;
            mTitleBar.setLayoutParams(layoutParams);
            mTitleBar.setPadding(0, SystemUtil.getStatusBarHeight(context), 0, 0);
            RelativeLayout.LayoutParams maskLayoutParams = (RelativeLayout.LayoutParams) mask.getLayoutParams();
            maskLayoutParams.height += SystemUtil.getStatusBarHeight(context) / 2;
            mask.setLayoutParams(maskLayoutParams);

            RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) ViewUtils.findById(mTitleBar, R.id.shareLayout).getLayoutParams();
            layoutParams1.setMargins(-ResourceHelper.getDimen(R.dimen.space_10), 0, 0, 0);
            ViewUtils.findById(mTitleBar, R.id.shareLayout).setLayoutParams(layoutParams1);

            if (Utils.isNotEmpty(agentProfitratio)) {
                share.setVisibility(GONE);
                proxyShare.setVisibility(VISIBLE);
                proxyShare.setText("赚"+agentProfitratio);
                Log.log("agentProfitratio="+agentProfitratio);
            }

        } else {
            // 低版本的，直接设置颜色就行了
            setScale(1);
            mTitleBar.setBackgroundColor(Color.argb(255, red, green, blue));
        }

    }

    private void setScale(float scale) {

        if (back == null || share == null || titleTv == null || service == null) {
            return;
        }

        // View的Alpha的值与scale的区间是一样的：0-1。
        titleTv.setAlpha(scale);

        if (scale <= 0.5) {
            back.setImageResource(R.drawable.nav_icon_back_white);
            share.setImageResource(R.drawable.nav_icon_share_white);
//            service.setImageResource(R.drawable.nav_btn_service_selector);
            service.setDrawable(ResourceHelper.getDrawable(R.drawable.nav_btn_service_selector));
        } else {
            back.setImageResource(R.drawable.nav_icon_back);
            share.setImageResource(R.drawable.nav_icon_share);
//            service.setImageResource(R.drawable.nav_icon_service);
            service.setDrawable(ResourceHelper.getDrawable(R.drawable.nav_icon_service));
        }

        if(scale == 1) {
            mDivider.setVisibility(VISIBLE);
        }else{
            mDivider.setVisibility(GONE);
        }

    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);

        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }

        if (mTitleBar != null && Build.VERSION.SDK_INT >= 19) {
            //AGB由相关工具获得，或者美工提供
            if (y <= 0) {
                setScale(0);
                mTitleBar.setBackgroundColor(Color.argb(0, red, green, blue));
            } else if (y > 0 && y <= mTitleBarHeight) {
                float scale = (float) y / mTitleBarHeight;
                float alpha = (255 * scale);
                // 只是layout背景透明
                setScale(scale);
                mTitleBar.setBackgroundColor(Color.argb((int) alpha, red, green, blue));
            } else {
                setScale(1);
                mTitleBar.setBackgroundColor(Color.argb(255, red, green, blue));
            }

        }
    }

    public TitleBarActionItem getIvService() {
        return service;
    }

    public interface ScrollViewListener {

        void onScrollChanged(GradientScrollView scrollView, int x, int y,
                             int oldx, int oldy);

    }

}