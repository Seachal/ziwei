package com.laka.live.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsIncomeDetail;
import com.laka.live.ui.activity.IncomeGoodsDetailInfoActivity;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;

import java.util.Date;

/**
 * @ClassName: IncomeGoodsDetailAdapter
 * @Description: 商品收益详情
 * @Author: chuan
 * @Version: 1.0
 * @Date: 25/07/2017
 */

public class IncomeGoodsDetailAdapter extends BaseAdapter<ShoppingGoodsIncomeDetail, IncomeGoodsDetailAdapter.ViewHolder> {

    private Context mContext;
    private int mIncomeType;

    public IncomeGoodsDetailAdapter(Context context, int incomeType) {
        this.mContext = context;
        this.mIncomeType = incomeType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_income_info, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends BaseAdapter.ViewHolder<ShoppingGoodsIncomeDetail> {
        private TextView mDateTv;
        private TextView mNicknameTv;
        private TextView mIncomeTv;

        public ViewHolder(View itemView) {
            super(itemView);

            mDateTv = (TextView) itemView.findViewById(R.id.date_tv);
            mNicknameTv = (TextView) itemView.findViewById(R.id.nickname_tv);
            mIncomeTv = (TextView) itemView.findViewById(R.id.income_tv);
        }

        @Override
        public void update(BaseAdapter adapter, int position, ShoppingGoodsIncomeDetail shoppingGoodsIncomeDetail) {
            if (shoppingGoodsIncomeDetail == null) {
                return;
            }

            mDateTv.setText(Utils.YMD_DATE_FORMATER.format(new Date(shoppingGoodsIncomeDetail.getCreateTime() * 1000)));
            mNicknameTv.setText(shoppingGoodsIncomeDetail.getNickName());
            mIncomeTv.setText(ResourceHelper.getString(R.string.income_add, Utils.float2String(shoppingGoodsIncomeDetail.getIncome())));
        }
    }

    @Override
    protected void onItemClick(int position) {
        super.onItemClick(position);

        ShoppingGoodsIncomeDetail incomeDetail = getItem(position);
        if (incomeDetail == null) {
            return;
        }

        IncomeGoodsDetailInfoActivity.startActivity(mContext, incomeDetail.getId(), mIncomeType);
    }
}
