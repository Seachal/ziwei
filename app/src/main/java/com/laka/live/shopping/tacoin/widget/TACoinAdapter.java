package com.laka.live.shopping.tacoin.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.laka.live.R;
import com.laka.live.shopping.bean.TACoinIncomeBean;
import com.laka.live.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gangqing on 2016/3/11.
 * Email:denggangqing@ta2she.com
 */
public class TACoinAdapter extends BaseAdapter {
    private Context mContext;
    private List<TACoinIncomeBean.Data.CoinDetail> mDataList = new ArrayList<TACoinIncomeBean.Data.CoinDetail>();
    public static final int TYPE_INCOME = 1;
    public static final int TYPE_DISBURSE = 2;
    private int mType;

    public TACoinAdapter(Context context, List<TACoinIncomeBean.Data.CoinDetail> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
    }

    public void setType(int type) {
        mType = type;
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.ta_coin_item_layout, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.ta_coin_income_title);
            holder.count = (TextView) convertView.findViewById(R.id.ta_coin_income_count);
            holder.description = (TextView) convertView.findViewById(R.id.ta_coin_income_description);
            holder.createTime = (TextView) convertView.findViewById(R.id.ta_coin_income_create_time);
            holder.indate = (TextView) convertView.findViewById(R.id.ta_coin_income_indate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        setData(holder, position);
        return convertView;
    }

    public void setData(ViewHolder holder, int position) {
        TACoinIncomeBean.Data.CoinDetail data = mDataList.get(position);
        holder.title.setText(data.title);
        if (mType == TYPE_INCOME) {
            holder.count.setText("+" + data.coinCount);
        } else {
            holder.count.setText("-" + data.coinCount);
        }
        if (StringUtils.isNotEmpty(data.des)) {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(data.des);
        } else {
            holder.description.setVisibility(View.GONE);
        }
        holder.createTime.setText(data.createtime);
        holder.indate.setText("有效期：" + data.expiredTime);
    }

    class ViewHolder {
        public TextView title;
        public TextView count;
        public TextView description;
        public TextView createTime;
        public TextView indate;
    }
}
