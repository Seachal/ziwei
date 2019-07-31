package com.laka.live.ui.search;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.ui.adapter.BaseAdapter;

/**
 * Created by iOS on 16/3/26.
 */
public class SearchHotKeywordAdapter extends BaseAdapter<String, SearchHotKeywordAdapter.ViewHolder> {

    private onItemClickListener mOnItemClickListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_hot_keyword, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String keyword = getItem(position);
        TextView textView = (TextView) holder.itemView;
        textView.setText(keyword);
        if (TextUtils.isEmpty(keyword) == false) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(keyword);
                }
            });
        } else {
            textView.setClickable(false);
        }
    }

    public static class ViewHolder extends BaseAdapter.ViewHolder<String> {
        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void update(BaseAdapter adapter, int position, String s) {

        }
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    private void onItemClick(String hotKeyword) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClickListener(hotKeyword);
        }
    }

    public interface onItemClickListener {
        void onItemClickListener(String hotKeyWord);
    }
}
