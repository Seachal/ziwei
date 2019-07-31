package com.laka.live.shopping.widget;

import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.util.HardwareUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;

/**
 * Created by zhxu on 2016/5/17.
 * Email:357599859@qq.com
 */
public class ShoppingTopicBanner extends LinearLayout {

    private Context mContext;

    private SimpleDraweeView mDraweeView;
    private TextView mTextView;

    public ShoppingTopicBanner(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    private void initView() {
        int padding = ResourceHelper.getDimen(R.dimen.space_15);
        setOrientation(VERTICAL);
        setBackgroundResource(R.color.white);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.bottomMargin = padding / 3;
        setLayoutParams(layoutParams);

        int screenWidth = HardwareUtil.screenWidth;
        mDraweeView = new SimpleDraweeView(mContext);
        mDraweeView.setAspectRatio(2.14f);
        addView(mDraweeView, new LayoutParams(screenWidth, (int) (screenWidth / 2.14f)));

        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setPadding(padding, padding, padding, padding);

        mTextView = new TextView(mContext);
        mTextView.setTextColor(ResourceHelper.getColor(R.color.title_text_color));
        mTextView.setGravity(Gravity.LEFT);
        linearLayout.addView(mTextView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        addView(linearLayout);
    }

    public void setupItems(String imageUrl, String desc) {
        if (StringUtils.isNotEmpty(imageUrl)) {
            mDraweeView.setImageURI(Uri.parse(imageUrl));
        }
        if (StringUtils.isNotEmpty(desc)) {
            mTextView.setText(desc);
        }
    }
}
