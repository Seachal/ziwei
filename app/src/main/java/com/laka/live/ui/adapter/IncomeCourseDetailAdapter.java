package com.laka.live.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.bean.IncomeCourseInfo;
import com.laka.live.ui.activity.IncomeCourseDetailInfoActivity;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.Utils;

import java.util.Date;

/**
 * @ClassName: IncomeCourseDetailAdapter
 * @Description: 课程收益详情
 * @Author: chuan
 * @Version: 1.0
 * @Date: 25/07/2017
 */

public class IncomeCourseDetailAdapter extends BaseAdapter<IncomeCourseInfo, IncomeCourseDetailAdapter.ViewHolder> {

    private Context mContext;
    private int mIncomeType;

    public IncomeCourseDetailAdapter(Context context, int incomeType) {
        this.mContext = context;
        this.mIncomeType = incomeType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_income_info, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends BaseAdapter.ViewHolder<IncomeCourseInfo> {
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
        public void update(BaseAdapter adapter, int position, IncomeCourseInfo incomeCourseInfo) {
            if (incomeCourseInfo == null) {
                return;
            }

            mDateTv.setText(Utils.YMD_DATE_FORMATER.format(new Date(incomeCourseInfo.getBuyerTime() * 1000)));
            mNicknameTv.setText(incomeCourseInfo.getBuyerName());
            mIncomeTv.setText(ResourceHelper.getString(R.string.income_add_string, Utils.float2String(incomeCourseInfo.getIncome())));
        }
    }

    @Override
    protected void onItemClick(int position) {
        super.onItemClick(position);

        IncomeCourseInfo incomeDetail = getItem(position);
        if (incomeDetail == null) {
            return;
        }

        IncomeCourseDetailInfoActivity.startActivity(mContext, incomeDetail.getId(), mIncomeType);
    }
}
