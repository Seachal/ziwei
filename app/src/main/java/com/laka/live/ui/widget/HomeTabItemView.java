package com.laka.live.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laka.live.R;

/**
 * @Author:summer
 * @Date:2018/12/11
 * @Description: 首页tab item view
 */
public class HomeTabItemView extends RelativeLayout {

    private TextView mTextViewTab;
    private ImageView mImageIcon;
    private String mBottomStr;
    private Drawable mImage;
    private ImageView mImagePointRed;

    public HomeTabItemView(Context context) {
        this(context, null);
    }

    public HomeTabItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeTabItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HomeTabItemView);
            mBottomStr = ta.getString(R.styleable.HomeTabItemView_bottomText);
            mImage = ta.getDrawable(R.styleable.HomeTabItemView_topImage);
        }
        setContentView(context);
    }

    private void setContentView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.tab_home_item, this, true);
        initView();
    }

    private void initView() {
        mImageIcon = findViewById(R.id.image_icon);
        mTextViewTab = findViewById(R.id.textview_tab);
        mImagePointRed = findViewById(R.id.image_point_red);
        mTextViewTab.setText(mBottomStr);
        mImageIcon.setImageDrawable(mImage);
    }

    /**
     * 是否显示小红点
     *
     * @param visible
     */
    public void showRedPoint(int visible) {
        mImagePointRed.setVisibility(visible);
    }


    public TextView getTextViewTab() {
        return mTextViewTab;
    }

    public ImageView getImageIcon() {
        return mImageIcon;
    }
}
