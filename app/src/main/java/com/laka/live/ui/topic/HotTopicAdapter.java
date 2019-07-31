package com.laka.live.ui.topic;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.laka.live.R;
import com.laka.live.bean.Topic;
import com.laka.live.util.ResourceHelper;

import java.util.List;

public class HotTopicAdapter extends
        RecyclerView.Adapter<HotTopicAdapter.ViewHolder> implements OnClickListener {
    private static final String TAG = "HotTopicAdapter";
    Context mContext;
    private List<String> selected;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private LayoutInflater mInflater;
    private List<String> mDatas;

    public HotTopicAdapter(Context context, List<String> datats,List<String> selected) {
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
        this.selected = selected;
        this.mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
            tvName = (TextView) v.findViewById(R.id.tv_name);
        }

        TextView tvName;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View convertView = mInflater.inflate(R.layout.item_hot_topic,
                viewGroup, false);
        ViewHolder holder = new ViewHolder(convertView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {
        String item = mDatas.get(i);
        holder.tvName.setText(getFormatName(holder.itemView.getContext(),item));

        boolean isSelected = false;
        for (String str : selected) {
            if(str.equals(item)){
                isSelected = true;
            }
        }

        if(isSelected) {
            holder.tvName.setTextColor(ResourceHelper.getColor(R.color.colorFF950B));
        }else{
            holder.tvName.setTextColor(ResourceHelper.getColor(R.color.color333333));
        }

        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.itemView, i);
                }
            });
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }

    public static String getFormatName(Context context,String name) {
        if (context != null) {
            return context.getString(R.string.topic_format, name);
        }
        return "";
    }
}
