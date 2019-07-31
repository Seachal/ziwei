package com.laka.live.ui.room;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsDetailBean;
import com.laka.live.shopping.widget.PriceView;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.util.ImageUtil;

/**
 * Created by luwies on 16/10/31.
 */

public class GoodsPanelAdapter extends BaseAdapter<ShoppingGoodsDetailBean, GoodsPanelAdapter.ViewHolder> {


    private Context mContext;


    public GoodsPanelAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_good_panel, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
//        holder.itemView.setOnClickListener(null);
    }


    class ViewHolder extends BaseAdapter.ViewHolder<ShoppingGoodsDetailBean> implements View.OnClickListener {
        SimpleDraweeView ivPic;
        TextView tvName;
        PriceView tvPrice, tvOldPrice;
        Button btnIndex, btnAddCar;
        ShoppingGoodsDetailBean curItem;

        public ViewHolder(View itemView) {
            super(itemView);
//            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) itemView.getLayoutParams();
//            lp.height = Utils.dip2px(mContext,104);
//            itemView.setLayoutParams(lp);
            ivPic = (SimpleDraweeView) itemView.findViewById(R.id.iv_pic);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvPrice = (PriceView) itemView.findViewById(R.id.tv_price);
            tvOldPrice = (PriceView) itemView.findViewById(R.id.tv_old_price);
            tvOldPrice.setCenterLine();
            btnIndex = (Button) itemView.findViewById(R.id.btn_index);
            btnAddCar = (Button) itemView.findViewById(R.id.btn_add_car);
            btnAddCar.setOnClickListener(this);
        }

        @Override
        public void update(BaseAdapter adapter, int position, ShoppingGoodsDetailBean item) {
            curItem = item;
            ImageUtil.loadImage(ivPic,item.getThumbUrl());
            tvName.setText(item.getTitle());
            tvPrice.setText(item.getSalePrice()+"");
            tvOldPrice.setText(item.getMarketPrice()+"");
            btnIndex.setText(String.valueOf(position+1));
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_add_car:
                    EventBusManager.postEvent(curItem, SubcriberTag.SHOW_GOOD_BUY_PANEL);
                    break;
            }
        }
    }
}
