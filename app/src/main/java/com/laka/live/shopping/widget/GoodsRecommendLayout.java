package com.laka.live.shopping.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.shopping.activity.ShoppingGoodsDetailActivity;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsBaseBean;
import com.laka.live.ui.widget.FlowLayout;
import com.laka.live.ui.widget.LoadingLayout;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.RippleEffectHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.Utils;

import java.util.List;


/**
 * @ClassName: GoodsRecommendLayout
 * @Description: 商品推荐
 * @Author: chuan
 * @Version: 1.0
 * @Date: 27/07/2017
 */

public class GoodsRecommendLayout extends FrameLayout {

    private LoadingLayout mLoadingLayout;
    private FlowLayout mFlowLayout;

    private boolean mHasRecommend = false;

    public GoodsRecommendLayout(Context context) {
        this(context, null);
    }

    public GoodsRecommendLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoodsRecommendLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        addFlowLayout();
        addLoadingLayout();
    }

    private void addLoadingLayout() {
        mLoadingLayout = new LoadingLayout(getContext());
        mLoadingLayout.setVisibility(VISIBLE);
        LayoutParams flp = new LayoutParams(LayoutParams.MATCH_PARENT, ResourceHelper.getDimen(R.dimen.space_360));
        addView(mLoadingLayout, flp);
    }

    private void addFlowLayout() {
        mFlowLayout = new FlowLayout(getContext());
        LayoutParams flp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        flp.gravity = Gravity.CENTER_HORIZONTAL;
        addView(mFlowLayout, flp);
    }

    public boolean hasRecommend() {
        return mHasRecommend;
    }

    public void setupGoodsRecommendedLayout(List<ShoppingGoodsBaseBean> goodsListBean) {
        if (Utils.listIsNullOrEmpty(goodsListBean)) {
            mLoadingLayout.setDefaultNoData();
            mHasRecommend = false;
            return;
        }

        mLoadingLayout.setVisibility(GONE);

        mHasRecommend = true;

        mFlowLayout.removeAllViewsInLayout();
        View itemView;
        MaterialRippleLayout rippleLayout;
        for (int i = 0; i < goodsListBean.size(); i++) {
            itemView = createRecommendItemView(goodsListBean.get(i));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT);
            if (i % 2 == 0) {
                lp.rightMargin = 0;
            } else {
                lp.rightMargin = Utils.dip2px(getContext(), -5);
            }
            rippleLayout = RippleEffectHelper.addRippleEffectInView(itemView);
            mFlowLayout.addView(rippleLayout, lp);
        }
    }

    private View createRecommendItemView(final ShoppingGoodsBaseBean goodsBean) {
        View view = inflate(getContext(), R.layout.item_shopping_good, null);

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handleGoodsItemClick(goodsBean);
            }
        });

        SimpleDraweeView thumbSDV = (SimpleDraweeView) view.findViewById(R.id.goods_image);
        TextView nameTv = (TextView) view.findViewById(R.id.goods_name);
        TextView priceTv = (TextView) view.findViewById(R.id.goods_price);
        TextView tagsTv = (TextView) view.findViewById(R.id.goods_tags);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) thumbSDV.getLayoutParams();
        params.width = Utils.getScreenWidth(view.getContext()) / 2 - Utils.dip2px(view.getContext(), 5);
        params.height = params.width;
        thumbSDV.setLayoutParams(params);

        if (StringUtils.isNotEmpty(goodsBean.getThumbUrl())) {
            ImageUtil.loadImage(thumbSDV, goodsBean.getThumbUrl());
        }

        if (StringUtils.isNotEmpty(goodsBean.getTitle())) {
            nameTv.setText(goodsBean.getTitle());
        }

        priceTv.setText(ResourceHelper.getString(R.string.search_goods_price, String.valueOf(goodsBean.getSalePrice())));

        if (!Utils.listIsNullOrEmpty(goodsBean.getTags()) &&
                goodsBean.getTags().contains("包邮")) {
            tagsTv.setText("包邮");
            tagsTv.setVisibility(View.VISIBLE);
        } else {
            tagsTv.setVisibility(View.GONE);
        }
        return view;
    }

    private void handleGoodsItemClick(ShoppingGoodsBaseBean goodsBean) {
        ShoppingGoodsDetailActivity.startActivity(getContext(), goodsBean.getGoodsId());
    }
}
