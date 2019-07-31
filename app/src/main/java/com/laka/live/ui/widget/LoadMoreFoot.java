package com.laka.live.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.laka.live.R;

/**
 * Created by luwies on 16/5/25.
 */
public class LoadMoreFoot extends FrameLayout {

    private View mLoadingLayout;
    private View mFootTip;

    private Animation mAnimation;

    private View mLoadingIcon;

    private TextView tvTips;

    public LoadMoreFoot(Context context) {
        this(context, null);
    }

    public LoadMoreFoot(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreFoot(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.load_more_foot, this);

        mFootTip = findViewById(R.id.foot_tip);

        mLoadingLayout = findViewById(R.id.loading_layout);
        mAnimation = AnimationUtils.loadAnimation(context, R.anim.foot_loading_anim);
        mLoadingIcon = findViewById(R.id.loading);


        tvTips = (TextView) findViewById(R.id.tv_tips);
    }

    public void showLoading() {
        mFootTip.setVisibility(GONE);
        mLoadingLayout.setVisibility(VISIBLE);
        mLoadingIcon.setAnimation(mAnimation);
        mAnimation.start();

        setVisibility(VISIBLE);
        tvTips.setText(R.string.loading_data);
    }

    public void hideLoading() {
        mFootTip.setVisibility(VISIBLE);
        mLoadingLayout.setVisibility(GONE);
        mAnimation.cancel();
        mLoadingIcon.clearAnimation();
    }
}
