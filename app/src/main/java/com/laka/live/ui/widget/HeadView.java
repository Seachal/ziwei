package com.laka.live.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.TextViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.laka.live.R;

/**
 * Created by luwies on 16/3/15.
 */
public class HeadView extends FrameLayout {

    private static final String TAG = "HeadView";
    private View mRoot;
    public Context mContext;
    private AlphaTextView mBackText;
    private TextView mTitle;
    private TextView mTip;
    private ImageView mRightIcon;
    private View mDividerLine;

    public HeadView(Context context) {
        super(context);
        init();
    }

    public HeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public HeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        init();

        final TypedArray a = mContext.obtainStyledAttributes(
                attrs, R.styleable.HeadView, defStyleAttr, 0);

        Resources resources = getResources();

        Drawable bg = a.getDrawable(R.styleable.HeadView_headBg);
        if (bg != null) {
            mRoot.setBackground(bg);
        }
        String titleText = a.getString(R.styleable.HeadView_headText);
        int titleColor = a.getColor(R.styleable.HeadView_headColor, resources.getColor(R.color.color2E2E2E));
        float titleSize = a.getDimension(R.styleable.HeadView_headSize,
                resources.getDimension(R.dimen.head_title_size));
        mTitle.setText(titleText);
        mTitle.setTextColor(titleColor);
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);

        String tipText = a.getString(R.styleable.HeadView_tipText);
        /*int tipColor = a.getColor(R.styleable.HeadView_tipColor,
                resources.getColor(R.color.theme_red_color_selector));*/
        ColorStateList tipColorStateList = a.getColorStateList(R.styleable.HeadView_tipColor);
        if (tipColorStateList == null) {
            tipColorStateList = resources.getColorStateList(R.color.color333333);
        }
        mTip.setTextColor(tipColorStateList);
        float tipSize = a.getDimension(R.styleable.HeadView_tipSize,
                resources.getDimension(R.dimen.head_tip_size));
        mTip.setText(tipText);
        bg = a.getDrawable(R.styleable.HeadView_tipBackground);
        mTip.setBackground(bg);

        mTip.setTextSize(TypedValue.COMPLEX_UNIT_PX, tipSize);
        mTip.setVisibility(VISIBLE);

        Drawable rightIcon = a.getDrawable(R.styleable.HeadView_rightIcon);
        if (mRightIcon != null && rightIcon != null) {
            mRightIcon.setImageDrawable(rightIcon);
        }

        boolean isShowDivider = a.getBoolean(R.styleable.HeadView_showDivider, true);
        if (isShowDivider) {
            mDividerLine.setVisibility(VISIBLE);
        } else {
            mDividerLine.setVisibility(GONE);
        }

        Drawable backIcon = a.getDrawable(R.styleable.HeadView_backIcon);
        if (backIcon != null) {
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(mBackText, backIcon, null, null, null);
        }

        CharSequence backText = a.getText(R.styleable.HeadView_backText);
        if (!TextUtils.isEmpty(backText)) {
            mBackText.setText(backText);
        }

        ColorStateList backTextColorStateList = a.getColorStateList(R.styleable.HeadView_backTextColor);
        if (backTextColorStateList != null) {
            mBackText.setTextColor(backTextColorStateList);
        }

        a.recycle();
    }

    private void init() {
        mContext = getContext();

        LayoutInflater.from(mContext).inflate(R.layout.back_head, this);

        mRoot = findViewById(R.id.root);
        mBackText = (AlphaTextView) findViewById(R.id.back_icon);

        mBackText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof Activity) {
                    ((Activity) mContext).finish();
                }
            }
        });

        mTitle = (TextView) findViewById(R.id.title);
        mTip = (TextView) findViewById(R.id.tip);
        mRightIcon = (ImageView) findViewById(R.id.right_icon);
        mDividerLine = findViewById(R.id.divider_line);
    }

    public void setBackShow(boolean isShow) {
        if (isShow) {
            mBackText.setVisibility(View.VISIBLE);
        } else {
            mBackText.setVisibility(View.GONE);
        }
    }

    public void setBackTextShow(boolean isShow) {
        if (!isShow) {
            mBackText.setText(null);
        }
    }

    public void setTipShow(boolean isShow) {
        if (isShow) {
            mTip.setVisibility(View.VISIBLE);
        } else {
            mTip.setVisibility(View.GONE);
        }
    }

    public void setRightIconShow(boolean isShow) {
        mRightIcon.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void setRightIcon(int resId) {
        mRightIcon.setImageResource(resId);
    }

    public void showDivider(boolean show) {
        if (show) {
            mDividerLine.setVisibility(VISIBLE);
        } else {
            mDividerLine.setVisibility(GONE);
        }
    }

    public void setBackOnClickListener(OnClickListener listener) {
        mBackText.setOnClickListener(listener);
    }

    public void setTipOnClickListener(OnClickListener listener) {
        mTip.setOnClickListener(listener);
    }

    public void setRightIconOnClickListener(OnClickListener listener) {
        mRightIcon.setOnClickListener(listener);
    }

    public void setTitle(CharSequence text) {
        mTitle.setText(text);
    }

    public void setTip(CharSequence tip) {
        mTip.setText(tip);
    }

}
