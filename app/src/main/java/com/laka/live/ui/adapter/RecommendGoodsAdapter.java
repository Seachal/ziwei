package com.laka.live.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.laka.live.R;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsBaseBean;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.ViewUtils;

/**
 * Created by Lyf on 2017/7/21.
 */
public class RecommendGoodsAdapter extends BaseAdapter<ShoppingGoodsBaseBean, BaseAdapter.ViewHolder> {


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecommendViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recommend_goods, null, false));
    }


    private class RecommendViewHolder extends BaseAdapter.ViewHolder<ShoppingGoodsBaseBean> {

        private ImageView deleteGoods;
        private ImageView goodsCover;

        public RecommendViewHolder(View itemView) {
            super(itemView);

            goodsCover = ViewUtils.findById(itemView, R.id.goodsCover);
            deleteGoods = ViewUtils.findById(itemView, R.id.deleteGoods);

        }

        @Override
        public void update(BaseAdapter adapter, final int position, final ShoppingGoodsBaseBean goods) {

            ImageUtil.displayImage(goodsCover,goods.getThumbUrl(),R.drawable.blank_icon_bigimages);

            deleteGoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBusManager.postEvent(goods, SubcriberTag.REMOVE_GOODS);
                }
            });

        }

    }

    public void remove(ShoppingGoodsBaseBean goods) {
        int position = -1;
        for (ShoppingGoodsBaseBean temp : getmDatas()) {
            ++position;
            if (temp.getGoodsId() == goods.getGoodsId()) {
                remove(position);
                break;
            }
        }
    }

}
