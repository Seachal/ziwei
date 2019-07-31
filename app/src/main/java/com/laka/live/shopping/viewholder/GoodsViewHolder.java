package com.laka.live.shopping.viewholder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.shopping.activity.ShoppingGoodsDetailActivity;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsBaseBean;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.util.FastClickUtil;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.Utils;

/**
 * @ClassName: GoodsViewHolder
 * @Description: 商品
 * @Author: chuan
 * @Version: 1.0
 * @Date: 26/07/2017
 */

public class GoodsViewHolder extends BaseAdapter.ViewHolder<ShoppingGoodsBaseBean> implements View.OnClickListener {

    private int relativeId = -1;
    private SimpleDraweeView mThumbSDV;
    private TextView mNameTv, mPriceTv, mTagsTv;
    private ShoppingGoodsBaseBean mGoodsBean;
    private boolean isCourse = true;

    public GoodsViewHolder(View itemView, int courseId) {
        this(itemView, courseId, true);
    }

    public GoodsViewHolder(View itemView, int relativeId, boolean isCourse) {
        this(itemView);
        this.relativeId = relativeId;
        this.isCourse = isCourse;
    }

    public GoodsViewHolder(View itemView) {
        super(itemView);
        mThumbSDV = (SimpleDraweeView) itemView.findViewById(R.id.goods_image);
        mNameTv = (TextView) itemView.findViewById(R.id.goods_name);
        mPriceTv = (TextView) itemView.findViewById(R.id.goods_price);
        mTagsTv = (TextView) itemView.findViewById(R.id.goods_tags);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mThumbSDV.getLayoutParams();
        params.width = (Utils.getScreenWidth(itemView.getContext()) - Utils.dip2px(itemView.getContext(), 5)) / 2;
        params.height = params.width;
        mThumbSDV.setLayoutParams(params);

        itemView.findViewById(R.id.content_ll).setOnClickListener(this);
    }

    @Override
    public void update(BaseAdapter adapter, int position, final ShoppingGoodsBaseBean baseGoodsBean) {
        mGoodsBean = baseGoodsBean;

        if (StringUtils.isNotEmpty(baseGoodsBean.getThumbUrl())) {
            ImageUtil.loadImage(mThumbSDV, baseGoodsBean.getThumbUrl());
        }

        if (StringUtils.isNotEmpty(baseGoodsBean.getTitle())) {
            mNameTv.setText(baseGoodsBean.getTitle());
        }

        mPriceTv.setText(ResourceHelper.getString(R.string.search_goods_price, String.valueOf(baseGoodsBean.getSalePrice())));

        StringBuilder tags = new StringBuilder();
        if (!Utils.listIsNullOrEmpty(baseGoodsBean.getTags())) {
            for (String tag : baseGoodsBean.getTags()) {
                tags.append(tag);
            }
        }

        mTagsTv.setText(tags.toString());
    }

    @Override
    public void onClick(View v) {
        if (mGoodsBean != null) {
            if (FastClickUtil.getInstance().isFastClick()) {
                return;
            }
            ShoppingGoodsDetailActivity.startActivity(itemView.getContext(), mGoodsBean.getGoodsId(), relativeId, isCourse);
        }
    }
}
