package com.laka.live.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.laka.live.R;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsBaseBean;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ViewUtils;

/**
 * Created by Lyf on 2017/7/20.
 */
public class HorizontalGoodsAdapter extends BaseAdapter<ShoppingGoodsBaseBean, BaseAdapter.ViewHolder> {


    private int goodsCoverWidth;

    public HorizontalGoodsAdapter(int screenWidth) {
        this.goodsCoverWidth = (screenWidth - ResourceHelper.getDimen(R.dimen.space_38)) / 2;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HorizontalViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_horizontal_goods, null, false));
    }


    private class HorizontalViewHolder extends BaseAdapter.ViewHolder<ShoppingGoodsBaseBean> {

        private TextView goodsName;
        private TextView goodsPrice;
        private ImageView goodsCover;

        public HorizontalViewHolder(final View itemView) {
            super(itemView);
            goodsName = ViewUtils.findById(itemView,R.id.goodsName);
            goodsPrice = ViewUtils.findById(itemView,R.id.goodsPrice);
            goodsCover = ViewUtils.findById(itemView, R.id.goodsCover);
            ViewGroup.LayoutParams layoutParams = goodsCover.getLayoutParams();
            layoutParams.height = layoutParams.width = goodsCoverWidth;
            goodsCover.setLayoutParams(layoutParams);
        }

        @Override
        public void update(BaseAdapter adapter, int position, final ShoppingGoodsBaseBean recommendGoods) {
            goodsPrice.setText("￥"+recommendGoods.getSalePrice());
            goodsName.setText(recommendGoods.getGoodsName());
            ImageUtil.displayImage(goodsCover,recommendGoods.getThumbUrl(),R.drawable.blank_icon_bigimages);
        }

    }

}
