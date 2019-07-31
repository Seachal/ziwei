package com.laka.live.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.bean.City;

/**
 * Created by luwies on 16/3/25.
 */
public class RegionAdapter extends BaseAdapter<City, RegionAdapter.ViewHolder> {

    private Context mContext;

    private OnItemClickListener mOnItemClickListener;
    public RegionAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RegionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.select_region_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        final City city = getItem(position);

        holder.update(this, position, city);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(city);
            }
        });
    }

    public static class ViewHolder extends BaseAdapter.ViewHolder<City> {

        public TextView name;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.region_name);
        }

        @Override
        public void update(BaseAdapter adapter, int position, City city) {
            Context context = name.getContext();
            name.setText(city.getName());
            Drawable nextDrawable = ContextCompat.getDrawable(context, R.drawable.mine_icon_next02);
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(name, null, null, nextDrawable, null);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    private void onItemClick(City city) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(city);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(City city);
    }
}
