package com.laka.live.shopping.search.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.shopping.search.info.SearchConstant;
import com.laka.live.util.ResourceHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gangqing on 2016/4/22.
 * Email:denggangqing@ta2she.com
 */
public class SearchClassifyTitleView extends FrameLayout implements View.OnClickListener {

    private List<TextView> mTitleList = new ArrayList<>();
    private TextView mComprehensive;
    private TextView mSales;
    private TextView mNew;
    private TextView mPrice;

    private static final int TYPE_PRICE_DEFAULT = 0;
    private static final int TYPE_PRICE_UP = 1;
    private static final int TYPE_PRICE_DOWN = 2;

    private ClickCallBack mClickCallBack;
    private int mCurrentClickTitleType;
    private int mCurrentPriceType = TYPE_PRICE_DEFAULT;

    public SearchClassifyTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SearchClassifyTitleView(Context context) {
        this(context, null);
    }

    private void initView() {
        View rootView = View.inflate(getContext(), R.layout.classify_view_layout, null);
        mComprehensive = (TextView) rootView.findViewById(R.id.classify_comprehensive);
        mSales = (TextView) rootView.findViewById(R.id.classify_sales);
        mNew = (TextView) rootView.findViewById(R.id.classify_new);
        mPrice = (TextView) rootView.findViewById(R.id.classify_price);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(rootView, params);

        mTitleList.clear();
        mTitleList.add(mComprehensive);
        mTitleList.add(mSales);
        mTitleList.add(mNew);
        mTitleList.add(mPrice);
        setType(0);

        mComprehensive.setTag(0);
        mSales.setTag(1);
        mNew.setTag(2);
        mPrice.setTag(3);
        mComprehensive.setOnClickListener(this);
        mSales.setOnClickListener(this);
        mNew.setOnClickListener(this);
        mPrice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        switch (v.getId()) {
            case R.id.classify_comprehensive:
                mCurrentClickTitleType = SearchConstant.TYPE_COMPREHENSIVE;
                break;
            case R.id.classify_sales:
                mCurrentClickTitleType = SearchConstant.TYPE_SALES;
                break;
            case R.id.classify_new:
                mCurrentClickTitleType = SearchConstant.TYPE_NEW;
                break;
        }
        setType(position);
        if (mClickCallBack != null) {
            mClickCallBack.onClick(mCurrentClickTitleType, ((TextView) v).getText().toString());
        }
    }

    private void setType(int position) {
        int size = mTitleList.size();
        for (int i = 0; i < size; i++) {
            TextView textView = mTitleList.get(i);
            textView.setTextColor(ResourceHelper.getColor(R.color.classify_title_color));
            if (i == position && position == size - 1) {
                textView.setTextColor(ResourceHelper.getColor(R.color.classify_select_title_color));
                mCurrentPriceType = mCurrentPriceType == TYPE_PRICE_UP ? TYPE_PRICE_DOWN : TYPE_PRICE_UP;
                setPriceType();
            } else if (i == position && position != size - 1) {
                textView.setTextColor(ResourceHelper.getColor(R.color.classify_select_title_color));
                mCurrentPriceType = TYPE_PRICE_DEFAULT;
                setPriceType();
            }
        }
    }

    private void setPriceType() {
        Drawable drawable = null;
        switch (mCurrentPriceType) {
            case TYPE_PRICE_DEFAULT:
                drawable = ResourceHelper.getDrawable(R.drawable.mall_icon_normal);
                break;
            case TYPE_PRICE_UP:
                mCurrentClickTitleType = SearchConstant.TYPE_UP_PRICE;
                drawable = ResourceHelper.getDrawable(R.drawable.mall_icon_asc);
                break;
            case TYPE_PRICE_DOWN:
                mCurrentClickTitleType = SearchConstant.TYPE_DOWN_PRICE;
                drawable = ResourceHelper.getDrawable(R.drawable.mall_icon_desc);
                break;
        }
        mPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
    }

    public void setClickCallBack(ClickCallBack callBack) {
        mClickCallBack = callBack;
    }

    public interface ClickCallBack {
        void onClick(int type, String title);
    }
}
