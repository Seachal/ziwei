package com.laka.live.ui.widget;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsBaseBean;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;

/**
 * @ClassName:
 * @Description:
 * @Author: chuan
 * @Version: 1.0
 * @Date: 28/08/2017
 */

public class IncomeGoodsView extends RelativeLayout implements View.OnClickListener {
    public final static int FROM_GOODS_DETAIL = 0;
    public final static int FROM_GOODS_LIST = 1;

    private SimpleDraweeView mThumbSdv;
    private TextView mTitleTv;
    private TextView mOriginalTv;
    private TextView mPriceTv;
    private TextView mIncomeTv;
    private TextView mBuyerCountTv;
    private LinearLayout mMoreLl;

    private int mFrom = FROM_GOODS_DETAIL;

    private OnViewClickListener mListener;

    public interface OnViewClickListener {
        void onTitleClick();

        void onThumbClick();

        void onMoreClick();
    }

    public IncomeGoodsView(Context context, int from) {
        super(context);
        this.mFrom = from;

        initUI(context);
    }

    public IncomeGoodsView(Context context) {
        this(context, null);
    }

    public IncomeGoodsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IncomeGoodsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initUI(context);
    }

    private void initUI(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_income_goods, this);
        setBackgroundColor(ResourceHelper.getColor(R.color.white));
        setPadding(0, 0, Utils.dip2px(context, 15), 0);

        mThumbSdv = (SimpleDraweeView) findViewById(R.id.thumb_sdv);
        mTitleTv = (TextView) findViewById(R.id.title_tv);
        mPriceTv = (TextView) findViewById(R.id.price_tv);
        mOriginalTv = (TextView) findViewById(R.id.original_tv);
        mOriginalTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        mIncomeTv = (TextView) findViewById(R.id.income_tv);
        mBuyerCountTv = (TextView) findViewById(R.id.buyer_count_tv);
        mMoreLl = (LinearLayout) findViewById(R.id.more_ll);

        if (mFrom == FROM_GOODS_DETAIL) {
            mMoreLl.setVisibility(GONE);
        } else {
            mThumbSdv.setOnClickListener(this);
            mTitleTv.setOnClickListener(this);
            mMoreLl.setOnClickListener(this);
        }

    }

    public void setOnViewClickListener(OnViewClickListener listener) {
        this.mListener = listener;
    }

    public void updateData(ShoppingGoodsBaseBean goodsBaseBean) {
        if (goodsBaseBean == null) {
            setVisibility(GONE);
            return;
        }

        setVisibility(VISIBLE);

        ImageUtil.loadImage(mThumbSdv, goodsBaseBean.getThumbUrl());
        mTitleTv.setText(goodsBaseBean.getTitle());
        mPriceTv.setText(ResourceHelper.getString(
                R.string.income_money, String.valueOf(goodsBaseBean.getSalePrice())));
        mOriginalTv.setText(ResourceHelper.getString(
                R.string.income_money, String.valueOf(goodsBaseBean.getMarketPrice())));
        mIncomeTv.setText(ResourceHelper.getString(
                R.string.income_money_type, goodsBaseBean.getIncomeTypeStr(), goodsBaseBean.getIncomeStr()));
        mBuyerCountTv.setText(ResourceHelper.getString(R.string.shopping_buyer_count, goodsBaseBean.getSaleCount()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.thumb_sdv:
                if (mListener != null) {
                    mListener.onThumbClick();
                }
                break;
            case R.id.title_tv:
                if (mListener != null) {
                    mListener.onTitleClick();
                }
                break;
            case R.id.more_ll:
                if (mListener != null) {
                    mListener.onMoreClick();
                }
                break;
            default:
                break;
        }
    }
}
