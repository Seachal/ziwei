package com.laka.live.shopping.search.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.shopping.search.bean.KeywordSearchTopList;
import com.laka.live.shopping.search.info.SearchResultEmptyInfo;
import com.laka.live.shopping.search.widget.SearchHotGoodsView;
import com.laka.live.util.ResourceHelper;

import java.util.List;

/**
 * Created by gangqing on 2016/4/22.
 * Email:denggangqing@ta2she.com
 */
public class SearchResultEmptyAdapter extends BaseAdapter {
    private Context mContext;
    private List<SearchResultEmptyInfo> mList;
    private static final int TYPE_COUNT = 2;
    private static final String NOT_FIND_TITLE_REPLACE = "#goods#";
    private SearchResultEmptyAdapterCallBack mCallBack;

    public SearchResultEmptyAdapter(Context context, List<SearchResultEmptyInfo> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).type;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            if (getItemViewType(position) == SearchResultEmptyInfo.view_type_hot_search) {
                convertView = View.inflate(mContext, R.layout.search_result_empty_hot_search_layout, null);
            } else if (getItemViewType(position) == SearchResultEmptyInfo.view_type_random_article) {
                convertView = View.inflate(mContext, R.layout.search_result_empty_random_article, null);
            }
        }
        if (getItemViewType(position) == SearchResultEmptyInfo.view_type_hot_search) {
            setHotSearchData(convertView, position);
        } else if (getItemViewType(position) == SearchResultEmptyInfo.view_type_random_article) {
            setRandomArticleData(convertView, position);
        }
        return convertView;
    }

    private void setHotSearchData(View view, int position) {
        TextView title = (TextView) view.findViewById(R.id.not_find_title);
        SearchHotGoodsView hotGoodsView = (SearchHotGoodsView) view.findViewById(R.id.search_hot_goods);
        String s = ResourceHelper.getString(R.string.classify_empty_hint);
        s = s.replace(NOT_FIND_TITLE_REPLACE, mList.get(position).title);
        title.setText(s);
        hotGoodsView.setData(mList.get(position).hotSearch);
        hotGoodsView.setTitle(ResourceHelper.getString(R.string.classify_empty_hot_title));
        hotGoodsView.setHotGoodsItemClickListener(new SearchHotGoodsView.HotGoodsItemClickListener() {
            @Override
            public void onItemClick(View v) {
                if (mCallBack != null) {
                    mCallBack.onHotGoodsItemClick(v);
                }
            }
        });
    }

    private void setRandomArticleData(View view, int position) {
        SimpleDraweeView ico = (SimpleDraweeView) view.findViewById(R.id.search_random_article);
        KeywordSearchTopList data = mList.get(position).randomTopList;
        if (data == null) {
            return;
        }
        if (data.imageUrl != null) {
            ico.setImageURI(Uri.parse(data.imageUrl));
        }
    }

    public void setSearchResultEmptyAdapterCallBack(SearchResultEmptyAdapterCallBack callBack) {
        this.mCallBack = callBack;
    }

    public interface SearchResultEmptyAdapterCallBack {
        void onHotGoodsItemClick(View v);
    }
}
