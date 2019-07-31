package com.laka.live.video.ui.widget.follow;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laka.live.R;

/**
 * @Author:Rayman
 * @Date:2018/8/13
 * @Description:小视频页面关注View
 */

public class FollowView extends LinearLayout implements IFollowView {

    private boolean isFollow;

    private int backGroundFollowRes;
    private int backGroundUnFollowRes;
    private int followArrowRes;

    private TextView mTvFollowText;
    private ImageView mIvFollowIcon;

    public FollowView(Context context) {
        this(context, null);
    }

    public FollowView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FollowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initProperties(context, attrs);
        initView(context);
    }

    @Override
    public void initProperties(Context context, AttributeSet attributeSet) {
        backGroundFollowRes = R.drawable.selector_round_corner_rectangle_hollow;
        backGroundUnFollowRes = R.drawable.selector_round_corner_rectangle_yellow;
        followArrowRes = R.drawable.btn_icon_follow;
    }

    @Override
    public void initView(Context context) {
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);
        setBackgroundResource(backGroundUnFollowRes);

        mTvFollowText = new TextView(context);
        mIvFollowIcon = new ImageView(context);

        mTvFollowText.setTextColor(Color.parseColor("#ffffff"));
        mTvFollowText.setTextSize(12);
        mIvFollowIcon.setImageResource(followArrowRes);
        upDateFollowState(false);
        addView(mIvFollowIcon);
        addView(mTvFollowText);
    }

    @Override
    public void setFollowState(boolean followState) {
        upDateFollowState(followState);
    }

    @Override
    public boolean getFollowState() {
        return isFollow;
    }

    @Override
    public void follow() {
        isFollow = true;
        upDateFollowState(isFollow);
    }

    @Override
    public void unFollow() {
        isFollow = false;
        upDateFollowState(isFollow);
    }

    private void upDateFollowState(boolean isFollow) {
        this.isFollow = isFollow;
        if (isFollow) {
            setBackgroundResource(backGroundFollowRes);
            mTvFollowText.setText("已关注");
            mIvFollowIcon.setVisibility(GONE);
        } else {
            setBackgroundResource(backGroundUnFollowRes);
            mTvFollowText.setText("关注");
            mIvFollowIcon.setVisibility(VISIBLE);
        }
    }
}
