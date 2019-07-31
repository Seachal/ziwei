package com.laka.live.ui.viewholder;


import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.BuildConfig;
import com.laka.live.R;
import com.laka.live.bean.Goods;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.SubcriberTag;
import com.laka.live.shopping.activity.ShoppingGoodsDetailActivity;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsBaseBean;
import com.laka.live.shopping.widget.PriceView;
import com.laka.live.ui.adapter.BaseAdapter;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.ViewUtils;

import java.util.List;

/**
 * Created by lyf on 2017/7/22.
 */
public class GoodsViewHolder extends BaseAdapter.ViewHolder<ShoppingGoodsBaseBean> {

    private TextView goodsName;
    private TextView profitShare;
    private PriceView goodsPrice;
    private PriceView strikePrice;
    private CheckBox checkBox;
    private SimpleDraweeView goodsCover;
    private List<ShoppingGoodsBaseBean> checkedList;
    private final int MAX_RECOMMEND_GOODS = BuildConfig.DEBUG ? 10 : 30; //推荐商品的最多个数

    public GoodsViewHolder(View root, List<ShoppingGoodsBaseBean> checkedList) {
        super(root);

        this.checkedList = checkedList;
        checkBox = ViewUtils.findById(root, R.id.checkBox);
        goodsName = ViewUtils.findById(root, R.id.goodsName);
        goodsCover = ViewUtils.findById(root, R.id.goodsCover);
        goodsPrice = ViewUtils.findById(root, R.id.goodsPrice);
        profitShare = ViewUtils.findById(root, R.id.profitShare);
        strikePrice = ViewUtils.findById(root, R.id.strikePrice);
    }

    @Override
    public void update(BaseAdapter adapter, final int position, final ShoppingGoodsBaseBean goods) {

        goodsPrice.setText(String.valueOf(goods.getSalePrice()));
        strikePrice.setText(String.valueOf(goods.getMarketPrice()));
        profitShare.setText(goods.getPromoteIncomeFormat());
        goodsName.setText(goods.getGoodsName());
        ImageUtil.displayImage(goodsCover, goods.getThumbUrl(), R.drawable.blank_icon_bigimages);

        if (contains(goods)) {
            checkBox.setChecked(true);
            checkBox.setText("移除推荐");
        } else {
            checkBox.setChecked(false);
            checkBox.setText("加入推荐");
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (contains(goods)) {
                    checkBox.setText("加入推荐");
                    checkedList.remove(goods);
                    EventBusManager.postEvent(goods, SubcriberTag.REMOVE_GOODS);
                } else {
                    if (checkedList.size() >= MAX_RECOMMEND_GOODS) {
                        ToastHelper.showToast("最多只能添加" + MAX_RECOMMEND_GOODS + "个推荐商品");
                        checkBox.setChecked(false);
                        return;
                    }
                    checkedList.add(goods);
                    checkBox.setText("移除推荐");
                    EventBusManager.postEvent(goods, SubcriberTag.ADD_GOODS);
                }

            }
        });

        goodsName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingGoodsDetailActivity.startActivity(itemView.getContext(), goods.getGoodsId());
            }
        });

        goodsCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingGoodsDetailActivity.startActivity(itemView.getContext(), goods.getGoodsId());
            }
        });

    }


    private boolean contains(ShoppingGoodsBaseBean goods) {

        boolean checked = false;

        for (ShoppingGoodsBaseBean temp : checkedList) {

            if (temp.getGoodsId() == goods.getGoodsId()) {
                checked = true;
                break;
            }

        }

        return checked;
    }


}
