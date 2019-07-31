package com.laka.live.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.laka.live.shopping.activity.ShoppingGoodsDetailActivity;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsBaseBean;
import com.laka.live.ui.activity.IncomeGoodsDetailActivity;
import com.laka.live.ui.widget.IncomeGoodsView;

/**
 * @ClassName: CourseIncomeAdapter
 * @Description: 课程收益
 * @Author: chuan
 * @Version: 1.0
 * @Date: 25/07/2017
 */

public class GoodsIncomeAdapter extends BaseAdapter<ShoppingGoodsBaseBean, GoodsIncomeAdapter.ViewHolder> {

    private Context mContext;

    public GoodsIncomeAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(new IncomeGoodsView(mContext, IncomeGoodsView.FROM_GOODS_LIST));
    }

    public static class ViewHolder extends BaseAdapter.ViewHolder<ShoppingGoodsBaseBean> {
        private IncomeGoodsView mIncomeGoodsView;

        public ViewHolder(View itemView) {
            super(itemView);

            if (itemView instanceof IncomeGoodsView) {
                mIncomeGoodsView = (IncomeGoodsView) itemView;
            }
        }

        @Override
        public void update(BaseAdapter adapter, int position, final ShoppingGoodsBaseBean goodsBaseBean) {
            if (mIncomeGoodsView != null) {
                mIncomeGoodsView.updateData(goodsBaseBean);
                mIncomeGoodsView.setOnViewClickListener(new IncomeGoodsView.OnViewClickListener() {
                    @Override
                    public void onTitleClick() {
                        ShoppingGoodsDetailActivity.startActivity(mIncomeGoodsView.getContext(), goodsBaseBean.getGoodsId());
                    }

                    @Override
                    public void onThumbClick() {
                        ShoppingGoodsDetailActivity.startActivity(mIncomeGoodsView.getContext(), goodsBaseBean.getGoodsId());
                    }

                    @Override
                    public void onMoreClick() {
                        IncomeGoodsDetailActivity.startActivity(mIncomeGoodsView.getContext(), goodsBaseBean.getGoodsId(), goodsBaseBean.getIncomeType());
                    }
                });
            }
        }
    }
}
