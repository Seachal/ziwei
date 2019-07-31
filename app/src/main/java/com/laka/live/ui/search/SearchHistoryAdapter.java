package com.laka.live.ui.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.ui.adapter.BaseAdapter;

import java.util.List;

/**
 * Created by luwies on 16/3/23.
 */
public class SearchHistoryAdapter extends BaseAdapter<String, BaseAdapter.ViewHolder> {

    private static final int TYPE_FOOTER = 0;

    private static final int TYPE_NORMAL = 1;

    private Context mContext;

    private SearchHisKeyManager mHisKeyManager;

    private OnItemClickListener mOnItemClickListener;

    public SearchHistoryAdapter(Context context) {
        super();
        mContext = context;
        mHisKeyManager = new SearchHisKeyManager();
    }

    public void addItem(String key) {
        mHisKeyManager.saveKey(key);
        notifyDataSetChanged();
    }

    public int getHistotyDataSize() {
        if (mHisKeyManager == null) {
            return 0;
        }
        return mHisKeyManager.size();
    }

    @Override
    public int getItemViewType(int position) {

        int lastIndex = getItemCount() - 1;
        if (position == lastIndex) {
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {

        int count = mHisKeyManager.getAllKeys().size();
        if (count > 0) {
            return count + 1;
        }
        return 0;
    }

    @Override
    public String getItem(int position) {
        List<String> keys = mHisKeyManager.getAllKeys();
        int count = keys.size();
        if (position < count) {
            return keys.get(position);
        }
        return "";
    }

    @Override
    public BaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_history_footer, parent, false);
            return new FooterHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_history_item, parent, false);
            return new ItemHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(BaseAdapter.ViewHolder holder, final int position) {
        int type = getItemViewType(position);
        if (type == TYPE_FOOTER) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHisKeyManager.clear();
                    notifyDataSetChanged();
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onClearItemClick();
                    }
                }
            });
        } else {
            final String key = getItem(position);
            ItemHolder itemHolder = (ItemHolder) holder;
            itemHolder.key.setText(key);
            itemHolder.clearIcon.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int lastIndex = mHisKeyManager.indexOf(key);
                            mHisKeyManager.remove(key);
                            notifyDataSetChanged();
                            if (mOnItemClickListener != null) {
                                mOnItemClickListener.onItemClearButtonClick(mHisKeyManager.size());
                            }
                        }
                    }
            );
            itemHolder.itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int lastIndex = mHisKeyManager.indexOf(key);
                            mHisKeyManager.saveKey(key);
                            notifyItemMoved(lastIndex, 0);
                            onItemClick(key);
                        }
                    }
            );
        }
    }

    public static class FooterHolder extends BaseAdapter.ViewHolder<Object> {

        public FooterHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void update(BaseAdapter adapter, int position, Object o) {
        }
    }

    public static class ItemHolder extends BaseAdapter.ViewHolder<String> {

        public TextView key;

        public ImageView clearIcon;

        public ItemHolder(View itemView) {
            super(itemView);
            key = (TextView) itemView.findViewById(R.id.key);
            clearIcon = (ImageView) itemView.findViewById(R.id.clear);
        }

        @Override
        public void update(BaseAdapter adapter, int position, String s) {

        }
    }

    public void save() {
        mHisKeyManager.saveToFile();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    private void onItemClick(String keyword) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(keyword);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String keyword);

        void onClearItemClick();

        void onItemClearButtonClick(int size);
    }
}
