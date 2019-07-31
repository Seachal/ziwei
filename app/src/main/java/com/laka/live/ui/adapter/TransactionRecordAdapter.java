package com.laka.live.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.bean.TransactionRecord;
import com.laka.live.util.ResourceHelper;

/**
 * Created by luwies on 16/4/6.
 */
public class TransactionRecordAdapter extends BaseAdapter<TransactionRecord, TransactionRecordAdapter.ViewHolder> {


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends BaseAdapter.ViewHolder<TransactionRecord> {

        private LinearLayout mHeadLl;
        private TextView mHeadTv;
        private TextView mOperationTv;
        private TextView mTimeTv;
        private TextView mTransTv;
        private TextView mBalanceTv;

        public ViewHolder(View itemView) {
            super(itemView);

            mHeadLl = (LinearLayout) itemView.findViewById(R.id.head_ll);
            mHeadTv = (TextView) itemView.findViewById(R.id.head_tv);
            mOperationTv = (TextView) itemView.findViewById(R.id.operation_tv);
            mTimeTv = (TextView) itemView.findViewById(R.id.time_tv);
            mTransTv = (TextView) itemView.findViewById(R.id.trans_tv);
            mBalanceTv = (TextView) itemView.findViewById(R.id.balance_tv);
        }

        @Override
        public void update(BaseAdapter adapter, int position, TransactionRecord transactionRecord) {
            if (transactionRecord == null) {
                itemView.setVisibility(View.GONE);
                return;
            }

            boolean isShowHead = false;
            if (position == 0) {
                isShowHead = true;
            } else {
                TransactionRecord lastRecord = (TransactionRecord) adapter.getItem(position - 1);
                if (lastRecord.getYear() != transactionRecord.getYear() || lastRecord.getMonth() !=
                        transactionRecord.getMonth()) {
                    isShowHead = true;
                }
            }

            if (isShowHead) {
                mHeadLl.setVisibility(View.VISIBLE);
                mHeadTv.setText(ResourceHelper.getString(R.string.year_month_format, transactionRecord.getYear(),
                        transactionRecord.getMonth()));
            } else {
                mHeadLl.setVisibility(View.GONE);
            }

            mOperationTv.setText(transactionRecord.getChannelStr() + "-" + transactionRecord.getSummary());
            mTimeTv.setText(transactionRecord.getTime());

            StringBuilder transCoinsBuilder = new StringBuilder();

            if (transactionRecord.getTransCoins() > 0) {
                mTransTv.setTextColor(ResourceHelper.getColor(R.color.color3BC36B));
                transCoinsBuilder.append("+");
            } else if (transactionRecord.getTransCoins() == 0) {
                mTransTv.setTextColor(ResourceHelper.getColor(R.color.color555555));
            } else {
                mTransTv.setTextColor(ResourceHelper.getColor(R.color.colorF65843));
            }

            if (transactionRecord.getTransCoins() - (int) transactionRecord.getTransCoins() == 0) {
                transCoinsBuilder.append((int) transactionRecord.getTransCoins());
            } else {
                transCoinsBuilder.append(transactionRecord.getTransCoins());
            }

            switch (transactionRecord.getChannel()) {
                case TransactionRecord.CHANNEL_MY_WALLET:
                    mTransTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ResourceHelper.getDrawable(R.drawable.jyjl_icon_bean), null);
                    break;
                default:
                    mTransTv.setCompoundDrawables(null, null, null, null);
                    break;
            }

            mTransTv.setText(transCoinsBuilder.toString());

            if (transactionRecord.getRestCoins() - (int) transactionRecord.getRestCoins() == 0) {
                mBalanceTv.setText(transactionRecord.getChannelStr2() + ": "
                        + (int) transactionRecord.getRestCoins());
            } else {
                mBalanceTv.setText(transactionRecord.getChannelStr2() + ": "
                        + transactionRecord.getRestCoins());
            }

        }

    }
}
