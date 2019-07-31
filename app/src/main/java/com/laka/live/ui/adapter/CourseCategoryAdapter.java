package com.laka.live.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.bean.CourseCategoryTwoBean;
import com.laka.live.shopping.bean.newversion.GoodsCate;
import com.laka.live.util.StringUtils;

import java.util.List;

/**
 * Created by zhxu on 2016/5/3.
 * Email:357599859@qq.com
 */
public class CourseCategoryAdapter extends RecyclerView.Adapter<CourseCategoryAdapter.CategoriesViewHolder>
        implements View.OnClickListener {

    private Context mContext;
    private List<CourseCategoryTwoBean.Category> mList;

    private int mCurrPosition;

    public void setCallback(onItemClickListner mCallback) {
        this.mCallback = mCallback;
    }

    private onItemClickListner mCallback;

    public CourseCategoryAdapter(Context context, List<CourseCategoryTwoBean.Category> list) {
        mList = list;
        mContext = context;
    }


    @Override
    public CategoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoriesViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_course_category_cate, parent, false));
    }

    @Override
    public void onBindViewHolder(CategoriesViewHolder holder, int position) {
        CourseCategoryTwoBean.Category goodsCate = mList.get(position);

        if (StringUtils.isNotEmpty(goodsCate.getName())) {
            holder.nameTv.setText(goodsCate.getName());
        }
        holder.nameTv.setSelected(mCurrPosition == position);
        holder.nameTv.setTag(R.id.tag_category_position, position);
        holder.nameTv.setTag(goodsCate);
        holder.nameTv.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View v) {
        mCurrPosition = (int) v.getTag(R.id.tag_category_position);
        if (mCallback != null) {
            mCallback.onItemsClick((CourseCategoryTwoBean.Category) v.getTag());
        }
    }

    class CategoriesViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTv;

        CategoriesViewHolder(View itemView) {
            super(itemView);

            nameTv = (TextView) itemView.findViewById(R.id.name_tv);
        }
    }

    public interface onItemClickListner {
        void onItemsClick(CourseCategoryTwoBean.Category goodsCate);
    }

    public int getSelectedPosition() {
        return mCurrPosition;
    }

    public void setSelectedCateId(int cateId) {
        int position = 0;
        for (CourseCategoryTwoBean.Category bean : mList) {
            if(bean.getId() == cateId) {
                this.mCurrPosition = position;
            }
            ++position;
        }
        notifyDataSetChanged();
    }

}
