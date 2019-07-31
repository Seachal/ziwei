package com.laka.live.shopping.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.laka.live.R;

/**
 * @ClassName: BuyPanelSkuView
 * @Description:
 * @Author: chuan
 * @Version: 1.0
 * @Date: 28/07/2017
 */

public class BuyPanelSkuView extends FrameLayout {
    private TextView mContentTv;
    private ImageView mIconIv;

    public BuyPanelSkuView(@NonNull Context context) {
        this(context, null);
    }

    public BuyPanelSkuView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BuyPanelSkuView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.view_buy_panel_sku, this);

        mContentTv = (TextView) findViewById(R.id.content_tv);
        mIconIv = (ImageView) findViewById(R.id.image_iv);
    }

    public void setContent(String content) {
        mContentTv.setText(content);
    }

    public void setSelected(boolean selected) {
        mContentTv.setSelected(selected);
        mIconIv.setVisibility(selected ? VISIBLE : GONE);
    }
}
