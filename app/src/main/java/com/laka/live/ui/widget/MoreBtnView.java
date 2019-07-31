package com.laka.live.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.laka.live.R;
import com.laka.live.pop.CourseCategoryPop;

/**
 * Created by Lyf on 2017/9/19.
 */

public class MoreBtnView extends RelativeLayout implements CourseCategoryPop.OnVisibleChangedListener {

    private ImageView mArrow;
    private RelativeLayout backGround;
    private Animation openAnimation,closeAnimation;

    public MoreBtnView(Context context) {
        this(context, null);
    }

    public MoreBtnView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoreBtnView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_more_btn_view, this);
        mArrow = (ImageView) view.findViewById(R.id.arrow);
        backGround = (RelativeLayout) view.findViewById(R.id.backGround);
        openAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.arrow_open_anim);//创建动画
        openAnimation.setInterpolator(new LinearInterpolator());//设置为线性旋转
        openAnimation.setFillAfter(true);//每次都取相反值，使得可以不恢复原位的旋转
        closeAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.arrow_close_anim);//创建动画
        closeAnimation.setInterpolator(new LinearInterpolator());//设置为线性旋转
        closeAnimation.setFillAfter(true);//每次都取相反值，使得可以不恢复原位的旋转

    }

    @Override
    public void onVisibleChanged(boolean visible) {
        if (visible) {
            mArrow.startAnimation(openAnimation);
        } else {
            mArrow.startAnimation(closeAnimation);
        }
    }

}
