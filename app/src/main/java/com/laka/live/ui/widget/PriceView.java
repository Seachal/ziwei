package com.laka.live.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.TypefaceHelper;
import com.laka.live.util.Utils;

/**
 * @ClassName: PriceView
 * @Description: 显示价格的控件
 * @Author: chuan
 * @Version: 1.0
 * @Date: 6/6/17
 */

public class PriceView extends RelativeLayout {
    private final static int DEFAULT_PRICE_COLOR = 0xFFFF950B;
    private final static int DEFAULT_WEIDOU_COLOR = 0xFFFF950B;
    private final static int DEFAULT_FREE_COLOR = 0xFFFF950B;
    private final static int DEFAULT_ORIGINAL_COLOR = 0xFF848484;

    private final static int DEFAULT_PRICE_TEXTSIZE = 30;
    private final static int DEFAULT_WEIDOU_TEXTSIZE = 12;
    private final static int DEFAULT_FREE_TEXTSIZE = 14;
    private final static int DEFAULT_ORIGINAL_TEXTSIZE = 12;

    private TextView mMoneyTv;
    //    private View mDividerView;
    private TextView mWeidouTv;
    private TextView mFreeTv;
    private TextView mOriginalTv;

    public PriceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PriceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.view_weidou, this);

        mMoneyTv = (TextView) findViewById(R.id.money_tv);
//        mDividerView = findViewById(R.id.divider_view);
        mWeidouTv = (TextView) findViewById(R.id.weidou_tv);
        mFreeTv = (TextView) findViewById(R.id.free_tv);
        mOriginalTv = (TextView) findViewById(R.id.original_tv);

        Typeface fontFace = TypefaceHelper.getInstance().getTypeface(TypefaceHelper.TYPE_FACE_BLACK_JACK);
        if (fontFace == null) {
            fontFace = Typeface.createFromAsset(context.getAssets(),
                    "fonts/BlackJack.TTF");
            TypefaceHelper.getInstance().putTypeface(TypefaceHelper.TYPE_FACE_BLACK_JACK, fontFace);
        }

        mMoneyTv.setTypeface(fontFace);

        mOriginalTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PriceView);
        if (typedArray == null) {
            return;
        }

        mMoneyTv.setTextColor(typedArray.getColor(R.styleable.PriceView_priceColor, DEFAULT_PRICE_COLOR));
        mMoneyTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(R.styleable.PriceView_priceTextSize, Utils.dp2px(context, DEFAULT_PRICE_TEXTSIZE)));

        int weidouColor = typedArray.getColor(R.styleable.PriceView_weidouColor, DEFAULT_WEIDOU_COLOR);
        mWeidouTv.setTextColor(weidouColor);
        GradientDrawable drawable = (GradientDrawable) ResourceHelper.getDrawable(R.drawable.drawable_divider_price);
        drawable.setColor(weidouColor);
        mWeidouTv.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);
//        mDividerView.setBackgroundColor(weidouColor);
        mWeidouTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(R.styleable.PriceView_weidouTextSize, Utils.dp2px(context, DEFAULT_WEIDOU_TEXTSIZE)));

        mFreeTv.setTextColor(typedArray.getColor(R.styleable.PriceView_freeColor, DEFAULT_FREE_COLOR));
        mFreeTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(R.styleable.PriceView_freeTextSize, Utils.dp2px(context, DEFAULT_FREE_TEXTSIZE)));

        mOriginalTv.setTextColor(typedArray.getColor(R.styleable.PriceView_originalColor, DEFAULT_ORIGINAL_COLOR));
        mOriginalTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(R.styleable.PriceView_originalTextSize, Utils.dp2px(context, DEFAULT_ORIGINAL_TEXTSIZE)));
        typedArray.recycle();
    }

    public void setPrice(float price) {
        this.setPrice(price, 0);
    }

    public void setPrice(float price, float originalPrice) {
        if (price <= 0) {
            mFreeTv.setVisibility(VISIBLE);
            mMoneyTv.setVisibility(GONE);
            mWeidouTv.setVisibility(GONE);
//            mDividerView.setVisibility(GONE);
            mOriginalTv.setVisibility(GONE);
        } else {
            mFreeTv.setVisibility(GONE);
            mMoneyTv.setVisibility(VISIBLE);
            mWeidouTv.setVisibility(VISIBLE);
//            mDividerView.setVisibility(VISIBLE);

            if ((price - (int) price) == 0) {
                mMoneyTv.setText(String.valueOf((int) price));
            } else {
                mMoneyTv.setText(String.valueOf(price));
            }

            if (originalPrice <= 0) {
                mOriginalTv.setVisibility(GONE);
                return;
            }

            mOriginalTv.setVisibility(VISIBLE);

            if ((originalPrice - (int) originalPrice) == 0) {
                mOriginalTv.setText(ResourceHelper.getString(R.string.original_price, (int) originalPrice));
            } else {
                mOriginalTv.setText(ResourceHelper.getString(R.string.original_price, originalPrice));
            }

        }
    }
}
