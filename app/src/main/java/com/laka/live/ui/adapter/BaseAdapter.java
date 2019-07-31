package com.laka.live.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luwies on 16/3/22.
 */
public abstract class BaseAdapter<T, VH extends BaseAdapter.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected List<T> mDatas;

    private OnItemClickListener mOnItemClickListener;

    private View.OnClickListener mOnClickListener;

    public BaseAdapter() {
        super();
        mDatas = new ArrayList<>();
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        T item = getItem(position);
        holder.update(this, position, item);
        if (holder.itemView != null) {

            if (mOnClickListener == null) {
                mOnClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int p = (int) v.getTag();
                        onItemClick(p);
                    }
                };
            }
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(mOnClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public int getPosition(T t) {
        return mDatas.indexOf(t);
    }

    public void addAll(List data) {
        if (data != null && data.isEmpty() == false) {
            mDatas.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void remove(int position) {
        if (position < 0 || position >= getItemCount()) {
            return;
        }
        mDatas.remove(position);
//        notifyItemRemoved(position);
    }

    public void clear() {
        clear(true);
    }

    public void clear(boolean isNotifyDataSetChanged) {
        mDatas.clear();
        if (isNotifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    public boolean isEmpty() {
        return mDatas.isEmpty();
    }

    public T getItem(int position) {
        if (position < 0 || position >= mDatas.size()) {
            return null;
        }
        return mDatas.get(position);
    }

    public List<T> getAll() {
        return mDatas;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    protected void onItemClick(int position) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(position);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static abstract class ViewHolder<T> extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void update(BaseAdapter adapter, int position, T t);

    }

    public List<T> getmDatas() {
        return mDatas;
    }

    public void setmDatas(List<T> mDatas) {
        this.mDatas = mDatas;
    }


}
