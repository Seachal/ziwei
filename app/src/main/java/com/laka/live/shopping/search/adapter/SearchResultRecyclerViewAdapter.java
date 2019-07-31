package com.laka.live.shopping.search.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.R;
import com.laka.live.shopping.framework.MsgDispatcher;
import com.laka.live.shopping.framework.adapter.MsgDef;
import com.laka.live.shopping.search.bean.KeywordSearchResult;
import com.laka.live.shopping.search.bean.KeywordSearchTopList;
import com.laka.live.shopping.search.info.SearchResultInfo;
import com.laka.live.shopping.widget.MaterialRippleLayout;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.RippleEffectHelper;
import com.laka.live.util.StringUtils;

import java.util.List;

/**
 * Created by gangqing on 2016/4/28.
 * Email:denggangqing@ta2she.com
 */
public class SearchResultRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String REPLACE_PRICE = "#price#";
    private static final String REPLACE_EVALUATE = "#evaluate#";
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<SearchResultInfo> mData;

    public SearchResultRecyclerViewAdapter(Context context, List<SearchResultInfo> data) {
        mContext = context;
        mData = data;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getViewType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == SearchResultInfo.TYPE_MATCH_SECONDARY_CLASSIFY) {
            View view = mLayoutInflater.inflate(R.layout.search_result_top_list_layout, parent, false);
            MaterialRippleLayout rippleLayout = RippleEffectHelper.addRippleEffectInView(view);
            viewHolder = new TopViewHolder(rippleLayout);
        } else if (viewType == SearchResultInfo.TYPE_DEFAULT) {
            View view = mLayoutInflater.inflate(R.layout.item_shopping_good, parent, false);
            MaterialRippleLayout rippleLayout = RippleEffectHelper.addRippleEffectInView(view);
            viewHolder = new DefaultViewHolder(rippleLayout);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == SearchResultInfo.TYPE_MATCH_SECONDARY_CLASSIFY) {
            KeywordSearchTopList toplist = mData.get(position).getToplist();
            TopViewHolder viewHolder = (TopViewHolder) holder;
            viewHolder.image.setImageURI(Uri.parse(toplist.imageUrl));
        } else if (getItemViewType(position) == SearchResultInfo.TYPE_DEFAULT) {
            KeywordSearchResult searchResultItem = mData.get(position).getSearchResultItem();
            int type = mData.get(position).getSecondaryClassifyType();
            DefaultViewHolder viewHolder = (DefaultViewHolder) holder;
            viewHolder.image.setImageURI(Uri.parse(searchResultItem.thumbUrl));
            viewHolder.name.setText(searchResultItem.title);
            String price = ResourceHelper.getString(R.string.search_goods_price);
            price = price.replace(REPLACE_PRICE, searchResultItem.salePrice);
            viewHolder.price.setText(price);
//            if (type == SearchConstant.TYPE_EVALUATE) {  //测评
//                String evaluate = ResourceHelper.getString(R.string.search_goods_evaluate);
//                evaluate = evaluate.replace(REPLACE_EVALUATE, String.valueOf(searchResultItem.evaluCount));
//                viewHolder.attribute.setText(evaluate);
//            } else {    //销量
//                viewHolder.attribute.setText("销量：" + searchResultItem.saleCount);
//            }
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    private void onItemClickListener(int position) {
//        StatsModel.stats(StatsKeyDef.SEARCH_LIST_CLICK);
        if (getItemViewType(position) == SearchResultInfo.TYPE_MATCH_SECONDARY_CLASSIFY) {
            Message message = Message.obtain();
            message.what = MsgDef.MSG_SHOW_SHOPPING_TOP_LIST_DETAIL_WINDOW;
            message.arg1 = StringUtils.parseInt(mData.get(position).getToplist().toplistId);
            MsgDispatcher.getInstance().sendMessage(message);
        } else if (getItemViewType(position) == SearchResultInfo.TYPE_DEFAULT) {
            String goodsId = mData.get(position).getSearchResultItem().goodsId;
            Message msg = Message.obtain();
            msg.what = MsgDef.MSG_SHOW_GOODS_DETAIL_WINDOW;
            msg.obj = goodsId;
            MsgDispatcher.getInstance().sendMessage(msg);
        }
    }

    public class DefaultViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView image;
        TextView name, price, attribute;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            image = (SimpleDraweeView) itemView.findViewById(R.id.goods_image);
            name = (TextView) itemView.findViewById(R.id.goods_name);
            price = (TextView) itemView.findViewById(R.id.goods_price);
            attribute = (TextView) itemView.findViewById(R.id.goods_attribute);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition() - 1;
                    if (position < 0) {
                        return;
                    }
                    onItemClickListener(position);
                }
            });
        }
    }

    public class TopViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView image;

        public TopViewHolder(View itemView) {
            super(itemView);
            image = (SimpleDraweeView) itemView.findViewById(R.id.goods_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition() - 1;
                    if (position < 0) {
                        return;
                    }
                    onItemClickListener(position);
                }
            });
        }
    }
}
