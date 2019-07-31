package com.laka.live.shopping.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.shopping.activity.ShoppingCategoryActivity;
import com.laka.live.shopping.bean.newversion.GoodsCate;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.Utils;

/**
 * @ClassName: HomeClassifyView
 * @Description: 首页一级分类的子控件
 * @Author: chuan
 * @Version: 1.0
 * @Date: 17/07/2017
 */

public class HomeClassifyView extends LinearLayout implements View.OnClickListener {
    private final static String TAG = HomeClassifyView.class.getSimpleName();

    private SimpleDraweeView mIconSdv;
    private TextView mNameTv;

    private GoodsCate mGoodsCate;

    public HomeClassifyView(Context context) {
        this(context, null);
    }

    public HomeClassifyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeClassifyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initUI();
    }

    private void initUI() {
        setOrientation(VERTICAL);

        View view = inflate(getContext(), R.layout.view_home_classivy_item, this);

        setLayoutParams(new LayoutParams(Utils.getScreenWidth(getContext()) / 5, ViewGroup.LayoutParams.WRAP_CONTENT));

        mIconSdv = (SimpleDraweeView) view.findViewById(R.id.icon_sdv);
        mNameTv = (TextView) view.findViewById(R.id.name_tv);

        setOnClickListener(this);

    }

    /**
     * 更新数据
     */
    public void updateData(GoodsCate goodsCate) {
        Log.d(TAG, "update data . goodsCate : " + goodsCate);

        mGoodsCate = goodsCate;
        if (goodsCate == null) {
            setVisibility(GONE);
            return;
        }

        ImageUtil.loadImage(mIconSdv, goodsCate.getImageUrl());
        mNameTv.setText(goodsCate.getTitle());
    }

    @Override
    public void onClick(View v) {
        if (mGoodsCate != null) {
            ShoppingCategoryActivity.startActivity(getContext(), mGoodsCate.getTitle(), mGoodsCate.getCateId());
        }
    }
}
